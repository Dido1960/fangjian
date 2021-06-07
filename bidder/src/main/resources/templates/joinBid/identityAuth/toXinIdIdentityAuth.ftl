<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>微信认证页面</title>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="${ctx}/js/common.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <style>
        * {
            margin: 0;
            padding: 0;
        }

        .box {
            width: 100%;
        }

        .box h3 {
            width: 100%;
            height: 40px;
            font-size: 18px;
            font-family: "Microsoft YaHei";
            font-weight: bold;
            line-height: 40px;
            text-align: center;
            color: rgba(34, 49, 101, 1);
            border-bottom: 1px solid #eee;
        }

        .box img {
            display: block;
            width: 20%;
            height: 20%;
            margin: 30px auto 0;
        }

        .box div {
            width: 100%;
            height: 40px;
            font-size: 18px;
            font-family: "Microsoft YaHei";
            font-weight: bold;
            line-height: 40px;
            text-align: center;
            color: rgba(34, 49, 101, 1);
        }

        .box p {
            width: 100%;
            height: 40px;
            font-size: 14px;
            font-family: "Microsoft YaHei";
            font-weight: 500;
            line-height: 40px;
            text-align: center;
            color: red;
        }
    </style>
</head>
<body>
<input type="hidden" id="invalid" value="${invalid}">
<input type="hidden" id="completeAuth" value="${completeAuth}">
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