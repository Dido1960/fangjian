<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>紧急签到</title>
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
    <style>
        * {
            margin: 0;
            padding: 0;
        }

        .box {
            width: 80%;
            height: 100%;
            margin: 0 auto;
        }

        .box h3 {
            width: 100%;
            height: 64px;
            font-size: 14px;
            font-family: "Microsoft YaHei";
            font-weight: bold;
            line-height: 64px;
            color: rgba(34, 49, 101, 1);
        }

        .box p {
            width: 100%;
            height: 34px;
            font-size: 14px;
            font-family: "Microsoft YaHei";
            font-weight: 400;
            line-height: 34px;
            color: rgba(34, 49, 101, 1);
        }

        .box form {
            width: 100%;
            margin-top: 30px;
        }

        form label {
            display: block;
            width: 100%;
            height: 34px;
            font-size: 14px;
            font-family: "Microsoft YaHei";
            font-weight: 400;
            line-height: 34px;
            color: rgba(34, 49, 101, 1);
        }

        form input {
            width: 100%;
            height: 40px;
            border: 1px solid rgba(213, 213, 213, 1);
            opacity: 1;
            padding: 0 15px;
            box-sizing: border-box;
        }

        input::-webkit-input-placeholder {
            height: 100%;
            color: rgba(213, 213, 213, 1);
        }

        .btn {
            width: 80px;
            height: 40px;
            opacity: 1;
            font-size: 14px;
            font-family: "Microsoft YaHei";
            font-weight: 500;
            line-height: 40px;
            text-align: center;
            border: none;
            color: rgba(255, 255, 255, 1);
            float: left;
            cursor: pointer;
            margin-left: 10px;
        }

        btn:hover {
            opacity: 0.7;
        }

        form .still {
            width: 100%;
            height: 40px;
        }

        form .still input {
            width: calc(100% - 90px);
            height: 40px;
            border: 1px solid rgba(213, 213, 213, 1);
            opacity: 1;
            outline: none;
            float: left;
            box-sizing: border-box;
            padding: 0 15px;
        }

        form .still .btn{
            width: 80px;
            height: 40px;
            margin-left: 10px;
        }

        form .still .up {
            background: linear-gradient(270deg,
            rgba(19, 97, 254, 1) 0%,
            rgba(78, 138, 255, 1) 100%);
            filter: progid:DXImageTransform.Microsoft.gradient(GradientType=1 ,startColorstr=#1361fe, endColorstr=#4e8aff);
        }

        form .still .watch {
            background: linear-gradient(270deg, #ff9100 0%, #ffb351 100%);
            filter: progid:DXImageTransform.Microsoft.gradient(GradientType=1 ,startColorstr=#FF9100, endColorstr=#FFB351);
        }
    </style>
</head>
<body>

<div class="box">
    <h3>重要提示:</h3>
    <p>1、本功能仅适用于多次人脸认证均失败的情况</p>
    <p>2、紧急签到只适用于开标前30分钟</p>
    <p>3、如需使用本功能，请授权委托人手持身份证拍实时照片以备上传</p>
    <form action="" id="urginForm" class="layui-form">
        <label for="name">企业名称</label>
        <input type="text" id="name" readonly value="${bidder.bidderName}">
        <input type="hidden" name="id" value="${bidderOpenInfo.id}">
        <input type="hidden" name="bidSectionId" value="${bidderOpenInfo.bidSectionId}">

        <label for="png">授权委托人照片(${bidderOpenInfo.clientName})</label>
        <div class="still">
            <@UploadOneTag name="sqwtsPngFileId" id="sqwtsPngFileId" isBindInput="false" allowFileSize="50M" readonly="readonly" allowType="*.jpg;*.png;"
            autocomplete="off" placeholder="请选择png或jpg格式照片" class="fileInput layui-input" verify="fileNotNull">
            </@UploadOneTag>
            <div class="btn up" onclick="upLoadThis(this)">上传文件</div>
            <span class="fileShow watch btn" style="display: none" onclick="fileShowFun(this)" >预览</span>
        </div>
        <div class="layui-input-block" style="margin-left: 120px;display: none">
            <input type="button" lay-submit lay-filter="*" id="formBtnSubmit" class="layui-btn" value="提交"/>
        </div>
    </form>
</div>

<script>
    $(function () {
        //修改上传插件的部分功能
        setTimeout(function () {
            $(".fileInput").attr("lay-verType", "tips");
            $(".fileInput").attr("name", "sqwtsFileName");
            $("#urginForm").find(".layui-icon-upload-drag").hide();
            $("#urginForm").find(".haveValueShow").remove();
        }, 400);
    });

    var successFunc;
    var lock = false;
    layui.use(['form', 'layer'], function () {
        var form = layui.form;

        form.verify({
            //委托书不可为空
            fileNotNull: function (value) {
                return fileNotNull(value);
            }
        });

        form.on('submit(*)', function (data) {
            if (lock) {
                return false;
            }
            lock = true;
            $.ajax({
                url: '${ctx}/identityAuth/updateUrgentSigin',
                type: 'post',
                cache: false,
                async: false,
                data: $("#urginForm").serialize(),
                success: function (data) {
                    if (data) {
                        layer.msg("紧急签到完成，等待核验！", {icon: 1, time: 2000, end: successFunc()});
                    } else {
                        layer.msg("开标时间已过，紧急签到失败！", {icon: 2, time: 2000, end: pageReload()});
                    }

                },
                error: function (data) {
                    console.error(data);
                    layer.msg("紧急签到失败！", {icon: 2, time: 2000});
                },
            });
        });
        form.render();
    });

    function pageReload() {
        setTimeout(function () {
            parent.$(".identityAuthLi").click();
            //先得到当前iframe层的索引
            var index = parent.layer.getFrameIndex(window.name);
            //再执行关闭
            parent.layer.close(index);
        },1000);
    }

    /**
     * @param successFunc1 成功回调函数
     */
    function updateUrgin(successFunc1) {
        successFunc = successFunc1;
        $("#formBtnSubmit").click();
    }

    //照片不可为空
    function fileNotNull(value) {
        if (value == "" || value == null) {
            return '请上传委托人照片';
        }
    }

    //文件上传按钮绑定
    var fileInterval = null;

    //文件上传按钮绑定
    function upLoadThis(obj) {
        $(obj).parent().find(".layui-icon-upload-drag").click();
        setInterval(function () {
            fileIsUpload(obj);
        }, 1000);
    }

    //判断文件是否上传完毕
    function fileIsUpload(obj) {
        if (!isNull($(obj).parent().find(".fileInput").val())) {
            $("#sqwtsPngFileId").css("width","calc(100% - 180px)");
            setTimeout(function () {
                $(".fileShow").show();
            }, 400);

            if (!isNull(fileInterval)) {
                clearInterval(fileInterval);
            }
        }else {
            $("#sqwtsPngFileId").css("width","calc(100% - 90px)");
            $(".fileShow").hide();
        }
    }

    //文件预览按钮绑定
    function fileShowFun(obj) {
        $(obj).parent().find(".showUploadFile").click();
    }
</script>
</body>
</html>