<head>
    <title>技术标评分明细表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
</head>
<#escape x as x!"">
    <#assign colspan = 0>
    <#list gradeDtos as gradeDto>
        <#assign colspan = (colspan + gradeDto.gradeItemDTOs?size)>
    </#list>
    <#list bidders as bidder>
        <div class="show">
            <div class="panel pagination">
                <table width="100%" class="base-normal-table">
                    <thead>
                    <tr>
                        <td colspan="${colspan + 2 }" align="left">
                            评标报告附件六
                        </td>
                    </tr>
                    <tr>
                        <td colspan="${colspan + 2 }" align="center">
                            <div style="text-align: center"
                                 class="base-header-mid<#if bidder_index ==0> first-title</#if>">技术标评分明细表</div>
                        </td>
                    </tr>
                    <tr>
                        <td align="left" colspan="${colspan + 2 }">标段名称：${bidSection.bidSectionName}</td>
                    </tr>
                    <tr>
                        <td colspan="${colspan + 2 }">
                            <div style="width: 70%; float: left; text-align: left">标段编号：${bidSection.bidSectionCode}</div>
                            <div style="width: 28%; float: left; text-align: left">日期：${date[0]}年${date[1]}月${date[2]}日</div>
                        </td>
                    </tr>
                    </thead>
                    <tbody>
                        <tr class="base-border">
                            <td colspan="${colspan + 2 }">
                                <div style="width: 80%; float: left; text-align: center;">投标人名称：${bidder.bidderName }</div>
                                <div style="width: 18%; float: left; text-align: center;">得分：${bidder.totalResult}</div>
                            </td>
                        </tr>
                        <tr class="base-border">
                            <td width="12%" align="center" rowspan="2">评标专家</td>
                            <#list gradeDtos as gradeDto>
                                <td align="center" colspan="${gradeDto.gradeItemDTOs?size}">${gradeDto.gradeName}(${gradeDto.score}分)</td>
                            </#list>
                            <td width="7%" align="center" rowspan="2">得分合计</td>
                        </tr>
                        <tr class="base-border">
                            <#list gradeDtos as gradeDto>
                                <#list gradeDto.gradeItemDTOs as gradeItemDTO>
                                    <td width="3%" style="text-align: center">${gradeItemDTO_index + 1} <br>${gradeItemDTO.score!}分</td>
                                </#list>
                            </#list>
                        </tr>
                        <#list expertUsers as expert>
                            <tr class="base-border">
                                <td align="center">${expert.expertName}</td>
                                <#assign expertSumScore = 0.0 >
                                <#list gradeDtos as gradeDto>
                                    <#list gradeDto.gradeItemDTOs as item>
                                        <#assign bidderDto = item.bidderDTOs[bidder_index]>
                                        <#if bidderDto??>
                                            <#list bidderDto.expertReviewDetailDTOs as expertReviewDetailDTO>
                                                <#if expertReviewDetailDTO.expertId == expert.id><#assign expertDto = expertReviewDetailDTO></#if>
                                            </#list>
                                            <#--打印每个专家的评分-->
                                            <#if expertDto??>
                                                <td align="center">${expertDto.expertReviewDetail}</td>
                                                <#assign expertSumScore += expertDto.expertReviewDetail?number/>
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
                    <tfoot>
                    <tr>
                        <td colspan="${colspan + 2}" style="padding-top: 20px;">
                            <div style="width: 78%; float: left; text-align: left">评标委员会成员签字：</div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="${colspan + 2}" style="padding-top: 20px;">
                            <div style="width: 20%; float: left; text-align: left">监标人签字：</div>
                        </td>
                    </tr>
                    </tfoot>
                </table>
            </div>
        </div>
    </#list>
</#escape>