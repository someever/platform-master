<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style>
    .radioSpace {
        float: left;
        margin-left: 10px;
    }

    .radioNorms {
        width: 20px;
        height: 20px;
        margin-top: -4px;
    }
</style>
<div>
    <div class="row">
        <div class="col-sm-6">
            <section class="panel">
                <div class="panel-body" ng-controller="roleQueryViewController">

                    <div class="panel-body" >
                        <div class="panel panel-primary">
                            <form class="form-horizontal " ng-submit="playersUpdateForm()"
                                  id="playersUpdateForm" name="playersUpdateForms">
                                <ul class="p-info" ng-hide="playersItems.length==0">
                                    <div class="panel-body"
                                         style="float: left;width: 500px;height: 100px;"
                                         ng-repeat="item in playersItems">
                                        <div style="float: left;">
                                            <div class="title"
                                                 style="width: 150px;font-weight:bold;"><span
                                                    translate="{{item.name}}"></span>：
                                            </div>
                                            <div ng-show="item.identity==-1" class="desk"
                                                 style="width: 250px; text-decoration: none;  overflow: hidden;  text-overflow: ellipsis; display: -webkit-box; -webkit-line-clamp:3;  -webkit-box-orient: vertical;"
                                                 title="{{item.value}}">{{item.value}}
                                            </div>
                                                            <textarea ng-show="item.identity==0"
                                                                      style="width: 250px;height: 80px;resize:none;"
                                                                      class="form-control"
                                                                      ng-model="this[item.dataTag]"></textarea>
                                        </div>
                                    </div>
                                </ul>
                                <div ng-hide="playersItems.length!=0" translate="load.notFindPlayer">
                                </div>
                                <div class="col-sm-6">
                                    <div class="modal-footer">
                                        <div style="float: right;margin-right: 20px;">
                                            <button type="button" class="btn btn-primary"
                                                    href="javascript:"
                                                    ng-show="condition==1"
                                                    ng-click="playersTransform()"><span
                                                    translate="load.update"></span>
                                            </button>
                                            <button ng-show="condition==2" type="submit"
                                                    class="btn btn-primary"
                                                    href="javascript:"><span
                                                    translate="load.Ok"></span></button>
                                            <button type="button" class="btn btn-default"
                                                    href="javascript:"
                                                    onclick="history.back(); "><span
                                                    translate="load.return"></span></button>
                                        </div>
                                    </div>
                                </div>

                            </form>
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

                        <div aria-hidden="true" role="dialog" tabindex="-1" id="mailMessagesConfirm" class="modal fade">
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
                                                ng-click="mailConfirm()"><span translate="load.Ok"></span></button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div aria-hidden="true" role="dialog" tabindex="-1" id="friendMessagesConfirm" class="modal fade">
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
                                                ng-click="friendConfirm()"><span translate="load.Ok"></span></button>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>


                    <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1"
                         id="heroUpdateViewModal" class="modal fade">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×
                                    </button>
                                    <h4 class="modal-title"><span translate="load.heroUpdate"></span></h4>
                                </div>
                                <div class="modal-body">

                                    <form role="form" ng-submit="heroUpdateForm()" id="heroUpdateForm"
                                          name="heroUpdateForms">


                                        <div class="form-group labelSpace">
                                            <label><span translate="load.UpdateHeroReqHeroId"></span>:<span
                                                    style="color: red">*</span></label>
                                            <input type="text" class="form-control updateHeroId" name="updateHeroId"
                                                   ng-model="heroUpdateData.UpdateHeroReqHeroId" readonly required>
                                        </div>

                                        <div class="form-group labelSpace">
                                            <label><span translate="load.GetHeroResName"></span>:<span
                                                    style="color: red">*</span></label>
                                            <input type="text" class="form-control heroName" name="heroName"
                                                   ng-model="heroUpdateData.GetHeroResName" readonly required>
                                        </div>
                                        <div class="form-group labelSpace">
                                            <label><span translate="load.UpdateHeroReqExp"></span>:<span
                                                    style="color: red">*</span></label>
                                            <input type="text" class="form-control heroExp" name="heroExp"
                                                   ng-model="heroUpdateData.UpdateHeroReqExp" required>
                                        </div>
                                        <div class="form-group labelSpace">
                                            <label><span translate="load.UpdateHeroReqQuality"></span>:<span
                                                    style="color: red">*</span></label>
                                            <input type="text" class="form-control heroQuality" name="heroQuality"
                                                   ng-model="heroUpdateData.UpdateHeroReqQuality" required>
                                        </div>

                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal"><span
                                                    translate="load.Cancel"></span></button>
                                            <button type="submit" class="btn btn-warning">
                                                <span translate="load.Ok"></span></button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>


                    <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1"
                         id="itemUpdateViewModal" class="modal fade">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×
                                    </button>
                                    <h4 class="modal-title"><span translate="load.itemCountUpdate"></span></h4>
                                </div>
                                <div class="modal-body">

                                    <form role="form" ng-submit="itemUpdateForm()" id="itemUpdateForm"
                                          name="itemUpdateForms">
                                        <div class="form-group labelSpace">
                                            <label><span translate="load.UpdateItemReqItemCount"></span>:<span
                                                    style="color: red">*</span></label>
                                            <input type="text" class="form-control itemCount" name="itemCount"
                                                   ng-model="itemUpdateData.UpdateItemReqItemCount" required>
                                        </div>


                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal"><span
                                                    translate="load.Cancel"></span></button>
                                            <button type="submit" class="btn btn-warning">
                                                <span translate="load.Ok"></span></button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1"
                         id="equipViewModal" class="modal fade">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×
                                    </button>
                                    <h4 class="modal-title"><span translate="load.heroEquipTitle"></span></h4>
                                </div>
                                <div class="modal-body">
                                    <div class="row">
                                        <aside style="margin-left: 10px;">
                                            <h4 class="drg-event-title"><span translate="load.heroEquipList"></span>
                                            </h4>
                                            <div id='external-events'>
                                                <div class='external-event label label-primary'
                                                     data-ng-repeat="code in heroEquipCode"
                                                     translate={{code.GetHeroResEquipId}}></div>
                                                <br/>
                                            </div>
                                        </aside>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal"><span
                                                    translate="load.Cancel"></span></button>
                                            <button type="button" data-dismiss="modal" class="btn btn-warning">
                                                <span translate="load.Ok"></span></button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1"
                         id="mailViewModal" class="modal fade">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×
                                    </button>
                                    <h4 class="modal-title"><span translate="load.accessoryItemTitle"></span></h4>
                                </div>
                                <div class="modal-body">
                                    <div class="row">
                                        <aside style="margin-left: 10px;">
                                            <h4 class="drg-event-title"><span translate="load.accessoryItemList"></span>
                                            </h4>
                                            <div>
                                                <div class='external-event label label-primary'
                                                     data-ng-repeat="code in mailItemCode"
                                                     translate={{code.GetMailResAccessoryItemId}}></div>
                                                <br/>
                                            </div>
                                        </aside>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal"><span
                                                    translate="load.Cancel"></span></button>
                                            <button type="button" data-dismiss="modal" class="btn btn-warning">
                                                <span translate="load.Ok"></span></button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1"
                         id="skillViewModal" class="modal fade">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×
                                    </button>
                                    <h4 class="modal-title"><span translate="load.heroSkillTitle"></span></h4>
                                </div>
                                <div class="modal-body">
                                    <div class="row">
                                        <aside style="margin-left: 10px;">
                                            <h4 class="drg-event-title"><span translate="load.heroSkillList"></span>
                                            </h4>
                                            <div>
                                                <div class='external-event label label-primary'
                                                     data-ng-repeat="code in heroSkillCode"
                                                     translate={{code.UpdateHeroReqSkilliD}}></div>
                                                <br/>
                                            </div>
                                        </aside>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal"><span
                                                    translate="load.Cancel"></span></button>
                                            <button type="button" data-dismiss="modal" class="btn btn-warning">
                                                <span translate="load.Ok"></span></button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1"
                         id="runeUpdateViewModal" class="modal fade">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×
                                    </button>
                                    <h4 class="modal-title"><span translate="load.runeLvUpdate"></span></h4>
                                </div>
                                <div class="modal-body">

                                    <form role="form" ng-submit="runeUpdateForm()" id="runeUpdateForm"
                                          name="runeUpdateForms">
                                        <div class="form-group labelSpace">
                                            <label><span translate="load.GetRuneResLv"></span>:<span
                                                    style="color: red">*</span></label>
                                            <input type="text" class="form-control runeLv" name="runeLv"
                                                   ng-model="runeUpdateData.UpdateRuneReqLv" required>
                                        </div>


                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal"><span
                                                    translate="load.Cancel"></span></button>
                                            <button type="submit" class="btn btn-warning">
                                                <span translate="load.Ok"></span></button>
                                        </div>
                                    </form>
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