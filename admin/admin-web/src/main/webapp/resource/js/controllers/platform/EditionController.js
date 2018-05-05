adminApp.config(function ($routeProvider) {
    $routeProvider.when('/edition/view', {
        templateUrl: ctx + '/platform/edition/editionInit',
        controller: 'editionListController'
    }).when("/edition/view/:check/:pageNumber", {
        templateUrl: ctx + '/platform/edition/editionInit',
        controller: 'editionListController'
    });
});

/**
 * 版本补丁管理
 */
adminApp.controller('editionListController', function ($scope, editionFnService, $rootScope, $location, $routeParams, gameAreaFnService) {
    $scope.messagesData = {};//设置提示框model数据集合
    $scope.messagesConfirm = {};//设置确认框model数据集合
    $scope.editionSearch = {};//初始化查询条件对象
    $scope.editionSearch.gameId = $scope.gameTransfer;//初始化查询gameId
    $scope.editionSearch.siteId = $scope.siteTransfer;//初始化查询siteId
    $scope.editionSearch.versionType = 2;//2：补丁更新
    //初始化菜单栏
    $rootScope.menuBarData.menuBarThreeName = "";
    $rootScope.menuBarData.menuBarTitle = "EditionManage";
    $scope.editionIds = "";//版本补丁id字符串
    $scope.pushGameVersionData = {};//版本更新
    $scope.pushGameVersionData.gameId = $scope.gameTransfer;

    $scope.areaSearch = {};//区服查询条件
    $scope.areaSearch.gameId = $scope.gameTransfer;
    $scope.areaSearch.siteId = $scope.siteTransfer;
    /**
     *根据全局game，site查询区服 公共方法
     */
    function gameAreaFindByGameIdSelect(areaSearch) {
        gameAreaFnService.gameAreaFindByGameId(areaSearch).success(function (response) {
            $scope.areaLists = response;
            $scope.$apply();
        });
    }

    /**
     * 游戏变换区服
     * @param gameId
     */
    $scope.gameIdChange = function (gameId) {
        $scope.areaSearch.gameId = gameId;
        gameAreaFindByGameIdSelect($scope.areaSearch);
    };

    /**
     *根据全局game，site查询区服
     */
    gameAreaFindByGameIdSelect($scope.areaSearch);

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
    }
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

    //添加单击
    $scope.addButton = function () {
        var add = function () {
            $location.path("/editionEdit/view/0/add/" + 2+ "/null");//2代表批次更新
        };
        //判断是否有权限
        checkAuthority(1, add);//1：添，2：删，3：改，4：查 执行函数
    }


    /**
     调用查询方法，并传最新的currentPage
     */
    function editionPagePublic(editionSearch, pageIndex, pageSize) {
        editionFnService.editionPage(editionSearch, pageIndex, pageSize).success(function (response) {
            $scope.versionData = response.rows;
            $scope.pageCount = response.page.pageCount;
            $scope.$apply();
        });
    }

    if ($routeParams.pageNumber != null) {
        editionFnService.editionPage($scope.editionSearch, $scope.currentPage, 10).success(function (response) {
            $scope.versionData = response.rows;
            $scope.pageCount = response.page.pageCount;
            $scope.currentPage = $routeParams.pageNumber;
            $scope.$apply();
        });
    } else {
        /**
         调用查询方法，并传最新的currentPage
         */
        editionPagePublic($scope.editionSearch, 1, 10);
    }

    ///**
    // 调用查询方法，并传最新的currentPage
    // */
    //editionPagePublic($scope.editionSearch, 1, 10);

    /**
     * 全选
     * @param chk
     */
    $scope.allCheckList = function (chk, data) {
        $scope.chk = chk;
        console.log(data);
        if (chk == true) {
            for (var i = 0; i < data.length; i++) {
                $scope.editionIds += data[i].id + ",";
            }
        } else {
            $scope.editionIds = "";
        }
        console.log( $scope.editionIds);
    };

    /*
     分页
     */
    $scope.onPageChange = function () {
        $scope.chk = false;
        $(".allCheckData").prop("checked", false);
        console.log($scope.currentPage);
        /*
         调用userService的查询方法，并传最新的currentPage
         */
        editionPagePublic($scope.editionSearch, $scope.currentPage, 10);
    };


    /**
     更改区服的选择状态，并保存相应的id
     */
    $scope.check = function (val, chk) {
        if (chk == true) {
            $scope.editionIds += val + ",";
        } else {
            $scope.editionIds = $scope.editionIds.replace(val + ",", "");
        }

    };

    /**
     删除角色confirm
     */
    $scope.deleteButton = function () {
        var deleteL = function () {
            if ($scope.editionIds != "") {
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
        $scope.chk = false;
        $(".allCheckData").prop("checked", false);
        /*
         调用删除方法
         */
        editionFnService.editionDelete($scope.editionIds).success(function () {
            $scope.messagesData.messagesTitle = "load.messagesTitleByDelete";
            $scope.messagesData.messagesBody = "load.messagesBodyByDeleteToSuccess";
            $("#messagesModal").modal('show');
            /*
             调用查询方法，并传最新的currentPage
             */
            editionPagePublic($scope.editionSearch, $scope.currentPage, 10);
            $scope.editionIds = "";
        }).error(function () {
            $scope.messagesData.messagesTitle = "load.messagesTitleByDelete";
            $scope.messagesData.messagesBody = "load.messagesBodyByDeleteToFailure";
            $("#messagesModal").modal('show');
            /*
             调用查询方法，并传最新的currentPage
             */
            editionPagePublic($scope.editionSearch, $scope.currentPage, 10);

            $scope.editionIds = "";
        });
    };

    /**
     修改（调转）
     */
    $scope.updateClicked = function (id) {
        var update = function () {
            $location.path("/editionEdit/view/" + id + "/update/" + 2+ "/" + $scope.currentPage);//2代表批次更新
        };
        //判断是否有权限
        checkAuthority(3, update);//1：添，2：删，3：改，4：查 执行函数


    };


    /**
     * 推送游戏版本号（弹框）
     */
    $scope.refreshVersion = function () {
        $("#refreshVersionModal").modal('show');
    };


    /**
     * 推送版本号
     */
    $scope.pushGameVersionForms = function () {
        editionFnService.pushGameVersion($scope.pushGameVersionData).success(function () {
            $scope.messagesData.messagesTitle = "load.noticeTipTitle";
            $scope.messagesData.messagesBody = "load.pushGameVersionNumberSuccess";
            $("#messagesModal").modal('show');
            /*
             调用查询方法，并传最新的currentPage
             */
            editionPagePublic($scope.editionSearch, $scope.currentPage, 10);
            $("#refreshVersionModal").modal('hide');
        }).error(function () {
            $scope.messagesData.messagesTitle = "load.noticeTipTitle";
            $scope.messagesData.messagesBody = "load.pushGameVersionNumberFailure";
            $("#messagesModal").modal('show');
            /*
             调用查询方法，并传最新的currentPage
             */
            editionPagePublic($scope.editionSearch, $scope.currentPage, 10);
            $("#refreshVersionModal").modal('hide');
        });
    }

    /**
     * 刷新缓存
     */
    $scope.refreshButton = function () {
        editionFnService.refreshCacheAll().success(function () {
            $scope.messagesData.messagesTitle = "load.UpAreaMessagesTitle";
            $scope.messagesData.messagesBody = "load.UpAreaMessagesToSuccess";
            $("#messagesModal").modal('show');
            /*
             调用查询方法，并传最新的currentPage
             */
            editionPagePublic($scope.editionSearch, $scope.currentPage, 10);
        }).error(function () {
            $scope.messagesData.messagesTitle = "load.UpAreaMessagesTitle";
            $scope.messagesData.messagesBody = "load.UpAreaMessagesToFailure";
            $("#messagesModal").modal('show');
            /*
             调用查询方法，并传最新的currentPage
             */
            editionPagePublic($scope.editionSearch, $scope.currentPage, 10);
        });
    }
});

