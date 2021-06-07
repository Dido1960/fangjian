<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>部门选择</title>
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
</head>
<body class="layui-layout-body">
<div id="LAY_app">
    <form class="layui-form">
        <div class="layui-form-item">
            <span>请选择用户</span>
            <select name="govUser">
                <#if users?? && users?size gt 0>
                    <#list users as user>
                        <option value="${user.id}">${user.name}------${user.depName}</option>
                    </#list>
                    <#else >

                </#if>

            </select>
        </div>
    </form>
</div>

<script src="${ctx}/layuiAdmin/layui/layui.js?t=1"></script>
<script type="text/javascript">
    layui.use(['form', 'layer'], function () {
        var form = layui.form;
        form.render();
    });



    function configGovUser() {
        console.log("============"+$("select[name='govUser'] option:selected").val());

            layer.confirm("确定将该用户配置到该环节下吗",
                {icon: 3, title: '操作确认提示'},
                function () {
                    var loadIndex = layer.load();
                    //点击确定回调事件

                    $.ajax({
                        url: '${ctx}/partAuditFlow/configGovUserToPart',
                        async: false,
                        cache: false,
                        traditional: "true",
                        data: {
                            gId: $("select[name='govUser'] option:selected").val()
                            ,id:'${id}'
                        },
                        success: function (data) {
                            if(data){
                                layer.alert("配置成功",{icon:6},function () {
                                    window.parent.location.reload();
                                })
                            }else {
                                layer.alert("配置失败",{icon:5},function () {
                                    window.location.reload();
                                })
                            }
                        }
                    })
                }
            )

    }



</script>
</body>
</html>

