package com.ejiaoyi.admin.controller;

import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.Dep;
import com.ejiaoyi.common.entity.TenderDoc;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.service.IClearBidV3Service;
import com.ejiaoyi.common.service.impl.BidSectionServiceImpl;
import com.ejiaoyi.common.service.impl.TenderDocServiceImpl;
import com.ejiaoyi.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengjunhong
 * @version 1.0
 * @date 2021-5-9 11:30
 */
@RestController
@RequestMapping("/clearV3")
public class ClearV3Controller {

    @Autowired
    private BidSectionServiceImpl bidSectionService;
    @Autowired
    IClearBidV3Service clearBidV3Service;
    @Autowired
    private TenderDocServiceImpl tenderDocService;


    /**
     * 推送项目到清标3.0
     *
     * @return 业务code
     */
    @RequestMapping("/pushBidSection")
    @UserLog(value = "'推送项目到清标3.0: bidSectionId='+#bidSectionId ", dmlType = DMLType.INSERT)
    public String pushClearV3(Integer bidSectionId) throws Exception {
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        return clearBidV3Service.createClearServer(bidSection,tenderDoc);
    }


}
