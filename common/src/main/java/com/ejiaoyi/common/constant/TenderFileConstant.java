package com.ejiaoyi.common.constant;

import java.io.File;

/**
 * 投标文件结构
 *
 * @author Mike
 * @date 2021-04-06 14:57
 */
public interface TenderFileConstant {

    /**
     * 标书相关内容 xml
     */
    String TENDER_RELATE_CONTENT = File.separator + "resources" + File.separator + "Config.xml";

    /**
     * 投标信息 xml
     */
    String TENDER_INFO = File.separator + "resources" + File.separator + "BidInfoTemplate.xml";

    /**
     * 纸质标投标文件 pdf
     */
    String PAPER_BID_FILE = File.separator + "resources" + File.separator + "BidFile.pdf";

    /**
     * 投标文件评审点存储文件
     */
    String JUDGING_POINT_XML = File.separator + "resources" + File.separator + "BidEvaluationMethodTemplate.xml";

    /**
     * 商务标 pdf
     */
    String BUSINESS_FILE = File.separator + "resources" + File.separator + "TempConvert" + File.separator + "BStemp.pdf";

    /**
     * 技术标 pdf
     */
    String TECHNICAL_FILE = File.separator + "resources" + File.separator + "TempConvert" + File.separator + "TEtemp.pdf";

    /**
     * 资格证明 pdf
     */
    String QUALIFICATION_FILE = File.separator + "resources" + File.separator + "TempConvert" + File.separator + "QUtemp.pdf";

    /**
     * 工程量清单 xml(施工类特有)
     */
    String ENGINEER_QUANTITY_LIST_XML = File.separator + "resources" + File.separator + "Quantities" + File.separator + "quantities.xml";

    /**
     * 工程量清单 pdf(施工类特有)
     */
    String ENGINEER_QUANTITY_LIST_PDF = File.separator + "resources" + File.separator + "Quantities" + File.separator + "quantities.pdf";

    interface OldFileName{
        /**
         * 标书相关内容 xml
         */
        String TENDER_RELATE_CONTENT = "bidDescription.xml";

        /**
         * 投标信息 xml
         */
        String TENDER_INFO = "tbxx.xml";

        /**
         * 商务标 pdf
         */
        String BUSINESS_FILE = "swb.pdf";

        /**
         * 技术标 pdf
         */
        String TECHNICAL_FILE = "jsb.pdf";

        /**
         * 资格证明 pdf
         */
        String QUALIFICATION_FILE = "zgzm.pdf";

        /**
         * 商务标 pdf带签章
         */
        String BUSINESS_FILE_SIGAR = "swb_sigar.pdf";

        /**
         * 技术标 pdf带签章
         */
        String TECHNICAL_FILE_SIGAR = "jsb_sigar.pdf";

        /**
         * 资格证明 pdf带签章
         */
        String QUALIFICATION_FILE_SIGAR = "zgzm_sigar.pdf";

        /**
         * 工程量清单 xml(施工类特有)
         */
        String ENGINEER_QUANTITY_LIST_XML = "gclqd.xml";

        /**
         * 工程量清单 pdf(施工类特有)
         */
        String ENGINEER_QUANTITY_LIST_PDF = "gclqd.pdf";

        /**
         * 工程量清单 pdf(施工类特有)带签章
         */
        String ENGINEER_QUANTITY_LIST_PDF_SIGAR = "gclqd_sigar.pdf";
    }

    interface OldFileNode{
        /**
         * 项目编号
         */
        String PROJECT_CODE = "ProjectCode";

        /**
         * 项目名称
         */
        String PROJECT_NAME = "ProjectName";

        /**
         * 标段编号
         */
        String BID_SECTION_CODE = "SectCode";
        /**
         * 标段名称
         */
        String BID_SECTION_NAME = "SectName";

        /**
         * 投标人名称
         */
        String BIDDER_NAME = "Bidder";

        /**
         * 投标人统一社会信用代码
         */
        String BIDDER_CO_CODE = "BidderCoCode";

        /**
         * 法定代表人或其委托代理人
         */
        String LEGAL_AGENT = "legalagent";

        /**
         * 投标报价
         */
        String BID_PRICE = "TotalQuote";

        /**
         * 工期
         */
        String TOTAL_TIME_LIMIT = "TotalTimeLimit";

        /**
         * 工程质量信息
         */
        String QUALITY = "Qualityinformation";

        /**
         * 费率
         */
        String RATE = "Rate";
    }
}
