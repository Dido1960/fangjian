package com.ejiaoyi.common.util.tagsAndCss;

import com.ejiaoyi.common.constant.HtmlLabelConstant;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.tags.SpanTagWorker;
import com.itextpdf.html2pdf.css.apply.impl.SpanTagCssApplier;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.element.Text;
import com.itextpdf.styledxmlparser.node.IStylesContainer;
import jodd.util.StringUtil;

import java.util.Iterator;

/**
 * @author make
 * @since 2020/7/6
 */
public class CustomSpanCssApplier extends SpanTagCssApplier {

    private String replaceStr = "";

    public CustomSpanCssApplier() {
    }

    public CustomSpanCssApplier(String tagClass) {
        //不需要填写信息但需要下划线
        if (StringUtil.isNotEmpty(tagClass) && tagClass.contains(HtmlLabelConstant.SPAN_NO_WORD)) {
            this.replaceStr = "\t\t";
        }
    }

    @Override
    public void apply(ProcessorContext context, IStylesContainer stylesContainer, ITagWorker tagWorker) {
        if (tagWorker instanceof SpanTagWorker) {
            SpanTagWorker spanTagWorker = (SpanTagWorker) tagWorker;
            Iterator var6 = spanTagWorker.getAllElements().iterator();
            IPropertyContainer elem;
            while (var6.hasNext()) {
                elem = (IPropertyContainer) var6.next();
                if (elem instanceof Text) {
                    Text content = (Text) elem;
                    //itext无法自动转换该编码
                    content.setText(content.getText().replaceAll("\\\\00A0", this.replaceStr));
                }
            }
            super.apply(context, stylesContainer, tagWorker);
        }


    }
}
