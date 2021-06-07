package com.ejiaoyi.common.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020/6/18 10:10
 */
@RestController
@RequestMapping("/errorPages")
public class ErrorController {
    @RequestMapping(value = "/{code}")
    public ModelAndView error(@PathVariable int code) {
        ModelAndView modelAndView=new ModelAndView("/errorPage/"+code);
        modelAndView.addObject("code",code);
        return modelAndView;
    }
}
