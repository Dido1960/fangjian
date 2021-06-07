<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>消息盒子</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-1.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-3.1.0.min.js"></script>
    <#include "${ctx}/webSocketBase/onlineBase.ftl">
    <!--[if lt IE 9]>
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/messageBox.css">
    <#--第一步：编写模版。你可以使用一个script标签存放模板，如：-->
    <script id="messageList" type="text/html">
        {{#  layui.each(d, function(index, lineMsg){ }}
            <div class="content">
                {{# if(!isNull(lineMsg.roleType) && lineMsg.roleType === 0){ }}
                    <img src="${ctx}/img/bells.png" alt="">
                    <div class="center">
                        <p class="gray-f">系统消息<span>{{lineMsg.insertTime||''}}</span></p>
                        <h3 class="gray-f">{{lineMsg.message}}</h3>
                    </div>
                {{# }else if(!isNull(lineMsg.roleType) && lineMsg.roleType === 2){ }}
                    <img src="${ctx}/img/mages.png" alt="">
                    <div class="center">
                        <p>{{lineMsg.sendName}}<span>{{lineMsg.insertTime||''}}</span></p>
                        {{# if(!isNull(lineMsg.message)){ }}
                            <h3>{{lineMsg.message}}</h3>
                        {{# } }}
                    </div>
                {{# }else{ }}
                    <img src="${ctx}/img/mages.png" alt="">
                    <div class="center">
                        {{# if(!isNull('${bidder.id}') && '${bidSection.bidClassifyCode}' === 'A10'){ }}
                            {{# if(isNull(lineMsg.bidderId) || lineMsg.bidderId == '${bidder.id}'){ }}
                            <p>{{lineMsg.sendName}}<span>{{lineMsg.insertTime}}</span></p>
                            {{# }else{ }}
                            <p>************<span>{{lineMsg.insertTime||''}}</span></p>
                            {{# } }}
                        {{# }else{ }}
                            <p>{{lineMsg.sendName}}<span>{{lineMsg.insertTime||''}}</span></p>
                        {{# } }}
                        {{# if(!isNull(lineMsg.message)){ }}
                            <h3>{{lineMsg.message}}
                                {{# if(isNull('${bidder.id}')){ }}
                                    <span class="callBack" onclick="reply('{{lineMsg.id }}')">回复</span>
                                {{# } }}
                                {{# if(!isNull(lineMsg.objectionFileId)){ }}
                                    <span class="watch" onclick="viewPicture('{{lineMsg.objectionUrl }}')">查看图片</span>
                                {{# } }}
                            </h3>
                        {{# } }}
                        {{# if(!isNull(lineMsg.backMsg)){ }}
                            <i>{{lineMsg.backName}} 回复：<span> {{lineMsg.backMsg}}</span></i>
                        {{# } }}
                    </div>
                {{# } }}
            </div>
        {{# }); }}
    </script>
</head>
<body>
    <div class="box messageRecord" id="messageRecord">
        <#--<div class="content">
            <img src="${ctx}/img/mages.png" alt="">
            <div class="center">
                <p class="gray-f">系统消息<span>2020-06-30 15:00:00</span></p>
                <h3 class="gray-f">投标文件递交检查结束</h3>
            </div>
        </div>
        <div class="content">
            <img src="${ctx}/img/mages.png" alt="">
            <div class="center">
                <p>交易通招标代理公司<span>2020-06-30 15:00:00</span></p>
                <h3>身份不符合 <span class="callBack">回复</span></h3>
                <i>交易通招标代理公司 回复：<span> 没关系啦</span></i>
            </div>
        </div>
        <div class="content">
            <img src="${ctx}/img/mages.png" alt="">
            <div class="center">
                <p>交易通招标代理公司<span>2020-06-30 15:00:00</span></p>
                <h3>身份不符合 <span class="callBack">回复</span> <span class="watch">查看图片</span></h3>
            </div>
        </div>-->
    </div>
    <div class="sendContent" style="height: 15%;">
        <textarea placeholder="请输入想要发送的内容" class="willSendContent"></textarea>
    </div>
</body>
<script>
    var layer, laytpl, laypage, element;
    $(function () {
        layui.use(['layer', 'element', 'laytpl', 'laypage'], function () {
            layer = layui.layer;
            element = layui.element;
            laypage = layui.laypage;
            laytpl = layui.laytpl;
            getUser();
            updateChatHistroy();
            updateLineMsgRead('${bidSection.id}');
        });
    })
    /**
     * 获取当前用户
     */
    var _currentUserName;
    function getUser() {
        $.ajax({
            url: '${ctx}/login/getUser',
            type: 'post',
            cache: false,
            async: false,
            success: function (data) {
                _currentUserName = data.name;
            }
        });
        if (!isNull("${bidder.id}")) {
            $(".sendContent .willSendContent").attr("disabled", true);
        }
    }
    var _send_state = false;

    function userSendMsg(roleType) {
        var $sendContent = $(".willSendContent");
        var sendMsg = $sendContent.val().trim();
        if (isNull(sendMsg)) {
            return;
        }

        if (_send_state) {
            return;
        }
        _send_state = true;
        $.ajax({
            type: 'post',
            url: '${ctx}/messageBox/addUserSendMsg',
            data: {
                "bidSectionId": "${bidSection.id}",
                "sendName": _currentUserName,
                "message": sendMsg,
                "roleType": roleType,
                "bidderId": "${bidder.id}"
            },
            success: function () {
                _send_state = false;
                $sendContent.val("");
                updateChatHistroy();
            }
        });
    }

    var _lineMsgId = 0;

    var getLastMsgCount=0;
    $(window).load(function () {
        webSocket("${ctx}/online/${bidSection.id}", "建立websocket连接", function (data) {
            if (isNull('${bidder.id}')) {
                if (data.question === '1' || data.resume === '1') {
                    layer.alert(data.message, {"title": "投标人质疑信息"});
                } else if (!isNull(data.bidderId)) {
                    layer.msg(data.message);
                }
            } else {
                if (data.roleType === '2') {
                    layer.alert(data.message);
                }
            }
            updateChatHistroy();
        });

        //获取最新消息
        setInterval(function () {
            if(getLastMsgCount!=0){
                return ;
            }
            getLastMsgCount++;
            $.ajax({
                type: "post",
                url:'${ctx}/messageBox/getLastLineMsg?bidSectionId=${bidSection.id}',
                success:function(data){
                    if(isNull(data)){
                        return;
                    }

                    if(data.id == _lineMsgId){
                        return;
                    }
                    _lineMsgId = data.id;
                    if(isNull(data.message)){
                        return;
                    }

                    if (isNull('${bidder.id}')) {
                        if (data.question === "1") {
                            layer.alert(data.message);
                        } else if (!isNull(data.bidderId)) {
                            layer.alert(data.message);
                        }
                    } else {
                        if (data.roleType === "0") {
                            layer.alert(data.message);
                        }
                    }
                    updateChatHistroy();
                    getLastMsgCount--;
                }
            });
        }, 5000);
    });

    /***
     * 获取项目信息
     * */
    function updateChatHistroy() {
        //$(".messageRecord").find(".content").remove();
        $.ajax({
            type: 'post',
            url: '${ctx}/messageBox/listLineMsg?bidSectionId=${bidSection.id}',
            success: function (data) {
                if (!isNull(data)) {
                    var getTpl = messageList.innerHTML
                        , view = document.getElementById('messageRecord');
                    laytpl(getTpl).render(data, function (html) {
                        view.innerHTML = html;
                    });
                    // 使滚动条滚动到最下面
                    $("#messageRecord").scrollTop($("#messageRecord")[0].scrollHeight);
                }
            }
        })
    }

    function reply(id) {
        layer.prompt({title: '请输入回复内容', formType: 2}, function (text, index) {
            _send_state = true;
            layer.close(index);
            $.ajax({
                type: 'post',
                url: '${ctx}/messageBox/updateLineMsg',
                data: {
                    "id": id,
                    "backMsg": text,
                    "backName": _currentUserName
                },
                success: function (data) {
                    _send_state = false;
                    if (data) {
                        updateChatHistroy();
                        layer.msg("质疑信息回复成功",{icon: 1});
                    }
                }
            });
        });
    }

    /**
     * 更新消息阅读情况
     * @param bidSectionId
     */
    function updateLineMsgRead(bidSectionId) {
        $.ajax({
            type: 'post',
            url: '${ctx}/messageBox/updateLineMsgRead',
            data: {
                "bidSectionId": bidSectionId,
                "bidderId": '${bidder.id}'
            },
            success: function () {
                parent.getMessageCount();
            }
        });
    }

</script>

</html>