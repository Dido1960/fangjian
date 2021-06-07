<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>身份认证结束</title>
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
<body
<#if !anthStatus?? ||anthStatus == 0>
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