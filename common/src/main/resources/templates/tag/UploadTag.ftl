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


<div>
    <div class="file_upload" onclick="uploadFile(this)">
        <input type="hidden" ${y} />
        <div class="pic"  ${x}  >
            <img src="http://61.178.200.56:8089/mygroup/M00/00/63/rBUBRl7MTCyAa3hRAAABdjMbfaU688.png">
            <input type="hidden" name="fileId">
        </div>
    </div>
</div>

<script>

    /**
     * 上传文件
     * @param e
     */
    function uploadFile(e) {

        var allowType = "*.jpg;*.png";
        var allowFileSize = "1M";
        var isUpdate = false;
        var val=$(e).find("input[name='fileId']").val();
        console.log(val)
        if (!isNull(val)) {
            isUpdate = true;
        }
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
                            $(e).html("<img src='" + data + "' width=\"160px\" height=\"160px\"><input type=\"hidden\" ${y} value='" + uploadFile.id + "'> </div>");
                            $(e).after("<div style=\" top: -10px;right: -10px;border-radius: 50%;z-index: 100;position: absolute\" onclick=\"deleteFile(this)\"><img src=\"${ctx}/img/delete.jpg\" style='border-radius: 50%;border: #EEEEEE 1px solid' width=\"30px\" height=\"30px\">");
                            if (!isUpdate) {
                                $(e).parent().append(" <div  ${x} onclick=\"uploadFile(this)\"> <div class=\"pic\" > <img src=\"${ctx}/img/upload.png\">  </div> </div>");
                            }
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
<style>
    
</style>
