<html lang="zh">
<head>
    <title>消息导出</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css">
</head>
<body>
<div class="cen-set">
    <div class="panel pagination">
        <div class="base-header first-title">${bidSection.bidSectionName}消息盒子记录单</div>
        <#list lineMsgs as lineMsg >
            <table class="base-normal-table" style="margin-top: 15px">
                <tr class="base-border" style="text-align: center">
                    <td width="30%">${lineMsg.sendName}</td>
                    <td>${lineMsg.insertTime}</td>
                </tr>
                <tr class="base-border">
                    <td colspan="2">${lineMsg.message}</td>
                </tr>
                <#if lineMsg.roleType != 0 && lineMsg.backName??>
                    <tr class="base-border">
                        <td colspan="2"><span style="color:red;">${lineMsg.backName}</span> 回复： ${lineMsg.backMsg}</td>
                    </tr>
                </#if>
            </table>
        </#list>
    </div>
</div>
</body>
</html>