package com.nick.api.service;

import com.nick.api.domain.TreeNode;
import com.nick.api.mapper.TreeNodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TreeNodeService {
    @Autowired
    private TreeNodeMapper treeNodeMapper;

    public List<TreeNode> getCityByCode(Long code) {
        return treeNodeMapper.getCityByCode(code);
    }
    public int addTreeNode(TreeNode treeNode) {
        return treeNodeMapper.addTreeNode(treeNode);
    }
}
