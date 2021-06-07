package com.ejiaoyi.common.util.tagsAndCss;

import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.tags.ATagWorker;
import com.itextpdf.styledxmlparser.node.IElementNode;

/**
 * @author make
 * @since 2020/7/6
 */
public class CustomATagWorker extends ATagWorker {
    public CustomATagWorker(IElementNode element, ProcessorContext context) {
        super(element, context);

    }

    /**
     * 设置a标签全部为空以隐藏a标签的内容
     *
     * @param content
     * @param context
     * @return
     */
    @Override
    public boolean processContent(String content, ProcessorContext context) {
        return super.processContent("", context);
    }
}
