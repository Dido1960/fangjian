<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>预审关联</title>
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
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>

    <link rel="stylesheet" href="${ctx}/css/preRelated.css">
</head>
<body>
<div class="cont">
    <div class="document">
        <h3>项目信息</h3>
        <form action="">
            <div class="check">
                <label for="">项目名称</label>
                <input type="text" value="${project.tenderProjectName}" disabled>
            </div>
            <div class="check">
                <label for="">项目编号</label>
                <input type="text" value="${project.tenderProjectCode}" disabled>
            </div>
            <div class="check">
                <label for="">招标类型</label>
                <input type="text" value="${type}" disabled>
            </div>
            <div class="check">
                <label for="">招标人</label>
                <input type="text" value="${project.tendererName}" disabled>
            </div>
        </form>
        <div class="document-list">
            <ol>
                <li>序号</li>
                <li class="long">标段名称</li>
                <li class="long">标段编号</li>
                <li>标段类型</li>
                <li>开标时间</li>
                <li>开标状态</li>
                <li>操作</li>
            </ol>
            <div class="document-table">
                <table cellspacing=0 cellpadding=0>
                    <#if list?? && (list?size > 0) >
                        <#list list as section>
                            <tr>
                                <td>${section_index + 1}</td>
                                <td class="long">${section.bidSectionName}</td>
                                <td class="long">${section.bidSectionCode}</td>
                                <td>资格预审</td>
                                <td>${section.bidOpenTime}</td>
                                <#if section.bidOpenStatus == 0>
                                    <td class="yellow-f">未开始</td>
                                <#elseif section.bidOpenStatus == 1>
                                    <td class="green-f">进行中</td>
                                <#else >
                                    <td class="red-f">结束</td>
                                </#if>
                                <td>
                                    <#if bidSectionRelate.preRelatedId?? && bidSectionRelate.preRelatedId == section.id>
                                        <span class="gray-b">已关联</span>
                                    <#else >
                                        <span class="blove-b" onclick="preRelatedThis('${section.id}')">关联</span>
                                    </#if>
                                </td>
                            </tr>
                        </#list>
                    <#else >
                        <tr><td colspan="7">无可关联的预审项目</td></tr>
                    </#if>
                </table>
            </div>
        </div>
    </div>
</div>

<script>
    function preRelatedThis(bidSectionId) {
        if (isNull('${bidSectionRelate.preRelatedId}')){
            layerConfirm("确认关联当前标段？",function () {
                preRelated(bidSectionId);
            });
        }else {
            layerConfirm("当前标段已关联预审标段，是否切换为关联当前标段",function () {
                preRelated(bidSectionId);
            });
        }
    }

    function preRelated(bidSectionId) {
        $.ajax({
            url: "${ctx}/staff/preRelated",
            type: "POST",
            cache: false,
            data: {
                "id" : '${bidSectionRelate.id}',
                "preRelatedId" : bidSectionId
            },
            success: function (data) {
                if (data) {
                    layerSuccess("关联成功",cancle());
                }else {
                    layerWarning("关联失败");
                }
            },
            error:function (e) {
                layerWarning("网络异常");
                console.error(e);
            }
        });
    }

    function cancle() {
        //先得到当前iframe层的索引
        var index = parent.layer.getFrameIndex(window.name);
        //再执行关闭
        parent.layer.close(index);
    }
</script>
</body>
</html>