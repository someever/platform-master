adminApp.config(function ($routeProvider) {
    $routeProvider.when('/accredit/view/:userId/:typeId', {
        templateUrl: ctx + '/system/user/accreditInit',
        controller: 'accreditController'
    });
});

/**
 * 权限配置
 */
adminApp.controller('accreditController', function ($scope, userFnService, $routeParams, roleFnService, $location, $translate, menuFnService, $rootScope, resourceFnService) {

    $scope.selectedId = [];//渠道id集合
    $scope.selectedCode = [];//渠道code集合
    $scope.channelCode = [];//渠道code集合（显示项）

    $scope.selectedRoleId = [];//角色id集合
    $scope.selectedRoleCode = [];//角色code集合
    $scope.roleCode = [];//角色code集合（显示项）

    $scope.gameSelectedId = [];//游戏id集合
    $scope.gameSelectedCode = [];//游戏code集合
    $scope.gameChannelCode = [];//游戏code集合（显示项）
    //初始化菜单栏
    $rootScope.menuBarData.menuBarThreeName = "userAccreditEdit";
    $rootScope.menuBarData.menuBarTitle = "userAccreditEdit";

    $scope.messagesData = {};//设置提示框model数据集合
    $scope.permission = {};
    $scope.roleHide = $routeParams.typeId;//2代表角色权限,需要隐藏roleList
    $scope.menus = [];
    $scope.parentMenu = [];
    $scope.parentId = 1;//默认父id
    $scope.menuCondition = {};

    //$scope.gameListModel = [];
    ////获取游戏
    //resourceFnService.resourceGetGame().success(function (data) {
    //    var list = [];
    //    for (var key in data) {
    //        list.push(data[key]);
    //    }
    //    $rootScope.gameList = list;
    //    //for (var i = 0; i < $rootScope.gameList.length; i++) {
    //    //    if ($rootScope.gameList[i].code == "game.tol") {
    //    //        $rootScope.gameTransfer = $rootScope.gameList[i].id;//全局game
    //    //        $rootScope.gameModel = $rootScope.gameList[i].id;
    //    //    }
    //    //}
    //    $rootScope.$apply();
    //
    //});


    /**
     * 获取父菜单
     */
    menuFnService.getParentMenu().success(function (data) {
        $scope.parentMenu = data;
        for (var i = 0; i < $scope.parentMenu.length; i++) {
            if (i == 0) {
                $scope.parentMenu[i].active = "active";
            } else {
                $scope.parentMenu[i].active = "";
            }

        }
        $scope.$apply();
    });

    /**
     * 添加/移除所有权限
     */
    $scope.allAuthority = function (flag) {
        $scope.menuCondition.userRoleId = $routeParams.userId;
        $scope.menuCondition.typeId = $routeParams.typeId;
        $scope.menuCondition.menuParentId = $scope.parentId;
        $scope.menuCondition.flag = flag;//true代表所有权限
        userFnService.appendAllRelation($scope.menuCondition).success(function (data) {
            menuSelect(1, 10, $routeParams.userId, $routeParams.typeId, $scope.parentId);
        }).error(function () {
            menuSelect(1, 10, $routeParams.userId, $routeParams.typeId, $scope.parentId);
        });
    }

    /**
     * 父菜单单击
     * @param id
     */
    $scope.menuClick = function (id) {
        $scope.parentId = id;
        menuSelect(1, 10, $routeParams.userId, $routeParams.typeId, $scope.parentId);
    }

    /**
     刷新页面工具方法
     */
    function menuSelect(pageIndex, pageSize, userId, typeId, parentId) {
        /*
         刷新查询页面，调用Service的查询方法
         */
        menuFnService.menuPFindByName(pageIndex, pageSize, userId, typeId, parentId).success(function (response) {
            $scope.menus = response.rows;
            for (var i = 0; i < $scope.menus.length; i++) {
                //初始化添删改查选项
                $scope.menus[i].menu.chkBoxAdd = "load.noHave";
                $scope.menus[i].menu.chkBoxDelete = "load.noHave";
                $scope.menus[i].menu.chkBoxUpdate = "load.noHave";
                $scope.menus[i].menu.chkBoxSelect = "load.noHave";

                $scope.menus[i].menu.addlabelType = "label-danger";
                $scope.menus[i].menu.deletelabelType = "label-danger";
                $scope.menus[i].menu.updatelabelType = "label-danger";
                $scope.menus[i].menu.selectlabelType = "label-danger";


                for (var j = 0; j < $scope.menus[i].relationList.length; j++) {
                    if ($scope.menus[i].relationList[j] == 1) {
                        $scope.menus[i].menu.chkBoxAdd = "load.have";
                        $scope.menus[i].menu.addlabelType = "label-success";
                    }
                    if ($scope.menus[i].relationList[j] == 2) {
                        $scope.menus[i].menu.chkBoxDelete = "load.have";
                        $scope.menus[i].menu.deletelabelType = "label-success";
                    }
                    if ($scope.menus[i].relationList[j] == 3) {
                        $scope.menus[i].menu.chkBoxUpdate = "load.have";
                        $scope.menus[i].menu.updatelabelType = "label-success";
                    }
                    if ($scope.menus[i].relationList[j] == 4) {
                        $scope.menus[i].menu.chkBoxSelect = "load.have";
                        $scope.menus[i].menu.selectlabelType = "label-success";
                    }
                }

                //$.ajax({
                //    url: ctx + "/system/relation/theRelationById",
                //    data: {userId: $routeParams.userId, type: $routeParams.typeId, menuId: $scope.menus[i].id},
                //    success: function (itemData) {
                //        for (var i = 0; i < itemData.length; i++) {
                //
                //            if (itemData[i] == 1) {
                //                $scope.menus[i].chkBoxAdd="load.have";
                //            }
                //            if (itemData[i] == 2) {
                //                $scope.menus[i].chkBoxDelete="load.have";
                //            }
                //            if (itemData[i] == 3) {
                //                $scope.menus[i].chkBoxUpdate="load.have";
                //            }
                //            if (itemData[i] == 4) {
                //                $scope.menus[i].chkBoxSelect="load.have";
                //            }
                //
                //
                //        }
                //    },
                //    async: true
                //}).responseText;

            }

            $scope.pageCount = response.page.pageCount;
            $scope.$apply();
        });


    }

    /**
     分页
     */
    $scope.onPageChange = function () {
        /*
         刷新查询页面
         */
        menuSelect($scope.currentPage, 10, $routeParams.userId, $routeParams.typeId, $scope.parentId);
    };


    /**
     刷新查询页面
     */
    menuSelect(1, 10, $routeParams.userId, $routeParams.typeId, $scope.parentId);

    /**
     * 选取权限
     * @param userId
     * @param typeId
     * @param resTypeId
     */
    $scope.selectPermission = function (userId, typeId, resTypeId) {
        $scope.permission.userId = userId;
        $scope.permission.typeId = typeId;
        $scope.permission.resTypeId = resTypeId;
        if (resTypeId == 1) {

            //$.ajax({
            //    url: ctx + '/system/relation/getPermission',
            //    data: $scope.permission,
            //    success: function (data) {
            //
            //    }, async: false
            //});
            userFnService.getPermission($scope.permission).success(function (data) {
                /*
                 游戏初始化
                 */
                var gameCheckbox = $(".gameCheckList span");
                var gameIds = data;
                for (var i = 0; i < gameIds.length; i++) {
                    for (var j = 0; j < $scope.gameList.length; j++) {
                        if (gameIds[i] == $scope.gameList[j].id) {
                            $scope.gameSelectedCode.push($scope.gameList[j].code);
                            $scope.gameSelectedId.push($scope.gameList[j].id);
                        }

                        /*
                         遍历checkbox，并初始化
                         */
                        for (var k = 0; k < gameCheckbox.length; k++) {
                            if ($(gameCheckbox[k]).attr('name')== gameIds[i]) {
                                $(gameCheckbox[k]).attr("class", "label label-success");
                            }
                        }

                    }
                }
                $scope.gameChannelCode = $scope.gameSelectedCode;
                $scope.$apply();
            });
        } else {

            //$.ajax({
            //    url: ctx + '/system/relation/getPermission',
            //    data: $scope.permission,
            //    success: function (data) {
            //
            //    }, async: false
            //});
            userFnService.getPermission($scope.permission).success(function (data) {
                /*
                 渠道初始化
                 */
                var checkbox = $(".siteCheckList span");
                var siteIds = data;
                for (var i = 0; i < siteIds.length; i++) {
                    for (var j = 0; j < $scope.siteList.length; j++) {
                        if (siteIds[i] == $scope.siteList[j].id) {
                            $scope.selectedCode.push($scope.siteList[j].code);
                            $scope.selectedId.push($scope.siteList[j].id);
                        }

                        /*
                         遍历checkbox，并初始化
                         */
                        for (var k = 0; k < checkbox.length; k++) {
                            if ($(checkbox[k]).attr('name') == siteIds[i]) {
                                $(checkbox[k]).attr("class", "label label-success");
                            }
                        }

                        /*if(siteIds.length==$scope.siteList.length){
                         $(".allCheck").text("取消全选");
                         $("#all").attr("checked","true");
                         $scope.siteModel=true;
                         }else{
                         $("#all").attr("checked","false")
                         $scope.siteModel=false;
                         $(".allCheck").text("全选");
                         }*/
                    }
                }
                $scope.channelCode = $scope.selectedCode;
                $scope.$apply();
            });
        }

    };

    /**
     * 修改权限
     * @param userId
     * @param typeId
     * @param resTypeId
     */
    $scope.updatePermission = function (userId, typeId, resTypeId) {
        $scope.permission.userId = userId;
        $scope.permission.typeId = typeId;
        $scope.permission.resTypeId = resTypeId;
        if (resTypeId == 1) {
            var game = "";
            for (var i = 0; i < $scope.gameSelectedId.length; i++) {
                game += $scope.gameSelectedId[i] + ",";
            }
            $scope.permission.siteIds = game;
        } else {
            var site = "";
            for (var i = 0; i < $scope.selectedId.length; i++) {
                site += $scope.selectedId[i] + ",";
            }
            $scope.permission.siteIds = site;
        }

        userFnService.setPermission($scope.permission).success();
    };
    $scope.selectPermission($routeParams.userId, $routeParams.typeId, 2);//获取渠道
    $scope.selectPermission($routeParams.userId, $routeParams.typeId, 1);//获取游戏
    /**
     选择渠道（弹框）
     */
    $scope.channelClick = function () {
        $("#channelAddModal").modal('show');
    }
    /**
     监听addForm表单
     */
    $scope.channelAdd = function () {
        console.log($scope.selectedId);
        console.log($scope.selectedCode);
        $scope.updatePermission($routeParams.userId, $routeParams.typeId, 2);
        $("#channelAddModal").modal('hide');
        $scope.channelCode = $scope.selectedCode;
    };


    /**
     选中判断逻辑方法
     */
    var updateSelected = function (action, id, code) {
        /*
         true添加集合元素
         */
        if (action == 'add' && $scope.selectedId.indexOf(id) == -1) {
            $scope.selectedId.push(id);
            $scope.selectedCode.push(code);
        }
        /*
         false移除集合元素
         */
        if (action == 'remove' && $scope.selectedId.indexOf(id) != -1) {
            var idx = $scope.selectedId.indexOf(id);
            $scope.selectedId.splice(idx, 1);
            $scope.selectedCode.splice(idx, 1);
        }
    };

    /**
     checkbox单击操作
     */
    $scope.updateSelection = function ($event, id, code) {
        if ($($event.target).attr("class") != "label label-success") {
            $($event.target).attr("class", "label label-success");
        } else {
            $($event.target).attr("class", "label label-default");
        }
        var action = "add";
        if ($($event.target).attr("class") == "label label-success") {
            action = "add";
        } else {
            action = "remove";
        }
        updateSelected(action, id, code);//选中判断逻辑方法
    };

    /**
     全选，遍历checkboxModel，正向操作
     */
    $scope.all = function (v) {
        var checkbox = $(".siteCheckList span");
        for (var i = 0; i < checkbox.length; i++) {
            $(checkbox[i]).attr("class", "label label-success");
        }
        var idData = [];
        var codeData = [];
        $scope.siteModel = true;
        for (var i = 0; i < v.length; i++) {
            idData.push(v[i].id);
            codeData.push(v[i].code);
        }
        $scope.selectedId = idData;
        $scope.selectedCode = codeData;
    };

    /**
     全不选
     */
    $scope.deselectAll = function () {
        var checkbox = $(".siteCheckList span");
        for (var i = 0; i < checkbox.length; i++) {
            $(checkbox[i]).attr("class", "label label-default");
        }
        $scope.selectedId = [];
        $scope.selectedCode = [];
        $scope.master = false;
    }

    /**
     反选
     */
    $scope.versa = function (c) {
        var checkbox = $(".siteCheckList span");
        /*
         遍历checkbox，并反向操作
         */
        for (var i = 0; i < checkbox.length; i++) {
            if ($(checkbox[i]).attr("class") == "label label-success") {
                $(checkbox[i]).attr("class", "label label-default");
                var idx = $scope.selectedId.indexOf(Number($(checkbox[i]).attr('name')));
                $scope.selectedId.splice(idx, 1);
                $scope.selectedCode.splice(idx, 1);

            } else {
                $scope.selectedId.push(Number($(checkbox[i]).attr('name')));
                $scope.selectedCode.push($(checkbox[i]).text());
                $(checkbox[i]).attr("class", "label label-success");
            }

        }

        console.log($scope.selectedId + "/////////" + $scope.selectedCode);
    };

    /**
     选择游戏（弹框）
     */
    $scope.gameChannelClick = function () {
        $("#gameChannelAddModal").modal('show');
    };


    /**
     监听addForm表单
     */
    $scope.gameChannelAdd = function () {
        $scope.gameChannelCode = $scope.gameSelectedCode;
        $scope.updatePermission($routeParams.userId, $routeParams.typeId, 1);
        $("#gameChannelAddModal").modal('hide');
    };

    /**
     选中判断逻辑方法
     */
    var updateGameSelected = function (action, id, code) {
        /*
         true添加集合元素
         */
        if (action == 'add' && $scope.gameSelectedId.indexOf(id) == -1) {
            $scope.gameSelectedId.push(id);
            $scope.gameSelectedCode.push(code);
        }
        /*
         false移除集合元素
         */
        if (action == 'remove' && $scope.gameSelectedId.indexOf(id) != -1) {
            var idx = $scope.gameSelectedId.indexOf(id);
            $scope.gameSelectedId.splice(idx, 1);
            $scope.gameSelectedCode.splice(idx, 1);
        }
    };

    /**
     checkbox单击操作
     */
    $scope.updateGameSelection = function ($event, id, code) {
        if ($($event.target).attr("class") != "label label-success") {
            $($event.target).attr("class", "label label-success");
        } else {
            $($event.target).attr("class", "label label-default");
        }
        var action = "add";
        if ($($event.target).attr("class") == "label label-success") {
            action = "add";
        } else {
            action = "remove";
        }
        updateGameSelected(action, id, code);//选中判断逻辑方法
    };


    /**
     全选，遍历checkboxModel，正向操作
     */
    $scope.gameAll = function (v) {
        var checkbox = $(".gameCheckList span");
        for (var i = 0; i < checkbox.length; i++) {
            $(checkbox[i]).attr("class", "label label-success");
        }
        var idData = [];
        var codeData = [];
        $scope.gameModel = true;
        for (var i = 0; i < v.length; i++) {
            idData.push(v[i].id);
            codeData.push(v[i].code);
        }
        $scope.gameSelectedId = idData;
        $scope.gameSelectedCode = codeData;
    };

    /**
     全不选
     */
    $scope.deselectAllGame = function () {
        var checkbox = $(".gameCheckList span");
        for (var i = 0; i < checkbox.length; i++) {
            $(checkbox[i]).attr("class", "label label-default");
        }

        $scope.gameSelectedId = [];
        $scope.gameSelectedCode = [];
    };

    /**
     反选
     */
    $scope.gameVersa = function (c) {
        var checkbox = $(".gameCheckList span");
        /*
         遍历checkbox，并反向操作
         */
        for (var i = 0; i < checkbox.length; i++) {
            if ($(checkbox[i]).attr("class") == "label label-success") {
                $(checkbox[i]).attr("class", "label label-default");
                var idx = $scope.gameSelectedId.indexOf(Number($(checkbox[i]).attr('name')));
                $scope.gameSelectedId.splice(idx, 1);
                $scope.gameSelectedCode.splice(idx, 1);

            } else {
                $scope.gameSelectedId.push($(checkbox[i]).attr('name'));
                $scope.gameSelectedCode.push($(checkbox[i]).text());
                $(checkbox[i]).attr("class", "label label-success");
            }

        }
    };

    /**
     刷新role角色查询页面，调用roleService的查询方法
     */
    roleFnService.allRole().success(function (response) {
        $scope.roles = response;
        $scope.$apply();
        /**
         查询已绑定的角色
         */
        userFnService.getRole($scope.userByRole).success(function (data) {
            var checkbox = $(".roleCheckList span");
            ///*
            // 遍历checkbox，并初始化
            // */
            //for (var k = 0; k < checkbox.length; k++) {
            //    checkbox[k].checked = false;
            //}

            for (var i = 0; i < data.length; i++) {
                for (var j = 0; j < $scope.roles.length; j++) {
                    if (data[i] == $scope.roles[j].id) {
                        $scope.selectedRoleCode.push($scope.roles[j].role);
                        $scope.selectedRoleId.push($scope.roles[j].id);
                    }

                    /*
                     遍历checkbox，并初始化
                     */
                    for (var k = 0; k < checkbox.length; k++) {
                        if ($(checkbox[k]).attr('name') == data[i]) {
                            $(checkbox[k]).attr("class", "label label-success");
                        }
                    }

                }
            }
            $scope.roleCode = $scope.selectedRoleCode;
            $scope.$apply();

        });
    });


    $scope.userBindingId = -1;//绑定角色初始id
    $scope.userByRole = {};//初始化绑定角色 的对象
    $scope.userBindingId = $routeParams.userId;
    $scope.userByRole.userId = $scope.userBindingId;
    $scope.selectedRoleId = [];

    $scope.roleClicked = function () {

        $("#roleAddModal").modal('show');
    };


    /**
     全选，遍历checkboxModel，正向操作
     */
    $scope.allRole = function (v) {
        var checkbox = $(".roleCheckList span");
        for (var i = 0; i < checkbox.length; i++) {
            $(checkbox[i]).attr("class", "label label-success");
        }
        var idData = [];
        var codeData = [];
        for (var i = 0; i < v.length; i++) {
            idData.push(v[i].id);
            codeData.push(v[i].role);
        }
        $scope.selectedRoleId = idData;
        $scope.selectedRoleCode = codeData;
        console.log($scope.selectedRoleId + "/////////" + $scope.selectedRoleCode);
    };

    /**
     全不选
     */
    $scope.deselectAllRole = function () {
        var checkbox = $(".roleCheckList span");
        for (var i = 0; i < checkbox.length; i++) {
            $(checkbox[i]).attr("class", "label label-default");
        }
        $scope.selectedRoleId = [];
        $scope.selectedRoleCode = [];
    };

    /**
     反选
     */
    $scope.versaRole = function (c) {
        var checkbox = $(".roleCheckList span");
        /*
         遍历checkbox，并反向操作
         */
        for (var i = 0; i < checkbox.length; i++) {
            if ($(checkbox[i]).attr("class") == "label label-success") {
                $(checkbox[i]).attr("class", "label label-default");
                var idx = $scope.selectedRoleId.indexOf($(checkbox[i]).attr('name'));
                $scope.selectedRoleId.splice(idx, 1);
                $scope.selectedRoleCode.splice(idx, 1);

            } else {
                $scope.selectedRoleId.push(Number($(checkbox[i]).attr('name')));
                $scope.selectedRoleCode.push($(checkbox[i]).text());
                $(checkbox[i]).attr("class", "label label-success");
            }

        }
    };

    /**
     * 绑定角色
     */
    $scope.roleAdd = function () {
        $scope.userByRole.userId = $scope.userBindingId;//用户id
        var role = "";
        for (var i = 0; i < $scope.selectedRoleId.length; i++) {
            role += $scope.selectedRoleId[i] + ",";
        }
        $scope.userByRole.roleIds = role;//角色id集合
        $scope.roleCode = $scope.selectedRoleCode;
        /*
         调用userService的绑定角色方法
         */
        userFnService.setRole($scope.userByRole).success();
        $('#roleAddModal').modal('hide');
        /*  $scope.selectedRoleCode=[]
         $scope.selectedRoleId=[];*/
    };


    /**
     选中判断逻辑方法
     */
    var updateSelectedRole = function (action, id, code) {
        /*
         true添加集合元素
         */
        if (action == 'add' && $scope.selectedRoleId.indexOf(id) == -1) {
            $scope.selectedRoleId.push(id);
            $scope.selectedRoleCode.push(code);
        }
        /*
         false移除集合元素
         */
        if (action == 'remove' && $scope.selectedRoleId.indexOf(id) != -1) {
            var idx = $scope.selectedRoleId.indexOf(id);
            $scope.selectedRoleId.splice(idx, 1);
            $scope.selectedRoleCode.splice(idx, 1);
        }
    };

    /**
     checkbox单击操作
     */
    $scope.updateSelectionRole = function ($event, id, code) {
        //var checkbox = $event.target;
        //var action = (checkbox.checked ? 'add' : 'remove');//判断是否选中
        //updateSelectedRole(action, id, code);//选中判断逻辑方法

        if ($($event.target).attr("class") != "label label-success") {
            $($event.target).attr("class", "label label-success");
        } else {
            $($event.target).attr("class", "label label-default");
        }
        var action = "add";
        if ($($event.target).attr("class") == "label label-success") {
            action = "add";
        } else {
            action = "remove";
        }
        updateSelectedRole(action, id, code);//选中判断逻辑方法

    };

    /**
     添加（跳转）
     */
    $scope.affirmButton = function () {
        roleFnService.allRole().success(function () {
            if ($routeParams.typeId == 2) {
                $location.path("/role/view/" + 1);
                $scope.$apply();
                $("#messagesModal").modal('show');
            } else if ($routeParams.typeId == 3) {
                $location.path("/user/view/" + 1);
                $scope.$apply();
                $("#messagesModal").modal('show');
            }


        });


    };

    /**
     * 权限处理
     */
    $scope.authorityCheck = function (menuId, chkBox, chkType) {
        var chk = true;

        if (chkBox == "load.have") {
            chk = false;
        } else if (chkBox == "load.notHave") {
            chk = true;
        }
        /*
         添加权限
         */
        roleFnService.appendRelation($routeParams.userId, $routeParams.typeId, menuId, chkType, chk).success(function (data) {
            /*
             刷新查询页面
             */
            menuSelect($scope.currentPage, 10, $routeParams.userId, $routeParams.typeId, $scope.parentId);
            console.log(data);
        }).error(function () {
            /*
             刷新查询页面
             */
            menuSelect($scope.currentPage, 10, $routeParams.userId, $routeParams.typeId, $scope.parentId);
        });

    };

    ///**
    // tree初始化,菜单国际化
    // */
    //$scope.init = function () {
    //
    //
    //    $.ajax({
    //        url: ctx + '/system/menu/getMenu',
    //        success: function (data) {
    //            for (var i = 0; i < data.childrenList.length; i++) {
    //                data.childrenList[i].menuName = $translate.instant(data.childrenList[i].menuName);
    //                for (var j = 0; j < data.childrenList[i].childrenList.length; j++) {
    //                    data.childrenList[i].childrenList[j].menuName = $translate.instant(data.childrenList[i].childrenList[j].menuName);
    //                }
    //            }
    //            var editData = {};
    //            editData.checkAdd = $translate.instant("load.checkAdd");
    //            editData.checkDelte = $translate.instant("load.checkDelte");
    //            editData.checkUpdate = $translate.instant("load.checkUpdate");
    //            editData.checkSelect = $translate.instant("load.checkSelect");
    //
    //            TreeView.init(data, editData);
    //
    //        },
    //        async: false
    //    }).responseText;
    //
    //}

});

