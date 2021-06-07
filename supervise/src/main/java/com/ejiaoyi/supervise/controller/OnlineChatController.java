package com.ejiaoyi.supervise.controller;


import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.dto.OnlineUserDTO;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.BidderOpenInfo;
import com.ejiaoyi.common.entity.Dep;
import com.ejiaoyi.common.entity.GovUser;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.RedisUtil;
import com.ejiaoyi.supervise.support.AuthUser;
import com.ejiaoyi.supervise.support.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 及时通讯信息
 *
 * @author Make
 * @since 2020-08-06
 */
@RestController
@RequestMapping("/onlineChat")
public class OnlineChatController {

    @Value("${message.room-id}")
    private String roomId;

    @Value("${message.gov-id-pre}")
    private String govIdPre;

    @Value("${message.bidder-id-pre}")
    private String bidderIdPre;

    @Autowired
    IBidSectionService bidSectionService;

    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;

    @Autowired
    private IMsgExportService msgExportService;

    @Autowired
    private IGovUserService govUserService;

    @Autowired
    private IDepService depService;

    /**
     * 获取登录的角色信息
     */
    @GetMapping("/getUser")
    public OnlineUserDTO getUser() {
        AuthUser user = CurrentUserHolder.getUser();
        GovUser govUser = govUserService.getGovUserById(user.getUserId());
        BidSection bidSection = bidSectionService.getBidSectionById(user.getBidSectionId());
        OnlineUserDTO onlineUserDTO = new OnlineUserDTO();
        onlineUserDTO.setRoomId((roomId + "_" + bidSection.getId()));
        onlineUserDTO.setRoomName(bidSection.getBidSectionName());
        onlineUserDTO.setUserId(govIdPre + "_" + bidSection.getId());
        onlineUserDTO.setUserName(govUser.getDepName());
        onlineUserDTO.setRole("1");
        onlineUserDTO.setSex(1);
        onlineUserDTO.setKickStatus(0);
        return onlineUserDTO;
    }


    @RequestMapping("/kickBidder")
    public void kickBidder(String userId) {
        int start = userId.lastIndexOf("_");
        if (start == -1) {
            return;
        }
        String bidderId = userId.substring(start + 1, userId.length());
        try {
            // 设置游客的被踢出状态
            RedisUtil.set(userId, 1);
            BidderOpenInfo bidderOpenInfo = new BidderOpenInfo();
            bidderOpenInfo.setBidderId(Integer.parseInt(bidderId)).setKickStatus(1);
            bidderOpenInfoService.updateByBidderId(bidderOpenInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * 消息导出 准备数据
     *
     * @return
     */
    @RequestMapping("/getMessageExport")
    public OnlineUserDTO getMessageExport() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        OnlineUserDTO onlineUserDTO = OnlineUserDTO.builder()
                .userId(govIdPre + "_" + bidSectionId)
                .userName(user.getLoginName())
                .roomId(roomId + "_" + bidSectionId)
                .roomName(bidSection.getBidSectionName())
                .sex(1)
                .role("1")
                .kickStatus(0)
                .importMessage(true)
                .build();
        return onlineUserDTO;
    }

    /**
     * 消息导出 合成pdf
     *
     * @param data 合成数据
     * @return 下载 文件名 及 路径
     */
    @RequestMapping("/msgPdfSynthesis")
    public JsonData msgPdfSynthesis(String data) {
        AuthUser user = CurrentUserHolder.getUser();
        return msgExportService.msgPdfSynthesis(user.getBidSectionId(), data);
    }

}
