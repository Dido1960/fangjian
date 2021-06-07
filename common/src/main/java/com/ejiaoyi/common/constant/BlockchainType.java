package com.ejiaoyi.common.constant;

/**
 * @Desc: 区块链 推送数据类型
 * @author: yyb
 * @date: 2020-8-18 13:55
 */
public interface BlockchainType {
    /**
     * 文件存证
     */
    Integer BID_RELATE_CONTENT = 1;

    /**
     *
     * 开标记录表
     */
    Integer BID_OPENING_RECORD = 2;

    /**
     * 投标人解密记录和确认开标结果
     */
    Integer BIDDER_DECLASSIFIED_RECORDS = 3;

    /**
     * 授权委托书
     */
    Integer BIDDER_ATTORNEY = 4;

    /**
     * 评标报告记录
     */
    Integer BID_EVAL_REPORT = 5;

    /**
     * 消息记录
     */
    Integer BID_MSG_EXPORT = 6;
}
