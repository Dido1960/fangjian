<head>
    <title>资格预审评审汇总表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
</head>
<#escape x as x!"">
    <div class="show">
        <div class="panel pagination">
            <table height="100%" class="base-normal-table base-write">
                <thead>
                <tr>
                    <td colspan="5" align="left">
                        评标报告附件五
                    </td>
                </tr>
                <tr>
                    <td colspan="5" align="center">
                        <div class="base-header-mid first-title base-bold">资格预审评审汇总表</div>
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
                <tr class="base-border base-bold">
                    <td align="center" width="15%">序号</td>
                    <td align="center" width="35%">申请人</td>
                    <td align="center" width="10%">初步审查</td>
                    <td align="center" width="10%">详细审查</td>
                    <td align="center" width="10%">资格预审结果</td>
                </tr>
                </thead>
                <tbody>
                <#if bidderResults>
                    <#list bidderResults as bidder>
                        <tr class="base-border">
                            <td align="center">${bidder_index+1}</td>
                            <td align="center">${bidder.bidderName}</td>
                            <#if bidder.preReviewResult == "0">
                                <td align="center">不合格</td>
                                <td align="center">--</td>
                            <#else>
                                <td align="center">合格</td>
                                <#if bidder.detailReviewResult == "0">
                                    <td align="center">不合格</td>
                                <#else>
                                    <td align="center">合格</td>
                                </#if>
                            </#if>
                            <td align="center"><#if bidder.reviewResult == "0">不通过<#else>通过 </#if></td>
                        </tr>
                    </#list>
                <#else>
                    <tr class="base-border">
                        <td>&emsp;</td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                </#if>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="5" style="padding-top: 20px;">
                        <div style="width: 20%; float: left; text-align: left">评标委员会成员签字：</div>
                    </td>
                </tr>
                <tr>
                    <td colspan="5" style="padding-top: 20px;">
                        <div style="width: 20%; float: left; text-align: left">监标人签字：</div>
                    </td>
                </tr>
                </tfoot>
            </table>
        </div>
    </div>
</#escape>