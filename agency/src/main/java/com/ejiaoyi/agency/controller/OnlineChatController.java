package com.ejiaoyi.agency.controller;

import com.ejiaoyi.agency.support.AuthUser;
import com.ejiaoyi.agency.support.CurrentUserHolder;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.dto.OnlineUserDTO;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.BidderOpenInfo;
import com.ejiaoyi.common.service.IBidSectionService;
import com.ejiaoyi.common.service.IBidderOpenInfoService;
import com.ejiaoyi.common.service.IMsgExportService;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.RedisUtil;
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

    @Value("${message.user-id}")
    private String userId;

    @Value("${message.bidder-id-pre}")
    private String bidderIdPre;

    @Autowired
    IBidSectionService bidSectionService;

    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;

    @Autowired
    private IMsgExportService msgExportService;

    /**
     * 获取登录的角色信息
     */
    @GetMapping("/getUser")
    public OnlineUserDTO getUser() {
        AuthUser user = CurrentUserHolder.getUser();
        BidSection bidSection = bidSectionService.getBidSectionById(user.getBidSectionId());
        OnlineUserDTO onlineUserDTO = new OnlineUserDTO();
        onlineUserDTO.setRoomId((roomId + "_" + bidSection.getId()));
        onlineUserDTO.setRoomName(bidSection.getBidSectionName());
        onlineUserDTO.setUserId(userId + "_" + bidSection.getId());
        onlineUserDTO.setUserName(user.getLoginName());
        onlineUserDTO.setRole("0");
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
            // 设置投标人被踢出状态
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
                .userId(userId + "_" + bidSectionId)
                .userName(user.getLoginName())
                .roomId(roomId + "_" + bidSectionId)
                .roomName(bidSection.getBidSectionName())
                .sex(1)
                .role("0")
                .kickStatus(0)
                .importMessage(true)
                .build();
        return onlineUserDTO;
    }

    /**
     * 消息导出 合成pdf
     * @param data 合成数据
     * @return 下载 文件名 及 路径
     */
    @RequestMapping("/msgPdfSynthesis")
    public JsonData msgPdfSynthesis(String data) {
        AuthUser user = CurrentUserHolder.getUser();
        return msgExportService.msgPdfSynthesis(user.getBidSectionId(), data);
    }

}
