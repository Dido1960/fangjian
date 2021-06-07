package com.ejiaoyi.common.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 评分标准DTO，用于数据封装
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GradeDTO implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * 评分标准id
     */
    public Integer gradeId;

    /**
     * 评分标准名称
     */
    public String gradeName;

    /**
     * 评审形式(0:打分;1:合格制)
     */
    public Integer type;

    /**
     * 评分标准总分（合格制可以没有）
     */
    public String score;

    /**
     * 评分项DTO集合
     */
    public List<GradeItemDTO> gradeItemDTOs;



}
