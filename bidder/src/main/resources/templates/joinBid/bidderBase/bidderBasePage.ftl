<!DOCTYPE html>
<html lang="zh">
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
    <script src="${ctx}/js/webService.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/plugin/font_2139689_5z9wrsngnn/iconfont.css">
    <link rel="stylesheet" href="${ctx}/css/bidderUtils.css">
    <link rel="stylesheet" href="${ctx}/css/topFlow.css">
    <style>
        .min-position-top {
            top: 380px !important;
            left: calc(100% - 292px) !important;
            position: absolute !important;
        }
    </style>
</head>
<body>
<#include "${ctx}/common/baseHeader.ftl"/>
<section>
    <ol class="flow">
        <li class="upfileLi" data-baseflowname="uploadBidFiles" data-flow="0"
            onclick="loadBidderBasePage('${ctx}/bidderModel/uploadBidFiles', this)">
            <span class="flow-left" style="opacity:0;"></span>
            <div>
                <i class="iconfont icon-wenjian"></i>
            </div>
            <span class="flow-right"></span>
            <p>文件递交</p>
        </li>
        <li class="projectOpenLi" data-baseflowname="projectOpen" data-flow="1"
            onclick="loadBidderBasePage('${ctx}/bidderModel/projectOpenPage', this)">
            <span class="flow-left"></span>
            <div>
                <i class="iconfont icon-xinfeng"></i>
            </div>
            <span class="flow-right"></span>
            <p>项目开标</p>
        </li>
        <li class="projectResumeLi" data-baseflowname="projectResume" data-flow="2"
            onclick="loadBidderBasePage('${ctx}/bidderModel/projectResumePage', this)">
            <span class="flow-left"></span>
            <div>
                <i class="iconfont icon-xinfeng"></i>
            </div>
            <span class="flow-right" style="opacity:0;"></span>
            <p>项目复会</p>
        </li>
    </ol>
    <div id="bidder-base-content">

    </div>
</section>
</body>
<script>
    $(function () {
        // listOnlineProcessComplete();
        listBidderBaseFlow();
        var $top_li = $(".flow li");
        if (localStorage.getItem("base_flow_${bidSection.id}_${bidder.id}") != null) {
            $top_li.eq(localStorage.getItem("base_flow_${bidSection.id}_${bidder.id}")).trigger("click");
        } else {
            $top_li.eq(0).trigger("click");
        }


        setInterval(function () {
            realTimeMessage("${ctx}/messageBox/getNewMessage", "${bidder.id}", function (data) {
                if (isNull(data)) {
                    return;
                }

                if (data.roleType == -1) {
                    if (!isNull(data.message)) {
                        // 其他消息

                        openConfirm(data.message, function () {
                            deleteMessage("${ctx}/messageBox/deleteNewMessage", "${bidder.id}");
                        }, function () {
                            window.location.href = window.location.href;
                        });
                        <#--hide_IWeb2018();-->
                        <#--layer.alert(data.message, {-->
                        <#--    id: 'other1', end: function () {-->
                        <#--        show_IWeb2018();-->
                        <#--    }, success: function () {-->
                        <#--        deleteMessage("${ctx}/messageBox/deleteNewMessage", "${bidder.id}");-->
                        <#--    }, btn1: function (index) {-->
                        <#--        layer.close(index);-->
                        <#--        window.location.href = window.location.href;-->
                        <#--    }-->
                        <#--});-->
                    }
                } else if (data.roleType == 1) {
                    // 解密
                    openConfirm(data.message + ",是否前往？", function () {
                        deleteMessage("${ctx}/messageBox/deleteNewMessage", "${bidder.id}");
                    }, function () {
                        localStorage.setItem("gotoMessages_${bidSection.id}_${bidder.id}", "bidderFileDecryptLi");
                        $(".projectOpenLi").trigger("click");
                    });

                    <#--hide_IWeb2018();-->
                    <#--layer.confirm(data.message + ",是否前往？", {-->
                    <#--    id: "decrypt1",-->
                    <#--    btn: ['前往', '取消'],-->
                    <#--    end: function () {-->
                    <#--        show_IWeb2018();-->
                    <#--    },-->
                    <#--    success: function () {-->
                    <#--        deleteMessage("${ctx}/messageBox/deleteNewMessage", "${bidder.id}");-->
                    <#--    }-->
                    <#--}, function (index) {-->
                    <#--    localStorage.setItem("gotoMessages_${bidSection.id}_${bidder.id}", "bidderFileDecryptLi");-->
                    <#--    $(".projectOpenLi").trigger("click");-->
                    <#--    layer.close(index);-->
                    <#--}, function () {-->
                    <#--    window.location.href = window.location.href;-->
                    <#--});-->
                } else if (data.roleType == 2) {
                    // 质询
                    openConfirm(data.message + ",是否前往？", function () {
                        deleteMessage("${ctx}/messageBox/deleteNewMessage", "${bidder.id}");
                    }, function () {
                        localStorage.setItem("gotoMessages_${bidSection.id}_${bidder.id}", "confirmBidOpenRecordLi");
                        $(".projectOpenLi").trigger("click");
                    });
                    <#--hide_IWeb2018();-->
                    <#--layer.confirm(data.message, {-->
                    <#--    id: "question1",-->
                    <#--    btn: ['前往', '取消'],-->
                    <#--    end: function () {-->
                    <#--        deleteMessage("${ctx}/messageBox/deleteNewMessage", "${bidder.id}");-->
                    <#--        show_IWeb2018();-->
                    <#--    },-->
                    <#--    success: function () {-->
                    <#--        deleteMessage("${ctx}/messageBox/deleteNewMessage", "${bidder.id}");-->
                    <#--    }-->
                    <#--}, function (index) {-->
                    <#--    localStorage.setItem("gotoMessages_${bidSection.id}_${bidder.id}", "confirmBidOpenRecordLi");-->
                    <#--    $(".projectOpenLi").trigger("click");-->

                    <#--    layer.close(index);-->
                    <#--}, function () {-->
                    <#--    window.location.href = window.location.href;-->
                    <#--});-->
                }
            })
        }, 5000);

    });


    /**
     * 内部js执行完成后执行，包含Iframe 内部Js
     * **/
    $(window).on("load", function () {
        // 开标后，显示消息盒子
        if ('${showMessBox}' === '-1') {
            setTimeout(showMinOnlinePage, 2500)
        }
    })

    /**
     * 加载局部div
     * @param targetUrl 目标路由
     * @param e
     */
    function loadBidderBasePage(targetUrl, e) {
        // 添加load
        doLoading();
        $.ajax({
            type: "post",
            url: "${ctx}/bidderModel/validEnterFlow",
            cache: false,
            data: {
                "bidSectionId": "${bidSection.id}",
                "flow": $(e).data("flow")
            },
            success: function (data) {
                loadComplete();
                if (data.code == 1) {
                    setTimeout(function () {
                        var $bidder_base_content = $("#bidder-base-content");
                        $bidder_base_content.hide();
                        $(".flow li").each(function (index) {
                            if ($(this).is($(e))) {
                                var oldBaseFlowIndex = localStorage.getItem("base_flow_${bidSection.id}_${bidder.id}");
                                // 大流程发生变化时，删除小流程进行环节的localStorage
                                if (oldBaseFlowIndex != index) {
                                    localStorage.removeItem("bidder_now_${bidSection.id}_${bidder.id}");
                                }
                                localStorage.setItem("base_flow_${bidSection.id}_${bidder.id}", index);
                                listBidderBaseFlow();
                            }
                        });
                        $bidder_base_content.load(targetUrl, function () {
                            // 添加跨域参数
                            $.ajaxSetup({
                                type: "POST",
                                cache: false,
                                beforeSend: function (xhr) {
                                    xhr.setRequestHeader(csrf_header, csrf_token);
                                }
                            })
                            $bidder_base_content.fadeIn();
                        });
                    }, 200);
                } else {
                    hide_IWeb2018();
                    layer.msg(data.msg, {
                        time: 3000, icon: 2, end: function () {
                            show_IWeb2018();
                        }
                    });
                }
            }
        });
    }

    /**
     * 获取开标大流程进行情况
     */
    function listBidderBaseFlow() {
        $.ajax({
            type: "post",
            url: "${ctx}/bidderModel/listBidderBaseFlow",
            cache: false,
            data: {
                "bidSectionId": "${bidSection.id}"
            },
            success: function (data) {
                updateBidderBaseFlowStep(data);
            }
        });
    }

    /**
     * 更新开标流程左侧导航
     * @param data 完成的流程
     */
    function updateBidderBaseFlowStep(data) {
        var $top_ul = $(".flow li");
        // 初始状态
        $top_ul.removeClass("check");
        for (var i = 0; i < data.length; i++) {
            var $cur_li = $(".flow li[data-baseflowname='" + data[i] + "']");
            $cur_li.addClass("check");
        }

        // 点击顶部流程导航时选择当前
        var $li;
        if (localStorage.getItem("base_flow_${bidSection.id}_${bidder.id}") != null) {
            $li = $top_ul.eq(localStorage.getItem("base_flow_${bidSection.id}_${bidder.id}"));
        } else {
            $li = $top_ul.eq(0);
        }
        $li.addClass("check");
    }

</script>
<script>
    /**
     * 加载局部div
     * @param targetUrl 目标路由
     * @param e
     */
    function goToUrl(targetUrl, e) {
        $('.sele').removeClass('check sele').find("i").removeClass("nowa");
        $(e).addClass("check sele").find("i").addClass("nowa");
        // 添加load
        doLoading();
        setTimeout(function () {
            var $bidder_right = $("#bidder-content-right");
            $bidder_right.hide();
            $(".left ul li").each(function (index) {
                if ($(this).is($(e))) {
                    localStorage.setItem("bidder_now_${bidSection.id}_${bidder.id}", index);
                    listOnlineProcessComplete();
                }
            });
            $bidder_right.load(targetUrl + "?bidSectionId=${bidSection.id}&bidderId=${bidder.id}", function () {
                // 添加跨域参数
                $.ajaxSetup({
                    type: "POST",
                    cache: false,
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(csrf_header, csrf_token);
                    }
                });
                loadComplete();
                $bidder_right.fadeIn();
            });
        }, 200);

        //显示当前进行的流程
        showProcessNow(e);
    }

    //显示当前进行的流程
    function showProcessNow(obj) {
        $(".time").text("当前进行：" + $(obj).text());
    }

    /**
     * 获取开标进行环节的完成情况
     */
    function listOnlineProcessComplete() {
        $.ajax({
            type: "post",
            url: "${ctx}/bidderModel/listOnlineProcessComplete",
            cache: false,
            data: {
                "id": "${bidder.id}",
                "bidSectionId": "${bidSection.id}"
            },
            success: function (data) {
                updateOnlineFlowStep(data);
            }
        });
    }

    /**
     * 更新开标流程左侧导航
     * @param data 完成的流程
     */
    function updateOnlineFlowStep(data) {
        var $left_ul = $(".left ul li");
        // 初始状态
        $left_ul.removeClass("sele");
        $left_ul.find("i").removeClass("nowa");
        for (var i = 0; i < data.length; i++) {
            var $cur_li = $(".left ul li[data-flowname='" + data[i] + "']");
            $cur_li.addClass("sele");
        }

        // 点击左侧流程导航时选择当前
        var $li;
        if (localStorage.getItem("bidder_now_${bidSection.id}_${bidder.id}") != null) {
            $li = $left_ul.eq(localStorage.getItem("bidder_now_${bidSection.id}_${bidder.id}"));
        } else {
            $li = $left_ul.eq(0);
        }
        $li.addClass("sele").find("i").addClass("nowa");
    }

    /**
     * 跳转直播查看页面
     */
    function toLivePage() {
        window.open("${ctx}/bidderModel/toLivePage", "_blank")
    }
</script>
</html>