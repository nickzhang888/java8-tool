package com.nick.api.domain;

import lombok.Data;

@Data
public class TreeNode {
        private Long code;
        private String name;
        private Integer level;
        private Long parentCode;
        private Integer isLeaf;
        // 子节点列表
//        private TreeNode[] children;
}
