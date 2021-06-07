package com.ejiaoyi.common.enums;

/**
 * 投标文件重新上传的类型
 * @Auther: liuguoqiang
 * @Date: 2021-1-6 17:19
 */
public enum FileReUplodType {
    /**
     * 商务文件
     */
    BUSINESS(1),
    /**
     * 技术文件
     */
    TECHNOLOGY(2),
    /**
     * 资格证明
     */
    QUALIFICATIONS(3),
    /**
     * 工程量清单pdf
     */
    CHECKLIST_PDF(4),
    /**
     * 工程量清单xml
     */
    CHECKLIST_XML(5),

    /**
     * 其他文件
     */
    OTHER(6);

    private final Integer code;

    public Integer getCode() {
        return this.code;
    }

    FileReUplodType(Integer code) {
        this.code = code;
    }

    public static FileReUplodType getEnum(Integer code) {
        for (FileReUplodType type : FileReUplodType.values()) {
            if(type.getCode().equals(code)) {
                return type;
            }
        }

        return null;
    }
}
