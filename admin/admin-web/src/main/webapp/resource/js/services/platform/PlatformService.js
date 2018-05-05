/**
 * 平台管理Service文件
 * author：钟亮
 * @type {module|*}
 */

/**
 商品配方Service
 */
adminApp.service('billingGoodsFnService', function () {


    /*
     添加信息
     对象：billingGoods
     */
    this.billingGoodsAdd = function (billingGoods) {
        return $.post(ctx + '/platform/billingGoods/insert', billingGoods);
    };

    /*
     根据id查询信息
     权限id：
     */
    this.goodsGetById = function (goodId) {
        return $.get(ctx + '/platform/billingGoods/getGoods/' + goodId);
    };

    /*
     根据id查询信息
     权限id：
     */
    this.checkCode = function (code) {
        return $.get(ctx + '/platform/billingGoods/checkCode', {"code": code});
    };

    /*
     刷新缓存
     */
    this.refreshBillingGoodsCache = function () {
        return $.get(ctx + '/platform/billingGoods/refreshBillingGoodsCache');
    }

    /*
     修改信息
     对象：billingGoods
     */
    this.billingGoodsUpdate = function (billingGoods) {
        return $.post(ctx + '/platform/billingGoods/update', billingGoods);
    };

    /*
     一键修改
     */
    this.keyUpdate = function (data) {
        return $.post(ctx + '/platform/billingGoods/keyUpdate', data);
    }

    /*
     根据billingGood查询billingGoods信息（分页）
     对象：billingGoods
     指定页：pageIndex
     每页条数：pageSize
     */
    this.billingGoodsFindByName = function (billingGood, pageIndex, pageSize) {
        return $.post(ctx + '/platform/billingGoods/pageList', {
            "gameId": billingGood.gameId,
            "siteId": billingGood.siteId,
            "goodsName": billingGood.goodsName,
            "goodsTypeId": billingGood.goodsTypeId,
            "goodsAmount": billingGood.goodsAmount,
            "goodsCode": billingGood.goodsCode,
            "pageIndex": pageIndex,
            "pageSize": pageSize,
            "sort": null,
            "order": null
        });
    };

    /*
     查询区服
     */
    this.areaList = function (gameId, siteId) {
        return $.get(ctx + '/platform/billingGoods/areaList', {"gameId": gameId, "siteId": siteId});
    };

    /*
     查询奖励包
     */
    this.awardPackage = function (awardCondition, pageIndex, pageSize) {
        return $.get(ctx + '/platform/billingGoods/awardPackage', {
            "awardName": awardCondition.awardName,
            "awardType": awardCondition.awardType,
            "pageIndex": pageIndex,
            "pageSize": pageSize,
            "sort": null,
            "order": null
        });
    };

    /*
     删除信息
     id：goodId
     */
    this.goodsDelete = function (goodId) {
        return $.get(ctx + '/platform/billingGoods/goodsDelete', {"goodIds": goodId});
    };

    /*
     获取首充策略
     id：gameId
     */
    this.getFirstBuyPolicy = function (gameId) {
        return $.get(ctx + '/platform/billingGoods/getFirstBuyPolicy', {"gameId": gameId});
    };

    /*
     首充策略编辑
     */
    this.firstBuyPolicyEdit = function (firstBuyData) {
        return $.get(ctx + '/platform/billingGoods/firstBuyPolicyEdit', firstBuyData);
    };

    ///*
    // 一键配置首充策略
    // */
    //this.firstBuyAdd = function (firstBuy) {
    //    return $.get(ctx + '/platform/billingGoods/firstBuyPolicyAdd', {
    //        "operation": firstBuy.operation,
    //        "operateCount": firstBuy.operateCount,
    //        "goodsIds": firstBuy.goodsId
    //    });
    //}
    //
    ///*
    // 一键配置奖励包
    // */
    //this.packageAdd = function (package) {
    //    return $.get(ctx + '/platform/billingGoods/packageAdd', {
    //        "packageType": package.packageType,
    //        "packageDesc": package.packageDesc,
    //        "value": package.packageValue,
    //        "itemIds": package.goodsItemsIds,
    //        "goodsIds": package.goodsId
    //    });
    //}
});

/**
 区服管理Service（小服）
 */
adminApp.service('gameAreaFnService', function () {


    /*
     添加信息
     对象：gameArea
     */
    this.gameAreaAdd = function (gameArea) {
        return $.post(ctx + '/platform/gameArea/insert', gameArea);
    }

    /*
     修改信息
     对象：gameArea
     */
    this.gameAreaUpdate = function (gameArea) {
        return $.post(ctx + '/platform/gameArea/update', gameArea);
    }

    /*
     批量修改信息
     对象：gameArea
     */
    this.batchUpdate = function (batchArea) {
        if (batchArea.areaTag == null || batchArea.areaTag == undefined || batchArea.areaTag == "") {
            batchArea.areaTag = 0;
        }
        if (batchArea.batchMaintenanceStatus == null || batchArea.batchMaintenanceStatus == undefined || batchArea.batchMaintenanceStatus == "") {
            batchArea.batchMaintenanceStatus = 0;
        }
        return $.post(ctx + '/platform/gameArea/batchUpdate', batchArea);
    }

    /*
     删除信息
     权限id：areaId
     */
    this.areaDelete = function (areaId) {
        return $.get(ctx + '/platform/gameArea/areaDelete', {"areaIds": areaId});
    }


    /*
     根据gameArea查询gameArea信息（分页）
     对象：gameArea
     指定页：pageIndex
     每页条数：pageSize
     */
    this.gameAreaFindByName = function (gameArea, pageIndex, pageSize) {
        if (gameArea.gameId == undefined || gameArea.gameId == null || gameArea.gameId == "") {
            gameArea.gameId = -1;
        }
        if (gameArea.siteId == undefined || gameArea.siteId == null || gameArea.siteId == "") {
            gameArea.siteId = null;
        }
        return $.post(ctx + '/platform/gameArea/pageList', {
            "areaName": gameArea.areaName,
            "gameId": gameArea.gameId,
            "siteId": gameArea.siteId,
            "pageIndex": pageIndex,
            "pageSize": pageSize,
            "sort": null,
            "order": null
        });
    }

    /*
     根据gameArea查询gameArea信息
     对象：gameArea

     */
    this.gameAreaFindByGameId = function (gameArea) {
        return $.post(ctx + '/platform/gameArea/getGameAreasById', {"gameId": gameArea.gameId});
    };

    /*
     根据游戏id，渠道类型查询
     */
    this.getGameAreasBySiteType = function (gameArea) {
        return $.post(ctx + '/platform/gameArea/getGameAreasBySiteType', {
            "gameId": gameArea.gameId,
            "siteType": gameArea.siteType
        });
    };


    /*
     根据id查询信息
     权限id：
     */
    this.gameAreaGetById = function (areaId) {
        return $.get(ctx + '/platform/gameArea/getGameArea/' + areaId);
    }

    /*
     修改状态
     权限id：
     */
    this.gameAreaStateUpdate = function (gameArea) {
        return $.post(ctx + '/platform/gameArea/updateState', gameArea);
    }

    /*
     开服关服
     */
    this.gameAreaUpdateServerStatus = function (gameArea) {
        return $.post(ctx + '/platform/gameArea/updateServerStatus', gameArea);
    }

    /*
     刷新缓存
     */
    this.refreshGameAreaCache = function () {
        return $.post(ctx + '/platform/gameArea/refreshGameAreaCache');
    }


});


/**
 * gameRegion 大区服Service
 */
adminApp.service('gameRegionFnService', function () {


    /*
     根据gameRegion查询gameRegion信息（分页）
     对象：gameRegion
     指定页：pageIndex
     每页条数：pageSize
     */
    this.gameRegionFindByName = function (gameRegion, pageIndex, pageSize) {
        if (gameRegion.gameId == undefined || gameRegion.gameId == null || gameRegion.gameId == "") {
            gameRegion.gameId = -1;
        }
        if (gameRegion.siteId == undefined || gameRegion.siteId == null || gameRegion.siteId == "") {
            gameRegion.siteId = null;
        }
        return $.post(ctx + '/platform/gameRegion/pageList', {
            "areaName": gameRegion.areaName,
            "gameId": gameRegion.gameId,
            "siteId": gameRegion.siteId,
            "pageIndex": pageIndex,
            "pageSize": pageSize,
            "sort": null,
            "order": null
        });
    };

    /*
     添加信息
     对象：gameRegion
     */
    this.gameRegionAdd = function (gameRegion) {
        return $.post(ctx + '/platform/gameRegion/insert', gameRegion);
    }

    /*
     修改信息
     对象：gameArea
     */
    this.gameRegionUpdate = function (gameRegion) {
        return $.post(ctx + '/platform/gameRegion/update', gameRegion);
    }

    /*
     删除信息
     权限id：areaId
     */
    this.regionDelete = function (regionId) {
        return $.get(ctx + '/platform/gameRegion/regionDelete', {"regionIds": regionId});
    }

    /*
     根据id查询信息
     权限id：
     */
    this.gameRegionGetById = function (regionId) {
        return $.get(ctx + '/platform/gameRegion/getGameRegion/' + regionId);
    }

    /*
     根据gameArea查询gameArea信息（分页）
     对象：gameArea

     */
    this.gameRegionFindByGameId = function (gameId) {
        return $.post(ctx + '/platform/gameRegion/getGameRegionsByGameId', {"gameId": gameId});
    }


});

/**
 版本信息Service
 */
adminApp.service('editionFnService', function () {


    /*
     查询信息
     对象：edition
     */
    this.editionPage = function (edition, pageIndex, pageSize) {
        if (edition.gameId == undefined || edition.gameId == null || edition.gameId == "") {
            edition.gameId = -1;
        }
        if (edition.siteId == undefined || edition.siteId == null || edition.siteId == "") {
            edition.siteId = -1;
        }
        return $.post(ctx + '/platform/edition/pageList', {
            "gameId": edition.gameId,
            "siteId": edition.siteId,
            "versionType": edition.versionType,
            "pageIndex": pageIndex,
            "pageSize": pageSize,
            "sort": null,
            "order": null
        });
    }

    //版本补丁添加
    this.editionAdd = function (edition) {
        return $.post(ctx + '/platform/edition/insert', edition);
    }

    /*
     删除信息
     */
    this.editionDelete = function (editionIds) {
        return $.get(ctx + '/platform/edition/editionDelete', {"editionIds": editionIds});
    }

    /*
     根据id查询
     */
    this.editionSelectById = function (editionId) {
        return $.get(ctx + '/platform/edition/editionSelectById', {"id": editionId});
    }

    //版本补丁修改
    this.editionUpdate = function (edition) {
        return $.post(ctx + '/platform/edition/update', edition);
    }

    //版本补丁缓存更新
    this.refreshCacheAll = function () {
        return $.post(ctx + '/platform/edition/refreshCacheAll');
    }

    //版本推送
    this.pushGameVersion = function (edition) {
        if (edition.areaId == "" || edition.areaId == null || edition.areaId == undefined) {
            edition.areaId = 0;
        }
        return $.post(ctx + '/platform/edition/pushGameVersion', edition);
    }
});


/**
 订单信息Service
 */
adminApp.service('billingOrderFnService', function () {


    /*
     查询信息
     对象：billingOrder
     */
    this.billingOrderPage = function (billingOrder, pageIndex, pageSize) {
        if (billingOrder.gameId == undefined || billingOrder.gameId == null || billingOrder.gameId == "") {
            billingOrder.gameId = -1;
        }
        if (billingOrder.siteId == undefined || billingOrder.siteId == null || billingOrder.siteId == "") {
            billingOrder.siteId = -1;
        }
        return $.post(ctx + '/platform/billingOrder/pageList', {
            "gameId": billingOrder.gameId,
            "siteId": billingOrder.siteId,
            "roleId": billingOrder.roleId,
            "roleName": billingOrder.roleName,
            "from": billingOrder.from,
            "to": billingOrder.to,
            "pageIndex": pageIndex,
            "pageSize": pageSize,
            "sort": null,
            "order": null
        });
    };

    //检查角色
    this.checkRole = function (role) {
        return $.post(ctx + '/platform/billingOrder/checkRole', {
            "gameId": role.gameId,
            "roleId": role.roleId,
            "roleName": role.roleName
        });
    };

    //订单提交
    this.orderParamAdd = function (orderParam) {
        if (orderParam.siteId == null || orderParam.siteId == "" || orderParam.siteId == undefined) {
            orderParam.siteId = 0;
        }
        return $.post(ctx + '/platform/billingOrder/orderParamAdd', orderParam);
    };

});