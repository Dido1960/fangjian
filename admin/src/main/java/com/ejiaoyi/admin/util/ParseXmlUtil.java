package com.ejiaoyi.admin.util;

import com.ejiaoyi.common.constant.BidFileConstant;
import com.ejiaoyi.common.constant.TenderFileConstant;
import com.ejiaoyi.admin.dto.BidInfoDTO;
import com.ejiaoyi.admin.dto.TenderInfoDTO;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * xml文件解析工具类
 *
 * @author Mike
 * @since 2021/4/6
 */
public class ParseXmlUtil {

    private static Logger logger = Logger.getLogger( ParseXmlUtil.class.getName());

    /**
     * 投标信息解析
     *
     * @param tenderInfoXml 投标文件地址
     */
    public static TenderInfoDTO parseTenderInfoXml(String tenderInfoXml) throws Exception {
        File file = new File(tenderInfoXml);
        if (!file.exists()) {
            logger.log(Level.INFO, "投标文件信息xml【" + tenderInfoXml + "】不存在");
            return null;
        }
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();

        String projectName = getAttr(root, TenderFileConstant.OldFileNode.PROJECT_NAME);
        String projectCode = getAttr(root, TenderFileConstant.OldFileNode.PROJECT_CODE);
        String bidSectionName = getAttr(root, TenderFileConstant.OldFileNode.BID_SECTION_NAME);
        String bidSectionCode = getAttr(root, TenderFileConstant.OldFileNode.BID_SECTION_CODE);
        String totalPrice = getAttr(root, TenderFileConstant.OldFileNode.BID_PRICE);
        String constructionDays = getAttr(root, TenderFileConstant.OldFileNode.TOTAL_TIME_LIMIT);
        String rate = getAttr(root, TenderFileConstant.OldFileNode.RATE);
        String quality = getAttr(root, TenderFileConstant.OldFileNode.QUALITY);
        String bidderName = getAttr(root, TenderFileConstant.OldFileNode.BIDDER_NAME);
        String bidderCode = getAttr(root, TenderFileConstant.OldFileNode.BIDDER_CO_CODE);
        String legalAgent = getAttr(root, TenderFileConstant.OldFileNode.LEGAL_AGENT);

        return TenderInfoDTO.builder()
                .projectCode(projectCode)
                .projectName(projectName)
                .bidSectionCode(bidSectionCode)
                .bidSectionName(bidSectionName)
                .priceTotal(totalPrice)
                .constructionDays(constructionDays)
                .rate(rate)
                .quality(quality)
                .bidderName(bidderName)
                .bidderCode(bidderCode)
                .legalAgent(legalAgent)
                .build();
    }


    /**
     * 招标信息解析
     *
     * @param bidInfoXml 招标文件信息地址
     */
    public static BidInfoDTO parseBidInfoXml(String bidInfoXml) throws Exception {
        File file = new File(bidInfoXml);
        if (!file.exists()) {
            logger.log(Level.INFO, "标文件信息xml【" + bidInfoXml + "】不存在");
            return null;
        }
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();

        String projectName = getAttr(root, BidFileConstant.OldFileNode.PROJECT_NAME);
        String projectCode = getAttr(root, BidFileConstant.OldFileNode.PROJECT_CODE);
        String bidSectionName = getAttr(root, BidFileConstant.OldFileNode.BID_SECTION_NAME);
        String bidSectionCode = getAttr(root, BidFileConstant.OldFileNode.BID_SECTION_CODE);
        String bidDocReferEndTime = getAttr(root, BidFileConstant.OldFileNode.BID_DOC_REFER_END_TIME);
        String bidOpenTime = getAttr(root, BidFileConstant.OldFileNode.BID_OPEN_TIME);
        String tendererName = getAttr(root, BidFileConstant.OldFileNode.TENDERER_NAME);
        String agencyName = getAttr(root, BidFileConstant.OldFileNode.AGENCY_NAME);
        String expertCount = getAttr(root, BidFileConstant.OldFileNode.EXPERT_COUNT);
        String representativeCount = getAttr(root, BidFileConstant.OldFileNode.REPRESENTATIVE_COUNT);

        return BidInfoDTO.builder()
                .projectCode(projectCode)
                .projectName(projectName)
                .bidSectionCode(bidSectionCode)
                .bidSectionName(bidSectionName)
                .bidDocReferEndTime(bidDocReferEndTime)
                .bidOpenTime(bidOpenTime)
                .tendererName(tendererName)
                .agencyName(agencyName)
                .expertCount(expertCount)
                .representativeCount(representativeCount)
                .build();
    }

    public static String parseProTypeByXml(String xml) throws Exception {
        File file = new File(xml);
        if (!file.exists()) {
            logger.log(Level.INFO, "标文件信息xml【" + xml + "】不存在");
            return null;
        }
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();

        String tenderProjectClassifyCode = null;
        String protype = getAttr(root, BidFileConstant.OldFileNode.PRO_TYPE);
        //SGFB 施工
        if ("SGFB".equalsIgnoreCase(protype)) {
            tenderProjectClassifyCode = "A08";
        }
        //SJFB 设计
        if ("SJFB".equalsIgnoreCase(protype)) {
            tenderProjectClassifyCode = "A04";
        }
        //KCFB 勘察
        if ("KCFB".equalsIgnoreCase(protype)) {
            tenderProjectClassifyCode = "A03";
        }
        //ZGFB 资格审查
        if ("ZGFB".equalsIgnoreCase(protype)) {
            tenderProjectClassifyCode = "A10";
        }
        //JLFB 监理
        if ("JLFB".equalsIgnoreCase(protype)) {
            tenderProjectClassifyCode = "A05";
        }
        //DTFB 电梯
        if ("DTFB".equalsIgnoreCase(protype)) {
            tenderProjectClassifyCode = "A11";
        }

        return tenderProjectClassifyCode;
    }

    /**
     * 获取XML元素属性
     *
     * @param root
     * @param name
     * @return
     */
    private static String getAttr(Element root, String name) {
        Element ele = (Element) root.selectSingleNode("./" + name);
        if (ele == null) {
            return null;
        }
        return ele.attributeValue("value").trim();
    }
}
