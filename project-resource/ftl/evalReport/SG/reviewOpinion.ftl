<head>
    <title>评审意见表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
</head>
<#escape x as x!"">
    <div class="show">
        <div class="panel pagination">
            <table height="100%" class="base-normal-table base-write">
                <thead>
                <tr>
                    <td colspan="4" align="left">
                        评标报告附件
                        <#if tenderDoc.mutualSecurityStatus == 1>
                            十
                        <#else >
                            九
                        </#if>
                    </td>
                </tr>
                <tr>
                    <td colspan="4" align="center">
                        <div class="base-header-mid first-title base-bold">评审意见汇总表</div>
                    </td>
                </tr>
                <tr>
                    <td colspan="4" align="left">标段名称：${bidSection.bidSectionName}</td>
                </tr>
                <tr>
                    <td colspan="4" align="left">标段编号：${bidSection.bidSectionCode}</td>
                </tr>
                <tr class="base-border base-bold">
                    <td align="center" width="20%">投标人名称</td>
                    <td align="center" width="20%">评审步骤</td>
                    <td align="center" width="20%">评审标段名称</td>
                    <td align="center" width="40%">评审意见</td>
                </tr>
                </thead>
                <tbody>
                <#if bidders>
                    <#list bidders as bidder>
                        <#list bidder.gradeItemMap?values as gradeItem>
                            <tr class="base-border">
                                <#if gradeItem_index == 0>
                                    <td align="center" rowspan="${bidder.gradeItemMap?size}">
                                        ${bidder.bidderName}
                                    </td>
                                </#if>
                                <td>
                                    评审环节：${gradeItem.reviewProcess} <br>
                                    <#if gradeItem.reviewType>
                                        评审类型：${gradeItem.reviewType}
                                    </#if>
                                </td>
                                <td style="text-align: left;line-height: 1.6rem">${gradeItem.gradeItemName}</td>
                                <td align="left">
                                    <#list gradeItem.expertReviewDetailDTOS as result>
                                        <span style="text-indent: 2em">${result.expertName}</span>: ${result.evalComments}<br/>
                                        <#if result_index < gradeItem.expertReviewDetailDTOS?size -1>
                                            <hr>
                                        </#if>
                                    </#list>
                                </td>
                            </tr>
                        </#list>
                    </#list>
                <#else>
                    <tr class="base-border">
                        <td>&emsp;</td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                </#if>
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