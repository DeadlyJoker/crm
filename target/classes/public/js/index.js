layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
    
    form.on("submit(login)",function (data){
        var username = data.field.username;
        var password = data.field.password;
        if(isNull(username) && isNull(password)){
            return false;
        }
        $.ajax({
            type : "post",
            url : ctx + "/user/login",
            data : {
                userName : username,
                userPwd : password
            },
            success : function (data){
                if(data.code == 200){

                    if(!$("#rememberMe").prop("checked")){
                        $.cookie("userId",data.result.userId,{domain:'localhost',path:'/crm'});
                        $.cookie("userName",data.result.userName,{domain:'localhost',path:'/crm'});
                        $.cookie("trueName",data.result.trueName,{domain:'localhost',path:'/crm'});

                    }else {
                        $.cookie("userId", data.result.userId, {domain: 'localhost', path: '/crm', expires: 7});
                        $.cookie("userName", data.result.userName, {domain: 'localhost', path: '/crm', expires: 7});
                        $.cookie("trueName", data.result.trueName, {domain: 'localhost', path: '/crm', expires: 7});

                    }
                    window.location.href = ctx + "/main";
                }else{
                    layer.msg(data.msg,{icon:5});
                }
            }

        })
        return false;
    });


});
