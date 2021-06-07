<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>评标专家</title>
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
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script src="${ctx}/js/common.js"></script>

    <script src="${ctx}/js/login.js"></script>

    <link rel="stylesheet" href="${ctx}/css/login.css">
</head>

<body>
<header>
    <div class="text">
        <div class="name">
            <img src="${ctx}/img/logo.png" alt="">
            甘肃省房建市政电子辅助评标系统
        </div>
        <div class="bao">
            <div class="try time">星期一 16:24:16</div>
        </div>
    </div>
</header>
<section>
    <div class="cont">
        <div class="cont-l">
            <h3>评标专家</h3>
        </div>
        <div class="cont-r">
            <h3>账号密码登录</h3>
            <form id="userPassForm" class="layui-form" action="${ctx}/login" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <input type="text" lay-verType="tips" class="name" name="username" lay-verify="required"
                       lay-reqText="账户名不能为空" autocomplete="false" placeholder="用户名">
                <input type="password" lay-verType="tips" class="pass" name="password" lay-verify="required"
                       lay-reqText="请输入登陆密码" autocomplete="false" placeholder="密码">
                <div class="inp">
                    <input type="text" class="verify" name="vrifyCode" autocomplete="false" lay-verType="tips"
                           lay-verify="required" lay-reqText="请输入验证码" placeholder="验证码" maxlength="4">
                    <img onclick="initVerification('.useinput')" class="yzm" style="cursor: pointer;">
                </div>
                <button class="login" lay-submit lay-filter="password*">登录</button>
            </form>
        </div>
    </div>
    <#--<div class="foot">copyright © 2020</div>-->
</section>

<#--<div class="box">-->
<#--    <h3 class="caption"></h3>-->
<#--    <div class="time">-->
<#--        <span class="hours"></span>-->
<#--        <span class="mao">:</span>-->
<#--        <span class="min"></span>-->
<#--        <span class="mao">:</span>-->
<#--        <span class="sen"></span>-->
<#--    </div>-->
<#--    <div class="center">-->
<#--        <div class="left">-->
<#--            <h3>评标专家</h3>-->
<#--            <img src="${ctx}/img/expert.png" alt="">-->
<#--        </div>-->
<#--        <div class="right">-->
<#--            <div class="tlt">-->
<#--                <div class="same">账户密码登录-->
<#--                    <span></span>-->
<#--                </div>-->
<#--                <div id="userPass">CA登录-->
<#--                    <span></span>-->
<#--                </div>-->
<#--            </div>-->
<#--<div class="puForm">-->
<#--    <form id="userPassForm" class="layui-form" action="${ctx}/login" method="post">-->
<#--        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">-->
<#--        <div class="useinput">-->
<#--            <input type="text" lay-verType="tips" class="name" name="username" lay-verify="required"-->
<#--                   lay-reqText="账户名不能为空" autocomplete="false" placeholder="用户名">-->
<#--            <input type="password" lay-verType="tips" class="pass" name="password" lay-verify="required"-->
<#--                   lay-reqText="请输入登陆密码" autocomplete="false" placeholder="密码">-->
<#--            <div class="photo">-->
<#--                <input type="text" class="verify" name="vrifyCode" autocomplete="false" lay-verType="tips"-->
<#--                       lay-verify="required"-->
<#--                       lay-reqText="请输入验证码" placeholder="验证码" maxlength="4">-->
<#--                <img onclick="initVerification('.useinput')" class="yzm" style="cursor: pointer;">-->
<#--            </div>-->
<#--            <div class="login" lay-submit lay-filter="password*">登 &nbsp;&nbsp;&nbsp;&nbsp;录</div>-->
<#--        </div>-->
<#--    </form>-->
<#--</div>-->
<#--            <div class="caForm cainput" style="display:none;">-->
<#--                <form id="caForm" class="layui-form" action="${ctx}/login" method="post">-->
<#--                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">-->
<#--                    <input name="password" value="" type="hidden"/>-->
<#--                    <select id="ca_list" name="casn" placeholder="请选择登录CA" class="list" style="height: 20%;" readonly="readonly">-->
<#--                    </select>-->
<#--                    <input type="password" name="casnPassWord" class="pass" placeholder="输入CA密码"-->
<#--                           lay-verify="required" lay-verType="tips" autocomplete="false"-->
<#--                           lay-reqText="CA密码不可为空"/>-->
<#--                    <div class="photo">-->
<#--                        <input type="text" class="verify" name="vrifyCode" autocomplete="false" lay-verType="tips"-->
<#--                               lay-verify="required"-->
<#--                               lay-reqText="请输入验证码" placeholder="验证码" maxlength="4">-->
<#--                        <img onclick="initVerification('.useinput')" style="cursor: pointer;" class="yzm">-->
<#--                    </div>-->
<#--                    <div class="login" lay-submit lay-filter="ca*">登 &nbsp;&nbsp;&nbsp;&nbsp;录</div>-->
<#--                </form>-->
<#--                <div class="care">注意事项:<br/><span>请先安装CA证书驱动</span></div>-->
<#--            </div>-->
<#--        </div>-->
<#--    </div>-->
<#--    <div class="foot">copyright © 2020</div>-->
<#--</div>-->
<!-- 处理登录错误信息 -->
<#assign msg=""/>
<#if Session.SPRING_SECURITY_LAST_EXCEPTION??>
    <#assign msg="${Session.SPRING_SECURITY_LAST_EXCEPTION.message}"/>
</#if>
<script>
    $(function () {
        initVerification();

    })

    /**
     * 验证码刷新
     */
    function initVerification() {
        $(".yzm").prop("src", "${ctx}/kaptcha/verification?p=" + Math.random())
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

            // 账号ajax登录
            form.on('submit(password*)', function (data) {
                layer.load();
                setTimeout(function () {
                    $.ajax({
                        url: '${ctx}/login',
                        type: 'post',
                        cache: false,
                        data: serializeObject($('#userPassForm')),
                        success: function (data) {
                            layer.closeAll();
                            if (data) {
                                window.location.href = "/index";
                            }
                        },
                        error: function (data) {
                            layer.closeAll();
                            console.error(data);
                            layer.msg(data);
                            window.location.href = window.location.href;
                        }
                    });
                }, 200);
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
            $(".cainput").show();
            $(".useinput").hide();
        } else {
            $(".cainput").hide();
            $(".useinput").show();
        }
    });

    function test(test) {
        $.ajax({
            url: '${ctx}/expert/test',
            type: 'post',
            cache: false,
            data: {
               test:  test
            },
            success: function (data) {

            },
            error: function (data) {
                layer.closeAll();
                console.error(data);
                layer.msg(data);
                window.location.href = window.location.href;
            }
        });
    }
</script>
</body>
</html>