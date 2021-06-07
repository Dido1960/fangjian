package com.ejiaoyi.common.dto;

import com.ejiaoyi.common.entity.BidderFileInfo;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 文件上传 前端显示 dto
 * @Auther: liuguoqiang
 * @Date: 2021-1-5 16:37
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BidderFileUploadDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 投标人文件上传情况 列表
     */
    private List<BidderFileInfo> bidderFileInfoList;

    /**
     * 投标人 总人数
     */
    private Integer bidderNum;

    /**
     * 上传成功 总数
     */
    private Integer uploadSuccessNum;
}
