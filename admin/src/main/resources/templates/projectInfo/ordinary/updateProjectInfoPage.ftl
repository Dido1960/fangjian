<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>项目信息修改</title>
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
    <style>
        #dep-form .layui-form-label {
            width: 130px;
        }
        #dep-form .layui-input-block {
            margin-left: 180px;
            width: 500px
        }
    </style>
</head>
<body>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend style="font-weight: bold">项目信息修改</legend>
</fieldset>
<form class="layui-form" method="post" id="dep-form">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <input type="hidden" name="bidSectionId" value="${bidSection.id}">
    <input type="hidden" name="tenderDocId" value="${tenderDoc.id}">
    <div class="layui-form-item">
        <label class="layui-form-label">项目归属地</label>
        <div class="layui-input-block">
            <select name="regId" lay-filter="aihao" id="regId" lay-verify="required" lay-search="">
                <#list regs as proReg><#--省级-->
                    <#if proReg.parentId == -1>
                        <option value="${proReg.id}" <#if bidSection.regId == proReg.id>selected</#if>>${proReg.regName }</option>
                        <#list regs as cityReg><#--市级-->
                            <#if cityReg.parentId == proReg.id>
                                <option value="${cityReg.id}" <#if bidSection.regId == cityReg.id>selected</#if>>&emsp;${cityReg.regName }</option>
                                <#list regs as areaReg><#--区级-->
                                    <#if areaReg.parentId == cityReg.id>
                                        <option value="${areaReg.id}" <#if bidSection.regId == areaReg.id>selected</#if>>&emsp;&emsp;${areaReg.regName }</option>
                                    </#if>
                                </#list>
                            </#if>
                        </#list>
                    </#if>
                </#list>
            </select>
        </div>
    </div>
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">开标时间</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" id="bidOpenTime" name="bidOpenTime" lay-verify="required" lay-reqtext="开标时间是必填项，岂能为空？" readonly>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">文件递交截至时间</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" id="bidDocReferEndTime" name="bidDocReferEndTime" lay-verify="required" lay-reqtext="投标文件递交截止时间是必填项，岂能为空？" readonly>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">评标专家总人数</label>
        <div class="layui-input-block">
            <input type="text" name="expertCount" id="expertCount" lay-verify="required" autocomplete="off" class="layui-input"
                   placeholder="请输入评标专家总人数" value="${tenderDoc.expertCount}">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">业主代表人数</label>
        <div class="layui-input-block">
            <input type="text" name="representativeCount" id="representativeCount" lay-verify="required" autocomplete="off" class="layui-input"
                   placeholder="请输入业主代表人数" value="${tenderDoc.representativeCount}">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">远程异地</label>
        <div class="layui-input-block">
            <select name="remoteEvaluation" lay-filter="remoteEvaluation" id="remoteEvaluation" lay-verify="required" lay-search="">
                <option value="0" <#if !(bidSection.remoteEvaluation?? && bidSection.remoteEvaluation == 1)>selected</#if>>否</option>
                <option value="1" <#if bidSection.remoteEvaluation?? && bidSection.remoteEvaluation == 1>selected</#if>>是</option>
            </select>

        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">网上开标</label>
        <div class="layui-input-block">
            <select name="bidOpenOnline" lay-filter="bidOpenOnline" id="bidOpenOnline" lay-verify="required" lay-search="">
                <option value="0" <#if !(bidSection.bidOpenOnline?? && bidSection.bidOpenOnline == 1)>selected</#if>>否</option>
                <option value="1" <#if bidSection.bidOpenOnline?? && bidSection.bidOpenOnline == 1>selected</#if>>是</option>
            </select>
        </div>
    </div>
    <div class="layui-input-block" style="margin-left: 120px;display: none">
        <button lay-submit lay-filter="*" id="formBtnSubmit">提交</button>
    </div>
</form>
<script type="text/javascript">
    var successFunc;
    layui.use(['form', 'layer'], function () {
        var form = layui.form;
        var laydate = layui.laydate;
        //执行一个laydate实例
        laydate.render({
            elem: '#bidDocReferEndTime',
            // 投标文件递交截止时间：默认为开标时间
            value: '${tenderDoc.bidOpenTime}',
            type: 'datetime',
            change: function (value, date) { //监听日期被切换
                $('.laydate-main-list-0').on('click', 'td', function () {
                    $(".laydate-btns-time").click();
                })
            }

        });
        laydate.render({
            elem: '#bidOpenTime',
            value: '${tenderDoc.bidOpenTime}',
            type: 'datetime',
            change: function (value, date) { //监听日期被切换
                $('.laydate-main-list-0').on('click', 'td', function () {
                    $(".laydate-btns-time").click();
                })
            }
        });
        form.on('submit(*)', function (data) {
            layer.load();
            $.ajax({
                url: '${ctx}/projectInfo/updateProjectInfo',
                type: 'post',
                cache: false,
                async: false,
                data: $("#dep-form").serialize(),
                success: function (data) {
                    if (!isNull(data)) {
                        data = JSON.parse(data);
                    }
                    successFunc();
                },
                error: function (data) {
                    console.error(data);
                    layer.msg("修改失败！")
                },
            });
        });

        form.render();
    });

    /**
     * 修改部门
     *
     * @param successFunc1 成功回调函数
     */
    function updateProjectInfo(successFunc1) {
        successFunc = successFunc1;
        $("#formBtnSubmit").trigger("click");
    }
</script>
</body>
</html>