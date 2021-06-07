package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.entity.ClarifyAnswer;
import com.ejiaoyi.common.entity.Fdfs;
import com.ejiaoyi.common.mapper.ClarifyAnswerMapper;
import com.ejiaoyi.common.service.IClarifyAnswerService;
import com.ejiaoyi.common.service.IFDFSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 澄清答疑文件 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-11-11
 */
@Service
public class ClarifyAnswerServiceImpl extends ServiceImpl<ClarifyAnswerMapper, ClarifyAnswer> implements IClarifyAnswerService {

    @Autowired
    private ClarifyAnswerMapper clarifyAnswerMapper;

    @Autowired
    private IFDFSService fdfsService;


    @Override
    public List<ClarifyAnswer> listClarifyAnswerBySectionId(Integer bidSectionId) {
        QueryWrapper<ClarifyAnswer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        queryWrapper.orderByDesc("ID");
        List<ClarifyAnswer> clarifyAnswers = clarifyAnswerMapper.selectList(queryWrapper);
        for (ClarifyAnswer clarifyAnswer : clarifyAnswers) {
            Fdfs urlByUpload = fdfsService.getFdfsByUpload(clarifyAnswer.getUpfilesId());
            clarifyAnswer.setFdfs(urlByUpload);
        }
        return clarifyAnswers;
    }
}
