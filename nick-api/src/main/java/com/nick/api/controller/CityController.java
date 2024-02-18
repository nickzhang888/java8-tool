package com.nick.api.controller;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nick.api.domain.City;
import com.nick.api.service.CityService;
import com.nick.common.core.domain.AjaxResult;
import com.nick.common.utils.http.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CityController {

    @Autowired
    private CityService cityService;
    //高雄,台式后面多加了个字市,查询为null,去掉市就可以了
    @GetMapping("updateCityByName")
    public AjaxResult updateCityByName(City city) {
        AjaxResult ajax = AjaxResult.success();
        List<City> cityList = cityService.findCity(city);
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
    public void getLatLng(String cityName){
        String url = "http://api.map.baidu.com/geocoder/v2/?output=json&ak=0it4Pc49C9eNBCQ6P86ZBw29APWcLXNj&address=" + cityName;
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
                City city = new City(cityName,lngDouble,latDouble);
                cityService.updateCity(city);
            }
        }
    }



    @GetMapping("/getSubwayCity")
    public AjaxResult subway(City city) {
        AjaxResult ajax = AjaxResult.success();
        List<City> cityList = cityService.findCity(city);
        if (!cityList.isEmpty()) {
            ajax.put("data", cityList);
        } else {
            String apiUrl = "https://map.baidu.com/?qt=subwayscity";
            // 发送HTTP请求获取接口数据，这里使用RestTemplate示例
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            List<Object> list = null;
            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                ArrayList<City> arrayList = new ArrayList<>();
                try {
                    // 将JSON字符串转换为Map
                    Map<String, Object> map = objectMapper.readValue(responseBody, Map.class);
                    Map<String, Object> cities = (Map<String, Object>) map.get("subways_city");
                    list = (List<Object>) cities.get("cities");

                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> obj = (Map<String, Object>) list.get(i);
                        Map<String, Object> keyValueMap = new HashMap<>();
                        keyValueMap.put("name", obj.get("cn_name"));
                        keyValueMap.put("code", obj.get("code"));
                        Integer code = (Integer) keyValueMap.get("code");
                        String name = (String) keyValueMap.get("name");
                        // 将键值对添加到 ArrayList
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
        }
        return ajax;
    }
}
