package com.ejiaoyi.expert.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 清标进度DTO
 * @author Make
 * @since 2020-12-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ExpertUserMsgDTO implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * 回退消息类型 1:环节重评 2：小组结束
     */
    public Integer msgStatus;

    /**
     * 需要发送的消息
     */
    public String msg;
}
