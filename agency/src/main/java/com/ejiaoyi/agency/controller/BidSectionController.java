package com.ejiaoyi.agency.controller;

import com.ejiaoyi.agency.service.impl.PushOpenResultServiceImpl;
import com.ejiaoyi.agency.service.impl.StaffServiceImpl;
import com.ejiaoyi.agency.support.CurrentUserHolder;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.TenderDoc;
import com.ejiaoyi.common.enums.BidProtype;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.enums.Status;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.service.IClearBidV3Service;
import com.ejiaoyi.common.service.IFDFSService;
import com.ejiaoyi.common.service.impl.BidSectionServiceImpl;
import com.ejiaoyi.common.service.impl.BsnChainInfoServiceImpl;
import com.ejiaoyi.common.service.impl.TenderDocServiceImpl;
import com.ejiaoyi.common.util.DateTimeUtil;
import com.ejiaoyi.common.util.ThreadUtlis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 标段信息 controller
 *
 * @author fengjunhong
 * @since 2020-7-13
 */
@RestController
@RequestMapping("/staff/bidSection")
public class BidSectionController {
    @Autowired
    private BidSectionServiceImpl bidSectionService;

    @Autowired
    private PushOpenResultServiceImpl pushOpenResultService;

    @Autowired
    private BsnChainInfoServiceImpl bsnChainInfoService;

    @Autowired
    private StaffServiceImpl staffService;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    private TenderDocServiceImpl tenderDocService;

    @Autowired
    private IClearBidV3Service clearBidService;

    /**
     * 获取标段列表
     *
     * @param bidSection
     * @return
     */
    @RequestMapping("/listBidSection")
    public List<BidSection> listBidSection(BidSection bidSection) {
        return bidSectionService.listBidSection(bidSection);
    }

    /**
     * 获取标段map集合
     *
     * @param bidSection
     * @return
     */
    @RequestMapping("/mapBidSection")
    public Map<String, Object> mapBidSection(BidSection bidSection) {
        bidSection.setAgencyId(CurrentUserHolder.getUser().getUserId());
        return bidSectionService.mapBidSection(bidSection);
    }

    /**
     * 修改标段信息
     *
     * @param bidSection
     * @return
     */
    @RequestMapping("/updateBidSection")
    @UserLog(value = "'签到设置:标段信息：'+#bidSection", dmlType = DMLType.UPDATE)
    public boolean updateBidSection(BidSection bidSection) {
        return bidSectionService.updateBidSectionById(bidSection) > 0;
    }

    /**
     * 结束开标
     *
     * @param id     标段id
     * @param userId 监管id
     * @return
     */
    @RequestMapping("/endBidOpen")
    public boolean endBidOpen(Integer id, Integer userId) throws Exception {
        BidSection bidSection = BidSection.builder()
                .id(id)
                .bidOpenStatus(Status.END.getCode())
                .managerId(userId)
                .bidOpenEndTime(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS))
                .build();

        BidSection old = bidSectionService.getBidSectionById(id);
        // 施工项目
        if (BidProtype.CONSTRUCTION.getCode().equals(old.getBidClassifyCode())) {
            TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(id);
            // 创建清标服务
            clearBidService.createClearServer(old, tenderDoc);
        }
        // 酒泉推送开标记录表
        ThreadUtlis.run(() -> {
            try {
                pushOpenResultService.pushOpenResultForJQ(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //区块信息调用*多线程
        bsnChainInfoService.tableBsnChainPut(id);

        return bidSectionService.updateBidSectionById(bidSection) != 0;

    }

}
