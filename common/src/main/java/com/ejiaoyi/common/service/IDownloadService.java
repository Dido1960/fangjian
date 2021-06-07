package com.ejiaoyi.common.service;

/**
 * 下载逻辑 接口
 *
 * @author Z0001
 * @since 2020-4-23
 */
public interface IDownloadService {

    /**
     * 多线程网络文件下载 默认线程数量: 3
     *
     * @param inPath      输入地址
     * @param outPath     输出地址
     */
    void multiThreadDownload(String inPath, String outPath) throws Exception;

    /**
     * 多线程网络文件下载
     *
     * @param inPath      输入地址
     * @param outPath     输出地址
     * @param threadCount 线程数量
     */
    void multiThreadDownload(String inPath, String outPath, int threadCount) throws Exception;
}
