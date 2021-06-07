<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <title>添加项目</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script src="${ctx}/plugin/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${ctx}/plugin/bootstrap-3.3.7-dist/css/bootstrap.min.css">
    <!--[if lt IE 9]>
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" href="${ctx}/plugin/citypicker/city-picker.css">
    <script src="${ctx}/plugin/citypicker/city-picker.data.js"></script>
    <script src="${ctx}/plugin/citypicker/city-picker.js"></script>
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <style>
        html .layui-layer-btn .layui-layer-btn0 {
            background-color: rgba(19, 97, 254, 1);
        }

        html .layui-layer-title {
            padding: 0;
            border-bottom: 1px solid rgba(213, 213, 213, 1);
        }

        html .layui-form-select dl dd.layui-this {
            background-color: rgba(19, 97, 254, 1);
        }

        html .layui-form-item {
            width: 450px;
            margin: 25px auto 0;
        }

        html .layui-anim,
        html .layui-anim-upbit {
            height: 144px;
            overflow: auto;
        }

        html .layui-form-label {
            width: 120px;
        }
        .layui-input-block {
            margin-left: 125px;
        }

        html .layui-anim::-webkit-scrollbar,
        .layui-anim-upbit::-webkit-scrollbar {
            display: none;
        }

        .layui-form-label span {
            color: red;
        }

        html .layui-laydate .layui-this {
            background: linear-gradient(263deg, rgba(19, 97, 254, 1) 0%, rgba(78, 138, 255, 1) 100%) !important;
            filter: progid:DXImageTransform.Microsoft.gradient(GradientType=1 ,startColorstr=#1361fe, endColorstr=#4e8aff) !important;
        }

        .layui-laydate-header i:hover,
        .layui-laydate-header span:hover {
            color: rgba(19, 97, 254, 1);
        }

        .laydate-time-list li ol {
            overflow: auto;
        }

        .laydate-time-list li ol::-webkit-scrollbar {
            display: none
        }

        html .layui-layer-btn .layui-layer-btn0 {
            background-color: rgba(19, 97, 254, 1);
        }

        .layui-layer-btn,
        .layui-layer-btn-c {
            background: rgba(239, 243, 244, 1);
        }

        html .layui-form-onswitch {
            background-color: rgba(19, 97, 254, 1)
        }

        #layerDemoauto {

        }
        .hide{
            display: none;
        }
        .radio .check {
            width: 50%;
            height: 40px;
            float: left;

        }
    </style>
</head>
<body style="overflow: hidden">
    <div class="addTan">
        <form class="layui-form">
            <!-- 提示：如果你不想用form，你可以换成div等任何一个普通元素 -->
            <div class="layui-form-item">
                <input type="hidden" name="flag" id="flag" value="${RequestParameters.flag! }"/>
                <input type="hidden" name="bidSectionId" value="${RequestParameters.bidSectionId!}">
                <label class="layui-form-label">
                    项目所属地 <span>*</span>
                </label>
                <div class="layui-input-block">
                    <select id="regId" name="regId" class="selectReg" lay-filter="aihao" lay-verify="required" lay-search="" placeholder="请选择项目交易平台">
                        <option value="">请选择项目所属地</option>
                    </select>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">招标文件 <span>*</span></label>
                <div class="layui-input-block">
                    <input type="text" name="bidFileName" id="bidFileName" placeholder="请上传招标文件" lay-verify="required" lay-reqtext="请上传招标文件" onclick="uploadBidFile(this)" autocomplete="off" class="layui-input" readonly>
                    <input type="hidden" name="bidFileId" id="bidFileId"/>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">开标时间 <span>*</span></label>
                <div class="layui-input-block">
                    <input type="text" class="layui-input" placeholder="请完善开标时间" lay-verify="required" lay-reqtext="请完善开标时间" id="bidOpenTime" readonly>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">澄清文件 <span>&nbsp;</span></label>
                <div class="layui-input-block">
                    <input type="text" name="clarifyFileName" placeholder="如有澄清文件，请上传澄清文件" onclick="uploadClarifyFile(this)" autocomplete="off" class="layui-input">
                    <input type="hidden" name="clarifyFileId" id="clarifyFileId"/>
                </div>
            </div>
            <div class="layui-form-item radio" style="display: flex;">
                <div class="check">
                    <label class="layui-form-label" style="width: 120px; padding: 9px 10px; text-align: center;">是否网上开标 <span>*</span></label>
<#--                <div class="layui-input-block" style="margin:0;">-->
                    <input type="checkbox" id="openBidOnline" name="openBidOnline" value="0"  lay-skin="switch" lay-filter="openBidOnline" lay-text="是|否" >
<#--                </div>-->
                </div>
                <div class="check">
                    <label class="layui-form-label" style="width: 150px; padding: 9px 10px; text-align: center;">是否远程异地开标 <span style="float: right;">*</span></label>
<#--                <div class="layui-input-block" style="margin:0;">-->
                    <input type="checkbox" id="remoteEvaluation" name="remoteEvaluation" value="0"  lay-skin="switch" lay-filter="remoteEvaluation" lay-text="是|否" >
<#--                </div>-->
                </div>
            </div>
        </form>
    </div>
</body>
<script>
    var regs = JSON.parse('${regs}');
    var openBidOnline;
    var remoteEvaluation;
    layui.use('form', function () {
        var form = layui.form;
        var laydate = layui.laydate;
        $(this).removeAttr("lay-key");
        laydate.render({
            elem: '#bidOpenTime',
            type: 'datetime',
            trigger : 'click',
            change: function (value, date) { //监听日期被切换
            $('.laydate-main-list-0').on('click','td',function () {
                $(".laydate-btns-time").click();
            })
        }
        });

        form.on('switch(openBidOnline)', function(){
            openBidOnline = this.checked ? "1" : "0";
        });

        form.on('switch(remoteEvaluation)', function(){
            remoteEvaluation = this.checked ? "1" : "0";
        });

        form.render();
    });

    /**
     * 招标文件上传
     */
    function uploadBidFile(e) {
        var allowType = "*.gef;*.GEF";
        var allowFileSize = "1024M";
        if (getIEVersion() === 9){
            ie9UploadBidFileByLayuiPage(e,allowType, allowFileSize);
        }else {
            window.top.layer.open({
                type: 2,
                content: '${ctx!}/fdfs/uploadFilePage',
                title: '招标文件上传(*.gef)',
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
            title: '招标文件上传(*.gef)',
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
     * 澄清文件上传
     */
    function uploadClarifyFile(e) {
        var allowType = "*.pdf;*.PDF";
        var allowFileSize = "1024M";
        if (getIEVersion() === 9){
            ie9UploadClarifyFileByLayuiPage(e,allowType, allowFileSize);
        }else {
            window.top.layer.open({
                type: 2,
                content: '${ctx!}/fdfs/uploadFilePage',
                title: '澄清文件上传(*.pdf)',
                shadeClose: false,
                area: ['600px', '540px'],
                btn: ['关闭'],
                success: function (layero, index) {
                    var body = layer.getChildFrame('body', index);
                    var iframeWin = window.top[layero.find('iframe')[0]['name']];
                    iframeWin.initUploadParam(allowType, allowFileSize);
                    iframeWin.dropzoneInit(null, function (uploadFile) {
                        console.log(uploadFile.name);
                        $(e).val(uploadFile.name);
                        $(e).next("input[id='clarifyFileId']").val(uploadFile.id);
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
    function ie9UploadClarifyFileByLayuiPage(e, allowType, allowFileSize) {
        window.top.layer.open({
            type: 2,
            title: '澄清文件上传(*.pdf)',
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
                    $(e).next("input[id='clarifyFileId']").val(id);
                    window.top.layer.close(index);
                });
            },
            btn1: function (index) {
                window.top.layer.close(index);
            }
        });
    }

    /**
     * 添加项目
     */
    function parseProjectInfo(callBack) {
        var regId = $("#regId option:selected").val();
        var bidFileId = $("#bidFileId").val();
        var bidOpenTime = $("#bidOpenTime").val();
        var clarifyFileId = $("#clarifyFileId").val();
        if (isNull(regId) || isNull(bidFileId) || isNull(bidOpenTime)) {
            if (isNull(regId)) {
                $("#regId").parent().css("border","red 1px solid");
            } else{
                $("#regId").parent().css("border","#E6E6E6 1px solid");
            }

            if (isNull(bidFileId)) {
                $("#bidFileName").css("border","red 1px solid");
            } else{
                $("#bidFileName").css("border","#E6E6E6 1px solid");
            }

            if (isNull(bidOpenTime)) {
                $("#bidOpenTime").css("border","red 1px solid");
            } else{
                $("#bidOpenTime").css("border","#E6E6E6 1px solid");
            }
            reloadLayui();
            parent.layer.msg("请先完善必填项！", {icon: 2});
            return;
        }

        // 点击确认的回调函数
        var loadingIdex = parent.layer.msg('招标文件解析中，请稍后', {
            icon: 16,
            time: 0,
            shade: [0.3, '#393D49']
        });

        $.ajax({
            type: "POST",
            url: "/staff/parseProjectInfo",
            data: {
                "regId": regId,
                "bidFileId": bidFileId,
                "bidOpenTime": bidOpenTime,
                "clarifyFileId": clarifyFileId,
                "remoteEvaluation": remoteEvaluation,
                "openBidOnline": openBidOnline
            },
            success: function (result) {
                parent.layer.close(loadingIdex);
                callBack(result);
            },
            error: function (result) {
                parent.layer.alert("网络请求异常!");
            }
        });
    }

    /**
     * 重载layui
     */
    function reloadLayui() {
        layui.use('form', function () {
            var form = layui.form;

            form.render();
        });
    }


    /**
     * 修改项目
     */
    function modifyProjectInfo(callBack) {
        var regId = $("#regId option:selected").val();
        var bidFileId = $("#bidFileId").val();
        var bidOpenTime = $("#bidOpenTime").val();
        if (isNull(regId) || isNull(bidFileId) || isNull(bidOpenTime)) {
            if (isNull(regId)) {
                $("#regId").parent().css("border","red 1px solid");
            } else{
                $("#regId").parent().css("border","#E6E6E6 1px solid");
            }

            if (isNull(bidFileId)) {
                $("#bidFileName").css("border","red 1px solid");
            } else{
                $("#bidFileName").css("border","#E6E6E6 1px solid");
            }

            if (isNull(bidOpenTime)) {
                $("#bidOpenTime").css("border","red 1px solid");
            } else{
                $("#bidOpenTime").css("border","#E6E6E6 1px solid");
            }
            reloadLayui();
            parent.layer.msg("请先完善必填项！", {icon: 2});
            return;
        }

        $.ajax({
            type: "POST",
            url: "/staff/parseProjectInfo",
            data: {
                "regId": regId,
                "bidFileId": bidFileId,
                "bidOpenTime": bidOpenTime,
                "remoteEvaluation": remoteEvaluation,
                "openBidOnline": openBidOnline
            },
            success: function (result) {
                callBack(result);
            },
            error: function (result) {
                parent.layer.alert("网络请求异常!");
            }
        });
    }

    $(function () {
       $(".selectReg").html(selectReg(-1, null));
       reloadLayui();
        //修改工程  隐藏信息
        if (!isNull($('#flag').val())){
            $('.layui-form-item').eq(3).addClass("hide");
            $('.layui-form-item').eq(4).addClass("hide");
        }
    });

    /**
     * 递归排序显示行政区划
     * pid 父级id
     * ppid 父级的父级id(为null表示没有)
     */
    var bstr = "<option value=''>请选择项目所属地</option>";
    function selectReg(pid, ppid) {
        for(var i = 0; i < regs.length; i++) {
            var parentId = regs[i].parentId;
            var id = regs[i].id;

            if(parentId === pid) {
                var sapce = "";
                if (ppid === -1) {
                    sapce += "&emsp;";
                } else if (ppid >= 1){
                    sapce += "&emsp;&emsp;";
                }
                bstr += "<option value='" + id + "'>" + sapce + regs[i].regName + "</option>";
                selectReg(id, pid);
            }
        }
        return bstr;
    }
</script>

</html>