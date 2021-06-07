package com.ejiaoyi.agency.util;

import com.ejiaoyi.agency.dto.BidderCursorDto;
import com.ejiaoyi.agency.dto.TenderCursorDto;
import com.ejiaoyi.common.dto.HttpResponseDTO;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.enums.BidProtype;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.ConvertMoney;
import com.ejiaoyi.common.util.FileUtil;
import com.ejiaoyi.common.util.HttpClientUtil;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.MultimediaInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * 在线语音合成工具类
 *
 * @author fengjunhong
 * @since 2020-7-22
 */
public class OnlineVoiceUtil {

    /**
     * 合成语音的接口
     */
    public static final String VOICE_IP = "http://127.0.0.1:18797/LocalVoiceService/VoicePlay";

    /**
     * 招标文件名（语音）
     */
    public static final String TENDER_VOICE_FILE_NAME = "bidder.wav";

    /**
     * 不唱标
     */
    private static final String NO_SING = "-1";

    /**
     * 获取唱标信息招标文件名
     *
     * @param dirPath 存放语音的目录
     * @param text    待语音合成的文本
     * @return
     */
    public static JsonData getTenderCursor(String dirPath, String text) {
        JsonData data = new JsonData();
        try {
            // 招标音频存放路径
            String filePath = dirPath + File.separator + TENDER_VOICE_FILE_NAME;
            // 不存在时生成，存在直接返回
            if (!FileUtil.isExistDir(filePath)) {
                // 创建文件
                FileUtil.createFile(filePath);
                // 调用本地文字转语音的接口
                String url = VOICE_IP + "?url=" + URLEncoder.encode(filePath, "utf-8") + "&text=" + URLEncoder.encode(text, "utf-8");
                HttpResponseDTO clientUtil = HttpClientUtil.get(url);
                if (clientUtil.getCode() == 200 && "1".equals(clientUtil.getContent())) {
                    data.setCode("200");
                } else {
                    data.setCode("500");
                    data.setMsg("语音合成失败！");
                }
            }
            data.setCode("200");
            // 返回招标语音文件名
            data.setData(TENDER_VOICE_FILE_NAME);
        } catch (Exception ignored) {
            data.setCode("-200");
            data.setMsg("语音合成失败！");
        }
        return data;
    }

    /**
     * 获取唱标信息投标文件名
     *
     * @param dirPath 存放语音的目录
     * @param bId     投标人主键
     * @param text    待合成语音的文本
     * @return
     */
    public static JsonData getBidderCursor(String dirPath, Integer bId, String text) throws IOException {
        JsonData data = new JsonData();
        // 存放所有投标音频文件名
        // 投标人音频存放路径
        String filePath = dirPath + File.separator + bId + ".wav";
        // 投标人音频文件名
        String bidFileName = bId + "";
        // 不存在时生成，存在直接返回
        if (!FileUtil.isExistDir(filePath)) {
            // 创建文件
            FileUtil.createFile(filePath);
            // 调用本地文字转语音的接口
            String url = VOICE_IP + "?url=" + URLEncoder.encode(filePath, "utf-8") + "&text=" + URLEncoder.encode(text, "utf-8");
            HttpResponseDTO clientUtil = HttpClientUtil.get(url);
            // 返回投标语音文件名
            data.setData(bidFileName);
        } else {
            data.setCode("200");
            // 返回招标语音文件名
            data.setData(bidFileName);
        }
        return data;
    }

    /**
     * 封装招标唱标内容
     *
     * @param tender 招标dto
     * @return
     */
    public static String getStrTender(TenderCursorDto tender) {
        StringBuilder str = new StringBuilder("; 招标项目信息 ;");
        str.append("; 标段名称 ;");
        if (!CommonUtil.isEmpty(tender.getBidderName())) {
            str.append(tender.getBidderName()).append(";");
        } else {
            str.append("无");
        }
        str.append("; 标段类型 ;");
        if (!CommonUtil.isEmpty(tender.getBidderCode())) {
            str.append(tender.getBidderCode()).append(";");
        } else {
            str.append("无");
        }
        str.append("; 标段编号 ;");
        if (!CommonUtil.isEmpty(tender.getBidderNo())) {
            str.append(tender.getBidderNo()).append(";");
        } else {
            str.append("无");
        }
        // 施工才有控制价
        if (!CommonUtil.isEmpty(tender.getBidderCode())) {
            if (BidProtype.CONSTRUCTION.getChineseName().equals(tender.getBidderCode()) ||  BidProtype.EPC.getChineseName().equals(tender.getBidderCode())) {
                if (BidProtype.CONSTRUCTION.getChineseName().equals(tender.getBidderCode())) {
                    str.append(";  招标控制价 ;");
                } else {
                    str.append(";  最高投标限价 ;");
                }
                if (!CommonUtil.isEmpty(tender.getBidderMaxPrice())) {
                    String maxPrice = tender.getBidderMaxPrice().trim().replace("元", "").replace(",", "");;
                    str.append(ConvertMoney.moneyToChinese(maxPrice).replaceAll("陆", "六")).append(";");
                } else {
                    str.append("无");
                }
            }
        }
        str.append("; 开标地点 ;");
        if (!CommonUtil.isEmpty(tender.getOpenPlace())) {
            str.append(tender.getOpenPlace()).append(";");
        } else {
            str.append("无");
        }
        return str.toString();
    }


    /**
     * 封装投标人唱标内容
     *
     * @param bidder 投标人dto
     *               说明：参数值为-1，表示，不唱此字段
     * @return
     */
    public static String getStrBidder(BidderCursorDto bidder) {
        StringBuilder str = new StringBuilder();

        if (!NO_SING.equals(bidder.getBidderName())) {
            str.append("; 投标人名称 ;").append(bidder.getBidderName() == null ? "无" : bidder.getBidderName()).append(";");
        }

        if (!NO_SING.equals(bidder.getBidPrice())) {
            if (CommonUtil.isEmpty(bidder.getBidPriceType()) || "总价".equals(bidder.getBidPriceType())) {
                str.append("; 投标报价 ;").append(bidder.getBidPrice() == null ? "无" : ConvertMoney.moneyToChinese(bidder.getBidPrice()).replaceAll("陆", "六")).append(";");
            } else {
                str.append("; 投标报价 ;").append(bidder.getBidPriceType() + bidder.getBidPrice().replaceAll(bidder.getBidPriceType(),"")).append(";");
            }
        }

        if (!NO_SING.equals(bidder.getMarginPay())) {
            str.append("; 投标保证金 ;").append(bidder.getMarginPay() == null ? "无" : bidder.getMarginPay()).append(";");
        }

        if (!NO_SING.equals(bidder.getTimeLimit()) && !CommonUtil.isEmpty(bidder.getTimeLimit())) {

            String timeLimitStr = bidder.getTimeLimit().replaceAll("日历天", " ").replaceAll("天", "");

            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
            if (pattern.matcher(timeLimitStr).matches()) {
                timeLimitStr += "日历天 ;";
            }
            str.append("; 投标工期 ;").append(bidder.getTimeLimit() == null ? "无" : timeLimitStr);
        }

        if (!NO_SING.equals(bidder.getQuality()) && !CommonUtil.isEmpty(bidder.getQuality())) {
            str.append("; 工程质量 ;").append(bidder.getQuality() == null ? "无" : bidder.getQuality()).append(";");
        }
        return str.toString();

    }

    /**
     * 获取wav音频时长
     *
     * @param filePath 文件路径
     * @return
     * @throws EncoderException
     */
    public static long getWavDuration(String filePath) throws EncoderException, FileNotFoundException {
        File source = new File(filePath);
        Encoder encoder = new Encoder();
        MultimediaInfo m = encoder.getInfo(source);
        return m.getDuration();
    }


}