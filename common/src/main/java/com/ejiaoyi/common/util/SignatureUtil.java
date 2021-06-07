package com.ejiaoyi.common.util;

import cn.org.bjca.anysign.components.bean.message.ExternalAnyWriteInfo;
import cn.org.bjca.anysign.components.bean.message.ExternalEncryptPackageInfo;
import cn.org.bjca.anysign.components.bean.message.MessageBodyReference;
import cn.org.bjca.common.model.DocumentExtType;
import cn.org.bjca.common.model.GeneratePDFInfo;
import cn.org.bjca.seal.esspdf.client.message.ChannelMessage;
import cn.org.bjca.seal.esspdf.client.tools.AnySignClientTool;
import cn.org.bjca.seal.esspdf.client.utils.ClientUtil;
import com.alibaba.fastjson.JSON;
import com.ejiaoyi.common.config.SignatureConfig;
import com.ejiaoyi.common.dto.ExpertSignatureDTO;
import com.ejiaoyi.common.dto.KeyWordRuleDTO;
import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 签名工具类
 * @author Make
 */
public class SignatureUtil {

    /**
     * 签名ip
     */
    static String signatureIp;

    /**
     * 签名端口
     */
    static String signaturePort;

    /**
     * 成功编码
     */
    private static String SUCCESS = "200";

    static {
        SignatureConfig signatureConfig=  ApplicationContextUtil.getApplicationContext().getBean(SignatureConfig.class);
        SignatureUtil.signatureIp = signatureConfig.getSignatureIp();
        SignatureUtil.signaturePort = signatureConfig.getSignaturePort();
    }

    /**
     * 读取保存签名信息的文件得到一个list
     *
     * @param expertSignatures 签名信息
     * @return
     */
    public static List<ExpertSignatureDTO> getExpertSignaturesByFile(File[] expertSignatures) {
        return expertSignatures(expertSignatures);
    }

    /**
     * 单文件多签名
     * @param filePath 文件路径
     * @param outPath 签名完成保存的路径
     * @param signatureFile 所有的专家字符串信息
     */
    public static boolean signatureSingleFile(String filePath, String outPath, File[] signatureFile) {
        try {
            AnySignClientTool anySignClientTool = new AnySignClientTool(signatureIp + ":" + signaturePort);
            List<ExternalAnyWriteInfo> externalAnyWriteInfoList = new ArrayList<>();
            // 添加PDF文件
            GeneratePDFInfo generatePDFInfo;
            List<GeneratePDFInfo> generatePDFInfoList = new ArrayList<>();
            List<ExternalEncryptPackageInfo> encryptPackageInfoList = new ArrayList<>();
            ExternalAnyWriteInfo externalAnyWriteInfo = null;
            externalAnyWriteInfo = new ExternalAnyWriteInfo();
            // 99999为测试信道
            // 40931586正式信道
            // externalAnyWriteInfo.setChannel("99999");
            externalAnyWriteInfo.setChannel("40931586");
            byte[] pdfBty = null;
            // PDF字节数组
            pdfBty = ClientUtil.readFileToByteArray(new File(filePath));
            generatePDFInfo = new GeneratePDFInfo();
            generatePDFInfo.setDocContent(pdfBty);
            generatePDFInfo.setDocumentExtType(DocumentExtType.PDF);
            generatePDFInfoList.add(generatePDFInfo);

            // 添加手写加密包
            byte[] encDataBty;
            ExternalEncryptPackageInfo encryptPackageInfo;
            for (File file : signatureFile) {
                // 加密包字节数组
                encDataBty = ClientUtil.readFileToByteArray(file);
                encryptPackageInfo = new ExternalEncryptPackageInfo(encDataBty);
                encryptPackageInfoList.add(encryptPackageInfo);
            }
            externalAnyWriteInfo.setEncryptPackageInfoList(encryptPackageInfoList);
            externalAnyWriteInfo.setGeneratePDFInfoList(generatePDFInfoList);
            externalAnyWriteInfoList.add(externalAnyWriteInfo);
            ChannelMessage message = anySignClientTool.anyWritePDFSignFacade(externalAnyWriteInfoList);
            if (SUCCESS.equals(message.getStatusCode())) {
                // 成功
                System.out.println("成功,PDF保存路径:" + outPath);
                List<MessageBodyReference> messageBodyReferenceList = message.getMessageBodyReferenceList();
                MessageBodyReference messageBodyReference;
                for (MessageBodyReference bodyReference : messageBodyReferenceList) {
                    messageBodyReference = bodyReference;
                    ClientUtil.writeByteArrayToFile(new File(outPath), messageBodyReference.getFileBty());
                }
                return true;
            } else {
                System.err.println("签名保存失败." + message.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("签名服务器异常：" + e.toString());
        }
        return false;
    }

    /**
     * 通过签名信息的文件合成签名PDF（为PDF添加签名图片）
     *
     * @param path 文件路径
     * @param outPath 签名完成保存的路径
     * @param expertSignaturesFile 签名信息文件
     * @throws Exception
     */
    public static void singleFileSignaturePic(String path, String outPath, File[] expertSignaturesFile) throws Exception {
        // 读取文件中专家签名信息，在将签名字符串转换成对应的签名对象并放入list中
        List<ExpertSignatureDTO> expertSignatures = expertSignatures(expertSignaturesFile);
        // 临时PDF路径
        String targetPath = new File(path).getParentFile().getPath() + "/temp/tempPdf.pdf";
        // 将inpath中PDF文件复制到临时PDF中
        FileUtil.copyFile(path, targetPath);
        // 获取PDF页码和对应的签字位置信息map
        HashMap<Integer, List<KeyWordRuleDTO>> map = getKeyWords(path, expertSignatures);

        // 读取模板文件
        InputStream input = new FileInputStream(new File(targetPath));
        PdfReader reader = new PdfReader(input);
        FileOutputStream fos = new FileOutputStream(outPath);
        PdfStamper stamper = new PdfStamper(reader, fos);
        int num = reader.getNumberOfPages();

        // 获取页码
        for (int pageIndex = 1; pageIndex <= num; pageIndex++) {
            if (map.get(pageIndex) == null) {
                continue;
            }
            List<KeyWordRuleDTO> keyWordRules = map.get(pageIndex);

            for (KeyWordRuleDTO keyWordRule : keyWordRules) {
                if ("validSignature".equals(keyWordRule.getKeyWord())) {
                    continue;
                }
                float imageW = Float.parseFloat(keyWordRule.getWidth());
                float imageH = Float.parseFloat(keyWordRule.getHeight());
                // 生成图片
                Image image = Image.getInstance(B64Utils.s_decode(keyWordRule.getImageBase64()));
                PdfContentByte under = stamper.getOverContent(pageIndex);
                // 根据域的大小缩放图片
                image.scaleToFit(imageW, imageH);
                float x = Float.parseFloat(keyWordRule.getX());
                float y = Float.parseFloat(keyWordRule.getY());
                // 添加图片
                image.setAbsolutePosition(x, y);
                under.addImage(image);
            }
        }

        stamper.close();
        input.close();
        reader.close();
        fos.close();
    }

    /**
     * 通过签名信息List合成签名PDF（为PDF添加签名图片）
     *
     * @param path 文件路径
     * @param outPath 签名完成保存的路径
     * @param expertSignatures 签名信息对象的list
     * @throws Exception
     */
    public static void singleFileSignaturePicByObj(String path, String outPath, List<ExpertSignatureDTO> expertSignatures) throws Exception {
        // 临时PDF路径
        String targetPath = new File(path).getParentFile().getPath() + "/temp/tempPdf2.pdf";
        // 将inpath中PDF文件复制到临时PDF中
        FileUtil.copyFile(path, targetPath);
        // 获取PDF页码和对应的签字位置信息map
        HashMap<Integer, List<KeyWordRuleDTO>> map = getKeyWords(path, expertSignatures);

        // 读取模板文件
        InputStream input = new FileInputStream(new File(targetPath));
        PdfReader reader = new PdfReader(input);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outPath));
        int num = reader.getNumberOfPages();

        // 获取页码
        for (int pageIndex = 1; pageIndex <= num; pageIndex++) {
            if (map.get(pageIndex) == null) {
                continue;
            }
            List<KeyWordRuleDTO> keyWordRules = map.get(pageIndex);

            for (KeyWordRuleDTO keyWordRule : keyWordRules) {
                float imageW = Float.parseFloat(keyWordRule.getWidth());
                float imageH = Float.parseFloat(keyWordRule.getHeight());
                // 生成图片
                Image image = Image.getInstance(B64Utils.s_decode(keyWordRule.getImageBase64()));
                PdfContentByte under = stamper.getOverContent(pageIndex);
                // 根据域的大小缩放图片
                image.scaleToFit(imageW, imageH);
                float x = Float.parseFloat(keyWordRule.getX());
                float y = Float.parseFloat(keyWordRule.getY());
                System.err.println("page" + pageIndex + "=" + x + ":" + y);
                // 添加图片
                image.setAbsolutePosition(x, y);
                under.addImage(image);
            }

        }

        stamper.close();

    }


    /**
     * 获取专家签名信息
     * @param expertSignatures 专家签名信息
     * @return
     */
    private static List<ExpertSignatureDTO> expertSignatures(File[] expertSignatures) {
        List<ExpertSignatureDTO> list = new ArrayList<>();
        for (File signature : expertSignatures) {
            ExpertSignatureDTO expertSignature = JSON.parseObject(FileUtil.readTxtFile(signature.getPath()), ExpertSignatureDTO.class);
            list.add(expertSignature);
        }
        return list;
    }

    /**
     * 替换掉保存签名信息对象中的keywordRule
     *
     * @param expertSignatureFiles 专家签名信息
     * @param keyWordRule 签名规则
     * @return
     */
    public static List<ExpertSignatureDTO> replaceKeyWordRule(File[] expertSignatureFiles, String keyWordRule) {
        List<ExpertSignatureDTO> expertSignatureList = SignatureUtil.getExpertSignaturesByFile(expertSignatureFiles);
        for (ExpertSignatureDTO expertSignature : expertSignatureList) {
            List<KeyWordRuleDTO> keyWordRules = expertSignature.getKeyWordRules();
            for (KeyWordRuleDTO keyword : keyWordRules) {
                keyword.setKeyWord(keyWordRule);
            }
        }
        return expertSignatureList;
    }

    /**
     * 返回关键字所在的坐标和页数
     * 页数与关键字页码放置的位置
     *
     * @param filePath 文件地址
     * @param expertSignatures 专家签名信息
     * @return
     */
    private static HashMap<Integer, List<KeyWordRuleDTO>> getKeyWords(String filePath, final List<ExpertSignatureDTO> expertSignatures) {
        final HashMap<Integer, List<KeyWordRuleDTO>> res = new HashMap<>();
        PdfReader pdfReader = null;
        try {
            // pdf读取器
            pdfReader = new PdfReader(filePath);
            // 获取当前PDF总页数
            int pageNum = pdfReader.getNumberOfPages();
            // pdf内容解析器
            PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(pdfReader);

            // 下标从1开始
            for (int pageIndex = 1; pageIndex <= pageNum; pageIndex++) {
                final int finalPageIndex = pageIndex;
                pdfReaderContentParser.processContent(pageIndex, new RenderListener() {
                    @Override
                    public void renderText(TextRenderInfo textRenderInfo) {
                        String text = textRenderInfo.getText();
                        if (text != null) {
                            // 遍历存储专家签名信息的对象List
                            for (ExpertSignatureDTO expertSignature : expertSignatures) {
                                for (int keySize = 0; keySize < expertSignature.getKeyWordRules().size(); keySize++) {
                                    KeyWordRuleDTO keyWordRule = expertSignature.getKeyWordRules().get(keySize);
                                    if(text.indexOf(keyWordRule.getKeyWord())!=-1){
                                        System.err.println("对比文本："+text+"==>>"+keyWordRule.getKeyWord());
                                    }else{
                                        System.out.println("对比文本："+text+"==>>"+keyWordRule.getKeyWord());
                                    }
                                    if (text.indexOf(keyWordRule.getKeyWord())!=-1) {
                                        Rectangle2D.Float boundingRectange = textRenderInfo.getBaseline().getBoundingRectange();

                                        float x = Float.parseFloat(keyWordRule.getX()) + new Float(boundingRectange.x + 15);
                                        float y = Float.parseFloat(keyWordRule.getY()) + new Float(boundingRectange.y - 11);

                                        List<KeyWordRuleDTO> list = res.get(finalPageIndex);
                                        if (list == null) {
                                            list = new ArrayList<>();
                                        }
                                        {
                                            KeyWordRuleDTO tempKey = null;
                                            try {
                                                tempKey = (KeyWordRuleDTO) keyWordRule.clone();
                                            } catch (CloneNotSupportedException e) {
                                                e.printStackTrace();
                                            }
                                            tempKey.setImageBase64(expertSignature.getImageBase64());
                                            tempKey.setX(String.valueOf(x));
                                            tempKey.setY(String.valueOf(y));
                                            list.add(tempKey);
                                        }
                                        res.put(finalPageIndex, list);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void renderImage(ImageRenderInfo arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void endTextBlock() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beginTextBlock() {
                        // TODO Auto-generated method stub

                    }
                });
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (pdfReader != null) {
                pdfReader.close();
            }
        }
        return res;
    }

    /**
     * 通过印模编号对pdf进行签章
     * @param ruleNum 印模编号
     * @param filePath 文件路径
     */
    public static boolean pdfSignByRuleNum(String ruleNum, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        byte[] pdfBty;
        AnySignClientTool anySignClientTool;
        ChannelMessage message;
        try {
            pdfBty = ClientUtil.readFileToByteArray(file);
            anySignClientTool = new AnySignClientTool(signatureIp + ":" + signaturePort);
            // pdf 字节数组
            message = anySignClientTool.pdfSign(ruleNum, pdfBty);
            if (SUCCESS.equals(message.getStatusCode())) {
                // 成功
                ClientUtil.writeByteArrayToFile(new File(filePath), message.getBody());
                System.out.println("成功,PDF保存路径:" + filePath);
                return true;
            } else {
                System.err.println("签名保存失败." + message.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("签名服务器异常：" + e.toString());
        }
        return false;
    }

    /**
     * 通过签名信息的文件合成签名PDF（为PDF添加签名图片）
     *
     * @param path
     * @param outPath
     * @param expertSigarsFile 签名信息文件
     * @throws Exception
     */
    public static void signarSingleFileSigarPic(String path, String outPath, File[] expertSigarsFile) throws Exception {
        //读取文件中专家签名信息，在将签名字符串转换成对应的签名对象并放入list中
        List<ExpertSignatureDTO> expertSignars = expertSignars(expertSigarsFile);
        //临时PDF路径
        String targetPath = new File(path).getParentFile().getPath() + "/temp/tempPdf.pdf";
        //将inpath中PDF文件复制到临时PDF中
        FileUtil.copyFile(path, targetPath);
        //获取PDF页码和对应的签字位置信息map
        HashMap<Integer, List<KeyWordRuleDTO>> map = getKeyWords(path, expertSignars);

        // 读取模板文件
        InputStream input = new FileInputStream(new File(targetPath));
        PdfReader reader = new PdfReader(input);
        FileOutputStream fos = new FileOutputStream(outPath);
        PdfStamper stamper = new PdfStamper(reader, fos);
        int num = reader.getNumberOfPages();

        // 获取页码
        for (int pageIndex = 1; pageIndex <= num; pageIndex++) {
            if (map.get(pageIndex) == null) {
                continue;
            }
            List<KeyWordRuleDTO> keyWordRules = map.get(pageIndex);

            for (KeyWordRuleDTO keyWordRule : keyWordRules) {
                if ("validSignature".equals(keyWordRule.getKeyWord())) {
                    continue;
                }
                float imageW = Float.parseFloat(keyWordRule.getWidth());
                float imageH = Float.parseFloat(keyWordRule.getHeight());
                // 生成图片
                Image image = Image.getInstance(B64Utils.s_decode(keyWordRule.getImageBase64()));
                PdfContentByte under = stamper.getOverContent(pageIndex);
                // 根据域的大小缩放图片
                image.scaleToFit(imageW, imageH);
                float x = Float.parseFloat(keyWordRule.getX());
                float y = Float.parseFloat(keyWordRule.getY());
                System.out.println("page" + pageIndex + "=" + x + ":" + y);
                // 添加图片
                image.setAbsolutePosition(x, y);
                under.addImage(image);
            }
        }

        stamper.close();
        input.close();
        reader.close();
        fos.close();
    }

    //专家签名信息
    private static List<ExpertSignatureDTO> expertSignars(File[] expertSigars) {
        List<ExpertSignatureDTO> list = new ArrayList<>();
        for (int i = 0; i < expertSigars.length; i++) {
            ExpertSignatureDTO expertSignar = JSON.parseObject(FileUtil.readTxtFile(expertSigars[i].getPath()), ExpertSignatureDTO.class);
            list.add(expertSignar);
        }
        return list;
    }

    /****
     * 单文件多签名
     * @param filePath 文件路径
     * @param outPath 签名完成保存的路径
     * @param signatureFile 所有的专家字符串信息
     * ***/
    public static boolean signarSingleFile(String filePath, String outPath, File[] signatureFile) {
        try {
            AnySignClientTool anySignClientTool = new AnySignClientTool(signatureIp + ":" + signaturePort);
            List<ExternalAnyWriteInfo> externalAnyWriteInfoList = new ArrayList<>();
            //添加PDF文件
            GeneratePDFInfo generatePDFInfo;
            List<GeneratePDFInfo> generatePDFInfoList = new ArrayList<>();
            List<ExternalEncryptPackageInfo> encryptPackageInfoList = new ArrayList<>();
            ExternalAnyWriteInfo externalAnyWriteInfo = null;
            externalAnyWriteInfo = new ExternalAnyWriteInfo();
            // 99999为测试信道
            // 40931586正式信道
            // externalAnyWriteInfo.setChannel("99999");
            externalAnyWriteInfo.setChannel("40931586");
            byte[] pdfBty = null;
            // PDF字节数组
            pdfBty = ClientUtil.readFileToByteArray(new File(filePath));
            generatePDFInfo = new GeneratePDFInfo();
            generatePDFInfo.setDocContent(pdfBty);
            generatePDFInfo.setDocumentExtType(DocumentExtType.PDF);
            generatePDFInfoList.add(generatePDFInfo);

            //添加手写加密包
            byte[] encDataBty;
            ExternalEncryptPackageInfo encryptPackageInfo;
            for (File file : signatureFile) {
                // 加密包字节数组
                encDataBty = ClientUtil.readFileToByteArray(file);
                encryptPackageInfo = new ExternalEncryptPackageInfo(encDataBty);
                encryptPackageInfoList.add(encryptPackageInfo);
            }
            externalAnyWriteInfo.setEncryptPackageInfoList(encryptPackageInfoList);
            externalAnyWriteInfo.setGeneratePDFInfoList(generatePDFInfoList);
            externalAnyWriteInfoList.add(externalAnyWriteInfo);
            long begin = System.currentTimeMillis();
            ChannelMessage message = anySignClientTool.anyWritePDFSignFacade(externalAnyWriteInfoList);
            if ("200".equals(message.getStatusCode())) {
                // 成功
                System.out.println("成功,PDF保存路径:" + outPath);
                List<MessageBodyReference> messageBodyReferenceList = message.getMessageBodyReferenceList();
                MessageBodyReference messageBodyReference;
                for (MessageBodyReference bodyReference : messageBodyReferenceList) {
                    messageBodyReference = bodyReference;
                    ClientUtil.writeByteArrayToFile(new File(outPath), messageBodyReference.getFileBty());
                }
                return true;
            } else {
                System.err.println("签名保存失败." + message.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("签名服务器异常：" + e.toString());
        }
        return false;
    }

}

