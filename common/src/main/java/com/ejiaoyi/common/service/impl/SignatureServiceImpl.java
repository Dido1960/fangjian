package com.ejiaoyi.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.constant.ReportPath;
import com.ejiaoyi.common.dto.ExpertSignatureDTO;
import com.ejiaoyi.common.dto.KeyWordRuleDTO;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.BidSectionRelate;
import com.ejiaoyi.common.entity.ExpertUser;
import com.ejiaoyi.common.entity.Fdfs;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.enums.FileType;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.FileUtil;
import com.ejiaoyi.common.util.SignatureUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangshaoyi
 * 证书管理 逻辑层
 */
@Service
public class SignatureServiceImpl extends BaseServiceImpl implements ISignatureService {

    @Autowired
    IExpertUserService expertUserService;

    @Autowired
    IBidSectionService bidSectionService;

    @Autowired
    IFDFSService fdfsService;

    @Autowired
    IBidSectionRelateService bidSectionRelateService;


    @Override
    public Map<String, Object> getPdfSignInfo(Integer bidSectionId, Integer expertUserId, Integer index) {
        Map<String, Object> map = new HashMap<>();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        // 获取专家信息
        List<ExpertUser> experts = expertUserService.listExpertsByBidSectionId(bidSectionId);

        //将专家数组排序：专家组长放在第一位，其他按照id升序排序
        experts.sort((o1, o2) -> {
            boolean equals1 = "1".equals(o1.getIsChairman());
            boolean equals2 = "1".equals(o2.getIsChairman());
            if (equals1 && !equals2) {
                return -1;
            } else if (!equals1 && equals2) {
                return 1;
            } else {
                return o1.getId() - o2.getId();
            }
        });

        boolean doneFinish = true;
        for (int i = 0; i < experts.size(); i++) {
            //判断当前专家是否签字
            if (CommonUtil.isNull(experts.get(i).getSignar()) || 0 == experts.get(i).getSignar()) {
                index = i;
                doneFinish = false;
                break;
            }
        }
        //签名结束
        if (doneFinish) {
            map.put("s", "n");
            return map;
        }
        // 当前专家还未签字
        if (index < experts.size()) {
            ExpertUser expert = experts.get(index);
            Map<String, String> expertMap = new HashMap<>();
            expertMap.put("name", expert.getExpertName());
            expertMap.put("idcard", CommonUtil.isNull(expert.getIdCard()) ? expert.getPhoneNumber() : expert.getIdCard());
            // 将专家的姓名和身份证号转成json字符串
            String expertJson = JSON.toJSONString(expertMap);

            String path = "";
            // 保存签名位置信息的xml文件路径
            path = ReportPath.QM_CONFIG_FILE;
            // 关于签名位置相关信息json字符串(并将签名信息对象保存到文件中)
            String positionJson = this.getPositionJson(path, index, expert.getExpertName(), expert.getId(), bidSection);
            map.put("s", "y");
            // 描述当前专家的姓名和身份证号信息的json字符串
            map.put("expertJson", expertJson);
            // 描述当前索引对应专家签名的位置信息
            map.put("positionJson", positionJson);
            map.put("expertId", expert.getId());
            if (index + 1 < experts.size()) {
                map.put("nextExpertName", experts.get(index + 1).getExpertName());
            }
        } else {
            map.put("s", "n");
        }

        return map;
    }


    @Override
    public Boolean expertSigarEnd(Integer bidSectionId) {
        List<ExpertUser> experts = expertUserService.listExpertUserSigin(bidSectionId);
        boolean doneFinish = true;
        for (ExpertUser temp : experts) {
            if (CommonUtil.isNull(temp.getSignar()) || 0 == temp.getSignar()) {
                doneFinish = false;
                break;
            }
        }
        return doneFinish;
    }

    @Override
    public String getPositionJson(String path, Integer index, String expertName, Integer expertId, BidSection bidSection) {
        try {
            //专家签名信息
            ExpertSignatureDTO expertSignar = new ExpertSignatureDTO();
            expertSignar.setExpertId(expertId);
            expertSignar.setExpertName(expertName);
            //当前专家在存储所有专家的list经过排序后所处索引
            expertSignar.setExpertIndex(index);
            List<KeyWordRuleDTO> keyWordRules = new ArrayList<>();

            File xmlFile = new File(path);
            //读取xml文件
            Document xml = new SAXReader().read(xmlFile);
            Element root = xml.getRootElement();

            List<Element> positions = new ArrayList<>();
            List<Element> validSignaturePositionEles = root.selectNodes("./rule[@type='validSignature']/area/position[@index='" + index + "']");
            List<Element> normalPositionEles = root.selectNodes("./rule[@type='normal']/area/position[@index='" + index + "']");
            List<Element> personPositionEles = root.selectNodes("./rule[@type='person']/area/position");


            positions.addAll(validSignaturePositionEles);
            positions.addAll(normalPositionEles);
            positions.addAll(personPositionEles);

            List<Map<String, String>> picPositionList = new ArrayList<>();
            List<Map<String, String>> signaturePositionList = new ArrayList<>();
            ExpertUser expert = expertUserService.getExpertUserById(expertId);
            for (Element position : positions) {
                KeyWordRuleDTO keyWordRule = new KeyWordRuleDTO();
                Element ruleEle = position.getParent().getParent();
                Element areaEle = position.getParent();

                String type = ruleEle.attributeValue("type");
                Map<String, String> signatureMap = new HashMap<>();
                Map<String, String> picMap = new HashMap<>();

                String expertRole = ruleEle.attributeValue("expertRole");

//                 bjCA签名
                if ("validSignature".equals(type)) {
                    signatureMap.put("keyword", ruleEle.attributeValue("keyword"));
                    signatureMap.put("index", areaEle.attributeValue("index"));
                    signatureMap.put("x", position.attributeValue("x"));
                    signatureMap.put("y", position.attributeValue("y"));
                    signatureMap.put("width", position.attributeValue("width"));
                    signatureMap.put("height", position.attributeValue("height"));
                    signaturePositionList.add(signatureMap);
                } else {
                    if ("normal".equals(type)) {
                        picMap.put("keyword", ruleEle.attributeValue("keyword"));
                    } else {
                        // 专家单人签名区
                        picMap.put("keyword", ruleEle.attributeValue("keyword").replaceAll("@@scqskj-name@", expertName));
                    }
                    picMap.put("index", areaEle.attributeValue("index"));
                    picMap.put("x", position.attributeValue("x"));
                    picMap.put("y", position.attributeValue("y"));
                    picMap.put("width", position.attributeValue("width"));
                    picMap.put("height", position.attributeValue("height"));
                    picPositionList.add(picMap);

                    keyWordRule.setKeyWord(picMap.get("keyword"));
                    keyWordRule.setIndex(picMap.get("index"));
                    keyWordRule.setX(picMap.get("x"));
                    keyWordRule.setY(picMap.get("y"));
                    keyWordRule.setWidth(picMap.get("width"));
                    keyWordRule.setHeight(picMap.get("height"));
                    keyWordRules.add(keyWordRule);
                }
            }
            expertSignar.setKeyWordRules(keyWordRules);
            //将保存签名信息保存到文件中
            FileUtil.bufferedWrite(ReportPath.REPORT_TEMPORARY_QM + bidSection.getId() + File.separator + expertId + ".txt", JSON.toJSONString(expertSignar));
            return JSON.toJSONString(signaturePositionList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @UserLog(value = "'手写板专家签名: expertId='+#expertId+', json='+#json+', expertSigarImage='+#expertSigarImage", dmlType = DMLType.UNKNOWN)
    public Boolean saveJsonReport(String json, Integer expertId, Integer bidSectionId, String expertSigarImage) throws Exception {
        // 查询当前专家
        ExpertUser bidExpert = expertUserService.getExpertUserById(expertId);
        // 设置当前专家已完成签名
        bidExpert.setSignar(1);
        expertUserService.updateExpertById(bidExpert);
        String signarTxt = ReportPath.REPORT_TEMPORARY_QZ + bidSectionId + File.separator + expertId + ".txt";
        // 将业务数据加密字符串存入“signature”文件夹下
        FileUtil.writeFile(json.getBytes(), signarTxt);
        // 读取“expertSigar”文件下的当前专家对应的签名信息
        String jsonStri = FileUtil.readTxtFile(ReportPath.REPORT_TEMPORARY_QM + bidSectionId + File.separator + expertId + ".txt");
        // 将json字符串转换成对应点的Java对象
        ExpertSignatureDTO expertSignar = JSON.parseObject(jsonStri, ExpertSignatureDTO.class);
        // 对象写入签名图片base64
        expertSignar.setImageBase64(expertSigarImage);
        // 重新将对象写入文件
        FileUtil.bufferedWrite(ReportPath.REPORT_TEMPORARY_QM + bidSectionId + File.separator + expertId + ".txt", JSON.toJSONString(expertSignar));
        return true;
    }


    @Override
    @UserLog(value = "'手写板签名PDF签章合成: bidSectionId='+#bidSectionId", dmlType = DMLType.UPDATE)
    public Boolean signrPdf(Integer bidSectionId) throws Exception {
        //得到该标段保存专家签名相关信息的文件夹路径
        String signaturePath = ReportPath.REPORT_TEMPORARY_QZ + bidSectionId + File.separator;
        //得到该标段保存专家签名，指纹，笔迹等加密相关信息的文件夹路径
        String expertSigarPath = ReportPath.REPORT_TEMPORARY_QM + bidSectionId + File.separator;
        Fdfs fdfs;
        String inpath;
        String outpath;
        //判断该标段的所有评标专家是否都签名结束
        if (expertSigarEnd(bidSectionId)) {
            try {
                //1. 合成评标报告中的签名或签字部分
                String fileName = ProjectFileTypeConstant.EVAL_REPORT + "." + FileType.PDF.getSuffix();
                String reportMark = File.separator + ProjectFileTypeConstant.EVAL_REPORT + File.separator + bidSectionId + File.separator + fileName;

                String url = fdfsService.getUrlByMark(reportMark);
                //下载文件
                inpath = ReportPath.REPORT_TEMPORARY_TEMP + bidSectionId + File.separator + "bidEvaluationReport2.pdf";
                byte[] bytes = fdfsService.downloadByUrl(url);
                FileUtil.writeFile(bytes, inpath);
                outpath = ReportPath.REPORT_TEMPORARY_TEMP + bidSectionId + File.separator + "bidEvaluationReport1.pdf";
                File[] signatureFile = new File(signaturePath).listFiles();
                File[] expertSigars = new File(expertSigarPath).listFiles();

                // 合成专家签字部分（直接将专家签字图片放入PDF中）
                SignatureUtil.signarSingleFileSigarPic(inpath, outpath, expertSigars);
                // 合成专家签名部分(签名含有专家的签字轨迹和指纹等加密信息)
                boolean res = SignatureUtil.signarSingleFile(outpath, outpath, signatureFile);

                FileUtil.copyFile(outpath, inpath);

                //将新的文件上传fdfs
                Fdfs fdfs1 = fdfsService.upload(new File(outpath), reportMark);
                //更新标段关联表评标报告id
                BidSectionRelate bidSectionRelate = new BidSectionRelate();
                bidSectionRelate.setBidSectionId(bidSectionId);
                bidSectionRelate.setEvaluationReportId(fdfs1.getId());
                bidSectionRelateService.updateRelateBySectionId(bidSectionRelate);
                //删除临时文件
                FileUtil.removeDir(new File(ReportPath.REPORT_TEMPORARY_TEMP));
                /*FileUtil.removeDir(new File(ReportPath.REPORT_TEMPORARY_QM));
                FileUtil.removeDir(new File(ReportPath.REPORT_TEMPORARY_QZ));*/

                return res;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void updateExpertSigarStatus(Integer bidSectionId) {
        List<ExpertUser> expertUsers = expertUserService.listExpertUserSigin(bidSectionId);
        for (ExpertUser expertUser : expertUsers) {
            expertUserService.updateExpertById(ExpertUser.builder()
                    .id(expertUser.getId())
                    .signar(0)
                    .build());
        }
        // 1、删除签名文件
        String expertSigarDir = ReportPath.REPORT_TEMPORARY_QM + bidSectionId;
        // 2、删除签名数据
        String signatureDir = ReportPath.REPORT_TEMPORARY_QZ + bidSectionId;
        FileUtil.removeDir(new File(expertSigarDir));
        FileUtil.removeDir(new File(signatureDir));
    }

//    @Override
//    public void updateExpertSigarStatus(String[] expertUserIds) {
//        for (String expertUserId : expertUserIds) {
//            if (!CommonUtil.isEmpty(expertUserId)){
//                Integer expertId = Integer.valueOf(expertUserId);
//                ExpertUser expertUser = expertUserService.getExpertUserById(expertId);
//                if (!CommonUtil.isEmpty(expertUser)){
//                    expertUserService.updateExpertById(ExpertUser.builder()
//                            .id(expertId)
//                            .signar(0)
//                            .build());
//
//                    // 1、删除签名文件
//                    String expertSigarDir = ReportPath.REPORT_TEMPORARY_QM+expertUser.getBidSectionId()+File.separator+expertId+".txt";
//                    // 2、删除签名数据
//                    String signatureDir = ReportPath.REPORT_TEMPORARY_QZ+expertUser.getBidSectionId()+File.separator+expertId+".txt";
//                    FileUtil.removeDir(new File(expertSigarDir));
//                    FileUtil.removeDir(new File(signatureDir));
//                }
//            }
//        }
//
//    }

}
