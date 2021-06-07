package com.ejiaoyi.expert.controller;

import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.entity.ExpertUser;
import com.ejiaoyi.common.service.IExpertUserService;
import com.ejiaoyi.common.service.IFDFSService;
import com.ejiaoyi.common.service.ISignatureService;
import com.ejiaoyi.common.service.impl.UploadFileServiceImpl;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.RedisUtil;
import com.ejiaoyi.expert.support.AuthUser;
import com.ejiaoyi.expert.support.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 手写签名控制器
 *
 * @author Make
 * @since 2020/9/4
 */
@RestController
@RequestMapping("/signature")
public class SignatureController {

    @Autowired
    UploadFileServiceImpl uploadFileService;

    @Autowired
    IExpertUserService expertUserService;

    @Autowired
    IFDFSService fdfsService;

    @Autowired
    ISignatureService signatureService;


    /**
     * 获取专家签名数据
     *
     * @param bidSectionId 标段id
     * @param index 签名
     * @return
     */
    @ResponseBody
    @RequestMapping("/getPdfSignInfo")
    public Map<String, Object> getPdfSignInfo(Integer bidSectionId, Integer index) {
        Map<String, Object> map = new HashMap<>();
        AuthUser user = CurrentUserHolder.getUser();
        Object getPdfSignInfo = RedisUtil.get(CacheName.GET_REPORT_PDF_SIGN_INFO+user.getBidSectionId());
        // 获取上次点击，生成签名专家
        ExpertUser expertUser = expertUserService.getExpertUserById((Integer) getPdfSignInfo);
        if (!CommonUtil.isEmpty(getPdfSignInfo)) {
            assert user != null;
            if (!user.getUserId().equals(expertUser.getId())) {
                map.put("errorMsg", "<b>" + expertUser.getExpertName() + "</b>正在签名，请您耐心等待！");
                return map;
            }
        }
        return signatureService.getPdfSignInfo(bidSectionId,user.getUserId(), index);
    }

    /**
     * 手写签名
     * @param json
     * @param expertId
     * @param bidSectionId
     * @param expertSignatureImage
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/signReport")
    public boolean signReport(String json, Integer expertId, Integer bidSectionId, String expertSignatureImage) throws Exception {
        json = HtmlUtils.htmlUnescape(json);
        return signatureService.saveJsonReport(json, expertId, bidSectionId,expertSignatureImage);
    }

    /**
     * 合成签名PDF
     * @param bidSectionId 标段id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/signaturePdf")
    public boolean signReport(Integer bidSectionId) throws Exception {
        return signatureService.signrPdf(bidSectionId);
    }

    /**
     * 重新签名
     */
    @RequestMapping("/afreshSign")
    public void afreshSign(){
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        // 还原专家签名状态
        signatureService.updateExpertSigarStatus(user.getBidSectionId());
    }

    /**
     * 显示当前标段所有专家
     * @return
     */
    @RequestMapping("/chooseExpertUser")
    public ModelAndView chooseRolePage() {
        return new ModelAndView("/showExpertsTable/showReportExpert");
    }

    /**
     * 分页获取当前标段所有专家
     * @return
     */
    @RequestMapping("pageExpertUser")
    public String pageExpertUser(){
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        return  expertUserService.pageExpertUsers(user.getBidSectionId());
    }

    /**
     * 所有专家签名是否结束
     * @param bidSectionId 标段id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/allExpertUserSignEnd")
    public boolean allExpertUserSignEnd(Integer bidSectionId){
        return signatureService.expertSigarEnd(bidSectionId);
    }


}
