package com.ejiaoyi.common.util;

import com.alibaba.fastjson.JSON;
import com.ejiaoyi.common.constant.HtmlLabelConstant;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.canvas.draw.DashedLine;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.styledxmlparser.css.media.MediaType;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jodd.util.StringUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;

/**
 * @author lesgod
 * @date 2020/07/15
 * PDF 工具类 新版PDF工具类
 */
public class PDFUtil {

    /***
     * 统一PDF字体
     *
     * **/
    private static String pdfFont = FileUtil.getProjectResourcePath() + "font" + File.separator + "STSONG.TTF";


    /***
     * 横向ftl pdf
     *
     * ***/
    public static void ftlToPdfTransverse(String ftlPath, String outPdf, Object data) throws Exception {
        ftlToPdf(ftlPath, outPdf, data, true);
    }

    /***
     * 竖向ftl pdf
     *
     * ***/
    public static void ftlToPdf(String ftlPath, String outPdf, Object data) throws Exception {
        ftlToPdf(ftlPath, outPdf, data, false);
    }

    /**
     * 用户控制，竖向、横向 pdf
     *
     * @param ftlPath
     * @param outPdf
     * @param data
     * @param isLevel
     * @throws Exception
     */
    public static void generatePdf(String ftlPath, String outPdf, Object data, boolean isLevel) throws Exception {
        ftlToPdf(ftlPath, outPdf, data, isLevel);
    }

    /**
     * 创建信息
     *
     * @param ftlPath         ftl文件路径
     * @param outPdf          输出PDF文件路径
     * @param data            需要传入到生成用的数据
     * @param orientationFlag 是否需要横向
     ****/
    private static void ftlToPdf(String ftlPath, String outPdf, Object data, boolean orientationFlag) throws Exception {
        ItextH2PEventHander eventsHander = new ItextH2PEventHander(null, null, null, true);
        Configuration freemarkerCfg = new Configuration(Configuration.VERSION_2_3_28);

        File outFileTemp = new File(outPdf + "_temp");
        FileUtil.createDir(outFileTemp.getPath());

        File ftlFile = new File(ftlPath);
        freemarkerCfg.setDirectoryForTemplateLoading(ftlFile.getParentFile());
        freemarkerCfg.setClassicCompatible(true);
        Template template;
        Locale.setDefault(Locale.ENGLISH);
        template = freemarkerCfg.getTemplate(ftlFile.getName());

        StringWriter sw = new StringWriter();
        try {
            template.process(data, sw);
        }catch (Exception e){
            System.err.println(JSON.toJSON(data));
            System.err.println("ftl报错+"+ftlPath);
            e.printStackTrace();
        }

        sw.getBuffer().toString();
        PdfDocument pdfDoc = null;
        FileOutputStream outputStream = null;
        PdfWriter pdfWriter = null;
        try {
            outputStream = new FileOutputStream(outFileTemp);
            WriterProperties writerProperties = new WriterProperties();
            writerProperties.addXmpMetadata();
            pdfWriter = new PdfWriter(outputStream, writerProperties);
            pdfDoc = new PdfDocument(pdfWriter);
            // 横向
            if (orientationFlag) {
                pdfDoc.setDefaultPageSize(com.itextpdf.kernel.geom.PageSize.A4.rotate());
            }
            pdfDoc.getCatalog().setLang(new PdfString("en-US"));

            pdfDoc.setTagged();
            pdfDoc.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));
            // 事件字体设置
            PdfFont font = PdfFontFactory.createFont(pdfFont, PdfEncodings.IDENTITY_H, false);
            eventsHander.setFont(font);
            // 页面、页脚、水印
            pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, eventsHander);
            // 设置pdf属性
            PdfDocumentInfo pdfMetaData = pdfDoc.getDocumentInfo();
            pdfMetaData.setAuthor("甘肃交易通信息技术有限公司");
            pdfMetaData.addCreationDate();
            pdfMetaData.getProducer();
            pdfMetaData.setCreator("iText Software");
            pdfMetaData.setKeywords("example, accessibility");
            pdfMetaData.setSubject("PDF accessibility");
            // pdf 转换属性设置
            ConverterProperties props = new ConverterProperties();
            // 设置输入打印用的css
            MediaDeviceDescription mediaDeviceDescription = new MediaDeviceDescription(MediaType.PRINT);
            props.setMediaDeviceDescription(mediaDeviceDescription);
            // 字体设置：itext默认不支持中文
            FontProvider fp = new FontProvider();
            fp.addStandardPdfFonts();
            // 中文设置方法一：使用itext-font-asian中的字体
            fp.addFont(pdfFont, PdfEncodings.IDENTITY_H);
            props.setFontProvider(fp);
            props.setBaseUri(FileUtil.getProjectResourcePath());
            // 设置标签自定义的标签css
            DefaultCssApplierFactory cssApplierFactory = new CustomCssApplierFactory();
            props.setCssApplierFactory(cssApplierFactory);
            // 设置自定义的标签
            DefaultTagWorkerFactory tagWorkerFactory = new CustomTagWorkerFactory();
            props.setTagWorkerFactory(tagWorkerFactory);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(sw.toString().getBytes(StandardCharsets.UTF_8));
            HtmlConverter.convertToPdf(byteArrayInputStream, pdfDoc, props);

            sw.close();
            byteArrayInputStream.close();
            pdfDoc.close();
            outputStream.close();
            pdfWriter.close();

            //自动创建书签，根据eventsHander 事件监听设置进去的值
            setBookMark(outFileTemp.getPath(), outPdf, eventsHander.getBookMark());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (sw != null) {
                    sw.close();
                }
                if (pdfDoc != null) {
                    pdfDoc.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (pdfWriter != null) {
                    pdfWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("PDF文档关闭失败!");
            }
        }

    }


    /**
     * pdf合并
     *
     * @param srcPdfPath file文件的路径信息
     * @param resultPath 合并后生成PDF路径信息
     **/
    public static void mergePdfs(List<String> srcPdfPath, String resultPath) throws Exception {
        mergePdfs(srcPdfPath, resultPath, -1, null);
    }

    /**
     * pdf合并 第一个PDF作为封面
     *
     * @param srcPdfPath file文件的路径信息
     * @param resultPath 合并后生成PDF路径信息
     **/
    public static void mergePdfs(List<String> srcPdfPath, String resultPath, String waterCode) throws Exception {
        mergePdfs(srcPdfPath, resultPath, -1, waterCode);
    }

    /**
     * pdf合并
     *
     * @param srcPdfPath file文件的路径信息
     * @param outPath    合并后生成PDF路径信息
     * @param indexCover 封面对应的srcPdfPath的索引位置 0开始
     **/
    public static void mergePdfs(List<String> srcPdfPath, String outPath, int indexCover, String waterCode) throws Exception {
        //文件缓存路径信息
        String resultPath = outPath + "_temp";
        FileUtil.createDir(resultPath);
        /******************************************1.合并pdf************************************************/
        PdfReader pdfReader = new PdfReader(srcPdfPath.get(0));
        Document document = new Document(pdfReader.getPageSize(1));
        document = setBaseInfo(document);
        FileOutputStream outputStream = new FileOutputStream(resultPath);
        PdfCopy copy = new PdfCopy(document, outputStream);
        document.open();

        List<HashMap<String, Object>> bookMark = new ArrayList<>();
        //pdf 计数PDF页面
        int countPage = 0;
        for (int i = 0; i < srcPdfPath.size(); i++) {
            PdfReader reader = new PdfReader(srcPdfPath.get(i));
            int n = reader.getNumberOfPages();
            if (i != indexCover) {
                //读取书签
                List<HashMap<String, Object>> bookMarkItems = SimpleBookmark.getBookmark(reader);
                if (bookMarkItems != null) {
                    bookMark.addAll(formatMergBookItems(bookMarkItems, countPage));
                }
                countPage += n;
                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
            }
            reader.close();
        }
        /*****************************************************************2.赋值书签***********************/

        copy.setOutlines(bookMark);

        copy.close();
        outputStream.close();
        document.close();
        pdfReader.close();

        /**********************3.封面与PDF文件合并****************/
        // 合成PDF,最后的
        if (indexCover >= 0) {
            /*****************************************************4.插入目录PDF*******************************************/
            String dirPdfPath = outPath + "_temp_dir.pdf";

            //需要封面的时候 生成目录 pdf 位置
            int num = writeDirectory(resultPath, dirPdfPath, srcPdfPath.get(indexCover), bookMark);
            writeBookMark(dirPdfPath, getCoverMark(formatMergBookItems(bookMark, num)));
            if (StringUtil.isNotEmpty(waterCode)) {
                writeWaterCode(dirPdfPath, waterCode);
            }
            writePageNum(dirPdfPath, 14, num + 1);
            FileUtil.copyFile(dirPdfPath, outPath);
            FileUtil.deleteFile(dirPdfPath);
            FileUtil.deleteFile(resultPath);
        } else {
            FileUtil.copyFile(resultPath, outPath);
            FileUtil.deleteFile(resultPath);
        }
    }

    /****
     * 创建PDF封面以及目录信息
     * @param bookMark  书签信息
     * ***/
    private static List<HashMap<String, Object>> getCoverMark(List<HashMap<String, Object>> bookMark) {

        List<HashMap<String, Object>> list = new ArrayList<>();
        {
            HashMap<String, Object> subHashMap = new HashMap<>();
            subHashMap.put("Action", "GoTo");
            subHashMap.put("Title", "封面");
            subHashMap.put("Page", 1 + " XYZ 87 790 0");
            list.add(subHashMap);
        }
        {
            HashMap<String, Object> subHashMap = new HashMap<>();
            subHashMap.put("Action", "GoTo");
            subHashMap.put("Title", "目录");
            subHashMap.put("Page", 4 + " XYZ 87 790 0");
            list.add(subHashMap);
        }
        list.addAll(bookMark);
        return list;

    }


    /**
     * 用于整理合并PDF的书签问题
     *
     * @param bookMark  书签
     * @param countPage 书签根据页码顺序往后延
     **/
    private static List<HashMap<String, Object>> formatMergBookItems(List<HashMap<String, Object>> bookMark, int countPage) {
        List<HashMap<String, Object>> list = new ArrayList<>();
        //map信息获取
        for (HashMap<String, Object> map : bookMark) {
            HashMap<String, Object> hashMap = new HashMap<>();
            if (!map.containsKey("Page")) {
                hashMap.put("Page", (countPage + 1) + " XYZ 87 769 0");
            }
            for (String key : map.keySet()) {
                if ("Page".equals(key)) {
                    StringBuilder pageInfo = new StringBuilder((String) map.get(key));
                    String[] pageInfos = pageInfo.toString().split(" ");
                    pageInfo = new StringBuilder(String.valueOf(Integer.parseInt(pageInfos[0].trim()) + countPage));
                    for (int i = 1; i < pageInfos.length; i++) {
                        pageInfo.append(" ").append(pageInfos[i]);
                    }
                    hashMap.put(key, pageInfo.toString());
                } else if ("Kids".equals(key)) {
                    hashMap.put("Kids", formatMergBookItems((List<HashMap<String, Object>>) map.get(key), countPage));
                } else if ("Named".equals(key)) {
                    // TODO
                } else {
                    hashMap.put(key, map.get(key));
                }
            }
            list.add(hashMap);
        }
        return list;
    }


    /**
     * PDF基本信息写入
     * 防止多次写入信息不一致等问题
     *
     * @param
     ****/
    private static PdfDocumentInfo setBaseInfo(PdfDocumentInfo pdfMetaData) {
        //设置相关属性
        pdfMetaData.setAuthor("甘肃交易通信息技术有限公司");
        pdfMetaData.addCreationDate();
        pdfMetaData.getProducer();
        pdfMetaData.setCreator("iText Software");
        pdfMetaData.setKeywords("example, accessibility");
        pdfMetaData.setSubject("PDF accessibility");
        return pdfMetaData;
    }

    /**
     * PDF基本信息写入
     * 防止多次写入信息不一致等问题
     ****/
    private static Document setBaseInfo(Document doc) {
        //设置相关属性
        doc.addTitle("PDF自定标题");
        doc.addAuthor("甘肃交易通信息技术有限公司");
        doc.addSubject("PDF accessibility");
        doc.addKeywords("Keywords");
        doc.addCreator("gsjytEleBid");
        doc.addCreationDate();
        //设置页边空白间距，类似网页盒模型的margin
        doc.setMargins(10, 10, 30, 30);
        return doc;
    }

    /**
     * 添加书签pdf
     *
     * @param tempPdfPath 文件缓存路径
     * @param resPdfPath  结果路径
     * @param bookMark    标记书签
     * @throws IOException
     */
    private static void setBookMark(String tempPdfPath, String resPdfPath, Map<String, List<String>> bookMark) {
        FileUtil.createDir(resPdfPath);
        PdfDocument resPdf = null;
        com.itextpdf.layout.Document document = null;
        PdfDocument tempDoc = null;
        com.itextpdf.kernel.pdf.PdfReader pdfReader = null;
        String content;
        String reg;
        // 目录TOC初始化
        Map<String, Integer> titlePrefix = new LinkedHashMap<>();
        List<HashMap<String, Object>> list = new ArrayList<>();
        // 一级目录
        HashMap<String, Object> hashMap = new HashMap<>();
        // 文件二级目录
        List<HashMap<String, Object>> subList = new ArrayList<>();
        String[] marks = {HtmlLabelConstant.FIRST_TITLE, HtmlLabelConstant.SECOND_TITLE};
        try {
            pdfReader = new com.itextpdf.kernel.pdf.PdfReader(tempPdfPath);
            tempDoc = new PdfDocument(pdfReader);

            // 书签查找分类
            for (int pageNum = 1; pageNum <= tempDoc.getNumberOfPages(); pageNum++) {
                // 读取第pageNum页的文档内容
                //  替换换行符避免换行导致的文本不匹配
                content = PdfTextExtractor.getTextFromPage(tempDoc.getPage(pageNum)).replace("\n", "");

                for (String mark : marks) {
                    Iterator<String> tocL = bookMark.get(mark).iterator();
                    while (tocL.hasNext()) {
                        // 可能存在Unicode编码为\u2003的空格,需要先转换为半角空格
                        reg = tocL.next().replace("\u2003", " ").trim();
                        if (content.contains(reg)) {
                            if (HtmlLabelConstant.FIRST_TITLE.equals(mark)) {
                                hashMap.put("Action", "GoTo");
                                hashMap.put("Title", reg);
                                hashMap.put("Page", pageNum + " XYZ 87 769 0");
                                hashMap.put("Open", "false");
                                //章节分段
                                titlePrefix.put("next" + reg + "" + pageNum, 1 - pageNum);
                            } else {
                                HashMap<String, Object> subHashMap = new HashMap<>();
                                subHashMap.put("Action", "GoTo");
                                subHashMap.put("Title", reg);
                                subHashMap.put("Page", pageNum + " XYZ 87 769 0");
                                subList.add(subHashMap);
                            }
                            //设置锚点
                            titlePrefix.put(reg, pageNum);
                            tocL.remove();
                            if (HtmlLabelConstant.FIRST_TITLE.equals(mark)) {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }

            if (subList.size() > 0) {
                hashMap.put("Kids", subList);
            }

            // 存在书签时，才加入书签
            if (hashMap.size() != 0) {
                list.add(hashMap);
            }

            PdfWriter pdfWriter = new PdfWriter(resPdfPath);
            resPdf = new PdfDocument(pdfWriter);
            tempDoc.copyPagesTo(1, tempDoc.getNumberOfPages(), resPdf);

            document = new com.itextpdf.layout.Document(resPdf);
            // 关闭资源
            document.close();
            pdfWriter.close();
            tempDoc.close();
            resPdf.close();
            pdfReader.close();
            //删除不需要的文件
            FileUtil.deleteFile(tempPdfPath);
            // 写入书签
            writeBookMark(resPdfPath, list);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pdfReader != null) {
                    pdfReader.close();
                }
                if (document != null) {
                    document.close();
                }
                if (resPdf != null) {
                    resPdf.close();
                }
                if (tempDoc != null) {
                    tempDoc.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("关闭流失败！");
            }
        }
    }

    /**
     * 将书签信息写入PDF
     *
     * @param filePdf
     * @param bookMarks
     * @throws IOException
     * @throws DocumentException
     */
    private static void writeBookMark(String filePdf, List<HashMap<String, Object>> bookMarks) throws IOException, DocumentException {
        if (CommonUtil.isEmpty(bookMarks)) {
            return;
        }

        File srcFile = new File(filePdf);
        String tempFile = srcFile.getPath() + "_temp";
        FileUtil.createFile(tempFile);

        PdfReader reader = new PdfReader(filePdf);
        FileOutputStream outputStream = new FileOutputStream(new File(tempFile));
        PdfStamper stamper = new PdfStamper(reader, outputStream);

        // 设置书签
        stamper.setOutlines(bookMarks);

        stamper.close();
        outputStream.close();
        reader.close();

        FileUtil.copyFile(tempFile, filePdf);
        FileUtil.deleteFile(tempFile);
    }

    /**
     * 写水印信息
     *
     * @param filePdf   pdf路径
     * @param waterCode 水印
     */
    public static void writeWaterCode(String filePdf, String waterCode) throws Exception {
        PdfReader reader = new PdfReader(filePdf);
        File srcFile = new File(filePdf);
        String tempFile = srcFile.getPath() + "_temp";
        FileUtil.createDir(tempFile);
        FileOutputStream outputStream = new FileOutputStream(new File(tempFile));
        PdfStamper stamper = new PdfStamper(reader, outputStream);
        int pageSize = reader.getNumberOfPages();
        BaseFont font = BaseFont.createFont(pdfFont, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        PdfContentByte content;
        for (int i = 1; i <= pageSize; i++) {
            // 水印的起始
            content = stamper.getOverContent(i);
            // 开始
            content.beginText();

            // 设置颜色 默认为蓝色
            content.setColorFill(BaseColor.RED);

            PdfGState gs = new PdfGState();
            // 设置透明度为0.8
            gs.setFillOpacity(0.1f);
            content.setGState(gs);
            content.saveState();
            // 设置字体及字号
            content.setFontAndSize(font, 40);

            // 设置起始位置
            content.setTextMatrix(64, 64);
            // content.setTextMatrix(textWidth, textHeight);
            // 开始写入水印
            content.showTextAligned(Element.ALIGN_CENTER, waterCode, 298, 421, 45);
            content.restoreState();
            content.endText();

        }
        stamper.close();
        reader.close();
        outputStream.close();

        FileUtil.copyFile(tempFile, filePdf);
        FileUtil.deleteFile(tempFile);
    }

    /**
     * 对PDF进行写目录操作
     *
     * @param sourceFile 资源文件地址
     * @param targetFile 目标文件地址
     * @param coverPath  封面地址
     * @param markBooks  书签信息
     */
    private static int writeDirectory(String sourceFile, String targetFile, String coverPath, List<HashMap<String, Object>> markBooks) throws IOException, DocumentException {
        writeBookMark(sourceFile, markBooks);
        FileUtil.createDir(targetFile);
        PdfWriter writer = new PdfWriter(targetFile);
        PdfDocument pdfDoc = new PdfDocument(writer);
        com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDoc);

        //创建目录页
        int menuIndex = 1;
        // pdfDoc.addNewPage(menuIndex);
        /*******************1.设置目录标题********/
        {
            Paragraph p = getBaseParagraph(24);
            p.setTextAlignment(TextAlignment.CENTER);
            p.add("目录");
            document.add(p);
        }
        //设置目录
        List<TabStop> tabStops = new ArrayList<>();
        tabStops.add(new TabStop(580, TabAlignment.RIGHT, new DashedLine()));

        List<TabStop> tabStopsSub = new ArrayList<>();
        tabStopsSub.add(new TabStop(580, TabAlignment.RIGHT, new DashedLine()));
        com.itextpdf.kernel.pdf.PdfReader pdfReader = new com.itextpdf.kernel.pdf.PdfReader(sourceFile);
        PdfDocument firstSourcePdf = new PdfDocument(pdfReader);

        int numberOfPages = firstSourcePdf.getNumberOfPages();
        int menuNum = 1;

        for (int i = 1; i <= numberOfPages; i++) {
            PdfPage page = firstSourcePdf.getPage(i).copyTo(pdfDoc);
            pdfDoc.addPage(page);
            /******************2.一级标题**********/
            for (HashMap<String, Object> mark : markBooks) {
                {
                    int markPage = Integer.parseInt(mark.get("Page").toString().split(" ")[0].trim());
                    if (i == markPage) {
                        menuNum++;
                        //第一页目录存放22条数据， 之后每页存放24条数据
                        if (menuNum % (menuIndex == 1 ? 24 : 26) == 0) {
                            menuIndex++;
                            pdfDoc.addNewPage(menuIndex);

                        }
                        if (mark.isEmpty()) {
                            continue;
                        }
                        String[] pageInfo = mark.get("Page").toString().split(" ");
                        //int pageNum = Integer.parseInt(pageInfo[0]);

                        Paragraph p = getBaseParagraph(16);
                        p.addTabStops(tabStops);
                        p.setBold();
                        p.add(mark.get("Title").toString());
                        p.add(new Tab());
                        p.add(pageInfo[0]);
                        String destinationKey = "p" + menuNum;
                        PdfArray destinationArray = new PdfArray();
                        destinationArray.add(page.getPdfObject());
                        destinationArray.add(new PdfName(pageInfo[1]));
                        for (int iTemp = 2; iTemp < pageInfo.length; iTemp++) {
                            destinationArray.add(new PdfNumber(Integer.parseInt(pageInfo[iTemp])));
                        }
                        try {
                            pdfDoc.addNamedDestination(destinationKey, destinationArray);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        p.setProperty(Property.ACTION, PdfAction.createGoTo(destinationKey));
                        document.add(p);
                    }
                }
                {
                    List<HashMap<String, Object>> subList = (List<HashMap<String, Object>>) mark.get("Kids");
                    /******************3.二级标题**********/
                    if (subList.isEmpty()) {
                        for (HashMap<String, Object> subMark : subList) {
                            int subMarkPage = Integer.parseInt(subMark.get("Page").toString().split(" ")[0].trim());
                            if (i == subMarkPage) {
                                if (subMark.get("Page") != null) {
                                    menuNum++;
                                    if (menuNum % (menuIndex == 1 ? 24 : 26) == 0) {
                                        menuIndex++;
                                        pdfDoc.addNewPage(menuIndex);
                                    }
                                    String[] pageInfo = subMark.get("Page").toString().split(" ");
                                    /*int pageNum = Integer.parseInt(pageInfo[0]);
                                    int x = Integer.parseInt(pageInfo[2]);
                                    int y = Integer.parseInt(pageInfo[3]);
                                    int z = Integer.parseInt(pageInfo[4]);*/
                                    Paragraph p = getBaseParagraph(14);
                                    p.setMarginLeft(40);
                                    p.addTabStops(tabStopsSub);
                                    p.add(subMark.get("Title").toString());
                                    p.add(new Tab());
                                    p.add(subMark.get("Page").toString().split(" ")[0].trim());
                                    String destinationKeySub = "p" + menuNum;
                                    PdfArray destinationArraySub = new PdfArray();
                                    destinationArraySub.add(page.getPdfObject());
                                    destinationArraySub.add(new PdfName(pageInfo[1]));
                                    for (int iTemp = 2; iTemp < pageInfo.length; iTemp++) {
                                        destinationArraySub.add(new PdfNumber(Integer.parseInt(pageInfo[iTemp])));
                                    }
                                    try {
                                        pdfDoc.addNamedDestination(destinationKeySub, destinationArraySub);
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                    p.setProperty(Property.ACTION, PdfAction.createGoTo(destinationKeySub));
                                    document.add(p);
                                }
                            }
                        }
                    }
                }
            }
        }
        pdfDoc.removePage(menuIndex);
        //添加封面
        com.itextpdf.kernel.pdf.PdfReader coverPdfReader = new com.itextpdf.kernel.pdf.PdfReader(coverPath);
        PdfDocument coverPdf = new PdfDocument(coverPdfReader);
        int numbersOfCover = coverPdf.getNumberOfPages();
        for (int i = numbersOfCover; i >= 1; i--) {
            PdfPage page = coverPdf.getPage(i).copyTo(pdfDoc);
            pdfDoc.addPage(1, page);
        }

        coverPdf.close();
        coverPdfReader.close();
        firstSourcePdf.close();
        pdfReader.close();
        document.flush();
        document.close();
        pdfDoc.close();
        writer.close();

        return menuIndex + numbersOfCover - 1;
    }


    /***
     * 获取字体基本信息
     * @param fontSize 字体大小
     */
    private static Paragraph getBaseParagraph(int fontSize) throws IOException {
        PdfFont font = PdfFontFactory.createFont(pdfFont, PdfEncodings.IDENTITY_H, false);
        Paragraph p = new Paragraph();
        p.setFont(font);
        p.setFontSize(fontSize);

        return p;
    }

    /**
     * 获取PDF页码总数
     *
     * @param pdfPath pdf路径
     */
    public static int getPageSize(String pdfPath) throws IOException {
        PdfReader reader = new PdfReader(pdfPath);
        int n = reader.getNumberOfPages();
        reader.close();
        return n;
    }

    /**
     * 对PDF文件添加页码信息
     *
     * @param pdfPath   文件地址
     * @param fontsSize 页码文字大小
     * @param startPage 页码编号的第几页
     *                  不支持jdk11
     */
    public static void writePageNum(String pdfPath, int fontsSize, int startPage) {
        File pdfFile = new File(pdfPath);
        String outFilePath = pdfFile.getParentFile().getPath() + "\\_temp" + pdfFile.getName();
        FileUtil.createDir(outFilePath);
        PdfReader reader = null;
        PdfStamper stamper;
        try {
            // 创建一个pdf读入流
            reader = new PdfReader(pdfPath);
            // 根据一个pdfreader创建一个pdfStamper.用来生成新的pdf.
            FileOutputStream outputStream = new FileOutputStream(outFilePath);
            stamper = new PdfStamper(reader, outputStream);
            // baseFont不支持字体样式设定.但是font字体要求操作系统支持此字体会带来移植问题.
            // PdfFont font = PdfFontFactory.createFont(pdfFont, PdfEncodings.IDENTITY_H, false);
            BaseFont font = BaseFont.createFont(pdfFont, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            // 获得宽
            // 获取页码
            int num = reader.getNumberOfPages();
            for (int i = 1; i <= num; i++) {
                if (i >= startPage) {
                    Rectangle pageSize = reader.getPageSize(i);
                    float width = pageSize.getWidth();

                    PdfContentByte over = stamper.getOverContent(i);
                    over.beginText();
                    over.setFontAndSize(font, fontsSize);
                    over.setColorFill(BaseColor.BLACK);
                    over.setTextMatrix((width / 2) - 20, 15);
                    // 设置页码在页面中的坐标
                    //over.setTextMatrix((int) width - 55, 15);
                    over.showText((i - startPage + 1) + "/" + (num - startPage + 1));
                    over.endText();
                    over.stroke();
                }
            }
            stamper.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        FileUtil.copyFile(outFilePath, pdfPath);
        FileUtil.deleteFile(outFilePath);
    }

    /**
     * 去掉签名的PDF信息
     *
     * @param pdfPath 将要删除签名的文件
     * @param outPath 删除签名后文件输出位置
     */
    public static void removeSigner(String pdfPath, String outPath) {
        try {
            byte[] src = FileUtil.read2ByteArray(pdfPath);
            PdfReader reader = new PdfReader(src);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, baos);
            AcroFields af = stamper.getAcroFields();
            ArrayList<String> names = af.getSignatureNames();
            if (names.size() > 0) {
                stamper.setFormFlattening(true);
            }
            stamper.close();
            byte[] out = baos.toByteArray();
            FileUtil.writeFile(out, outPath);
            baos.close();
        } catch (Exception e) {
            FileUtil.copyFile(pdfPath, outPath);
            e.printStackTrace();
        }
    }

    /**
     * 检测PDF是否损坏
     *
     * @param file 校验的pdf文件
     * @return 是否破损
     */
    public static boolean validPdf(String file) {
        boolean flag = false;
        int n = 0;
        PdfReader reader = null;
        try {
            reader = new PdfReader(file);
            Document document = new Document(reader.getPageSize(1));
            document.open();
            n = reader.getNumberOfPages();
            if (n != 0) {
                flag = true;
            }
            document.close();
        } catch (IOException e) {
            return false;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return flag;
    }

}
