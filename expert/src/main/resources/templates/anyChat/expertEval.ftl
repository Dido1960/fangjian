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
<div id="localvideo-normal" class="video-box" style="height: 93vh;width: 95vw;"></div>
<div id="main" style="display: none; height: 98vh;">
    <h2>甘肃交易通电子评标 (远程异地评标室)</h2>
    <div class="box">
        <div class="box-top">
            <div>
                <h3>标段名称</h3>
                <p title="${bidSection.bidSectionName}">${bidSection.bidSectionName}</p>
            </div>
            <div>
                <h3>标段编号</h3>
                <p title="${bidSection.bidSectionCode}">${bidSection.bidSectionCode}</p>
            </div>
        </div>
        <div class="cont">
            <div class="cont-left layui-form">
                <div class="shutUp layui-form-item">
                    当前在线人数：<b class="current-online-count">0</b>人
                    <#if expert.isChairman == 1><input type="checkbox" id="maiSwitch" name="maiSwitch" lay-skin="switch" lay-filter="mai-switch" lay-text="全员开麦|全员闭麦"/></#if>
                </div>
                <ul id="expert-video">
                    <li>
                        <div class="face" id="localvideo"></div>
                        <div class="name">
                            ${expert.name}
                            <span>
                                <#if expert.isChairman == 1>(评标组长)</#if>
                            </span>
                            <i class="mai" data-status="1" onclick="changeSingleAudio('-1', this)"></i>
                        </div>
                    </li>
                </ul>
            </div>
            <div class="cont-right">
                <ul id="show-msg" style="border-bottom: 1px solid grey">

                </ul>
                <textarea id="sendMsg" class="layui-textarea" placeholder="请输入内容,按enter发送" onkeypress="sendMsg()"></textarea>
                <div class="foot" style="border-top: 1px solid grey">
                    <div class="btn" onclick="clickMsg()">发送</div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    var status = 1;
    var loading=false;
    $(function () {
        layui.use('form', function() {
            var form = layui.form;
            form.on('switch(mai-switch)', function(data){
                var status = this.checked ? "1" : "0";
                changeAudio(status);
            });
            form.render();
        });
        charMan = '${expert.isChairman}';
        // 基础信息的初始化
        ip = '${ip}';
        port = '${port}';
        roomId = '${bidSection.id}';

        var expert = {"name": "${expert.name}", "chairMan": "${expert.isChairman }"};
        expertJson = JSON.stringify(expert);

        onloadAnyChat();
    });

    function sendMsg() {
        // 点击ENTER
        if(event.keyCode === 13) {
            var msg = $("#sendMsg").val().trim();
            if(msg) {
                // 发送消息
                BRAC_SendTextMessage(0, 0, msg);
                showMsg(mSelfUserId, msg);
                $("textarea").val("");
            }
        }
    }

    function clickMsg() {
        var msg = $("#sendMsg").val().trim();
        if(msg) {
            // 发送消息
            BRAC_SendTextMessage(0, 0, msg);
            showMsg(mSelfUserId, msg);
            $("textarea").val("");
        }
    }
</script>
</body>
</html>
