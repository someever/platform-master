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
<div ng-controller="orderParamEditController">


    <form role="form" ng-submit="orderParamEditForm()" name="orderParamEditForms">
        <div class="row">
            <div class="col-sm-6">
                <section class="panel">
                    <header class="panel-heading">
                        <span translate="load.basicInfo"></span><span style="color: red;">&nbsp;&nbsp;(&nbsp;<span
                            translate="load.billingOrderExplain"> </span>&nbsp;)</span>
                    </header>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-3 form-group labelSpace">
                                <label><span translate="load.billingOrderRoleId"></span>:<span
                                        style="color: red">*</span></label>
                                <div class="iconic-input right">
                                    <i class="fa fa-times roleIdRed" style="color:red"></i>
                                    <i class="fa fa-check roleIdGreen" style="color:green"></i>
                                    <input type="text" class="form-control"
                                           maxlength="20" ng-model="roleSearchData.roleId" required>
                                </div>
                            </div>

                            <div class="col-md-3 form-group labelSpace">
                                <label><span translate="load.playersListRoleName"></span>:<span
                                        style="color: red">*</span></label>
                                <div class="iconic-input right">
                                    <i class="fa fa-times roleNameRed" style="color:red"></i>
                                    <i class="fa fa-check roleNameGreen" style="color:green"></i>
                                    <input type="text" class="form-control"
                                           maxlength="20" ng-model="roleSearchData.roleName" required>
                                </div>
                            </div>

                            <div class="col-md-3 form-group labelSpace">
                                <button type="button" class="btn btn-primary" ng-click="checkRole()"
                                        style="margin-top: 20px;"><span
                                        translate="load.checkRole"></span></button>
                                <button type="button" style="margin-top: 20px;" ng-click="removeRole()"
                                        class="btn btn-default"
                                ><span
                                        translate="load.Empty"></span></button>
                            </div>

                        </div>

                    </div>
                </section>
            </div>


            <div class="col-sm-6">
                <section class="panel">
                    <header class="panel-heading">
                        <span translate="load.orderParamTitle"></span>
                    </header>

                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-3 form-group labelSpace">
                                <label><span translate="load.goodsGoodsDesc"></span>:</label>
                                    <textarea class="form-control goodsDesc" style="height: 150px;resize:none;"
                                              name="goodsDesc" ng-model="orderParamData.orderDesc"></textarea>
                            </div>

                            <div class="col-md-3 form-group labelSpace">
                                <label><span translate="load.GameId"></span>:<span
                                        style="color: red">*</span></label>
                                <select name="gameId" aria-controls="editable-sample" class="form-control gameId"
                                        ng-options="game.id as game.code|translate for game in gameList"
                                        ng-change="resourceGameListChange(gameOrderGameList)"
                                        ng-model="gameOrderGameList" required>
                                    <option value="" translate="load.pleaseSelect"></option>
                                </select>
                            </div>

                            <div class="col-md-3 form-group labelSpace">
                                <label><span translate="load.orderType"></span>:<span
                                        style="color: red">*</span></label>
                                <select name="maintenanceStatus" aria-controls="editable-sample"
                                        class="form-control maintenanceStatus" ng-model="orderParamData.payType"
                                        required>
                                    <option value="" translate="load.pleaseSelect"></option>
                                    <option value="5" translate="load.replenishment"></option>
                                    <option value="4" translate="load.orderParamTitle"></option>
                                </select>
                            </div>

                            <div class="col-md-3 form-group labelSpace">
                                <div class="checkbox single-row" style="margin-top: 23px;">
                                    <input type="checkbox" id="VipExperience" class="checkbox VipExperience"
                                           style="width:50px;cursor:pointer" ng-model="VipExperience"
                                           ng-true-value="true" ng-false-value="false"/>
                                    <div style="margin-top: 23px;"><label for="VipExperience" style="color: red;"
                                                                          translate="load.VipExperience"></label>
                                    </div>
                                </div>
                            </div>

                        </div>


                    </div>
                </section>
            </div>

            <div class="col-sm-6">
                <section class="panel">
                    <header class="panel-heading">
                        <span translate="load.checkGoods"></span>
                    </header>

                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-3 form-group labelSpace">
                                <label><span translate="load.goodsGoodsId"></span>:</label>
                                <input type="text" class="form-control goodsPic" ng-model="orderParamData.goodsId"
                                       readonly>
                            </div>

                            <div class="col-md-3 form-group labelSpace">
                                <label><span translate="load.goodsGoodsName"></span>:</label>
                                <input type="text" class="form-control goodsPic" ng-model="orderParamData.goodsCode"
                                       readonly>
                            </div>

                            <div class="col-md-3 form-group labelSpace">
                                <label><span translate="load.goodsUnitPrice"></span>:</label>
                                <input type="text" class="form-control goodsPic" ng-model="orderParamData.payAmount"
                                       readonly>
                            </div>

                            <div class="col-md-3 form-group labelSpace">
                                <label><span translate="load.goodsGoodsCount"></span>:</label>
                                <input type="text" class="form-control goodsPic" ng-model="orderParamData.goodsCount"
                                       readonly>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-3 form-group labelSpace">
                                <button type="button" class="btn btn-primary" ng-click="goodsClick()"
                                ><span translate="load.checkGoods"></span></button>
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
                            <p ng-hide="!orderParamEditForms.$invalid"
                               style="color: red;float: right;margin-top: 15px;"><span
                                    translate="load.remindValidate"></span></p>
                        </div>

                    </div>
                </section>
            </div>
        </div>
    </form>


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
                                <div class="form-group" style="width: 800px;">
                                    <div class="col-sm-9 icheck ">
                                        <div style="margin-left: 20px;">
                                            <a ng-click="areaAll(areaLists)"
                                               class="btn btn-success"><span translate="load.checkAll"></span></a>
                                            <a ng-click="deselectAllArea(areaLists)"
                                               class="btn btn-danger"><span translate="load.deselectAll"></span></a>
                                            <a ng-click="areaVersa()"
                                               class="btn btn-info"><span
                                                    translate="load.InvertSelection"></span></a>
                                        </div>
                                    </div>
                                    <div class="col-sm-9 icheck ">
                                        <div>
                                            <div class="checkbox" data-ng-repeat="area in areaLists" align="center">
                                                <input type="checkbox" id={{area.areaName}} name={{area.id}}
                                                       ng-click="updateAreaSelection($event,area.id,area.areaName)"
                                                       ng-model="areaModel" ng-checked="areaMaster"
                                                       class="chk_1 areaChildren">
                                                <label for={{area.areaName}}>{{area.areaName}}</label>
                                            </div>

                                        </div>
                                    </div>


                                </div>
                                <div style="width: 550px;text-align:center;">
                                    <pager page-count="pageCount" current-page="currentPage"
                                           on-page-change="onPageChange()" first-text="load.HomePage"
                                           next-text="load.NextPage" prev-text="load.PreviousPage"
                                           last-text="load.EndPage" show-goto="true"
                                           goto-text="load.GoToPage"></pager>
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

    <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="goodsModal"
         class="modal fade ">
        <div class="modal-dialog" style="width: 900px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
                    <h4 class="modal-title"><span translate="load.checkGoods"></span></h4>
                </div>
                <div class="modal-body">
                    <div class="clearfix">
                        <div style="float: right;">
                            <form class="searchform" name="searchForms" method="post"
                                  style="float:left;margin-top: 18px;">
                                <input type="text" class="form-control searchText"
                                       ng-model="billingGoodsSearch.goodsName"
                                       placeholder={{placeholderGoodsName|translate}}
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
                            <th><span translate="load.goodsGoodsId"></span></th>
                            <th><span translate="load.goodsGoodsCode"></span></th>
                            <th><span translate="load.goodsGoodsType"></span></th>
                            <th><span translate="load.goodsGoodsName"></span></th>
                            <th><span translate="load.goodsGoodsDesc"></span></th>
                            <th><span translate="load.goodsGoodsAmount"></span></th>
                            <th><span translate="load.goodsGoodsCount"></span></th>
                            <th><span translate="load.goodsUnitPrice"></span></th>
                            <th><span translate="load.bagValidStatus"></span></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="good in goods" ng-click="goodsChoice(good)">
                            <td style="vertical-align:middle">{{$index + 1}}</td>
                            <td style="vertical-align:middle">{{good.goodsId}}</td>
                            <td style="vertical-align:middle">{{good.goodsCode}}</td>
                            <td style="vertical-align:middle">
                                <span ng-show="good.goodsType==1" class="label label-success label-mini">{{good.goodsType|NumToStr16|translate}}</span>
                                <span ng-show="good.goodsType==2" class="label label-danger label-mini">{{good.goodsType|NumToStr16|translate}}</span>
                                <span ng-show="good.goodsType==3" class="label label-primary label-mini">{{good.goodsType|NumToStr16|translate}}</span>
                                <span ng-show="good.goodsType==4" class="label label-default label-mini">{{good.goodsType|NumToStr16|translate}}</span>
                                <span ng-show="good.goodsType==5" class="label label-info label-mini">{{good.goodsType|NumToStr16|translate}}</span>
                            </td>
                            <td style="vertical-align:middle">{{good.goodsName}}</td>
                            <td style="vertical-align:middle">{{good.goodsDesc}}</td>
                            <td style="vertical-align:middle">{{good.goodsAmount}}</td>
                            <td style="vertical-align:middle">{{good.goodsCount}}</td>
                            <td style="vertical-align:middle">{{good.unitPrice}}</td>
                            <td style="vertical-align:middle">
                                <span ng-show="good.validStatus==1" class="label label-success label-mini">{{good.validStatus|NumToStr23|translate}}</span>
                                <span ng-show="good.validStatus==2" class="label label-danger label-mini">{{good.validStatus|NumToStr23|translate}}</span>
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


<script type="text/javascript">
    function globelQuery(e) {
        if (!e)
            e = window.event;
        if ((e.keyCode || e.which) == 13) {
            $(".searchButton").click();
        }
    }

</script>