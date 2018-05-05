<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="${ctx}/resource/adminex/js/DateTimePicker/DateInit.js"></script>
<%--<script src="${ctx}/resource/adminex/js/multi-select-init.js"></script>--%>
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
<script>
    /* jQuery(document).ready(function(){
     $('.wysihtml5').wysihtml5();
     });*/
</script>
<section>
    <div ng-controller="noticeEditController">
        <form role="form" ng-submit="noticeAddForm()" id="noticeForm" name="noticeForm">
            <div class="row">
                <div class="col-sm-6">
                    <section class="panel">
                        <header class="panel-heading">
                            <span translate="load.basicInfo"></span>
                        </header>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-md-4 form-group labelSpace">
                                    <label><span translate="load.noticeNoticeTitle"></span>:<span
                                            style="color: red">*</span></label>
                                    <input type="text" class="form-control" maxlength="20"
                                           ng-model="noticeData.noticeTitle" required>
                                </div>
                                <div class="col-md-4 form-group labelSpace">
                                    <label><span translate="load.noticeNoticeType"></span>:<span
                                            style="color: red">*</span></label>
                                    <select name="noticeType" aria-controls="editable-sample"
                                            class="form-control noticeType" ng-model="noticeData.noticeTypes"
                                            ng-change="noticeTypeChange(noticeData.noticeTypes)" required>
                                        <option value="" translate="load.pleaseSelect"></option>
                                        <option value="1" translate="load.noticePopup"></option>
                                        <option value="2" translate="load.noticeRoll"></option>
                                        <option value="3" translate="load.noticeSystem"></option>
                                        <option value="4" translate="load.noticeWorld"></option>
                                    </select>
                                </div>

                                <div class="col-md-4 form-group labelSpace">
                                    <label><span translate="load.GameId"></span>:<span
                                            style="color: red">*</span></label>
                                    <select ng-disabled="accessForbidden" name="gameList"
                                            aria-controls="editable-sample"
                                            class="form-control gameList"
                                            ng-options="game.id as game.code|translate for game in gameList"
                                            ng-change="resourceGameListChange(noticeGameList)" ng-model="noticeGameList"
                                            required>
                                        <option value="" translate="load.pleaseSelect"></option>
                                    </select>
                                </div>
                            </div>
                            <div class="row extra" hidden="hidden">
                                <div class="col-md-3 form-group labelSpace">
                                    <label><span translate="load.startTime"></span>:</label>
                                    <input size="16" type="text" readonly class="ui_timepicker form-control"
                                           ng-model="noticeData.beginTime">
                                </div>

                                <div class="col-md-3 form-group labelSpace">
                                    <label><span translate="load.endTime"></span>:</label>
                                    <input size="16" type="text" readonly class="ui_timepicker form-control"
                                           ng-model="noticeData.endTime">
                                </div>
                                <div class="col-md-3 form-group labelSpace">
                                    <label><span translate="load.noticePublishCountLable"></span>:</label>
                                    <input type="number" class="form-control" name="publishCount" min="0"
                                           max="100000000000" ng-model="noticeData.publishCount">
                                </div>

                                <div class="col-md-3 form-group labelSpace">
                                    <label><span translate="load.noticePublishCount"></span>:</label>
                                    <input type="number" class="form-control" name="publishSpace" min="0"
                                           max="100000000000" ng-model="noticeData.publishSpace">
                                </div>


                            </div>


                        </div>

                    </section>
                </div>

                <div class="col-sm-6">
                    <section class="panel">
                        <header class="panel-heading">
                            <span translate="load.channelorarea"></span><span style="color:red;">&nbsp;&nbsp;(<span
                                translate="load.noticeUpdateAccessForbidden"></span>)</span>
                        </header>

                        <div class="panel-body">
                            <div class="row" style="margin-top: 30px;">
                                <aside class="col-md-3">
                                    <h4 class="drg-event-title"><span translate="load.channelList"></span></h4>
                                    <div id='external-events'>
                                        <div class='external-event label label-primary'
                                             data-ng-repeat="code in channelCode track by $index" translate={{code}}></div>
                                        <br/>
                                    </div>
                                </aside>
                                <button class="btn btn-warning" type="button" ng-click="channelClick()"><span
                                        translate="load.channelChoice"></span></button>


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


                <div class="col-md-12">
                    <section class="panel">
                        <header class="panel-heading">
                            <span translate="load.noticeNoticeContent"></span><span style="color: red">*</span>
                             <span class="tools pull-right">
                                <a class="fa fa-chevron-down" href="javascript:;"></a>
                                <a class="fa fa-times" href="javascript:;"></a>
                             </span>
                        </header>
                        <div class="panel-body">
                            <div class="noticeText noticeUeditor">
                                <textarea ng-model="noticeData.noticeContent" placeholder={{noticeHint|translate}}
                                          style="width: 1000px;height: 300px;resize: vertical;"></textarea>
                            </div>

                            <div class="noticeTextItem" hidden>
                                <div class="clientContent" ng-repeat="notice in noticeData.noticeItem">
                                    <div style="margin-top: 20px;">
                                        <div class="row">
                                            <div class="col-md-3 form-group labelSpace">
                                                <label><span translate="load.noticeLittleType"></span>:</label>
                                                <select name="noticeLittleType" aria-controls="editable-sample"
                                                        class="form-control noticeLittleType"
                                                        ng-model="notice.noticeLittleType">
                                                    <option value="" translate="load.pleaseSelect"></option>
                                                    <option value="1" translate="load.noticeHomeTypeNotice"></option>
                                                    <option value="2" translate="load.noticeHomeTypeActivity"></option>
                                                    <option value="3" translate="load.noticeHomeTypeRenewal"></option>
                                                </select>
                                            </div>


                                            <div class="col-md-3 form-group labelSpace">
                                                <label><span translate="load.noticeLittleTitle"></span>:</label>
                                                <input type="text" class="form-control" maxlength="20"
                                                       ng-model="notice.noticeLittleTitle">
                                            </div>

                                        </div>
                                        <div class="row">
                                            <div class="col-md-9 form-group labelSpace">
                                             <textarea ng-model="notice.noticeLittleText"
                                                       placeholder={{noticeHint|translate}}
                                                       style="width: 1000px;height: 300px;resize: vertical;"></textarea>
                                            </div>
                                        </div>

                                        <button type="button" class="btn btn-danger" style="margin-top: 25px;"
                                                href="javascript:" ng-show="clientContent.canDescReply"
                                                ng-click="clientContent.clientDelete($index)"><span
                                                translate="load.Delte"></span>&nbsp<i class="fa fa-times"></i></button>

                                    </div>
                                </div>
                                <div class="clientFoot" style="margin-top: 20px;">
                                    <button type="button" class="btn btn-success" href="javascript:"
                                            ng-click="clientContent.clientAdd(clientCount)"><span
                                            translate="load.add"></span>&nbsp<i class="fa fa-plus"></i></button>
                                </div>
                            </div>

                            <div class="modal-footer">
                                <div style="float: right;margin-right: 20px;">
                                    <button type="button" class="btn btn-default" href="javascript:"
                                            onclick="history.back(); "><span translate="load.Cancel"></span>
                                    </button>
                                    <button type="submit" class="btn btn-primary addSubmit"><span
                                            translate="load.Ok"></span></button>
                                </div>
                                <p ng-hide="!noticeForm.$invalid" style="color: red;float: right;margin-top: 15px;">
                                    <span translate="load.remindValidate"></span></p>

                            </div>

                        </div>

                    </section>
                </div>

            </div>
        </form>
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
                                    <%--<div class="form-group" style="width: 800px;">--%>
                                        <%--<div class="col-sm-9 icheck ">--%>
                                            <%--<div style="margin-left: 20px;">--%>
                                                <%--<a ng-model="master" ng-click="all(siteList)"--%>
                                                   <%--class="btn btn-success"><span translate="load.checkAll"></span></a>--%>
                                                <%--<a ng-model="master" ng-click="deselectAll(siteList)"--%>
                                                   <%--class="btn btn-danger"><span translate="load.deselectAll"></span></a>--%>

                                                <%--<a ng-model="versaCheck" ng-click="versa(versaCheck)"--%>
                                                   <%--class="btn btn-info"><span--%>
                                                        <%--translate="load.InvertSelection"></span></a>--%>
                                            <%--</div>--%>
                                        <%--</div>--%>
                                        <%--<div class="col-sm-9 icheck ">--%>
                                            <%--<div>--%>
                                                <%--<div class="checkbox" data-ng-repeat="site in siteList" align="center">--%>
                                                    <%--<input type="checkbox" id={{site.id}} name={{site.code}}--%>
                                                           <%--ng-click="updateSelection($event,site.id,site.code)"--%>
                                                           <%--ng-model="siteModel" ng-checked="master"--%>
                                                           <%--class="chk_1 children">--%>
                                                    <%--<label for={{site.id}} translate={{site.code}}></label>--%>
                                                <%--</div>--%>

                                            <%--</div>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>

                                    <div class="form-group" style="margin-left: 1px;">
                                        <span class="label label-success"  ng-click="all(siteList)" style="cursor:pointer" translate="load.checkAll"></span>
                                        <span class="label label-danger"  ng-click="deselectAll(siteList)" translate="load.deselectAll" style="cursor:pointer"></span>
                                        <span class="label label-info"  ng-click="versa(versaCheck)" style="cursor:pointer" translate="load.InvertSelection"></span>
                                    </div>
                                    <div class="form-group" style="width: 590px;overflow :auto;height: 500px;" >
                                        <div class="col-sm-9 icheck ">
                                            <div>
                                                <div  data-ng-repeat="site in siteList track by $index" align="center" class="siteCheckList">
                                                    <span class="label label-default" name={{site.id}} ng-click="updateSelection($event,site.id,site.code)" style="cursor:pointer">{{site.code | translate}}</span>
                                                    <br/>
                                                    <br/>
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
</section>


