var http = 'http://39.107.60.206:8189/data/'
//var http = 'http://172.16.2.32:8188/data/'

 //获取location url的参数kaid
function GetQueryString(name) { 
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i"); 
  var r = window.location.search.substr(1).match(reg); 
  if (r!=null) return (r[2]); return null; 
}

! function (n, e) {
    var t = n.documentElement,
        i = "orientationchange" in window ? "orientationchange" : "resize",
        o = function () {
            var n = t.clientWidth;
            n && (n >= 640 ? t.style.fontSize = "100px" : t.style.fontSize = 100 * (n / 640) + "px")
        };
    n.addEventListener && (e.addEventListener(i, o, !1), n.addEventListener("DOMContentLoaded", o, !1))
}(document, window);
//var user_id = GetQueryString("user_id");

//var user_id = 30947;
