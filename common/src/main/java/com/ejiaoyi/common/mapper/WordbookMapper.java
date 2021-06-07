package com.ejiaoyi.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ejiaoyi.common.entity.Wordbook;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author fengjunhong
 * @since 2020/4/30
 */
@Component
public interface WordbookMapper extends BaseMapper<Wordbook> {

    /**
     * 清空数据表
     */
    void truncateTable();
}
