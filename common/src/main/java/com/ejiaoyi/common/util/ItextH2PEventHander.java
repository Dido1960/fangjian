package com.ejiaoyi.common.util;

import com.ejiaoyi.common.constant.HtmlLabelConstant;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Itext处理工具类
 *
 * @author Make
 * @since 2020/7/24
 */
@Slf4j
public class ItextH2PEventHander implements IEventHandler {
    //基本内容
    private String header = null;
    private boolean showPage = true;

    //设置文字水印
    private String waterMarker = null;
    private Float waterMarkerOpacity = 0.1F;

    //设置水印印章
    private String sealUrl = null;
    private float height = 64;
    private float width = 64;
    private float left = 0;
    private float top = 0;
    private Boolean imageAlign = true;

    //分页属性
    private Integer startPage = 1;
    private Integer lastPagedNum = 1;

    //整体字体设置
    private PdfFont font = null;

    //设置单个内容的字体
    private float headerFontSize = 14;
    private float makerFontSize = 40;
    private float footFontSize = 14;


    //设置单个内容的字体
    private PdfFont headerFont = null;
    private PdfFont makerFont = null;
    private PdfFont footFont = null;

    //外部获取书签
    private Map<String, List<String>> bookMark = new HashMap<>();

    /**
     * 默认设置，自动打印页尾页数
     */
    public ItextH2PEventHander() {
    }

    /**
     * 初始化设置1
     *
     * @param header
     * @param waterMarker
     * @param sealUrl
     * @param showPage    默认为true
     */
    public ItextH2PEventHander(String header, String waterMarker, String sealUrl, boolean showPage) {

        this.header = header;
        this.waterMarker = waterMarker;
        this.sealUrl = sealUrl;
        if (!showPage) {
            this.showPage = showPage;
        }
    }

    /**
     * 初始化设置2
     *
     * @param header
     * @param waterMarker
     * @param sealUrl
     * @param showPage    默认为true
     */
    public ItextH2PEventHander(String header, String waterMarker, String sealUrl, boolean showPage, PdfFont font) {
        this.header = header;
        this.waterMarker = waterMarker;
        this.sealUrl = sealUrl;
        if (!showPage) {
            this.showPage = showPage;
        }
        //字体设置
        this.font = font;
    }

    @Override
    public void handleEvent(Event event) {
        //获取基本参数
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        int pageNumber = pdfDoc.getPageNumber(page);
        PdfDocumentInfo info = pdfDoc.getDocumentInfo();
        getBookMark(info);
        PdfCanvas pdfCanvas = new PdfCanvas(
                page.newContentStreamBefore(), page.getResources(), pdfDoc);
        try {
            Canvas canvas = new Canvas(pdfCanvas, page.getPageSize());

            if (font != null) {
                canvas.setFont(font);
            }
            lastPagedNum = pageNumber;
            canvas.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pdfCanvas.release();
    }

    /**
     * 获取pdf属性中保存的书签信息
     *
     * @param info
     */
    private void getBookMark(PdfDocumentInfo info) {
        String[] marks = {HtmlLabelConstant.FIRST_TITLE, HtmlLabelConstant.SECOND_TITLE};
        this.bookMark.put(HtmlLabelConstant.NEXT_PREFIX, new ArrayList<>());
        for (String mark : marks) {
            this.bookMark.put(mark, new ArrayList<>());
            int i = 0;
            String moreInfo = null;
            while (!StringUtil.isEmpty(moreInfo = info.getMoreInfo(mark + i))) {
                if (HtmlLabelConstant.FIRST_TITLE.equals(mark) && i != 0) {
                    log.warn("ftl中含有多个一级标题 ===>" + mark + i + moreInfo);
                }
                this.bookMark.get(mark).add(moreInfo);
                if (i != 0 && mark.equals(HtmlLabelConstant.SECOND_TITLE)) {
                    this.bookMark.get(HtmlLabelConstant.NEXT_PREFIX).add(info.getMoreInfo(HtmlLabelConstant.NEXT_PREFIX + i));
                }
                i++;
            }
        }
    }

    /**
     * 设置水印图片居中
     */
    public void setImageCenter() {
        imageAlign = true;
    }


    public Map<String, List<String>> getBookMark() {
        return bookMark;
    }

    /**
     * 设置页头
     *
     * @param header
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * 设置页尾
     *
     * @param waterMarker
     */
    public void setWaterMarker(String waterMarker) {
        this.waterMarker = waterMarker;
    }

    /**
     * 设置图片水印路径
     *
     * @param sealUrl
     */
    public void setsealUrl(String sealUrl) {
        this.sealUrl = sealUrl;
    }

    /**
     * 设置是否显示页码
     *
     * @param showPage
     */
    public void setShowPage(boolean showPage) {
        this.showPage = showPage;
    }

    /**
     * 设置初始页码
     *
     * @param startPage
     */
    public void setStartPage(Integer startPage) {
        this.startPage = startPage;
    }

    /**
     * 设置字体
     *
     * @param font
     */
    public void setFont(PdfFont font) {
        this.font = font;
    }

    /**
     * 获取上次转换时的页码数
     *
     * @return
     */
    public Integer getLastPagedNum() {
        return lastPagedNum;
    }

    /**
     * 文字大小
     *
     * @param headerFontSize
     */
    public void setHeaderFontSize(float headerFontSize) {
        this.headerFontSize = headerFontSize;
    }

    /**
     * 文字大小
     *
     * @param makerFontSize
     */
    public void setMakerFontSize(float makerFontSize) {
        this.makerFontSize = makerFontSize;
    }

    /**
     * 文字大小
     *
     * @param footFontSize
     */
    public void setFootFontSize(float footFontSize) {
        this.footFontSize = footFontSize;
    }

    /**
     * 设置水印图片的大小与位置
     *
     * @param height
     */
    public void setSealImage(String sealUrl, float height, float width, float left, float top) {
        imageAlign = false;
        this.height = height;
        this.width = width;
        this.left = left;
        this.top = top;
    }

    /**
     * 设置文字水印的透明度
     *
     * @param waterMarkerOpacity
     */
    public void setWaterMarkerOpacity(Float waterMarkerOpacity) {
        this.waterMarkerOpacity = waterMarkerOpacity;
    }
}
