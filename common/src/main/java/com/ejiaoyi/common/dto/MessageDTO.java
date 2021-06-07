package com.ejiaoyi.common.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 投标人DTO，用于数据封装
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MessageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 消息类型 0：系统消息 1：投标人消息 2：代理机构消息
     */
    private Integer roleType;

    /**
     * 内容信息
     */
    private String message;
}