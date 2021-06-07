package com.ejiaoyi.common.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 投标人评审点dto
 * @author Make
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BidderReviewPointDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 企业Id
     */
    private Integer bidderId;

    /**
     * 评分标准id
     */
    private Integer gradeId;

    /**
     * 评审项id
     */
    private Integer gradeItemId;

    /**
     * 资格证明文件评审点对应pdf的页码
     */
    private List<String> quReviewPointPages;

    /**
     * 商务标文件评审点对应pdf的页码
     */
    private List<String> bsReviewPointPages;

    /**
     * 技术标文件评审点对应pdf的页码
     */
    private List<String> teReviewPointPages;

}
