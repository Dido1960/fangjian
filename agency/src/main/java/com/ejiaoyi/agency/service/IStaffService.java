package com.ejiaoyi.agency.service;

import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.dto.ProjectInfoTemp;
import com.ejiaoyi.common.entity.UploadFile;

import java.util.List;
import java.util.Map;

/**
 * 代理机构（招标人）服务类
 *
 * @author Make
 * @since 2020-07-07
 */
public interface IStaffService {

    /**
     * 保存项目信息
     *
     * @param bidFileId        上传的招标文件ID
     * @param clarifyFileId    上传的澄清文件ID
     * @param regId            项目归属地
     * @param openBidOnline    网上开标标志
     * @param remoteEvaluation 远程异地评标标志
     * @return
     */
    Map<String, Object> parseProjectInfo(Integer bidFileId, String clarifyFileId, Integer regId, Integer openBidOnline, Integer remoteEvaluation, String bidOpenTime, String bidDocReferEndTime) throws Exception;

    /**
     * 保存项目信息
     *
     * @param projectInfo
     * @return
     * @throws Exception
     */
    boolean saveProjectInfo(ProjectInfoTemp projectInfo) throws Exception;

    /**
     * 校验是否到达开标时间
     *
     * @param bidSectionId
     * @return
     */
    boolean checkModify(Integer bidSectionId);

    /**
     * 通过id获取当前标段的开标流程的完成情况
     *
     * @param id 标段id
     * @return
     */
    List<String> listBidOpenProcessComplete(Integer id);


    /**
     * 生成开标记录表
     *
     * @param bidSectionId 标段id
     * @param bidOpenPlace 开标地点
     * @return
     */
    boolean createRecordTable(Integer bidSectionId,String bidOpenPlace);

    /**
     * 唱标（招标信息+所有投标人信息）
     *
     * @param bId        标段主键
     * @param tenderDto  招标(唱标内容)
     * @param listBidder 投标人对象列表(唱标内容)
     * @return
     */
    JsonData tenderFileCursor(Integer bId, String tenderDto, String listBidder);

    /**
     * 唱标 （单个投标人语音）
     *
     * @param bidSectionId  标段主键
     * @param bidderDto 投标人DTO
     * @return
     */
    JsonData bidderFileCursor(Integer bidSectionId, String bidderDto);

    /**
     * 获取开标记录表数据
     * @param bidSectionId 标段id
     * @param bidOpenPlace 开标地点
     * @return
     */
    Map<String,Object> getBidOpenRecordData(Integer bidSectionId,String bidOpenPlace);

    /**
     * 获取开标结束异常信息
     * @param bidSectionId 标段id
     * @param step 异常环节
     * @return
     */
    Map<String, Object> getExceptionMessage(Integer bidSectionId, String step);
    /**
     * 检测唱标插件是否正常启动
     * @return
     */
     boolean checkVoicePlug() ;

    /**
     * 当前标段语音是否存在
     * @param bidId 当前标段id
     * @param bidderId 投标人id
     *
     * @return
     */
    boolean thisBidVoiceExist(int bidId,int bidderId);

    /**
     * 删除唱标语音
     *
     * @param id 标段id
     * @return
     */
    boolean removeSingingVoice(Integer id);

    /**
     * 保存澄清文件数据
     * @param id 标段id
     * @param regId 区划id
     * @param uploadFile 上传文件
     */
    void saveClarifyFileAndData(Integer id, Integer regId, UploadFile uploadFile);


    /**
     * 保存纸质标项目信息
     *
     * @param projectInfo
     * @return
     * @throws Exception
     */
    boolean savePaperProjectInfo(ProjectInfoTemp projectInfo) throws Exception;
}
