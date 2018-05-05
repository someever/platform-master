<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script src="${ctx}/resource/adminex/js/DateTimePicker/DateInit.js"></script>
<script src="${ctx}/resource/adminex/js/multi-select-init.js"></script>
<script src="${ctx}/resource/adminex/js/icheck-init.js"></script>
<div>
    <div class="row" ng-controller="bagCodeQueryController">
        <div class="col-sm-6">
        </div>
        <div class="col-sm-6">
            <section class="panel">
                <div class="panel-body">
                    <div class="clearfix">

                        <div style="float: right;">

                            <form class="searchform" method="post" style="float:left;margin-top: 15px;">
                                <input type="text" class="form-control searchText"
                                       ng-model="bagCodeQuerySearch.codeName" name="keyword"
                                       placeholder={{codeSearchText|translate}} onkeydown="globelQuery(event)"/>
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
                            <th><span translate="load.codeQueryNumber"></span></th>
                            <th><span translate="load.codeQueryBatchCode"></span></th>
                            <th><span translate="load.codeQueryGameName"></span></th>
                            <th><span translate="load.codeQueryGameArea"></span></th>
                            <th><span translate="load.codeQueryChannel"></span></th>
                            <th><span translate="load.codeQueryUserStatus"></span></th>
                            <th><span translate="load.codeQueryUserAccount"></span></th>
                            <th><span translate="load.codeQueryUserTime"></span></th>
                            <th><span translate="load.codeQueryValidStatus"></span></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="bagCode in bagCodeQueryDatas">
                            <td style="vertical-align:middle">{{bagCode.promoteCode}}</td>
                            <td style="vertical-align:middle">{{bagCode.batchId}}</td>
                            <td style="vertical-align:middle">
                                <span ng-hide="bagCode.drawGameId!='1'" class="label label-success label-mini">{{bagCode.drawGameId|NumToStr7|translate}}</span>
                                <span ng-hide="bagCode.drawGameId=='1'" class="label label-default label-mini">{{bagCode.drawGameId|NumToStr7|translate}}</span>
                            </td>
                            <td style="vertical-align:middle">
                                {{bagCode.drawGameAreaId}}
                            </td>
                            <td style="vertical-align:middle">
                            <span ng-hide="bagCode.drawSiteId!=2"
                                  class="label label-mini"
                                  style="background-color: #769efa">{{bagCode.drawSiteId|NumToStr9|translate}}</span>
                                    <span ng-hide="bagCode.drawSiteId!=3" class="label label-mini"
                                          style="background-color: #6de90f">{{bagCode.drawSiteId|NumToStr9|translate}}</span>
                                    <span ng-hide="bagCode.drawSiteId!=4" class="label label-mini"
                                          style="background-color: #565acb">{{bagCode.drawSiteId|NumToStr9|translate}}</span>
                                    <span ng-hide="bagCode.drawSiteId!=5" class="label  label-mini"
                                          style="background-color: #2fd4cd">{{bagCode.drawSiteId|NumToStr9|translate}}</span>
                                    <span ng-hide="bagCode.drawSiteId!=6" class="label label-mini"
                                          style="background-color: #00b7ee">{{bagCode.drawSiteId|NumToStr9|translate}}</span>
                                    <span ng-hide="bagCode.drawSiteId!=7" class="label label-mini"
                                          style="background-color: #b33ac4">{{bagCode.drawSiteId|NumToStr9|translate}}</span>
                                    <span ng-hide="bagCode.drawSiteId!=8" class="label label-mini"
                                          style="background-color: #962928">{{bagCode.drawSiteId|NumToStr9|translate}}</span>
                                    <span ng-hide="bagCode.drawSiteId!=9" class="label label-mini"
                                          style="background-color: #adb327">{{bagCode.drawSiteId|NumToStr9|translate}}</span>
                                    <span ng-hide="bagCode.drawSiteId!=10" class="label label-mini"
                                          style="background-color: #af9312">{{bagCode.drawSiteId|NumToStr9|translate}}</span>
                                    <span ng-hide="bagCode.drawSiteId!=11" class="label label-mini"
                                          style="background-color: #a693af">{{bagCode.drawSiteId|NumToStr9|translate}}</span>
                                    <span ng-hide="bagCode.drawSiteId!=12" class="label  label-mini"
                                          style="background-color: #71af5c">{{bagCode.drawSiteId|NumToStr9|translate}}</span>
                                    <span ng-hide="bagCode.drawSiteId!=13" class="label label-mini"
                                          style="background-color: #acaf17">{{bagCode.drawSiteId|NumToStr9|translate}}</span>
                                    <span ng-hide="bagCode.drawSiteId!=14" class="label label-mini"
                                          style="background-color: #af6fad">{{bagCode.drawSiteId|NumToStr9|translate}}</span>
                                    <span ng-hide="bagCode.drawSiteId!=15" class="label label-mini"
                                          style="background-color: #6312af">{{bagCode.drawSiteId|NumToStr9|translate}}</span>
                                    <span ng-hide="bagCode.drawSiteId==2 || bagCode.drawSiteId==3 || bagCode.drawSiteId==4 || bagCode.drawSiteId==5 || bagCode.drawSiteId==6 || bagCode.drawSiteId==7 || bagCode.drawSiteId==8 || bagCode.drawSiteId==9 || bagCode.drawSiteId==10 || bagCode.drawSiteId==11 || bagCode.drawSiteId==12 || bagCode.drawSiteId==13 || bagCode.drawSiteId==14 || bagCode.drawSiteId==15"
                                          class="label label-default label-mini">{{bagCode.siteId|NumToStr9|translate}}</span>
                            </td>
                            <td style="vertical-align:middle">
                                <span ng-show="bagCode.drawStatus==0" class="label label-success label-mini">{{bagCode.drawStatus|NumToStr21|translate}}</span>
                                <span ng-show="bagCode.drawStatus==1" style="background-color: #71af5c"
                                      class="label label-mini">{{bagCode.drawStatus|NumToStr21|translate}}</span>
                                <span ng-show="bagCode.drawStatus==2" class="label label-primary label-mini">{{bagCode.drawStatus|NumToStr21|translate}}</span>
                                <span ng-show="bagCode.drawStatus==3" class="label label-default label-mini">{{bagCode.drawStatus|NumToStr21|translate}}</span>
                                <span ng-show="bagCode.drawStatus==-1" class="label label-info label-mini">{{bagCode.drawStatus|NumToStr21|translate}}</span>
                                <span ng-show="bagCode.drawStatus==-2" class="label label-danger label-mini">{{bagCode.drawStatus|NumToStr21|translate}}</span>
                            </td>
                            <td style="vertical-align:middle">{{bagCode.drawRoleId}}</td>
                            <td style="vertical-align:middle">{{bagCode.drawDate|date:'yyyy-MM-dd HH:mm:ss'}}</td>
                            <td style="vertical-align:middle">
                                <span ng-hide="bagCode.validStatus!=1" class="label label-success label-mini">{{bagCode.validStatus|NumToStr19|translate}}</span>
                                <span ng-hide="bagCode.validStatus!=2" class="label label-danger label-mini">{{bagCode.validStatus|NumToStr19|translate}}</span>
                                <span ng-hide="bagCode.validStatus!=3" class="label label-default label-mini">{{bagCode.validStatus|NumToStr19|translate}}</span>
                            </td>
                        </tr>
                        <tr ng-show="bagCodeQueryDatas.length==0">
                            <td colspan="9" style="text-align: center;">
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