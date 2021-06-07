package com.ejiaoyi.common.util.tagsAndCss;

import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.css.CssConstants;
import com.itextpdf.html2pdf.css.apply.impl.BodyTagCssApplier;
import com.itextpdf.styledxmlparser.node.IStylesContainer;

import java.util.Map;

/**
 * @author make
 * @since 2020/7/6
 */
public class CustomBodyCssApplier extends BodyTagCssApplier {
    // 设置背景为透明
    private String color = "rgba(0 0 0 0)";

    public CustomBodyCssApplier() {
    }

    @Override
    public void apply(ProcessorContext context, IStylesContainer stylesContainer, ITagWorker tagWorker) {
        Map<String, String> cssProps = stylesContainer.getStyles();
        cssProps.put(CssConstants.BACKGROUND_COLOR, this.color);
        super.apply(context, stylesContainer, tagWorker);
    }


    /**
     * 设置body背景颜色
     *
     * @param color 颜色名称或十六进制
     */
    public void setColor(String color) {
        this.color = color;
    }
}
