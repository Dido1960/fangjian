package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.entity.UploadFile;
import com.ejiaoyi.common.mapper.UploadFileMapper;
import com.ejiaoyi.common.service.IFDFSService;
import com.ejiaoyi.common.service.IUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 附件 服务实现类
 * </p>
 *
 * @author Z0001
 * @since 2020-03-31
 */
@Service
public class UploadFileServiceImpl extends ServiceImpl<UploadFileMapper, UploadFile> implements IUploadFileService {

    @Autowired
    UploadFileMapper uploadFileMapper;

    @Autowired
    IFDFSService fdfsService;

    @Override
    public void insert(UploadFile uploadFile) {
        uploadFileMapper.insert(uploadFile);
    }

    @Override
    @Cacheable(value = CacheName.UPLOAD_FILE, key = "#id", unless = "#result==null")
    public UploadFile getUploadById(Integer id) {
        QueryWrapper<UploadFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ID", id);
        return uploadFileMapper.selectOne(queryWrapper);
    }

    @Override
    @CacheEvict(value = CacheName.UPLOAD_FILE, allEntries = true)
    public void updateUpload(UploadFile uploadFile) {
        uploadFileMapper.updateById(uploadFile);
    }


    @Override
    @Cacheable(value = CacheName.UPLOAD_FILE, key = "#uuid", unless = "#result==null")
    public UploadFile getUploadByUid(String uuid) {
        QueryWrapper<UploadFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("FILE_UID", uuid);
        return uploadFileMapper.selectOne(queryWrapper);
    }
}
