<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="${ctx}/resource/adminex/js/DateTimePicker/DateInit.js"></script>
<%--<script src="${ctx}/resource/adminex/js/multi-select-init.js"></script>--%>
<script src="${ctx}/resource/adminex/js/icheck-init.js"></script>
<script src="${ctx}/resource/adminex/js/ios-switch/switchery.js"></script>
<script src="${ctx}/resource/adminex/js/ios-switch/ios-init.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/resource/adminex/js/ios-switch/switchery.css"/>
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
<script>
    /* jQuery(document).ready(function(){
     $('.wysihtml5').wysihtml5();
     });*/
</script>
<div>
    <div ng-controller="gameActivityConEditController">
        <form role="form" ng-submit="gameActivityConEditForm()" id="gameActivityConEditForm"
              name="gameActivityConEditForms">
            <div class="row">
                <div class="col-sm-6">
                    <section class="panel">
                        <header class="panel-heading">
                            <span translate="load.ApplicationInformation"></span><span id="timeDesc" style="color: red;"
                                                                                       hidden>&nbsp;&nbsp;(&nbsp;<span
                                translate="load.timeDesc"> </span>&nbsp;)</span>
                        </header>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-md-4 form-group labelSpace">
                                    <label><span translate="load.activityType"></span>:<span
                                            style="color: red">*</span></label><span
                                        style="color:red;">&nbsp;&nbsp;(<span
                                        translate="load.activityTypeTip"></span>)</span>
                                    <select name="loadStatus" aria-controls="editable-sample"
                                            class="form-control loadStatus"
                                            ng-model="activityConDatas.type" ng-change="typeChange()"
                                            ng-show="typePd!='update'" required>
                                        <option value="" translate="load.pleaseSelect"></option>
                                        <option value="1" translate="load.routineActivity"></option>
                                        <option value="2" translate="load.CAC"></option>
                                        <option value="3" translate="load.luckyChicken"></option>
                                    </select>
                                    <select name="loadStatus" aria-controls="editable-sample"
                                            class="form-control loadStatus"
                                            ng-model="activityConDatas.type" ng-change="typeChange()"
                                            ng-show="typePd=='update'" disabled required>
                                        <option value="" translate="load.pleaseSelect"></option>
                                        <option value="1" translate="load.routineActivity"></option>
                                        <option value="2" translate="load.CAC"></option>
                                        <option value="3" translate="load.luckyChicken"></option>
                                    </select>
                                </div>
                            </div>
                            <div class="row enableChanges">
                                <div class="col-md-4 form-group labelSpace">
                                    <label><span translate="load.gameActivityConBeginTime"></span>:<span
                                            style="color: red">*</span></label>
                                    <input size="16" type="text" name="startTime" readonly
                                           class="ui_timepicker form-control" ng-model="activityConDatas.beginTime"
                                           required>
                                </div>

                                <div class="col-md-4 form-group labelSpace">
                                    <label><span translate="load.gameActivityConEndTime"></span>:<span
                                            style="color: red">*</span></label>
                                    <input size="16" type="text" name="endTime" readonly
                                           class="ui_timepicker form-control" ng-model="activityConDatas.endTime"
                                           required>
                                </div>

                                <div class="col-md-4 form-group labelSpace">
                                    <label><span translate="load.channelType"></span>:<span
                                            style="color: red">*</span></label><span
                                        style="color:red;">&nbsp;&nbsp;(<span
                                        translate="load.gameActivityConSiteTypeExplain"></span>)</span>
                                    <select name="loadStatus" aria-controls="editable-sample"
                                            class="form-control loadStatus"
                                            ng-model="channelType" ng-change="channelTypeChange()"
                                    >
                                        <option value="" translate="load.OnChangeTip"></option>
                                        <option value="1" translate="load.hybridChannel"></option>
                                        <option value="2" translate="load.hardcoreChannel"></option>
                                        <option value="3" translate="load.myApp"></option>
                                    </select>
                                </div>
                            </div>
                            <div class="row reveal" hidden>
                                <div class="col-md-4 form-group labelSpace">
                                    <label><span translate="load.gameActivityConBeginTime"></span>:<span
                                            style="color: red">*</span></label>
                                    <input size="16" type="text" name="startTime" readonly
                                           class=" form-control" ng-model="activityConDatas.beginTime"
                                           required>
                                </div>

                                <div class="col-md-4 form-group labelSpace">
                                    <label><span translate="load.gameActivityConEndTime"></span>:<span
                                            style="color: red">*</span></label>
                                    <input size="16" type="text" name="endTime" readonly
                                           class=" form-control" ng-model="activityConDatas.endTime"
                                           required>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-4 form-group labelSpace">
                                    <label><span translate="load.gameActivityConRedundancy"></span>:</label>
                                   <textarea class="form-control" style="height: 150px;resize:none;"
                                             ng-model="activityConDatas.redundancy"></textarea>
                                </div>
                                <div class="col-md-4 form-group labelSpace">
                                    <label><span translate="load.GameId"></span>:<span
                                            style="color: red">*</span></label>
                                    <select name="gameList" aria-controls="editable-sample"
                                            class="form-control gameList activityConGameList"
                                            ng-options="game.id as game.code|translate for game in gameList"
                                            ng-change="resourceGameListChange(activityConGameList)"
                                            ng-model="activityConGameList" required>
                                        <option value="" translate="load.pleaseSelect"></option>
                                    </select>
                                </div>
                                <div class="col-md-4 form-group labelSpace">
                                    <label><span translate="load.gameActivityConActivityName"></span>:<span
                                            style="color: red">*</span></label>
                                    <input type="text" class="form-control activityName"
                                           ng-model="activityConDatas.activityName" required/>
                                </div>
                            </div>


                        </div>
                    </section>


                </div>

                <div class="col-sm-6">
                    <section class="panel">
                        <header class="panel-heading">
                            <span translate="load.areaList"></span>
                        </header>

                        <div class="panel-body">
                            <div class="row" style="margin-top: 30px;">
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

                        </div>
                    </section>
                </div>


                <div class="col-md-12" ng-repeat="task in activityConDatas.taskItem"
                     ng-hide="activityConDatas.type == 3">
                    <section class="panel">
                        <header class="panel-heading">
                            <span translate="load.gameActivityConInfo"></span><span>{{task.taskId}}</span>
                        </header>
                        <div class="panel-body">
                            <div class="row" ng-hide="task.taskId == 99">
                                <div class="col-md-3 form-group labelSpace" ng-show="activityConDatas.type!=2">
                                    <label><span translate="load.gameActivityConType"></span>:</label>
                                    <select aria-controls="editable-sample"
                                            ng-options="taskType.value as taskType.key|translate for taskType in taskTypeItem"
                                            class="form-control activityName"
                                            ng-model="task.taskType">
                                        <option value="" translate="load.pleaseSelect"></option>
                                    </select>
                                </div>
                                <div class="col-md-3 form-group labelSpace">
                                    <label><span translate="load.orderTaskTerm1"></span>:</label>
                                    <input type="number" class="form-control"
                                           ng-model="task.taskTerm1" min="0"/>
                                </div>
                                <div class="col-md-3 form-group labelSpace">
                                    <label><span translate="load.orderTaskTerm2"></span>:</label>
                                    <input type="number" class="form-control"
                                           ng-model="task.taskTerm2" min="0"/>
                                </div>
                            </div>
                            <div class="row" ng-hide="task.taskId == 99 || activityConDatas.type==2">
                                <div class="col-md-4 form-group labelSpace">
                                    <label><span translate="load.orderTaskName"></span>:</label>
                                    <input type="text" class="form-control activityName"
                                           ng-model="task.taskName" min="0"/>
                                </div>
                                <div class="col-md-4 form-group labelSpace">
                                    <label><span translate="load.orderTaskDesc"></span>:</label>
                                    <input type="text" class="form-control"
                                           ng-model="task.taskDesc" min="0"/>
                                </div>
                            </div>
                            <div class="clientContent" ng-repeat="item in task.taskValue">
                                <div style="margin-top: 20px;">

                                    <div class="row">
                                        <div class="col-md-3 form-group labelSpace">
                                            <label><span translate="load.itemItemType"></span>:<span style="color:red;">&nbsp;&nbsp;(<span
                                                    translate="load.itemChoiceExplain"></span>)</span></label>
                                            <select name="itemType" aria-controls="editable-sample"
                                                    ng-change="itemTypeChange(item.itemType,$index,$parent.$index)"
                                                    ng-options="itemType.value as itemType.key|translate for itemType in itemTypeList"
                                                    class="form-control itemType" ng-model="item.itemType">
                                                <option value="" translate="load.pleaseSelect"></option>
                                                <%--   <option value="{{itemType.id}}" ng-repeat="itemType in itemTypeList" translate="load.itemType{{itemType.code}}"></option>--%>
                                            </select>
                                        </div>

                                        <div class="prop">
                                            <div class="col-md-3 form-group labelSpace">
                                                <label><span translate="load.applicationItemName"></span>:</label>
                                                <div class="input-group m-bot15">
                                                    <input type="text" class="form-control" ng-model="item.itemName"
                                                           ng-click="itemSelected(item.itemType,$index,$parent.$index)"
                                                           readonly/>
                                              <span class="input-group-btn">
                                                <button class="btn btn-default"
                                                        ng-click="itemSelected(item.itemType,$index,$parent.$index)"
                                                        style="height: 34px;" type="button"><i class="fa fa-search"></i>
                                                </button>
                                              </span>
                                                </div>
                                            </div>

                                            <div class="col-md-3 form-group labelSpace">
                                                <label><span translate="load.applicationItemNum"></span>:</label>
                                                <input type="number" class="form-control" min="0" max="100000000"
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
                                                       ng-model="analog.formValue" {{analog.formRequired}}
                                                       name="{{analog.formCode}}" maxlength="20">
                                            </div>

                                        </div>

                                    </div>

                                    <button type="button" class="btn btn-danger" style="margin-top: 25px;"
                                            href="javascript:" ng-show="task.clientContent.canDescReply"
                                            ng-click="clientDelete($parent.$index,$index)"><span
                                            translate="load.Delte"></span>&nbsp<i class="fa fa-times"></i></button>

                                    <header class="panel-heading">

                                    </header>
                                </div>
                            </div>
                            <div class="clientFoot">
                                <button type="button" class="btn btn-success" href="javascript:"
                                        ng-click="clientAdd($index)"><span
                                        translate="load.add"></span>&nbsp<i class="fa fa-plus"></i></button>
                            </div>

                            <div class="modal-footer taskFoot" ng-show="clientTaskContent.canDescReply">
                                <div style="float: right;margin-right: 20px;" ng-click="clientTaskDelete($index)">
                                    <button type="button" class="btn btn-danger gameActivityConDeleteTask"><span
                                            translate="load.gameActivityConDeleteTask"></span>&nbsp<i
                                            class="fa fa-times"></i></button>
                                </div>

                            </div>

                        </div>

                    </section>
                </div>
                <div class="col-md-12">
                    <section class="panel">
                        <div class="modal-footer taskFoot" ng-hide="activityConDatas.type == 3">
                            <div style="float: right;margin-right: 20px;" ng-click="clientTaskAdd()">
                                <button type="button" class="btn btn-success gameActivityConAddTask"><span
                                        translate="load.gameActivityConAddTask"></span>&nbsp<i class="fa fa-plus"></i>
                                </button>
                            </div>

                        </div>
                        <div class="modal-footer">
                            <div style="float: right;margin-right: 20px;">
                                <button type="button" class="btn btn-default" href="javascript:"
                                        onclick="history.back(); "><span translate="load.Cancel"></span></button>
                                <button type="submit" class="btn btn-primary activityConAddSubmit"><span
                                        translate="load.Ok"></span></button>
                            </div>
                            <p ng-hide="!gameActivityConEditForms.$invalid"
                               style="color: red;float: right;margin-top: 15px;">
                                <span translate="load.remindValidate"></span></p>
                        </div>
                    </section>
                </div>
            </div>
        </form>


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
                            <tr ng-repeat="gameToyType in gameTypeToys" ng-click="itemItemChoice()">
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
                            <pager page-count="pageCount" current-page="currentPage" on-page-change="onPageChange()"
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
    </div>
</div>


