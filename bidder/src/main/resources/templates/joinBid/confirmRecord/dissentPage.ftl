<!DOCTYPE html>
<html lang="zh">

<head>
    <title>填写意异信息</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-1.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-3.1.0.min.js"></script>

    <script src="${ctx}/js/html5shiv.min.js"></script>
    <script src="${ctx}/js/respond.min.js"></script>

    <script src="${ctx}/js/common.js"></script>
    <script src="${ctx}/js/convertMoney.js"></script>
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>

    <link rel="stylesheet" href="${ctx}/css/dissent.css">

</head>

<body>
<form class="layui-form" id="dissentForm">
    <div style="clear: both;overflow: hidden">
        <label for="">附件上传（非必传）</label>
        <@UploadOneTag name="objectionFileId" allowFileSize="50M"  allowType="*.png;*.jpg" autocomplete="off" placeholder="请选择png格式附件上传"
        class="fileInput layui-input" style="float:left;" >
        </@UploadOneTag>
        <div class="uploadFileBtn" onclick="upLoadFile()">上传文件</div>
    </div>
    <div>
        <label for="">异议内容</label>
        <textarea name="message" id="message" lay-verify="message"></textarea>
    </div>
    <div class="layui-input-block" style="display: none">
        <button lay-submit lay-filter="add" id="submit">提交</button>
    </div>

    <#--  其他信息  -->
    <div style="display: none">
        <#--        <input type="hidden" name="resume" value="1">-->
        <input type="hidden" name="roleType" value="1">
        <input type="hidden" name="question" value="1">
        <input type="hidden" name="bidderId" value="${bidder.id}">
        <input type="hidden" name="bidSectionId" value="${bidder.bidSectionId}">
        <input type="hidden" name="sendName" value="${bidder.bidderName}" class="sendName">
    </div>
</form>

</body>

<script>
    var successFunc;
    layui.use('form', function () {
        var form = layui.form;
        //表单校验
        form.verify({
            message: function (value, obj) {
                if (isNull(value)) {
                    return '请输入异议内容';
                }
            }
        });
        form.on('submit(add)', function (data) {
            doLoading();
            $.ajax({
                url: '${ctx}/bidderModel/addDissent',
                type: 'post',
                cache: false,
                async: false,
                data: $("#dissentForm").serialize(),
                success: function (data) {
                    loadComplete();
                    if (data) {
                        // 添加公告
                        postMessageFun({linkType: '${bidder.bidderName}有异议：' + $('#message').val()});
                        successFunc();
                    }
                },
                error: function (data) {
                    loadComplete();
                    console.error(data);
                    layer.msg("操作失败！", {icon: 5})
                },
            });

        });

    });

    /**
     * 提交异议
     *
     * @param successFunc1 成功回调函数
     */
    function addDissent(sf) {
        successFunc = sf;
        $("#submit").trigger("click");
    }

    function upLoadFile() {
        $(".fileInput").click();
    }
</script>

</html>