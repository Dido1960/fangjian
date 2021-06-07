<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>证书识别</title>
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
    <script src="${ctx}/js/base64.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <OBJECT id="XTXAPP" classid="clsid:3F367B74-92D9-4C5E-AB93-234F8A91D5E6" height="0" width="0"></OBJECT>
    <object id="PIC" classid="clsid:3BC3C868-95B5-47ED-8686-E0E3E94EF366" height="0" width="0"></object>
    <style>
        .layui-table td, .layui-table th {
            position: static;
        }
        .layui-table-view {
            height: calc(100% - 200px)!important;
        }
    </style>
</head>
<body>
<div>
    <div class="layui-layout layui-layout-admin">
        <div class="layui-card" style="margin: 15px 0 0 15px; width: calc(100% - 30px)">
            <div class="layui-card-header"><i class="layui-icon layui-icon-form" style="color: #FF5722"></i> 离线审核权限维护</div>
            <div class="layui-card-body layui-form">
                <div class="layui-form-item">
                    <div class="layui-inline  layui-col-md4" style="display: none">
                        <label class="layui-form-label">唯一识别码</label>
                        <div class="layui-input-block">
                            <input type="text" name="certKeyNum" id="cert_key_num"
                                   lay-verify="required" autocomplete="off" readonly
                                   class="layui-input layui-bg-gray">
                        </div>
                    </div>
                    <div class="layui-input-inline layui-col-md6">
                        <button type="button" id="cert-info-submit" onclick="writeAuth()" class="layui-btn">审核权限写入</button>
                    </div>
                    <div class="layui-input-inline layui-col-md6">
                         <button type="button" onclick="removeAuth()" class="layui-btn layui-btn-danger">审核权限移除</button>
                     </div>
                </div>
            </div>
        </div>
        <div class="layui-card" style="margin: 15px 0 0 15px; width: calc(100% - 30px)">
            <form class="layui-form" style="padding-top: 15px;">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">日志时限</label>
                        <div class="layui-input-block">
                            <input type="text" readonly name="searchTime" id="search-time" class="layui-input"
                                   style="width: 300px">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">用户名</label>
                        <div class="layui-input-block">
                            <input type="text" name="username" id="user-name" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">日志内容</label>
                        <div class="layui-input-block">
                            <input type="text" name="content" id="content" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">操作类型</label>
                        <div class="layui-input-block">
                            <select id="dml-type" lay-filter="aihao">
                                <option value="">请选择</option>
                                <option value="1">写入</option>
                                <option value="0">移除</option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-input-block">
                            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                            <button type="button" class="layui-btn layui-btn-normal" onclick="startSearch()">查询</button>
                        </div>
                    </div>
                </div>
            </form>
            <table class="layui-hide" id="user-log-table" style="margin-left: 15px">
                <script type="text/html" id="orderNum">
                    {{d.LAY_TABLE_INDEX+1}}
                </script>
                <script type="text/html" id="operateType">
                    {{#  if(d.operateType === 1){ }}
                        写入
                    {{#  } else if(d.operateType === 0){ }}
                        移除
                    {{#  } else { }}
                        未知
                    {{#  } }}
                </script>
            </table>
        </div>
    </div>
</div>
<script>
    /**
     * 用户日志查询
     */
    function startSearch() {
        var username = $("#user-name").val();
        var search_time = $("#search-time").val();
        var content = $("#content").val();
        var dmlType = $('#dml-type option:selected').val();

        layui.table.reload('user-log-table', {
            where: {
                username: username,
                searchTime: search_time,
                content: content,
                dmlType: dmlType
            }
            , page: {
                curr: 1
            }
        });
    }

    /**
     * 初始化layui
     */
    layui.use(['form', 'table', 'layer', 'laydate'], function () {
        var form = layui.form;
        var table = layui.table;
        var laydate = layui.laydate;

        // 执行一个laydate实例
        laydate.render({
            elem: '#search-time',
            range: '~',
            format: 'yyyy-MM-dd HH:mm:ss',
            type: 'datetime'
        });

        // layui form 渲染
        form.render();

        // 创建静态表格实例
        table.render({
            id: "user-log-table",
            elem: '#user-log-table',
            height: 'full-80',
            url: '${ctx}/certInfo/pagedReviewAuthorityLog',
            page: true,
            toolbar: true,
            cols: [[
                {title: '序号', type: 'numbers'},
                {field: 'userName', title: '用户名', align: 'center'},
                {field: 'certName', title: '证书名称', align: 'center'},
                {field: 'keyNo', title: '锁唯一序列号', align: 'center'},
                {field: 'operateType', title: '操作方式', align: 'center', toolbar: '#operateType'},
                {field: 'insertTime', title: '创建时间', align: 'center'}
            ]],
            done:function(res, curr, count) {
                var columsName = ['userName','certName','keyNo'];//需要合并的列名称
                layuiRowspan( columsName,1);
            }
        });

    });

    /**
     *  写入权限
     */
    function writeAuth() {
        layer.open({
            type: 2,
            title: "请选择CA",
            offset: 'c',
            content: "/common/data/selectCaInfoPage",
            area: ['400px', '300px'],
            btn: ['确定', '关闭'],
            btn1: function (index, layero) {
                //信息内容，参考于 BJCA_CertInfo
                var body = layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.loginCa(function (cert_info, qskj_info) {
                    if (isNull(cert_info.CERT_SERIAL_NUMBER)) {
                        return;
                    }
                    var index1 = layer.confirm("是否写入离线审核权限进入该锁？", function () {
                        layer.close(index1);
                        iframeWin.WriteFileEx(cert_info.CERT_NO_INDEX, "c3112ffee20e1280b9317de7815c8640", _Base64encode("active"), function () {
                            var certNo = cert_info.CERT_NO_INDEX;
                            var certName = cert_info.CERT_NAME;
                            $.ajax({
                                url: "${ctx}/certInfo/recordReviewAuthorityLog",
                                type: "POST",
                                cache: false,
                                data: {
                                    keyNo: certNo,
                                    certName: certName,
                                    operateType: 1
                                },
                                success: function (data) {
                                    layer.close(index);
                                    if (data) {
                                        window.top.layer.msg("离线审核权限写入成功");
                                        layui.table.reload("user-log-table");
                                    } else {
                                        window.top.layer.msg("离线审核权限写入失败");
                                    }
                                },
                                error: function (e) {
                                    layer.close(index);
                                    window.top.layer.msg("离线审核权限写入失败");
                                }
                            });
                        });
                    })
                });
            },
            btn2: function (index) {
                // 点击取消的回调函数
                layer.close(index);
            },
            end: function () {

            }
        });
    }

    /**
     * 移除权限
     */
    function removeAuth() {
        layer.open({
            type: 2,
            title: "请选择CA",
            offset: 'c',
            content: "/common/data/selectCaInfoPage",
            area: ['400px', '300px'],
            btn: ['确定', '关闭'],
            btn1: function (index, layero) {
                //信息内容，参考于 BJCA_CertInfo
                var body = layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.loginCa(function (cert_info, qskj_info) {
                    if (isNull(cert_info.CERT_SERIAL_NUMBER)) {
                        return;
                    }
                    iframeWin.ReadFileEx(cert_info.CERT_NO_INDEX, "c3112ffee20e1280b9317de7815c8640", function (returnInfo) {
                        var retVal = returnInfo.retVal;
                        if (isNull(retVal) || _Base64decode(retVal) != "active") {
                            window.top.layer.msg("该锁没有离线审核权限，无需移除");
                            return;
                        } else {
                            var index1 = layer.confirm("是否将该锁离线审核权限移除？", function () {
                                layer.close(index1);
                                iframeWin.WriteFileEx(cert_info.CERT_NO_INDEX, "c3112ffee20e1280b9317de7815c8640", _Base64encode("unActive"), function () {
                                    var certNo = cert_info.CERT_NO_INDEX;
                                    var certName = cert_info.CERT_NAME;
                                    $.ajax({
                                        url: "${ctx}/certInfo/recordReviewAuthorityLog",
                                        type: "POST",
                                        cache: false,
                                        data: {
                                            keyNo: certNo,
                                            certName: certName,
                                            operateType: 0
                                        },
                                        success: function (data) {
                                            layer.close(index);
                                            if (data) {
                                                window.top.layer.msg("离线审核权限移除成功");
                                                layui.table.reload("user-log-table");
                                            } else {
                                                window.top.layer.msg("离线审核权限移除失败");
                                            }
                                        },
                                        error: function (e) {
                                            layer.close(index);
                                            window.top.layer.msg("离线审核权限移除失败");
                                        }
                                    });
                                });
                            })
                        }
                    });
                });
            },
            btn2: function (index) {
                // 点击取消的回调函数
                layer.close(index);
            },
            end: function () {

            }
        });
    }
</script>
</body>
</html>