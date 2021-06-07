<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>添加人员</title>
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
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
</head>
<body>

<div class="layui-panel-window">
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
        <legend>${user.name!}</legend>
    </fieldset>
    <table class="layui-table" lay-even="">
        <thead>
        <tr>
            <th colspan="6">
                <button type="button" class="layui-btn layui-btn-normal layui-btn-sm" onclick="addBindPage()">新增绑定
                </button>
                <button type="button" class="layui-btn layui-btn-primary layui-btn-sm"
                        onclick="window.location.href=decodeURIComponent('${redirectPage!}')">
                    <i class="layui-icon layui-icon-return"></i>
                    返回
                </button>
            </th>
        </tr>
        <tr>
            <th>序号</th>
            <th>绑定时间</th>
            <td>锁名称</td>
            <th>绑定锁号</th>
            <th>操作</th>
            <th>登录权限</th>
        </tr>
        </thead>
        <tbody>
        <#if certs??>
            <#list  certs as cert>
                <tr>
                    <td>${cert_index+1}</td>
                    <td>${cert.insertTime}</td>
                    <td>${cert.caName}</td>
                    <td>${cert.ukeyNum}</td>
                    <td>
                        <#if cert.loginFlag??&&cert.loginFlag?c=="1">
                            支持登录
                        <#else>
                            非登录锁
                        </#if>
                    </td>
                    <td>
                        <#if cert.loginFlag??&&cert.loginFlag?c=="1">
                            <button type="button" class="layui-btn layui-btn-normal layui-btn-sm"
                                    onclick="loginCert('${cert.id}',0)">解开登录
                            </button>
                        <#else>
                            <button type="button" class="layui-btn layui-btn-warm layui-btn-sm"
                                    onclick="loginCert('${cert.id}',1)">支持登录
                            </button>
                        </#if>
                         <button type="button" class="layui-btn layui-btn-warm layui-btn-sm"
                                onclick="unbindCert('${cert.id}')">解开绑定
                        </button>
                    </td>
                </tr>
            </#list>

        </#if>
        </tbody>
    </table>
</div>
</body>
<script>

    //解开绑定
    function unbindCert(id) {
        layer.confirm("是否解开绑定？解开绑定后将，该锁将与账号无关！",
            {
                icon: 3,
                title: '操作确认提示'
            },
            function (index) {
                layer.close(index);
                var indexLoad = layer.load();
                //点击确定回调事件
                $.ajax({
                    url: '${ctx}/admin/userCert/removeUserCert',
                    type: 'post',
                    cache: false,
                    async: false,
                    data: {
                        id: id
                    },
                    success: function (data) {
                        if (!isNull(data)) {
                            data = JSON.parse(data);
                        }
                        layer.close(indexLoad);
                        window.top.layer.msg('操作成功');
                        window.location.reload();

                    },
                    error: function (data) {
                        console.error(data);
                        layer.close(indexLoad);
                    },
                });
            }
        )
    }

    //新增绑定
    function addBindPage() {
        window.top.layer.open({
            type: 2,
            offset: 'r',
            title: '新增绑定信息',
            shadeClose: true,
            area: ['45%', '100%'],
            btn: ['确认', '取消'],
            content: "${ctx}/admin/userCert/addUserCertPage?userId=${userId}&type=${type}",
            btn1: function (index, layero) {
                var body = window.top.layer.getChildFrame('body', index);
                var iframeWin = window.top[layero.find('iframe')[0]['name']];
                iframeWin.submitCompanyUser(function () {
                    window.location.reload();
                    window.top.layer.close(index);
                });
            },
            btn2: function (index) {
                window.top.layer.close(index);
            }
        });
    }


    /**
     *  允许CA登录
     * @return
     * @author lesgod
     * @date 2020-6-23 17:07
     */
    function loginCert(id) {
        layer.confirm("是否允许该CA进行登录？",
            {
                icon: 3,
                title: '操作确认提示'
            },
            function (index) {
                layer.close(index);
                var indexLoad = layer.load();
                //点击确定回调事件
                $.ajax({
                    url: '${ctx}/admin/userCert/loginCert',
                    type: 'post',
                    cache: false,
                    async: false,
                    data: {
                        id: id
                    },
                    success: function (data) {
                        if (!isNull(data)) {
                            data = JSON.parse(data);
                        }
                        layer.close(indexLoad);
                        window.top.layer.msg('操作成功');
                        window.location.reload();

                    },
                    error: function (data) {
                        console.error(data);
                        layer.close(indexLoad);
                    },
                });
            }
        )
    }


    /**
     *  取消CA登录
     * @return
     * @author lesgod
     * @date 2020-6-23 17:07
     */
    function unLoginCert(id) {
        layer.confirm("是否取消该CA进行登录？",
            {
                icon: 3,
                title: '操作确认提示'
            },
            function (index) {
                layer.close(index);
                var indexLoad = layer.load();
                //点击确定回调事件
                $.ajax({
                    url: '${ctx}/admin/userCert/unLoginCert',
                    type: 'post',
                    cache: false,
                    async: false,
                    data: {
                        id: id
                    },
                    success: function (data) {
                        if (!isNull(data)) {
                            data = JSON.parse(data);
                        }
                        layer.close(indexLoad);
                        window.top.layer.msg('操作成功');
                        window.location.reload();

                    },
                    error: function (data) {
                        console.error(data);
                        layer.close(indexLoad);
                    },
                });
            }
        )
    }

</script>
</html>