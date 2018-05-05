/**
 * 商品配方添加controllers文件
 *   author：钟亮
 * @type {module|*}
 */
adminApp.config(function ($routeProvider) {
    $routeProvider.when('/billingGoodsEdit/view', {
        templateUrl: ctx + '/platform/billingGoods/billingGoodsEditInit',
        controller: 'billingGoodsEditController'
    }).when('/billingGoodsEdit/view/:id/:judge/:pageNumber', {
        templateUrl: ctx + '/platform/billingGoods/billingGoodsEditInit',
        controller: 'billingGoodsEditController'
    });
});


/**
 商品配方controller
 全局：$scope
 Service:billingGoodsFnService
 */
adminApp.controller('billingGoodsEditController', function ($scope, billingGoodsFnService, gameAreaFnService, $location, $routeParams, gameToyTypeFnService, uniformFnService, $rootScope, customFormService) {

    //初始化菜单栏
    $rootScope.menuBarData.menuBarThreeName = "ProductFormulaEdit";
    $rootScope.menuBarData.menuBarTitle = "ProductFormulaEdit";
    $scope.selectedId = [];//渠道id集合
    $scope.selectedCode = [];//渠道code集合
    $scope.messagesData = {};//设置提示框model数据集合
    $scope.channelCode = [];//渠道code集合（显示项）
    $scope.areaLists = [];//区服集合
    $scope.areaId = [];//区服id集合
    $scope.areaCode = [];//区服name集合
    $scope.gameToySiteList = $scope.siteTransfer;//初始化gameToySiteList，与全局site同步
    $scope.gameToyGameList = $scope.gameTransfer;//gameToyGameList，与全局game同步
    $scope.gameAreasData = [];//区服查询条件集合
    $scope.gameAreasData.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
    //$scope.awardPackage = [];//奖励包集合
    //$scope.firstAwardPackage = [];//首充奖励包集合
    //$scope.awardCondition = {};//奖励包查询条件对象
    //$scope.awardCondition.awardName = "";//初始化条件
    //$scope.awardCondition.awardType = -1;//初始化条件
    //$scope.awardId = "";//奖励包id字符串
    //$scope.awardNames = [];//奖励包显示集合
    //$scope.firstAwardId = "";//首充奖励包id字符串
    //$scope.firstAwardNames = [];//首充奖励包显示集合
    //$scope.firstBuyNames = [];//首充显示集合
    //$scope.goodsTypeDatas = [{id: 1, code: "load.goodsGem"}, {id: 2, code: "load.goodsProp"}];
    if ($scope.gameToyGameList == 27) {//十冷
        $scope.storeTypeDatas = [{id: 1, code: "load.normalPrepaidPay"}, {id: 0, code: "load.gameActivitiePay"}, {
            id: 3,
            code: "load.bagStore"
        }];
    } else {//其他游戏
        $scope.storeTypeDatas = [{id: 0, code: "load.gemStore"}, {id: 1, code: "load.RMBStore"}];
    }
    $scope.uniFormData = {};//下拉框选项集合key对象初始化
    $scope.uniFormData.key = "operation.item.itemType";//初始化动态下拉框的key
    $scope.flag = -1;//保存宝石类型id
    $scope.flagGoods = -1;//保存道具集合下标
    $scope.flagGem = -1;//保存宝石集合下标
    $scope.shiLengKeyData = [{"key": "load.itemTypegem", "value": "2"}];
    $scope.otherKeyData = [];


    /**
     加载下拉框
     */
    uniformFnService.uniGetKey($scope.uniFormData).success(function (response) {

        $scope.itemTypeList = JSON.parse(response.itemJson);
        /*
         国际化
         */
        for (var i = 0; i < $scope.itemTypeList.length; i++) {
            $scope.itemTypeList[i].key = "load.itemType" + $scope.itemTypeList[i].key;
        }
        response = JSON.parse(response.itemJson);
        for (var i = 0; i < response.length; i++) {
            response[i].key = "load.itemType" + response[i].key;
            if (response[i].key == "load.itemTypegem") {
                $scope.flag = response[i].value;
            }
            ;
            if (response[i].key == "load.itemTypeitem") {
                $scope.flagGoods = i;
            }
            ;
        }
        response.splice($scope.flagGoods, 1);
        $scope.otherKeyData = response;
        if ($scope.gameToyGameList == 27) {//十冷
            $scope.goodsTypeDatas = $scope.shiLengKeyData;
        } else {//其他游戏
            $scope.goodsTypeDatas = response;
        }

    });

    $scope.gameToyTypeSearch = {};//初始化查询条件对象
    $scope.gameToyTypeSearch.gameId = $scope.gameTransfer;//初始化查询gameId
    $scope.gameToyTypeSearch.itemTypeName = "";
    $scope.gameToyTypeSearch.itemCode = "";
    $scope.placeholderItemName = "load.placeholderItemName";//道具名称搜索国际化
    $scope.placeholderItemCode = "load.placeholderItemCode";//道具code搜索国际化

    var check = -1;//判断是否成功，url参数 1，是修改成功，0是修改失败，2是添加成功，3是添加失败，-1是失效
    $scope.goodsData = {};//商品集合;
    $scope.goodsData.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
    $scope.goodsData.siteId = $scope.siteTransfer;//初始化gameId，与全局game同步

    $scope.billingGoodsSearch = {};//商品查询条件
    $scope.billingGoodsSearch.gameId = $scope.gameTransfer;
    $scope.billingGoodsSearch.siteId = $scope.siteTransfer;

    $scope.goodsData.firstBuyPolicy = {};//首充策略集合
    $scope.goodsData.firstBuyPolicy.operateCount = 2;//默认倍率
    $scope.goodsData.itemList = {};//商品物品对象
    $scope.goodsData.itemList.item = [];//商品物品集合
    $scope.clientCount = 0;
    $scope.clientContent = new Object();
    $scope.goodsData.itemList.item = [{itemType: "", itemId: "", itemName: "", value: "", extraData: []}];
    // 初始化时由于只有1条回复，所以不允许删除
    $scope.clientContent.canDescReply = false;
    $scope.firstDataChoice = false;//false默认商品选择，true为物品选择


    $scope.giftBagDatas = [];//显示集合
    $scope.giftBagId = [];//礼包id集合
    $scope.giftBagCode = [];//礼包name集合
    $scope.giftBagLists = [];//礼包集合
    /**
     * 增加物品填写条例
     */
    $scope.clientContent.clientAdd = function ($index) {
        if ($scope.clientCount < 4) {
            $scope.clientCount++;
            $scope.goodsData.itemList.item.splice($index + 1, 0, {
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
        if ($scope.goodsData.itemList.item.length > 1) {
            $scope.goodsData.itemList.item.splice($index, 1);
            $scope.clientCount--;
        }
        // 如果回复数为1，不允许删除
        if ($scope.goodsData.itemList.item.length == 1) {
            $scope.clientContent.canDescReply = false;
        }
    };

    /**
     物品选择（弹框）
     */
    $scope.firstItemSelected = function (itemType, index) {
        $scope.firstDataChoice = true;
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
    $scope.itemItemChoice = function (data) {

        if ($scope.firstDataChoice) {
            var datas = $(this);
            /*
             保存用户习惯
             */
            gameToyTypeFnService.itemHabitEdit(datas[0].gameToyType.id, datas[0].gameToyType.itemType, $scope.gameToyTypeSearch.gameId);
            $scope.goodsData.itemList.item[$scope.choiceCheck].itemId = datas[0].gameToyType.itemCode;
            $scope.goodsData.itemList.item[$scope.choiceCheck].itemName = datas[0].gameToyType.itemName;
            $("#itemModal").modal("hide");
        } else {
            gameToyTypeFnService.itemHabitEdit(data.id, data.itemType, $scope.gameToyTypeSearch.gameId);
            $scope.goodsData.goodsName = data.itemName;
            $scope.goodsData.goodsCode = data.itemCode;
            $scope.goodsData.goodsDesc = data.itemExtend;
            $("#itemModal").modal("hide");
        }


    };


    /*
     选择礼包（弹框）
     */
    $scope.bagClick = function () {
        $("#bagAddModal").modal('show');
    }
    $scope.bagListCondition = {};
    $scope.bagListCondition.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步

    if ($scope.bagListCondition.gameId == "") {
        $scope.bagListCondition.gameId = -1;
    }
    //查询礼包
    $.ajax({
        url: ctx + "/operation/bagCode/getList",
        data: {"gameId": $scope.bagListCondition.gameId},
        success: function (response) {
            $scope.giftBagLists = response;

        }, async: false
    });

    /*
     监听bagAdd表单
     */
    $scope.bagAdd = function () {
        $("#bagAddModal").modal('hide');
        $scope.giftBagDatas = $scope.giftBagCode;

    };

    /*
     checkbox单击操作
     */
    $scope.updateBagSelection = function ($event, id, code) {
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
        updateBagSelected(action, id, code);//选中判断逻辑方法
    }


    /*
     选中判断逻辑方法
     */
    var updateBagSelected = function (action, id, code) {
        /*
         true添加集合元素
         */
        if (action == 'add' && $scope.giftBagId.indexOf(id) == -1) {
            $scope.giftBagId.push(id);
            $scope.giftBagCode.push(code);
        }
        /*
         false移除集合元素
         */
        if (action == 'remove' && $scope.giftBagId.indexOf(id) != -1) {
            var idx = $scope.giftBagId.indexOf(id);
            $scope.giftBagId.splice(idx, 1);
            $scope.giftBagCode.splice(idx, 1);
        }
    }


    /*
     全选，遍历checkboxModel，正向操作
     */
    $scope.bagAll = function (v) {
        var checkbox = $(".bagCheckList span");
        for (var i = 0; i < checkbox.length; i++) {
            $(checkbox[i]).attr("class", "label label-success");
        }
        var idData = [];
        var codeData = [];
        $scope.bagModel = true;
        for (var i = 0; i < v.length; i++) {
            idData.push(v[i].packageId);
            codeData.push(v[i].packageName);
        }
        $scope.giftBagId = idData;
        $scope.giftBagCode = codeData;
    };

    /*
     全不选
     */
    $scope.deselectAllBag = function () {
        var checkbox = $(".bagCheckList span");
        for (var i = 0; i < checkbox.length; i++) {
            $(checkbox[i]).attr("class", "label label-default");
        }
        $scope.giftBagId = [];
        $scope.giftBagCode = [];
    }

    /*
     反选
     */
    $scope.bagVersa = function () {
        var checkbox = $(".bagCheckList span");
        /*
         遍历checkbox，并反向操作
         */
        for (var i = 0; i < checkbox.length; i++) {
            if ($(checkbox[i]).attr("class") == "label label-success") {
                $(checkbox[i]).attr("class", "label label-default");
                var idx = $scope.giftBagId.indexOf(Number($(checkbox[i]).attr('name')));
                $scope.giftBagId.splice(idx, 1);
                $scope.giftBagCode.splice(idx, 1);

            } else {
                $scope.giftBagId.push(Number($(checkbox[i]).attr('name')));
                $scope.giftBagCode.push($(checkbox[i]).text());
                $(checkbox[i]).attr("class", "label label-success");

            }

        }
    };

    /**
     * 物品类型改变时，查询自定义表单
     * @param itemType
     * @param index
     */
    $scope.itemTypeChange = function (itemType, index) {
        $scope.choiceItemType = itemType;
        $scope.goodsData.itemList.item[index].itemId = "";
        $scope.goodsData.itemList.item[index].itemName = "";
        if ($scope.goodsData.firstBuyPolicy.gameId != undefined && itemType != undefined && $scope.goodsData.firstBuyPolicy.gameId != "") {
            $scope.KEY = "formobject.list." + $scope.goodsData.firstBuyPolicy.gameId + "." + itemType;//配置自定义表单的key
            /*
             查询自定义表单
             */
            customFormService.customFormByKey({key: $scope.KEY}).success(function (response) {
                $scope.goodsData.itemList.item[index].extraData = JSON.parse(response.formJson);
                $scope.$apply();
            });

        } else {
            $scope.goodsData.itemList.item[index].extraData = [];
        }
    };


    if ($scope.gameToyGameList == 27) {//十冷
        $("#propIdDiv").hide();
        $("#goodsCodeDiv").show();
        $("#propDetails").show();
        $("#goodsGoodsName").show();
        $("#propGoodsName").hide();
        $scope.goodsData.goodsName = "";
        $scope.goodsData.goodsCode = "";
        $scope.goodsData.goodsDesc = "";
        $scope.goodsData.goodsPic = "";
        $scope.goodsData.relatedGoodsId = "";
    } else {//其他游戏
        if ($scope.goodsData.goodsType == $scope.flag) {
            $("#propIdDiv").hide();
            $("#goodsCodeDiv").show();
            $("#propDetails").hide();
            $("#goodsGoodsName").show();
            $("#propGoodsName").hide();
            $scope.goodsData.goodsName = "";
            $scope.goodsData.goodsCode = "";
            $scope.goodsData.goodsDesc = "";
            $scope.goodsData.goodsPic = "";
            $scope.goodsData.relatedGoodsId = "";
        } else {
            $("#propIdDiv").show();
            $("#goodsCodeDiv").hide();
            $("#propDetails").show();
            $("#goodsGoodsName").hide();
            $("#propGoodsName").show();
            $scope.goodsData.goodsName = "";
            $scope.goodsData.goodsCode = "";
            $scope.goodsData.goodsDesc = "";
            $scope.goodsData.goodsPic = "";
            $scope.goodsData.relatedGoodsId = "";
        }
    }
    /**
     * 商城类型change
     * @param shopType
     */
    $scope.shopTypeChange = function (shopType) {
        if ($scope.gameToyGameList == 27) {//十冷
            $("#propIdDiv").hide();
            $("#goodsCodeDiv").show();
            $("#propDetails").show();
            $("#goodsGoodsName").show();
            $("#propGoodsName").hide();
            $scope.goodsData.goodsName = "";
            $scope.goodsData.goodsCode = "";
            $scope.goodsData.goodsDesc = "";
            $scope.goodsData.goodsPic = "";
            $scope.goodsData.relatedGoodsId = "";
        } else {//其他游戏
            //storeType 0代表宝石商城 1代表RMB商城
            if (shopType == 0) {
                //$scope.goodsTypeDatas = [{id: 2, code: "load.goodsProp"}];
                /**
                 加载下拉框
                 */
                uniformFnService.uniGetKey($scope.uniFormData).success(function (response) {
                    response = JSON.parse(response.itemJson);
                    for (var i = 0; i < response.length; i++) {
                        response[i].key = "load.itemType" + response[i].key;
                        if (response[i].key == "load.itemTypegem") {
                            $scope.flagGem = i;

                        }
                        if (response[i].key == "load.itemTypeitem") {
                            $scope.flagGoods = i;
                        }

                    }
                    response.splice($scope.flagGem, 1);
                    response.splice($scope.flagGoods - 1, 1);
                    $scope.goodsTypeDatas = response;
                    $scope.$apply();
                });
                $scope.goodsData.goodsType = "";
                $("#propIdDiv").show();
                $("#goodsCodeDiv").hide();
                $("#propDetails").show();
                $("#goodsGoodsName").hide();
                $("#propGoodsName").show();
                $scope.goodsData.goodsName = "";
                $scope.goodsData.goodsCode = "";
                $scope.goodsData.goodsDesc = "";
            } else if (shopType == 1) {
                //$scope.goodsTypeDatas = [{id: 1, code: "load.goodsGem"}, {id: 2, code: "load.goodsProp"}];
                /**
                 加载下拉框
                 */
                uniformFnService.uniGetKey($scope.uniFormData).success(function (response) {
                    response = JSON.parse(response.itemJson);
                    for (var i = 0; i < response.length; i++) {
                        response[i].key = "load.itemType" + response[i].key;
                        if (response[i].key == "load.itemTypeitem") {
                            $scope.flagGoods = i;
                        }
                        ;
                    }
                    response.splice($scope.flagGoods, 1);
                    $scope.goodsTypeDatas = response;
                    $scope.$apply();
                });
                $scope.goodsData.goodsType = "";
                $("#propIdDiv").hide();
                $("#goodsCodeDiv").show();
                $("#propDetails").hide();
                $("#goodsGoodsName").show();
                $("#propGoodsName").hide();
                $scope.goodsData.goodsName = "";
                $scope.goodsData.goodsCode = "";
                $scope.goodsData.goodsDesc = "";
                $scope.goodsData.goodsPic = "";
                $scope.goodsData.relatedGoodsId = "";
            } else {
                $scope.goodsData.goodsType = "";
                $scope.goodsData.goodsName = "";
                $scope.goodsData.goodsCode = "";
                $scope.goodsData.goodsDesc = "";
                $scope.goodsData.goodsPic = "";
                $scope.goodsData.relatedGoodsId = "";
            }
        }

    }

    /**
     * 商品类型change
     * @param goodsType
     */
    $scope.goodsTypeChange = function (goodsType) {
        //$scope.flag 代表宝石
        if ($scope.gameToyGameList == 27) {//十冷
            $("#propIdDiv").hide();
            $("#goodsCodeDiv").show();
            $("#propDetails").show();
            $("#goodsGoodsName").show();
            $("#propGoodsName").hide();
            $scope.goodsData.goodsName = "";
            $scope.goodsData.goodsCode = "";
            $scope.goodsData.goodsDesc = "";
            $scope.goodsData.goodsPic = "";
            $scope.goodsData.relatedGoodsId = "";
        } else {//其他游戏
            if (goodsType == $scope.flag) {
                $("#propIdDiv").hide();
                $("#goodsCodeDiv").show();
                $("#propDetails").hide();
                $("#goodsGoodsName").show();
                $("#propGoodsName").hide();
                $scope.goodsData.goodsName = "";
                $scope.goodsData.goodsCode = "";
                $scope.goodsData.goodsDesc = "";
                $scope.goodsData.goodsPic = "";
                $scope.goodsData.relatedGoodsId = "";
            } else {
                $("#propIdDiv").show();
                $("#goodsCodeDiv").hide();
                $("#propDetails").show();
                $("#goodsGoodsName").hide();
                $("#propGoodsName").show();
                $scope.goodsData.goodsName = "";
                $scope.goodsData.goodsCode = "";
                $scope.goodsData.goodsDesc = "";
                $scope.goodsData.goodsPic = "";
                $scope.goodsData.relatedGoodsId = "";
            }
        }

    };

    /**
     *刷新查询页面，调用查询方法 公共方法
     */
    function itemFindByItemTypePublic(gameToyTypeSearch, pageIndex, pageSize) {
        gameToyTypeFnService.itemFindByItemType(gameToyTypeSearch, pageIndex, pageSize).success(function (response) {
            //$scope.gameTypeToys = response.rows;
            //$scope.pageCountItem = response.page.pageCount;
            //$scope.$apply();

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

            $scope.pageCountItem = response.page.pageCount;
            $scope.$apply();
        });
    }

    /**
     *分页
     */
    $scope.onPageChangeItem = function () {
        /*
         刷新查询页面，调用查询方法
         */
        itemFindByItemTypePublic($scope.gameToyTypeSearch, $scope.currentPageItem, 10);
    };


    /**
     *查询表单监听
     */
    $scope.searchButtonClicked = function () {
        /*
         刷新查询页面，调用查询方法
         */
        itemFindByItemTypePublic($scope.gameToyTypeSearch, 1, 10);
    }

    /**
     物品选择（弹框）
     */
    $scope.itemSelected = function () {
        $scope.firstDataChoice = false;
        if ($scope.goodsData.gameId != "" && $scope.goodsData.gameId != undefined && $scope.goodsData.gameId != -1 && $scope.goodsData.goodsType != "" && $scope.goodsData.goodsType != undefined) {
            $scope.gameToyTypeSearch.itemType = $scope.goodsData.goodsType;//3代表道具
            $scope.gameToyTypeSearch.gameId = $scope.goodsData.gameId;
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

    }


    ///**
    // *物品选择（勾选）
    // */
    //$scope.itemItemChoice = function (data) {
    //    $scope.goodsData.goodsName = data.itemName;
    //    $scope.goodsData.goodsCode = data.itemCode;
    //    $scope.goodsData.goodsDesc = data.itemExtend;
    //    $("#itemModal").modal("hide");
    //};

    /**
     根据全局game，site查询区服
     */
    if ($scope.billingGoodsSearch.gameId == undefined) {
        $scope.billingGoodsSearch.gameId = -1;
    }

    if ($routeParams.judge == "update") {
        $("#goodsCode").attr("readonly", "readonly");
        billingGoodsFnService.goodsGetById($routeParams.id).success(function (data) {


            $scope.goodsData = data;
            $scope.goodsData.itemList = {};
            $scope.goodsData.itemList.item = JSON.parse(data.itemJson);
            //if (data.firstBuyPolicy != null) {
            //    $scope.goodsData.firstBuyPolicy.operateCount = data.firstBuyPolicy.operateCount;
            //    $scope.goodsData.firstBuyPolicy.isFirstPay = data.firstBuyPolicy.firstPay;
            //} else {
            //    $scope.goodsData.firstBuyPolicy = {};
            //    $scope.goodsData.firstBuyPolicy.operateCount = 2;
            //    data.firstBuyPolicy = {};
            //    data.firstBuyPolicy.packageId = "";
            //    data.firstBuyPolicy.operateCount = 2;
            //}

            if ($scope.goodsData.itemList.item != null) {
                $scope.clientCount = $scope.goodsData.itemList.item.length;
            } else {
                $scope.clientCount = 0;
                $scope.goodsData.itemList.item = [{itemType: "", itemId: "", itemName: "", value: "", extraData: []}];
            }

            if ($scope.clientCount > 1) {
                $scope.clientContent.canDescReply = true;
            }
            for (var i = 0; i < $scope.goodsData.itemList.item.length; i++) {
                //$scope.articleEditData.item[i].itemType = $scope.articleEditData.item[i].itemType.toString();
                $scope.goodsData.itemList.item[i].value = Number($scope.goodsData.itemList.item[i].value);
                $scope.goodsData.itemList.item[i].itemType = $scope.goodsData.itemList.item[i].itemType.value;
            }
            data.goodsMarkTime = $scope.format(data.goodsMarkTime, "yyyy-MM-dd HH:mm:ss");
            $scope.goodsData.goodsType = data.goodsType.toString();
            $scope.validStatus = data.validStatus.toString();
            $scope.shopType = Number(data.shopType);
            $scope.gameToyGameList = data.gameId;
            $scope.gameToySiteList = data.siteId;
            $scope.goodsData.gameId = data.gameId;
            $scope.goodsData.siteId = data.siteId;
            $scope.goodsData.areaIds = data.areaIds;
            $scope.bagListCondition.gameId = data.gameId;
            $scope.choiceGameId = data.gameId;
            if ($scope.goodsData.gameId == 27) {//十冷
                $scope.storeTypeDatas = [{id: 0, code: "load.normalPrepaidPay"}, {
                    id: 1,
                    code: "load.gameActivitiePay"
                }, {id: 3, code: "load.bagStore"}];
            } else {//其他游戏
                $scope.storeTypeDatas = [{id: 0, code: "load.gemStore"}, {id: 1, code: "load.RMBStore"}];
            }

            if ($scope.goodsData.gameId == 27) {//十冷
                $scope.goodsTypeDatas = $scope.shiLengKeyData;
            } else {//其他游戏
                $scope.goodsTypeDatas = $scope.otherKeyData;
            }
            /*
             初始化
             */

            var bagCheckbox = $(".bagCheckList span");
            var bagIds = data.awardId;

            for (var j = 0; j < $scope.giftBagLists.length; j++) {
                if (bagIds == $scope.giftBagLists[j].packageId) {
                    $scope.giftBagCode.push($scope.giftBagLists[j].packageName);
                    $scope.giftBagId.push($scope.giftBagLists[j].packageId);
                }
            }

            /*
             遍历checkbox，并初始化
             */
            for (var k = 0; k < bagCheckbox.length; k++) {
                if ($(bagCheckbox[k]).attr('name') == bagIds) {
                    $(bagCheckbox[k]).attr("class", "label label-success");
                }
            }

            $scope.giftBagDatas = $scope.giftBagCode;
            //查询区服
            $.ajax({
                url: ctx + '/platform/gameArea/getGameAreasById',
                data: {"gameId": data.gameId},
                success: function (response) {
                    $scope.areaLists = response;
                    $scope.$apply();
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
                    $scope.$apply();
                    console.log($scope.areaData);
                }, async: false
            });


            if ($scope.gameToyGameList == 27) {//十冷
                $("#propIdDiv").hide();
                $("#goodsCodeDiv").show();
                $("#propDetails").show();
                $("#goodsGoodsName").show();
                $("#propGoodsName").hide();
            } else {
                if ($scope.goodsData.goodsType == 1) {
                    $("#propIdDiv").hide();
                    $("#goodsCodeDiv").show();
                    $("#propDetails").hide();
                    $("#goodsGoodsName").show();
                    $("#propGoodsName").hide();
                } else if ($scope.goodsData.goodsType == 2) {
                    $("#propIdDiv").show();
                    $("#goodsCodeDiv").hide();
                    $("#propDetails").show();
                    $("#goodsGoodsName").hide();
                    $("#propGoodsName").show();
                }
            }
            $scope.$apply();
        });
    }
    ;


    $scope.goodsData.goodsId = $routeParams.id;


    /**
     根据全局game，site查询区服
     */
    if ($scope.gameAreasData.gameId == undefined) {
        $scope.gameAreasData.gameId = -1;
    }

    /**
     * 获取区服列表
     */
    $.ajax({
        url: ctx + '/platform/gameArea/getGameAreasById',
        data: {"gameId": $scope.gameAreasData.gameId},
        success: function (data) {
            $scope.areaLists = data;
        },
        async: false
    });

    //gameAreaFindByNamePublic($scope.billingGoodsSearch, 1, 10);
    ///**
    // * 调用查询方法，并传最新的currentPage
    // * @param gameToyTypeSearch
    // * @param pageIndex
    // * @param pageSize
    // */
    //function gameAreaFindByNamePublic(billingGoodsSearch, pageIndex, pageSize) {
    //    gameAreaFnService.gameAreaFindByName(billingGoodsSearch, pageIndex, pageSize).success(function (response) {
    //        $scope.areaLists = response.rows;
    //        $scope.pageCount = response.page.pageCount;
    //        $scope.$apply();
    //    });
    //};

    //
    ///**
    // 区服分页
    // */
    //$scope.onPageChange = function () {
    //    /*
    //     调用查询方法，并传最新的currentPage
    //     */
    //    gameAreaFindByNamePublic($scope.billingGoodsSearch, $scope.currentPage, 10);
    //};

    /*    /!**
     奖励包列表
     *!/
     billingGoodsFnService.awardPackage($scope.awardCondition, 1, 5).success(function (data) {
     $scope.awardPackage = data.rows;//绑定奖励包
     $scope.firstAwardPackage = data.rows;//绑定首充奖励包
     $scope.pageAwardCount = data.page.pageCount;//绑定奖励包分页
     $scope.pageFirstAwardCount = data.page.pageCount;//绑定奖励包分页
     $scope.$apply();
     });*/

    ///**
    // 首充奖励（弹框）
    // */
    //$scope.firstBuyClick = function () {
    //    $("#firstBuyModal").modal('show');
    //}
    //
    ///**
    // 监听首充form
    // */
    //$scope.firstBuyAdd = function () {
    //    var name = $scope.firstGoodsCode + $scope.firstBuyOperation + $scope.operateCount;
    //    var names = [];
    //    names.push(name);
    //    $scope.firstBuyNames = names;
    //    $scope.messagesData.messagesTitle = "修改结果";
    //    $scope.messagesData.messagesBody = "首充奖励礼包修改成功！";
    //    $("#messagesModal").modal('show');
    //    $("#firstBuyModal").modal('hide');
    //}

    /**
     监听商品form
     */
    $scope.billingGoodsEditForm = function () {

        billingGoodsFnService.checkCode($scope.goodsData.goodsCode).success(function (data) {

            $scope.goodsData.goodsMarkTime = new Date($scope.goodsData.goodsMarkTime);
            $scope.goodsData.createTime = new Date($scope.goodsData.createTime);

            for (var i = 0; i < $scope.goodsData.itemList.item.length; i++) {
                delete $scope.goodsData.itemList.item[i].$$hashKey;
                if ($scope.goodsData.itemList.item[i].extraData != undefined) {
                    for (var j = 0; j < $scope.goodsData.itemList.item[i].extraData.length; j++) {
                        delete $scope.goodsData.itemList.item[i].extraData[j].$$hashKey;
                    }
                }
                $scope.goodsData.itemList.item[i].itemType = {
                    key: "",
                    value: $scope.goodsData.itemList.item[i].itemType
                };
            }

            if ($scope.giftBagId.length > 0) {
                $scope.goodsData.packageId = $scope.giftBagId[0];
            } else {
                $scope.goodsData.packageId = 0;
            }

            $scope.goodsData.itemPackage = JSON.stringify($scope.goodsData.itemList.item);
            //$scope.goodsData.operateCount = $scope.goodsData.firstBuyPolicy.operateCount;
            //$scope.goodsData.isFirstPay = $scope.goodsData.firstBuyPolicy.isFirstPay;

            delete $scope.goodsData.firstBuyPolicy;
            $scope.goodsData.valid = $scope.validStatus;
            $scope.goodsData.shopTypeId = $scope.shopType;
            var areaids = "";
            for (var i = 0; i < $scope.areaId.length; i++) {
                areaids += $scope.areaId[i] + ",";
            }
            $scope.goodsData.areaIds = areaids;

            if ($routeParams.judge == "update") {
                delete $scope.goodsData.validStatus;
                delete $scope.goodsData.shopType;
                /*
                 调用修改方法
                 */
                billingGoodsFnService.billingGoodsUpdate($scope.goodsData).success(function () {
                    check = 1;
                    $location.path("/billingGoods/view/" + check + "/" + $routeParams.pageNumber);
                    $scope.$apply();
                    $("#messagesModal").modal('show');

                }).error(function () {
                    check = 0;
                    $location.path("/billingGoods/view/" + check + "/" + $routeParams.pageNumber);
                    $scope.$apply();
                    $("#messagesModal").modal('show');
                });
            } else {
                if (data == 0) {
                    delete $scope.goodsData.createTime;
                    delete $scope.goodsData.goodsId;
                    /*
                     调用添加方法
                     */
                    billingGoodsFnService.billingGoodsAdd($scope.goodsData).success(function () {
                        check = 2;
                        $location.path("/billingGoods/view/" + check + "/" + $routeParams.pageNumber);
                        $scope.$apply();
                        $("#messagesModal").modal('show');

                    }).error(function () {
                        check = 3;
                        $location.path("/billingGoods/view/" + check + "/" + $routeParams.pageNumber);
                        $scope.$apply();
                        $("#messagesModal").modal('show');
                    });
                } else {
                    $scope.messagesData.messagesTitle = "load.noticeTipTitle";
                    $scope.messagesData.messagesBody = "load.goodsCheckCodeTip";
                    $("#messagesModal").modal('show');
                    $scope.$apply();
                }
            }
        });


    };

    /**
     更改奖励包的选择状态，并保存相应的id
     */
    $scope.check = function (val, chk) {
        var data = $(this);
        if (chk == true) {
            $scope.awardId += data[0].award.awardId + ",";
            $scope.awardNames.push(data[0].award.awardName);
        } else {
            var idx = $scope.awardNames.indexOf(data[0].award.awardName);
            $scope.awardNames.splice(idx, 1);
            $scope.awardId = $scope.awardId.replace(data[0].award.awardId + ",", "");
        }

    };

    /**
     更改首充奖励包的选择状态，并保存相应的id
     */
    $scope.firstAwardCheck = function (val, chk) {
        var data = $(this);
        if (chk == true) {
            $scope.firstAwardId += data[0].award.awardId + ",";
            $scope.firstAwardNames.push(data[0].award.awardName);
        } else {
            var idx = $scope.firstAwardNames.indexOf(data[0].award.awardName);
            $scope.firstAwardNames.splice(idx, 1);
            $scope.firstAwardId = $scope.firstAwardId.replace(data[0].award.awardId + ",", "");
        }

    };

    /**
     全选 （奖励包）
     */
    $scope.awardCheckClick = function () {
        var checkbox = $(".awardCheck");
        /*
         遍历checkbox，并操作
         */
        for (var i = 0; i < checkbox.length; i++) {
            if (checkbox[i].checked) {
                var idx = $scope.awardNames.indexOf(checkbox[i].name);
                $scope.awardNames.splice(idx, 1);
                $scope.awardId = $scope.awardId.replace(checkbox[i].value + ",", "");
                checkbox[i].checked = false;
            } else {
                $scope.awardId += checkbox[i].value + ",";
                $scope.awardNames.push(checkbox[i].name);
                checkbox[i].checked = true;
            }

        }
    }


    /**
     全选 （首充奖励包）
     */
    $scope.firstAwardCheckClick = function () {
        var checkbox = $(".firstAwardCheck");
        /*
         遍历checkbox，并操作
         */
        for (var i = 0; i < checkbox.length; i++) {
            if (checkbox[i].checked) {
                var idx = $scope.firstAwardNames.indexOf(checkbox[i].name);
                $scope.firstAwardNames.splice(idx, 1);
                $scope.firstAwardId = $scope.awardId.replace(checkbox[i].value + ",", "");
                checkbox[i].checked = false;
            } else {
                $scope.firstAwardId += checkbox[i].value + ",";
                $scope.firstAwardNames.push(checkbox[i].name);
                checkbox[i].checked = true;
            }

        }
    }

    /**
     奖励包form监听
     */
    $scope.awardForm = function () {
        $scope.messagesData.messagesTitle = "修改结果";
        $scope.messagesData.messagesBody = "奖励礼包修改成功！";
        $("#messagesModal").modal('show');
        $("#awardPackageModal").modal('hide');
        $scope.awardData = $scope.awardNames;
    };

    /**
     首充奖励包form监听
     */
    $scope.firstAwardForm = function () {
        $scope.messagesData.messagesTitle = "修改结果";
        $scope.messagesData.messagesBody = "首充奖励礼包修改成功！";
        $("#messagesModal").modal('show');
        $("#firstAwardModal").modal('hide');
        $scope.firstAwardData = $scope.firstAwardNames;
    };


    /**
     根据awardPackage名称模糊查询
     */
    $scope.searchAwardClicked = function () {
        /* $scope.awardCondition.awardName=$scope.awardSearch;*/
        /*
         调用awardPackage的查询方法
         */
        billingGoodsFnService.awardPackage($scope.awardCondition, $scope.currentAwardPage, 5).success(function (data) {
            $scope.awardPackage = data.rows;
            $scope.pageAwardCount = data.page.pageCount;
            $scope.$apply();
        });
    };

    /**
     根据firstAwardSearch名称模糊查询
     */
    $scope.searchFirstAwardClicked = function () {
        /* $scope.awardCondition.awardName=$scope.awardSearch;*/
        /*
         调用firstBuy的查询方法
         */
        billingGoodsFnService.awardPackage($scope.awardCondition, $scope.currentFirstAwardPage, 5).success(function (data) {
            $scope.firstAwardPackage = data.rows;
            $scope.pageFirstAwardCount = data.page.pageCount;
            $scope.$apply();
        });
    };
    /**
     奖励包分页
     */
    $scope.onAwardPageChange = function () {
        // ajax request to load data
        console.log($scope.currentAwardPage);
        /*
         调用awardPackage的查询方法
         */
        billingGoodsFnService.awardPackage($scope.awardCondition, $scope.currentAwardPage, 5).success(function (data) {
            $scope.awardPackage = data.rows;
            $scope.pageAwardCount = data.page.pageCount;
            $scope.$apply();
        });
    };

    /* /!*
     首充奖励包分页
     *!/
     $scope.onFirstAwardPageChange = function() {
     // ajax request to load data
     console.log($scope.currentFirstAwardPage);
     /!*
     调用awardPackage的查询方法
     *!/
     billingGoodsFnService.awardPackage($scope.awardCondition,$scope.currentFirstAwardPage,5).success(function(data){
     $scope.firstAwardPackage=data.rows;
     $scope.pageFirstAwardCount =data.page.pageCount;
     $scope.$apply();
     });
     };*/

    /**
     奖励方式change事件
     */
    $scope.awardChange = function (data) {
        if (data == undefined) {
            data = -1;
        }
        $scope.awardCondition.awardType = data;
    }

    /**
     首充奖励方式change事件
     */
    $scope.firstAwardChange = function (data) {
        if (data == undefined) {
            data = -1;
        }
        $scope.awardCondition.awardType = data;
    }


    /**
     绑定select，change事件
     */
    $scope.resourceGameListChange = function (id) {

        //
        $scope.clientCount = 0;
        $scope.goodsData.itemList.item = [{itemType: "", itemId: "", itemName: "", value: "", extraData: []}];//清空物品选项
        // 初始化时由于只有1条回复，所以不允许删除
        $scope.clientContent.canDescReply = false;

        $scope.choiceGameId = id;
        if ($scope.goodsData.firstBuyPolicy.gameId != undefined && $scope.goodsData.itemList.itemType != undefined) {
            $scope.KEY = "formobject.list." + id + "." + $scope.goodsData.itemList.itemType;//配置自定义表单的key
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

        $scope.gameToyTypeSearch.gameId = id;//初始化查询gameId
        if (id == 27) {//十冷
            $scope.storeTypeDatas = [{id: 0, code: "load.normalPrepaidPay"}, {
                id: 1,
                code: "load.gameActivitiePay"
            }, {id: 3, code: "load.bagStore"}];
        } else {//其他游戏
            $scope.storeTypeDatas = [{id: 0, code: "load.gemStore"}, {id: 1, code: "load.RMBStore"}];
        }

        if (id == 27) {//十冷
            $scope.goodsTypeDatas = $scope.shiLengKeyData;
        } else {//其他游戏
            $scope.goodsTypeDatas = $scope.otherKeyData;
        }

        $scope.billingGoodsSearch.gameId = id;
        if ($scope.billingGoodsSearch.gameId == undefined) {
            $scope.billingGoodsSearch.gameId = -1;
        }
        if ($scope.billingGoodsSearch.siteId == undefined) {
            $scope.billingGoodsSearch.siteId = -1;
        }
        $scope.goodsData.gameId = id;
        /**
         * 获取区服列表
         */
        $.ajax({
            url: ctx + '/platform/gameArea/getGameAreasById',
            data: {"gameId": id},
            success: function (data) {
                $scope.areaLists = data;
            },
            async: false
        });


        /*billingGoodsFnService.areaList(id,$scope.gameToySiteList).success(function(data){
         $scope.gameToyGameList=id;
         $scope.goodsData.gameId=id;
         $scope.areaLists=data;
         });*/
    };

    $scope.resourceSiteListChange = function (id) {
        $scope.billingGoodsSearch.siteId = id;
        if ($scope.billingGoodsSearch.gameId == undefined) {
            $scope.billingGoodsSearch.gameId = -1;
        }
        if ($scope.billingGoodsSearch.siteId == undefined) {
            $scope.billingGoodsSearch.siteId = -1;
        }

        /*
         刷新查询页面，调用查询方法
         */
        gameAreaFnService.gameAreaFindByName($scope.billingGoodsSearch, 1, 10).success(function (response) {
            $scope.goodsData.siteId = id;
            $scope.areaLists = response.rows;
            $scope.pageCount = response.page.pageCount;
            $scope.$apply();
        });

    };

    /**
     选择区服（弹框）
     */
    $scope.areaClick = function () {
        $("#areaAddModal").modal('show');
    };
    /**
     选择渠道（弹框）
     */
    $scope.channelClick = function () {
        $("#channelAddModal").modal('show');
    }

    /**
     选择奖励包（弹框）
     */
    $scope.awardPackageClick = function () {
        $("#awardPackageModal").modal('show');
    }


    /**
     首充奖励包（弹框）
     */
    $scope.firstAwardClick = function () {
        $("#firstAwardModal").modal('show');
    }

    /**
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


    //===================================================================

    /**
     * 物品包添加
     */
    $scope.goodsData.item = {}//商品集合;
    $scope.placeholderItemName = "load.placeholderItemName";//道具名称搜索国际化
    $scope.placeholderItemCode = "load.placeholderItemCode";//道具code搜索国际化
    $scope.messagesData = {};//设置提示框model数据集合
    $scope.uniFormData = {};//下拉框选项集合key对象初始化
    $scope.uniFormData.key = "operation.item.itemType";//初始化动态下拉框的key
    $scope.choiceCheck = 0;//判断第几个自定义表单
    $scope.choiceItemType = "";//选中的物品类型
    $scope.choiceGameId = $scope.gameTransfer;//物品选择：游戏类型
    $scope.gameToyTypeSearch = {};//初始化查询条件对象
    $scope.gameToyTypeSearch.gameId = $scope.gameTransfer;//初始化查询gameId
    $scope.gameToyTypeSearch.itemTypeName = "";
    $scope.gameToyTypeSearch.itemCode = "";


});