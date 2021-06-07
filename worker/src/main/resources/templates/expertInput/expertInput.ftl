<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>甘肃省电子开评标平台</title>
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
    <link rel="stylesheet" href="${ctx}/css/header.css">
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/judges.css">
</head>
<body>
<header>
    <div class="text">
        <div class="name">
            <img src="${ctx}/img/logo.png" alt="">
            甘肃省房建市政电子开标评标平台
        </div>
        <div class="bao">
            <div onclick="window.location.href='${ctx}/index'" class="off blove-f" >返回项目管理</div>
            <div class="try">
                <b title="${user.name}">${user.name}</b>
                <i onclick="exitSystem()"></i>
            </div>
        </div>
    </div>
</header>
<section>
    <h3 class="head">确定评委<span class="current-time">2020年9月19日 15:14:30 星期一</span></h3>
    <div class="total">
        <div class="total-l">
            <div class="blove-b" onclick="print()">打印</div>
            <#if !bidApply.chairMan ?? || bidApply.evalStatus != ''>
                <div class="blove-b" onclick="expertMultiplexingPage()">专家复用</div>
            </#if>
        </div>
        共需专家:${bidSection.tenderDoc.expertCount}人
        <div class="total-r">(其中包含业主代表:<span>${bidSection.tenderDoc.representativeCount}人</span>)已录入专家：<span id="entered">${eListSize}人</span>
        </div>
    </div>
    <#if !bidApply.chairMan ?? || bidApply.evalStatus != ''>
        <div class="addJudges" onclick="addExpert()">
            <div class="aj-cen"><i></i>新增评委</div>
        </div>
    </#if>
    <ul class="cont">
        <#list expertList as expert>
            <li>
                <img src="${ctx}/img/face.png" alt="">
                <div class="right">
                    <p>评委姓名:${expert.expertName}</p>
                    <p>工作单位:${expert.company}</p>
                    <p>评委身份:${expert.categoryName}</p>
                </div>
                <#if !bidApply.chairMan ?? || bidApply.evalStatus != ''>
                    <div class="tips" onclick="deleteExpert(${expert.id},this)"></div>
                </#if>
                <div class="group">已签到</div>
            </li>
        </#list>
    </ul>
</section>

<script>
    var eListSize = '${eListSize}';
    var expertCount = '${bidSection.tenderDoc.expertCount}';
    var laytpl;
    layui.use(['form', 'layer', 'laytpl'], function () {
        var form = layui.form;
        var layer = layui.layer;
        laytpl = layui.laytpl;
        form.render();
        setInterval(showtime,1000);
    });

    /**
     * 新增专家评委
     */
    function addExpert() {
        //判断是否开标结束或流标
        if ('${bidSection.bidOpenStatus}' != '2'){
            layer.msg("开标尚未结束，无法添加专家！", {icon: 2, time: 2000});
            return false;
        }
        if ('${bidSection.cancelStatus}' == '1'){
            layer.msg("项目招标失败，无法添加专家！", {icon: 2, time: 2000});
            return false;
        }

        if (parseInt(expertCount) > parseInt(eListSize)) {
            layer.open({
                type: 2,
                title: "新增评委",
                content: "${ctx}/expertInput/addExpertPage?bidSectionId=${bidSection.id}",
                offset: 'auto',
                area: ['40%', '60%']
            });
        } else {
            layer.msg("专家人数已满，无法添加！", {icon: 2, time: 2000});
        }
    }

    /**
     * 删除专家评委
     */
    function deleteExpert(id,obj) {
        layer.confirm('确定删除当前专家吗？',{
            btn: ['确定','取消'],
        }, function(){
            var layerLoadIndex = layer.load(1, {shade: [0.1, '#fff']});
            $.ajax({
                url: "${ctx}/expertInput/deleteExpert",
                type: "POST",
                cache: false,
                data: {"id": id},
                success: function (data) {
                    if (data) {
                        layer.msg("删除成功！",{icon: 1,time: 2000});
                        $(obj).parent().remove();
                        eListSize -= 1;
                        $("#entered").text(eListSize);
                    }
                    layer.close(layerLoadIndex);
                },
                error: function (e) {
                    console.error(e);
                    layer.msg("删除失败！",{icon: 2,time: 2000});
                    layer.close(layerLoadIndex);
                    if(e.status == 403){
                        console.warn("用户登录失效！！！")
                        window.top.location.href = "/login.html";
                    }
                }
            });
            layer.closeAll();
        }, function(){
        });
    }


    /**
     * 打印专家信息
     */
    function print() {
        if (getLodop()) {
            layer.open({
                type: 2,
                title: "打印预览",
                content: "${ctx}/expertInput/printExpert?bidSectionId=${bidSection.id}",
                offset: 'auto',
                shadeClose: true,
                area: ['80%', '70%'],
                resize: false,
                move: false,
                //btn: ['打印'],
                btnAlign: 'c',
                btn1: function (index, layero) {
                    var body = window.layer.getChildFrame('body', index);
                    var iframeWin = window[layero.find('iframe')[0]['name']];
                    iframeWin.printExpert(function () {
                        window.layer.close(index);
                    });
                }
            });
        }
    }

    /**
     * 专家复用
     */
    function expertMultiplexingPage() {
        //判断是否开标结束或流标
        if ('${bidSection.bidOpenStatus}' != '2'){
            layer.msg("开标尚未结束，无法复用专家！", {icon: 2, time: 2000});
            return false;
        }
        if ('${bidSection.cancelStatus}' == '1'){
            layer.msg("项目招标失败，无法复用专家！", {icon: 2, time: 2000});
            return false;
        }
        if (parseInt(expertCount) > parseInt(eListSize)) {
            layer.open({
                type: 2,
                title: "专家复用",
                content: "${ctx}/expertInput/expertMultiplexingPage?id=${bidSection.id}&tenderProjectId=${bidSection.tenderProjectId}",
                offset: 'auto',
                resize: false,
                move: false,
                area: ['50%', '80%']
            });
        } else {
            layer.msg("专家人数已达上限！", {icon: 2, time: 2000});
        }
    }


    /**
     * 展示当前时间
     */
    function showtime() {
        var nowTime = new Date();
        // 年 月 日
        var year = timeAdd0(nowTime.getFullYear().toString());
        var month = timeAdd0((nowTime.getMonth() + 1).toString());
        var day = timeAdd0(nowTime.getDate().toString());
        var date = year + "年" + month + "月" + day + "日" + " ";
        // 时 分 秒
        var hours = timeAdd0(nowTime.getHours().toString());
        var min = timeAdd0(nowTime.getMinutes().toString());
        var sen = timeAdd0(nowTime.getSeconds().toString());
        var time = hours + ":" + min + ":" + sen+ " ";
        // 星期几
        var weekArr = ["日", "一", "二", "三", "四", "五", "六"];
        var week = "星期" + weekArr[nowTime.getDay()];

        $(".current-time").text(date + time + week);
    }

</script>
</body>
</html>
