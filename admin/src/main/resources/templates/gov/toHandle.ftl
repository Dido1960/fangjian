<!DOCTYPE html>
<html lang="zh">
<head>
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
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
</head>
<body>

<div>
    <button type="button" id="handle" class="layui-btn layui-btn-normal red"  onclick="toHandle()">进入备案</button>
    <button type="button" id="next" class="layui-btn layui-btn-normal red"  onclick="toNext()" disabled>下一流程</button>
    <input type="hidden" id="partId">
    <input type="hidden" id="flowId">
</div>

</body>
<script>
    function toHandle() {
        $.ajax({
          url:'${ctx}/partAuditFlow/handleAllPart',
          type:'post',
          cache:false,
          async: false,
          data:{
              partId:1,
              hId:3
          },
          success:function(data){
         if(data.code=='1'){
             layer.alert("备案完成！",{icon:6},function () {
                 window.location.href="${ctx}/proProcess/handleButtonPage?partId=1&hId=3";
             })
         }else if(data.code=='0'){
             layer.confirm("该标段已经备案，是否直接前往？",{icon:9},function () {
                 window.location.href="${ctx}/proProcess/handleButtonPage?partId="+data.data.partId+"&hId="+data.data.flowId;
             })
         }else if(data.code=='2'){
             $("#partId").val(data.data.partId);
             $("#flowId").val(data.data.flowId);
             $("#next").removeAttr("disabled");
             layer.confirm("该环节已经审核完成，是否前往？",{icon:9},function () {

                 window.location.href="${ctx}/proProcess/handleButtonPage?partId="+data.data.partId+"&hId="+data.data.flowId;
             })
         }
          },
          error:function(data){

          },
        });
    }
    
    function toNext() {
        layer.confirm("确认进入下一模块吗",{icon:6},function () {
            $.ajax({
                url:'${ctx}/partAuditFlow/toNext',
                type:'post',
                cache:false,
                async: false,
                data:{
                    partId:$("#partId").val(),
                    flowId:$("#flowId").val()
                },
                success:function(data){
                    if(data!=null){
                        ;
                        window.location.href="${ctx}/proProcess/handleButtonPage?partId="+data.partId+"&hId="+data.flowId;
                    }
                },
                error:function(data){

                },
            });
        })

    }
</script>
</html>