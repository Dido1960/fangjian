package com.ejiaoyi.common.service.impl;

import cn.hutool.http.HttpStatus;
import com.ejiaoyi.common.exception.CustomException;
import com.ejiaoyi.common.service.IDownloadService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载逻辑 实现类
 *
 * @author Z0001
 * @since 2020-4-23
 */
@Component
public class DownloadServiceImpl implements IDownloadService {

    @Override
    public void multiThreadDownload(String inPath, String outPath) throws Exception {
        this.multiThreadDownload(inPath, outPath, 3);
    }

    @Override
    public void multiThreadDownload(String inPath, String outPath, int threadCount) throws Exception {
        // 连接服务器地址
        URL url = new URL(inPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setRequestMethod("GET");
        int code = connection.getResponseCode();

        if (code != HttpStatus.HTTP_OK) {
            throw new CustomException(url + " connect failed : " + code);
        }

        // 获取服务器数据长度
        int length = connection.getContentLength();
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(outPath, "rwd")) {
            // 创建临时文件
            // 指定临时文件长度
            randomAccessFile.setLength(length);
        }

        // 计算平均线程下载大小
        int blockSize = length / threadCount;
        for (int threadIndex = 1; threadIndex <= threadCount; threadIndex++) {
            // 线程下载起始位置
            int startIndex = (threadIndex - 1) * blockSize;
            int endIndex = threadIndex * blockSize - 1;
            // 最后一个线程下载长度可能长一点
            if (threadIndex == threadCount) {
                endIndex = length - 1;
            }

            this.threadDownload(inPath, outPath, startIndex, endIndex);
        }
    }

    /**
     * 多线程分段下载
     *
     * @param inPath     输入地址
     * @param outPath    输出地址
     * @param startIndex 起始位置
     * @param endIndex   结束位置
     * @throws Exception 异常
     */
    @Async
    void threadDownload(String inPath, String outPath, int startIndex, int endIndex) throws Exception {
        // 连接服务器地址
        URL url = new URL(inPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setRequestMethod("GET");
        // 请求服务器文件指定部分
        connection.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);

        int code = connection.getResponseCode();

        if(code != HttpStatus.HTTP_PARTIAL) {
            throw new CustomException(url + " connect failed : " + code);
        }

        InputStream inputStream = connection.getInputStream();
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(outPath, "rwd")){
            // 设置临时文件写入开始位置
            randomAccessFile.seek(startIndex);

            int length = 0;
            byte[] buffer = new byte[1024];
            while ((length = inputStream.read(buffer)) != -1) {
                randomAccessFile.write(buffer, 0, length);
            }
            inputStream.close();
        }
    }
}
