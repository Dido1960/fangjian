/*let pass = document.querySelector("#password");
let ero = document.querySelector(".err");
let suc = document.querySelector(".pass");
let low = document.querySelector(".red");
let cen = document.querySelector(".yell");
let str = document.querySelector(".gr");

// console.log(div1.style.display);
pass.onkeydown = function () {
  let num = pass.value.length;
  if (num <= 6) {
    ero.style.display = "block";
    cen.style.display = "none";
    str.style.dispaly = "none";
    suc.style.display = "none";
  } else if (num > 6 && num <= 9) {
    ero.style.display = "block";
    cen.style.display = "block";
    str.style.dispaly = "none";
    suc.style.display = "none";
  } else {
    ero.style.display = "none";
    suc.style.display = "block";
  }
};*/

function showtime() {
    var nowTime = new Date();
    var hours = nowTime.getHours();
    var min = nowTime.getMinutes();
    var sen = nowTime.getSeconds();
    //var date = nowTime.getDate();
    var weekArr = ["日", "一", "二", "三", "四", "五", "六"];
    var week = weekArr[nowTime.getDay()];

    if (sen < 10) {
        sen = "0" + sen;
    }
    if (min < 10) {
        min = "0" + min;
    }
    if (hours < 10) {
        hours = "0" + hours;
    }

    $(".time").html("星期" + week + " " + hours + ":" + min + ":" + sen);
}

setInterval("showtime()", 1000);


