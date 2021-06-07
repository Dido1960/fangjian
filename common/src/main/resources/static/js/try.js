// let xia = $(".right li");
// xia.click(function () {

// });
let xia = $(".right");
xia.on("click", "li", function () {
  $(this)
    .after(
      ` <div class="left">
  <h3>公共资源行政监督平台</h3>
  <p>对接公共资源交易系统、信用平台，通过风险点监控、痕迹监管、在线监控平台，记录监督公共资源交易流程各环节，变“现场监管”为“网络监管”<br />
      <br />
      从事多年公共资源交易及电子招投标系统相关工作，通过对已有工作、成果的转化和利用大数据技术，公司建设了拥有自主知识产
      权的“交易通”公共资源电子交易平台、为全省的建设工程项目、政府采购项目、土地产权项目提供全流程电子化的招投标服务。</p>
  <span>查看详情</span>
</div>`
    )
    .siblings()
    .remove(".left");
  //   xia.on("mouseleave", "li", function () {
  //     let kuang = $(".left");
  //     kuang.attr("style", "display:none");
  //     kuang.remove();
  //   });
});

// $(this).unbind('click');
//     //事件处理逻辑，例如
//     alert('只会弹一次');
