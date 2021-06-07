package com.ejiaoyi.admin.controller;


import com.ejiaoyi.common.service.IOnlineInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 系统运行状态
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
@RestController
@RequestMapping("/sys")
public class SysInfoController {

    @Autowired
    IOnlineInfoService onlineInfoService;


    /**
     * 系统用户状态信息
     *
     * @return 角色管理页面
     */
    @RequestMapping("/onlinePage")
    public ModelAndView frameRolePage() {
        return new ModelAndView("/online/userOnlinePage");
    }

    /**
     * 获取角色JSON
     *
     * @return 角色JSON
     */
    @RequestMapping("/pagedOnlineInfo")
    public String pagedOnlineInfo() {
        return onlineInfoService.pagedOnlineInfo();
    }

}
