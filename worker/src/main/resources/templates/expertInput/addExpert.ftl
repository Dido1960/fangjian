<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>新增评委</title>
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
    <script src="${ctx}/js/LodopFuncs.js"></script>
    <link rel="stylesheet" href="${ctx}/css/addExpert.css">
</head>
<body>
<form action="" class="layui-form">
    <input type="hidden" name="bidSectionId" value="${bidSectionId}">
    <input type="hidden" name="bidApplyId" value="${bidApply.id}">
    <div class="layui-form-item">
        <label class="layui-form-label">评委姓名<span>*</span></label>
        <input type="text" placeholder="请输入" name="expertName" id="expertName" lay-verify="required" oninput="search();" onblur="cancel()"
               autocomplete="off" maxlength="20">
        <ul id="hotWords" style="display: none">
        </ul>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">手机号码<span>*</span></label>
        <input type="text" placeholder="请输入" name="phoneNumber" id="expertPhone" lay-verify="required|phone|isPhoneRepeat"
               autocomplete="off">
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">证件号码<span>*</span></label>
        <input type="text" placeholder="请输入" name="idCard" id="expertCard" lay-verify="required|isIdCard|isIdCardRepeat"
               autocomplete="off">
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">工作单位<span>*</span></label>
        <input type="text" name="company" id="expertCompany" lay-verify="required" placeholder="请输入" autocomplete="off" maxlength="50">
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">评委身份<span>*</span></label>
        <div class="layui-input-block tab">
            <#list expertCategoryList as type>
                <input  id="radioInput_${type_index}" type="radio" value="${type.bookKey}" title="${type.bookValue}" name="category" lay-verify="required"
                        <#if type_index == 0>
                            checked
                        </#if>
                >
            </#list>
        </div>
    </div>
    <button id="submit-btn" class="layui-btn layui-hide" lay-filter="add-expert" lay-submit></button>
</form>
<div class="foot">
    <div class="btns">
        <span onclick="saveExpert()">保存</span>
        <span onclick="parent.layer.closeAll()">取消</span>
    </div>
</div>
</body>

<#--关键字搜索模板-->
<script id="searchTemplate" type="text/html">
    {{# layui.each(d,function(index, expert){ }}
        <li class="searchLi" searchIndex="{{index}}" onclick="choseExpert(this)">{{expert.expertName}}<span>{{expert.idCard}}</span></li>
    {{#  }); }}
</script>
<script>
    var searchList;
    layui.use(['form', 'layer', 'laytpl'], function () {
        var form = layui.form;
        var layer = layui.layer;
        laytpl = layui.laytpl;
        form.render();
        //表单验证
        form.verify({
            //验证身份证
            isIdCard: function (value) {
                return isIdCard(value);
            },
            isIdCardRepeat: function (value) {
                return isIdCardRepeat(value);
            },
            isPhoneRepeat: function (value) {
                return isPhoneRepeat(value);
            }
        });
        //添加专家信息
        form.on("submit(add-expert)", function () {
            var layetLoadIndex = window.top.layer.load();
            $.ajax({
                url: "${ctx}/expertInput/addExpert",
                type: "POST",
                cache: false,
                data: serializeObject($(".layui-form")),
                success: function (data) {
                    window.top.layer.close(layetLoadIndex);
                    if (data.code == "1") {
                        layer.msg("添加成功！", {icon: 1, time: 2000});
                        setTimeout(function () {
                            parent.window.location.reload();
                        },1500);
                    } else {
                        layer.msg(data.msg, {icon: 2, time: 2000});
                    }
                }
            });
            return false;
        });
    });

    /**
     *保存专家信息
     */
    function saveExpert() {
        $("#submit-btn").click();
    }

    /**
     * 身份证校验
     */
    function isIdCard(value) {
        var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
        if (reg.test(value) === false) {
            return '身份证输入有误，请重新输入！';
        }
    }

    /**
     * 身份证重复验证
     * @param value
     * @returns {string}
     */
    function isIdCardRepeat(value) {
        var data;
        $.ajax({
            url: "${ctx}/expertInput/isIdCardRepeat?idCard=" + value + "&bidSectionId=${bidSectionId}",
            dataType: 'json',
            type: "get",
            cache: false,
            async: false,
            success: function (result) {
                data = result;
            },
            error: function (e) {
                console.error(e);
                if(e.status == 403){
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }
            }
        });
        if (data) {
            // $("#loginName").val("");
            return '身份证重复，请重新输入';
        }
    }

    //电话号码重复验证
    function isPhoneRepeat(value) {
        var data;
        $.ajax({
            url: "${ctx}/expertInput/isPhoneRepeat?phone=" + value+ "&bidSectionId=${bidSectionId}",
            dataType: 'json',
            type: "get",
            cache: false,
            async: false,
            success: function (result) {
                data = result;
            },
            error: function (e) {
                console.error(e);
            }
        });
        if (data) {
            // $("#loginName").val("");
            return '电话号码重复，请重新输入';
        }
    }

    /**
     * 关键字搜索
     */
    function search() {
        var expertName = $("#expertName").val();
        $("#hotWords").html("");
        if (expertName != "" && expertName != undefined) {
            $.ajax({
                url: "${ctx}/expertInput/searchExpert",
                type: "POST",
                cache: false,
                data: {
                    "expertName": expertName
                },
                success: function (data) {

                    if (data.length > 0) {
                        searchList = data;
                        var getTpl = searchTemplate.innerHTML;
                        var view = document.getElementById("hotWords");
                        laytpl(getTpl).render(data, function (html) {
                            view.innerHTML = html;
                        });
                        $("#hotWords").show();
                    } else {
                        cancel();
                    }
                },
                error: function (e) {
                    console.error(e);
                    if(e.status == 403){
                        console.warn("用户登录失效！！！")
                        window.top.location.href = "/login.html";
                    }
                }
            });
        } else {
            cancel();
        }
    }

    function cancel() {
        setTimeout(function () {
            $("#hotWords").css("display","none");
        },200);

    }

    /**
     *填充专家信息
     */
    function choseExpert(elem) {
        var i = $(elem).attr("searchIndex");
        var expert = searchList[i];
        $("#expertName").val(expert.expertName);
        $("#expertPhone").val(expert.phoneNumber);
        $("#expertCard").val(expert.idCard);
        $("#expertCompany").val(expert.company);
        $("#hotWords").hide();
    }
</script>
</html>
