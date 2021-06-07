package com.ejiaoyi.common.dto;

import com.ejiaoyi.common.entity.CalcScoreParam;
import lombok.*;

/**
 * 评审方法解析数据返回类
 * @author : liuguoqiang
 * @date : 2020-12-1 13:20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ParseEvalMethodDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 所有GradeID
     */
    private String gradeIds;

    /**
     * 报价得分计算参数
     */
    private CalcScoreParam calcScoreParam;
}
