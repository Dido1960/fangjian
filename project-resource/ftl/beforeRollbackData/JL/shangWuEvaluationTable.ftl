<head>
    <title>4、商务技术（个人打分）</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css\base.css"/>

</head>
<#escape x as x!"">
    <div class="show">
        <#--一行显示的投标人数量-->
        <#assign showBidderCount = 5/>
        <#--投标人数量-->
        <#assign bidderNum = bidders?size/>
        <#--计算循环完所有投标人的次数（横向循环次数）-->
        <#if bidderNum < showBidderCount>
            <#assign endNum=1/>
        <#elseif bidderNum % showBidderCount == 0>
            <#assign endNum=(bidderNum/showBidderCount)/>
        <#else>
            <#assign endNum=((bidderNum/showBidderCount)+1)/>
        </#if>
        <#list 0..(endNum-1) as tableNum>
            <#assign i=(tableNum*showBidderCount) j=(tableNum*showBidderCount+(showBidderCount-1))>
            <div class="panel pagination">
                <table width="100%" class="base-normal-table">
                    <thead>
                    <tr>
                        <td colspan="9">
                            <div style="text-align: center;" class="<#if tableNum == 0>first-title </#if> base-header-mid">商务评审表</div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="9" align="left">标段名称：${bidSection.bidSectionName}</td>
                    </tr>
                    <tr>
                        <td colspan="9" align="left">标段编号：${bidSection.bidSectionCode}</td>
                    </tr>
                    <tr class="base-border" style="text-align: center">
                        <td rowspan="2" width="5%">序号</td>
                        <td rowspan="2" colspan="2" style="width: 44%">评审因素</td>
<#--                        <td rowspan="2" style="width: 3%">分值</td>-->
                        <#list i..j as k>
                            <td align="center" width="10%">
                                <#if bidders[k]>
                                    ${(k+1)}
                                </#if>
                            </td>
                        </#list>
                        <#--补齐td-->
                        <#--默认显示，（showBidderCount）个投标人，不足的空td补齐-->
                        <#assign countTd = showBidderCount - bidders?size -1>
                        <#if countTd gt 0>
                           <#list 0..countTd as i>
                               <td width="10%"></td>
                           </#list>
                        </#if>
                    </tr>
                    <tr class="base-border">
                        <#list i..j as k>
                            <td align="center" width="10%">
                                <#if bidders[k]??>
                                    ${bidders[k].bidderName}
                                </#if>
                            </td>
                        </#list>
                        <#if countTd gt 0>
                            <#list 0..countTd as i>
                                <td width="10%"></td>
                            </#list>
                        </#if>
                    </tr>
                    </thead>
                    <tbody>
                    <#list gradeDtos as gradeDto>
                        <#list gradeDto.gradeItemDTOs as item>
                            <tr class="base-border">
                                <#if item_index == 0>
                                    <td rowspan="${gradeDto.gradeItemDTOs?size}" style="width: 4%;line-height: 1.6rem;text-align: center">${gradeDto_index + 1}</td>
                                    <td rowspan="${gradeDto.gradeItemDTOs?size}" style="width:8%;line-height: 1.6rem">${gradeDto.gradeName}(${gradeDto.score}分)</td>
                                </#if>
                                <td width="15%">${item.gradeItemName} (${item.score!}分)</td>
<#--                                <td align="center">${item.score}</td>-->
                                <#list i..j as num>
                                    <td align="center">
                                        <#if bidders[num]??>
                                            <#if item.bidderDTOs[num] && item.bidderDTOs[num].gradeItemResult??>
                                                ${item.bidderDTOs[num].gradeItemResult?number?abs}
                                            </#if>
                                        </#if>
                                    </td>
                                </#list>
                                <#if countTd gt 0>
                                    <#list 0..countTd as i>
                                        <td></td>
                                    </#list>
                                </#if>
                            </tr>
                        </#list>
                    </#list>
                    <tr class="base-border">
                        <td colspan="3" style="text-align: center"> 最终加权得分合计</td>
                        <#--计算所有投标人所有评分点的总分-->
                        <#list i..j as k>
                            <#assign scores = 0/>
                            <#list gradeDtos as gradeDTO>
                                <#list gradeDTO.gradeItemDTOs as item>
                                    <#if item.bidderDTOs[k].gradeItemResult>
                                        <#assign scores += item.bidderDTOs[k].gradeItemResult?number />
                                    </#if>
                                </#list>
                            </#list>
                            <td align="center">
                                <#if bidders[k] ??>${scores}</#if>
                            </td>
                        </#list>
                        <#if countTd gt 0>
                            <#list 0..countTd as i>
                                <td></td>
                            </#list>
                        </#if>
                    </tr>
                    </tbody>
                </table>
            </div>
        </#list>
    </div>
</#escape>