function handlerScroll(className) {
    // 获取包括上下箭头、滚动盒子的元素
    var bigElement = $(".section-right-list");
    // 获取滚动盒子的高度
    var scrollBoxHeight = $(".section-right-list-center").height();
    //动态设置li高度
    $(".section-right-list-center " + className).find("li").height((scrollBoxHeight - 40) / 3)
    setTimeout(function () {
        // 获取滚动盒子内部的元素
        var scrollChildEle = $(".section-right-list-center " + className);
        // 获取滚动盒子内部的元素的整体高度
        var scrollChildEleHeight = scrollChildEle.height() - 20;
        // 获取滚动盒子内部元素整体高度与滚动盒子高度相除的值，获取需要滚动的次数
        var scrollNum = Math.ceil(scrollChildEleHeight / scrollBoxHeight);
        // 声明标量保存当前已滚动的次数
        var currentNum = 0;
        // 初始化定时器
        var timer;

        // 设置自动滚动函数，5s自动滚动一次
        function scrollFun() {
            if (scrollNum > 1) {
                timer = setInterval(function () {
                    if (currentNum >= scrollNum) {
                        currentNum = 1;
                        scrollChildEle.css("transform", "translateY(0px)");
                    } else {
                        scrollChildEle.css(
                            "transform",
                            "translateY(-" +
                            currentNum * (scrollBoxHeight + 19) +
                            "px)"
                        );
                        currentNum++;
                    }
                }, 5000);
            }
        }

        // scrollFun();
        // 鼠标移入时清除定时器
        bigElement.mouseenter(function () {
            if (timer) {
                clearInterval(timer);
            }
        });
        // 鼠标移入时恢复定时器
        bigElement.mouseleave(function () {
            // scrollFun();
        });
        // 上箭头点击事件
        $("#topButton").click(function () {
            if (scrollNum <= 1) return;
            if (currentNum <= 1) {
                currentNum = scrollNum;
                scrollChildEle.css(
                    "transform",
                    "translateY(-" + (scrollChildEleHeight - 180) + "px)"
                );
            } else {
                currentNum--;
                scrollChildEle.css(
                    "transform",
                    "translateY(-" +
                    (currentNum - 1) * (scrollBoxHeight + 19) +
                    "px)"
                );
            }
        });
        // 下箭头点击事件
        $("#bottomButton").click(function () {
            if (scrollNum <= 1) return;
            if (currentNum >= scrollNum - 1) {
                scrollChildEle.css("transform", "translateY(0px)");
                currentNum = 0;
            } else {
                scrollChildEle.css(
                    "transform",
                    "translateY(-" +
                    (currentNum + 1) * (scrollBoxHeight + 19) +
                    "px)"
                );
                currentNum++;
            }
        });
    }, 500)
}

handlerScroll(".endBidUl")

// 直播画面切换示例
function toggleDemo() {
    var placeholderImg = $("#placeholderImg");
    var liveLoading = $(".section-left_box .box-live-loading");
    var liveFail = $(".section-left_box .box-live-fail");
    var timer_1, timer_2;
    timer_1 = setTimeout(function () {
        placeholderImg.css("display", "none");
        liveLoading.css("display", "block");
        liveFail.css("display", "none");
        clearTimeout(timer_1);
        timer_2 = setTimeout(function () {
            placeholderImg.css("display", "none");
            liveLoading.css("display", "none");
            liveFail.css("display", "block");
            clearTimeout(timer_2);
        }, 2000);
    }, 2000);
}

// toggleDemo();
var mouseenter = false;
// 鼠标移入列表显示直播画面
$(".section-right-list-center li").mouseenter(function () {
    if (!mouseenter) {
        closeplay();
        mouseenter = true;
        console.log('mouseenter-移入')
        var status = $(this).attr("data-status");
        var playId = $(this).attr("data-playId");
        var liveRoom = $(this).attr("data-liveRoom");
        var currDivId = $(this).children("div[class='live']").attr("id");
        $(this).siblings("li").children("div[class='info']").css("display", "block");
        $(this).siblings("li").children("div[class='live-none']").css("display", "none");
        $(this).siblings("li").children("div[class='jwplayer']").css("display", "none");
        if (status == 1) {
            $(this).find(".info").css("display", "none");
            $(this).find(".live").css("display", "block");
            // $(this).find(".live-none").css("display", "block");
            $(this).find(".jwplayer").css("display", "block");
            LiveChoiceRight(liveRoom, 2, playId)
            // LiveChoice(liveRoom,2);
        }
    }
});

// 鼠标移出列表隐藏直播画面
$(".section-right-list-center li").mouseleave(function () {
    console.log('mouseleave-移出')
    mouseenter = false;
    var status = $(this).attr("data-status");
    if (status == 1) {
        // $(this).find(".live-none").css("display", "none");
        $(this).find(".live").css("display", "none");
        // $(".jwplayer").remove();
        $(this).find(".jwplayer").css("display", "none");
        $(this).find(".info").css("display", "block");
    }
    closeplay()
});


$(".section-right-list-center").mouseleave(function () {
    console.log("最外层-----移出")
    mouseenter = false;
    // $(".jwplayer").remove();
    // $(this).find(".live-none").css("display", "none");
    $(this).find(".live").css("display", "none");
    $(this).find(".jwplayer").css("display", "none");
    $(this).find(".info").css("display", "block");
    closeplay();
})

//开标未开标项目切换
$(".bidStateCut").click(function () {
    var type = $(this).attr("data-type");
    if (type == 1) {
        $(".endBidUl").show();
        $(".notBidUl").hide();
        handlerScroll(".endBidUl")
    } else {
        $(".endBidUl").hide();
        $(".notBidUl").show();
        handlerScroll(".notBidUl")
    }
    $(".section-right-list-center ul").css("transform", "translateY(0px)")
    $(this).addClass("check-nav");
    $(this).siblings().removeClass("check-nav");
})


//项目信息切换
function projectInfoCut(bidSectionId, projectName, bidOpenPlace, bidOpenStatusName, bidOpenTime, tenderAgencyName, tenderAgencyPhone, liveRoom) {
    parent.projectInfoCut(bidSectionId, projectName, bidOpenPlace, bidOpenStatusName, bidOpenTime, tenderAgencyName, tenderAgencyPhone, liveRoom);


}