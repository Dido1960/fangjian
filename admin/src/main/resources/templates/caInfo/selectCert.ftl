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
    <script src="/js/XTXSAB.js"></script>
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
                <select id="sele_devices" name="sele_devices" lay-filter="sele_devices" class="form-select"
                        style="width: 150px;">
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
        <div class="layui-form-item notNeedPassWord" style="display: none">
            <label class="layui-form-label"></label>
            <div class="layui-input-inline">
                <span style="color: green"><i class="layui-icon layui-icon-username"></i>已登录</span>
            </div>
        </div>
        <div class="layui-form-item">

        </div>
    </form>
</div>

<script defer="defer">
    $(window).on("load", function () {
        layui.use(['form', 'layer'], function () {
            var form = layui.form;
            form.on('select(sele_devices)', function (data) {
                //得到被选中的值
                needPassPanel();
            })
            form.render();
        });
        setTimeout(function () {
            initSelect();
        }, 500);
    })

    /**
     * 初始CA
     * ***/
    function initSelect() {
        $("#sele_devices").empty();

        listUkeys(function (keyInfos) {
            for (var i = 0; i < keyInfos.length; i++) {
                $("#sele_devices").append("<option value='" + keyInfos[i].cert_no + "'>" + keyInfos[i].cert_name + "</option>");
            }
            needPassPanel();

            layui.form.render();

        });

    }

    var HIDDEN_PASSWROD=false;
    /**
     * 只选锁，不输入密码
     */
    function hidePassWord(){
        HIDDEN_PASSWROD=true;
    }

    /**
     * 是否需要秘密输入面板
     * **/
    function needPassPanel() {

        SOF_IsLogin($("#sele_devices").val(), function (returnInfo) {
            if (returnInfo.retVal||HIDDEN_PASSWROD) {
                $(".notNeedPassWord").show();
                $(".needPassWord").hide();
                $(".needPassWord input").attr("lay-verify", "");
            } else {
                $(".notNeedPassWord").hide();
                $(".needPassWord").show();
                $(".needPassWord input").attr("lay-verify", "required");
            }
            layui.form.render();
        })

    }

    //ca登录
    function loginCa(callSuccss) {
        var sn = $("#sele_devices").find("option:selected").val();
        var pwd = $(":password").val();

        if (!sn) {
            layer.msg("请选择KEY");
            return null;
        }

        if (!$(".needPassWord").is(":hidden")) {
            if (!pwd) {
                layer.msg("请输入密码");
                return null;
            }
            var loginFlag = caLogin(sn, pwd, function () {
                getCertInfo(sn, function (certInfo, QSKJInfo) {
                    callSuccss(certInfo, QSKJInfo);
                })
            });
        } else {
            getCertInfo(sn, function (certInfo, QSKJInfo) {
                callSuccss(certInfo, QSKJInfo);
            })
        }

    }


</script>
<script language=JavaScript event=OnUsbKeyChange for=XTXAPP>
    initSelect();
</script>
</body>
</html>