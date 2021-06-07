package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.annotation.RedissonLock;
import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.constant.PublishBiddersState;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.LineMsg;
import com.ejiaoyi.common.entity.LineStatus;
import com.ejiaoyi.common.entity.TenderDoc;
import com.ejiaoyi.common.enums.Status;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.mapper.BidSectionMapper;
import com.ejiaoyi.common.mapper.LineStatusMapper;
import com.ejiaoyi.common.mapper.TenderDocMapper;
import com.ejiaoyi.common.service.ILineMsgService;
import com.ejiaoyi.common.service.ILineStatusService;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.DateTimeUtil;
import com.ejiaoyi.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 网上开标信息信息 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-7-22
 */
@Service
public class LineStatusServiceImpl extends BaseServiceImpl implements ILineStatusService {

    @Value("${bidder.param.sign-time-left}")
    public Integer configSignTimeLeft;

    @Autowired
    private LineStatusMapper lineStatusMapper;

    @Autowired
    private BidSectionServiceImpl bidSectionService;

    @Autowired
    private TenderDocServiceImpl tenderDocService;

    @Autowired
    private ILineMsgService lineMsgService;

    @Override
    public LineStatus getLineStatusById(Integer id) {
        Assert.notNull(id, "param id can not be null!");
        return lineStatusMapper.selectById(id);
    }

    @Override
    @Cacheable(value = CacheName.LINE_STATUS, key = "#bidSectionId", unless = "#result==null")
    public LineStatus getLineStatusBySectionId(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        QueryWrapper<LineStatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        LineStatus lineStatus = lineStatusMapper.selectOne(queryWrapper);
        if (lineStatus == null) {
            lineStatus = LineStatus.builder().bidSectionId(bidSectionId).build();
            lineStatusMapper.insert(lineStatus);
        }
        return lineStatus;
    }

    @Override
    @CacheEvict(value = CacheName.LINE_STATUS, allEntries = true)
    public Integer updateLineStatusById(LineStatus lineStatus) {
        Assert.notNull(lineStatus, "param lineStatus can not be null!");
        Assert.notNull(lineStatus.getId(), "param id can not be null!");

        return lineStatusMapper.updateById(lineStatus);
    }

    @Override
    @CacheEvict(value = CacheName.LINE_STATUS, allEntries = true)
    public boolean updateLineStatus(LineStatus lineStatus) {
        Assert.notNull(lineStatus, "param lineStatus can not be null!");
        Assert.notNull(lineStatus.getBidSectionId(), "param bidSectionId can not be null!");
        LineMsg lineMsg = new LineMsg();
        lineMsg.setRoleType(0);
        lineMsg.setBidSectionId(lineStatus.getBidSectionId());
        lineMsg.setSendName("系统消息");
        LineStatus oldLineStatus = getLineStatusBySectionId(lineStatus.getBidSectionId());
        //签到
        if (!CommonUtil.isEmpty(lineStatus.getSigninStatus())) {
            if (Status.PROCESSING.getCode().equals(lineStatus.getSigninStatus())) {
                lineMsg.setMessage("开始投标人签到");
            } else if (Status.END.getCode().equals(lineStatus.getSigninStatus())) {
                lineMsg.setMessage("结束投标人签到");
            }
        }

        //公布投标人
        if (!CommonUtil.isEmpty(lineStatus.getPublishBidderStatus())) {
            if (Status.PROCESSING.getCode().equals(lineStatus.getPublishBidderStatus())) {
                lineMsg.setMessage("开始投标文件递交检查");
            } else if (Status.END.getCode().equals(lineStatus.getPublishBidderStatus())) {
                lineMsg.setMessage("结束投标文件递交检查");
            }
        }

        //身份检查
        if (!CommonUtil.isEmpty(lineStatus.getBidderCheckStatus())) {
            if (Status.PROCESSING.getCode().equals(lineStatus.getBidderCheckStatus())) {
                lineMsg.setMessage("开始投标人身份校验");
            } else if (Status.END.getCode().equals(lineStatus.getBidderCheckStatus())) {
                lineMsg.setMessage("结束投标人身份校验");
            }
        }

        //开启解密
        if (!CommonUtil.isEmpty(lineStatus.getDecryptionStatus())) {
            String decryptionPeriods = oldLineStatus.getDecryptionPeriods();
            if (Status.PROCESSING.getCode().equals(lineStatus.getDecryptionStatus())) {
                lineMsg.setMessage("开始投标文件解密");
                lineStatus = lineStatus.setDecryptionPeriods(getStartPeriods(decryptionPeriods));
            } else if (Status.END.getCode().equals(lineStatus.getDecryptionStatus())) {
                lineMsg.setMessage("结束投标文件解密");
                lineStatus = setEndTime(lineStatus, decryptionPeriods, 1);
            }
        }
        //开启质疑
        if (!CommonUtil.isEmpty(lineStatus.getQuestionStatus())) {
            String questionPeriods = oldLineStatus.getQuestionPeriods();
            if (Status.PROCESSING.getCode().equals(lineStatus.getQuestionStatus())) {
                lineMsg.setMessage("开始质询环节");
                lineStatus = lineStatus.setQuestionPeriods(getStartPeriods(questionPeriods));
            } else if (Status.END.getCode().equals(lineStatus.getQuestionStatus())) {
                lineMsg.setMessage("结束质询环节");
                setEndTime(lineStatus, questionPeriods, 2);
            }
        }

        //开启复会
        if (!CommonUtil.isEmpty(lineStatus.getResumeStatus())) {
            String resumePeriods = oldLineStatus.getResumePeriods();
            if (Status.PROCESSING.getCode().equals(lineStatus.getResumeStatus())) {
                lineMsg.setMessage("开始复会环节");
                lineStatus = lineStatus.setResumePeriods(getStartPeriods(resumePeriods));
            } else if (Status.END.getCode().equals(lineStatus.getResumeStatus())) {
                lineMsg.setMessage("结束复会环节");
                setEndTime(lineStatus, resumePeriods, 3);
            }
        }

        lineMsgService.addLineMsg(lineMsg);
        LineStatus lineStatusOld = getLineStatusBySectionId(lineStatus.getBidSectionId());
        if (lineStatusOld == null) {
            return lineStatusMapper.insert(lineStatus) > 0;
        }

        lineStatus.setId(lineStatusOld.getId());
        return lineStatusMapper.updateById(lineStatus) > 0;
    }

    /**
     * 设置代理每次开始 解密\质询 时间
     *
     * @param periods 历史数组字符串
     * @return
     */
    private String getStartPeriods(String periods) {
        //设置解密开始时间
        JSONArray jArray;
        if (CommonUtil.isEmpty(periods)) {
            jArray = new JSONArray();
        } else {
            jArray = JSONArray.parseArray(periods);
        }
        if (jArray.size() > 0) {
            // 获取数组最后一个json对象,查询结束时间，防止重复插入
            JSONObject lastJObject = jArray.getJSONObject(jArray.size() - 1);
            Object endTime = lastJObject.get("endTime");
            if (!CommonUtil.isEmpty(endTime)) {
                JSONObject jObject = new JSONObject();
                jObject.put("startTime", DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS));
                jArray.add(jObject);
            }
        }else {
            JSONObject jObject = new JSONObject();
            jObject.put("startTime", DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS));
            jArray.add(jObject);
        }

        return jArray.toString();
    }

    /**
     * 增加每次解密\质询\复会的结束解密时间，并计算总的解密用时
     *
     * @param lineStatus 待更新的网上开标状态
     * @param periods    解密\质询\复会时间历史记录数组字符串
     * @param type       设置类型 1解密 2质询 3复会
     * @return
     */
    private LineStatus setEndTime(LineStatus lineStatus, String periods, Integer type) {
        // 设置解密结束时间并计算解密总用时
        JSONArray jArray = JSONArray.parseArray(periods);
        // 获取数组最后一个json对象
        JSONObject lastJObject = jArray.getJSONObject(jArray.size() - 1);
        lastJObject.put("endTime", DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS));
        long totalTime = 0;
        for (int i = 0; i < jArray.size(); i++) {
            JSONObject jObject = jArray.getJSONObject(i);
            String startTime = (String) jObject.get("startTime");
            String endTime = (String) jObject.get("endTime");
            totalTime += DateTimeUtil.getTimeDiff(startTime, endTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        }
        if (type == 1) {
            lineStatus.setDecryptionTime(String.valueOf(totalTime));
            lineStatus.setDecryptionPeriods(jArray.toString());
        } else if (type == 2) {
            lineStatus.setQuestionTime(String.valueOf(totalTime));
            lineStatus.setQuestionPeriods(jArray.toString());
        } else {
            lineStatus.setResumeTime(String.valueOf(totalTime));
            lineStatus.setResumePeriods(jArray.toString());
        }

        return lineStatus;
    }

    @Override
    @CacheEvict(value = CacheName.LINE_STATUS, key = "#bidSectionId")
    public void updateFileUploadOrsigninStatus(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        QueryWrapper<LineStatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);

        LineStatus lineStatus = lineStatusMapper.selectOne(queryWrapper);
        if (CommonUtil.isNull(lineStatus)) {
            lineStatus = new LineStatus();
            lineStatus.setBidSectionId(bidSectionId);
            lineStatusMapper.insert(lineStatus);
        }
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String openBidTime = tenderDoc.getBidOpenTime();
        String bidDocReferEndTime = tenderDoc.getBidDocReferEndTime();
        String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);

        // 如果文件递交截止时间大于开标时间 则把截止时间临时改为开标时间
        long timeDiff1 = DateTimeUtil.getTimeDiff(bidDocReferEndTime, openBidTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        if (timeDiff1 < 0) {
            bidDocReferEndTime = openBidTime;
        }
        boolean docReferEnd = DateTimeUtil.compareDate(nowTime, bidDocReferEndTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS) >= 0;

        // 文件截止时间到了就不能上传文件
        if (docReferEnd) {
            lineStatus.setUploadStatus(Status.END.getCode());
        } else {
            lineStatus.setUploadStatus(Status.PROCESSING.getCode());
        }

        // 设置签到状态，根据代理设置的时间而定
        boolean sign = DateTimeUtil.compareDate(nowTime, openBidTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS) >= 0;
        if (sign) {
            //签到结束
            lineStatus.setSigninStatus(Status.END.getCode());
        } else {
            if (CommonUtil.isEmpty(lineStatus.getSigninStatus()) || !Status.PROCESSING.getCode().equals(lineStatus.getSigninStatus())) {
                int startMs = configSignTimeLeft;
                if (!CommonUtil.isEmpty(bidSection.getSignInStartTimeLeft())) {
                    startMs = bidSection.getSignInStartTimeLeft();
                } else {
                    BidSection section = new BidSection();
                    section.setId(bidSection.getId());
                    section.setSignInStartTimeLeft(startMs);
                    bidSectionService.updateBidSectionById(section);
                }
                long ms = startMs * 60 * 1000;
                long timeDiff = DateTimeUtil.getTimeDiff(openBidTime, nowTime, null, TimeFormatter.YYYY_HH_DD_HH_MM_SS);
                if (timeDiff <= ms) {
                    lineStatus.setSigninStatus(Status.PROCESSING.getCode());
                } else {
                    lineStatus.setSigninStatus(Status.NOT_START.getCode());
                }
            }
        }

        if (!CommonUtil.isEmpty(bidSection.getCheckStatus()) && Status.PROCESSING.getCode().toString().equals(bidSection.getCheckStatus())) {
            lineStatus.setUploadStatus(Status.END.getCode());
        } else {
            lineStatus.setUploadStatus(Status.PROCESSING.getCode());
        }

        this.updateLineStatusById(lineStatus);
    }

    @Override
    public void insertLineStatusByBidSectionId(Integer bidSectionId) {
        lineStatusMapper.insert(LineStatus.builder()
                .bidSectionId(bidSectionId)
                .build());
    }

}
