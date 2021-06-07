package com.ejiaoyi.agency.controller;

import com.ejiaoyi.common.entity.Fdfs;
import com.ejiaoyi.common.entity.UploadFile;
import com.ejiaoyi.common.service.IFDFSService;
import com.ejiaoyi.common.service.impl.UploadFileServiceImpl;
import com.ejiaoyi.agency.support.AuthUser;
import com.ejiaoyi.agency.support.CurrentUserHolder;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/***
 * 登录控制器
 *
 * @author lesgod
 * @since 2020/3/26
 */
@RestController
@RequestMapping("/signar")
public class SignarController extends BaseController {

    @Autowired
    UploadFileServiceImpl uploadFileService;

    @Autowired
    IFDFSService  fdfsService;


    /**
     * 显示需要签名的PDF页面
     *
     * @return
     */
    @RequestMapping("showPdfPage")
    public ModelAndView showPdfPage(@Param("uploadFileId") Integer uploadFileId) {
        AuthUser authUser = CurrentUserHolder.getUser();
        ModelAndView modelAndView = new ModelAndView("/signar/showPdf");
        UploadFile uploadFile = uploadFileService.getUploadById(uploadFileId);
        modelAndView.addObject("url",  fdfsService.getUrlByUpload(uploadFileId));
        modelAndView.addObject("uploadFile", uploadFile);
        modelAndView.addObject("user",authUser);

        return modelAndView;
    }

    /**
     * 通过fdfs的mark获取文件地址
     * @param mark 文件标记
     * @return
     */
    @RequestMapping("/showPdfByMarkPage")
    public ModelAndView showPdfByMarkPage(@Param("mark") String mark) {
        AuthUser authUser = CurrentUserHolder.getUser();
        ModelAndView modelAndView = new ModelAndView("/signar/showPdfByMark");
        Fdfs fdfs = fdfsService.downloadByMark(mark);
        modelAndView.addObject("fdfs", fdfs);
        modelAndView.addObject("user",authUser);

        return modelAndView;
    }

}
