/**
 * 玩家查询列表controllers文件
 *   author：钟亮
 * @type {module|*}
 */
adminApp.config(function ($routeProvider) {
    $routeProvider.when('/roleQueryList/view', {
        templateUrl: ctx + '/roleQuery/roleQueryListInit',
        controller: 'RoleQueryListController'
    }).when("/roleQueryList/view/:check", {
        templateUrl: ctx + '/roleQuery/roleQueryListInit',
        controller: 'RoleQueryListController'
    });
});


/**
 玩家查询列表controller
 全局：$scope
 */
adminApp.controller('RoleQueryListController', function ($scope, $location, $routeParams, $rootScope, gameAreaFnService, roleQueryListService) {

    $scope.roleQueryListData = [];//角色数据集合
    $scope.roleQueryListSearchData = {};//分页搜索条件
    $scope.roleQueryListSearchData.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
    $scope.roleQueryListSearchData.siteId = $scope.siteTransfer;//初始化gameId，与全局game同步
    $scope.gameAreasData = {};
    $scope.gameAreasData.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
    $.ajax({
        url: ctx + '/platform/gameArea/getGameAreasById',
        data: {"gameId": $scope.gameAreasData.gameId},
        success: function (data) {
            $scope.areaLists = data;
        },
        async: false
    });

    /**
     * 查询的公共方法
     * @param gameActivityConSearch
     * @param pageIndex
     * @param pageSize
     */
    function roleQueryListGetPage(roleQueryListSearchData, pageIndex, pageSize) {
        /*
         调用查询方法，并传最新的currentPage
         */
        roleQueryListService.gameRolePage(roleQueryListSearchData, pageIndex, pageSize).success(function (response) {
            $scope.roleQueryListData = response.rows;
            $scope.pageCount = response.page.pageCount;
            $scope.$apply();
        });
    };

    //区服改变
    $scope.areaChange = function () {
        roleQueryListGetPage($scope.roleQueryListSearchData, 1, 10);
    };

    /**
     * 分页查询，条件
     */
    $scope.searchButtonClicked = function () {
        /**
         调用查询方法，并传最新的currentPage
         */
        roleQueryListGetPage($scope.roleQueryListSearchData, 1, 10);
    };

    /**
     调用查询方法，并传最新的currentPage
     */
    roleQueryListGetPage($scope.roleQueryListSearchData, 1, 10);

    /*
     分页
     */
    $scope.onPageChange = function () {
        /**
         调用查询方法，并传最新的currentPage
         */
        roleQueryListGetPage($scope.roleQueryListSearchData, $scope.currentPage, 10);
    };

    /**
     * 跳转角色详情
     * @param id
     */
    $scope.roleViewJump = function (id) {
        $location.path("/roleQueryView/view/" + id + "/view")
    }
});