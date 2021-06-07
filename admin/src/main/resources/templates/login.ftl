<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>后台登录</title>
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
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/css/adminLogin.css">
    <#--天威盾 --START-->
    <script src="${ctx}/js/topEsa.min.js"></script>
    <#--天威盾 --END-->
    <#--BJCA--START-->
    <script src="${ctx}/js/base64.js"></script>
    <script src="/js/XTXSAB.js"></script>
    <script src="/js/baseCertInfo.js"></script>
    <#--BJCA --END-->

    <script>
        $(function () {
            $('.tab').on('click', 'div', function () {
                $(this).addClass('light').siblings().removeClass('light')
                var id = $(this).attr('id')
                layer.closeAll();
                if (id == 'out') {
                    initCaList();
                    $('.cainput').show()
                    $('.cainput .photo img').attr('style', 'display:block')
                    $('.useinput').hide()
                    $('.useinput .photo img').attr('style', 'display:none')
                } else {
                    $('.cainput').hide()
                    $('.cainput .photo img').attr('style', 'display:none')
                    $('.useinput').show()
                    $('.useinput .photo img').attr('style', 'display:block')
                }
            })
            $('.capass').on('click', '.name', function () {
                $('.sele').fadeIn();
            })
            $('.capass').on('blur', '.name', function () {
                setTimeout(function () {
                    $('.sele').fadeOut();
                }, 100);
            })
            initVerification();

            /**旋转按钮***/
            $('.btn').on('click', function () {
                var $_img = $(this).children('img');
                $_img.addClass('xuan')
                setTimeout(function () {
                    $_img.removeClass('xuan');
                }, 800)
            });
        })

        function initCaList() {
            $(".layui-icon-refresh").css({
                "transform": "rotate(-2deg)",
                "webkitTransform": "rotate(-2deg)",
                "mozTransform": "rotate(-2deg)",
                "msTransform": "rotate(-2deg)",
            })

            try {
                listUkeys(function (ukeys) {
                    initVerification();
                    $("#listCAli").empty();
                    var lis = "";
                    if (!isNull(ukeys) && ukeys.length > 0) {
                        for (var i = 0; i <= ukeys.length - 1; i++) {
                            if (i==0) {
                                $("input[name='ukey_serial_number']").val(ukeys[i].cert_no);
                            }
                            lis += "<li onclick='selectCAInfo(this)' data-certno='" + ukeys[i].cert_no + "'  data-serialnumber='' data-name='" + ukeys[i].cert_name + "'>" + ukeys[i].cert_name + "</li>";
                        }
                        $('#listCAli').append(lis);
                    }
                });
            } catch (e) {
                console.warn("无法初始化锁");
                console.warn(e);
                var options = "";
                //3、拼接html
                options += "<option>未检测到UKEY</option>";
                $('#listCAli').append(options);
            } finally {
                layui.form.render();
            }
        }

        /**
         *  设置CaInfo
         * @return
         * @author lesgod
         * @date 2020-6-19 17:45
         */
        function selectCAInfo(e) {
            var ukey_serial_number = $(e).data("certno");
            var cert_serial_number = $(e).data("serialnumber");
            $("input[name='caName']").val($(e).text())
            $("input[name='cert_serial_number']").val(cert_serial_number);
            $("input[name='ukey_serial_number']").val(ukey_serial_number);
        }

    </script>
</head>
<body>
<div class="body">
    <div class="box">
        <div class="left">
            <div class="tlt" style="font-size: 20px">
                 市政房建电子开评标（管理端）
            </div>
        </div>
        <div class="right">
            <div class="tab">
                <div class="light" id="in">账号登录
                    <span></span>
                </div>
                <div id="out">CA登录
                    <span></span>
                </div>
            </div>
            <form class="layui-form" id="userPassForm" action="${ctx}/login" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <div class="useinput">
                    <input type="text" lay-verType="tips" class="name" name="username" lay-verify="required"
                           lay-reqText="账户名不能为空" autocomplete="false"
                           placeholder="用户名">
                    <input type="password" lay-verType="tips" class="pass" name="password" lay-verify="required"
                           lay-reqText="请输入登陆密码" autocomplete="false"
                           placeholder="密码">
                    <div class="photo">
                        <input type="text" name="vrifyCode" autocomplete="false" lay-verType="tips" class="verify" lay-verify="required"
                               lay-reqText="请输入验证码" placeholder="验证码" maxlength="4">
                        <img onclick="initVerification('.useinput')" class="yzm">
                    </div>

                    <div class="login" lay-submit lay-filter="password*">登 &nbsp;&nbsp;&nbsp;&nbsp;录</div>
                </div>
            </form>
            <form class="layui-form" id="caForm" action="${ctx}/login" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <div class="cainput" style="display:none;">
                    <div class="capass">
                        <input name="cert_serial_number" value="" type="hidden"/>
                        <input name="ukey_serial_number" value="" type="hidden"/>
                        <input name="toSign" value="" type="hidden" id="ToSign"/>
                        <input name="signData" value="" type="hidden" id="Signature"/>
                        <input name="username" value="" type="hidden"/>
                        <input name="caName" type=" " class="name "
                               placeholder="请选择登陆锁" readonly="readonly" autocomplete="false"
                               lay-verify="required" lay-verType="tips"
                               lay-reqText="请选择需要登录的CA"
                               style="cursor: pointer;"/>
                        <div class="btn" onclick="initCaList()">
                            <img src="/img/new.png" alt="">
                        </div>
                    </div>
                    <ul class="sele" id="listCAli" style="display: none;">

                    </ul>
                    <input type="password" name="password" class="pass" placeholder="密码"
                           lay-verify="required" lay-verType="tips" autocomplete="false"
                           lay-reqText="CA密码不可为空"/>
                    <div class="photo">
                        <input type="text" name="vrifyCode"  autocomplete="false" lay-verify="required" lay-verType="tips"
                               lay-reqText="验证码不可为空" class="verify" placeholder="验证码" maxlength="4"/>
                        <img onclick="initVerification('.cainput')" class="yzm"/>
                    </div>
                    <div class="login" lay-submit lay-filter="ca*">登 &nbsp;&nbsp;&nbsp;&nbsp;录</div>
                </div>

            </form>

        </div>
    </div>
</div>

<#--
<div class="pb_gradient_v1">
    <form class="layui-form" action="${ctx}/login" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        <div class="container">
            <div class="layui-form-item">
                <h2 style="text-align: center">
                    <img src="${ctx}/logo/logo.png" width="30px" height="30px">
                    <span>交易通</span>
                </h2>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">用户名</label>
                <div class="layui-input-block">
                    <input type="text" name="username" required lay-verify="required" placeholder="请输入用户名" value="admin"
                           autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">密 &nbsp;&nbsp;码</label>
                <div class="layui-input-inline">
                    <input type="password" name="password" required lay-verify="required" placeholder="请输入密码"
                           value="admin"
                           autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">验证码</label>
                <div class="layui-input-inline">
                    <input type="text" name="vrifyCode" required lay-verify="required|number" placeholder="请输入验证码"
                           autocomplete="off"
                           class="layui-input verity" maxlength="4">
                </div>
                <div class="layui-form-mid layui-word-aux">
                    <img onclick="initVerification()" id="yzm" style="width: 100px;height: 30px">
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button class="layui-btn layui-btn-normal" lay-submit lay-filter="formDemo">登陆</button>
                </div>
            </div>
            <a href="" class="font-set">忘记密码?</a> <a href="" class="font-set">立即注册</a>
        </div>
    </form>
</div>
-->


<!-- 处理登录错误信息 -->
<#assign msg=""/>
<#if Session.SPRING_SECURITY_LAST_EXCEPTION??>
    <#assign msg="${Session.SPRING_SECURITY_LAST_EXCEPTION.message}"/>
</#if>
<script>

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
                var cert_serial_number = $("input[name='cert_serial_number']").val();
                if (!isNull(ukey_serial_number)) {
                    caLogin(ukey_serial_number, $password.val(), function (returnInfo) {
                        getQSKJCertInfo(ukey_serial_number,function (QSKJ_INFO) {
                            // if(QSKJ_INFO.QSKJ_COMPANY_KEY!="0"){
                            //     layerError("非招标锁！无登录权限")
                            //     return;
                            // }
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

    })

    /***
     * 验证码刷新
     * **/
    function initVerification() {
        $(".yzm").prop("src", "${ctx}/kaptcha/verification?p=" + Math.random())
    }

    if (window.top != window.self) {
        window.top.location = "${ctx}/";
    }
</script>
</body>
</html>