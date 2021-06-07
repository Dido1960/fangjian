<html>
<head>
    <meta charset="utf-8">
    <title>专家评标音视频</title>
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
    <script language="javascript" type="text/javascript" src="${ctx}/plugin/anychat/javascript/anychatsdk.js" charset="GB2312"></script>
    <script language="javascript" type="text/javascript" src="${ctx}/plugin/anychat/javascript/anychatevent.js" charset="GB2312"></script>
    <script language="javascript" type="text/javascript" src="${ctx}/plugin/anychat/javascript/advanceset.js" charset="GB2312"></script>
    <script language="javascript" type="text/javascript" src="${ctx}/js/anychat/expertEvalPicture.js"></script>
    <link type="text/css" href="${ctx}/css/anychat/expertEval.css" rel="stylesheet"/>
</head>
<body>
<div id="localvideo-normal" class="video-box"></div>
<div class="cenetr-box" id="main" style="overflow-y: hidden; display: none">
    <div class="header">
        <img src="/img/anychat/logo.png" class="logo">
        <div class="header-title">
            <p>甘肃交易通电子评标</p>
            <p>远程异地评标室</p>
        </div>
        <div class="header-lit-title">
            <p>标段名称：${bidSection.bidSectionName}</p>
            <p>标段编号：${bidSection.bidSectionCode}</p>
        </div>
        <img src="/img/anychat/cmai.png" class="mai" onclick="changeAudio(this)">
    </div>
    <div class="bottom-box">
        <div class="bottom-box-left">
            <ul class="img-list" id="video-box">
                <li>
                    <div class="video-box" id="localvideo"></div>
                    <div class="bom-del">
                        ${expert.expertName}&nbsp;
                        <#if expert.isChairMan == 1>
                            <span class="lit-bg bg1">评标组长</span>
                        </#if>
                    </div>
                </li>
            </ul>
        </div>
        <div class="bottom-box-right">
            <div class="right-top" id="show-msg">
            </div>
            <div class="right-bom">
                <textarea placeholder="按enter发送消息" class="msg-box" onkeypress="sendmsg()"></textarea>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        // 绑定窗口变化的事件
        $(window).resize(function() {
            //获取浏览器窗口宽度
            var width = $(window).width();
            var height = $(window).height();

            if(width > 1900) {
                windowState = WINDOW_MAX;
            } else if(width <= 180){
                windowState = WINDOW_MIN;
            } else{
                windowState = WINDOW_NORMAL;
            }

            windowResize();
        });


        // 基础信息的初始化
        ip = '${ip}';
        port = '${port}';
        roomId = '${bidSection.id}';

        var expert = {"name": "${expert.expertName}", "chairMan": "${expert.isChairMan}"};
        expertJson = JSON.stringify(expert);

        onloadAnyChat();

    })

    function sendmsg() {
        // 点击ENTER
        if(event.keyCode === 13) {
            var msg = $("textarea").val().trim();
            if(msg) {
                // 发送消息
                BRAC_SendTextMessage(0, 0, msg);
                showMsg(mSelfUserId, msg);
                $("textarea").val("");
            }
        }
    }
</script>
</body>
</html>
