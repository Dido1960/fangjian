<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>甘肃省房建市政电子辅助开标系统</title>
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
    <script src="${ctx}/js/convertMoney.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/entrust.css">
    <script type="text/javascript" src="${ctx}/js/webService.js"></script>

    <style>
        <#if isEndCheck>
            .tou li {
                <#if isA10Bid >
                    width: 22.5%;
                <#else>
                    width: 15.5%;
                </#if>
            }
            .custom-num {
                width: 10% !important;
            }
            form table {
                table-layout: fixed;
            }
            form table td {
                <#if isA10Bid >
                    width: 22.5%;
                <#else>
                    width: 15.5%;
                </#if>
            }
            .tou .how {
                width: 28% !important;
            }
            form table .how {
                width: 28% !important;
            }
        <#else>
            .tou li {
                <#if isA10Bid >
                    width: 20%;
                <#else>
                    width: 20.66%;
                </#if>
            }
            .custom-num {
                width: 10% !important;
            }
            form table {
                table-layout: fixed;
            }
            form table td {
                <#if isA10Bid >
                    width: 20%;
                <#else>
                    width: 20.66%;
                </#if>
            }
            .tou .how {
                width: 28% !important;
            }
            form table .how {
                width: 28% !important;
            }
        </#if>
    </style>
</head>
<body>
<div class="shang lit-btn site-demo-button" id="layerDemo">当前共有投标人： ${bidderNum}家
    <#if isEndCheck>
        <span onclick="updateCheckStatus(1)" class="layui-btn layui-btn-normal over" id="organe">重新检查</span>
    <#else>
        <span onclick="endingCheck()" id="red" data-method="offset" class="layui-btn layui-btn-normal over">结束检查</span>
    </#if>
</div>
<ul class="tou">
    <li class="custom-num">序号</li>
    <li>投标人名称</li>
    <li <#if isA10Bid && !isEndCheck>style="width: 50%" </#if>>投标人身份检查</li>
    <#if !isA10Bid>
        <li class="how">保证金状态</li>
    </#if>
    <#if isEndCheck>
        <li>检查结果</li>
    </#if>
    <li>操作</li>
</ul>

<form action="" class="layui-form" style="overflow: auto">
    <table cellpadding=0 cellpadding=0>
        <#list list as bidder>
            <tr>
                <td class="custom-num">${bidder_index + 1}
                    <#if bidder.bidderOpenInfo.isClientCheck==1>
                        <b>已检查</b>
                    <#else>
                        <b>未检查</b>
                    </#if>
                </td>
                <td>${bidder.bidderName}</td>
                <td <#if isA10Bid && !isEndCheck>style="width: 50%" </#if>>
                    <input type="checkbox" lay-filter="identity" class="test1" lay-skin="switch"
                           <#if bidder.bidderOpenInfo.bidderIdentityStatus==0><#else >checked="checked"</#if>
                           lay-text="符合|不符合" data-value="${bidder.bidderOpenInfo.id}"
                            <#if isEndCheck>disabled</#if>
                    >
                </td>
                <#if !isA10Bid>
                    <td class="how">
                        <p>
                            <label for="">保证金：</label>
                            <input type="radio" value="1" lay-filter="marginPay" name="marginPay_${bidder.id}"
                                   data-value="${bidder.bidderOpenInfo.id}" title="已缴纳"
                                   <#if bidder.bidderOpenInfo.marginPayStatus == 1>checked</#if>
                                    <#if isEndCheck>disabled</#if>
                            >
                            <input type="radio" value="0" lay-filter="marginPay" name="marginPay_${bidder.id}"
                                   data-value="${bidder.bidderOpenInfo.id}" title="未缴纳"
                                   <#if bidder.bidderOpenInfo.marginPayStatus == 0>checked</#if>
                                    <#if isEndCheck>disabled</#if>
                            >
                        </p>
                        <p>
                            <label for="">保函：</label>
                            <input type="radio" value="2" lay-filter="marginPay" name="marginPay_${bidder.id}"
                                   data-value="${bidder.bidderOpenInfo.id}" title="已递交"
                                   <#if bidder.bidderOpenInfo.marginPayStatus == 2>checked</#if>
                                    <#if isEndCheck>disabled</#if>
                            >
                        </p>
                    </td>
                </#if>

                <#if isEndCheck>
                    <td class="guo">
                        <span class="blove-f blove-s" onclick="clientCheckOnePage('${bidder.bidderOpenInfo.id}',true)">查看</span>
                    </td>
                    <#if bidder.bidderOpenInfo.tenderRejection != 1>
                        <td id="tender1" class="doing lit-btn site-demo-button" id="layerDemo">
                            <span class="red-b" onclick="saveTenderReason(this,'${bidder.id}')">标书拒绝</span>
                            <span class="gray-b">撤销</span>
                        </td>
                    <#else >
                        <td id="tender0" class="doing">
                            <span class="red-b" onclick="showTenderReason('${bidder.id}')">拒绝理由</span>
                            <span class="blove-b"
                                  onclick="cancelTenderReason(this,'${bidder.bidderOpenInfo.bidderId}')">撤销</span>
                        </td>
                    </#if>
                <#else >
                    <td>
                        <span onclick="clientCheckOnePage('${bidder.bidderOpenInfo.id}',false)"
                              class="blove-b das">开始检查</span>
                    </td>
                </#if>
            </tr>
        </#list>
    </table>
</form>


<div class="tan layui-form-item layui-form-text" style="display: none; z-index: 99999;">
    <div class="layui-input-block">
        <textarea id="tenderReason" placeholder="请输入内容" class="layui-textarea"></textarea>
    </div>
</div>
<div class="overTan layui-form-item layui-form-text" style="display: none; z-index: 99999;">
    <p></p>
</div>

<script>
    var isCheck = ${checkNum.isCheck};
    var notCheck = ${checkNum.notCheck};
    layui.use(['form'], function () {
        var form = layui.form;
        form.render('checkbox');
        form.render('radio');

        form.on('switch(identity)', function (data) {
            clientCheck("identity", data);
        });
        form.on('radio(marginPay)', function (data) {
            clientCheck("marginPay", data);
        });
    });

    //跳转检查页面
    function clientCheckOnePage(id, isShow) {
        window.top.layer.open({
            type: 2,
            offset: 'auto',
            title: ['授权委托书查看', 'text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);'],
            content: "${ctx}/clientCheck/clientCheckOnePage?bidderIdTo=" + id + "&bidSectionId=${bidSectionId}&isShow=" + isShow,
            area: ['1000px', '860px'],
            shade: 0.3,
            resize: false,
            move: false,
            end: function (index, layero) {
                window.top.layer.close(index);
                if (!isShow) {
                    window.top.goToUrl('${ctx}/clientCheck/clientCheckPage', window.top.$("#checkLi"))
                }
            }
        });
    }

    //列表页检查
    function clientCheck(checkType, data) {
        var passType;
        if (checkType === "marginPay") {
            passType = $(data.elem).val();
        } else {
            passType = 0;
            if (data.elem.checked) {
                passType = 1;
            }
        }
        $.ajax({
            url: "${ctx}/clientCheck/clientCheckThis",
            type: "POST",
            cache: false,
            async: true,
            data: {
                "boiId": $(data.elem).attr("data-value"),
                "checkType": checkType,
                "passType": passType
            },
            success: function (data) {
                if (data) {
                    layer.msg('操作成功！', {icon: 1});
                } else {
                    layer.alert('操作失败', {
                        icon: 5,
                        btnAlign: 'c',
                        end: function () {
                            window.location.href = window.location.href;
                        }
                    });
                }
            },
            error: function (e) {
                console.error(e);
                if (e.status == 403) {
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }
            }
        });
        $(data.elem).parents("li").find(".cha").text("已检查");
    }

    //更新标段检查状态
    function updateCheckStatus(checkStatus) {
        var msg = checkStatus === 1 ? '委托人身份检查已开启' : '委托人身份检查已结束';
        $.ajax({
            url: "${ctx}/clientCheck/updateCheckStatus",
            type: "POST",
            cache: false,
            data: {
                "bidSectionId": '${bidSectionId}',
                "bidderCheckStatus": checkStatus,
                "msg": msg
            },
            success: function (data) {
                if (data) {
                    // 添加公告
                    postMessageFun({linkType:msg});
                    parent.window.location.reload();
                    return false;
                }
            },
            error: function (e) {
                console.error(e);
                parent.window.location.reload();
            }
        });
    }

    //弹出拒绝理由填写
    function saveTenderReason(elem, bidderId) {
        window.top.layer.open({
            type: 2,
            offset: 'auto',
            title: ['标书拒绝', 'height:57px;text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);line-height:57px;padding:0;'],
            id: 'layerDemo',
            content: '${crx}/staff/bidRejectionPage?flag=0&bidSectionId=${bidSectionId}&bidderId=' + bidderId,
            area: ['600px', '360px'],
            btn: ['确认', '取消'],
            btnAlign: 'c',
            shade: 0.3,
            yes: function (index, layero) {
                var body = window.top.layer.getChildFrame('body', index);
                var iframeWin = window.top[layero.find('iframe')[0]['name']];
                iframeWin.submitReason(function () {
                    window.top.layer.close(index);
                    window.location.reload();
                });
            }
        });
    }

    function showTenderReason(bidderId) {
        // 显示拒绝理由
        window.top.layer.open({
            type: 2,
            offset: 'auto',
            title: ['标书拒绝', 'height:57px;text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);line-height:57px;padding:0;'],
            id: 'layerDemo',
            content: '${crx}/staff/bidRejectionPage?flag=1&bidSectionId=${bidSectionId}&bidderId=' + bidderId,
            area: ['600px', '360px'],
            btnAlign: 'c',
            shade: 0.3
        });
    }


    var lock2 = false;

    //撤销
    function cancelTenderReason(elem, bidderId) {
        var _index = window.top.layer.confirm('确定要撤销吗？', {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function () {
            window.top.layer.close(_index)
            revoke_status(elem, bidderId);
        }, function () {

        });
    }

    /**
     * 执行撤销标书拒绝操作
     * @param bidderId
     */
    function revoke_status(elem, bidderId) {
        doLoading();
        $.ajax({
            url: '${ctx}/staff/updateBidderOpenInfo',
            type: 'post',
            cache: false,
            data: {
                'bidSectionId': '${bidSectionId}',
                'bidderId': bidderId,
                'tenderRejection': 0,
                'tenderRejectionReason': ''
            },
            success: function (data) {
                loadComplete();
                if (data) {
                    parent.layer.msg("撤销成功!", {icon: 1});
                    setTimeout(function () {
                        window.location.reload();
                    }, 1500);
                }
            },
            error: function (e) {
                loadComplete();
                console.error(e);
                if (e.status == 403) {
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }
            }
        });
    }

    //切换拒绝按钮
    function turnDiv(elem, status) {
        $(elem).parents("li").find("#tender1").css("display", "none");
        $(elem).parents("li").find("#tender0").css("display", "none");
        if (status == 0) {
            $(elem).parents("li").find("#tender1").css("display", "flex");
        }
        if (status == 1) {
            $(elem).parents("li").find("#tender0").css("display", "flex");
        }
    }

    //结束检查判断当前是否检查完毕
    var lock4 = false;

    function endingCheck() {
        if (lock4) {
            return false;
        }
        lock4 = true;
        $.ajax({
            url: "${ctx}/clientCheck/endingCheck",
            type: "POST",
            cache: false,
            async: true,
            data: {"bidSectionId": '${bidSectionId}'},
            success: function (data) {
                lock4 = false;
                if (data.notCheck > 0) {
                    //弹出确认框
                    openEndCheckSure(data);
                } else {
                    //判断原因列表大小是否为0
                    if (data.listSize > 0) {
                        //弹出原因框
                        openExceReacon();
                    } else {
                        updateCheckStatus(2);
                    }

                }
            },
            error: function (e) {
                lock4 = false;
                console.error(e);
                if (e.status == 403) {
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }
            }
        });
    }

    //弹出确认框
    function openEndCheckSure(data) {
        $('.overTan p').text("还有" + data.notCheck + "位投标人未检查核验身份，确定结束检查吗？")
        var index = layer.open({
            type: 1
            ,
            offset: 't'
            ,
            title: ['结束检查', 'height:57px;text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);line-height:57px;padding:0;']
            ,
            content: $('.overTan')
            ,
            area: ['600px', '360px']
            ,
            btn: ['确认', '取消']
            ,
            btnAlign: 'c' //按钮居中
            ,
            shade: 0.01 //不显示遮罩
            ,
            yes: function () {
                layer.close(index);
                //判断原因列表大小是否为0
                if (data.listSize > 0) {
                    //弹出原因框
                    openExceReacon();
                } else {
                    updateCheckStatus(2);
                }
            }
        });
    }

    //弹出原因页面
    function openExceReacon() {
        window.top.layer.open({
            type: 2
            , offset: "auto"
            , title: ['异常原因查看', 'text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);']
            , content: '/clientCheck/getExceToReasonPage?bidSectionId=${bidSectionId}'
            , btn: ['确认', '取消']
            , area: ['60%', '60%']
            , btnAlign: 'c' //按钮居中
            , shade: 0.3 //不显示遮罩
            , resize: false //不允许拖拽
            , move: false //不允许移动
            , btn1: function (index, layero) {
                var body = layer.getChildFrame('body', index);
                var iframe = window.top[layero.find('iframe')[0]['name']];
                //执行完成后的回调函数
                iframe.updateReason(function () {
                    window.top.layer.close(index);
                    updateCheckStatus(2);
                });
            }
            , btn2: function (index) {
                // 点击取消的回调函数
                window.top.layer.close(index);
            }
        });
    }

</script>

<script>
    layui.use('layer', function () { //独立版的layer无需执行这一句
        var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句

        //触发事件
        var active = {
            clientCheckOnePage: function (othis) {
                clientCheckOnePage(othis.attr("data-value"), false);
            }
        };

        $('#layerDemo .das').on('click', function () {
            var othis = $(this), method = othis.data('method');
            active[method] ? active[method].call(this, othis) : '';
        });

    });

    $(function () {
        if (!'${isPublishBidderEnd}') {
            window.top.layer.alert("公布投标人名单环节未结束检查！");
        }
    })
</script>

</body>