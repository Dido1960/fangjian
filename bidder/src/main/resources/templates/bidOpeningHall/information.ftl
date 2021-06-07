<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <title>开标大厅</title>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <link rel="stylesheet" href="${ctx}/css/information.css">
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script src="${ctx}/js/webService.js"></script>
    <script type="text/javascript" src="${ctx}/js/like_num.js"></script>
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script type="text/javascript" src="${ctx}/plugin/baiduPlayer/cyberplayer-3.4.1/cyberplayer.js"></script>
    <script src="${ctx}/plugin/echarts.min.js"></script>
</head>

<body>
<header>
    <h3> 甘肃省公共资源交易不见面开标大厅</h3>
    <p class="current-time"></p>
    <span><a href="/visitor/listPage">返回大厅</a></span>
    <span><a href="/login.html">请登录</a></span>
</header>
<section>
    <div class="left">
        <div class="live">
            <h3>开标大厅直播</h3>
            <div class="live-cont">
                <div id="playerContainer" tabindex="0"></div>
            </div>
            <div class="live-foot">
                <span class="name">代理机构：${bidSection.tenderAgencyName}</span><span class="phone">联系电话：${bidSection.tenderAgencyPhone}</span>
            </div>
        </div>
        <div id="onlineChatInfo" class="talk">
            <iframe id="talk" src="/dist/index.html#/web-chat"></iframe>
        </div>
    </div>
    <div class="right">
        <div class="likes">
            <img src="../img/like.png" alt="" onclick="like('${bidSection.id}')">
            <span onclick="like('${bidSection.id}')" style="float: left">点赞</span>
            <div id="all">
                <div class="amount">
                    <ul>
                        <li>
                            <div class="am_num">
                                <div id="total">
                                    <span id="like">人点赞</span><span class="t_num t_num1"></span>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>

        </div>
        <div class="introduce">
            <div class="information">
                <span>项目名称</span>
                <p title="${bidSection.bidSectionName}">${bidSection.bidSectionName}</p>
            </div>
            <div class="information">
                <span>项目编号</span>
                <p title="${bidSection.bidSectionCode}">${bidSection.bidSectionCode}</p>
            </div>
            <div class="information">
                <span>开标时间</span>
                <p title="${bidSection.bidOpenTime}">${bidSection.bidOpenTime}</p>
            </div>
        </div>
        <div class="cont">
            <ol>
                <li class="sele" id="map">投标人分布</li>
                <li id="identity">身份检查</li>
                <li id="decrypt">文件解密</li>
                <li id="state">投标人状态</li>
            </ol>
            <div class="map">
                <div id="main" style="width: 100%;height:100%;"></div>
            </div>
            <div class="identity" style="display: none;">
            </div>
            <div class="decrypt" style="display: none;">
            </div>
            <div class="state" style="display: none;">
            </div>
        </div>
    </div>
</section>
</body>
<script src="${ctx}/js/common.js"></script>
<script src="${ctx}/plugin/china.js"></script>
<script>
    var haveShow = false;
    // 开标情况定时器
    var bidOpenSituation;
    $(function () {
        setInterval("showtime()", 1000);
        // showOnlinePage();
        //页面加载立即执行
        checkLike('${bidSection.id}');
        //直播
        LiveChoice();

        setTimeout(function () {
            postMessageFun({ themeType: 1 })
        },1000);

        loadMap();

        bidOpenSituation = setInterval(function () {
            //点赞查看
            checkLike('${bidSection.id}');
            //开标情况查看
            checkOpenEnd();
        }, 1000);
    })
    // 切换页面
    $('.cont ol').on('click', 'li', function () {
        $(this).addClass('sele').siblings().removeClass('sele')

        if ($(this).attr('id') == 'map') {
            //观众分布
            // jumpPage('map');
            $('.map').show()
            $('.state').hide()
            $('.decrypt').hide()
            $('.qualifications').hide()
            $('.identity').hide()
            // loadMap();
        } else if ($(this).attr('id') == 'decrypt') {
            //文件解密
            jumpPage('decrypt');
            $('.map').hide()
            $('.state').hide()
            $('.decrypt').show()
            $('.qualifications').hide()
            $('.identity').hide()
        } else if ($(this).attr('id') == 'state') {
            //投标人状态
            jumpPage('state');
            $('.map').hide()
            $('.state').show()
            $('.decrypt').hide()
            $('.qualifications').hide()
            $('.identity').hide()
        } else if ($(this).attr('id') == 'identity') {
            //身份检查
            jumpPage('identity');
            $('.map').hide()
            $('.state').hide()
            $('.decrypt').hide()
            $('.qualifications').hide()
            $('.identity').show()
        }
    })

    function jumpPage(url) {
        var indexLoad = layer.msg("数据加载中...", {icon: 16, time: 0, shade: 0.3});
        $("." + url).load("${ctx}/visitor/" + url
            , function () {
                // 添加跨域参数
                $.ajaxSetup({
                    type: "POST",
                    cache: false,
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(csrf_header, csrf_token);
                    }
                })
                window.top.layer.close(indexLoad);
            });

    }


    // map的表格
    function loadMap() {

        //使用echarts.init()方法初始化一个Echarts实例，在init方法中传入echarts map的容器 dom对象
        var mapChart = echarts.init(document.getElementById('main'));
        // mapChart的配置
        <#--console.log(JSON.parse(${geoCoordMap}));-->
        var geoCoordMap='';
            <#if geoCoordMap!='""'>
        geoCoordMap = JSON.parse(${geoCoordMap});
        </#if>

        var min = ${min};
        <#if min == max>
        var max = ${min?number+2};
        <#else >
        var max = ${max};
        </#if>

        var convertData = function (data) {
            var res = [];
            for (var i = 0; i < data.length; i++) {
                var geoCoord = geoCoordMap[data[i].name];
                if (geoCoord) {
                    res.push({
                        name: data[i].name,
                        value: geoCoord.concat(data[i].value)
                    });
                }
            }
            return res;
        };

        option = {
            backgroundColor: 'rgba(0,0,0,0)',
            title: {
                text: '投标人分布',
                subtext: '当前人数：${total}人',
                x: 'center',
                textStyle: {
                    color: '#fff'
                }
            },
            tooltip: {
                trigger: 'item',
                formatter: function (params) {
                    return params.name;
                }
            },

            visualMap: {
                min: min,
                max: max,
                calculable: false,
                inRange: {
                    color: ['#40f100', '#eac736', '#d94e5d']
                },
                textStyle: {
                    color: '#fff'
                }
            },
            geo: {
                label: {
                    show: true,
                        color: '#fff'

                },
                regions: [{
                    name: '甘肃',
                    itemStyle: {
                        areaColor: '#10bfde',
                        color: 'red'
                    }
                }],
                map: 'china',
                itemStyle: {
                    areaColor: '#013064',
                    borderColor: '#1691FF'
                },
                emphasis: {
                    itemStyle: {
                        areaColor: '#2a333d'
                    },
                    label: {
                        show: true
                    }

                },
                center: [103.73, 36.03],
                zoom: 1.2
            },

            series: [
                {
                    center: [115.97, 29.71],
                    zoom: 1.2,
                    roam:true,
                    name: '投标人分布',
                    type: 'effectScatter',
                    coordinateSystem: 'geo',
                    <#if geoCoordData!>
                    data: convertData(${geoCoordData}),
                        </#if>
                    symbol: 'pin',
                    symbolSize: 10,
                    encode: {
                        value: 2
                    },
                    label: {
                        formatter: '{b}',
                        position: 'right',

                        show: false
                    },
                    emphasis: {
                        textStyle: {
                            color: '#fff'
                        }
                    }

                },

            ]
        }
        //设置图表的配置项
        mapChart.setOption(option);
    }

    /**
     * 点赞功能
     * @param bidSectionId
     */
    function like(bidSectionId) {
        $('.likes').find('img').attr('src', '../img/good.png');
        //数字统计
        $.ajax({
            url: '${ctx}/visitor/like',
            type: 'post',
            cache: false,
            data: {'bidSectionId': bidSectionId},
            success: function (data) {
                show_num1(data)
            }, error: function (data) {
                console.warn(data);
            },
        });
    }

    function checkLike(bidSectionId) {
        //数字统计
        $.ajax({
            url: '${ctx}/visitor/checkLike',
            type: 'post',
            cache: false,
            data: {'bidSectionId': bidSectionId},
            success: function (data) {
                show_num1(data)
            }, error: function (data) {
                console.warn(data);
            },
        });
    }

    function show_num1(n) {
        var it = $(".t_num1 i");
        var len = String(n).length;
        for (var i = 0; i < len; i++) {
            if (it.length <= i) {
                $(".t_num1").append("<i></i>");
            }
            var num = String(n).charAt(i);
            var y = -parseInt(num) * 40;
            var obj = $(".t_num1 i").eq(i);
            obj.stop().animate({
                backgroundPosition: '(0 ' + String(y) + 'px)'
            }, 'slow', 'swing', function () {
            });
        }
    }

    function LiveChoice() {
        var explorer = navigator.userAgent;
        if (explorer.indexOf("Chrome") >= 0) {
            LiveGoogle();
        } else {
            LiveIe();
        }
    }
    function LiveGoogle() {
        var player = cyberplayer("playerContainer").setup({
            width: '100%',
            height: 744,
            isLive: true,
            file: "http://${liveUrlAddress}/${bidSection.liveRoom}.flv",
            image: "/img/login-pic.png",
            autostart: true,
            stretching: "uniform",
            volume: 60,
            barLogo: false,
            controls: true,
            ak: "548d3d6cdbeb4007a86e3e3cc81082c7" // 公有云平台注册即可获得accessKey
        });
        setTimeout(function () {
            player.onNoLiveStream(function () {
            });
            player.onLiveStop(function () {
                console.log("onLiveStop");
                if (!haveShow) {
                    layer.alert("当前直播不存在！");
                }
            });
        }, 3000);
    }
    function LiveIe() {
        var player = cyberplayer("playerContainer").setup({
            width: '100%',
            height: 744,
            isLive: true,
            file: "rtmp://${liveUrlAddress}/${bidSection.liveRoom}",
            image: "/img/login-pic.png",
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
        setTimeout(function () {
            player.onNoLiveStream(function () {
            });
            player.onLiveStop(function () {
                console.log("onLiveStop");
                if (!haveShow) {
                    layer.alert("当前直播不存在！");
                }
            });
        }, 3000);
    }

    /**
     * 开标情况查看
     */
    function checkOpenEnd() {
        $.ajax({
            url: '${ctx}/visitor/checkOpenEnd',
            type: 'post',
            cache: false,
            success: function (data) {
                if (data) {
                    window.clearInterval(bidOpenSituation);
                    layer.msg('开标结束', {icon: 1}, function () {
                        window.location.href = '/visitor/listPage'
                    })
                }
            }, error: function (data) {
                console.warn(data);
            },
        });
    }

    function showtime() {
        var nowtime = new Date();
        var year = timeAdd0(nowtime.getFullYear().toString());
        var month = timeAdd0((nowtime.getMonth() + 1).toString());
        var day = timeAdd0(nowtime.getDate().toString());

        var hours = timeAdd0(nowtime.getHours().toString());
        var min = timeAdd0(nowtime.getMinutes().toString());
        var sen = timeAdd0(nowtime.getSeconds().toString());
        var week = '星期' + ['日', '一', '二', '三', '四', '五', '六'][nowtime.getDay()];
        var time = year + "年" + month + "月" + day + "日" + "&emsp;"+ week + "&emsp;" + hours + ":" + min + ":" + sen + " ";
        $(".current-time").html(time);
    }
</script>

</html>