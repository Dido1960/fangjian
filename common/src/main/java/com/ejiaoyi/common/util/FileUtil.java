package com.ejiaoyi.common.util;

import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.crypto.SM3Digest;
import com.ejiaoyi.common.entity.ExpertUser;
import com.ejiaoyi.common.enums.TimeFormatter;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作工具类
 *
 * @author Z0001
 * @since 2020/3/30
 */
public class FileUtil {

    private static String rootPath;

    private static final String FILE_POINT = ".";

    public static String getRootPath() {
        return rootPath;
    }

    public static void setRootPath(String rootPath) {
        FileUtil.rootPath = rootPath;
    }

    public static String getClassPath() {
        try {
            return ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX).getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCurrentPath() {

        Class<?> caller = getCaller();
        if (caller == null) {
            caller = FileUtil.class;
        }

        return getCurrentPath(caller);
    }

    public static Class<?> getCaller() {
        StackTraceElement[] stack = (new Throwable()).getStackTrace();
        if (stack.length < 3) {
            return FileUtil.class;
        }
        String className = stack[2].getClassName();
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrentPath(Class<?> cls) {
        String path = cls.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = path.replaceFirst("file:/", "");
        path = path.replaceAll("!/", "");
        if (path.lastIndexOf(File.separator) >= 0) {
            path = path.substring(0, path.lastIndexOf(File.separator));
        }
        if ("/".equalsIgnoreCase(path.substring(0, 1))) {
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("window")) {
                path = path.substring(1);
            }
        }
        return path;
    }

    /**
     * 获取本地文件生成目录
     *
     * @return 本地文件生成目录
     */
    public static String getCustomFilePath() {
        try {
            return rootPath + "\\custom-file\\";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取本地资源文件目录
     *
     * @return 本地文件生成目录
     */
    public static String getProjectResourcePath() {
        try {
            return rootPath + "\\project-resource\\";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过字节大小 计算可读性大小 自动匹配单位
     *
     * @param byteSize 字节大小
     * @return 文件可读大小
     */
    public static String getReadSize(long byteSize) {
        int decimal = 1024;
        double readSize = CalcUtil.divide(byteSize, decimal);
        String unit = "KB";

        if (readSize > decimal) {
            readSize = CalcUtil.divide(readSize, decimal);
            unit = "MB";
            if (readSize > decimal) {
                readSize = CalcUtil.divide(readSize, decimal);
                unit = "GB";
                if (readSize > decimal) {
                    readSize = CalcUtil.divide(readSize, decimal);
                    unit = "TB";
                }
            }
        }
        return readSize + unit;
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName 文件名
     * @return 文件后缀名
     */
    public static String getSuffix(String fileName) {
        String suffix = "";

        if (StringUtils.isNotEmpty(fileName)) {
            int index = fileName.lastIndexOf(".");

            if (index > 0 && index < fileName.length() - 1) {
                suffix = fileName.substring(index + 1);
            }
        }

        return suffix;
    }

    /**
     * 获取文件的MD5
     *
     * @param path 文件路径
     * @return 文件MD5
     */
    public static String getMD5(String path) throws IOException {
        return DigestUtils.md5Hex(new FileInputStream(path));
    }

    /**
     * 获取文件的SHA1
     *
     * @param path 文件路径
     * @return 文件SHA1
     */
    public static String getSHA1(String path) throws IOException {
        return DigestUtils.sha1Hex(new FileInputStream(path));
    }

    /**
     * 创建文件
     *
     * @param path 文件路径
     * @throws IOException IO流异常
     */
    public static boolean createFile(String path) throws IOException {
        File file = new File(path);

        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            return file.createNewFile();
        }

        return true;
    }

    /**
     * 创建目录
     *
     * @param path 文件路径
     */
    public static boolean createDir(String path) {
        if (StringUtils.contains(path, ".")) {
            path = new File(path).getParentFile().getPath();
        }

        File file = new File(path);

        if (!file.exists()) {
            return file.mkdirs();
        } else {
            return true;
        }
    }

    /**
     * 文件复制
     *
     * @param inputFile  输入文件
     * @param outputFile 输出文件
     */
    public static void copyFile(String inputFile, String outputFile) {
        try {
            deleteFile(outputFile);
            System.gc();
            Thread.sleep(500);
            copyFile(new FileInputStream(inputFile), outputFile);
        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭文件流
     *
     * @param in
     * @param out
     * @param fi
     * @param fo
     */
    private static void closeSteam(FileChannel in, FileChannel out, FileInputStream fi, FileOutputStream fo) {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (fi != null) {
                fi.close();
            }
            if (fo != null) {
                fo.close();
            }
        } catch (IOException ex) {
//            logger.error( "关闭文件资源失败。", ex );
        } finally {
            System.gc();//启动垃圾回收
        }
    }

    /**
     * 把文件流生成到磁盘中
     *
     * @param fi         输入文件流
     * @param outputFile 输出文件地址
     */
    public static void copyFile(FileInputStream fi, String outputFile) {
        File tFile = new File(outputFile);
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            if (!tFile.getParentFile().exists()) {
                tFile.getParentFile().mkdirs();
            }
            fo = new FileOutputStream(tFile);
            // 得到对应的文件通道
            in = fi.getChannel();
            // 得到对应的文件通道
            out = fo.getChannel();
            // 连接两个通道，并且从in通道读取，然后写入out通道
            in.transferTo(0, in.size(), out);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeSteam(in, out, fi, fo);
        }
    }

    /**
     * 删除本地文件
     *
     * @param file 需要删除的文件
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    /***
     * 删除文件
     * ***/
    public static void deleteFile(String filePath) {
        deleteFile(new File(filePath));
    }

    /**
     * 读取文件内容为二进制数组
     *
     * @param filePath 文件地址
     * @return
     * @throws IOException
     */
    public static byte[] read2ByteArray(String filePath) throws IOException {
        InputStream in = new FileInputStream(filePath);
        byte[] data = inputStream2ByteArray(in);
        in.close();

        return data;
    }

    /**
     * 流转二进制数组
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] inputStream2ByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    /**
     * 把文件写入流
     *
     * @param buffer
     * @param outFile
     * @throws Exception
     */
    public static void writeFile(byte[] buffer, String outFile) throws Exception {
        createDir(outFile);
        File oFile = new File(outFile);
        OutputStream toClient = new FileOutputStream(oFile);
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
    }

    /**
     * 递归删除文件
     *
     * @param dir
     */
    public static void removeDir(File dir) {
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();

        if (files == null) {
            return;
        }

        //遍历
        for (File file : files) {
            //判断是否为文件夹？
            if (file.isDirectory()) {
                //递归
                removeDir(file);
            } else {
                //如果不是文件夹，就删除。
                file.delete();
            }
        }

        dir.delete();

    }

    /**
     * 获取招标文件解密路径
     *
     * @param uploadFileId 招标文件上传附件id
     * @return 返回路径
     */
    public static String getDecryptTenderDocPath(String uploadFileId) {
        return getCustomFilePath() + DateTimeUtil.getInternetTime(TimeFormatter.YYYY)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.MM)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.DD)
                + File.separator + "decrypt"
                + File.separator + ProjectFileTypeConstant.TENDER_DOC
                + File.separator + uploadFileId;
    }

    /**
     * 获取tjf mtjf投标文件解密路径
     *
     * @param uploadFileId 投标文件上传附件id
     * @param tenderType   ProjectFileTypeConstant 解压文件类型
     * @return 返回路径
     */
    public static String getTenderFilePath(String uploadFileId, String tenderType) {
        return getCustomFilePath() + DateTimeUtil.getInternetTime(TimeFormatter.YYYY)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.MM)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.DD)
                + File.separator + tenderType
                + File.separator + uploadFileId;
    }

    /**
     * 获取投标文件解密路径
     *
     * @param uploadFileId 投标文件上传附件id
     * @return 返回路径
     */
    public static String getDecryptBidderFilePath(String uploadFileId) {
        return getCustomFilePath() + DateTimeUtil.getInternetTime(TimeFormatter.YYYY)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.MM)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.DD)
                + File.separator + "decrypt"
                + File.separator + ProjectFileTypeConstant.BIDDER_FILE
                + File.separator + uploadFileId;
    }

    /**
     * 获取唱标文件生成路径
     *
     * @param bidSectionId 标段主键id
     * @return 返回路径
     */
    public static String getVoiceFilePath(String bidSectionId) {
        return getCustomFilePath() + DateTimeUtil.getInternetTime(TimeFormatter.YYYY)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.MM)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.DD)
                + File.separator + ProjectFileTypeConstant.VOICE
                + File.separator + bidSectionId;
    }

    /**
     * 获取开标记录表路径
     *
     * @param bidSectionId 标段主键id
     * @return 返回路径
     */
    public static String getBidOpenRecordFilePath(String bidSectionId) {
        return getCustomFilePath() + DateTimeUtil.getInternetTime(TimeFormatter.YYYY)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.MM)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.DD)
                + File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD
                + File.separator + bidSectionId;
    }

    /**
     * 获取评标报告路径
     *
     * @param bidSectionId 标段主键id
     * @return 返回路径
     */
    public static String getEvalReportFilePath(String bidSectionId) {
        return getCustomFilePath() + DateTimeUtil.getInternetTime(TimeFormatter.YYYY)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.MM)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.DD)
                + File.separator + ProjectFileTypeConstant.EVAL_REPORT
                + File.separator + bidSectionId;
    }

    /**
     * 回退前所有专家回退数据
     *
     * @param bidSectionId 标段主键id
     * @return 返回路径
     */
    public static String getBackAllEvalReportFilePath(Integer bidSectionId) {
        return getCustomFilePath() + DateTimeUtil.getInternetTime(TimeFormatter.YYYY)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.MM)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.DD)
                + File.separator + ProjectFileTypeConstant.BEFORE_ROLLBACK_DATA
                + File.separator + bidSectionId
                + File.separator + ProjectFileTypeConstant.BACK_DATA;
    }

    /**
     * 获取回退历史数据路径
     *
     * @param bidSectionId 标段主键id
     * @return 返回路径
     */
    public static String getBeforeRollbackDataFilePath(Integer bidSectionId) {
        return getCustomFilePath() + DateTimeUtil.getInternetTime(TimeFormatter.YYYY)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.MM)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.DD)
                + File.separator + ProjectFileTypeConstant.BEFORE_ROLLBACK_DATA
                + File.separator + bidSectionId;
    }

    /**
     * 获取每个专家回退历史数据pdf路径
     *
     * @param expertUser 评审专家
     * @return 返回路径
     */
    public static String getExpertUserBeforeDataPdfPath(ExpertUser expertUser) {
        return getCustomFilePath() + DateTimeUtil.getInternetTime(TimeFormatter.YYYY)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.MM)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.DD)
                + File.separator + ProjectFileTypeConstant.BEFORE_ROLLBACK_DATA
                + File.separator + expertUser.getBidSectionId()
                + File.separator +expertUser.getId();
    }

    /**
     * 获取复会报告路径
     *
     * @param bidSectionId 标段主键id
     * @return 返回路径
     */
    public static String getResumptionReportFilePath(String bidSectionId) {
        return getCustomFilePath() + DateTimeUtil.getInternetTime(TimeFormatter.YYYY)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.MM)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.DD)
                + File.separator + ProjectFileTypeConstant.RESUMPTION_REPORT
                + File.separator + bidSectionId;
    }


    /**
     * 遍历文件夹下面的所有文件
     *
     * @param dirFile 文件夹
     * @return 文件集合
     */
    public static List<File> listDirFile(File dirFile) {
        List<File> files = new ArrayList<>();
        if (!dirFile.exists()) {
            return files;
        }

        File[] fileArr = dirFile.listFiles();
        if (fileArr == null) {
            return files;
        }

        for (File file : fileArr) {
            if (file.isDirectory()) {
                files.addAll(listDirFile(file));
            } else {
                files.add(file);
            }
        }

        return files;
    }

    /**
     * 返回指定目录下，所有文件名
     *
     * @param dirPath
     * @return
     */
    public static List<String> listFileName(String dirPath) {
        List<String> list = new ArrayList<>();
        File f = new File(dirPath);
        if (f.exists()) {
            File[] fa = f.listFiles();
            if (fa == null) {
                return null;
            }
            for (File fs : fa) {
                if (!fs.isDirectory()) {
                    list.add(fs.getName());
                }
            }
        }
        return list;
    }

    /**
     * 判断当前目录是否存在
     *
     * @param dirPath 目录路径
     * @return
     */
    public static boolean isExistDir(String dirPath) {
        File file = new File(dirPath);
        return file.exists();
    }


    /**
     * 获取wav音频时长
     *
     * @param filePath 文件路径
     * @return
     * @throws EncoderException
     */
    public static Integer getWavDuration(String filePath) throws EncoderException, FileNotFoundException, it.sauronsoftware.jave.EncoderException {
        File source = new File(filePath);
        return getWavDuration(source);
    }

    /**
     * 获取wav音频时长
     *
     * @param source 文件
     * @return
     * @throws EncoderException
     */
    public static Integer getWavDuration(File source) throws EncoderException, FileNotFoundException, it.sauronsoftware.jave.EncoderException {
        Encoder encoder = new Encoder();
        MultimediaInfo m = encoder.getInfo(source);
        // 延迟唱标时长 1s
        return Math.toIntExact(m.getDuration()) + 1000;
    }

    /**
     * 获取指定目录下，特定格式的文件
     *
     * @param dirPath  文件目录
     * @param fileType 文件类型
     * @return
     */
    public static List<File> listFileByFormat(String dirPath, String fileType) {
        // 默认筛选pdf格式文件
        if (CommonUtil.isEmpty(fileType)) {
            fileType = "pdf";
        }
        List<File> listFile = new ArrayList<>();
        //读取输入路径的文件
        File[] list = new File(dirPath).listFiles();
        for (File file : list) {
            if (file.isDirectory()) {
                listFile.addAll(listFileByFormat(file.getAbsolutePath(), "pdf"));
            }else if (file.isFile()) {
                if (file.getName().endsWith(fileType)) {
                    // 就输出该文件的绝对路径
                    System.out.println(file.getAbsolutePath());
                    listFile.add(file);
                }
            }
        }
        return listFile;
    }

    /**
     * 获取指定目录下，所有特定格式的绝对路径
     *
     * @param dirPath  文件目录
     * @param fileType 文件类型
     * @return
     */
    public static List<String> listAbsolutePath(String dirPath, String fileType) {
        // 默认筛选pdf格式文件
        if (CommonUtil.isEmpty(fileType)) {
            fileType = "pdf";
        }
        List<String> listPaht = new ArrayList<>();
        //读取输入路径的文件
        File[] list = new File(dirPath).listFiles();
        for (File file : list) {
            if (file.isFile()) {
                if (file.getName().endsWith(fileType)) {
                    // 就输出该文件的绝对路径
                    listPaht.add(file.getAbsolutePath());
                }
            }
        }
        return listPaht;
    }

    /**
     * 通过字节数组获取文件MD5
     *
     * @param bytes
     * @return
     */
    public static String getFileMD5(byte[] bytes) {
        if (bytes.length == 0) {
            return null;
        }
        try {
            return DigestUtils.md5Hex(bytes).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从二进制文件获取int值
     *
     * @param fileData
     * @param position
     * @return
     */
    public static int byteToInt(byte[] fileData, int position) {
        int value = 0;

        value = (int) ((fileData[position] & 0xFF)
                | ((fileData[position + 1] & 0xFF) << 8)
                | ((fileData[position + 2] & 0xFF) << 16)
                | ((fileData[position + 3] & 0xFF) << 24));
        return value;
    }

    /**
     * 文件写入
     *
     * @param path
     * @param str
     * @throws IOException
     */
    public static void bufferedWrite(String path, String str) throws IOException {
        File file = new File(path);

        //文件不存在就新建文件
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }

        OutputStreamWriter writer = null;
        BufferedWriter bw = null;
        try {
            OutputStream os = new FileOutputStream(file);
            writer = new OutputStreamWriter(os);
            bw = new BufferedWriter(writer);
            bw.write(str);
            bw.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取文件
     *
     * @param filePath
     */
    public static String readTxtFile(String filePath) {
        StringBuffer buffer = new StringBuffer();
        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file));
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    buffer.append(lineTxt);
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 获取文件的SM3
     *
     * @param path 文件路径
     * @return 文件SM3
     */
    public static String getSM3(String path) {
        File file = new File(path);

        byte[] b = new byte[32];
        SM3Digest sm3 = new SM3Digest();
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] fileBytes = new byte[1024];
            int n;
            while ((n = fis.read(fileBytes)) != -1)
            {
                bos.write(fileBytes, 0, n);
                sm3.update(fileBytes, 0 ,n);
            }
            fis.close();
            bos.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        sm3.doFinal(b, 0);
        String s = new String(Hex.encode(b));

        return s.toUpperCase();
    }

    /**
     * 通过文件输入流获取文件的SM3
     *
     * @param fis 文件输入流
     * @return 文件SM3
     */
    public static String getSM3(FileInputStream fis) {
        byte[] b = new byte[32];
        SM3Digest sm3 = new SM3Digest();
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] fileBytes = new byte[1024];
            int n;
            while ((n = fis.read(fileBytes)) != -1)
            {
                bos.write(fileBytes, 0, n);
                sm3.update(fileBytes, 0 ,n);
            }
            fis.close();
            bos.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        sm3.doFinal(b, 0);
        String s = new String(Hex.encode(b));

        return s.toUpperCase();
    }

    /**
     * 通过文件字节获取文件的SM3
     *
     * @param bytes 文件字节流
     * @return 文件SM3
     */
    public static String getSM3(byte[] bytes) {
        byte[] b = new byte[32];
        SM3Digest sm3 = new SM3Digest();
        sm3.update(bytes, 0, bytes.length);
        sm3.doFinal(b, 0);
        String s = new String(Hex.encode(b));

        return s.toUpperCase();
    }
}
