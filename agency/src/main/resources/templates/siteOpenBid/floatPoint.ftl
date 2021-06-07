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
    <link rel="stylesheet" href="${ctx}/css/floatPoint.css">
</head>
<body>

<#if tenderDoc.floatPoint??>
    <div class="down">
        <p class="extraFont">当前浮动点为: ${tenderDoc.floatPoint}%</p>
    </div>
<#else>
    <div class="down">
        <ul class="num">
            <li>0.25%</li>
            <li>0.5%</li>
            <li>0.75%</li>
            <li>1%</li>
            <li>1.25%</li>
            <li>1.5%</li>
        </ul>
        <div class="hide">
            <div class="big-btn" id="float-point">0%</div>
            <div class="big-nothing" style="display: none;">******</div>
            <span></span>
            <div class="save">
                <img src="${ctx}/img/eye.png" alt="">
            </div>
        </div>
        <div class="kong"></div>
    </div>
    <div class="foot" id="div-show-float">
        <div class="lit-btn blove site-demo-button" id="auto-layerDemo">
            <button data-method="confirmTrans" id="auto-extract" class="layui-btn move">自动抽取</button>
        </div>
        <div class="lit-btn organe site-demo-button" id="layerDemo">
            <button data-method="offset" data-type="auto" class="layui-btn layui-btn-normal hard">手动输入</button>
        </div>
        <div class="lit-btn blove site-demo-button" id="submit-float" style="display: none">
            <button class="layui-btn move" onclick="submit_form()">确定</button>
        </div>
    </div>
</#if>
<div class="tan layui-form" style="display: none; z-index: 99999;">
    <form action="">
        <input class="layui-form-danger" id="hand-float-point" type="number" min="0" step="0.01" oninput="validHandFloatPoint(this)" placeholder="请输入浮动点">
        <p>手动设置完成后，将不能再进行修改</p>
    </form>
</div>
<div class="loging" style="display: none;">
    <p>已抽取的浮动点：1.25%</p>
</div>
<script>
    var timer = -1;
    $(function () {
        clearInterval(timer);
        timer = -1
    })
    layui.use('layer', function () { //独立版的layer无需执行这一句
        var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句

        /**
         * 手动输入浮动点
         */
        $('#layerDemo .hard').on('click', function () {
            // 判断当前按钮是否处于禁用状态
            if ($(this).hasClass("layui-btn-disabled")) {
                return
            }
            var othis = $(this), method = othis.data('method');
            var type = othis.data('type')
                , text = othis.text();
            layer.open({
                type: 1,
                offset: type,
                title: ['手动设置', 'height:57px;text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);line-height:57px;padding:0;'],
                id: 'layerDemo' + type,
                content: $('.tan'),
                area: ['600px', '360px'],
                btn: ['确认', '取消'],
                btnAlign: 'c',
                shade: 0.3,
                yes: function () {
                    // 校验数据
                    var hade_float = $("#hand-float-point").val().trim();
                    if (hade_float.length == 0) {
                        $("#hand-float-point").focus();
                        $("#hand-float-point").addClass("layui-form-danger");
                        layer.tips('请输入浮动点', '#hand-float-point', {
                            tips: [1, 'rgba(78, 138, 255, 1)'] //还可配置颜色
                        });
                        return;
                    } else{
                        // 验证用户输入的浮动点格式
                        var reg = "(^[1-9](\\d+)?(\\.\\d{1,2})?$)|(^\\d\\.\\d{1,2}$)";
                        if(!hade_float.match(reg)){
                            $("#hand-float-point").focus();
                            $("#hand-float-point").addClass("layui-form-danger");
                            showTips('#hand-float-point','只能输两位小数哦')
                            return;
                        }
                    }
                    // 拼接一个%
                    hade_float += "%";
                    layer.closeAll();
                    layer.msg("您输入的浮动点为：" + hade_float, {icon: 1});
                    $("#float-point").text(hade_float);
                    // 关闭自动抽取功能
                    $("#auto-layerDemo").hide();
                    // 开启确定按钮
                    $("#submit-float").show();
                    // 禁用手动抽取按
                    $("#layerDemo button").addClass("layui-btn-disabled");
                }
            });
        });
    });

    var hide_status = false;
    $('.hide').on('click', '.save', function () {
        if ($('.big-btn').css('display') == 'none') {
            $('.big-btn').show()
            $('.big-nothing').hide()
            hide_status = false;
        } else {
            $('.big-btn').hide()
            $('.big-nothing').show()
            hide_status = true
        }
    })
    /**
     * 自动抽取浮动点
     */
    $("#auto-extract").click(function () {
        // 判断当前按钮是否处于禁用状态
        if ($(this).hasClass("layui-btn-disabled")) {
            return
        }
        // 隐藏手动抽取
        $("#layerDemo").hide();
        if (timer === -1) {
            $(this).text("停止");
            var arrs = ['0.25', '0.5', '0.75', '1', '1.25', '1.5'];
            timer = setInterval(function () {
                $("#float-point").text(arrs[Math.floor(Math.random() * 5)] + "%");
                var rand = randomNumBoth(1,6);
                var randnum = arrs[rand - 1] + "%";
                var randnum_str = getCoefficientStr(rand);
                $(".big-btn").text(randnum);
                if (hide_status) {
                    $(".big-nothing").text(randnum_str);
                }
            }, 50);
        } else {
            clearInterval(timer);
            $(".loging p").text('已抽取的浮动点：' + $(".big-btn").text());
            var floatIndex = layer.open({
                type: 1,
                title: null,
                content: $(".loging"),
                area: ['400px', '166px']
            });
            setTimeout(function () {
                layer.close(floatIndex);
            }, 2500);

            // 禁用自动抽取按钮
            $("#auto-extract").addClass("layui-btn-disabled");
            // 开启提交按钮
            $("#submit-float").show();
            $('.big-nothing').text("******");
            $('.big-btn').show()
            $('.big-nothing').hide()

        }
    })

    /**
     * 修改招标文件
     */
    function submit_form() {
        doLoading();
        $.ajax({
            url: '${ctx}/staff/updateTenderDoc',
            type: 'post',
            cache: false,
            async: false,
            data: {
                bidSectionId: '${bidSection.id}',
                floatPoint: $(".big-btn").text().replace("%","")
            },
            success: function (data) {
                loadComplete();
                if (data) {
                    // 成功
                    layer.msg("浮动点设置成功！", {icon: 1})
                    $("#div-show-float").hide();
                }
                parent.window.location.reload();
            },
            error: function (data) {
                loadComplete();
                console.error(data);
            },
        });
    }

    /**
     * 产生随机数 min <= random <= max
     *
     * @param min
     * @param max
     * @returns {*}
     * @constructor
     */
    function randomNumBoth(min , max){
        var Range = max - min;
        var Rand = Math.random();
        return min + Math.round(Rand * Range); //四舍五入;
    }

    function getCoefficientStr(rand) {
        var str = "";
        for (var i = 0; i < rand; i++) {
            str += "*";
        }
        return str;
    }

    /**
     * 验证用户输入的浮动点
     * 1、非负数
     * 2、小数点后保留两位
     * @param value
     */
    function validHandFloatPoint(e){
        // 用户输入的值
        var value = $(e).val();
        if (value < 0){
            showTips('#hand-float-point','不允许为负数哦')
            $(e).val("");
        }
    }

    /**
     * 展示tips
     * @param id
     * @param msg
     */
    function showTips(id,msg){
        layer.tips(msg, id, {
            tips: [1, 'rgba(78, 138, 255, 1)'] //还可配置颜色
        });
    }
</script>

</body>