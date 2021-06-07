<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>专家录入</title>
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
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/login.css">
    <style>
        .caLogin .inp {
            width: 100%;
            height: 40px;
            background: #ffffff;
            border: 1px solid #cfdae5;
            margin-top: 30px;
        }
        .caLogin .inp input {
            width: calc(100% - 112px);
            float: left;
            border: none;
            margin-top: 0px;
        }
        .caLogin .inp img {
            float: left;
            cursor: pointer;
        }
    </style>
</head>

<body>
<header>
    <header>
        <div class="text">
            <div class="name">
                <img src="${ctx}/img/logo_black.png" alt="">
                甘肃省房建市政电子开标评标平台</div>
            <div class="bao">
                <div class="try time">星期一 16:24:16</div>
            </div>
        </div>
    </header>
</header>
<section>
    <div class="cont">
        <div class="cont-l">
            <h3>专家录入</h3>
        </div>
        <div class="cont-r">
            <ol class="title">
                <li class="sele" id="pass">账号密码登录<i></i></li>
                <li>证书登录<i></i></li>
            </ol>
            <form class="pass" id="userPassForm" action="${ctx}/login" method="post">
                <input type="text" lay-verType="tips" class="name" name="username" lay-verify="required" lay-reqText="账户名不能为空" autocomplete="false" placeholder="用户名">
                <input type="password" lay-verType="tips" class="pass" name="password" lay-verify="required" lay-reqText="请输入登陆密码" autocomplete="false" placeholder="密码">
                <div class="inp">
                    <input type="text" class="verify" name="vrifyCode" autocomplete="false" lay-verType="tips"
                           lay-verify="required" lay-reqText="请输入验证码" placeholder="验证码" maxlength="4">
                    <img onclick="initVerification('.useinput')" class="yzm" style="cursor: pointer;">
                </div>
                <div class="btn login" lay-submit lay-filter="password*">登录</div>
            </form>
            <form id="caForm" action="${ctx}/login" class="layui-form caLogin" style="display: none;">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <input name="ukey_serial_number" value="" type="hidden"/>
                <input name="username" value="" type="hidden"/>
                <div class="capass">
                    <select id="ca_list" name="casn" placeholder="请选择登录CA" lay-filter="casnSelect" readonly="readonly">
                    </select>
                    <span></span>
                    <div class="refresh" onclick="initCaList()"></div>
                </div>
                <input type="password" name="password" placeholder="输入CA密码"
                       lay-verify="required" lay-verType="tips" autocomplete="false"
                       lay-reqText="CA密码不可为空">
                <div class="inp">
                    <input type="text" class="verify" name="vrifyCode" autocomplete="false" lay-verType="tips"
                           lay-verify="required" lay-reqText="请输入验证码" placeholder="验证码" maxlength="4">
                    <img onclick="initVerification('.useinput')" class="yzm" style="cursor: pointer;">
                </div>
                <div class="btn login" lay-submit lay-filter="ca*">登录</div>
                <h3>注意事项:</h3>
                <p>1、使用CA登陆的时候，必须使用IE浏览器<br>
                    2、请先安装CA证书驱动</p>
            </form>
        </div>
    </div>
    <#--<div class="foot">copyright © 2020</div>-->
</section>
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

    $('.title').on('click', 'li', function () {
        $(this).addClass('sele').siblings().removeClass('sele')
        if ($(this).attr('id') == 'pass') {
            $('.pass').show()
            $('.caLogin').hide()
        } else {
            initCaList();
            $('.pass').hide()
            $('.caLogin').show()
        }
    })
</script>
</body>
</html>