<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>稍后在试</title>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script src="${ctx}/js/common.js"></script>
    <style>
        * {
            margin: 0;
            padding: 0;
        }

        html,
        body {
            width: 100%;
            height: 100%;
            overflow: hidden;
        }

        .box {
            width: 375px;
            height: 380px;
            margin: 400px auto;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: space-between;
        }

        .box img {
            display: block;
            width: 372px;
            height: 258px;
        }

        .box p {
            width: 154px;
            height: 34px;
            font-size: 14px;
            font-family: "Microsoft YaHei";
            font-weight: 900;
            line-height: 34px;
            color: rgba(26, 51, 88, 1);
            opacity: 1;
        }

        .box .btn {
            width: 100px;
            height: 40px;
            background: rgba(255, 255, 255, 1);
            border: 1px solid rgba(0, 98, 235, 1);
            opacity: 1;
            font-size: 14px;
            font-family: "Microsoft YaHei";
            font-weight: 900;
            line-height: 40px;
            color: rgba(0, 98, 235, 1);
            text-align: center;
            cursor: pointer;
        }

        .box .btn:hover {
            background: rgba(12, 113, 255, 1);
            color: #fff;
            transition: 1s;
        }

        .box p a {
            text-decoration: none;
            color: #0062EB;
        }
    </style>
</head>

<body>
    <div class="box">
        <img src="${ctx}/img/500.png" alt="">
        <p>服务器出错，请稍后在试</p>
        <span class="btn" onclick="location.reload()">重新加载</span>
    </div>
</body>

</html>