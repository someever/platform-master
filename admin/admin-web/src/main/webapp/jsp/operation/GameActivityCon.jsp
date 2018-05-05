<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="${ctx}/resource/adminex/js/DateTimePicker/DateInit.js"></script>
<script src="${ctx}/resource/adminex/js/multi-select-init.js"></script>
<script src="${ctx}/resource/adminex/js/icheck-init.js"></script>
<div>
    <div class="row" ng-controller="GameActivityConController">
        <div class="col-sm-6">
            <section class="panel">
                <div class="panel-body">
                    <div class="clearfix">
                        <div class="btn-group" style="margin-top: 18px;">
                            <a ng-click="addButton()"
                               class="btn btn-success"><span translate="load.add"></span>&nbsp<i class="fa fa-plus"></i></a>
                            <a class="btn btn-danger" ng-click="delete()"><span translate="load.batchDelte"></span>&nbsp<i
                                    class="fa fa-times"></i></a>
                            <a ng-click="LeiJiMuBanButton()"
                               class="btn btn-success"><span translate="load.CumulativeRechargeTemplates"></span>&nbsp<i
                                    class="fa fa-plus"></i></a>
                            <a ng-click="DanBiMuBanButton()"
                               class="btn btn-success"><span translate="load.ASingleRechargeTemplate"></span>&nbsp<i
                                    class="fa fa-plus"></i></a>
                            <a ng-click="LeiJiXiaoFeiButton()"
                               class="btn btn-success"><span translate="load.CumulativeConsumptionTemplate"></span>&nbsp<i
                                    class="fa fa-plus"></i></a>
                            <a ng-click="MeiRiChongZhiButton()"
                               class="btn btn-success"><span translate="load.DailyPrepaidPhone"></span>&nbsp<i
                                    class="fa fa-plus"></i></a>
                            <a ng-click="batchRemoveArea()"
                               class="btn btn-danger"><span translate="load.batchRemoveArea"></span>&nbsp<i
                                    class="fa fa-times"></i></a>
                            <a ng-click="MuBanEdit()"
                               class="btn btn-success"><span translate="load.muBanEditTip"></span>&nbsp<i
                                    class="fa fa-plus"></i></a>
                            <a ng-click="MuBanGet()"
                               class="btn btn-success"><span translate="load.muBanGet"></span>&nbsp<i
                                    class="fa fa-plus"></i></a>
                        </div>
                    </div>


                    <table class="table  table-hover general-table">
                        <thead>
                        <tr>
                            <th></th>
                            <th><span translate="load.gameActivityConId"></span></th>
                            <th><span translate="load.gameActivityConActivityName"></span></th>
                            <th><span translate="load.gameActivityConGameId"></span></th>
                            <th><span translate="load.gameActivityConBeginTime"></span></th>
                            <th><span translate="load.gameActivityConEndTime"></span></th>
                            <th><span translate="load.gameActivityConCreateTime"></span></th>
                            <th><span translate="load.operation"></span></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="activityCon in gameActivityConDatas">
                            <td><input type="checkbox" class="checkbox dataCheck"
                                       data-ng-click="check(activityCon.id,chk,activityCon.beginTime,this)"
                                       data-ng-model="chk"
                                       value="" style="width:50px;"></td>
                            <td style="vertical-align:middle">{{activityCon.id}}</td>
                            <td style="vertical-align:middle">{{activityCon.activityName}}</td>
                            <td style="vertical-align:middle">
                                <span ng-hide="activityCon.gameId!='1'"
                                      class="label label-success label-mini">{{activityCon.gameId|NumToStr7|translate}}</span>
                                <span ng-hide="activityCon.gameId=='1'" class="label label-default label-mini">{{activityCon.gameId|NumToStr7|translate}}</span>
                            </td>
                            <td style="vertical-align:middle">{{activityCon.beginTime|date:'yyyy-MM-dd HH:mm:ss'}}</td>
                            <td style="vertical-align:middle">{{activityCon.endTime|date:'yyyy-MM-dd HH:mm:ss'}}</td>
                            <td style="vertical-align:middle">{{activityCon.createTime|date:'yyyy-MM-dd HH:mm:ss'}}</td>
                            <td style="vertical-align:middle"><a ng-click="updateClicked(activityCon.id)"
                                                                 style="cursor:pointer;color: navy"><span
                                    translate="load.update"></span><i class="fa fa-edit"></i></a>
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


                    <%--<div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1"--%>
                         <%--id="batchRemoveArea" class="modal fade">--%>
                        <%--<div class="modal-dialog">--%>
                            <%--<div class="modal-content">--%>
                                <%--<div class="modal-header">--%>
                                    <%--<button aria-hidden="true" data-dismiss="modal" class="close" type="button">×--%>
                                    <%--</button>--%>
                                    <%--<h4 class="modal-title"><span translate="load.batchRemoveArea"></span></h4>--%>
                                <%--</div>--%>
                                <%--<div class="modal-body">--%>
                                    <%--<div class="row">--%>
                                        <%--<div class="col-md-6">--%>
                                            <%--<form role="form" ng-submit="batchRemoveAreaForms()"--%>
                                                  <%--name="gameToyTypeEditForm">--%>
                                                <%--<div class="form-group" style="margin-left: 1px;">--%>
                                                    <%--<span class="label label-success" ng-click="areaAll(areaLists)"--%>
                                                          <%--style="cursor:pointer" translate="load.checkAll"></span>--%>
                                                    <%--<span class="label label-danger"--%>
                                                          <%--ng-click="deselectAllArea(areaLists)"--%>
                                                          <%--translate="load.deselectAll" style="cursor:pointer"></span>--%>
                                                    <%--<span class="label label-info" ng-click="areaVersa()"--%>
                                                          <%--style="cursor:pointer"--%>
                                                          <%--translate="load.InvertSelection"></span>--%>
                                                <%--</div>--%>
                                                <%--<div class="form-group"--%>
                                                     <%--style="width: 590px;overflow :auto;height: 500px;">--%>
                                                    <%--<div class="col-sm-9 icheck ">--%>
                                                        <%--<div>--%>
                                                            <%--<div data-ng-repeat="area in areaLists track by $index"--%>
                                                                 <%--align="center" class="checkList">--%>
                                                                <%--<span class="label label-default" name={{area.id}}--%>
                                                                      <%--ng-click="updateAreaSelection($event,area.id,area.areaName)"--%>
                                                                      <%--style="cursor:pointer">{{area.areaName}}</span>--%>
                                                                <%--<br/>--%>
                                                                <%--<br/>--%>
                                                            <%--</div>--%>

                                                        <%--</div>--%>
                                                    <%--</div>--%>
                                                <%--</div>--%>

                                                <%--<div class="modal-footer" style="width: 550px;">--%>

                                                    <%--<button type="button" class="btn btn-default"--%>
                                                            <%--data-dismiss="modal"><span--%>
                                                            <%--translate="load.Cancel"></span></button>--%>
                                                    <%--<button type="submit" class="btn btn-primary addSubmit"><span--%>
                                                            <%--translate="load.Ok"></span></button>--%>
                                                <%--</div>--%>
                                            <%--</form>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>

                    <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="batchRemoveArea"
                         class="modal fade">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
                                    <h4 class="modal-title"><span translate="load.batchRemoveArea"></span></h4>
                                </div>
                                <div class="modal-body">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <form class="form-horizontal bucket-form" ng-submit="batchRemoveAreaForms()" name="gameToyTypeEditForm">
                                                <div class="form-group" style="margin-left: 1px;">
                                                    <span class="label label-success"  ng-click="areaAll(areaLists)" style="cursor:pointer" translate="load.checkAll"></span>
                                                    <span class="label label-danger"  ng-click="deselectAllArea(areaLists)" translate="load.deselectAll" style="cursor:pointer"></span>
                                                    <span class="label label-info"  ng-click="areaVersa()" style="cursor:pointer" translate="load.InvertSelection"></span>
                                                </div>
                                                <div class="form-group" style="width: 590px;overflow :auto;height: 500px;" >
                                                    <div class="col-sm-9 icheck ">
                                                        <div>
                                                            <div  data-ng-repeat="area in areaLists track by $index" align="center" class="checkList">
                                                                <span class="label label-default" name={{area.id}} ng-click="updateAreaSelection($event,area.id,area.areaName)" style="cursor:pointer">{{area.areaName}}</span>
                                                                <br/>
                                                                <br/>
                                                            </div>

                                                        </div>
                                                    </div>
                                                </div>
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