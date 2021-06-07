package com.ejiaoyi.common.constant;

import com.ejiaoyi.common.util.FileUtil;

import java.io.File;

public class ReportPath {
    //报告临时文件地址（签名）
    public static final String REPORT_TEMPORARY_QM = FileUtil.getCustomFilePath()  + "expertSigar" + File.separator;

    //报告临时文件地址（签字）
    public static final String REPORT_TEMPORARY_QZ = FileUtil.getCustomFilePath()  + "signature" + File.separator;

    //report.xml
    public static final String QM_CONFIG_FILE = FileUtil.getProjectResourcePath() + "sign" + File.separator + "reportV.xml";

    //临时下载报告地址
    public static final String REPORT_TEMPORARY_TEMP = FileUtil.getCustomFilePath() + "temp" + File.separator;
}
