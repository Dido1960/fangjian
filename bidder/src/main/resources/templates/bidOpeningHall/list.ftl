<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>今日开标项目</title>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <#--<link rel="stylesheet" href="${ctx}/css/visitorList.css">-->
    <link rel="stylesheet" href="${ctx}/css/visitorList2.css">

    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script src="${ctx}/js/common.js"></script>
    <#--<script src="${ctx}/js/three.min.js"></script>-->
    <script src="${ctx}/js/jquery.ripples.js"></script>
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <style type="text/css">
        .layui-layer-content {
            color: #0b1a41 !important;
        }

    </style>
</head>

<body>
<header>
    <h3>甘肃省公共资源交易不见面开标大厅</h3>
    <p class="current-time"></p>
    <span onclick="window.location.href='/login.html'">请登录</span>
</header>

<section>
    <div class="section-left">
        <div class="section-left_title">开标项目</div>
        <div class="section-left_box">
            <div class="box-left-img">
                <img
                        src="/img/visitor/left-icon.png"
                        alt=""
                        id="placeholderImg"
                        <#if bidSectionOne.bidOpenStatus != 1>
                            style="display: block"
                        <#else>
                            style="display: none"
                        </#if>
                />
                <div class="box-live">
                    <div class="box-live-fail" id="playerContainer" tabindex="0"
                            <#if bidSectionOne.bidOpenStatus == 1>
                                style="display: block"
                            <#else>
                                style="display: none"
                            </#if>
                    >
                    </div>
                </div>
            </div>
            <div class="box-left-item" onclick="saveBidLoingitude(${bidSectionOne.id})">
                <input type="hidden" class="bidSection-id" value="${bidSectionOne.id}">
                <div class="box-left-item_box">
                    <div class="box-label">项目名称</div>
                    <div class="box-value line-tow box-projectName">
                        ${bidSectionOne.bidSectionName}
                    </div>
                </div>
                <div class="box-left-item_box">
                    <div class="box-label">场地安排</div>
                    <div class="box-value line-tow box-bidOpenPlace">
                        ${bidSectionOne.bidOpenPlace}
                    </div>
                </div>
                <div class="box-left-item_box">
                    <div class="box-label">开标状态</div>
                    <div class="box-value line-tow box-bidOpenStatusName">${bidSectionOne.bidOpenStatusName}</div>
                </div>
                <div class="box-left-item_box">
                    <div class="box-label">开标时间</div>
                    <div class="box-value line-tow box-bidOpenTime">
                        ${bidSectionOne.bidOpenTime}
                    </div>
                </div>
                <div class="box-left-item_box">
                    <div class="box-label">代理机构</div>
                    <div class="box-value line-tow box-tenderAgencyName">
                        ${bidSectionOne.tenderAgencyName}
                    </div>
                </div>
                <div class="box-left-item_box">
                    <div class="box-label">联系电话</div>
                    <div class="box-value line-tow box-tenderAgencyPhone">${bidSectionOne.tenderAgencyPhone}</div>
                </div>
            </div>
        </div>
    </div>
    <div class="section-right">
        <iframe style="height: 100%;width: 100%;border: 0" src="${ctx}/visitor/listRightPage"></iframe>
    </div>
</section>

</body>

<#--<script src="https://www.jq22.com/jquery/jquery-1.10.2.js"></script>-->
<script src="https://www.jq22.com/jquery/three.min.js"></script>
<script src="${ctx}/js/visitorList.js"></script>
<script>
    var SEPARATION = 100,
        AMOUNTX = 60,
        AMOUNTY = 40;
    var container;
    var camera, scene, renderer;
    var particles,
        particle,
        count = 0;
    var mouseX = 0,
        mouseY = 0;
    var windowHalfX = window.innerWidth / 2;
    var windowHalfY = window.innerHeight / 2;

    $(function () {
        init(); //初始化
        animate(); //动画效果
        // change();	//验证码
    });

    //初始化
    function init() {
        container = document.createElement("div"); //创建容器
        document.body.appendChild(container); //将容器添加到页面上
        camera = new THREE.PerspectiveCamera(
            120,
            window.innerWidth / window.innerHeight,
            1,
            1500
        ); //创建透视相机设置相机角度大小等
        camera.position.set(0, 450, 2000); //设置相机位置

        scene = new THREE.Scene(); //创建场景
        particles = new Array();

        var PI2 = Math.PI * 2;
        //设置粒子的大小，颜色位置等
        var material = new THREE.ParticleCanvasMaterial({
            color: 0x0f96ff,
            vertexColors: true,
            size: 4,
            program: function (context) {
                context.beginPath();
                context.arc(0, 0, 0.01, 0, PI2, true); //画一个圆形。此处可修改大小。
                context.fill();
            },
        });
        //设置长条粒子的大小颜色长度等
        var materialY = new THREE.ParticleCanvasMaterial({
            color: 0xffffff,
            vertexColors: true,
            size: 1,
            program: function (context) {
                context.beginPath();
                //绘制渐变色的矩形
                var lGrd = context.createLinearGradient(
                    -0.008,
                    0.25,
                    0.016,
                    -0.25
                );
                lGrd.addColorStop(0, "#16eff7");
                lGrd.addColorStop(1, "#0090ff");
                context.fillStyle = lGrd;
                context.fillRect(-0.008, 0.25, 0.016, -0.25); //注意此处的坐标大小
                //绘制底部和顶部圆圈
                context.fillStyle = "#0090ff";
                context.arc(0, 0, 0.008, 0, PI2, true); //绘制底部圆圈
                context.fillStyle = "#16eff7";
                context.arc(0, 0.25, 0.008, 0, PI2, true); //绘制顶部圆圈
                context.fill();
                context.closePath();
                //绘制顶部渐变色光圈
                var rGrd = context.createRadialGradient(
                    0,
                    0.25,
                    0,
                    0,
                    0.25,
                    0.025
                );
                rGrd.addColorStop(0, "transparent");
                rGrd.addColorStop(1, "#16eff7");
                context.fillStyle = rGrd;
                context.arc(0, 0.25, 0.025, 0, PI2, true); //绘制一个圆圈
                context.fill();
            },
        });

        //循环判断创建随机数选择创建粒子或者粒子条
        var i = 0;
        for (var ix = 0; ix < AMOUNTX; ix++) {
            for (var iy = 0; iy < AMOUNTY; iy++) {
                var num = Math.random() - 0.1;
                if (num > 0) {
                    particle = particles[i++] = new THREE.Particle(
                        material
                    );
                } else {
                    particle = particles[i++] = new THREE.Particle(
                        materialY
                    );
                }
                //particle = particles[ i ++ ] = new THREE.Particle( material );
                particle.position.x =
                    ix * SEPARATION - (AMOUNTX * SEPARATION) / 2;
                particle.position.z =
                    iy * SEPARATION - (AMOUNTY * SEPARATION) / 2;
                scene.add(particle);
            }
        }

        renderer = new THREE.CanvasRenderer();
        renderer.setSize(window.innerWidth, window.innerHeight);
        container.appendChild(renderer.domElement);
        //document.addEventListener( 'mousemove', onDocumentMouseMove, false );
        //document.addEventListener( 'touchstart', onDocumentTouchStart, false );
        //document.addEventListener( 'touchmove', onDocumentTouchMove, false );
        window.addEventListener("resize", onWindowResize, false);
    }

    //浏览器大小改变时重新渲染
    function onWindowResize() {
        windowHalfX = window.innerWidth / 2;
        windowHalfY = window.innerHeight / 2;
        camera.aspect = window.innerWidth / window.innerHeight;
        camera.updateProjectionMatrix();
        renderer.setSize(window.innerWidth, window.innerHeight);
    }

    function animate() {
        requestAnimationFrame(animate);
        render();
    }

    //将相机和场景渲染到页面上
    function render() {
        var i = 0;
        //更新粒子的位置和大小
        for (var ix = 0; ix < AMOUNTX; ix++) {
            for (var iy = 0; iy < AMOUNTY; iy++) {
                particle = particles[i++];
                //更新粒子位置
                particle.position.y =
                    Math.sin((ix + count) * 0.3) * 200 +
                    Math.sin((iy + count) * 0.5) * 5;
                //更新粒子大小
                particle.scale.x = particle.scale.y = particle.scale.z =
                    ((Math.sin((ix + count) * 0.3) + 1) * 4 +
                        (Math.sin((iy + count) * 0.5) + 1) * 4) *
                    50; //正常情况下再放大100倍*1200
            }
        }

        renderer.render(scene, camera);
        count += 0.1;
    }
</script>

<script type="text/javascript" src="${ctx}/plugin/baiduPlayer/cyberplayer-3.4.1/cyberplayer.js"></script>
<script src="${ctx}/plugin/echarts.min.js"></script>
<script type="text/javascript" src="//api.map.baidu.com/api?v=2.0&ak=Ykswepd0YvdNc5zmAWAkVhY0UCTrdZGE"></script>
<script type="application/javascript">
    $(function () {
        showtime();
        setInterval("showtime()", 1000);
        //直播 liveUrlAddress,liveRoom
        var sectionOneId = '${bidSectionOne.id}';
        var sectionOneStatus = '${bidSectionOne.bidOpenStatus}';
        if (!isNull(sectionOneId) && sectionOneStatus == '1'){
            LiveChoice('${bidSectionOne.liveRoom}', 1);
        }
    })

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
                layerWarning("刷新后重试", function () {
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

    function saveBidLoingitude(bidSectionId) {
        if ('${!user??}') {
            layer.alert("<span style='color: #DE8F35'>请进入登录界面,使用【游客登录】</span>", {icon: 0});
            return;
        }
        var nowCity = new BMap.LocalCity();
        var abc = '';
        nowCity.get(function (rs) {
            abc = rs.center.lng + "," + rs.center.lat;
            console.log(abc)
            $.ajax({
                url: '/bidderModel/saveLatiLongitude',
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

    function LiveChoice(liveRoom, type) {
        var explorer = navigator.userAgent;
        var liveHeignt = 402;
        var liveId = 'playerContainer';
        if (type == 2) {
            liveHeignt = 180;
            var divId = $("li[data-liveRoom=" + liveRoom + "]").children("div[class='live']").attr("id");
            liveId = divId;
        }
        if (explorer.indexOf("Chrome") >= 0) {
            LiveGoogle(liveRoom, liveHeignt, liveId);
        } else {
            LiveIe(liveRoom, liveHeignt, liveId);
        }
    }

    function LiveGoogle(liveRoom, liveHeignt, liveId) {
        var player = cyberplayer(liveId).setup({
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

    function LiveIe(liveRoom, liveHeignt, liveId) {
        var player = cyberplayer(liveId).setup({
            width: '100%',
            height: liveHeignt,
            isLive: true,
            file: "rtmp://${liveUrlAddress}/" + liveRoom,
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
     * 项目信息切换
     * @param bidSectionId
     * @param projectName
     * @param bidOpenPlace
     * @param bidOpenStatusName
     * @param bidOpenTime
     * @param tenderAgencyName
     * @param tenderAgencyPhone
     * @param liveRoom
     */
    function projectInfoCut(bidSectionId, projectName, bidOpenPlace, bidOpenStatusName, bidOpenTime, tenderAgencyName, tenderAgencyPhone, liveRoom) {
        $(".bidSection-id").val(bidSectionId);
        $(".box-projectName").text(projectName);
        $(".box-bidOpenPlace").text(bidOpenPlace);
        $(".box-bidOpenStatusName").text(bidOpenStatusName);
        $(".box-bidOpenTime").text(bidOpenTime);
        $(".box-tenderAgencyName").text(tenderAgencyName);
        $(".tenderAgencyPhone").text(tenderAgencyPhone);
        if (bidOpenStatusName == '进行中') {
            $(".box-live").css("display", "block");
            $("#placeholderImg").css("display", "none");
            LiveChoice(liveRoom);
        } else {
            $("#placeholderImg").css("display", "block");
            $(".box-live").css("display", "none");
            $(".jwplayer").css("display", "none");
        }
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