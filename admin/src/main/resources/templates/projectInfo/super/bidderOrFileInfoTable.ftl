<html>
<head>
    <meta charset="utf-8">
    <title>报名信息查询</title>
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
    <link rel="stylesheet" href="${ctx}/layuiAdmin/opTable/opTable.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script src='${ctx}/layuiAdmin/opTable/opTable.js'></script>
    <style>
        html .green-s {
            color: rgba(1, 176, 120, 1);
            cursor:pointer;
        }
        html .red-s {
            color: rgba(229, 70, 20, 1);
            cursor:pointer;
        }
        html .gray-s {
            color: darkgray;
            cursor:pointer;
        }
        .layui-table-cell .layui-form-checkbox[lay-skin=primary] {
            top: 5px!important;
        }
    </style>
    <script type="text/html" id="bid-classify-code-table">
        {{# if(d.bidClassifyCode == 'A03') { }}
        勘察
        {{# } else if(d.bidClassifyCode == 'A04') { }}
        设计
        {{# } else if(d.bidClassifyCode == 'A05') { }}
        监理
        {{# } else if(d.bidClassifyCode == 'A08') { }}
        施工
        {{# } else if(d.bidClassifyCode == 'A10') { }}
        资格预审
        {{# } else if(d.bidClassifyCode == 'A11') { }}
        电梯采购与安装
        {{# } else if(d.bidClassifyCode == 'A12') { }}
        工程总承包
        {{# } else { }}
        其他
        {{# } }}
    </script>
    <script type="text/html" id="open-status-table">
        {{# if(d.bidOpenStatus == 1) { }}
            <span class="green-s" onclick="viewTime(this, '1', '{{ d.bidOpenTime }}', '{{ d.bidOpenEndTime }}')">进行中</span>
        {{# } else if(d.bidOpenStatus == 2) { }}
            <span class="red-s" onclick="viewTime(this, '1', '{{ d.bidOpenTime }}', '{{ d.bidOpenEndTime }}')">结束</span>
        {{# } else { }}
            <span class="gray-s" onclick="viewTime(this, '1', '{{ d.bidOpenTime }}', '{{ d.bidOpenEndTime }}')">未开始</span>
        {{# } }}
    </script>
    <script type="text/html" id="eval-status-table">
        {{# if(d.evalStatus == 1) { }}
            <span class="green-s" onclick="viewTime(this, '2', '{{ d.evalStartTime }}', '{{ d.evalEndTime }}')">进行中</span>
        {{# } else if(d.evalStatus == 2) { }}
            <span class="red-s" onclick="viewTime(this, '2', '{{ d.evalStartTime }}', '{{ d.evalEndTime }}')">结束</span>
        {{# } else { }}
            <span class="gray-s" onclick="viewTime(this, '2', '{{ d.evalStartTime }}', '{{ d.evalEndTime }}')">未开始</span>
        {{# } }}
    </script>
    <script type="text/html" id="barOperate">
        {{# if(!(isNull(d.bidOpenStatus) || d.bidOpenStatus == 0)) { }}
        <a class="layui-btn layui-btn-sm" lay-event="edit" onclick="downFiles('{{=d.id}}')">招投标文件下载</a>
        {{# } }}
        {{# if(!isNull(d.bidSectionRelate.evaluationReportId) && d.evalStatus == 1 && d.bidOpenStatus == 2) { }}
            <a class="layui-btn layui-btn-sm" lay-event="edit" onclick="endEvaluation('{{=d.id}}')">结束评标</a>
        {{# } }}
        {{# if(d.dataFrom == 1) { }}
        <a class="layui-btn layui-btn-sm" lay-event="edit" onclick="pushOpenResult('{{=d.id}}')">开标结果推送</a>
        <a class="layui-btn layui-btn-sm" lay-event="edit" onclick="pushEvalResult('{{=d.id}}')">评标结果推送</a>
        {{# } }}
    </script>
    <script type="text/html" id="sign-in-status-table">
        {{# if(isNull(d.bidderOpenInfo.signinTime) || d.bidderOpenInfo.signinTime == '') { }}
        未签到
        {{# } else { }}
        已签到 (<b class="red-s">签到时间：{{d.bidderOpenInfo.signinTime}}</b>)
        {{# } }}
    </script>
    <script type="text/html" id="submit-file-status-table">
        {{# if(isNull(d.bidDocId) || d.bidDocId == '') { }}
        未递交
        {{# } else { }}
        已递交 {{# if(!(isNull(d.upfileTime) || d.upfileTime == '')) { }}
                (<b class="red-s">递交时间：{{d.bidderOpenInfo.upfileTime}}</b>)
            {{# } }}
        {{# } }}
    </script>
</head>
<body class="layui-layout-body">
<div id="LAY_app">
    <div class="layui-layout layui-layout-admin">
        <div class="layui-card">
            <form class="layui-form">
                <input type="hidden" id="regId" name="regId" value="${reg.regNo}"/>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">所属地区</label>
                        <div class="layui-input-block">
                            <input type="text" name="regName" value="${reg.regName}" autocomplete="off"
                                   class="layui-input" disabled>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">标段类型</label>
                        <div class="layui-input-block">
                            <@WordBookTag key="bidClassifyCode" name="bidClassifyCode" id="bidClassifyCode" please="t"></@WordBookTag>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">关键字</label>
                        <div class="layui-input-block">
                            <input type="text" name="search" id="search" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-input-block">
                            <button type="reset" class="layui-btn layui-btn-primary" id="reset">重置</button>
                            <button type="button" class="layui-btn layui-btn-normal" onclick="startSearch()">查询</button>
                        </div>
                    </div>
                </div>
            </form>
            <table class="layui-hide" id="project-info" lay-filter="operate">
                <script type="text/html" id="orderNum">
                    {{d.LAY_TABLE_INDEX+1}}
                </script>
                <script type="text/html" id="project-info-toolbar">
                </script>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript">
    // layui-table
    var tableIns;

    /**
     * 重载table
     *
     * @param layer_index 需要关闭的layer
     */
    function renderTable() {
        $("#reset").trigger("click");
        startSearch();
    }
    /**
     * 查询
     */
    function startSearch() {
        var search = $("#search").val();
        var regId = $("#regId").val();
        var bidClassifyCode = $('#bidClassifyCode option:selected').val();

        layui.table.reload('project-info', {
            where: {
                bidSectionName: encodeURI(search),
                bidSectionCode: encodeURI(search),
                bidClassifyCode: bidClassifyCode,
                regId: regId
            },
            page: {
                curr: 1
            }
        });
    }

</script>
<script>
    layui.config({
        base: '${ctx}/layuiAdmin/opTable'
    }).extend({
        opTable: '/opTable'
    });

    $(function () {
        tableIns = layui.opTable.render({
            height: 'full-110',
            elem: '#project-info',
            url: '${ctx}/projectInfo/pagedProjectInfo?regId=${reg.id}',
            toolbar: '#project-info-toolbar',
            cols: [[
                {type: 'checkbox'}
                , {field: 'id', title: 'ID', width: '5%', align: 'center', hide: true}
                , {title: '序号', type: 'numbers', width: '5%'}
                , {field: 'bidSectionName', title: '标段名称', align: 'left', width: '30%'}
                , {field: 'bidSectionCode', title: '标段编号', align: 'center', width: '15%'}
                , {field: 'bidOpenStatus', title: '开标状态',templet: '#open-status-table', align: 'center', width: '7%'}
                , {field: 'evalStatus', title: '评标状态',templet: '#eval-status-table', align: 'center', width: '7%'}
                , {field: 'bidClassifyCode', title: '标段类型',templet: '#bid-classify-code-table', align: 'center', width: '8%'}
                , {fixed: 'right', title: '操作', align: 'center', toolbar: '#barOperate'}
            ]],
            page: true,
            openType: 1,
            openTable: function (showItemData) {
                return {
                    elem: '#child'+showItemData.id
                    , id: 'child'+showItemData.id
                    , url: '${ctx}/projectInfo/pageBidderBySectionId?id='+showItemData.id
                    , page: true
                    , cols: [[
                        {title: '序号', width: '5%', type: 'numbers'},
                        {field: 'id', width: '5%', title: 'ID', hide: true}
                        , {field: 'bidderName', width: '20%', title: '投标人名称'}
                        , {field: 'bidderOrgCode', width: '10%', title: '统一社会信用代码'}
                        , {field: 'clientName', width: '5%', title: '授权委托人',templet: function (d) {
                            var clientName = "";
                            if (!isNull(d.bidderOpenInfo.clientName)) {
                                clientName = d.bidderOpenInfo.clientName;
                            }
                            return clientName;
                            }}
                        , {field: 'clientPhone', width: '9%', title: '联系电话',templet: function (d) {
                                var clientPhone = "";
                                if (!isNull(d.bidderOpenInfo.clientPhone)) {
                                    clientPhone = d.bidderOpenInfo.clientPhone;
                                }
                                return clientPhone;
                            }}
                        , {field: 'signinTime', width: '18%', title: '签到情况',templet: '#sign-in-status-table'}
                        , {field: 'bidDocId', width: '18%', title: '文件递交情况',templet: '#submit-file-status-table'}
                        , {title: '操作', toolbar: '#barDemo', width: '10%'}
                    ]]

                }
            }
        });
    })
    layui.use(['form', 'table', 'layer', 'opTable'], function () {
        var form = layui.form;
        // layui form 渲染
        form.render();
    });

    /**
     * 查看开始和结束时间
     * @param obj 触发对象
     * @param type 1：开标  2：评标
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    function viewTime(obj, type, startTime, endTime) {
        var type_str = "";
        if (type === "1") {
            type_str = "开标";
        } else if (type === "2") {
            type_str = "评标";
        }

        var msg = type_str + "开始时间：" + (isNull(startTime) ? "/" : startTime) + "<br>"
            + type_str + "结束时间：" + (isNull(endTime) ? "/" : endTime);

        layer.tips(msg, $(obj), {
            tips: [3, '#3595CC'],
            time: 4000
        });
    }

    /**
     * 文件下载
     */
    function downFiles(bidSectionId) {
        setBidSectionId(bidSectionId,function () {
            window.open("${ctx}/projectInfo/downFilesPage", "_blank")
        });
    }

    /**
     * 设置操作的标段ID
     * ***/
    function setBidSectionId(bidSectionId, callback) {
        $.ajax({
            url: '${ctx}/projectInfo/setBidSectionId',
            type: 'post',
            cache: false,
            data: {
                bidSectionId: bidSectionId
            },
            success: function (data) {
                callback();
            },
            error: function (data) {
                console.warn(data);
                layer.msg("操作失败！")
            },
        });
    }

    /**
     * 开标结果推送
     */
    function pushOpenResult(bidSectionId) {
        var indexLoad = layer.load();
        $.ajax({
            url: '${ctx}/projectInfo/pushOpenResult',
            type: 'post',
            cache: false,
            data: {
                bidSectionId: bidSectionId
            },
            success: function (data) {
                layer.close(indexLoad)
                if (data) {
                    layer.msg("推送成功", {time: 2000}, function () {
                        renderTable()
                    })
                } else {
                    layer.msg("推送失败", {time: 2000}, function () {
                        renderTable()
                    })
                }
            },
            error: function (data) {
                console.error(data);
                layer.close(indexLoad)
            },
        });
    }

    /**
     * 评标结果推送
     */
    function pushEvalResult(bidSectionId) {
        var indexLoad = layer.load();
        $.ajax({
            url: '${ctx}/projectInfo/pushEvalResult',
            type: 'post',
            cache: false,
            data: {
                bidSectionId: bidSectionId
            },
            success: function (data) {
                layer.close(indexLoad)
                if (data) {
                    layer.msg("推送成功", {time: 2000}, function () {
                        renderTable()
                    })
                } else {
                    layer.msg("推送失败", {time: 2000}, function () {
                        renderTable()
                    })
                }
            },
            error: function (data) {
                console.error(data);
                layer.close(indexLoad)
            },
        });
    }
</script>
</body>
</html>

