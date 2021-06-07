package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.entity.Fdfs;
import com.ejiaoyi.common.entity.FdfsSignature;
import com.ejiaoyi.common.mapper.FdfsSignatureMapper;
import com.ejiaoyi.common.service.IFDFSService;
import com.ejiaoyi.common.service.IFdfsSignatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * <p>
 * FDFS文件签章记录表 服务实现类
 * </p>
 *
 * @author Mike
 * @since 2021-04-26
 */
@Service
public class FdfsSignatureServiceImpl extends ServiceImpl<FdfsSignatureMapper, FdfsSignature> implements IFdfsSignatureService {
    @Autowired
    FdfsSignatureMapper fdfsSignatureMapper;

    @Autowired
    IFDFSService fdfsService;

    @Override
    public void addFdfsSignature(FdfsSignature fdfsSignature) {
        fdfsSignatureMapper.insert(fdfsSignature);
    }

    @Override
    public void addFdfsSignatureAndUpdateFdfs(FdfsSignature fdfsSignature, byte[] data) throws IOException {
        Fdfs oldFdfs = fdfsService.getFdfdById(fdfsSignature.getFdfsId());
        fdfsSignature.setOldPath(oldFdfs.getUrl());

        fdfsService.upload(data, oldFdfs.getName(), oldFdfs.getSuffix(), oldFdfs.getMark());
        String newPath = fdfsService.getUrlByMark(oldFdfs.getMark());
        fdfsSignature.setNowPath(newPath);

        this.addFdfsSignature(fdfsSignature);
    }
}
