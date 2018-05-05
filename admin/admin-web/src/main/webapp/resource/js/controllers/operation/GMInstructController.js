/**
 * GM指令controllers文件
 *   author：钟亮
 * @type {module|*}
 */
adminApp.config(function ($routeProvider) {
    $routeProvider.when('/gmInstruct/view', {
        templateUrl: ctx + '/operation/gmInstruct/gmInstructInit',
        controller: 'GMInstructController'
    }).when("/gmInstruct/view/:check", {
        templateUrl: ctx + '/operation/gmInstruct/gmInstructInit',
        controller: 'GMInstructController'
    });
    ;
});


/**
 GM指令管理controller
 全局：$scope
 */
adminApp.controller('GMInstructController', function ($scope, $location, $routeParams, $rootScope, gameAreaFnService, GMInstructFnService) {
    //初始化菜单栏
    $rootScope.menuBarData.menuBarThreeName = "";
    $rootScope.menuBarData.menuBarTitle = "GMInstructManage";
    $scope.messagesData = {};//设置提示框model数据集合
    $scope.messagesConfirm = {};//设置确认框model数据集合
    $scope.areaLists = [];//区服集合
    $scope.areaId = [];//区服id集合
    $scope.areaCode = [];//区服name集合
    $scope.gmInstructData = {};//gm指令集合
    $scope.gameAreasData = [];//区服查询条件集合
    $scope.gameAreasData.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
    $scope.gmInstructGameList = $scope.gameTransfer;//初始化gameId，与全局game同步
    /**
     绑定game,site select，change事件
     */
    $scope.resourceGameListChange = function (id) {
        $scope.gameAreasData.gameId = id;
        gameAreaFnService.gameAreaFindByGameId($scope.gameAreasData).success(function (response) {
            $scope.areaLists = response;
            $scope.$apply();
        });
    };

    /**
     根据全局game，site查询区服
     */
    if ($scope.gameAreasData.gameId == undefined) {
        $scope.gameAreasData.gameId = -1;
    }

    $.ajax({
        url: ctx + '/platform/gameArea/getGameAreasById',
        data: {"gameId": $scope.gameAreasData.gameId},
        success: function (data) {
            $scope.areaLists = data;
        },
        async: false
    });

    //指令发送
    $scope.GMInstructAddForm = function () {
        $scope.gmInstructData.gameId = $scope.gameAreasData.gameId;
        var areaIdString = "";
        for (var i = 0; i < $scope.areaId.length; i++) {
            areaIdString += $scope.areaId[i] + ",";
        }
        ;
        $scope.gmInstructData.areaIds = areaIdString;
        GMInstructFnService.gmInstruct($scope.gmInstructData).success(function () {
            $scope.gmInstructData = {};
            $scope.areaData=[];
            $scope.areaLists = [];//区服集合
            $scope.areaId = [];//区服id集合
            $scope.areaCode = [];//区服name集合
            $scope.gameAreasData = [];//区服查询条件集合
            $scope.gameAreasData.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
            $scope.gmInstructGameList = $scope.gameTransfer;//初始化gameId，与全局game同步
            $scope.messagesData.messagesTitle = "load.messagesTitleBygm";
            $scope.messagesData.messagesBody = "load.messagesTitleBygmSuccess";
            $("#messagesModal").modal('show');
            $scope.$apply();
        }).error(function () {
            $scope.gmInstructData = {};
            $scope.areaData=[];
            $scope.areaLists = [];//区服集合
            $scope.areaId = [];//区服id集合
            $scope.areaCode = [];//区服name集合
            $scope.gameAreasData = [];//区服查询条件集合
            $scope.gameAreasData.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
            $scope.gmInstructGameList = $scope.gameTransfer;//初始化gameId，与全局game同步
            $scope.messagesData.messagesTitle = "load.messagesTitleBygm";
            $scope.messagesData.messagesBody = "load.messagesBodyBygmFailure";
            $("#messagesModal").modal('show');
            $scope.$apply();
        });
    };

    $scope.resetForm = function () {
        $scope.gmInstructData = {};
        $scope.areaData=[];
        $scope.areaLists = [];//区服集合
        $scope.areaId = [];//区服id集合
        $scope.areaCode = [];//区服name集合
        $scope.gameAreasData = [];//区服查询条件集合
        $scope.gameAreasData.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
        $scope.gmInstructGameList = $scope.gameTransfer;//初始化gameId，与全局game同步
    };

    /*
     选择区服（弹框）
     */
    $scope.areaClick = function () {
        $("#areaAddModal").modal('show');
    };

    /*
     监听areaAdd表单
     */
    $scope.areaAdd = function () {
        $("#areaAddModal").modal('hide');
        $scope.areaData = $scope.areaCode;

    };

    /*
     区服 checkbox单击操作
     */
    $scope.updateAreaSelection = function ($event, id, code) {
        var checkbox = $event.target;
        var action = (checkbox.checked ? 'add' : 'remove');//判断是否选中
        updateAreaSelected(action, id, code);//选中判断逻辑方法
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
     全选，遍历checkboxModel，正向操作
     */
    $scope.areaAll = function (v) {
        var checkbox = $(".areaChildren");
        for (var i = 0; i < checkbox.length; i++) {
            checkbox[i].checked = true;
        }
        var idData = [];
        var codeData = [];
        $scope.areaModel = true;
        for (var i = 0; i < v.length; i++) {
            idData.push(v[i].id);
            codeData.push(v[i].areaName);
        }
        $scope.areaId = idData;
        $scope.areaCode = codeData;
    };

    /**
     全不选
     */
    $scope.deselectAllArea = function () {
        var checkbox = $(".areaChildren");
        for (var i = 0; i < checkbox.length; i++) {
            checkbox[i].checked = false;
        }
        $scope.areaId = [];
        $scope.areaCode = [];
    }

    /**
     反选
     */
    $scope.areaVersa = function () {
        var checkbox = $(".areaChildren");
        /*
         遍历checkbox，并反向操作
         */
        for (var i = 0; i < checkbox.length; i++) {
            if (checkbox[i].checked) {

                checkbox[i].checked = false;
                var idx = $scope.areaId.indexOf(Number(checkbox[i].name));
                $scope.areaId.splice(idx, 1);
                $scope.areaCode.splice(idx, 1);

            } else {
                $scope.areaId.push(Number(checkbox[i].name));
                $scope.areaCode.push(checkbox[i].id);
                checkbox[i].checked = true;

            }

        }
    }

});