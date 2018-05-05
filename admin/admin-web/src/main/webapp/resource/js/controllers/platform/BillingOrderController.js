/**
 * 订单查询controllers文件
 *   author：钟亮
 * @type {module|*}
 */
adminApp.config(function ($routeProvider) {
    $routeProvider.when('/billingOrder/view', {
        templateUrl: ctx + '/platform/billingOrder/billingOrderInit',
        controller: 'billingGoodsController'
    }).when("/billingOrder/view/:check", {
        templateUrl: ctx + '/platform/billingOrder/billingOrderInit',
        controller: 'billingOrderController'
    });
});


/*
 订单查询controller
 全局：$scope
 Service:billingGoodsFnService
 */
adminApp.controller('billingOrderController', function ($scope, billingOrderFnService, $location, $routeParams, $rootScope) {

    //初始化菜单栏
    $rootScope.menuBarData.menuBarThreeName = "";
    $rootScope.menuBarData.menuBarTitle = "BillingOrderManage";
    $scope.messagesData = {};//设置提示框model数据集合
    $scope.messagesConfirm = {};//设置确认框model数据集合
    $scope.billingGoodsSearch = {};//初始化查询条件对象
    $scope.billingGoodsSearch.gameId = $scope.gameTransfer;//初始化查询gameId
    $scope.billingGoodsSearch.siteId = $scope.siteTransfer;//初始化查询siteId
    $scope.goodsSearchText = "load.goodsSearchText";//搜索提示
    $scope.goodsId = "";//商品id字符串
    $scope.gameToyGameList = $scope.gameTransfer;//gameToyGameList，与全局game同步
    $scope.orderData = [];//订单data
    $scope.billingOrderSearch = {};//搜寻条件
    $scope.billingOrderSearch.gameId = $scope.gameTransfer;//绑定游戏
    $scope.billingOrderSearch.siteId = $scope.siteTransfer;//绑定渠道

    /*
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
     刷新查询页面，调用查询方法
     */
    function billingOrderFindByNamePublic(billingOrderSearch, pageIndex, pageSize) {
        billingOrderFnService.billingOrderPage(billingOrderSearch, pageIndex, pageSize).success(function (response) {
            console.log(response);
            $scope.orderData = response.rows;
            $scope.pageCount = response.page.pageCount;
            $scope.$apply();
        });
    }

    billingOrderFindByNamePublic($scope.billingOrderSearch, 1, 10);


    /*
    模拟充值
     */
    $scope.orderParamClicked = function () {
        $location.path("/orderParamEdit/view");
    };

    /**
     查询表单监听
     */
    $scope.searchButtonClicked = function () {
        billingOrderFindByNamePublic($scope.billingOrderSearch, $scope.currentPage, 10);
    };

    /**
     分页
     */
    $scope.onPageChange = function () {
        // ajax request to load data
        console.log($scope.currentPage);
        /*
         调用查询方法，并传最新的currentPage
         */
        billingOrderFindByNamePublic($scope.billingOrderSearch, $scope.currentPage, 10);
    };
});