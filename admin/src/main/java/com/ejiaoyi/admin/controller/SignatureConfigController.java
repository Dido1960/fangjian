package com.ejiaoyi.admin.controller;

import com.ejiaoyi.admin.support.AuthUser;
import com.ejiaoyi.admin.support.CurrentUserHolder;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.entity.Reg;
import com.ejiaoyi.common.entity.SignatureConfigInfo;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.service.IRegService;
import com.ejiaoyi.common.service.impl.SignatureConfigInfoServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 印模签章配置信息（回执单签章） 控制层类
 * </p>
 *
 * @author Mike
 * @since 2021-02-18
 */
@RestController
@RequestMapping("/signatureConfig")
public class SignatureConfigController {
    @Autowired
    private SignatureConfigInfoServiceImpl signatureConfigInfoService;
    @Autowired
    private IRegService regService;

    /**
     * 印模签章配置框架页
     * @return
     */
    @RequestMapping("/frameSignatureConfigPage")
    public ModelAndView frameSignatureConfigPage() {
        return new ModelAndView("/signatureConfig/frameSignatureConfigPage");
    }

    /**
     * 印模签章配置树页
     * @return
     */
    @RequestMapping("/treeSignatureConfigPage")
    public ModelAndView treeSignatureConfigPage(){
        ModelAndView mav = new ModelAndView("/signatureConfig/treeSignatureConfig");
        AuthUser user = CurrentUserHolder.getUser();
        mav.addObject("regId", user.getRegId());
        return mav;
    }

    /**
     * 印模签章配置表页
     * @param signatureConfigInfo
     * @return
     */
    @RequestMapping("/signatureConfigTablePage")
    public ModelAndView signatureConfigTablePage(SignatureConfigInfo signatureConfigInfo){
        ModelAndView mav = new ModelAndView("/signatureConfig/signatureConfigTable");
        mav.addObject("signatureConfigInfo",signatureConfigInfo);
        return mav;
    }

    /**
     * 分页印模签章配置
     * @param signatureConfigInfo 部门对象
     * @return
     */
    @RequestMapping("/pagedSignatureConfig")
    public String pagedMonitor(SignatureConfigInfo signatureConfigInfo) {
        return signatureConfigInfoService.pagedSignatureConfigInfo(signatureConfigInfo);
    }

    /**
     * 修改
     * @param signatureConfigInfo
     * @return
     */
    @RequestMapping("/updateSignatureConfigInfo")
    @UserLog(value = "'修改印模签章配置信息: signatureConfigInfo='+#signatureConfigInfo.toString()", dmlType = DMLType.INSERT)
    public Boolean updateSignatureConfigInfo(SignatureConfigInfo signatureConfigInfo){
        return 1 == signatureConfigInfoService.updateSignatureConfigInfo(signatureConfigInfo);
    }

    /**
     * 添加页
     * @param signatureConfigInfo
     * @return
     */
    @RequestMapping("/addSignatureConfigInfoPage")
    public ModelAndView addSignatureConfigInfoPage(SignatureConfigInfo signatureConfigInfo){
        ModelAndView mav = new ModelAndView("/signatureConfig/addSignatureConfigInfo");
        Reg reg = regService.getRegByRegNo(signatureConfigInfo.getRegNo());
        signatureConfigInfo.setRegName(reg.getRegName());
        mav.addObject("signatureConfigInfo",signatureConfigInfo);
        return mav;
    }

    /**
     * 添加
     * @param signatureConfigInfo
     * @return
     */
    @RequestMapping("/addSignatureConfigInfo")
    @UserLog(value = "'添加印模签章配置信息: signatureConfigInfo='+#signatureConfigInfo.toString()", dmlType = DMLType.INSERT)
    public Boolean addSignatureConfigInfo(SignatureConfigInfo signatureConfigInfo){
        return 1 == signatureConfigInfoService.addSignatureConfigInfo(signatureConfigInfo);
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @RequestMapping("/deleteSignatureConfigInfo")
    @UserLog(value = "'删除印模签章配置信息: ids='+#ids", dmlType = DMLType.DELETE)
    public Boolean deleteSignatureConfigInfo(Integer[] ids){
        if (ids!=null){
            return ids.length == signatureConfigInfoService.deleteSignatureConfigInfo(ids);
        }
        return false;
    }

    /**
     * 修改页
     * @param id
     * @return
     */
    @RequestMapping("/updateSignatureConfigInfoPage")
    public ModelAndView updateSignatureConfigInfoPage(Integer id){
        ModelAndView mav = new ModelAndView("/signatureConfig/updateSignatureConfig");
        SignatureConfigInfo signatureConfigInfo = signatureConfigInfoService.getSignatureConfigInfoById(id);
        if (StringUtils.isNotEmpty(signatureConfigInfo.getRegNo())) {
            Reg reg = regService.getRegByRegNo(signatureConfigInfo.getRegNo());
            if (reg != null) {
                signatureConfigInfo.setRegName(reg.getRegName());
            }
        }

        mav.addObject("signatureConfigInfo", signatureConfigInfo);
        return mav;
    }

    /**
     * 查看页面
     * @param id
     * @return
     */
    @RequestMapping("/showSignatureConfigInfoPage")
    public ModelAndView showSignatureConfigInfoPage(Integer id){
        ModelAndView mav = new ModelAndView("/signatureConfig/showSignatureConfigInfo");
        SignatureConfigInfo signatureConfigInfo = signatureConfigInfoService.getSignatureConfigInfoById(id);
        if (StringUtils.isNotEmpty(signatureConfigInfo.getRegNo())) {
            Reg reg = regService.getRegByRegNo(signatureConfigInfo.getRegNo());
            if (reg != null) {
                signatureConfigInfo.setRegName(reg.getRegName());
            }
        }
        mav.addObject("signatureConfigInfo", signatureConfigInfo);
        return mav;
    }
}
