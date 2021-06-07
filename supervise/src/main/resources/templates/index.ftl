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
    <script src="${ctx}/layuiAdmin/layui/layui.js"></script>
<#--    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>-->
    <script type="text/javascript" src="${ctx}/js/webService.js"></script>
    <link rel="stylesheet" href="${ctx}/css/process.css">
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/list.css">

    <style>
        .postion-out {
            top: 9999px !important;
            left: 9999px !important;
            z-index: -88 !important;
            opacity: 0;
        }
    </style>
</head>
<body>
<div class="box">
    <!-- 头部 -->
    <#include "${ctx}/include/header.ftl">
    <section>
        <div class="head">
            <#-- 记录招标项目类型-->
            <input id="hidden-bid-type" type="hidden" value="A08">
            <#-- 记录评标状态-->
            <input id="evalStatus" type="hidden" value="">
            <ol class="head projectType">
                <li data-type="bid-type" data-id="A08" class="sele">施工</li>
                <li data-type="bid-type" data-id="A05">监理</li>
                <li data-type="bid-type" data-id="A03">勘察</li>
                <li data-type="bid-type" data-id="A04">设计</li>
                <li data-type="bid-type" data-id="A11" style="width: 115px;">电梯采购与安装</li>
                <li data-type="bid-type" data-id="A10">资格预审</li>
                <li data-type="bid-type" data-id="A12">工程总承包</li>
            </ol>
            <div class="head-form layui-form">
                <div class="layui-input-block">
                    <select name="city evalStateBox" lay-filter="evalStatus">
                        <option value=""></option>
                        <option value="0">未开始评标</option>
                        <option value="1">进行中评标</option>
                        <option value="2">已结束评标</option>
                    </select>
                </div>
                <div class="seach">
                    <input type="text" placeholder="请输入标段名称/编号" id="search-value">
                    <img src="${ctx}/img/seach.png" alt="" id="search-btn" onclick="page_search()">
                </div>
            </div>

        </div>

        <div class="cont">
            <ol>
                <li>序号</li>
                <li class="long">标段名称</li>
                <li>标段编号</li>
                <li>标段类型</li>
                <li>开标时间</li>
                <li>开标状态</li>
                <li class="long">操作</li>
            </ol>
            <ul id="projectContent">

            </ul>
        </div>

        <#--设置自定义分页-->
        <!--保存总记录数-->
        <input type="hidden" id="count">
        <div class="foot" id="page-temp" style="position: relative;"></div>
        <input id="curPage" type="hidden" value="1"/>
        <input id="pageSize" type="hidden" value="5"/>

        <form id="enter-bid-form" action="${ctx}/gov/bidOpen/baseGovPage" method="post" style="display: none">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <input type="hidden" name="bidSectionId" id="bid-section-id">
        </form>
        <#--        <button class="layui-btn" onclick="window.location.href='${ctx}/gov/anyChatScreen?bidSectionId=15'">音视频</button>-->
        <#--        <button class="layui-btn" onclick="window.location.href='${ctx}/gov/expert?bidSectionId=15'">专家</button>-->

    </section>
</div>
<#--项目模板。 -->
<script id="projectTemplate" type="text/html">
    {{# var startNum= ($("#curPage").val()-1)*($("#pageSize").val())}}
    {{#  layui.each(d, function(index, bid){ }}
    <li>
        <div>{{=startNum+index+1}}</div>
        <div class="long"><p>{{bid.bidSectionName||''}}</p></div>
        <div><p>{{bid.bidSectionCode||''}}</p></div>
        <div>{{bid.bidClassifyName||''}}</div>
        <div>{{bid.bidOpenTime||''}}</div>
        <#--开标状态名称 -->
        {{# if(bid.bidOpenStatus==0){ }}
        <div class="state organ">{{bid.bidOpenStatusName||''}}</div>
        {{# }else if(bid.bidOpenStatus == 1 || bid.bidOpenStatus == 3){ }}
        <div class="state qin">{{bid.bidOpenStatusName||''}}</div>
        {{# }else if(bid.bidOpenStatus == 2 || bid.bidOpenStatus == 4){ }}
        <div class="state over">{{bid.bidOpenStatusName||''}}</div>
        {{# } }}

        <div class="long">
            {{#if(bid.isOldProject == "1"){ }}
                <span class="blove-b" onclick="downFiles('{{=bid.id }}')">文件下载</span>
            {{# }else { }}
                <span class="blove-b" onclick="enterProject('{{=bid.id }}')">进入开标</span>
                {{#if(bid.evalReviewStatus == "1"){ }}
                <span class="green-b" onclick='agreeEval("{{=bid.id}}",this)'>同意评标</span>
                {{# } }}
                <span class="yellow-b option" onclick="moreClick(this)">更多
                    <ol>
                        {{# if(bid.bidOpenStatus==2){ }}
                            <li onclick="downFiles('{{=bid.id }}')">文件下载</li>
                        {{# } }}
                        <li onclick="startDownLoad('{{=bid.id }}', '1')">开启下载</li>
                        <li onclick="msgExport('{{=bid.id }}')">消息导出</li>
                    </ol>
                </span>
            {{# } }}
        </div>
    </li>
    {{# }); }}
    {{#  if(d.length === 0){ }}
    <li style="text-align: center;
    vertical-align: middle;
    line-height: 96px;
    font-size: 16px;
    font-weight: 700;
    color: #464646; ">暂无数据</li>
    {{#  } }}
</script>
<script>
    var layer, laytpl, laypage, element, form;
    layui.use(['form', 'table', 'layer'], function () {
        laypage = layui.laypage;
        laytpl = layui.laytpl;
        form = layui.form;
        form.render();
        form.on('select(evalStatus)', function(data){
            $("#evalStatus").val(data.value);
            page_search();
        });
        projectTypeClick();
        //初始化 数据分页
        pageShowProject(getProjectTotal());

    });

    /**
     * 分页查询
     */
    function page_search() {
        pageShowProject(getProjectTotal());
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
            page: true,
            jump: function (obj, first) {
                var curr = obj.curr;
                var pageSize = obj.limit;
                $("#curPage").val(curr);
                $("#pageSize").val(pageSize);
                // 展示当前页数据
                getProjectList(curr, pageSize);
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
            url: '${ctx}/gov/bidSection/listBidSection',
            type: 'post',
            cache: false,
            data: {
                page: curr,
                limit: pageSize,
                bidClassifyCode: $("#hidden-bid-type").val(),
                bidSectionCode: searchValue,
                bidSectionName: searchValue,
                evalStatus: $("#evalStatus").val(),
                deleteFlag: 0
            },
            success: function (data) {
                window.top.layer.close(indexLoad)
                if (!isNull(data)) {
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
            url: '${ctx}/gov/bidSection/getProjectTotal',
            type: 'post',
            cache: false,
            async: false,
            data: {
                bidClassifyCode: $("#hidden-bid-type").val(),
                bidSectionCode: searchValue,
                bidSectionName: searchValue,
                bidOpenStatus: $("#bidOpenStatus").val(),
                deleteFlag: 0
            },
            success: function (data) {
                if (data) {
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
     *  条件查询 点击事件绑定
     */
    function projectTypeClick() {
        var bindItem = $(".projectType").children();
        bindItem.bind('click', function () {
            // 点击按钮所属类型
            var type = $(this).attr("data-type");
            switch (type) {
                case "bid-type":
                    // 标段类型
                    $("#hidden-bid-type").val($(this).attr("data-id"));
                    $(this).addClass("sele").siblings().removeClass("sele");
                    break;
            }
            // 重新渲染分页
            page_search();
        });
    }


    /**
     * 同意评标
     * @param id 标段id
     * @param e 当前按钮
     */
    function agreeEval(id,e) {
        layer.confirm('是否同意开始评标?',{icon: 3, title: '操作提示'},function () {
            var loadIndex = window.top.layer.load(3);
            $.ajax({
                url: '${ctx}/gov/bidSection/agreeEval',
                type: 'post',
                cache: false,
                async: true,
                data: {
                    bidSectionId: id
                },
                success: function (data) {
                    layer.close(loadIndex);
                    if (data) {
                        $(e).hide();
                        layer.msg('操作成功', {icon: 1})
                    } else {
                        layer.msg('操作失败', {icon: 5})
                    }
                },
                error: function (data) {
                    console.log(data);
                    layer.close(loadIndex);
                    layer.msg('操作失败', {icon: 5})
                }
            });
        });

    }

    /**
     * 设置操作的标段ID
     * ***/
    function setBidSectionId(bidSectionId, callback) {
        $.ajax({
            url: '/gov/setBidSectionId',
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

    /**
     * 项目列表页更多下拉框得展开以及收缩
     */
    function moreClick(e) {
        if($(e).hasClass('drop-down')){
            $(e).removeClass('drop-down').addClass('option')
        }else{
            $(e).removeClass('option').addClass('drop-down')
        }
    }

    /**
     * 进入主管部门主界面
     */
    function enterProject(bidSectionId) {
        $("#bid-section-id").val(bidSectionId);
        $("#enter-bid-form").submit();
    }

    /**
     * 文件下载
     */
    function downFiles(bidSectionId) {
       setBidSectionId(bidSectionId,function () {
           window.open("${ctx}/gov/bidSection/downFilesPage", "_blank")
       });
    }

/**
 *  开启下载
 */
    function startDownLoad(id, status){
        // var loadIndex = window.top.layer.load(3);
        setBidSectionId(id, function () {
            $.ajax({
                url:"${ctx}/gov/bidSection/changeStartDownLoad",
                type: 'post',
                data: {
                    status: status
                },
                success: function (data) {
                    if (data) {
                        layer.msg('操作成功', {icon: 1})
                    } else {
                        layer.msg('操作失败', {icon: 5})
                    }
                },
                error: function (data) {
                    console.log(data);
                    // layer.close(loadIndex);
                    layer.msg('操作失败', {icon: 5})
                }
            });
        })
    }

    /**
     * 消息导出
     */
    function msgExport(id) {
        layerLoading();
        setBidSectionId(id,function () {
            initNoShowMesgBox(function(){
                $.ajax({
                    url: "${ctx}/onlineChat/getMessageExport",
                    type: "POST",
                    cache: false,
                    success: function (data) {
                        if (data) {
                            postMessageFun(data);
                            window.addEventListener("message",function (e) {
                                if (!isNull(e.data.importList)){
                                    // console.log(e.data.importList)
                                    msgPdfSynthesis(e.data.importList)
                                }else {
                                    loadComplete();
                                    clearNoShow();
                                    layerWarning("暂无消息记录！");
                                }
                            })
                        }
                    },
                    error:function (e) {
                        console.error(e);
                    }
                });
            });
        })
    }

    /**
     * 合成pdf
     */
    function msgPdfSynthesis(data) {
        $.ajax({
            url: "${ctx}/onlineChat/msgPdfSynthesis",
            type: "POST",
            cache: false,
            data: {
                "data": JSON.stringify(data)
            },
            success: function (result) {
                if (result.code == "1") {
                    var newUrl =result.data + "?filename=" + encodeURI(result.msg, "utf8");
                    window.location.href = encodeURI(newUrl, "utf8");
                }else {
                    layerWarning(result.msg);
                }
                loadComplete();
                clearNoShow();
            },
            error:function (e) {
                console.error(e);
            }
        });
    }

</script>


</body>
</html>