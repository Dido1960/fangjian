<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>新增老系统项目</title>
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
</head>
<body>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend style="font-weight: bold">新增老系统项目（文件路径需选择文件归档程序的项目目录打zip的压缩包）</legend>
</fieldset>
<form class="layui-form" method="post" id="dep-form">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">文件目录</label>
        <div class="layui-input-block" style="margin-left: 120px;width: 700px">
            <input type="text" name="bidFileName" id="bidFileName" placeholder="请上传项目压缩包"
                   lay-verify="required" lay-reqtext="请上传项目压缩包" onclick="uploadFileZip(this)" autocomplete="off" class="layui-input" readonly>
            <input type="hidden" name="bidFileId" id="bidFileId"/>
        </div>
    </div>
    <div class="layui-input-block" style="margin-left: 120px;display: none">
        <button lay-submit lay-filter="*" id="formBtnSubmit">提交</button>
    </div>
</form>
<script type="text/javascript">
    var successFunc;
    var lock = false;
    layui.use(['form', 'layer'], function () {
        var form = layui.form;
        form.on('submit(*)', function (data) {
            if (lock) {
                return false;
            }
            lock = true;
            $.ajax({
                url: '${ctx}/projectInfo/addOldProject',
                type: 'post',
                cache: false,
                async: false,
                data: $("#dep-form").serialize(),
                success: function (data) {
                    if (!isNull(data)) {
                        data = JSON.parse(data);
                    }
                    successFunc();
                },
                error: function (data) {
                    console.error(data);
                    layer.msg("添加失败！")
                },
            });
        });
        form.render();
    });

    /**
     * 招标文件上传
     */
    function uploadFileZip(e) {
        var allowType = "*.zip;*.ZIP";
        var allowFileSize = "10240M";
        if (getIEVersion() === 9){
            ie9UploadBidFileByLayuiPage(e,allowType, allowFileSize);
        }else {
            window.top.layer.open({
                type: 2,
                content: '${ctx!}/fdfs/uploadFilePage',
                title: '老系统项目文件上传(*.zip)',
                shadeClose: false,
                area: ['600px', '540px'],
                btn: ['关闭'],
                success: function (layero, index) {
                    var body = layer.getChildFrame('body', index);
                    var iframeWin = window.top[layero.find('iframe')[0]['name']];
                    iframeWin.initUploadParam(allowType, allowFileSize);
                    iframeWin.dropzoneInit("1", function (uploadFile) {
                        console.log(uploadFile.name);
                        $(e).val(uploadFile.name);
                        $(e).next("input[id='bidFileId']").val(uploadFile.id);
                        window.top.layer.close(index);
                    });
                },
                btn1: function (index) {
                    window.top.layer.close(index);
                }
            });
        }
    }

    /**
     *使用layui插件上传
     */
    function ie9UploadBidFileByLayuiPage(e, allowType, allowFileSize) {
        window.top.layer.open({
            type: 2,
            title: '老系统项目文件上传(*.zip)',
            shadeClose: false,
            area: ['600px', '540px'],
            btn: ['关闭'],
            content: '${ctx!}/fdfs/ie9UploadFileByLayuiPage',
            success: function (layero, index) {
                var body = layer.getChildFrame('body', index);
                var iframeWin = window.top[layero.find('iframe')[0]['name']];
                iframeWin.initUploadParam(allowType, allowFileSize);
                iframeWin.layuiUploadInit("1", function (id,name) {
                    $(e).val(name);
                    $(e).next("input[id='bidFileId']").val(id);
                    window.top.layer.close(index);
                });
            },
            btn1: function (index) {
                window.top.layer.close(index);
            }
        });
    }

    /**
     * 新增部门
     *
     * @param successFunc1 成功回调函数
     */
    function addDepartment(successFunc1) {
        successFunc = successFunc1;
        $("#formBtnSubmit").trigger("click");
    }
</script>
</body>
</html>