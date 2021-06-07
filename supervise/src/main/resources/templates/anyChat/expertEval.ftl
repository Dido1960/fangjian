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
    <script type="text/javascript" src="${ctx}/js/jquery-1.12.3.min.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script src="${ctx}/js/common.js"></script>
    <!--[if lt IE 9]>
    <script src="${ctx}/js/html5shiv.min.js"></script>
    <script src="${ctx}/js/respond.min.js"></script>
    <![endif]-->
    <script language="javascript" type="text/javascript" src="${ctx}/plugin/anychat/javascript/logicfunc.js" charset="GB2312"></script>
    <script language="javascript" type="text/javascript" src="${ctx}/plugin/anychat/javascript/anychatsdk.js" charset="GB2312"></script>
    <script language="javascript" type="text/javascript" src="${ctx}/plugin/anychat/javascript/anychatevent.js" charset="GB2312"></script>
    <script language="javascript" type="text/javascript" src="${ctx}/plugin/anychat/javascript/advanceset.js" charset="GB2312"></script>
    <script language="javascript" type="text/javascript" src="${ctx}/js/anychat/expertEvalPicture.js"></script>
    <link type="text/css" href="${ctx}/css/anychat/expertEval.css" rel="stylesheet"/>
    <link type="text/css" href="${ctx}/css/utils.css" rel="stylesheet"/>
</head>
<body>
<div id="localvideo-normal" class="video-box" style="width: 310px;height: 276px;"></div>
<div id="main" style="display: none">
    <h2>甘肃交易通电子评标 (远程异地评标室)</h2>
    <div class="box">
        <div class="box-top">
            <div>
                <h3>标段名称</h3>
                <p>${bidSection.bidSectionName}</p>
            </div>
            <div>
                <h3>标段编号</h3>
                <p>${bidSection.bidSectionCode}</p>
            </div>
        </div>
        <#if chairMan == 1>
            <div class="shutUp" id="open-mai" onclick="changeAudio('0')">全员开麦</div>
            <div class="shutUp" id="close-mai" onclick="changeAudio('1')">全员闭麦</div>
        </#if>
        <div class="cont">
            <div class="cont-left">
                <ul id="expert-video">
                    <li>
                        <div class="face" id="localvideo"></div>
                        <div class="name">${expertName}<span><#if chairMan == 1>(评标组长)</#if></span>
                            <i class='mai' data-status="1" onclick="changeSingleAudio('-1', this)"></i></div>
                    </li>
                </ul>
            </div>
            <div class="cont-right">
                <ul id="show-msg">

                </ul>
                <textarea name="" id="" clas="layui-textarea" placeholder="请输入内容,按enter发送" onkeypress="sendmsg()"></textarea>
                <#--<div class="foot">
                    <div class="btn">发送</div>
                </div>-->
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        charMan = '${chairMan}';
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

        var expert = {"name": "${expertName}", "chairMan": "${chairMan }"};
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
