package com.ejiaoyi.common.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.entity.Wordbook;
import com.ejiaoyi.common.mapper.WordbookMapper;
import com.ejiaoyi.common.service.IWordbookService;
import com.ejiaoyi.common.support.DataSourceKey;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author fengjunhong
 * @since 2020/4/30
 */
@Service
@DS(DataSourceKey.COMMON)
public class WordbookServiceImpl implements IWordbookService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WordbookMapper wordbookMapper;

    @Override
    public void initWordBook() {

        wordbookMapper.truncateTable();

        try {
            Resource resource = new ClassPathResource("/com/ejiaoyi/common/resource/wordbook.xml");
            InputStream is = resource.getInputStream();

            Document xml = new SAXReader().read(is);

            List<Element> wordbookElements = xml.selectNodes("/root/wordbook");

            for (Element wordbookElement : wordbookElements) {
                String key = wordbookElement.attributeValue("key");
                this.readDataNode(wordbookElement, key, key);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 根据TOP KEY值 获取数据集
     *
     * @param topKey TOP KEY
     * @return 数据集
     */
    @Override
    @Cacheable(value = CacheName.WORDBOOK_REDIS_CACHE, key = "#topKey",unless = "#result==null")
    public List<Wordbook> listWordbookByTopKey(String topKey) {
        QueryWrapper<Wordbook> wordbookQueryWrapper = new QueryWrapper<Wordbook>()
                .eq("BOOK_TOP_KEY", topKey);

        return wordbookMapper.selectList(wordbookQueryWrapper);
    }

    /**
     * 根据TOP KEY & KEY值 获取对应的值
     *
     * @param topKey TOP KEY
     * @param key    KEY
     * @return 数据值
     */
    @Override
    @Cacheable(value = CacheName.WORDBOOK_REDIS_CACHE, key = "#topKey+'_'+#key+'_str'", unless = "#result == null")
    public String getValue(String topKey, String key) {
        if (StringUtils.isEmpty(topKey) || StringUtils.isEmpty(key)) {
            return null;
        }

        QueryWrapper<Wordbook> wordbookQueryWrapper = new QueryWrapper<Wordbook>()
                .eq("BOOK_TOP_KEY", topKey)
                .eq("BOOK_KEY", key);
        Wordbook wordbook = wordbookMapper.selectOne(wordbookQueryWrapper);

        return wordbook == null ? "" : wordbook.getBookValue();
    }

    /**
     * 根据TOP KEY & KEY值 获取Wordbook
     *
     * @param topKey TOP KEY
     * @param key    KEY
     * @return Wordbook
     */
    @Override
    @Cacheable(value = CacheName.WORDBOOK_REDIS_CACHE, key = "#topKey+'_'+#key+'_obj'", unless = "#result==null")
    public Wordbook getWordBook(String topKey, String key) {
        QueryWrapper<Wordbook> wordbookQueryWrapper = new QueryWrapper<Wordbook>()
                .eq("BOOK_TOP_KEY", topKey)
                .eq("BOOK_KEY", key);

        return wordbookMapper.selectOne(wordbookQueryWrapper);
    }

    /**
     * 根据TOP KEY和VALUE获取KEY
     *
     * @param topKey TOP KEY
     * @param value  VALUE
     * @return KEY
     */
    @Override
    @Cacheable(value = CacheName.WORDBOOK_REDIS_CACHE, key = "#topKey+'_'+#value+'_key'",unless = "#result==null")
    public String getHashKey(String topKey, String value) {
        QueryWrapper<Wordbook> wordbookQueryWrapper = new QueryWrapper<Wordbook>()
                .eq("BOOK_TOP_KEY", topKey)
                .eq("BOOK_VALUE", value);

        Wordbook wordbook = wordbookMapper.selectOne(wordbookQueryWrapper);

        return wordbook == null ? "" : wordbook.getBookKey();
    }

    /**
     * 读取数据节点
     *
     * @param ele       节点
     * @param topKey    顶级KEY值
     * @param parentKey 父级KEY值
     */
    private void readDataNode(Element ele, String topKey, String parentKey) {
        Assert.notNull(ele, "param ele can not be null");
        Assert.notEmpty(parentKey, "param parentKey can not be empty");

        List<Element> dataElements = ele.selectNodes("./data");

        for (Element dataEle : dataElements) {
            String key = dataEle.attributeValue("hash_key");
            String value = dataEle.attributeValue("value");

            Wordbook wordBook = Wordbook.builder()
                    .bookTopKey(topKey)
                    .bookParentKey(parentKey)
                    .bookKey(key)
                    .bookValue(value)
                    .build();

            wordbookMapper.insert(wordBook);

            List<Element> childElements = dataEle.selectNodes("./data");

            if (CollectionUtil.isNotEmpty(childElements)) {
                this.readDataNode(dataEle, topKey, dataEle.attributeValue("hash_key"));
            }
        }
    }
}
