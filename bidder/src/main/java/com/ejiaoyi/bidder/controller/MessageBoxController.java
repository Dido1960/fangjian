package com.ejiaoyi.bidder.controller;

import com.ejiaoyi.bidder.support.CurrentUserHolder;
import com.ejiaoyi.common.constant.UserType;
import com.ejiaoyi.common.dto.MessageDTO;
import com.ejiaoyi.common.entity.LineMsg;
import com.ejiaoyi.common.entity.LineMsgRead;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @Description: 消息盒子控制层
 * @Auther: Make
 * @Date: 2020-8-6 14:01
 */
@RestController
@RequestMapping("/messageBox")
public class MessageBoxController extends BaseController {

    @Autowired
    private ILineMsgService lineMsgService;

    @Autowired
    private ILineMsgReadService lineMsgReadService;

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IMessageService messageService;

    /**
     * 新增用户发送信息
     * @param lineMsg 网上开标消息
     */
    @RequestMapping("/addUserSendMsg")
    public void addUserSendMsg(LineMsg lineMsg) {
        lineMsgService.addLineMsg(lineMsg);
    }

    /**
     * 跳转查看所有消息页面
     * @param bidSectionId 标段id
     * @param bidderId 投标人id
     */
    @RequestMapping("/viewAllMessagePage")
    public ModelAndView viewAllMessagePage(Integer bidSectionId, Integer bidderId) {
        ModelAndView mav = new ModelAndView("/messageBox/message");
        if (!CommonUtil.isEmpty(bidderId)) {
            mav.addObject("bidder", bidderService.getBidderById(bidderId));
        }
        mav.addObject("bidSection", bidSectionService.getBidSectionById(bidSectionId));
        return mav;
    }

    /**
     * 分页查询标段所有消息
     * @param bidSectionId 标段id
     */
    @RequestMapping("/listLineMsg")
    public List<LineMsg> listLineMsg(Integer bidSectionId) {
        return lineMsgService.listLineMsg(bidSectionId);
    }

    /**
     * 获取最后一条消息记录
     * @param bidSectionId 标段id
     */
    @RequestMapping("/getLastLineMsg")
    public LineMsg getLastLineMsg(Integer bidSectionId) {
        return lineMsgService.getLastLineMsg(bidSectionId);
    }

    /**
     * 更新消息记录
     * @param lineMsg 需要更新的消息记录
     * @return 是否更新成功
     */
    @RequestMapping("/updateLineMsg")
    public boolean updateLineMsg(LineMsg lineMsg) {
        return lineMsgService.updateLineMsg(lineMsg);
    }

    /**
     * 更新消息记录阅读情况
     * @param bidSectionId 标段id
     */
    @RequestMapping("/updateLineMsgRead")
    public void updateLineMsgRead(Integer bidSectionId, Integer bidderId) {
        Integer userId = bidderId;
        Integer roleType = UserType.BIDDER;

        if (CommonUtil.isEmpty(bidderId)) {
            userId = CurrentUserHolder.getUser().getUserId();
            roleType = UserType.AGENT;
        }

        lineMsgReadService.updateUserLineMsgRead(bidSectionId, userId, roleType);
    }

    /**
     * 获取未读的网上开标消息条数
     * @param lineMsgRead 阅读情况
     * @param bidSectionId 标段id
     * @return
     */
    @RequestMapping("/getUnReadCount")
    public Integer getUnReadCount(LineMsgRead lineMsgRead, Integer bidSectionId) {
        return lineMsgReadService.getUnReadCount(lineMsgRead, bidSectionId);
    }

    /**
     * 获取最新的投标人消息
     * @param bidderId 投标人id
     * @return MessageDTO
     */
    @RequestMapping("/getNewMessage")
    public MessageDTO getNewMessage(Integer bidderId) {
        String key = "BID_OPEN_MESSAGE_BIDDER:" + bidderId;
        return messageService.getUserMessage(key);
    }

    /**
     * 删除投标人消息
     * @param bidderId 投标人id
     * @return
     */
    @RequestMapping("/deleteNewMessage")
    public void deleteNewMessage(Integer bidderId) {
        String key = "BID_OPEN_MESSAGE_BIDDER:" + bidderId;
        messageService.deleteNewMessage(key);
    }

}
