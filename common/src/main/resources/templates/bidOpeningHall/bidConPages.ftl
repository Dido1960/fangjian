<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <title>开标大厅</title>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <link rel="stylesheet" href="${ctx}/css/bidConPages.css">
</head>

<body>
<div class="box">
    <h3 class="caption">
        <#if active=="test">
            测试服
        <#elseif active=="dev">
            开发
        <#else>
        </#if>
    </h3>

    <ul class="center">
        <li>
            <div class="top">
                <img src="../img/change_1.png" alt="">
            </div>
            <div class="bottom">
                <span class="touch">开标人员</span>
            </div>
        </li>
        <li>
            <div class="top">
                <img src="../img/change_2.png" alt="">
            </div>
            <div class="bottom">
                <span class="touch">投标人员</span>
            </div>
        </li>
        <li>
            <div class="top">
                <img src="../img/change_3.png" alt="">
            </div>
            <div class="bottom">
                <span class="touch">专家录入</span>
            </div>
        </li>
        <li>
            <div class="top">
                <img src="../img/change_4.png" alt="">
            </div>
            <div class="bottom">
                <span class="touch">评标专家</span>
            </div>
        </li>
        <li>
            <div class="top">
                <img src="../img/change_5.png" alt="">
            </div>
            <div class="bottom">
                <span class="touch">主管部门</span>
            </div>
        </li>
    </ul>
    <div class="foot">copyright © 2020</div>
</div>
</body>
<script>
    function bidConRolePage() {

        layer.open({
            type: 2
            , title: false //不显示标题栏
            , closeBtn: false
            , area: ['100%', '100%']
            , shade: 0.8
            , id: 'LAY_layuipro' //设定一个id，防止重复弹出
            , moveType: 1 //拖拽模式，0或者1
            , content: '${ctx}/bidConRolePage'
        });
    }


    <#--layui.use('layer', function () { //独立版的layer无需执行这一句-->
    <#--    var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句-->

    <#--    //触发事件-->
    <#--    var active = {-->
    <#--        notice: function () {-->
    <#--            //示范一个公告层-->
    <#--            layer.open({-->
    <#--                type: 2-->
    <#--                , title: false //不显示标题栏-->
    <#--                , closeBtn: false-->
    <#--                , area: ['100%', '100%']-->
    <#--                , shade: 0.8-->
    <#--                , id: 'LAY_layuipro' //设定一个id，防止重复弹出-->
    <#--                , moveType: 1 //拖拽模式，0或者1-->
    <#--                , content: '${ctx}/govRolePage'-->
    <#--            });-->
    <#--        }-->

    <#--    };-->
    <#--    $('#layerDemo .three').on('click', function () {-->
    <#--        var othis = $(this), method = othis.data('method');-->
    <#--        active[method] ? active[method].call(this, othis) : '';-->
    <#--    });-->

    <#--});-->
</script>

</html>