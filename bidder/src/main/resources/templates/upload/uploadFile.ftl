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
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-1.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-3.1.0.min.js"></script>
    <!--[if lt IE 9]>
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/plugin/dragzone/min/basic.min.css"/>
    <link rel="stylesheet" href="${ctx}/plugin/dragzone/min/dropzone.min.css"/>
    <script type="text/javascript" charset="UTF-8" src="${ctx}/plugin/dragzone/min/dropzone.min.js"></script>
    <style type="text/css">
        .dropzone {
            margin: 5px;
            border: 2px dashed #0087F7;
            border-radius: 5px;
            background: white;
        }
        .dropzone .pullTiltle i{
            font-size: 28px;
            color: #777;
        }
    </style>
    <script type="text/javascript" charset="UTF-8">
        Dropzone.autoDiscover = false;

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
            fileSize = parseInt(fileSize / 1024);
            if (!numberOfFiles) {
                numberOfFiles = 1;
            }

            $('#remark').html(remark);
            fileTypes = fileTypes.replace(/\*/g, "");
            fileTypes = fileTypes.replace(/;/g, ",");
        }

        /***
         * 上传成功后执行
         * **/
        function UploadSuccess(callback) {
            if (callback && typeof (callback) === "function") {
                callback();
            }
        }

        /**
         * 上传前执行
         *
         * **/
        function uploadPre() {

        }

        /**
         * 上传进度信息
         * **/
        function uploadProgress() {

        }

        /***
         * 上传函数
         * **/
        function dropzoneInit(is_local, success) {
            Dropzone.autoDiscover = false;
            $("#myDropzone").dropzone({
                url: "/fdfs/uploadFile?isLocal=" + is_local, // 文件提交地址
                method: "post",  // 也可用put
                timeout: '3600000',
                clickable: true,
                paramName: "uploadify", // 默认为file
                maxFiles: numberOfFiles + "",// 一次性上传的文件数量上限
                maxFilesize: fileSize + "", // 文件大小，单位：MB
                acceptedFiles: fileTypes, // 上传的类型
                addRemoveLinks: true,
                parallelUploads: 1,// 一次上传的文件数量
                //previewsContainer:"#preview", // 上传图片的预览窗口
                dictDefaultMessage: '<span class="pullTiltle"><i class="layui-icon layui-icon-upload"><i>拖动文件至此或者点击上传<span>',
                dictMaxFilesExceeded: "您最多只能上传" + numberOfFiles + "个文件！",
                dictResponseError: '文件上传失败!',
                dictInvalidFileType: "文件类型只能是" + fileTypes + "格式",
                dictFallbackMessage: "浏览器不受支持",
                dictRemoveFile: '删除',
                dictCancelUpload: '取消',
                headers: {
                    "${_csrf.headerName}": "${_csrf.token}"
                },
                dictFileTooBig: "文件过大上传文件最大支持.",
                init: function () {
                    this.on("addedfile", function (file) {
                        // 上传文件时触发的事件
                    });
                    this.on("success", function (file, data) {
                        // 上传成功触发的事件
                        var rs = data;
                        // var str = "<div class='fileIdsEnd' fileDel='" + data.deleteUrl + "'>";
                        // str = str + "<input type=\"hidden\" name=\"resultId\" id=\"resultId\" value=\"" + rs.id + "\"><input type=\"hidden\" name=\"resultName\" id=\"resultName\" value=\"" + rs.name + "\"><input type=\"hidden\" name=\"filePath\" id=\"filePath\" value=\"" + rs.path + "\">";
                        // str = str + "</div>"
                        // $("#result").append(str);
                        var size = (Math.round(rs.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
                        if (success && typeof (success) === "function") {
                            success(rs.file);
                        }
                    });
                    this.on("error", function (file, data) {
                        // 上传失败触发的事件
                        $("#myDropzone .dz-error-message").html("文件上传失败，请重试！");
                        layer.msg("上传失败！")
                    });
                    this.on("removedfile", function (file) {

                    });
                    this.on("uploadprogress", function (file, progress) {
                        console.log(progress.toFixed(2) + "%");
                        $("#remark").html(remark + "&nbsp;&nbsp;当前进度：" + (progress - 0.01).toFixed(2) + "%")
                        renderProgressBar((progress - 0.01).toFixed(2) + "%");
                    })
                }
            });
        }

        /**
         *
         * **/
        function bindDetele() {
            $(".dz-remove").on("click", function (e) {
                var index_count = 0;
                var bind = $(this);
                $(".dz-remove").each(function (index) {
                    if (bind.is($(this))) {
                        index_count = index;
                    }
                });
                delFile($("#result .fileIdsEnd").eq(index_count).attr("delFile"))
                $("#result .fileIdsEnd").eq(index_count).remove();
            });
        }

        function delFile(delUrl, fileId) {

            num--;

        }

        function renderProgressBar(percent) {
            $(".layui-progress-bar").html(percent)
            layui.use('element', function () {
                var element = layui.element;
                element.progress('progress_filter', percent);
            });
        }
    </script>
</head>
<body>
<div class="layui-progress" style="height: 2px !important;" lay-showPercent="yes" lay-filter="progress_filter">
    <div class="layui-progress-bar  layui-bg-green" style="height: 2px !important;color: #00B83F"></div>
</div>
<div id="remark" style="margin: 5px; font-size: 14px; color: red"></div>
<div id="myDropzone" class="dropzone" style="margin-top: 20px">
</div>
<div id="result"></div>
</body>
</html>
