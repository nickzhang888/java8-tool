package com.nick.api.controller;

import com.nick.api.domain.WarnRule;
import com.nick.api.service.WarnRuleService;
import com.nick.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "告警规则")
@RestController
@RequestMapping("/api/warnRule")
public class WarnRuleController {

    @Autowired
    private WarnRuleService warnRuleService;

    @ApiOperation("规则列表")
    @GetMapping("/list")
    public AjaxResult list(WarnRule query) {
        return AjaxResult.success(warnRuleService.list(query));
    }

    @ApiOperation("规则详情")
    @GetMapping("/{id}")
    public AjaxResult get(@ApiParam(value = "规则ID", required = true) @PathVariable Long id) {
        WarnRule rule = warnRuleService.getById(id);
        if (rule == null) {
            return AjaxResult.error("规则不存在");
        }
        return AjaxResult.success(rule);
    }

    @ApiOperation("新增规则")
    @PostMapping
    public AjaxResult add(@RequestBody WarnRule rule) {
        Long id = warnRuleService.add(rule);
        return AjaxResult.success(id);
    }

    @ApiOperation("修改规则")
    @PutMapping
    public AjaxResult update(@RequestBody WarnRule rule) {
        if (rule.getId() == null) {
            return AjaxResult.error("id不能为空");
        }
        return warnRuleService.update(rule) ? AjaxResult.success() : AjaxResult.error("修改失败");
    }

    @ApiOperation("删除规则")
    @DeleteMapping("/{id}")
    public AjaxResult delete(@ApiParam(value = "规则ID", required = true) @PathVariable Long id) {
        return warnRuleService.delete(id) ? AjaxResult.success() : AjaxResult.error("删除失败");
    }
}
