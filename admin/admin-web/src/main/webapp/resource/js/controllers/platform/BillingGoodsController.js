/**
 * 商品配方controllers文件
 *   author：钟亮
 * @type {module|*}
 */
adminApp.config(function ($routeProvider) {
    $routeProvider.when('/billingGoods/view', {
        templateUrl: ctx + '/platform/billingGoods/billingGoodsInit',
        controller: 'billingGoodsController'
    }).when("/billingGoods/view/:check/:pageNumber", {
        templateUrl: ctx + '/platform/billingGoods/billingGoodsInit',
        controller: 'billingGoodsController'
    });
});


/*
 商品配方controller
 全局：$scope
 Service:billingGoodsFnService
 */
adminApp.controller('billingGoodsController', function ($scope, billingGoodsFnService, $location, $routeParams, $rootScope) {

    //初始化菜单栏
    $rootScope.menuBarData.menuBarThreeName = "";
    $rootScope.menuBarData.menuBarTitle = "ProductFormula";
    $scope.messagesData = {};//设置提示框model数据集合
    $scope.messagesConfirm = {};//设置确认框model数据集合
    $scope.billingGoodsSearch = {};//初始化查询条件对象
    $scope.billingGoodsSearch.gameId = $scope.gameTransfer;//初始化查询gameId
    $scope.billingGoodsSearch.siteId = $scope.siteTransfer;//初始化查询siteId
    $scope.goodsSearchText = "load.goodsSearchText";//搜索提示
    $scope.goodsId = "";//商品id字符串
    var index = 0;//修改角色的下标

    $scope.firstBuyData = {};//区服对象集合
    $scope.firstBuyData.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
    $scope.gameToyGameList = $scope.gameTransfer;//gameToyGameList，与全局game同步
    $scope.keydata = {};//一键修改
    /**
     绑定game,site select，change事件
     */
    $scope.resourceGameListChange = function (id) {
        $scope.firstBuyData.gameId = id;
    };

    /**
     * 判断是否有操作权限
     */
    function checkAuthority(type, execute) {
        //判断是否有操作权限
        $.ajax({
            url: ctx + '/system/relation/checkAuthority',
            data: {menuName: $rootScope.menuBarData.menuBarTitle, type: type},//1：添，2：删，3：改，4：查
            success: function (data) {
                if (data) {
                    execute();
                } else {
                    $scope.messagesData.messagesTitle = "load.messagesTitleByAuthorityLimit";
                    $scope.messagesData.messagesBody = "load.messagesBodyByAuthorityLimit";
                    $("#messagesModal").modal('show');
                }
            }, async: false
        });

    }

    /*
     商品配方添加（跳转）
     */
    $scope.addButton = function () {
        var add = function () {
            $location.path("/billingGoodsEdit/view/null/null/" + 1);
        };
        //判断是否有权限
        checkAuthority(1, add);//1：添，2：删，3：改，4：查 执行函数

    };

    function keyUpdate(id, value, state) {
        $scope.chk = false;
        $(".allCheckData").prop("checked", false);
        $scope.keydata.id = id;
        $scope.keydata.value = value;
        $scope.keydata.state = state

        /*
         调用修改方法
         */
        billingGoodsFnService.keyUpdate($scope.keydata).success(function () {
            $scope.messagesData.messagesTitle = "load.messagesTitleByUpdate";
            $scope.messagesData.messagesBody = "load.messagesBodyByUpdateToSuccess";
            $("#messagesModal").modal('show');
            $scope.goodsId = "";
            $scope.$apply();
            /*
             刷新查询页面，调用查询方法
             */
            billingGoodsFnService.billingGoodsFindByName($scope.billingGoodsSearch, $scope.currentPage, 10).success(function (response) {
                console.log(response);
                $scope.goods = response.rows;
                $scope.pageCount = response.page.pageCount;
                $scope.$apply();
            });
        }).error(function () {
            $scope.messagesData.messagesTitle = "load.messagesTitleByUpdate";
            $scope.messagesData.messagesBody = "load.messagesBodyByUpdateToFailure";
            $("#messagesModal").modal('show');
            $scope.goodsId = "";
            $scope.$apply();
            /*
             刷新查询页面，调用查询方法
             */
            billingGoodsFnService.billingGoodsFindByName($scope.billingGoodsSearch, $scope.currentPage, 10).success(function (response) {
                console.log(response);
                $scope.goods = response.rows;
                $scope.pageCount = response.page.pageCount;
                $scope.$apply();
            });
        });
    };

    /**
     * 一键上架
     */
    $scope.goodsKeyStores = function () {
        var updateL = function () {
            if ($scope.goodsId != "") {
                keyUpdate($scope.goodsId, 1, "frame");//1代表上架 frame代表这一键上架下架
            } else {
                $scope.messagesData.messagesTitle = "load.promptTitle";
                $scope.messagesData.messagesBody = "load.promptBody";
                $("#messagesModal").modal('show');
            }
        };
        //判断是否有权限
        checkAuthority(3, updateL);//1：添，2：删，3：改，4：查 执行函数

    };

    /**
     * 刷新缓存
     */
    $scope.refreshBillingGoodsCache = function () {
        billingGoodsFnService.refreshBillingGoodsCache().success(function () {
            $scope.messagesData.messagesTitle = "load.UpAreaMessagesTitle";
            $scope.messagesData.messagesBody = "load.UpAreaMessagesToSuccess";
            $("#messagesModal").modal('show');
            $scope.$apply();
        }).error(function () {
            $scope.messagesData.messagesTitle = "load.UpAreaMessagesTitle";
            $scope.messagesData.messagesBody = "load.UpAreaMessagesToFailure";
            $("#messagesModal").modal('show');
            $scope.$apply();
        });
    };

    /**
     * 一键下架
     */
    $scope.goodsKeySoldOut = function () {
        var updateL = function () {
            if ($scope.goodsId != "") {
                keyUpdate($scope.goodsId, 2, "frame");//2代表下架 frame代表这一键上架下架
            } else {
                $scope.messagesData.messagesTitle = "load.promptTitle";
                $scope.messagesData.messagesBody = "load.promptBody";
                $("#messagesModal").modal('show');
            }
        };
        //判断是否有权限
        checkAuthority(3, updateL);//1：添，2：删，3：改，4：查 执行函数

    };

    /**
     * 一键开启首充
     */
    $scope.goodsKeyOpenFirstBuy = function () {
        var updateL = function () {
            if ($scope.goodsId != "") {
                keyUpdate($scope.goodsId, 1, "firstBuy");//1代表开启 firstBuy代表这首充开启关闭
            } else {
                $scope.messagesData.messagesTitle = "load.promptTitle";
                $scope.messagesData.messagesBody = "load.promptBody";
                $("#messagesModal").modal('show');
            }
        };
        //判断是否有权限
        checkAuthority(3, updateL);//1：添，2：删，3：改，4：查 执行函数
    };

    /**
     * 一键关闭首充
     */
    $scope.goodsKeyCloseFirstBuy = function () {
        var updateL = function () {
            if ($scope.goodsId != "") {
                keyUpdate($scope.goodsId, 0, "firstBuy");//0代表关闭 firstBuy代表这首充开启关闭
            } else {
                $scope.messagesData.messagesTitle = "load.promptTitle";
                $scope.messagesData.messagesBody = "load.promptBody";
                $("#messagesModal").modal('show');
            }
        };
        //判断是否有权限
        checkAuthority(3, updateL);//1：添，2：删，3：改，4：查 执行函数
    };

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
     商品配方修改（跳转）
     */
    $scope.updateClicked = function () {
        var data = $(this);
        var update = function () {
            index = data[0].good.goodsId;
            $scope.COMMONALITY.goodsId = index;
            $location.path("/billingGoodsEdit/view/" + index + "/update/" + $scope.currentPage);
        };
        //判断是否有权限
        checkAuthority(3, update);//1：添，2：删，3：改，4：查 执行函数

    };
    ///*
    // 刷新查询页面，调用查询方法
    // */
    //billingGoodsFnService.billingGoodsFindByName($scope.billingGoodsSearch, 1, 10).success(function (response) {
    //    console.log(response);
    //    $scope.goods = response.rows;
    //    $scope.pageCount = response.page.pageCount;
    //    $scope.$apply();
    //});


    if ($routeParams.pageNumber != null) {
        /**
         调用查询方法，并传最新的currentPage
         */
        /*
         调用查询方法，并传最新的currentPage
         */
        billingGoodsFnService.billingGoodsFindByName($scope.billingGoodsSearch, $routeParams.pageNumber, 10).success(function (response) {
            $scope.goods = response.rows;
            $scope.pageCount = response.page.pageCount;
            $scope.currentPage = $routeParams.pageNumber;
            $scope.$apply();
        });
    } else {
        /*
         刷新查询页面，调用查询方法
         */
        billingGoodsFnService.billingGoodsFindByName($scope.billingGoodsSearch, 1, 10).success(function (response) {
            console.log(response);
            $scope.goods = response.rows;
            $scope.pageCount = response.page.pageCount;
            $scope.$apply();
        });
    }

    /**
     * 全选
     * @param chk
     */
    $scope.allCheckList = function (chk, data) {
        $scope.chk = chk;
        console.log(data);
        if (chk == true) {
            for (var i = 0; i < data.length; i++) {
                $scope.goodsId += data[i].goodsId + ",";
            }
        } else {
            $scope.goodsId = "";
        }
        console.log($scope.goodsId);
    };
    /*
     分页
     */
    $scope.onPageChange = function () {
        $scope.chk = false;
        $(".allCheckData").prop("checked", false);
        // ajax request to load data
        console.log($scope.currentPage);
        /*
         调用查询方法，并传最新的currentPage
         */
        billingGoodsFnService.billingGoodsFindByName($scope.billingGoodsSearch, $scope.currentPage, 10).success(function (response) {
            $scope.goods = response.rows;
            $scope.pageCount = response.page.pageCount;
            $scope.$apply();
        });
    };

    /*
     更改区服的选择状态，并保存相应的id
     */
    $scope.check = function (val, chk) {
        var data = $(this);
        if (chk == true) {
            $scope.goodsId += data[0].good.goodsId + ",";
        } else {
            $scope.goodsId = $scope.goodsId.replace(data[0].good.goodsId + ",", "");
        }

    };

    /*
     删除角色confirm
     */
    $scope.delete = function () {
        var deleteL = function () {
            if ($scope.goodsId != "") {
                $scope.messagesConfirm.title = "load.messagesConfirmTitleByDelete";
                $scope.messagesConfirm.body = "load.messagesConfirmBodyByDelete";
                $("#messagesConfirm").modal('show');
            } else {
                $scope.messagesData.messagesTitle = "load.promptTitle";
                $scope.messagesData.messagesBody = "load.promptBody";
                $("#messagesModal").modal('show');
            }
        };
        //判断是否有权限
        checkAuthority(2, deleteL);//1：添，2：删，3：改，4：查 执行函数


    };

    /*
     删除角色确认
     */
    $scope.confirm = function () {
        $scope.chk = false;
        $(".allCheckData").prop("checked", false);
        /*
         调用删除方法
         */
        billingGoodsFnService.goodsDelete($scope.goodsId).success(function () {
            $scope.messagesData.messagesTitle = "load.messagesTitleByDelete";
            $scope.messagesData.messagesBody = "load.messagesBodyByDeleteToSuccess";
            $("#messagesModal").modal('show');
            /*
             调用查询方法，并传最新的currentPage
             */
            billingGoodsFnService.billingGoodsFindByName($scope.billingGoodsSearch, $scope.currentPage, 10).success(function (response) {
                $scope.goods = response.rows;
                $scope.pageCount = response.page.pageCount;
                $scope.$apply();
            });
            $scope.goodsId = "";
        }).error(function () {
            $scope.messagesData.messagesTitle = "load.messagesTitleByDelete";
            $scope.messagesData.messagesBody = "load.messagesBodyByDeleteToFailure";
            $("#messagesModal").modal('show');
            $scope.goodsId = "";
            /*
             调用查询方法，并传最新的currentPage
             */
            billingGoodsFnService.billingGoodsFindByName($scope.billingGoodsSearch, $scope.currentPage, 10).success(function (response) {
                $scope.goods = response.rows;
                $scope.pageCount = response.page.pageCount;
                $scope.$apply();
            });
        });
        ;
    };

    /**
     一键首充策略（弹框）
     */
    $scope.firstBuyAdd = function () {
        if ($scope.gameTransfer != -1 && $scope.gameTransfer != null && $scope.gameTransfer != undefined && $scope.gameTransfer != "") {
            $location.path("/firstBuyPolicy/view");
        } else {
            $scope.messagesData.messagesTitle = "load.messagesTitleByFirstBuyPolicy";
            $scope.messagesData.messagesBody = "load.messagesBodyByDeleteToFirstBuyPolicy";
            $("#messagesModal").modal('show');
        }

    };


    //$scope.firstBuy = {};//首充集合
    //$scope.firstBuy.operation = "*";//操作方式默认为*
    //$scope.firstBuy.operateCount = 1;//倍数设置默认为一倍
    ///*
    // 一键首充策略（弹框）
    // */
    //$scope.firstBuyAdd = function () {
    //    if ($scope.goodsId != "") {
    //        $("#firstBuyModal").modal("show");
    //    } else {
    //        $scope.messagesData.messagesTitle = "load.promptTitle";
    //        $scope.messagesData.messagesBody = "load.promptBody";
    //        $("#messagesModal").modal('show');
    //    }
    //
    //
    //}
    ///*
    // 监听首充一键配置
    // */
    //$scope.firstBuyForm = function () {
    //    $scope.firstBuy.goodsId = $scope.goodsId;
    //    billingGoodsFnService.firstBuyAdd($scope.firstBuy).success(function () {
    //        $scope.messagesData.messagesTitle = "load.configurationResult";
    //        $scope.messagesData.messagesBody = "load.messagesBodyByOneFirstToSuccess";
    //        $("#firstBuyModal").modal("hide");
    //        $("#messagesModal").modal('show');
    //        /*
    //         调用查询方法，并传最新的currentPage
    //         */
    //        billingGoodsFnService.billingGoodsFindByName($scope.billingGoodsSearch, $scope.currentPage, 10).success(function (response) {
    //            $scope.goods = response.rows;
    //            $scope.pageCount = response.page.pageCount;
    //            $scope.$apply();
    //        })
    //        $scope.goodsId = "";
    //    }).error(function () {
    //        $scope.messagesData.messagesTitle = "load.configurationResult";
    //        $scope.messagesData.messagesBody = "load.messagesBodyByOneFirstToFailure";
    //        $("#firstBuyModal").modal("hide");
    //        $("#messagesModal").modal('show');
    //        $scope.goodsId = "";
    //        /*
    //         调用查询方法，并传最新的currentPage
    //         */
    //        billingGoodsFnService.billingGoodsFindByName($scope.billingGoodsSearch, $scope.currentPage, 10).success(function (response) {
    //            $scope.goods = response.rows;
    //            $scope.pageCount = response.page.pageCount;
    //            $scope.$apply();
    //        });
    //    });
    //    ;
    //}
    //
    //$scope.packageTypeList = [{id: 1, code: "宝石"}, {id: 2, code: "道具"}];
    //$scope.package = {};
    //$scope.goodsItemsIds = "";//道具包id集合
    //$scope.package.packageValue = 0;//宝石数量初始化
    ///*
    // 一键配置奖励包（弹框）
    // */
    //$scope.packageAdd = function () {
    //    if ($scope.goodsId != "") {
    //        $("#packageModal").modal("show");
    //    } else {
    //        $scope.messagesData.messagesTitle = "load.promptTitle";
    //        $scope.messagesData.messagesBody = "load.promptBody";
    //        $("#messagesModal").modal('show');
    //    }
    //
    //}

    //$scope.packageTypeChange = function (packageType) {
    //    if (packageType == 1) {
    //        $scope.package.packageType = packageType;
    //        $scope.goodsItemsIds = "";
    //        $(".itemCheck").checked = false;
    //        $(".packageValue").show();
    //        $(".goodsItems").hide();
    //    } else if (packageType == 2) {
    //        $scope.package.packageValue = 0;
    //        $scope.package.packageType = packageType;
    //        $(".packageValue").hide();
    //        $(".goodsItems").show();
    //    } else {
    //        $(".packageValue").hide();
    //        $(".goodsItems").hide();
    //    }
    //}


    ///*
    // 保存goodsItemid
    // */
    //$scope.itemCheck = function (val, chk) {
    //    var data = $(this);
    //    if (chk == true) {
    //        $scope.goodsItemsIds += data[0].good.goodsId + ",";
    //    } else {
    //        $scope.goodsItemsIds = $scope.goodsItemsIds.replace(data[0].good.goodsId + ",", "");
    //    }
    //
    //};

    //$scope.packageForm = function () {
    //    $scope.package.goodsId = $scope.goodsId;
    //    billingGoodsFnService.packageAdd($scope.package).success(function () {
    //        $scope.messagesData.messagesTitle = "load.configurationResult";
    //        $scope.messagesData.messagesBody = "load.messagesBodyByOneKeyToSuccess";
    //        $("#packageModal").modal("hide");
    //        $("#messagesModal").modal('show');
    //        /*
    //         调用查询方法，并传最新的currentPage
    //         */
    //        billingGoodsFnService.billingGoodsFindByName($scope.billingGoodsSearch, $scope.currentPage, 10).success(function (response) {
    //            $scope.goods = response.rows;
    //            $scope.pageCount = response.page.pageCount;
    //            $scope.$apply();
    //        })
    //        $scope.goodsId = "";
    //    }).error(function () {
    //        $scope.messagesData.messagesTitle = "load.configurationResult";
    //        $scope.messagesData.messagesBody = "load.messagesBodyByOneKeyToFailure";
    //        $("#packageModal").modal("hide");
    //        $("#messagesModal").modal('show');
    //        $scope.goodsId = "";
    //        /*
    //         调用查询方法，并传最新的currentPage
    //         */
    //        billingGoodsFnService.billingGoodsFindByName($scope.billingGoodsSearch, $scope.currentPage, 10).success(function (response) {
    //            $scope.goods = response.rows;
    //            $scope.pageCount = response.page.pageCount;
    //            $scope.$apply();
    //        });
    //    });
    //}

    /**
     查询表单监听
     */
    $scope.searchButtonClicked = function () {
        /*
         调用查询方法，并传最新的currentPage
         */
        billingGoodsFnService.billingGoodsFindByName($scope.billingGoodsSearch, $scope.currentPage, 10).success(function (response) {
            $scope.goods = response.rows;
            $scope.pageCount = response.page.pageCount;
            $scope.$apply();
        });

    }
});