<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <title>角色选择</title>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script src="${ctx}/js/webService.js"></script>
    <script type="text/javascript" src="${ctx}/js/like_num.js"></script>
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script type="text/javascript" src="${ctx}/plugin/baiduPlayer/cyberplayer-3.4.1/cyberplayer.js"></script>
    <script src="${ctx}/plugin/echarts.min.js"></script>

    <style>
        * {
            margin: 0;
            padding: 0;
            user-select: none;
        }

        ul,
        ol {
            list-style: none;
        }

        html,
        body {
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0);
        }

        /*包含以下四种的链接*/
        a {
            text-decoration: none;
        }

        /*正常的未被访问过的链接*/
        a:link {
            text-decoration: none;
        }

        /*已经访问过的链接*/
        a:visited {
            text-decoration: none;
        }

        /*鼠标划过(停留)的链接*/
        a:hover {
            text-decoration: none;
        }

        /* 正在点击的链接*/
        a:active {
            text-decoration: none;
        }

        .cont {
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0);
        }

        .cont .three {
            width: 80%;
            height: 530px;
            position: absolute;
            left: 0;
            top: 0;
            right: 0;
            bottom: 0;
            margin: auto;
        }

        .cont .three li {
            width: 33.3%;
            height: 530px;
            float: left;
            cursor: pointer;
        }

        .cont .three li img {
            display: block;
            margin: 0 auto;
        }

        .cont .three li:hover span {

            background: #005CEA;
            box-shadow: 0px 3px 0px #003F9F;
            opacity: 1;
            font-weight: bold;
            color: #FFFFFF;
        }

        .cont .three li span {
            display: block;
            width: 224px;
            height: 56px;
            background: #EBF3FF;
            box-shadow: 0px 3px 0px #005CEA;
            opacity: 1;
            border-radius: 56px;
            font-size: 22px;
            font-family: Microsoft YaHei;
            font-weight: bold;
            line-height: 56px;
            color: #005CEA;
            text-align: center;
            margin: 50px auto 0px;
        }

        .cont .close {
            display: block;
            width: 48px;
            height: 48px;
            background: url(../img/button_close.png) no-repeat center center;
            background-size: 100% 100%;
            cursor: pointer;
            position: absolute;
            top: 100px;
            right: 100px;
        }

        .cont .layui-layer {
            background-color: rgba(0, 0, 0, 0) !important;
        }

        .cont #layui-layer1 {
            background-color: rgba(0, 0, 0, 0) !important;
        }
    </style>
</head>

<body>
    <div class="cont">
        <span class="close" id="clickme_close" onclick="ClickMe_close()"></span>
        <ul class="three">
            <li>
                <img src="../img/agent_small.png" alt="">
                <span>招标代理机构</span>
            </li>
            <li>
                <img src="../img/bidder_small.png" alt="">
                <span>供应商</span>
            </li>
            <li>
                <img src="../img/documentMake.png" alt="">
                <span>招标文件制作</span>
            </li>
        </ul>
    </div>
</body>
<script>
    var index = parent.layer.getFrameIndex(window.name);//获取窗口索引
    function ClickMe_close() {
        parent.layer.close(index);
    }
</script>

</html>