package com.ejiaoyi.supervise.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 树形组件字段
 *
 * @author fengjunhong
 * @version 1.0
 * @date 2020/4/1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TreeNode {

    //父级名称
    private String title;

    //id
    private Integer id;

    //子节点列表
    private List<TreeNode> children;
}
