/**
 * 活动添加controllers文件
 *   author：钟亮
 * @type {module|*}
 */
adminApp.config(function ($routeProvider) {
    $routeProvider.when('/gameActivityConEdit/view', {
        templateUrl: ctx + '/operation/gameActivityCon/gameActivityConEdit',
        controller: 'gameActivityConEditController'
    }).when('/gameActivityConEdit/view/:id/:judge/:pageNumber', {
        templateUrl: ctx + '/operation/gameActivityCon/gameActivityConEdit',
        controller: 'gameActivityConEditController'
    });
});


/**
 公告编辑controller
 全局：$scope
 */
adminApp.controller('gameActivityConEditController', function ($scope, gameActivityConFnService, gameAreaFnService, $location, $routeParams, $rootScope, uniformFnService, gameToyTypeFnService, customFormService) {

        $scope.areaLists = [];//区服集合
        $scope.areaId = [];//区服id集合
        $scope.areaCode = [];//区服name集合
        $scope.messagesData = {};//设置提示框model数据集合
        $scope.activityConGameList = $scope.gameTransfer;//初始化gameId，与全局game同步
        $scope.gameAreasData = [];//区服查询条件集合
        $scope.gameAreasData.gameId = $scope.gameTransfer;//初始化gameId，与全局game同步
        $scope.activityConDatas = {};//活动对象
        var check = -1;//判断是否成功，url参数 1，是修改成功，0是修改失败，2是添加成功，3是添加失败，-1是失效
        //初始化菜单栏
        $rootScope.menuBarData.menuBarThreeName = "GameActivityEdit";
        $rootScope.menuBarData.menuBarTitle = "GameActivityEdit";

        $scope.uniFormData = {};//下拉框选项集合key对象初始化
        $scope.uniFormData.key = "operation.item.itemType";//初始化动态下拉框的key
        $scope.gameToyTypeSearch = {};//初始化查询条件对象
        $scope.gameToyTypeSearch.gameId = $scope.gameTransfer;//初始化查询gameId
        $scope.gameToyTypeSearch.itemTypeName = "";
        $scope.gameToyTypeSearch.itemCode = "";
        $scope.choiceItemType = "";//选中的物品类型
        $scope.choiceGameId = $scope.gameTransfer;
        $scope.placeholderItemName = "load.placeholderItemName";//道具名称搜索国际化
        $scope.placeholderItemCode = "load.placeholderItemCode";//道具code搜索国际化
        var check = -1;//判断是否成功，url参数 1，是修改成功，0是修改失败，2是添加成功，3是添加失败，-1是失效
        $scope.typePd = $routeParams.judge;

        //获取最新的活动任务id
        $.ajax({
            url: ctx + '/operation/gameActivityCon/getTaskId',
            success: function (data) {
                $scope.gameActivityconTaskId = Number(data) + 1;//活动任务id
            }, async: false
        });

        $scope.typeChange = function () {
            if ($scope.activityConDatas.type == 2) {
                $scope.gameActivityconTaskId = 0;
                $scope.clientTaskCount = 0;
                $scope.activityConDatas.taskItem = [{
                    taskId: 99,
                    taskType: "",
                    taskValue: [{itemType: "", itemId: "", itemName: "", value: "", extraData: []}],
                    clientCount: 0,
                    clientContent: {canDescReply: false}
                }];
            } else if ($scope.activityConDatas.type == 1) {
                //获取最新的活动任务id
                $.ajax({
                    url: ctx + '/operation/gameActivityCon/getTaskId',
                    success: function (data) {
                        $scope.gameActivityconTaskId = Number(data) + 1;//活动任务id
                    }, async: false
                });

                $scope.clientTaskCount = 0;
                $scope.activityConDatas.taskItem = [{
                    taskId: $scope.gameActivityconTaskId,
                    taskType: "",
                    taskValue: [{itemType: "", itemId: "", itemName: "", value: "", extraData: []}],
                    clientCount: 0,
                    clientContent: {canDescReply: false}
                }];
            }


        };

        //var activityJson = [
        //    {
        //        "taskId": 100018,
        //        "taskType": 16,
        //        taskValue: [
        //            {
        //                "itemType": 101,
        //                "itemId": 20057,
        //                "itemName": "胡子小姐魂魄",
        //                "value": 20,
        //                "extraData": []
        //            }, {
        //                "itemType": 101,
        //                "itemId": 10020,
        //                "itemName": "体力药剂",
        //                "value": 2,
        //                "extraData": []
        //            }, {
        //                "itemType": 101,
        //                "itemId": 10120,
        //                "itemName": "绿色金属箱",
        //                "value": 5,
        //                "extraData": []
        //            }, {
        //                "itemType": 1,
        //                "itemId": 0,
        //                "itemName": "",
        //                "value": 100000,
        //                "extraData": []
        //            }, {
        //                "itemType": 1,
        //                "itemId": 0,
        //                "itemName": "",
        //                "value": 100000,
        //                "extraData": []
        //            }
        //        ],
        //        "clientCount": 3,
        //        "clientContent": {"canDescReply": true},
        //        "taskTerm1": 10000,
        //        "taskName": "累积充值奖励",
        //        "taskDesc": "累积充值100元"
        //    }
        //];

        if ($routeParams.judge != null && $routeParams.judge != 'update' && $routeParams.judge != 'null' && $routeParams.judge != 5  && $routeParams.judge != 6) {
            var data = {};
            if ($routeParams.judge == 1) {
                $scope.clientTaskCount = 7;
                $scope.clientTaskContent = new Object();
                data = {
                    gameActivityId: null,
                    activityName: "累积充值",
                    beginTime: 1504540800000,
                    endTime: 1504627200000,
                    activityJson: [
                        {
                            "taskId": $scope.gameActivityconTaskId,
                            "taskType": 16,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 20057,
                                    "itemName": "胡子小姐魂魄",
                                    "value": 20,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10020,
                                    "itemName": "体力药剂",
                                    "value": 2,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10120,
                                    "itemName": "绿色金属箱",
                                    "value": 5,
                                    "extraData": []
                                }, {
                                    "itemType": 1,
                                    "itemId": 0,
                                    "itemName": "",
                                    "value": 100000,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 4,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 10000,
                            "taskName": "累积充值奖励",
                            "taskDesc": "累积充值100元"
                        },
                        {
                            "taskId": $scope.gameActivityconTaskId + 1,
                            "taskType": 16,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 20057,
                                    "itemName": "胡子小姐魂魄",
                                    "value": 20,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10121,
                                    "itemName": "蓝色金属箱",
                                    "value": 10,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 31001,
                                    "itemName": "强化石Lv1",
                                    "value": 10,
                                    "extraData": []
                                }, {
                                    "itemType": 1,
                                    "itemId": 0,
                                    "itemName": "",
                                    "value": 150000,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 4,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 30000,
                            "taskName": "累积充值奖励",
                            "taskDesc": "累积充值300元"
                        },
                        {
                            "taskId": $scope.gameActivityconTaskId + 2,
                            "taskType": 16,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 20012,
                                    "itemName": "白雪公主魂魄",
                                    "value": 40,
                                    "extraData": []
                                }, {
                                    "itemType": 5,
                                    "itemId": 0,
                                    "itemName": "",
                                    "value": 1000,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 31002,
                                    "itemName": "强化石Lv2",
                                    "value": 5,
                                    "extraData": []
                                }, {
                                    "itemType": 1,
                                    "itemId": 0,
                                    "itemName": "",
                                    "value": 200000,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 4,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 50000,
                            "taskName": "累积充值奖励",
                            "taskDesc": "累积充值500元"
                        },
                        {
                            "taskId": $scope.gameActivityconTaskId + 3,
                            "taskType": 16,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 20012,
                                    "itemName": "白雪公主魂魄",
                                    "value": 60,
                                    "extraData": []
                                }, {
                                    "itemType": 6,
                                    "itemId": 0,
                                    "itemName": "",
                                    "value": 1000,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10164,
                                    "itemName": "紫色装备材料箱",
                                    "value": 20,
                                    "extraData": []
                                }, {
                                    "itemType": "1",
                                    "itemId": 0,
                                    "itemName": "",
                                    "value": 500000,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 4,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 100000,
                            "taskName": "累积充值奖励",
                            "taskDesc": "累积充值1000元"
                        },
                        {
                            "taskId": $scope.gameActivityconTaskId + 4,
                            "taskType": 16,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 10197,
                                    "itemName": "超级角色魂魄箱",
                                    "value": 3,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10027,
                                    "itemName": "金色尼玛币",
                                    "value": 20,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10164,
                                    "itemName": "紫色装备材料箱",
                                    "value": 30,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10007,
                                    "itemName": "高级潜力果",
                                    "value": 5,
                                    "extraData": []
                                }],
                            "clientCount": 4,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 300000,
                            "taskName": "累积充值奖励",
                            "taskDesc": "累积充值3000元"
                        },
                        {
                            "taskId": $scope.gameActivityconTaskId + 5,
                            "taskType": 16,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 20017,
                                    "itemName": "鸟不拉屎大王魂魄",
                                    "value": 60,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10027,
                                    "itemName": "金色尼玛币",
                                    "value": 30,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10164,
                                    "itemName": "紫色装备材料箱",
                                    "value": 40,
                                    "extraData": []
                                }, {
                                    "itemType": "1",
                                    "itemId": 0,
                                    "itemName": "",
                                    "value": 1000000,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 4,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 500000,
                            "taskName": "累积充值奖励",
                            "taskDesc": "累积充值5000元"
                        },
                        {
                            "taskId": $scope.gameActivityconTaskId + 6,
                            "taskType": 16,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 20015,
                                    "itemName": "福禄小金刚魂魄",
                                    "value": 60,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10027,
                                    "itemName": "金色尼玛币",
                                    "value": 50,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10144,
                                    "itemName": "橙色强化石箱",
                                    "value": 20,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10119,
                                    "itemName": "4阶礼物箱",
                                    "value": 50,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 4,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 800000,
                            "taskName": "累积充值奖励",
                            "taskDesc": "累积充值8000元"
                        },
                        {
                            "taskId": $scope.gameActivityconTaskId + 7,
                            "taskType": 16,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 20015,
                                    "itemName": "福禄小金刚魂魄",
                                    "value": 80,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 20017,
                                    "itemName": "鸟不拉屎大王魂魄",
                                    "value": 80,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10144,
                                    "itemName": "橙色强化石箱",
                                    "value": 30,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10119,
                                    "itemName": "4阶礼物箱",
                                    "value": 80,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 4,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 1200000,
                            "taskName": "累积充值奖励",
                            "taskDesc": "累积充值12000元"
                        }
                    ],
                    areaId: "",
                    channelType: -1,
                    createTime: new Date(1504524484000),
                    gameId: 27,
                    redundancy: null,
                    type: "1"
                };
                $scope.clientTaskContent.canDescReply = true;
            } else if ($routeParams.judge == 2) {
                $scope.clientTaskCount = 7;
                $scope.clientTaskContent = new Object();
                data = {
                    gameActivityId: null,
                    activityName: "单笔充值",
                    beginTime: 1504540800000,
                    endTime: 1504627200000,
                    activityJson: [
                        {
                            "taskId": $scope.gameActivityconTaskId,
                            "taskType": 23,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 10124,
                                    "itemName": "1阶角色碎片包",
                                    "value": 5,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10001,
                                    "itemName": "中级饮品",
                                    "value": 10,
                                    "extraData": []
                                }, {
                                    "itemType": 1,
                                    "itemId": 0,
                                    "itemName": "",
                                    "value": 50000,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 3,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 600,
                            "taskTerm2": 1,
                            "taskName": "单笔充值奖励",
                            "taskDesc": "单笔充值6元"
                        },
                        {
                            "taskId": $scope.gameActivityconTaskId + 1,
                            "taskType": 23,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 10163,
                                    "itemName": "蓝色装备材料箱",
                                    "value": 10,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10117,
                                    "itemName": "2阶礼物箱",
                                    "value": 10,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10129,
                                    "itemName": "绿色宝箱",
                                    "value": 5,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 3,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 2500,
                            "taskTerm2": 1,
                            "taskName": "单笔充值奖励",
                            "taskDesc": "单笔充值25元"
                        },
                        {
                            "taskId": $scope.gameActivityconTaskId + 2,
                            "taskType": 23,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 10213,
                                    "itemName": "1阶觉醒材料箱",
                                    "value": 10,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10026,
                                    "itemName": "银色尼玛币",
                                    "value": 20,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10130,
                                    "itemName": "蓝色宝箱",
                                    "value": 5,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 3,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 5000,
                            "taskTerm2": 1,
                            "taskName": "单笔充值奖励",
                            "taskDesc": "单笔充值50元"
                        },
                        {
                            "taskId": $scope.gameActivityconTaskId + 3,
                            "taskType": 23,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 10173,
                                    "itemName": "天赋点礼包",
                                    "value": 10,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10117,
                                    "itemName": "2阶礼物箱",
                                    "value": 20,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10130,
                                    "itemName": "蓝色宝箱",
                                    "value": 5,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 3,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 9800,
                            "taskTerm2": 1,
                            "taskName": "单笔充值奖励",
                            "taskDesc": "单笔充值98元"
                        },
                        {
                            "taskId": $scope.gameActivityconTaskId + 4,
                            "taskType": 23,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 10214,
                                    "itemName": "2阶觉醒材料箱",
                                    "value": 20,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10164,
                                    "itemName": "紫色装备材料箱",
                                    "value": 10,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10131,
                                    "itemName": "紫色宝箱",
                                    "value": 5,
                                    "extraData": []
                                }],
                            "clientCount": 3,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 12800,
                            "taskTerm2": 1,
                            "taskName": "单笔充值奖励",
                            "taskDesc": "单笔充值128元"
                        },
                        {
                            "taskId": $scope.gameActivityconTaskId + 5,
                            "taskType": 23,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 10027,
                                    "itemName": "金色尼玛币",
                                    "value": 10,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10118,
                                    "itemName": "3阶礼物箱",
                                    "value": 10,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10131,
                                    "itemName": "紫色宝箱",
                                    "value": 5,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 3,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 19800,
                            "taskTerm2": 1,
                            "taskName": "单笔充值奖励",
                            "taskDesc": "单笔充值198元"
                        },
                        {
                            "taskId": $scope.gameActivityconTaskId + 6,
                            "taskType": 23,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 10197,
                                    "itemName": "超级角色魂魄箱",
                                    "value": 1,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10165,
                                    "itemName": "橙色装备材料箱",
                                    "value": 20,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10132,
                                    "itemName": "橙色宝箱",
                                    "value": 5,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 3,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 32800,
                            "taskTerm2": 1,
                            "taskName": "单笔充值奖励",
                            "taskDesc": "单笔充值328元"
                        },
                        {
                            "taskId": $scope.gameActivityconTaskId + 7,
                            "taskType": 23,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 10197,
                                    "itemName": "超级角色魂魄箱",
                                    "value": 2,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10027,
                                    "itemName": "金色尼玛币",
                                    "value": 20,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10132,
                                    "itemName": "橙色宝箱",
                                    "value": 5,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 3,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 64800,
                            "taskTerm2": 1,
                            "taskName": "单笔充值奖励",
                            "taskDesc": "单笔充值648元"
                        }
                    ],
                    areaId: "",
                    channelType: -1,
                    createTime: new Date(1504524484000),
                    gameId: 27,
                    redundancy: null,
                    type: "1"
                };
                $scope.clientTaskContent.canDescReply = true;
            } else if ($routeParams.judge == 3) {
                $scope.clientTaskCount = 6;
                $scope.clientTaskContent = new Object();
                data = {
                    gameActivityId: null,
                    activityName: "累积消费",
                    beginTime: 1504540800000,
                    endTime: 1504627200000,
                    activityJson: [
                        {
                            "taskId": $scope.gameActivityconTaskId,
                            "taskType": 17,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 10124,
                                    "itemName": "1阶角色碎片包",
                                    "value": 5,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10003,
                                    "itemName": "超级饮品",
                                    "value": 5,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 2,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 1000,
                            "taskName": "累积消费奖励",
                            "taskDesc": "累积消费1000元宝"
                        },
                        {
                            "taskId": $scope.gameActivityconTaskId + 1,
                            "taskType": 17,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 10026,
                                    "itemName": "银色尼玛币",
                                    "value": 20,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10003,
                                    "itemName": "超级饮品",
                                    "value": 10,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 2,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 3000,
                            "taskName": "累积消费奖励",
                            "taskDesc": "累积消费3000元宝"
                        },
                        {
                            "taskId": $scope.gameActivityconTaskId + 2,
                            "taskType": 17,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 10125,
                                    "itemName": "2阶角色碎片包",
                                    "value": 5,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10021,
                                    "itemName": "音符",
                                    "value": 20,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10119,
                                    "itemName": "4阶礼物箱",
                                    "value": 5,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 3,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 5000,
                            "taskName": "累积消费奖励",
                            "taskDesc": "累积消费5000元宝"
                        },
                        {
                            "taskId": $scope.gameActivityconTaskId + 3,
                            "taskType": 17,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 10027,
                                    "itemName": "金色尼玛币",
                                    "value": 5,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10030,
                                    "itemName": "角色重生丹",
                                    "value": 5,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10119,
                                    "itemName": "4阶礼物箱",
                                    "value": 5,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 3,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 8000,
                            "taskName": "累积消费奖励",
                            "taskDesc": "累积消费8000元"
                        },
                        {
                            "taskId": $scope.gameActivityconTaskId + 4,
                            "taskType": 17,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 10126,
                                    "itemName": "3阶角色碎片包",
                                    "value": 5,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10007,
                                    "itemName": "高级潜力果",
                                    "value": 5,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10003,
                                    "itemName": "超级饮品",
                                    "value": 10,
                                    "extraData": []
                                }],
                            "clientCount": 3,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 10000,
                            "taskName": "累积消费奖励",
                            "taskDesc": "累积消费10000元宝"
                        },
                        {
                            "taskId": $scope.gameActivityconTaskId + 5,
                            "taskType": 17,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 10027,
                                    "itemName": "金色尼玛币",
                                    "value": 10,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10030,
                                    "itemName": "角色重生丹",
                                    "value": 5,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10003,
                                    "itemName": "超级饮品",
                                    "value": 10,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 3,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 15000,
                            "taskName": "累积消费奖励",
                            "taskDesc": "累积消费15000元宝"
                        },
                        {
                            "taskId": $scope.gameActivityconTaskId + 6,
                            "taskType": 17,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 10027,
                                    "itemName": "金色尼玛币",
                                    "value": 10,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10026,
                                    "itemName": "银色尼玛币",
                                    "value": 50,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10119,
                                    "itemName": "4阶礼物箱",
                                    "value": 5,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 3,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 20000,
                            "taskName": "累积消费奖励",
                            "taskDesc": "累积消费20000元宝"
                        }
                    ],
                    areaId: "",
                    channelType: -1,
                    createTime: new Date(1504524484000),
                    gameId: 27,
                    redundancy: null,
                    type: "1"
                };
                $scope.clientTaskContent.canDescReply = true;
            } else if ($routeParams.judge == 4) {
                $scope.clientTaskCount = 5;
                $scope.clientTaskContent = new Object();
                data = {
                    gameActivityId: null,
                    activityName: "每日充值",
                    beginTime: 1504540800000,
                    endTime: 1504627200000,
                    activityJson: [
                        {
                            "taskId": 99,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 11057,
                                    "itemName": "胡子小姐觉醒材料",
                                    "value": 100,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 1,
                            "clientContent": {"canDescReply": true}
                        },
                        {
                            "taskId": 1,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 20005,
                                    "itemName": "三娃魂魄",
                                    "value": 20,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10001,
                                    "itemName": "中级饮品",
                                    "value": 20,
                                    "extraData": []
                                }, {
                                    "itemType": 1,
                                    "itemId": 0,
                                    "itemName": "",
                                    "value": 100000,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 3,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 2500
                        },
                        {
                            "taskId": 2,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 20005,
                                    "itemName": "三娃魂魄",
                                    "value": 20,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10001,
                                    "itemName": "中级饮品",
                                    "value": 30,
                                    "extraData": []
                                }, {
                                    "itemType": 1,
                                    "itemId": 0,
                                    "itemName": "",
                                    "value": 150000,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 3,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 2500
                        },
                        {
                            "taskId": 3,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 20005,
                                    "itemName": "三娃魂魄",
                                    "value": 20,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10001,
                                    "itemName": "中级饮品",
                                    "value": 40,
                                    "extraData": []
                                }, {
                                    "itemType": 1,
                                    "itemId": 0,
                                    "itemName": "",
                                    "value": 200000,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 3,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 2500
                        },
                        {
                            "taskId": 4,
                            "taskType": 17,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 10027,
                                    "itemName": "金色尼玛币",
                                    "value": 5,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10002,
                                    "itemName": "高级饮品",
                                    "value": 20,
                                    "extraData": []
                                }, {
                                    "itemType": 1,
                                    "itemId": 0,
                                    "itemName": "",
                                    "value": 250000,
                                    "extraData": []
                                }],
                            "clientCount": 3,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 2500
                        },
                        {
                            "taskId": 5,
                            taskValue: [
                                {
                                    "itemType": 101,
                                    "itemId": 10027,
                                    "itemName": "金色尼玛币",
                                    "value": 10,
                                    "extraData": []
                                }, {
                                    "itemType": 101,
                                    "itemId": 10002,
                                    "itemName": "高级饮品",
                                    "value": 30,
                                    "extraData": []
                                }, {
                                    "itemType": 1,
                                    "itemId": 0,
                                    "itemName": "",
                                    "value": 300000,
                                    "extraData": []
                                }
                            ],
                            "clientCount": 3,
                            "clientContent": {"canDescReply": true},
                            "taskTerm1": 2500
                        }
                    ],
                    areaId: "",
                    channelType: -1,
                    createTime: new Date(1504524484000),
                    gameId: 27,
                    redundancy: null,
                    type: "2"
                };
                $scope.clientTaskContent.canDescReply = true;
            }


            $scope.activityConDatas = data;
            //$scope.channelType = data.channelType.toString();
            if (data.channelType == -1) {
                $scope.channelType = null;
            } else {
                $scope.channelType = data.channelType.toString();
            }
            $scope.activityConGameList = data.gameId;
            $scope.activityConDatas.taskItem = data.activityJson;
            $scope.activityConDatas.type = data.type.toString();
            for (var i = 0; i < $scope.activityConDatas.taskItem.length; i++) {
                if ($scope.activityConDatas.taskItem[i].taskType == 16 || $scope.activityConDatas.taskItem[i].taskType == 23 || $scope.activityConDatas.type == 2) {
                    if ($scope.activityConDatas.taskItem[i].taskTerm1 != null && $scope.activityConDatas.taskItem[i].taskTerm1 != "" && $scope.activityConDatas.taskItem[i].taskTerm1 != undefined) {
                        $scope.activityConDatas.taskItem[i].taskTerm1 = $scope.activityConDatas.taskItem[i].taskTerm1 / 100;
                    }

                }
                if ($scope.activityConDatas.taskItem[i].taskType != null && $scope.activityConDatas.taskItem[i].taskType != "" && $scope.activityConDatas.taskItem[i].taskType != undefined) {
                    $scope.activityConDatas.taskItem[i].taskType = $scope.activityConDatas.taskItem[i].taskType.toString();
                }


                for (var j = 0; j < $scope.activityConDatas.taskItem[i].taskValue.length; j++) {
                    $scope.activityConDatas.taskItem[i].taskValue[j].itemId = $scope.activityConDatas.taskItem[i].taskValue[j].itemId.toString();
                    $scope.activityConDatas.taskItem[i].taskValue[j].itemType = $scope.activityConDatas.taskItem[i].taskValue[j].itemType.toString();
                    //$scope.activityConDatas.taskItem[i].taskValue[j].value = $scope.activityConDatas.taskItem[i].taskValue[j].value
                }
            }
            $scope.gameAreasData.gameId = data.gameId;
            $scope.activityConDatas.beginTime = $scope.format(data.beginTime, "yyyy-MM-dd HH:mm:ss");
            $scope.activityConDatas.endTime = $scope.format(data.endTime, "yyyy-MM-dd HH:mm:ss");

            //var time1 = $scope.format(new Date(), "yyyy-MM-dd HH:mm:ss");
            //var date1 = new Date(time1.replace("-", "/"));
            //var date2 = new Date($scope.activityConDatas.beginTime.replace("-", "/"));
            //if (date2 < date1) {
            //    $(".enableChanges").hide();
            //    $(".reveal").show();
            //    $("#timeDesc").show();
            //} else {
            //    $(".enableChanges").show();
            //    $(".reveal").hide();
            //    $("#timeDesc").hide();
            //}

        } else {
            $scope.clientTaskCount = 0;
            $scope.clientTaskContent = new Object();

            $scope.activityConDatas.taskItem = [{
                taskId: $scope.gameActivityconTaskId,
                taskType: "",
                taskValue: [{itemType: "", itemId: "", itemName: "", value: "", extraData: []}],
                clientCount: 0,
                clientContent: {canDescReply: false}
            }];
            // 初始化时由于只有1条回复，所以不允许删除
            $scope.clientTaskContent.canDescReply = false;

        }


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

        $scope.uniFormData.key = "operation.activity.task";//初始化动态下拉框的key
        /**
         *加载下拉框
         */
        uniformFnService.uniGetKey($scope.uniFormData).success(function (response) {
            $scope.taskTypeItem = JSON.parse(response.itemJson);
            $scope.$apply();
        });

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
        $scope.itemTypeChange = function (itemType, index, parentIndex) {
            $scope.choiceItemType = itemType;
            $scope.activityConDatas.taskItem[parentIndex].taskValue[index].itemId = "";
            $scope.activityConDatas.taskItem[parentIndex].taskValue[index].itemName = "";
            if ($scope.activityConDatas.gameId != undefined && itemType != undefined && $scope.activityConDatas.gameId != "") {
                $scope.KEY = "formobject.list." + $scope.activityConDatas.gameId + "." + itemType;//配置自定义表单的key
                /*
                 查询自定义表单
                 */
                customFormService.customFormByKey({key: $scope.KEY}).success(function (response) {
                    $scope.activityConDatas.taskItem[parentIndex].taskValue[index].extraData = JSON.parse(response.formJson);
                    $scope.$apply();
                });

            } else {
                $scope.activityConDatas.taskItem[parentIndex].taskValue[index].extraData = [];
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
        $scope.itemSelected = function (itemType, index, parentIndex) {
            console.log(itemType + ":" + $scope.choiceGameId);
            if (itemType != "" && itemType != undefined && $scope.choiceGameId != "" && $scope.choiceGameId != undefined) {
                $scope.choiceCheck = index;
                $scope.choiceParentCheck = parentIndex;
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
            $scope.activityConDatas.taskItem[$scope.choiceParentCheck].taskValue[$scope.choiceCheck].itemId = data[0].gameToyType.itemCode;
            $scope.activityConDatas.taskItem[$scope.choiceParentCheck].taskValue[$scope.choiceCheck].itemName = data[0].gameToyType.itemName;
            $("#itemModal").modal("hide");
        };

        /**
         *根据全局game，site查询区服
         */
        gameAreaFnService.getGameAreasBySiteType($scope.gameAreasData).success(function (response) {
            $scope.areaLists = response;
            $scope.$apply();
        });

        /**
         * 渠道类型改变
         */
        $scope.channelTypeChange = function () {
            $scope.gameAreasData.siteType = $scope.channelType;
            $scope.areaId = [];//区服id集合
            $scope.areaCode = [];//区服name集合
            $scope.areaData = [];
            /**
             *根据全局game，site查询区服
             */
            gameAreaFnService.getGameAreasBySiteType($scope.gameAreasData).success(function (response) {
                $scope.areaLists = response;
                $scope.$apply();
            });
        };

        /**
         *gameId改变时，查询自定义表单
         */
        $scope.resourceGameListChange = function (gameId) {
            $scope.gameAreasData.gameId = gameId;
            //
            $scope.clientTaskCount = 0;
            $scope.activityConDatas.taskItem = [{
                taskId: $scope.gameActivityconTaskId,
                taskType: "",
                taskValue: [{itemType: "", itemId: "", itemName: "", value: "", extraData: []}],
                clientCount: 0,
                clientContent: {canDescReply: false}
            }];
            // 初始化时由于只有1条回复，所以不允许删除
            $scope.clientTaskContent.canDescReply = false;
            $scope.areaId = [];//区服id集合
            $scope.areaCode = [];//区服name集合
            $scope.areaData = [];
            //
            /*
             根据全局game，site查询区服
             */
            gameAreaFnService.getGameAreasBySiteType($scope.gameAreasData).success(function (response) {
                $scope.areaLists = response;
                $scope.$apply();
            });

            $scope.choiceGameId = gameId;
            if ($scope.activityConDatas.gameId != undefined && $scope.activityConDatas.itemType != undefined) {
                $scope.KEY = "formobject.list." + gameId + "." + $scope.activityConDatas.itemType;//配置自定义表单的key
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


        //$scope.clientCount = 0;
        //$scope.clientContent = new Object();
        //$scope.activityConDatas.taskValue = [{
        //    itemType: "",
        //    itemId: "",
        //    itemName: "",
        //    value: "",
        //    extraData: []
        //}, {itemType: "", itemId: "", itemName: "", value: "", extraData: []}];
        //// 初始化时由于只有1条回复，所以不允许删除
        //$scope.clientContent.canDescReply = true;

        /**
         * 添加活动
         */
        $scope.clientTaskAdd = function () {
            if ($routeParams.judge == "update") {
                $scope.messagesData.messagesTitle = "load.messagesTitleByForm";
                $scope.messagesData.messagesBody = "load.gameActivityConAddExplain";
                $("#messagesModal").modal('show');
            } else {
                if ($scope.activityConDatas.type == 2) {
                    if ($scope.clientTaskCount < 7) {
                        $scope.clientTaskCount++;
                        $scope.activityConDatas.taskItem.splice($scope.clientTaskCount, 0, {
                            taskId: $scope.gameActivityconTaskId + 1,
                            taskType: "",
                            taskValue: [{itemType: "", itemId: "", itemName: "", value: "", extraData: []}],
                            clientCount: 0,
                            clientContent: {canDescReply: false}
                        });
                        $scope.gameActivityconTaskId = $scope.gameActivityconTaskId + 1;
                        // 增加后允许删除
                        $scope.clientTaskContent.canDescReply = true;
                    } else {
                        $scope.messagesData.messagesTitle = "load.messagesTitleByForm";
                        $scope.messagesData.messagesBody = "load.DayActivityTip";
                        $("#messagesModal").modal('show');
                    }
                } else {
                    if ($scope.clientTaskCount < 9) {
                        $scope.clientTaskCount++;
                        $scope.activityConDatas.taskItem.splice($scope.clientTaskCount, 0, {
                            taskId: $scope.gameActivityconTaskId + 1,
                            taskType: "",
                            taskValue: [{itemType: "", itemId: "", itemName: "", value: "", extraData: []}],
                            clientCount: 0,
                            clientContent: {canDescReply: false}
                        });
                        $scope.gameActivityconTaskId = $scope.gameActivityconTaskId + 1;
                        // 增加后允许删除
                        $scope.clientTaskContent.canDescReply = true;
                    } else {
                        $scope.messagesData.messagesTitle = "load.messagesTitleByForm";
                        $scope.messagesData.messagesBody = "load.messagesBodyByFormShi";
                        $("#messagesModal").modal('show');
                    }
                }


            }
        };

        /**
         * 减少活动填写条例
         */
        $scope.clientTaskDelete = function ($index) {
            if ($routeParams.judge == "update") {
                $scope.messagesData.messagesTitle = "load.messagesTitleByForm";
                $scope.messagesData.messagesBody = "load.gameActivityConUpdateExplain";
                $("#messagesModal").modal('show');
            } else {
                // 如果回复数大于1，删除被点击回复
                if ($scope.activityConDatas.taskItem.length > 1) {
                    $scope.activityConDatas.taskItem.splice($index, 1);
                    $scope.clientTaskCount--;
                    if ($scope.activityConDatas.type == 2) {
                        $scope.gameActivityconTaskId = -1;
                        for (var i = 0; i < $scope.activityConDatas.taskItem.length; i++) {
                            if ($scope.gameActivityconTaskId == -1) {
                                $scope.activityConDatas.taskItem[i].taskId = 99;
                                $scope.gameActivityconTaskId = $scope.gameActivityconTaskId + 1;
                            } else {
                                $scope.activityConDatas.taskItem[i].taskId = $scope.gameActivityconTaskId + 1;
                                $scope.gameActivityconTaskId = $scope.gameActivityconTaskId + 1;
                            }

                        }
                    } else if ($scope.activityConDatas.type == 1) {
                        //获取最新的活动任务id
                        $.ajax({
                            url: ctx + '/operation/gameActivityCon/getTaskId',
                            success: function (data) {
                                $scope.gameActivityconTaskId = Number(data) + 1;//活动任务id
                            }, async: false
                        });
                        for (var i = 0; i < $scope.activityConDatas.taskItem.length; i++) {
                            $scope.activityConDatas.taskItem[i].taskId = $scope.gameActivityconTaskId + 1;
                            $scope.gameActivityconTaskId = $scope.gameActivityconTaskId + 1;
                        }
                    }

                }
                // 如果回复数为1，不允许删除
                if ($scope.activityConDatas.taskItem.length == 1) {
                    $scope.clientTaskContent.canDescReply = false;
                }
            }

        };

        /**
         * 增加物品填写条例
         */
        $scope.clientAdd = function (index) {
            if ($scope.activityConDatas.taskItem[index].clientCount < 4) {
                $scope.activityConDatas.taskItem[index].clientCount++;
                $scope.activityConDatas.taskItem[index].taskValue.splice($scope.activityConDatas.taskItem[index].clientCount, 0, {
                    itemType: "",
                    itemId: "",
                    itemName: "",
                    value: "",
                    extraData: []
                });
                // 增加后允许删除
                $scope.activityConDatas.taskItem[index].clientContent.canDescReply = true;
            } else {
                $scope.messagesData.messagesTitle = "load.messagesTitleByForm";
                $scope.messagesData.messagesBody = "load.messagesBodyByForm";
                $("#messagesModal").modal('show');
            }
        };

        /**
         * 减少物品填写条例
         */
        $scope.clientDelete = function (index, item) {
            // 如果回复数大于1，删除被点击回复
            if ($scope.activityConDatas.taskItem[index].taskValue.length > 1) {
                $scope.activityConDatas.taskItem[index].taskValue.splice(item, 1);
                $scope.activityConDatas.taskItem[index].clientCount--;
            }
            // 如果回复数为1，不允许删除
            if ($scope.activityConDatas.taskItem[index].taskValue.length == 1) {
                $scope.activityConDatas.taskItem[index].clientContent.canDescReply = false;
            }
        };

        ///**
        // 绑定game,site select，change事件
        // */
        //$scope.resourceGameListChange = function (id) {
        //
        //};

        /**
         根据全局game，site查询区服
         */
        if ($scope.gameAreasData.gameId == undefined) {
            $scope.gameAreasData.gameId = -1;
        }

        ///**
        // * 获取区服列表
        // */
        //$.ajax({
        //    url: ctx + '/platform/gameArea/getGameAreasById',
        //    data: {"gameId": $scope.gameAreasData.gameId},
        //    success: function (data) {
        //        $scope.areaLists = data;
        //    },
        //    async: false
        //});


        /**
         * 如果跳转参数是update，根据id查询活动对象
         */
        if ($routeParams.judge == "update") {
            /*
             根据id查询公告
             */
            gameActivityConFnService.gameActivityConById($routeParams.id).success(function (data) {
                console.log(data);
                $scope.activityConDatas = data;
                //$scope.channelType = data.channelType.toString();
                if (data.channelType == -1) {
                    $scope.channelType = null;
                } else {
                    $scope.channelType = data.channelType.toString();
                }
                $scope.activityConGameList = data.gameId;
                $scope.activityConDatas.taskItem = JSON.parse(data.activityJson);
                $scope.activityConDatas.type = data.type.toString();
                for (var i = 0; i < $scope.activityConDatas.taskItem.length; i++) {
                    if ($scope.activityConDatas.taskItem[i].taskType == 16 || $scope.activityConDatas.taskItem[i].taskType == 23 || $scope.activityConDatas.type == 2) {
                        if ($scope.activityConDatas.taskItem[i].taskTerm1 != null && $scope.activityConDatas.taskItem[i].taskTerm1 != "" && $scope.activityConDatas.taskItem[i].taskTerm1 != undefined) {
                            $scope.activityConDatas.taskItem[i].taskTerm1 = $scope.activityConDatas.taskItem[i].taskTerm1 / 100;
                        }

                    }
                    if ($scope.activityConDatas.taskItem[i].taskType != null && $scope.activityConDatas.taskItem[i].taskType != "" && $scope.activityConDatas.taskItem[i].taskType != undefined) {
                        $scope.activityConDatas.taskItem[i].taskType = $scope.activityConDatas.taskItem[i].taskType.toString();
                    }


                    for (var j = 0; j < $scope.activityConDatas.taskItem[i].taskValue.length; j++) {
                        $scope.activityConDatas.taskItem[i].taskValue[j].itemId = $scope.activityConDatas.taskItem[i].taskValue[j].itemId.toString();
                        $scope.activityConDatas.taskItem[i].taskValue[j].itemType = $scope.activityConDatas.taskItem[i].taskValue[j].itemType.toString();
                        //$scope.activityConDatas.taskItem[i].taskValue[j].value = $scope.activityConDatas.taskItem[i].taskValue[j].value
                    }
                }
                $scope.gameAreasData.gameId = data.gameId;
                $scope.activityConDatas.beginTime = $scope.format(data.beginTime, "yyyy-MM-dd HH:mm:ss");
                $scope.activityConDatas.endTime = $scope.format(data.endTime, "yyyy-MM-dd HH:mm:ss");

                var time1 = $scope.format(new Date(), "yyyy-MM-dd HH:mm:ss");
                var date1 = new Date(time1.replace("-", "/"));
                var date2 = new Date($scope.activityConDatas.beginTime.replace("-", "/"));
                if (date2 < date1) {
                    $(".enableChanges").hide();
                    $(".reveal").show();
                    $("#timeDesc").show();
                } else {
                    $(".enableChanges").show();
                    $(".reveal").hide();
                    $("#timeDesc").hide();
                }
                //$.ajax({
                //    url: ctx + '/platform/gameArea/getGameAreasById',
                //    data: {"gameId": $scope.gameAreasData.gameId},
                //    success: function (data) {
                //        $scope.areaLists = data;
                //    },
                //    async: false
                //});
                $scope.gameAreasData.siteType = $scope.channelType;
                $scope.gameAreasData.gameId = data.gameId;
                /*
                 根据全局game，site查询区服
                 */
                gameAreaFnService.getGameAreasBySiteType($scope.gameAreasData).success(function (response) {
                    $scope.areaLists = response;
                    $scope.$apply();
                    /*
                     区服 初始化
                     */
                    var areaCheckbox = $(".checkList span");
                    if (data.areaId.substring(0, 1) == ",") {
                        //字符串以,开头，去掉"
                        data.areaId = data.areaId.substr(1, data.areaId.length - 1)
                    }
                    if (data.areaId.substring(data.areaId.length - 1, data.areaId.length) == ",") {
                        //字符串以,结尾，去掉"
                        data.areaId = data.areaId.substr(0, data.areaId.length - 1)
                    }
                    var areaIds = data.areaId.split(",");
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
                });

            });
        } else if ($routeParams.judge == 5 || $routeParams.judge == 6) {
            /*
             根据id查询公告
             */
            gameActivityConFnService.getGameActivityTemplate().success(function (data) {
                console.log(data);
                if (data == "") {
                    $scope.clientTaskCount = 0;
                    $scope.clientTaskContent = new Object();

                    $scope.activityConDatas.taskItem = [{
                        taskId: $scope.gameActivityconTaskId,
                        taskType: "",
                        taskValue: [{itemType: "", itemId: "", itemName: "", value: "", extraData: []}],
                        clientCount: 0,
                        clientContent: {canDescReply: false}
                    }];
                    // 初始化时由于只有1条回复，所以不允许删除
                    $scope.clientTaskContent.canDescReply = false;
                } else {
                    $scope.activityConDatas = data;
                    //$scope.channelType = data.channelType.toString();
                    if (data.channelType == -1) {
                        $scope.channelType = null;
                    } else {
                        $scope.channelType = data.channelType.toString();
                    }
                    $scope.activityConGameList = data.gameId;
                    $scope.activityConDatas.taskItem = JSON.parse(data.activityJson);
                    $scope.activityConDatas.type = data.type.toString();
                    for (var i = 0; i < $scope.activityConDatas.taskItem.length; i++) {
                        if ($scope.activityConDatas.taskItem[i].taskType == 16 || $scope.activityConDatas.taskItem[i].taskType == 23 || $scope.activityConDatas.type == 2) {
                            if ($scope.activityConDatas.taskItem[i].taskTerm1 != null && $scope.activityConDatas.taskItem[i].taskTerm1 != "" && $scope.activityConDatas.taskItem[i].taskTerm1 != undefined) {
                                $scope.activityConDatas.taskItem[i].taskTerm1 = $scope.activityConDatas.taskItem[i].taskTerm1 / 100;
                            }

                        }
                        if ($scope.activityConDatas.taskItem[i].taskType != null && $scope.activityConDatas.taskItem[i].taskType != "" && $scope.activityConDatas.taskItem[i].taskType != undefined) {
                            $scope.activityConDatas.taskItem[i].taskType = $scope.activityConDatas.taskItem[i].taskType.toString();
                        }


                        for (var j = 0; j < $scope.activityConDatas.taskItem[i].taskValue.length; j++) {
                            $scope.activityConDatas.taskItem[i].taskValue[j].itemId = $scope.activityConDatas.taskItem[i].taskValue[j].itemId.toString();
                            $scope.activityConDatas.taskItem[i].taskValue[j].itemType = $scope.activityConDatas.taskItem[i].taskValue[j].itemType.toString();
                            //$scope.activityConDatas.taskItem[i].taskValue[j].value = $scope.activityConDatas.taskItem[i].taskValue[j].value
                        }
                    }
                    $scope.gameAreasData.gameId = data.gameId;
                    $scope.activityConDatas.beginTime = $scope.format(data.beginTime, "yyyy-MM-dd HH:mm:ss");
                    $scope.activityConDatas.endTime = $scope.format(data.endTime, "yyyy-MM-dd HH:mm:ss");

                    var time1 = $scope.format(new Date(), "yyyy-MM-dd HH:mm:ss");
                    var date1 = new Date(time1.replace("-", "/"));
                    var date2 = new Date($scope.activityConDatas.beginTime.replace("-", "/"));
                    if (date2 < date1) {
                        $(".enableChanges").hide();
                        $(".reveal").show();
                        $("#timeDesc").show();
                    } else {
                        $(".enableChanges").show();
                        $(".reveal").hide();
                        $("#timeDesc").hide();
                    }
                    //$.ajax({
                    //    url: ctx + '/platform/gameArea/getGameAreasById',
                    //    data: {"gameId": $scope.gameAreasData.gameId},
                    //    success: function (data) {
                    //        $scope.areaLists = data;
                    //    },
                    //    async: false
                    //});
                    $scope.gameAreasData.siteType = $scope.channelType;
                    $scope.gameAreasData.gameId = data.gameId;
                    /*
                     根据全局game，site查询区服
                     */
                    gameAreaFnService.getGameAreasBySiteType($scope.gameAreasData).success(function (response) {
                        $scope.areaLists = response;
                        $scope.$apply();
                        /*
                         区服 初始化
                         */
                        var areaCheckbox = $(".checkList span");
                        if (data.areaId.substring(0, 1) == ",") {
                            //字符串以,开头，去掉"
                            data.areaId = data.areaId.substr(1, data.areaId.length - 1)
                        }
                        if (data.areaId.substring(data.areaId.length - 1, data.areaId.length) == ",") {
                            //字符串以,结尾，去掉"
                            data.areaId = data.areaId.substr(0, data.areaId.length - 1)
                        }
                        var areaIds = data.areaId.split(",");
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
                    });
                }


            });
        }

        /**
         * 监听Add提交表单
         */
        $scope.gameActivityConEditForm = function () {
            var areaids = "";
            for (var i = 0; i < $scope.areaId.length; i++) {
                areaids += $scope.areaId[i] + ",";
            }
            var taskIdsStr = "";
            for (var i = 0; i < $scope.activityConDatas.taskItem.length; i++) {
                delete $scope.activityConDatas.taskItem[i].$$hashKey;
                $scope.activityConDatas.taskItem[i].taskType = Number($scope.activityConDatas.taskItem[i].taskType);
                if ($scope.activityConDatas.taskItem[i].taskType == 16 || $scope.activityConDatas.taskItem[i].taskType == 23 || $scope.activityConDatas.type == 2) {
                    $scope.activityConDatas.taskItem[i].taskTerm1 = $scope.activityConDatas.taskItem[i].taskTerm1 * 100;
                }
                if ($scope.activityConDatas.taskItem[i].taskId == 99) {
                    delete $scope.activityConDatas.taskItem[i].taskType;
                    delete $scope.activityConDatas.taskItem[i].taskName;
                    delete $scope.activityConDatas.taskItem[i].taskDesc;
                    delete $scope.activityConDatas.taskItem[i].taskTerm1;
                    delete $scope.activityConDatas.taskItem[i].taskTerm2;
                }
                if (taskIdsStr == "") {
                    taskIdsStr = $scope.activityConDatas.taskItem[i].taskId;
                } else {
                    taskIdsStr = taskIdsStr + ";" + $scope.activityConDatas.taskItem[i].taskId;
                }
                if ($scope.activityConDatas == 2) {
                    delete $scope.activityConDatas.taskItem[i].taskType;
                    delete $scope.activityConDatas.taskItem[i].taskName;
                    delete $scope.activityConDatas.taskItem[i].taskDesc;
                }
                for (var j = 0; j < $scope.activityConDatas.taskItem[i].taskValue.length; j++) {
                    $scope.activityConDatas.taskItem[i].taskValue[j].itemId = Number($scope.activityConDatas.taskItem[i].taskValue[j].itemId);
                    $scope.activityConDatas.taskItem[i].taskValue[j].itemType = Number($scope.activityConDatas.taskItem[i].taskValue[j].itemType);
                    $scope.activityConDatas.taskItem[i].taskValue[j].value = Number($scope.activityConDatas.taskItem[i].taskValue[j].value);
                    delete $scope.activityConDatas.taskItem[i].taskValue[j].$$hashKey;
                }
            }
            $scope.activityConDatas.taskIds = taskIdsStr;
            $scope.activityConDatas.gameId = $scope.activityConGameList;
            $scope.activityConDatas.areaId = areaids;
            $scope.activityConDatas.beginTime = new Date($scope.activityConDatas.beginTime);
            $scope.activityConDatas.endTime = new Date($scope.activityConDatas.endTime);
            $scope.activityConDatas.taskJson = JSON.stringify($scope.activityConDatas.taskItem);
            $scope.activityConDatas.lastTaskId = $scope.activityConDatas.taskItem[$scope.activityConDatas.taskItem.length - 1].taskId;
            $scope.activityConDatas.channelType = $scope.channelType;
            delete $scope.activityConDatas.taskItem;
            delete $scope.activityConDatas.activityJson;
            if ($routeParams.judge == "update") {
                $scope.activityConDatas.createTime = new Date($scope.activityConDatas.createTime);
                gameActivityConFnService.gameActivityConUpdate($scope.activityConDatas).success(function (data) {
                    check = 1;
                    if (data != null && data != "") {
                        check = data;
                    }
                    $location.path("/gameActivityCon/view/" + check + "/" + $routeParams.pageNumber);
                    $scope.$apply();
                    $("#messagesModal").modal('show');
                }).error(function () {
                    check = 0;
                    $location.path("/gameActivityCon/view/" + check + "/" + $routeParams.pageNumber);
                    $scope.$apply();
                    $("#messagesModal").modal('show');
                });
            } else if ($routeParams.judge == 5) {

                delete $scope.activityConDatas.createTime;
                gameActivityConFnService.templateEdit($scope.activityConDatas).success(function (data) {
                    check = 1;
                    if (data != null && data != "") {
                        check = data;
                    }
                    $location.path("/gameActivityCon/view/" + check + "/" + $routeParams.pageNumber);
                    $scope.$apply();
                    $("#messagesModal").modal('show');
                }).error(function () {
                    check = 0;
                    $location.path("/gameActivityCon/view/" + check + "/" + $routeParams.pageNumber);
                    $scope.$apply();
                    $("#messagesModal").modal('show');
                });
            } else {
                delete $scope.activityConDatas.createTime;
                gameActivityConFnService.gameActivityConAdd($scope.activityConDatas).success(function (data) {
                    check = 2;
                    if (data != null && data != "") {
                        check = data;
                    }
                    $location.path("/gameActivityCon/view/" + check + "/" + $routeParams.pageNumber);
                    $scope.$apply();
                    $("#messagesModal").modal('show');
                }).error(function () {
                    check = 3;
                    $location.path("/gameActivityCon/view/" + check + "/" + $routeParams.pageNumber);
                    $scope.$apply();
                    $("#messagesModal").modal('show');
                });
            }


            console.log($scope.activityConDatas);
        };


        /*
         选择区服（弹框）
         */
        $scope.areaClick = function () {
            //if ($routeParams.judge == "update") {
            //    $scope.messagesData.messagesTitle = "load.menuTipTitle";
            //    $scope.messagesData.messagesBody = "load.areaCheckTip";
            //    $("#messagesModal").modal('show');
            //}else{
            if ($scope.activityConGameList == "" || $scope.activityConGameList == null || $scope.activityConGameList == undefined) {
                $scope.messagesData.messagesTitle = "load.menuTipTitle";
                $scope.messagesData.messagesBody = "load.gameActivityConSiteTypeExplain";
                $("#messagesModal").modal('show');
            } else {
                $("#areaAddModal").modal('show');
            }
            //}


        }
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
            console.log($scope.areaId + "/////////" + $scope.areaCode);
        };

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
            //$scope.areaModel = true;
            for (var i = 0; i < v.length; i++) {
                idData.push(v[i].id);
                codeData.push(v[i].areaName);
            }
            $scope.areaId = idData;
            $scope.areaCode = codeData;
            console.log($scope.areaId + "/////////" + $scope.areaCode);
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
        };

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
            console.log($scope.areaId + "/////////" + $scope.areaCode);
        }


        /*
         监听areaAdd表单
         */
        $scope.areaAdd = function () {
            $("#areaAddModal").modal('hide');
            $scope.areaData = $scope.areaCode;

        };

        ///*
        // 区服 checkbox单击操作
        // */
        //$scope.updateAreaSelection = function ($event, id, code) {
        //    var checkbox = $event.target;
        //    var action = (checkbox.checked ? 'add' : 'remove');//判断是否选中
        //    updateAreaSelected(action, id, code);//选中判断逻辑方法
        //}


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


        ///**
        // 全选，遍历checkboxModel，正向操作
        // */
        //$scope.areaAll = function (v) {
        //    var checkbox = $(".areaChildren");
        //    for (var i = 0; i < checkbox.length; i++) {
        //        checkbox[i].checked = true;
        //    }
        //    var idData = [];
        //    var codeData = [];
        //    $scope.areaModel = true;
        //    for (var i = 0; i < v.length; i++) {
        //        idData.push(v[i].id);
        //        codeData.push(v[i].areaName);
        //    }
        //    $scope.areaId = idData;
        //    $scope.areaCode = codeData;
        //};
        //
        ///**
        // 全不选
        // */
        //$scope.deselectAllArea = function () {
        //    var checkbox = $(".areaChildren");
        //    for (var i = 0; i < checkbox.length; i++) {
        //        checkbox[i].checked = false;
        //    }
        //    $scope.areaId = [];
        //    $scope.areaCode = [];
        //}
        //
        ///**
        // 反选
        // */
        //$scope.areaVersa = function () {
        //    var checkbox = $(".areaChildren");
        //    /*
        //     遍历checkbox，并反向操作
        //     */
        //    for (var i = 0; i < checkbox.length; i++) {
        //        if (checkbox[i].checked) {
        //
        //            checkbox[i].checked = false;
        //            var idx = $scope.areaId.indexOf(Number(checkbox[i].name));
        //            $scope.areaId.splice(idx, 1);
        //            $scope.areaCode.splice(idx, 1);
        //
        //        } else {
        //            $scope.areaId.push(Number(checkbox[i].name));
        //            $scope.areaCode.push(checkbox[i].id);
        //            checkbox[i].checked = true;
        //
        //        }
        //
        //    }
        //}


    }
);