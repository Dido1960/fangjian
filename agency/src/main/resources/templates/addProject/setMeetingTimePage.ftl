<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <title>设置复会时间</title>
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
    <link rel="stylesheet" href="${ctx}/css/utils.css">
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
            width: 125px;
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

    </style>
</head>
<body style="overflow: hidden">
<div class="addTan">
    <form class="layui-form">
        <div class="layui-form-item">
            <label class="layui-form-label">
                标段名称
            </label>
            <div class="layui-input-block">
                <input type="text" disabled value="${bidSection.bidSectionName}"
                       class="layui-input layui-bg-gray"
                       readonly>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">
                标段编号
            </label>
            <div class="layui-input-block">
                <input type="text"  lay-verify="required"  disabled value="${bidSection.bidSectionCode}"
                        class="layui-input layui-bg-gray"
                       readonly>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">复会时间 <span>*</span></label>
            <div class="layui-input-block">
                <input type="text" class="layui-input" placeholder="请完善复会时间" lay-verify="required" lay-reqtext="请完善复会时间"
                       id="resumeTime" readonly value="${bidSection.resumeTime}">
            </div>
        </div>
    </form>
</div>
</body>
<script>
    var nowtime = new Date();
    var year = timeAdd0(nowtime.getFullYear().toString());
    var month = timeAdd0((nowtime.getMonth() + 1).toString());
    var day = timeAdd0(nowtime.getDate().toString());
    // 当前时间字符串
    var currentTime = year+'-'+month+'-'+day;

    layui.use('form', function () {
        var form = layui.form;
        var laydate = layui.laydate;
        $(this).removeAttr("lay-key");
        laydate.render({
            elem: '#resumeTime',
            type: 'datetime',
            trigger: 'click',
            min: currentTime,
            change: function (value, date) { //监听日期被切换
                $('.laydate-main-list-0').on('click', 'td', function () {
                    $(".laydate-btns-time").click();
                })
            }
        });
        form.render();
    });

    /**
     * 修改复会时间R
     */
    function modifyResumeTime(callBack) {
        var resumeTime = $("#resumeTime").val();
        editResumeTime(resumeTime,callBack);
    }


    //项目 时间修改
    function editResumeTime(resumeTime,callBack) {
        $.ajax({
            type: "POST",
            url: "${ctx}/staff/bidSection/updateBidSection",
            data: {
                "id": ${bidSection.id},
                "resumeTime": resumeTime,
            },
            success: function (result) {
                callBack(result);
            },
            error: function (result) {
                parent.layer.alert("网络请求异常!");
            }
        });
    }

</script>

</html>