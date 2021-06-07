package com.ejiaoyi.common.dto.statistical;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * 标讯信息参数 DTO
 *
 * @author Mike
 * @since 2021-04-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BidNewsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标段名称
     */
    @ApiModelProperty(value = "标段名称", example = "交易通施工测试一标段", position = 1)
    private String bidSectionName;

    /**
     * 标段编号
     */
    @ApiModelProperty(value = "标段编号", example = "JYT-SG-TEST-00101", position = 2)
    private String bidSectionCode;

    /**
     * 标段类型
     */
    @ApiModelProperty(value = "标段类型", example = "施工", position = 3)
    private String bidType;

    /**
     * 开标时间
     */
    @ApiModelProperty(value = "开标时间", example = "2021-04-01 10:00:00", position = 4)
    private String bidOpenTime;

    /**
     * 开标状态
     */
    @ApiModelProperty(value = "开标状态(0或空：未开始  1：进行中   2：结束)", example = "1", position = 5)
    private String bidOpenStatus;

    /**
     * 复会状态
     */
    @ApiModelProperty(value = "复会状态(0或空：未开始  1：进行中   2：结束)", example = "1", position = 6)
    private String resumeStatus;

    /**
     * 直播房间号
     */
    @ApiModelProperty(value = "直播房间号", example = "FJSZA0820210207091014", position = 7)
    private String liveRoom;

    /**
     * 标段ID
     */
    @ApiModelProperty(value = "标段ID", example = "1", position = 8)
    private Integer bidSectionId;
}
