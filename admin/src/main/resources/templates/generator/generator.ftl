<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>代码生成</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
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
    <link rel="stylesheet" href="${ctx}/layuiAdmin/style/admin.css" media="all">
</head>
<body>
<div class="layui-fluid">
    <div class="layui-card">
        <div class="layui-card-header">代码生成器</div>
        <div class="layui-card-body" style="padding: 15px;">
            <form class="layui-form" action="" lay-filter="component-form-group">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">@author</label>
                        <div class="layui-input-inline">
                            <input type="text" name="author" placeholder="请输入作者" lay-verify="required" lay-reqtext="你怕是在逗我?" autocomplete="off" class="layui-input" value="${author!}">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">模块名称</label>
                        <div class="layui-input-inline">
                            <input type="text" name="moduleName" lay-verify="required" lay-reqtext="你怕是在逗我?" placeholder="请输入模块名称" autocomplete="off" class="layui-input" value="${moduleName!}">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">数据库名称</label>
                        <div class="layui-input-inline">
                            <input type="text" id="db-name" name="dbName" lay-verify="required" lay-reqtext="你怕是在逗我?" placeholder="请输入数据库名称" autocomplete="off" class="layui-input"  value="${dbName!}">
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">数据库用户</label>
                        <div class="layui-input-inline">
                            <input type="text" id="db-user" name="dbUser" lay-verify="required" lay-reqtext="你怕是在逗我?" placeholder="请输入数据库用户" autocomplete="off" class="layui-input" value="${dbUser!}">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">数据库密码</label>
                        <div class="layui-input-inline">
                            <input type="text" id="db-pwd" name="dbPwd" lay-verify="required" lay-reqtext="你怕是在逗我?" placeholder="请输入数据库密码" autocomplete="off" class="layui-input" value="${dbPwd!}">
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">生成内容</label>
                    <div class="layui-input-block">
                        <input type="checkbox" name="generator" value="entity" lay-skin="primary" title="Entity" checked>
                        <input type="checkbox" name="generator" value="controller" lay-skin="primary" title="Controller">
                        <input type="checkbox" name="generator" value="service" lay-skin="primary" title="Service">
                        <input type="checkbox" name="generator" value="mapper" lay-skin="primary" title="Mapper">
                        <input type="checkbox" name="generator" value="mapperXml" lay-skin="primary" title="Mapper Xml">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">功能选项</label>
                    <div class="layui-input-block">
                        <input type="checkbox" name="module" value="swagger2" lay-skin="primary" title="Swagger2">
                        <input type="checkbox" name="module" value="override" lay-skin="primary" title="Override">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">实体类</label>
                    <div class="layui-input-block" id="entity-block">

                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-input-block">
                        <div class="layui-footer" style="left: 0;">
                            <button class="layui-btn layui-btn-normal" type="button" onclick="selectEntity()">加载实体类</button>
                            <button type="button" class="layui-btn" lay-filter="submit" lay-submit>立即提交</button>
                            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script src="${ctx}/layuiAdmin/layui/layui.js"></script>
<script>
    function submitForm() {
        $.ajax({
            url: "${ctx}/generator/generator",
            type: "POST",
            data: $("form").serialize(),
            success: function(data) {

            }
        })
    }

    /**
     * 查询实体类
     */
    function selectEntity() {
        var db_name = $("#db-name").val();
        var db_user = $("#db-user").val();
        var db_pwd = $("#db-pwd").val();

        if (!db_name) {
            window.top.layer.msg("请输入数据库名称!", {icon: 2, anim: 6});
            return;
        }

        if (!db_user) {
            window.top.layer.msg("请输入数据库用户!", {icon: 2, anim: 6});
            return;
        }

        if (!db_pwd) {
            window.top.layer.msg("请输入数据库密码!", {icon: 2, anim: 6});
            return;
        }

        $.ajax({
            url: "${ctx}/generator/listEntity",
            type: "POST",
            data: {
                dbName: db_name,
                dbUser: db_user,
                dbPwd: db_pwd
            },
            success: function (map) {
                if (map.s) {
                    var $entity_block = $("#entity-block");
                    var entity_str = "<input type=\"checkbox\" lay-skin=\"primary\" name=\"entity\" title=\"全选\" lay-filter=\"check-all-entity\">";

                    for (var i = 0; i < map.entityList.length; i++) {
                        entity_str += "<input type=\"checkbox\" name=\"entity\" value=\"" + map.entityList[i] + "\" lay-skin=\"primary\" title=\"" + map.entityList[i] + "\">";
                    }

                    $entity_block.append(entity_str);
                    layui.form.render("checkbox");
                } else {
                    window.top.layer.msg(map.msg, {icon: 2, anim: 6});
                }
            }
        })
    }
</script>
<script>
    layui.config({
        base: '${ctx}/layuiAdmin' //静态资源所在路径
    }).extend({
        index: 'lib/index' //主入口模块
    }).use(['form'], function () {
        var form = layui.form;

        form.render();

        // 监听实体类全选
        form.on("checkbox(check-all-entity)", function (data) {
            if (data.elem.checked) {
                $(":checkbox[name=entity]").prop("checked", true);
                form.render("checkbox");
            } else {
                $(":checkbox[name=entity]").prop("checked", false);
                form.render("checkbox");
            }
        });

        // 监听提交
        form.on("submit(submit)", function () {
            var generator_size = $(":checkbox[name=generator]:checked").size();
            var entity_size = $(":checkbox[name=entity]:checked").size();

            if(generator_size < 1) {
                window.top.layer.msg("生成内容一个都不选?", {icon: 2, anim: 6});
                return false;
            }

            if(entity_size < 1) {
                window.top.layer.msg("实体类一个都不选?", {icon: 2, anim: 6});
                return false;
            }

            var override_flag = $(":checkbox[name=module][value=override]").prop("checked");

            if(override_flag) {
                window.top.layer.confirm("Override功能被启用, 已存在的内容将被直接覆盖, 可能造成数据丢失, 是否确认?", {
                    icon: 3,
                    title: '提示'
                }, function (index) {
                    // 确认的回调函数
                    window.top.layer.close(index);
                    submitForm();
                }, function (index) {
                    // 取消的回调函数
                    window.top.layer.msg("已取消!", {icon: 1});
                    window.top.layer.close(index);
                });
            } else {
                submitForm();
            }

            return false;
        })
    });
</script>
</body>
</html>
