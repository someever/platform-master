<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="${ctx}/resource/adminex/js/DateTimePicker/DateInit.js"></script>
<script src="${ctx}/resource/adminex/js/multi-select-init.js"></script>
<script src="${ctx}/resource/adminex/js/icheck-init.js"></script>
<div>
    <div class="row" ng-controller="billingOrderController">
        <div class="col-sm-6">
            <section class="panel" style="margin-top: 20px;">
                <header class="panel-heading">
                    <span translate="load.searchCriteria"></span><span
                        style="color:red;">&nbsp;&nbsp;(<span translate="load.billingOrderExplain"></span>)</span>
                    <span class="tools pull-right">
                       <a href="javascript:;" class="fa fa-chevron-down" onclick="chevronClick(this)"></a>
                    </span>
                </header>
                <div class="panel-body searchCriteria">
                    <div style="float: left;margin-right: 20px;" class="labelSpace">
                        <label><span translate="load.billingOrderRoleId"></span></label>
                        <input size="16" type="text" name="proTime" class=" form-control"
                               ng-model="billingOrderSearch.roleId" onkeydown="globelQuery(event)">
                    </div>
                    <div style="float: left;margin-right: 20px;" class="labelSpace">
                        <label><span translate="load.billingOrderRoleName"></span></label>
                        <input size="16" type="text" name="proTime" class=" form-control"
                               ng-model="billingOrderSearch.roleName" onkeydown="globelQuery(event)">
                    </div>
                    <div style="float: left;margin-right: 20px;" class="labelSpace">
                        <label><span translate="load.startTime"></span></label>
                        <input size="16" type="text" name="proTime" readonly class="ui_timepicker form-control"
                               ng-model="billingOrderSearch.from">
                    </div>
                    <div style="float: left;margin-right: 20px;" class="labelSpace">
                        <label><span translate="load.endTime"></span></label>
                        <input size="16" type="text" name="proTime" readonly class="ui_timepicker form-control"
                               ng-model="billingOrderSearch.to">
                    </div>
                    <div style="float: left;margin-top: 21px;">
                        <button ng-click="searchButtonClicked()" class="btn btn-primary searchButton"><span
                                translate="load.search"></span>&nbsp;<i class="fa fa-search"></i></button>
                    </div>

                    <div style="float: right;margin-top: 21px;">
                        <button ng-click="orderParamClicked()" class="btn btn-success orderParamTitle"><span
                                translate="load.orderParamTitle"></span>&nbsp;<i class="fa fa-plus"></i></button>
                    </div>
                </div>
            </section>
            <section class="panel">
                <div class="panel-body">
                    <table class="table  table-hover general-table">
                        <thead>
                        <tr>
                            <th><span translate="load.billingOrderOId"></span></th>
                            <th><span translate="load.billingOrderReOrderId"></span></th>
                            <th><span translate="load.billingOrderAccountName"></span></th>
                            <th><span translate="load.billingOrderPayType"></span></th>
                            <th><span translate="load.billingOrderSiteId"></span></th>
                            <th><span translate="load.billingOrderGameId"></span></th>
                            <th><span translate="load.billingOrderRoleId"></span></th>
                            <th><span translate="load.billingOrderRoleName"></span></th>
                            <th><span translate="load.billingOrderPayAmount"></span></th>
                            <th><span translate="load.billingOrderOrderStatus"></span></th>
                            <th><span translate="load.billingOrderPayTime"></span></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="order in orderData">

                            <td style="vertical-align:middle">{{order.orderId}}</td>
                            <td style="vertical-align:middle">{{order.reOrderId}}</td>
                            <td style="vertical-align:middle">{{order.accountName}}</td>
                            <td style="vertical-align:middle">{{order.payType|NumToStr26|translate}}</td>
                            <td style="vertical-align:middle">
                                 <span ng-hide="order.siteId!=2"
                                       class="label label-mini"
                                       style="background-color: #769efa">{{order.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="order.siteId!=3" class="label label-mini"
                                          style="background-color: #6de90f">{{order.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="order.siteId!=4" class="label label-mini"
                                          style="background-color: #565acb">{{order.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="order.siteId!=5" class="label  label-mini"
                                          style="background-color: #2fd4cd">{{order.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="order.siteId!=6" class="label label-mini"
                                          style="background-color: #00b7ee">{{order.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="order.siteId!=7" class="label label-mini"
                                          style="background-color: #b33ac4">{{order.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="order.siteId!=8" class="label label-mini"
                                          style="background-color: #962928">{{order.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="order.siteId!=9" class="label label-mini"
                                          style="background-color: #adb327">{{order.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="order.siteId!=10" class="label label-mini"
                                          style="background-color: #af9312">{{order.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="order.siteId!=11" class="label label-mini"
                                          style="background-color: #a693af">{{order.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="order.siteId!=12" class="label  label-mini"
                                          style="background-color: #71af5c">{{order.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="order.siteId!=13" class="label label-mini"
                                          style="background-color: #acaf17">{{order.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="order.siteId!=14" class="label label-mini"
                                          style="background-color: #af6fad">{{order.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="order.siteId!=15" class="label label-mini"
                                          style="background-color: #6312af">{{order.siteId|NumToStr9|translate}}</span>
                                    <span ng-hide="order.siteId==2 || order.siteId==3 || order.siteId==4 || order.siteId==5 || order.siteId==6 || order.siteId==7 || order.siteId==8 || order.siteId==9 || order.siteId==10 || order.siteId==11 || order.siteId==12 || order.siteId==13 || order.siteId==14 || order.siteId==15"
                                          class="label label-default label-mini">{{order.siteId|NumToStr9|translate}}</span>
                            </td>
                            <td style="vertical-align:middle">{{order.areaName}}</td>
                            <td style="vertical-align:middle">{{order.roleId}}</td>
                            <td style="vertical-align:middle">{{order.roleName}}</td>
                            <td style="vertical-align:middle">{{order.payAmount}}</td>
                            <td style="vertical-align:middle"><span ng-show="order.orderStatus=='UNPAID'"
                                                                    class="label label-default label-mini"
                            >{{order.orderStatus|NumToStr24|translate}}</span>
                                <span ng-show="order.orderStatus=='PAID'" class="label label-danger label-mini"
                                >{{order.orderStatus|NumToStr24|translate}}</span>
                                <span ng-show="order.orderStatus=='DELIVERING'" class="label label-info label-mini"
                                >{{order.orderStatus|NumToStr24|translate}}</span>
                                <span ng-show="order.orderStatus=='DELIVERED'" class="label label-success label-mini"
                                >{{order.orderStatus|NumToStr24|translate}}</span>
                                 <span ng-show="order.orderStatus=='REPAIR'" class="label label-warning label-mini"
                                 >{{order.orderStatus|NumToStr24|translate}}</span>
                            </td>
                            <td style="vertical-align:middle">{{order.payTime|date:'yyyy-MM-dd HH:mm:ss'}}</td>
                        </tr>
                        </tbody>
                    </table>
                    <div class="pager">
                        <pager page-count="pageCount" current-page="currentPage" on-page-change="onPageChange()"
                               first-text="load.HomePage" next-text="load.NextPage" prev-text="load.PreviousPage"
                               last-text="load.EndPage" show-goto="true" goto-text="load.GoToPage"></pager>
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