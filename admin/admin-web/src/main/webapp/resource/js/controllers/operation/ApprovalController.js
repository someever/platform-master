/**
 * 物品审批controllers文件
 *   author：钟亮
 * @type {module|*}
 */
adminApp.config(function ($routeProvider) {
    $routeProvider.when('/approval/view', {
        templateUrl: ctx + '/operation/article/approvalInit',
        controller: 'approvalController'
    });
});


/**
 物品申请controller
 全局：$scope
 */
adminApp.controller('approvalController', function ($scope, articleService, $location, $routeParams, $rootScope) {

    $scope.articleSearch = {};//初始化查询条件对象
    $scope.articleSearch.gameId = $scope.gameTransfer;//初始化查询gameId
    $scope.articleSearch.typeCode = 2;//2代表审批
    $scope.articleId = "";//区服id字符串
    $scope.messagesData = {};//设置提示框model数据集合
    $scope.messagesConfirm = {};//设置确认框model数据集合
    $scope.approvalData = {};//审批对象
    $scope.placeholderSearch = "load.ApprovalFromSearch";//搜索提示
    $scope.approvalStatusCode = "";//审批状态（审批时判断用）
    //初始化菜单栏
    $rootScope.menuBarData.menuBarThreeName = "";
    $rootScope.menuBarData.menuBarTitle = "ApprovalManage";
    $scope.approvalData.timingCheck = 2;//默认不定时发送
    /**
     *刷新数据、list
     */
    function approvalSelect(approvalSearch, pageIndex, pageSize) {
        /*
         调用查询方法，并传最新的currentPage
         */
        articleService.articleFindByName(approvalSearch, pageIndex, pageSize).success(function (response) {
            $scope.applicationDatas = response.rows;
            $scope.pageCount = response.page.pageCount;
            $scope.$apply();
        });
    }

    //定时发送
    $scope.valueChange = function (check) {
        if (check == 1) {
            $(".timingTimeDiv").show();
        } else {
            $(".timingTimeDiv").hide();
        }

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

    /**
     调用查询方法，并传最新的currentPage
     */
    approvalSelect($scope.articleSearch, 1, 10);

    /**
     * 搜索单击事件
     */
    $scope.searchButtonClicked = function () {
        /*
         调用查询方法，并传最新的currentPage
         */
        approvalSelect($scope.articleSearch, 1, 10);
    }

    /**
     * 状态更改change事件
     */
    $scope.statusChange = function () {
        /*
         调用查询方法，并传最新的currentPage
         */
        approvalSelect($scope.articleSearch, 1, 10);
    };

    /**
     * 全选
     * @param chk
     */
    $scope.allCheckList = function (chk, data) {
        $scope.chk = chk;
        console.log(data);
        if (chk == true) {
            for (var i = 0; i < data.length; i++) {
                $scope.approvalStatusCode += data[i].id + ",";
            }
        } else {
            $scope.approvalStatusCode = "";
        }
        console.log($scope.approvalStatusCode);
    };

    /**
     *分页
     */
    $scope.onPageChange = function () {
        $scope.chk = false;
        $(".allCheckData").prop("checked", false);
        /*
         调用查询方法，并传最新的currentPage
         */
        approvalSelect($scope.articleSearch, $scope.currentPage, 10);
    };

    /**
     *更改区服的选择状态，并保存相应的id
     */
    $scope.check = function (val, chk) {
        var data = $(this);
        if (chk == true) {
            $scope.articleId += data[0].application.mailOrder.id + ",";
            if (data[0].application.approvalStatus != 2) {
                $scope.approvalStatusCode += data[0].application.approvalStatus + ",";
            }

        } else {
            $scope.articleId = $scope.articleId.replace(data[0].application.mailOrder.id + ",", "");
            if (data[0].application.approvalStatus != 2) {
                $scope.approvalStatusCode = $scope.approvalStatusCode.replace(data[0].application.approvalStatus + ",", "");
            }

        }

    };


    /**
     * 一键审核
     */
    $scope.pocApprovalFor = function () {
        var pocApprovalFor = function () {
            if ($scope.articleId != "") {
                if ($scope.approvalStatusCode.length == 0) {
                    $scope.approvalData.articleId = $scope.articleId;
                    $("#approvalAddModal").modal("show");
                } else {
                    $scope.messagesData.messagesTitle = "load.promptTitle";
                    $scope.messagesData.messagesBody = "load.ApprovaeMessageBodyByApprova";
                    $("#messagesModal").modal('show');
                }

            } else {
                $scope.messagesData.messagesTitle = "load.promptTitle";
                $scope.messagesData.messagesBody = "load.promptBody";
                $("#messagesModal").modal('show');
            }
        };
        //判断是否有权限
        checkAuthority(3, pocApprovalFor);//1：添，2：删，3：改，4：查 执行函数
    }


    /**
     *修改（调转）
     */
    $scope.updateClicked = function (id) {
        var update = function () {
            $location.path("/articleEdit/view/" + id + "/checkSee/" + $scope.currentPage);
        };
        //判断是否有权限
        checkAuthority(3, update);//1：添，2：删，3：改，4：查 执行函数

    };

    /**
     *审批弹窗
     */
    $scope.approvalFor = function (id) {
        var approvalFor = function () {
            $scope.approvalData.articleId = id;
            $("#approvalAddModal").modal("show");
        };
        //判断是否有权限
        checkAuthority(3, approvalFor);//1：添，2：删，3：改，4：查 执行函数

    }

    /**
     *审批查看
     */
    $scope.approvalForEye = function (id) {
        articleService.approveGetById(id).success(function (data) {
            $scope.approvalDataEye = data;
            $scope.approvalDataEye.timingTime = $scope.format($scope.approvalDataEye.timingTime, "yyyy-MM-dd HH:mm:ss");
            if (data.timingCheck == 1) {
                $(".timingTimeDivEye").show();
            } else {
                $(".timingTimeDivEye").hide();
            }
            $scope.$apply();
        });
        $("#approvalEyeModal").modal("show");
    }

    /**
     *审批提交
     */
    $scope.approvalFromAdd = function (status) {
        $scope.chk = false;
        $(".allCheckData").prop("checked", false);
        $scope.approvalData.status = status;//3代表审批成功,4代表打回
        if ($scope.approvalData.timingCheck == 1) {
            $scope.approvalData.timingTime = new Date($scope.approvalData.timingTime);
        } else {
            $scope.approvalData.timingTime = new Date();
        }

        articleService.approvalFromAdd($scope.approvalData).success(function () {
            $scope.messagesData.messagesTitle = "load.ApprovalMessagesTitle";
            $scope.messagesData.messagesBody = "load.ApprovalMessagesBodyToSuccess";
            $("#messagesModal").modal('show');
            $scope.articleId = "";
            /*
             调用查询方法，并传最新的currentPage
             */
            approvalSelect($scope.articleSearch, $scope.currentPage, 10);
            $scope.approvalData = {};
            $scope.approvalData.timingCheck = 2;
            $(".timingTimeDiv").hide();
            $("#approvalAddModal").modal("hide");
        }).error(function () {
            $scope.messagesData.messagesTitle = "load.ApprovalMessagesTitle";
            $scope.messagesData.messagesBody = "load.ApprovalMessagesBodyToFailure";
            $("#messagesModal").modal('show');
            /*
             调用查询方法，并传最新的currentPage
             */
            approvalSelect($scope.articleSearch, $scope.currentPage, 10);
            $scope.articleId = "";
            $scope.approvalData = {};
            $scope.approvalData.timingCheck = 2;
            $(".timingTimeDiv").hide();
            $("#approvalAddModal").modal("hide");
        });

    }
});