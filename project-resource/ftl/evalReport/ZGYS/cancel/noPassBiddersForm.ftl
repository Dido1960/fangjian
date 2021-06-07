<head>
    <title>评标结果</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
</head>
<#escape x as x!"">
    <div class="show">
        <div class="panel pagination">
            <table height="100%" class="base-normal-table base-write">
                <thead>
                <tr>
                    <td colspan="2" align="center">
                        <div class="base-header-mid first-title base-bold">资格预审结果</div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="left">标段名称：${bidSection.bidSectionName}</td>
                </tr>
                <tr>
                    <td colspan="2" align="left">标段编号：${bidSection.bidSectionCode}</td>
                </tr>
                <tr class="base-border base-bold">
                    <td align="center" width="30%">未通过资格预审申请人名称</td>
                    <td align="center" width="70%">未通过原因</td>
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
                                <td align="left">
                                    <#list gradeItem.expertReviewDetailDTOS as result>
                                        <span style="text-indent: 2em">${result.evalComments}<span/>
                                           <br/>
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
                    </tr>
                    <tr class="base-border">
                        <td>&emsp;</td>
                        <td></td>
                    </tr>
                    <tr class="base-border">
                        <td>&emsp;</td>
                        <td></td>
                    </tr>
                </#if>
                </tbody>
            </table>
        </div>
    </div>
</#escape>