package com.ejiaoyi.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ejiaoyi.common.entity.Fdfs;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * FastDFS文件 服务类
 * </p>
 *
 * @author Z0001
 * @since 2020-03-30
 */
public interface IFDFSService   {


    /**
     * 多附件上传
     *
     * @param multipartFile 多附件
     * @param mark          FastDFS文件标记
     */
    Fdfs upload(MultipartFile multipartFile, String mark) throws IOException;


    /**
     * layui 单附件上传
     *
     * @param multipartFile 多附件
     * @param mark          FastDFS文件标记
     */
    Fdfs uploadByLayui(MultipartFile multipartFile, String mark) throws IOException;


    /**
     * 文件上传
     *
     * @param file 文件
     * @param mark FastDFS文件标记
     */
    Fdfs upload(File file, String mark) throws IOException;

    /**
     * 文件上传
     *
     * @param fis      需要上传文件的输入流
     * @param fileName 文件名称
     * @param suffix   文件后缀名
     * @param mark     FastDFS文件标记
     */
    Fdfs upload(FileInputStream fis, String fileName, String suffix, String mark) throws IOException;

    /**
     * 文件上传
     *
     * @param bytes    需要上传文件的字节数组
     * @param fileName 文件名称
     * @param suffix   文件后缀名
     * @param mark     FastDFS文件标记
     */
    Fdfs upload(byte[] bytes, String fileName, String suffix, String mark);

    /**
     * 通过附件主键 下载FastDFS文件
     *
     * @param id 附件主键
     * @return FastDfsFile对象 bytes[]属性为 文件字节数组
     */
    Fdfs downloadByUpload(Integer id);

    /**
     * 通过FastDFS文件标记 下载FastDFS文件
     *
     * @param mark FastDFS文件标记
     * @return FastDfsFile对象 bytes[]属性为 文件字节数组
     */
    Fdfs downloadByMark(String mark);

    /**
     * 通过访问地址下载FastDFS文件流
     *
     * @param url 访问地址
     * @return FastDFS文件文件流
     */
    byte[] downloadByUrl(String url);

    /**
     * 通过FastDFS文件标记 删除FastDFS文件
     *
     * @param mark FastDFS文件标记
     * @return 是否删除成功
     */
    boolean deleteByMark(String mark);

    /**
     * 删除FastDFS文件
     *
     * @param fastDfsFile FastDFS文件
     * @return 是否删除成功
     */
    boolean delete(Fdfs fastDfsFile);

    /**
     * 通过附件主键 获取FastDFS文件访问地址
     *
     * @param id 附件主键
     * @return FastDFS文件访问地址
     */
    String getUrlByUpload(Integer id);

    /**
     * 通过附件主键 获取FastDFS文件访问地址
     *
     * @param id 附件主键
     * @return FastDFS文件访问地址
     */
    String getMarkByUpload(Integer id);

    /**
     * 通过FastDFS文件标记 获取FastDFS文件访问地址
     *
     * @param mark FastDFS文件标记
     * @return FastDFS文件访问地址
     */
    String getUrlByMark(String mark);

    /**
     * 上传项目文件
     *
     * @param type 文件类型
     * @param uploadFileId 上传文件路径（主要用于解压招投标文件上传，其他可传null）
     * @param file 文件
     * @return 是否上传成功
     */
    Boolean uploadProjectFile(String type, Integer uploadFileId, File file);

    /**
     * 获取指定目录下所有文件信息
     * @param dirPath
     * @return
     */
    List<Fdfs> listFdfsByMark(String dirPath);



    /**
     * 通过bidderId获取开标一览表url
     * @param bidDocId
     * @return
     */
    String getOpenBidUrl(Integer bidDocId);

    /***
     * 获取对应的fdfs
     * **/
    Fdfs getFdfsByMark(String mark);


    /****
     * 根据mark下载文件到本地
     * @param mark  文件对应的唯一标识
     *
     * @param outFilePath  保存地址
     * **/
    void downLoadFileByMark(String mark, String outFilePath) throws Exception;

    /***
     * 通过上传的id查找对应的fdfs
     *
     * ***/
    Fdfs getFdfsByUpload(Integer uploadId);

    /**
     * 根据fdfs的主键获取fdfs对象
     * @param fdfsId
     * @return
     */
    Fdfs getFdfdById(Integer fdfsId);

    /**
     * 插入fdfs数据
     * @param fdfs
     * @return id
     */
    Integer insertFdfs(Fdfs fdfs);
}
