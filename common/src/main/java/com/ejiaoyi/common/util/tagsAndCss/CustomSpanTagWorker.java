package com.ejiaoyi.common.util.tagsAndCss;

import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.tags.SpanTagWorker;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.styledxmlparser.node.IElementNode;

/**
 * @author make
 * @since 2020/7/6
 */
public class CustomSpanTagWorker extends SpanTagWorker {
    private String display;

    public CustomSpanTagWorker(IElementNode element, ProcessorContext context) {
        super(element, context);
    }

    @Override
    public boolean processContent(String content, ProcessorContext context) {
        content = content.replace(" ", "");
        return super.processContent(content, context);
    }

    @Override
    public IPropertyContainer getElementResult() {
        return super.getElementResult();
    }
}
