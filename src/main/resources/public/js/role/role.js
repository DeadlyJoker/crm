layui.use(['table', 'layer'], function () {
        var layer = parent.layer === undefined ? layui.layer : top.layer,
            $ = layui.jquery,
            table = layui.table


        var tableIns = table.render({
            elem: '#roleList',
            url: ctx + '/role/list',
            cellMinWidth: 95,
            page: true,
            height: "full-125",
            limits: [10, 15, 20, 25],
            limit: 10,
            toolbar: "#toolbarDemo",
            id: "roleListTable",
            cols: [[{type: "checkbox", fixed: "left", width: 50}, {
                field: "id",
                title: '编号',
                fixed: "true",
                width: 80
            }, {field: 'roleName', title: '角色名', minWidth: 50, align: "center"}, {
                field: 'roleRemark',
                title: '角色备注',
                minWidth: 100,
                align: 'center'
            }, {field: 'createDate', title: '创建时间', align: 'center', minWidth: 150}, {
                field: 'updateDate',
                title: '更新时间',
                align: 'center',
                minWidth: 150
            }, {title: '操作', minWidth: 150, templet: '#roleListBar', fixed: "right", align: "center"}
            ]]
        });

        $(".search_btn").on("click", function () {
            table.reload("roleListTable", {
                page: {
                    curr: 1 //重新从第 1 页开始
                }, where: {
                    roleName: $("input[name='roleName']").val()
                }
            })
        });


        table.on('toolbar(roles)', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            switch (obj.event) {
                case 'add':
                    // 点击添加按钮，打开添加营销机会的对话框
                    openAddOrUpdatePage();
                    break;
                case 'grant':
                    openAddGrantDailog(checkStatus.data);
                    break;
            };
        });

        table.on('tool(roles)', function (obj) {
            switch (obj.event) {
                case 'edit':
                    // 点击添加按钮，打开添加营销机会的对话框
                    openAddOrUpdatePage(obj.data.id);
                    break;
                case 'del':
                    deleteRole(obj.data.id)

            }
            ;
        });

        function openAddGrantDailog(datas) {
            if (datas.length == 0) {
                layer.msg("请选择待授权角色记录!", {icon: 5});
                return;
            }
            if (datas.length > 1) {
                layer.msg("暂不支持批量角色授权!", {icon: 5});
                return;
            }
            var url = ctx + "/role/toAddGrantPage?roleId=" + datas[0].id;
            var title = "角色管理-角色授权";
            layui.layer.open({
                title: title,
                type: 2,
                area: ["600px", "280px"],
                maxmin: true,
                content: url
            });
        }

        function openAddOrUpdatePage(id) {
            var url = ctx + "/role/toAddOrUpdatePage";
            var title = "角色管理-用户添加";
            if (id) {
                url += "?id=" + id;
                title = "角色管理-用户修改";
            }
            layer.open({
                type: 2,
                content: url,
                title: title,
                area: ["650px", "450px"],
                maxmin: true
            })
        }


        function deleteRole(data) {
            layer.confirm("你确定要删除这些数据吗？", {
                btn: ["确认", "取消"]
            }, function (index) {
                layer.close(index);
                var id = "ids=" + data;
                $.ajax({
                    type: "post",
                    url: ctx + "/role/delete",
                    data: id,
                    dataType: "json",
                    success: function (data) {
                        if (data.code == 200) {
                            layer.msg("删除成功");
                            tableIns.reload();
                        } else {
                            layer.msg(data.msg, {icon: 5});
                        }
                    }
                })
            })
        }
    }
);