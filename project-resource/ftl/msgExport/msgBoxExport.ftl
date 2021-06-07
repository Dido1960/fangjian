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
        <#list list as msg >
            <table class="base-normal-table" style="margin-top: 15px">
                <tr class="base-border" style="text-align: center">
                    <td width="30%">${msg.name}</td>
                    <td>${msg.time}</td>
                </tr>
                <tr class="base-border">
                    <td colspan="2">${msg.content}</td>
                </tr>
            </table>
        </#list>
    </div>
</div>
</body>
</html>