package com.ejiaoyi.common.util.tagsAndCss;

import com.ejiaoyi.common.constant.HtmlLabelConstant;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.tags.SpanTagWorker;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.styledxmlparser.node.IElementNode;

/**
 * @author make
 * @since 2020/7/6
 */
public class CustomInputTagWorker extends SpanTagWorker {
    private String iValue;

    public CustomInputTagWorker(IElementNode element, ProcessorContext context) {
        super(element, context);
        this.iValue = element.getAttribute(HtmlLabelConstant.LABEL_ATTR_VALUE);
    }

    @Override
    public boolean processContent(String content, ProcessorContext context) {
        return super.processContent(this.iValue, context);
    }

    @Override
    public IPropertyContainer getElementResult() {
        return super.getElementResult();
    }
}
