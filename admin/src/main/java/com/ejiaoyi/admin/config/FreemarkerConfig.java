package com.ejiaoyi.admin.config;

import com.ejiaoyi.common.GlobalConfig;
import com.ejiaoyi.common.freemarker.CustomFreemarkerView;
import freemarker.template.TemplateModelException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 * Freemarker 配置类
 *
 * @author Z0001
 * @since 2020-03-17
 */
@Configuration
public class FreemarkerConfig {

    @Bean
    public ViewResolver viewResolver(FreeMarkerViewResolver resolver) {
        resolver.setViewClass(CustomFreemarkerView.class);
        return resolver;
    }

    @Bean
    public freemarker.template.Configuration configuration(freemarker.template.Configuration configuration) throws TemplateModelException {
        return GlobalConfig.configureFreemarkerLayoutDirectives(configuration);
    }
}
