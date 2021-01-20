layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);


    form.on("submit(addOrUpdateSaleChance)",function (data){
        var index = layer.msg("数据提交中，请稍后...",{
            icon:6,
            time:false,
            shade:0.8
        });

        var url = ctx + "/sale_chance/save";
        if(data.field.id != null){
            var url = ctx + "/sale_chance/update";
        }
        $.post(url,data.field,function (data){
            if(data.code == 200){
                layer.msg(data.msg);
                //关闭弹出层
                layer.close(index);
                //关闭弹出框
                layer.closeAll("iframe");
                parent.location.reload();
            }else{
                layer.msg(data.msg);
            }
        });
        return false;
    });

    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function (){
        // 先得到当前iframe层的索引
        var index = parent.layer.getFrameIndex(window.name);
        // 再执行关闭
        parent.layer.close(index);
    })

    /**
     * 初始化下拉列表
     */
    $.post(ctx+"/user/allRole",function (data) {
        console.log(data);
        var assignMan = $("input[name='assignMan']").val();
        var selection = $("#assignMan");
        for(var i = 0; i < data.length;i++){
            console.log(assignMan+"---------"+data[i].id);
            if(assignMan == data[i].id){
                selection.append("<option value="+data[i].id+" selected>"+data[i].uname+"</option>");
            }else{
                selection.append("<option value="+data[i].id+">"+data[i].uname+"</option>");
            }
        }
        layui.form.render("select");
    })

});