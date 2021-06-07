<head>
    <title>得分汇总表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
</head>
<#escape x as x!"">
    <div class="show">
        <#-- 显示的投标人数量-->
        <#assign showBidderCount = 9/>
        <#assign bidderNum =evalResultJls?size/>
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
                        <td colspan="5" align="left">
                            评标报告附件七
                        </td>
                    </tr>
                    <#if tableNum == 0>
                        <tr>
                            <td colspan="5" align="center">
                                <div style="text-align: center" class="base-header-mid<#if tableNum ==0> first-title</#if>">评标结果</div>
                            </td>
                        </tr>
                    </#if>
                    <tr>
                        <td colspan="5" align="center">
                            <div class="<#if tableNum == 0>second-title </#if>base-header-small">${gradeDto.gradeName}</div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="5" align="left">标段名称：${bidSection.bidSectionName}</td>
                    </tr>
                    <tr>
                        <td colspan="5" align="left">
                            <div style="width: 79%; float: left; text-align: left">标段编号：${bidSection.bidSectionCode}</div>
                            <div style="width: 20%; float: left; text-align: right">日期：${date[0]}年${date[1]}月${date[2]}日</div>
                        </td>
                    </tr>
                    <tr class="base-border">
                        <td rowspan="2" style="width: 5%;text-align: center">序号</td>
                        <td rowspan="2" style="width: 40%;text-align: center">
                            投标人名称
                        </td>
                        <td rowspan="2" style="width: 10%;text-align: center">
                            投标报价 <br>
                            （元）
                        </td>
                        <td rowspan="2" style="width: 10%;text-align: center">
                            得分汇总
                        </td>
                        <td rowspan="2" style="width: 5%;text-align: center">
                            名次
                        </td>
                    </tr>
                    </thead>
                    <tbody>
                    <#list i..j as num>
                        <#if evalResultJls[num]??>
                            <tr class="base-border">
                                <td align="center">${num+1}</td>
                                <td align="center">${evalResultJls[num].bidderName}</td>
                                <td align="center"><#if evalResultJls[num].bidPrice??>${(evalResultJls[num].bidPrice)?number?string(",###.##")}</#if></td>
                                <td align="center">${evalResultJls[num].totalScore}</td>
                                <td align="center">${evalResultJls[num].orderNo}</td>
                            </tr>
                        </#if>
                    </#list>
                    </tbody>
                </table>
            </div>
        </#list>
    </div>
</#escape>