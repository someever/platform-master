<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="${ctx}/resource/adminex/js/DateTimePicker/DateInit.js"></script>
<script src="${ctx}/resource/adminex/js/multi-select-init.js"></script>
<script src="${ctx}/resource/adminex/js/icheck-init.js"></script>
<style>

    .chk_1 {
        display: none;
    }

    .chk_1 + label {
        background-color: #FFF;
        border: 1px solid #C1CACA;
        box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05), inset 0px -15px 10px -12px rgba(0, 0, 0, 0.05);
        padding: 7px;
        border-radius: 5px;
        display: inline-block;
        position: relative;
        margin-right: -20px;
    }

    .chk_1 + label:active {
        box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05), inset 0px 1px 3px rgba(0, 0, 0, 0.1);
    }

    .chk_1:checked + label {
        background-color: #ECF2F7;
        border: 1px solid #92A1AC;
        box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05), inset 0px -15px 10px -12px rgba(0, 0, 0, 0.05), inset 15px 10px -12px rgba(255, 255, 255, 0.1);
        color: #243441;
    }

    .chk_1:checked + label:after {
        content: '\2714';
    / / 勾选符号 position: absolute;
        top: 0px;
        left: 0px;
        color: red;
        width: 100%;
        text-align: center;
        font-size: 1.4em;
        padding: 1px 0 0 0;
        vertical-align: text-top;
    }

</style>
<div ng-controller="billingGoodsEditController">


    <form role="form" ng-submit="billingGoodsEditForm()" name="billingGoodsEditForms">
        <div class="row">
            <div class="col-sm-6">
                <section class="panel">
                    <header class="panel-heading">
                        <span translate="load.basicInfo"></span>
                    </header>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-3 form-group labelSpace">
                                <label><span translate="load.shopType"></span>:<span
                                        style="color: red">*</span></label>
                                <select name="shopType" aria-controls="editable-sample"
                                        class="form-control shopType" ng-model="shopType"
                                        ng-change="shopTypeChange(shopType)"
                                        ng-options="shopType.id as shopType.code|translate for shopType in storeTypeDatas"
                                        required>
                                    <option value="" translate="load.pleaseSelect"></option>
                                </select>

                            </div>

                            <div class="col-md-3 form-group labelSpace">
                                <label><span translate="load.goodsGoodsType"></span>:<span
                                        style="color: red">*</span></label>
                                <select name="goodsType" aria-controls="editable-sample"
                                        class="form-control goodsType" ng-model="goodsData.goodsType"
                                        ng-change="goodsTypeChange(goodsData.goodsType)"
                                        ng-options="goodsType.value as goodsType.key|translate for goodsType in goodsTypeDatas"
                                        required>
                                    <option value="" translate="load.pleaseSelect"></option>
                                    <%--   <option value="{{goodsType.id}}" ng-repeat="goodsType in goodsTypeDatas" translate="load.{{goodsType.code}}"></option>--%>
                                </select>
                            </div>

                            <div class="col-md-3 form-group labelSpace" id="goodsGoodsName">
                                <label><span translate="load.goodsGoodsName"></span>:<span
                                        style="color: red">*</span></label>
                                <input type="text" class="form-control goodsName" name="goodsName"
                                       ng-model="goodsData.goodsName" maxlength="50" required>
                            </div>

                            <div class="col-md-3 form-group labelSpace" id="propGoodsName" hidden>
                                <label><span translate="load.goodsGoodsName"></span>:<span
                                        style="color: red">*</span></label>
                                <div class="input-group ">
                                    <input type="text" class="form-control" ng-model="goodsData.goodsName"
                                           ng-click="itemSelected()" readonly/>
                                              <span class="input-group-btn">
                                                <button class="btn btn-default" ng-click="itemSelected()"
                                                        style="height: 34px;" type="button"><i class="fa fa-search"></i>
                                                </button>
                                              </span>
                                </div>
                            </div>
                            <div class="col-md-3 form-group labelSpace" id="propIdDiv" hidden>
                                <label><span translate="load.propId"></span>:<span
                                        style="color: red">*</span></label>
                                <input type="text" class="form-control goodsCodeProps" name="goodsCodeProps"
                                       id="goodsCodeProps" ng-model="goodsData.goodsCode" maxlength="50"
                                       pattern="^[A-Za-z0-9]+$"
                                       ng-Blur="checkVerify('goodsCodeProps','load.lettersAndInteger')" readonly
                                       required>
                            </div>

                            <div class="col-md-3 form-group labelSpace" id="goodsCodeDiv">
                                <label><span translate="load.goodsGoodsCode"></span>:<span
                                        style="color: red">*</span></label>
                                <input type="text" class="form-control goodsCode" name="goodsCode"
                                       id="goodsCode" ng-model="goodsData.goodsCode" maxlength="50"
                                       pattern="^[A-Za-z0-9]+$"
                                       ng-Blur="checkVerify('goodsCode','load.lettersAndInteger')" required>
                            </div>

                        </div>

                        <div class="row">
                            <div class="col-md-3 form-group labelSpace">
                                <label><span translate="load.goodsGoodsCount"></span>:<span
                                        style="color: red">*</span></label>
                                <input type="number" class="form-control goodsCount" name="goodsCount"
                                       ng-model="goodsData.goodsCount" maxlength="50" min="0" max="100000000000"
                                       required>
                            </div>

                            <%--<div class="col-md-3 form-group labelSpace">--%>
                            <%--<label><span translate="load.goodsUnitPrice"></span>:<span--%>
                            <%--style="color: red">*</span></label>--%>
                            <%--<input type="number" class="form-control unitPrice" ng-model="goodsData.unitPrice"--%>
                            <%--maxlength="50" min="0" max="100000000000" required>--%>
                            <%--</div>--%>
                            <div class="col-md-3 form-group labelSpace">
                                <label><span translate="load.goodsValidStatus"></span>:<span
                                        style="color: red">*</span></label>
                                <select name="validStatus" aria-controls="editable-sample"
                                        class="form-control validStatus" ng-model="validStatus" required>
                                    <option value="" translate="load.pleaseSelect"></option>
                                    <option value="1" translate="load.validStatusValid"></option>
                                    <option value="2" translate="load.validStatusInvalid"></option>
                                </select>
                            </div>

                            <div class="col-md-3 form-group labelSpace">
                                <label><span translate="load.goodsGoodsAmount"></span>:<span
                                        style="color: red">*</span></label>
                                <input type="number" class="form-control goodsAmount" name="goodsAmount"
                                       ng-model="goodsData.goodsAmount" maxlength="50" min="0" max="100000000000"
                                       required>
                            </div>

                            <div class="col-md-3 form-group labelSpace">
                                <label><span translate="load.goodsDisaccount"></span>:<span
                                        style="color: red">*</span></label>
                                <input type="number" class="form-control disaccount" name="disaccount"
                                       ng-model="goodsData.disaccount" maxlength="50" max="1" min="0"
                                       required>

                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-3 form-group labelSpace">
                                <label><span translate="load.goodsGoodsDesc"></span>:</label>
                                    <textarea class="form-control goodsDesc" style="height: 150px;resize:none;"
                                              name="goodsDesc" ng-model="goodsData.goodsDesc"></textarea>
                            </div>

                            <div class="col-md-3 form-group labelSpace">
                                <label><span translate="load.goodsGoodsMarkTime"></span>:<span
                                        style="color: red">*</span></label>
                                <input size="16" type="text" readonly class="ui_timepicker form-control"
                                       ng-model="goodsData.goodsMarkTime">
                            </div>

                        </div>
                    </div>
                </section>
            </div>

            <div class="col-sm-6" id="propDetails" hidden>
                <section class="panel">
                    <header class="panel-heading">
                        <span translate="load.propDetails"></span>
                    </header>

                    <div class="panel-body">
                        <div class="row">

                            <div class="col-md-3 form-group labelSpace">
                                <label><span translate="load.goodsGoodsPic"></span>:</label>
                                <input type="text" class="form-control goodsPic" ng-model="goodsData.goodsPic">
                            </div>

                            <div class="col-md-3 form-group labelSpace">
                                <label><span translate="load.goodsRelatedGoodsId"></span>:</label>
                                <input type="text" class="form-control discount" name="relatedGoodsId"
                                       id="relatedGoodsId" ng-model="goodsData.relatedGoodsId" maxlength="50"
                                       pattern="^[A-Za-z0-9]+$"
                                       ng-Blur="checkVerify('relatedGoodsId','load.lettersAndInteger')">
                            </div>


                        </div>
                        <%--  <div class="row">
                              <div class="col-md-3 form-group labelSpace">
                                  <label><span translate="load.goodsGoodsDesc"></span>:</label>
                                  <textarea class="form-control goodsDesc" style="height: 150px;resize:none;"
                                            name="goodsDesc" ng-model="goodsData.goodsDesc"></textarea>
                              </div>
                          </div>--%>
                    </div>
                </section>
            </div>

            <div class="col-sm-6">
                <section class="panel">
                    <header class="panel-heading">
                        <span translate="load.Config"></span>
                    </header>

                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-4 form-group labelSpace">
                                <label><span translate="load.GameId"></span>:<span
                                        style="color: red">*</span></label>
                                <select name="gameId" aria-controls="editable-sample" class="form-control gameId"
                                        ng-options="game.id as game.code|translate for game in gameList"
                                        ng-change="resourceGameListChange(gameToyGameList)"
                                        ng-model="gameToyGameList" required>
                                    <option value="" translate="load.pleaseSelect"></option>
                                </select>
                            </div>

                            <div class="col-md-4 form-group labelSpace">
                                <label><span translate="load.SiteId"></span>:</label>
                                <select name="siteId" aria-controls="editable-sample" class="form-control siteId"
                                        ng-options="site.id as site.code|translate for site in siteList"
                                        ng-change="resourceSiteListChange(gameToySiteList)"
                                        ng-model="gameToySiteList">
                                    <option value="" translate="load.OnChangeTip"></option>
                                </select>
                            </div>

                        </div>
                        <div class="row">
                            <aside class="col-md-3">
                                <h4 class="drg-event-title"><span translate="load.areaList"></span></h4>
                                <div class='external-events'>
                                    <div class='external-event label label-primary'
                                         data-ng-repeat="area in areaData track by $index">{{area}}
                                    </div>
                                    <br/>
                                </div>
                            </aside>
                            <button class="btn btn-warning" type="button" ng-click="areaClick()"><span
                                    translate="load.areaChoice"></span></button>
                        </div>
                        <%--<div class="modal-footer">--%>
                        <%--<div style="float: right;margin-right: 20px;">--%>
                        <%--<button type="button" class="btn btn-default" href="javascript:"--%>
                        <%--onclick="history.back(); "><span translate="load.Cancel"></span></button>--%>
                        <%--<button type="submit" class="btn btn-primary addSubmit"--%>
                        <%--><span--%>
                        <%--translate="load.Ok"></span></button>--%>
                        <%--</div>--%>
                        <%--<p ng-hide="!billingGoodsEditForms.$invalid"--%>
                        <%--style="color: red;float: right;margin-top: 15px;"><span--%>
                        <%--translate="load.remindValidate"></span></p>--%>
                        <%--</div>--%>
                    </div>
                </section>
            </div>

            <div class="col-sm-6">
                <section class="panel">
                    <header class="panel-heading">
                                <span translate="load.applicationEmailContent"
                                ></span><span style="color:red;">&nbsp;&nbsp;(<span
                            translate="load.articleAttention"></span>)</span>
                    </header>

                    <div class="panel-body">


                        <div class="form-group labelSpace">
                            <div class="clientContent" ng-repeat="item in goodsData.itemList.item">
                                <div>

                                    <div class="row">
                                        <div class="col-md-3 form-group labelSpace">
                                            <label><span translate="load.itemItemType"></span>:<span
                                                    style="color:red;">&nbsp;&nbsp;(<span
                                                    translate="load.itemChoiceExplain"></span>)</span></label>
                                            <select name="itemType" aria-controls="editable-sample"
                                                    ng-change="itemTypeChange(item.itemType,$index)"
                                                    ng-options="itemType.value as itemType.key|translate for itemType in itemTypeList"
                                                    class="form-control itemType"
                                                    ng-model="item.itemType">
                                                <option value="" translate="load.pleaseSelect"></option>
                                                <%--   <option value="{{itemType.id}}" ng-repeat="itemType in itemTypeList" translate="load.itemType{{itemType.code}}"></option>--%>
                                            </select>
                                        </div>

                                        <div class="prop">
                                            <div class="col-md-3 form-group labelSpace">
                                                <label><span
                                                        translate="load.applicationItemName"></span>:</label>
                                                <div class="input-group m-bot15">
                                                    <input type="text" class="form-control"
                                                           ng-model="item.itemName"
                                                           ng-click="firstItemSelected(item.itemType,$index)"
                                                           readonly/>
                                              <span class="input-group-btn">
                                                <button class="btn btn-default"
                                                        ng-click="firstItemSelected(item.itemType,$index)"
                                                        style="height: 34px;" type="button"><i class="fa fa-search"></i>
                                                </button>
                                              </span>
                                                </div>
                                            </div>

                                            <div class="col-md-3 form-group labelSpace">
                                                <label><span
                                                        translate="load.applicationItemNum"></span>:</label>
                                                <input type="number" class="form-control" min="0"
                                                       max="100000000"
                                                       ng-model="item.value"/>
                                            </div>

                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-3 form-group labelSpace"
                                             ng-repeat="analog in item.extraData">
                                            <label>{{analog.formName}}:<span style="color: red"
                                                                             ng-hide="analog.formRequired==required">*</span></label>
                                            <div class="iconic-input right">
                                                <i>{{analog.formUnit}}</i>
                                                <input type={{analog.formType}} class="form-control"
                                                       ng-model="analog.formValue"
                                                       {{analog.formRequired}}
                                                       name="{{analog.formCode}}" maxlength="20">
                                            </div>

                                        </div>

                                    </div>

                                    <button type="button" class="btn btn-danger"
                                            style="margin-top: 25px;"
                                            href="javascript:" ng-show="clientContent.canDescReply"
                                            ng-click="clientContent.clientDelete($index)"><span
                                            translate="load.Delte"></span>&nbsp<i
                                            class="fa fa-times"></i></button>

                                    <header class="panel-heading">

                                    </header>
                                </div>
                            </div>
                            <div class="clientFoot">
                                <button type="button" class="btn btn-success" href="javascript:"
                                        ng-click="clientContent.clientAdd(clientCount)"><span
                                        translate="load.add"></span>&nbsp<i class="fa fa-plus"></i>
                                </button>
                            </div>
                        </div>
                </section>
            </div>

            <div class="col-sm-6">
                <section class="panel">
                    <header class="panel-heading">
                        <span translate="load.goodsFirstBuyPolicy"></span>
                    </header>

                    <div class="panel-body">
                        <div class="row">
                            <%--<div class=" col-md-4 form-group labelSpace">--%>
                                <%--<label translate="load.goodsSetTheRatio"></label>--%>
                                <%--<input type="number" class="form-control" name="goodsCount"--%>
                                       <%--ng-model="goodsData.firstBuyPolicy.operateCount" maxlength="50" min="0"--%>
                                       <%--max="100000000000" readonly--%>
                                <%-->--%>
                            <%--</div>--%>
                            <%--<div class="form-group">--%>
                                <%--<div class="checkbox single-row"--%>
                                     <%--style="margin-top: 23px;margin-left: -15px;">--%>
                                    <%--<input type="checkbox" id="goodsFirstBuyPolicySwitch"--%>
                                           <%--class="checkbox goodsFirstBuyPolicySwitch"--%>
                                           <%--style="width:50px;cursor:pointer"--%>
                                           <%--ng-model="goodsData.firstBuyPolicy.isFirstPay"--%>
                                           <%--ng-change="valueChange()"--%>
                                           <%--ng-true-value="1" ng-false-value="0"/>--%>
                                    <%--<div style="margin-top: 23px;"><label for="goodsFirstBuyPolicySwitch"--%>
                                                                          <%--style="color: red;"--%>
                                                                          <%--translate="load.goodsFirstBuyPolicySwitch"></label>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            <%--</div>--%>

                        </div>
                        <div class="row" style="margin-top: 20px;">

                            <div class="form-group labelSpace">
                                <aside class="col-md-4">
                                    <h4 class="drg-event-title"><span translate="load.bagBagChoice"></span></h4>
                                    <div class='external-events'>
                                        <div class='external-event label label-primary'
                                             data-ng-repeat="bag in giftBagDatas track by $index">{{bag}}
                                        </div>
                                        <br/>
                                    </div>
                                </aside>
                                <button class="btn btn-warning" type="button" ng-click="bagClick()"><span
                                        translate="load.bagCheckBag"></span></button>

                            </div>
                        </div>

                        <div class="modal-footer">
                            <div style="float: right;margin-right: 20px;">
                                <button type="button" class="btn btn-default" href="javascript:"
                                        onclick="history.back(); "><span translate="load.Cancel"></span></button>
                                <button type="submit" class="btn btn-primary addSubmit"
                                ><span
                                        translate="load.Ok"></span></button>
                            </div>
                            <p ng-hide="!billingGoodsEditForms.$invalid"
                               style="color: red;float: right;margin-top: 15px;"><span
                                    translate="load.remindValidate"></span></p>
                        </div>

                    </div>
                </section>
            </div>

        </div>
    </form>


    <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="bagAddModal"
         class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
                    <h4 class="modal-title"><span translate="load.bagBagChoice"></span></h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-6">
                            <form class="form-horizontal bucket-form" ng-submit="bagAdd()">
                                <%--<div class="form-group" style="width: 800px;">--%>
                                <%--<div class="col-sm-9 icheck ">--%>
                                <%--<div style="margin-left: 20px;">--%>
                                <%--<a ng-click="bagAll(giftBagLists)"--%>
                                <%--class="btn btn-success"><span translate="load.checkAll"></span></a>--%>
                                <%--<a ng-click="deselectAllBag(giftBagLists)"--%>
                                <%--class="btn btn-danger"><span translate="load.deselectAll"></span></a>--%>
                                <%--<a ng-click="bagVersa()"--%>
                                <%--class="btn btn-info"><span--%>
                                <%--translate="load.InvertSelection"></span></a>--%>
                                <%--</div>--%>
                                <%--</div>--%>
                                <%--<div class="col-sm-9 icheck ">--%>
                                <%--<div>--%>
                                <%--<div class="checkbox" data-ng-repeat="bag in giftBagLists"--%>
                                <%--align="center">--%>
                                <%--<input type="checkbox" id={{bag.packageName}} name={{bag.packageId}}--%>
                                <%--ng-click="updateBagSelection($event,bag.packageId,bag.packageName)"--%>
                                <%--ng-model="bagModel" ng-checked="bagMaster"--%>
                                <%--class="chk_1 bagChildren">--%>
                                <%--<label for={{bag.packageName}}>{{bag.packageName}}</label>--%>
                                <%--</div>--%>

                                <%--</div>--%>
                                <%--</div>--%>


                                <%--</div>--%>

                                <div class="form-group" style="margin-left: 1px;">
                                    <span class="label label-success" ng-click="bagAll(giftBagLists)"
                                          style="cursor:pointer" translate="load.checkAll"></span>
                                    <span class="label label-danger" ng-click="deselectAllBag(giftBagLists)"
                                          translate="load.deselectAll" style="cursor:pointer"></span>
                                    <span class="label label-info" ng-click="bagVersa()" style="cursor:pointer"
                                          translate="load.InvertSelection"></span>
                                </div>
                                <div class="form-group" style="width: 590px;overflow :auto;height: 500px;">
                                    <div class="col-sm-9 icheck ">
                                        <div>
                                            <div data-ng-repeat="bag in giftBagLists track by $index" align="center"
                                                 class="bagCheckList">
                                                <span class="label label-default" name={{bag.packageId}}
                                                      ng-click="updateBagSelection($event,bag.packageId,bag.packageName)"
                                                      style="cursor:pointer">{{bag.packageName}}</span>
                                                <br/>
                                                <br/>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                                <%--<div style="width: 550px;text-align:center;">--%>
                                <%--<pager page-count="pageCount" current-page="currentPage"--%>
                                <%--on-page-change="onPageChange()" first-text="load.HomePage"--%>
                                <%--next-text="load.NextPage" prev-text="load.PreviousPage"--%>
                                <%--last-text="load.EndPage" show-goto="true"--%>
                                <%--goto-text="load.GoToPage"></pager>--%>
                                <%--</div>--%>
                                <div class="modal-footer" style="width: 550px;">

                                    <button type="button" class="btn btn-default" data-dismiss="modal"><span
                                            translate="load.Cancel"></span></button>
                                    <button type="submit" class="btn btn-primary addSubmit"><span
                                            translate="load.Ok"></span></button>
                                </div>
                            </form>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="channelAddModal"
         class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
                    <h4 class="modal-title"><span translate="load.channelChoice"></span></h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-6">
                            <form class="form-horizontal bucket-form" ng-submit="channelAdd()">
                                <div class="form-group" style="width: 800px;">
                                    <div class="col-sm-9 icheck ">
                                        <div>
                                            <div style="margin-left: 20px;">
                                                <a ng-click="areaAll(areaLists)"
                                                   class="btn btn-success"><span
                                                        translate="load.checkAll"></span></a>
                                                <a ng-click="deselectAllArea(areaLists)"
                                                   class="btn btn-danger"><span translate="load.deselectAll"></span></a>
                                                <a ng-click="areaVersa()"
                                                   class="btn btn-info"><span
                                                        translate="load.InvertSelection"></span></a>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-sm-9 icheck ">
                                        <div>
                                            <div class="checkbox" data-ng-repeat="site in siteList" align="center">
                                                <input type="checkbox" id={{site.id}} name={{site.code}}
                                                       ng-click="updateSelection($event,site.id,site.code)"
                                                       ng-model="siteModel" ng-checked="master"
                                                       class="chk_1 children">
                                                <label for={{site.id}}>{{site.code}}</label>
                                            </div>

                                        </div>
                                    </div>


                                </div>
                                <div class="modal-footer" style="width: 550px;">
                                    <button type="button" class="btn btn-default" data-dismiss="modal"><span
                                            translate="load.Cancel"></span></button>
                                    <button type="submit" class="btn btn-primary addSubmit"><span
                                            translate="load.Ok"></span></button>
                                </div>
                            </form>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="areaAddModal"
         class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
                    <h4 class="modal-title"><span translate="load.areaChoice"></span></h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-6">
                            <form class="form-horizontal bucket-form" ng-submit="areaAdd()">
                                <%--<div class="form-group" style="width: 800px;">--%>
                                <%--<div class="col-sm-9 icheck ">--%>
                                <%--<div style="margin-left: 20px;">--%>
                                <%--<a ng-click="areaAll(areaLists)"--%>
                                <%--class="btn btn-success"><span translate="load.checkAll"></span></a>--%>
                                <%--<a ng-click="deselectAllArea(areaLists)"--%>
                                <%--class="btn btn-danger"><span translate="load.deselectAll"></span></a>--%>
                                <%--<a ng-click="areaVersa()"--%>
                                <%--class="btn btn-info"><span--%>
                                <%--translate="load.InvertSelection"></span></a>--%>
                                <%--</div>--%>
                                <%--</div>--%>
                                <%--<div class="col-sm-9 icheck ">--%>
                                <%--<div>--%>
                                <%--<div class="checkbox" data-ng-repeat="area in areaLists" align="center">--%>
                                <%--<input type="checkbox" id={{area.areaName}} name={{area.id}}--%>
                                <%--ng-click="updateAreaSelection($event,area.id,area.areaName)"--%>
                                <%--ng-model="areaModel" ng-checked="areaMaster"--%>
                                <%--class="chk_1 areaChildren">--%>
                                <%--<label for={{area.areaName}}>{{area.areaName}}</label>--%>
                                <%--</div>--%>

                                <%--</div>--%>
                                <%--</div>--%>
                                <%--</div>--%>

                                <div class="form-group" style="margin-left: 1px;">
                                    <span class="label label-success" ng-click="areaAll(areaLists)"
                                          style="cursor:pointer" translate="load.checkAll"></span>
                                    <span class="label label-danger" ng-click="deselectAllArea(areaLists)"
                                          translate="load.deselectAll" style="cursor:pointer"></span>
                                    <span class="label label-info" ng-click="areaVersa()" style="cursor:pointer"
                                          translate="load.InvertSelection"></span>
                                </div>
                                <div class="form-group" style="width: 590px;overflow :auto;height: 500px;">
                                    <div class="col-sm-9 icheck ">
                                        <div>
                                            <div data-ng-repeat="area in areaLists track by $index" align="center"
                                                 class="checkList">
                                                <span class="label label-default" name={{area.id}}
                                                      ng-click="updateAreaSelection($event,area.id,area.areaName)"
                                                      style="cursor:pointer">{{area.areaName}}</span>
                                                <br/>
                                                <br/>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                                <%--<div style="width: 550px;text-align:center;">--%>
                                <%--<pager page-count="pageCount" current-page="currentPage"--%>
                                <%--on-page-change="onPageChange()" first-text="load.HomePage"--%>
                                <%--next-text="load.NextPage" prev-text="load.PreviousPage"--%>
                                <%--last-text="load.EndPage" show-goto="true"--%>
                                <%--goto-text="load.GoToPage"></pager>--%>
                                <%--</div>--%>
                                <div class="modal-footer" style="width: 550px;">

                                    <button type="button" class="btn btn-default" data-dismiss="modal"><span
                                            translate="load.Cancel"></span></button>
                                    <button type="submit" class="btn btn-primary addSubmit"><span
                                            translate="load.Ok"></span></button>
                                </div>
                            </form>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="awardPackageModal"
         class="modal fade">
        <div class="modal-dialog" style="width:800px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
                    <h4 class="modal-title">选择奖励包</h4>
                </div>
                <div class="modal-body">
                    <div style="float: left;width:150px;margin-top: 27px;margin-left: 320px;">
                        <form class="searchform" method="post">
                            <select name="awardType" aria-controls="editable-sample" class="form-control awardType"
                                    ng-model="awardType" ng-change="awardChange(awardType)" required>
                                <option value="">请选择奖励类型</option>
                                <option value="1">新手奖励</option>
                                <option value="2">回归礼包</option>
                            </select>
                        </form>
                    </div>
                    <div style="float: right;margin-top: 20px;">
                        <form class="searchform" method="post">
                            <input type="text" class="form-control searchText" name="keyword"
                                   placeholder="Search here..." ng-model="awardSearch"/>
                            <div style="float: left;margin-top: 7px;">
                                <button ng-click="searchAwardClicked()" class="btn btn-primary searchButton">搜索
                                </button>
                            </div>

                        </form>
                    </div>
                    <div class="row">
                        <form class="form-horizontal bucket-form" ng-submit="awardForm()">
                            <table class="table">
                                <thead>
                                <tr>
                                    <th><input type="checkbox" class="checkbox"
                                               data-ng-click="awardCheckClick()" value=""></th>
                                    <th>awardId</th>
                                    <th>awardCount</th>
                                    <th>awardType</th>
                                    <th>awardName</th>
                                    <th>awardDesc</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-repeat="award in awardPackage">
                                    <td align="center"><input type="checkbox" class="checkbox awardCheck"
                                                              data-ng-click="check($index,chk)" data-ng-model="chk"
                                                              value={{award.awardId}} name={{award.awardName}}></td>
                                    <td>{{award.awardId}}</td>
                                    <td>{{award.awardCount}}</td>
                                    <td>{{award.awardType}}</td>
                                    <td>{{award.awardName}}</td>
                                    <td>{{award.awardDesc}}</td>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                            <div class="pager">
                                <pager page-count="pageAwardCount" current-page="currentAwardPage"
                                       on-page-change="onAwardPageChange()" first-text="load.HomePage"
                                       next-text="load.NextPage" prev-text="load.PreviousPage"
                                       last-text="load.EndPage" show-goto="true" goto-text="load.GoToPage"></pager>
                            </div>
                            <div class="modal-footer" style="width:750px;">
                                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                                <button type="submit" class="btn btn-primary awardSubmit"> 确认</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="firstBuyModal"
         class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
                    <h4 class="modal-title">选择区服</h4>
                </div>
                <div class="modal-body">
                    <section class="panel">
                        <div class="panel-body">
                            <aside class="col-md-10">
                                <h4 class="drg-event-title">基本信息</h4>
                            </aside>
                            <form class="form-horizontal bucket-form" ng-submit="firstBuyAdd()">
                                <div class="row">
                                    <div class="col-md-10 form-group">
                                        <label>goodsCode:</label>
                                        <input type="text" class="form-control goodsCode" ng-model="firstGoodsCode">
                                    </div>

                                </div>

                                <div class="row">
                                    <div class="col-md-10 form-group">
                                        <label>firstBuyOperation:</label>
                                        <select name="firstBuyOperation" aria-controls="editable-sample"
                                                class="form-control firstBuyOperation"
                                                ng-change="firstBuyChange(firstBuyOperation)"
                                                ng-model="firstBuyOperation" required>
                                            <option value="">请选择</option>
                                            <option value="*">乘法</option>
                                            <option value="+">加法</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-10 form-group">
                                        <label>operateCount:</label>
                                        <input type="number" class="form-control operateCount"
                                               ng-model="operateCount">
                                    </div>

                                </div>

                                <div class="row">
                                    <aside class="col-md-10">
                                        <h4 class="drg-event-title">奖励礼包</h4>
                                        <div class='external-events'>
                                            <div class='external-event label label-primary'
                                                 data-ng-repeat="code in firstAwardData">{{code}}
                                            </div>
                                            <br/>
                                        </div>
                                    </aside>
                                    <button class="btn btn-warning" type="button" ng-click="firstAwardClick()">
                                        选择奖励包
                                    </button>

                                </div>
                                <div class="modal-footer" style="width: 550px;">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                                    <button type="submit" class="btn btn-primary addSubmit"> 确认</button>
                                </div>
                            </form>


                        </div>
                    </section>


                </div>
            </div>
        </div>
    </div>

    <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="firstAwardModal"
         class="modal fade">
        <div class="modal-dialog" style="width:800px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
                    <h4 class="modal-title">选择奖励包</h4>
                </div>
                <div class="modal-body">
                    <div style="float: left;width:150px;margin-top: 27px;margin-left: 320px;">
                        <form class="searchform" method="post">
                            <select name="firstAwardType" aria-controls="editable-sample"
                                    class="form-control firstAwardType" ng-model="firstAwardType"
                                    ng-change="firstAwardChange(firstAwardType)" required>
                                <option value="">请选择奖励类型</option>
                                <option value="1">新手奖励</option>
                                <option value="2">回归礼包</option>
                            </select>
                        </form>
                    </div>
                    <div style="float: right;margin-top: 20px;">
                        <form class="searchform" method="post">
                            <input type="text" class="form-control searchText" name="keyword"
                                   placeholder="Search here..." ng-model="firstAwardSearch"/>
                            <div style="float: left;margin-top: 7px;">
                                <button ng-click="searchFirstAwardClicked()" class="btn btn-primary searchButton">
                                    搜索
                                </button>
                            </div>

                        </form>
                    </div>
                    <div class="row">
                        <form class="form-horizontal bucket-form" ng-submit="firstAwardForm()">
                            <table class="table">
                                <thead>
                                <tr>
                                    <th><input type="checkbox" class="checkbox"
                                               data-ng-click="firstAwardCheckClick()" value=""></th>
                                    <th>awardId</th>
                                    <th>awardCount</th>
                                    <th>awardType</th>
                                    <th>awardName</th>
                                    <th>awardDesc</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-repeat="award in firstAwardPackage">
                                    <td align="center"><input type="checkbox" class="checkbox firstAwardCheck"
                                                              data-ng-click="firstAwardCheck($index,firstAwardChk)"
                                                              data-ng-model="firstAwardChk" value={{award.awardId}}
                                                              name={{award.awardName}}></td>
                                    <td>{{award.awardId}}</td>
                                    <td>{{award.awardCount}}</td>
                                    <td>{{award.awardType}}</td>
                                    <td>{{award.awardName}}</td>
                                    <td>{{award.awardDesc}}</td>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                            <div class="pager">
                                <pager page-count="pageFirstAwardCount" current-page="currentFirstAwardPage"
                                       on-page-change="onFirstAwardPageChange()" first-text="load.HomePage"
                                       next-text="load.NextPage" prev-text="load.PreviousPage"
                                       last-text="load.EndPage" show-goto="true" goto-text="load.GoToPage"></pager>
                            </div>
                            <div class="modal-footer" style="width:750px;">
                                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                                <button type="submit" class="btn btn-primary firstAwardSubmit"> 确认</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div aria-hidden="true" role="dialog" tabindex="-1" id="messagesModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
                    <h4 class="modal-title" translate="{{messagesData.messagesTitle}}"></h4>
                </div>
                <div class="modal-body" translate="{{messagesData.messagesBody}}">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-dismiss="modal"><span
                            translate="load.Ok"></span></button>
                </div>
            </div>
        </div>
    </div>

    <div aria-hidden="true" role="dialog" tabindex="-1" id="messagesConfirm" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
                    <h4 class="modal-title" translate="{{messagesConfirm.title}}"></h4>
                </div>
                <div class="modal-body" translate="{{messagesConfirm.body}}">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal"><span
                            translate="load.Cancel"></span></button>
                    <button type="button" class="btn btn-warning" data-dismiss="modal" ng-click="confirm()"><span
                            translate="load.Ok"></span></button>
                </div>
            </div>
        </div>
    </div>


    <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="itemModal"
         class="modal fade ">
        <div class="modal-dialog" style="width: 900px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
                    <h4 class="modal-title"><span translate="load.itemSelect"></span></h4>
                </div>
                <div class="modal-body">
                    <div class="clearfix">
                        <div style="float: right;">
                            <form class="searchform" name="searchForms" method="post"
                                  style="float:left;margin-top: 18px;">
                                <input type="number" class="form-control searchText" name="itemCodeSearch"
                                       ng-model="gameToyTypeSearch.itemCode"
                                       placeholder={{placeholderItemCode|translate}}
                                       onkeydown="globelQuery(event)"/>
                                <input type="text" class="form-control searchText"
                                       ng-model="gameToyTypeSearch.itemTypeName"
                                       placeholder={{placeholderItemName|translate}}
                                       onkeydown="globelQuery(event)"/>
                                <div style="float: left;margin-top: 7px;">
                                    <button ng-click="searchButtonClicked()" class="btn btn-primary searchButton">
                                        <span translate="load.search"></span>&nbsp;<i class="fa fa-search"></i>
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <table class="table  table-hover general-table">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th><span translate="load.itemItemCode"></span></th>
                            <th><span translate="load.itemItemName"></span></th>
                            <th><span translate="load.itemItemType"></span></th>
                            <th><span translate="load.itemExtend"></span></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="gameToyType in gameTypeToys" ng-click="itemItemChoice(gameToyType)">
                            <td>{{gameToyType.id}}</td>
                            <td>{{gameToyType.itemCode}}</td>
                            <td>{{gameToyType.itemName}}</td>
                            <td><span ng-show="gameToyType.itemType==1" class="label label-success label-mini">{{gameToyType.itemType|NumToStr16|translate}}</span>
                                <span ng-show="gameToyType.itemType==2" class="label label-danger label-mini">{{gameToyType.itemType|NumToStr16|translate}}</span>
                                <span ng-show="gameToyType.itemType==3" class="label label-primary label-mini">{{gameToyType.itemType|NumToStr16|translate}}</span>
                                <span ng-show="gameToyType.itemType==4" class="label label-default label-mini">{{gameToyType.itemType|NumToStr16|translate}}</span>
                                <span ng-show="gameToyType.itemType==5" class="label label-info label-mini">{{gameToyType.itemType|NumToStr16|translate}}</span>
                            </td>
                            <td>{{gameToyType.itemExtend}} <span style="float: right;color: red;"
                                                                 ng-show="gameToyType.lately==1"
                                                                 translate="load.gameToyTypeLately"></span>
                            </td>

                        </tr>
                        </tbody>
                    </table>
                    <div class="pager">
                        <pager page-count="pageCountItem" current-page="currentPageItem"
                               on-page-change="onPageChangeItem()"
                               first-text="load.HomePage" next-text="load.NextPage" prev-text="load.PreviousPage"
                               last-text="load.EndPage" show-goto="true" goto-text="load.GoToPage"></pager>
                    </div>
                </div>
                <div class="modal-footer" style="width: 900px;">
                    <button type="button" class="btn btn-danger" data-dismiss="modal"><span
                            translate="load.Cancel"></span></button>
                </div>
            </div>
        </div>
    </div>

</div>


