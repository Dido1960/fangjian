<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <title>确认招标项目信息</title>
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
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <style>
        input {
            outline: none;
        }

        html .layui-layer-title {
            padding: 0;
            border-bottom: 1px solid rgba(213, 213, 213, 1);
        }

        html .layui-layer-btn .layui-layer-btn0 {
            background-color: rgba(19, 97, 254, 1);
        }

        html .layui-laydate .layui-this {
            background: linear-gradient(263deg, rgba(19, 97, 254, 1) 0%, rgba(78, 138, 255, 1) 100%) !important;
            filter: progid:DXImageTransform.Microsoft.gradient(GradientType=1 ,startColorstr=#1361fe, endColorstr=#4e8aff) !important;
        }

        html .layui-form-select dl dd.layui-this {
            background-color: rgba(19, 97, 254, 0.1);
            color: rgba(19, 97, 254, 1);
        }

        html .layui-form-radio>i:hover,
        html .layui-form-radioed>i {
            color: rgba(19, 97, 254, 1);
        }

        .input {
            width: 50%;
            height: 40px;
            display: flex;
            padding-left: 30px;
            padding-right: 20px;
            justify-content: space-between;
            box-sizing: border-box;
            margin-top: 30px;
            float: left;
        }

        .input label {
            display: block;
            height: 40px;
            font-size: 14px;
            font-family: Microsoft YaHei;
            font-weight: 900;
            line-height: 40px;
            color: rgba(34, 49, 101, 1);
            opacity: 1;
            float: left;
        }

        .input input,
        #yes {
            width: 300px;
            height: 40px;
            border: 1px solid rgba(213, 213, 213, 1);
            opacity: 1;
            padding-left: 10px;
            box-sizing: border-box;
            float: right;
        }

        .form {
            width: 1000px;
            display: flex;
            flex-wrap: wrap;
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

        .input .layui-input-block {
            margin-left: 0px;
        }

        html .layui-form-selected dl {
            height: 122px;
            overflow: auto;
        }

        html .layui-form-selected dl::-webkit-scrollbar {
            display: none
        }

        .input #yes option {
            display: block;
            width: 300px;
            height: 42px;
            line-height: 42px;
            font-size: 14px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: rgba(34, 49, 101, 1);

        }

        .layui-layer-btn,
        .layui-layer-btn-c {
            background: rgba(239, 243, 244, 1);
        }

        .infor {
            width: 300px;
            height: 40px;
            background: rgba(255, 255, 255, 1);
            border: 1px solid rgba(213, 213, 213, 1);
            opacity: 1;
            text-align: right;
        }

        .infor .layui-form-radio>i {
            padding: 0 0 7px 0;
        }

        html .layui-form-radio {
            margin: 6px 6px 0 0;
        }
        .modifyhide{
            display: none;
        }
        .input .layui-form-select {
            width: 300px;
            height: 40px;
            float: right;
        }

        .input .layui-form-select dl dd.layui-this {
            background-color: rgba(78, 138, 255, 1);
        }
    </style>
</head>
<div class="msgTan">
    <form action="#" class="form layui-form">
        <input type="hidden" id="flag" value="${flag}">
        <input type="hidden" name="clarifyFileId" value="${clarifyFileId}">
        <input type="hidden" name="gradeId" value="${tenderDoc.gradeId}">
        <input type="hidden" name="xmlUid" value="${tenderDoc.xmlUid}">
        <input type="hidden" name="projectId" value="${project.id}">
        <input type="hidden" name="tenderProjectId" value="${tenderProject.id}">
        <input type="hidden" name="docFileId" value="${tenderDoc.docFileId}">
        <input type="hidden" name="tenderDocId" value="${tenderDoc.id}">
        <input type="hidden" name="bidSectionId" value="${bidSection.id}">
        <input type="hidden" name="bidClassifyCode" value="${bidSection.bidClassifyCode}">
        <input type="hidden" name="evaluationMethod" value="${tenderDoc.evaluationMethod}">
        <input type="hidden" name="tenderMode" value="${tenderProject.tenderMode}">
        <div class="input">
            <label for="name">项目名称</label>
            <input type="text" name="projectName" lay-verify="required" lay-reqtext="项目名称是必填项，岂能为空？" value="${project.projectName }" readonly>
        </div>
        <div class="input">
            <label for="num">项目编号</label>
            <input type="text" name="projectCode" lay-verify="required" lay-reqtext="项目编号是必填项，岂能为空？" value="${project.projectCode }" readonly>
        </div>
        <div class="input">
            <label for="tltName">标段名称</label>
            <input type="text" name="bidSectionName" lay-verify="required" lay-reqtext="标段名称是必填项，岂能为空？" value="${bidSection.bidSectionName }" readonly>
        </div>
        <div class="input">
            <label for="tltNum">标段编号</label>
            <input type="text" name="bidSectionCode" lay-verify="required" lay-reqtext="标段编号是必填项，岂能为空？" value="${bidSection.bidSectionCode }" readonly>
        </div>
        <div class="input">
            <label for="hum">招标人</label>
            <input type="text" name="tendererName" lay-verify="required" lay-reqtext="招标人是必填项，岂能为空？" value="${tenderProject.tendererName }" placeholder="请输入招标人" readonly>
        </div>
        <div class="input">
            <label for="agency">招标代理机构</label>
            <input type="text" name="tenderAgencyName" lay-verify="required" lay-reqtext="招标代理机构是必填项，岂能为空？" value="${tenderProject.tenderAgencyName }" placeholder="请输入招标代理机构" readonly>
        </div>
        <div class="input">
            <label for="how">代理机构联系方式</label>
            <input type="text" name="tenderAgencyPhone" lay-verify="required|phone" lay-reqtext="代理机构联系方式是必填项，岂能为空？" value="${tenderProject.tenderAgencyPhone }" placeholder="请输入代理机构联系方式">
        </div>
        <div class="input">
            <label for="yes">项目所属地</label>
            <div class="layui-input-block">
                <select name="regId" lay-filter="aihao" id="regId" lay-verify="required" lay-search="">
                    <#list regs as proReg><#--省级-->
                        <#if proReg.parentId == -1>
                            <option value="${proReg.id}" <#if reg.id == proReg.id>selected</#if>>${proReg.regName }</option>
                            <#list regs as cityReg><#--市级-->
                                <#if cityReg.parentId == proReg.id>
                                    <option value="${cityReg.id}" <#if reg.id == cityReg.id>selected</#if>>&emsp;${cityReg.regName }</option>
                                    <#list regs as areaReg><#--区级-->
                                        <#if areaReg.parentId == cityReg.id>
                                            <option value="${areaReg.id}" <#if reg.id == areaReg.id>selected</#if>>&emsp;&emsp;${areaReg.regName }</option>
                                        </#if>
                                    </#list>
                                </#if>
                            </#list>
                        </#if>
                    </#list>
                </select>
            </div>
        </div>
        <div class="input">
            <label for="tltName">评标专家总人数</label>
            <input type="text" name="expertCount" value="${tenderDoc.expertCount }" lay-verify="required|num" lay-reqtext="评标专家总人数是必填项，岂能为空？" placeholder="评标专家总人数（包含业主代表）">
        </div>
        <div class="input">
            <label for="tltNum">招标代表人数</label>
            <input type="text" name="representativeCount" value="${tenderDoc.representativeCount }" lay-verify="required|num" lay-reqtext="招标代表人数是必填项，岂能为空？" placeholder="招标代表人数">
        </div>

        <div class="input">
            <label for="test1">投标文件递交截止时间</label>
            <input type="text" class="layui-input" id="bidDocReferEndTime" name="bidDocReferEndTime" lay-verify="required" lay-reqtext="投标文件递交截止时间是必填项，岂能为空？" readonly>
        </div>
        <div class="input">
            <label for="test2">开标时间</label>
            <input type="text" class="layui-input" id="bidOpenTime" name="bidOpenTime" lay-verify="required" lay-reqtext="开标时间是必填项，岂能为空？" readonly>
        </div>
        <div class="input notModifyItem">
            <label for="yes">是否网上开标</label>
            <div class="infor" style="float:right;">
                <#if bidSection.bidOpenOnline?? && bidSection.bidOpenOnline == 1>
                    是 <input type="radio" name="bidOpenOnline" value="1" checked style="padding: 0 0 7px 0;">
                    否 <input type="radio" name="bidOpenOnline" value="0" style="padding: 0 0 7px 0;">
                <#else>
                    是 <input type="radio" name="bidOpenOnline" value="1" style="padding: 0 0 7px 0;">
                    否 <input type="radio" name="bidOpenOnline" value="0" checked style="padding: 0 0 7px 0;">
                </#if>

            </div>
        </div>
        <div class="input notModifyItem">
            <label for="yes">是否远程异地评标</label>
            <div class="infor"  style="float:right;">
                <#if bidSection.remoteEvaluation?? && bidSection.remoteEvaluation == 1>
                    是 <input type="radio" name="remoteEvaluation" value="1" checked style="padding: 0 0 7px 0;">
                    否 <input type="radio" name="remoteEvaluation" value="0" style="padding: 0 0 7px 0;">
                <#else>
                    是 <input type="radio" name="remoteEvaluation" value="1" style="padding: 0 0 7px 0;">
                    否 <input type="radio" name="remoteEvaluation" value="0" checked style="padding: 0 0 7px 0;">
                </#if>
            </div>
        </div>
        <button id="save" type="button" class="layui-btn layui-hide" lay-submit="" lay-filter="*">立即提交</button>
    </form>
</div>
<script>
    /**
     * 重载layui
     */
    function reloadLayui() {
        layui.use('form', function () {
            var form = layui.form;

            form.render();
        });
    }

    function saveProjectInfo() {
        $("#save").click();
    }

    layui.use(['form','laydate'], function(){
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

        //自定义验证规则
        form.verify({
            num: [
                /^[0-9]*$/,
                '请输入正确的数字'
            ]
        });

        //监听提交
        form.on('submit(*)', function(result){
            var bidDocReferEndTime = new Date($('#bidDocReferEndTime').val().replace(/-/g, '/'));
            var bidOpenTime = new Date($('#bidOpenTime').val().replace(/-/g, '/'));
            if (bidDocReferEndTime.getTime() > bidOpenTime.getTime()) {
                parent.layer.msg("投标文件递交时间应该在开标时间之前!", {icon: 5});
                return false;
            }

            var tipInfo="项目保存中, 请稍候...";
            var url="${ctx}/staff/saveProjectInfo";
            if (!isNull($("#flag").val())){
                tipInfo="项目修改中, 请稍候...";
            }
            var loadindex = parent.layer.msg(tipInfo, {
                icon: 16,
                shade: [0.3, '#393D49'],
                time: 0
            });
            $.ajax({
                url: url,
                type: "POST",
                data: JSON.stringify(result.field),
                contentType : "application/json;charset=utf-8",
                dataType : "json",
                cache: false,
                success: function(result){
                    parent.layer.close(loadindex);
                    if (result.code === "1") {
                        parent.layer.msg(result.msg, {
                            icon: result.code, end: function () {
                                parent.layer.closeAll();
                                parent.location.reload();
                            }
                        });
                        // 记录新增项目类型
                        localStorage.setItem("userBidClassifyCode","${bidSection.bidClassifyCode}");
                    } else {
                        parent.layer.alert(result.msg, {
                            icon: result.code, yes: function () {
                                parent.layer.closeAll();
                                parent.location.reload();
                            }
                        });
                    }
                },
                error: function (data) {
                    parent.layer.close(loadindex);
                    parent.layer.msg("项目新增失败!", {icon: 2, time: 3000});
                }
            });

            return false;
        });

        reloadLayui();
    });

    $(function () {
        //修改工程  隐藏信息
        if (!isNull($('#flag').val())){
            $('.notModifyItem').addClass("modifyhide");
            $('.notModifyItem').addClass("modifyhide");
        }
    });

</script>

</html>