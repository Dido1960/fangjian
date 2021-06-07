<head>
    <title>技术标评分个人明细表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
</head>
<#escape x as x!"">
    <#assign colspan = 0>
    <#list gradeDtos as gradeDto>
        <#assign colspan = (colspan + gradeDto.gradeItemDTOs?size)>
    </#list>
    <#list expertUsers as expert>
        <div class="show">
            <div class="panel pagination">
                <table width="100%" class="base-normal-table">
                    <thead>
                    <tr>
                        <td colspan="${colspan + 2 }" align="center">
                            <div style="text-align: center"
                                 class="base-header-mid<#if expert_index == 0> first-title</#if>">技术评审表</div>
                        </td>
                    </tr>
                    <tr>
                        <td align="left" colspan="${colspan + 2 }">标段名称：${bidSection.bidSectionName}</td>
                    </tr>
                    <tr>
                        <td colspan="${colspan + 2 }">
                            <div style="width: 70%; float: left; text-align: left">标段编号：${bidSection.bidSectionCode}</div>
                            <div style="width: 20%; float: right; text-align: left">日期：${date[0]}年${date[1]}月${date[2]}日</div>
                        </td>
                    </tr>
                    </thead>
                    <tbody>
                        <tr class="base-border">
                            <td width="12%" align="center" rowspan="2">评标专家</td>
                            <#list gradeDtos as gradeDto>
                                <td width="3%" align="center" colspan="${gradeDto.gradeItemDTOs?size}">${gradeDto.gradeName}(${gradeDto.score}分)</td>
                            </#list>
                            <td width="7%" align="center" rowspan="2">得分合计</td>
                        </tr>
                        <tr class="base-border">
                            <#list gradeDtos as gradeDto>
                                <#list gradeDto.gradeItemDTOs as gradeItemDTO>
                                    <td width="3%" align="center">${gradeItemDTO_index + 1}<br>${gradeItemDTO.score!}分</td>
                                </#list>
                            </#list>
                        </tr>
                        <#list bidders as bidder>
                            <tr class="base-border">
                                <td align="left">${bidder.bidderName}</td>
                                <#assign expertSumScore = 0.0 >
                                <#list gradeDtos as gradeDto>
                                    <#list gradeDto.gradeItemDTOs as item>
                                        <#assign bidderDto = item.bidderDTOs[bidder_index]>
                                        <#if bidderDto??>
                                            <#list bidderDto.expertReviewDetailDTOs as expertReviewDetailDTO>
                                                <#if expertReviewDetailDTO.expertId == expert.id><#assign expertDto = expertReviewDetailDTO></#if>
                                            </#list>
                                            <#-- 打印每个专家的评分&ndash-->
                                            <#if expertDto??>
                                                <td align="center" width="3%">${expertDto.expertReviewDetail}</td>
                                                <#if expertDto.expertReviewDetail??>
                                                    <#assign expertSumScore += expertDto.expertReviewDetail?number/>
                                                </#if>
                                            <#else>
                                                <td></td>
                                            </#if>
                                        <#else>
                                            <td></td>
                                        </#if>
                                    </#list>
                                </#list>
                                <td align="center">${expertSumScore}</td>
                            </tr>
                        </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </#list>
</#escape>