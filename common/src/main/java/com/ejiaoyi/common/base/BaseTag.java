package com.ejiaoyi.common.base;

import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.*;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * 自定义标签基础类
 *
 * @author Z0001
 * @since 2020-03-17
 */
@Data
public abstract class BaseTag implements TemplateDirectiveModel {

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 自定义标签名称
     */
    private String tagName;

    protected BaseTag(String tagName) {
        this.tagName = tagName;
    }

    /**
     * 根据模板输出内容
     *
     * @param templateName 模板名称
     * @param map          键值对
     * @param env          freemarker上下文
     * @return 输出内容
     */
    protected String process(String templateName, Map<Object, Object> map, Environment env) {
        Template template = this.getTemplate(templateName, env);
        StringWriter stringWriter = new StringWriter();
        try {
            template.process(map, stringWriter);
        } catch (TemplateException e) {
            logger.error("template exception creating processing environment", e);
        } catch (IOException e) {
            logger.error("io exception creating processing environment", e);
        }
        return stringWriter.toString();
    }

    /**
     * 获取模板
     *
     * @param name        模板名称
     * @param environment freemarker环境设置
     * @return 对应的模板
     */
    private Template getTemplate(String name, Environment environment) {
        Template template = null;
        try {
            Configuration configuration= environment.getConfiguration();
            configuration.setClassForTemplateLoading(BaseTag.class,"/templates");
            template =configuration.getTemplate(name);
        } catch (IOException e) {
            logger.error("can not get template : " + name, e);
        }
        return template;
    }

    /**
     * 设置基础变量键值对
     *
     * @param params 基础变量接收Map
     */
    protected void initParams(Map<Object, Object> params) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String ctx = request.getContextPath();
            String base = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + ctx;

            if (ctx == null || "".equals(ctx)) {
                ctx = "/";
            }
            params.put("ctx", ctx);
            params.put("base", base);
            params.put("sid", request.getSession().getId());
        }
    }

    /**
     * 添加自定义标签
     *
     * @param configuration freemarker 配置
     */
    public void setSharedVariable(Configuration configuration) {
        configuration.setSharedVariable(getTagName(), this);
    }
}
