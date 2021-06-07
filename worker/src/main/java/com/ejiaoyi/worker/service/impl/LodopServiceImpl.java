package com.ejiaoyi.worker.service.impl;

import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import com.ejiaoyi.common.util.FileUtil;
import com.ejiaoyi.worker.service.ILodopService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020-8-27 14:29
 */
@Service
public class LodopServiceImpl extends BaseServiceImpl implements ILodopService {
    @Override
    public void lodopDownload(HttpServletResponse response, String file) {
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            //获取文件字节流
            String projectResourcePath = FileUtil.getProjectResourcePath();
            String ftlPath = projectResourcePath + "lodop/" + file;
            byte[] bytes = FileUtil.read2ByteArray(ftlPath);
            out.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
