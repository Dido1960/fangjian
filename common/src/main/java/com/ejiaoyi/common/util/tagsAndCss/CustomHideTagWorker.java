package com.ejiaoyi.common.util.tagsAndCss;

import com.ejiaoyi.common.constant.HtmlLabelConstant;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.util.WaitingInlineElementsHelper;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.styledxmlparser.node.IElementNode;

import java.util.Map;

/**
 * @author make
 * @since 2020/7/6
 */
public class CustomHideTagWorker implements ITagWorker {
    private WaitingInlineElementsHelper inlineHelper;

    public CustomHideTagWorker(IElementNode element, ProcessorContext context) {
        Map<String, String> styles = element.getStyles();
        this.inlineHelper = new WaitingInlineElementsHelper(styles == null ? null : styles.get(HtmlLabelConstant.WHITE_SPACE),
                styles == null ? null : styles.get(HtmlLabelConstant.TEXT_TRANSFORM));
    }

    @Override
    public void processEnd(IElementNode elementNode, ProcessorContext context) {

    }

    @Override
    public boolean processContent(String content, ProcessorContext context) {
        this.inlineHelper.add(content);
        return true;
    }

    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        return childTagWorker instanceof CustomHideTagWorker;
    }

    @Override
    public IPropertyContainer getElementResult() {
        return null;
    }
}
