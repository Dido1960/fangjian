package com.ejiaoyi.common.constant;

import java.io.File;

/**
 * 招标文件结构
 *
 * @author Mike
 * @date 2021-04-06 14:57
 */
public interface BidFileConstant {

    /**
     * 标书相关内容 xml
     */
    String CONFIG_XML = File.separator + "resources" + File.separator + "Config.xml";

    /**
     * 招标文件正文 pdf
     */
    String TENDER_DOC = File.separator + "resources" + File.separator + "TempConvert" + File.separator + "temp.pdf";

    /**
     * 招标项目信息 xml
     */
    String TENDER_INFO = File.separator + "resources" + File.separator + "TenderInfoTemplate.xml";

    /**
     * 评标办法 xml
     */
    String BID_EVALUATION_METHOD= File.separator + "resources" + File.separator + "BidEvaluationMethodTemplate.xml";

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
        String CONFIG_XML = "bidDescription.xml";

        /**
         * 招标文件正文 pdf
         */
        String TENDER_DOC = "zbwj.pdf";

        /**
         * 招标文件正文 pdf带签章
         */
        String TENDER_DOC_SIGAR = "zbwj_sigar.pdf";

        /**
         * 招标项目信息 xml
         */
        String TENDER_INFO = "zbxx.xml";

        /**
         * 评标办法 xml
         */
        String BID_EVALUATION_METHOD = "pbfs.xml";

        /**
         * 工程量清单 xml(施工类特有)
         */
        String ENGINEER_QUANTITY_LIST_XML = "gclqd.xml";

        /**
         * 工程量清单 pdf(施工类特有)
         */
        String ENGINEER_QUANTITY_LIST_PDF = "listPdf.pdf";

        /**
         * 工程量清单 pdf(施工类特有)带签章
         */
        String ENGINEER_QUANTITY_LIST_PDF_SIGAR = "listPdf_sigar.pdf";
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
         * 招标项目编号
         */
        String TENDER_PROJECT_CODE = "TenderProjectCode";
        /**
         * 招标项目名称
         */
        String TENDER_PROJECT_NAME = "TenderProjectName";
        /**
         * 标段编号
         */
        String BID_SECTION_CODE = "SectCode";
        /**
         * 标段名称
         */
        String BID_SECTION_NAME = "SectName";
        /**
         * 文件递交时间
         */
        String BID_DOC_REFER_END_TIME = "BidDocReferEndTime";
        /**
         * 开标时间
         */
        String BID_OPEN_TIME = "BidOpenTime";
        /**
         * 招标人名称
         */
        String TENDERER_NAME = "CoName_Inv";
        /**
         * 招标代理名称
         */
        String AGENCY_NAME = "IPBName";
        /**
         * 评标委员会人数
         */
        String EXPERT_COUNT = "Expert";
        /**
         * 业主代表人数
         */
        String REPRESENTATIVE_COUNT = "TenderRepre";
        /**
         * 标段类型
         */
        String PRO_TYPE = "protype";
    }
}
