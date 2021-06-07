<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>政府人员管理</title>
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

</head>
<body class="layui-layout-body">
<div id="LAY_app">
    <p class="layui-form-label">${now.flowItemName}---- ${now.partId} -------${now.govUser.name}</p>
    <div class="layui-layout layui-layout-admin">
        <div class="layui-card">
            <form class="layui-form">
                <#-- 区划id-->
                <div class="layui-form-item">
                    <div class="layui-inline" style="margin-left: -40px">
                        <label class="layui-form-label">姓名</label>
                        <div class="layui-input-block">
                            <input type="text" name="name" id="name" autocomplete="off" class="layui-input">
                        </div>
                    </div>

                </div>

                <div class="layui-inline" style="margin-left: -50px">
                    <div class="layui-input-block">
                        <button type="button" id="handle" class="layui-btn layui-btn-normal">请先备案！！</button>
                        <button type="button" id="back" class="layui-btn layui-btn-normal red" onclick="handleBack()">退回</button>
                        <button type="button" id="again" class="layui-btn layui-btn-normal red"  onclick="toNowHandle()">二次澄清</button>
                        <button type="button" id="twice" class="layui-btn layui-btn-normal red"  onclick="twiceHandle()">重新备案</button>


                    </div>
                </div>

                <div style="overflow: auto; height: 600px">
                    <ul class="layui-timeline">
                        <#list logs as log>
                            <li class="layui-timeline-item">
                                <i class="layui-icon layui-timeline-axis">&#xe63f;</i>
                                <div class="layui-timeline-content layui-text">
                                    <h3 class="layui-timeline-title">${log.insertTime}</h3>
                                    <p>用户 ${log.GId} ${log.flowItemName}·····</p>
                                </div>
                            </li>
                        </#list>
                    </ul>
                </div>
        </div>
        </form>
        <#-- 展示数据 -->
        <table class="layui-hide" id="list-gov-user" lay-filter="operate"></table>
    </div>
</div>
</div>


<script src="${ctx}/layuiAdmin/layui/layui.js?t=1"></script>


<script type="text/javascript">


    // 新增人员弹窗
    var add_user_index;

    // 人员查看弹窗
    var show_user_index;

    var tableIns;


    $(function () {


        layui.use(['layer'], function () {

            var layer = layui.layer;
        })

        if('${now.flowItemType}'==''||'${now.flowItemType}'=='0'&& '${now.flowItemState}'=='1'){

            $("#handle").text("受理")
            $("#handle").click(function () {
                if('${now.GId}'==''){
                    $.ajax({
                        url:'/proProcess/hasGovUsers?id=${now.flowItemId}',
                      type:'post',
                      cache:false,
                      async: false,
                      data:{
                      },
                      success:function(data) {
                          if (data) {
                              layer.confirm("当前流程有多个审批用户，请选择一位", {icon: 8}, function () {
                                  window.layer.open({
                                      type: 2,
                                      title: '人员选择',
                                      shadeClose: true,
                                      area: ['50%', '50%'],
                                      btn: ['分发', '取消'],
                                      content: '${ctx}/user/pageChoose?pId=${now.flowItemId}&id=${now.id}',
                                      btn1: function (index, layero) {
                                          var body = window.layer.getChildFrame('body', index);
                                          var iframeWin = window[layero.find('iframe')[0]['name']];
                                          iframeWin.configGovUser(function () {
                                              window.layer.close(index);
                                              renderTable();
                                          });
                                      },
                                      btn2: function (index) {
                                          window.layer.close(index);
                                      }
                                  });
                              })
                          } else {
                              layer.alert("该环节暂未配置人员！", {icon: 5}, function () {
                                  window.location.reload();
                              })
                          }
                      }
                    });

                }else {
                    layer.confirm("确定受理通过吗？",{icon:9},function () {
                        var indexLoad= layer.load();
                        $.ajax({
                            url:'${ctx}/partAuditFlow/handlePart',
                            type:'post',
                            cache:false,
                            async: false,
                            data:{
                                id:'${now.id}'
                            },
                            success:function(data){
                                if(data){
                                    layer.alert("受理成功！",{icon:6},function () {
                                        window.location.reload();
                                    })
                                }else {
                                    layer.alert("受理失败！",{icon:5},function () {
                                        window.location.reload();
                                    })
                                }
                            },
                            error:function(data){

                            },
                        });
                    })
                }

            })
        }else if('${now.flowItemType}'=='1'){

            $("#handle").text("分发")
            $("#handle").click(function () {

                if('${now.GId}'==''){
                    $.ajax({
                      url:'/proProcess/hasGovUsers?id=${now.flowItemId}',
                      type:'post',
                      cache:false,
                      async: false,
                      data:{
                      },
                      success:function(data){
                          if(data){
                              layer.confirm("当前流程有多个审批用户，请选择一位",{icon:8},function () {
                                  window.layer.open({
                                      type: 2,
                                      title: '人员选择',
                                      shadeClose: true,
                                      area: ['50%', '50%'],
                                      btn: ['分发', '取消'],
                                      content: '${ctx}/user/pageChoose?pId=${now.flowItemId}&id=${now.id}',
                                      btn1: function (index, layero) {
                                          var body = window.layer.getChildFrame('body', index);
                                          var iframeWin = window[layero.find('iframe')[0]['name']];
                                          iframeWin.configGovUser(function () {
                                              window.layer.close(index);
                                              renderTable();
                                          });
                                      },
                                      btn2: function (index) {
                                          window.layer.close(index);
                                      }
                                  });
                              })
                          }else {
                              layer.alert("该环节暂未配置人员！",{icon:5},function () {
                                window.location.reload();
                              })
                          }
                      },
                      error:function(data){
                         console.error(data);
                         layer.close(indexLoad)
                      },
                    });

                }else {

                    window.layer.open({
                        type: 2,
                        title: '业务分发',
                        shadeClose: true,
                        area: ['70%', '50%'],
                        btn: ['分发', '取消'],
                        content: '${ctx}/partAuditFlow/distributePartPage?partId=${now.partId}&flowId=${now.flowId}',
                        btn1: function (index, layero) {
                            var body = window.layer.getChildFrame('body', index);
                            var iframeWin = window[layero.find('iframe')[0]['name']];
                            iframeWin.configGovUser(function () {
                                window.layer.close(index);
                                renderTable();
                            });
                        },
                        btn2: function (index) {
                            window.layer.close(index);
                        }
                    });
                }

            })
        }else if('${now.flowItemType}'=='2'){
            $("#handle").text("审核")
            $("#handle").click(function () {
                layer.confirm("确定审核通过吗？",{icon:9},function () {
                    var indexLoad= layer.load();
                    $.ajax({
                        url:'${ctx}/partAuditFlow/examinePart',
                        type:'post',
                        cache:false,
                        async: false,
                        data:{
                            id:'${now.id}'
                        },
                        success:function(data){
                            if(data.code=='1'){
                                layer.alert("审核成功！",{icon:6},function () {
                                    window.location.reload();
                                })
                            }else if(data.code=='0'){
                                layer.alert("审核失败！",{icon:5},function () {
                                    window.location.reload();
                                })
                            }else if(data.code=='2'){
                                $("#handle").css("display","none");
                               layer.alert("该环节已经审核完成",{icon:6},function () {
                                   window.location.reload();
                                })
                            }else if(data.code=='3'){
                                layer.alert(data.msg,{icon:6},function () {
                                    window.location.reload();
                                })
                            }
                        },
                        error:function(data){
                            layer.alert("审核失败！",{icon:5},function () {
                                window.location.reload();
                            })
                        },
                    });
                })
            })
        }

        if('${now.flowItemState==2}'){
            $("#handle").css("display","none");
        }
    })
    
    function handleBack() {

       var back_layer_index=window.layer.open({
            type: 2,
            title: '退回选择',
            shadeClose: true,
            area: ['30%', '40%'],
            btn: ['退回', '取消'],
            content: '${ctx}/partAuditFlow/handleBackPage?id='+${now.id},
            btn1: function (index, layero) {
                var body = window.layer.getChildFrame('body', index);
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.submitGovUser(function () {
                    window.top.layer.alert("退回完成！",{icon:6})
                    window.location.reload();
                },index);


            },
            btn2: function (index) {
                window.layer.close(index);
            }
        });
    }


function toNowHandle() {

        $.ajax({
          url:'${ctx}/partAuditFlow/lastFlow',
          type:'post',
          cache:false,
          async: false,
          data:{
              partId:'${now.partId}',
              flowId:'${now.flowId}'
          },
          success:function(data){
            if(data){
                layer.confirm("确定要重新备案吗",{icon:9},function () {
                    $.ajax({
                        url:'${ctx}/partAuditFlow/reFiling',
                        type:'post',
                        cache:false,
                        async: false,
                        data:{
                            id:'${now.flowId}',
                            partId:'${now.partId}'
                        },
                        success:function(data){
                            if(data){
                                layer.alert("重新备案成功",{icon:6},function () {
                                    window.location.reload();
                                })
                            }
                        },
                        error:function(data){
                            console.error(data);

                        },
                    });
                })
            }else {
                layer.alert("当前流程未审核完，无法重新备案",{icon:6});
            }

          },
          error:function(data){
             console.error(data);
          },
        });

}

function twiceHandle() {
    layer.confirm("确定二次备案吗",{icon:9},function () {
        $.ajax({
          url:'${ctx}/partAuditFlow/twiceHandle',
          type:'post',
          cache:false,
          async: false,
          data:{
              partId:'${now.partId}'
          },
          success:function(data){
                if(data){
                    layer.alert("二次备案成功",{icon:6},function () {
                        window.location.reload();
                    })
                }else {
                    layer.alert("二次备案失败",{icon:5},function () {
                        window.location.reload();
                    })
                }
          },
          error:function(data){

          },
        });
    })
}









</script>
</body>
</html>

