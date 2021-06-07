<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>甘肃省房建市政电子辅助评标系统</title>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/css/header.css">
    <link rel="stylesheet" href="${ctx}/css/paperBid.css">
    <link rel="stylesheet" href="${ctx}/css/utils.css">
</head>
<body>
<header>
    <div class="text">
        <div class="name"><img class="logo" src="/img/logo_blue.png"/>甘肃省房建市政电子辅助评标系统</div>
        <div class="bao">
            <div class="try">
                <a class="userName" style="color: #fff">${expert.name}</a>
                <i></i>
            </div>
        </div>
    </div>
</header>
    <section>
        <div class="title">
            <p>当前评标：</p>
            <span>${bidSection.bidSectionName}</span>
            <#if bidSection.evalStatus == 1>
                 <span class="green-b overEvalSection">结束评标</span>
            </#if>
        </div>
        <div class="cont">
            <div class="left">
                <h3>招标文件</h3>
                <div class="tenderFile" style="height: 700px">
                    <iframe id="tenderFileIframe" src="${ctx}/paper/showTenderPdf?fileId=${docFileId}" height="100%" width="100%" frameborder="0" style="border: 0;"></iframe>
                </div>
            </div>
            <div class="right">
                <div class="right-top">
                    <ul>
                        <li class="long">
                            <p>投标单位:</p>
                            <span class="bidder_name" title="${bidderOne.bidderName!""}">${bidderOne.bidderName!""}</span>
                        </li>
                        <#if bidSection.bidClassifyCode != "A10">
                            <li class="long">
                                <p>投标报价:</p>
                                <#if !bidderOne.bidderOpenInfo.bidPriceType?? || bidderOne.bidderOpenInfo.bidPriceType == "总价">
                                    <#if bidderOne.bidderOpenInfo.bidPrice?? && bidderOne.bidderOpenInfo.bidPrice !=''>
                                        <span class="bidder_price">${bidderOne.bidderOpenInfo.bidPrice?number?string(',###.##')}元</span>
                                    </#if>
                                <#else>
                                    <span class="bidder_price"> ${bidderOne.bidderOpenInfo.bidPriceType}${bidderOne.bidderOpenInfo.bidPrice}</span>
                                </#if>
                            </li>
                            <li>
                                <p>投标保证金:</p>
                                <span class="bidder_margin">
                                <#if bidderOne.bidderOpenInfo.marginPayStatus == '1'>
                                    已缴纳
                                <#elseif bidderOne.bidderOpenInfo.marginPayStatus == '2'>
                                    保函
                                <#else>
                                    未缴纳
                                </#if>
                            </span>
                            </li>
                            <li>
                                <p>投标工期:</p>
                                <span class="bidder_time">${bidderOne.timeLimit!"0"}日历天</span>
                            </li>
                        </#if>
                    </ul>
                    <div class="seach">
                        <span class="bidderNameSpan">${bidderOne.bidderName!"请选择投标单位"}</span>
                        <img src="/img/select1.png" alt="">
                        <ol>
                            <#list bidders as bidder>
                                <#assign currentBidderPrice=""/>
                                <#if !bidder.bidderOpenInfo.bidPriceType?? || bidder.bidderOpenInfo.bidPriceType == "总价">
                                    <#if bidder.bidderOpenInfo.bidPrice?? && bidder.bidderOpenInfo.bidPrice !=''>
                                        <#assign currentBidderPrice="${bidder.bidderOpenInfo.bidPrice?number?string(',###.##')}"/>
                                    </#if>
                                <#else>
                                    <#assign currentBidderPrice="${bidder.bidderOpenInfo.bidPriceType}${bidder.bidderOpenInfo.bidPrice}"/>
                                </#if>
                                <li data-bidderid="${bidder.id}" data-bidderprice="${currentBidderPrice}" data-biddername="${bidder.bidderName}"
                                    data-biddertime="${bidder.bidderOpenInfo.timeLimit}" data-biddermargin="${bidder.bidderOpenInfo.marginPayStatus}" data-bidderfileid="${bidder.bidDocId}"
                                    onclick="selectBidder(this)">${bidder.bidderName}</li>
                            </#list>
                        </ol>
                    </div>
                </div>
                <div class="bidderFile" style="height: 700px;">
                    <iframe id="bidderFileIframe" src="${ctx}/paper/showBidderPdf?fileId=${bidderOne.bidDocId}" height="100%" width="100%" frameborder="0" style="border: 0px"></iframe>
                </div>
            </div>
        </div>
    </section>
</body>
<script>
    $('.seach').on('click', 'span', function () {
        if ($('.seach').children('ol').hasClass('select')) {
            $("#bidderFileIframe")[0].contentWindow.show_IWeb2018();
            $('.seach').children('ol').removeClass('select')
        } else {
            $("#bidderFileIframe")[0].contentWindow.hide_IWeb2018()
            $('.seach').children('ol').addClass('select')
        }
    })

    setTimeout(function () {
        $(".overEvalSection").on('click',overEvalSection);
        $(".try").on('click',"i",exitSystemPaper);
    },1000);


    function selectBidder(e) {
        $('.seach').children('ol').removeClass('select')
        var bidder_file_id = $(e).data("bidderfileid");

        $(".bidder_name").text(bidder_name);
        var bidder_name = $(e).data("biddername");
        if ("${bidSection.bidClassifyCode}" != "A10") {
            var bidder_price = $(e).data("bidderprice");
            var bidder_time = $(e).data("biddertime");
            var bidder_margin = $(e).data("biddermargin");
            $(".bidder_price").text(bidder_price);
            $(".bidder_time").text(bidder_time + "日历天");
            $(".bidderNameSpan").text(bidder_name);
            if (bidder_margin == '1'){
                bidder_margin = "已缴纳"
            }else if (bidder_margin == '2'){
                bidder_margin = "保函"
            }else {
                bidder_margin = "未缴纳"
            }
            $(".bidder_margin").text(bidder_margin);
        }

        $(".bidderFile").css("display","block");
        // 处理页面F5刷新相关问题
        var iframe = document.getElementById("bidderFileIframe");
        // 把缓存给iframe
        iframe.src = "${ctx}/paper/showBidderPdf?fileId=" + bidder_file_id;
    }

    /**
     * 结束评标
     */
    function overEvalSection() {
        $("#bidderFileIframe")[0].contentWindow.hide_IWeb2018();
        $("#tenderFileIframe")[0].contentWindow.hide_IWeb2018();
        layer.confirm("是否确定结束评标?", {
                icon: 3,
                title: '操作确认提示'
            },
            function (index) {
                $.ajax({
                    url: "${ctx}/paper/endEval",
                    type: "POST",
                    cache: false,
                    success: function (data) {
                        layerMsg(data.code,data.msg,2,function () {
                            window.location.reload();
                        })
                    },
                    error:function (e) {
                        console.error(e);
                    }
                });
            }, function (index) {
                $("#bidderFileIframe")[0].contentWindow.show_IWeb2018();
                $("#tenderFileIframe")[0].contentWindow.show_IWeb2018();
                layer.close(index);
            })
    }

    /**
     * 退出登录
     */
    function exitSystemPaper() {
        $("#bidderFileIframe")[0].contentWindow.hide_IWeb2018();
        $("#tenderFileIframe")[0].contentWindow.hide_IWeb2018();
        layer.confirm("确认要退出系统？", {
            icon: 3,
            end: function() {
                $("#bidderFileIframe")[0].contentWindow.show_IWeb2018();
                $("#tenderFileIframe")[0].contentWindow.show_IWeb2018();
            }
        }, function (index) {
            layer.close(index);
            $.ajax({
                url: '/logout',
                type: 'post',
                cache: false,
                async: false,
                success: function () {
                    window.location.href = "/login.html";
                }
            });
        }, function (index) {
            // 取消的回调函数
            layer.close(index);
        });
    }

    function layerMsg(icon,msg,time,endFunc) {
        if (isNull(icon)){
            icon = 0;
        }
        if (isNull(msg)){
            msg = "加载中...";
        }
        if (isNull(time)){
            time = 2;
        }

        $("#bidderFileIframe")[0].contentWindow.hide_IWeb2018();
        $("#tenderFileIframe")[0].contentWindow.hide_IWeb2018();
        layer.msg(msg,{icon:icon ,time: time * 1000, end:function () {
                if (endFunc && typeof (endFunc) === "function") {
                    endFunc();
                }
                $("#bidderFileIframe")[0].contentWindow.show_IWeb2018();
                $("#tenderFileIframe")[0].contentWindow.show_IWeb2018();
            }});
    }
</script>
</html>