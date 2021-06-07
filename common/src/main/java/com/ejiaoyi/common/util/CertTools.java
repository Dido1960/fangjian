package com.ejiaoyi.common.util;

import com.ejiaoyi.common.dto.DecoderCipherInfoDTO;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

/**
 * 证书工具
 *
 * @author Xie
 * @date 2020/09/23
 **/
public class CertTools {

    /**
     * 文件解密（BJca解密）
     * 如将sgef 转换为gef
     *
     * @param encryptFile 解密的文件
     * @param outPath 输出路径
     * @param privateKey 从加密因子中获取到的公钥（需要从对应的锁中获取），对应方法SOF_DecryptData(CERT_NO_INDEX, cipher,call)
     * @throws IOException
     */
    public static void fileDecoder(File encryptFile, String outPath, String privateKey) {
        //1.第一步去掉含有加密因子的加密文件
        String cipherFilePath = FileUtil.getCustomFilePath() + File.separator + UUID.randomUUID();
        try {
            decoderCipher(encryptFile, cipherFilePath);
            //2.将加密文件变为备用文件
            boolean flag = decry(privateKey, cipherFilePath, outPath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtil.deleteFile(cipherFilePath);
        }
    }


    /**
     * 使用BJCA 进行文件解密
     *
     * @param privateKey
     * @param cipherFile  信封加密信息
     * @param fileout
     * @return
     */
    private static boolean decry(String privateKey, String cipherFile, String fileout) {
        ActiveXComponent com = new ActiveXComponent("clsid:3F367B74-92D9-4C5E-AB93-234F8A91D5E6");
        Dispatch XTXAPP = com.getObject();
        Dispatch.call(XTXAPP, "SOF_SetEncryptMethod", 0x4001);
        Dispatch.call(XTXAPP, "SetUserConfig", 8, "0");
        Variant sof_symDecryptFile = Dispatch.call(XTXAPP, "SOF_SymDecryptFile", privateKey, cipherFile, fileout);
        return sof_symDecryptFile.getBoolean();
    }

    /**
     * 返回加密因子
     * @param encryptFile 加密文件
     * @param outPath 输出路径
     * @return
     * @throws IOException
     */
    private static DecoderCipherInfoDTO decoderCipher(File encryptFile, String outPath) throws IOException {
        // 创建输入流
        InputStream inputStream = new FileInputStream(encryptFile);
        // 获取投标文件字节大小
        int fileLength = (int) encryptFile.length();
        // 创建缓冲区
        byte[] fileData = new byte[fileLength + 1];
        // 读取数据
        inputStream.read(fileData, 0, fileLength);
        // 关闭输入流
        inputStream.close();

        /**
         * 加密文件包(长度0-47存储文件长度， 48及以后正式内容)
         *
         * 文件头内容（2位）
         * 版本号内容（1位）
         * 文件类型内容（1位）
         * 锁号内容 长度 4
         * 解密因子 长度 4
         * 信封加密文件 长度 4
         * MD5值 长度 4
         */

        // 锁号内容长度
        int certContentLen = FileUtil.byteToInt(fileData, 4);
        // 解密因子长度
        int cipherLen = FileUtil.byteToInt(fileData, 8);
        // 信封加密文件长度
        int tenderFileLen = FileUtil.byteToInt(fileData, 12);
        // MD5值长度
        int md5Len = FileUtil.byteToInt(fileData, 16);

        // 初始化偏移位置
        int position = 48;

        byte[] fileInfoByte = Arrays.copyOfRange(fileData, 0, 4);
        /*String fileInfo = new String(fileInfoByte);*/

        // 读取文件头信息
        /*String fileHeader = fileInfo.substring(0, 1);*/

        // 读取版本号信息
       /* String version = fileInfo.substring(2, 2);*/

        // 读取文件类信息
        byte typeByte = fileInfoByte[3];
        String fileType = String.valueOf(typeByte);

        // 读取锁号内容型信息
        byte[] certContentByte = Arrays.copyOfRange(fileData, position, position + certContentLen);
        String certContent = new String(certContentByte);
        position += certContentLen;

        // 读取解密因子信息
        byte[] cipherByte = Arrays.copyOfRange(fileData, position, position + cipherLen);
        String cipher = new String(cipherByte);
        position += cipherLen;

        if (StringUtils.isNotEmpty(outPath)) {
            // 把文件内容装填到文件中
            OutputStream outputStream = new FileOutputStream(outPath);
            outputStream.write(fileData, position, tenderFileLen);
            position += tenderFileLen;
            outputStream.flush();
            outputStream.close();
        }


        // 读取MD5值信息
        /*byte[] md5Byte = Arrays.copyOfRange(fileData, position, position + md5Len);
        String md5 = new String(md5Byte);
        position += md5Len;*/

        return DecoderCipherInfoDTO.builder()
                /*.fileHeader(fileHeader)
                .version(version)*/
                .fileType(fileType)
                .cipher(cipher)
                .certId(certContent)
                /*.md5(md5)*/
                .build();

    }


    /**
     * 从加密文件中获取加密因子
     *
     * @param filePath 文件路径
     */
    public String getCipher(String filePath) throws IOException {
        return getCipher(new File(filePath));
    }

    /**
     * 从加密文件中获取加密因子
     * @param encryptFile 加密文件
     * @return
     * @throws IOException
     */
    public String getCipher(File encryptFile) throws IOException {
        String cipher = null;
        // 创建输入流
        InputStream inputStream = new FileInputStream(encryptFile);
        // 获取投标文件字节大小
        int fileLength = (int) encryptFile.length();
        // 创建缓冲区
        byte[] fileData = new byte[fileLength + 1];
        // 读取数据
        inputStream.read(fileData, 0, fileLength);
        // 关闭输入流
        inputStream.close();

        /**
         * 加密文件包
         * cipher 长度 4
         * 信封加密文件 长度 4
         * sym_key
         * 加密文件
         */

        // cipher长度
        int cipherLen = FileUtil.byteToInt(fileData, 0);
        // 加密文件长度
        int fileLen = FileUtil.byteToInt(fileData, 4);

        // 初始化偏移位置
        int position = 8;

        // 读取招标锁信息
        byte[] symkeyByte = Arrays.copyOfRange(fileData, position, position + cipherLen);

        cipher = new String(symkeyByte);
        position += cipherLen;

        return cipher;
    }


    /**
     * 文件解密（互认ca解密）
     * 如将sgef 转换为gef
     *
     * @param encryFile 解密的文件
     * @param outPath 输出路径
     * @throws IOException
     */
    public static DecoderCipherInfoDTO otherFileDecoder(File encryFile, String outPath) {
        try {
            return decoderCipher(encryFile, outPath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
