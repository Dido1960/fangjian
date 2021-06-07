package com.ejiaoyi.worker.controller;

import com.ejiaoyi.worker.service.ILodopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 云打印基础控制器
 * @Auther: liuguoqiang
 * @Date: 2020-8-27 14:21
 */
@RestController
@RequestMapping("/lodop")
public class LodopController {

    @Autowired
    private ILodopService lodopService;

    /**
     * 插件下载
     * @param file 插件全名称
     */
    @RequestMapping("/lodopDownload")
    public void lodopDownload(HttpServletResponse response, @RequestParam("file") String file){
        lodopService.lodopDownload(response,file);
    }
}
