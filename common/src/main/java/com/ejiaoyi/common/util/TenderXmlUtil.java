package com.ejiaoyi.common.util;

import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.common.constant.BidInfoTemplateConstant;
import com.ejiaoyi.common.entity.ProjectCostXml;
import com.ejiaoyi.common.enums.BidProtype;
import com.ejiaoyi.common.exception.CustomException;
import jodd.util.StringUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 投标xml文件工具类
 *
 * @author Make
 * @since 2020/7/9
 */
public class TenderXmlUtil {
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


    /**
     * 解析投标文件
     *
     * @param xmlPath 标段相关信息xml地址
     * @return 解析后封装的map
     */
    public static Map<String, String> parseTenderRelateInfo(String xmlPath) throws Exception {
        Map<String, String> dataMap = new HashMap<String, String>();

        File xmlFile = new File(xmlPath);
        // 描述文件不存在的时候，就不解析
        if (!xmlFile.exists()) {
            return null;
        }

        Document xml = new SAXReader().read(xmlFile);

        // 描述文件
        Element root = (Element) xml.selectSingleNode("/root");
        Element protypeNode = root.element("tenderType");

        // 工程属性
        String protype = protypeNode.attributeValue("value");

        String bidClassifyCode = null;

        // 施工
        if (BidProtype.CONSTRUCTION.getName().equalsIgnoreCase(protype)) {
            bidClassifyCode = BidProtype.CONSTRUCTION.getCode();
        }

        // 设计
        if (BidProtype.DESIGN.getName().equalsIgnoreCase(protype)) {
            bidClassifyCode = BidProtype.DESIGN.getCode();
        }
        // 勘察
        if (BidProtype.INVESTIGATION.getName().equalsIgnoreCase(protype)) {
            bidClassifyCode = BidProtype.INVESTIGATION.getCode();
        }
        // 资格预审
        if (BidProtype.QUALIFICATION.getName().equalsIgnoreCase(protype)) {
            bidClassifyCode = BidProtype.QUALIFICATION.getCode();
        }
        // 监理
        if (BidProtype.SUPERVISION.getName().equalsIgnoreCase(protype)) {
            bidClassifyCode = BidProtype.SUPERVISION.getCode();
        }
        // 电梯采购与安装
        if (BidProtype.ELEVATOR.getName().equalsIgnoreCase(protype)) {
            bidClassifyCode = BidProtype.ELEVATOR.getCode();
        }
        // 电梯采购与安装
        if (BidProtype.EPC.getName().equalsIgnoreCase(protype)) {
            bidClassifyCode = BidProtype.EPC.getCode();
        }

        if ("EPCQualification".equals(protype)){
            bidClassifyCode = BidProtype.QUALIFICATION.getCode();
        }

        dataMap.put("bidClassifyCode", bidClassifyCode);
        return dataMap;
    }

    /**
     * 解析投标项目详细信息
     *
     * @param xmlPath xml路径
     * @param bidProtype 标段类型
     * @return 解析后封装的map
     * @throws
     */
    public static Map<String, String> parseTenderDetailInfo(String xmlPath, BidProtype bidProtype) throws Exception {
        File xmlFile = new File(xmlPath);
        if (!xmlFile.exists()) {
            return null;
        }

        Map<String, String> dataMap = new HashMap<String, String>();
        SAXReader reader = new SAXReader();
        Document ducument = reader.read(xmlFile);
        Element root = (Element) ducument.selectSingleNode("/root");

        // 项目编号
        String projectCode;
        // 项目名称
        String projectName;
        // 标段名称
        String bidSectionName;
        // 标段编码
        String bidSectionCode;
        // 招标人
        String tendererName;
        // 招标代理名称
        String agencyName;
        // 投标人
        String bidderName;
        // 投标人统一社会信用代码
        String bidderCoCode;
        // 法定代表人或其委托代理人
        String legalAgent;
        // 投标报价
        String totalQuote;
        // 投标报价类型
        String quoteType;
        // 工期
        String totalTimeLimit;
        // 工程质量信息
        String qualityInformation;
        //费率
        String rate;

        switch (bidProtype) {
            case CONSTRUCTION:
            case DESIGN:
            case INVESTIGATION:
            case QUALIFICATION:
            case SUPERVISION:
            case ELEVATOR:
            case EPC:
                projectCode = getAttr(root, BidInfoTemplateConstant.PROJECT_CODE);
                projectName = getAttr(root, BidInfoTemplateConstant.PROJECT_NAME);
                bidSectionCode = getAttr(root, BidInfoTemplateConstant.SECT_CODE);
                bidSectionName = getAttr(root, BidInfoTemplateConstant.SECT_NAME);
                tendererName = getAttr(root, BidInfoTemplateConstant.CO_NAME_INV);
                agencyName = getAttr(root, BidInfoTemplateConstant.IPB_NAME);
                bidderName = getAttr(root,  BidInfoTemplateConstant.BIDDER);
                bidderCoCode = getAttr(root,  BidInfoTemplateConstant.BIDDER_CO_CODE);
                legalAgent = getAttr(root,  BidInfoTemplateConstant.LEGAL_AGENT);
                quoteType = getAttr(root,  BidInfoTemplateConstant.QUOTE_TYPE);
                totalQuote = getAttr(root,  BidInfoTemplateConstant.TOTAL_QUOTE);
                totalTimeLimit  = getAttr(root,  BidInfoTemplateConstant.TOTAL_TIME_LIMIT);
                qualityInformation  = getAttr(root,  BidInfoTemplateConstant.QUALITY_INFORMATION);
                rate = getAttr(root, BidInfoTemplateConstant.RATE);

                dataMap.put(BidInfoTemplateConstant.PROJECT_CODE, projectCode);
                dataMap.put(BidInfoTemplateConstant.PROJECT_NAME, projectName);
                dataMap.put(BidInfoTemplateConstant.SECT_CODE, bidSectionCode);
                dataMap.put(BidInfoTemplateConstant.SECT_NAME, bidSectionName);
                dataMap.put(BidInfoTemplateConstant.CO_NAME_INV, tendererName);
                dataMap.put(BidInfoTemplateConstant.IPB_NAME, agencyName);
                dataMap.put(BidInfoTemplateConstant.BIDDER, bidderName);
                dataMap.put(BidInfoTemplateConstant.BIDDER_CO_CODE, bidderCoCode);
                dataMap.put(BidInfoTemplateConstant.LEGAL_AGENT, legalAgent);
                dataMap.put(BidInfoTemplateConstant.TOTAL_QUOTE, totalQuote);
                dataMap.put(BidInfoTemplateConstant.QUOTE_TYPE, quoteType);
                dataMap.put(BidInfoTemplateConstant.TOTAL_TIME_LIMIT, totalTimeLimit);
                dataMap.put(BidInfoTemplateConstant.QUALITY_INFORMATION, qualityInformation);
                dataMap.put(BidInfoTemplateConstant.RATE, rate);
                break;
            default:
                break;
        }

        return dataMap;
    }

    /**
     * 工程量清单解析 获取关键信息
     *
     * @param xmlPath 本地文件地址
     * @return 关键信息
     * @throws Exception
     */
    public static String parseQuantityXmlByFilePath(String xmlPath) throws Exception {
        // 校验清单合法性
        Resource xsdResource = new ClassPathResource("com/ejiaoyi/common/resource/gclqd.xsd");
        InputStream xsdIs = xsdResource.getInputStream();
        Resource xsdForJRResource = new ClassPathResource("com/ejiaoyi/common/resource/gclqd_jr.xsd");
        InputStream xsdForJRIs = xsdForJRResource.getInputStream();

        boolean validXML = XSDUtil.validXML(xsdIs, xmlPath);
        boolean validXMLForJR = XSDUtil.validXML(xsdForJRIs, xmlPath);

        if (!validXML && !validXMLForJR) {
            throw new CustomException("工程量清单不符合甘肃省工程量清单标准!");
        }

        File xmlFile = new File(xmlPath);
        //工程量清单不存在的时候，就不解析
        if (!xmlFile.exists()) {
            throw new CustomException("工程量清单丢失, 解析失败!");
        }

        // 创建返回数据 工程数据造价文件 所有工程量清单数据 进行封装到工程数据造价文件信息中
        Document xml;
        String bidPrice;
        SAXReader saxReader;
        try {
            saxReader = new SAXReader();
            xml = saxReader.read(xmlFile);
            // 总工程
            Element totalProject = (Element) xml.selectSingleNode("/工程造价数据文件/总工程");
            bidPrice = totalProject.attributeValue("金额");
            return bidPrice;
        } catch (Exception e) {
            throw new CustomException("工程量清单异常,解析失败!");
        }
    }



}
