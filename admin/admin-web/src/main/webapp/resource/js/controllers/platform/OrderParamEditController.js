/**
 * 模拟充值编辑controllers文件
 *   author：钟亮
 * @type {module|*}
 */
adminApp.config(function ($routeProvider) {
    $routeProvider.when('/orderParamEdit/view', {
        templateUrl: ctx + '/platform/billingOrder/orderParamEdit',
        controller: 'editionEditController'
    }).when("/orderParamEdit/view/:id/:judge/:versionType", {
        templateUrl: ctx + '/platform/edition/editionEdit',
        controller: 'editionEditController'
    });
    ;
});


/**
 模拟充值编辑controller
 全局：$scope
 */
adminApp.controller('orderParamEditController', function ($scope, billingOrderFnService, $location, gameAreaFnService, billingGoodsFnService, $routeParams, $rootScope) {
    //初始化菜单栏
    $rootScope.menuBarData.menuBarThreeName = "load.orderParamTitle";
    $rootScope.menuBarData.menuBarTitle = "load.orderParamTitle";
    $scope.messagesData = {};//设置提示框model数据集合
    $scope.messagesConfirm = {};//设置确认框model数据集合
    $scope.orderParamData = {};//
    $scope.gameOrderSiteList = $scope.siteTransfer;//初始化gameToySiteList，与全局site同步
    $scope.gameOrderGameList = $scope.gameTransfer;//gameToyGameList，与全局game同步
    $scope.roleSearchData = {};//角色查询条件
    $scope.roleSearchData.gameId = $scope.gameTransfer;//初始化查询gameId
    $scope.roleSearchData.siteId = $scope.siteTransfer;//初始化查询siteId
    $scope.roleData = {};//角色Data
    $scope.areaLists = [];//区服集合
    $scope.areaId = [];//区服id集合
    $scope.areaCode = [];//区服name集合
    $scope.billingGoodsSearch = {};//初始化查询条件对象
    $scope.billingGoodsSearch.gameId = $scope.gameTransfer;//初始化查询gameId
    $scope.billingGoodsSearch.siteId = $scope.siteTransfer;//初始化查询siteId
    $scope.placeholderGoodsName = "load.placeholderGoodsName";//道具名称搜索国际化
    var check = -1;//判断是否成功，url参数 1，是修改成功，0是修改失败，2是添加成功，3是添加失败，-1是失效
    $scope.checkOrder = false;
    $scope.VipExperience = false;
    //初始化
    $(".roleIdGreen").hide();
    $(".roleIdRed").hide();
    $(".roleNameGreen").hide();
    $(".roleNameRed").hide();

    gameAreaFindByNamePublic($scope.roleSearchData);
    /**
     * 调用查询方法，并传最新的currentPage
     * @param gameToyTypeSearch
     * @param pageIndex
     * @param pageSize
     */
    function gameAreaFindByNamePublic(search) {
        gameAreaFnService.gameAreaFindByGameId(search).success(function (response) {
            $scope.areaLists = response;
            $scope.$apply();
        });
    };

    /*
     刷新查询页面，调用查询方法
     */
    billingGoodsFnService.billingGoodsFindByName($scope.billingGoodsSearch, 1, 10).success(function (response) {
        console.log(response);
        $scope.goods = response.rows;
        $scope.pageCountItem = response.page.pageCount;
        $scope.$apply();
    });


    /*
     商品
     */
    $scope.onPageChangeItem = function () {
        // ajax request to load data
        console.log($scope.currentPage);
        /*
         调用查询方法，并传最新的currentPage
         */
        billingGoodsFnService.billingGoodsFindByName($scope.billingGoodsSearch, $scope.currentPageItem, 10).success(function (response) {
            $scope.goods = response.rows;
            $scope.pageCountItem = response.page.pageCount;
            $scope.$apply();
        });
    };

    /**
     *查询表单监听
     */
    $scope.searchButtonClicked = function () {
        /*
         刷新查询页面，调用查询方法
         */
        billingGoodsFnService.billingGoodsFindByName($scope.billingGoodsSearch, 1, 10).success(function (response) {
            console.log(response);
            $scope.goods = response.rows;
            $scope.pageCountItem = response.page.pageCount;
            $scope.$apply();
        });
    }

    /**
     * 商品选择
     * @param data
     */
    $scope.goodsChoice = function (data) {
        $scope.orderParamData.goodsId = data.goodsCode;
        $scope.orderParamData.goodsCode = data.goodsCode;
        $scope.orderParamData.goodsCount = data.goodsCount;
        $scope.orderParamData.payAmount = data.goodsAmount;
        $("#goodsModal").modal('hide');

    };


    /**
     选择商品（弹框）
     */
    $scope.goodsClick = function () {
        $("#goodsModal").modal('show');
    };


    //检查是否存在角色
    $scope.checkRole = function () {
        billingOrderFnService.checkRole($scope.roleSearchData).success(function (data) {
            if (data.roleId == "" || data.roleId == null) {
                $(".roleIdGreen").hide();
                $(".roleIdRed").show();
                $(".roleNameGreen").hide();
                $(".roleNameRed").show();
                $scope.checkOrder = false;
            } else {
                $scope.roleSearchData.roleId = data.roleId;
                $scope.roleSearchData.roleName = data.roleName;

                $(".roleIdGreen").show();
                $(".roleIdRed").hide();
                $(".roleNameGreen").show();
                $(".roleNameRed").hide();
                $scope.checkOrder = true;
            }
            $scope.$apply();
        });
    };

    //区服查询
    $scope.resourceGameListChange = function (gameId) {
        $scope.roleSearchData.gameId = gameId;
        gameAreaFindByNamePublic($scope.roleSearchData);
    };

    /**
     * 监听表单
     */
    $scope.orderParamEditForm = function () {
        if ($scope.checkOrder) {
            $scope.orderParamData.roleId = $scope.roleSearchData.roleId;
            //$scope.orderParamData.payType = 4;//如果是模拟充值 id = 4
            $scope.orderParamData.currency = 2;//币种，没有就填RMB
            $scope.orderParamData.gameId = $scope.gameOrderGameList;
            $scope.orderParamData.siteId = $scope.gameOrderSiteList;
            if ($scope.VipExperience) {
                $scope.orderParamData.goodsCount = 0;
            }
            billingOrderFnService.orderParamAdd($scope.orderParamData).success(function () {
                check = 2;
                $location.path("/billingOrder/view/" + check);
                $scope.$apply();
                $("#messagesModal").modal('show');

            }).error(function () {
                check = 3;
                $location.path("/billingOrder/view/" + check);
                $scope.$apply();
                $("#messagesModal").modal('show');
            });
        } else {
            $scope.messagesData.messagesTitle = "load.promptOrderTitle";
            $scope.messagesData.messagesBody = "load.promptOrderBody";
            $("#messagesModal").modal('show');
        }

    };

    //清空
    $scope.removeRole = function () {
        $scope.roleSearchData.roleId = "";
        $scope.roleSearchData.roleName = "";
    }
})
;