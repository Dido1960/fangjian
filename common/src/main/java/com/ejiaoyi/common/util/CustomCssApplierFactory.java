package com.ejiaoyi.common.util;

import com.ejiaoyi.common.constant.HtmlLabelConstant;
import com.ejiaoyi.common.util.tagsAndCss.CustomBodyCssApplier;
import com.ejiaoyi.common.util.tagsAndCss.CustomDivCssApplier;
import com.ejiaoyi.common.util.tagsAndCss.CustomSpanCssApplier;
import com.itextpdf.html2pdf.css.apply.ICssApplier;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.styledxmlparser.node.IElementNode;
import jodd.util.StringUtil;

/**
 * 自定义css转换
 *
 * @author make
 * @since 2020/7/6
 */
public class CustomCssApplierFactory extends DefaultCssApplierFactory {

    @Override
    public ICssApplier getCustomCssApplier(IElementNode tag) {
        //设置body为透明
        if (HtmlLabelConstant.LABEL_BODY.equalsIgnoreCase(tag.name())) {
            return new CustomBodyCssApplier();
        }
        //\00A0不会被转换，需手动执行
        if (HtmlLabelConstant.LABEL_SPAN.equalsIgnoreCase(tag.name())) {
            return new CustomSpanCssApplier(tag.getAttribute(HtmlLabelConstant.LABEL_CLASS));
        }
        //设置去掉panel的边框
        if (HtmlLabelConstant.LABEL_DIV.equalsIgnoreCase(tag.name())) {
            CustomDivCssApplier divCssApplier = new CustomDivCssApplier();
            if (StringUtil.isEmpty(tag.getAttribute(HtmlLabelConstant.LABEL_CLASS))) {
                return null;
            }
            if (tag.getAttribute(HtmlLabelConstant.LABEL_CLASS).contains(HtmlLabelConstant.LABEL_CLASS_PANEL)) {
                divCssApplier.setBorder(null);
                return divCssApplier;
            }
        }
        return null;
    }
}
