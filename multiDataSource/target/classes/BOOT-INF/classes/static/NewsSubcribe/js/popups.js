        setSize();
        addEventListener('resize',setSize);
        function setSize() {
            document.documentElement.style.fontSize = document.documentElement.clientWidth/750*100+'px';
        }

/*toast 弹出提示*/
function jqtoast(text,sec) {
    var _this = text;
    var this_sec = sec;
    var htm = '';
    htm += '<div class="jq-toast" style="display: none;">';
    if (_this){
        htm +='<div class="toast">'+_this+'</div></div>';
        $('body').append(htm);
        $('.jq-toast').fadeIn();

    }else {
        jqalert({
            title:'提示',
            content:'提示文字不能为空',
            yestext:'确定'
        })
    }
    if (!sec){
        this_sec = 2000;
    }
    setTimeout(function () {
        $('.jq-toast').fadeOut(function () {
            $(this).remove();
        });
        _this = '';
    },this_sec);
}