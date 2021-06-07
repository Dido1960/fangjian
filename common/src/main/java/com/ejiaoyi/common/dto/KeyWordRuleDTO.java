package com.ejiaoyi.common.dto;

import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 关键字匹配规则DTO
 * </p>
 * @author Make
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class KeyWordRuleDTO implements Serializable ,Cloneable  {
    private static final long serialVersionUID = 1L;

    /**
     * 匹配第一个出现的只需要
     * 如果值为1的话，为0全部匹配
     */
    public String index;

    /**
     * 关键字
     */
    private  String keyWord;

    /**
     * 绝对定位x值
     */
    private String x;


    /**
     * 绝对定位y值
     */
    private String y;


    /**
     * 定位图片宽度匹配
     */
    private String width;

    /**
     * 定位图片高度匹配
     */
    private String height;

    /**
     * 签名图片base64值
     */
    private String imageBase64;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
