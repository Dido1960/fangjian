<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>添加区划</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_parameterName" content="${ _csrf.parameterName}"/>
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
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.js"></script>
    <!--citypicker-->
    <script type="text/javascript" src="${ctx}/plugin/citypicker/city-picker.data.js"></script>
    <script type="text/javascript" src="${ctx}/plugin/citypicker/city-picker.js"></script>
    <link href="${ctx}/plugin/citypicker/city-picker.css" rel="stylesheet"/>
</head>
<body>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend>添加区划</legend>
</fieldset>
<form class="layui-form" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <input type="hidden" name="province">
    <input type="hidden" name="city">
    <input type="hidden" name="district">
    <div class="layui-inline" style="margin-bottom: 15px">
        <label class="layui-form-label width_auto text-r" style="margin-top:2px">行政区划</label>
        <div class="layui-input-inline" style="width:400px">
            <input type="text" autocomplete="on" class="layui-input" id="reg" name="reg" readonly="readonly"
                   data-toggle="city-picker" placeholder="请选择">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">启用状态</label>
        <div class="layui-input-block" style="width: 400px;">
            <@WordBookTag key="enabled" name="enabled" id="enabled" please="t"></@WordBookTag>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button type="button" id="form-submit" class="layui-btn">确定</button>
            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
        </div>
    </div>
</form>
<script type="text/javascript">
    layui.use(['form', 'layer'], function () {
        var form = layui.form;
        form.render();
    });

    /**
     * 确认新增
     */
    $("#form-submit").on("click", function () {
        var enabled = $('#enabled option:selected').val();
        var regobj = $("input[name=reg]");
        var province = regobj.data("citypicker").getCode("province");
        var city = regobj.data("citypicker").getCode("city");
        var district = regobj.data("citypicker").getCode("district");

        var regName = regobj.data("citypicker").getVal() + "/";

        var provinceName = regName.substring(0, regName.indexOf("/"));
        regName = regName.replace(provinceName + "/", "");
        var cityName = regName.substring(0, regName.indexOf("/"));
        regName = regName.replace(cityName + "/", "");
        var districtName = regName.replace("/", "");
        if (regobj.val() && enabled) {
            $.ajax({
                url: '${ctx}/reg/addReg',
                data: {
                    regName: regName,
                    state: enabled,
                    provinceCode: province,
                    cityCode: city,
                    districtCode: district,
                    provinceName: provinceName,
                    cityName: cityName,
                    districtName: districtName
                },
                success: function () {
                    parent.location.href = '${ctx}/reg/frameRegPage';
                }
            })
        } else if (!regobj.val()) {
            window.top.layer.msg("请选择行政区划！", {icon: 2});
        } else if (!enabled) {
            window.top.layer.msg("请选择启用状态！", {icon: 2});
        }

    });

    /**
     * citypicker使用
     */
    $(function () {
        $("#reg").citypicker();
    })
</script>
</body>
</html>