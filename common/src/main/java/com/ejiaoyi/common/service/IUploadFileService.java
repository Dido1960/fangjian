package com.ejiaoyi.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ejiaoyi.common.entity.UploadFile;

/**
 * <p>
 * 附件 服务类
 * </p>
 *
 * @author Z0001
 * @since 2020-03-31
 */
public interface IUploadFileService extends IService<UploadFile> {
    /**
     * 上传文件
     *
     * @param uploadFile
     * @return
     * @author lesgod
     * @date 2020/5/14 13:37
     */
    void insert(UploadFile uploadFile);


    /**
     * 查找文件上传依据
     *
     * @param id
     * @return
     * @author lesgod
     * @date 2020/5/14 13:37
     */
    UploadFile getUploadById(Integer id);

    /**
     * 更新上传文件信息
     * @param uploadFile 附件
     */
    void updateUpload(UploadFile uploadFile);

    /**
     * 通过uuid获取上传文件
     * @return
     * @author Mike
     */
    UploadFile getUploadByUid(String uuid);
}
