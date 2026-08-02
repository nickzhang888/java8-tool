package com.nick.api.controller;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nick.api.domain.City;
import com.nick.api.service.CityService;
import com.nick.common.core.domain.AjaxResult;
import com.nick.common.utils.StringUtils;
import com.nick.common.utils.http.HttpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(tags = "城市")
@RestController
@RequestMapping("/api")
public class CityController {

    @Autowired
    private CityService cityService;

    // 更新城市经纬度接口, 只在初始化调用一次
    @ApiOperation("批量更新城市经纬度")
    @GetMapping("updateCityByName")
    public AjaxResult updateCityByName(City city) {
        AjaxResult ajax = AjaxResult.success();
        // 有 name/code 条件则按条件查，否则更新全部
        List<City> cityList = (city != null
                && (StringUtils.isNotEmpty(city.getName()) || city.getCode() != null))
                        ? cityService.findCity(city)
                        : cityService.listAll();
        for (City item : cityList) {
            try {
                // 延迟触发时间为1秒
                Thread.sleep(1000);
                String cityName = item.getName();
                getLatLng(cityName);
                System.out.println(cityName);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return ajax;
    }

    // 获取城市经纬度接口
    public void getLatLng(String cityName) {
        String url = "http://api.map.baidu.com/geocoder/v2/?output=json&ak=0it4Pc49C9eNBCQ6P86ZBw29APWcLXNj&address="
                + cityName;
        String res = HttpUtils.sendGet(url);
        JSONObject obj = JSONObject.parseObject(res);
        if (obj.containsKey("result")) {
            JSONObject result = obj.getJSONObject("result");
            if (result.containsKey("location")) {
                JSONObject location = result.getJSONObject("location");
                String lng = location.getString("lng");
                String lat = location.getString("lat");
                double lngDouble = Double.parseDouble(lng);
                double latDouble = Double.parseDouble(lat);
                City city = new City(cityName, lngDouble, latDouble);
                cityService.updateCity(city);
            }
        }
    }

    @ApiOperation("查询/同步地铁城市")
    @GetMapping("/getSubwayCity")
    public AjaxResult subway(City city) {
        AjaxResult ajax = AjaxResult.success();
        boolean hasCondition = city != null
                && (StringUtils.isNotEmpty(city.getName()) || city.getCode() != null);

        // 条件查询时，只查库，查不到返回空
        if (hasCondition) {
            ajax.put("data", cityService.findCity(city));
            return ajax;
        }

        // 无查询条件：库里已有数据则直接返回
        List<City> cityList = cityService.listAll();
        if (!cityList.isEmpty()) {
            ajax.put("data", cityList);
            return ajax;
        }

        // 库为空时，才从百度同步全量城市
        String apiUrl = "https://map.baidu.com/?qt=subwayscity";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayList<City> arrayList = new ArrayList<>();
            try {
                Map<String, Object> map = objectMapper.readValue(responseBody, Map.class);
                Map<String, Object> cities = (Map<String, Object>) map.get("subways_city");
                List<Object> list = (List<Object>) cities.get("cities");

                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> obj = (Map<String, Object>) list.get(i);
                    Integer code = (Integer) obj.get("code");
                    String name = (String) obj.get("cn_name");
                    arrayList.add(new City(code, name, code));
                }
                for (City item : arrayList) {
                    cityService.addCity(item);
                }
                ajax.put("data", arrayList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ajax;
    }
}
