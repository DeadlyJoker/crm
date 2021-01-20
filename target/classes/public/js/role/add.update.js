layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery


    form.on("submit(addOrUpdateUser)",function (data){
        var index = layer.msg("数据提交中，请稍后...",{
            icon:6,
            time:false,
            shade:0.8
        });

        var url = ctx + "/role/save";
        if(data.field.id){
            url = ctx + "/role/update";
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
});