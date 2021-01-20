layui.use(['table', 'layer'], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table




    /**
     * 用户列表展示
     */
    var tableIns = table.render({
        elem: '#userList',
        url: ctx + '/user/list',
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "userListTable",
        cols: [[
            {type: "checkbox", fixed: "left", width: 50},
            {field: "id", title: '编号', fixed: "true", width: 80},
            {field: 'userName', title: '用户名', minWidth: 50, align: "center"},
            {field: 'email', title: '用户邮箱', minWidth: 100, align: 'center'},
            {field: 'phone', title: '用户电话', minWidth: 100, align: 'center'},
            {field: 'trueName', title: '真实姓名', align: 'center'},
            {field: 'createDate', title: '创建时间', align: 'center', minWidth: 150},
            {field: 'updateDate', title: '更新时间', align: 'center', minWidth: 150},
            {title: '操作', minWidth: 150, templet: '#userListBar', fixed: "right", align: "center"}
        ]]
    });

    /**
     * 绑定搜索按钮的点击事件
     */
    $(".search_btn").click(function () {
        table.reload('userListTable', {
            where: { //设定异步数据接口的额外参数，任意设
                userName: $("input[name='userName']").val(), // 客户名
                email: $("input[name='email']").val(), // 创建人
                phone: $("input[name='phone']").val() // 状态
            }, page: {
                curr: 1 // 重新从第 1 页开始
            }
        }); // 只重载数据
    });
    table.on('toolbar(users)', function (obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        switch (obj.event) {
            case 'add':
                // 点击添加按钮，打开添加营销机会的对话框
                openAddOrUpdatePage();
                break;
            case 'del':
                deleteUsers(checkStatus.data);
        };
    });
    table.on("tool(users)", function (obj) {

        var event = obj.event;
        switch (event) {
            case "edit":
                // 详情
                openAddOrUpdatePage(obj.data.id);
                break;
            case "del":
                obj.data.length = 1;
                deleteUsers(obj.data);
        }
    });

    function openAddOrUpdatePage(id) {
        var url = ctx + "/user/toAddOrUpdatePage";
        var title = "用户管理-用户添加";
        if (id) {
            url = url + "?id=" + id;
            title = "用户管理-用户更新";
        }
        layer.open({
            type: 2,
            content: url,
            title: title,
            area:["650px","450px"],
            maxmin: true
        })
    }

    function deleteUsers(data) {
        if (data.length == 0){
            layer.msg("请选中你要删除的数据！");
            return ;
        }
        layer.confirm("你确定要删除这些数据吗？",{
            btn:["确认","取消"]
        },function (index) {
            layer.close(index);
            var ids = "ids=";
            if(data.length == 1){
                ids += data.id;
            }else{
                for(var i = 0;i < data.length;i++){
                    if(i == 0){
                        ids += data[i].id;
                        continue;
                    }
                    ids += "&ids="+data[i].id;

                }
            }
            console.log(ids);
            $.ajax({
                type:"post",
                url:ctx+"/user/delete",
                data:ids,
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

});