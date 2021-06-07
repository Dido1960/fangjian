package com.ejiaoyi.common.tag;

import com.ejiaoyi.common.base.BaseTag;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

@Component
public class UploadTag extends BaseTag {


    private static final String TAG_NAME = "UploadTag";

    public UploadTag() {
        super(TAG_NAME);
    }

    private static final String TEMPLATE_NAME = "/tag/UploadTag.ftl";


    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels, TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        Writer out = environment.getOut();

        String content = process(TEMPLATE_NAME, map, environment);
        out.write(content);
    }
}
