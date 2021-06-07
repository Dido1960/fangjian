<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>印模签章配置管理</title>
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
    </style>
    <script type="text/html" id="bid-clear-ywCode-table">
        {{# if(d.ywCode) { }}
            {{d.ywCode}}
        {{# } else { }}
            -
        {{# } }}
    </script>
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
                <script type="text/html" id="barOperate">
                    <a class="layui-btn layui-btn-sm" lay-event="edit">修改</a>
                    {{# if(d.isOldProject != 1) { }}
                        <a class="layui-btn layui-btn-sm" lay-event="gb-record">跟标记录</a>
                        <!-- 开标结束，且没有给清标3.0推送 -->
                        {{# if(d.bidClassifyCode == 'A08' && d.bidOpenStatus == 2) { }}
                            <a class="layui-btn layui-btn-warm layui-btn-sm" lay-event="pushClearV3">清标3.0</a>
                        {{# } }}
                    {{# } }}
                    <#--<a class="layui-btn layui-btn-sm" lay-event="show">查看</a>
                    <a class="layui-btn layui-btn-sm" lay-event="edit">修改</a>-->
                </script>
            </table>
        </div>
    </div>
</div>
<script src="${ctx}/layuiAdmin/layui/layui.js?t=1"></script>
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
    layui.use(['form', 'table', 'layer'], function () {
        var form = layui.form;
        var table = layui.table;

        // layui form 渲染
        form.render();
        tableIns = table.render({
            height: 'full-110',
            elem: '#project-info',
            url: '${ctx}/projectInfo/pagedProjectInfo?regId=${reg.id}',
            toolbar: '#project-info-toolbar',
            cols: [[
                {type: 'checkbox'}
                , {field: 'id', title: 'ID', width: '5%', align: 'center', hide: true}
                , {title: '序号', type: 'numbers', width: '5%'}
                , {field: 'bidSectionName', title: '标段名称', align: 'left', width: '20%'}
                , {field: 'bidSectionCode', title: '标段编号', align: 'center', width: '15%'}
                , {field: 'bidOpenStatus', title: '开标状态',templet: '#open-status-table', align: 'center', width: '7%'}
                , {field: 'evalStatus', title: '评标状态',templet: '#eval-status-table', align: 'center', width: '7%'}
                , {field: 'bidClassifyCode', title: '标段类型',templet: '#bid-classify-code-table', align: 'center', width: '7%'}
                , {field: 'ywCode', title: '业务code',templet: '#bid-clear-ywCode-table', align: 'center', width: '20%'}
                , {fixed: 'right', title: '操作', align: 'center', toolbar: '#barOperate'}
            ]],
            page: true
        });

        /**
         * layui table 工具栏绑定事件
         */
        table.on('tool(operate)', function (obj) {
            var data = obj.data;
            switch (obj.event) {
                case "gb-record":
                    gbRecord(data);
                    break;
                case "show":
                    /*showSignatureConfigInfo(data);*/
                    break;
                case "edit":
                    updateProjectInfo(data);
                    break;
                case "pushClearV3":
                    pushClearProject(data)
                    break;
            }
        });

        /**
         *  跟标记录维护
         */
        function gbRecord(data) {
            window.layer.open({
                type: 2,
                offset: 'auto',
                title: '跟标记录维护',
                area: ['780px', '450px'],
                btn: ['确认', '取消'],
                content: '${ctx}/projectInfo/operationMaintenanceInfo?bidSectionId=' + data.id,
                btn1: function (index, layero) {
                    var body = window.layer.getChildFrame('body', index);
                    var iframeWin = window[layero.find('iframe')[0]['name']];
                    iframeWin.saveOrUpdateOperationMaintenanceInfo(function () {
                        window.layer.close(index);
                        renderTable();
                    });
                },
                btn2: function (index) {
                    window.layer.close(index);
                }
            });
        }
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
     * 修改项目信息
     */
    function updateProjectInfo(data) {
        window.top.layer.open({
            type: 2,
            title: '修改项目信息',
            area: ['30%', '100%'],
            btn: ['确认', '取消'],
            offset: 'r',
            content: '${ctx}/projectInfo/updateProjectInfoPage?id=' + data.id,
            btn1: function (index, layero) {
                var body = window.top.layer.getChildFrame('body', index);
                var iframeWin = window.top[layero.find('iframe')[0]['name']];
                iframeWin.updateProjectInfo(function () {
                    window.top.layer.close(index);
                    renderTable();
                });
            },
            btn2: function (index) {
                window.top.layer.close(index);
            }
        });
    }

    /**
     * 添加新增老系统项目layer
     */
    function addProjectPage() {
        window.layer.open({
            type: 2,
            title: '新增老系统项目',
            area: ['850px', '50%'],
            btn: ['确认', '取消'],
            offset: 'auto',
            content: '${ctx}/projectInfo/addOldProjectPage?regId=${reg.id}',
            btn1: function (index, layero) {
                var body = window.layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.addDepartment(function () {
                    window.layer.close(index);
                    renderTable();
                });
            },
            btn2: function (index) {
                window.layer.close(index);
            }
        });
    }

    /**
     * 推送项目到清标3.0
     * @param id bidSectionId
     */
    function pushClearProject(bidSection){
        if (bidSection.ywCode){
            var _index = window.top.layer.confirm('当前项目已经推送，确定要重新推送吗?', {
                icon: 3,
                btn: ['确定', '取消'] //按钮
            }, function () {
                window.top. layer.close(_index)
                pushProject(bidSection);
            }, function () {
            });
        } else {
            pushProject(bidSection);
        }
    }

    /**
     * Start推送项目到清标3.0
     * @param bidSection
     */
    function pushProject(bidSection){
        var indexLoad = window.top.layer.msg('数据推送中，请稍等...', {
            icon: 16,
            shade: [0.3, '#393D49'],
            time: 0
        });
        setTimeout(function (){
            $.ajax({
                url:'${ctx}/clearV3/pushBidSection',
                type:'post',
                cache:false,
                async: false,
                data:{
                    bidSectionId: bidSection.id
                },
                success:function(data){
                    if (data){
                        window.top.layer.close(indexLoad)
                        window.top.layer.msg("数据推送成功",{icon: 1})
                        renderTable();
                    }

                },
                error:function(data){
                    console.error(data);
                    layer.close(indexLoad)
                },
            });
        },1000)
    }
</script>
</body>
</html>

