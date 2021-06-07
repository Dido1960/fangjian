package com.ejiaoyi.common.util.tagsAndCss;

import com.ejiaoyi.common.constant.HtmlLabelConstant;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.tags.TdTagWorker;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.styledxmlparser.node.IElementNode;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author make
 * @since 2020/7/6
 */
@Slf4j
public class CustomTdTagWorker extends TdTagWorker {
    private boolean isSecond;
    private String mark = "";

    public CustomTdTagWorker(IElementNode element, ProcessorContext context) {
        super(element, context);
        this.isSecond = StringUtil.isNotEmpty(element.getAttribute(HtmlLabelConstant.LABEL_CLASS))
                && element.getAttribute(HtmlLabelConstant.LABEL_CLASS).contains(HtmlLabelConstant.TITLE_SUFFIX);
        if (isSecond) {
            if (element.getAttribute(HtmlLabelConstant.LABEL_CLASS).contains(HtmlLabelConstant.FIRST_TITLE)) {
                mark = HtmlLabelConstant.FIRST_TITLE;
            } else {
                mark = HtmlLabelConstant.SECOND_TITLE;
            }
        }
    }

    @Override
    public boolean processContent(String content, ProcessorContext context) {
        PdfDocumentInfo info = context.getPdfDocument().getDocumentInfo();
        int i = 0;
        while (this.isSecond) {
            if (StringUtil.isEmpty(info.getMoreInfo(this.mark + i))) {
                info.setMoreInfo(this.mark + i, content.split("\n")[0]);
                if (i != 0 && HtmlLabelConstant.FIRST_TITLE.equals(this.mark)) {
                    int secondNum = 0;
                    String msg=null;
                    while (!StringUtil.isEmpty(msg=info.getMoreInfo(HtmlLabelConstant.SECOND_TITLE + secondNum))) {
                        log.error(HtmlLabelConstant.SECOND_TITLE + secondNum+":==TD=>>:::"+msg+":");
                        secondNum++;
                    }
                    info.setMoreInfo(HtmlLabelConstant.NEXT_PREFIX + i, String.valueOf(secondNum));
                }
                break;
            }
            i++;
        }
        return super.processContent(content, context);
    }
}
