package com.ejiaoyi.api.service;

import com.ejiaoyi.api.dto.*;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.dto.RestPageInfo;
import com.ejiaoyi.common.dto.statistical.*;
import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.BidderOpenInfo;
import io.swagger.models.auth.In;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 数据统计对接处理 服务类
 *
 * @author Mike
 * @since 2021/03/23
 */
public interface IStatisticalDataService extends IBaseService {

    /**
     * 获取项目数据
     *
     * @param getProjectDataParam 筛选条件
     * @return 项目数据
     */
    List<ProjectDataDTO> listProjectData(GetProjectDataParam getProjectDataParam);

    /**
     * 统计项目情况
     *
     * @param getStatisticalDataParam 筛选条件
     * @return 项目情况
     */
    StatisticalDataDTO getStatisticalInfo(GetStatisticalDataParam getStatisticalDataParam);

    /**
     * 根据时间分页查询今日标讯
     *
     * @param getBidDataParam 标段查询信息参数
     * @return
     * @throws Exception
     */
    RestPageInfo<BidNewsDTO> pageBidNewsByDate(GetBidDataParam getBidDataParam);

    /**
     * 获取回执单地址等信息
     *
     * @param bidderId 投标人id
     * @return
     */
    String getReceiptInfo(Integer bidderId);

    /**
     * 获取回执单地址等信息
     *
     * @param getBidderSignInfo 投标人签到信息
     * @return
     */
    RestResultVO<String> processBidderSignInfo(GetBidderSignInfo getBidderSignInfo);

    /**
     * 获取回执单地址等信息
     *
     * @param bidSectionId 标段id
     * @param type         1控制价 2浮动点
     * @return
     */
    RestResultVO<String> getPriceOrFloat(Integer bidSectionId, Integer type);

    /**
     * 通过id获取当前标段的开标流程的完成情况
     *
     * @param getBidderCommonInfo
     * @return
     */
    RestResultVO<BidOpenDataDTO> getBidOpenFlowSituation(GetBidderCommonInfo getBidderCommonInfo);

    /**
     * 获取投标人名单
     *
     * @param bidSectionId 标段id
     * @return
     */
    List<BidderListDTO> listBidders(Integer bidSectionId);

    /**
     * 获取投标人名单
     *
     * @param bidSectionId 标段id
     * @return
     */
    RestResultVO<ControlPriceAnalysisToDTO> getControlPriceAnalysis(Integer bidSectionId);

    /**
     * 获取投标人名单
     *
     * @param bidSectionId 标段id
     * @return
     */
    List<ConfirmBidOpenRecordDTO> getConfirmBidOpenRecord(Integer bidSectionId);

    /**
     * 投标报价确认
     *
     * @param getBidderCommonInfo 投标人参数信息
     * @return
     */
    RestResultVO<Boolean> confirmBidderPrice(GetBidderCommonInfo getBidderCommonInfo);

    /**
     * 开始解密
     *
     * @param getBidderDecryptParam 投标人解密参数信息
     * @return
     */
    RestResultVO<Boolean> startDecrypt(GetBidderDecryptParam getBidderDecryptParam);

    /**
     * 获取解密情况
     *
     * @param getBidderCommonInfo 投标人参数信息
     * @return
     */
    RestResultVO<DecryptSituationDTO> getDecryptSituation(GetBidderCommonInfo getBidderCommonInfo);

    /**
     * 获取标段信息
     * @param bidSectionId 标段ID
     * @return BidSectionInformationDTO
     */
    RestResultVO<BidSectionInformationDTO> getBidSectionInformation(Integer bidSectionId);

    /**
     * 进入签到
     * @param bidSectionOpenParam 参数
     * @return EnterSignInDTO
     */
    RestResultVO<EnterSignInDTO> enterSignIn(BidSectionOpenParam bidSectionOpenParam);

    /**
     * 获取招标文件PDF
     * @param bidSectionId 标段ID
     * @return PDF地址
     */
    RestResultVO<String> getTenderPDF(Integer bidSectionId);

    /**
     * 校验二维码有效性
     *
     * @param bidSectionId 标段id
     * @return 是否失效
     */
    boolean verifyQRCodeInvalid(Integer bidSectionId);

    /**
     * 校验该投标人是否完成了身份认证
     *
     * @param bidderId 投标人id
     * @return 是否完成
     */
    boolean verifyCompleteAuth(Integer bidderId);

    /**
     * 获取认证完成后的回调地址
     * @param boiId boiId
     * @param optAuthWay 认证方式
     * @return 回调地址
     */
    String getAuthCallBakUrl(Integer boiId, Integer optAuthWay);

    /**
     * 获取投标人认证结果
     * @param bidSectionOpenParam
     * @return
     */
    RestResultVO<Integer> getBidderAuthResult(BidSectionOpenParam bidSectionOpenParam);

    /**
     * 获取解密文件
     * @param getBidderCommonInfo
     * @return
     */
    RestResultVO<List<BidderFileDTO>> getDecryptFile(GetBidderCommonInfo getBidderCommonInfo);

    /**
     * 获取投标报价确认数据
     * @param getBidderCommonInfo
     * @return
     */
    RestResultVO<ConfirmBidderPriceDTO> getConfirmBidderPriceData(GetBidderCommonInfo getBidderCommonInfo);

    /**
     * 获取投标人质疑状态
     * @param getBidderCommonInfo
     * @return
     */
    RestResultVO<Integer> getQuestionStatus(GetBidderCommonInfo getBidderCommonInfo);

    /**
     * 修改投标人质疑状态
     * @param updateBidderParam
     * @return
     */
    RestResultVO<Boolean> updateQuestionStatus(UpdateBidderParam updateBidderParam);

    /**
     * 获取群聊数据
     * @param getBidderCommonInfo
     * @return
     */
    RestResultVO<GroupChatDTO> getGroupChatData(GetBidderCommonInfo getBidderCommonInfo);

    /**
     * 获取开标结果区块链地址
     * @param getBidderCommonInfo
     * @return
     */
    RestResultVO<String> getBlockchainUrl(GetBidderCommonInfo getBidderCommonInfo);
}
