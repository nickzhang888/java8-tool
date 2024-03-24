package com.nick.api.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.nick.api.domain.TreeNode;
import com.nick.api.service.TreeNodeService;
import com.nick.common.core.controller.BaseController;
import com.nick.common.core.domain.AjaxResult;
import com.nick.common.core.page.TableDataInfo;
import com.nick.common.utils.http.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TreeNodeController extends BaseController {
    // 定义树节点相关的操作
    @GetMapping("/getAllCity")
    public AjaxResult getAllCity() {
        //参考文档:https://lbsyun.baidu.com/faq/api?title=webapi/district-search/base
        String url = "http://api.map.baidu.com/api_region_search/v1/?keyword=%E4%B8%AD%E5%9B%BD&sub_admin=2&extensions_code=1&ak=MTdrAebsGT07f9oUQL1cXwTOCFmWyttg";
        String res = HttpUtils.sendGet(url);
        JSONObject obj = JSONObject.parseObject(res);
        JSONArray result = null;
        if (obj.containsKey("districts")) {
            result = obj.getJSONArray("districts");
            System.out.println(result);
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("data", result);
        return ajax;

    }
    @Autowired
    private TreeNodeService treeNodeService;
    @GetMapping("/getCityByCode")
    public TableDataInfo getCityByCode(Long code) {
        startPage();
        List<TreeNode> list = treeNodeService.getCityByCode(code);
        return getDataTable(list);
    }
}