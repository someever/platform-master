/**
 * 活动开关controllers文件
 *   author：钟亮
 * @type {module|*}
 */
adminApp.config(function ($routeProvider) {
    $routeProvider.when('/gameActivityCon/view', {
        templateUrl: ctx + '/operation/gameActivityCon/gameActivityConInit',
        controller: 'GameActivityConController'
    }).when("/gameActivityCon/view/:check/:pageNumber", {
        templateUrl: ctx + '/operation/gameActivityCon/gameActivityConInit',
        controller: 'GameActivityConController'
    });
    ;
});


/**
 活动开关controller
 全局：$scope
 */
adminApp.controller('GameActivityConController', function ($scope, gameActivityConFnService, $location, $routeParams, $rootScope) {
    $scope.messagesData = {};//设置提示框model数据集合
    $scope.messagesConfirm = {};//设置确认框model数据集合
    $scope.gameActivityConSearch = {};//活动查询条件集合
    $scope.gameActivityConSearch.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
    $scope.activityConGameList = $scope.gameTransfer;//初始化gameId，与全局game同步
    $scope.activityConIds = "";//活动id字符串
    $scope.gameActivityConDatas = [];//list集合
    $scope.activityState = {};//删除
    $scope.batchRemoveAreaText = "";
    //初始化菜单栏
    $rootScope.menuBarData.menuBarThreeName = "";
    $rootScope.menuBarData.menuBarTitle = "GameActivityConManage";
    $scope.areaLists = [];//区服集合
    $scope.areaId = [];//区服id集合
    $scope.areaCode = [];//区服name集合
    /**
     * 判断是否有操作权限
     */
    function checkAuthority(type, execute) {
        //判断是否有操作权限
        $.ajax({
            url: ctx + '/system/relation/checkAuthority',
            data: {menuName: $rootScope.menuBarData.menuBarTitle, type: type},//1：添，2：删，3：改，4：查
            success: function (data) {
                if (data) {
                    execute();
                } else {
                    $scope.messagesData.messagesTitle = "load.messagesTitleByAuthorityLimit";
                    $scope.messagesData.messagesBody = "load.messagesBodyByAuthorityLimit";
                    $("#messagesModal").modal('show');
                }
            }, async: false
        });

    }

    /**
     1：修改成功，0：修改失败，2,：添加成功，3：添加失败
     */
    if ($routeParams.check == 1) {
        $scope.messagesData.messagesTitle = "load.messagesTitleByUpdate";
        $scope.messagesData.messagesBody = "load.messagesBodyByUpdateToSuccess";
    } else if ($routeParams.check == 0) {
        $scope.messagesData.messagesTitle = "load.messagesTitleByUpdate";
        $scope.messagesData.messagesBody = "load.messagesBodyByUpdateToFailure";
    } else if ($routeParams.check == 2) {
        $scope.messagesData.messagesTitle = "load.messagesTitleByadd";
        $scope.messagesData.messagesBody = "load.messagesBodyByaddToSuccess";
    } else if ($routeParams.check == 3) {
        $scope.messagesData.messagesTitle = "load.messagesTitleByadd";
        $scope.messagesData.messagesBody = "load.messagesBodyByaddToFailure";
    } else if ($routeParams.check == "load.gameActivity230004") {
        $scope.messagesData.messagesTitle = "load.messagesTitleByUnusual";
        $scope.messagesData.messagesBody = "load.gameActivity230004";
    }



    /**
     模板编辑（跳转）
     */
    $scope.MuBanEdit = function () {
        var add = function () {
            $location.path("/gameActivityConEdit/view/null/5/" + 1);
        };
        //判断是否有权限
        checkAuthority(1, add);//1：添，2：删，3：改，4：查 执行函数

    }

    /**
     模板获取（跳转）
     */
    $scope.MuBanGet = function () {
        var add = function () {
            $location.path("/gameActivityConEdit/view/null/6/" + 1);
        };
        //判断是否有权限
        checkAuthority(1, add);//1：添，2：删，3：改，4：查 执行函数

    }

    /**
     添加（跳转）
     */
    $scope.addButton = function () {
        var add = function () {
            $location.path("/gameActivityConEdit/view/null/null/" + 1);
        };
        //判断是否有权限
        checkAuthority(1, add);//1：添，2：删，3：改，4：查 执行函数

    }


    /**
     添加（跳转）累积充值
     */
    $scope.LeiJiMuBanButton = function () {
        var add = function () {
            $location.path("/gameActivityConEdit/view/null/1/" + 1);
        };
        //判断是否有权限
        checkAuthority(1, add);//1：添，2：删，3：改，4：查 执行函数

    };

    /**
     添加（跳转）单笔充值
     */
    $scope.DanBiMuBanButton = function () {
        var add = function () {
            $location.path("/gameActivityConEdit/view/null/2/" + 1);
        };
        //判断是否有权限
        checkAuthority(1, add);//1：添，2：删，3：改，4：查 执行函数

    };

    /**
     添加（跳转）累积消费
     */
    $scope.LeiJiXiaoFeiButton = function () {
        var add = function () {
            $location.path("/gameActivityConEdit/view/null/3/" + 1);
        };
        //判断是否有权限
        checkAuthority(1, add);//1：添，2：删，3：改，4：查 执行函数

    };

    /**
     添加（跳转）每日充值
     */
    $scope.MeiRiChongZhiButton = function () {
        var add = function () {
            $location.path("/gameActivityConEdit/view/null/4/" + 1);
        };
        //判断是否有权限
        checkAuthority(1, add);//1：添，2：删，3：改，4：查 执行函数

    };

    /**
     * 批量删除区服（弹窗）
     */
    $scope.batchRemoveArea = function () {
        var removeArea = function () {
            $scope.deselectAllArea();
            if ($scope.activityConIds != "") {
                $scope.activityData = {};
                $scope.activityData.gameId = $scope.gameTransfer;
                $scope.activityData.ids = $scope.activityConIds;
                gameActivityConFnService.getAreaListByActivityId($scope.activityData).success(function (data) {
                    console.log(data);
                    $scope.areaLists = data;
                    $scope.$apply();
                });
                $("#batchRemoveArea").modal("show");
            } else {
                $scope.messagesData.messagesTitle = "load.promptTitle";
                $scope.messagesData.messagesBody = "load.promptBody";
                $("#messagesModal").modal('show');
            }

        };
        //判断是否有权限
        checkAuthority(3, removeArea);//1：添，2：删，3：改，4：查 执行函数
    };

    /**
     * 批量删除区服
     */
    $scope.batchRemoveAreaForms = function () {
        var areaids = "";
        for (var i = 0; i < $scope.areaId.length; i++) {
            areaids += $scope.areaId[i] + ",";
        }
        gameActivityConFnService.gameActivityConAreaDelete($scope.activityConIds, areaids).success(function () {
            $("#batchRemoveArea").modal("hide");
            $scope.messagesData.messagesTitle = "load.messagesTitleByDelete";
            $scope.messagesData.messagesBody = "load.messagesBodyByDeleteToSuccess";
            $("#messagesModal").modal('show');
            $scope.activityConIds = "";
            $scope.deselectAllArea();
            /*
             调用查询方法，并传最新的currentPage
             */
            gameActivityConGetPage($scope.gameActivityConSearch, $scope.currentPage, 10);
        }).error(function () {
            $("#batchRemoveArea").modal("hide");
            $scope.messagesData.messagesTitle = "load.messagesTitleByDelete";
            $scope.messagesData.messagesBody = "load.messagesBodyByDeleteToFailure";
            $("#messagesModal").modal('show');
            $scope.activityConIds = "";
            $scope.deselectAllArea();
            /*
             调用查询方法，并传最新的currentPage
             */
            gameActivityConGetPage($scope.gameActivityConSearch, $scope.currentPage, 10);
        });

    };

    $scope.updateAreaSelection = function ($event, id, code) {
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
        updateAreaSelected(action, id, code);//选中判断逻辑方法
        console.log($scope.areaId + "/////////" + $scope.areaCode);
    };

    /**
     全选，遍历checkboxModel，正向操作
     */
    $scope.areaAll = function (v) {
        var checkbox = $(".checkList span");
        for (var i = 0; i < checkbox.length; i++) {
            $(checkbox[i]).attr("class", "label label-success");
        }
        var idData = [];
        var codeData = [];
        //$scope.areaModel = true;
        for (var i = 0; i < v.length; i++) {
            idData.push(v[i].id);
            codeData.push(v[i].areaName);
        }
        $scope.areaId = idData;
        $scope.areaCode = codeData;
        console.log($scope.areaId + "/////////" + $scope.areaCode);
    };

    /**
     全不选
     */
    $scope.deselectAllArea = function () {
        var checkbox = $(".checkList span");
        for (var i = 0; i < checkbox.length; i++) {
            $(checkbox[i]).attr("class", "label label-default");
        }
        $scope.areaId = [];
        $scope.areaCode = [];
    };

    /**
     反选
     */
    $scope.areaVersa = function () {
        var checkbox = $(".checkList span");
        /*
         遍历checkbox，并反向操作
         */
        for (var i = 0; i < checkbox.length; i++) {
            if ($(checkbox[i]).attr("class") == "label label-success") {
                $(checkbox[i]).attr("class", "label label-default");
                var idx = $scope.areaId.indexOf(Number($(checkbox[i]).attr('name')));
                $scope.areaId.splice(idx, 1);
                $scope.areaCode.splice(idx, 1);

            } else {
                $scope.areaId.push(Number($(checkbox[i]).attr('name')));
                $scope.areaCode.push($(checkbox[i]).text());
                $(checkbox[i]).attr("class", "label label-success");

            }

        }
        console.log($scope.areaId + "/////////" + $scope.areaCode);
    }

    /**
     选中判断逻辑方法（区服）
     */
    var updateAreaSelected = function (action, id, code) {
        /*
         true添加集合元素
         */
        if (action == 'add' && $scope.areaId.indexOf(id) == -1) {
            $scope.areaId.push(id);
            $scope.areaCode.push(code);
        }
        /*
         false移除集合元素
         */
        if (action == 'remove' && $scope.areaId.indexOf(id) != -1) {
            var idx = $scope.areaId.indexOf(id);
            $scope.areaId.splice(idx, 1);
            $scope.areaCode.splice(idx, 1);
        }
    }


    /**
     * 修改
     * @param id
     */
    $scope.updateClicked = function (id) {
        var update = function () {
            $location.path("/gameActivityConEdit/view/" + id + "/update/" + $scope.currentPage);
        };
        //判断是否有权限
        checkAuthority(3, update);//1：添，2：删，3：改，4：查 执行函数

    }


    /**
     * 查询的公共方法
     * @param gameActivityConSearch
     * @param pageIndex
     * @param pageSize
     */
    function gameActivityConGetPage(gameActivityConSearch, pageIndex, pageSize) {
        /*
         调用查询方法，并传最新的currentPage
         */
        gameActivityConFnService.gameActivityConPage(gameActivityConSearch, pageIndex, pageSize).success(function (response) {
            $scope.gameActivityConDatas = response.rows;
            $scope.pageCount = response.page.pageCount;
            $scope.$apply();
        });
    }

    if ($routeParams.pageNumber != null) {
        /**
         调用查询方法，并传最新的currentPage
         */
        /*
         调用查询方法，并传最新的currentPage
         */
        gameActivityConFnService.gameActivityConPage($scope.gameActivityConSearch, $routeParams.pageNumber, 10).success(function (response) {
            $scope.gameActivityConDatas = response.rows;
            $scope.pageCount = response.page.pageCount;
            $scope.currentPage = $routeParams.pageNumber;
            $scope.$apply();
        });
    } else {
        /**
         调用查询方法，并传最新的currentPage
         */
        gameActivityConGetPage($scope.gameActivityConSearch, 1, 10);
    }


    /**
     *分页
     */
    $scope.onPageChange = function () {
        //$scope.chk = false;
        //$(".allCheckData").prop("checked", false);
        /**
         调用查询方法，并传最新的currentPage
         */
        gameActivityConGetPage($scope.gameActivityConSearch, $scope.currentPage, 10);
    };

    ///**
    // * 全选
    // * @param chk
    // */
    //$scope.allCheckList = function (chk, gameActivityConDatas) {
    //    $scope.chk = true;
    //    console.log(gameActivityConDatas);
    //};

    /**
     更改公告的选择状态，并保存相应的id
     */
    $scope.check = function (id, chk, date, row) {
        var time1 = $scope.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        var time2 = $scope.format(date, "yyyy-MM-dd HH:mm:ss");
        var date1 = new Date(time1.replace("-", "/"));
        var date2 = new Date(time2.replace("-", "/"));
        //if (date2 < date1 && chk == true) {
        //    row.chk = false;
        //    $scope.messagesData.messagesTitle = "load.noticeTipTitle";
        //    $scope.messagesData.messagesBody = "load.gameActivityConDeleteDescTime";
        //    $("#messagesModal").modal('show');
        //} else {
        if (chk == true) {
            $scope.activityConIds += id + ",";
        } else {
            $scope.activityConIds = $scope.activityConIds.replace(id + ",", "");
        }
        //}


    };

    /**
     删除角色confirm
     */
    $scope.delete = function () {
        var deleteL = function () {
            if ($scope.activityConIds != "") {
                $scope.messagesConfirm.title = "load.messagesConfirmTitleByDelete";
                $scope.messagesConfirm.body = "load.messagesConfirmBodyByDelete";
                $("#messagesConfirm").modal('show');
            } else {
                $scope.messagesData.messagesTitle = "load.promptTitle";
                $scope.messagesData.messagesBody = "load.promptBody";
                $("#messagesModal").modal('show');
            }

        };
        //判断是否有权限
        checkAuthority(2, deleteL);//1：添，2：删，3：改，4：查 执行函数

    };

    /**
     删除角色确认
     */
    $scope.confirm = function () {
        gameActivityConFnService.gameActivityConDelete($scope.activityConIds).success(function () {
            $scope.messagesData.messagesTitle = "load.messagesTitleByDelete";
            $scope.messagesData.messagesBody = "load.messagesBodyByDeleteToSuccess";
            $("#messagesModal").modal('show');
            $scope.activityConIds = "";
            /*
             调用查询方法，并传最新的currentPage
             */
            gameActivityConGetPage($scope.gameActivityConSearch, $scope.currentPage, 10);
        }).error(function () {
            $scope.messagesData.messagesTitle = "load.messagesTitleByDelete";
            $scope.messagesData.messagesBody = "load.messagesBodyByDeleteToFailure";
            $("#messagesModal").modal('show');
            $scope.activityConIds = "";
            /*
             调用查询方法，并传最新的currentPage
             */
            gameActivityConGetPage($scope.gameActivityConSearch, $scope.currentPage, 10);
        });

    };
});