package com.ejiaoyi.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

/**
 * 手机证书加密操作
 *
 * @author Z0001
 * @since 2020-4-21
 */
@Slf4j
public class MobileCertUtil {

    public static void main(String[] args) throws Exception {
        FileUtil.setRootPath(System.getProperty("user.dir"));
        fileEncoder("12345678", "E:\\Users\\scqsk\\Desktop\\二建-投.gef", "E:\\Users\\scqsk\\Desktop\\des加密.mgef");
        //fileDecoder("C:\\Users\\LuoJiang\\Desktop\\des加密.mgef", "E:\\Users\\scqsk\\Desktop\\11\\二建-投解.gef");
    }

    /***
     * 源文件解密
     * @param encryptFile 加密文件
     * @param destFile  解密文件
     * **/
    public static void fileDecoder(String certPassWord, String encryptFile, String destFile) throws Exception {
        // 创建输入流
        InputStream inputStream = new FileInputStream(encryptFile);
        // 获取投标文件字节大小
        int fileLength = inputStream.available();
        // 创建缓冲区
        byte[] fileData = new byte[fileLength + 1];
        // 读取数据
        inputStream.read(fileData, 0, fileLength);
        // 关闭输入流
        inputStream.close();

        // 锁号内容长度
        String cipherFilePath = FileUtil.getCustomFilePath() + File.separator + UUID.randomUUID() + ".temp";
        DesUtil.decryptFile(certPassWord, cipherFilePath, destFile);
        FileUtil.deleteFile(cipherFilePath);
    }

    /***
     * 源文件加密
     * @param srcFile 加密文件
     * @param destFile  解密文件
     * **/
    public static void fileEncoder(String password, String srcFile, String destFile) throws Exception {
        String cipherFilePath = FileUtil.getCustomFilePath() + File.separator + UUID.randomUUID() + ".temp";
        DesUtil.encryptFile(password, srcFile, cipherFilePath);
        // 创建输入流
        InputStream inputStream = new FileInputStream(cipherFilePath);
        // 获取投标文件字节大小
        int fileLength = inputStream.available();
        // 创建缓冲区
        byte[] fileData = new byte[fileLength];
        // 读取数据
        inputStream.read(fileData, 0, fileLength);
        // 关闭输入流
        inputStream.close();
        //keyId
        byte[] keyIdBytes = new byte[8];
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        byte[] fileDataNew = ArrayUtils.addAll(keyIdBytes, ArrayUtils.addAll(passwordBytes, fileData));
        FileUtil.writeFile(fileDataNew, destFile);
        FileUtil.deleteFile(cipherFilePath);
    }


}
