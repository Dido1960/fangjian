<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>绑定CA</title>
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
    <#--BJCA--START-->
    <script src="${ctx}/js/md5.js"></script>
    <script src="${ctx}/js/base64.js"></script>
    <script src="/layuiAdmin/layui/layui.js"></script>
    <script src="/js/XTXSAB.js"></script>
    <script src="/js/baseCertInfo.js"></script>
    <#--BJCA--END-->
    <script type="text/javascript">
        var successFunc;
        var submitIng = false;
        layui.use('form', function () {
            var form = layui.form;
            var url = '${ctx}/admin/userCert/bindValid';
            form.verify({
                usekeyNum: function (value, item) {//value：表单的值、item：表单的DOM对象
                    var msg;
                    $.ajax({
                        type: "get",
                        url: url,
                        cache: false,
                        async: false, // 使用同步的方法
                        data: {
                            "ukeyNum": $("input[name='ukeyNum']").val(),
                            "userType": '${type}'
                        },
                        success: function (result) {
                            if (!isNull(result.msg)) {
                                msg = "该Key已经绑定用户，请先解绑 [" + result.msg + "]";
                                layer.alert(msg);
                            }
                        }
                    });
                    return msg;
                },
            });
            form.on('submit(*)', function (data) {
                var msg;
                $.ajax({
                    type: "get",
                    url: url,
                    cache: false,
                    async: false, // 使用同步的方法
                    data: {
                        "ukeyNum": $("input[name='ukeyNum']").val(),
                        "userType": '${type}'
                    },
                    success: function (result) {
                        if (!isNull(result.msg)) {
                            msg = "该Key已经绑定用户，请先解绑 [" + result.msg + "]";
                            layer.alert(msg);
                        }
                    }
                });
                if(!isNull(msg)){
                    return false;
                }
                if (submitIng) {
                    return false;
                }
                submitIng = true;

                getCertInfo($("input[name='ukeyNum']").val(), function (cert_info) {
                    var index = window.top.layer.load();
                    $("input[name='ukeyCertNo']").val(cert_info.CERT_SERIAL_NUMBER);
                    $("input[name='companyBase64Img']").val(cert_info.SIGNAR_PIC_BASE);

                    $.ajax({
                        url: '${ctx}/admin/userCert/addUserCert',
                        type: 'post',
                        cache: false,
                        async: true,
                        data: $("#form1").serialize(),
                        success: function () {
                            window.top.layer.close(index);
                            successFunc();
                        },
                        error: function (data) {
                            console.error(data);
                            layer.msg("操作失败！")
                            window.top.layer.close(index);
                        },
                    });
                })

                return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
            });
            form.render();
        });

        /**
         * 新增人员
         *
         * @param successFunc1 成功回调函数
         */
        function submitCompanyUser(successFunc1) {
            successFunc = successFunc1;
            $("#formBtnSubmit").trigger("click");
        }


        $(function () {
            $('.tab').on('click', 'div', function () {
                $(this).addClass('light').siblings().removeClass('light')
                var id = $(this).attr('id')
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

            /**旋转按钮***/
            $('.btn').on('click', function () {
                var $_img = $(this).children('img');
                $_img.addClass('xuan')
                setTimeout(function () {
                    $_img.removeClass('xuan');
                }, 800)
            });
            setTimeout(function () {
                initCaList();
            }, 200)

        })

        function initCaList() {
            $(".layui-icon-refresh").css({
                "transform": "rotate(-2deg)",
                "webkitTransform": "rotate(-2deg)",
                "mozTransform": "rotate(-2deg)",
                "msTransform": "rotate(-2deg)",
            })
            // 获取设备列表
            try {
                listUkeys(function (ukeys) {
                    $("#listCAli").empty();
                    $("input[name='usekeyNum']").val("");
                    $("input[name='caName']").val("");
                    var options = "";
                    for (var i = 0; i <= ukeys.length - 1; i++) {
                        options += "<li onclick=selectCAInfo(this) ukeyNum='" + ukeys[i].cert_no + "'>" + ukeys[i].cert_name + "</li>";
                    }
                    $('#listCAli').append(options);
                });

            } catch (e) {
                console.error("无法初始化锁")
                console.error(e);
            }

        }

        /**
         *  设置CaInfo
         * @return
         * @author lesgod
         * @date 2020-6-19 17:45
         */
        function selectCAInfo(e) {
            console.log($(e).attr("ukeyNum"));
            $("input[name='ukeyNum']").val($(e).attr("ukeyNum"));
            $("input[name='caName']").val($(e).html());
        }

    </script>
</head>
<body style="background: #F1F1F1">
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend>新增CA绑定</legend>
</fieldset>
<form class="layui-form" method="post" id="form1">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <input type="hidden" name="userId" value="${userId}">
    <input type="hidden" name="userType" value="${type}">
    <div class="cainput" style="text-align: left;align-items:stretch;margin-left: 20px;width: 80%">
        <div class="capass">
            <input name="ukeyCertNo" type="hidden"/>
            <input name="companyBase64Img" type="hidden"/>
            <input name="ukeyNum" type="hidden"/>
            <input name="caName" type="text" class="name " lay-verify="required|usekeyNum" placeholder="请选择登陆锁"
                   readonly="readonly"
                   style="cursor: pointer;"/>
            <input name="Ce" type="hidden"/>
            <div class="btn" onclick="initCaList()">
                <img src="/img/new.png" alt="">
            </div>
        </div>
        <ul class="sele" id="listCAli" style="display: none;left: 0px">

        </ul>
    </div>
    <div class="layui-input-block" style="margin-left: 120px;display: none">
        <button lay-submit lay-filter="*" id="formBtnSubmit">提交</button>
    </div>
</form>
</body>
</html>