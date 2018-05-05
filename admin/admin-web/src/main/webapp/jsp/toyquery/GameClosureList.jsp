<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="${ctx}/resource/adminex/js/DateTimePicker/DateInit.js"></script>
<script src="${ctx}/resource/adminex/js/multi-select-init.js"></script>
<script src="${ctx}/resource/adminex/js/icheck-init.js"></script>
<div>
    <div class="row" ng-controller="GameClosureController">
        <div class="col-sm-6">
            <section class="panel" style="margin-top: 20px;">
                <header class="panel-heading">
                    <span translate="load.searchCriteria"></span><span style="color: red;">&nbsp;&nbsp;(&nbsp;<span
                        translate="load.playersListFindDesc"> </span>&nbsp;)</span>
                    <span class="tools pull-right">
                       <a href="javascript:;" class="fa fa-chevron-down" onclick="chevronClick(this)"></a>
                    </span>
                </header>
                <div class="panel-body searchCriteria">
                    <div style="float: left;margin-right: 20px;" class="labelSpace">
                        <label><span translate="load.playersListRoleId"></span></label>
                        <input type="number" name="roleId" class="form-control"
                               ng-model="gameClosureSearchData.roleId">
                    </div>
                    <div style="float:left;margin-right: 20px;" class="labelSpace">
                        <label><span translate="load.playersListRoleName"></span></label>
                        <input type="text" class="form-control " ng-model="gameClosureSearchData.roleName"
                               name="keyword"/>
                    </div>
                    <div style="float: left;margin-top: 21px;">
                        <button ng-click="searchButtonClicked()" class="btn btn-primary searchButton"><span
                                translate="load.search"></span>&nbsp;<i class="fa fa-search"></i></button>
                    </div>
                </div>
            </section>
        </div>
        <div class="col-sm-6">
            <section class="panel">
                <div class="panel-body">
                    <div class="clearfix">
                        <div class="btn-group" style="margin-top: 18px;">
                            <a ng-click="addButton()"
                               class="btn btn-success"><span translate="load.gameClosureRoleAdd"></span>&nbsp<i
                                    class="fa fa-plus"></i></a>
                            <a ng-click="addShutUpButton()"
                               class="btn btn-success"><span translate="load.gameClosureRoleShutUpAdd"></span>&nbsp<i
                                    class="fa fa-plus"></i></a>
                        </div>
                    </div>


                    <table class="table  table-hover general-table">
                        <thead>
                        <tr>
                            <th></th>
                            <th><span translate="load.gameClosureRoleId"></span></th>
                            <th><span translate="load.gameClosureRoleName"></span></th>
                            <th><span translate="load.gameClosureUserId"></span></th>
                            <th><span translate="load.gameClosureGameId"></span></th>
                            <th><span translate="load.gameClosureSiteId"></span></th>
                            <th><span translate="load.gameClosureAreaId"></span></th>
                            <th><span translate="load.gameClosureClosureReason"></span></th>
                            <th><span translate="load.gameClosureClosureTime"></span></th>
                            <th><span translate="load.gameClosureRoleStatus"></span></th>
                            <th><span translate="load.operation"></span></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="closure in gameClosureData">
                            <td align="center"><input type="checkbox" class="checkbox"
                                                      data-ng-click="check(closure.id,chk)" data-ng-model="chk"
                                                      value="" style="width:50px;"></td>
                            <td style="vertical-align:middle">{{closure.roleId}}</td>
                            <td style="vertical-align:middle">{{closure.roleName}}</td>
                            <td style="vertical-align:middle">{{closure.userId}}</td>
                            <td style="vertical-align:middle">
                                <span ng-hide="closure.gameId!='1'" class="label label-success label-mini">{{closure.gameId|NumToStr7|translate}}</span>
                                <span ng-hide="closure.gameId=='1'" class="label label-default label-mini">{{closure.gameId|NumToStr7|translate}}</span>
                            </td>
                            <td style="vertical-align:middle">
                                <span ng-hide="closure.siteId!=2"
                                      class="label label-mini"
                                      style="background-color: #769efa">{{closure.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="closure.siteId!=3" class="label label-mini"
                                          style="background-color: #6de90f">{{closure.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="closure.siteId!=4" class="label label-mini"
                                          style="background-color: #565acb">{{closure.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="closure.siteId!=5" class="label  label-mini"
                                          style="background-color: #2fd4cd">{{closure.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="closure.siteId!=6" class="label label-mini"
                                          style="background-color: #00b7ee">{{closure.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="closure.siteId!=7" class="label label-mini"
                                          style="background-color: #b33ac4">{{closure.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="closure.siteId!=8" class="label label-mini"
                                          style="background-color: #962928">{{closure.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="closure.siteId!=9" class="label label-mini"
                                          style="background-color: #adb327">{{closure.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="closure.siteId!=10" class="label label-mini"
                                          style="background-color: #af9312">{{closure.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="closure.siteId!=11" class="label label-mini"
                                          style="background-color: #a693af">{{closure.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="closure.siteId!=12" class="label  label-mini"
                                          style="background-color: #71af5c">{{closure.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="closure.siteId!=13" class="label label-mini"
                                          style="background-color: #acaf17">{{closure.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="closure.siteId!=14" class="label label-mini"
                                          style="background-color: #af6fad">{{closure.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="closure.siteId!=15" class="label label-mini"
                                          style="background-color: #6312af">{{closure.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="closure.siteId==2 || closure.siteId==3 || closure.siteId==4 || closure.siteId==5 || closure.siteId==6 || closure.siteId==7 || closure.siteId==8 || closure.siteId==9 || closure.siteId==10 || closure.siteId==11 || closure.siteId==12 || closure.siteId==13 || closure.siteId==14 || closure.siteId==15"
                                          class="label label-default label-mini">{{closure.siteId|NumToStr9|translate}}</span>
                            </td>
                            <td style="vertical-align:middle">{{closure.areaName}}</td>
                            <td style="vertical-align:middle">{{closure.closureReason}}</td>
                            <td style="vertical-align:middle">{{closure.closureTime}}</td>
                            <td style="vertical-align:middle">
                                <span ng-show="closure.roleStatus==1" class="label label-default label-mini">{{closure.roleStatus|NumToStr25|translate}}</span>
                                <span ng-show="closure.roleStatus==2" class="label label-danger label-mini">{{closure.roleStatus|NumToStr25|translate}}</span>
                            </td>
                            <td style="vertical-align:middle">
                                <a ng-click="outRole(closure.id,closure.roleStatus)"
                                   style="cursor:pointer;color: red"><span translate="load.gameClosureRoleOut"></span><i
                                        class="fa fa-times"></i></a> 
                            </td>
                        </tr>
                        <tr ng-show="gameActivityDatas.length==0">
                            <td colspan="8" style="text-align: center;">
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
                         id="gameRoleModalForSearch" class="modal fade">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×
                                    </button>
                                    <h4 class="modal-title"><span translate="load.add"></span></h4>
                                </div>
                                <div class="modal-body">
                                    <form class="form-horizontal " ng-submit="playersForm()"
                                          id="playersForm"
                                          name="playersForms">

                                        <div class="form-group">
                                            <label class="control-label col-md-2"><span translate="load.GameId"></span>:<span
                                                    style="color: red">*</span></label>
                                            <div class="col-md-9 col-xs-11">
                                                <select name="gameList" aria-controls="editable-sample"
                                                        class="form-control gameList"
                                                        ng-options="game.id as game.code|translate for game in gameList"
                                                        ng-model="playersSearchData.gameId"
                                                        ng-change="gameIdChange(playersSearchData.gameId)" required>
                                                    <option value="" translate="load.pleaseSelect"></option>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="control-label col-md-2"><span
                                                    translate="load.billingOrderRoleId"></span>:<span
                                                    style="color: red">*</span></label>
                                            <div class="col-md-9 col-xs-11 iconic-input right">
                                                <i class="fa fa-times roleIdRed" style="color:red"></i>
                                                <i class="fa fa-check roleIdGreen" style="color:green"></i>
                                                <input type="text" class="form-control"
                                                       maxlength="20" ng-model="playersSearchData.roleId" required>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="control-label col-md-2"><span
                                                    translate="load.playersListRoleName"></span>:<span
                                                    style="color: red">*</span></label>
                                            <div class="col-md-9 col-xs-11 iconic-input right">
                                                <i class="fa fa-times roleNameRed" style="color:red"></i>
                                                <i class="fa fa-check roleNameGreen" style="color:green"></i>
                                                <input type="text" class="form-control"
                                                       maxlength="20" ng-model="playersSearchData.roleName" required>
                                            </div>
                                        </div>


                                        <div class="form-group checkRole">
                                            <label class="control-label col-md-2"></label>
                                            <div class="col-md-9 col-xs-11">
                                                <button type="button" class="btn btn-primary" ng-click="checkRole()"
                                                        style="margin-top: 20px;"><span
                                                        translate="load.checkRole"></span></button>
                                                <button type="button" style="margin-top: 20px;" ng-click="removeRole()"
                                                        class="btn btn-default"
                                                ><span
                                                        translate="load.Empty"></span></button>
                                            </div>
                                        </div>

                                        <div class="form-group roleDataFrom">
                                            <label class="control-label col-md-2"><span
                                                    translate="load.gameClosureClosureTime"></span>:<span
                                                    style="color: red">*</span></label>
                                            <div class="col-md-9 col-xs-11">
                                                <input type="number" class="form-control"
                                                       maxlength="20" ng-model="playersData.closureTime">
                                            </div>
                                        </div>

                                        <div class="form-group roleDataFrom">
                                            <label class="control-label col-md-2"><span
                                                    translate="load.gameClosureClosureReason"></span>:<span
                                                    style="color: red">*</span></label>
                                            <div class="col-md-9 col-xs-11">
                                                <textarea class="form-control closureReason"
                                                          style="height: 150px;resize:none;"
                                                          name="goodsDesc"
                                                          ng-model="playersData.closureReason"></textarea>
                                            </div>
                                        </div>

                                        <div class="modal-footer">
                                            <div style="float: right;margin-right: 20px;">
                                                <button type="button" ng-click="replacement()" class="btn btn-success"
                                                        href="javascript:"
                                                ><span translate="load.ResetForm"></span></button>
                                                <button type="button" ng-click="cancelModel()" class="btn btn-default"
                                                        href="javascript:"
                                                ><span translate="load.Cancel"></span></button>
                                                <button type="submit" class="btn btn-primary roleDataFrom"
                                                ><span
                                                        translate="load.Ok"></span></button>
                                            </div>
                                            <p ng-hide="!playersForms.$invalid"
                                               style="color: red;float: right;margin-top: 15px;"><span
                                                    translate="load.remindValidate"></span></p>
                                        </div>

                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>

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