<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>微信认证页面</title>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <meta
            name="viewport"
            content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"
    />
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/phone.js"></script>
    <link rel="stylesheet" href="${ctx}/css/phoneAuth.css">
<body>
<#if !invalid?? && !completeAuth??>
    <script>
        $(function () {
            var a = document.createElement("a");
            a.setAttribute("href", "${url}");
            a.click();
        })
    </script>
<#elseif invalid?? && !invalid>
    <div class="box">
        <h3>身份认证失败</h3>
        <img src="${ctx}/img/mistake.png" alt="">
        <div>二维码已失效</div>
        <p>请重试</p>
    </div>
<#elseif completeAuth?? && completeAuth>
    <div class="box">
        <h3>身份认证成功</h3>
        <img src="${ctx}/img/correct.png" alt="">
        <div>您已身份认证完成，无需重复认证！</div>
        <p>恭喜你，完成了身份认证</p>
    </div>
</#if>

</body>
<script>

</script>
</html>