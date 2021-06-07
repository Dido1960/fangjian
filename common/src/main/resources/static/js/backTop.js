$(function () {
  $(window).scroll(function () {
    var scrollt = document.documentElement.scrollTop + document.body.scrollTop;
    if (scrollt > 200) {
      $("#backTop").fadeIn(400);
    } else {
      $("#backTop").stop().fadeOut(400);
    }
  });
  $("#backTop").click(function () {
    $("html,body").animate({ scrollTop: "0px" }, 1000);
  });
  //前往主体
  $(document).on("click",".mainIndex",function () {
    location.href="/web/company/index";
  });

  getLoginUser();
});

function getLoginUser(){
  $.ajax({
    url: "/companyInfo/getCompanyInfo",
    type: "get",
    cache: false,
    async: true,
    success: function (data) {
      if (data.id!=null) {
        $(".nick").css("display","block");
        $(".no").css("display","none");
        $(".nick").text(data.name);
      }else {
        $(".no").css("display","block");
        $(".nick").css("display","none");
      }
    },
    error: function (e) {
      $(".no").css("display","block");
      $(".nick").css("display","none");
    }
  });
}

var ol1 = $(".o1");
var ol2 = $(".o2");
$(".btn1").on("click", function () {
  // var ol = $(".o1");
  ol1.attr("style", "display:block");
});
$(".btn2").on("click", function () {
  // var ol = $(".o2");
  ol2.attr("style", "display:block");
});
$(".banner").on("click", function () {
  ol1.attr("style", "display:none");
  ol2.attr("style", "display:none");
});
$(".btn").on("click", function () {
  var seach = $(".seach");
  seach.attr("style", "opacity:1");
});
$(".banner").on("click", function () {
  var seach = $(".seach");
  seach.attr("style", "opacity:0");
});
$(".nick").on("click", function () {
  var out = $(".out");
  out.attr("style", "display:block");
});
$(".banner").on("click", function () {
  var out = $(".out");
  out.attr("style", "display:none");
});



//前往登录的页面
$(".no").on("click",function () {
  location.href="/web/company/login.html";
});

//退出登录
$(".out").on("click",function () {
  $.ajax({
    url: '/home/loginOut',
    type: 'get',
    cache: false,
    async: false,
    data: {},
    success: function (data) {
      if (data){
        location.href='/home/homeIndexPage';
      }
    },
    error: function (data) {
    },
  });
});

