package com.nick.api.controller;

import com.nick.api.domain.MetroInfo;
import com.nick.api.service.MetroInfoService;
import com.nick.common.core.controller.BaseController;
import com.nick.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MetroInfoController extends BaseController {
    @Autowired
    private MetroInfoService MetroInfoService;
    @GetMapping("/getMetroInfo")
//    metroId是参数名,最终会传到xml中
    public AjaxResult getMetroInfo(Long metroId) {
        List<MetroInfo> list = MetroInfoService.getMetroInfo(metroId);
        AjaxResult ajaxResult = AjaxResult.success();
        ajaxResult.put("data",list);
        return ajaxResult;
    }
}
