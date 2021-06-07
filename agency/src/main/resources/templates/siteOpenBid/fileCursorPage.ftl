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
    <link rel="stylesheet" href="${ctx}/css/fileCursorSite.css">
    <style type="text/css">
        <#if bidSection.bidClassifyCode == 'A11'>
            .tltit li {
                width: 33.3% !important;
            }

            .conts li div {
                width: 33%;
            }
        </#if>
        <#if bidSection.bidClassifyCode == 'A03'
        || bidSection.bidClassifyCode == 'A04'
        || bidSection.bidClassifyCode == 'A05'>
            .tltit li {
                width: 24.9% !important;
            }

            .conts li div {
                width: 24.6%;
            }
        </#if>
        <#if bidSection.bidClassifyCode == 'A08' ||  bidSection.bidClassifyCode == 'A12'>
        .tltit li {
            width: 19.9% !important;
        }

        .conts li div {
            width: 19.6%;
        }
        </#if>
        .box {
            width: 100%;
            height: 100%;
        }

        .bidder-box .center {
            width: 100%;
            height: 100%;
        }

        .center .bidder-name {
            width: 100%;
            min-height: 64px;
            background: rgba(230, 237, 244, 1);
            font-size: 16px;
            font-family: Microsoft YaHei;
            font-weight: bold;
            line-height: 64px;
            text-align: center;
            color: rgba(34, 49, 101, 1);
        }

        .bidder-box .center p {
            width: 100%;
            height: 50px;
            padding: 0 20px;
            font-size: 14px;
            font-family: 'Microsoft YaHei';
            font-weight: bold;
            line-height: 50px;
            float: left;
            box-sizing: border-box;
            color: rgba(34, 49, 101, 1);
        }

        .bidder-box .center p + p {
            border-top: 1px solid rgba(213, 213, 213, 1);
        }

        .bidder-box .center p span {
            float: right;
            font-size: 14px;
            font-family: 'Microsoft YaHei';
            font-weight: bold;
            color: rgba(19, 97, 254, 1);
        }

        .my_li {
            background: pink;
        }

        .checkbox-box {
            width: 90%;
            margin: 20% auto 0;
        }

        .layui-form-item {
            width: 100%;
        }

        .layui-input-block {
            margin: 0;
        }

        .layui-form-checked i,
        .layui-form-checked:hover i {
            color: #1361FE;
        }

        .layui-form-checked span,
        .layui-form-checked:hover span {
            background-color: #1361FE;
        }

        .bidder-info_0 {
            width: 80% !important;
            line-height: 24px;
            display: inline-block;
            vertical-align: middle;
            overflow: visible;
            white-space: pre-wrap;
            padding: 10px;
            box-sizing: border-box;
        }

    </style>
</head>
<body>
<div class="big">
    <h3>招标项目信息</h3>
    <form id="bidder-info" class="index-play layui-form" action="javascript:void(0)">
        <div class="inp bidder" data-title="bidderName"><label>标段名称</label>
            <input type="text" placeholder="请输入" class="bidder-val layui-bg-gray" id="bidder-name" readonly
                   value="${bidSection.bidSectionName}">
        </div>
        <div class="inp bidder" data-title="bidderCode"><label>标段类型</label>
            <input type="text" placeholder="请输入" class="bidder-val layui-bg-gray" readonly
                   value="${bidSection.bidClassifyName}">
        </div>
        <div class="inp bidder" data-title="bidderNo"><label>标段编号</label>
            <input type="text" placeholder="请输入" class="bidder-val layui-bg-gray" readonly
                   value="${bidSection.bidSectionCode}">
        </div>
        <#if bidSection.bidClassifyCode == 'A08' || bidSection.bidClassifyCode == 'A12'>
            <div class="inp bidder" data-title="bidderMaxPrice">
                <label><#if bidSection.bidClassifyCode =='A08'>招标控制价<#else>最高投标限价</#if></label>
                <input type="text" placeholder="请输入" class="layui-bg-gray" readonly
                       value="<#if tenderDoc.controlPrice?? && tenderDoc.controlPrice != ''>${((tenderDoc.controlPrice)?number)?string(",###.00")}元</#if>">
                <input type="hidden" class="bidder-val" readonly
                       value="<#if tenderDoc.controlPrice?? && tenderDoc.controlPrice != ''>${tenderDoc.controlPrice ! 0}</#if>">
            </div>
        </#if>
        <div class="long bidder" data-title="openPlace">开标地点
            <input type="text" placeholder="请输入" class="bidder-val bidOpenPlace" lay-verify="required"
                   lay-reqText="请填写开标地点"
                   value="${tenderDoc.bidOpenPlace}" onblur="validData()">
        </div>
        <button style="visibility: hidden" class="layui-btn" lay-submit="" lay-filter="formSub" id="submitForm">验证表单
        </button>
    </form>
</div>
<div id="audioBox">
    <audio id="voice" autoplay></audio>
</div>
<div>
    <div class="shang" style="line-height: 34px">
        <div class="current-num">当前共有投标人： <span class="bidder_count">0</span>家</div>
        <div class="auto" id="auto_voice" onclick="autoVoice()">
            <span class="left-btn">自动唱标</span>
        </div>
    </div>
</div>
<ul class="tltit">
    <li>投标人名称</li>
    <li>投标报价（元）</li>
    <li>投标保证金</li>
    <#--电梯项目不要投标工期跟工程质量-->
    <#if bidSection.bidClassifyCode != 'A11'>
        <li style="width: 20%">投标工期（日历天）</li>
    </#if>
    <#--  勘察\设计\电梯\监理项目不要工程质量  -->
    <#if bidSection.bidClassifyCode != 'A03'
    && bidSection.bidClassifyCode != 'A04'
    && bidSection.bidClassifyCode != 'A05'
    && bidSection.bidClassifyCode != 'A11'>
        <li>工程质量</li>
    </#if>
</ul>
<ul class="conts" id="bid-open-list">

</ul>

<#--投标人信息模板-->
<div id="show-bidder-voice-info" hidden>
    <div class="bidder-box">
        <div class="center">
            <div class="bidder-name">
                <span id="voice-stop" class="layui-icon" style="padding-left: 20px">
                    <#--暂停图标-->
                    <i class="voice-pause layui-icon layui-icon-pause" style="font-size: 25px;"></i>
                    <#-- 播放图标-->
                    <i class="voice-play layui-icon layui-icon-play" hidden style="font-size: 25px;"></i>
                </span>
                <span class="bidder-info_0"></span>
            </div>
            <p>投标报价:
                <span class="bidder-info_1"></span>
            </p>
            <p>保证金:
                <span class="bidder-info_2"></span>
            </p>
            <#if bidSection.bidClassifyCode != 'A11'>
                <p>投标工期:
                    <span class="bidder-info_3"></span>
                </p>
            </#if>

            <#if bidSection.bidClassifyCode != 'A03'
            && bidSection.bidClassifyCode != 'A04'
            && bidSection.bidClassifyCode != 'A05'
            && bidSection.bidClassifyCode != 'A11'>
                <p>工程质量:
                    <span class="bidder-info_4"></span>
                </p>
            </#if>
        </div>
    </div>
</div>
<#--选择唱标项目-->
<div class="checkbox-box layui-form" id="choose-sign" style="display: none;z-index: 99999;">
    <div class="layui-form-item">
        <div class="layui-input-block">
            <input type="checkbox" lay-filter="checkbox-filter" name="choice" value="bidderName" title="投标人名称" checked>
            <input type="checkbox" lay-filter="checkbox-filter" name="choice" value="bidPrice" title="投标报价" checked>
            <input type="checkbox" lay-filter="checkbox-filter" name="choice" value="marginPayStatus" title="保证金"
                   checked>
            <#if bidSection.bidClassifyCode != 'A11'>
                <input type="checkbox" lay-filter="checkbox-filter" name="choice" value="timeLimit" title="投标工期"
                       checked>
            </#if>
            <#if bidSection.bidClassifyCode != 'A03'
            && bidSection.bidClassifyCode != 'A04'
            && bidSection.bidClassifyCode != 'A05'
            && bidSection.bidClassifyCode != 'A11'>
                <input type="checkbox" lay-filter="checkbox-filter" name="choice" value="quality" title="工程质量" checked>
            </#if>
        </div>
    </div>
</div>

<#--第一步：编写模版。你可以使用一个script标签存放模板，如：-->
<script id="biddingList" type="text/html">
    {{#  layui.each(d, function(index, bidder){}}
    {{# if(isNull(bidder.bidderOpenInfo.bidPriceType) || bidder.bidderOpenInfo.bidPriceType == '总价'){ }}
    {{# var bidPrice = formatCurrency(parseFloat(bidder.bidderOpenInfo.bidPrice));}}
    {{# }else{ }}
    {{# var bidPrice = bidder.bidderOpenInfo.bidPriceType + bidder.bidderOpenInfo.bidPrice }}
    {{# } }}
    <li id="bidder_{{index}}" class="bidder_{{bidder.id}}" >
        <div class="bidder" data-title="bidderName" data-id="{{bidder.id}}" data-index="{{index}}">
            <img src="${ctx}/img/vioce.png" alt="" onclick="playBidderVoice('{{bidder.id}}','{{index}}')">
            <#--            <span onclick="thisPlayVoice('{{index}}')">👇</span>-->
            <p class="bidder-val" onclick="thisPlayVoice('{{index}}')" title="{{bidder.bidderName}}">
                {{bidder.bidderName}}</p>
        </div>
        <div class="bidder" data-title="bidPrice">
            <input type="hidden" name="type" value="{{bidder.bidderOpenInfo.bidPriceType}}">
            <span class="bidder-val">
                {{# if(isNull(bidder.bidderOpenInfo.bidPrice)){ }}
                    -
                {{# }else{ }}
                    {{bidPrice}}
                {{# } }}
            </span>
        </div>
        <div class="bidder" data-title="marginPayStatus">
            <span class="bidder-val">
                {{bidder.bidderOpenInfo.marginPay}}
            </span>
        </div>
        {{# if('${bidSection.bidClassifyCode}' != 'A11'){ }}
            <div class="bidder" data-title="timeLimit">
                <span class="bidder-val">
                    {{# if(isNull(bidder.bidderOpenInfo.timeLimit)){ }}
                        -
                    {{# }else{ }}
                        {{bidder.bidderOpenInfo.timeLimit}}
                    {{# } }}
                </span>
            </div>
        {{# } }}
        {{# if('${bidSection.bidClassifyCode}' != 'A03'
        && '${bidSection.bidClassifyCode}' != 'A04'
        && '${bidSection.bidClassifyCode}' != 'A05'
        && '${bidSection.bidClassifyCode}' != 'A11'){ }}
        <div class="bidder" data-title="quality">
                <span class="bidder-val">
                {{# if(isNull(bidder.bidderOpenInfo.quality)){ }}
                    -
                {{# }else{ }}
                    {{bidder.bidderOpenInfo.quality}}
                {{# } }}
                </span>
        </div>
        {{# } }}
        <div class="bidder" data-title="bidPriceType" style="display: none">
                <span class="bidder-val">
                    {{bidder.bidderOpenInfo.bidPriceType}}
                </span>
        </div>
    </li>
    {{# }); }}
    <li></li>
</script>
<script>

    var form, indexLoad, isValidTrue = true;

    layui.use(['form', 'layer', 'element', 'laytpl', 'laypage'], function () {
        form = layui.form;
        var $ = layui.jquery;
        //监听提交
        form.on('submit(*)', function (data) {
            // 阻止表单提交
            isValidTrue = false;
            return false;
        });
        form.render();
    });

    // 移除多余loading
    $("[id^=layui-layer1000]").remove();

    //layui-layer-shade
    $("[id^=layui-layer-shade1000]").remove();

    // 标识是否为自动唱标
    var isAutoVoice = true;

    // 招标唱标内容
    var tenderDto = {};

    // 所有投标唱标内容
    var listBidder = null;

    // 一个投标人唱标内容
    var bidderDto = {};

    // 指定不唱的字段
    var noVoice = [];

    // 自动唱标，音频+时长 map
    var voiceMap;

    // 标识当前播放的音频索引
    var playIndex = 0;

    // 是否播放
    var isPlayVoice = false;

    // 是否为从指定位置往下唱标
    var isThisNext = false;

    // 当前投标人li的索引
    var this_index = -1;

    // 当前标段语音是否生成
    var thiVoiceExist = false;

    // 唱标弹窗id
    var LAYUI_OPEN_INDEX = 0;

    // 当前语音时长
    var voice_time = 0;

    // 暂停计时器
    var stop_interval;

    // 当前开启的计时器
    var run_setTimeout = null;

    // 监听ie是否在播放
    var ie_play = true;

    // 唱标服务是否正常开启
    var voice_plug = false;

    // 检查唱标驱动是否开启
    checkVoicePlug();

    showProject();

    /**
     * 检测唱标服务是否启动
     */
    function checkVoicePlug() {
        // 等页面load加载完成后执行
        doLoading();
        $.ajax({
            url: '${ctx}/staff/checkVoicePlug',
            type: 'post',
            cache: false,
            async: false,
            success: function (data) {
                loadComplete();
                if (data) {
                    voice_plug = data;
                    // 关闭load
                } else {
                    doLoading("唱标服务没有正常启动!", 0, 1);
                }
            },
            error: function (data) {
                loadComplete();
            },
        });
    }

    /**
     * 展示投标人开标信息
     */
    function showProject() {
        $.ajax({
            url: '${ctx}/siteOpenBid/listBiddersForSing',
            type: 'post',
            cache: false,
            async: true,
            data: {
                bidSectionId: '${bidSection.id}'
            },
            success: function (data) {
                if (!isNull(data)) {
                    //第二步：填充数据
                    var data = data;
                    //第三步：渲染模版
                    var getTpl = biddingList.innerHTML
                        , view = document.getElementById('bid-open-list');
                    layui.laytpl(getTpl).render(data, function (html) {
                        view.innerHTML = html;
                    });
                    // 显示投标人个数
                    $(".bidder_count").text(data.length);

                    listBidder = data;
                }
            },
            error: function (data) {
                console.error(data);
            },
        });

        // 判断当前浏览器 == > 添加不同的暂停事件
        if (getIEVersion() !== -1 && getIEVersion() > 8) {
            // IE内核浏览器
            $("#voice-stop").attr("onclick", "ie_stop_audtio();");
        } else {
            //非IE内核浏览器
            $("#voice-stop").attr("onclick", "chrome_stop_audtio();");
        }
    }

    /**
     * 自动唱标
     */
    function autoVoice() {
        // 唱标服务，没开直接返回
        if (!voice_plug) {
            doLoading("唱标服务没有开启，无法合成语音!", 0, 1);
            return;
        }
        // 防止重复点击，自动唱标
        if(isPlayVoice){
            doLoading("当前正在唱标，请稍后再试!", 0, 2);
            return;
        }
        // 开标地点
        if ($(".bidOpenPlace").val().length == 0) {
            $("#submitForm").trigger('click');
            return;
        }
        if (!thiVoiceExist) {
            userChooseVoiceType(1);
        }

        // 唱标索引归零
        playIndex = 0;
        // 拼接招标唱标内容
        getTenderInfo();
        if (!thiVoiceExist) {
            // 自定义唱标的列
            listenCheckbox();
            //自定列
            doLoading();
            var indexOpen = layer.open({
                type: 1,
                offset: 'c',
                title: ['唱标设置', 'height:57px;text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);line-height:57px;padding:0;'],
                content: $('#choose-sign'),
                area: ['700px', '400px'],
                btn: ['确认', '取消'],
                btnAlign: 'c',
                shade: 0.01,
                btn1: function () {
                    layerConfirm("确定唱以上的选项吗？", function () {
                        doLoading("设置成功！", 0, 2);
                        layer.close(indexOpen);
                        setTimeout(function () {
                            loadComplete();
                            getVoice();
                        }, 500);
                    })
                },
                btn2: function () {
                    loadComplete();
                }
            });
        } else {
            getVoice();
        }

    }

    /**
     * 从当前位置往下唱标
     */
    function thisPlayVoice(index) {
        // 防止重复点击
        if (isPlayVoice) {
            doLoading("当前正在唱标，请稍后再试!", 0, 2);
            return;
        }
        // 语音未合成时，允许用户自定义唱标的列
        userChooseVoiceType(-1);
        // 首次需要先点，自动唱标
        if (thiVoiceExist) {
            // 拼接招标唱标内容
            getTenderInfo();
            // 从指定投标人位置往下唱标
            if (index !== undefined && index !== "") {
                playIndex = Number(index) + Number(1);
                // 从当前位置往下唱标
                isThisNext = true;
            }
            getVoice();
        } else {
            layer.tips('请先点击自动唱标', '#auto_voice', {
                tips: [1, 'rgb(19, 97, 254)']
            });
        }

    }

    /**
     * 用户自定义唱标的列
     */
    function userChooseVoiceType(this_bidder) {
        // 当前标段否生成过语音，未生成允许用户自定义唱标的列
        doLoading();
        $.ajax({
            url: '${ctx}/staff/thisBidVoiceExist',
            type: 'post',
            cache: false,
            async: false,
            data: {
                bidderId: this_bidder
            },
            success: function (data) {
                loadComplete();
                if (!isNull(data)) {
                    thiVoiceExist = data;
                }
            },
            error: function (data) {
                loadComplete();
                console.error(data);
            },
        });
    }

    /**
     * 获取自动唱标语音
     */
    function getVoice() {
        // 获取唱标语音
        window.top.layer.msg('语音合成中，请稍等...', {
            icon: 16,
            shade: [0.3, '#393D49'],
            time: 0
        });
        // 过滤不唱标的字段
        if (noVoice.length > 0) {
            for (var i = 0; i < listBidder.length; i++) {
                noVoice.forEach(function (key) {
                    if (listBidder[i].hasOwnProperty(key)) {
                        // 需要过滤的字段设置为 "-1"
                        listBidder[i][key] = -1;
                    }
                })
            }
        }
        // 获取唱标语音
        doLoading();
        $.ajax({
            url: '${ctx}/staff/tenderFileCursor',
            type: 'post',
            cache: false,
            async: false,
            data: {
                bId: '${bidSection.id}',
                tenderDto: JSON.stringify(tenderDto),
                listBidder: JSON.stringify(listBidder)
            },
            success: function (data) {
                loadComplete();
                if (!isNull(data)) {
                    // 播放音频
                    voiceMap = data.data;
                    if (getIEVersion() !== -1 && getIEVersion() > 8) {
                        // IE内核浏览器
                        IE_PlayVoice();
                    } else {
                        //非IE内核浏览器
                        PlayVoice();
                    }
                }
            },
            error: function (data) {
                loadComplete();
                console.error(data);
            },
        });
    }

    /**
     * 过滤掉，不需要唱标的列
     */
    function listenCheckbox() {
        form.on('checkbox(checkbox-filter)', function (data) {
            // 动态选中项目类型（多选）
            var index = noVoice.indexOf(data.value);
            if (index > -1) {
                noVoice.splice(index, 1)
            } else {
                noVoice.push(data.value);
            }
        });
    }

    /**
     * 唱指定投标人信息
     */
    function playBidderVoice(bidderId, index) {
        // 防止重复点击
        if (isPlayVoice) {
            doLoading("当前正在唱标，请稍后再试!", 0, 2);
            return;
        }
        // 语音未合成时，允许用户自定义唱标的列
        userChooseVoiceType(-1);
        if (thiVoiceExist) {
            // 修改标识
            isAutoVoice = false;
            // 从当前位置往下播放
            isThisNext = true;
            // 投标人主键
            bidderDto["id"] = bidderId;
            // 封装当前投标信息
            $("#bidder_" + index + " .bidder").each(function () {
                var _key = $(this).data("title");
                var _value = $(this).children(".bidder-val").text().trim();
                // 对null值处理，为null时唱 “无”
                if (_value === "-") {
                    _value = -1;
                }
                bidderDto[_key] = _value;
            })
            doLoading();
            $.ajax({
                url: '${ctx}/staff/bidderFileCursor',
                type: 'post',
                cache: false,
                async: false,
                data: {
                    bidSectionId: '${bidSection.id}',
                    bidderDto: JSON.stringify(bidderDto)
                },
                success: function (data) {
                    loadComplete();
                    if (!isNull(data)) {
                        // 播放音频
                        voiceMap = data.data;
                        // 每次点击，在线唱标时（重头开始唱）
                        playIndex = 0;
                        if (getIEVersion() !== -1 && getIEVersion() > 8) {
                            // IE内核浏览器
                            IE_PlayOneVoice(index);
                        } else {
                            //非IE内核浏览器
                            PlayOneVoice(index);
                        }
                    }
                },
                error: function (data) {
                    loadComplete();
                    console.error(data);
                },
            });
        } else {
            layer.tips('请先点击自动唱标', '#auto_voice', {
                tips: [1, 'rgb(19, 97, 254)']
            });
        }

    }

    /**
     * 拼接招标唱标内容
     * @type {string}
     */
    function getTenderInfo() {
        $("#bidder-info .bidder").each(function () {
            var _key = $(this).data("title").trim();
            tenderDto[_key] = $(this).children(".bidder-val").val();
        })
    }

    /**
     * 适用于非IE浏览器
     */
    function PlayVoice() {
        console.log("唱标文件个数："+voiceMap['urls'].length)
        window.top.layer.closeAll();
        if (voiceMap['urls'].length==0){
            window.top.layer.msg("获取文件服务器资源失败",{icon:2})
            return
        }
        // 获取audio
        audio = $("#voice")[0];
        audio.src = voiceMap['urls'][playIndex];
        // 记录时长
        voice_time = voiceMap['duration'][playIndex];
        // 开启自定义计时器
        run_setTimeout = new MySetTimeout(function () {
            // 删除样式
            CANCEL_EFFECT();
            playIndex++;
            if (playIndex < voiceMap['urls'].length) {
                // 递归播放
                PlayVoice();
            } else {
                doLoading("唱标完成！", 1, 2);
                showProject();
            }
            // 正在播放 => 标志位
            isPlayVoice = false;
        }, voice_time);
        audio.play();
        isPlayVoice = true;
        // 不同的播放方式，调用不同渲染方法
        if (isThisNext) {
            renderViewThisPlay(playIndex - 1);
        } else {
            renderViewThisPlay(playIndex);
        }
    }

    /**
     * audio播放单个音频
     */
    function PlayOneVoice(index) {
        if (isPlayVoice) {
            doLoading("当前正在唱标，请稍后再试!", 0, 1);
            return
        }
        // 获取audio
        audio = $("#voice")[0];
        audio.src = voiceMap['urls'];
        audio.addEventListener("ended", function () {
            //监听audio是否加载完毕，如果加载完毕，则读取audio播放时间
            if (!isNull(audio) && !isNull(audio.duration)) {
                // 播放完毕触发
            }
            ONE_PLAY_CANCEL_EFFECT();
            // 正在播放 => 标志位
            isPlayVoice = false;
        }, false);
        audio.play();
        isPlayVoice = true;
        // 渲染当前HTML样式
        renderViewThisPlay(index);
    }

    /**
     *  IE内核浏览器  播放全部
     */
    function IE_PlayVoice() {
        console.log("唱标文件个数："+voiceMap['urls'].length)
        window.top.layer.closeAll();
        if (voiceMap['urls'].length==0){
            window.top.layer.msg("获取文件服务器资源失败",{icon:2})
            return
        }
        // 删除之前的控件
        $("body embed").remove();
        var bid = '${bidSection.id}';
        if ($("body").find("embed").length <= 0) {
            //IE内核浏览器
            var strEmbed = '<embed id="embedPlay" hidden="true" loop="false" volume="100"></embed>';
            $("body").append(strEmbed);
        }
        var embed = document.getElementById("embedPlay");
        embed.src = voiceMap['urls'][playIndex];
        // 播放
        embed.play;
        isPlayVoice = true;
        // 监听音频
        paly_next(voiceMap['duration'][playIndex], playIndex - 1);
        // 不同的播放方式，调用不同渲染方法
        if (isThisNext) {
            renderViewThisPlay(playIndex - 1);
        } else {
            renderViewThisPlay(playIndex);
        }
    }

    /**
     * IE内核浏览器  播放单个音频
     */
    function IE_PlayOneVoice(index) {
        if (isPlayVoice) {
            doLoading("当前正在唱标，请稍后再试!", 0, 1);
            return
        }
        $("body embed").remove();
        // 移除标签
        if ($("body").find("embed").length <= 0) {
            //IE内核浏览器
            var strEmbed = '<embed id="embedPlay" hidden="true" loop="false" volume="100" ></embed>';
            $("body").append(strEmbed);
        }
        var embed = document.getElementById("embedPlay");
        embed.src = voiceMap['urls'];
        isPlayVoice = true;
        // 监听音频
        paly_one_next(voiceMap['duration'], index)
        // 渲染当前HTML样式
        renderViewThisPlay(index);
    }

    /**
     * 模拟：音频播放监听事件
     * timer : 时间
     * this_index 当前的索引
     * 播放下一个音频
     */
    function paly_next(timer, this_index) {
        run_setTimeout = new MySetTimeout(function () {
            // 删除上个li的样式
            CANCEL_EFFECT();
            // 上一个音频播放结束，索引加1
            playIndex++;
            if (playIndex < voiceMap['urls'].length) {
                $("body embed").remove();
                // 递归播放
                IE_PlayVoice();
            } else {
                isPlayVoice = false;
                doLoading("唱标完成！", 1, 2);
                showProject();
            }
        }, timer)
    }

    /**
     * 模拟：音频播放监听事件
     *
     * 播放下一个音频
     */
    function paly_one_next(timer, this_index) {
        run_setTimeout = new MySetTimeout(function () {
            // 删除上个li的样式
            ONE_PLAY_CANCEL_EFFECT();
            // 正在播放 => 标志位
            isPlayVoice = false;
        }, timer)
    }

    /**
     * 单个投标/从指定位置播放，播放音频时显示效果
     * @param index: 投标人li的索引（非主键）
     */
    function renderViewThisPlay(index) {
        if (index >= 0) {
            if (isThisNext) {
                // 从指定位置往下唱标，
                DISPLAY_EFFECT($("#bidder_" + index));
            } else {
                if (index !== 0) {
                    // 自动唱标
                    DISPLAY_EFFECT($("#bidder_" + (index - 1)));
                }
            }
        }
    }

    /**
     * 显示唱标效果
     */
    function DISPLAY_EFFECT(_this) {
        // 获取当前li的小标
        this_index = $(_this).children('.bidder').data("index");
        // 添加背景色
        $(_this).attr("style", "background:pink;");
        // 遍历当前唱标投标人的信息
        $(_this).children('.bidder').each(function (index) {
            var _li = $(this).data('title');
            var _val = $(this).children('.bidder-val').text();
            if (_li === "timeLimit" && _val.trim() !== "-" && _val.trim() !== "0") {
                try {
                    _val = _val.replace(/^\s*|\s*$/g, "").replace("日历天", "").replace("日", "").replace("天", "");
                    // 工期为数字时，拼接日历天
                    if (!isNaN(parseInt(_val, 10))) {
                        _val += "日历天";
                    }
                } catch (e) {
                    console.warn(_val + "不能转换为数字，则原样展示");
                }
            }

            if (_li === "bidPrice") {
                var type = $(this).find("input[name='type']").val();
                if (isNull(type) || "总价" == type) {
                    _val += "元";
                }
            }
            // 给弹窗赋值
            $(".bidder-info_" + index).text(_val);
        })

        // 唱标弹窗
        LAYUI_OPEN_INDEX = layer.open({
            title: false,
            type: 1,
            offset: 'c',
            skin: 'layui-layer-demo', //样式类名
            area: ['350px', '320px'], //宽高
            closeBtn: 1, //显示关闭按钮
            shade: 0.01,
            anim: 1,
            shadeClose: false, //开启遮罩关闭
            content: $("#show-bidder-voice-info").html()
        });
    }

    /**
     * 取消效果
     */
    function CANCEL_EFFECT() {
        var $bidder_index = $("#bidder_" + this_index);
        // 删除样式
        $bidder_index.removeAttr("style");
        // 隐藏上一个li
        $bidder_index.hide('slide');
        layer.close(LAYUI_OPEN_INDEX);
    }

    /**
     * 取消效果
     */
    function ONE_PLAY_CANCEL_EFFECT() {
        var $bidder_index = $("#bidder_" + this_index);
        // 删除样式
        $bidder_index.removeAttr("style");
        layer.close(LAYUI_OPEN_INDEX);
    }


    /**
     * 自定义计时器
     * 作用：支持暂停、播放音频
     */
    function MySetTimeout(callback, delay) {
        var timerId, start, remaining = delay;
        // 暂停
        this.pause = function () {
            window.clearTimeout(timerId);
            remaining -= Date.now() - start;
        };
        // 继续
        this.resume = function () {
            start = Date.now();
            window.clearTimeout(timerId);
            timerId = window.setTimeout(callback, remaining);
        };
        this.resume();
    }

    /**
     * 非IE浏览器的播放、暂停 唱标
     */
    function chrome_stop_audtio() {
        var audio = document.getElementById('voice');
        if (audio !== null) {
            //检测播放是否已暂停.audio.paused 在播放器播放时返回false.
            if (audio.paused) {
                doLoading("开始播放", 1, 2);
                $(".voice-pause").show();
                $(".voice-play").hide();
                audio.play();// 这个就是播放
                run_setTimeout.resume();
            } else {
                form.render();
                doLoading("暂停成功", 1, 2);
                $(".voice-pause").hide();
                $(".voice-play").show();
                audio.pause();// 这个就是暂停
                run_setTimeout.pause();
            }
        }
    }


    /**
     * IE浏览器播放、暂停 唱标
     */
    function ie_stop_audtio() {
        var embed = document.getElementById('embedPlay');
        if (embed !== null) {
            if (ie_play) {
                ie_play = false;
                doLoading("暂停成功", 1, 2);
                $(".voice-pause").hide();
                $(".voice-play").show();
                // 正在唱标 == > 执行：暂停
                embed.pause();
                run_setTimeout.pause();
            } else {
                ie_play = true;
                doLoading("开始播放", 1, 2);
                $(".voice-pause").show();
                $(".voice-play").hide();
                // 未唱标 == > 执行: 播放
                embed.play();
                run_setTimeout.resume();
            }
        }
    }

    /**
     * 修改开标地点
     */
    function validData() {
        // 触发表单验证
        $("#submitForm").trigger("click");
        var open_place = $(".bidOpenPlace").val();
        if (open_place.trim().length <= 0) {
            return
        }
        // 保存开标地点
        doLoading();
        $.ajax({
            url: '${ctx}/staff/updateTenderDoc',
            type: 'post',
            cache: false,
            async: true,
            data: {
                bidSectionId:${bidSection.id},
                bidOpenPlace: open_place
            },
            success: function (data) {
                loadComplete();
                if (!isNull(data)) {
                    if (data) {
                        parent.removeSingingVioce();
                    }
                }
            },
            error: function (data) {
                loadComplete();
                console.error(data);
            },
        });
    }

    /**
     * 从当前位置往下唱标
     * @param index
     */
    function doubleNextPlay(index) {
        if (isPlayVoice) {
            doLoading("当前正在唱标，请稍后再试!", 0, 1);
            return
        }
        layerConfirm("确定要从当前位置往下唱标吗？", function () {
            thisPlayVoice(index);
        })
    }

</script>
</body>
</html>
