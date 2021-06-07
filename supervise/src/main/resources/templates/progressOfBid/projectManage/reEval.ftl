<!DOCTYPE html>
<html lang="en">
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
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/reconsideration.css" />
    <title>项目复议</title>
</head>
<body>
<div class="top-title-box">
    <div class="top-title">
        <div class="top-title-label">标段信息</div>
        <div class="top-title-btns">
            <div class="green-btn" onclick="reEvalThis()">标段复议</div>
        </div>
    </div>
</div>
<div class="input-box">
    <div class="input-box-item">
        <div class="item-label">
            <span>标段名</span>
        </div>
        <div class="item-input">
            <input
                    type="text"
                    disabled
                    value="${bidSection.bidSectionName!}"
            />
        </div>
    </div>
    <div class="input-box-item">
        <div class="item-label">
            <span>标段编号</span>
        </div>
        <div class="item-input">
            <input
                    type="text"
                    disabled
                    value="${bidSection.bidSectionCode!}"
            />
        </div>
    </div>
    <div class="input-box-item">
        <div class="item-label">
            <span>招标人</span>
        </div>
        <div class="item-input">
            <input
                    type="text"
                    disabled
                    value="${tenderProject.tendererName!}"
            />
        </div>
    </div>
    <div class="input-box-item">
        <div class="item-label">
            <span>标段类型</span>
        </div>
        <div class="item-input">
            <input
                    type="text"
                    disabled
                    value="${bidSection.bidClassifyName}"
            />
        </div>
    </div>
    <div class="input-box-item">
        <div class="item-label">
            <span>是否网上开标</span>
        </div>
        <div class="item-input">
            <input
                    type="text"
                    disabled
                    value="<#if bidSection.bidOpenOnline ==1 >是<#else> 否</#if>"
            />
        </div>
    </div>
    <div class="input-box-item">
        <div class="item-label">
            <span>开标时间</span>
        </div>
        <div class="item-input">
            <input
                    type="text"
                    disabled
                    value="${tenderDoc.bidOpenTime}"
            />
        </div>
    </div>
</div>
<div class="textarea-box">
    <div class="textarea-box-label">复议理由</div>
    <textarea placeholder="请输入复议理由"  id="reason" maxlength="255" onpaste="return false;" ></textarea>
</div>
<div class="table-box">
    <div class="table-box-label">复议数据记录</div>
    <div class="table-box-item">
        <ul class="head">
            <li>
                <div class="id">序号</div>
                <div class="name">申请人</div>
                <div class="time">申请时间</div>
                <div class="liyou">复议理由</div>
                <div class="btn">历史数据</div>
            </li>
        </ul>
        <ul class="body">
            <#list reevalLogs as log>
                <li>
                    <div class="id">${log_index+1}</div>
                    <div class="name">${log.userName}</div>
                    <div class="time">${log.submitTime}</div>
                    <div class="liyou">${log.reason}</div>
                    <div class="btn"  onclick="showPdf('${log.reEvalAnnexId}')">
                        <div>查看</div>
                    </div>
                </li>
            </#list>
        </ul>
    </div>
</div>
</body>
<script>
    var layer;
    layui.use(['layer'], function () {
        layer = layui.layer;
    });

    function reEvalThis() {
        var reason = $("#reason").val().trim();
        if (isNull(reason)){
            layer.msg("复议理由不可为空！",{icon: 0,time: 2000});
            return false;
        }
        layerConfirm("确认开启复议吗?", function () {
            $.ajax({
                url: "${ctx}/gov/bidEval/reEvalThis",
                type: "POST",
                cache: false,
                data: {
                    "reason": reason
                },
                success: function (data) {
                    if (data) {
                        layerSuccess("复议已开启",function () {
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
                        })
                    }else {
                        layerWarning("复议开启失败！",function () {
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
                        })
                    }
                },
                error:function (e) {
                    layerWarning("网络错误！",function () {
                        var index = parent.layer.getFrameIndex(window.name);
                        parent.layer.close(index);
                    })
                }
            });
        })
    }

    /**
     * 预览pdf
     * @param url pdf的外网地址
     */
    function showPdf(id) {
        if (isNull(id)){
            layerWarning("暂无数据！");
            return false;
        }
        window.top.layer.open({
            type: 2,
            title: '历史数据查看',
            shadeClose: true,
            area: ['60%', '100%'],
            btn: 0,
            maxmin: true,
            offset: 'rb',
            content: "${ctx}/gov/bidSection/previewPDFPageById?id=" + id,
            btn1: function (index) {
                window.top.layer.close(index);
            }
        });
    }
</script>
</html>
