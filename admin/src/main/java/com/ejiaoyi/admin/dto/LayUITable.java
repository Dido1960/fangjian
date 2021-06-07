package com.ejiaoyi.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * layui 表格数据
 *
 * @author chenkexu
 * @date 2020/07/13
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class LayUITable<T> {

    private Integer code;
    private String msg;
    private Integer count;
    private List<T> data;
}
