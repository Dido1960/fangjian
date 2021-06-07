//==本JS是加载Lodop插件及CLodop服务的综合示例，可直接使用，建议看懂后融进自己页面程序==
//修改 提示方式需要引入 layui js以及css

var CreatedOKLodopObject, CLodopIsLocal, CLodopJsState;
var innerHTMLStr = "<style>.lodopDiv{width: 90%;height: 124.75px;margin: 0 auto;} a{cursor: pointer}</style>"
//==判断是否需要CLodop(那些不支持插件的浏览器):==
function needCLodop() {
    try {
        var ua = navigator.userAgent;
        if (ua.match(/Windows\sPhone/i))
            return true;
        if (ua.match(/iPhone|iPod|iPad/i))
            return true;
        if (ua.match(/Android/i))
            return true;
        if (ua.match(/Edge\D?\d+/i))
            return true;

        var verTrident = ua.match(/Trident\D?\d+/i);
        var verIE = ua.match(/MSIE\D?\d+/i);
        var verOPR = ua.match(/OPR\D?\d+/i);
        var verFF = ua.match(/Firefox\D?\d+/i);
        var x64 = ua.match(/x64/i);
        if ((!verTrident) && (!verIE) && (x64))
            return true;
        else if (verFF) {
            verFF = verFF[0].match(/\d+/);
            if ((verFF[0] >= 41) || (x64))
                return true;
        } else if (verOPR) {
            verOPR = verOPR[0].match(/\d+/);
            if (verOPR[0] >= 32)
                return true;
        } else if ((!verTrident) && (!verIE)) {
            var verChrome = ua.match(/Chrome\D?\d+/i);
            if (verChrome) {
                verChrome = verChrome[0].match(/\d+/);
                if (verChrome[0] >= 41)
                    return true;
            }
        }
        return false;
    } catch (err) {
        return true;
    }
}

//==加载引用CLodop的主JS,用双端口8000和18000(以防其中一个被占):==
function loadCLodop() {
    if (CLodopJsState == "loading" || CLodopJsState == "complete") return;
    CLodopJsState = "loading";
    var head = document.head || document.getElementsByTagName("head")[0] || document.documentElement;
    var JS1 = document.createElement("script");
    var JS2 = document.createElement("script");
    JS1.src = "http://localhost:8000/CLodopfuncs.js?priority=1";
    JS2.src = "http://localhost:18000/CLodopfuncs.js";
    JS1.onload  = JS2.onload  = function()    {CLodopJsState = "complete";}
    JS1.onerror = JS2.onerror = function(evt) {CLodopJsState = "complete";}
    head.insertBefore(JS1, head.firstChild);
    head.insertBefore(JS2, head.firstChild);
    CLodopIsLocal = !!((JS1.src + JS2.src).match(/\/\/localho|\/\/127.0.0./i));
}

if (needCLodop()){loadCLodop();}//加载

//==获取LODOP对象主过程,判断是否安装、需否升级:==
function getLodop(oOBJECT, oEMBED) {
    var strHtmInstall = "<div class='lodopDiv'><br><font color='#FF00FF'>打印控件未安装!点击这里<a onclick='lodopDownload(32)' target='_self'>执行安装</a>,安装后请刷新页面或重新进入。</font></div>";
    var strHtmUpdate = "<div class='lodopDiv'><br><font color='#FF00FF'>打印控件需要升级!点击这里<a onclick='lodopDownload(32)' target='_self'>执行升级</a>,升级后请重新进入。</font></div>";
    var strHtm64_Install = "<div class='lodopDiv'><br><font color='#FF00FF'>打印控件未安装!点击这里<a onclick='lodopDownload(64)' target='_self'>执行安装</a>,安装后请刷新页面或重新进入。</font></div>";
    var strHtm64_Update = "<div class='lodopDiv'><br><font color='#FF00FF'>打印控件需要升级!点击这里<a onclick='lodopDownload(64)' target='_self'>执行升级</a>,升级后请重新进入。</font></div>";
    var strHtmFireFox = "<div class='lodopDiv'><br><br><font color='#FF00FF'>（注意：如曾安装过Lodop旧版附件npActiveXPLugin,请在【工具】->【附加组件】->【扩展】中先卸它）</font></div>";
    var strHtmChrome = "<div class='lodopDiv'><br><br><font color='#FF00FF'>(如果此前正常，仅因浏览器升级或重安装而出问题，需重新执行以上安装）</font></div>";
    var strCLodopInstall_1 = "<div class='lodopDiv'><br><font color='#FF00FF'>Web打印服务CLodop未安装启动，点击这里<a onclick='lodopDownload(0)' target='_self'>下载执行安装</a>";
    var strCLodopInstall_2 = "<br>（若此前已安装过，可<a href='CLodop.protocol:setup' target='_self'>点这里直接再次启动</a>）";
    var strCLodopInstall_3 = "，成功后请刷新本页面。</font></div>";
    var strCLodopUpdate = "<div class='lodopDiv'><br><font color='#FF00FF'>Web打印服务CLodop需升级!点击这里<a onclick='lodopDownload(0)' target='_self'>执行升级</a>,升级后请刷新页面。</font></div>";
    var LODOP;
    try {
        var ua = navigator.userAgent;
        var isIE = !!(ua.match(/MSIE/i)) || !!(ua.match(/Trident/i));
        if (needCLodop()) {
            try {
                LODOP = getCLodop();
            } catch (err) {}
            if (!LODOP && CLodopJsState !== "complete") {
                if (CLodopJsState == "loading") alert("网页还没下载完毕，请稍等一下再操作."); else alert("网络出错，请刷新页面！");
                return;
            }
            if (!LODOP) {
                // document.body.innerHTML = strCLodopInstall_1 + (CLodopIsLocal ? strCLodopInstall_2 : "") + strCLodopInstall_3 + document.body.innerHTML;
                var innerHtml = innerHTMLStr + strCLodopInstall_1 + (CLodopIsLocal ? strCLodopInstall_2 : "") + strCLodopInstall_3 ;
                prompt(innerHtml);
                return;
            } else {
                var innerHTML = innerHTMLStr;
                if (CLODOP.CVERSION < "4.1.0.4") {
                    // document.body.innerHTML = strCLodopUpdate + document.body.innerHTML;
                    innerHtml += strCLodopUpdate;
                }
                if (oEMBED && oEMBED.parentNode)
                    oEMBED.parentNode.removeChild(oEMBED); //清理旧版无效元素
                if (oOBJECT && oOBJECT.parentNode)
                    oOBJECT.parentNode.removeChild(oOBJECT);
            }
        } else {
            var is64IE = isIE && !!(ua.match(/x64/i));
            //==如果页面有Lodop就直接使用,否则新建:==
            if (oOBJECT || oEMBED) {
                if (isIE)
                    LODOP = oOBJECT;
                else
                    LODOP = oEMBED;
            } else if (!CreatedOKLodopObject) {
                LODOP = document.createElement("object");
                LODOP.setAttribute("width", 0);
                LODOP.setAttribute("height", 0);
                LODOP.setAttribute("style", "position:absolute;left:0px;top:-100px;width:0px;height:0px;");
                if (isIE)
                    LODOP.setAttribute("classid", "clsid:2105C259-1E0C-4534-8141-A753534CB4CA");
                else
                    LODOP.setAttribute("type", "application/x-print-lodop");
                document.documentElement.appendChild(LODOP);
                CreatedOKLodopObject = LODOP;
            } else
                LODOP = CreatedOKLodopObject;
            //==Lodop插件未安装时提示下载地址:==
            if ((!LODOP) || (!LODOP.VERSION)) {
                if (ua.indexOf('Chrome') >= 0)
                    // document.body.innerHTML = strHtmChrome + document.body.innerHTML;
                    innerHtml += strHtmChrome;
                if (ua.indexOf('Firefox') >= 0)
                    // document.body.innerHTML = strHtmFireFox + document.body.innerHTML;
                    innerHtml += strHtmFireFox;
                // document.body.innerHTML = (is64IE ? strHtm64_Install : strHtmInstall) + document.body.innerHTML;
                innerHtml += is64IE ? strHtm64_Install : strHtmInstall;
                prompt(innerHtml);
                return LODOP;
            }
        }
        if (LODOP.VERSION < "6.2.2.6") {
            if (!needCLodop()){
                // document.body.innerHTML = (is64IE ? strHtm64_Update : strHtmUpdate) + document.body.innerHTML;
                var innerHTML = is64IE ? strHtm64_Update : strHtmUpdate;
                prompt(innerHtml);
            }
        }
        //===如下空白位置适合调用统一功能(如注册语句、语言选择等):==


        //=======================================================
        return LODOP;
    } catch (err) {
        alert("getLodop出错:" + err);
    }
}

function lodopDownload(status) {
    var file = status == 0 ? "CLodop_Setup_for_Win32NT.exe" :
        status == 32 ? "install_lodop32.exe" :
            status == 64 ? "install_lodop64.exe" : "";
    var request = new XMLHttpRequest();
    request.responseType = "blob";
    request.open("GET", "/lodop/lodopDownload?file=" + file);
    request.onload = function () {
        var url = window.URL.createObjectURL(this.response);
        console.log(url);
        var a = document.createElement("a");
        document.body.appendChild(a);
        a.href = url;
        a.download = file;
        a.click();
    }
    request.send();
}

function prompt(htmlStr) {
    layer.open({
        type: "1",
        title: "打印提示",
        content: htmlStr,
        offset: 'auto',
        shadeClose: true,
        resize: false,
        move: false,
        area: ['400px', '180px'],
    });
}