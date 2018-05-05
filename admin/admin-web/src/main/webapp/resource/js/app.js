/**
 * app配置文件
 * author：钟亮
 * @type {module|*}
 */
/*
 配置全局app
 */
var adminApp = angular.module("adminApp", ['ng-pagination', 'ngRoute', 'pascalprecht.translate', 'ng.ueditor']);

adminApp.config(function ($routeProvider) {

        //$routeProvider.when('/index', {
        //    templateUrl: 'welcome',
        //    controller: 'centerController'
        //});


    })

    .config(['$translateProvider', function ($translateProvider) {

        var simpleJavaPropertiesReader = function (lines) {
            var result = {};
            var patterns = [
                /^\s*([^=:]+)\s*[:|=]\s*(.*)$/ // anything before = or : (the key), then everything unitl the end
            ];
            var quotePattern = /^"(.*)"$/;
            for (var i = 0; i < lines.length; i++) {
                for (var j = 0; j < patterns.length; j++) {
                    var match = lines[i].match(patterns[j]);
                    if (match && match[0] && match[0].length > 0) {
                        var key = match[1], value = match[2];
                        if (value && value.length > 0) {
                            var quoteMatch = value.match(quotePattern);
                            if (quoteMatch && quoteMatch[0] && quoteMatch[0].length) {
                                value = quoteMatch[1];
                            }
                        }
                        result[key] = value;
                        break;
                    }
                }
            }
            return result;
        };

        // Register a loader for the static files
        // So, the module will search missing translation tables under the specified urls.
        // Those urls are [prefix][langKey][suffix].
        $translateProvider.useStaticFilesLoader({
            prefix: 'resource/i18n/',
            suffix: '.properties',
            $http: {
                transformResponse: function (data, headersGetter, status) {
                    return simpleJavaPropertiesReader(data.split('\n'));
                }
            }
        });

        // Tell the module what language to use by default
        $translateProvider.preferredLanguage('zh_ZH');

    }]);


/*adminApp.controller('centerController', ['$scope', '$translate', function ($scope, $translate) {

 $scope.setLang = function (langKey) {
 // You can change the language during runtime
 $translate.use(langKey);
 };

 }]);*/
adminApp.controller('centerController', function ($rootScope, $location, menuFnService, resourceFnService, $route, $translate, userFnService, articleService) {
    $rootScope.siteList = [];
    $rootScope.COMMONALITY = {};//公共变量，用来跳转传值
    $rootScope.menu = {};
    $rootScope.ITEMLIST = [];//物品list，公用
    $rootScope.itemPlan = {};
    $rootScope.itemPlan.unapprovedCount = 0;
    $rootScope.itemPlan.applyForCount = 0;
    $rootScope.langKey = "load.DefaultLanguage";
    $rootScope.siteModelCode = "load.OnChangeTip";
    $rootScope.siteModel = "";
    $rootScope.gameModelCode = "load.OnChangeTip";
    $rootScope.gameModel = "";
    $rootScope.menuSearchModel = "";
    $rootScope.menuBarData = {};//菜单栏集合
    $rootScope.menuBarData.menuBarOneName = "";//一级菜单名称
    $rootScope.menuBarData.menuBarTwoName = "";//二级菜单名称
    $rootScope.menuBarData.menuBarTwoUrl = "";//二级菜单url
    $rootScope.menuBarData.menuBarThreeName = "";//三级菜单名称
    $rootScope.menuBarData.menuBarTitle = "";//标题
    //获取未审批个数
    articleService.itemDisposePlan(0, 2).success(function (data) {
        $rootScope.itemPlan.unapprovedCount = data;
        $rootScope.itemPlan.sumCount = $rootScope.itemPlan.unapprovedCount + $rootScope.itemPlan.applyForCount;
        $rootScope.$apply();
    });

    //获取未申请个数
    articleService.itemDisposePlan(0, 1).success(function (data) {
        $rootScope.itemPlan.applyForCount = data;
        $rootScope.itemPlan.sumCount = $rootScope.itemPlan.unapprovedCount + $rootScope.itemPlan.applyForCount;
        $rootScope.$apply();
    });


    //每隔三分钟刷新订单状态
    setInterval(function () {
        //获取未审批个数
        articleService.itemDisposePlan(0, 2).success(function (data) {
            $rootScope.itemPlan.unapprovedCount = data;
            $rootScope.itemPlan.sumCount = $rootScope.itemPlan.unapprovedCount + $rootScope.itemPlan.applyForCount;
            $rootScope.$apply();
        });

        //获取未申请个数
        articleService.itemDisposePlan(0, 1).success(function (data) {
            $rootScope.itemPlan.applyForCount = data;
            $rootScope.itemPlan.sumCount = $rootScope.itemPlan.unapprovedCount + $rootScope.itemPlan.applyForCount;
            $rootScope.$apply();
        });
        $rootScope.$apply();
    }, 360000);

    /*
     任务进度点击
     */
    $rootScope.disposePlanClick = function () {
        $.ajax({
            url: ctx + '/operation/article/articleAuthorityInit',
            data: {menuName: "ArticleApplication"},
            success: function (data) {
                if (data) {
                    $location.path("/article/view");
                } else {
                    $location.path("/approval/view");
                }
            }, async: false
        });
    }

    /*
     任务进度点击（申请）
     */
    $rootScope.disposePlanArticleClick = function () {
        $.ajax({
            url: ctx + '/operation/article/articleAuthorityInit',
            data: {menuName: "ArticleApplication"},
            success: function (data) {
                if (data) {
                    $location.path("/article/view");
                } else {
                    $location.path("/approval/view");
                }
            }, async: false
        });
    }

    /*
     任务进度点击（审核）
     */
    $rootScope.disposePlanApprovalClick = function () {
        $.ajax({
            url: ctx + '/operation/article/articleAuthorityInit',
            data: {menuName: "ApprovalManage"},
            success: function (data) {
                if (data) {
                    $location.path("/approval/view");
                } else {
                    $location.path("/article/view");
                }
            }, async: false
        });
    }

    //获取游戏
    resourceFnService.resourceGetGame().success(function (data) {
        var list = [];
        for (var key in data) {
            list.push(data[key]);
        }
        $rootScope.gameList = list;
        //for (var i = 0; i < $rootScope.gameList.length; i++) {
        //    if ($rootScope.gameList[i].code == "game.tol") {
        //        $rootScope.gameTransfer = $rootScope.gameList[i].id;//全局game
        //        $rootScope.gameModel = $rootScope.gameList[i].id;
        //    }
        //}
        $rootScope.$apply();

    });


    //获取资源
    resourceFnService.resourceGetSite().success(function (data) {
        var list = [];
        for (var key in data) {
            data[key].code = "site." + data[key].code;
            list.push(data[key]);
        }
        $rootScope.siteList = list;
        $rootScope.$apply();
        $route.reload();
    });

    /*
     调用menuService，查询菜单列表
     */
    menuFnService.menuList().success(function (data) {
        console.log(data);
        $rootScope.menu = data;
        $rootScope.$apply();
        $route.reload();
    });


    //设置翻译参数
    $rootScope.setLang = function (langKey) {
        if (langKey == "en_US") {
            $rootScope.langKey = "load.English";
        }
        if (langKey == "zh_ZH") {
            $rootScope.langKey = "load.Chinese";
        }
        if (langKey == "default") {
            langKey = "zh_ZH";
            $rootScope.langKey = "load.DefaultLanguage";
        }
        $translate.use(langKey);
        $route.reload();
    };

    $rootScope.setLang("zh_ZH");
    //菜单栏跳转
    $rootScope.skipPageBar = function (url) {
        $location.path(url);
    }

    //获取用户习惯
    $.ajax({
        type: "post",
        url: ctx + '/system/user/habitUrlQuery',
        success: function (data) {
            if (data == null || data == "") {
                $rootScope.menuBarData = {};
            } else {
                $rootScope.menuBarData = JSON.parse(data);
            }
        }

        , async: false
    });

    /*
     动态绑定url
     */
    $rootScope.skipPage = function (data) {
        for (var i = 0; i < $rootScope.menu.childrenList.length; i++) {
            if ($rootScope.menu.childrenList[i].id == data.parentId) {
                $rootScope.menuBarData.menuBarOneName = $rootScope.menu.childrenList[i].menuName;
            }
        }
        $rootScope.menuBarData.menuBarTwoName = data.menuName;//二级菜单名称
        $rootScope.menuBarData.menuBarTwoUrl = data.url;//二级菜单url
        $rootScope.menuBarData.menuBarThreeName = "";
        $rootScope.menuBarData.menuBarTitle = data.menuName;
        var habitJson = JSON.stringify($rootScope.menuBarData);
        userFnService.habitUrlEdit(habitJson);
        $location.path(data.url);
    };

    //菜单点击
    $rootScope.menuListClicked = function (name, id, menuList) {
        /*
         菜单状态切换
         */
        if ($("." + name + id).attr("class") == "menu-list " + name + id) {
            $("." + name + id).attr("class", "menu-list " + name + id + " nav-active");
            for (var i = 0; i < menuList.length; i++) {
                if (menuList[i].id != id) {
                    $("." + menuList[i].menuName + menuList[i].id).attr("class", "menu-list " + menuList[i].menuName + menuList[i].id);
                }
            }
        } else {
            $("." + name + id).attr("class", "menu-list " + name + id);
        }
    }

    function menuHandover(name, id, menuList) {
        /*
         菜单状态切换
         */
        if ($("." + name + id).attr("class") == "menu-list " + name + id) {
            $("." + name + id).attr("class", "menu-list " + name + id + " nav-active");
            for (var i = 0; i < menuList.length; i++) {
                if (menuList[i].id != id) {
                    $("." + menuList[i].menuName + menuList[i].id).attr("class", "menu-list " + menuList[i].menuName + menuList[i].id);
                }
            }
        }
    };

    $rootScope.SearchTips = "load.SearchTips";//菜单搜索国际化
    //菜单检索
    $rootScope.myKeyUp = function (e, menuSearchModel) {
        var keycode = window.event ? e.keyCode : e.which;
        if (keycode == 13) {
            //国际化替换
            var name = $translate.instant(menuSearchModel);
            if (name == null || name == "") {
                $location.path("/homePage/view");
                for (var i = 0; i < $rootScope.menu.childrenList.length; i++) {
                    $("." + $rootScope.menu.childrenList[i].menuName + $rootScope.menu.childrenList[i].id).attr("class", "menu-list " + $rootScope.menu.childrenList[i].menuName + $rootScope.menu.childrenList[i].id);
                }
            } else {
                //根据名称查询
                $.ajax({
                    url: ctx + '/system/menu/getMenuByName',
                    data: {name: name},
                    success: function (data) {
                        if (data == null || data == "") {
                            $.gritter.add({
                                // (string | mandatory) the heading of the notification
                                title: $translate.instant("load.menuTipTitle"),
                                // (string | mandatory) the text inside the notification
                                text: $translate.instant("load.menuTipBody")
                            });
                        } else {
                            //是否是一级菜单
                            if (data.parentId == 0) {
                                menuHandover(data.menuName, data.id, $rootScope.menu.childrenList);
                            } else {
                                $("." + data.menuName + data.id).trigger("click");
                                for (var i = 0; i < $rootScope.menu.childrenList.length; i++) {
                                    if (data.parentId == $rootScope.menu.childrenList[i].id) {
                                        menuHandover($rootScope.menu.childrenList[i].menuName, $rootScope.menu.childrenList[i].id, $rootScope.menu.childrenList);
                                        $rootScope.skipPage(data);
                                    }
                                }

                            }
                        }
                    }, async: false
                });
            }


        }
    }

    /**
     * 验证
     * @param i 控件id
     * @param tip 提示内容
     */
    $rootScope.checkVerify = function (i, tip) {
        if (document.getElementById(i).validity.patternMismatch === true) {
            document.getElementById(i).setCustomValidity($translate.instant(tip));
        } else {
            document.getElementById(i).setCustomValidity("");
        }
    }

    $rootScope.habit = {};
    $rootScope.gameTransfer = "";//全局game
    $rootScope.siteTransfer = "";//全局site
    /*
     绑定select，change事件
     */
    $rootScope.resourceGameListChange = function (id, code) {
        $rootScope.gameModelCode = code;
        $rootScope.gameTransfer = id;
        $rootScope.habit.gameTransfer = $rootScope.gameTransfer;
        $rootScope.habit.siteTransfer = $rootScope.siteTransfer;
        var habitJson = JSON.stringify($rootScope.habit);
        userFnService.habitEdit(habitJson);
        $route.reload();//刷新路由，view
    };
    $rootScope.resourceSiteListChange = function (id, code) {
        $rootScope.siteModelCode = code;
        $rootScope.siteTransfer = id;
        $rootScope.habit.gameTransfer = $rootScope.gameTransfer;
        $rootScope.habit.siteTransfer = $rootScope.siteTransfer;
        var habitJson = JSON.stringify($rootScope.habit);
        userFnService.habitEdit(habitJson);
        $route.reload();
    };

    /**
     * 个人中心
     */
    $rootScope.profileJump = function () {
        $location.path("/profile/view");
    };


    console.log(loginChk);
    if (loginChk == "login") {
        $location.path('/homePage/view');//跳转首页
    }
    /**
     * 判断是否是第一次登录
     */
    if (loginChk == "login") {
        if (habit != null) {
            $rootScope.conGame = habit.gameTransfer;
            $rootScope.conSite = habit.siteTransfer;
        }
        $("#batchModalForAdd").modal('show');
        loginChk = "homePage";
    }

    /**
     * 登录选择game，change事件监听
     * @param conGame
     */
    $rootScope.conGameChange = function (conGame) {
        $rootScope.conGame = conGame;
    }

    /**
     * 登录选择site，change事件监听
     * @param conGame
     */
    $rootScope.conSiteChange = function (conSite) {
        $rootScope.conSite = conSite;
    }

    /**
     * 确认配置项
     */
    $rootScope.configurationForms = function () {
        //重置抬头显示
        for (var i = 0; i < $rootScope.siteList.length; i++) {
            if ($rootScope.conSite == $rootScope.siteList[i].id) {
                $rootScope.siteModelCode = $rootScope.siteList[i].code;
            }
        }
        for (var i = 0; i < $rootScope.gameList.length; i++) {
            if ($rootScope.conGame == $rootScope.gameList[i].id) {
                $rootScope.gameModelCode = $rootScope.gameList[i].code;
            }
        }


        $rootScope.habit.gameTransfer = $rootScope.conGame;
        $rootScope.habit.siteTransfer = $rootScope.conSite;
        $rootScope.gameModel = $rootScope.conGame;
        $rootScope.siteModel = $rootScope.conSite;
        $rootScope.gameTransfer = $rootScope.conGame;
        $rootScope.siteTransfer = $rootScope.conSite;
        var habitJson = JSON.stringify($rootScope.habit);
        userFnService.habitEdit(habitJson);
        $("#batchModalForAdd").modal('hide');
        //$location.path('/homePage/view');
    }

    $rootScope.loginCs = loginChk;

    /*
     转换日期工具，time是毫秒数。format格式。
     */
    $rootScope.format = function (time, format) {
        var t = new Date(time);
        var tf = function (i) {
            return (i < 10 ? '0' : '') + i
        };
        return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function (a) {
            switch (a) {
                case 'yyyy':
                    return tf(t.getFullYear());
                    break;
                case 'MM':
                    return tf(t.getMonth() + 1);
                    break;
                case 'mm':
                    return tf(t.getMinutes());
                    break;
                case 'dd':
                    return tf(t.getDate());
                    break;
                case 'HH':
                    return tf(t.getHours());
                    break;
                case 'ss':
                    return tf(t.getSeconds());
                    break;
            }
        })
    }
});

//是否可用
adminApp.filter('NumToStr', function () {
    return function (num) {
        switch (num) {
            case 1:
                return "load.numToStrAvailable";
                break;
            case 0:
                return "load.numToStrUnavailable";
                break;
        }
    };
});

//打开关闭
adminApp.filter('NumToStr2', function () {
    return function (num) {
        switch (num) {
            case 1:
                return "load.Open";
                break;
            case 0:
                return "load.Close";
                break;
        }
    };
});

//游戏渠道
adminApp.filter('NumToStr3', function () {
    return function (num) {
        switch (num) {
            case 'game':
                return "load.resGame";
                break;
            case 'site':
                return "load.resSite";
                break;
        }

    };
});

//服务器状态
adminApp.filter('NumToStr4', function () {
    return function (num) {
        switch (num) {
            case 1:
                return "load.Normal";
                break;
            case 2:
                return "load.Maintaining";
                break;
        }
    };
});

//负载状态
adminApp.filter('NumToStr5', function () {
    return function (num) {
        switch (num) {
            case 1:
                return "load.loadStatusFree";
                break;
            case 2:
                return "load.loadStatusFull";
                break;
            case 3:
                return "load.loadStatusFluency";
                break;
            case 4:
                return "load.loadStatusBusy";
                break;
        }
    };
});

//服务器标记、
adminApp.filter('NumToStr6', function () {
    return function (num) {
        switch (num) {
            case 1:
                return "load.areaTagNewArea";
                break;
            case 2:
                return "load.areaTagHot";
                break;
            case 3:
                return "load.areaTagRecommend";
                break;
        }
    };
});

//游戏
adminApp.filter('NumToStr7', function () {
    return function (num) {
        switch (num) {
            case 1:
                return "game.tol";
                break;
            case 27:
                return "game.jokes";
                break;
        }
    };
});

//物品类型、
adminApp.filter('NumToStr8', function () {
    return function (num) {
        switch (num) {
            case 1:
                return "load.itemTypeToys";
                break;
            case 2:
                return "load.itemTypeEquip";
                break;
            case 3:
                return "load.itemTypeitem";
                break;
            case 4:
                return "load.itemTypegem";
                break;
            case 5:
                return "load.itemTypegoods";
                break;
        }

    };
});

//渠道
adminApp.filter('NumToStr9', function () {
    return function (num) {
        switch (num) {
            case 0:
                return "site.checkAll";
                break;
            case 2:
                return "site.mobile";
                break;
            case 3:
                return "site.pc";
                break;
            case 4:
                return "site.web";
                break;
            case 5:
                return "site.tv";
                break;
            case 6:
                return "site.mobile.android";
                break;
            case 7:
                return "site.mobile.ios";
                break;
            case 8:
                return "site.mobile.wp";
                break;
            case 9:
                return "site.tv.letv";
                break;
            case 10:
                return "site.mobile.android.letv";
                break;
            case 11:
                return "site.group.letv";
                break;
            case 14:
                return "site.pc.test";
                break;
            case 15:
                return "site.mobile.android.downjoy";
                break;
            case 18:
                return "site.pc.ps";
                break;
            case 19:
                return "site.pc.xbox";
                break;
            case 20:
                return "site.pc.ffd";
                break;
            case 26:
                return "site.tv.osletv";
                break;
            case 27:
                return "site.game.jokes";
                break;
            case 28:
                return "site.mobile.android.test";
                break;
            case 29:
                return "site.mobile.android.lyt";
                break;
            case 30:
                return "site.mobile.android.qihoo";
                break;
            case 31:
                return "site.mobile.android.vivo";
                break;
            case 32:
                return "site.mobile.android.uc";
                break;
            case 33:
                return "site.mobile.android.mi";
                break;
            case 34:
                return "site.mobile.android.tencent";
                break;
            case 35:
                return "site.mobile.android.baidu";
                break;
            case 36:
                return "site.mobile.android.huawei";
                break;
            case 37:
                return "site.mobile.android.oppo";
                break;
            case 38:
                return "site.mobile.android.jinli";
                break;
            case 39:
                return "site.mobile.android.coolpad";
                break;
            case 40:
                return "site.mobile.android.meizu";
                break;
            case 41:
                return "site.mobile.android.lenovo";
                break;
            case 42:
                return "site.mobile.android.yaoqi";
                break;
            case 43:
                return "site.mobile.android.taptap";
                break;
            case 44:
                return "site.mobile.android.shichang";
                break;
            case 45:
                return "site.mobile.android.anfeng";
                break;
            case 46:
                return "site.mobile.android.c18183";
                break;
            case 47:
                return "site.mobile.android.banma";
                break;
            case 48:
                return "site.mobile.android.youjiu";
                break;
            case 49:
                return "site.mobile.android.anqu";
                break;
            case 50:
                return "site.mobile.android.chaohaowan";
                break;
            case 51:
                return "site.mobile.android.xinlang";
                break;
            case 52:
                return "site.mobile.android.c07073";
                break;
            case 53:
                return "site.mobile.android.c3367";
                break;
            case 54:
                return "site.mobile.android.c4399";
                break;
            case 55:
                return "site.mobile.android.youxiduo";
                break;
            case 56:
                return "site.mobile.android.gaoquwan";
                break;
            case 57:
                return "site.mobile.android.c1688";
                break;
            case 58:
                return "site.mobile.android.pipa";
                break;
            case 59:
                return "site.mobile.android.qiyou";
                break;
            case 60:
                return "site.mobile.android.c8477";
                break;
            case 61:
                return "site.mobile.android.c87";
                break;
            case 62:
                return "site.mobile.android.h5";
                break;
            case 63:
                return "site.mobile.ios.appstore";
                break;
            case 64:
                return "site.mobile.ios.chuangxing";
                break;
            case 65:
                return "site.mobile.android.haima";
                break;
            case 66:
                return "site.mobile.android.youmiwan";
                break;
            case 67:
                return "site.mobile.android.zhuoyi";
                break;
            case 68:
                return "site.mobile.android.shoumeng";
                break;
            case 69:
                return "site.mobile.android.papa";
                break;
            case 70:
                return "site.mobile.android.shuowan";
                break;
            case 71:
                return "site.mobile.android.kaopu";
                break;
            case 72:
                return "site.mobile.android.paojiao";
                break;
            case 73:
                return "site.mobile.android.pptv";
                break;
            case 74:
                return "site.mobile.android.gfan";
                break;
            case 75:
                return "site.mobile.android.jiudu";
                break;
            case 76:
                return "site.mobile.android.pps";
                break;
            case 77:
                return "site.mobile.android.vdianyou";
                break;
            case 78:
                return "site.mobile.android.chongchong";
                break;
            case 79:
                return "site.mobile.android.youyoucun";
                break;
            case 80:
                return "site.mobile.android.pandawan";
                break;
            case 81:
                return "site.mobile.android.tt";
                break;
            case 82:
                return "site.mobile.android.muant";
                break;
            case 83:
                return "site.mobile.android.zhangyue";
                break;
            case 84:
                return "site.mobile.android.yycsw";
                break;
            case 85:
                return "site.mobile.android.htc";
                break;
            case 86:
                return "site.mobile.android.mzw";
                break;
            case 87:
                return "site.mobile.android.youku";
                break;
            case 88:
                return "site.mobile.android.anzhi";
                break;
            case 89:
                return "site.mobile.android.sogou";
                break;
            case 90:
                return "site.mobile.android.sina";
                break;
            case 91:
                return "site.mobile.android.letv";
                break;
            case 92:
                return "site.mobile.android.apphui";
                break;
            case 93:
                return "site.mobile.android.itools";
                break;
            case 94:
                return "site.mobile.android.lehaihai";
                break;
            case 95:
                return "site.mobile.android.pywan";
                break;
            case 96:
                return "site.mobile.android.youlong";
                break;
            case 97:
                return "site.mobile.android.juhaowan";
                break;
            case 98:
                return "site.mobile.android.guopan";
                break;
            case 99:
                return "site.mobile.android.yeshen";
                break;
            case 100:
                return "site.mobile.android.wanke";
                break;
            case 101:
                return "site.mobile.android.toutiao";
                break;
            case 102:
                return "site.mobile.android.quick";
                break;
            case 103:
                return "site.mobile.android.maze";
                break;
            case 107:
                return "site.mobile.android.lycps.changhe";
                break;
            case 108:
                return "site.mobile.android.lycps.channle01";
                break;
            case 109:
                return "site.mobile.android.lycps.channle02";
                break;
            case 110:
                return "site.mobile.android.lycps.channle03";
                break;
            case 111:
                return "site.mobile.ios.appstore.cps1";
                break;
            case 112:
                return "site.mobile.ios.chuangxin.cps1";
                break;
            case 113:
                return "site.mobile.android.chuangxin";
                break;

        }

    };
});

//邮件类型
adminApp.filter('NumToStr10', function () {
    return function (num) {
        switch (num) {
            case 1:
                return "load.ActivityReward";
                break;
            case 2:
                return "load.IndividualCompensation";
                break;
            case 3:
                return "load.InternalApplication";
                break;
            case 4:
                return "load.RechargeCompensation";
                break;
            case 5:
                return "load.Other";
                break;
        }
    };
});

//发送类型
adminApp.filter('NumToStr11', function () {
    return function (num) {
        switch (num) {
            case 1:
                return "load.PersonalRule";
                break;
            case 2:
                return "load.AllRule";
                break;

        }

    };
});

//发送状态
adminApp.filter('NumToStr12', function () {
    return function (num) {
        switch (num) {
            case 1:
                return "load.Unsent";
                break;
            case 2:
                return "load.sending";
                break;
            case 3:
                return "load.sendSuccess";
                break;
            case 4:
                return "load.sendFailed";
                break;
        }
    };
});

//审批状态
adminApp.filter('NumToStr13', function () {
    return function (num) {
        switch (num) {
            case 1:
                return "load.NotApply";
                break;
            case 2:
                return "load.NotApprove";
                break;
            case 3:
                return "load.ApprovalMessagesBodyToSuccess";
                break;
            case 4:
                return "load.ApprovalMessagesBodyToFailure";
                break;
        }

    };
});

//公告类型
adminApp.filter('NumToStr14', function () {
    return function (num) {
        switch (num) {
            case 1:
                return "load.noticePopup";
                break;
            case 2:
                return "load.noticeRoll";
                break;
            case 3:
                return "load.noticeSystem";
                break;
            case 4:
                return "load.noticeWorld";
                break;
        }
    };
});

//绑定状态
adminApp.filter('NumToStr15', function () {
    return function (num) {
        switch (num) {
            case 1:
                return "load.UNBIND";
                break;
            case 2:
                return "load.BINDING";
                break;
            case 3:
                return "load.BOUND";
                break;
            case 4:
                return "load.INVALID";
                break;
        }
    };
});

//商品类型
adminApp.filter('NumToStr16', function () {
    return function (num) {
        switch (num) {
            case 1:
                return "load.itemTypegold";
                break;
            case 2:
                return "load.itemTypegem";
                break;
            case 3:
                return "load.itemTypeexperience";
                break;
            case 4:
                return "load.itemTypestamina";
                break;
            case 5:
                return "load.itemTypehonor";
                break;
            case 6:
                return "load.itemTypeclimb";
                break
            case 7:
                return "load.itemTypewishing";
                break;
            case 8:
                return "load.itemTypeconsortia";
                break;
            case 101:
                return "load.itemTypeprop";
                break;
            case 102:
                return "load.itemTypechumly";
                break;
            case 103:
                return "load.itemTypedesignation";
                break;

        }
    };
});



//活动打开关闭
adminApp.filter('NumToStr17', function () {
    return function (num) {
        switch (num) {
            case 1:
                return "load.Open";
                break;
            case 2:
                return "load.Close";
                break;
        }
    };
});

//手机系统
adminApp.filter('NumToStr18', function () {
    return function (num) {
        switch (num) {
            case 1:
                return "load.versionAndroid";
                break;
            case 2:
                return "load.versionIos";
                break;
        }
    };
});

//有效状态（版本更新）
adminApp.filter('NumToStr19', function () {
    return function (num) {
        switch (num) {
            case 1:
                return "load.versionValid";
                break;
            case 2:
                return "load.versionUnValid";
                break;
            case 3:
                return "load.versionRemoved";
                break;
        }
    };
});

//是或否
adminApp.filter('NumToStr20', function () {
    return function (num) {
        switch (num) {
            case 2:
                return "load.giftBagNo";
                break;
            case 1:
                return "load.giftBagYes";
                break;
        }
    };
});

//使用状态
adminApp.filter('NumToStr21', function () {
    return function (num) {
        switch (num) {
            case 0:
                return "load.codeUnact";
                break;
            case 1:
                return "load.codeActed";
                break;
            case 2:
                return "load.codeActing";
                break;
            case 3:
                return "load.codeLocked";
                break;
            case -1:
                return "load.codeError";
                break;
            case -2:
                return "load.codeReject";
                break;
        }
    };
});

//公告游戏
adminApp.filter('NumToStr22', function () {
    return function (num) {
        switch (num) {
            case '1':
                return "game.tol";
                break;
            case '27':
                return "game.jokes";
                break;
        }
    };
});

//商品上架状态
adminApp.filter('NumToStr23', function () {
    return function (num) {
        switch (num) {
            case 1:
                return "load.goodsPutaway";
                break;
            case 2:
                return "load.goodsSoldout";
                break;
        }
    };
});

//订单状态
adminApp.filter('NumToStr24', function () {
    return function (num) {
        switch (num) {
            case "UNPAID":
                return "load.billingOrderUNPAID";
                break;
            case "PAID":
                return "load.billingOrderPAID";
                break;
            case "DELIVERING":
                return "load.billingOrderDELIVERING";
                break;
            case "DELIVERED":
                return "load.billingOrderDELIVERED";
                break;
            case "REPAIR":
                return "load.billingOrderREPAIR";
                break;
        }
    };
});

//玩家封停，禁言状态
adminApp.filter('NumToStr25', function () {
    return function (num) {
        switch (num) {
            case 1:
                return "load.Shutup";
                break;
            case 2:
                return "load.Ban";
                break;
        }
    };
});

//订单类型
adminApp.filter('NumToStr26', function () {
    return function (num) {
        switch (num) {
            case "FANFANDOU":
                return "load.billingOrderfanfandou";
                break;
            case "ALIPAY":
                return "load.billingOrderalipay";
                break;
            case "WECHAT":
                return "load.billingOrderwechat";
                break;
            case "THIRD":
                return "load.billingOrderthird";
                break;
            case "REPAIR":
                return "load.billingOrderrepair";
                break;
        }
    };
});


//商城类型
adminApp.filter('NumToStr27', function () {
    return function (num) {
        switch (num) {
            case 0:
                return "load.normalPrepaidPay";
                break;
            case 1:
                return "load.gameActivitiePay";
                break;
            case 3:
                return "load.bagStore";
                break;

        }
    };
});