<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="${ctx}/resource/adminex/js/DateTimePicker/DateInit.js"></script>
<script src="${ctx}/resource/adminex/js/multi-select-init.js"></script>
<script src="${ctx}/resource/adminex/js/icheck-init.js"></script>
<div>
    <div class="row" ng-controller="PlayersQueryController">
        <div class="col-sm-6">
            <section class="panel">
                <div class="panel-body">
                    <div class="clearfix">
                        <div class="btn-group" style="margin-top: 18px;">
                            <a ng-click="addButton()"
                               class="btn btn-success"><span translate="load.gameClosureRoleAdd"></span>&nbsp<i
                                    class="fa fa-plus"></i></a>
                            <a class="btn btn-info" ng-click="pocOpen()"><span translate="load.OBT"></span>&nbsp<i
                                    class="fa fa-flag"></i></a>
                            <a class="btn btn-danger" ng-click="pocClose()"><span translate="load.superEsc"></span>&nbsp<i
                                    class="fa fa-flag"></i></a>
                            <a class="btn btn-danger" ng-click="delete()"><span
                                    translate="load.gameClosureRoleDelete"></span>&nbsp<i
                                    class="fa fa-times"></i></a>
                        </div>
                    </div>


                    <table class="table  table-hover general-table">
                        <thead>
                        <tr>
                            <th></th>
                            <th><span translate="load.playersListRoleId"></span></th>
                            <th><span translate="load.playersListRoleName"></span></th>
                            <th><span translate="load.playersListGameId"></span></th>
                            <th><span translate="load.playersListSiteId"></span></th>
                            <th><span translate="load.playersListAreaId"></span></th>
                            <th><span translate="load.playersListRoleLevel"></span></th>
                            <th><span translate="load.playersListRoleDesc"></span></th>
                            <th><span translate="load.playersListLastLoginTime"></span></th>
                            <th><span translate="load.playersListValidStatus"></span></th>
                            <th><span translate="load.playersListTotalPayAmount"></span></th>
                            <th><span translate="load.operation"></span></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="players in playersListData">
                            <td align="center"><input type="checkbox" class="checkbox"
                                                      data-ng-click="check(activity.id,chk)" data-ng-model="chk"
                                                      value="" style="width:50px;"></td>
                            <td style="vertical-align:middle">{{players.roleId}}</td>
                            <td style="vertical-align:middle">{{players.roleName}}</td>
                            <td style="vertical-align:middle">{{players.gameId}}</td>
                            <td style="vertical-align:middle">{{players.siteId}}</td>
                            <td style="vertical-align:middle">{{players.areaId}}</td>
                            <td style="vertical-align:middle">{{players.roleLevel}}</td>
                            <td style="vertical-align:middle">{{players.roleDesc}}</td>
                            <td style="vertical-align:middle">{{players.lastLoginTime}}</td>
                            <td style="vertical-align:middle">{{players.validStatus}}</td>
                            <td style="vertical-align:middle">{{players.totalPayAmount}}</td>
                            <td style="vertical-align:middle"><a ng-click="updateClicked(players.roleId)"
                                                                 style="cursor:pointer;color: navy"><span
                                    translate="load.update"></span><i class="fa fa-edit"></i></a>
                                <a ng-click="open(activity.id)" ng-hide="activity.validStatus==1"
                                   style="cursor:pointer;"><span translate="load.Open"></span><i
                                        class="fa fa-check"></i></a>
                                <a ng-click="close(activity.id)" ng-hide="activity.validStatus==2"
                                   style="cursor:pointer;color: red"><span translate="load.Close"></span><i
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
                                    <form class="form-horizontal " ng-submit="playersSearchForm()"
                                          id="playersSearchForm"
                                          name="playersSearchForms">

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
                                                    translate="load.LoadReqServerId"></span>:<span
                                                    style="color: red">*</span></label>
                                            <div class="col-md-9 col-xs-11">
                                                <select aria-controls="editable-sample" class="form-control"
                                                        ng-model="playersSearchData.serverId"
                                                        ng-options="area.id as area.areaName for area in areaLists"
                                                        required>
                                                    <option value="" translate="load.pleaseSelect"></option>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="control-label col-md-2"><span
                                                    translate="load.roleSearchRoleId"></span></label>
                                            <div class="col-md-9 col-xs-11">
                                                <input type="text" class="form-control" maxlength="50"
                                                       ng-model="playersSearchData.roleId">
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="control-label col-md-2"><span
                                                    translate="load.roleSearchRoleName"></span></label>
                                            <div class="col-md-9 col-xs-11">
                                                <input type="text" class="form-control" maxlength="50"
                                                       ng-model="playersSearchData.roleName">
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="control-label col-md-2"><span translate="load.userId"></span></label>
                                            <div class="col-md-9 col-xs-11">
                                                <input type="text" class="form-control" maxlength="50"
                                                       ng-model="playersSearchData.userId">
                                            </div>
                                        </div>


                                        <div class="form-group">
                                            <label class="control-label col-md-2"></label>
                                            <div class="col-md-9 col-xs-11">
                                                <button type="button" class="btn btn-default" ng-click="empty()"><span
                                                        translate="load.Empty"></span></button>
                                                <button type="submit" class="btn btn-primary"><span
                                                        translate="load.checkSelect"></span></button>
                                            </div>
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