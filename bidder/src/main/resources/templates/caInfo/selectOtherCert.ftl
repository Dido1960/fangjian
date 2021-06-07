<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>写证书</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="/js/jquery.min.js"></script>
    <script type="text/javascript" src="/js/jquery-migrate-1.4.1.min.js"></script>
    <script type="text/javascript" src="/js/jquery-migrate-3.1.0.min.js"></script>
    <!--[if lt IE 9]>
    <script src="/js/html5shiv.min.js"></script>
    <script src="/js/respond.min.js"></script>
    <![endif]-->
    <script src="/js/common.js"></script>
    <script src="/js/base64.js"></script>
    <link rel="stylesheet" href="/layuiAdmin/layui/css/layui.css" media="all">
    <script src="/layuiAdmin/layui/layui.js"></script>
    <script type="text/javascript" src="${ctx}/js/topEsa.min.js"></script>
    <script src="/js/baseCertInfo.js"></script>
    <style>
        .layui-form-select dl {
            max-height: 200px;
        }
    </style>
</head>
<body class="layui-layout-body">

<div style="margin: 10px 20px 10px 10px;">
    <form class="layui-form" action="">
        <div class="layui-form-item">
            <label class="layui-form-label">证书选择</label>
            <div class="layui-input-block">
                <select id="sele_devices" name="sele_devices" lay-filter="sele_devices" class="form-select" style="width: 150px;">
                    <option>请插入UKEY</option>
                </select>
            </div>
        </div>
        <div class="layui-form-item needPassWord">
            <label class="layui-form-label">密码</label>
            <div class="layui-input-inline">
                <input type="password" name="password" required lay-verify="required" placeholder="请输入密码"
                       autocomplete="off" class="layui-input">
            </div>
        </div>
    </form>
</div>

<script defer="defer">
    $(window).on("load", function () {
        layui.use(['form', 'layer'], function () {
            var form = layui.form;
            form.render();
        });
        setTimeout(function () {
            initCertList();
        }, 500);
    })

    //ca登录
    function loginCa(callSuccess) {
        var cert_serial_number = $("#sele_devices").find("option:selected").val();
        var pwd = $(":password").val();

        if (!cert_serial_number) {
            layer.msg("请选择KEY");
            return null;
        }

        if (!pwd) {
            layer.msg("请输入密码");
            return null;
        }
        $.ajax({
            url: "/common/data/otherCaCert",
            type: "POST",
            cache: false,
            data: {
                certSerialNumber: cert_serial_number,
                pwd: pwd
            },
            success: function (data) {
                console.log(data);
                callSuccess(data);
            },
            error: function (data) {
                layer.msg("操作失败", {icon: 5});
            }
        });
    }

    /***********互认CA JS 信息**************start******/

    function initCertList() {
        try {
            //var certs = CertStore.listAllCerts().forSign() ; //过滤签名证书
            var certs = CertStore.listAllCerts().byKeyUsage(128); //过滤签名证书
            var certs_Validity = CertStore.listAllCerts().byKeyUsage(128).byValidity(); //过滤有效期内的签名证书
            if (certs.size() > 0) {
                var keyName = "";
                for (var i = 0; i < certs.size(); i++) {
                    var flag = 0;
                    var cert = certs.get(i);
                    var sn = cert.serialNumber();
                    var cn = getCNFromSubject(cert);
                    if (i == 0)
                        keyName = cn;
                    for (var j = 0; j < certs_Validity.size(); j++) {
                        var cert_validity = certs_Validity.get(j);
                        var sn_validity = cert_validity.serialNumber();
                        if (sn == sn_validity) {
                            $("#sele_devices").append("<option value='" + cert.serialNumber() + "' data-name='" + cn + "'>" + cn + "</option>");
                            flag = 1;
                        }
                    }
                    if (flag == 0) {
                        $("#sele_devices").append("<option value='" + cert.serialNumber() + "' data-name='" + cn + "'>(已过期)" + cn + "</option>");
                    }
                }
            }
                layui.form.render();
        } catch (e) {
            if (e instanceof TCACErr) {
                console.error("互认驱动读取失败！请检查互认驱动")
            } else {
                alert("过滤证书异常！");
            }
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
</body>
</html>