/**
 * 玩家查询列表controllers文件
 *   author：钟亮
 * @type {module|*}
 */
adminApp.config(function ($routeProvider) {
    $routeProvider.when('/playersQueryList/view', {
        templateUrl: ctx + '/toyQuery/playersQueryList/playersQueryListInit',
        controller: 'PlayersQueryListController'
    }).when("/playersQueryList/view/:check", {
        templateUrl: ctx + '/toyQuery/playersQueryList/playersQueryListInit',
        controller: 'PlayersQueryListController'
    });
});


/**
 玩家查询列表controller
 全局：$scope
 */
adminApp.controller('PlayersQueryListController', function ($scope, $location, $routeParams, $rootScope, gameAreaFnService) {

});