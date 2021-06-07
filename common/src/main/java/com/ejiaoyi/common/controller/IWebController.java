package com.ejiaoyi.common.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 项目文件信息 前端控制器
 * </p>
 *
 * @author Make
 * @since 2020-07-16
 */
@Controller
@RequestMapping("/iweb")
public class IWebController {
    /**
     * 显示需要签名的PDF页面
     *
     * @return
     */
    @RequestMapping("/iweb2018Page")
    public ModelAndView iweb2018Page() {
        ModelAndView   modelAndView= new ModelAndView("/signar/iweb2018");
        return modelAndView;
    }

}
