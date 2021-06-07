<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <title>新建纸质标项目信息</title>
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
            width: 500px;
            height: 40px;
            display: flex;
            padding-left: 30px;
            padding-right: 20px;
            justify-content: space-between;
            box-sizing: border-box;
            margin-top: 30px;
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
        }

        .input input,
        #yes {
            width: 300px;
            height: 40px;
            border: 1px solid rgba(213, 213, 213, 1);
            opacity: 1;
            padding-left: 10px;
            box-sizing: border-box;
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
    </style>
</head>
<div class="msgTan">
    <form action="#" class="form layui-form">
        <div class="input">
            <label for="name">项目名称</label>
            <input type="text" name="projectName" lay-verify="required" lay-reqtext="项目名称是必填项，岂能为空？" placeholder="请输入项目名称">
        </div>
        <div class="input">
            <label for="num">项目编号</label>
            <input type="text" name="projectCode" lay-verify="required" lay-reqtext="项目编号是必填项，岂能为空？" placeholder="请输入项目编号">
        </div>
        <div class="input">
            <label for="tltName">标段名称</label>
            <input type="text" name="bidSectionName" lay-verify="required" lay-reqtext="标段名称是必填项，岂能为空？" placeholder="请输入标段名称">
        </div>
        <div class="input">
            <label for="tltNum">标段编号</label>
            <input type="text" name="bidSectionCode" lay-verify="required" lay-reqtext="标段编号是必填项，岂能为空？" placeholder="请输入标段编号">
        </div>
        <div class="input">
            <label for="hum">招标人</label>
            <input type="text" name="tendererName" lay-verify="required" lay-reqtext="招标人是必填项，岂能为空？" placeholder="请输入招标人">
        </div>
        <div class="input">
            <label for="agency">招标代理机构</label>
            <input type="text" name="tenderAgencyName" value="${user.name}" lay-verify="required" lay-reqtext="招标代理机构是必填项，岂能为空？" placeholder="请输入招标代理机构" readonly>
        </div>
        <div class="input">
            <label for="how">代理机构联系方式</label>
            <input type="text" name="tenderAgencyPhone" lay-verify="required|phone" lay-reqtext="代理机构联系方式是必填项，岂能为空？" placeholder="请输入代理机构联系方式">
        </div>
        <div class="input">
            <label for="price">投标保证金</label>
            <input type="text" name="marginAmount" lay-verify="required|amount" placeholder="（单位：元）请输入合理的金额" autocomplete="off" class="layui-input">
        </div>
        <div class="input">
            <label for="tltName">评标专家总人数</label>
            <input type="number" name="expertCount" value="${tenderDoc.expertCount }" lay-verify="required|num" lay-reqtext="评标专家总人数是必填项，岂能为空？" placeholder="评标专家总人数（包含业主代表）">
        </div>
        <div class="input">
            <label for="tltNum">招标代表人数</label>
            <input type="number" name="representativeCount" value="${tenderDoc.representativeCount }" lay-verify="required|num" lay-reqtext="招标代表人数是必填项，岂能为空？" placeholder="招标代表人数">
        </div>
        <div class="input">
            <label for="yes">项目交易平台</label>
            <div class="layui-input-block">
                <select name="regId" lay-filter="aihao" class="selectReg" id="regId" lay-verify="required" lay-search="" placeholder="请选择项目交易平台">
                    <option value="">请直接选择或搜索</option>
                    <#--<#list regs as regChild>
                        <#if regChild.parentId == -1>
                            <option value="${regChild.id}">${regChild.regName }</option>
                        <#else>
                            <option value="${regChild.id}">&emsp;${regChild.regName }</option>
                        </#if>
                    </#list>-->
                </select>
            </div>
        </div>
        <div class="input">
            <label for="yes">标段分类</label>
            <div class="layui-input-block">
                <@WordBookTag key="bidClassifyCode" name="bidClassifyCode" id="bidClassifyCode" please="t" verify="required" filter="bidClassifyCode" reqtext="请选择标段类型"></@WordBookTag>
            </div>
        </div>
        <div class="input">
            <label for="shape">招标组织形式</label>
            <div class="into" style="display: flex;height: 40px; line-height: 40px;text-align: right;">
                自主招标 &nbsp;<input type="radio" name="tenderOrganizeForm" value="自主招标" checked style="padding: 0 0 7px 0;">
                委托招标 &nbsp;<input type="radio" name="tenderOrganizeForm" value="委托招标" style="padding: 0 0 7px 0;">
                其他 &nbsp;<input type="radio" name="tenderOrganizeForm" value="其他" style="padding: 0 0 7px 0;">
            </div>
        </div>
        <div class="input">
            <label for="tenderMode">招标方式</label>
            <div class="into" style="display: flex; justify-content: space-between;height: 40px; line-height: 40px;text-align: right;">
                公开招标 &nbsp;<input type="radio" name="tenderMode" value="公开招标" checked style="padding: 0 0 7px 0;">
                邀请招标 &nbsp;<input type="radio" name="tenderMode" value="邀请招标" style="padding: 0 0 7px 0;">
            </div>
        </div>

        <div class="input">
            <label for="test1">投标文件递交截止时间</label>
            <input type="text" class="layui-input" id="bidDocReferEndTime" name="bidDocReferEndTime" lay-verify="required" lay-reqtext="投标文件递交截止时间是必填项，岂能为空？" placeholder="请完善投标文件递交截止时间" readonly>
        </div>
        <div class="input">
            <label for="test2">开标时间</label>
            <input type="text" class="layui-input" id="bidOpenTime" name="bidOpenTime" lay-verify="required" lay-reqtext="开标时间是必填项，岂能为空？" placeholder="请完善开标时间" readonly>
        </div>
        <div class="input">
            <label for="inter">是否网上开标</label>
            <div class="infor">
                是 <input type="radio" name="bidOpenOnline" value="1" readonly checked style="padding: 0 0 7px 0;">
                否 <input type="radio" name="bidOpenOnline" value="0" readonly style="padding: 0 0 7px 0;">
            </div>
        </div>
        <div class="input">
            <label for="tenderFile">招标文件（PDF）</label>
            <input type="text" name="docFileName" id="docFileName" lay-verify="required" lay-reqtext="请上传招标文件（PDF）" placeholder="请上传招标文件（PDF）" onclick="uploadBidFile(this)" autocomplete="off" class="layui-input">
            <input type="hidden" name="docFileId" id="docFileId"/>
        </div>
        <button id="save" type="submit" class="layui-btn layui-hide" lay-submit="" lay-filter="*">立即提交</button>
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
            value: '${tenderDoc.bidDocReferEndTime}',
            type: 'datetime',
            trigger : 'click',
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
            trigger : 'click',
            change: function (value, date) { //监听日期被切换
                $('.laydate-main-list-0').on('click', 'td', function () {
                    $(".laydate-btns-time").click();
                })
            }
        });

        //自定义验证规则
        form.verify({
            amount: [
                /^(([1-9]{1}\d*)|(0{1}))(\.\d{0,2})?$/,
                '请输入正确的金额'
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

            var loadindex = parent.layer.msg('项目保存中, 请稍候...', {
                icon: 16,
                shade: [0.3, '#393D49'],
                time: 0
            });

            $.ajax({
                url: "${ctx}/staff/savePaperProjectInfo",
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
    /**
     * 招标文件pdf上传
     */
    function uploadBidFile(e) {
        var allowType = "*.pdf;*.PDF";
        var allowFileSize = "500M";
        window.top.layer.open({
            type: 2,
            content: '${ctx!}/fdfs/uploadFilePage',
            title: '招标文件上传（*.pdf）',
            area: ['600px', '540px'],
            btn: ['关闭'],
            success: function (layero, index) {
                var body = layer.getChildFrame('body', index);
                var iframeWin = window.top[layero.find('iframe')[0]['name']];
                iframeWin.initUploadParam(allowType, allowFileSize);
                iframeWin.dropzoneInit("0",function (uploadFile) {
                    console.log(uploadFile.name);
                    $(e).val(uploadFile.name);
                    $(e).next("input[id='docFileId']").val(uploadFile.id);
                    window.top.layer.close(index);
                });
            },
            btn1: function (index) {
                window.top.layer.close(index);
            }
        });
    }

    $(function () {
        $(".selectReg").html(selectReg(-1, null));
        reloadLayui();
    })

    /**
     * 递归排序显示行政区划
     * pid 父级id
     * ppid 父级的父级id(为null表示没有)
     */
    var regs = JSON.parse('${regs}');
    var bstr = "<option value=''>请直接选择或搜索</option>";
    function selectReg(pid, ppid) {
        for(var i = 0; i < regs.length; i++) {
            var parentId = regs[i].parentId;
            var id = regs[i].id;

            if(parentId === pid) {
                var sapce = "";
                if (ppid === -1) {
                    sapce += "&emsp;";
                } else if (ppid >= 1){
                    sapce += "&emsp;&emsp;";
                }
                bstr += "<option value='" + id + "'>" + sapce + regs[i].regName + "</option>";
                selectReg(id, pid);
            }
        }
        return bstr;
    }
</script>

</html>