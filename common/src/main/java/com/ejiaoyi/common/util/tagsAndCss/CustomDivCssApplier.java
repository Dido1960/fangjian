package com.ejiaoyi.common.util.tagsAndCss;

import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.css.apply.ICssApplier;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.property.Property;
import com.itextpdf.styledxmlparser.node.IStylesContainer;

import java.util.Map;

/**
 * @author make
 * @since 2020/7/6
 */
public class CustomDivCssApplier implements ICssApplier {

    private Border border = null;
    private Boolean changeBoder = false;

    public CustomDivCssApplier() {
    }

    @Override
    public void apply(ProcessorContext context, IStylesContainer stylesContainer, ITagWorker tagWorker) {
        Map<String, String> cssProps = stylesContainer.getStyles();
        IPropertyContainer container = tagWorker.getElementResult();
        if (container != null) {
            //没有边框
            if (this.changeBoder) {
                container.setProperty(Property.BORDER, border);
            }
        }
    }

    public void setBorder(Border border) {
        changeBoder = true;
        this.border = border;
    }

}
