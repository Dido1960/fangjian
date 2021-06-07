<head>
    <title>评标报告封面</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
    <style>
        .pageNext {
            page-break-before: always;
        }
    </style>
</head>
<div class="show">
    <div class="panel pageNext">
        <table class="base-normal-table">
            <tbody>
            <#assign expertUserName>
                <#list expertUsers as expert>
                    <#if expert>
                        ${expert.expertName}
                    </#if>
                </#list>
            </#assign>
            <tr>
                <td style="height: 350px;">
                    <div class="first-title" style="color: white">${expertUserName}</div>
                    <div class="second-title" style="color: white">封面</div>
                    <div class="base-header" style="text-align: center; line-height: 150px; font-size: 60px;">回<br>退<br>数<br>据
                    </div>
                </td>
            </tr>
            <tr class="base-tfoot">
                <td>
                    <div style="text-align: left;margin-left: 150px;padding-top: 60px;font-size: 20px;text-indent: 2rem">
                        &emsp;&emsp;标段编号：<span class="base-underline">${bidSection.bidSectionCode}</span></div>
                    <div style="text-align: left;margin-left: 150px;padding-top: 40px;font-size: 20px;text-indent: 2rem">
                        标段名称：<span class="base-underline">${bidSection.bidSectionName}</span></div>
                    <div style="text-align: left;margin-left: 150px;padding-top: 40px;font-size: 20px;text-indent: 2rem">
                        评审专家：<span class="base-underline">
                               ${expertUserName}
                            </span>
                    </div>
                    <div style="text-align: left;margin-left: 150px;padding-top: 40px;font-size: 20px;text-indent: 2rem">
                        审核人员：<span class="base-underline">${freeBackApply.checkUserName}</span></div>
                    <div style="text-align: left;margin-left: 150px;padding-top: 40px;font-size: 20px;text-indent: 2rem">
                        审核时间：<span class="base-underline">${freeBackApply.checkTime}</span></div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>