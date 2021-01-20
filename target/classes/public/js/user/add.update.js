layui.use(['form', 'layer','formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

        // 引入 formSelects 模块
        formSelects = layui.formSelects;

    /**
     * 加载下拉框列表
     */
    formSelects.config('selectId',{
        type:'post',
        searchUrl: ctx+"/role/queryAllRoles?id="+$("input[name='id']").val(),
        keyName:'roleName',
        keyVal:'id'
    },true);

    form.on("submit(addOrUpdateUser)",function (data){
        console.log(data)
        var index = layer.msg("数据提交中，请稍后...",{
            icon:6,
            time:false,
            shade:0.8
        });

        var url = ctx + "/user/save";
        if(data.field.id){
            url = ctx + "/user/update";
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
});