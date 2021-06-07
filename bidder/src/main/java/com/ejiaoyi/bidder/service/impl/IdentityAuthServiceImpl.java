package com.ejiaoyi.bidder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.bidder.service.IIdentityAuthService;
import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.BidderOpenInfo;
import com.ejiaoyi.common.entity.Fdfs;
import com.ejiaoyi.common.entity.TenderDoc;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.mapper.FdfsMapper;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.DateTimeUtil;
import com.ejiaoyi.common.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

/**
 * 身份认证 服务实现类
 *
 * @author Make
 * @since 2020-7-31
 */
@Service
public class IdentityAuthServiceImpl extends BaseServiceImpl implements IIdentityAuthService {

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    private FdfsMapper fdfsMapper;

    @Override
    public boolean verifyQRCodeInvalid(Integer bidSectionId) {
        if (bidSectionId == null) {
            return false;
        }
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String bidOpenTime = tenderDoc.getBidOpenTime();
        String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        return DateTimeUtil.compareDate(bidOpenTime, nowTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS) == 1;
    }

    @Override
    public boolean verifyCompleteAuth(Integer bidderId) {
        Bidder bidder = bidderService.getBidderById(bidderId);
        Integer bidSectionId = bidder.getBidSectionId();
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);
        // 若已经身份认证成功或者使用了紧急签到，表示已完成认证
        return !CommonUtil.isEmpty(bidderOpenInfo) && ((bidderOpenInfo.getAuthentication() != null && bidderOpenInfo.getAuthentication() == 1)
                || (bidderOpenInfo.getUrgentSigin() != null && bidderOpenInfo.getUrgentSigin() == 1));
    }

    /**
     * @Description 获取扫码后回调地址
     * @Author liuguoqiang
     * @Date 2020-8-6 14:24
     */
    @Override
    public String getCallBakUrl(Integer boiId, Integer authType) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        String url = "";
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String ctx = request.getContextPath();
            String base = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + ctx;
            if (authType == 1) {
                //url = "http://scqskj.f3322.net:23157/xin/call?id=" + boiId;
                url = base + "/xin/call?id=" + boiId;
            } else {
                url = base + "/phoneScan/phoneCallBackPage?"
                        + "boiId=" + boiId
                        + "&authType=" + authType;
                try {
                    url = URLEncoder.encode(url, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        }
        return url;
    }

    @Override
    public String uploadPhoto(String url, Integer id){

        String customPath = FileUtil.getCustomFilePath()+"xinPhoto";
        String fdfsUploadPath = customPath + File.separator + id;
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + ".jpg" ;
        String path = fdfsUploadPath + File.separator + ProjectFileTypeConstant.XIN_PHOTO;
        String outPath = path + File.separator + fileName;
        downloadPicture(url,fileName, path);

        //构造FastDfs 文件Mark
        String mark = File.separator + ProjectFileTypeConstant.XIN_PHOTO +
                File.separator + id + File.separator + fileName+".jpg";
        //上传
        String urlPath = "";
        try {
            Fdfs fdfs= fdfsService.upload(new File(outPath), mark);
            return fdfs.getUrl();
        } catch (IOException e) {
            e.printStackTrace();
            return urlPath;
        }
    }

    private static void downloadPicture(String urlString, String filename,String savePath){
        // 构造URL
        URL url = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            url = new URL(urlString);
            // 打开连接
            URLConnection con = url.openConnection();
            //设置请求超时为5s
            con.setConnectTimeout(5*1000);
            // 输入流
            is = con.getInputStream();

            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            File sf=new File(savePath);
            if(!sf.exists()){
                sf.mkdirs();
            }
            // 获取图片的扩展名
            String extensionName = filename.substring(filename.lastIndexOf(".") +     1);
            // 新的图片文件名 = 编号 +"."图片扩展名
            os = new FileOutputStream(sf.getPath()+"\\"+filename);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过Mark获取fdfs如果重复返回最后一条
     * @param mark
     * @return
     */
    private Fdfs getFdfsByMark(String mark){

        mark = mark.replaceAll("\\\\", "/");
        QueryWrapper<Fdfs> wrapper = new QueryWrapper<>();
        wrapper.eq("MARK",mark);
        List<Fdfs> list = fdfsMapper.selectList(wrapper);
        if (list.size()>0){
            return list.get(list.size()-1);
        }
        return null;
    }
}
