<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="utf-8">
    <title>部门选择</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_parameterName" content="${ _csrf.parameterName}"/>
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
    <style>
        form {
            width: 59%;
            height: 50%;
            margin: 0 auto;
            padding-top: 30px;
            min-width: 400px;
            min-height: 338px;
            box-sizing: border-box;
        }

        form label {
            display: block;
            width: 20%;
            height: 40px;
            line-height: 40px;
            text-align: center;
            font-size: 14px;
            font-family: Microsoft YaHei;
            font-weight: 500;
            color: rgba(36, 43, 41, 1);
            float: left;
        }

        .first {
            width: 100%;
            height: 40px;
            position: relative;
        }

        .second {
            width: 100%;
            height: 40px;
            position: relative;
        }

        form input {
            width: 70%;
            height: 40px;
            background: rgba(255, 255, 255, 1);
            border: 1px solid rgba(226, 226, 226, 1);
            opacity: 1;
            margin-left: 20px;
            font-size: 14px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            line-height: 40px;
            color: rgba(36, 43, 41, 1);
            padding: 0 15px;
            box-sizing: border-box;
            float: right;
        }

        .sele-f {
            width: 70%;
            height: 127px;
            background: rgba(255, 255, 255, 1);
            box-shadow: 0px 3px 10px rgba(0, 0, 0, 0.06);
            opacity: 1;
            position: absolute;
            right: 0;
            top: 102%;
            overflow: auto;
            z-index: 99999;
        }

        .sele-f::-webkit-scrollbar {
            display: none;
        }

        .sele-f li {
            width: 100%;
            height: 28px;
            padding: 0 10px;
            box-sizing: border-box;
            cursor: pointer;
        }

        .sele-f li div {
            width: 30%;
            height: 28px;
            font-size: 14px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            line-height: 28px;
            color: rgba(36, 43, 41, 1);
            float: left;
        }

        .sele-f li .cont {
            width: 50%;
            float: right;
            text-align: right;
        }

        .sele-f li .cont span {
            color: red;
        }

        .sele-f li:hover {
            background-color: rgba(0, 0, 0, 0.1);
        }

        .sele-s {
            width: 70%;
            height: 127px;
            background: rgba(255, 255, 255, 1);
            box-shadow: 0px 3px 10px rgba(0, 0, 0, 0.06);
            opacity: 1;
            position: absolute;
            right: 0;
            top: 102%;
            overflow: auto;
            z-index: 9999;
        }

        .sele-s::-webkit-scrollbar {
            display: none;
        }

        .sele-s li {
            width: 100%;
            height: 28px;
            padding: 0 10px;
            box-sizing: border-box;
            cursor: pointer;
        }

        .sele-s li div {
            width: 30%;
            height: 28px;
            font-size: 14px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            line-height: 28px;
            color: rgba(36, 43, 41, 1);
            float: left;
        }

        .sele-s li .cont {
            width: 50%;
            float: right;
            text-align: right;
        }

        .sele-s li .cont span {
            color: red;
        }

        .sele-s li:hover {
            background-color: rgba(0, 0, 0, 0.1);
        }

        .second {
            margin-top: 20px;
        }
    </style>
</head>

<body>
<form  class="layui-form">
    <form class="layui-form">
        <#list map?keys as key>
            <div class="layui-form-item" >
                <span>${key}</span>
                <select name="${key}">
                    <#list map["${key}"] as user>
                        <option key-data="${key}" value="${user.id}">${user.name} -----------待处理${user.handle}个</option>
                    </#list>
                </select>

            </div>
        </#list>
    </form>
    <#list map?keys as key>
    <div class="first">
        <label for="first">${key}</label>
        <input type="text" id="first" value="">
        <ul class="sele-f" style="display: none;">
            <#list map["${key}"] as user>
            <li  key-data="${key}"  value="${user.id}">
                <div class="name">${user.name}</div>
                <div class="cont">
                    待处理：<span>${user.handle}个</span>
                </div>
            </li>
            </#list>
        </ul>
    </div>
    </#list>
    <!-- <label for="second">业务复审</label>
    <input type="text" id="second"> -->
</form>

</body>

</html>










<#--
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>部门选择</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_parameterName" content="${ _csrf.parameterName}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-1.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-migrate-3.1.0.min.js"></script>
    <!--[if lt IE 9]>
    <script src="${ctx}/js/html5shiv.min.js"></script>
    <script src="${ctx}/js/respond.min.js"></script>
    <![endif]&ndash;&gt;
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
</head>
<body class="layui-layout-body">
<div id="LAY_app">
            <form class="layui-form">
                <#list map?keys as key>
                    <div class="layui-form-item" >
                        <span>${key}</span>
                            <select name="${key}">
                                <#list map["${key}"] as user>
                                    <option key-data="${key}" value="${user.id}">${user.name} -----------待处理${user.handle}个</option>
                                </#list>
                            </select>

                    </div>
                </#list>
            </form>


</div>

<script src="${ctx}/layuiAdmin/layui/layui.js?t=1"></script>
<script type="text/javascript">
    layui.use(['form', 'layer'], function () {
        var form = layui.form;
        form.render();
    });





    function configGovUser() {
        var map = new Map();
        $("select option:selected").each(function () {
           map.set($(this).attr("key-data"),$(this).val());

        })
        console.log(map);

        var obj= Object.create(null);
        for (let[k,v] of map) {
            obj[k] = v;
        }

        var maps=JSON.stringify(obj);
        $.ajax({
          url:'${ctx}/partAuditFlow/distributePart',
          type:'post',
          cache:false,
          async: false,
          data: {map:maps,id:'${flow.id}'}
          ,
          success:function(data){
          if(data){
              layer.alert("分发完成！",{icon:6},function () {
                window.parent.location.reload();
              })
          }else {
              layer.alert("分发失败！",{icon:5},function () {
                  window.parent.location.reload();
              })
          }
          },
          error:function(data){

          },
        });
    }



</script>
</body>
</html>

-->
