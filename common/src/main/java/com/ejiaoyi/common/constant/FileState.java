package com.ejiaoyi.common.constant;

/**
 * @Desc:
 * @author: yyb
 * @date: 2020-8-11 13:41
 */
public interface FileState {
    /**
     * 未开始
     */
    Integer UNSTART = 0;
    /**
     * 成功（解密，生成，删除...）
     */
    Integer SUCCESS = 1;

    /**
     * 失败（解密，生成，删除...）
     */
    Integer FAIL = 2;

    /**
     * 进行中（解密，生成，删除...）
     */
    Integer ING = 3;

}
