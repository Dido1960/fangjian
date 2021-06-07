package com.ejiaoyi.common.util.cfcaUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author qazhang
 * @Description 测试工具包，仅供测试使用
 * @CodeReviewer maben
 *
 */
public final class TextFileHelper {

    private TextFileHelper() {
    }

    /**
     * 读文件
     * 
     * @param file
     *            文件路径
     * @return 文件内容
     * @throws IOException
     */
    public  static final byte[] readFile(String file) throws IOException {

        int offset = 0;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] out = new byte[in.available()];

            if (out.length < offset + in.available()) {
                throw new IOException("Illegal Argument: filepath");
            }

            byte[] buffer = new byte[1024];
            int numRead;
            while ((numRead = in.read(buffer, 0, buffer.length)) >= 0) {
                System.arraycopy(buffer, 0, out, offset, numRead);
                offset += numRead;
            }
            return out;

        } finally {
            in.close();

        }

    }

    /**
     * 写文件
     * 
     * @param filePath
     *            文件输出路径
     * @param data
     *            文件内容
     * @throws IOException
     */
    public  static final void writeFile(String filePath, byte[] data) throws IOException {

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            out.write(data, 0, data.length);
        } finally {
            out.close();
        }

    }

}
