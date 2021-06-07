<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>ie9文件上传</title>
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
    <link rel="stylesheet" href="${ctx}/plugin/uploadify/uploadify.css">
    <script type="text/javascript" src="${ctx}/plugin/uploadify/jquery.uploadify.min.js"></script>
</head>
<body>
<form>
    <div id="queue"></div>
    <input id="file_upload" name="file_upload" type="file" multiple="true">
</form>

<script>

    /***
     * 文件限制大小
     * **/
    var fileSize;
    var fileTypes;
    var numberOfFiles;
    var num = 0;
    var remark = "";

    function initUploadParam(fTypes, fSize) {
        numberOfFiles = 1;
        fileSize = fSize;
        fileTypes = fTypes;
        if (isNull(fileTypes)) {
            fileTypes = ".xls;.xlsx;.doc;.docx;*.ppt;*.pptx;*.txt;*.dwg;*.zip;*.rar;*.jpg;*.gif;*.jpeg;*.png;.bmp;*.asf;*.navi;*.avi;*.mpeg;.mpg;*.wma;*.wmv;*.rm;*.mid;*.flash;*.flv;*.mp3;*.mp4;*.3gp;*.mov;*.rmvb;*.pdf";
        }

        if (isNull(fileSize)) {
            remark = "&nbsp;&nbsp;大小限制:" + fileSize;
        } else {
            //500M
            fileSize = fSize;
            remark = "&nbsp;&nbsp;大小限制:" + fSize;
        }
        if (fSize.indexOf("M") != -1) {
            fileSize = parseInt(fSize.split("M")[0]) * 1024;
        }
        fileSize = parseInt(fileSize);
        if (!numberOfFiles) {
            numberOfFiles = 1;
        }
    }

    function uploadifyInit(is_local, success) {
        $('#file_upload').uploadify({
            uploader: "${ctx}/fdfs/ie9UploadFile?${_csrf.parameterName}=${_csrf.token}", // 文件提交地址
            swf: '${ctx}/plugin/uploadify/uploadify.swf',    // 上传使用的 Flash
            method: 'post',
            width: 80,                          // 按钮的宽度
            height: 23,                         // 按钮的高度
            buttonText: "文件上传",                 // 按钮上的文字
            buttonCursor: 'hand',                // 按钮的鼠标图标

            fileObjName: 'uploadify',            // 上传参数名称

            formData: {
                "isLocal": is_local,
                "${_csrf.headerName}": "${_csrf.token}",
            },

            overrideEvents: ['onDialogClose', 'onUploadSuccess', 'onSelectError', 'onUploadError'],   //要重写的事件
            onUploadSuccess: function (file, data, respons) {
                console.log(file.name);
                var id = $.parseJSON(data).file.id;
                console.log(id);
                if (success && typeof (success) === "function") {
                    success(id, file.name);
                }
            },
            removeCompleted: true,               // 上传成功后移除进度条
            fileSizeLimit: fileSize,                  // 文件大小限制
            onSelectError: function (file, errorCode, errorMsg) {
                var msgText = "上传失败\n";
                switch (errorCode) {
                    case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
                        //this.queueData.errorMsg = "每次最多上传 " + this.settings.queueSizeLimit + "个文件";
                        msgText += "每次最多上传 " + this.settings.queueSizeLimit + "个文件";
                        break;
                    case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
                        msgText += "文件大小超过限制( " + this.settings.fileSizeLimit + " )";
                        break;
                    case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
                        msgText += "文件大小为0";
                        break;
                    case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
                        msgText += "文件格式不正确，仅限 " + this.settings.fileTypeExts;
                        break;
                    default:
                        msgText += "错误代码：" + errorCode + "\n" + errorMsg;
                }
                layer.alert(msgText);

            },
            onUploadError: function (file, errorCode, errorMsg) {
                // Load the swfupload settings
                var settings = this.settings;

                // Set the error string
                var errorString = '上传失败';
                switch (errorCode) {
                    case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
                        errorString = '服务器错误 (' + errorMsg + ')';
                        break;
                    case SWFUpload.UPLOAD_ERROR.MISSING_UPLOAD_URL:
                        errorString = 'Missing Upload URL';
                        break;
                    case SWFUpload.UPLOAD_ERROR.IO_ERROR:
                        errorString = 'IO Error';
                        break;
                    case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
                        errorString = 'Security Error';
                        break;
                    case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
                        alert('The upload limit has been reached (' + errorMsg + ').');
                        errorString = 'Exceeds Upload Limit';
                        break;
                    case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
                        errorString = 'Failed';
                        break;
                    case SWFUpload.UPLOAD_ERROR.SPECIFIED_FILE_ID_NOT_FOUND:
                        break;
                    case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
                        errorString = 'Validation Error';
                        break;
                    case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
                        errorString = 'Cancelled';
                        this.queueData.queueSize -= file.size;
                        this.queueData.queueLength -= 1;
                        if (file.status == SWFUpload.FILE_STATUS.IN_PROGRESS || $.inArray(file.id, this.queueData.uploadQueue) >= 0) {
                            this.queueData.uploadSize -= file.size;
                        }
                        // Trigger the onCancel event
                        if (settings.onCancel) settings.onCancel.call(this, file);
                        delete this.queueData.files[file.id];
                        break;
                    case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
                        errorString = 'Stopped';
                        break;
                }
                layer.alert(errorString);
            },
            // 两个配套使用
            fileTypeExts: fileTypes,             // 扩展名
            fileTypeDesc: "请选择 " + fileTypes + " 文件",     // 文件说明

            auto: true,                // 选择之后，自动开始上传
            multi: false,               // 是否支持同时上传多个文件
            queueSizeLimit: numberOfFiles          // 允许多文件上传的时候，同时上传文件的个数
        });
    }
</script>
</body>
</html>