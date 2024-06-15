package com.nick.api.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.nick.api.domain.City;
import com.nick.api.domain.TreeNode;
import com.nick.api.service.TreeNodeService;
import com.nick.common.core.controller.BaseController;
import com.nick.common.core.domain.AjaxResult;
import com.nick.common.core.page.TableDataInfo;
import com.nick.common.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class TreeNodeController extends BaseController {
    @Autowired
    private TreeNodeService treeNodeService;

    // 定义树节点相关的操作
    @GetMapping("/getAllCity")
    public AjaxResult getAllCity() {
        AjaxResult ajax = AjaxResult.success();
        List<TreeNode> dataInfo = treeNodeService.getCityByCode(null);
        ArrayList<TreeNode> nodes;
        if (dataInfo.isEmpty()) {
            nodes = getTreeList();
            ajax.put("data", nodes);
        } else {
            ajax.put("data", dataInfo);
        }
        return ajax;
    }

    public ArrayList<TreeNode> getTreeList() {
        //参考文档:https://lbsyun.baidu.com/faq/api?title=webapi/district-search/base
        String url = "http://api.map.baidu.com/api_region_search/v1/?keyword=%E4%B8%AD%E5%9B%BD&sub_admin=2&extensions_code=1&ak=MTdrAebsGT07f9oUQL1cXwTOCFmWyttg";
        String res = HttpUtils.sendGet(url);
        JSONObject obj = JSONObject.parseObject(res);
        JSONArray arr;
        ArrayList<TreeNode> treeNodes = new ArrayList<>();
        if (obj.containsKey("districts")) {
            arr = obj.getJSONArray("districts");
            treeToList(arr, treeNodes, null);
        }
        // 添加到数据库
        for (TreeNode item : treeNodes) {
            treeNodeService.addTreeNode(item);
        }

        return treeNodes;
    }


    public void treeToList(JSONArray arr, ArrayList<TreeNode> treeNodes, String pCode) {
        for (int i = 0; i < arr.size(); i++) {
            JSONObject jsonObject = arr.getJSONObject(i);
            String code = jsonObject.getString("code");
            String name = jsonObject.getString("name");
            Integer level = Integer.valueOf(jsonObject.getString("level"));
            treeNodes.add(new TreeNode(code, name, level, pCode));
            if (jsonObject.containsKey("districts")) {
                treeToList(jsonObject.getJSONArray("districts"), treeNodes, jsonObject.getString("code"));
            }
        }
    }


    @GetMapping("/getCityByCode")
    public TableDataInfo getCityByCode(Long code) {
        startPage();
        List<TreeNode> list = treeNodeService.getCityByCode(code);
        return getDataTable(list);
    }
}