package com.ejiaoyi.bidder.controller;

import com.ejiaoyi.bidder.support.AuthUser;
import com.ejiaoyi.bidder.support.CurrentUserHolder;
import com.ejiaoyi.common.dto.OnlineUserDTO;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.BidderOpenInfo;
import com.ejiaoyi.common.service.IBidSectionService;
import com.ejiaoyi.common.service.IBidderOpenInfoService;
import com.ejiaoyi.common.service.IBidderService;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 系统消息 前端控制器
 * </p>
 *
 * @author Make
 * @since 2020-08-06
 */
@RestController
@RequestMapping("/onlineChat")
public class OnlineChatController {

    @Autowired
    IBidderService bidderService;

    @Value("${message.room-id}")
    private String roomId;

    @Value("${message.bidder-id-pre}")
    private String bidderIdPre;

    @Autowired
    IBidSectionService bidSectionService;

    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;


    /**
     * 删除提示消息
     */
    @RequestMapping("/getUser")
    public OnlineUserDTO getUser() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        BidSection bidSection = bidSectionService.getBidSectionById(user.getBidSectionId());
        //游客
        if (user.getBidderId() == null) {
            OnlineUserDTO onlineUserDTO = new OnlineUserDTO();
            onlineUserDTO.setKickStatus(0);
            onlineUserDTO.setRoomId((roomId + "_" + bidSection.getId()));
            onlineUserDTO.setRoomName(bidSection.getBidSectionName());
            onlineUserDTO.setUserId(bidderIdPre + "_" + user.getUserId());
            onlineUserDTO.setUserName(user.getName());
            onlineUserDTO.setRole("3");
            onlineUserDTO.setSex(1);
            Object kickStatus = RedisUtil.get(bidderIdPre + "_" + user.getUserId());
            if (kickStatus instanceof Integer) {
                onlineUserDTO.setKickStatus((Integer) kickStatus);
            }
            return onlineUserDTO;
        } else {
            //投标单位
            Integer bidderId = user.getBidderId();
            BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSection.getId());
            OnlineUserDTO onlineUserDTO = new OnlineUserDTO();
            String clientIdcard;
            if (!CommonUtil.isEmpty(bidderOpenInfo)) {
                onlineUserDTO.setKickStatus(bidderOpenInfo.getKickStatus());
                clientIdcard = bidderOpenInfo.getClientIdcard();
            } else {
                onlineUserDTO.setKickStatus(0);
                clientIdcard = null;
            }
            onlineUserDTO.setRoomId((roomId + "_" + bidSection.getId()));
            onlineUserDTO.setRoomName(bidSection.getBidSectionName());

            Bidder bidder = bidderService.getBidderById(bidderId);
            onlineUserDTO.setUserName(bidder.getBidderName());
            onlineUserDTO.setRole("2");
            onlineUserDTO.setUserId(bidderIdPre + "_" + bidder.getId());
            if (!CommonUtil.isEmpty(clientIdcard)) {
                String gender = clientIdcard.substring(16, 17);
                if (Integer.parseInt(gender) % 2 == 1) {
                    onlineUserDTO.setSex(1);
                } else {
                    onlineUserDTO.setSex(0);
                }
            } else {
                onlineUserDTO.setSex(1);
            }
            return onlineUserDTO;
        }

    }

}
