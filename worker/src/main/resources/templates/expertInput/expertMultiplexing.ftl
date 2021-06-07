<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>专家复用</title>
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
    <link rel="stylesheet" href="${ctx}/css/multiplexing.css">
</head>
<body>
<div class="bos">
    <div class="cont-top">
        <ol>
            <li>标段名称</li>
            <li>标段编号</li>
            <li>操作</li>
        </ol>
        <div class="center">
            <#list list as bidSecton>
                <div class="center">
                    <div class="center-top">
                        <div>${bidSecton.bidSectionName}</div>
                        <div>${bidSecton.bidSectionCode}</div>
                        <div>
                            <span onclick="checkAll('checkBis_${bidSecton.id}')">使用</span>
                            <img src="${ctx}/img/down2.png" alt="">
                        </div>
                    </div>
                    <table class="sele layui-table" lay-skin="line" style="display: none;">
                        <thead>
                        <tr>
                            <th>评委名称</th>
                            <th>工作单位</th>
                            <th>证件号码</th>
                            <th>手机号码</th>
                            <th>评委身份</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="checkBis_${bidSecton.id}">
                        <#list bidSecton.expertList as expert>
                            <input type="hidden" class="checkExpert" data-value="${expert.id}" expertType="${expert.categoryName}">
                            <tr>
                                <td>${expert.expertName}</td>
                                <td>${expert.company}</td>
                                <td>${expert.idCard}</td>
                                <td>${expert.phoneNumber}</td>
                                <td>${expert.categoryName}</td>
                                <td>
                                    <span onclick="addExpertList('${expert.id}',<#if expert.category == 3>1<#else>0</#if>)">复用</span>
                                </td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
            </#list>
        </div>
    </div>
</div>
<script>
    function checkAll(id) {
        var ids = new Array();
        var representativeCount = 0;
        var i = 0;
        $("#"+id).find(".checkExpert").each(function (index) {
            ids[i] = $(this).attr("data-value");
            if ($(this).attr("expertType") == "业主代表"){
                representativeCount++;
            }
            i++;
        })

        addExpertList(ids , representativeCount);
        event.stopPropagation();
    }

    var lock = false;
    function addExpertList(ids , representativeCount) {
        if (lock) {
            return false;
        }
        lock = true;
        if (ids.length == 0) {
            layer.msg("请选择专家！", {icon: 2, time: 2000});
            return false;
        } else {
            var layerLoadIndex = layer.load(1, {shade: [0.1, '#fff']});
            $.ajax({
                url: "${ctx}/expertInput/addExpertList",
                type: "POST",
                cache: false,
                data: {
                    "bidSectionId": '${bsId}',
                    "ids": ids,
                    "representativeCount": representativeCount
                },
                traditional: true,
                success: function (data) {
                    lock = false;
                    if (data) {
                        layer.msg("添加成功", {icon: 1, time: 2000,end: function () {
                                parent.goToUrl("${ctx}/expertInput/expertInputPage");
                                parent.layer.closeAll();
                            }});
                    } else {
                        layer.msg("添加失败,请确认录入人数！", {icon: 2, time: 2000});
                    }
                    layer.close(layerLoadIndex);
                },
                error: function (e) {
                    console.error(e);
                    layer.close(layerLoadIndex);
                    lock = false;
                    if(e.status == 403){
                        console.warn("用户登录失效！！！")
                        window.top.location.href = "/login.html";
                    }
                }
            });
        }
    }
</script>
<script>
    var lock = false;
    $('.center').on('click', '.center-top', function () {
        if (lock){
            return false;
        }
        lock = true;
        if ($(this).parent('.center').children('.sele').css('display') == 'none') {
            $(this).parent('.center').children('.sele').fadeIn(300);
            $(this).parent('.center').siblings().children('.sele').hide();
            setTimeout(function () {
                lock = false;
            },500);
        } else {
            $(this).parent('.center').children('.sele').fadeOut(500);
            setTimeout(function () {
                lock = false;
            },500);
        }
    });
</script>
</body>
</html>