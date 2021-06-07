package com.ejiaoyi.common.util;

import com.ejiaoyi.common.constant.BidDetailConstant;
import com.ejiaoyi.common.constant.BidRelateConstant;
import com.ejiaoyi.common.enums.BidProtype;
import com.ejiaoyi.common.exception.CustomException;
import jodd.util.StringUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 招标xml文件工具类
 *
 * @author Make
 * @since 2020/7/9
 */
public class BidXmlUtil {

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
     * 全角字符转半角字符
     *
     * @param fullWidthStr
     * @return
     */
    private static String fullWidth2halfWidth(String fullWidthStr) {
        if (StringUtil.isEmpty(fullWidthStr)) {
            return null;
        }

        char[] charArray = fullWidthStr.toCharArray();
        //对全角字符转换的char数组遍历
        for (int i = 0; i < charArray.length; ++i) {
            int charIntValue = (int) charArray[i];
            //如果符合转换关系,将对应下标之间减掉偏移量65248;如果是空格的话,直接做转换
            if (charIntValue >= 65281 && charIntValue <= 65374) {
                charArray[i] = (char) (charIntValue - 65248);
            } else if (charIntValue == 12288) {
                charArray[i] = (char) 32;
            }
        }
        return new String(charArray);
    }

    /**
     * 解析标段相关信息
     *
     * @param xmlPath 标段相关信息xml地址
     * @return 解析后封装的map
     */
    public static Map<String, String> parseBidRelateInfo(String xmlPath) throws Exception {
        Map<String, String> dataMap = new HashMap<>();

        File xmlFile = new File(xmlPath);
        // 描述文件不存在的时候，就不解析
        if (!xmlFile.exists()) {
            return null;
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(xmlPath);
            SAXReader reader = new SAXReader();
            Document xml = reader.read(fis);

            // 描述文件
            Element root = (Element) xml.selectSingleNode("/root");
            Element protypeNode = root.element(BidRelateConstant.TENDER_TYPE);
            Element serviceTypeNode = root.element(BidRelateConstant.SERVICE_TYPE);
            Element tenderCertIdNode = root.element(BidRelateConstant.TENDER_CERT_ID);
            Element bidCertIdNode = root.element(BidRelateConstant.BID_CERT_ID);

            // 工程属性
            String protype = protypeNode.attributeValue(BidRelateConstant.VALUE);

            // 服务类型
            String serviceType = serviceTypeNode.attributeValue(BidRelateConstant.VALUE);
            // 招标锁号
            String tenderCertId = tenderCertIdNode.attributeValue(BidRelateConstant.VALUE);
            // 投标锁号
            String bidCertId = bidCertIdNode.attributeValue(BidRelateConstant.VALUE);

            // 项目类型代码
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
            // 资格预审（施工总承包的资格预审(EPCQualification)和正常资格预审(Qualification)评审方式都是一致的,只要类型中包含资格预审即为资格预审）
            if (protype.contains(BidProtype.QUALIFICATION.getName())) {
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
            //施工总承包
            if (BidProtype.EPC.getName().equalsIgnoreCase(protype)){
                bidClassifyCode = BidProtype.EPC.getCode();
            }

            dataMap.put(BidRelateConstant.TENDER_TYPE, bidClassifyCode);
            dataMap.put(BidRelateConstant.SERVICE_TYPE, serviceType);
            dataMap.put(BidRelateConstant.TENDER_CERT_ID, tenderCertId);
            dataMap.put(BidRelateConstant.BID_CERT_ID, bidCertId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("文件解析失败");
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException("文件解析失败");
            }
        }
        return dataMap;
    }

    /**
     * 解析招标项目详细信息
     *
     * @param xmlPath xml路径
     * @param bidProtype 标段类型
     * @return 解析后封装的map
     * @throws
     */
    public static Map<String, String> parseBidDetailInfo(String xmlPath, BidProtype bidProtype) throws Exception {
        Map<String, String> dataMap = new HashMap<>();
        if (!new File(xmlPath).exists()) {
            return null;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(xmlPath);
            SAXReader reader = new SAXReader();
            Document ducument = reader.read(fis);
            Element root = (Element) ducument.selectSingleNode("/root");

            // 项目编号
            String projectCode;
            // 项目名称
            String projectName;
            // 招标项目编号
            String tenderProjectCode;
            // 招标项目编号
            String tenderProjectName;
            // 标段编号
            String bidSectionCode;
            // 标段名称
            String bidSectionName;
            // 投标文件截至时间
            String bidDocReferEndTime;
            // 开标时间
            String bidOpenTime;
            // 招标人
            String tendererName;
            // 招标代理名称
            String agencyName;
            // 招标代理统一社会信用代码
            String tenderAgencyCode;
            // 投标保证金金额
            String marginAmount;
            // 资格审查方式
            String qualType;
            // 招标方式
            String tenderMode;
            // 评标办法
            String evaluationMethod;
            // 评标委员会总人数
            String expertCount;
            // 业主代表人数
            String representativeCount;

            switch (bidProtype) {
                case CONSTRUCTION:
                    tenderProjectCode = getAttr(root, BidDetailConstant.TENDER_PROJECT_CODE);
                    tenderProjectName = getAttr(root, BidDetailConstant.TENDER_PROJECT_NAME);
                    marginAmount = getAttr(root, BidDetailConstant.MARGIN_AMOUNT);
                    qualType = getAttr(root, BidDetailConstant.QUALIFICATION_TYPE);

                    dataMap.put(BidDetailConstant.TENDER_PROJECT_CODE, tenderProjectCode);
                    dataMap.put(BidDetailConstant.TENDER_PROJECT_NAME, tenderProjectName);
                    dataMap.put(BidDetailConstant.MARGIN_AMOUNT, marginAmount);
                    dataMap.put(BidDetailConstant.QUALIFICATION_TYPE, qualType);

                case DESIGN:
                case INVESTIGATION:
                case QUALIFICATION:
                case SUPERVISION:
                case ELEVATOR:
                case EPC:
                    projectCode = getAttr(root, BidDetailConstant.PROJECT_CODE);
                    projectName = getAttr(root, BidDetailConstant.PROJECT_NAME);
                    bidSectionCode = getAttr(root, BidDetailConstant.SECT_CODE);
                    bidSectionName = getAttr(root, BidDetailConstant.SECT_NAME);
                    bidDocReferEndTime = getAttr(root, BidDetailConstant.BID_DOC_REFER_END_TIME);
                    bidOpenTime = getAttr(root, BidDetailConstant.BID_OPEN_TIME);
                    tendererName = getAttr(root, BidDetailConstant.CO_NAME_INV);
                    tenderAgencyCode = getAttr(root, BidDetailConstant.IPB_CODE);
                    agencyName = getAttr(root, BidDetailConstant.IPB_NAME);
                    expertCount = getAttr(root, BidDetailConstant.EXPERT).replaceAll("人", "");
                    representativeCount = getAttr(root, BidDetailConstant.TENDER_REPRE).replaceAll("人", "");

                    tenderMode = getAttr(root, BidDetailConstant.BID_METHOD);
                    evaluationMethod = getAttr(root, BidDetailConstant.BID_EVAL_METHOD);

                    dataMap.put(BidDetailConstant.PROJECT_CODE, projectCode);
                    dataMap.put(BidDetailConstant.PROJECT_NAME, projectName);
                    dataMap.put(BidDetailConstant.SECT_CODE, bidSectionCode);
                    dataMap.put(BidDetailConstant.SECT_NAME, bidSectionName);
                    dataMap.put(BidDetailConstant.BID_DOC_REFER_END_TIME, bidDocReferEndTime);
                    dataMap.put(BidDetailConstant.BID_OPEN_TIME, bidOpenTime);
                    dataMap.put(BidDetailConstant.CO_NAME_INV, tendererName);
                    dataMap.put(BidDetailConstant.IPB_NAME, agencyName);
                    dataMap.put(BidDetailConstant.IPB_CODE, tenderAgencyCode);
                    dataMap.put(BidDetailConstant.EXPERT, expertCount);
                    dataMap.put(BidDetailConstant.TENDER_REPRE, representativeCount);
                    dataMap.put(BidDetailConstant.BID_METHOD, tenderMode);
                    dataMap.put(BidDetailConstant.BID_EVAL_METHOD, evaluationMethod);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("文件解析失败！");
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException("文件解析失败");
            }
        }

        return dataMap;
    }

    /**
     * 通过xmlUrl路径，获取节点值,只获取根节点的子节点值
     * @param name
     * @return
     */
    public static String getAttrByBytes(byte[] bytes,String name){
        Element root = null;
        try {
            Document xml = new SAXReader().read(new ByteArrayInputStream(bytes));
            // 描述文件
            root = xml.getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (root!=null){
            Element element = root.element(name);
            if (element!=null){
                return element.getText();
            }
            return null;
        }
        return null;
    }

}
