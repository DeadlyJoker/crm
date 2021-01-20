layui.use(['table', 'layer'], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 计划项数据展示
     */
    var tableIns = table.render({
        elem: '#cusDevPlanList',
        url: ctx + '/cus_dev_plan/list?sid=' + $("input[name='id']").val(),
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "cusDevPlanListTable",
        cols: [[
            {type: "checkbox", fixed: "center"},
            {field: "id", title: '编号', fixed: "true"},
            {field: 'planItem', title: '计划项', align: "center"},
            {field: 'exeAffect', title: '执行效果', align: "center"},
            {field: 'planDate', title: '执行时间', align: "center"},
            {field: 'createDate', title: '创建时间', align: "center"},
            {field: 'updateDate', title: '更新时间', align: "center"},
            {title: '操作', fixed: "right", align: "center", minWidth: 150, templet: "#cusDevPlanListBar"}
        ]]
    });


    table.on('toolbar(cusDevPlans)', function (obj) {
        var id = $('[name="id"]').val();
        switch (obj.event) {
            case 'add':
                // 点击添加按钮，打开添加营销机会的对话框
                openAddOrUpdateCusDevPlanDialog();
                break;
            case 'success':
                updateSaleChanceDevResult(id,2);
                break;

            case 'failed':
                updateSaleChanceDevResult(id,3);
        }
        ;
    });

    table.on('tool(cusDevPlans)', function (obj) {
        console.log(obj);
        switch (obj.event) {
            case 'edit':
                // 点击添加按钮，打开添加营销机会的对话框
                openAddOrUpdateCusDevPlanDialog(obj.data.id);
                break;
            case 'del':
                deleteSaleChance(obj.data.id);
        };
    });

    function deleteSaleChance(cid) {
        if (!cid){
            layer.msg("请选中你要删除的数据！");
            return ;
        }
        layer.confirm("你确定要删除这条数据吗？",{
            btn:["确认","取消"]
        },function (index) {
            layer.close(index);

            var id = "id="+cid;

            console.log(id);
            $.ajax({
                type:"post",
                url:ctx+"/cus_dev_plan/delete",
                data:id,
                dataType:"json",
                success:function (data){
                    if(data.code == 200){
                        layer.msg("删除成功");
                        tableIns.reload();
                    }else{
                        layer.msg(data.msg,{icon:5});
                    }
                }
            })
        })
    }

    function openAddOrUpdateCusDevPlanDialog(id) {
        var url = ctx + "/cus_dev_plan/addOrUpdateCusDevPlanPage?sid=" + $("input[name='id']").val();
        var title = "计划项管理-添加计划项";
        if (id) {
            url = url + "&id=" + id;
            title = "计划项管理-更新计划项";
        }
        layer.open({
            title: title,
            type: 2,
            area: ["500px", "300px"],
            maxmin: true,
            content: url
        });
    }

    function updateSaleChanceDevResult(id,res) {

        layer.confirm(res == 3?"你确定开发计划要宣布失败吗？":"你确定开发计划要宣布成功吗？",{
            btn:["确认","取消"]
        },function (index) {
            layer.close(index);
            $.ajax({
                type:"post",
                url:ctx+"/sale_chance/updateSaleChanceDevResult",
                data:{
                    id:id,
                    devResult:res
                },
                dataType:"json",
                success:function (data){
                    if(data.code == 200){
                        layer.msg("修改成功");
                        tableIns.reload();
                        parent.location.reload();
                    }else{
                        layer.msg(data.msg,{icon:5});
                    }
                }
            })
        })
    }

});
