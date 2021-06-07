package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.constant.EvalMethodConstant;
import com.ejiaoyi.common.constant.ScoreType;
import com.ejiaoyi.common.dto.*;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.ConDetailedMethod;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.enums.QualifiedType;
import com.ejiaoyi.common.exception.CustomException;
import com.ejiaoyi.common.mapper.GradeItemMapper;
import com.ejiaoyi.common.mapper.GradeMapper;
import com.ejiaoyi.common.service.IEvalResultJlService;
import com.ejiaoyi.common.service.IGradeItemService;
import com.ejiaoyi.common.service.IGradeService;
import com.ejiaoyi.common.util.CommonUtil;
import jodd.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 评标分数 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-07-07
 */
@Service
public class GradeServiceImpl extends BaseServiceImpl implements IGradeService {

    @Autowired
    private GradeMapper gradeMapper;

    @Autowired
    private GradeItemMapper gradeItemMapper;

    @Autowired
    private IGradeItemService gradeItemService;

    @Autowired
    IEvalResultJlService evalResultJlService;

    @Override
    @Cacheable(value = CacheName.GRADE_CACHE, key = "#id", unless = "#result==null")
    public Grade getGradeById(Integer id) {
        Assert.notNull(id, "param id can not be null!");
        Grade grade = gradeMapper.selectById(id);
        if (null != grade) {
            QueryWrapper<GradeItem> qw = new QueryWrapper<>();
            qw.eq("GRADE_ID", grade.getId());

            List<GradeItem> gradeItems = gradeItemMapper.selectList(qw);
            grade.setGradeItems(gradeItems);
        }
        return grade;
    }

    @Override
    public ProcessCompletionDTO getProcessCompletion(String[] gradeIds, Integer evalProcess) {
        return gradeMapper.getProcessCompletion(gradeIds, evalProcess);
    }


    @Override
    @Cacheable(value = CacheName.GRADE_CACHE, key = "#ids+ '_' + #reviewProcess", unless = "#result==null")
    public List<Grade> listGrade(String[] ids, Integer reviewProcess) {
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(!CommonUtil.isEmpty(reviewProcess), "REVIEW_PROCESS", reviewProcess);
        queryWrapper.eq("DELETE_FLAG", 0);
        queryWrapper.in("ID", ids);
        queryWrapper.orderByAsc("ID");

        List<Grade> grades = gradeMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(grades)) {
            return null;
        }

        for (Grade grade : grades) {
            QueryWrapper<GradeItem> qw = new QueryWrapper<>();
            qw.eq("GRADE_ID", grade.getId());

            List<GradeItem> gradeItems = gradeItemMapper.selectList(qw);
            grade.setGradeItems(gradeItems);
        }

        return grades;
    }

    @Override
    public ParseEvalMethodDTO addParseEvalMethodInfo(String xmlPath) throws Exception {
        ParseEvalMethodDTO resultData = new ParseEvalMethodDTO();
        if (!new File(xmlPath).exists()) {
            return null;
        }
        String gradeIds;
        CalcScoreParam calc = null;
        FileInputStream fis = null;
        boolean initPriceStatus = false;
        try {
            fis = new FileInputStream(xmlPath);
            SAXReader reader = new SAXReader();
            Document ducument = reader.read(fis);
            Element root = ducument.getRootElement();
            Element method = root.element(EvalMethodConstant.BID_EVALUATION_METHOD);

            if (method == null) {
                throw new CustomException("评分标准异常, 解析失败!");
            }

            List<Element> list = method.elements(EvalMethodConstant.EVALUATION_FACTOR);

            if (list == null) {
                throw new CustomException("评分标准异常, 解析失败!");
            }

            String[] ids = new String[list.size()];
            int i = 0;
            for (Element ele : list) {
                Grade grade = Grade.builder()
                        .gradeType(String.valueOf(i + 1))
                        .name(ele.attributeValue(EvalMethodConstant.TITLE))
                        .score(ele.attributeValue(EvalMethodConstant.FACTOR_SCORE))
                        .remark(ele.attributeValue(EvalMethodConstant.REMARK))
                        .deleteFlag(0)
                        .build();
                String reviewType = ele.attributeValue(EvalMethodConstant.REVIEW_TYPE);
                if (!CommonUtil.isEmpty(reviewType)) {
                    grade.setReviewType(Integer.parseInt(reviewType));
                }

                // 打分方式
                String calcType = ele.attributeValue(EvalMethodConstant.SCORE_METHOD);
                if (ScoreType.QUALIFIED.equals(calcType)) {
                    //合格类型
                    grade.setType(1);
                } else if (ScoreType.ACCUMULATE_DEDUCT.equals(calcType)) {
                    //打分扣分制
                    grade.setType(2);
                    grade.setCalcType(2);
                } else {
                    grade.setType(0);
                    if (ScoreType.ACCUMULATE.equals(calcType)) {
                        //得分类型
                        grade.setCalcType(1);
                    } else if (ScoreType.DEDUCT.equals(calcType)) {
                        //扣分类型
                        grade.setCalcType(2);
                    }
                }

                // 评审环节
                String reviewProcess = ele.attributeValue(EvalMethodConstant.REVIEW_PROCESS);
                if (EvalProcess.PRELIMINARY.getName().equals(reviewProcess)) {
                    //初步评审
                    grade.setReviewProcess(EvalProcess.PRELIMINARY.getCode());
                } else if (EvalProcess.DETAILED.getName().equals(reviewProcess)) {
                    //详细评审
                    grade.setReviewProcess(EvalProcess.DETAILED.getCode());
                } else if (EvalProcess.QUALIFICATION.getName().equals(reviewProcess)) {
                    //资格审查
                    grade.setReviewProcess(EvalProcess.QUALIFICATION.getCode());
                } else if (EvalProcess.CALC_PRICE_SCORE.getName().equals(reviewProcess)) {
                    //投标报价
                    grade.setReviewProcess(EvalProcess.CALC_PRICE_SCORE.getCode());
                    //解析扣分方式
                    calc = CalcScoreParam.builder()
                            .gtBasePriceDeduction(ele.attributeValue(EvalMethodConstant.E1))
                            .ltBasePriceDeduction(ele.attributeValue(EvalMethodConstant.E2))
                            .build();
                    initPriceStatus = true;
                }

                gradeMapper.insert(grade);
                Integer gradeId = grade.getId();

                ids[i++] = String.valueOf(gradeId);
                List<Element> listItems = ele.elements(EvalMethodConstant.EVALUATION_STANDARD);

                if (listItems == null) {
                    throw new CustomException("评分标准异常, 解析失败!");
                }

                for (Element eleItem : listItems) {
                    String itemContent = eleItem.attributeValue(EvalMethodConstant.DESCRIBE);
                    String score = eleItem.attributeValue(EvalMethodConstant.SCORE);
                    GradeItem gradeItem = GradeItem.builder()
                            .gradeId(gradeId)
                            .itemContent(itemContent)
                            .score(score)
                            .scoreType(eleItem.attributeValue(EvalMethodConstant.SCORE_TYPE))
                            .scoreRange(eleItem.attributeValue(EvalMethodConstant.SCORE_RANGE))
                            .remark(eleItem.attributeValue(EvalMethodConstant.REMARK))
                            .build();
                    gradeItemMapper.insert(gradeItem);
                    if (initPriceStatus) {
                        calc.setEvalPriceScoreMethodDesc(itemContent);
                        calc.setTotalScore(score);
                        initPriceStatus = false;
                    }
                }
            }

            gradeIds = StringUtil.join(ids, ",");
            resultData.setGradeIds(gradeIds);
            resultData.setCalcScoreParam(calc);
        } catch (Exception e) {
            throw new CustomException("评分标准异常, 解析失败!");
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException("评分标准异常, 解析失败!");
            }
        }
        return resultData;
    }


    @Override
    @Cacheable(value = CacheName.GRADE_CACHE, key = "#gradeId", unless = "#result==null")
    public Grade getGrade(Integer gradeId, Integer reviewProcess) {
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq(!CommonUtil.isEmpty(reviewProcess), "REVIEW_PROCESS", reviewProcess);
        queryWrapper.eq(!CommonUtil.isEmpty(gradeId), "ID", gradeId);
        queryWrapper.eq("DELETE_FLAG", 0);
        queryWrapper.orderByAsc("GRADE_TYPE");

        Grade grade = gradeMapper.selectOne(queryWrapper);
        if (null != grade) {
            QueryWrapper<GradeItem> qw = new QueryWrapper<>();
            qw.eq("GRADE_ID", grade.getId());

            List<GradeItem> gradeItems = gradeItemMapper.selectList(qw);
            grade.setGradeItems(gradeItems);
        }
        return grade;
    }

    @Override
    @CacheEvict(value = CacheName.GRADE_CACHE, allEntries = true)
    public boolean updateGrade(Grade grade) {
        Assert.notNull(grade, "param grade can not be null!");
        Assert.notNull(grade.getId(), "param id can not be null!");
        return gradeMapper.updateById(grade) == 1;
    }

    @Override
    public Grade getGradeByIdsAndName(String[] gradeIds, ConDetailedMethod methodName) {
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("ID", gradeIds);
        queryWrapper.eq("NAME", methodName.getName());
        return gradeMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Grade> listGradeByReviewType(String[] gradeIds, Integer evalProcess, Integer reviewType) {
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(!CommonUtil.isEmpty(evalProcess), "REVIEW_PROCESS", evalProcess);
        queryWrapper.eq(!CommonUtil.isEmpty(reviewType), "REVIEW_TYPE", reviewType);
        queryWrapper.eq("DELETE_FLAG", 0);
        queryWrapper.in("ID", gradeIds);
        queryWrapper.orderByAsc("ID");
        List<Grade> grades = gradeMapper.selectList(queryWrapper);

        for (Grade grade : grades) {
            QueryWrapper<GradeItem> qw = new QueryWrapper<>();
            qw.eq("GRADE_ID", grade.getId());

            List<GradeItem> gradeItems = gradeItemMapper.selectList(qw);
            grade.setGradeItems(gradeItems);
        }

        return grades;
    }

    @Override
    public String getGradeScoreByReviewType(String[] gradeIds, Integer evalProcess, Integer reviewType) {
        return gradeMapper.getGradeScoreByReviewType(gradeIds, evalProcess, reviewType);
    }

    @Override
    public GradeDTO getGradeDto(Integer gradeId, Integer bidSectionId, List<Bidder> bidders) {
        Assert.notNull(gradeId, "param gradeId can not be null!");
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        Grade grade = this.getGradeById(gradeId);
        if (grade != null) {
            List<GradeItem> gradeItems = gradeItemService.listGradeItem(gradeId);
            List<GradeItemDTO> gradeItemDTOList = new ArrayList<>();
            for (GradeItem gradeItem : gradeItems) {
                Integer gradeItemId = gradeItem.getId();
                List<BidderDTO> bidderDTOList = new ArrayList<>();

                for (Bidder bidder : bidders) {
                    Integer bidderId = bidder.getId();
                    //判断grade类型
                    List<ExpertReviewDetailDTO> expertReviewDetailList = gradeItemService.listAllExpertGradeItem(bidSectionId, bidderId, gradeItemId);
                    //合格制
                    boolean itemResult = true;
                    for (ExpertReviewDetailDTO expertReviewDetailDTO : expertReviewDetailList) {
                        if (QualifiedType.UNQUALIFIED.getCode().equals(expertReviewDetailDTO.getExpertReviewDetail())) {
                            itemResult = false;
                            break;
                        }
                    }
                    String gradeItemResult = itemResult ? QualifiedType.QUALIFIED.getCode() : QualifiedType.UNQUALIFIED.getCode();
                    bidderDTOList.add(BidderDTO.builder()
                            .bidderId(bidderId)
                            .bidderName(bidder.getBidderName())
                            .gradeItemResult(gradeItemResult)
                            .expertReviewDetailDTOs(expertReviewDetailList)
                            .build());
                }
                gradeItemDTOList.add(GradeItemDTO.builder()
                        .gradeItemId(gradeItemId)
                        .gradeItemName(gradeItem.getItemContent())
                        .score(gradeItem.getScore())
                        .remark(gradeItem.getRemark())
                        .bidderDTOs(bidderDTOList)
                        .build());
            }

            return GradeDTO.builder()
                    .gradeId(gradeId)
                    .gradeName(grade.getName())
                    .score(grade.getScore())
                    .gradeItemDTOs(gradeItemDTOList)
                    .build();
        }
        return null;
    }

    @Override
    public GradeDTO getGradeDtoScore(Integer gradeId, Integer bidSectionId, List<Bidder> bidders) {
        Assert.notNull(gradeId, "param gradeId can not be null!");
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        Grade grade = this.getGradeById(gradeId);
        if (grade != null) {

            List<GradeItem> gradeItems = gradeItemService.listGradeItem(gradeId);

            List<GradeItemDTO> gradeItemDTOList = new ArrayList<>();
            for (GradeItem gradeItem : gradeItems) {
                Integer gradeItemId = gradeItem.getId();
                List<BidderDTO> bidderDTOList = new ArrayList<>();
                for (Bidder bidder : bidders) {
                    Integer bidderId = bidder.getId();
                    List<ExpertReviewDetailDTO> expertReviewDetailList = gradeItemService.listAllExpertGradeItemScore(bidSectionId, bidderId, gradeItemId);
                    //打分制
                    int expertCount = expertReviewDetailList.size();
                    double sumScore = 0;
                    // 当前投标人，所有专家的打分情况
                    double bidderGradeTotalScore = 0.0;
                    for (ExpertReviewDetailDTO expertReviewDetailDTO : expertReviewDetailList) {
                        // 评分项主键
                        expertReviewDetailDTO.setGradeItemId(gradeItem.getId());
                        // 投标人主键
                        expertReviewDetailDTO.setBidderId(bidder.getId());
                        String expertReviewDetail = expertReviewDetailDTO.getExpertReviewDetail();
                        expertReviewDetail = expertReviewDetail == null ? "0" : expertReviewDetail;
                        sumScore += Double.parseDouble(expertReviewDetail);
                        // 当前投标人，所有专家的打分情况
                        bidderGradeTotalScore += sumScore;
                    }
                    String gradeItemResult = "0";
                    if (expertCount > 0) {
                        gradeItemResult = String.valueOf(sumScore / expertCount);
                    }
                    bidderDTOList.add(BidderDTO.builder()
                            .bidderId(bidderId)
                            .bidderName(bidder.getBidderName())
                            .gradeItemResult(gradeItemResult)
                            .expertReviewDetailDTOs(expertReviewDetailList)
                            .gradesTotal(String.valueOf(bidderGradeTotalScore))
                            .build());
                }
                gradeItemDTOList.add(GradeItemDTO.builder()
                        .gradeItemId(gradeItemId)
                        .gradeItemName(gradeItem.getItemContent())
                        .score(gradeItem.getScore())
                        .remark(gradeItem.getRemark())
                        .bidderDTOs(bidderDTOList)
                        .build());
            }
            return GradeDTO.builder()
                    .gradeId(gradeId)
                    .gradeName(grade.getName())
                    .score(grade.getScore())
                    .gradeItemDTOs(gradeItemDTOList)
                    .build();
        }
        return null;
    }

    @Override
    public GradeDTO getGradeViolationDto(Integer gradeId, Integer bidSectionId, List<Bidder> bidders) {
        Assert.notNull(gradeId, "param gradeId can not be null!");
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        Grade grade = this.getGradeById(gradeId);
        if (grade != null) {

            List<GradeItem> gradeItems = gradeItemService.listGradeItem(gradeId);

            List<GradeItemDTO> gradeItemDTOList = new ArrayList<>();
            for (GradeItem gradeItem : gradeItems) {
                Integer gradeItemId = gradeItem.getId();
                List<BidderDTO> bidderDTOList = new ArrayList<>();
                for (Bidder bidder : bidders) {
                    // 获取违章行为扣分
                    EvalResultJl evalResultJl = evalResultJlService.getEvalResultJl(bidSectionId, bidder.getId());
                    BidderDTO bidderDTO = BidderDTO.builder()
                            .bidderId(bidder.getId())
                            .bidderName(bidder.getBidderName())
                            .build();

                    if (evalResultJl != null) {
                        bidderDTO.setGradeItemResult("-" + evalResultJl.getViolationDeduct());
                    }
                    bidderDTOList.add(bidderDTO);
                }
                gradeItemDTOList.add(GradeItemDTO.builder()
                        .gradeItemId(gradeItemId)
                        .gradeItemName(gradeItem.getItemContent())
                        .score(gradeItem.getScore())
                        .remark(gradeItem.getRemark())
                        .bidderDTOs(bidderDTOList)
                        .build());
            }
            return GradeDTO.builder()
                    .gradeId(gradeId)
                    .gradeName(grade.getName())
                    .score(grade.getScore())
                    .gradeItemDTOs(gradeItemDTOList)
                    .build();
        }
        return null;
    }

    @Override
    public GradeDTO getGradeDtoDeduct(Integer gradeId, Integer bidSectionId, List<Bidder> bidders) {
        Assert.notNull(gradeId, "param gradeId can not be null!");
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        Grade grade = this.getGradeById(gradeId);
        if (grade != null) {
            List<GradeItem> gradeItems = gradeItemService.listGradeItem(gradeId);
            List<GradeItemDTO> gradeItemDTOList = new ArrayList<>();
            for (GradeItem gradeItem : gradeItems) {
                // 获取当前gradeItem的扣分值
                GradeItem item = gradeItemService.getById(gradeItem.getId());
                List<BidderDTO> bidderDTOList = new ArrayList<>();
                for (Bidder bidder : bidders) {
                    Integer bidderId = bidder.getId();
                    // 当前投标人，所有专家的打分情况
                    double deductSumScore = 0.0;
                    // 扣分分值
                    double deductScore = 0.0;
                    // 扣分人数
                    Integer deductionCount = 0;
                    List<ExpertReviewDetailDTO> expertReviewDetailList = gradeItemService.listAllExpertGradeItemDeductScore(bidSectionId, bidderId, item.getId());
                    for (ExpertReviewDetailDTO detailDTO : expertReviewDetailList) {
                        // 评分项主键
                        detailDTO.setGradeItemId(gradeItem.getId());
                        // 投标人主键
                        detailDTO.setBidderId(bidder.getId());
                        // 0,表示扣分
                        if ("0".equals(detailDTO.getExpertReviewDetail())) {
                            // 扣分分值
                            deductScore = Double.parseDouble(item.getScore());
                            // 每个专家的扣分值
                            detailDTO.setExpertReviewDetail(String.valueOf(deductScore));
                            // 扣分总分
                            deductSumScore += deductScore;
                            // 统计扣分人数
                            deductionCount++;
                        } else {
                            // 当前item未扣分
                            detailDTO.setExpertReviewDetail(String.valueOf(0.0));
                        }
                    }
                    // 采用少数服从多数原则
                    if (!CommonUtil.isEmpty(expertReviewDetailList)) {
                        // 百分之六十的人数
                        Double sixtyPercentCount = expertReviewDetailList.size() * 0.6;
                        DecimalFormat f = new DecimalFormat("#0");
                        String format = f.format(sixtyPercentCount);
                        if (deductionCount < Integer.parseInt(format)) {
                            deductScore = 0.0;
                        }
                    }
                    bidderDTOList.add(BidderDTO.builder()
                            .bidderId(bidderId)
                            .bidderName(bidder.getBidderName())
                            .gradeItemResult(String.valueOf(deductScore))
                            .expertReviewDetailDTOs(expertReviewDetailList)
                            .gradesTotal(String.valueOf(deductSumScore))
                            .build());
                }
                gradeItemDTOList.add(GradeItemDTO.builder()
                        .gradeItemId(gradeItem.getId())
                        .gradeItemName(gradeItem.getItemContent())
                        .score(gradeItem.getScore())
                        .remark(gradeItem.getRemark())
                        .bidderDTOs(bidderDTOList)
                        .build());
            }
            return GradeDTO.builder()
                    .gradeId(gradeId)
                    .gradeName(grade.getName())
                    .score(grade.getScore())
                    .gradeItemDTOs(gradeItemDTOList)
                    .build();
        }
        return null;
    }

    @Override
    public List<ExpertReviewDetailDTO> listAllExpertGradeItemScore(Integer bidSectionId, Integer bidderId, Integer gradeItemId) {
        return gradeItemMapper.listAllExpertGradeItemScore(bidSectionId, bidderId, gradeItemId);
    }

    @Override
    public String[] getGradeIds(String[] gradeIds, Integer evalProcess) {
        List<Grade> grades = listGrade(gradeIds, evalProcess);
        String[] result = new String[grades.size()];
        for (int i = 0; i < grades.size(); i++) {
            result[i] = grades.get(i).getId().toString();
        }
        return result;
    }

    @Override
    @CacheEvict(value = CacheName.GRADE_CACHE, allEntries = true)
    public Integer updateGroupEndByIds(Integer[] ids, Integer status) {
        UpdateWrapper<Grade> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("ID", ids);
        updateWrapper.set("GROUP_END", status);
        return gradeMapper.update(null, updateWrapper);
    }

    @Override
    public Integer insertGrade(Grade grade) {
        gradeMapper.insert(grade);
        return grade.getId();
    }
}
