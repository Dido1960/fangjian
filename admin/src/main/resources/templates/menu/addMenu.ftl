<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>添加菜单</title>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_parameterName" content="${ _csrf.parameterName}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-1.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-3.1.0.min.js"></script>

    <!--[if lt IE 9]>
    <script src="${ctx}/js/html5shiv.min.js"></script>
    <script src="${ctx}/js/respond.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script src="${ctx}/plugin/iconpicker/module/common.js"></script>
    <script type="text/javascript" src="${ctx}/js/common.js"></script>
</head>
<body>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend>添加<#if id??>子</#if>菜单</legend>
</fieldset>
<form class="layui-form" method="post" id="form1">
    <#if id??>
        <div class="layui-form-item">
            <label class="layui-form-label">父级菜单</label>
            <div class="layui-input-block">
                <input type="text" name="parentName" id="parent-name" value="${menuName!}"
                       autocomplete="off" class="layui-input" readonly>
                <input type="hidden" name="parentId" id="parent-id" value="${id!}">
            </div>
        </div>
    </#if>
    <div class="layui-form-item">
        <label class="layui-form-label">菜单名称</label>
        <div class="layui-input-block">
            <input type="text" name="menuName" id="menu-name" autocomplete="off" placeholder="请输入菜单名称"
                   class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">访问地址</label>
        <div class="layui-input-block">
            <input type="text" name="url" id="url" placeholder="请输入访问地址" autocomplete="off" class="layui-input">
        </div>
    </div>
    <#if id=null>
    <div class="layui-form-item">
        <label class="layui-form-label">选择图标</label>
        <div class="layui-input-block">
            <input type="text" name="iconFont" id="iconPicker" lay-filter="iconPicker" class="layui-input">
        </div>
    </div>
    </#if>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button type="button" class="layui-btn" id="form-submit" lay-submit="" lay-filter="submit">确定</button>
            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
        </div>
    </div>
</form>
<script type="text/javascript">
    layui.use(['form', 'layer', 'iconPicker'], function () {
        var form = layui.form;
        var iconPicker = layui.iconPicker;

        //
        iconPicker.render({
            // 选择器，推荐使用input
            elem: '#iconPicker',
            // 数据类型：fontClass/unicode，推荐使用fontClass
            type: 'fontClass',
            // 是否开启搜索：true/false
            search: true,
            // 是否开启分页
            page: true,
            // 每页显示数量，默认12
            limit: 12,
            // 点击回调
            click: function (data) {
                $("input[name='iconFont']").val(data.icon);
            }
        });
        $("input[name='iconFont']").val('layui-icon-component');
        iconPicker.checkIcon('iconPicker', 'layui-icon-component');
        form.render();
    });

    /**
     * 确认新增
     */
    $("#form-submit").on("click", function () {
        var menu_name = $("#menu-name").val();
        var parent_id = $("#parent-id").val();
        var url = $("#url").val();
        if (menu_name) {
            layer.load();
            $.ajax({
                url: '${ctx}/menu/addMenu',
                cache: false,
                type: 'post',
                data: serializeObject($("#form1")),
                success: function () {
                    window.top.layer.msg("添加成功！", {icon: 1})
                    parent.location.href = '${ctx}/menu/frameMenuPage';
                }
            })
        } else if (!menu_name) {
            window.top.layer.msg("请输入菜单名称！", {icon: 2});
        } else if (!url) {
            window.top.layer.msg("请输入地址！", {icon: 2});
        }

    });
</script>
</body>
</html>