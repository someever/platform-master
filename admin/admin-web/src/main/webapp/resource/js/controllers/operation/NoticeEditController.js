/**
 * 公告添加controllers文件
 *   author：钟亮
 * @type {module|*}
 */
adminApp.config(function ($routeProvider) {
    $routeProvider.when('/noticeEdit/view', {
        templateUrl: ctx + '/operation/notice/noticeEdit',
        controller: 'noticeEditController'
    }).when('/noticeEdit/view/:id/:judge/:pageNumber', {
        templateUrl: ctx + '/operation/notice/noticeEdit',
        controller: 'noticeEditController'
    });
});


/**
 公告编辑controller
 全局：$scope
 */
adminApp.controller('noticeEditController', function ($scope, gameAreaFnService, $location, uniformFnService, noticeFnService, $routeParams, $rootScope, $translate) {

    //初始化菜单栏
    $rootScope.menuBarData.menuBarThreeName = "NoticeEditManage";
    $rootScope.menuBarData.menuBarTitle = "NoticeEditManage";
    $scope.selectedId = [];//渠道id集合
    $scope.selectedCode = [];//渠道code集合
    $scope.areaLists = [];//区服集合
    $scope.areaId = [];//区服id集合
    $scope.areaCode = [];//区服name集合

    $scope.areaRepetition = [];//重复的区服
    $scope.siteRepetition = [];//重复的渠道

    $scope.messagesData = {};//设置提示框model数据集合
    $scope.channelCode = [];//渠道code集合（显示项）
    $scope.gameAreasData = {};//区服对象集合
    $scope.gameAreasData.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
    $scope.noticeGameList = $scope.gameTransfer;//noticeGameList，与全局game同步
    $scope.uniFormData = {};//下拉框选项集合key对象初始化
    $scope.areaTagList = [];//动态下拉框初始化
    $scope.noticeData = {};//公告对象集合
    $scope.noticeHint = "请输入公告内容...";
    var check = -1;//判断是否成功，url参数 1，是修改成功，0是修改失败，2是添加成功，3是添加失败，-1是失效
    var noticeRenewalGame = -1;//判断游戏是否有重复公告
    var noticeRenewalArea = -1;//判断区服是否有重复公告
    var noticeRenewalSite = -1;//判断渠道是否有重复公告

    if ($routeParams.judge == "update") {
        $scope.accessForbidden = true;
    } else {
        $scope.accessForbidden = false;
    }
    ;

    $scope.clientCount = 0;
    $scope.clientContent = new Object();
    $scope.noticeData.noticeItem = [{noticeLittleType: "", noticeLittleTitle: "", noticeLittleText: ""}];
    // 初始化时由于只有1条回复，所以不允许删除
    $scope.clientContent.canDescReply = false;
    /**
     * 增加物品填写条例
     */
    $scope.clientContent.clientAdd = function ($index) {
        if ($scope.clientCount < 4) {
            $scope.clientCount++;
            $scope.noticeData.noticeItem.splice($index + 1, 0, {
                noticeLittleType: "",
                noticeLittleTitle: "",
                noticeLittleText: ""
            });
            // 增加后允许删除
            $scope.clientContent.canDescReply = true;
        } else {
            $scope.messagesData.messagesTitle = "load.messagesTitleByForm";
            $scope.messagesData.messagesBody = "load.messagesBodyByForm";
            $("#messagesModal").modal('show');
        }

    };

    /**
     * 减少物品填写条例
     */
    $scope.clientContent.clientDelete = function ($index) {
        // 如果回复数大于1，删除被点击回复
        if ($scope.noticeData.noticeItem.length > 1) {
            $scope.noticeData.noticeItem.splice($index, 1);
            $scope.clientCount--;
        }
        // 如果回复数为1，不允许删除
        if ($scope.noticeData.noticeItem.length == 1) {
            $scope.clientContent.canDescReply = false;
        }
    };


    //function findNotice(gameId, areaId, noticeTypes, siteId, noticeId) {
    //    $.ajax({
    //        url: ctx + '/operation/notice/noticeCount',
    //        data: {"gameId": gameId, "areaId": areaId, "noticeTypes": noticeTypes, siteId: siteId, noticeId: noticeId},
    //        success: function (data) {
    //
    //            if (areaId == 0 && siteId == 0) {
    //                if (data > 0) {
    //                    $scope.messagesData.messagesTitle = "load.noticeTipTitle";
    //                    $scope.messagesData.messagesBody = "load.noticeTipGameContent";
    //                    $("#messagesModal").modal('show');
    //                    noticeRenewalGame = 1;
    //                }
    //            } else if (areaId == 0 && siteId != 0) {
    //                if (data > 0) {
    //                    $scope.messagesData.messagesTitle = "load.noticeTipTitle";
    //                    $scope.messagesData.messagesBody = $translate.instant("load.noticeTipSiteNextContentOne") + siteId + $translate.instant("load.noticeTipSiteNextContentTwo");
    //                    $("#messagesModal").modal('show');
    //                    noticeRenewalSite = 1;
    //                    var isNoSite = false;
    //
    //                    for (var j = 0; j < $scope.siteRepetition.length; j++) {
    //                        if (siteId == $scope.siteRepetition[j]) {
    //                            isNoSite = true;
    //                        }
    //                    }
    //                    if (!isNoSite) {
    //                        $scope.siteRepetition.push(siteId);
    //                    }
    //                }
    //            } else {
    //                if (data > 0) {
    //                    $scope.messagesData.messagesTitle = "load.noticeTipTitle";
    //                    $scope.messagesData.messagesBody = $translate.instant("load.noticeTipAreaNextContentOne") + areaId + $translate.instant("load.noticeTipAreaNextContentTwo");
    //                    $("#messagesModal").modal('show');
    //                    noticeRenewalArea = 1;
    //                    var isNo = false;
    //
    //                    for (var i = 0; i < $scope.areaRepetition.length; i++) {
    //                        if (areaId == $scope.areaRepetition[i]) {
    //                            isNo = true;
    //                        }
    //                    }
    //                    if (!isNo) {
    //                        $scope.areaRepetition.push(areaId);
    //                    }
    //
    //
    //                }
    //            }
    //
    //
    //        },
    //        async: false
    //    });
    //}

    /**
     绑定game,site select，change事件
     */
    $scope.resourceGameListChange = function (id) {
        //if ($scope.noticeData.noticeTypes == 3 && $routeParams.judge != "update") {
        //    findNotice(id, 0, $scope.noticeData.noticeTypes, 0, $routeParams.id);
        //} else {
        //    noticeRenewalGame = -1;
        //}
        $scope.areaId = [];//区服id集合
        $scope.areaCode = [];//区服name集合
        $scope.areaData = [];
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

    $scope.noticeTypeChange = function (noticeType) {
        $scope.channelCode = [];//渠道code集合（显示项）
        $scope.selectedId = [];//渠道id集合
        $scope.selectedCode = [];//渠道code集合
        $scope.areaId = [];//区服id集合
        $scope.areaCode = [];//区服name集合
        $scope.areaData = [];
        $scope.deselectAllArea();
        $scope.deselectAll();
        $scope.noticeData.noticeContent = "";
        $scope.clientCount = 0;
        $scope.noticeData.noticeItem = [{noticeLittleType: "", noticeLittleTitle: "", noticeLittleText: ""}];
        // 初始化时由于只有1条回复，所以不允许删除
        $scope.clientContent.canDescReply = false;
        if (noticeType == 1 || noticeType == 4) {
            $(".extra").hide();
            $(".noticeUeditor").hide();
            $(".noticeTextItem").show();
        } else if (noticeType == 2) {
            $(".extra").show();
            $(".noticeUeditor").show();
            $(".noticeTextItem").hide();
            //$scope.noticeData.noticeContentText = undefined;
        } else {
            $(".extra").hide();
            $(".noticeUeditor").show();
            $(".noticeTextItem").hide();
        }

        //if (noticeType == 1 && $routeParams.judge != "update") {
        //    for (var i = 0; i < $scope.areaId.length; i++) {
        //        for (var j = 0; j < $scope.selectedId.length; j++) {
        //            findNotice($scope.gameAreasData.gameId, $scope.areaId[i], noticeType, $scope.selectedId[j], $routeParams.id);
        //        }
        //
        //    }
        //
        //} else {
        //    noticeRenewalArea = -1;
        //}

    };

    /*
     监听addForm表单
     */
    $scope.noticeAddForm = function () {
        //if ($scope.noticeData.noticeContent.indexOf("%") != -1 || $scope.noticeData.noticeTitle.indexOf("%") != -1) {
        //    $scope.messagesData.messagesTitle = "load.noticeTipTitle";
        //    $scope.messagesData.messagesBody = "load.noticePercentTip";
        //    $("#messagesModal").modal('show');
        //} else {
        if (noticeRenewalGame == 1 && $routeParams.judge != "update") {
            $scope.messagesData.messagesTitle = "load.noticeTipTitle";
            $scope.messagesData.messagesBody = "load.noticeTipGameContent";
            $("#messagesModal").modal('show');
        } else {
            var site = "";
            for (var i = 0; i < $scope.selectedId.length; i++) {
                site += $scope.selectedId[i] + ",";
                //if ($scope.noticeData.noticeTypes == 1 && $routeParams.judge != "update" || $scope.noticeData.noticeTypes == 4 && $routeParams.judge != "update") {
                //    findNotice($scope.gameAreasData.gameId, 0, $scope.noticeData.noticeTypes, $scope.selectedId[i], $routeParams.id);
                //}


            }
            var areaids = "";
            for (var i = 0; i < $scope.areaId.length; i++) {
                areaids += $scope.areaId[i] + ",";
                //if ($scope.noticeData.noticeTypes == 1 && $routeParams.judge != "update") {
                //    for (var j = 0; j < $scope.selectedId.length; j++) {
                //        findNotice($scope.gameAreasData.gameId, $scope.areaId[i], $scope.noticeData.noticeTypes, $scope.selectedId[j], $routeParams.id);
                //    }
                //}


            }
            if ($scope.areaRepetition.length == 0) {
                noticeRenewalArea = -1;
            }
            if ($scope.siteRepetition.length == 0) {
                noticeRenewalSite = -1;
            }
            $scope.gameAreasData.siteIds = site;
            $scope.noticeData.gameIds = $scope.gameAreasData.gameId;
            $scope.noticeData.siteIds = site;
            $scope.noticeData.areaIds = areaids;

            $scope.noticeData.createTime = new Date($scope.noticeData.createTime);
            if ($scope.noticeData.beginTime != undefined) {
                $scope.noticeData.beginTime = new Date($scope.noticeData.beginTime);
            }
            if ($scope.noticeData.endTime != undefined) {
                $scope.noticeData.endTime = new Date($scope.noticeData.endTime);
            }
            var percent = true;
            for (var k = 0; k < $scope.noticeData.noticeItem.length; k++) {
                delete  $scope.noticeData.noticeItem[k].$$hashKey;
                //if ($scope.noticeData.noticeItem[k].noticeLittleText.indexOf("%") != -1 || $scope.noticeData.noticeItem[k].noticeLittleTitle.indexOf("%") != -1) {
                //    percent = false;
                //}
            }
            $scope.noticeData.noticeJson = JSON.stringify($scope.noticeData.noticeItem);
            delete $scope.noticeData.noticeType;

            //if (percent) {
            if (noticeRenewalSite == 1) {
                $scope.messagesData.messagesTitle = "load.noticeTipTitle";
                $scope.messagesData.messagesBody = $translate.instant("load.noticeTipSiteNextContentOne") + $scope.siteRepetition + $translate.instant("load.noticeTipSiteNextContentTwo");
                $("#messagesModal").modal('show');
            } else {
                if (noticeRenewalArea == 1) {
                    $scope.messagesData.messagesTitle = "load.noticeTipTitle";
                    $scope.messagesData.messagesBody = $translate.instant("load.noticeTipAreaNextContentOne") + $scope.areaRepetition + $translate.instant("load.noticeTipAreaNextContentTwo");
                    $("#messagesModal").modal('show');
                } else {
                    if ($routeParams.judge == "update") {
                        if ($scope.noticeData.noticeTypes == 2) {
                            $scope.noticeData.noticeContent = $scope.noticeData.noticeContent.replace(/[ ]/g, "");
                            $scope.noticeData.noticeContent = $scope.noticeData.noticeContent.replace(/[\r\n]/g, "");
                        }
                        noticeFnService.noticeUpdate($scope.noticeData).success(function () {
                            check = 1;
                            $location.path("/notice/view/" + check+ "/" + $routeParams.pageNumber);
                            $scope.$apply();
                            $("#messagesModal").modal('show');

                        }).error(function () {
                            check = 0;
                            $location.path("/notice/view/" + check+ "/" + $routeParams.pageNumber);
                            $scope.$apply();
                            $("#messagesModal").modal('show');
                        });
                    } else {
                        delete $scope.noticeData.createTime;
                        if ($scope.noticeData.noticeTypes == 2) {
                            $scope.noticeData.noticeContent = $scope.noticeData.noticeContent.replace(/[ ]/g, "");
                            $scope.noticeData.noticeContent = $scope.noticeData.noticeContent.replace(/[\r\n]/g, "");
                        }
                        noticeFnService.noticeAdd($scope.noticeData).success(function () {
                            check = 2;
                            $location.path("/notice/view/" + check+ "/" + $routeParams.pageNumber);
                            $scope.$apply();
                            $("#messagesModal").modal('show');

                        }).error(function () {
                            check = 3;
                            $location.path("/notice/view/" + check+ "/" + $routeParams.pageNumber);
                            $scope.$apply();
                            $("#messagesModal").modal('show');

                        });
                    }
                }
            }
            //} else {
            //    $scope.messagesData.messagesTitle = "load.noticeTipTitle";
            //    $scope.messagesData.messagesBody = "load.noticePercentTip";
            //    $("#messagesModal").modal('show');
            //}


        }


    };

    /*
     选择渠道（弹框）
     */
    $scope.channelClick = function () {
        //if ($scope.noticeData.noticeTypes == 4) {
        //    $scope.messagesData.messagesTitle = "load.noticeTipTitle";
        //    $scope.messagesData.messagesBody = "load.noticWorldText";
        //    $("#messagesModal").modal('show');
        //} else {
        if ($scope.noticeData.noticeTypes == undefined || $scope.noticeData.noticeTypes == null || $scope.noticeData.noticeTypes == "") {
            $scope.messagesData.messagesTitle = "load.noticeTipTitle";
            $scope.messagesData.messagesBody = "load.noticeTypeTip";
            $("#messagesModal").modal('show');
        } else {
            $("#channelAddModal").modal('show');
        }
        //if ($scope.noticeData.noticeTypes == 1 && $routeParams.judge == "update" || $scope.noticeData.noticeTypes == 4 && $routeParams.judge == "update") {
        //    $scope.messagesData.messagesTitle = "load.noticeTipTitle";
        //    $scope.messagesData.messagesBody = "load.noticeUpdateAccessForbidden";
        //    $("#messagesModal").modal('show');
        //} else {
        //
        //}
        //}

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
        if ($scope.noticeData.noticeTypes == 1 || $scope.noticeData.noticeTypes == 4) {
            if (action == "add") {
                //for (var j = 0; j < $scope.selectedId.length; j++) {
                //    findNotice($scope.gameAreasData.gameId, 0, $scope.noticeData.noticeTypes, $scope.selectedId[j], $routeParams.id);
                //}

            } else {
                if ($scope.siteRepetition.indexOf(id) != -1) {
                    var idx = $scope.siteRepetition.indexOf(id);
                    $scope.siteRepetition.splice(idx, 1);
                }

            }

        }
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
                if ($scope.siteRepetition.indexOf(Number($(checkbox[i]).attr('name'))) != -1) {
                    var ids = $scope.siteRepetition.indexOf(Number($(checkbox[i]).attr('name')));
                    $scope.siteRepetition.splice(ids, 1);
                }
            } else {
                $scope.selectedId.push(Number($(checkbox[i]).attr('name')));
                $scope.selectedCode.push($(checkbox[i]).text());
                $(checkbox[i]).attr("class", "label label-success");
            }

        }
    }
    /*
     选择区服（弹框）
     */
    $scope.areaClick = function () {
        if ($scope.noticeData.noticeTypes == undefined || $scope.noticeData.noticeTypes == null || $scope.noticeData.noticeTypes == "") {
            $scope.messagesData.messagesTitle = "load.noticeTipTitle";
            $scope.messagesData.messagesBody = "load.noticeTypeTip";
            $("#messagesModal").modal('show');
        } else {
            if ($scope.noticeData.noticeTypes == 4) {
                $scope.messagesData.messagesTitle = "load.noticeTipTitle";
                $scope.messagesData.messagesBody = "load.noticWorldText";
                $("#messagesModal").modal('show');
            } else {
                //if ($scope.noticeData.noticeTypes == 1 && $routeParams.judge == "update") {
                //    $scope.messagesData.messagesTitle = "load.noticeTipTitle";
                //    $scope.messagesData.messagesBody = "load.noticeUpdateAccessForbidden";
                //    $("#messagesModal").modal('show');
                //} else {

                $("#areaAddModal").modal('show');
                //}
            }
        }


    };

    /*
     监听areaAdd表单
     */
    $scope.areaAdd = function () {

        //$scope.messagesData.messagesTitle = "load.messagesTitleByUpdate";
        //$scope.messagesData.messagesBody = "load.messagesBodyByAreaToSuccess";
        //$("#messagesModal").modal('show');
        $("#areaAddModal").modal('hide');
        $scope.areaData = $scope.areaCode;

    };

    /*
     区服 checkbox单击操作
     */
    $scope.updateAreaSelection = function ($event, id, code) {

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
        updateAreaSelected(action, id, code);//选中判断逻辑方法

        if ($scope.noticeData.noticeTypes == 1) {
            if (action == "add") {
                //for (var j = 0; j < $scope.selectedId.length; j++) {
                //    findNotice($scope.gameAreasData.gameId, id, $scope.noticeData.noticeTypes, $scope.selectedId[j], $routeParams.id);
                //}

            } else {
                if ($scope.areaRepetition.indexOf(id) != -1) {
                    var idx = $scope.areaRepetition.indexOf(id);
                    $scope.areaRepetition.splice(idx, 1);
                }

            }

        }
    }


    /*
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


    /*
     全选，遍历checkboxModel，正向操作
     */
    $scope.areaAll = function (v) {
        var checkbox = $(".checkList span");
        for (var i = 0; i < checkbox.length; i++) {
            $(checkbox[i]).attr("class", "label label-success");
        }
        var idData = [];
        var codeData = [];
        $scope.areaModel = true;
        for (var i = 0; i < v.length; i++) {
            //if ($scope.noticeData.noticeTypes == 1) {
            //    for (var j = 0; j < $scope.selectedId.length; j++) {
            //        findNotice($scope.gameAreasData.gameId, v[i].id, $scope.noticeData.noticeTypes, $scope.selectedId[j], $routeParams.id);
            //    }
            //}
            idData.push(v[i].id);
            codeData.push(v[i].areaName);
        }
        $scope.areaId = idData;
        $scope.areaCode = codeData;
    };

    /*
     全不选
     */
    $scope.deselectAllArea = function () {
        var checkbox = $(".checkList span");
        for (var i = 0; i < checkbox.length; i++) {
            $(checkbox[i]).attr("class", "label label-default");
        }
        if ($scope.noticeData.noticeTypes == 1) {
            noticeRenewalArea = -1;//判断区服是否有重复公告
        }
        $scope.areaRepetition = [];
        $scope.areaId = [];
        $scope.areaCode = [];
    }

    /*
     反选
     */
    $scope.areaVersa = function () {
        var checkbox = $(".checkList span");
        /*
         遍历checkbox，并反向操作
         */
        for (var i = 0; i < checkbox.length; i++) {
            if ($(checkbox[i]).attr("class") == "label label-success") {
                $(checkbox[i]).attr("class", "label label-default");
                var idx = $scope.areaId.indexOf(Number($(checkbox[i]).attr('name')));
                $scope.areaId.splice(idx, 1);
                $scope.areaCode.splice(idx, 1);
                if ($scope.areaRepetition.indexOf(Number($(checkbox[i]).attr('name'))) != -1) {
                    var ids = $scope.areaRepetition.indexOf(Number($(checkbox[i]).attr('name')));
                    $scope.areaRepetition.splice(ids, 1);
                }
            } else {
                $scope.areaId.push(Number($(checkbox[i]).attr('name')));
                $scope.areaCode.push($(checkbox[i]).text());
                $(checkbox[i]).attr("class", "label label-success");

            }

        }
    }

    /**
     * 如果跳转参数是update，根据id查询Notice对象
     */
    if ($routeParams.judge == "update" || $routeParams.judge == "checkSee") {
        /*
         根据id查询公告
         */
        noticeFnService.noticeGetById($routeParams.id).success(function (data) {
            $scope.noticeGameList = Number(data.gameIds);
            data.noticeType = data.noticeType.toString();
            data.beginTime = $scope.format(data.beginTime, "yyyy-MM-dd HH:mm:ss");
            data.endTime = $scope.format(data.endTime, "yyyy-MM-dd HH:mm:ss");
            $scope.noticeData = data;
            $scope.gameAreasData.gameId = Number(data.gameIds);
            $scope.noticeData.noticeTypes = data.noticeType;

            if ($scope.noticeData.noticeTypes == 1 || $scope.noticeData.noticeTypes == 4) {
                $(".extra").hide();
                $(".noticeUeditor").hide();
                $(".noticeTextItem").show();
            } else if ($scope.noticeData.noticeTypes == 2) {
                $(".extra").show();
                $(".noticeUeditor").show();
                $(".noticeTextItem").hide();
                //$scope.noticeData.noticeContentText = undefined;
            } else {
                $(".extra").hide();
                $(".noticeUeditor").show();
                $(".noticeTextItem").hide();
            }
            //if ($scope.noticeData.noticeTypes == 1) {
            //    $(".extra").hide();
            //    $(".noticeUeditor").hide();
            //    $(".noticeTextItem").show();
            //} else if ($scope.noticeData.noticeTypes == 2) {
            //    $(".extra").show();
            //    $(".noticeUeditor").show();
            //    $(".noticeTextItem").hide();
            //    //$scope.noticeData.noticeContentText = undefined;
            //} else {
            //    $(".extra").hide();
            //    $(".noticeUeditor").show();
            //    $(".noticeTextItem").hide();
            //}

            $scope.noticeData.noticeItem = JSON.parse(data.noticeJson);
            delete $scope.noticeData.noticeJson;
            if ($scope.noticeData.noticeItem != null) {
                $scope.clientCount = $scope.noticeData.noticeItem.length;
                if ($scope.clientCount > 1) {
                    $scope.clientContent.canDescReply = true;
                }
            } else {
                $scope.clientCount = 0;
                $scope.noticeData.noticeItem = [{noticeLittleType: "", noticeLittleTitle: "", noticeLittleText: ""}];
                // 初始化时由于只有1条回复，所以不允许删除
                $scope.clientContent.canDescReply = false;
            }


            //查询区服
            $.ajax({
                url: ctx + '/platform/gameArea/getGameAreasById',
                data: {"gameId": $scope.gameAreasData.gameId},
                success: function (response) {
                    $scope.areaLists = response;
                    $scope.$apply();
                    /*
                     区服 初始化
                     */
                    var areaCheckbox = $(".checkList span");
                    var areaIds = data.areaIds.split(",");
                    for (var i = 0; i < areaIds.length; i++) {
                        for (var j = 0; j < $scope.areaLists.length; j++) {
                            if (areaIds[i] == $scope.areaLists[j].id) {
                                $scope.areaCode.push($scope.areaLists[j].areaName);
                                $scope.areaId.push($scope.areaLists[j].id);
                            }
                        }

                        /*
                         遍历checkbox，并初始化
                         */
                        for (var k = 0; k < areaCheckbox.length; k++) {
                            if ($(areaCheckbox[k]).attr('name') == areaIds[i]) {
                                $(areaCheckbox[k]).attr("class", "label label-success");
                            }
                        }
                    }
                }, async: false
            });


            /*
             渠道初始化
             */
            var checkbox = $(".siteCheckList span");
            var siteIds = data.siteIds.split(",");
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
            $scope.areaData = $scope.areaCode;
            $scope.channelCode = $scope.selectedCode;
            $scope.$apply();
        });

    }


});