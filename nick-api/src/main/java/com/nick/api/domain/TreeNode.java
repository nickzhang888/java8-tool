package com.nick.api.domain;

import lombok.Data;

@Data
public class TreeNode {
    private String code;
    private String name;
    private Integer level;
    private String parentCode;


    public TreeNode(String code, String name, Integer level,String parentCode) {
        this.code = code;
        this.name = name;
        this.level = level;
        this.parentCode = parentCode;
    }
}
