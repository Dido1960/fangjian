package com.ejiaoyi.api.controller.v2;

import com.ejiaoyi.api.controller.BaseController;
import com.ejiaoyi.api.dto.*;
import com.ejiaoyi.api.exception.APIException;
import com.ejiaoyi.api.service.impl.ServicePlatformDockServiceImpl;
import com.ejiaoyi.common.annotation.ApiAuthentication;
import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.entity.UploadFile;
import com.ejiaoyi.common.enums.DockApiCode;
import com.ejiaoyi.common.service.impl.ApiAuthServiceImpl;
import com.ejiaoyi.common.util.DesUtil;
import com.ejiaoyi.common.util.RedisUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import ejiaoyi.crypto.SM4Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * 甘肃电子开评标与第三方服务平台对接接口
 *
 * @author Mike
 * @since 2020-12-26
 */
@Api(value = "甘肃省房建市政与第三方服务平台对接接口", tags = "甘肃省房建市政与第三方服务平台对接接口")
@RestController
@RequestMapping("/v2/servicePlatformDock")
public class ThirdPartyServicePlatformDockController implements BaseController {

    private static final String API_NAME = "servicePlatformDock";

    @Autowired
    private ServicePlatformDockServiceImpl servicePlatformDockService;

    @Autowired
    private ApiAuthServiceImpl apiAuthService;

    @Override
    @PostMapping("/getToken")
    @ApiOperation(value = "获取TOKEN", notes = "获取TOKEN",response = String.class)
    @ApiOperationSupport(
            order = 1,
            author = "Mike"
    )
    //该注解是用于验证授权值是否有效,token是否有效
    @ApiAuthentication(apiName = API_NAME, replay = false)
    public String getToken(@RequestBody @Valid GetToken getToken) {
        return servicePlatformDockService.getToken(getToken);
    }

    @ApiOperation(value = "接收服务平台文件信息（招标文件(gef)、澄清答疑文件(pdf)、中标通知书(pdf)都可调用此接口）",
            notes = "接收服务平台文件信息招标文件(gef)、澄清答疑文件(pdf)、中标通知书(pdf)都可调用此接口"
            ,response = String.class)
    @ApiOperationSupport(
            order = 2,
            author = "Mike"
    )
    @RequestMapping(value = "/receiveDocument", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public String receiveBiddingDocument(@ApiParam(value = "platform", required = true)
                                         @RequestParam(value = "platform", required = true)
                                                 String platform,
                                         @ApiParam(value = "api_key", required = true)
                                             @RequestParam(value = "api_key", required = true)
                                                     String apiKey,
                                         @ApiParam(value = "token", required = true)
                                             @RequestParam(value = "token", required = true)
                                                     String token,
                                         @ApiParam(value = "文件（仅支持gef格式与pdf格式文件）", required = true)
                                         @RequestParam(value = "file", required = true)
                                                 MultipartFile file,
                                         @ApiParam(value = "文件SM3值", required = true)
                                             @RequestParam(value = "fileSM3", required = true)
                                                     String fileSM3) throws Exception {

        platform = SM4Util.decryptCBC(platform);
        apiKey = SM4Util.decryptCBC(apiKey);
        token = SM4Util.decryptCBC(token);
        fileSM3 = SM4Util.decryptCBC(fileSM3);
        if (StringUtils.isEmpty(token)) {
            throw new APIException(DockApiCode.TOKEN_MUST);
        }
        boolean authFlag = apiAuthService.authentication(API_NAME, platform, apiKey);
        boolean backInfo = apiAuthService.authentication(API_NAME, apiKey, platform);
        if (backInfo) {
            throw new APIException(DockApiCode.UNAUTHORIZED_ACCESS, "平台授权码与API_key位置反了", null);
        }

        if (!authFlag) {
            throw new APIException(DockApiCode.UNAUTHORIZED_ACCESS);
        }
        if (StringUtils.isEmpty(token)) {
            throw new APIException(DockApiCode.TOKEN_MUST);
        }
        if (!RedisUtil.hasKey(CacheName.TOKEN + token)) {
            throw new APIException(DockApiCode.TOKEN_INVALID);
        }

        UploadFile uploadFile = servicePlatformDockService.receiveDocument(file, fileSM3);
        return uploadFile.getFileUid();
    }

    @PostMapping("/receiveProjectInfo")
    @ApiOperation(value = "接收服务平台项目信息", notes = "接收服务平台项目信息")
    @ApiOperationSupport(
            order = 3,
            author = "Mike"
    )
    @ApiAuthentication(apiName = API_NAME, replay = false)
    public void receiveProjectInfo(@RequestBody @Valid GetProjectInfo getProjectInfo) throws Exception {
        servicePlatformDockService.receiveProjectInfo(getProjectInfo);
    }

    @PostMapping("/receiveBidderInfo")
    @ApiOperation(value = "接收服务平台投标人报名信息", notes = "接收服务平台投标人报名信息")
    @ApiOperationSupport(
            order = 4,
            author = "Mike"
    )
    @ApiAuthentication(apiName = API_NAME, replay = false)
    public void receiveBidderInfo(@RequestBody @Valid GetBidderInfo getBidderInfo) throws Exception {
        servicePlatformDockService.receiveBidderInfo(getBidderInfo);
    }

    @PostMapping("/receiveMarginInfo")
    @ApiOperation(value = "接收服务平台投标人保证金缴纳信息", notes = "接收服务平台投标人保证金缴纳信息")
    @ApiOperationSupport(
            order = 5,
            author = "Mike"
    )
    @ApiAuthentication(apiName = API_NAME, replay = false)
    public void receiveMarginInfo(@RequestBody @Valid GetBidderMarginInfo getBidderMarginInfo) throws Exception {
        servicePlatformDockService.receiveMarginInfo(getBidderMarginInfo);
    }

    @PostMapping("/receiveExpertInfo")
    @ApiOperation(value = "接收服务平台评标专家信息", notes = "接收服务平台评标专家信息")
    @ApiOperationSupport(
            order = 6,
            author = "Mike"
    )
    @ApiAuthentication(apiName = API_NAME, replay = false)
    public void receiveExpertInfo(@RequestBody @Valid GetExpertInfo getExpertInfo) throws Exception {
        servicePlatformDockService.receiveExpertInfo(getExpertInfo);
    }

    @ApiOperation(value = "接收服务平台中标通知书文件信息", notes = "接收服务平台中标通知书文件信息")
    @ApiOperationSupport(
            order = 7,
            author = "Mike"
    )
    @PostMapping(value = "/receiveWiningBidDocument")
    public void receiveWiningBidDocument(@RequestBody @Valid GetWiningBidFileInfo getWiningBidFileInfo) throws Exception {
        servicePlatformDockService.receiveWiningBidDocument(getWiningBidFileInfo);
    }
}
