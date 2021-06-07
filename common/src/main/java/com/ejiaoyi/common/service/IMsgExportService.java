package com.ejiaoyi.common.service;

import com.ejiaoyi.common.dto.JsonData;

/**
 * @Description: 消息导出 合成PDF 接口
 * @Auther: liuguoqiang
 * @Date: 2021-1-20 20:56
 */
public interface IMsgExportService {
    /**
     * 消息导出 合成pdf
     * @param bidSectionId 标段ID
     * @param data 合成数据
     * @return 下载 文件名 及 路径
     */
    JsonData msgPdfSynthesis(Integer bidSectionId, String data);
}
