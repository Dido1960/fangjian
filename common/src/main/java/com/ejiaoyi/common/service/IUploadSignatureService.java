package com.ejiaoyi.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ejiaoyi.common.entity.UploadSignature;

import java.io.IOException;

/**
 * <p>
 * 上传文件签章记录表 服务类
 * </p>
 *
 * @author langwei
 * @since 2020-07-14
 */
public interface IUploadSignatureService extends IService<UploadSignature> {

    /**
     * 保存签章记录信息
     * */
    void addUploadSignature(UploadSignature uploadSignature);

    /**
     * 保存签章记录信息并更新上传表
     * */
    void addUploadSignatureAndUpdateUpfile(UploadSignature uploadSignature,byte[] data) throws IOException;
}
