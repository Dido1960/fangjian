<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>代理机构</title>
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
    <link rel="stylesheet" href="${ctx}/css/login.css">
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script type="text/javascript" src="${ctx}/js/utf.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-qrcode.js"></script>
    <script src="${ctx}/js/common.js"></script>
    <#--天威盾 --START-->
    <script src="${ctx}/js/topEsa.min.js"></script>
    <#--天威盾 --END-->
    <#--BJCA--START-->
    <script src="${ctx}/js/base64.js"></script>
    <script src="/js/XTXSAB.js"></script>
    <script src="/js/baseCertInfo.js"></script>
    <#--BJCA --END-->
    <script src="${ctx}/js/login.js"></script>
</head>

<body>
<div class="box">
    <h3 class="caption">甘肃省房建市政电子辅助开标系统</h3>
    <div class="time">
        <span class="hours"></span>
        <span class="mao">:</span>
        <span class="min"></span>
        <span class="mao">:</span>
        <span class="sen"></span>
    </div>
    <div class="center">
        <div class="left">
            <h3>代理机构</h3>
            <img src="${ctx}/img/opener.png" alt="">
        </div>
        <div class="right">
            <div class="tlt">
                <div class="same">账号登录
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
                    <input name="cert_serial_number" value="" type="hidden"/>
                    <input name="ukey_serial_number" value="" type="hidden"/>
                    <input name="toSign" value="" type="hidden" id="ToSign"/>
                    <input name="signData" value="" type="hidden" id="Signature"/>
                    <input name="username" value="" type="hidden"/>
                    <div class="caLigon">
                        <div style="height: 20%;width: 85%">
                            <select id="ca_list" name="casn" placeholder="请选择登录CA" class="list" lay-filter="casnSelect" style="height: 20%;" readonly="readonly">
                            </select>
                        </div>
                        <div class="refresh">
                            <img src="${ctx}/img/new.png" alt="">
                        </div>
                    </div>

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
            <div class="scanCodeForm scaninput" style="display:none;">
                <form id="scanCodeForm" class="layui-form" action="${ctx}/login" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                    <input name="username" value="" type="hidden"/>
                    <input type="hidden" name="password" value=""/>
                    <input name="qrCodeKey" value="" type="hidden"/>
                    <div class="QRCode" style="text-align: center;margin-top: 50px;" onclick="generateQRCode()"></div>
                    <input type="button" lay-submit lay-filter="scanCode*" style="display: none" id="scanCodeInput">
                </form>
            </div>
            <div class="scan-box" id="scanBox">
                <div class="scan-box-img">
                    <img class="scan-img" src="${ctx}/img/scan-icon.png" onclick="showScanBox(this)" />
                    <img style="display: none" class="close-img" src="${ctx}/img/diannao-icon.png" onclick="hideScanBox(this)">
                </div>
                <div class="scan-box-content">
                    <div class="content">
                        <div class="content-label">请使用 <span>标易信APP</span> 扫码登录</div>
                        <div class="scanCodeForm scaninput">
                            <form id="scanCodeForm" class="layui-form" action="${ctx}/login" method="post">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                <input name="username" value="" type="hidden"/>
                                <input type="hidden" name="password" value=""/>
                                <input name="qrCodeKey" value="" type="hidden"/>
                                <div class="QRCode" onclick="generateQRCode()"></div>
                                <input type="button" lay-submit lay-filter="scanCode*" style="display: none" id="scanCodeInput">
                            </form>
                            <div class="invalid-box" style="display: none" onclick="generateQRCode()">
                                <img src="${ctx}/img/reload-icon.png">
                                <span>二维码已失效</span>
                            </div>
                        </div>
                    </div>
                </div>
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
     * 点击显示二维码
     */
    function showScanBox(e) {
        $("#scanBox").addClass("show-scan-box")
        $("#scanBox").find(".scan-box-content").addClass("show-scan-box-content")
        var timer = setTimeout(function() {
            $("#scanBox").find(".scan-box-content .content").addClass("show-content")
        },300)
        $(e).css("display","none").siblings().css("display","inline-block")
        generateQRCode()
    }

    /**
     * 隐藏二维码
     */
    function hideScanBox(e) {
        $("#scanBox").removeClass("show-scan-box")
        $("#scanBox").find(".scan-box-content").removeClass("show-scan-box-content")
        $("#scanBox").find(".scan-box-content .content").removeClass("show-content")
        $(e).css("display","none").siblings().css("display","inline-block")
        if (!isNull(interval)) {
            clearInterval(interval);
        }
    }

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
                initVerification();
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
        setTimeout(function () {
            // 天威盾驱动加载
            initCertList();
        }, 500)
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
                var certno = $(data.elem).attr("data-certno");
                var serialnumber =  $(data.elem).data("serialnumber");
                $("input[name='cert_serial_number']").val(serialnumber);
                $("input[name='ukey_serial_number']").val(certno);
            });

            // 账号登录
            form.on('submit(password*)', function (data) {
                layer.msg("数据加载中...", {icon: 16, time: 0, shade: 0.3});
                setTimeout(function () {
                    $.ajax({
                        url: '${ctx}/login',
                        type: 'post',
                        cache: false,
                        data: $("#userPassForm").serialize(),
                        success: function (data) {
                            window.location.href = "${ctx}/index"
                        },
                        error: function (data) {
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
                }, 200);
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
                            console.log("  =====当前ukey类型====： "+QSKJ_INFO.QSKJ_COMPANY_KEY)
                            if(QSKJ_INFO.QSKJ_COMPANY_KEY != "0"){
                                layerAlert("非招标锁！无登录权限")
                                return;
                            }
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
            // 扫码登录
            form.on('submit(scanCode*)', function (data) {
                layer.msg("数据加载中...", {icon: 16, time: 0, shade: 0.3});
                $.ajax({
                    url: '${ctx}/login',
                    type: 'post',
                    cache: false,
                    data: $("#scanCodeForm").serialize(),
                    success: function (data) {
                        window.location.href = "${ctx}/index"
                    },
                    error: function (data) {
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
            form.render();
        });
    });

    $(".tlt").on("click", "div", function () {
        $(this).addClass("same").siblings().removeClass("same");
        var str = $(this).attr("id");
        if (str === "userPass") {
            $(".cainput").show();
            $(".useinput").hide();
            initCaList();
        } else {
            $(".cainput").hide();
            $(".useinput").show();
        }
    });
    var refreshCount=0;
    $(".refresh").on("click", function () {
        if(refreshCount!=0){
            return ;
        }
        refreshCount++;
        initCaList();
        var $_img=$(this).children("img");
        $_img.addClass("rotate");
        setTimeout(function () {
            $_img.removeClass("rotate");
            refreshCount--;
        }, 1000);

    });
    if (window.top != window.self) {
        window.top.location = "${ctx}/login.html";
    }

    /***********互认CA JS 信息**************start******/

    $(document).ready(function () {
        TopESAConfig();
    });

    function initCertList() {
        try {
            var certs = CertStore.listAllCerts().byKeyUsage(TCA.digitalSignature).byIssuer(arrayIssuerDN); //过滤签名证书
            var certs_Validity =  CertStore.listAllCerts().byKeyUsage(TCA.digitalSignature).byValidity();  //过滤有效期内的签名证书
            if (certs.size() > 0) {
                var keyName = "";
                for (var i = 0; i < certs.size(); i++) {
                    var flag = 0;
                    var cert = certs.get(i);
                    var sn = cert.serialNumber();
                    var cn = getCNFromSubject(cert);
                    if (i == 0)
                        keyName = cn;
                    for (j = 0; j < certs_Validity.size(); j++) {
                        var cert_validity = certs_Validity.get(j);
                        var sn_validity = cert_validity.serialNumber();
                        if (sn == sn_validity) {
                            addOption(cert.serialNumber(), cn, cn);
                            var ukey_number = $("input[name='ukey_serial_number']").val();
                            var cert_number = $("input[name='cert_serial_number']").val();
                            if (isNull(ukey_number) && isNull(cert_number)) {
                                $("input[name='cert_serial_number']").val(cert.serialNumber());
                            }
                            flag = 1;
                        }
                    }
                    if (flag == 0) {
                        addOption(cert.serialNumber(), "(已过期)" + cn, cn);
                    }
                }
            }
        } catch (e) {
            if (e instanceof TCACErr) {
                console.error("互认驱动读取失败！请检查互认驱动")
            } else {
                alert("过滤证书异常！");
            }
        } finally {
            layui.form.render();
        }
    }

    // 从Certificate对象中获取CN
    function getCNFromSubject(cert) {
        try {
            var t = cert.subject().match(/(S(?!N)|L|O(?!U)|OU|SN|CN|E)=([^=]+)(?=, |$)/g);
            for (var i = 0; i < t.length; i++) {
                if (t[i].indexOf("CN=") === 0)
                    return t[i].substr(3, t[i].length);
            }
            return null;
        } catch (e) {
            if (e instanceof TCACErr) {
                alert(e.toStr());
            } else {
                alert("获取CN异常!");
            }
        }
    }

    function addOption(oValue, oName, certName) {
        /***证书存在就取消**/
        if ($("#ca_list option[data-name='" + certName + "']").size() > 0) {
            return;
        }
        var options = "<option data-certno='' data-serialnumber='" + oValue + "' data-name='" + certName + "'>" + oName + "</option>";
        $('#ca_list').append(options);
    }

    // 返回Certificate对象
    function getSelectedCert() {
        try {
            var certs = CertStore.listAllCerts();
            var selectedCertSN = $("input[name='cert_serial_number']").val();
            var r = certs.bySerialnumber(selectedCertSN);
            return r.get(0);
        } catch (e) {
            if (e instanceof TCACErr) {
                alert(e.toStr());
            } else {
                alert("没有找到证书");
            }
        }
    }

    function GetRandomNum(Min, Max) {
        var Range = Max - Min;
        var Rand = Math.random();
        return (Min + Math.round(Rand * Range));
    }

    //签名方法
    function logonSign() {
        try {
            var toSign = GetRandomNum(1000000, 9999999);
            $("#ToSign").val(toSign);
            var cert = getSelectedCert();
            var P7 = cert.signLogondata(toSign);
            $("#Signature").val(P7);
            return true;
        } catch (e) {
            if (e instanceof TCACErr) {
                console.log(e.toStr());
            } else {
                console.log("签名失败!!!");
            }
            return false;
        }
    }

    function sign() {
        var orginData = $("#orginData").val();
        var signData = $("#signData").val();
        try {
            var cert = getSelectedCert();
            var P7 = cert.signMessage(orginData);
            $("#signData").val(P7);
        } catch (e) {
            if (e instanceof TCACErr) {
                alert(e.toStr());
            } else {
                alert("签名失败!!!");
            }
            return false;
        }
    }

    /***********互认CA JS 信息**************END******/
</script>
<script>
    var interval;
    /**
     * 生成扫码二维码
     */
    var generate_flag = false;
    function generateQRCode() {
        $(".invalid-box").hide();
        //清空div
        $(".QRCode").empty();

        if (generate_flag) {
            return false;
        }
        generate_flag = true;
        //获取二维码生成数据，以及日志记录
        doLoading();
        $.ajax({
            url: "${ctx}/mobilePhoneScanCode/generateQRCode",
            type: "POST",
            cache: false,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrf_header, csrf_token);
            },
            success: function (data) {
                loadComplete();
                generate_flag = false;
                if (data.httpStatus && !isNull(data.address)) {
                    $(".QRCode").qrcode({
                        width: 228,
                        height: 228,
                        text: data.address,
                        render: "canvas",
                        src: '${ctx}/img/byx-icon.png'
                    });
                    verifyQRLogin(data.verifyId, data.token, function (qrCodeKey) {
                        console.log("认证成功开始登录：" + qrCodeKey)
                        if (!isNull(interval)) {
                            clearInterval(interval);
                        }
                        if (isNull(qrCodeKey)) {
                            layer.msg("未匹配到相应的企业")
                            return
                        }
                        var $scanCodeForm = $("#scanCodeForm");
                        var $username = $scanCodeForm.find("input[name='username']");
                        var $qrCodeKey = $scanCodeForm.find("input[name='qrCodeKey']");
                        var $password = $scanCodeForm.find("input[name='password']");
                        $username.val(qrCodeKey);
                        $qrCodeKey.val(qrCodeKey);
                        $password.val(qrCodeKey);
                        $("#scanCodeInput").trigger("click");
                    });
                } else {
                    layer.msg("二维码获取失败")
                }
            },
            error: function (e) {
                generate_flag = false;
                loadComplete();
                console.error(e);
                if(e.status == 403){
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }
            }
        });
    }

    /**
     * 获取二维码登录结果
     * @param verifyId
     * @param token
     * @param callback
     */
    var verify = 0;
    function verifyQRLogin(verifyId, token, callback) {
        console.log("开始检测认证情况")
        interval = setInterval(function () {
            if (verify != 0) {
                return;
            }
            verify++;
            $.ajax({
                url: "${ctx}/mobilePhoneScanCode/verifyQRLogin",
                type: "POST",
                cache: false,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrf_header, csrf_token);
                },
                data: {
                    verifyId: verifyId,
                    token: token
                },
                success: function (data) {
                    console.log("认证代码：" + data.code)
                    console.log("认证信息：" + data.msg)
                    console.log("认证结果：" + data.data)
                    loadComplete();
                    if (data.code == "10050") {
                        if (!isNull(interval)) {
                            clearInterval(interval);
                        }
                        $(".invalid-box").show();
                    }
                    if (data.code == "200" && !isNull(data.data)) {
                        callback(data.data);
                    }
                    verify--;
                },
                error: function (e) {
                    loadComplete();
                    console.error(e);
                    if(e.status == 403){
                        console.warn("用户登录失效！！！")
                        window.top.location.href = "/login.html";
                    }
                }
            });
        }, 1000)
    }

</script>
</body>
</html>