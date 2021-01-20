layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    form.on("submit(saveBtn)",function (data){

        var oldPwd = $('[name="old_password"]').val();
        var newPwd = $('[name="new_password"]').val();
        var confirmPwd = $('[name="again_password"]').val()
        if(isNull(oldPwd)){
            layui.msg("旧密码不能为空",{icon:5});
            return false;
        }
        if(isNull(oldPwd)){
            layui.msg("新密码不能为空",{icon:5});
            return false;
        }
        if(isNull(confirmPwd)){
            layui.msg("确密码不能为空",{icon:5});
            return false;
        }
        $.ajax({
            type : "post",
            url : ctx + "/user/updatePwd",
            data : {
                oldPwd : oldPwd,
                newPwd : newPwd,
                confirmPwd :confirmPwd
            },
            success : function (data){
                if(data.code == 200){
                    layer.msg(data.msg,{icon:7});
                    $.removeCookie("userId",{path:'/crm'});
                    $.removeCookie("userName",{path:'/crm'});
                    $.removeCookie("trueName",{path:'/crm'});
                    window.parent.location.href = ctx + "/index";
                }else{
                    layer.msg(data.msg,{icon:7});
                }

            }

        })
        return false;
    });


});