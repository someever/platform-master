/**
 * 礼包码-礼包码处理controllers文件
 *   author：钟亮
 * @type {module|*}
 */
adminApp.config(function ($routeProvider) {
    $routeProvider.when('/bagCodeDispose/view', {
        templateUrl: ctx + '/operation/bagCode/bagCodeDisposeInit',
        controller: 'bagCodeDisposeController'
    }).when("/bagCodeDispose/view/:check", {
        templateUrl: ctx + '/operation/bagCode/bagCodeDisposeInit',
        controller: 'bagCodeDisposeController'
    });

});


/**
 礼包码-礼包码处理controller
 全局：$scope
 */
adminApp.controller('bagCodeDisposeController', function ($scope, bagCodeDisposeFnService, $location, $routeParams, $rootScope) {
    $scope.bagCodeDisposeSearch = {};
    $scope.bagCodeDisposeSearch.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步

    $scope.messagesData = {};//设置提示框model数据集合
    $scope.messagesConfirm = {};//设置确认框model数据集合

    $scope.bagCodeIds = "";

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
     * 分页
     * @param bagCodeDisposeSearch
     * @param pageIndex
     * @param pageSize
     */
    function batchCodeQueryPage(bagCodeDisposeSearch, pageIndex, pageSize) {
        /*
         调用查询方法，并传最新的currentPage
         */
        bagCodeDisposeFnService.batchCodeDisposePage(bagCodeDisposeSearch, pageIndex, pageSize).success(function (response) {
            $scope.bagCodeDisposeDatas = response.rows;
            $scope.pageCount = response.page.pageCount;
            $scope.$apply();
        });
    }

    /**
     调用查询方法，并传最新的currentPage
     */
    batchCodeQueryPage($scope.bagCodeDisposeSearch, 1, 10);

    /**
     * 搜索单击事件
     */
    $scope.searchButtonClicked = function () {
        /*
         调用查询方法，并传最新的currentPage
         */
        batchCodeQueryPage($scope.bagCodeDisposeSearch, 1, 10);
    }

    /**
     *分页
     */
    $scope.onPageChange = function () {
        /*
         调用查询方法，并传最新的currentPage
         */
        batchCodeQueryPage($scope.bagCodeDisposeSearch, $scope.currentPage, 10);
    };

    /**
     * 修改状态
     * @param id
     * @param drawStatus
     */
    $scope.updateDrawStatus = function (id, drawStatus) {
        var codeData = {};
        codeData.id = id;
        codeData.drawStatus = drawStatus;
        bagCodeDisposeFnService.updateDrawStatus(codeData).success(function () {
            $scope.messagesData.messagesTitle = "load.messagesTitleByReset";
            $scope.messagesData.messagesBody = "load.messagesBodyByResetToSuccess";
            $("#messagesModal").modal('show');
            batchCodeQueryPage($scope.bagCodeDisposeSearch, $scope.currentPage, 10);
        }).error(function () {
            $scope.messagesData.messagesTitle = "load.messagesTitleByReset";
            $scope.messagesData.messagesBody = "load.messagesBodyByResetToFailure";
            $("#messagesModal").modal('show');
            /*
             调用查询方法，并传最新的currentPage
             */
            batchCodeQueryPage($scope.bagCodeDisposeSearch, $scope.currentPage, 10);
        });


    }

    /**
     更改选择状态，并保存相应的id
     */
    $scope.check = function (id, chk) {
        if (chk == true) {
            $scope.bagCodeIds += id + ",";
        } else {
            $scope.bagCodeIds = $scope.bagCodeIds.replace(id + ",", "");
        }

    };

    /**
     重置状态confirm
     */
    $scope.resetStatus = function () {
        var reset = function () {
            if ($scope.bagCodeIds != "") {
                $scope.messagesConfirm.title = "load.messagesConfirmTitleByReset";
                $scope.messagesConfirm.body = "load.messagesConfirmBodyByReset";
                $("#messagesConfirm").modal('show');
            } else {
                $scope.messagesData.messagesTitle = "load.promptTitle";
                $scope.messagesData.messagesBody = "load.promptBody";
                $("#messagesModal").modal('show');
            }

        };
        //判断是否有权限
        checkAuthority(3, reset);//1：添，2：删，3：改，4：查 执行函数

    };

    /**
     重置状态确认
     */
    $scope.confirm = function () {
        var codeData = {};
        codeData.id = $scope.bagCodeIds;
        codeData.drawStatus = 0;
        bagCodeDisposeFnService.updateDrawStatus(codeData).success(function () {
            $scope.bagCodeIds="";
            $scope.messagesData.messagesTitle = "load.messagesTitleByReset";
            $scope.messagesData.messagesBody = "load.messagesBodyByResetToSuccess";
            $("#messagesModal").modal('show');
            batchCodeQueryPage($scope.bagCodeDisposeSearch, $scope.currentPage, 10);
        }).error(function () {
            $scope.bagCodeIds="";
            $scope.messagesData.messagesTitle = "load.messagesTitleByReset";
            $scope.messagesData.messagesBody = "load.messagesBodyByResetToFailure";
            $("#messagesModal").modal('show');
            /*
             调用查询方法，并传最新的currentPage
             */
            batchCodeQueryPage($scope.bagCodeDisposeSearch, $scope.currentPage, 10);
        });


    };


});