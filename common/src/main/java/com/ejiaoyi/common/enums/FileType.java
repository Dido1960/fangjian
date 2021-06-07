package com.ejiaoyi.common.enums;

/**
 * @Desc:
 * @author: yyb
 * @date: 2020-8-6 16:01
 */
public enum FileType {
    /**
     *0:gef或tjy
     */
    GEFORTJY(0,"gef"),

    /**
     * 1:sgef或etjy
     */
    SGEFORETJY(1,"sgef"),

    /**
     * pdf
     */
    PDF(2,"pdf"),

    /**
     * ftl
     */
    FTL(3,"ftl"),

    /**
     * png
     */
    PNG(4,"png");

    private Integer type;
    private String suffix;

    FileType(Integer type, String suffix) {
        this.type = type;
        this.suffix = suffix;
    }

    public Integer getType() {
        return this.type;
    }
    public String  getSuffix() {
        return this.suffix;
    }

    /**
     * 根据扩展名获取type
     *
     * @param suffix
     */
    public static Integer getType(String suffix){
        for(FileType bidFileType: FileType.values()) {
            if(bidFileType.getSuffix().equals(suffix)) {
                return bidFileType.type;
            }
        }
        return null;
    }

}
