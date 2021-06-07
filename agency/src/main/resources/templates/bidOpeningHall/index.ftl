<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <title>开标大厅</title>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script src="${ctx}/js/webService.js"></script>
    <script type="text/javascript" src="${ctx}/js/like_num.js"></script>
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script type="text/javascript" src="${ctx}/plugin/baiduPlayer/cyberplayer-3.4.1/cyberplayer.js"></script>
    <script src="${ctx}/plugin/echarts.min.js"></script>

    <link rel="stylesheet" href="${ctx}/css/hallIndex.css">
</head>

<body>
<div class="cont">
    <h3> 甘肃省公共资源交易不见面开标大厅</h3>
    <div class="out-box">
        <div class="scroll-btn left" onclick="clickButton('left')">
            <img src="${ctx}/img/noface/left-icon.png" />
        </div>
        <div class="scroll-btn right" onclick="clickButton('right')">
            <img src="${ctx}/img/noface/right-icon.png" />
        </div>
        <div id="scrollBox">
            <ul>
                <li id="layerDemo" onclick="rolePage('${ctx}/bidConRolePage')">
                    <img src="${ctx}/img/building.png" alt="">
                    <span class="layui-btn double">市政房建</span>
                </li>
                <li id="layerDemo" onclick="rolePage('${ctx}/govRolePage')">
                    <img src="${ctx}/img/purchase.png" alt="">
                    <span class="layui-btn three">政府采购</span>
                </li>
                <li id="layerDemo" onclick="rolePage('${ctx}/trafficRolePage')">
                    <img src="${ctx}/img/highway.png" alt="">
                    <span class="layui-btn double">公路工程</span>
                </li>
                <li id="layerDemo" onclick="rolePage('${ctx}/waterRolePage')">
                    <img src="${ctx}/img/conservancy.png" alt="">
                    <span class="layui-btn double">水利工程</span>
                </li>
                <li id="layerDemo" onclick="rolePage('${ctx}/paperRolePage')">
                    <img src="${ctx}/img/noface/paper.png" alt="">
                    <span class="layui-btn double">纸质标</span>
                </li>
                <li id="layerDemo" onclick="rolePage('${ctx}/noTemplateRolePage')">
                    <img src="${ctx}/img/noface/no-template.png" alt="">
                    <span class="layui-btn double">无范本</span>
                </li>
            </ul>
        </div>
    </div>
</div>
<div class="foot">copyright © 2020</div>
</body>
<script>
    /**
     * 项目角色选择页面
     */
    function rolePage(url) {
        layer.open({
            type: 2
            , title: false //不显示标题栏
            , closeBtn: false
            , area: ['100%', '100%']
            , shade: 0.8
            , id: 'LAY_layuipro' //设定一个id，防止重复弹出
            , moveType: 1 //拖拽模式，0或者1
            , content: url
        });
    }
    var num;
    // 申明变量保存定时器
    var timer;
    // 获取滚动元素
    var scrollBox = $("#scrollBox")
    // 滚动元素宽度
    var scrollBoxWidth = scrollBox.width()
    // 获取子元素宽度之和
    var childsWidth = 0
    $("#scrollBox ul li").each(function (index, i) {
        childsWidth += $(i).width() + 80
    })
    childsWidth -= 80
    // 判断内部元素是否超出滚动元素宽度
    if (childsWidth > scrollBoxWidth) {
        // 获取超出距离计算需要滚动的次数，获取滚动n次后剩余的距离
        var tranlateX = childsWidth - scrollBoxWidth
        num = parseInt(Math.ceil(tranlateX / scrollBoxWidth))
        var _num = 0
        timer = setInterval(function () {
            if(_num === num) {
                _num  = 0
                $("#scrollBox ul").css("transform", "translateX(0)")
            } else {
                _num++
                $("#scrollBox ul").css("transform", "translateX(-" + (_num   * scrollBoxWidth + 80) + "px)")
            }
        }, 3000)
    }
    // 鼠标移入暂停滚动
    $("#scrollBox").mouseenter(function () {
        clearInterval(timer)
    })
    // 鼠标移除继续滚动
    $("#scrollBox").mouseleave(function () {
        timer = setInterval(function () {
            if(_num === num) {
                _num  = 0
                $("#scrollBox ul").css("transform", "translateX(0)")
            } else {
                _num++
                $("#scrollBox ul").css("transform", "translateX(-" + (_num   * scrollBoxWidth + 80) + "px)")
            }
        }, 3000)
    })
    /**
     * 点击按钮
     * @param type 按钮类型
     */
    function clickButton(type) {
        if(type === 'left') {
            if(_num === 0) {
                _num = num
            } else {
                _num--
            }
            var lateX = _num === 0 ? 0 : (_num  * scrollBoxWidth + 80);
            $("#scrollBox ul").css("transform", "translateX(-" + lateX + "px)")
        }else {
            if(_num === num) {
                _num  = 0
                $("#scrollBox ul").css("transform", "translateX(0)")
            } else {
                _num++
                $("#scrollBox ul").css("transform", "translateX(-" + (_num   * scrollBoxWidth + 80) + "px)")
            }
        }
    }
</script>

</html>