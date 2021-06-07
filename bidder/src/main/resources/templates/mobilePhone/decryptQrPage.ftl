<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>手机证书扫码解密</title>
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
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script type="text/javascript" src="${ctx}/js/utf.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-qrcode.js"></script>
    <style>
        .scan-box-content {
            height: 100%;
            width: 100%;
            padding: 20px;
            box-sizing: border-box;
            position: relative;
        }
        .content {
            position: absolute;
            top: 50%;
            left: 0;
            transform: translateY(-50%);
            width: 100%;
            text-align: center;
        }
        .content-label {
            text-align: center;
            margin-bottom: 20px;
            font-weight: bold;
            font-size: 20px;
        }
        .content-label span {
            color: rgba(48, 60, 100, 1);
        }
        .scanCodeForm {
            position: relative;
            width: 228px;
            margin: auto;
        }
        .scanCodeForm .invalid-box {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            color: #ffffff;
            background: rgba(0,0,0,0.8);
            text-align: center;
            line-height: 228px;
            cursor: pointer;
        }

        .scanCodeForm .invalid-box img {
            width: 24px;
            height: 24px;
            margin-right: 10px;
            transform: translateY(-2px);
        }

        .scanCodeForm .invalid-box span {
            font-weight: bold;
            font-size: 16px;
        }
        .layui-form-select dl {
            max-height: 200px;
        }
        #select-phone-cert {
            height: 100%;
            width: 100%;
            position: relative;
            box-sizing: border-box;
            padding: 50px 30px 0;
        }
        #select-phone-cert .select-btns {
            position: absolute;
            bottom: 30px;
            width: 100%;
            left: 0;
            word-spacing: -5px;
            text-align: center;
        }
        #select-phone-cert .select-btns > * {
            display: inline-block;
            height: 40px;
            line-height: 40px;
            font-size: 14px;
            cursor: pointer;
            text-align: center;
            width: 80px;
            border-radius: 6px;
            transition-duration: 200ms;
            font-weight: bold;
        }
        #select-phone-cert .layui-this {
            background-color: rgba(48, 60, 100, 1);
        }
        #select-phone-cert .select-btns .select-phone-btn:hover {
            opacity: 0.8;
        }
        #select-phone-cert .select-btns .yes {
            background: rgba(48, 60, 100, 1);
            color: #ffffff;
            margin-right: 40px;
        }
        #select-phone-cert .select-btns .not {
            color: #ffffff;
            background: #ccc;
        }
        #select-phone-cert .layui-form-label {
            padding: 0 15px;
            width: auto;
            line-height: 38px;
        }
        #select-phone-cert .layui-input-block {
            margin-left: 90px;
        }
    </style>
</head>
<body>
<div id="scan-box" class="scan-box-content">
    <div class="content">
        <div class="content-label">请使用 <span>标易信APP</span> 扫码解密</div>
        <div class="scanCodeForm scaninput">
            <form id="scanCodeForm" class="layui-form" action="javascript:void(0);" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <div class="QRCode" onclick="generateQRCode()"></div>
            </form>
            <div class="invalid-box" style="display: none" onclick="generateQRCode()">
                <img src="${ctx}/img/reload-icon.png">
                <span>二维码已失效</span>
            </div>
        </div>
    </div>
</div>
<div id="select-phone-cert" style="display: none;">
    <form class="layui-form" action="">
        <div class="layui-form-item">
            <label class="layui-form-label">证书选择</label>
            <div class="layui-input-block">
                <select id="phoneCert" name="phoneCert" lay-filter="phoneCert" class="form-select" style="width: 150px;">
                    <option>未检测到证书信息</option>
                </select>
            </div>
        </div>
    </form>
    <div class="select-btns">
        <div class="select-phone-btn yes" onclick="parent.phoneDecrypt()">确定</div>
        <div class="select-phone-btn not" onclick="phoneCancel()">取消</div>
    </div>
</div>
</body>
<script>
    $(function () {
        generateQRCode()
        layui.use('form', function () {
            var form = layui.form;
            form.on("select(phoneCert)", function (data) {
                if (!isNull(data.value)) {
                    console.log("切换phoneCertNo:" + data.value)
                    parent.$("#phoneCertStr").val(data.value);
                    form.render();
                }
            });
            form.render();
        });
    })
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
                    verifyQrUserCert(data.verifyId, data.token, function (phoneDecrypt) {
                        console.log("获取成功，弹出证书选择：" + phoneDecrypt)
                        if (!isNull(interval)) {
                            clearInterval(interval);
                        }
                        if (!isNull(phoneDecrypt)) {
                            parent.$("#successVerifyId").val(data.verifyId);
                            parent.$("#successToken").val(data.token);
                            selectPhoneCert(phoneDecrypt);
                        } else {
                            layer.alert("该用户没有可以使用的企业证书",{icon: 2});
                        }
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
     * 获取二维码证书获取结果
     * @param verifyId
     * @param token
     * @param callback
     */
    var verify = 0;
    function verifyQrUserCert(verifyId, token, callback) {
        console.log("开始检测证书获取情况")
        interval = setInterval(function () {
            if (verify != 0) {
                return;
            }
            verify++;
            $.ajax({
                url: "${ctx}/mobilePhoneScanCode/verifyQrUserCert",
                type: "POST",
                cache: false,
                data: {
                    verifyId: verifyId,
                    token: token
                },
                success: function (data) {
                    console.log("证书获取代码：" + data.code)
                    console.log("证书获取信息：" + data.msg)
                    console.log("证书获取结果：" + data.data)
                    loadComplete();
                    if (data.code == "10050") {
                        if (!isNull(interval)) {
                            clearInterval(interval);
                        }
                        $(".invalid-box").show();
                    }

                    if (data.code == "200") {
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

    /**
     * 选择手机证书解密
     * @param phoneDecrypt
     */
    function selectPhoneCert(phoneDecrypt) {
        $("#phoneCert").empty();
        var options = "";
        var num = 0;
        for (var i = 0; i < phoneDecrypt.length; i++) {
            if (phoneDecrypt[i].type === 1) {
                if (num === 0) {
                    parent.$("#phoneCertStr").val(phoneDecrypt[i].keyId);
                }
                options += "<option value='"+ phoneDecrypt[i].keyId +"'>"+ phoneDecrypt[i].name +"</option>";
            }
        }
        $("#phoneCert").append(options);
        layui.form.render();
        $("#scan-box").hide();
        $("#select-phone-cert").show();
    }

    function phoneCancel() {
        $("#scan-box").show();
        $("#select-phone-cert").hide();
        generateQRCode();
    }

</script>
</html>