<link rel="stylesheet" href="${ctx}/css/scanning.css">
<script type="text/javascript" src="${ctx}/js/utf.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-qrcode.js"></script>
<div class="right-center site-demo-button" id="layerDemo">
    <div class="change">
        <ol>
            <li class="choice" data-value="2" onclick="changeAuth(this)">支付宝认证<span></span></li>
            <li data-value="1" onclick="changeAuth(this)">微信认证<span></span></li>
        </ol>
    </div>
    <div class="back-btn gray-f gray-s" onclick="returnMsgInput()">返回</div>
    <div class="sos-btn red-b layui-btn layui-btn-normal" data-method="offset" data-type="auto"
         onclick="toUrgentSigin()">紧急签到
    </div>
    <h2>请 ${bidderOpenInfo.clientName} 扫描二维码，进行身份认证</h2>
    <div class="qrDiv" onclick="changeAuth($('.choice'));">
    </div>
    <p>1、二维码有效时间为2分钟，且只能被扫描一次，链接失效后请点击二维码重新生成</p>
    <p>2、使用手机扫一扫功能，扫描二维码后，在打开的录制视频页面上传录制的2-5秒的视频即可</p>
    <p>3、上传成功后，等待反馈认证结果，通过后会自动认证成功</p>
</div>
<script>
    var polling = null;
    var lisstenPoll = null;

    var checkPayStateCount=0;
    $(function () {
        madeQR('${bidSectionId}', '${bidderId}', 2);
        //轮询监听认证状态
        polling = setInterval(function () {
            checkPayState('${bidderOpenInfo.id}');
        }, 1000);
        lissten();
    })

    /**
     * 切换认证类型 1 信ID 2 支付宝
     * */
    function changeAuth(obj) {
        $(obj).siblings().removeClass("choice");
        $(obj).addClass("choice");
        madeQR('${bidSectionId}', '${bidderId}', $(obj).attr("data-value"));
    }

    /**
     * 制作二维码
     * @param bidSectionId
     * @param bidderId
     * @param authType 选择的认证类型 1：信ID 2：支付宝
     */
    var l1 = false;

    function madeQR(bidSectionId, bidderId, authType) {
        //清空div
        $(".qrDiv").empty();

        if (l1) {
            return false;
        }
        l1 = true;
        //获取二维码生成数据，以及日志记录
        doLoading();
        $.ajax({
            url: "${ctx}/identityAuth/madeQR",
            type: "POST",
            cache: false,
            data: {
                "bidSectionId": bidSectionId,
                "bidderId": bidderId,
                "authType": authType,
            },
            success: function (data) {
                loadComplete();
                l1 = false;
                if (data) {
                    $(".qrDiv").qrcode({
                        width: 230,
                        height: 214,
                        text: '${base}' + data.url,
                        render: "canvas",
                        src: '${ctx}/img/qrLogo.png'             //二维码中间的图片
                    });
                }
                return false;
            },
            error: function (e) {
                l1 = false;
                loadComplete();
                console.error(e);
                if(e.status == 403){
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }
                return false;
            }
        });
    }

    var authStatus = false;

    function checkPayState(boiId) {
        if (!authStatus) {
            if(checkPayStateCount!=0){
                return ;
            }
            checkPayStateCount++;
            $.ajax({
                url: '${ctx}/identityAuth/isAuthPass',
                type: 'post',
                cache: false,
                async: false,
                data: {
                    boiId: boiId,
                },
                success: function (data) {
                    if (!isNull(data)) {
                        if (data === true) {
                            // 更改轮询标识
                            authStatus = true;
                        }

                    }
                    checkPayStateCount--;
                },
                error: function (data) {
                    console.error(data);
                    checkPayStateCount--;
                },
            });
        } else {
            // 关闭轮询
            clearTimeTerval();
            //跳转到支付成功页面
            goToUrl('${ctx}/identityAuth/identityAuthEnd', $(".identityAuthLi"));
        }

    }

    //返回信息填写
    var lock5 = false;

    function returnMsgInput() {
        if (lock5) {
            return null;
        }
        lock5 = true;
        //清空ticketNo
        $.ajax({
            url: "${ctx}/identityAuth/clearTicketNo/",
            type: "POST",
            cache: false,
            data: {
                id: '${bidderOpenInfo.id}'
            },
            success: function (data) {
                lock5 = true;
                if (data) {
                    goToUrl('${ctx}/identityAuth/msgInputPage', $(".identityAuthLi"));
                }
            },
            error: function (e) {
                lock5 = true;
                console.error(e);
                if(e.status == 403){
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }
            }
        });
    }

    //紧急签到
    function toUrgentSigin() {
        //关闭轮询
        clearTimeTerval();
        window.layer.open({
            type: 2,
            title: '紧急签到',
            area: ['50%', '60%'],
            btn: ['确认', '取消'],
            offset: 'auto',
            content: '${ctx}/identityAuth/urgentSiginPage?bidSectionId=' + '${bidSectionId}' + '&bidderId=' + '${bidderId}',
            btn1: function (index, layero) {
                var body = window.layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.updateUrgin(function () {
                    window.layer.close(index);
                    goToUrl('${ctx}/identityAuth/urgentSiginEndPage', $(".identityAuthLi"));
                });
            },
            btn2: function (index) {
                window.layer.close(index);
                goToUrl('${ctx}/identityAuth/identityAuthPage', $(".identityAuthLi"));
            },
            cancel: function () {
                goToUrl('${ctx}/identityAuth/identityAuthPage', $(".identityAuthLi"));
            }
        });
    }

    //离开页面关闭轮询
    window.onUnloadx = function (e) {
        clearTimeTerval();
    }


    /**
     * @Description 定时器监听轮询事件，防止一直请求服务器
     * 监听二维码div是否存在，不存在则关闭；
     * @Author      liuguoqiang
     * @Date        2020-8-10 14:03
     */
    function lissten() {
        lisstenPoll = setInterval(function () {
            if ($(".qrDiv").length <= 0) {
                clearTimeTerval();
            }
        }, 2000);
    }

    /**
     * 清除定时器
     */
    function clearTimeTerval() {
        if (polling != null) {
            window.clearInterval(polling);
        }
        if (lisstenPoll != null) {
            window.clearInterval(lisstenPoll);
        }
    }
</script>