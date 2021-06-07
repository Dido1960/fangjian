package com.ejiaoyi.api.controller.mobilePhone;

import com.ejiaoyi.api.controller.BaseController;
import com.ejiaoyi.api.dto.*;
import com.ejiaoyi.api.service.impl.StatisticalDataServiceImpl;
import com.ejiaoyi.common.annotation.ApiAuthentication;
import com.ejiaoyi.common.constant.PhoneResponseState;
import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.dto.RestPageInfo;
import com.ejiaoyi.common.dto.statistical.*;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.BidderOpenInfo;
import com.ejiaoyi.common.entity.TenderDoc;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.util.FileUtil;
import com.ejiaoyi.common.util.ThreadUtlis;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * 手机端数据对接接口
 *
 * @author Mike
 * @since 2021-03-31
 */
@Api(value = "手机端数据对接接口", tags = "手机端数据对接接口")
@RestController
@RequestMapping("/phone")
public class PhoneController implements BaseController {

    private static final String API_NAME = "phone";

    @Autowired
    private StatisticalDataServiceImpl statisticalDataService;

    @Override
    @PostMapping("/getToken")
    @ApiOperation(value = "获取TOKEN", notes = "获取TOKEN", response = String.class)
    @ApiOperationSupport(
            order = 1,
            author = "Mike"
    )
    //该注解是用于验证授权值是否有效,token是否有效
    @ApiAuthentication(apiName = API_NAME, replay = false)
    public String getToken(@RequestBody @Valid GetToken getToken) {
        return statisticalDataService.getToken(getToken);
    }

    @PostMapping("/getTodayBidNews")
    @ApiOperation(value = "获取今日标讯", notes = "获取今日标讯", response = BidNewsDTO.class)
    @ApiOperationSupport(
            order = 2,
            author = "Mike"
    )
    @ApiAuthentication(apiName = API_NAME)
    public RestPageInfo<BidNewsDTO> getTodayBidNews(@RequestBody @Valid GetBidDataParam getBidDataParam) {
        return statisticalDataService.pageBidNewsByDate(getBidDataParam);
    }

    @PostMapping("/getReceiptInfo")
    @ApiOperation(value = "获取回执单", notes = "获取回执单", response = String.class)
    @ApiOperationSupport(
            order = 3,
            author = "yyb"
    )
    @ApiAuthentication(apiName = API_NAME)
    public String getReceiptInfo(@RequestBody @Valid GetBidderCommonInfo getBidderCommonInfo) {
        return statisticalDataService.getReceiptInfo(getBidderCommonInfo.getBidderId());
    }

    @PostMapping("/processBidderSignInfo")
    @ApiOperation(value = "接收授权人签到信息", notes = "接收授权人签到信息", response = String.class)
    @ApiOperationSupport(
            order = 4,
            author = "yyb"
    )
    @ApiAuthentication(apiName = API_NAME)
    public RestResultVO<String> processBidderSignInfo(@RequestBody @Valid GetBidderSignInfo getBidderSignInfo) {
        return statisticalDataService.processBidderSignInfo(getBidderSignInfo);
    }

    @PostMapping("/getPriceOrFloat")
    @ApiOperation(value = "获取控制价或浮动点", notes = "获取控制价或浮动点", response = String.class)
    @ApiOperationSupport(
            order = 5,
            author = "yyb"
    )
    @ApiAuthentication(apiName = API_NAME)
    public RestResultVO<String> getPriceOrFloat(@RequestBody @Valid GetControlFloatParam getControlFloatParam) {
        return statisticalDataService.getPriceOrFloat(getControlFloatParam.getBidSectionId(), getControlFloatParam.getType());
    }

    @PostMapping("/getBidOpenFlowSituation")
    @ApiOperation(value = "获取项目开标流程进行情况", notes = "获取项目开标流程进行情况", response = BidOpenDataDTO.class)
    @ApiOperationSupport(
            order = 6,
            author = "yyb"
    )
    @ApiAuthentication(apiName = API_NAME)
    public RestResultVO<BidOpenDataDTO> getBidOpenFlowSituation(@RequestBody @Valid GetBidderCommonInfo getBidderCommonInfo) {
        return statisticalDataService.getBidOpenFlowSituation(getBidderCommonInfo);
    }

    @PostMapping("/getBidderList")
    @ApiOperation(value = "获取投标人名单", notes = "获取投标人名单", response = BidderListDTO.class)
    @ApiOperationSupport(
            order = 7,
            author = "yyb"
    )
    @ApiAuthentication(apiName = API_NAME)
    public List<BidderListDTO> getBidderList(@RequestBody @Valid GetBidSectionIdParam getBidSectionIdParam) {
        return statisticalDataService.listBidders(getBidSectionIdParam.getBidSectionId());
    }

    @PostMapping("/getControlPriceAnalysis")
    @ApiOperation(value = "获取控制价分析", notes = "获取控制价分析", response = ControlPriceAnalysisToDTO.class)
    @ApiOperationSupport(
            order = 8,
            author = "yyb"
    )
    @ApiAuthentication(apiName = API_NAME)
    public RestResultVO<ControlPriceAnalysisToDTO> getControlPriceAnalysis(@RequestBody @Valid GetBidSectionIdParam getBidSectionIdParam) {
        return statisticalDataService.getControlPriceAnalysis(getBidSectionIdParam.getBidSectionId());
    }

    @PostMapping("/getConfirmBidOpenRecord")
    @ApiOperation(value = "获取开标一览表", notes = "获取开标一览表", response = ConfirmBidOpenRecordDTO.class)
    @ApiOperationSupport(
            order = 9,
            author = "yyb"
    )
    @ApiAuthentication(apiName = API_NAME)
    public List<ConfirmBidOpenRecordDTO> getConfirmBidOpenRecord(@RequestBody @Valid GetBidSectionIdParam getBidSectionIdParam) {
        return statisticalDataService.getConfirmBidOpenRecord(getBidSectionIdParam.getBidSectionId());
    }

    @PostMapping("/confirmBidderPrice")
    @ApiOperation(value = "投标报价确认", notes = "投标报价确认", response = Boolean.class)
    @ApiOperationSupport(
            order = 10,
            author = "yyb"
    )
    @ApiAuthentication(apiName = API_NAME)
    public RestResultVO<Boolean> confirmBidderPrice(@RequestBody @Valid GetBidderCommonInfo getBidderCommonInfo) {
        return statisticalDataService.confirmBidderPrice(getBidderCommonInfo);
    }

    @PostMapping("/startDecrypt")
    @ApiOperation(value = "开始解密", notes = "开始解密", response = Boolean.class)
    @ApiOperationSupport(
            order = 11,
            author = "yyb"
    )
    @ApiAuthentication(apiName = API_NAME)
    public RestResultVO<Boolean> startDecrypt(@RequestBody @Valid GetBidderDecryptParam getBidderDecryptParam) {
        return statisticalDataService.startDecrypt(getBidderDecryptParam);
    }

    @PostMapping("/getDecryptSituation")
    @ApiOperation(value = "获取解密情况", notes = "获取解密情况", response = DecryptSituationDTO.class)
    @ApiOperationSupport(
            order = 12,
            author = "yyb"
    )
    @ApiAuthentication(apiName = API_NAME)
    public RestResultVO<DecryptSituationDTO> getDecryptSituation(@RequestBody @Valid GetBidderCommonInfo getBidderCommonInfo) {
        return statisticalDataService.getDecryptSituation(getBidderCommonInfo);
    }

    @PostMapping("/getBidSectionInformation")
    @ApiOperation(value = "获取标段信息", notes = "获取标段信息", response = BidSectionInformationDTO.class)
    @ApiOperationSupport(
            order = 13,
            author = "lgq"
    )
    @ApiAuthentication(apiName = API_NAME)
    public RestResultVO<BidSectionInformationDTO> getBidSectionInformation(@RequestBody @Valid GetBidSectionIdParam getBidSectionIdParam) {
        return statisticalDataService.getBidSectionInformation(getBidSectionIdParam.getBidSectionId());
    }

    @PostMapping("/enterSignIn")
    @ApiOperation(value = "进入签到", notes = "进入签到", response = EnterSignInDTO.class)
    @ApiOperationSupport(
            order = 14,
            author = "lgq"
    )
    @ApiAuthentication(apiName = API_NAME)
    public RestResultVO<EnterSignInDTO> enterSignIn(@RequestBody @Valid BidSectionOpenParam bidSectionOpenParam) {
        return statisticalDataService.enterSignIn(bidSectionOpenParam);
    }

    @PostMapping("/getTenderPDF")
    @ApiOperation(value = "获取招标PDF", notes = "获取招标PDF", response = String.class)
    @ApiOperationSupport(
            order = 15,
            author = "lgq"
    )
    @ApiAuthentication(apiName = API_NAME)
    public RestResultVO<String> getTenderPDF(@RequestBody @Valid GetBidSectionIdParam getBidSectionIdParam) {
        return statisticalDataService.getTenderPDF(getBidSectionIdParam.getBidSectionId());
    }

    @PostMapping("/getBidderAuthResult")
    @ApiOperation(value = "获取投标人认证结果", notes = "获取投标人认证结果", response = Integer.class)
    @ApiOperationSupport(
            order = 16,
            author = "lgq"
    )
    @ApiAuthentication(apiName = API_NAME)
    public RestResultVO<Integer> getBidderAuthResult(@RequestBody @Valid BidSectionOpenParam bidSectionOpenParam) {
        return statisticalDataService.getBidderAuthResult(bidSectionOpenParam);
    }

    @PostMapping("/getDecryptFile")
    @ApiOperation(value = "获取解密文件", notes = "获取解密文件", response = BidderFileDTO.class)
    @ApiOperationSupport(
            order = 17,
            author = "lgq"
    )
    @ApiAuthentication(apiName = API_NAME)
    public RestResultVO<List<BidderFileDTO>> getDecryptFile(@RequestBody @Valid GetBidderCommonInfo getBidderCommonInfo) {
        return statisticalDataService.getDecryptFile(getBidderCommonInfo);
    }

    @PostMapping("/getConfirmBidderPriceData")
    @ApiOperation(value = "获取投标报价确认数据", notes = "获取投标报价确认数据", response = ConfirmBidderPriceDTO.class)
    @ApiOperationSupport(
            order = 18,
            author = "lgq"
    )
    @ApiAuthentication(apiName = API_NAME)
    public RestResultVO<ConfirmBidderPriceDTO> getConfirmBidderPriceData(@RequestBody @Valid GetBidderCommonInfo getBidderCommonInfo) {
        return statisticalDataService.getConfirmBidderPriceData(getBidderCommonInfo);
    }

    @PostMapping("/getQuestionStatus")
    @ApiOperation(value = "获取投标人质疑状态", notes = "获取投标人质疑状态 1:无异议 2：有异议 0: 未做选择", response = Integer.class)
    @ApiOperationSupport(
            order = 19,
            author = "lgq"
    )
    @ApiAuthentication(apiName = API_NAME)
    public RestResultVO<Integer> getQuestionStatus(@RequestBody @Valid GetBidderCommonInfo getBidderCommonInfo) {
        return statisticalDataService.getQuestionStatus(getBidderCommonInfo);
    }

    @PostMapping("/updateQuestionStatus")
    @ApiOperation(value = "修改投标人质疑状态", notes = "修改投标人质疑状态 1:成功 0: 失败", response = Boolean.class)
    @ApiOperationSupport(
            order = 20,
            author = "lgq"
    )
    @ApiAuthentication(apiName = API_NAME)
    public RestResultVO<Boolean> updateQuestionStatus(@RequestBody @Valid UpdateBidderParam updateBidderParam) {
        return statisticalDataService.updateQuestionStatus(updateBidderParam);
    }

    @PostMapping("/getGroupChatData")
    @ApiOperation(value = "获取群聊数据", notes = "获取群聊数据", response = GroupChatDTO.class)
    @ApiOperationSupport(
            order = 21,
            author = "lgq"
    )
    @ApiAuthentication(apiName = API_NAME)
    public RestResultVO<GroupChatDTO> getGroupChatData(@RequestBody @Valid GetBidderCommonInfo getBidderCommonInfo) {
        return statisticalDataService.getGroupChatData(getBidderCommonInfo);
    }

    @PostMapping("/getBlockchainUrl")
    @ApiOperation(value = "获取开标结果区块链地址", notes = "获取开标结果区块链地址", response = String.class)
    @ApiOperationSupport(
            order = 22,
            author = "lgq"
    )
    @ApiAuthentication(apiName = API_NAME)
    public RestResultVO<String> getBlockchainUrl(@RequestBody @Valid GetBidderCommonInfo getBidderCommonInfo) {
        return statisticalDataService.getBlockchainUrl(getBidderCommonInfo);
    }
}
