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
public enum FileSchedule {
    /**
     *0: 存证文件解析
     */
    CZR_PARSING(0,"存证文件解析"),

    /**
     * 1: 标段一致性验证
     */
    BID_CONSISTENT(1,"标段一致性验证"),

    /**
     * 2： 文件密封性验证
     */
    FILE_SEALING(2,"文件密封性验证"),

    /**
     * 3： 认证数据封装
     */
    DATA_ENCAPSULATION(3,"认证数据封装"),

    /**
     *4： 认证数据上传区块链
     */
    DATA_UPLOAD(4,"认证数据上传区块链"),

    /**
     * 5： 回执单生成
     */
    RECEIPT_SYNTHESIS(5,"回执单生成");


    /**
     * 按照流程顺畅 为列表 index 数据
     */
    private Integer index;
    private String textName;

    FileSchedule(){}

    FileSchedule(Integer index, String textName) {
        this.index = index;
        this.textName = textName;
    }

    public Integer getIndex() {
        return this.index;
    }
    public String  getTextName() {
        return this.textName;
    }

    public static List<CurrentScheduleDTO> listCurrentSchedule(){
        List<CurrentScheduleDTO> result = new ArrayList<>();

//        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.CZR_PARSING.getTextName()).status(Status.NOT_START.getCode()).build());
//        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.BID_CONSISTENT.getTextName()).status(Status.NOT_START.getCode()).build());
//        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.FILE_SEALING.getTextName()).status(Status.NOT_START.getCode()).build());
//        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.DATA_ENCAPSULATION.getTextName()).status(Status.NOT_START.getCode()).build());
//        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.DATA_UPLOAD.getTextName()).status(Status.NOT_START.getCode()).build());
//        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.RECEIPT_SYNTHESIS.getTextName()).status(Status.NOT_START.getCode()).build());

        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.CZR_PARSING).status(Status.NOT_START.getCode()).build());
        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.BID_CONSISTENT).status(Status.NOT_START.getCode()).build());
        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.FILE_SEALING).status(Status.NOT_START.getCode()).build());
        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.DATA_ENCAPSULATION).status(Status.NOT_START.getCode()).build());
        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.DATA_UPLOAD).status(Status.NOT_START.getCode()).build());
        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.RECEIPT_SYNTHESIS).status(Status.NOT_START.getCode()).build());

        return result;
    }

    public static List<CurrentScheduleDTO> listPaperCurrentSchedule(){
        List<CurrentScheduleDTO> result = new ArrayList<>();

//        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.CZR_PARSING.getTextName()).status(Status.NOT_START.getCode()).build());
//        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.BID_CONSISTENT.getTextName()).status(Status.NOT_START.getCode()).build());
//        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.FILE_SEALING.getTextName()).status(Status.NOT_START.getCode()).build());
//        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.DATA_ENCAPSULATION.getTextName()).status(Status.NOT_START.getCode()).build());
//        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.DATA_UPLOAD.getTextName()).status(Status.NOT_START.getCode()).build());
//        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.RECEIPT_SYNTHESIS.getTextName()).status(Status.NOT_START.getCode()).build());

        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.CZR_PARSING).status(Status.NOT_START.getCode()).build());
        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.FILE_SEALING).status(Status.NOT_START.getCode()).build());
        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.DATA_ENCAPSULATION).status(Status.NOT_START.getCode()).build());
        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.DATA_UPLOAD).status(Status.NOT_START.getCode()).build());
        result.add(CurrentScheduleDTO.builder().fileSchedule(FileSchedule.RECEIPT_SYNTHESIS).status(Status.NOT_START.getCode()).build());

        return result;
    }

}
