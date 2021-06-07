package com.ejiaoyi.bidder.config;

import com.ejiaoyi.common.tag.UploadOneTag;
import com.ejiaoyi.common.tag.UploadTag;
import com.ejiaoyi.common.tag.ViewWordBookTag;
import com.ejiaoyi.common.tag.WordBookTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 自定义标签配置
 *
 * @author Z0001
 * @since 2020-03-17
 */
@Configuration
public class TagConfig {

    @Autowired
    private freemarker.template.Configuration configuration;

    @Autowired
    private WordBookTag wordBookTag;

    @Autowired
    private UploadOneTag uploadOneTag;

    @Autowired
    private UploadTag uploadTag;

    @Autowired
    private ViewWordBookTag viewWordBookTag;

    @PostConstruct
    public void setSharedVariable() {
        wordBookTag.setSharedVariable(configuration);
        uploadOneTag.setSharedVariable(configuration);
        uploadTag.setSharedVariable(configuration);
        viewWordBookTag.setSharedVariable(configuration);
    }
}
