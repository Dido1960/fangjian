<#--单位上传标签文件上传-->
<#assign x = "">
<#assign y = "">
<#if id??>
    <#assign x = x + " id=\"" + id + "\"">
</#if>
<#if class??>
    <#assign x = x + " class=\"" + class + "\"">
</#if>
<#if style??>
    <#assign x = x + " style=\"" + style + " ;padding-right:10px\"">
</#if>
<#if reqtext??>
    <#assign x = x + " lay-reqtext=\"" + reqtext + "\"">
</#if>
<#if readonly??>
    <#assign x = x + " readonly=\"" + readonly + "\"">
</#if>
<#if disabled??>
    <#assign x = x + " disabled=\"" + disabled + "\"">
</#if>
<#if verify??>
    <#assign x = x + " lay-verify=\"" + verify + "\"">
</#if>
<#if placeholder??>
    <#assign x = x + " placeholder=\"" + placeholder + "\"">
</#if>
<#if isLocal??>
    <#assign x = x + " is-local-cache=\"" + isLocal + "\"">
</#if>
<#--隐藏域需要的-->
<#if name??>
    <#assign y = y + " name=\"" + name + "\"">
</#if>
<#if value??>
    <#assign y = y + " value=\"" + value + "\"">
</#if>
<#if allowType>
    <#assign y = y + " allow_type=\"" + allowType + "\"">
</#if>
<#if allowFileSize>
    <#assign y = y + " allow_file_size=\"" + allowFileSize + "\"">
</#if>

<span class="upload_one_form">
    <input type="hidden" ${y} />
    <input   ${x}  <#if !(isBindInput??) || isBindInput == 'true' || isBindInput == '1'> onclick="tagUploadFileOne(this)"</#if> "/>
    <span class="layui-icon layui-icon-upload-drag" onclick="tagUploadFileOne(this)"
          style="position: absolute;float: right;right: 5px;top:10px"></span>
    <span class="layui-icon layui-icon-close-fill haveValueShow" onclick="tagUploadFileClear(this)"
          style="position: absolute;float: right;right: 25px;top:10px"></span>
      <span class="layui-icon layui-icon-screen-full haveValueShow" onclick="tagUploadFileDown(this)"
            style="position: absolute;float: right;right: 45px;top:10px"></span>
    <span class="layui-icon layui-icon-file layui-hide showUploadFile" onclick="showUploadFile(this)"
          style="position: absolute;float: right;right: 45px;top:10px"></span>
</span>
<script>

    /**
     * 单文件上传
     * @return
     * @author lesgod
     * @date 2020/5/14 18:08
     */
    function tagUploadFileOne(e) {
        var $upload_space = $(e).parents(".upload_one_form");
        var is_local = $(e).attr("is-local-cache");
        if (isNull(is_local)) {
            is_local = "";
        }
        var allowType = $upload_space.find("input").eq(0).attr("allow_type");
        var allowFileSize = $upload_space.find("input").eq(0).attr("allow_file_size");
        // add('fileId','fileName','*.mtjy;','900MB','false');
        if (isNull(allowType)) {
            allowType = "*";
        }
        if (isNull(allowFileSize)) {
            allowFileSize = "500M";
        }
        if (getIEVersion() === 9){
            // ie9UploadFilePage($upload_space,allowType, allowFileSize, is_local);
            ie9UploadFileByLayuiPage($upload_space,allowType, allowFileSize, is_local);
        }else {
            window.top.layer.open({
                type: 2,
                title: '单附件上传',
                shadeClose: false,
                area: ['30%', '50%'],
                btn: ['取消'],
                content: '${ctx!}/fdfs/uploadFilePage',
                success: function (layero, index) {
                    var body = layer.getChildFrame('body', index);
                    var iframeWin = window.top[layero.find('iframe')[0]['name']];
                    iframeWin.initUploadParam(allowType, allowFileSize);
                    iframeWin.dropzoneInit(is_local, function (uploadFile) {
                        console.log(uploadFile.id);
                        console.log(uploadFile.name);
                        $upload_space.find("input").eq(0).val(uploadFile.id);
                        $upload_space.find("input").eq(1).val(uploadFile.name);
                        $upload_space.find(".haveValueShow").show();
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
     *使用uploadifiy插件上传
     */
    function ie9UploadFilePage($upload_space, allowType, allowFileSize, is_local) {
        window.top.layer.open({
            type: 2,
            title: '单附件上传',
            shadeClose: false,
            area: ['30%', '50%'],
            btn: ['取消'],
            content: '${ctx!}/fdfs/ie9UploadFilePage',
            success: function (layero, index) {
                var body = layer.getChildFrame('body', index);
                var iframeWin = window.top[layero.find('iframe')[0]['name']];
                iframeWin.initUploadParam(allowType, allowFileSize);
                iframeWin.uploadifyInit(is_local, function (id,name) {
                    console.log(id);
                    console.log(name);
                    $upload_space.find("input").eq(0).val(id);
                    $upload_space.find("input").eq(1).val(name);
                    $upload_space.find(".haveValueShow").show();
                    window.top.layer.close(index);
                });
            },
            btn1: function (index) {
                window.top.layer.close(index);
            }
        });
    }

    /**
     *使用layui插件上传
     */
    function ie9UploadFileByLayuiPage($upload_space, allowType, allowFileSize, is_local) {
        window.top.layer.open({
            type: 2,
            title: '单附件上传',
            shadeClose: false,
            area: ['30%', '50%'],
            btn: ['取消'],
            content: '${ctx!}/fdfs/ie9UploadFileByLayuiPage',
            success: function (layero, index) {
                var body = layer.getChildFrame('body', index);
                var iframeWin = window.top[layero.find('iframe')[0]['name']];
                iframeWin.initUploadParam(allowType, allowFileSize);
                iframeWin.layuiUploadInit(is_local, function (id,name) {
                    console.log(id);
                    console.log(name);
                    $upload_space.find("input").eq(0).val(id);
                    $upload_space.find("input").eq(1).val(name);
                    $upload_space.find(".haveValueShow").show();
                    window.top.layer.close(index);
                });
            },
            btn1: function (index) {
                window.top.layer.close(index);
            }
        });
    }


    /**
     *  文件上传结束事件
     * @return
     * @author lesgod
     * @date 2020/5/14 17:18
     */
    function uploadEnd() {

    }

    /**
     *  删除上传文件
     * @return
     * @author lesgod
     * @date 2020/5/14 16:41
     */
    function tagUploadFileClear(e) {
        layer.confirm("是否清空上传的文件？",
            {
                icon: 3,
                title: '操作确认提示'
            },
            function (index) {
                //点击确定回调事件
                var uploadFileId = $(e).parents(".upload_one_form").find("input[type='hidden']").val();
                $(e).parents(".upload_one_form").find("input").val("");
                layer.close(index);
                $(e).parents(".upload_one_form").find(".haveValueShow").hide();
            }
        );

    }


    /**
     *  上传文件下载功能
     * @return
     * @author lesgod
     * @date 2020/5/15 13:54
     */
    function tagUploadFileDown(e) {
        var fileId = $(e).parents(".upload_one_form").find("input[type='hidden']").val();
        if (!isNull(fileId)) {
            $.ajax({
                url: '/fdfs/uploadFileDown',
                type: 'post',
                cache: false,
                async: false,
                data: {
                    id: fileId
                },
                success: function (data) {
                    if (!isNull(data)) {
                        previewFile(data);
                        //window.open(data);
                    }
                },
                error: function (data) {
                    console.error(data);
                    layer.msg("网络异常");
                },
            });
        }
    }

    /**
     *  上传文件预览功能 （通过预览服务器预览）
     * @author lgq
     */
    function showUploadFile(e) {
        var fileId = $(e).parents(".upload_one_form").find("input[type='hidden']").val();
        if (!isNull(fileId)) {
            $.ajax({
                url: '/fdfs/showUploadFile',
                type: 'post',
                cache: false,
                async: false,
                data: {
                    id: fileId
                },
                success: function (url) {
                    window.top.layer.open({
                        type: 2,
                        title: '文件预览',
                        shadeClose: true,
                        shade: 0.8,
                        move: false,
                        resize: false,
                        scrollbar: true,
                        area: ['80%', '80%'],
                        content: url
                    });
                },
                error: function (data) {
                    console.error(data);
                    layer.msg("网络异常");
                },
            });
        }
    }


    $(function () {
        //初始化值
        initBaseReordData();
    })

    var tag_upload_flag=false;

    /**
     *
     * @return
     * @author lesgod
     * @date 2020/5/15 10:28
     */
    function initBaseReordData() {
        if(tag_upload_flag){
            return;
        }
        tag_upload_flag=true;
        $(".upload_one_form input[type='hidden']").each(function () {
            var $_input=$(this);
            var fileId = $(this).val();
            if (!isNull(fileId)) {
                $.ajax({
                    url: '/fdfs/getUploadFile',
                    type: 'post',
                    cache: false,
                    async: false,
                    data: {
                        id: fileId
                    },
                    success: function (data) {
                        if (!isNull(data)) {
                            $_input.parents(".upload_one_form").find(".haveValueShow").show();
                            $_input.parents(".upload_one_form").find("input").eq(1).val(data.name);
                        }
                    },
                    error: function (data) {
                        console.error(data);
                        layer.msg("网络异常");
                    },
                });
            }
        })



    }
</script>
<style>
    .haveValueShow {
        display: none;
    }
</style>
