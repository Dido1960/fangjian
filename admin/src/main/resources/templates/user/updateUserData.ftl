<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>人员修改</title>
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
     <script src="${ctx}/js/common.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
</head>
<body>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend>个人资料</legend>
</fieldset>
<form class="layui-form" method="post" id="form1" enctype="multipart/form-data">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <input type="hidden" name="id" value="${userInfo.id}">
    <div class="layui-form-item">
        <label class="layui-form-label">用户头像</label>
        <div class="layui-input-block" style="margin-left: 120px" id="user_img">
            <#if userInfo.url!>
                <img src="${userInfo.url}" width="100px" height="100px">
            <#else >
                <img src="http://61.178.200.56:8089/mygroup/M00/00/62/rBUBRl7EfAOAHU9RAAKkyX5DntI431.jpg" width="100px"
                     height="100px">
            </#if>
        </div>
    </div>
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">用户名称</label>
        <div class="layui-input-block" style="margin-left: 120px;margin-right: 15px">
            <input type="text" name="name" id="user-name" value="${userInfo.name}" lay-verify="required"
                   autocomplete="off" placeholder="请输入用户名称" class="layui-input layui-bg-gray" readonly>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">登录名</label>
        <div class="layui-input-block" style="margin-left: 120px;margin-right: 15px">
            <input type="text" name="loginName" id="login-name" value="${userInfo.loginName}" lay-verify="required"
                   placeholder="请输入登录名" autocomplete="off" class="layui-input layui-bg-gray" readonly>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">手机号码</label>
        <div class="layui-input-block" style="margin-left: 120px;margin-right: 15px">
            <input type="text" name="phone" id="phone" value="${userInfo.phone!}"
                   lay-verify="required" autocomplete="off" placeholder="请输入手机号码" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">上传头像</label>
        <input type="hidden" id="user_file_id" name="userFileId" value="${file.id}">
        <button class="layui-btn" id="preview_img" type="button" onclick="upload()">
            上传图片
        </button>
    </div>
    </div>


</form>
<script type="text/javascript">


    layui.use('form', 'layer', 'upload', function () {
        var form = layui.form;
        form.render();
    });
    // 选择角色弹窗
    var choose_role_index;


    /**
     * 修改人员信息
     *
     * @param successFunc 成功回调函数
     */
    function updateUser(successFunc) {
        var user_name = $("#user-name").val();
        var login_name = $("#login-name").val();


        if (!user_name) {
            window.top.layer.msg("用户名称不能为空！", {icon: 2})
        } else if (!login_name) {
            window.top.layer.msg("登录名不能为空！", {icon: 2})
        } else {

            $.ajax({
                url: '${ctx}/user/updateUserData',
                dataType: "json",
                type: "post",
                cache: false,
                async: false,
                data: $("#form1").serialize(),

                success: function (data) {
                    if (data) {
                        window.top.layer.msg("修改成功！", {icon: 1, time: 2000, end: successFunc})
                    } else {
                        window.top.layer.msg("修改失败！", {icon: 2, time: 2000})
                    }
                }
            })
        }
    }

    /**
     * 头像上传
     */
    function upload() {
        var allowType = "*.jpg;*.png";
        var allowFileSize = "1M";
        window.top.layer.open({
            type: 2,
            title: '头像',
            shadeClose: true,
            area: ['30%', '50%'],
            btn: ['取消'],
            content: '${ctx!}/fdfs/uploadFilePage',
            success: function (layero, index) {
                var body = layer.getChildFrame('body', index);
                var iframeWin = window.top[layero.find('iframe')[0]['name']];
                iframeWin.initUploadParam(allowType, allowFileSize);
                iframeWin.dropzoneInit(function (uploadFile) {
                    console.log(uploadFile.id);
                    $("#user_file_id").val(uploadFile.id);
                    console.log(uploadFile.name);

                    window.top.layer.close(index);
                    $.ajax({
                        url: "${ctx!}/fdfs/uploadFileDown",
                        type: "post",
                        dataType: "text",
                        data: {id: uploadFile.id},
                        cache: false,
                        async: false,
                        success: function (data) {
                            $("#user_img img").prop("src", data);
                        }, error: function () {
                            console.log('失败。。。')
                        }
                    })
                });

            },
            btn1: function (index) {
                window.top.layer.close(index);
            }
        });

    }
</script>
</body>
</html>