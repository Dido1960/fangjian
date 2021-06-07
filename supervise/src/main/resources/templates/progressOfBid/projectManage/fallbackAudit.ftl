<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>甘肃省电子开标评标平台</title>
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
    <script src="${ctx}/js/convertMoney.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
    <script src="${ctx}/js/base64.js"></script>
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/fallbackAudit.css">
    <style>
        .expertUser {
            color: blue;
            font-weight: bolder;
        }
    </style>
</head>

<body>
<div class="cont">
    <div class="head">
        标段信息
        <div class="head-btn">
            <#if waiteFreeBackApply>
                <span class="yellow-b" onclick="fallbackAuditConfirm('1')">通过</span>
                <span class="green-b" onclick="fallbackAuditConfirm('2')">驳回</span>
            </#if>
        </div>
    </div>
    <form action="" class="information">
        <div>
            <label for="">标段名称</label>
            <input type="text" value="${bidSection.bidSectionName!}" disabled>
        </div>
        <div>
            <label for="">标段编号</label>
            <input type="text" value="${bidSection.bidSectionCode!}" disabled>
        </div>
        <div>
            <label for="">招标人</label>
            <input type="text" value="${tenderProject.tendererName!}" disabled>
        </div>
        <div>
            <label for="">标段类型</label>
            <input type="text" value="${bidSection.bidClassifyName}" disabled>
        </div>
        <div>
            <label for="">是否网上开标</label>
            <input type="text" value="<#if bidSection.bidOpenOnline ==1 >是<#else> 否</#if>" disabled>
        </div>
        <div>
            <label for="">开标时间</label>
            <input type="text" value="${tenderDoc.bidOpenTime}" disabled>
        </div>
    </form>
    <div class="cont-cen" style="display:  <#if !waiteFreeBackApply>none</#if>">
        <h3>回退申请</h3>
        <div class="apply">
            <div>
                <label for="">评审进展环节</label>
                <input type="text" value="${waiteFreeBackApply.beforeStepName!}" disabled>
            </div>
            <div>
                <label for="">申请回退环节</label>
                <input type="text" value="${waiteFreeBackApply.stepName!}" disabled>
            </div>
            <div>
                <label for="">回退申请人</label>
                <input type="text" value="${waiteFreeBackApply.applyUserName!}" disabled>
            </div>
            <div>
                <label for="">申请时间</label>
                <input type="text" value="${waiteFreeBackApply.applyTime}" disabled>
            </div>
        </div>
        <h5>回退原因</h5>
        <textarea name="" id="" placeholder="请输入" disabled>${waiteFreeBackApply.reason!}</textarea>
    </div>
    <div class="cont-table">
        <h3>回退申请记录</h3>
        <div class="document">
            <ol>
                <li>序号</li>
                <li>申请人</li>
                <li>申请时间</li>
                <li>审核状态</li>
                <li>审核时间</li>
                <li>历史数据</li>
            </ol>
            <div class="document-table">
                <table class="layui-table" id="freabackTable" lay-filter="freabackTable">
                    <#list listFreeBackApply as freeBackApply>
                        <tr>
                            <td>${freeBackApply_index+1}</td>
                            <td>${freeBackApply.applyUserName}</td>
                            <td>${freeBackApply.applyTime}</td>
                            <td>${freeBackApply.checkStatusName}</td>
                            <td>${freeBackApply.checkTime}</td>
                            <#if freeBackApply.checkStatus == 1>
                                <td title="点击可查看回退数据" onclick="showPdf('${freeBackApply.freeBackAnnexUrl}')"
                                    style="color: rgba(0, 102, 204, 1)">
                                    <i class="layui-icon layui-icon-read"
                                       style="font-size: 30px; color: rgba(0, 102, 204, 1);"></i>
                                </td>
                            <#else >
                                <td title="无数据"><i class="layui-icon layui-icon-close"
                                                   style="font-size: 30px; color: #FF5722;"></i></td>
                            </#if>
                        </tr>
                    </#list>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
<script>

    var layer, table;
    layui.use(['element', 'layer', 'table'], function () {
        layer = layui.layer;
        table = layui.table;
    });

    /**
     * 回退审核
     */
    function fallbackAuditConfirm(checkStatus) {
        var statusName = "通过";
        if (checkStatus == 2) {
            statusName = "驳回"
        }
        var index = parent.layer.confirm('确定要 【' + statusName + '】 回退申请吗？', {
            icon: 3, btn: ['确定', '取消']
        }, function () {
            window.top.layer.close(index);
            // 回退处理
            updateBackApply(checkStatus);
        });
    }

    /**
     * 回退处理请求
     * checkStatus 处理状态值
     */
    function updateBackApply(checkStatus) {
        var indexLoad;
        // 审核通过时，才显示pdf合成进度
        if (!isNull(checkStatus) && checkStatus == "1") {
            // 请求当前数据处理进度
            indexLoad = currentConversion();
        } else {
            indexLoad = window.top.layer.msg('数据正在回退中，请稍等...', {
                icon: 16,
                shade: [0.3, '#393d49'],
                time: 0
            });
        }

        $.ajax({
            url: '${ctx}/gov/bidEval/updateBackApply',
            type: 'post',
            cache: false,
            data: {
                id:${waiteFreeBackApply.id!-1},
                checkStatus: checkStatus
            },
            success: function (data) {
                window.top.layer.close(indexLoad);
                if (data) {
                    layerAlert("审核操作成功!")
                } else {
                    layerError("审核操作失败!")
                }
                window.location.reload();
            },
            error: function (data) {
                console.error(data);
                window.top.layer.close(indexLoad)
            },
        });


    }

    /**
     * 请求当前数据处理进度
     */
    function currentConversion() {
        var indexLoad = window.top.layer.open({
            type: 2,
            offset: 'c',
            title: false,
            shadeClose: false,
            area: ['45%', '60%'],
            btn: false,
            content: '${ctx}/gov/showSysInfoPage',
            btn1: false
        });

        return indexLoad;
    }

    /**
     * 预览pdf
     * @param url pdf的外网地址
     */
    function showPdf(url) {
        window.top.layer.open({
            type: 2,
            title: '评标历史查看',
            shadeClose: true,
            area: ['60%', '100%'],
            btn: 0,
            maxmin: true,
            offset: 'rb',
            content: "${ctx}/gov/bidSection/previewPDFPage?url=" + url,
            btn1: function (index) {
                window.top.layer.close(index);
            }
        });
    }
</script>
</html>