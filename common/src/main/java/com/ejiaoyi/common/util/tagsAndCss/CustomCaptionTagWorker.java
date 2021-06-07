package com.ejiaoyi.common.util.tagsAndCss;

import com.ejiaoyi.common.constant.HtmlLabelConstant;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.tags.TableTagWorker;
import com.itextpdf.html2pdf.attach.util.WaitingInlineElementsHelper;
import com.itextpdf.html2pdf.attach.wrapelement.TableRowWrapper;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.styledxmlparser.node.IElementNode;
import jodd.util.StringUtil;

import java.util.Map;

/**
 * @author make
 * @since 2020/7/6
 */
public class CustomCaptionTagWorker implements ITagWorker {
    private TableRowWrapper rowWrapper = new TableRowWrapper();
    private ITagWorker parentTagWorker;

    private WaitingInlineElementsHelper inlineHelper;
    private String value;
    private Map<String, String> css;

    private boolean isTitleSuffix;
    private String mark = "";


    public CustomCaptionTagWorker(IElementNode element, ProcessorContext context) {
        this.css = element.getStyles();
        this.parentTagWorker = context.getState().empty() ? null : context.getState().top();
        this.inlineHelper = new WaitingInlineElementsHelper(element.getStyles().get(HtmlLabelConstant.WHITE_SPACE),
                element.getStyles().get(HtmlLabelConstant.TEXT_TRANSFORM));
        if (this.parentTagWorker instanceof TableTagWorker) {
            ((TableTagWorker) this.parentTagWorker).applyColStyles();
        }
        this.isTitleSuffix = StringUtil.isNotEmpty(element.getAttribute(HtmlLabelConstant.LABEL_CLASS))
                && element.getAttribute(HtmlLabelConstant.LABEL_CLASS).contains(HtmlLabelConstant.TITLE_SUFFIX);
        if (isTitleSuffix) {
            if (element.getAttribute(HtmlLabelConstant.LABEL_CLASS).contains(HtmlLabelConstant.FIRST_TITLE)) {
                mark = HtmlLabelConstant.FIRST_TITLE;
            } else {
                mark = HtmlLabelConstant.SECOND_TITLE;
            }
        }

    }

    @Override
    public void processEnd(IElementNode elementNode, ProcessorContext processorContext) {

    }

    @Override
    public boolean processContent(String content, ProcessorContext context) {
        PdfDocumentInfo info = context.getPdfDocument().getDocumentInfo();
        int i = 0;
        while (this.isTitleSuffix) {
            if (StringUtil.isEmpty(info.getMoreInfo(this.mark + i))) {
                info.setMoreInfo(this.mark + i, content.split("\n")[0]);
                break;
            }
            i++;
        }
        this.value = content;
        return this.parentTagWorker != null && this.parentTagWorker.processContent(content, context);
    }

    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext processorContext) {
        if (childTagWorker.getElementResult() instanceof Cell) {
            Cell cell = (Cell) childTagWorker.getElementResult();

            this.rowWrapper.addCell(cell);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public IPropertyContainer getElementResult() {
        return null;
    }

    TableRowWrapper getTableRowWrapper() {
        return this.rowWrapper;
    }
}
