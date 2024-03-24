package com.nick.api.mapper;

import com.nick.api.domain.TreeNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface TreeNodeMapper {
    public List<TreeNode> getCityByCode(Long code);

    public int addTreeNode(TreeNode treeNode);


}
