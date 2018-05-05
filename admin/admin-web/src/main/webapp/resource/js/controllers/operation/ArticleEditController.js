/**
 * 物品申请添加controllers文件
 *   author：钟亮
 * @type {module|*}
 */
adminApp.config(function ($routeProvider) {
    $routeProvider.when('/articleEdit/view', {
        templateUrl: ctx + '/operation/article/articleEdit',
        controller: 'articleEditController'
    }).when("/articleEdit/view/:id/:judge/:pageNumber", {
        templateUrl: ctx + '/operation/article/articleEdit',
        controller: 'articleEditController'
    });
    ;
});


/**
 物品申请编辑controller
 全局：$scope
 */
adminApp.controller('articleEditController', function ($scope, articleService, uniformFnService, customFormService, gameToyTypeFnService, gameAreaFnService, $routeParams, $location, $rootScope) {
    $scope.articleEditData = {};//物品申请编辑对象
    $scope.articleEditData.item = {};//物品对象
    $scope.uniFormData = {};//下拉框选项集合key对象初始化
    $scope.uniFormData.key = "operation.item.itemType";//初始化动态下拉框的key
    $scope.messagesData = {};//设置提示框model数据集合
    $scope.articleEditData.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
    $scope.gameToyTypeSearch = {};//初始化查询条件对象
    $scope.gameToyTypeSearch.gameId = $scope.gameTransfer;//初始化查询gameId
    $scope.gameToyTypeSearch.itemTypeName = "";
    $scope.gameToyTypeSearch.itemCode = "";
    $scope.choiceCheck = 0;//判断第几个自定义表单
    $scope.choiceItemType = "";//选中的物品类型
    $scope.choiceGameId = $scope.gameTransfer;
    $scope.placeholderItemName = "load.placeholderItemName";//道具名称搜索国际化
    $scope.placeholderItemCode = "load.placeholderItemCode";//道具code搜索国际化
    var check = -1;//判断是否成功，url参数 1，是修改成功，0是修改失败，2是添加成功，3是添加失败，-1是失效
    //初始化菜单栏
    $rootScope.menuBarData.menuBarThreeName = "load.ArticleViewTitle";
    $rootScope.menuBarData.menuBarTitle = "load.ArticleViewTitle";

    $scope.areaLists = [];//区服集合
    $scope.areaId = [];//区服id集合
    $scope.areaCode = [];//区服name集合
    /**
     *加载下拉框
     */
    uniformFnService.uniGetKey($scope.uniFormData).success(function (response) {
        $scope.itemTypeList = JSON.parse(response.itemJson);
        /*
         国际化
         */
        for (var i = 0; i < $scope.itemTypeList.length; i++) {
            $scope.itemTypeList[i].key = "load.itemType" + $scope.itemTypeList[i].key;
        }
    });

    /*$scope.KEY="formobject.list."+$scope.articleEditData.gameId+"."+$scope.articleEditData.itemType;//配置自定义表单的key
     /!*
     查询自定义表单
     *!/
     customFormService.customFormByKey({key:$scope.KEY}).success(function(response){
     $scope.analogData=response.formObjects;
     $scope.$apply();
     });*/

    $scope.areaSearch = {};//区服查询条件
    $scope.areaSearch.gameId = $scope.gameTransfer;
    $scope.areaSearch.siteType = $scope.channelType;

    /**
     *根据全局game，site查询区服 公共方法
     */
    function gameAreaFindByGameIdSelect(areaSearch) {
        gameAreaFnService.getGameAreasBySiteType(areaSearch).success(function (response) {
            $scope.areaLists = response;
            $scope.$apply();
        });
    }

    /**
     *根据全局game，site查询区服
     */
    gameAreaFindByGameIdSelect($scope.areaSearch);

    /**
     * 渠道类型改变
     */
    $scope.channelTypeChange = function () {
        $scope.areaSearch.siteType = $scope.channelType;
        /**
         *根据全局game，site查询区服
         */
        gameAreaFindByGameIdSelect($scope.areaSearch);
    };

    $scope.sendByTypeChange = function (data) {
        if (data == 1) {
            $(".sendByType").show();
            $(".sendByValue").show();
        } else {
            $(".sendByType").hide();
            $(".sendByValue").hide();
        }
    };

    /*
     选择区服（弹框）
     */
    $scope.areaClick = function () {
        if ($scope.articleEditData.gameId == "" || $scope.articleEditData.gameId == null || $scope.articleEditData.gameId == undefined) {
            $scope.messagesData.messagesTitle = "load.menuTipTitle";
            $scope.messagesData.messagesBody = "load.gameActivityConSiteTypeExplain";
            $("#messagesModal").modal('show');
        } else {
            $("#areaAddModal").modal('show');
        }
    }

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
        var checkbox = $(".checkList span");
        for (var i = 0; i < checkbox.length; i++) {
            $(checkbox[i]).attr("class", "label label-success");
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
        var checkbox = $(".checkList span");
        for (var i = 0; i < checkbox.length; i++) {
            $(checkbox[i]).attr("class", "label label-default");
        }
        $scope.areaId = [];
        $scope.areaCode = [];
    }

    /**
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

            } else {
                $scope.areaId.push(Number($(checkbox[i]).attr('name')));
                $scope.areaCode.push($(checkbox[i]).text());
                $(checkbox[i]).attr("class", "label label-success");

            }

        }
    };


    /**
     *gameId改变时，查询自定义表单
     */
    $scope.gameIdChange = function (gameId) {
        $scope.areaSearch.gameId = gameId;
        $scope.areaSearch.siteType = $scope.channelType;
        //
        $scope.clientCount = 0;
        $scope.articleEditData.item = [{itemType: "", itemId: "", itemName: "", value: "", extraData: []}];//清空物品选项
        // 初始化时由于只有1条回复，所以不允许删除
        $scope.clientContent.canDescReply = false;

        //
        /*
         根据全局game，site查询区服
         */
        gameAreaFindByGameIdSelect($scope.areaSearch);

        $scope.choiceGameId = gameId;
        if ($scope.articleEditData.gameId != undefined && $scope.articleEditData.itemType != undefined) {
            $scope.KEY = "formobject.list." + gameId + "." + $scope.articleEditData.itemType;//配置自定义表单的key
            /*
             查询自定义表单
             */
            customFormService.customFormByKey({key: $scope.KEY}).success(function (response) {
                $scope.extraData = JSON.parse(response.formJson);
                $scope.$apply();
            });
        } else {
            $scope.extraData = [];
        }

        $scope.gameToyTypeSearch.gameId = gameId;//初始化查询gameId
    };

    /**
     *刷新查询页面，调用查询方法 公共方法
     */
    function itemFindByItemTypePublic(gameToyTypeSearch, pageIndex, pageSize) {
        gameToyTypeFnService.itemFindByItemType(gameToyTypeSearch, pageIndex, pageSize).success(function (response) {
            $scope.gameTypeToys = response.rows;

            //如果物品item当页长度大于10并且item总条数>=10
            if ($scope.gameTypeToys.length > 10 && response.page.totalCount >= 10) {
                //如果当页长度=11，添加用户习惯标记，以此类推
                if ($scope.gameTypeToys.length == 11) {
                    $scope.gameTypeToys[0].lately = 1;
                }
                if ($scope.gameTypeToys.length == 12) {
                    $scope.gameTypeToys[0].lately = 1;
                    $scope.gameTypeToys[1].lately = 1;
                }
                if ($scope.gameTypeToys.length == 13) {
                    $scope.gameTypeToys[0].lately = 1;
                    $scope.gameTypeToys[1].lately = 1;
                    $scope.gameTypeToys[2].lately = 1;
                }
            } else {
                //如果物品item当页长度小于10并且item总条数<10
                if ($scope.gameTypeToys.length - response.page.totalCount == 1) {
                    $scope.gameTypeToys[0].lately = 1;
                }
                if ($scope.gameTypeToys.length - response.page.totalCount == 2) {
                    $scope.gameTypeToys[0].lately = 1;
                    $scope.gameTypeToys[1].lately = 1;
                }
                if ($scope.gameTypeToys.length - response.page.totalCount == 3) {
                    $scope.gameTypeToys[0].lately = 1;
                    $scope.gameTypeToys[1].lately = 1;
                    $scope.gameTypeToys[2].lately = 1;
                }

            }

            $scope.pageCount = response.page.pageCount;
            $scope.$apply();
        });
    };

    /**
     *分页
     */
    $scope.onPageChange = function () {
        /*
         刷新查询页面，调用查询方法
         */
        itemFindByItemTypePublic($scope.gameToyTypeSearch, $scope.currentPage, 10);
    };

    /**
     * 物品类型改变时，查询自定义表单
     * @param itemType
     * @param index
     */
    $scope.itemTypeChange = function (itemType, index) {
        $scope.choiceItemType = itemType;
        $scope.articleEditData.item[index].itemId = "";
        $scope.articleEditData.item[index].itemName = "";
        if ($scope.articleEditData.gameId != undefined && itemType != undefined && $scope.articleEditData.gameId != "") {
            $scope.KEY = "formobject.list." + $scope.articleEditData.gameId + "." + itemType;//配置自定义表单的key
            /*
             查询自定义表单
             */
            customFormService.customFormByKey({key: $scope.KEY}).success(function (response) {
                $scope.articleEditData.item[index].extraData = JSON.parse(response.formJson);
                $scope.$apply();
            });

        } else {
            $scope.articleEditData.item[index].extraData = [];
        }
    };

    /**
     *查询表单监听
     */
    $scope.searchButtonClicked = function () {
        /*
         刷新查询页面，调用查询方法
         */
        itemFindByItemTypePublic($scope.gameToyTypeSearch, 1, 10);
    };


    /**
     物品选择（弹框）
     */
    $scope.itemSelected = function (itemType, index) {
        console.log(itemType + ":" + $scope.choiceGameId);
        if (itemType != "" && itemType != undefined && $scope.choiceGameId != "" && $scope.choiceGameId != undefined) {
            $scope.choiceCheck = index;
            $scope.gameToyTypeSearch.itemType = itemType;
            $scope.gameToyTypeSearch.gameId = $scope.choiceGameId;
            /*
             刷新查询页面，调用查询方法
             */
            itemFindByItemTypePublic($scope.gameToyTypeSearch, 1, 10);

            $("#itemModal").modal("show");
        } else {
            $scope.messagesData.messagesTitle = "load.promptTitle";
            $scope.messagesData.messagesBody = "load.itemSelectMessagesBody";
            $("#messagesModal").modal('show');
        }

    };

    /**
     *物品选择（勾选）
     */
    $scope.itemItemChoice = function () {
        var data = $(this);
        /*
         保存用户习惯
         */
        gameToyTypeFnService.itemHabitEdit(data[0].gameToyType.id, data[0].gameToyType.itemType, $scope.gameToyTypeSearch.gameId);
        $scope.articleEditData.item[$scope.choiceCheck].itemId = data[0].gameToyType.itemCode;
        $scope.articleEditData.item[$scope.choiceCheck].itemName = data[0].gameToyType.itemName;
        $("#itemModal").modal("hide");
    };


    $scope.clientCount = 0;
    $scope.clientContent = new Object();
    $scope.articleEditData.item = [{itemType: "", itemId: "", itemName: "", value: "", extraData: []}];
    // 初始化时由于只有1条回复，所以不允许删除
    $scope.clientContent.canDescReply = false;
    /**
     * 增加物品填写条例
     */
    $scope.clientContent.clientAdd = function ($index) {
        if ($scope.clientCount < 4) {
            $scope.clientCount++;
            $scope.articleEditData.item.splice($index + 1, 0, {
                itemType: "",
                itemId: "",
                itemName: "",
                value: "",
                extraData: []
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
        if ($scope.articleEditData.item.length > 1) {
            $scope.articleEditData.item.splice($index, 1);
            $scope.clientCount--;
        }
        // 如果回复数为1，不允许删除
        if ($scope.articleEditData.item.length == 1) {
            $scope.clientContent.canDescReply = false;
        }
    };

    /**
     * 监听Add提交表单
     */
    $scope.articleAddForm = function () {
        if ($scope.articleEditData.sendByType != null && $scope.articleEditData.sendByType != undefined && $scope.articleEditData.sendByType != "" && $scope.articleEditData.sendByValue != "" && $scope.articleEditData.sendByValue != null && $scope.articleEditData.sendByValue != undefined || $scope.articleEditData.sendType == 2) {
            var judge = true;
            for (var i = 0; i < $scope.articleEditData.item.length; i++) {
                delete $scope.articleEditData.item[i].$$hashKey;
                if ($scope.articleEditData.item[i].itemId == "") {
                    judge = false;
                }
                if ($scope.articleEditData.item[i].extraData != undefined) {
                    for (var j = 0; j < $scope.articleEditData.item[i].extraData.length; j++) {
                        delete $scope.articleEditData.item[i].extraData[j].$$hashKey;
                        if ($scope.articleEditData.item[i].extraData[j].formRequired == "required") {
                            if ($scope.articleEditData.item[i].extraData[j].formValue == null || $scope.articleEditData.item[i].extraData[j].formValue == "") {
                                judge = false;
                            }
                        }
                    }
                }
                $scope.articleEditData.item[i].itemType = {key: "", value: $scope.articleEditData.item[i].itemType};
            }
            var areaids = "";
            for (var k = 0; k < $scope.areaId.length; k++) {
                areaids += $scope.areaId[k] + ",";
            }
            //if (judge) {
            if ($scope.articleEditData.sendType == 2) {
                $scope.articleEditData.areaIds = areaids;
            }
            $scope.articleEditData.itemJson = JSON.stringify($scope.articleEditData.item);
            $scope.articleEditData.channelType = $scope.channelType;
            if ($routeParams.judge == "update") {

                delete $scope.articleEditData.goodsItemList;
                $scope.articleEditData.createTime = new Date($scope.articleEditData.createTime);
                articleService.articleUpdate($scope.articleEditData).success(function () {
                    check = 1;
                    $location.path("/article/view/" + check+ "/" + $routeParams.pageNumber);
                    $scope.$apply();
                    $("#messagesModal").modal('show');

                }).error(function () {
                    check = 0;
                    $location.path("/article/view/" + check+ "/" + $routeParams.pageNumber);
                    $scope.$apply();
                    $("#messagesModal").modal('show');
                });
            } else {
                articleService.articleAdd($scope.articleEditData).success(function () {
                    check = 2;
                    $location.path("/article/view/" + check+ "/" + $routeParams.pageNumber);
                    $scope.$apply();
                    $("#messagesModal").modal('show');

                }).error(function () {
                    check = 3;
                    $location.path("/article/view/" + check+ "/" + $routeParams.pageNumber);
                    $scope.$apply();
                    $("#messagesModal").modal('show');

                });
            }
            //} else {
            //    $scope.messagesData.messagesTitle = "load.promptTitle";
            //    $scope.messagesData.messagesBody = "load.customFormMessagesBody";
            //    $("#messagesModal").modal('show');
            //}

        } else {
            $scope.messagesData.messagesTitle = "load.promptTitle";
            $scope.messagesData.messagesBody = "load.articleMessagesBody";
            $("#messagesModal").modal('show');
        }


    }


    /**
     * 如果跳转参数是update，根据id查询article对象
     */
    if ($routeParams.judge == "update" || $routeParams.judge == "checkSee") {
        articleService.articleGetById($routeParams.id).success(function (data) {
            $scope.areaSearch.gameId = data.gameId;
            ///*
            // 根据全局game，site查询区服
            // */
            //gameAreaFnService.gameAreaFindByGameId($scope.areaSearch).success(function (response) {
            //    $scope.areaLists = response;
            //    $scope.$apply();
            //});
            $scope.articleEditData = data;
            $scope.choiceGameId = data.gameId;
            $scope.gameAreasData = {};
            $scope.areaSearch = {};
            $scope.gameAreasData.gameId = data.gameId;
            $scope.articleEditData.sendType = data.sendType.toString();
            if (data.channelType == -1) {
                $scope.channelType = null;
            } else {
                $scope.channelType = data.channelType.toString();
            }


            $scope.areaSearch.gameId = data.gameId;
            $scope.areaSearch.siteType = $scope.channelType;
            gameAreaFnService.getGameAreasBySiteType($scope.areaSearch).success(function (response) {
                $scope.areaLists = response;
                $scope.$apply();
                if ($scope.articleEditData.sendType == 1) {
                    $scope.articleEditData.areaIds = Number(data.areaIds);
                    $scope.$apply();
                } else {

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
                    $scope.areaData = $scope.areaCode;
                }


            });


            $scope.articleEditData.sendByType = data.sendByType.toString();
            $scope.articleEditData.mailType = data.mailType.toString();
            $scope.articleEditData.item = JSON.parse(data.itemJson);
            $scope.clientCount = $scope.articleEditData.item.length;
            if ($scope.clientCount > 1) {
                $scope.clientContent.canDescReply = true;
            }
            for (var i = 0; i < $scope.articleEditData.item.length; i++) {
                //$scope.articleEditData.item[i].itemType = $scope.articleEditData.item[i].itemType.toString();
                $scope.articleEditData.item[i].value = Number($scope.articleEditData.item[i].value);
                $scope.articleEditData.item[i].itemType = $scope.articleEditData.item[i].itemType.value;
            }
            $scope.$apply();
        });
        if ($routeParams.judge == "checkSee") {
            $(".articleAddSubmit").hide();

        }
    }


});