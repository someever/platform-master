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
<section>
    <div ng-controller="GMInstructController">
        <form role="form" ng-submit="GMInstructAddForm()" id="GMInstructForm" name="GMInstructForms">
            <div class="row">
                <div class="col-sm-6">
                    <section class="panel">
                        <header class="panel-heading">
                            <span translate="load.gmInstructTitle"></span>
                        </header>
                        <div class="panel-body">

                            <div class="row">
                                <div class="col-md-4 form-group labelSpace">
                                    <label><span translate="load.gmCmdValue"></span>:</label>
                                    <textarea class="form-control" style="height: 150px;resize:none;"
                                              ng-model="gmInstructData.gmCmdValue"></textarea>
                                </div>


                                <div class="col-md-4 form-group labelSpace">
                                    <label><span translate="load.GameId"></span>:<span
                                            style="color: red">*</span></label>
                                    <select name="gameList" aria-controls="editable-sample"
                                            class="form-control gameList"
                                            ng-options="game.id as game.code|translate for game in gameList"
                                            ng-change="resourceGameListChange(gmInstructGameList)"
                                            ng-model="gmInstructGameList"
                                            required>
                                        <option value="" translate="load.pleaseSelect"></option>
                                    </select>
                                </div>

                                <div class="col-md-4 form-group labelSpace">
                                    <label><span translate="load.gmCmdType"></span>:<span
                                            style="color: red">*</span></label>
                                    <select aria-controls="editable-sample" class="form-control"
                                            ng-model="gmInstructData.gmCmdType" required>
                                        <option value="" translate="load.pleaseSelect"></option>
                                        <%--<option value="1" translate="load.platformId"></option>--%>
                                        <option value="1" translate="load.loadGameCfg"></option>
                                        <option value="2" translate="load.version"></option>
                                    </select>
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
                                             data-ng-repeat="area in areaData">{{area}}
                                        </div>
                                        <br/>
                                    </div>
                                </aside>
                                <button class="btn btn-warning" type="button" ng-click="areaClick()"><span
                                        translate="load.areaChoice"></span></button>

                            </div>
                            <div class="modal-footer">
                                <div style="float: right;margin-right: 20px;">
                                    <button type="button" class="btn btn-default" href="javascript:"
                                            ng-click="resetForm()"><span translate="load.ResetForm"></span></button>
                                    <button type="submit" class="btn btn-primary GMInstructAddSubmit"><span
                                            translate="load.Ok"></span></button>
                                </div>
                                <p ng-hide="!GMInstructForms.$invalid"
                                   style="color: red;float: right;margin-top: 15px;">
                                    <span translate="load.remindValidate"></span></p>
                            </div>
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


<script type="text/javascript">
    function globelQuery(e) {
        if (!e)
            e = window.event;
        if ((e.keyCode || e.which) == 13) {
            $(".searchButton").click();
        }
    }

</script>