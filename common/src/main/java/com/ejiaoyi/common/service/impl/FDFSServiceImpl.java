package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.dto.WebSocketMessage;
import com.ejiaoyi.common.entity.Fdfs;
import com.ejiaoyi.common.entity.UploadFile;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.mapper.FdfsMapper;
import com.ejiaoyi.common.service.IFDFSService;
import com.ejiaoyi.common.service.IUploadFileService;
import com.ejiaoyi.common.support.DataSourceKey;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.DateTimeUtil;
import com.ejiaoyi.common.util.FileUtil;
import com.ejiaoyi.common.util.RedisUtil;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import jodd.util.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;

/**
 * <p>
 * FastDFS文件 服务实现类
 * </p>
 *
 * @author Z0001
 * @since 2020-03-30
 */
@Service
@DS(DataSourceKey.COMMON)
@Log4j2
@Transactional(isolation = Isolation.READ_UNCOMMITTED, readOnly = false)
public class FDFSServiceImpl implements IFDFSService {
    @Autowired
    private FdfsMapper fdfsMapper;

    @Autowired
    private IUploadFileService uploadFileService;

    /**
     * FastDFS group
     */
    @Value("${fdfs.group}")
    private String fastDfsGroup;

    /**
     * FastDFS address
     */
    @Value("${fdfs.address}")
    private String fastDfsAddress;

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Override
    public Fdfs upload(MultipartFile multipartFile, String mark) throws IOException {
        Assert.notNull(multipartFile, "param multipartFile can not be null !");
        Assert.notEmpty(mark, "param mark can not be empty !");

        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = FileUtil.getSuffix(originalFilename);
        InputStream inputStream = multipartFile.getInputStream();
        StorePath storePath = fastFileStorageClient.uploadFile(fastDfsGroup, inputStream, multipartFile.getSize(), suffix);

        Fdfs fastDfsFile = Fdfs.builder()
                .mark(mark.replaceAll("\\\\", "/"))
                .dfsGroup(storePath.getGroup())
                .dfsAddress(storePath.getPath())
                .name(originalFilename)
                .suffix(suffix)
                .byteSize((int) multipartFile.getSize())
                .fileHash(DigestUtils.md5Hex(multipartFile.getBytes()).toUpperCase())
                .readSize(FileUtil.getReadSize((int) multipartFile.getSize()))
                .url("http://" + fastDfsAddress + "/" + storePath.getFullPath())
                .build();

        fdfsMapper.insert(fastDfsFile);
        inputStream.close();
        return fastDfsFile;
    }

    @Override
    public Fdfs uploadByLayui(MultipartFile multipartFile, String mark) throws IOException {
        Assert.notNull(multipartFile, "param multipartFile can not be null !");
        Assert.notEmpty(mark, "param mark can not be empty !");

        String fileName = multipartFile.getOriginalFilename();
        int i = fileName.lastIndexOf("\\");
        if (i > 0) {
            fileName = fileName.substring(i + 1);
        }
        String suffix = FileUtil.getSuffix(fileName);
        InputStream inputStream = multipartFile.getInputStream();
        StorePath storePath = fastFileStorageClient.uploadFile(fastDfsGroup, inputStream, multipartFile.getSize(), suffix);

        Fdfs fastDfsFile = Fdfs.builder()
                .mark(mark.replaceAll("\\\\", "/"))
                .dfsGroup(storePath.getGroup())
                .dfsAddress(storePath.getPath())
                .name(fileName)
                .suffix(suffix)
                .byteSize((int) multipartFile.getSize())
                .fileHash(DigestUtils.md5Hex(multipartFile.getBytes()).toUpperCase())
                .readSize(FileUtil.getReadSize((int) multipartFile.getSize()))
                .url("http://" + fastDfsAddress + "/" + storePath.getFullPath())
                .build();

        fdfsMapper.insert(fastDfsFile);
        inputStream.close();
        return fastDfsFile;
    }

    @Override
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Fdfs upload(File file, String mark) throws IOException {
        Assert.notNull(file, "param file can not be null !");
        Assert.notEmpty(mark, "param mark can not be empty !");

        String fileName = file.getName();
        String suffix = FileUtil.getSuffix(fileName);
        FileInputStream fis = new FileInputStream(file);
        Fdfs fdfs = this.upload(fis, fileName, suffix, mark);
        fis.close();
        return fdfs;
    }

    @Override
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Fdfs upload(FileInputStream fis, String fileName, String suffix, String mark) throws IOException {
        return upload(fis, fileName, suffix, mark, null);
    }

    /**
     * 文件上传
     *
     * @param fis
     * @param fileName
     * @param suffix
     * @param mark
     * @param voiceTimes wav音频文件时长
     * @throws IOException
     */
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Fdfs upload(FileInputStream fis, String fileName, String suffix, String mark, Integer voiceTimes) throws IOException {
        //存储文件流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;

        while ((len = fis.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        byte[] data=baos.toByteArray();
        long size = data.length;
        StorePath storePath = fastFileStorageClient.uploadFile(fastDfsGroup, new ByteArrayInputStream(data), size, suffix);

        Fdfs fastDfsFile = Fdfs.builder()
                .mark(mark.replaceAll("\\\\", "/"))
                .dfsGroup(storePath.getGroup())
                .dfsAddress(storePath.getPath())
                .name(fileName + "." + suffix)
                .suffix(suffix)
                .byteSize((int) size)
                .readSize(FileUtil.getReadSize(size))
                .url("http://" + fastDfsAddress + "/" + storePath.getFullPath())
                .fileHash(DigestUtils.md5Hex(baos.toByteArray()).toUpperCase())
                .build();

        // 如果是音频文件，计算时长
        if (voiceTimes != null) {
            fastDfsFile.setWavDuration(voiceTimes);
        }
        long start = Calendar.getInstance().getTimeInMillis();
        log.error("开始保存时间：====》》》" + DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS));
        deleteByMark(mark);
        fdfsMapper.insert(fastDfsFile);
        long endTime = Calendar.getInstance().getTimeInMillis();
        log.error("结束保存时间：====》》》" + ((endTime - start) / 1000) + "s");
        return fastDfsFile;
    }

    @Override
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Fdfs upload(byte[] bytes, String fileName, String suffix, String mark) {
        int size = bytes.length;
        StorePath storePath = fastFileStorageClient.uploadFile(fastDfsGroup, new ByteArrayInputStream(bytes), size, suffix);
        Fdfs fastDfsFile = Fdfs.builder()
                .mark(mark.replaceAll("\\\\", "/"))
                .dfsGroup(storePath.getGroup())
                .dfsAddress(storePath.getPath())
                .name(fileName + "." + suffix)
                .suffix(suffix)
                .byteSize(size)
                .fileHash(DigestUtils.md5Hex(bytes).toUpperCase())
                .readSize(FileUtil.getReadSize(size))
                .url("http://" + fastDfsAddress + "/" + storePath.getFullPath())
                .build();

        deleteByMark(mark);
        fdfsMapper.insert(fastDfsFile);
        return fastDfsFile;
    }

    @Override
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Fdfs downloadByUpload(Integer id) {
        Assert.notNull(id, "param id can not be null");

        UploadFile uploadFile = uploadFileService.getUploadById(id);

        if (uploadFile == null) {
            return null;
        }

        Fdfs fastDfsFile = this.getCacheFdfs(uploadFile.getPath(),null);

        if (fastDfsFile != null) {
            byte[] bytes = fastFileStorageClient.downloadFile(fastDfsFile.getDfsGroup(), fastDfsFile.getDfsAddress(), new DownloadByteArray());
            fastDfsFile.setBytes(bytes);
        }

        return fastDfsFile;
    }

    @Override
    public Fdfs downloadByMark(String mark) {
        Assert.notEmpty(mark, "param mark can not be empty");

        Fdfs fastDfsFile = this.getCacheFdfs(mark,null);

        if (fastDfsFile != null) {
            byte[] bytes = fastFileStorageClient.downloadFile(fastDfsFile.getDfsGroup(), fastDfsFile.getDfsAddress(), new DownloadByteArray());
            fastDfsFile.setBytes(bytes);
        }

        return fastDfsFile;
    }

    @Override
    public byte[] downloadByUrl(String url) {
        Assert.notEmpty(url, "param url can not be empty");

        url = url.replace("http://", "");
        url = url.replace("https://", "");
        url = url.substring(url.indexOf("/") + 1);

        String group = url.substring(0, url.indexOf("/"));
        String path = url.substring(url.indexOf("/") + 1);

        return fastFileStorageClient.downloadFile(group, path, new DownloadByteArray());
    }


    @Override
    public boolean deleteByMark(String mark) {
        if (StringUtils.isEmpty(mark)) {
            return false;
        }
        Fdfs fastDfsFile = this.getCacheFdfs(mark,null);
        return this.delete(fastDfsFile);
    }

    @Override
    public boolean delete(Fdfs fastDfsFile) {
        if (fastDfsFile == null) {
            return false;
        }

        try {
            fastFileStorageClient.deleteFile(fastDfsFile.getDfsGroup(), fastDfsFile.getDfsAddress());
        } catch (Exception e) {
            log.warn("文件服务器上删除对应文件失败！"+e.getMessage());
        }
        try {
            this.deleteCacheFdfs(null,fastDfsFile.getId());
            fdfsMapper.deleteById(fastDfsFile.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public String getUrlByUpload(Integer uploadId) {
        String url = "";

        UploadFile uploadFile = uploadFileService.getUploadById(uploadId);

        if (uploadFile != null) {
            Fdfs fastDfsFile = this.getCacheFdfs(uploadFile.getPath(),null);

            if (fastDfsFile != null) {
                return fastDfsFile.getUrl();
            }
        }

        return url;
    }

    @Override
    public String getMarkByUpload(Integer uploadId) {
        String mark = "";
        UploadFile uploadFile = uploadFileService.getUploadById(uploadId);
        if (uploadFile != null) {
            return uploadFile.getPath();
        }
        return mark;
    }

    @Override
    public String getUrlByMark(String mark) {
        if (StringUtils.isNotEmpty(mark)) {
            Fdfs fastDfsFile = this.getCacheFdfs(mark,null);
            if (fastDfsFile != null) {
                return fastDfsFile.getUrl();
            }
        }
        return null;
    }

    @Override
    public Boolean uploadProjectFile(String type, Integer uploadFileId, File file) {
        Assert.notNull(file, "param file can not be empty");
        String mark = "";
        String markPrefix = "";
        FileInputStream fis = null;
        try {
            if (StringUtil.isNotEmpty(type)) {
                markPrefix += File.separator + type;
            }

            if (uploadFileId != null) {
                markPrefix += File.separator + uploadFileId;
            }
            //文件不存在
            if(!file.exists()){
                return  false;
            }

            // 如果这个路径是文件夹
            if (file.isDirectory()) {
                List<File> files = FileUtil.listDirFile(file);
                for (File uploadFile : files) {
                    if (uploadFile.getPath().contains("BidFileCompose")) {
                        continue;
                    }
                    mark = markPrefix + File.separator + uploadFile.getPath().replace(file.getPath() + File.separator, "");
                    String fileName = uploadFile.getName();
                    String suffix = FileUtil.getSuffix(fileName);
                    fis = new FileInputStream(uploadFile);
                    if (type.equals(ProjectFileTypeConstant.VOICE)) {
                        try {
                            this.upload(fis, fileName.replaceAll("[.][^.]+$", ""), suffix, mark, FileUtil.getWavDuration(uploadFile.getPath()));
                        } catch (Exception e) {
                            e.printStackTrace();
                            fis.close();
                        }
                    } else {
                        this.upload(fis, fileName.replaceAll("[.][^.]+$", ""), suffix, mark);
                    }
                    fis.close();
                }
            } else {
                String fileName = file.getName();
                String suffix = FileUtil.getSuffix(fileName);
                mark = markPrefix + File.separator + fileName;
                fis = new FileInputStream(file);
                this.upload(fis, fileName.replaceAll("[.][^.]+$", ""), suffix, mark);
                fis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("关闭流失败！");
                }
            }
        }
        return true;
    }

    @Override
    public List<Fdfs> listFdfsByMark(String dirPath) {
        dirPath = dirPath.replaceAll("\\\\", "/");
        QueryWrapper<Fdfs> fdfsQueryWrapper = new QueryWrapper<Fdfs>()
                .like("MARK", dirPath);
        return fdfsMapper.selectList(fdfsQueryWrapper);
    }


    @Override
    public String getOpenBidUrl(Integer bidDocId) {

        String mark = "/" + ProjectFileTypeConstant.BIDDER_FILE + "/" + bidDocId + "/resources/TempConvert/OpenBidView.pdf";
        return this.getUrlByMark(mark);
    }

    @Override
    public Fdfs getFdfsByMark (String mark) {
        return this.getCacheFdfs(mark,null);
    }

    //根据mark下载文件到本地
    @Override
    public void downLoadFileByMark(String mark, String outFilePath) throws Exception {
        Fdfs fdfs = this.downloadByMark(mark);
        if (CommonUtil.isEmpty(fdfs)) {
            return;
        }
        FileUtil.writeFile(fdfs.getBytes(), outFilePath);
    }


    @Override
    public Fdfs getFdfsByUpload(Integer uploadId) {
        UploadFile uploadFile = uploadFileService.getUploadById(uploadId);
        if (uploadFile != null) {
            return this.getCacheFdfs(uploadFile.getPath(),null);
        }
        return null;
    }


    @Override
    public Fdfs getFdfdById(Integer fdfsId) {
        return getCacheFdfs(null,fdfsId);
    }

    @Override
    public Integer insertFdfs(Fdfs fdfs) {
        return fdfsMapper.insert(fdfs);
    }

    private Fdfs getOneByMark(String mark) {
        mark = mark.replaceAll("\\\\", "/");
        Assert.notEmpty(mark, "param mark can not be empty");
        QueryWrapper<Fdfs> query = new QueryWrapper<>();
        query.eq("MARK", mark);
        return fdfsMapper.selectOne(query);
    }

    /***
     * 缓存fdfs
     * ***/
    private Fdfs getCacheFdfs(String mark, Integer fdfdId) {

        Fdfs fdfs = null;
        if (StringUtil.isNotEmpty(mark)) {
            Integer id = (Integer) RedisUtil.get(CacheName.FDFS_MARK_INFO+mark);
            if(id!=null){
                return getCacheFdfs(null,id);
            }
            fdfs = this.getOneByMark(mark);
        } else if (fdfdId != null) {
            fdfs = (Fdfs) RedisUtil.get(CacheName.FDFS_ID_INFO+fdfdId);
            if(fdfs!=null){
                return fdfs;
            }
            fdfs = fdfsMapper.selectById(fdfdId);
        }
        if (fdfs != null) {
            RedisUtil.set(CacheName.FDFS_MARK_INFO +fdfs.getMark(), fdfs.getId());
            RedisUtil.set(CacheName.FDFS_ID_INFO+fdfs.getId(), fdfs);
        }
        return fdfs;
    }


    /***
     * 删除缓存fdfs
     *
     * ***/
    private void deleteCacheFdfs(String mark, Integer fdfdId) {
        Fdfs fdfs = null;
        if (StringUtil.isNotEmpty(mark)) {
            fdfs = (Fdfs) RedisUtil.get(CacheName.FDFS_MARK_INFO +mark);
            if(fdfs==null) {
                fdfs = this.getOneByMark(mark);
            }
        } else if (fdfdId != null) {
            fdfs = (Fdfs) RedisUtil.get(CacheName.FDFS_MARK_INFO+fdfdId);
            if(fdfs==null) {
                fdfs = fdfsMapper.selectById(fdfdId);
            }
        }
        if (fdfs != null) {
            RedisUtil.delete(CacheName.FDFS_MARK_INFO +fdfs.getMark());
            RedisUtil.delete(CacheName.FDFS_ID_INFO+fdfs.getId());
        }
    }
}
