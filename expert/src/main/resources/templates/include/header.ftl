<link rel="stylesheet" href="${ctx}/css/header.css">
<link rel="stylesheet" href="${ctx}/css/utils.css">
<header>
    <div class="text">
        <div class="name">
            <img src="${ctx}/img/logo_blue.png" alt="">
            甘肃省房建市政电子辅助评标系统
        </div>
        <div class="bao">
            <#if expert.isChairman == 1>
                <div class="off blove-f" title="点击回退主页" onclick="window.location.href='/expert/confirmBidEvalPage'">专家组长</div>
            <#else >
                <div class="off blove-f" title="点击回退主页" onclick="window.location.href='/expert/confirmBidEvalPage'">评标专家</div>
            </#if>
            <div class="try" onclick="exitSystem()">
                <b class="username" title="${expert.name}">${expert.name}</b>
                <i></i>
            </div>
        </div>
        <#if !menu??>
            <div class="more">
                <#if expert.isChairman == 1>
                <#--<span>无效投标</span>-->
                    <span onclick="evalBack()">评审回退</span>
                    <span onclick="evalBackRecord()">回退记录</span>
                </#if>
                <span class="still pitch">更多
                <ol id="moreOl">
                    <#--<li>项目废标</li>-->
                    <#if bidSection.bidClassifyCode == "A08">
                        <li onclick="checkList()">查看清单</li>
                    </#if>
                    <li onclick="verifySignature()">签章验证</li>
                    <li onclick="bidFileCompared()">文件对比</li>
                    <li onclick="recordFile()">开标记录表</li>
                    <li onclick="clarifyFile()">澄清文件</li>
                </ol>
            </span>
            </div>
        </#if>
    </div>
</header>


<script>
    $(function () {
        validRemoteEvaluation();
        if ('${bidSection.bidSection.bidClassifyCode != 'A10'}'){
            isHaveRelateBid();
        }
        //查看当前是否有未接收的消息
        setInterval(getBackPush, 3000);

        //获取组长消息
        if ('${expert.isChairman}' != 1 ){
            setInterval(getExpertUserMsg,3000);
        }
    })

    /**
     * 获取当前用户
     */
    function getUser() {
        $.ajax({
            url: '${ctx}/getUser',
            type: 'post',
            cache: false,
            success: function (data) {
                $('.try .username').append(data.name);
            }
        });
    }

    /**
     * 退出登录
     */
    function exitSystem() {
        double_iframe_hide_pdf();
        layer.confirm("确认要退出系统？", {
            icon: 3,
            title: '提示'
        }, function (index) {
            layer.close(index);
            layer.load();
            logout();
        }, function (index) {
            // 取消的回调函数
            layer.msg("已取消!", {icon: 1});
            layer.close(index);
        });
    }

    function logout() {
        $.ajax({
            url: '${ctx}/logout',
            type: 'post',
            cache: false,
            success: function () {
                window.location.href = "${ctx}/login.html";
            }
        });
    }

    /**
     * 判断当前标段是否有关联标段
     * */
    function isHaveRelateBid() {
        $.ajax({
            url: "${ctx}/expert/evalPlan/isHaveRelateBid",
            type: "POST",
            cache: false,
            success: function (data) {
                if (data) {
                  $("#moreOl").append("<li onclick='relatePage()'>预审关联</li>")
                }
            },
            error:function (e) {
                console.error(e);
            }
        });
    }

    /**
     * 预审关联页面
     * */
    function relatePage() {
        window.open("${ctx}/expert/evalPlan/bidRelatePage", "_blank");
    }

    /**
     * 评审回退
     */
    function evalBack() {
        //判断是否符合评审条件
        setTimeout(function () {
            $.ajax({
                url: "${ctx}/evalBack/validBack",
                type: "POST",
                cache: false,
                success: function (data) {
                    double_iframe_hide_pdf();
                    if (data.code === "2") {
                        layer.msg(data.msg, {
                            icon: 2, time: 3000, end: function () {
                                double_iframe_show_pdf();
                            }
                        });
                    } else {
                        loadBackStepPage();
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
        }, 400)
    }

    /**
     * 评审回退页面
     */
    function loadBackStepPage() {
        layer.open({
            type: 2,
            title: "评审回退",
            content: "${ctx}/evalBack/loadBackStepPage",
            offset: 'auto',
            area: ['40%', '70%'],
            resize: false,
            move: false,
            end: function () {
                double_iframe_show_pdf();
            }
        });
    }

    /**
     * 远程异地评标小窗口
     */
    function validRemoteEvaluation() {
        if ('${bidSection.remoteEvaluation}' === '1' && '${bidSection.evalStatus}' !== '2') {
            layer.open({
                id: 'layer-remote-eval-${bidSection.id}',
                type: 2,
                title: '在线通讯',
                content: "${ctx}/expert/expertEval",
                area: ['15vw', '25vh'],
                offset: 'lb',
                maxmin: true,
                resize: false,
                shade: false,
                shadeClose: false,
                closeBtn: false,
                full: function (layero) {
                    double_iframe_hide_pdf();
                    var iframeWin = window.top[layero.find('iframe')[0]['name']];
                    iframeWin.resizeMax();
                },
                min: function (layero) {
                    double_iframe_show_pdf();
                    var iframeWin = window.top[layero.find('iframe')[0]['name']];
                    iframeWin.resizeMin();
                },
                restore: function (layero) {
                    double_iframe_show_pdf();
                    var iframeWin = window.top[layero.find('iframe')[0]['name']];
                    iframeWin.resizeNormal();
                },
                end: function () {
                    // 禁止关闭 销毁后自动重启
                    double_iframe_show_pdf();
                    validRemoteEvaluation();

                }
            })
        }
    }

    /**
     * 获取最新回退消息审核情况阅读情况
     */
    var backe_message = "";

    var back_push_status = 0;
    function getBackPush() {
        if (back_push_status !=0) {
            return false;
        }
        back_push_status ++;
        $.ajax({
            url: "${ctx}/evalBack/getBackPush",
            type: "POST",
            cache: false,
            success: function (data) {
                if (data.code === "1") {
                    if (isNull(backe_message) || backe_message != data.msg) {
                        backe_message = data.msg;
                        layerAlertAndEndForExpert(data.msg, null, updateBackPush, function () {
                            localStorage.clear();
                            if (data.data === 0) {
                                logout();
                            } else {
                                returnUpFlow();
                            }
                        });
                    }
                } else if (data.code === "2") {
                    layerAlertAndEndForExpert(data.msg, null, updateBackPush, function () {
                        returnUpFlow();
                    }, 2);
                }
                back_push_status --;
            },
            error: function (e) {
                console.error(e);
                if (e.status == 403) {
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }
                back_push_status --;
            }
        });
    }

    //获取组长消息
    var expert_user_msg_count = 0;
    function getExpertUserMsg() {
        if (expert_user_msg_count !=0) {
            return false;
        }
        expert_user_msg_count ++;
        $.ajax({
            url: "${ctx}/expert/evalPlan/getExpertUserMsg",
            type: "POST",
            cache: false,
            success: function (data) {
                if (data.msgStatus === 1) {
                    //环节重评
                    single_double_iframe_hide_pdf();
                    window.top.layer.msg(data.msg, {
                        icon: 1,
                        time: 3000,
                        shade: [0.3, '#393D49'],
                        success: function (layero) {
                            deleteExpertUserMsg();
                        },
                        end: function () {
                            single_double_iframe_show_pdf();
                            window.location.reload();
                        }
                    });
                } else if (data.msgStatus === 2) {
                    //小组结束
                    window.top.layer.msg(data.msg, {
                        icon: 1,
                        time: 3000,
                        shade: [0.3, '#393D49'],
                        success: function (layero) {
                            deleteExpertUserMsg();
                        },
                        end: function () {
                            single_double_iframe_show_pdf();
                            returnUpFlow();
                        }
                    });
                }
                expert_user_msg_count --;
            },
            error: function (e) {
                console.error(e);
                if (e.status == 403) {
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }
                expert_user_msg_count --;
            }
        });
    }

    /**
     * 已读消息删除
     * */
    function deleteExpertUserMsg() {
        $.ajax({
            url: "${ctx}/expert/evalPlan/deleteExpertUserMsg",
            type: "POST",
            cache: false,
            success: function (data) {

            },
            error: function (e) {
                console.error(e);
            }
        });
    }

    /**
     * 返回到评标首页
     */
    function returnUpFlow() {
        doLoading("请稍后",null,null,null,function (){
            window.location.href = "${ctx}/expert/startEval";
        },null)

    }

    /**
     * 更新回退神审核消息阅读情况
     */
    function updateBackPush() {
        $.ajax({
            url: "${ctx}/evalBack/updateBackPush",
            type: "POST",
            cache: false,

            success: function (data) {
            },
            error: function (e) {
                console.error(e);
                if (e.status == 403) {
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }
            }
        });
    }

    /**
     * 文件比对
     */
    function bidFileCompared() {
        window.open("${ctx}/viewPdf/bidFileComparedPage", "_blank")
    }

    /**
     * 查看清单
     */
    function checkList() {
        double_iframe_hide_pdf();
        window.layer.open({
            type: 2,
            title: ['查看清单', 'height:57px;text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);line-height:57px;padding:0;'],
            shadeClose: true,
            area: ['1520px', '770px'],
            btn: ['关闭'],
            resize: false,
            move: false,
            offset: 'auto',
            content: "${ctx}/viewPdf/checkListPage",
            btn1: function (index) {
                window.layer.close(index);
            },
            end: function () {
                double_iframe_show_pdf();
            }
        });
    }

    /**
     * 开标记录表查看
     */
    function recordFile() {
        double_iframe_hide_pdf();
        window.layer.open({
            type: 2,
            title: ['开标记录表', 'height:57px;text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);line-height:57px;padding:0;'],
            shadeClose: true,
            area: ['1520px', '770px'],
            //btn: ['关闭'],
            resize: false,
            move: false,
            offset: 'auto',
            content: "${ctx}/viewPdf/recordFilePage",
            btn1: function (index) {
                window.layer.close(index);
            },
            end: function () {
                double_iframe_show_pdf();
            }
        });
    }

    /**
     * 查看澄清文件
     */
    function clarifyFile() {
        double_iframe_hide_pdf();
        window.layer.open({
            type: 2,
            title: ['澄清文件', 'height:57px;text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);line-height:57px;padding:0;'],
            shadeClose: true,
            area: ['1520px', '770px'],
            btn: ['关闭'],
            resize: false,
            move: false,
            offset: 'auto',
            content: "${ctx}/viewPdf/clarifyFilePage",
            btn1: function (index) {
                window.layer.close(index);
            },
            end: function () {
                double_iframe_show_pdf();
            }
        });
    }

    /**
     * 专家组长选择流标进行状态
     * @param isChairMan 是否是组长
     * @param msg 非组长提示信息
     */
    function chooseCancelBidSection(isChairMan, msg) {
        if (isChairMan == '1') {
            if (isNull(msg)) {
                layer.confirm("有效投标人不足三家,是否继续进行评标?", {
                    icon: 3,
                    title: '提示',
                    btn: ['继续评标', '流标']
                }, function (index) {
                    // 确认的回调函数
                    cancelBidSection(2);
                }, function (index) {
                    cancelBidSection(1);
                });
            } else {
                layer.alert(msg, {icon: 2, end: function () {
                        window.top.location.reload();
                    }});
            }
        } else {
            if (isNull(msg)) {
                layer.alert("有效投标人不足三家,请等待评标组长确认是否继续评标!", {icon: 3});
            } else {
                layer.alert(msg, {icon: 2, end: function () {
                    window.top.location.reload();
                    }});
            }
        }
    }

    /**
     * 标段流标
     * @param flag
     */
    function cancelBidSection(flag) {
        if (flag === 1) {
            double_iframe_hide_pdf();
            layer.prompt({title: '流标理由', formType: 2}, function (text, index) {
                $.ajax({
                    url: "${ctx}/expert/cancelBidSection",
                    type: "POST",
                    data: {
                        "cancelStatus": flag,
                        "cancelReason": text
                    },
                    success: function (data) {
                        double_iframe_show_pdf();

                        if (data) {
                            layerAlert("操作成功，是否进入评审结果生成评标报告？", function () {
                                window.location.href = "${ctx}/expert/evalPlan/endBasePage";
                            }, null, 1);
                        } else {
                            layerAlert("操作失败!", null, null, 2);
                        }
                    }
                })
                layer.close(index);
            });
        } else {
            $.ajax({
                url: "${ctx}/expert/cancelBidSection",
                type: "POST",
                data: {
                    "cancelStatus": flag
                },
                success: function (data) {
                    if (data) {
                        layerAlert("操作成功!", null, null, 1);
                    } else {
                        layerAlert("操作失败!", null, null, 2);
                    }
                }
            })
        }
    }

    /**
     * 评审回退记录
     */
    function evalBackRecord() {
        double_iframe_hide_pdf();
        window.layer.open({
            type: 2,
            title: ['回退记录', 'height:57px;text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);line-height:57px;padding:0;'],
            shadeClose: true,
            area: ['1520px', '770px'],
            btn: ['关闭'],
            resize: false,
            move: false,
            offset: 'auto',
            content: "${ctx}/evalBack/evalBackRecord",
            btn1: function (index) {
                window.layer.close(index);
            },
            end: function () {
                double_iframe_show_pdf();
            }
        });
    }
</script>
<script>
    $('.radio').on('click', 'div', function () {
        $(this).addClass('chioce').siblings().removeClass('chioce')
    })

    // 页面头部的更多的下拉效果
    $('.more').on('click', '.still', function () {
        if ($(this).hasClass('onlist')) {
            $(this).removeClass('onlist')
        } else {
            $(this).addClass('onlist')
        }
    })

    /**
     * 单、双层iframe隐藏pdf插件
     */
    function double_iframe_hide_pdf() {
        try {
            var $firstIframe = document.getElementsByTagName("iframe")[0];
            if (!isNull($firstIframe) && $firstIframe.id != 'showPdfView') {
                $firstIframe.contentWindow.hide_IWeb2018();
            } else {
                hide_IWeb2018();
            }
        } catch (e) {
            console.warn("隐藏插件错误，或无隐藏的pdf插件")
        }
    }

    /**
     * 单、双层iframe显示pdf插件
     */
    function double_iframe_show_pdf() {
        try {
            var $firstIframe = document.getElementsByTagName("iframe")[0];
            if (!isNull($firstIframe) && $firstIframe.id != 'showPdfView') {
                $firstIframe.contentWindow.show_IWeb2018();
            } else {
                show_IWeb2018();
            }
        } catch (e) {
            console.warn("显示插件错误，或无显示的pdf插件")
        }
    }

    /**
     * 返回到评标流程页面
     */
    function returnUpFlow() {
        doLoading("请稍后",null,null,null,function (){
            window.location.href = "${ctx}/expert/startEval";
        },null)
    }

    /**
     * 返回到清标主页面页面
     */
    function returnClearBidFlow() {
        doLoading("请稍后",null,null,null,function (){
            window.top.location = "${ctx}/clearBid/clearBidIndex";
        },null)
    }
    
    function verifySignature() {
        double_iframe_hide_pdf();
        window.layer.open({
            title: '签章验证',
            area: ['1200px','900px'],
            type: 2,
            content: '${ctx}/viewPdf/verifySignaturePage',
            end: function () {
                double_iframe_show_pdf();
            }
        })
    }
</script>
