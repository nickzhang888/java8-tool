package com.nick.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@TableName("warn_rule")
public class WarnRule {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String ruleName;

    private String snapAddressId;

    private String relatedWarnId;

    private String warnType;

    private BigDecimal angleThreshold;

    private Integer duration;

    private String riskLevel;

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer deleted;

    /** 非表字段：条件列表 */
    @TableField(exist = false)
    private List<WarnRuleCondition> conditions;
}
