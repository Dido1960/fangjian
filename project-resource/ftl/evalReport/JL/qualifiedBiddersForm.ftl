<head>
    <title>初审合格表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
</head>
<#escape x as x!"">
    <div class="show">
        <#--初步评审表-->
        <div class="panel pagination">
            <table width="100%" class="base-normal-table">
                <thead>
                    <tr>
                        <td colspan="4" align="left">
                            评标报告附件四
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4" align="center">
                            <div style="text-align: center" class="base-header-mid first-title">初审合格表</div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4" align="center">
                            <div class="<#if tableNum == 0>second-title </#if>base-header-small">${gradeDto.gradeName}</div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4" align="left">标段名称：${bidSection.bidSectionName}</td>
                    </tr>
                    <tr>
                        <td colspan="4" align="right">日期：${date[0]}年${date[1]}月${date[2]}日</td>
                    </tr>
                    <tr class="base-border">
                        <td rowspan="2" style="width: 5%;text-align: center">序号</td>
                        <td rowspan="2" style="width: 40%;text-align: center">
                            投标人名称
                        </td>
                        <td rowspan="2" style="width: 20%;text-align: center">
                            投标报价 <br>
                            （元）
                        </td>
                        <td rowspan="2" style="width: 20%;text-align: center">
                            备注
                        </td>
                    </tr>
                </thead>
                <tbody>
                    <#list qualifiedBidders as bidder>
                    <tr class="base-border">
                        <td align="center">${bidder_index+1}</td>
                        <td align="center">
                            ${bidder.bidderName}
                        </td>
                        <td align="center"><#if !bidder.bidderOpenInfo.bidPriceType?? || bidder.bidderOpenInfo.bidPriceType == "总价">
                                <#if bidder.bidderOpenInfo.bidPrice?? && bidder.bidderOpenInfo.bidPrice != ''>
                                    ${((bidder.bidderOpenInfo.bidPrice)?number)?string(",###.##")}
                                </#if>
                            <#else>
                                ${bidder.bidderOpenInfo.bidPriceType}${bidder.bidderOpenInfo.bidPrice}
                            </#if></td>
                        <td align="center"></td>
                    </tr>
                </#list>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="4" style="padding-top: 20px;">
                            <div style="width: 20%; float: left; text-align: left">评标委员会成员签字：</div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4" style="padding-top: 20px;">
                            <div style="width: 20%; float: left; text-align: left">监标人签字：</div>
                        </td>
                    </tr>
                </tfoot>
            </table>
        </div>
    </div>
</#escape>