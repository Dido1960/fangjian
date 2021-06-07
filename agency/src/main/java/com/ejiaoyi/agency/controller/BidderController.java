package com.ejiaoyi.agency.controller;

import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.BidderOpenInfo;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.enums.ExecuteCode;
import com.ejiaoyi.common.service.IBidderService;
import net.sf.jsqlparser.statement.execute.Execute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 投标人信息 controller
 *
 * @author Make
 * @since 2020-7-15
 */
@RestController
@RequestMapping("/staff/bidder")
public class BidderController {

    @Autowired
    IBidderService bidderService;

    /**
     * 获取通过表标段id未删除的所有投标人列表
     * @param bidSectionId 标段id
     * @return 投标人列表
     */
    @RequestMapping("/listBidderEnabled")
    @UserLog(value = "'获取未删除的所有投标人列表:标段id='+#bidSectionId", dmlType = DMLType.SELECT)
    public JsonData listBidderEnabled(Integer bidSectionId) {
        JsonData jsonData = new JsonData();
        List<Bidder> bidders = bidderService.listBidderEnabled(bidSectionId,false);
        if (bidders.size() > 0) {
            jsonData.setCode(ExecuteCode.SUCCESS.getCode().toString());
            jsonData.setData(bidders);
        } else {
            jsonData.setCode(ExecuteCode.FAIL.getCode().toString());
        }
        return jsonData;
    }

    /**
     * 未递交原因
     * @param bidderOpenInfo
     * @return
     */
    @RequestMapping("/notCheckinReason")
    public boolean notCheckinReason(BidderOpenInfo bidderOpenInfo) {
        return bidderService.notCheckinReason(bidderOpenInfo);
    }

    /**
     * 通过标段id获取解密成功，且未被标书拒绝的投标人
     * @param bidSectionId 标段id
     * @return 投标人列表
     */
    @RequestMapping("/listDecrySuccessBidder")
    public List<Bidder> listDecrySuccessBidder(Integer bidSectionId) {
        return bidderService.listDecrySuccessBidder(bidSectionId, false);
    }

}
