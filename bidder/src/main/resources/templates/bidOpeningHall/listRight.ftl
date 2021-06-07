<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>今日开标项目</title>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script src="${ctx}/js/common.js"></script>
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/visitorListRight.css">
</head>
<body>
<div class="section-right">
    <div class="section-right-nav">
        <div class="check-nav bidStateCut" data-type="1">已开标</div>
        <div class="bidStateCut" data-type="2">未开标</div>
    </div>
    <div class="section-right-list">
        <div class="section-right-list-btn" id="topButton">
            <img src="../img/visitor/prev-icon.png" alt=""/>
        </div>
        <div class="section-right-list-center">
            <ul class="endBidUl">
                <#if startOpens?? && startOpens?size gt 0>
                    <#list startOpens as bidSection>
                        <li class="check-item" data-status="${bidSection.bidOpenStatus}"
                            data-liveRoom="${bidSection.liveRoom}" data-playId="rightPlayerContainer_${bidSection.id}"
                            onclick="projectInfoCut('${bidSection.id}','${bidSection.bidSectionName}','${bidSection.bidOpenPlace}',
                                    '${bidSection.bidOpenStatusName}','${bidSection.bidOpenTime}',
                                    '${bidSection.tenderAgencyName}','${bidSection.tenderAgencyPhone}','${bidSection.liveRoom}')"
                        >
                            <div class="info">
                                <div class="info-box">
                                    <div class="info-label">项目名称</div>
                                    <div class="info-value line-tow">
                                        ${bidSection.bidSectionName}
                                    </div>
                                </div>
                                <div class="info-box">
                                    <div class="info-label">开标时间</div>
                                    <div class="info-value line-tow">
                                        ${bidSection.bidOpenTime}
                                    </div>
                                </div>
                                <div class="info-box">
                                    <div class="info-label">开标状态</div>
                                    <div class="info-value line-tow">
                                        ${bidSection.bidOpenStatusName}
                                    </div>
                                </div>
                                <div class="info-box">
                                    <div class="info-label">联系电话</div>
                                    <div class="info-value line-tow">
                                        ${bidSection.tenderAgencyPhone}
                                    </div>
                                </div>
                            </div>
                            <#--                            <div class="live-none" style="width: 250px;height: 180px;">-->
                            <iframe class="live" style="display: none" id="rightPlayerContainer_${bidSection.id}">
                                <#--                                <div class="live" style="display: none" mouseleave="mose()"-->
                                <#--                                     id="rightPlayerContainer_${bidSection.id}" tabindex="0">-->

                                <#--                                </div>-->
                            </iframe>

                            <#--                            </div>-->
                        </li>
                    </#list>
                </#if>
            </ul>
            <ul class="notBidUl" style="display: none">
                <#if notOpens?? && notOpens?size gt 0>
                    <#list notOpens as bidSection>
                        <li class="check-item" data-status="${bidSection.bidOpenStatus}"
                            data-liveRoom="${bidSection.liveRoom}"
                            onclick="projectInfoCut('${bidSection.id}','${bidSection.bidSectionName}','${bidSection.bidOpenPlace}',
                                    '${bidSection.bidOpenStatusName}','${bidSection.bidOpenTime}',
                                    '${bidSection.tenderAgencyName}','${bidSection.tenderAgencyPhone}','${bidSection.liveRoom}')"
                        >
                            <div class="info">
                                <div class="info-box">
                                    <div class="info-label">项目名称</div>
                                    <div class="info-value line-tow">
                                        ${bidSection.bidSectionName}
                                    </div>
                                </div>
                                <div class="info-box">
                                    <div class="info-label">开标时间</div>
                                    <div class="info-value line-tow">
                                        ${bidSection.bidOpenTime}
                                    </div>
                                </div>
                                <div class="info-box">
                                    <div class="info-label">开标状态</div>
                                    <div class="info-value line-tow">
                                        ${bidSection.bidOpenStatusName}
                                    </div>
                                </div>
                                <div class="info-box">
                                    <div class="info-label">联系电话</div>
                                    <div class="info-value line-tow">
                                        ${bidSection.tenderAgencyPhone}
                                    </div>
                                </div>
                            </div>
                        </li>
                    </#list>
                </#if>
            </ul>
        </div>
        <div class="section-right-list-btn" id="bottomButton">
            <img src="../img/visitor/next-icon.png" alt=""/>
        </div>
    </div>
</div>
<#--<script src="https://www.jq22.com/jquery/jquery-1.10.2.js"></script>-->
<script src="https://www.jq22.com/jquery/three.min.js"></script>
<script src="${ctx}/js/visitorList.js"></script>
</body>

<script type="text/javascript" src="${ctx}/plugin/baiduPlayer/cyberplayer-3.4.1/cyberplayer.js"></script>
<script src="${ctx}/plugin/echarts.min.js"></script>
<script type="text/javascript" src="//api.map.baidu.com/api?v=2.0&ak=Ykswepd0YvdNc5zmAWAkVhY0UCTrdZGE"></script>
<script type="application/javascript">

    /**
     * 进入项目
     */
    function intoInfor(bidSectionId) {
        setbidSection(bidSectionId);
        $.ajax({
            url: '${ctx}/visitor/intoInforPage',
            type: 'post',
            cache: false,
            data: {'bidSectionId': bidSectionId},
            success: function (data) {
                if ("200" === data.code) {
                    if ("0" === data.msg) {
                        layer.msg("<span style='color: #DE8F35'>项目尚未开标</span>", {icon: 0});
                    } else if ("1" === data.msg) {
                        window.location.href = "/visitor/information"
                    } else if ("2" === data.msg) {
                        layer.msg("<span style='color: #DE8F35'>项目开标已结束</span>", {icon: 0});
                    }
                }
            }, error: function (data) {
                console.warn(data);
                layerMsgError("刷新后重试", function () {
                    window.location.reload();
                })
            },
        });
    }

    function setbidSection(bidSectionId) {
        $.ajax({
            url: '${ctx}/visitor/setbidSection',
            type: 'post',
            cache: false,
            async: false,
            data: {'bidSectionId': bidSectionId},
            success: function (data) {

            }, error: function (data) {

            },
        });
    }

    function saveBidLoingitude() {
        var bidSectionId = $(".bidSection-id").val();
        if ('${!user??}' === 'true') {
            layer.msg('请进入登录界面，使用【游客登录】', {icon: 6});
            return false;
        }
        var nowCity = new BMap.LocalCity();
        var abc = '';
        nowCity.get(function (rs) {
            abc = rs.center.lng + "," + rs.center.lat;
            console.log(abc)
            $.ajax({
                url: '${ctx}/bidderModel/saveLatiLongitude',
                type: 'post',
                cache: false,
                //async: false,
                data: {
                    bidSectionId: bidSectionId,
                    latiLongitude: abc
                },
                success: function (data) {
                    intoInfor(bidSectionId);
                },
                error: function (data) {
                    console.log(data);
                },
            });
        })

    }

    //
    // function LiveChoice(liveRoom, type) {
    //     var explorer = navigator.userAgent;
    //     var liveHeignt = 402;
    //     var liveId = 'playerContainer';
    //     if (type == 2) {
    //         liveHeignt = 180;
    //         var divId = $("li[data-liveRoom=" + liveRoom + "]").children("div[class='live']").attr("id");
    //         liveId = divId;
    //     }
    //     if (explorer.indexOf("Chrome") >= 0) {
    //         LiveGoogle(liveRoom, liveHeignt, liveId);
    //     } else {
    //         LiveIe(liveRoom, liveHeignt, liveId);
    //     }
    // }

    <#--function LiveGoogle(liveRoom, liveHeignt, liveId) {-->
    <#--    var player = cyberplayer(liveId).setup({-->
    <#--        width: '100%',-->
    <#--        height: '100%',-->
    <#--        isLive: true,-->
    <#--        file: "http://${liveUrlAddress}/" + liveRoom + ".flv",-->
    <#--        // image: "/img/login-pic.png",-->
    <#--        autostart: true,-->
    <#--        stretching: "uniform",-->
    <#--        volume: 60,-->
    <#--        barLogo: false,-->
    <#--        controls: true,-->
    <#--        ak: "548d3d6cdbeb4007a86e3e3cc81082c7" // 公有云平台注册即可获得accessKey-->
    <#--    });-->
    <#--    setTimeout(function () {-->
    <#--        player.onNoLiveStream(function () {-->
    <#--        });-->
    <#--        player.onLiveStop(function () {-->
    <#--            console.log("onLiveStop");-->
    <#--            if (!haveShow) {-->
    <#--                layer.alert("当前直播不存在！");-->
    <#--            }-->

    <#--        });-->
    <#--    }, 3000);-->
    <#--}-->

    <#--function LiveIe(liveRoom, liveHeignt, liveId) {-->
    <#--    var player = cyberplayer(liveId).setup({-->
    <#--        width: '100%',-->
    <#--        height: liveHeignt,-->
    <#--        isLive: true,-->
    <#--        file: "rtmp://${liveUrlAddress}/" + liveRoom,-->
    <#--        // image: "/img/login-pic.png",-->
    <#--        autostart: true,-->
    <#--        stretching: "uniform",-->
    <#--        volume: 60,-->
    <#--        barLogo: false,-->
    <#--        controls: true,-->
    <#--        rtmp: {-->
    <#--            reconnecttime: 5, // rtmp直播的重连次数-->
    <#--            bufferlength: 1 // 缓冲多少秒之后开始播放 默认1秒-->
    <#--        },-->
    <#--        ak: "548d3d6cdbeb4007a86e3e3cc81082c7" // 公有云平台注册即可获得accessKey-->
    <#--    });-->
    <#--    setTimeout(function () {-->
    <#--        player.onNoLiveStream(function () {-->
    <#--        });-->
    <#--        player.onLiveStop(function () {-->
    <#--            console.log("onLiveStop");-->
    <#--            if (!haveShow) {-->
    <#--                layer.alert("当前直播不存在！");-->
    <#--            }-->

    <#--        });-->
    <#--    }, 3000);-->
    <#--}-->


    function LiveChoiceRight(liveRoom, type, playId) {
        var explorer = navigator.userAgent;
        var liveHeignt = 402;
        var liveId = 'playerContainer';
        if (type == 2) {
            liveHeignt = 180;
            var divId = playId
            // var divId = $("li[data-liveRoom=" + liveRoom + "]").children("div[class='live']").attr("id");
            // var divId = $("li[data-liveRoom=" + liveRoom + "]").children("div[class='live-none']").children("div[class='live']").attr("id");
            liveId = divId;
        }
        if (explorer.indexOf("Chrome") >= 0) {
            LiveGoogleRight(liveRoom, liveHeignt, liveId);
        } else {
            LiveIe(liveRoom, liveHeignt, liveId);
        }
    }
    //当前直播对象
    var playerRight;

    //已加载的直播房间
    var playIds = [];

    function LiveGoogleRight(liveRoom, liveHeignt, liveId) {
        var count_id = 0;
        // console.log(playIds)
        // console.log("-----谷歌")
        if (playIds.length != 0){
            //重复进入的房间直播 不在进行加载
            for (var i = 0; i < playIds.length; i++) {
                // console.log(playIds);
                if (playIds[i].id == liveId){
                    // console.log(playIds[i].id)
                    playIds[i].play();
                    count_id++;
                }
            }
        }
        if (count_id == 0){
            playerRight = cyberplayer(liveId).setup({
                width: '100%',
                height: '100%',
                isLive: true,
                file: "http://${liveUrlAddress}/" + liveRoom + ".flv",
                // image: "/img/login-pic.png",
                autostart: true,
                stretching: "uniform",
                volume: 60,
                barLogo: false,
                controls: true,
                ak: "548d3d6cdbeb4007a86e3e3cc81082c7" // 公有云平台注册即可获得accessKey
            });
            playIds.push(playerRight);
            setTimeout(function () {
                playerRight.onNoLiveStream(function () {
                });
                playerRight.onLiveStop(function () {
                    console.log("onLiveStop");
                    if (!haveShow) {
                        layer.alert("当前直播不存在！");
                    }

                });
            }, 3000);

        }

    }

    function LiveIe(liveRoom,liveHeignt,liveId) {
        var count_id = 0;
        console.log(playIds)
        console.log("-----IE")
        if (playIds.length != 0){
            //重复进入的房间直播 不在进行加载
            for (var i = 0; i < playIds.length; i++) {
                console.log(playIds);
                if (playIds[i].id == liveId){
                    // console.log(playIds[i].id)
                    playIds[i].play();
                    count_id++;
                }
            }
        }
        if (count_id == 0){
            playerRight = cyberplayer(liveId).setup({
                width: '100%',
                height: '100%',
                isLive: true,
                file: "rtmp://${liveUrlAddress}/"+liveRoom,
                // image: "/img/login-pic.png",
                autostart: true,
                stretching: "uniform",
                volume: 60,
                barLogo: false,
                controls: true,
                rtmp: {
                    reconnecttime: 5, // rtmp直播的重连次数
                    bufferlength: 1 // 缓冲多少秒之后开始播放 默认1秒
                },
                ak: "548d3d6cdbeb4007a86e3e3cc81082c7" // 公有云平台注册即可获得accessKey
            });
            if (undefined != playerRight || "" != playerRight){
                playIds.push(playerRight);
            }
            setTimeout(function () {
                playerRight.onNoLiveStream(function () {
                });
                playerRight.onLiveStop(function () {
                    console.log("onLiveStop");
                    if (!haveShow) {
                        layer.alert("当前直播不存在！");
                    }

                });
            }, 3000);
        }
    }

    function closeplay() {
        if (undefined === playerRight || "" === playerRight) return false;
        playerRight.pause();
        // playerRight.stop();
        // playerRight.remove();
        // console.log("guanbi~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
    }


</script>
</html>