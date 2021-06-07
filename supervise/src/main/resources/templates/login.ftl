<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>主管部门</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-3.1.0.min.js"></script>
    <!--[if lt IE 9]>
    <script src="${ctx}/js/html5shiv.min.js"></script>
    <script src="${ctx}/js/respond.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script src="${ctx}/js/common.js"></script>
    <#--BJCA--START-->
    <script src="${ctx}/js/base64.js"></script>
    <script src="/js/XTXSAB.js"></script>
    <script src="/js/baseCertInfo.js"></script>
    <#--BJCA --END-->
    <script src="${ctx}/js/login.js"></script>
    <link rel="stylesheet" href="${ctx}/css/login.css">
</head>

<body>
<div class="box">
    <h3 class="caption">甘肃省房建市政电子开标评标过程监督平台</h3>
    <div class="time">
        <span class="hours"></span>
        <span class="mao">:</span>
        <span class="min"></span>
        <span class="mao">:</span>
        <span class="sen"></span>
    </div>
    <div class="center">
        <div class="left">
            <h3>主管部门</h3>
            <img src="${ctx}/img/manager.png" alt="">
        </div>
        <div class="right">
            <div class="tlt">
                <div class="same">账户密码登录
                    <span></span>
                </div>
                <div id="userPass">CA登录
                    <span></span>
                </div>
            </div>
            <div class="puForm">
                <form id="userPassForm" class="layui-form" action="${ctx}/login" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                    <div class="useinput">
                        <input type="text" lay-verType="tips" class="name" name="username" lay-verify="required" lay-reqText="账户名不能为空" autocomplete="false" placeholder="用户名">
                        <input type="password" lay-verType="tips" class="pass" name="password" lay-verify="required" lay-reqText="请输入登陆密码" autocomplete="false" placeholder="密码">
                        <div class="photo">
                            <input type="text" class="verify" name="vrifyCode" autocomplete="false" lay-verType="tips"
                                   lay-verify="required"
                                   lay-reqText="请输入验证码" placeholder="验证码" maxlength="4">
                            <img onclick="initVerification('.useinput')" class="yzm" style="cursor: pointer;">
                        </div>
                        <div class="login" lay-submit lay-filter="password*">登 &nbsp;&nbsp;&nbsp;&nbsp;录</div>
                    </div>
                </form>
            </div>
            <div class="caForm cainput" style="display:none;">
                <form id="caForm" class="layui-form" action="${ctx}/login" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                    <input name="ukey_serial_number" value="" type="hidden"/>
                    <input name="username" value="" type="hidden"/>
                    <select id="ca_list" name="casn" placeholder="请选择登录CA" class="list" lay-filter="casnSelect" style="height: 20%;" readonly="readonly">
                    </select>
                    <input type="password" name="password" class="pass" placeholder="输入CA密码"
                           lay-verify="required" lay-verType="tips" autocomplete="false"
                           lay-reqText="CA密码不可为空"/>
                    <div class="photo">
                        <input type="text" class="verify" name="vrifyCode" autocomplete="false" lay-verType="tips"
                               lay-verify="required"
                               lay-reqText="请输入验证码" placeholder="验证码" maxlength="4">
                        <img onclick="initVerification('.useinput')" style="cursor: pointer;" class="yzm">
                    </div>
                    <div class="login" lay-submit lay-filter="ca*">登 &nbsp;&nbsp;&nbsp;&nbsp;录</div>
                </form>
                <div class="care">注意事项:<br/><span>请先安装CA证书驱动</span></div>
            </div>
        </div>
    </div>
    <#--<div class="foot">copyright © 2020</div>-->
</div>
<!-- 处理登录错误信息 -->
<#assign msg=""/>
<#if Session.SPRING_SECURITY_LAST_EXCEPTION??>
    <#assign msg="${Session.SPRING_SECURITY_LAST_EXCEPTION.message}"/>
</#if>
<script>
    $(function () {
        initVerification();
        initCaList();
    })

    /**
     * 验证码刷新
     */
    function initVerification() {
        $(".yzm").prop("src", "${ctx}/kaptcha/verification?p=" + Math.random())
    }

    /**
     * 加载证书列表
     */
    function initCaList() {
        // 获取设备列表
        try {
            listUkeys(function (ukeys) {
                $("#ca_list").empty();
                var lis = "";
                if (!isNull(ukeys) && ukeys.length > 0) {
                    for (var i = 0; i <= ukeys.length - 1; i++) {
                        if (i==0) {
                            $("input[name='ukey_serial_number']").val(ukeys[i].cert_no);
                        }
                        lis += "<option data-certno='" + ukeys[i].cert_no + "'  data-serialnumber='' data-name='" + ukeys[i].cert_name + "'>" + ukeys[i].cert_name + "</option>";
                    }
                    $('#ca_list').append(lis);
                }
            });
        } catch (e) {
            console.warn("无法初始化锁");
            console.warn(e);
            var options = "";
            //3、拼接html
            options += "<option>未检测到UKEY</option>";
            $('#ca_list').append(options);
        } finally {
            layui.form.render();
        }
    }

    /**
     * 处理登录错误信息
     */
    $(window).on("load", function () {
        var error_msg = "${msg}";
        if (error_msg) {
            layer.msg(error_msg, {icon: 2});
        }

        layui.use('form', function () {
            var form = layui.form;
            form.on('select(casnSelect)', function (data) {
                var certno = data.elem.data("certno");
                var serialnumber = data.elem.data("serialnumber");
                $("input[name='cert_serial_number']").val(serialnumber);
                $("input[name='ukey_serial_number']").val(certno);
            });

            // 账号登录
            form.on('submit(password*)', function (data) {
                layer.msg("数据加载中...", {icon: 16, time: 0, shade: 0.3});
                $.ajax({
                    url: '${ctx}/login',
                    type: 'post',
                    cache: false,

                    data: $("#userPassForm").serialize(),
                    success: function (data) {
                        window.location.href = "${ctx}/index"
                    },
                    error: function (data) {
                        console.log(data)
                        loadComplete();
                        if (data.status == 403) {
                            layerError("浏览器无访问权限", function () {
                                window.location.reload();
                            })
                        } else {
                            window.location.reload();
                        }
                    },
                });
                //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                return false;
            });

            // ca登录
            form.on('submit(ca*)', function (data) {
                var $password = $("#caForm").find("input[name='password']");
                var ukey_serial_number = $("input[name='ukey_serial_number']").val();
                if (!isNull(ukey_serial_number)) {
                    caLogin(ukey_serial_number, $password.val(), function (returnInfo) {
                        getQSKJCertInfo(ukey_serial_number,function (QSKJ_INFO) {
                            var index =  layer.msg("数据加载中...", {icon: 16,time:0,shade:0.2});
                            $.ajax({
                                url: '/login',
                                type: 'post',
                                cache: false,
                                data: serializeObject('#caForm'),
                                success: function (data) {
                                    if (data) {
                                        window.location.href = "/index"
                                    }
                                },
                                error: function (data) {
                                    console.warn(data);
                                    if (data.status == 403) {
                                        layerError("浏览器无访问权限", function () {
                                            window.location.reload();
                                        })
                                    } else {
                                        window.location.reload();
                                    }
                                },
                            });
                        })
                    });
                } else {
                    //互认的CA登录
                    var index =  layer.msg("数据加载中...", {icon: 16,time:0,shade:0.2});
                    if (logonSign()) {
                        setTimeout(function () {
                            $.ajax({
                                url: '/login',
                                type: 'post',
                                cache: false,
                                data: serializeObject('#caForm'),
                                success: function (data) {
                                    if (data) {
                                        window.location.href = "/index"
                                    }
                                },
                                error: function (data) {
                                    console.warn(data);
                                    if (data.status == 403) {
                                        layerError("浏览器无访问权限", function () {
                                            window.location.reload();
                                        })
                                    } else {
                                        window.location.reload();
                                    }
                                },
                            });
                        }, 200);
                    }
                }
                //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                return false;
            });
            form.render();
        });
    });

    $(".tlt").on("click", "div", function () {
        $(this).addClass("same").siblings().removeClass("same");
        var str = $(this).attr("id");
        if (str === "userPass") {
            initCaList();
            $(".cainput").show();
            $(".useinput").hide();
        } else {
            $(".cainput").hide();
            $(".useinput").show();
        }
    });
</script>
</body>
</html>