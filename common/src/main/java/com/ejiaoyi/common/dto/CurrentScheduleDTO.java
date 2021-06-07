package com.ejiaoyi.common.dto;

import com.ejiaoyi.common.enums.FileSchedule;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 当前文件递交的流程 显示dto
 * @author lgq
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CurrentScheduleDTO implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * 进度
     */
    public Object fileSchedule;

    /**
     * 进行状态 0:进行 1:成功 2:失败
     */
    public Integer status;


}
