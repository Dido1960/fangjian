<head>
    <title>详细个人评审</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
</head>
<#assign testIndex=0/>
<#list expertUsers as expert>
    <#list gradeDtos as gradeDto>
        <#if gradeDto_index>
            <div class="show">
                <#-- 显示的投标人数量-->
                <#assign showBidderCount = 6/>
                <#assign bidderNum =bidders?size/>
                <#assign isShow = true/>
                <#if bidderNum<showBidderCount>
                    <#assign endNum=1/>
                <#elseif bidderNum%showBidderCount==0 >
                    <#assign endNum=(bidderNum/showBidderCount)/>
                <#else>
                    <#assign endNum=((bidderNum/showBidderCount)+1)/>
                </#if>
                <#assign count =bidderNum/>
                <#list 0..(endNum-1) as tableNum>
                    <#assign i=(tableNum*showBidderCount) j=(tableNum*showBidderCount+(showBidderCount-1))>
                <#--初步评审表-->
                    <div class="panel pagination">
                        <table width="100%" class="base-normal-table">
                            <thead>
                            <tr>
                                <td colspan="9" align="left">
                                    评标委员会成员个人打分表
                                </td>
                            </tr>
                            <#if tableNum == 0 && gradeDto_index == 0>
                                <tr>
                                    <td colspan="9" align="center">
                                        <div style="text-align: center"
                                             class="base-header-mid<#if testIndex ==0> first-title
                                                                         <#assign testIndex=1/>
                                                                    </#if>">详细个人评审
                                        </div>
                                    </td>
                                </tr>
                            </#if>
                            <tr>
                                <td colspan="9" align="center">
                                    <div class="">${gradeDto.gradeName}</div>
                                    <#if tableNum == 0 && gradeDto_index == 0>
                                        <div style="color: white"
                                             class="second-title base-header-small">${expert.expertName}
                                        </div>
                                    </#if>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="9" align="left">标段名称：${bidSection.bidSectionName}</td>
                            </tr>
                            <tr>
                                <td colspan="9" align="left">标段编号：${bidSection.bidSectionCode}</td>
                            </tr>
                            <tr class="base-border">
                                <td rowspan="2" style="width: 7%;text-align: center">序号</td>
                                <td rowspan="2" style="width: 44%;text-align: center">
                                    评审项目
                                </td>
                                <td rowspan="2" style="width: 7%;text-align: center">扣分分值</td>
                                <#list i..j as num>
                                    <td width="7%" align="center">
                                        <#if bidders[num]??>
                                            ${num + 1}
                                        </#if>
                                    </td>
                                </#list>
                            </tr>
                            <tr class="base-border">
                                <#list i..j as num>
                                    <td align="center">
                                        <#if bidders[num]??>${bidders[num].bidderName}</#if>
                                    </td>
                                </#list>
                            </tr>
                            </thead>
                            <tbody>
                            <#--  此处遍历评分项-->
                            <#list gradeDto.gradeItemDTOs as item>
                                <tr class="base-border">
                                    <td style="line-height: 1.6rem;text-align: center">${item_index + 1}</td>
                                    <td style="line-height: 1.6rem;text-align: left">${item.gradeItemName}</td>
                                    <td style="line-height: 1.6rem;text-align: center">${item.score}</td>
                                    <#list i..j as num>
                                        <td align="center">
                                            <#if bidders[num]??>
                                                <#if item.bidderDTOs[num].expertReviewDetailDTOs[expert_index]??>
                                                    ${item.bidderDTOs[num].expertReviewDetailDTOs[expert_index].expertReviewDetail}
                                                </#if>
                                            </#if>
                                        </td>
                                    </#list>
                                </tr>
                            </#list>
                            <tr class="base-border">
                                <td align="center" colspan="3">投标人扣分合计</td>
                                <#list i..j as num>
                                    <#assign scores = 0/>
                                    <#list gradeDto.gradeItemDTOs as item>
                                        <#if item.bidderDTOs[num].expertReviewDetailDTOs[expert_index]??>
                                            <#assign scores += item.bidderDTOs[num].expertReviewDetailDTOs[expert_index].expertReviewDetail?number />
                                        </#if>
                                    </#list>
                                    <td align="center">
                                        <#if bidders[num] ??>
                                            ${scores}
                                        </#if>
                                    </td>
                                </#list>
                            </tr>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="9" style="padding-top: 20px;">
                                    <div style="width: 40%; float: left; text-align: left">评审专家 ${expert.expertName} 签字：</div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </#list>
            </div>
        </#if>
    </#list>
</#list>
