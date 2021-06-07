<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>甘肃省电子开评标平台</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <!--[if lt IE 9]>
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/plugin/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/header.css">
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/list.css">

</head>
<body>
<header>
    <div class="text">
        <div class="name">
            <img src="${ctx}/img/logo.png" alt="">
            甘肃省房建市政电子开标评标平台</div>
        <div class="bao">
            <div class="try">
                <b title="${user.name}">${user.name}</b>
                <i onclick="exitSystem()"></i>
            </div>
        </div>
    </div>
</header>
<section>
    <#-- 记录招标项目类型-->
    <input id="hidden-bid-type" type="hidden" value="A08">
    <#-- 记录开标地点-->
    <input id="hidden-bid-place" type="hidden" value="1">
    <ul class="head projectType">
        <li data-type="bid-type" data-id="A08" class="sele">施工</li>
        <li data-type="bid-type" data-id="A05">监理</li>
        <li data-type="bid-type" data-id="A03">勘察</li>
        <li data-type="bid-type" data-id="A04">设计</li>
        <li data-type="bid-type" data-id="A11" style="width: 130px;">电梯采购与安装</li>
        <li data-type="bid-type" data-id="A10">资格预审</li>
        <li data-type="bid-type" data-id="A12">施工总承包</li>
    </ul>
    <div class="cont">
        <div class="title">
            <h3>项目列表</h3>
            <div class="seach">
                <input type="text" placeholder="搜索关键词" id="search-value">
                <img src="${ctx}/img/seach.png" alt="" id="search-btn">
            </div>
        </div>
        <ol class="kind">
            <li>标段名称</li>
            <li>标段编号</li>
            <li>开标时间</li>
            <li>开标状态</li>
            <li>操作</li>
        </ol>
        <ul id="projectContent">
        </ul>
    </div>
    <div class="foot" id="page-temp" style="position: relative"></div>
</section>
</body>
<#--项目模板。 -->
<script id="projectTemplate" type="text/html">
    {{# layui.each(d,function(index, bidSection){ }}
    <li>
        <div>
            <p>{{bidSection.bidSectionName||'-'}}</p>
        </div>
        <div>{{bidSection.bidSectionCode||'-'}}</div>
        <div>{{bidSection.bidOpenTime||'-'}}</div>
        <div class="bidOpenStatus">{{bidSection.bidOpenStatusName||''}}</div>
        <div>
            {{#if(bidSection.isOldProject != "1"){ }}
                <span class="blove-b" onclick='expertInput("{{bidSection.id}}")'>进入</span>
            {{# } }}
        </div>
    </li>
    {{#  }); }}
</script>
<script>
    /**
     * 加载Layui组件
     */
    var layer, laytpl, laypage, element;
    layui.use(['layer', 'element', 'laytpl', 'laypage'], function () {
        layer = layui.layer;
        element = layui.element;
        laypage = layui.laypage;
        laytpl = layui.laytpl;
        //初始化 数据分页
        pageShowProject(getProjectTotal());
        //项目类型搜索点击事件加载
        projectTypeClick();
    });

    /**
     *   给开标状态添加样式
     */
    function initBidOpenStatus() {
        var bidOpenStatusDivs = $(".bidOpenStatus");
        bidOpenStatusDivs.each(function () {
            var bidOpenStatus = $(this).text().trim();
            if (bidOpenStatus == "进行中") {
                $(this).addClass("green-f");
            }
            if (bidOpenStatus == "未开始") {
                $(this).addClass("oragen-f");
            }
            if (bidOpenStatus == "结束") {
                $(this).addClass("red-f");
            }
        });


    }

    /**
     *   数据分页
     */
    function pageShowProject() {
        laypage.render({
            elem: 'page-temp',
            curr: 1,//当前页
            limit: 5,//页大小
            count: arguments[0],
            prev: "<i class='layui-icon layui-icon-left'></i>",//上一页图标
            next: "<i class='layui-icon layui-icon-right'></i>",//上一页图标
            limits: [5, 10, 15, 20],
            groups: 5,
            layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip'],
            // page: true,
            jump: function (obj, first) {
                // 展示当前页数据
                getProjectList(obj.curr, obj.limit);
                initBidOpenStatus();
                //首次不执行
                if (!first) {
                    // 页面加载时，显示全部
                }
            }
        });
    }

    /**
     * 获取数据并渲染(条件查询)
     * @param curr 页码
     * @param pageSize
     */
    function getProjectList(curr, pageSize) {
        var indexLoad = window.top.layer.load();
        var searchValue = $("#search-value").val().trim();

        $.ajax({
            url: '${ctx}/expertInput/listBidSection',
            type: 'post',
            cache: false,
            async: false,
            data: {
                page: curr,
                limit: pageSize,
                bidClassifyCode: $("#hidden-bid-type").val(),
                bidSectionCode: searchValue,
                bidSectionName: searchValue,
                deleteFlag: 0
            },
            success: function (data) {
                window.top.layer.close(indexLoad)
                if (!isNull(data)) {
                    // var datalist=JSON.parse(data);
                    var dataList = $.parseJSON(data);
                    $(".layui-laypage-count").text(" 共 " + dataList.count + " 条 ");
                    // 获取模版
                    var getTpl = projectTemplate.innerHTML;
                    var view = document.getElementById("projectContent");
                    // 渲染模版
                    laytpl(getTpl).render(dataList.data, function (html) {
                        view.innerHTML = html;
                    });
                }
            },
            error: function (data) {
                console.error(data);
                window.top.layer.close(indexLoad)
            },
        });

    }


    /**
     * 获取显示总条数
     */
    function getProjectTotal() {
        var searchValue = $("#search-value").val().trim();
        var count = 0;
        $.ajax({
            url: '${ctx}/expertInput/getProjectTotal',
            type: 'post',
            cache: false,
            async: false,
            data: {
                bidClassifyCode: $("#hidden-bid-type").val(),
                bidSectionCode: searchValue,
                bidSectionName: searchValue,
                deleteFlag: 0
            },
            success: function (data) {
                if (data) {
                    //$(".layui-laypage-count").text(" 共 " + data + " 条 ");
                    count = data;
                }
            },
            error: function (data) {
                console.error(data);
            },
        });

        return count;
    }

    /**
     * 进入专家录入
     */
    function expertInput(bidSectionId) {
        setBidSectionId(bidSectionId, function () {
            window.location.href = '${ctx}/expertInput/expertInput';
        });
    }

    /**
     * 项目类型、网上（线下）开标、模糊搜索 点击事件绑定
     */
    function projectTypeClick() {
        var bindItem = $(".projectType").children();
        bindItem.bind('click', function () {
            // 点击按钮所属类型
            var type = $(this).attr("data-type");
            switch (type) {
                case "bid-open-place":
                    // 开标地点
                    $("#hidden-bid-place").val($(this).attr("data-id"))
                    $(this).removeClass().addClass("blove-s blove-f")
                        .siblings().removeClass().addClass("gray-s gray-f");
                    break;
                case "bid-type":
                    // 标段类型
                    $("#hidden-bid-type").val($(this).attr("data-id"));
                    $(this).addClass("sele").siblings().removeClass("sele");
                    break;
            }
            // 重新分页渲染模板
            pageShowProject(getProjectTotal());
        });
        $("#search-btn").click(function () {
            // 重新分页渲染模板
            pageShowProject(getProjectTotal());
        });

    }

    /**
     * 设置操作的标段ID
     */
    function setBidSectionId(bidSectionId, callback) {
        $.ajax({
            url: '${ctx}/expertInput/setBidSectionId',
            type: 'post',
            cache: false,
            data: {
                bidSectionId: bidSectionId
            },
            success: function (data) {
                callback();
            },
            error: function (data) {
                console.warn(data);
                layer.msg("操作失败！")
            },
        });

    }
</script>

</html>