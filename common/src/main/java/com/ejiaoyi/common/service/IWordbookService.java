package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.Wordbook;

import java.util.List;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author fengjunhong
 * @since 2020/4/30
 */
public interface IWordbookService {

    /**
     * 初始化WordBook字典
     */
    void initWordBook();

    /**
     * 根据TOP KEY值 获取数据集
     *
     * @param topKey TOP KEY
     * @return 数据集
     */
    public List<Wordbook> listWordbookByTopKey(String topKey);

    /**
     * 根据TOP KEY & KEY值 获取对应的值
     *
     * @param topKey TOP KEY
     * @param key    KEY
     * @return 数据值
     */
    public String getValue(String topKey, String key);

    /**
     * 根据TOP KEY & KEY值 获取Wordbook
     *
     * @param topKey TOP KEY
     * @param key    KEY
     * @return Wordbook
     */
    public Wordbook getWordBook(String topKey, String key);

    /**
     * 根据TOP KEY和VALUE获取KEY
     *
     * @param topKey TOP KEY
     * @param value  VALUE
     * @return KEY
     */
    public String getHashKey(String topKey, String value);

}
