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
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script src="${ctx}/js/base64.js"></script>
    <link rel="stylesheet" href="${ctx}/css/baseStaff.css">
    <style>
        .min-position-top {
            top: 184px !important;
            left: calc(100% - 283px) !important;
        }
    </style>
</head>
<body>
<#include "${ctx}/common/baseTitile.ftl"/>
<section>
    <div class="content">
        <div class="tlt">
            <div>
                <span>当前开标：</span>
                <p>${bidSection.bidSectionName}</p>
            </div>
            <span class="current-time">2020年06月20日 星期一15:00:00</span>

            <#--当前标段的主键-->
            <input type="hidden" id="bid-section-id" value="${bidSection.id}">
            <input type="hidden" id="bid-type" value="${bidType}">
        </div>
        <div class="matter">
            <div class="left">
                <ul>
                    <#if bidSection.bidClassifyCode == "A12">
                        <li data-flowname="controlPrice" data-img="left_1"
                            onclick="goToUrl('${ctx}/siteOpenBid/maxBidPricePage', this)">
                            <img src="${ctx}/img/left_1_gray.png" alt="">
                            <p class="left_menu">最高投标限价</p>
                            <i></i>
                        </li>
                    </#if>
                    <#if bidSection.bidClassifyCode == "A08">
                        <li data-flowname="controlPrice" data-img="left_1"
                            onclick="goToUrl('${ctx}/siteOpenBid/addControlPricePage', this)">
                            <img src="${ctx}/img/left_1_gray.png" alt="">
                            <p class="left_menu">招标控制价</p>
                            <i></i>
                        </li>
                        <li data-flowname="floatPoint" data-img="left_2"
                            onclick="goToUrl('${ctx}/siteOpenBid/floatPointPage', this)">
                            <img src="${ctx}/img/left_2_gray.png" alt="">
                            <p class="left_menu">抽取浮动点</p>
                            <i></i>
                        </li>
                    </#if>
                    <li id="fileDecLi" data-flowname="bidderFileDecrypt" data-img="left_5"
                        onclick="goToUrl('${ctx}/siteOpenBid/bidderFileDecryptPage', this)">
                        <img src="${ctx}/img/left_5_gray.png" alt="">
                        <p class="left_menu">文件上传及解密</p>
                        <i></i>
                    </li>
                    <#if bidSection.bidClassifyCode == "A08">
                        <li data-flowname="controlPriceAnalysis" data-img="left_6"
                            onclick="goToUrl('${ctx}/siteOpenBid/controlPriceAnalysisPage', this)">
                            <img src="${ctx}/img/left_6_gray.png" alt="">
                            <p class="left_menu">控制价分析</p>
                            <i></i>
                        </li>
                    </#if>
                    <#if bidSection.bidClassifyCode == "A12">
                        <li data-flowname="controlPriceAnalysis" data-img="left_6"
                            onclick="goToUrl('${ctx}/siteOpenBid/controlPriceAnalysisPage', this)">
                            <img src="${ctx}/img/left_6_gray.png" alt="">
                            <p class="left_menu">最高投标限价分析</p>
                            <i></i>
                        </li>
                    </#if>
                    <#if bidSection.bidClassifyCode != "A10">
                        <li id="fileCursorLi" data-flowname="fileCursor" data-img="left_7"
                            onclick="goToUrl('${ctx}/siteOpenBid/fileCursorPage', this)">
                            <img src="${ctx}/img/left_7_gray.png" alt="">
                            <p class="left_menu">文件唱标</p>
                            <i></i>
                        </li>
                    </#if>
                    <li data-flowname="bidOpenRecord" data-img="left_8"
                        onclick="goToUrl('${ctx}/staff/bidOpenRecordPage/1', this)">
                        <img src="${ctx}/img/left_8_gray.png" alt="">
                        <p class="left_menu">开标记录表</p>
                        <i></i>
                    </li>
                    <#if bidSection.paperEval?? && bidSection.paperEval == "1">
                        <li data-flowname="bidFileUpload" data-img="left_11"
                            onclick="goToUrl('${ctx}/staff/paperBidFileUploadPage', this)">
                            <img src="${ctx}/img/left_11_gray.png" alt="">
                            <p class="left_menu">数据上传评标</p>
                            <i></i>
                        </li>
                    <#else >
                        <li data-flowname="bidFileUpload" data-img="left_11"
                            onclick="goToUrl('${ctx}/staff/bidFileUploadPage', this)">
                            <img src="${ctx}/img/left_11_gray.png" alt="">
                            <p class="left_menu">数据上传评标</p>
                            <i></i>
                        </li>
                    </#if>
                    <li data-flowname="bidOpenEnd" data-img="left_9"
                        onclick="goToUrl('${ctx}/siteOpenBid/bidOpenEndPage', this)">
                        <img src="${ctx}/img/left_9_gray.png" alt="">
                        <p class="left_menu">开标结束</p>
                        <i></i>
                    </li>
                    <#--                    <li data-flowname="resumeTime" data-img="left_10"-->
                    <#--                        onclick="goToUrl('${ctx}/staff/resumeTimePage', this)">-->
                    <#--                        <img src="${ctx}/img/left_10_gray.png" alt="">-->
                    <#--                        <p class="left_menu">复会时间</p>-->
                    <#--                        <i></i>-->
                    <#--                    </li>-->
                </ul>
            </div>
            <div class="right">
                <div class="right-head">
                    <div class="top"  style="width: 100%">当前环节：<b class="now_flow"></b>
                    </div>
                </div>
                <div class="load_url_content" id="scrollbar_base"
                     style="height: calc(100vh - 270px);">
                    <iframe style="width: 100%;height: 1100px; border: 0px" frameborder="0" id="showPdfView1"></iframe>
                </div>
                <div class="clear" style="clear:both;"></div>
            </div>
        </div>
    </div>
</section>
<script>
    var bidOpenStatus = '${bidSection.bidOpenStatus}';
    var bidOpenEndTime = '${bidSection.bidOpenEndTime}';

    /**
     * 加载局部div
     * @param targetUrl 目标路由
     * @param e
     */
    var flag = false;

    function goToUrl(targetUrl, e) {
        //开标结束判断是否文件上传完成
        if (targetUrl == '${ctx}/siteOpenBid/bidOpenEndPage') {
            isBidderFileUploadAll();
            if (!flag) {
                layerLoadingForExpert("数据上传评标尚未上传完成，无法结束开标！", 0, 2);
                return false;
            }
        }

        // 添加load
        layerLoading("数据加载中...", 16, 1);
        setTimeout(function () {
            $(".left ul li").each(function (index) {
                if ($(this).is($(e))) {
                    localStorage.setItem("now_flow_${bidSection.id}", index);
                    listBidOpenProcessComplete();
                }
            });

            var iframe = $(".load_url_content iframe")[0];
            iframe.src = targetUrl + encodeURI("?id=${bidSection.id}");
            if (iframe.attachEvent) {
                iframe.attachEvent("onload", function () {
                    window.top.layer.closeAll();
                    $(".now_flow").html($(e).children(".left_menu").html())
                });
            } else {
                iframe.onload = function () {
                    window.top.layer.closeAll();
                    $(".now_flow").html($(e).children(".left_menu").html())
                };
            }
        }, 400)
    }

    // 全局加载layui组件，无需在子页面单独引入
    var form, layer, laytpl, laypage, element;
    $(function () {
        listBidOpenProcessComplete();
        setInterval(showtime, 1000);
        layui.use(['form', 'layer', 'element', 'laytpl', 'laypage'], function () {
            form = layui.form;
            element = layui.element;
            laypage = layui.laypage;
            laytpl = layui.laytpl;

            //监听提交
            form.on('submit(formSub)', function (data) {
                updateTenderDoc();
                // 阻止表单提交
                return false;
            });
        });
        var $left_ul_li = $(".left ul li");
        if (bidOpenStatus === "2" || !isNull(bidOpenEndTime)) {
            $left_ul_li.eq($left_ul_li.length - 1).trigger("click");
            // 开标结束，禁用非复会环节的所有页面跳转
            $left_ul_li.each(function (index) {
                if (index !== ($left_ul_li.length - 1)) {
                    $(this).addClass("disable-click").removeAttr("onclick");
                    $(this).css("cursor", "not-allowed");
                }
            });
        } else {
            if (localStorage.getItem("now_flow_${bidSection.id}") != null) {
                $left_ul_li.eq(localStorage.getItem("now_flow_${bidSection.id}")).trigger("click");
            } else {
                $left_ul_li.eq(0).trigger("click");
            }
        }
    })

    function showtime() {
        var nowtime = new Date();
        var year = timeAdd0(nowtime.getFullYear().toString());
        var month = timeAdd0((nowtime.getMonth() + 1).toString());
        var day = timeAdd0(nowtime.getDate().toString());

        var hours = timeAdd0(nowtime.getHours().toString());
        var min = timeAdd0(nowtime.getMinutes().toString());
        var sen = timeAdd0(nowtime.getSeconds().toString());
        var week = '星期' + ['日', '一', '二', '三', '四', '五', '六'][nowtime.getDay()];
        var time = year + "年" + month + "月" + day + "日" + " " + week + " " + hours + ":" + min + ":" + sen;
        $(".current-time").text(time);
    }

    /**
     * 获取开标进行环节的完成情况
     */
    function listBidOpenProcessComplete() {
        $.ajax({
            type: "post",
            url: "${ctx}/siteOpenBid/listBidOpenProcessComplete",
            cache: false,
            async: false,//必须用同步
            data: {
                "id": "${bidSection.id}"
            },
            success: function (data) {
                updateNowFlowStep(data);
            }
        });
    }

    /**
     * 更新开标流程左侧导航
     * @param data 完成的流程
     */
    function updateNowFlowStep(data) {
        var $left_ul = $(".left ul li");
        // 初始状态
        $left_ul.removeClass("blue");
        $left_ul.find("i").removeClass("nowa");
        var flowNameArr = [];
        $left_ul.each(function (index) {
            var img = $(this).data('img');
            $(this).find("img").attr("src", "${ctx}/img/" + img + "_gray.png");
            flowNameArr.push($(this).attr("data-flowname"));
        });

        for (var i = 0; i < flowNameArr.length; i++) {
            var $cur_li = $(".left ul li[data-flowname='" + data[i] + "']");
            var img = $cur_li.data("img");
            $cur_li.addClass("blue complete");
            $cur_li.find("img").attr("src", "${ctx}/img/" + img + ".png");
        }

        // 点击左侧流程导航时选择当前
        var $li;
        if (localStorage.getItem("now_flow_${bidSection.id}") != null) {
            $li = $left_ul.eq(localStorage.getItem("now_flow_${bidSection.id}"));
        } else {
            if (bidOpenStatus === "2") {
                $li = $left_ul.eq($(".left ul li").length - 1);
            } else {
                $li = $left_ul.eq(0);
            }
        }
        var img = $li.data("img");
        $li.addClass("blue");
        $li.find("img").attr("src", "${ctx}/img/" + img + ".png");
        $li.find("i").removeClass("nowa").addClass("nowa");
    }


    /**
     * 点击生成pdf输出到页面
     */
    function outBidOpenRecord() {
        layerLoading("开标记录表生成中，请稍等...");
        $.ajax({
            url: '${ctx}/staff/createRecordTable',
            type: 'post',
            cache: false,
            async: true,
            data: {
                bidSectionId: ${bidSection.id},
                bidOpenPlace: $("#bidOpenPlace").val()
            },
            success: function (data) {
                if (data) {
                    layer.msg('生成成功！', {icon: 1});
                    setTimeout(function () {
                        parent.window.location.reload();
                    }, 1500);
                } else {
                    layer.msg("生成失败！", {icon: 5});
                }
                // window.top.layer.closeAll();
            },
            error: function (data) {
                layer.msg("生成失败！", {icon: 5});
                console.error(data);
                // window.top.layer.closeAll();
            },
        });
    }


    /**
     * 删除唱标语音
     */
    function removeSingingVioce() {
        $.ajax({
            url: '${ctx}/staff/removeSingingVoice',
            type: 'post',
            cache: false,
            data: {
                id: ${bidSection.id}
            },
            success: function (data) {
                if (!isNull(data)) {
                    if (data) {
                        layer.msg("操作成功！", {icon: 1});
                    }
                }
            }
        });
    }

    /**
     * 判断文件是否上传完成
     */
    function isBidderFileUploadAll() {
        $.ajax({
            url: "${ctx}/staff/isBidderFileUploadAll",
            type: "POST",
            cache: false,
            async: false,//必须用同步
            success: function (data) {
                if (data == 1) {
                    flag = true;
                }
            },
            error: function (e) {
                console.error(e);
                return false;
            }
        });
    }
</script>
</body>

</html>