package com.nick.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@TableName(value = "warn_rule_condition", autoResultMap = true)
public class WarnRuleCondition {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long ruleId;

    /** 对应 JSON 的 index */
    @TableField("cond_index")
    private Integer index;

    private String name;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> shipTags;

    private String shipType;

    private BigDecimal shipLengthMin;

    private BigDecimal shipLengthMax;

    private BigDecimal shipWidthMin;

    private BigDecimal shipWidthMax;

    private String shipKeyword;

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer deleted;
}
