/**
 * 玩家封停controllers文件
 *   author：钟亮
 * @type {module|*}
 */
adminApp.config(function ($routeProvider) {
    $routeProvider.when('/gameClosure/view', {
        templateUrl: ctx + '/toyQuery/gameClosure/gameClosureInit',
        controller: 'GameClosureController'
    }).when("/gameClosure/view/:check", {
        templateUrl: ctx + '/toyQuery/gameClosure/gameClosureInit',
        controller: 'GameClosureController'
    });
    ;
});


/**
 玩家封停管理controller
 全局：$scope
 */
adminApp.controller('GameClosureController', function ($scope, $location, $routeParams, $rootScope, gameAreaFnService, billingOrderFnService, gameClosureService) {
    $scope.playersSearchData = {};//搜索条件
    $scope.playersSearchData.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
    $scope.playersData = {};//封停禁言对象
    $scope.areaSearch = {};//区服查询条件
    $scope.areaSearch.gameId = $scope.gameTransfer;
    $scope.areaSearch.siteId = $scope.siteTransfer;
    $scope.messagesData = {};//设置提示框model数据集合
    $scope.messagesConfirm = {};//设置确认框model数据集合
    $scope.checkOrder = false;
    $scope.gameClosureSearchData = {};//分页搜索条件
    $scope.gameClosureSearchData.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
    $scope.gameClosureSearchData.siteId = $scope.siteTransfer;//初始化gameId，与全局game同步
    $scope.gameClosureData = {};//数据集合
    //初始化
    $(".roleIdGreen").hide();
    $(".roleIdRed").hide();
    $(".roleNameGreen").hide();
    $(".roleNameRed").hide();
    $(".roleDataFrom").hide();
    $(".checkRole").show();


    /**
     * 分页查询，条件
     */
    $scope.searchButtonClicked = function () {
        /**
         调用查询方法，并传最新的currentPage
         */
        gameClosureGetPage($scope.gameClosureSearchData, 1, 10);
    };

    /**
     *分页
     */
    $scope.onPageChange = function () {
        /*
         刷新查询页面，调用查询方法
         */
        gameClosureGetPage($scope.gameClosureSearchData, $scope.currentPage, 10);
    };

    /**
     * 封停添加
     */
    $scope.addButton = function () {
        //初始化
        $(".roleIdGreen").hide();
        $(".roleIdRed").hide();
        $(".roleNameGreen").hide();
        $(".roleNameRed").hide();
        $(".roleDataFrom").hide();
        $(".checkRole").show();
        $scope.playersSearchData = {};
        $scope.playersSearchData.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
        $scope.playersItems = [];
        $scope.playersData.roleStatus = 2;//2代表封停
        $("#gameRoleModalForSearch").modal('show');
    };

    /**
     * 禁言添加
     */
    $scope.addShutUpButton = function () {
        //初始化
        $(".roleIdGreen").hide();
        $(".roleIdRed").hide();
        $(".roleNameGreen").hide();
        $(".roleNameRed").hide();
        $(".roleDataFrom").hide();
        $(".checkRole").show();
        $scope.playersSearchData = {};
        $scope.playersSearchData.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
        $scope.playersItems = [];
        $scope.playersData.roleStatus = 1;//1代表禁言
        $("#gameRoleModalForSearch").modal('show');
    };
    /**
     * 查询的公共方法
     * @param gameActivityConSearch
     * @param pageIndex
     * @param pageSize
     */
    function gameClosureGetPage(gameClosureSearchData, pageIndex, pageSize) {
        /*
         调用查询方法，并传最新的currentPage
         */
        gameClosureService.gameClosurePage(gameClosureSearchData, pageIndex, pageSize).success(function (response) {
            $scope.gameClosureData = response.rows;
            $scope.pageCount = response.page.pageCount;
            $scope.$apply();
        });
    }

    /**
     调用查询方法，并传最新的currentPage
     */
    gameClosureGetPage($scope.gameClosureSearchData, 1, 10);

    /**
     *gameId改变时，查询自定义表单
     */
    $scope.gameIdChange = function (gameId) {
        $scope.areaSearch.gameId = gameId;
        /*
         根据全局game，site查询区服
         */
        gameAreaFnService.gameAreaFindByGameId($scope.areaSearch).success(function (response) {
            $scope.areaLists = response;
            $scope.$apply();
        });
    };

    //检查是否存在角色
    $scope.checkRole = function () {
        billingOrderFnService.checkRole($scope.playersSearchData).success(function (data) {
            if (data.roleId == "" || data.roleId == null) {
                $(".roleIdGreen").hide();
                $(".roleIdRed").show();
                $(".roleNameGreen").hide();
                $(".roleNameRed").show();

                $(".roleDataFrom").hide();
                $(".checkRole").show();
                $scope.checkOrder = false;
            } else {
                $scope.playersSearchData.roleId = data.roleId;
                $scope.playersSearchData.roleName = data.roleName;

                $(".roleIdGreen").show();
                $(".roleIdRed").hide();
                $(".roleNameGreen").show();
                $(".roleNameRed").hide();

                $(".roleDataFrom").show();
                $(".checkRole").hide();
                $scope.checkOrder = true;
            }
            $scope.$apply();
        });
    };

    $scope.cancelModel = function () {
        $("#gameRoleModalForSearch").modal("hide");
    };

    //清空
    $scope.removeRole = function () {
        $scope.playersSearchData.roleId = "";
        $scope.playersSearchData.roleName = "";
        $scope.playersSearchData = {};
        $scope.playersSearchData.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
        $scope.playersItems = [];
    }

    /*
     根据全局game，site查询区服
     */
    gameAreaFnService.gameAreaFindByGameId($scope.areaSearch).success(function (response) {
        $scope.areaLists = response;
        $scope.$apply();
    });

    /**
     * 监听玩家查询表单
     */
    $scope.playersForm = function () {
        $scope.playersData.gameId = $scope.playersSearchData.gameId;
        $scope.playersData.roleId = $scope.playersSearchData.roleId;
        $scope.playersData.roleName = $scope.playersSearchData.roleName;
        gameClosureService.addGameClosure($scope.playersData).success(function () {
            $("#gameRoleModalForSearch").modal("hide");
            $scope.messagesData.messagesTitle = "load.messagesTitleByadd";
            $scope.messagesData.messagesBody = "load.messagesBodyByaddToSuccess";
            $("#messagesModal").modal('show');
            $scope.$apply();

            /**
             调用查询方法，并传最新的currentPage
             */
            gameClosureGetPage($scope.gameClosureSearchData, 1, 10);
        }).error(function () {
            $("#gameRoleModalForSearch").modal("hide");
            $scope.messagesData.messagesTitle = "load.messagesTitleByadd";
            $scope.messagesData.messagesBody = "load.messagesBodyByaddToFailure";
            $scope.$apply();
            $("#messagesModal").modal('show');
            $scope.$apply();
            /**
             调用查询方法，并传最新的currentPage
             */
            gameClosureGetPage($scope.gameClosureSearchData, 1, 10);

        });
    };

    $scope.outRole = function (id, status) {
        $scope.statusUpdate = {};
        $scope.statusUpdate.id = id;
        $scope.statusUpdate.status = 3;
        gameClosureService.updateGameClosureStatus($scope.statusUpdate).success(function () {
            $scope.messagesData.messagesTitle = "load.gameClosureRoleOutIip";
            $scope.messagesData.messagesBody = "load.gameClosureRoleOutSuccess";
            $scope.$apply();
            $("#messagesModal").modal('show');
            /**
             调用查询方法，并传最新的currentPage
             */
            gameClosureGetPage($scope.gameClosureSearchData, 1, 10);
        }).error(function () {
            $scope.messagesData.messagesTitle = "load.gameClosureRoleOutIip";
            $scope.messagesData.messagesBody = "load.gameClosureRoleOutFailure";
            $scope.$apply();
            $("#messagesModal").modal('show');
            $scope.$apply();
        });
    };

    /**
     * 返回搜索
     */
    $scope.returnSearchBox = function () {
        $scope.playersSearchBox = 0;
        $scope.playersSearchData.roleId = "";
        $scope.playersSearchData.userId = "";
        $scope.playersSearchData.roleName = "";
        $scope.playersItems = [];
    }

    /**
     * 清空
     */
    $scope.empty = function () {
        $scope.playersSearchData = {};
        $scope.playersSearchData.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
        $scope.playersItems = [];
    }

    /**
     * 重置
     */
    $scope.replacement = function () {
        //初始化
        $(".roleIdGreen").hide();
        $(".roleIdRed").hide();
        $(".roleNameGreen").hide();
        $(".roleNameRed").hide();
        $(".roleDataFrom").hide();
        $(".checkRole").show();
        $scope.playersSearchData = {};
        $scope.playersSearchData.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
        $scope.playersItems = [];
    }

});