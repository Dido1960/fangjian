package com.ejiaoyi.common.enums;

import com.ejiaoyi.common.dto.CurrentScheduleDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件递交流程
 * @auther: liuguoqiang
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PaperFileSchedule {
    /**
     *0: 存证文件解析
     */
    CZR_PARSING(0,"存证文件解析"),

    /**
     * 1： 文件密封性验证
     */
    FILE_SEALING(1,"文件密封性验证"),

    /**
     * 2： 认证数据封装
     */
    DATA_ENCAPSULATION(2,"认证数据封装"),

    /**
     *3： 认证数据上传区块链
     */
    DATA_UPLOAD(3,"认证数据上传区块链"),

    /**
     * 4： 回执单生成
     */
    RECEIPT_SYNTHESIS(4,"回执单生成");


    /**
     * 按照流程顺畅 为列表 index 数据
     */
    private Integer index;
    private String textName;

    PaperFileSchedule(){}

    PaperFileSchedule(Integer index, String textName) {
        this.index = index;
        this.textName = textName;
    }

    public Integer getIndex() {
        return this.index;
    }
    public String  getTextName() {
        return this.textName;
    }

    public static List<CurrentScheduleDTO> listPaperCurrentSchedule(){
        List<CurrentScheduleDTO> result = new ArrayList<>();

        result.add(CurrentScheduleDTO.builder().fileSchedule(PaperFileSchedule.CZR_PARSING).status(Status.NOT_START.getCode()).build());
        result.add(CurrentScheduleDTO.builder().fileSchedule(PaperFileSchedule.FILE_SEALING).status(Status.NOT_START.getCode()).build());
        result.add(CurrentScheduleDTO.builder().fileSchedule(PaperFileSchedule.DATA_ENCAPSULATION).status(Status.NOT_START.getCode()).build());
        result.add(CurrentScheduleDTO.builder().fileSchedule(PaperFileSchedule.DATA_UPLOAD).status(Status.NOT_START.getCode()).build());
        result.add(CurrentScheduleDTO.builder().fileSchedule(PaperFileSchedule.RECEIPT_SYNTHESIS).status(Status.NOT_START.getCode()).build());

        return result;
    }

}
