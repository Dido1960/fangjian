<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>身份认证结束</title>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <meta
            name="viewport"
            content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"
    />
    <script type="text/javascript" src="${ctx}/js/phone.js"></script>
    <link rel="stylesheet" href="${ctx}/css/phoneAuth.css">
</head>
<body
<#if !authStatus?? ||authStatus == 0>
    <div class="box">
        <h3>身份认证失败</h3>
        <img src="${ctx}/img/mistake.png" alt="">
        <p>请重试</p>
    </div>
<#else>
    <div class="box">
        <h3>身份认证成功</h3>
        <img src="${ctx}/img/correct.png" alt="">
        <p>恭喜你，完成了身份认证</p>
    </div>
</#if>

</body>

</html>