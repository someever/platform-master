/**
 * 首充策略controllers文件
 *   author：钟亮
 * @type {module|*}
 */
adminApp.config(function ($routeProvider) {
    $routeProvider.when('/firstBuyPolicy/view', {
        templateUrl: ctx + '/platform/billingGoods/firstBuyPolicyInit',
        controller: 'firstBuyPolicyController'
    });
});


/**
 首充策略controller
 全局：$scope
 */
adminApp.controller('firstBuyPolicyController', function ($scope, uniformFnService, $location, customFormService, gameToyTypeFnService, billingGoodsFnService) {
    $scope.firstBuyData = {};//物品申请编辑对象
    $scope.firstBuyData.item = {};//物品对象
    $scope.uniFormData = {};//下拉框选项集合key对象初始化
    $scope.uniFormData.key = "operation.item.itemType";//初始化动态下拉框的key
    $scope.messagesData = {};//设置提示框model数据集合
    $scope.firstBuyData.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
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
    $scope.firstBuyData.isFirstPay = false;

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

    /**
     *gameId改变时，查询自定义表单
     */
    $scope.gameIdChange = function (gameId) {
        //
        $scope.clientCount = 0;
        $scope.firstBuyData.item = [{itemType: "", itemId: "", itemName: "", value: "", extraData: []}];//清空物品选项
        // 初始化时由于只有1条回复，所以不允许删除
        $scope.clientContent.canDescReply = false;

        $scope.choiceGameId = gameId;
        if ($scope.firstBuyData.gameId != undefined && $scope.firstBuyData.itemType != undefined) {
            $scope.KEY = "formobject.list." + gameId + "." + $scope.firstBuyData.itemType;//配置自定义表单的key
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
        $scope.firstBuyData.item[index].itemId = "";
        $scope.firstBuyData.item[index].itemName = "";
        if ($scope.firstBuyData.gameId != undefined && itemType != undefined && $scope.firstBuyData.gameId != "") {
            $scope.KEY = "formobject.list." + $scope.firstBuyData.gameId + "." + itemType;//配置自定义表单的key
            /*
             查询自定义表单
             */
            customFormService.customFormByKey({key: $scope.KEY}).success(function (response) {
                $scope.firstBuyData.item[index].extraData = JSON.parse(response.formJson);
                $scope.$apply();
            });

        } else {
            $scope.firstBuyData.item[index].extraData = [];
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
        $scope.firstBuyData.item[$scope.choiceCheck].itemId = data[0].gameToyType.itemCode;
        $scope.firstBuyData.item[$scope.choiceCheck].itemName = data[0].gameToyType.itemName;
        $("#itemModal").modal("hide");
    };


    $scope.clientCount = 0;
    $scope.clientContent = new Object();
    $scope.firstBuyData.item = [{itemType: "", itemId: "", itemName: "", value: "", extraData: []}];
    // 初始化时由于只有1条回复，所以不允许删除
    $scope.clientContent.canDescReply = false;
    /**
     * 增加物品填写条例
     */
    $scope.clientContent.clientAdd = function ($index) {
        if ($scope.clientCount < 4) {
            $scope.clientCount++;
            $scope.firstBuyData.item.splice($index + 1, 0, {itemType: "", itemId: "", itemName: "", value: ""});
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
        if ($scope.firstBuyData.item.length > 1) {
            $scope.firstBuyData.item.splice($index, 1);
            $scope.clientCount--;
        }
        // 如果回复数为1，不允许删除
        if ($scope.firstBuyData.item.length == 1) {
            $scope.clientContent.canDescReply = false;
        }
    };

    billingGoodsFnService.getFirstBuyPolicy($scope.gameTransfer).success(function (data) {
        if (data != "" && data != null) {
            $scope.firstBuyData.operateCount = data.operateCount;
            $scope.firstBuyData.isFirstPay = data.firstPay;
            $scope.firstBuyData.gameId = $scope.gameTransfer;
            $scope.choiceGameId = $scope.gameTransfer;
            if (data.itemPackage != "" && data.itemPackage != null && data.itemPackage != undefined) {
                $scope.firstBuyData.item = JSON.parse(data.itemPackage);
                $scope.clientCount = $scope.firstBuyData.item.length;
                if ($scope.clientCount > 1) {
                    $scope.clientContent.canDescReply = true;
                }
                for (var i = 0; i < $scope.firstBuyData.item.length; i++) {
                    //$scope.firstBuyData.item[i].itemType = $scope.firstBuyData.item[i].itemType.toString();
                    $scope.firstBuyData.item[i].value = Number($scope.firstBuyData.item[i].value);
                    $scope.firstBuyData.item[i].itemType = $scope.firstBuyData.item[i].itemType.value;
                }
            } else {
                $scope.clientCount = 0;
                $scope.clientContent = new Object();
                $scope.firstBuyData.item = [{itemType: "", itemId: "", itemName: "", value: "", extraData: []}];
                // 初始化时由于只有1条回复，所以不允许删除
                $scope.clientContent.canDescReply = false;
            }

            $scope.$apply();
        }
        ;
    });

    /**
     * 添加首充
     */
    $scope.firstBuyPolicyForm = function () {

        for (var i = 0; i < $scope.firstBuyData.item.length; i++) {
            delete $scope.firstBuyData.item[i].$$hashKey;
            if ($scope.firstBuyData.item[i].extraData != undefined) {
                for (var j = 0; j < $scope.firstBuyData.item[i].extraData.length; j++) {
                    delete $scope.firstBuyData.item[i].extraData[j].$$hashKey;
                }
            }
            $scope.firstBuyData.item[i].itemType = {key: "", value: $scope.firstBuyData.item[i].itemType};
        }
        $scope.firstBuyData.itemPackage = encodeURIComponent(JSON.stringify($scope.firstBuyData.item), "utf-8");
        billingGoodsFnService.firstBuyPolicyEdit($scope.firstBuyData).success(function () {
            check = 1;
            $location.path("/billingGoods/view/" + check);
            $scope.$apply();
            $("#messagesModal").modal('show');

        }).error(function () {
            check = 0;
            $location.path("/billingGoods/view/" + check);
            $scope.$apply();
            $("#messagesModal").modal('show');
        });
    };

});