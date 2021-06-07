package com.ejiaoyi.common.util.tagsAndCss;

import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.css.apply.impl.TrTagCssApplier;
import com.itextpdf.styledxmlparser.node.IStylesContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author make
 * @since 2020/7/6
 */
public class CustomCaptionCssApplier extends TrTagCssApplier {
    public CustomCaptionCssApplier() {
    }

    @Override
    public void apply(ProcessorContext context, IStylesContainer stylesContainer, ITagWorker tagWorker) {
        super.apply(context, stylesContainer, tagWorker);
        Map<String, String> css = new HashMap<>();
        stylesContainer.setStyles(css);
    }
}
