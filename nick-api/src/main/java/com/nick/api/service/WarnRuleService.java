package com.nick.api.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nick.api.domain.WarnRule;
import com.nick.api.domain.WarnRuleCondition;
import com.nick.api.mapper.WarnRuleConditionMapper;
import com.nick.api.mapper.WarnRuleMapper;
import com.nick.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class WarnRuleService {

    @Autowired
    private WarnRuleMapper warnRuleMapper;

    @Autowired
    private WarnRuleConditionMapper warnRuleConditionMapper;

    /** 列表（可按规则名模糊查） */
    public List<WarnRule> list(WarnRule query) {
        LambdaQueryWrapper<WarnRule> wrapper = new LambdaQueryWrapper<>();
        if (query != null && StringUtils.isNotEmpty(query.getRuleName())) {
            wrapper.like(WarnRule::getRuleName, query.getRuleName());
        }
        if (query != null && StringUtils.isNotEmpty(query.getWarnType())) {
            wrapper.eq(WarnRule::getWarnType, query.getWarnType());
        }
        if (query != null && StringUtils.isNotEmpty(query.getRiskLevel())) {
            wrapper.eq(WarnRule::getRiskLevel, query.getRiskLevel());
        }
        wrapper.orderByDesc(WarnRule::getId);
        return warnRuleMapper.selectList(wrapper);
    }

    /** 详情（含条件） */
    public WarnRule getById(Long id) {
        WarnRule rule = warnRuleMapper.selectById(id);
        if (rule == null) {
            return null;
        }
        rule.setConditions(listConditions(id));
        return rule;
    }

    /** 新增规则 + 条件 */
    @Transactional(rollbackFor = Exception.class)
    public Long add(WarnRule rule) {
        warnRuleMapper.insert(rule);
        saveConditions(rule.getId(), rule.getConditions());
        return rule.getId();
    }

    /** 修改规则 + 条件（先删旧条件再插新条件） */
    @Transactional(rollbackFor = Exception.class)
    public boolean update(WarnRule rule) {
        if (rule.getId() == null) {
            return false;
        }
        int rows = warnRuleMapper.updateById(rule);
        // 物理删旧条件，避免唯一/序号混乱；表有逻辑删除时也可用 remove
        warnRuleConditionMapper.delete(new LambdaQueryWrapper<WarnRuleCondition>()
                .eq(WarnRuleCondition::getRuleId, rule.getId()));
        saveConditions(rule.getId(), rule.getConditions());
        return rows > 0;
    }

    /** 删除规则及条件 */
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        warnRuleConditionMapper.delete(new LambdaQueryWrapper<WarnRuleCondition>()
                .eq(WarnRuleCondition::getRuleId, id));
        return warnRuleMapper.deleteById(id) > 0;
    }

    private List<WarnRuleCondition> listConditions(Long ruleId) {
        return warnRuleConditionMapper.selectList(new LambdaQueryWrapper<WarnRuleCondition>()
                .eq(WarnRuleCondition::getRuleId, ruleId)
                .orderByAsc(WarnRuleCondition::getIndex));
    }

    private void saveConditions(Long ruleId, List<WarnRuleCondition> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            return;
        }
        for (int i = 0; i < conditions.size(); i++) {
            WarnRuleCondition condition = conditions.get(i);
            condition.setId(null);
            condition.setRuleId(ruleId);
            if (condition.getIndex() == null) {
                condition.setIndex(i);
            }
            if (condition.getShipTags() == null) {
                condition.setShipTags(Collections.emptyList());
            }
            warnRuleConditionMapper.insert(condition);
        }
    }
}
