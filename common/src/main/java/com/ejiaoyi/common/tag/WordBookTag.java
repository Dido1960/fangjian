package com.ejiaoyi.common.tag;

import com.ejiaoyi.common.base.BaseTag;
import com.ejiaoyi.common.entity.Wordbook;
import com.ejiaoyi.common.service.impl.WordbookServiceImpl;
import com.ejiaoyi.common.util.TagUtil;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * 字典表下拉菜单
 *
 * @author Z0001
 * @since 2020-03-17
 */
@Component
public class WordBookTag extends BaseTag {

    private static final String TAG_NAME = "WordBookTag";

    public WordBookTag() {
        super(TAG_NAME);
    }

    /**
     * 模板文件
     */
    private static final String TEMPLATE_NAME = "/tag/WordBookTag.ftl";

    /**
     * 字典表KEY值
     */
    private static final String PARAM_KEY = "key";

    @Autowired
    private WordbookServiceImpl wordbookService;

    /**
     * 标签参数:
     * id: select元素id属性
     * name: select元素name属性
     * class: select元素class属性
     * style: select元素style属性
     * verify: select元素lay-verify属性
     * func: select元素回调函数名称
     * please: 是否显示空选项，该属性若存在select元素第一个option值将为empty
     * <p>
     * key: 数据字典KEY值 required
     */
    @Override
    @SuppressWarnings("unchecked")
    public void execute(Environment environment, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Writer out = environment.getOut();
        initParams(params);
        String key = TagUtil.getTemplateModel(PARAM_KEY, params);
        List<Wordbook> wordbooks = wordbookService.listWordbookByTopKey(key);

        params.put("wordbooks", wordbooks);

        String content = process(TEMPLATE_NAME, params, environment);
        out.write(content);
    }
}
