<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>不见面开标大厅</title>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <link rel="stylesheet" href="${ctx}/css/noFaceIndex.css">
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script src="${ctx}/js/common.js"></script>
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
</head>

<body>
<div class="cont">
    <h3> 甘肃省公共资源交易不见面开标大厅</h3>
    <ul>
        <li onclick="goToIndex('${conListPage}')">
            <img src="${ctx}/img/building.png" alt="">
            <span>市政房建</span>
        </li>
        <li onclick="goToIndex('${govListPage}')">
            <img src="${ctx}/img/purchase.png" alt="">
            <span>政府采购</span>
        </li>
        <li onclick="goToIndex('${trafficListPage}')">
            <img src="${ctx}/img/highway.png" alt="">
            <span>公路工程</span>
        </li>
        <li onclick="goToIndex('${waterListPage}')">
            <img src="${ctx}/img/conservancy.png" alt="">
            <span>水利工程</span>
        </li>
    </ul>
</div>
<div class="foot">copyright © 2020</div>
</body>
<script>
    /**
     * 跳转不见面开标列表页面
     * @param url
     */
    function goToIndex(url) {
        if (!isNull(url)) {
            window.location.href = url;
        }else {
            layer.msg("敬请期待!", {icon: 1});
        }
    }
</script>

</html>