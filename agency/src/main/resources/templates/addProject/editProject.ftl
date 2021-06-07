<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <title>修改项目</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
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
    <link rel="stylesheet" href="${ctx}/css/editProject.css">
</head>
<body style="overflow: hidden">
<div class="addTan">
    <form class="layui-form">
        <div class="layui-form-item">
            <label class="layui-form-label">
                项目所属地 <span>*</span>
            </label>
            <div class="layui-input-block">
                <select id="regId" name="regId" class="selectReg" lay-filter="aihao" lay-verify="required" lay-search=""
                        placeholder="请选择项目交易平台">
                    <option value="">请选择项目所属地</option>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">招标文件 <span>*</span></label>
            <div class="layui-input-block">
                <input type="text" name="bidFileName" id="bidFileName" placeholder="请上传招标文件" lay-verify="required"
                       lay-reqtext="请上传招标文件" onclick="uploadBidFile(this)" autocomplete="off" class="layui-input"
                       readonly>
                <input type="hidden" name="bidFileId" id="bidFileId"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">开标时间 <span>*</span></label>
            <div class="layui-input-block">
                <input type="text" class="layui-input" placeholder="请完善开标时间" lay-verify="required" lay-reqtext="请完善开标时间"
                       id="bidOpenTime" readonly value="${tenderDoc.bidOpenTime}">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">文件递交时间 <span>*</span></label>
            <div class="layui-input-block">
                <input type="text" class="layui-input" placeholder="请完善文件递交时间" lay-verify="required"
                       lay-reqtext="请完善文件递交时间" id="bidDocReferEndTime" readonly value="${tenderDoc.bidDocReferEndTime}">
            </div>
        </div>
    </form>
</div>
</body>
<script>
    var regs = JSON.parse('${regs}');
    $(function () {
        $(".selectReg").html(selectReg(-1, null));
        reloadLayui();
    });

    layui.use('form', function () {
        var form = layui.form;
        var laydate = layui.laydate;
        $(this).removeAttr("lay-key");
        laydate.render({
            elem: '#bidOpenTime',
            type: 'datetime',
            trigger: 'click',
            change: function (value, date) { //监听日期被切换
                $('.laydate-main-list-0').on('click', 'td', function () {
                    $(".laydate-btns-time").click();
                })
            }
        });
        laydate.render({
            elem: '#bidDocReferEndTime',
            type: 'datetime',
            trigger: 'click',
            change: function (value, date) { //监听日期被切换
                $('.laydate-main-list-0').on('click', 'td', function () {
                    $(".laydate-btns-time").click();
                })
            }
        });
        form.render();
    });

    /**
     * 招标文件上传
     */
    function uploadBidFile(e) {
        var allowType = "*.gef;*.GEF";
        var allowFileSize = "500M";
        window.top.layer.open({
            type: 2,
            content: '${ctx!}/fdfs/uploadFilePage',
            title: '招标文件上传(*.gef)',
            shadeClose: true,
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

    /**
     * 修改项目
     */
    function modifyProjectInfo(callBack) {
        var regId = $("#regId option:selected").val();
        var bidFileId = $("#bidFileId").val();
        var bidOpenTime = $("#bidOpenTime").val();
        var bidDocReferEndTime = $("#bidDocReferEndTime").val();
        //修改方式判断(0:只修改时间 1：做了文件修改 2:数据为空)
        var flag = 0;
        if (isNull(regId) && isNull(bidFileId) || ('${bidSection.regId??}' && '${bidSection.regId}' == regId)) {
            //只做时间修改
            if (isNull(bidOpenTime)) {
                flag = 2;
                $("#bidOpenTime").css("red", "#E6E6E6 1px solid");
            } else {
                $("#bidOpenTime").css("border", "#E6E6E6 1px solid");
            }
            if (isNull(bidDocReferEndTime)) {
                flag = 2;
                $("#bidDocReferEndTime").css("red", "#E6E6E6 1px solid");
            } else {
                $("#bidDocReferEndTime").css("border", "#E6E6E6 1px solid");
            }
        } else {
            //修改区划 上传文件
            if (isNull(regId)) {
                flag = 2;
                $("#regId").parent().css("border", "red 1px solid");
            } else {
                $("#regId").parent().css("border", "#E6E6E6 1px solid");
            }
            if (isNull(bidFileId)) {
                flag = 2;
                $("#bidFileName").parent().css("border", "red 1px solid");
            } else {
                $("#bidFileName").parent().css("border", "#E6E6E6 1px solid");
            }
            if (isNull(bidOpenTime)) {
                flag = 2;
                $("#bidOpenTime").css("red", "#E6E6E6 1px solid");
            } else {
                $("#bidOpenTime").css("border", "#E6E6E6 1px solid");
            }
            if (isNull(bidDocReferEndTime)) {
                flag = 2;
                $("#bidDocReferEndTime").css("red", "#E6E6E6 1px solid");
            } else {
                $("#bidDocReferEndTime").css("border", "#E6E6E6 1px solid");
            }
            if (flag != 2) {
                flag = 1;
            }
        }
        //修改方式判断
        if (flag == 2) {
            reloadLayui();
            parent.layer.msg("请先完善必填项！", {icon: 2});
            return;
        } else if (flag == 1) {
            //文件修改
            editProjectFile(regId, bidFileId, bidOpenTime, bidDocReferEndTime, callBack);
        } else {
            //时间修改
            editProjectTime(bidOpenTime, bidDocReferEndTime, callBack);
        }
    }

    //项目 文件修改
    function editProjectFile(regId, bidFileId, bidOpenTime, bidDocReferEndTime, callBack) {
        if (new Date(bidDocReferEndTime.replace(/-/g, '/')).getTime() > new Date(bidOpenTime.replace(/-/g, '/')).getTime()) {
            layer.msg("投标文件递交时间应该在开标时间之前!", {icon: 5});
            return;
        }
        // 点击确认的回调函数
        layerLoading('招标文件解析中，请稍后');
        $.ajax({
            type: "POST",
            url: "/staff/parseProjectInfo",
            data: {
                "regId": regId,
                "bidFileId": bidFileId,
                "bidOpenTime": bidOpenTime,
                "bidDocReferEndTime": bidDocReferEndTime,
                "remoteEvaluation": '${bidSection.remoteEvaluation}',
                "openBidOnline": '${bidSection.bidOpenOnline}'
            },
            success: function (result) {
                callBack(result);
            },
            error: function (result) {
                parent.layer.alert("网络请求异常!");
            }
        });
    }

    //项目 时间修改
    function editProjectTime(bidOpenTime, bidDocReferEndTime, callBack) {
        if (new Date(bidDocReferEndTime.replace(/-/g, '/')).getTime() > new Date(bidOpenTime.replace(/-/g, '/')).getTime()) {
            layer.msg("投标文件递交时间应该在开标时间之前!", {icon: 5});
            return;
        }
        $.ajax({
            type: "POST",
            url: "/staff/editProjectTime",
            data: {
                "id": ${tenderDoc.id},
                "bidOpenTime": bidOpenTime,
                "bidDocReferEndTime": bidDocReferEndTime
            },
            success: function (result) {
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
     * 递归排序显示行政区划
     * pid 父级id
     * ppid 父级的父级id(为null表示没有)
     */
    var bstr = "<option value='' disabled>请选择项目所属地</option>";

    function selectReg(pid, ppid) {
        for (var i = 0; i < regs.length; i++) {
            var parentId = regs[i].parentId;
            var id = regs[i].id;

            if (parentId === pid) {
                var sapce = "";
                if (ppid === -1) {
                    sapce += "&emsp;";
                } else if (ppid >= 1) {
                    sapce += "&emsp;&emsp;";
                }
                if ('${bidSection.regId??}' && '${bidSection.regId}' == id) {
                    bstr += "<option value='" + id + "' selected>" + sapce + regs[i].regName + "</option>";
                } else {
                    bstr += "<option value='" + id + "'>" + sapce + regs[i].regName + "</option>";
                }

                selectReg(id, pid);
            }
        }
        return bstr;
    }
</script>

</html>