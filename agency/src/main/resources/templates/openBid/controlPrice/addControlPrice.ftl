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
    <link rel="stylesheet" href="${ctx}/css/controlPrice.css">
    <script type="text/javascript" src="${ctx}/js/webService.js"></script>
</head>
<body>


<input type="hidden" id="final-control-price" value="${tenderDoc.controlPrice}">
<#--修改控制价-->
<#if tenderDoc.controlPrice?? && tenderDoc.controlPrice != ''>
    <div class="down" id="show-control-price">
        <p class="show-money">招标控制价为：${((tenderDoc.controlPrice)?number)?string(",###.00")} 元</p>
        <p class="show-chines-money"></p>
        <form action="javascript:void(0)" class="layui-form" style="margin-top: 50px;">
            <button id="btn-update" type="button" class="layui-btn btn" onclick="update_price()">修改控制价</button>
            <div id="update-price" style="display: none">
                <input class="control-price" type="number" maxlength="15" autocomplete="off"
                       placeholder="请填写修改招标控制价，（单位：元）" lay-reqText="请填写修改招标控制价" lay-verify="required|number|plength">
                <button class="layui-btn btn" lay-submit="" lay-filter="formSub">修改</button>
            </div>
        </form>
    </div>
<#else>
<#--新增控制价-->
    <div class="down" id="add-control-price">
        <form action="javascript:void(0)" class="layui-form" style="margin: 0 auto;">
            <input class="control-price" type="number" maxlength="15" autocomplete="off" placeholder="请填写招标控制价，（单位：元）"
                   lay-reqText="请填写招标控制价" lay-verify="required|number|plength">
            <button class="btn layui-btn" lay-submit="" lay-filter="formSub">确定</button>
        </form>
        <p class="show-chines-money"></p>
    </div>
</#if>
<script>
    // 浮点数正则
    var reg1 = /^\d+\.?\d{0,2}$/;
    // 正整数正则
    var reg2 = /^[+]{0,1}(\d+)$/;
    //规则：任意正整数，正小数（小数位不超过2位）
    var reg3 = /^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
    // 控制价的最大金额（不超过1万亿）
    var maxControlPrice = 1000000000000;
    var form;
    var layer;
    layui.use(['form', 'layer'], function () {
        form = layui.form;
        layer = layui.layer;
        form.verify({
            plength: function (value) {
                if (reg2.test(value)) {
                    if (value.length > 12) {
                        return '控制价超出最大金额限制';
                    }
                }
                if (reg1.test(value)) {
                    if (value.length > 15) {
                        return '控制价超出最大金额限制';
                    }
                }
                if (!reg3.test(value)){
                    return '控制价输入金额错误';
                }
            }
        });

        //监听提交
        form.on('submit(formSub)', function (data) {
            updateTenderDoc();
            // 阻止表单提交
            return false;
        });
    });

    /**
     * 修改招标文件
     */
    function updateTenderDoc() {
        var price = $(".control-price").val();
        if (price >= maxControlPrice) {
            layer.msg("控制价超出最大金额限制", {icon: 2});
            return;
        }
        doLoading();
        $.ajax({
            url: '${ctx}/staff/updateTenderDoc',
            type: 'post',
            cache: false,
            async: false,
            data: {
                bidSectionId: '${bidSection.id}',
                controlPrice: price
            },
            success: function (data) {
                loadComplete();
                if (data) {
                    // 添加公告
                    postMessageFun({linkType:"代理修改了控制价"});
                    // 成功
                    layer.msg("控制价添加成功！", {icon: 1})
                    // 隐藏填写控制价div
                    $("#add-control-price").attr("style", "display:none")
                    parent.window.location.reload();
                }
            },
            error: function (data) {
                loadComplete();
                console.error(data);
            },
        });
    }

    /**
     * 转换用户输入的小写数字
     */
    $(".control-price").on("keyup", function (e) {
        var controlPrice = $(this).val();
        if (controlPrice >= maxControlPrice) {
            layer.msg("控制价超出最大金额限制", {icon: 2});
            return;
        }
        // 招标控制价

        if (reg1.test(controlPrice)) {
            // 记录控制价
            $("#final-control-price").val($(this).val())
            $(".show-chines-money").text("大写: " + convertMoney(controlPrice, "元"));
            $(".show-money").text("招标控制价为：" + controlPrice + " 元");
        } else {
            $(".show-money").attr("style", "display:none");
            $(".show-chines-money").text("请填写合理的金额");
        }
    })

    /**
     * 当前项目存在控制价时，直接显示
     */
    function show_chinese_money() {
        control_price = $("#final-control-price").val();
        // 数字转换为金钱格式
        if (!isNull(control_price)) {
            $(".show-chines-money").text("大写: " + convertMoney(control_price, "元"));
        }
    }

    show_chinese_money();

    /**
     * 修改询问弹窗
     */
    function update_price() {
        var _index = window.top.layer.confirm('确定要修改招标控制价吗？', {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function () {
            // 显示输入框，修改按钮
            $("#update-price").show();
            // 修改询问按钮
            $("#btn-update").hide();
           window.top. layer.close(_index)
        }, function () {
        });
    }


</script>

<script>
    //placeholder兼容ie9
    function isPlaceholder(){
        var input = document.createElement('input');
        return 'placeholder' in input;
    }
    if (!isPlaceholder()) {//不支持placeholder 用jquery来完成
        $(document).ready(function() {
            if(!isPlaceholder()){
                $("input").not("input[type='password']").each(//把input绑定事件 排除password框
                    function(){
                        if($(this).val()=="" && $(this).attr("placeholder")!=""){
                            $(this).val($(this).attr("placeholder"));
                            $(this).focus(function(){
                                if($(this).val()==$(this).attr("placeholder")) $(this).val("");
                            });
                            $(this).blur(function(){
                                if($(this).val()=="") $(this).val($(this).attr("placeholder"));
                            });
                        }
                    });
                //对password框的特殊处理1.创建一个text框 2获取焦点和失去焦点的时候切换
                $("input[type='password']").each(
                    function() {
                        var pwdField    = $(this);
                        var pwdVal      = pwdField.attr('placeholder');
                        pwdField.after('<input  class="login-input" type="text" value='+pwdVal+' autocomplete="off" />');
                        var pwdPlaceholder = $(this).siblings('.login-input');
                        pwdPlaceholder.show();
                        pwdField.hide();

                        pwdPlaceholder.focus(function(){
                            pwdPlaceholder.hide();
                            pwdField.show();
                            pwdField.focus();
                        });

                        pwdField.blur(function(){
                            if(pwdField.val() == '') {
                                pwdPlaceholder.show();
                                pwdField.hide();
                            }
                        });
                    })
            }
        });
    }
</script>

</body>