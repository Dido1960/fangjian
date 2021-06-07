package com.ejiaoyi.common.enums;

/**
 * 评审类型
 * @Auther: liuguoqiang
 * @Date: 2020-11-17 15:57
 */
public enum ItemScoreType {
    /**
     * 酌情打分
     */
    RANGE(1,"range","酌情打分"),

    /**
     * 固定分值
     */
    FIXED(2,"fixed","固定分值");

    /**
     * 编码
     */
    private final int code;

    /**
     * 文件存储状态
     */
    private final String name;

    /**
     * 描述
     */
    private final String remark;

    ItemScoreType(Integer code, String name, String remark) {
        this.code = code;
        this.name = name;
        this.remark = remark;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public String getRemark() {
        return this.remark;
    }
    /**
     * 根据扩展名获取type
     *
     * @param code
     */
    public static ItemScoreType getCode(int code){
        for(ItemScoreType itemScoreType: ItemScoreType.values()) {
            if(itemScoreType.code == code) {
                return itemScoreType;
            }
        }
        return null;
    }

}
