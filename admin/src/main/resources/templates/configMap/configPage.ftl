<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>前台系统配置页面</title>
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
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
</head>
<body>
<div id="LAY_app">
    <div class="layui-layout layui-layout-admin">
        <div class="layui-card" style="margin: 15px 0 0 15px; width: calc(100% - 30px)">
            <form class="layui-form" method="post" id="addConfiMapForm">
                <#assign index = 0>
                <#list enumList as enumObj>
                    <div class="layui-form-item">
                        <#if index <= enumObj_index>
                        <#--控制一行显示input的个数-->
                            <#list 1..2 as i>
                            <#--自定义索引的值，应该小于list的长度-->
                                <#if index<enumList?size>
                                    <div class="layui-inline layui-col-md5">
                                        <#assign code="${enumList[index].code}" v=""/>
                                        <#if entityMap?? && entityMap[code]??>
                                            <#assign cId="${entityMap[code]}"/>
                                            <#assign v="${entityMap[code].configValue}"/>
                                        </#if>
                                        <label class="layui-form-label">${enumList[index].des}</label>
                                        <div class="layui-input-block" data-type="${enumList[index].configType}"
                                             data-key="${enumList[index].code}" data-des="${enumList[index].des}"
                                             config-map-div>
                                            <input type="text" class="layui-input layui-btn-radius"
                                                   style="border-radius:9px;" name="config-value" value="${v}"
                                                   placeholder="请输入系统配置值"/>
                                            <#if enumList[index].remark != "">
                                                <span class="layui-badge"
                                                      style="border-radius:4px;">${enumList[index].remark}</span>
                                            </#if>
                                        </div>
                                    </div>
                                </#if>
                            <#--更新索引的值-->
                                <#assign index +=1>
                            </#list>
                        </#if>
                    </div>
                </#list>
                <button type="button" class="layui-btn layui-btn-radius text-center layui-col-md1 layui-col-lg-offset5"
                        onclick="addConfigMap()">固化
                </button>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">
    /**
     * 初始化layui
     */
    layui.use(['form', 'table', 'layer'], function () {
        var form = layui.form;
        var layer = layui.layer;
        var table = layui.table;
    });
</script>
<script>

    function addConfigMap() {

        <#--//定义一个数组-->
        var config_map = [];
        var $config_map_div = $("div[config-map-div]");

        $config_map_div.each(function () {
            var map = {};
            map["configType"] = $(this).data("type");
            map["configKey"] = $(this).data("key");
            map["configValue"] = $(this).find("input[name=config-value]").val();
            map["configDes"] = $(this).data("des");

            config_map.push(map);
        })

        $.ajax({
            url: "${ctx}/configMap/saveConfigMap",
            type: "post",
            data: JSON.stringify(config_map),
            contentType: 'application/json;charset=utf-8',
            success: function (data) {
                layer.msg("操作成功", {icon: 1});
                // 操作成功后，刷新选项卡
                window.location.reload();
            }
        });
    }
</script>
</body>
</html>