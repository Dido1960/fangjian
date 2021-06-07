package com.ejiaoyi.worker.service;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020-8-27 14:29
 */
public interface ILodopService {
    /**
     * 插件下载
     * @param response
     * @param file
     */
    void lodopDownload(HttpServletResponse response, String file);
}
