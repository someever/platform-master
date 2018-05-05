/**
 * 区服管理controllers文件
 *   author：钟亮
 * @type {module|*}
 */
adminApp.config(function ($routeProvider) {
    $routeProvider.when('/gameArea/view', {
        templateUrl: ctx + '/platform/gameArea/gameAreaInit',
        controller: 'gameAreaController'
    }).when("/gameArea/view/:check/:pageNumber", {
        templateUrl: ctx + '/platform/gameArea/gameAreaInit',
        controller: 'gameAreaController'
    });
});


/**
 区服管理controller
 全局：$scope
 */
adminApp.controller('gameAreaController', function ($scope, gameAreaFnService, $location, $routeParams, $rootScope, uniformFnService) {
    //初始化菜单栏
    $rootScope.menuBarData.menuBarThreeName = "";
    $rootScope.menuBarData.menuBarTitle = "GameAreaManage";
    $scope.messagesData = {};//设置提示框model数据集合
    $scope.messagesConfirm = {};//设置确认框model数据集合
    $scope.areaMessagesConfirm = {}//开启，关闭服务器确认框
    $scope.statusPocMessagesConfirm = {} //开服维护确认框
    $scope.gameAreaSearch = {};//初始化查询条件对象
    $scope.gameAreaSearch.gameId = $scope.gameTransfer;//初始化查询gameId
    $scope.gameAreaSearch.siteId = $scope.siteTransfer;//初始化查询siteId
    $scope.areaId = "";//区服id字符串
    var index = 0;//修改角色的下标
    $scope.batchArea = {};//批量修改集合
    $scope.gameAreaState = {};//修改状态条件对象
    $scope.areaIndex = 0;//区服id下标
    $scope.areaStatus = 1;
    $scope.areaTagList = [];//动态下拉框初始化
    $scope.uniFormData = {};//下拉框选项集合key对象初始化
    /**
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
    } else if ($routeParams.check == "load.region70044") {
        $scope.messagesData.messagesTitle = "load.messagesTitleByUnusual";
        $scope.messagesData.messagesBody = "load.region70044";
    }
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

    $scope.uniFormData.key = "platform.gameArea.areaTag";//初始化动态下拉框的key
    /**
     加载下拉框
     */
    uniformFnService.uniGetKey($scope.uniFormData).success(function (response) {
        $scope.areaTagList = JSON.parse(response.itemJson);
    });

    /**
     区服添加（跳转）
     */
    $scope.addButton = function () {
        var add = function () {
            $location.path("/gameAreaEdit/view/null/null/" + $scope.pageCount);
        };
        //判断是否有权限
        checkAuthority(1, add);//1：添，2：删，3：改，4：查 执行函数

    };

    /**
     刷新查询页面，调用查询方法
     */
    function gameAreaFindByNamePublic(gameAreaSearch, pageIndex, pageSize) {
        gameAreaFnService.gameAreaFindByName(gameAreaSearch, pageIndex, pageSize).success(function (response) {
            console.log(response);
            $scope.gameAreas = response.rows;
            $scope.pageCount = response.page.pageCount;
            $scope.$apply();
        });
    }

    /**
     查询表单监听
     */
    $scope.searchButtonClicked = function () {
        /*
         刷新查询页面，调用查询方法
         */
        gameAreaFindByNamePublic($scope.gameAreaSearch, 1, 10);
    }

    /**
     批量修改
     */
    $scope.bulkEditing = function () {
        var bulkEditing = function () {
            if ($scope.areaId != "") {
                $("#BulkUpdate").modal("show");
            } else {
                $scope.messagesData.messagesTitle = "load.promptTitle";
                $scope.messagesData.messagesBody = "load.promptBody";
                $("#messagesModal").modal('show');
            }
        };
        //判断是否有权限
        checkAuthority(3, bulkEditing);//1：添，2：删，3：改，4：查 执行函数

    }

    /**
     copy
     */
    $scope.CopyArea = function () {
        var CopyArea = function () {
            if ($scope.areaId != "") {
                var dataId = $scope.areaId.split(",");
                console.log(dataId);
                $scope.COMMONALITY.areaId = dataId[0];
                $location.path("/gameAreaEdit/view/" + dataId[0] + "/copy");
            } else {
                $scope.messagesData.messagesTitle = "load.promptTitle";
                $scope.messagesData.messagesBody = "load.promptBody";
                $("#messagesModal").modal('show');
            }
        };
        //判断是否有权限
        checkAuthority(1, CopyArea);//1：添，2：删，3：改，4：查 执行函数

    }

    /**
     修改（调转）
     */
    $scope.updateClicked = function (id) {
        var data = $(this);
        var CopyArea = function () {
            index = id;
            $scope.COMMONALITY.areaId = index;
            $location.path("/gameAreaEdit/view/" + index + "/update/" + $scope.currentPage);
        };
        //判断是否有权限
        checkAuthority(3, CopyArea);//1：添，2：删，3：改，4：查 执行函数
    };

    /**
     更改区服的选择状态，并保存相应的id
     */
    $scope.check = function (val, chk) {
        var data = $(this);
        if (chk == true) {
            $scope.areaId += data[0].gameArea.id + ",";
        } else {
            $scope.areaId = $scope.areaId.replace(data[0].gameArea.id + ",", "");
        }

    };
    if ($routeParams.pageNumber != null) {
        /**
         刷新查询页面，调用查询方法
         */
        gameAreaFnService.gameAreaFindByName($scope.gameAreaSearch, $routeParams.pageNumber, 10).success(function (response) {
            console.log(response);
            $scope.gameAreas = response.rows;
            $scope.pageCount = response.page.pageCount;
            $scope.currentPage = $routeParams.pageNumber;
            $scope.$apply();
        });
    } else {
        /**
         刷新查询页面，调用查询方法
         */
        gameAreaFindByNamePublic($scope.gameAreaSearch, 1, 10);
    }
    /**
     开启、关闭状态工具方法 ids:id集合 state 1代表开启，0代表关闭
     */
    function stateTool(ids, state) {
        $scope.gameAreaState.ids = ids;
        $scope.gameAreaState.state = state;
        gameAreaFnService.gameAreaStateUpdate($scope.gameAreaState).success(function () {
            $scope.messagesData.messagesTitle = "load.messagesTitleByUpdate";
            $scope.messagesData.messagesBody = "load.messagesBodyByUpdateToSuccess";
            $("#messagesModal").modal('show');
            /*
             刷新查询页面，调用查询方法
             */
            gameAreaFindByNamePublic($scope.gameAreaSearch, $scope.currentPage, 10);

            $scope.areaId = "";
        }).error(function () {
            $scope.messagesData.messagesTitle = "load.messagesTitleByUpdate";
            $scope.messagesData.messagesBody = "load.messagesBodyByUpdateToFailure";
            $("#messagesModal").modal('show');
            /*
             刷新查询页面，调用查询方法
             */
            gameAreaFindByNamePublic($scope.gameAreaSearch, $scope.currentPage, 10);

            $scope.areaId = "";
        });
    }

    /**
     开启、关闭状态工具方法 ids:id集合 state 1代表开启，0代表关闭（服务器维护）
     */
    function serverStatusTool(ids, state) {
        $scope.gameAreaState.ids = ids;
        $scope.gameAreaState.state = state;
        gameAreaFnService.gameAreaUpdateServerStatus($scope.gameAreaState).success(function () {
            $scope.messagesData.messagesTitle = "load.messagesTitleByUpdate";
            $scope.messagesData.messagesBody = "load.messagesBodyOpenServiceToSuccess";
            $("#messagesModal").modal('show');
            /*
             刷新查询页面，调用查询方法
             */
            gameAreaFindByNamePublic($scope.gameAreaSearch, $scope.currentPage, 10);

            $scope.areaId = "";
        }).error(function () {
            $scope.messagesData.messagesTitle = "load.messagesTitleByUpdate";
            $scope.messagesData.messagesBody = "load.messagesBodyWithdrawalToFailure";
            $("#messagesModal").modal('show');
            /*
             刷新查询页面，调用查询方法
             */
            gameAreaFindByNamePublic($scope.gameAreaSearch, $scope.currentPage, 10);

            $scope.areaId = "";
        });
    }

    /**
     一键开启（服务器维护）
     */
    $scope.pocOpenService = function () {
        var pocOpenService = function () {
            if ($scope.areaId != "") {
                //stateTool($scope.areaId, 1);//1代表开启
                $scope.areaIndex = $scope.areaId;
                $scope.areaStatus = 1;//1代表开启
                $scope.statusPocMessagesConfirm.title = "load.noticeTipTitle";
                $scope.statusPocMessagesConfirm.body = "load.openServiceDescBody";
                $("#statusPocMessagesConfirm").modal('show');
            } else {
                $scope.messagesData.messagesTitle = "load.promptTitle";
                $scope.messagesData.messagesBody = "load.promptBody";
                $("#messagesModal").modal('show');
            }
        };
        //判断是否有权限
        checkAuthority(3, pocOpenService);//1：添，2：删，3：改，4：查 执行函数
    };

    /**
     一键关闭（服务器维护）
     */
    $scope.pocWithdrawal = function () {
        var pocWithdrawal = function () {
            if ($scope.areaId != "") {
                //stateTool($scope.areaId, 0);//0代表关闭
                $scope.areaIndex = $scope.areaId;
                $scope.areaStatus = 2;//2代表维护
                $scope.statusPocMessagesConfirm.title = "load.noticeTipTitle";
                $scope.statusPocMessagesConfirm.body = "load.withdrawalDescBody";
                $("#statusPocMessagesConfirm").modal('show');
            } else {
                $scope.messagesData.messagesTitle = "load.promptTitle";
                $scope.messagesData.messagesBody = "load.promptBody";
                $("#messagesModal").modal('show');
            }
        };
        //判断是否有权限
        checkAuthority(3, pocWithdrawal);//1：添，2：删，3：改，4：查 执行函数
    };

    /**
     开启（服务器维护）
     */
    $scope.openService = function () {
        var data = $(this);
        var openService = function () {
            $scope.areaIndex = data[0].gameArea.id;
            $scope.areaStatus = 1;//1代表开启
            $scope.statusPocMessagesConfirm.title = "load.noticeTipTitle";
            $scope.statusPocMessagesConfirm.body = "load.openServiceDescBody";
            $("#statusPocMessagesConfirm").modal('show');
        };
        //判断是否有权限
        checkAuthority(3, openService);//1：添，2：删，3：改，4：查 执行函数
    };

    /**
     * 开服 关服确认
     */
    $scope.statusPocConfirm = function () {
        serverStatusTool($scope.areaIndex, $scope.areaStatus);//1代表开启 2代表关闭
    };

    /**
     关闭
     */
    $scope.withdrawal = function () {
        var data = $(this);
        var withdrawal = function () {
            $scope.areaIndex = data[0].gameArea.id;
            $scope.areaStatus = 2;//2代表维护
            $scope.statusPocMessagesConfirm.title = "load.noticeTipTitle";
            $scope.statusPocMessagesConfirm.body = "load.withdrawalDescBody";
            $("#statusPocMessagesConfirm").modal('show');
        };
        //判断是否有权限
        checkAuthority(3, withdrawal);//1：添，2：删，3：改，4：查 执行函数

    };


    /**
     一键开启
     */
    $scope.pocOpen = function () {
        var pocOpen = function () {
            if ($scope.areaId != "") {
                //stateTool($scope.areaId, 1);//1代表开启
                $scope.areaIndex = $scope.areaId;
                $scope.areaStatus = 1;//1代表开启
                $scope.areaMessagesConfirm.title = "load.noticeTipTitle";
                $scope.areaMessagesConfirm.body = "load.openPocServiceDesc";
                $("#areaMessagesConfirm").modal('show');
            } else {
                $scope.messagesData.messagesTitle = "load.promptTitle";
                $scope.messagesData.messagesBody = "load.promptBody";
                $("#messagesModal").modal('show');
            }
        };
        //判断是否有权限
        checkAuthority(3, pocOpen);//1：添，2：删，3：改，4：查 执行函数


    };

    /**
     一键关闭
     */
    $scope.pocClose = function () {
        var pocClose = function () {
            if ($scope.areaId != "") {
                //stateTool($scope.areaId, 0);//0代表关闭
                $scope.areaIndex = $scope.areaId;
                $scope.areaStatus = 0;//1代表开启
                $scope.areaMessagesConfirm.title = "load.noticeTipTitle";
                $scope.areaMessagesConfirm.body = "load.closePocServiceDesc";
                $("#areaMessagesConfirm").modal('show');
            } else {
                $scope.messagesData.messagesTitle = "load.promptTitle";
                $scope.messagesData.messagesBody = "load.promptBody";
                $("#messagesModal").modal('show');
            }
        };
        //判断是否有权限
        checkAuthority(3, pocClose);//1：添，2：删，3：改，4：查 执行函数
    };

    /**
     * 刷新缓存
     */
    $scope.refreshAreaCache = function () {
        gameAreaFnService.refreshGameAreaCache().success(function () {
            $scope.messagesData.messagesTitle = "load.UpAreaMessagesTitle";
            $scope.messagesData.messagesBody = "load.messagesBodyUpAreaToSuccess";
            $("#messagesModal").modal('show');
            gameAreaFindByNamePublic($scope.gameAreaSearch, 1, 10);
        }).error(function () {
            $scope.messagesData.messagesTitle = "load.UpAreaMessagesTitle";
            $scope.messagesData.messagesBody = "load.messagesBodyUpAreaToFailure";
            $("#messagesModal").modal('show');
            /*
             刷新查询页面，调用查询方法
             */
            gameAreaFindByNamePublic($scope.gameAreaSearch, 1, 10);

        });
    }

    /**
     开启
     */
    $scope.open = function () {
        var data = $(this);
        var open = function () {
            $scope.areaIndex = data[0].gameArea.id;
            $scope.areaStatus = 1;//1代表开启
            $scope.areaMessagesConfirm.title = "load.noticeTipTitle";
            $scope.areaMessagesConfirm.body = "load.openServiceDesc";
            $("#areaMessagesConfirm").modal('show');
        };
        //判断是否有权限
        checkAuthority(3, open);//1：添，2：删，3：改，4：查 执行函数
    };

    /**
     * 开服 关服确认
     */
    $scope.areaConfirm = function () {
        stateTool($scope.areaIndex, $scope.areaStatus);//1代表开启 2代表关闭
    };

    /**
     关闭
     */
    $scope.close = function () {
        var data = $(this);
        var close = function () {
            $scope.areaIndex = data[0].gameArea.id;
            $scope.areaStatus = 0;//0代表关闭
            $scope.areaMessagesConfirm.title = "load.noticeTipTitle";
            $scope.areaMessagesConfirm.body = "load.closeServiceDesc";
            $("#areaMessagesConfirm").modal('show');
        };
        //判断是否有权限
        checkAuthority(3, close);//1：添，2：删，3：改，4：查 执行函数

    };

    /**
     * 全选
     * @param chk
     */
    $scope.allCheckList = function (chk, gameAreas) {
        $scope.chk = chk;
        console.log(gameAreas);
        if (chk == true) {
            for (var i = 0; i < gameAreas.length; i++) {
                $scope.areaId += gameAreas[i].id + ",";
            }
        } else {
            $scope.areaId = "";
        }
        console.log($scope.areaId);
    };

    /**
     分页
     */
    $scope.onPageChange = function () {
        $scope.chk = false;
        $(".allCheckData").prop("checked", false);
        /*
         调用查询方法，并传最新的currentPage
         */
        gameAreaFindByNamePublic($scope.gameAreaSearch, $scope.currentPage, 10);
    };

    /**
     监听batchForm表单
     */
    $scope.batchForms = function () {
        $scope.batchArea.ids = $scope.areaId;
        gameAreaFnService.batchUpdate($scope.batchArea).success(function () {
            $scope.messagesData.messagesTitle = "load.messagesTitleByUpdate";
            $scope.messagesData.messagesBody = "load.messagesBodyByUpdateToSuccess";
            $scope.$apply();
            $("#messagesModal").modal('show');
            $("#BulkUpdate").modal("hide");
            $scope.areaId = "";
            $scope.batchArea = {};
            /*
             调用查询方法，并传最新的currentPage
             */
            gameAreaFindByNamePublic($scope.gameAreaSearch, $scope.currentPage, 10);
        }).error(function () {
            $scope.messagesData.messagesTitle = "load.messagesTitleByUpdate";
            $scope.messagesData.messagesBody = "load.messagesBodyByUpdateToFailure";
            $scope.$apply();
            $("#messagesModal").modal('show');
            $("#BulkUpdate").modal("hide");
            /*
             调用查询方法，并传最新的currentPage
             */
            gameAreaFindByNamePublic($scope.gameAreaSearch, $scope.currentPage, 10);
        });
    };

    /**
     删除角色confirm
     */
    $scope.delete = function () {
        var deleteL = function () {
            if ($scope.areaId != "") {
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

    /**
     删除角色确认
     */
    $scope.confirm = function () {
        $scope.chk = false;
        $(".allCheckData").prop("checked", false);
        /*
         调用删除方法
         */
        gameAreaFnService.areaDelete($scope.areaId).success(function () {
            $scope.messagesData.messagesTitle = "load.messagesTitleByDelete";
            $scope.messagesData.messagesBody = "load.messagesBodyByDeleteToSuccess";
            $("#messagesModal").modal('show');
            /*
             调用查询方法，并传最新的currentPage
             */
            gameAreaFindByNamePublic($scope.gameAreaSearch, $scope.currentPage, 10);
            $scope.areaId = "";
        }).error(function () {
            $scope.messagesData.messagesTitle = "load.messagesTitleByDelete";
            $scope.messagesData.messagesBody = "load.messagesBodyByDeleteToFailure";
            $("#messagesModal").modal('show');
            /*
             调用查询方法，并传最新的currentPage
             */
            gameAreaFindByNamePublic($scope.gameAreaSearch, $scope.currentPage, 10);

            $scope.areaId = "";
        });

    };

});