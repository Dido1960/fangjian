package com.ejiaoyi.common.service;

import com.ejiaoyi.common.dto.quantity.QuantityBidder;
import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.BidderOpenInfo;
import com.ejiaoyi.common.enums.EvalProcess;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 投标人信息服务类
 *
 * @author Make
 * @since 2020-07-15
 */
public interface IBidderService {

    /**
     * 通过主键id获取标段信息
     *
     * @param id 投标人主键id
     * @return 投标人信息
     */
    Bidder getBidderById(Integer id);

    /**
     * 通过条件获取投标人信息
     *
     * @param bidder 条件
     * @return 投标人信息
     */
    Bidder getBidder(Bidder bidder);

    /**
     * 根据标段id获取所有投标人列表（不包含删除的）
     * @param bidSectionId 标段id
     * @param isPage 是否分页
     * @return
     */
    List<Bidder> listBidderEnabled(Integer bidSectionId,Boolean isPage);

    /**
     * 未递交原因
     * @param bidderOpenInfo
     * @return
     */
     boolean notCheckinReason(BidderOpenInfo bidderOpenInfo);

    /**
     * 通过标段id获取解密成功，且未被标书拒绝的投标人
     * @param bidSectionId 标段id
     * @param isPage 是否分页
     * @return
     */
    List<Bidder> listDecrySuccessBidder(Integer bidSectionId, boolean isPage);

    /**
     * 通过标段id获取解密失败，未签到和被标书拒绝的投标人
     * @param bidSectionId 标段id
     * @return
     */
    List<Bidder> listFailBidder(Integer bidSectionId);

    /**
     * 通过标段id获取投标人列表，做身份检查
     * 筛选条件：投标文件确定，有签到时间
     * @param bidSectionId
     * @return
     *
     * @author: liuguoqiang
     */
    List<Bidder> listBiddersForCheck(Integer bidSectionId);

    /**
     * 根据标段id获取所有投标人列表（不包含删除的）
     *
     * @param bidSectionId 标段id
     * @param isPage       是否分页
     * @return
     */
    List<Bidder> listAllBidders(Integer bidSectionId, Boolean isPage);


    /**
     *  通过code获取某标段的 投标人
     * @param bidderOrgCode 投标人统一社会信用代码(为null时，查询标段所有Bidder)
     * @param bidSectionId 标段id(为null时，查询该企业所有参标信息)
     * @return
     */
    List<Bidder> getBidder(String bidderOrgCode,Integer bidSectionId);

    /**
     *  通过开标投标人（即能够进入评标系统）
     * @param bidSectionId 标段id(为null时，查询该企业所有参标信息)
     * @return
     */
    List<Bidder> listPassBidOpenBidder(Integer bidSectionId);

    /**
     *  查询 成功解密的且未被标书拒绝的投标人
     *  并分离当前登录投标人和其他投标人，
     *  并判断是否是资格预审
     *
     * @param bidSectionId 标段id
     * @param bidderOrgCode 投标人统一社会信用代码
     * @param stage 阶段
     * @return
     */
    Map<String,Object> getBidder(Integer bidSectionId,String bidderOrgCode,String stage);

    /**
     * 给投标人添加开标信息
     * @param bidSectionId 标段id
     * @param bidderOrgCode 当前登录的投标人组织机构代码
     * @param bidderName 当前登录的投标人名称
     * @return
     */
    Boolean addBidderOpenInfo(Integer bidSectionId, String bidderOrgCode, String bidderName) throws Exception;

    /**
     * 确认报价
     *
     * @param bidder 投标人
     * @return
     * @throws Exception
     */
    Boolean confirmPrice(Bidder bidder);

    /**
     * 投标文件解密
     *
     * @param fileId       文件id
     * @param bidderId     投标人id
     * @param bidSectionId 标段id
     * @param fileType     文件类型（0：非证书加密，1：证书加密）
     * @param privateKey   私钥
     * @param isOtherCa   是否互认解密（1:是， 0:不是）
     * @return
     */
    Boolean decrypt(Integer fileId, Integer bidderId, Integer bidSectionId, String fileType, String privateKey, String isOtherCa);

    /**
     * 对接投标人文件上传和解密
     *
     * @param fileId 文件id
     * @param bidder 当前投标人
     * @return
     */
    void dockUploadBidFile(Integer fileId,Bidder bidder);

    /**
     * 通过主键id获取投标人信息及其评审点页码信息
     *
     * @param bidderId 投标人主键id
     * @return 投标人信息及其评审点页码信息
     */
    Bidder getBidderReviewPoints(Integer bidderId);

    /**
     * 根据标段id修改投标人信息
     *
     * @param bidder 要修改的投标人信息
     * @return
     */
    Boolean updateBidderById(Bidder bidder);

    /**
     * 当前标段参加解密的人以及已经解密的人数
     *
     * @param bidSectionId
     * @return map "decryptNum": 参加解密的人数  "successNum": 成功解密的人数
     */
    Map<String, Integer> getDecryptNum(Integer bidSectionId);

    /**
     * 通过标段id获取文件解密的投标人列表
     *
     * @param bidSectionId 标段id
     * @return
     */
    List<Bidder> listBiddersForDecrypt(Integer bidSectionId);

    /**
     * 获取唱标投标人列表
     *
     * @param bidSectionId 标段id
     * @return
     */
    List<Bidder> listBiddersForSing(Integer bidSectionId);

    /**
     * 通过标段id获取通过初步评审,进入详细评审的投标人列表
     *
     * @param bidSectionId 标段id
     * @return 投标人集合
     */
    List<Bidder> listDetailedBidder(Integer bidSectionId);

    /**
     * 通过标段id获取通过详细评审
     *
     * @param bidSectionId 标段id
     * @return 投标人集合
     */
    List<Bidder> listPassDetailedBidder(Integer bidSectionId);

    /**
     * 获取未通过初步评审的投标人
     *
     * @param bidSectionId 标段id
     * @return 投标人集合
     */
    List<Bidder> listNoPassFirstStepBidder(Integer bidSectionId);
    /**
     *  标书拒绝和文件未递交 修改投标人信息（两信封）
     *  1.修改是否通过开标标志(包含撤销标书拒绝)
     *  2.修改标书拒绝标志和理由
     *
     * @param bidderOpenInfo 开标信息
     * @param isPassOpen 是否通过开标
     * @return
     */
    Boolean updateBidderInfo(BidderOpenInfo bidderOpenInfo, Integer isPassOpen);

    /**
     * 给投标人设置解密用时
     * @param bidders 投标人列表
     * @param bidSectionId 标段主键
     * @return
     */
    List<Bidder> calDecryptTime(List<Bidder> bidders, Integer bidSectionId) ;

    /**
     * 修改 是否通过开标
     * @param bidderId 投标人id
     * @param isPassBidOpen 是否通过开标
     */
    void updateBiddersIsPassBidOpen(Integer bidderId, Integer isPassBidOpen);

    /**
     * 获取通过资格审查的投标人
     * @param bidSectionId 标段ID
     * @return 投标人list
     */
    List<Bidder> listBidderPassQualifyReview(Integer bidSectionId);

    /**
     * 分页查询标书解密的投标人列表
     *
     * @param bidSectionId 标段id
     * @param isQualification 是否是资格预审
     * @return
     */
    List<Bidder> pageTenderDecryptBidders(Integer bidSectionId,Boolean isQualification);

    /**
     * 通过标段id封装需要清标的投标人数
     * @param bidSectionId
     * @return
     */
    List<QuantityBidder> listClearQuantityBidder(Integer bidSectionId);

    /**
     * 通过标段id判该标段清标服务是否已经全部完成
     * @param bidSectionId 标段id
     * @return
     */
    boolean validAllQuantityService(Integer bidSectionId);

    /**
     * 通过标段id封装需要报价得分的投标人数
     * @param bidSectionId
     * @return
     */
    List<QuantityBidder> listPriceScoreQuantityBidder(Integer bidSectionId);

    /**
     * 新增投标人信息
     * @param bidder 投标人信息
     * @return 新增后的投标人id
     */
    Integer saveBidder(Bidder bidder);

    /**
     * 校验投标人是否通过某环节评审
     *
     * @param bidSectionId 标段id
     * @param bidderId 投标人id
     * @param reviewProcess 环节流程
     * @return 是否通过某环节
     */
    boolean validProcessPassInfo(Integer bidSectionId, Integer bidderId, Integer reviewProcess);

    /**
     * 通过投标人名称和标段id获取投标人信息
     * @param bidderName 投标人名称
     * @param bidSectionId 标段id
     * @return
     */
    List<Bidder> listBidderByName(String bidderName, Integer bidSectionId);

    /**
     * 获取现场标唱标投标人列表
     *
     * @param bidSectionId 标段id
     * @return
     */
    List<Bidder> listSiteBiddersForSing(Integer bidSectionId);

    /**
     * 通过 信用代码 以及 关联标段 获取投标人
     * @param bidderOrgCode 唯一信用代码
     * @param preRelatedId 关联标段ID
     * @return 投标人
     */
    Bidder getBidderForRelate(String bidderOrgCode, Integer preRelatedId);

    /**
     * 投标文件解密
     *
     * @param fileId       文件id
     * @param bidderId     投标人id
     * @param bidSectionId 标段id
     * @param fileType     文件类型（0：非证书加密，1：证书加密）
     * @param privateKey   私钥
     * @param isOtherCa   是否互认解密（1:是， 0:不是）
     * @return
     */
    Boolean paperDecrypt(Integer fileId, Integer bidderId, Integer bidSectionId, String fileType, String privateKey, String isOtherCa);

    /**
     * 所有文件上传成功的投标人列表
     * @param bidSectionId 标段主键
     * @return
     */
    List<Bidder> listFileUploadSuccessBidder(Integer bidSectionId);

    /**
     * 现场标，所有解密成功的投标人
     * @param bidSectionId 标段id
     * @return
     */
    List<Bidder> listOfflineDecrySuccessBidder(Integer bidSectionId);

    /**
     * 获取所有简易投标人
     * @param bidSectionId 标段id
     * @param isPage 是否分页
     * @return
     */
    List<Bidder> listAllSimpleBidders(Integer bidSectionId, Boolean isPage);

    /**
     * 获取所有通过开标的简易投标人
     * @param bidSectionId 标段id
     * @return
     */
    List<Bidder> listPassBidOpenSimpleBidder(Integer bidSectionId);

    /**
     * 手机证书解密投标文件
     *
     * @param fileId       文件id
     * @param bidderId     投标人id
     * @param bidSectionId 标段id
     * @param phoneCertNo   手机证书号
     * @return
     */
    Boolean phoneDecrypt(Integer fileId, Integer bidderId, Integer bidSectionId, String phoneCertNo);

    /**
     * 开标前半小时分页查询投标人信息
     * @param bidSectionId
     * @return
     */
    String pageBidderBySectionId(Integer bidSectionId);

    /**
     * 获取投标人信息
     * 得到投标人
     * 通过条件获取投标人信息
     *
     * @param bidSectionId 标段ID
     * @param bidderID     投标人身份证
     * @return 投标人信息
     */
    Bidder getClearV3Bidder(Integer bidSectionId,Integer bidderID);
}
