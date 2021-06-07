package com.ejiaoyi.common.util;

import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.enums.BidProtype;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.enums.TemplateNameEnum;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.SimpleBookmark;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评标报告PDF 工具类
 *
 * @author fengjunhong
 * @since 2020-9-24
 */
@Log4j2
public class ReportPdfUtil {

    /**
     * 评标报告后缀
     */
    private static final String PDF_SUFFIX = ".pdf";

    /**
     * 模板后缀
     */
    private static final String FTL_SUFFIX = ".ftl";

    /**
     * 模板所在文件夹
     */
    private static final String TEMPLATE_PATH = FileUtil.getProjectResourcePath() + "ftl" + File.separator + "evalReport";

    /**
     * 回退数据模板所在文件夹
     */
    private static final String BACK_TEMPLATE_PATH = FileUtil.getProjectResourcePath() + "ftl" + File.separator + ProjectFileTypeConstant.BEFORE_ROLLBACK_DATA;


    /**
     * ftl为PDF
     *
     * @param bidSection   标段信息
     * @param map          数据
     * @param name 模板名称
     * @param levelPrint 是否水平打印
     * @param outPdfPath   pdf输出路径
     * @return
     */
    public static boolean generatePdf(BidSection bidSection, Map<String, Object> map,String name,boolean levelPrint, String outPdfPath) {

        String bidClassifyCode = bidSection.getBidClassifyCode();
        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidClassifyCode);
        assert bidProtype != null;
        String bidType = getProjectCode(bidProtype);
        String ftlPath;
        if (Enabled.YES.getCode().equals(bidSection.getCancelStatus())) {
            // 流标获取流标模板
            ftlPath = TEMPLATE_PATH + File.separator + bidType + File.separator + "cancel" + File.separator + name + FTL_SUFFIX;
        } else {
            ftlPath = TEMPLATE_PATH + File.separator + bidType + File.separator + name + FTL_SUFFIX;
        }
        if (CommonUtil.isEmpty(outPdfPath)) {
            // 指定默认路径
            outPdfPath = FileUtil.getEvalReportFilePath(String.valueOf(bidSection.getId())) + File.separator + name + PDF_SUFFIX;
        }
        try {
            RedissonUtil.lock("FTLTOPDF:"+ftlPath);
            PDFUtil.generatePdf(ftlPath, outPdfPath, map, levelPrint);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(ftlPath+":==>>>"+e.getMessage());
            return false;
        }finally {
            RedissonUtil.unlock("FTLTOPDF:"+ftlPath);
        }
        return true;
    }

    /**
     * 合成最终评标报告
     * @param bidSection       标段
     * @param dic 存放生成报告的目录
     * @param tempList 模板列表
     *
     * @param evaluationMethod 评标办法
     * @return 合成成功，返回true
     */
    public static boolean mergePdf(BidSection bidSection,String dic,List<String> tempList, String evaluationMethod,String outPutPath) {
        List<String> list = checkReportComplete(tempList,dic, evaluationMethod, bidSection);
        if (CommonUtil.isEmpty(list)) {
            return false;
        }
        if (CommonUtil.isEmpty(outPutPath)){
            outPutPath = FileUtil.getEvalReportFilePath(String.valueOf(bidSection.getId())) + File.separator + "evalReport.pdf";
        }
        try {
            // 合成评标报告
            PDFUtil.mergePdfs(list, outPutPath);
            // 插入页码
            PDFUtil.writePageNum(outPutPath, 15, 2);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 检查评标报告完整性
     * @param finalPdfNames 模板列表名称
     * @param dic              评标报告存放的目录
     * @param evaluationMethod 评标办法
     * @param bidSection       标段信息
     * @return 返回合成的评标报告list
     */
    private static List<String> checkReportComplete(List<String> finalPdfNames,String dic, String evaluationMethod, BidSection bidSection) {
        // pdf绝对路径
        List<String> absolutePdfPath = new ArrayList<>();
        // 读取存放各环节生成报告的目录
        List<String> generatePdfNames = FileUtil.listFileName(dic);
        assert generatePdfNames != null;
        // 获取当前分类所有模板名称
        for (String finalPdfName : finalPdfNames) {
            if (!generatePdfNames.contains(finalPdfName + PDF_SUFFIX)) {
                System.err.println("评标报告缺少：" + finalPdfName+ PDF_SUFFIX);
                return null;
            }
            absolutePdfPath.add(dic + File.separator + finalPdfName + PDF_SUFFIX);
        }
        return absolutePdfPath;
    }


    /**
     * 根据项目类型，返回模板所在目录名称
     *
     * @param bidCode 标段类型
     * @return
     */
    private static String getProjectCode(BidProtype bidCode) {
        switch (bidCode) {
            case CONSTRUCTION:
                return BidProtype.CONSTRUCTION.getTemplateDir();
            case SUPERVISION:
                return BidProtype.SUPERVISION.getTemplateDir();
            case INVESTIGATION:
                return BidProtype.INVESTIGATION.getTemplateDir();
            case DESIGN:
                return BidProtype.DESIGN.getTemplateDir();
            case ELEVATOR:
                return BidProtype.ELEVATOR.getTemplateDir();
            case QUALIFICATION:
                return BidProtype.QUALIFICATION.getTemplateDir();
            case EPC:
                return BidProtype.EPC.getTemplateDir();
            default:
                return null;
        }
    }

    /**
     * ftl为PDF
     *
     * @param map          数据
     * @param templateEnum 模板对象
     * @param outPdfPath   pdf输出路径
     * @return
     */
    public static boolean generateBackPdf(BidSection bidSection, Map<String, Object> map, TemplateNameEnum templateEnum, String outPdfPath) {
        String bidClassifyCode = bidSection.getBidClassifyCode();
        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidClassifyCode);
        String bidType = getProjectCode(bidProtype);
        String ftlPath = BACK_TEMPLATE_PATH + File.separator + bidType + File.separator + templateEnum.getName() + FTL_SUFFIX;
        if (CommonUtil.isEmpty(outPdfPath)) {
            // 指定默认路径
            outPdfPath = FileUtil.getBeforeRollbackDataFilePath(bidSection.getId()) + File.separator + templateEnum.getName() + PDF_SUFFIX;
        }
        try {
            PDFUtil.generatePdf(ftlPath, outPdfPath, map, templateEnum.getLevelPrint());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 合并评审回退报告整合书签
     *
     * @param srcPdfPath file文件的路径信息
     * @param outPath    合并后生成PDF路径信息
     **/
    public static void mergeBackReportPdf(List<String> srcPdfPath, String outPath) throws Exception {
        int indexCover = -1;
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
        List<HashMap<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < bookMark.size(); i++) {
            Map<String, Object> map = bookMark.get(i);
            if (i != 0 && CommonUtil.isEmpty(map.get("Title"))) {
                List<?> kids = (List<?>) list.get(0).get("Kids");
                if (!CommonUtil.isEmpty(kids)){
                    kids.addAll((List) map.get("Kids"));
                    list.get(0).put("Kids", kids);
                }else {
                    list.get(0).put("Kids", map.get("Kids"));
                }
                continue;
            }
            list.add(bookMark.get(0));
        }
        copy.setOutlines(list);

        copy.close();
        outputStream.close();
        document.close();
        pdfReader.close();
        // 合成PDF,最后的
        FileUtil.copyFile(resultPath, outPath);
        FileUtil.deleteFile(resultPath);
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
     * 合成报告
     * @param bidSection 标段信息
     * @param finalPdfNames 待合成模板列表
     * @param dic 保存存放目录
     * @param evaluationMethod 评标办法
     * @param outPutPath 合成输出路径
     * @return
     */
    public static boolean mergeReportPdf(BidSection bidSection,List<String> finalPdfNames,String dic, String evaluationMethod,String outPutPath) {
        List<String> list = checkReportComplete(finalPdfNames,dic, evaluationMethod, bidSection);
        if (CommonUtil.isEmpty(list)) {
            return false;
        }
        try {
            // 合成评标报告
            PDFUtil.mergePdfs(list, outPutPath);
            // 插入页码
            PDFUtil.writePageNum(outPutPath, 15, 2);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
