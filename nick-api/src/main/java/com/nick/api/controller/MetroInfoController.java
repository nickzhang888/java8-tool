package com.nick.api.controller;

import com.nick.api.domain.Driver;
import com.nick.api.domain.MetroInfo;
import com.nick.api.service.DriverService;
import com.nick.api.service.MetroInfoService;
import com.nick.common.core.controller.BaseController;
import com.nick.common.core.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@Slf4j
public class MetroInfoController extends BaseController {
    @Autowired
    private MetroInfoService MetroInfoService;
    @Autowired
    private DriverService driverService;

    @GetMapping("/getMetroInfo")
//    metroId是参数名,最终会传到xml中
    public AjaxResult getMetroInfo(Integer metroId) {
        List<MetroInfo> list = MetroInfoService.getMetroInfo(metroId);
        AjaxResult ajaxResult = AjaxResult.success();
        ajaxResult.put("data", list);
        return ajaxResult;
    }

    @GetMapping("/getMetroAndDriver")
    public AjaxResult getMetroAndDriver(Long metroId) {
        ArrayList<Object> tree = new ArrayList<>();
        List<MetroInfo> metrolist = MetroInfoService.getMetroInfo(null);
        List<Driver> driverList = driverService.findUser(null);
        for (MetroInfo metro : metrolist) {
            tree.add(findChildren(metro, driverList));
        }
        AjaxResult ajaxResult = AjaxResult.success();
        ajaxResult.put("data", tree);
        return ajaxResult;
    }

    public MetroInfo findChildren(MetroInfo metro, List<Driver> dirverList) {
        for (Driver driver : dirverList) {
            if (Objects.equals(metro.getMetroId(), driver.getMetroId())) {
                if (metro.getChildren() == null) {
                    metro.setChildren(new ArrayList<>());
                }
                metro.getChildren().add(driver);
            }
        }
        return metro;
    }
}
