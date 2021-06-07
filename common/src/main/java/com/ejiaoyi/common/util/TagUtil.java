package com.ejiaoyi.common.util;

import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;

import java.util.Map;

/**
 * 自定义标签工具
 *
 * @author Z0001
 * @since 2020-03-18
 */
public class TagUtil {

    /**
     * 获取参数值
     *
     * @param name   参数名
     * @param params 自定义标签的参数
     * @return 参数值
     * @throws TemplateModelException 模板异常
     */
    public static String getTemplateModel(String name, Map<Object, Object> params) throws TemplateModelException {
        TagUtil.validParam(name, params);
        return TagUtil.getString(name, params);
    }

    /**
     * 校验参数是否存在
     *
     * @param name   参数名称
     * @param params 自定义标签的参数
     * @throws TemplateModelException 模板异常
     */
    private static void validParam(String name, Map<Object, Object> params) throws TemplateModelException {
        Object model = params.get(name);
        if (model == null) {
            throw new TemplateModelException("can not find param " + name);
        }
    }

    /**
     * 获取参数值
     *
     * @param name   参数名
     * @param params 自定义标签的参数
     * @return 参数值
     * @throws TemplateModelException 模板异常
     */
    private static String getString(String name, Map<Object, Object> params) throws TemplateModelException {
        Object model = params.get(name);
        if (model == null) {
            return null;
        }

        if (model instanceof TemplateScalarModel) {
            return ((TemplateScalarModel) model).getAsString();
        } else if ((model instanceof TemplateNumberModel)) {
            return ((TemplateNumberModel) model).getAsNumber().toString();
        } else {
            throw new TemplateModelException("the param " + name + " must as string");
        }
    }
}
