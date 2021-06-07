<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>直播页面</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-1.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-3.1.0.min.js"></script>
    <!--[if lt IE 9]>
    <script src="${ctx}/js/html5shiv.min.js"></script>
    <script src="${ctx}/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/css/live.css"/>
    <script type="text/javascript" src="${ctx}/plugin/baiduPlayer/cyberplayer-3.4.1/cyberplayer.js"></script>
    <script type="text/javascript">
        var haveShow = false;
        $(function () {
            LiveChoice()
        })

        function LiveChoice() {
            var explorer = navigator.userAgent;
            if (explorer.indexOf("Chrome") >= 0) {
                LiveGoogle();
            } else {
                LiveIe();
            }
        }
        function LiveGoogle() {
            var player = cyberplayer("playerContainer").setup({
                width: '100%',
                height: 744,
                isLive: true,
                file: "http://${liveUrlAddress}/${bidSection.liveRoom}.flv",
                image: "/img/login-pic.png",
                autostart: true,
                stretching: "uniform",
                volume: 60,
                barLogo: false,
                controls: true,
                ak: "548d3d6cdbeb4007a86e3e3cc81082c7" // 公有云平台注册即可获得accessKey
            });
            setTimeout(function () {
                player.onNoLiveStream(function () {
                });
                player.onLiveStop(function () {
                    console.log("onLiveStop");
                    if (!haveShow) {
                        layer.alert("当前直播不存在！");
                    }

                });
            }, 3000);
        }
        function LiveIe() {
            var player = cyberplayer("playerContainer").setup({
                width: '100%',
                height: 744,
                isLive: true,
                file: "rtmp://${liveUrlAddress}/${bidSection.liveRoom}",
                image: "/img/login-pic.png",
                autostart: true,
                stretching: "uniform",
                volume: 60,
                barLogo: false,
                controls: true,
                rtmp: {
                    reconnecttime: 5, // rtmp直播的重连次数
                    bufferlength: 1 // 缓冲多少秒之后开始播放 默认1秒
                },
                ak: "548d3d6cdbeb4007a86e3e3cc81082c7" // 公有云平台注册即可获得accessKey
            });
            setTimeout(function () {
                player.onNoLiveStream(function () {
                });
                player.onLiveStop(function () {
                    console.log("onLiveStop");
                    if (!haveShow) {
                        layer.alert("当前直播不存在！");
                    }

                });
            }, 3000);
        }
    </script>
</head>

<body>
    <div class="box">
        <div class="live" id="playerContainer" tabindex="0"></div>
        <p>标段名称:<span>${bidSection.bidSectionName}</span></p>
        <p>标段编号:<span>${bidSection.bidSectionCode}</span></p>
        <div class="foot">
            <div class="center">
                <div>开标时间：${tenderDoc.bidOpenTime }</div>
                <div>代理机构：${tenderProject.tenderAgencyName}</div>
                <div>代理机构联系电话：${tenderProject.tenderAgencyPhone}</div>
            </div>
        </div>
    </div>

</body>

</html>