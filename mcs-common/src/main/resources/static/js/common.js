
//将表单数据转为json
$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

//全局配置ajax
$.ajaxSetup({
    dataType: "json",
    complete:function(XMLHttpRequest,textStatus){
        if(textStatus=="parsererror"){
            $.messager.alert('提示信息', "登陆超时！请重新登陆！", 'info',function(){
                window.location.href = '/';
            });
        } else if(textStatus=="error"){
            $.messager.alert('提示信息', "请求超时！请稍后再试！", 'info');
        }
    }
});

//百分比宽度
function fixWidth(percent) {
    return document.body.clientWidth * percent ;//根据自身情况更改
}