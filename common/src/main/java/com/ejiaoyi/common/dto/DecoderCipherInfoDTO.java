package com.ejiaoyi.common.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 拆信封信息存储
 *
 * @author Make
 * @since 2021/01/07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DecoderCipherInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 读取文件头信息
     */
    private String fileHeader;

    /**
     * 读取版本号信息
     */
    private String version;

    /**
     * 读取文件类信息
     */
    private String fileType;

    /**
     * 读取解密因子信息
     */
    private String cipher;

    /**
     * 读取MD5值信息
     */
    private String md5;

    /**
     * 读取锁号信息
     */
    private String certId;
}
