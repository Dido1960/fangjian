package com.ejiaoyi.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 消息记录实体类
 * @Auther: liuguoqiang
 * @Date: 2021-1-20 21:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MsgHistoryDTO {

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息ID
     */
    private String id;

    /**
     * 发消息的人
     */
    private String name;

    /**
     * 消息时间
     */
    private String time;

    /**
     * 消息类型
     */
    private String type;
}
