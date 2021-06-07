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
</head>

<body>
<div>
    <h3 class="tips" style="text-align: center;padding: 2px;margin-top: 35px"></h3>
    <p style="text-align: center">(注:方便你的体验可更换谷歌,360,火狐及ie10以上等浏览器)</p>
</div>
<div class="layui-upload" style="margin-top: 35px">
    <div id="upload">
        <p style="margin-left: 100px;margin-bottom: 20px">(单个文件上传)</p>
        <button class="layui-btn layui-btn-normal" id="test8" type="button" style="margin-right: 10px;margin-left: 100px;background: rgba(13, 65, 157, 1)">选择文件</button>
    </div>
</div>
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
        fileTypes = fileTypes.replace(/\./g,"");
        fileTypes = fileTypes.replace(/\*/g,"");
        fileTypes = fileTypes.replace(/\;/g,"|");

        var textTypes = fileTypes;
        if (fileTypes.charAt(fileTypes.length - 1) == "|"){
            textTypes = fileTypes.substring(0 ,fileTypes.length-1);
        }

        $(".tips").text("上传文件类型为（"+textTypes+"）,文件大小限制为（"+ fSize +"）");
    }

    layui.use('layer', function(){
        var layer = layui.layer;
    });

    function layuiUploadInit(is_local, success){
        layui.use('upload',function () {
            var $ = layui.jquery
                ,upload = layui.upload;
            var uploadIns = upload.render({
                headers:{
                    '${_csrf.headerName}':"${_csrf.token}"
                }
                ,data: {
                    'isLocal': is_local
                }
                ,size: fileSize
                ,elem: '#test8'
                ,url: '/fdfs/ie9UploadFileByLayui?${_csrf.parameterName}=${_csrf.token}'
                ,auto: true
                ,accept: 'file'
                ,exts: fileTypes
                ,before: function () {
                   layerLoading("文件上传中请稍后...");
                }
                ,done: function(res, index, upload){
                    loadComplete();
                    window.top.layer.msg("文件上传成功！", {icon: 1, time: 1000,end:function () {
                            success(res.file.id,res.file.name);
                        }});
                }
                ,error: function () {
                    loadComplete();
                    window.top.layer.msg("文件上传失败！", {icon: 2, time: 1000});
                }
            });

        })
    }
</script>

</body>

</html>