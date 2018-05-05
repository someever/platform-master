<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div>
    <div class="row">
        <div class="col-sm-6">
            <section class="panel">
                <div class="panel-body" ng-controller="billingGoodsController">
                    <div class="clearfix">
                        <div class="btn-group">
                            <a ng-click="addButton()"
                               class="btn btn-success"> <span translate="load.add"></span>&nbsp<i
                                    class="fa fa-plus"></i></a>
                            <a class="btn btn-danger" ng-click="delete()"> <span translate="load.batchDelte"></span>&nbsp<i
                                    class="fa fa-times"></i></a>
                            <%--  <a class="btn btn-info" ng-click="packageAdd()"> <span translate="load.oneKeyPackage"></span>&nbsp<i class="fa fa-flag"></i></a>
                              <a class="btn btn-info" ng-click="firstBuyAdd()"> <span translate="load.oneFirstBuyPolicy"></span>&nbsp<i class="fa fa-flag"></i></a>--%>
                            <a class="btn btn-info" ng-click="goodsKeyStores()"> <span
                                    translate="load.goodsKeyStores"></span>&nbsp<i class="fa fa-flag"></i></a>
                            <a class="btn btn-danger" ng-click="goodsKeySoldOut()"> <span
                                    translate="load.goodsKeySoldOut"></span>&nbsp<i class="fa fa-flag"></i></a>
                            <%--<a class="btn btn-info" ng-click="goodsKeyOpenFirstBuy()"> <span--%>
                                    <%--translate="load.goodsKeyOpenFirstBuy"></span>&nbsp<i class="fa fa-flag"></i></a>--%>
                            <%--<a class="btn btn-danger" ng-click="goodsKeyCloseFirstBuy()"> <span--%>
                                    <%--translate="load.goodsKeyCloseFirstBuy"></span>&nbsp<i class="fa fa-flag"></i></a>--%>
                            <a class="btn btn-info" ng-click="refreshBillingGoodsCache()"> <span
                                    translate="load.refreshCache"></span>&nbsp<i class="fa fa-flag"></i></a>
                        </div>


                        <div style="float: right;">
                            <form class="searchform" method="post">
                                <input type="text" class="form-control searchText" name="keyword"
                                       placeholder={{goodsSearchText|translate}} ng-model="billingGoodsSearch.goodsName"
                                       onkeydown="globelQuery(event)"/>
                                <div style="float: left;margin-top: 7px;margin-left: 20px;">
                                    <button ng-click="searchButtonClicked()" class="btn btn-primary searchButton"><span
                                            translate="load.easyRetrieval"></span>&nbsp;<i class="fa fa-search"></i>
                                    </button>
                                </div>

                            </form>
                        </div>
                    </div>


                    <table class="table  table-hover general-table">
                        <thead>
                        <tr>
                            <th><input type="checkbox" class="checkbox allCheckData"
                                       data-ng-click="allCheckList(chk,goods)" data-ng-model="chk"
                                       value="" style="width:50px;"></th>
                            <th>#</th>
                            <th><span translate="load.goodsGoodsId"></span></th>
                            <th><span translate="load.goodsGoodsCode"></span></th>
                            <th><span translate="load.goodsGoodsType"></span></th>
                            <th><span translate="load.goodsGoodsName"></span></th>
                            <th><span translate="load.goodsGoodsDesc"></span></th>
                            <th><span translate="load.goodsGoodsAmount"></span></th>
                            <th><span translate="load.goodsGoodsCount"></span></th>
                            <th><span translate="load.goodsUnitPrice"></span></th>
                            <th><span translate="load.goodsGoodsMarkTime"></span></th>
                            <th><span translate="load.goodsRelatedGoodsId"></span></th>
                            <th><span translate="load.goodsDisaccount"></span></th>
                            <th><span translate="load.goodsCreateTime"></span></th>
                            <th><span translate="load.bagValidStatus"></span></th>
                            <th><span translate="load.operation"></span></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="good in goods">
                            <td><input type="checkbox" class="checkbox"
                                       data-ng-click="check($index,chk)" data-ng-model="chk" value=""
                                       style="width:50px;"></td>
                            <td style="vertical-align:middle">{{$index + 1}}</td>
                            <td style="vertical-align:middle">{{good.goodsId}}</td>
                            <td style="vertical-align:middle">{{good.goodsCode}}</td>
                            <td style="vertical-align:middle">
                                <span ng-show="good.shopType==1" class="label label-success label-mini">{{good.shopType|NumToStr27|translate}}</span>
                                <span ng-show="good.shopType==2" class="label label-danger label-mini">{{good.shopType|NumToStr27|translate}}</span>
                                <span ng-show="good.shopType==3" class="label label-primary label-mini">{{good.shopType|NumToStr27|translate}}</span>
                            </td>
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
                            <td style="vertical-align:middle">{{good.goodsMarkTime|date:'yyyy-MM-dd HH:mm:ss'}}</td>
                            <td style="vertical-align:middle">{{good.relatedGoodsId}}</td>
                            <td style="vertical-align:middle">{{good.disaccount}}</td>
                            <td style="vertical-align:middle">{{good.createTime|date:'yyyy-MM-dd HH:mm:ss'}}</td>
                            <td style="vertical-align:middle">
                                <span ng-show="good.validStatus==1" class="label label-success label-mini">{{good.validStatus|NumToStr23|translate}}</span>
                                <span ng-show="good.validStatus==2" class="label label-danger label-mini">{{good.validStatus|NumToStr23|translate}}</span>
                            </td>

                            <%--<td>{{gameArea.clientEnterAddr}}</td>
                            <td>{{gameArea.serverEnterAddr}}</td>--%>
                            <td><a ng-click="updateClicked()" style="cursor:pointer;color: navy"><span
                                    translate="load.update"></span><i class="fa fa-edit"></i></a>
                            <td>
                        </tr>
                        <tr ng-show="goods.length==0">
                            <td colspan="14" style="text-align: center;">
                                <span translate="load.dataTableWarn" style="color: red"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <div class="pager">
                        <pager page-count="pageCount" current-page="currentPage" on-page-change="onPageChange()"
                               first-text="load.HomePage" next-text="load.NextPage" prev-text="load.PreviousPage"
                               last-text="load.EndPage" show-goto="true" goto-text="load.GoToPage"></pager>
                    </div>

                    <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1"
                         id="firstBuyModal" class="modal fade">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×
                                    </button>
                                    <h4 class="modal-title" translate="load.goodsConfigurationFirstBuyPolicy"></h4>
                                </div>
                                <div class="modal-body">

                                    <form role="form" ng-submit="firstBuyForm()">
                                        <div class="form-group labelSpace">
                                            <label><span translate="load.GameId"></span>:<span
                                                    style="color: red">*</span></label>
                                            <select name="gameId" aria-controls="editable-sample"
                                                    class="form-control gameId"
                                                    ng-options="game.id as game.code|translate for game in gameList"
                                                    ng-change="resourceGameListChange(gameToyGameList)"
                                                    ng-model="gameToyGameList" required>
                                                <option value="" translate="load.pleaseSelect"></option>
                                            </select>
                                        </div>
                                        <div class="form-group labelSpace">
                                            <label translate="load.goodsSetTheRatio"></label>
                                            <input type="number" class="form-control" name="goodsCount"
                                                   ng-model="goodsData.goodsCount" maxlength="50" min="0"
                                                   max="100000000000"
                                                   required>
                                        </div>
                                        <div class="form-group">
                                            <div class="checkbox single-row"
                                                 style="margin-top: 23px;margin-left: -15px;">
                                                <input type="checkbox" id="goodsFirstBuyPolicySwitch"
                                                       class="checkbox goodsFirstBuyPolicySwitch"
                                                       style="width:50px;cursor:pointer"
                                                       ng-model="gameAreasData.goodsFirstBuyPolicySwitch"
                                                       ng-change="valueChange()"
                                                       ng-true-value="true" ng-false-value="false"/>
                                                <div style="margin-top: 23px;"><label for="goodsFirstBuyPolicySwitch"
                                                                                      style="color: red;"
                                                                                      translate="load.goodsFirstBuyPolicySwitch"></label>
                                                </div>
                                            </div>
                                        </div>

                                        <header class="panel-heading">
                                            <span translate="load.applicationEmailContent"
                                                  style="margin-top: 23px;margin-left: -15px;"></span><span
                                                style="color:red;">&nbsp;&nbsp;(<span
                                                translate="load.articleAttention"></span>)</span>
                                        </header>

                                        <div class="form-group labelSpace">
                                            <div class="clientContent" ng-repeat="item in articleEditData.item">
                                                <div style="margin-top: 20px;">

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
                                                                           ng-click="itemSelected(item.itemType,$index)"
                                                                           readonly/>
                                              <span class="input-group-btn">
                                                <button class="btn btn-default"
                                                        ng-click="itemSelected(item.itemType,$index)"
                                                        style="height: 34px;" type="button"><i class="fa fa-search"></i>
                                                </button>
                                              </span>
                                                                </div>
                                                            </div>

                                                            <div class="col-md-3 form-group labelSpace">
                                                                <label><span translate="load.applicationItemNum"></span>:</label>
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
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal"><span
                                                    translate="load.Cancel"></span></button>
                                            <button type="submit" class="btn btn-primary addSubmit"><span
                                                    translate="load.Ok"></span></button>
                                            <p ng-hide="!gameToyTypeEditForm.$invalid"
                                               style="color: red;margin-top: 15px;"><span
                                                    translate="load.remindValidate"></span></p>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>

                    <%--   <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1"
                            id="gameAreaModalForAdd" class="modal fade">
                           <div class="modal-dialog">
                               <div class="modal-content">
                                   <div class="modal-header">
                                       <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×
                                       </button>
                                       <h4 class="modal-title">添加</h4>
                                   </div>
                                   <div class="modal-body">

                                       <form role="form" ng-submit="gameAreaAddForm()">

                                           <div class="form-group">

                                           </div>
                                           <div class="form-group">
                                               <div class="col-md-6 form-group">
                                                   <input type="number" class="form-control code" name="codeFrom"
                                                          placeholder="Enter code<1~50>" ng-model="gameToyAddData.codeFrom"
                                                          required>
                                               </div>
                                               <div class="col-md-6 form-group">
                                                   <input type="number" class="form-control code" name="codeEnd"
                                                          placeholder="Enter code<1~50>" ng-model="gameToyAddData.codeEnd"
                                                          required>
                                               </div>

                                           </div>

                                           <div class="form-group">
                                               <label>gameId:</label>
                                               &lt;%&ndash;  <input type="text" class="form-control gameId" name="gameId" placeholder="Enter gameId" ng-model="gameToyAddData.gameId" required>&ndash;%&gt;
                                               &lt;%&ndash;<select name="gameList" aria-controls="editable-sample" class="form-control gameList" ng-model="gameToyAddData.gameId" required>&ndash;%&gt;
                                               &lt;%&ndash;<option ng-repeat="game in gameList" value="game.id">{{game.code}}</option>&ndash;%&gt;
                                               &lt;%&ndash;</select>&ndash;%&gt;

                                               <select name="gameList" aria-controls="editable-sample"
                                                       class="form-control gameList"
                                                       ng-options="game.id as game.code for game in gameList"
                                                       ng-change="resourceGameListChange(gameToyGameList)"
                                                       ng-model="gameToyGameList" required>
                                                   <option value="">请选择</option>
                                               </select>
                                           </div>
                                           <div class="form-group">
                                               <label>siteId:</label>
                                               &lt;%&ndash;<input type="text" class="form-control siteId" name="type" placeholder="Enter siteId" ng-model="gameToyAddData.siteId" required>&ndash;%&gt;
                                               &lt;%&ndash;<select name="siteList" aria-controls="editable-sample" class="form-control siteList gameToy"  ng-model="gameToyAddData.siteId" required>
                                                   <option ng-repeat="site in siteList" value="site.id">{{site.code}}</option>
                                               </select>&ndash;%&gt;

                                               <select name="siteList" aria-controls="editable-sample"
                                                       class="form-control siteList"
                                                       ng-options="site.id as site.code for site in siteList"
                                                       ng-change="resourceSiteListChange(gameToySiteList)"
                                                       ng-model="gameToySiteList" required>
                                                   <option value="">请选择</option>
                                               </select>
                                           </div>

                                           &lt;%&ndash;<div class="form-group">
                                               <label>bindUid:</label>
                                               <input type="text" class="form-control bindUid" name="bindUid" placeholder="Enter bindUid" ng-model="gameToyAddData.bindUid" required>
                                           </div>
                                           <div class="form-group">
                                               <label>bindGame:</label>
                                               <input type="text" class="form-control bindGame" name="bindGame" placeholder="Enter bindGame" ng-model="gameToyAddData.bindGame" required>
                                           </div>

                                           <div class="form-group">
                                               <label>bindSite:</label>
                                               <input type="text" class="form-control bindSite" name="bindSite" placeholder="Enter bindSite" ng-model="gameToyAddData.bindSite" required>
                                           </div>
                                           <div class="form-group">
                                               <label>bindArea:</label>
                                               <input type="text" class="form-control bindArea" name="bindArea" placeholder="Enter bindArea" ng-model="gameToyAddData.bindArea" required>
                                           </div>
                                           <div class="form-group">
                                               <label>bindStatus:</label>
                                               <input type="text" class="form-control bindStatus" name="bindStatus" placeholder="Enter bindStatus" ng-model="gameToyAddData.bindStatus" required>
                                           </div>
                                           <div class="form-group">
                                               <label>bindTime:</label>
                                               <input type="date" class="form_datetime form-control bindTime" name="bindTime" placeholder="Enter bindTime" ng-model="gameToyAddData.bindTime" required>
                                           </div>&ndash;%&gt;
                                           <button type="submit" class="btn btn-primary addSubmit">添加</button>


                                       </form>
                                   </div>
                               </div>
                           </div>
                       </div>

                       <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1"
                            id="firstBuyModal" class="modal fade">
                           <div class="modal-dialog">
                               <div class="modal-content">
                                   <div class="modal-header">
                                       <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×
                                       </button>
                                       <h4 class="modal-title">一键配置首充策略</h4>
                                   </div>
                                   <div class="modal-body">

                                       <form role="form" ng-submit="firstBuyForm()">
                                           <div class="form-group">
                                               <label>默认设置倍数:</label>
                                               <input type="text" class="form-control" name="firstBuyOperation" value="*"
                                                      readonly>
                                           </div>
                                           <div class="form-group">
                                               <label>operateCount:</label>
                                               <input type="number" class="form-control" name="operateCount"
                                                      ng-model="firstBuy.operateCount" required>
                                           </div>
                                           <button type="submit" class="btn btn-primary updateSubmit">修改</button>
                                       </form>
                                   </div>
                               </div>
                           </div>
                       </div>

                       <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="packageModal"
                            class="modal fade">
                           <div class="modal-dialog">
                               <div class="modal-content">
                                   <div class="modal-header">
                                       <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×
                                       </button>
                                       <h4 class="modal-title">一键配置奖励包</h4>
                                   </div>
                                   <div class="modal-body">

                                       <form role="form" ng-submit="packageForm()">
                                           <div class="form-group">
                                               <label>请选择奖励包类型:</label>
                                               <select name="packageType" aria-controls="editable-sample"
                                                       class="form-control packageType"
                                                       ng-options="type.id as type.code for type in packageTypeList"
                                                       ng-change="packageTypeChange(packageType)" ng-model="packageType"
                                                       required>
                                                   <option value="">请选择</option>
                                               </select>
                                           </div>

                                           <div class="form-group">
                                               <label>packageDesc:</label>
                                               <input type="text" class="form-control" name="packageDesc"
                                                      ng-model="package.packageDesc" required>
                                           </div>

                                           <div class="form-group packageValue" hidden="hidden">
                                               <label>packageValue:</label>
                                               <input type="number" class="form-control" name="packageValue"
                                                      ng-model="package.packageValue">
                                           </div>

                                           <div class="form-group goodsItems" hidden="hidden">
                                               <label>goodsItems:</label>
                                               <table class="display table table-bordered table-striped dataTable"
                                                      style="width: 300px;height: 10px;">
                                                   <thead>
                                                   <tr>
                                                       <th></th>
                                                       <th>#</th>
                                                       <th>itemName</th>
                                                   </tr>
                                                   </thead>
                                                   <tbody>
                                                   <tr ng-repeat="good in goods">
                                                       <td align="center"><input type="checkbox" class="checkbox itemCheck"
                                                                                 data-ng-click="itemCheck($index,chk)"
                                                                                 data-ng-model="chk" value=""></td>
                                                       <td>{{$index + 1}}</td>
                                                       <td>{{good.goodsId}}</td>
                                                   </tr>
                                                   </tbody>
                                               </table>
                                           </div>

                                           <div class="modal-footer">
                                               <button type="button" class="btn btn-default" data-dismiss="modal">取消
                                               </button>
                                               <button type="submit" class="btn btn-warning"> 确认</button>
                                           </div>
                                       </form>
                                   </div>
                               </div>
                           </div>
                       </div>--%>

                    <div aria-hidden="true" role="dialog" tabindex="-1" id="messagesModal" class="modal fade">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×
                                    </button>
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
                                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×
                                    </button>
                                    <h4 class="modal-title" translate="{{messagesConfirm.title}}"></h4>
                                </div>
                                <div class="modal-body" translate="{{messagesConfirm.body}}">
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal"><span
                                            translate="load.Cancel"></span></button>
                                    <button type="button" class="btn btn-warning" data-dismiss="modal"
                                            ng-click="confirm()"><span translate="load.Ok"></span></button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </section>
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