package com.ejiaoyi.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.dto.MsgHistoryDTO;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.Fdfs;
import com.ejiaoyi.common.entity.LineMsg;
import com.ejiaoyi.common.service.IBidSectionService;
import com.ejiaoyi.common.service.IBsnChainInfoService;
import com.ejiaoyi.common.service.IFDFSService;
import com.ejiaoyi.common.service.IMsgExportService;
import com.ejiaoyi.common.util.FileUtil;
import com.ejiaoyi.common.util.PDFUtil;
import com.ejiaoyi.common.util.ThreadUtlis;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2021-1-20 20:56
 */
@Service
public class MsgExportServiceImpl implements IMsgExportService {

    @Autowired
    private IBidSectionService bidSectionService;
    @Autowired
    private IFDFSService fdfsService;
    @Autowired
    private IBsnChainInfoService bsnChainInfoService;

    @Override
    public JsonData msgPdfSynthesis(Integer bidSectionId, String data) {
        JsonData result = new JsonData();

        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        data = StringEscapeUtils.unescapeHtml4(data);
        List<MsgHistoryDTO> list = JSON.parseArray(data, MsgHistoryDTO.class);

        OutputStream out = null;
        FileInputStream in = null;
        String uuid = UUID.randomUUID().toString();
        String ftlPath = FileUtil.getProjectResourcePath() + File.separator + "ftl" + File.separator + "msgExport" + File.separator + "msgBoxExport.ftl";
        String outPdfPath = FileUtil.getEvalReportFilePath(String.valueOf(bidSectionId)) + File.separator + uuid + ".pdf";
        //构造FastDfs 文件Mark
        String mark = File.separator + ProjectFileTypeConstant.MSG_EXPORT +
                File.separator + bidSectionId + File.separator + "msgBoxExport" + ".pdf";

        try {
            Map<String, Object> pdfData = new HashMap<>();
            pdfData.put("bidSection", bidSection);
            pdfData.put("list", list);

            PDFUtil.generatePdf(ftlPath, outPdfPath, pdfData, false);

            //上传Fdfs
            Fdfs fdfs = fdfsService.upload(new File(outPdfPath), mark);

            //上传区块
            ThreadUtlis.run(() -> bsnChainInfoService.msgExportBsnChainPut(bidSectionId, fdfs.getFileHash()));

            result.setCode("1");
            result.setMsg("开标消息记录.pdf");
            result.setData(fdfs.getUrl());

        } catch (Exception e) {
            e.printStackTrace();
            result.setCode("2");
            result.setMsg("消息记录导出失败！");
        } finally {
            FileUtil.deleteFile(outPdfPath);
        }
        return result;
    }
}
