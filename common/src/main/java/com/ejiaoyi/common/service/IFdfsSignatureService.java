package com.ejiaoyi.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ejiaoyi.common.entity.FdfsSignature;

import java.io.IOException;

/**
 * <p>
 * Fdfs文件签章记录表 服务类
 * </p>
 *
 * @author Mike
 * @since 2021-04-26
 */
public interface IFdfsSignatureService extends IService<FdfsSignature> {

    /**
     * 保存签章记录信息
     * */
    void addFdfsSignature(FdfsSignature fdfsSignature);

    /**
     * 保存签章记录信息并更新上传表
     * */
    void addFdfsSignatureAndUpdateFdfs(FdfsSignature fdfsSignature, byte[] data) throws IOException;
}
