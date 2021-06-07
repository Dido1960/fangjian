package com.ejiaoyi.common.controller;


import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.common.entity.UploadFile;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.service.IFDFSService;
import com.ejiaoyi.common.service.IUploadFileService;
import com.ejiaoyi.common.util.DateTimeUtil;
import com.ejiaoyi.common.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * FastDFS文件 前端控制器
 * </p>
 *
 * @author Z0001
 * @since 2020-03-30
 */
@RestController
@RequestMapping("/fdfs")
public class FdfsController {
    /**
     * 文件预览服务器地址
     */
    @Value("${file.view.address}")
    private String fileViewAddress;

    @Autowired
    IFDFSService fdfsService;

    @Autowired
    IUploadFileService uploadFileService;

    /**
     * 在线编辑器 清空附件信息session
     */
    @RequestMapping("/cleanSession")
    public void cleanSession(HttpSession session) {
        session.removeAttribute("ewebeditorUpload");
    }


    /**
     * 文件上传页面
     * @author lesgod
     * @date 2020/5/14 12:29
     */
    @RequestMapping("/uploadFilePage")
    public ModelAndView uploadFilePage() {
        return new ModelAndView("/upload/uploadFile");
    }

    /**
     * ie9 uploadifiy 单文件上传页面
     * @return 文件长传页面
     */
    @RequestMapping("/ie9UploadFilePage")
    public ModelAndView ie9UploadFilePage(){
        return new ModelAndView("/upload/ie9UploadFile");
    }


    /**
     * ie9 layui 单文件上传页面
     * @return 文件长传页面
     */
    @RequestMapping("/ie9UploadFileByLayuiPage")
    public ModelAndView ie9UploadFileByLayuiPage(){
        return new ModelAndView("/upload/ie9UploadFileByLayui");
    }

    /**
     * 上传附件
     *
     * @param request
     * @return
     */
    @RequestMapping("/uploadFile")
    public Map<String, Object> uploadFile(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<>(1);

        //文件索引
        String path = File.separator + "uploads"
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.YYYY)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.MM)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.DD);
        String isLocal = request.getParameter("isLocal");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();

            String fileName = file.getOriginalFilename();
            String fileExt = FileUtil.getSuffix(fileName);
            String uploadRecordUUID = UUID.randomUUID().toString();
            path = path + File.separator + uploadRecordUUID + "." + fileExt.toLowerCase();

            fdfsService.upload(file, path);
            // 将文件缓存到本地
            boolean localStatus = StringUtils.isNotBlank(isLocal) && ("true".equals(isLocal) || "1".equals(isLocal));
            if (localStatus) {
                FileUtil.writeFile(file.getBytes(), FileUtil.getCustomFilePath() + path);
            }
            // 附件信息存库,作为文件快速索引存在
            UploadFile uploadFile = UploadFile.builder()
                    .name(fileName)
                    .path(path)
                    .suffix(fileExt.toLowerCase())
                    .byteSize(Integer.parseInt(String.valueOf(file.getSize())))
                    .readSize(FileUtil.getReadSize(file.getSize()))
                    .build();
            uploadFileService.insert(uploadFile);
            map.put("file", uploadFile);
        }
        return map;
    }

    /**
     * ie9 上传单附件
     *
     * @param request
     * @return
     */
    @RequestMapping("/ie9UploadFile")
    @ResponseBody
    public String ie9UploadFile(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<>(1);

        //文件索引
        String path = File.separator + "uploads"
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.YYYY)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.MM)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.DD);
        String isLocal = request.getParameter("isLocal");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();

            String fileName = file.getOriginalFilename();
            String fileExt = FileUtil.getSuffix(fileName);
            String uploadRecordUUID = UUID.randomUUID().toString();
            path = path + File.separator + uploadRecordUUID + "." + fileExt.toLowerCase();

            fdfsService.upload(file, path);
            // 将文件缓存到本地
            boolean localStatus = StringUtils.isNotBlank(isLocal) && ("true".equals(isLocal) || "1".equals(isLocal));
            if (localStatus) {
                FileUtil.writeFile(file.getBytes(), FileUtil.getCustomFilePath() + path);
            }
            // 附件信息存库,作为文件快速索引存在
            UploadFile uploadFile = UploadFile.builder()
                    .name(fileName)
                    .path(path)
                    .suffix(fileExt.toLowerCase())
                    .byteSize(Integer.parseInt(String.valueOf(file.getSize())))
                    .readSize(FileUtil.getReadSize(file.getSize()))
                    .build();
            uploadFileService.insert(uploadFile);
            map.put("file", uploadFile);
        }
        return JSONObject.toJSONString(map);
    }


    /**
     * ie9 上传单附件
     *
     * @param request
     * @return
     */
    @RequestMapping(value = { "/ie9UploadFileByLayui" }, produces="text/html;charset=UTF-8")
    @ResponseBody
    public String ie9UploadFileByLayui(HttpServletRequest request ) throws Exception {
        Map<String, Object> map = new HashMap<>(1);

        //文件索引
        String path = File.separator + "uploads"
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.YYYY)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.MM)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.DD);
        String isLocal = request.getParameter("isLocal");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();

            String fileName = file.getOriginalFilename();
            int i = fileName.lastIndexOf("\\");
            if (i > 0){
                fileName = fileName.substring(i+1);
            }
            String fileExt = FileUtil.getSuffix(fileName);
            String uploadRecordUUID = UUID.randomUUID().toString();
            path = path + File.separator + uploadRecordUUID + "." + fileExt.toLowerCase();

            fdfsService.uploadByLayui(file, path);
            // 将文件缓存到本地
            boolean localStatus = StringUtils.isNotBlank(isLocal) && ("true".equals(isLocal) || "1".equals(isLocal));
            if (localStatus) {
                FileUtil.writeFile(file.getBytes(), FileUtil.getCustomFilePath() + path);
            }
            // 附件信息存库,作为文件快速索引存在
            UploadFile uploadFile = UploadFile.builder()
                    .name(fileName)
                    .path(path)
                    .suffix(fileExt.toLowerCase())
                    .byteSize(Integer.parseInt(String.valueOf(file.getSize())))
                    .readSize(FileUtil.getReadSize(file.getSize()))
                    .build();
            uploadFileService.insert(uploadFile);
            map.put("file", uploadFile);
        }
        return JSONObject.toJSONString(map);
    }

    /**
     * 获取上传索引信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/getUploadFile")
    public UploadFile getUploadFile(Integer id) {
        return uploadFileService.getUploadById(id);
    }


    /**
     * 上传文件下载
     *
     * @return
     * @author lesgod
     * @date 2020/5/15 13:57
     */
    @RequestMapping("/uploadFileDown")
    public String uploadFileDown(Integer id) {
        UploadFile uploadFile = uploadFileService.getUploadById(id);
        return fdfsService.getUrlByMark(uploadFile.getPath());
    }

    @RequestMapping("/downFileByUploadId")
    public void downFileByUploadId(HttpServletResponse response, @RequestParam("id") Integer id) throws IOException {
        UploadFile uploadFile = uploadFileService.getUploadById(id);
        OutputStream out = response.getOutputStream();
        out.write(fdfsService.downloadByUrl(fdfsService.getUrlByMark(uploadFile.getPath())));
    }

    /**
     * 获取mark对应的外网地址
     *
     * @return
     */
    @RequestMapping("/getMarkUrl")
    public String getMarkUrl(String markUrl) {
        return fdfsService.getUrlByMark(markUrl);
    }

    /**
     * 通过FDFS的id获取对应的外网地址
     *
     * @return
     */
    @RequestMapping("/getIdUrl")
    public String getIdUrl(Integer id) {
        return fdfsService.getFdfdById(id).getUrl();
    }

    /**
     * 上传文件预览
     * @author lgq
     * @return 预览文件地址
     */
    @RequestMapping("/showUploadFile")
    public String showUploadFile(Integer id) {
        UploadFile uploadFile = uploadFileService.getUploadById(id);
        return fileViewAddress + "?url=" + fdfsService.getUrlByMark(uploadFile.getPath());
    }

}
