package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.entity.UploadFile;
import com.ejiaoyi.common.entity.UploadSignature;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.mapper.UploadSignatureMapper;
import com.ejiaoyi.common.service.IFDFSService;
import com.ejiaoyi.common.service.IUploadFileService;
import com.ejiaoyi.common.service.IUploadSignatureService;
import com.ejiaoyi.common.util.DateTimeUtil;
import com.ejiaoyi.common.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * <p>
 * 上传文件签章记录表 服务实现类
 * </p>
 *
 * @author langwei
 * @since 2020-07-14
 */
@Service
public class UploadSignatureServiceImpl extends ServiceImpl<UploadSignatureMapper, UploadSignature> implements IUploadSignatureService {
    @Autowired
    UploadSignatureMapper uploadSignatureMapper;

    @Autowired
    IFDFSService fdfsService;
    @Autowired
    IUploadFileService uploadFileService;

    @Override
    public void addUploadSignature(UploadSignature uploadSignature) {
        uploadSignatureMapper.insert(uploadSignature);
    }

    @Override
    public void addUploadSignatureAndUpdateUpfile(UploadSignature uploadSignature, byte[] data) throws IOException {


        //文件索引
        String path = File.separator + "uploads"
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.YYYY)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.MM)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.DD);


        UploadFile uploadFile = uploadFileService.getUploadById(uploadSignature.getUploadId());
        path += File.separator + UUID.randomUUID() + "." + uploadFile.getSuffix();

        String oldPath = fdfsService.getUrlByMark(uploadFile.getPath());
        uploadSignature.setOldPath(oldPath);

        fdfsService.upload(data, uploadFile.getName(), uploadFile.getSuffix(), path);
        String newPath = fdfsService.getUrlByMark(path);
        uploadSignature.setNowPath(newPath);

        uploadFile.setPath(path);
        uploadFile.setByteSize(data.length);
        uploadFile.setReadSize(FileUtil.getReadSize(data.length));
        // 附件信息存库,作为文件快速索引存在
        uploadFileService.updateUpload(uploadFile);
        this.addUploadSignature(uploadSignature);
    }
}
