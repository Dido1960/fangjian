<head>
    <title>评标委员会签到表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
    <style>
        .pageNext {
            page-break-before: always;
        }
    </style>
</head>
<#escape x as x!"">
    <div class="show">
        <div class="panel">
            <table width="100%" class="base-normal-table">
                <thead>
                <tr>
                    <td colspan="5">
                        评标报告附件一
                    </td>
                </tr>
                <tr>
                    <td colspan="5">
                        <div class="base-header-mid first-title" style="text-align: center">评标专家签到表</div>
                    </td>
                </tr>
                <tr>
                    <td colspan="5" align="left">标段名称：${bidSection.bidSectionName}</td>
                </tr>
                <tr>
                    <td colspan="5" align="left">标段编号：${bidSection.bidSectionCode}</td>
                </tr>
                <tr class="base-border" style="text-align: center">
                    <td width="6%">序号</td>
                    <td width="30%">姓名</td>
                    <td width="17%">职称</td>
                    <td width="30%">工作单位</td>
                    <td width="17%">专业</td>
                </tr>
                </thead>
                <tbody>
                <#list expertUsers as expertUser>
                    <tr class="base-border">
                        <td align="center">${expertUser?counter}</td>
                        <td align="center">${expertUser.expertName}</td>
                        <td align="center"></td>
                        <td align="center">${expertUser.company}</td>
                        <td align="center"></td>
                    </tr>
                </#list>
                </tbody>

                <tfoot>
                <tr>
                    <td colspan="5" align="left" style="padding-top: 20px;">评标委员会成员签名：</td>
                </tr>
                <tr>
                    <td colspan="2" align="left" style="padding-top: 20px;">监标人签字：</td>
                    <td colspan="3" align="right" style="padding-top: 20px;">
                        <div>${date[0]}年${date[1]}月${date[2]}日</div>
                    </td>
                </tr>
                </tfoot>
            </table>
        </div>
    </div>
</#escape>