package com.ejiaoyi.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 基础 服务类接口
 *
 * @author Z0001
 * @since 2020-03-18
 */
public interface IBaseService<T> {

    /**
     * 获取分页参数 LayUI分页
     *
     * @return 分页参数
     */
    Page getPageForLayUI();

    /**
     * 封装 layUI table 返回值
     *
     * @param list  数据集合
     * @param count 总数据量
     * @return Map数据
     */
    String initJsonForLayUI(List<Object> list, Integer count);

    /**
     * 封装 layUI table 返回值
     *
     * @param list  数据集合
     * @param count 总数据量
     * @param code  状态码 0 为成功
     * @param msg   消息
     * @return Map数据
     */
    String initJsonForLayUI(List list, Integer count, Integer code, String msg);

}
