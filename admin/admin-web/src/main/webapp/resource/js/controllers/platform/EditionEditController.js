/**
 * 版本补丁编辑controllers文件
 *   author：钟亮
 * @type {module|*}
 */
adminApp.config(function ($routeProvider) {
    $routeProvider.when('/editionEdit/view', {
        templateUrl: ctx + '/platform/edition/editionEditInit',
        controller: 'editionEditController'
    }).when("/editionEdit/view/:id/:judge/:versionType/:pageNumber", {
        templateUrl: ctx + '/platform/edition/editionEditInit',
        controller: 'editionEditController'
    });

});


/**
 版本补丁编辑controller
 全局：$scope
 */
adminApp.controller('editionEditController', function ($scope, editionFnService, $location, uniformFnService, $routeParams, $rootScope) {
    //初始化菜单栏
    if ($routeParams.versionType == 1) {//1代表整包更新，2代表补丁更新
        $rootScope.menuBarData.menuBarThreeName = "load.versionEditTotalManage";
        $rootScope.menuBarData.menuBarTitle = "load.versionEditTotalManage";
    } else {
        $rootScope.menuBarData.menuBarThreeName = "load.versionEditBatchManage";
        $rootScope.menuBarData.menuBarTitle = "load.versionEditBatchManage";
    }

    $scope.typeShow = $routeParams.versionType;
    $scope.messagesData = {};//设置提示框model数据集合

    $scope.editionData = {};//版本补丁数据源
    $scope.editionData.gameId = $scope.gameTransfer;
    $scope.editionData.whiteListCheck = '2';
    $scope.selectedId = [];//渠道id集合
    $scope.selectedCode = [];//渠道code集合
    var check = -1;//判断是否成功，url参数 1，是修改成功，0是修改失败，2是添加成功，3是添加失败，-1是失效\


    $scope.editionEditForm = function () {
        var patchUrlData = $scope.editionData.patchUrl.split('/');
        console.log(patchUrlData);
        $scope.editionData.versionType = $routeParams.versionType;
        if ($routeParams.versionType == 2 && patchUrlData[patchUrlData.length - 1] != $scope.editionData.patchName) {//判断文件名称与文件URL是否一致
            $scope.messagesData.messagesTitle = "load.versionFileExplainTitle";
            $scope.messagesData.messagesBody = "load.versionFileExplainURLText";
            $("#messagesModal").modal('show');
        } else {
            var patchVersionData = patchUrlData[patchUrlData.length - 1].split('_');
            if ($routeParams.versionType == 2 && $scope.editionData.patchVersion != patchVersionData[1]) {//判断文件URL是否与版本编号一致
                $scope.messagesData.messagesTitle = "load.versionFileExplainTitle";
                $scope.messagesData.messagesBody = "load.versionFileExplainVersionText";
                $("#messagesModal").modal('show');
            } else {
                var site = "";
                for (var i = 0; i < $scope.selectedId.length; i++) {
                    site += $scope.selectedId[i] + ",";
                }
                if ($routeParams.versionType == 1) {
                    delete $scope.editionData.blockingSite;
                } else {
                    $scope.editionData.blockingSite = site;
                }
                if ($routeParams.judge == "update") {
                    editionFnService.editionUpdate($scope.editionData).success(function (data) {
                        check = 1;
                        if (data != null && data != "") {
                            check = data;
                        }
                        if ($routeParams.versionType == 1) {
                            $location.path("/editionTotal/view/" + check + "/" + $routeParams.pageNumber);
                        } else {
                            $location.path("/edition/view/" + check + "/" + $routeParams.pageNumber);
                        }

                        $scope.$apply();
                        $("#messagesModal").modal('show');
                    }).error(function () {
                        check = 0;
                        if ($routeParams.versionType == 1) {
                            $location.path("/editionTotal/view/" + check + "/" + $routeParams.pageNumber);
                        } else {
                            $location.path("/edition/view/" + check + "/" + $routeParams.pageNumber);
                        }
                        $scope.$apply();
                        $("#messagesModal").modal('show');

                    });
                } else {

                    editionFnService.editionAdd($scope.editionData).success(function (data) {
                        check = 2;
                        if (data != null && data != "") {
                            check = data;
                        }
                        if ($routeParams.versionType == 1) {
                            $location.path("/editionTotal/view/" + check + "/" + $routeParams.pageNumber);
                        } else {
                            $location.path("/edition/view/" + check + "/" + $routeParams.pageNumber);
                        }
                        $scope.$apply();
                        $("#messagesModal").modal('show');
                    }).error(function () {
                        check = 3;
                        if ($routeParams.versionType == 1) {
                            $location.path("/editionTotal/view/" + check + "/" + $routeParams.pageNumber);
                        } else {
                            $location.path("/edition/view/" + check + "/" + $routeParams.pageNumber);
                        }
                        $scope.$apply();
                        $("#messagesModal").modal('show');

                    });

                }
            }

        }
    };

    /**
     * 如果跳转参数是update，根据id查询edition对象
     */
    if ($routeParams.judge == "update") {
        /*
         根据id查询公告
         */
        editionFnService.editionSelectById($routeParams.id).success(function (data) {
            $scope.editionData = data;
            $scope.editionData.validStatusView = data.validStatus.toString();
            $scope.editionData.deviceTypeView = data.deviceType.toString();
            $scope.editionData.whiteListCheck = data.whiteStatus.toString();
            $scope.editionData.whiteListText = data.whiteContent;
            delete $scope.editionData.validStatus;
            delete $scope.editionData.deviceType;
            delete $scope.editionData.whiteStatus;
            delete $scope.editionData.whiteListOp;
            /*
             渠道初始化
             */
            var checkbox = $(".siteCheckList span");
            var siteIds = data.blockingSite.split(",");
            for (var i = 0; i < siteIds.length; i++) {
                for (var j = 0; j < $scope.siteList.length; j++) {
                    if (siteIds[i] == $scope.siteList[j].id) {
                        $scope.selectedCode.push($scope.siteList[j].code);
                        $scope.selectedId.push($scope.siteList[j].id);
                    }

                }
                /*
                 遍历checkbox，并初始化
                 */
                for (var k = 0; k < checkbox.length; k++) {
                    if ($(checkbox[k]).attr('name') == siteIds[i]) {
                        $(checkbox[k]).attr("class", "label label-success");
                    }
                }
            }
            $scope.channelCode = $scope.selectedCode;
            $scope.$apply();
        });

    }

    /*
     选择渠道（弹框）
     */
    $scope.channelClick = function () {
        $("#channelAddModal").modal('show');
    };

    /*
     监听addForm表单
     */
    $scope.channelAdd = function () {
        console.log($scope.selectedId);
        console.log($scope.selectedCode);
        //$scope.messagesData.messagesTitle = "load.messagesTitleByUpdate";
        //$scope.messagesData.messagesBody = "load.messagesBodyByChannelToSuccess";
        //$("#messagesModal").modal('show');
        $("#channelAddModal").modal('hide');
        $scope.channelCode = $scope.selectedCode;

    };

    /*
     选中判断逻辑方法
     */
    var updateSelected = function (action, id, code) {
        /*
         true添加集合元素
         */
        if (action == 'add' && $scope.selectedId.indexOf(id) == -1) {
            $scope.selectedId.push(id);
            $scope.selectedCode.push(code);
        }
        /*
         false移除集合元素
         */
        if (action == 'remove' && $scope.selectedId.indexOf(id) != -1) {
            var idx = $scope.selectedId.indexOf(id);
            $scope.selectedId.splice(idx, 1);
            $scope.selectedCode.splice(idx, 1);
        }
    }

    /*
     checkbox单击操作
     */
    $scope.updateSelection = function ($event, id, code) {
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
        updateSelected(action, id, code);//选中判断逻辑方法

    }

    /*
     全选，遍历checkboxModel，正向操作
     */
    $scope.all = function (v) {
        var checkbox = $(".siteCheckList span");
        for (var i = 0; i < checkbox.length; i++) {
            $(checkbox[i]).attr("class", "label label-success");
        }
        var idData = [];
        var codeData = [];
        $scope.siteModel = true;
        for (var i = 0; i < v.length; i++) {
            idData.push(v[i].id);
            codeData.push(v[i].code);
        }
        $scope.selectedId = idData;
        $scope.selectedCode = codeData;
    };

    /*
     全不选
     */
    $scope.deselectAll = function () {
        var checkbox = $(".siteCheckList span");
        for (var i = 0; i < checkbox.length; i++) {
            $(checkbox[i]).attr("class", "label label-default");
        }
        $scope.siteRepetition = [];
        $scope.selectedId = [];
        $scope.selectedCode = [];
        $scope.master = false;
    }

    /*
     反选
     */
    $scope.versa = function (c) {
        var checkbox = $(".siteCheckList span");
        /*
         遍历checkbox，并反向操作
         */
        for (var i = 0; i < checkbox.length; i++) {
            if ($(checkbox[i]).attr("class") == "label label-success") {
                $(checkbox[i]).attr("class", "label label-default");
                var idx = $scope.selectedId.indexOf(Number($(checkbox[i]).attr('name')));
                $scope.selectedId.splice(idx, 1);
                $scope.selectedCode.splice(idx, 1);
            } else {
                $scope.selectedId.push(Number($(checkbox[i]).attr('name')));
                $scope.selectedCode.push($(checkbox[i]).text());
                $(checkbox[i]).attr("class", "label label-success");
            }

        }
    }

})
;