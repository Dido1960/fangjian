package com.ejiaoyi.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * WordBook字典
 *
 * @author Z0001
 * @since 2020-03-18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class WordBook {

    /**
     * key
     */
    private String key;

    /**
     * value
     */
    private String value;

    /**
     * 父 KEY
     */
    private String parentKey;
}
