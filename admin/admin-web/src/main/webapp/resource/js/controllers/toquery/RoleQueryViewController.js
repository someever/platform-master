adminApp.config(function ($routeProvider) {
    $routeProvider.when('/roleQueryView/view/:id/:type', {
        templateUrl: ctx + '/roleQuery/roleQueryViewInit',
        controller: 'roleQueryViewController'
    });
});

adminApp.controller('roleQueryViewController', function ($scope, gameAreaFnService, roleQueryService, $rootScope, $routeParams, $translate, roleQueryListService) {
    //初始化菜单栏
    $rootScope.menuBarData.menuBarThreeName = "";
    $rootScope.menuBarData.menuBarTitle = "PlayersQueryManage";
    $scope.playersItems = [];//查询结果转换
    $scope.messagesData = {};//设置提示框model数据集合
    $scope.messagesConfirm = {};//设置确认框model数据集合


    /**
     * 如果跳转参数是update，根据id查询edition对象
     */
    if ($routeParams.type == "view") {
        /*
         根据id查询公告
         */
        $scope.playersItems = [];
        roleQueryListService.getByRoleJson($routeParams.id).success(function (data) {
            var dataItem = [];
            dataItem = data;
            //if (data != "" && data != null) {
            //    dataItem = JSON.parse(data);
            //}
            console.log(dataItem);
            //拼装前端js数据
            for (var key in dataItem) {
                var item = {};
                item.dataTag = key;
                item.name = "load.ROLE" + key;
                var keyPd = key.toLowerCase();
                if (keyPd.indexOf("time") != -1) {
                    dataItem[key] = $scope.format(dataItem[key], "yyyy-MM-dd HH:mm:ss");
                }
                item.value = dataItem[key];
                item.identity = -1;
                $scope[key] = dataItem[key];
                $scope.playersItems.push(item);
            }
            $scope.tabPaneState = "PlayersQueryManage";
            $scope.$apply();
        });

    }


    /**
     * 更改修改状态
     */
    $scope.playersTransform = function () {
        $scope.condition = 2;
        for (var j = 0; j < $scope.playersItems.length; j++) {
            for (var i = 0; i < $scope.updateListJson.length; i++) {
                if ($scope.playersItems[j].dataTag == $scope.updateListJson[i].itemName) {
                    $scope.playersItems[j].identity = 0;
                }
            }
        }

    }

    /**
     * 监听修改表单
     */
    $scope.playersUpdateForm = function () {
        var playersUpdateData = [];
        for (var j = 0; j < $scope.playersItems.length; j++) {
            for (var i = 0; i < $scope.updateListJson.length; i++) {
                if ($scope.playersItems[j].dataTag == $scope.updateListJson[i].itemName) {
                    var dicItem = {};
                    dicItem.key = $scope.updateListJson[i].itemUpdateName;
                    dicItem.value = $scope[$scope.playersItems[j].dataTag];
                    playersUpdateData.push(dicItem);
                }
            }

        }
        console.log(playersUpdateData);
        $scope.playersSearchData.itemData = JSON.stringify(playersUpdateData);
        $scope.playersSearchData.gmMsgType = "Gm2LogicPlayerBaseInfoUpdateReq";

    }

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
        $scope.playersItems = [];
    }


});

