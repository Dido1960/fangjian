package com.ejiaoyi.common.util;

import com.ejiaoyi.common.constant.HtmlLabelConstant;
import com.ejiaoyi.common.util.tagsAndCss.*;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.styledxmlparser.node.IElementNode;

import java.util.Map;

/**
 * 自定义tag转换
 *
 * @author make
 * @since 2020/7/6
 */
public class CustomTagWorkerFactory extends DefaultTagWorkerFactory {

    @Override
    public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
        Map<String, String> styles = tag.getStyles();
        if (styles.get(HtmlLabelConstant.WHITE_SPACE) != null && (styles.get(HtmlLabelConstant.TEXT_DECORATION) == null || !styles.get(HtmlLabelConstant.TEXT_DECORATION).contains(HtmlLabelConstant.UNDERLINE)) && HtmlLabelConstant.LABEL_SPAN.equalsIgnoreCase(tag.name())) {
            return new CustomSpanTagWorker(tag, context);
        }
        //设置书签用
        if (HtmlLabelConstant.LABEL_DIV.equalsIgnoreCase(tag.name())) {
            return new CustomDivTagWorker(tag, context);
        }
        //默认的input标签会出现异常,将其转换为span标签
        if (HtmlLabelConstant.LABEL_INPUT.equalsIgnoreCase(tag.name())) {
            return new CustomInputTagWorker(tag, context);
        }
        // 设置书签用
        if (HtmlLabelConstant.LABEL_TABLE_TD.equalsIgnoreCase(tag.name())) {
            return new CustomTdTagWorker(tag, context);
        }
        // iText没有对应caption的处理器,暂时用于隐藏该标签的值
        if (HtmlLabelConstant.CAPTION.equalsIgnoreCase(tag.name())) {
            return new CustomCaptionTagWorker(tag, context);
        }
        // 设置a标签的值为空使其隐藏(可以在页面使用css隐藏)
        if (HtmlLabelConstant.LABEL_A.equalsIgnoreCase(tag.name())) {
            return new CustomATagWorker(tag, context);
        }
        // 不需要的标签
        if (HtmlLabelConstant.OBJECT.equalsIgnoreCase(tag.name()) || HtmlLabelConstant.LABEL_EMBED.equalsIgnoreCase(tag.name())) {
            return new CustomHideTagWorker(tag, context);
        }

        return null;
    }
}
