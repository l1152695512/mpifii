<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="../../common/splitPage.jsp" %> 
<form id="splitPage" class="form-horizontal" action="${cxt}/business/statistics/toSmsFlowSta" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="#">运营分析－短信统计</a>
			</li>
		</ul>
	</div>
	<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-edit"></i> 短信信息查询</h2>
				<div class="box-icon">
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-down"></i></a>
				</div>
			</div>
			<div class="box-content" style="display: none;" >
				<fieldset>
				 	<div class="control-group">
					  		<label class="control-label" for="focusedInput">开始日期：</label>
							<div class="controls">
								<input type="text" id="startDate" name="_query.startDate" value="${splitPage.queryParam.startDate}" readonly="readonly" class="input-xlarge datepicker" />
							</div>
							<label class="control-label" for="focusedInput">结束日期：</label>
							<div class="controls">
								<input type="text" id="endDate" name="_query.endDate" value="${splitPage.queryParam.endDate}"  readonly="readonly" class="input-xlarge datepicker" />
							</div>
								<label class="control-label" for="focusedInput">组织：</label>
							<div class="controls">
								<input name="_query.org_id" id="qOrgId" value="${splitPage.queryParam.org_id}"  type="hidden" />
								<input class="input-xlarge focused" style="width: 150PX" id="org_id" type="text" value="" onclick="selectOrg(this)"  />
							</div>
							<div id="orgSelect_Div">
							</div>
							<label class="control-label" for="focusedInput">商铺：</label>
							<div class="controls">
								<select id="select_shop" multiple="multiple" >
									<c:forEach var="shop" items="${shopList}">
										<option value="${shop.id}">${shop.name}</option>
									</c:forEach>
								</select>
								<input class="input-xlarge focused" name="_query.shop_id" id="qShopId" type="hidden" onclick="selectShop(this)" value="${splitPage.queryParam.shop_id}"  />
							</div>
					  	</div>
					  	<input name="_query.monthnums" id="monthnums" type="hidden" />
					  	<div class="form-actions">
							<button type="button" class="btn btn-primary" onclick="return checkDate();">查询</button>
							<button type="reset" class="btn" onclick="clearSelVal()">清除</button>
					  	</div>
				</fieldset>
			</div>
		</div><!--/span-->
	</div><!--/row-->
	<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-edit"></i>短信统计报表</h2>
				<div class="box-icon">
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
				</div>
			</div>
			<div class="box-content" >
				<input id="dataXml" value="${dataXml}" type="hidden" />
				<fieldset>
				 	<div class="control-group">
				 		<div id="chartdivv"></div>
				 	</div>
				</fieldset>
			</div>
		</div><!--/span-->
	</div><!--/row-->
	<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-user"></i>短信统计列表</h2>
				<div class="box-icon">
					<a href="javascript:down('${pageContext.request.contextPath}')" class="btn btn-round" title="导出"><i class="icon-download"></i></a>
				</div>
			</div>
			<div class="box-content">
					<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
						    <th onclick="orderbyFun('sendTime')">短信发送日期</th>
							<th onclick="orderbyFun('orgName')">组织名称</th>
							<th onclick="orderbyFun('shopName')">商铺名称</th>
							<th onclick="orderbyFun('zs')">总数量</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="smsFlow" items="${splitPage.page.list}" >
							<tr>
							    <td class="center">${smsFlow.get("sendTime")}</td>
								<td class="center">${smsFlow.get("orgName")}</td>
								<td class="center">${smsFlow.get("shopName")}</td>
								<td class="center">${smsFlow.get("zs")}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div id="splitPageDiv" class="pagination pagination-centered"></div>
			</div>
		</div>
		<!--/span-->
	</div>
	<!--/row-->
</form>	
<script type="text/javascript">
$('#select_shop').multiselect({
	enableFiltering: true,
	maxHeight: 150,
	onChange: function(){
		var checkId = $("#select_shop").val();
		$("#qShopId").attr("value",checkId);
	}
});
var dates = $("#startDate,#endDate");
dates.datepicker({
	maxDate:0,
    onSelect: function(selectedDate){
       var option = this.id == "startDate"?"minDate" : "maxDate";
       dates.not(this).datepicker("option", option, selectedDate);
    }
});
    var dataXml =$("#dataXml").val();
	var myChart = new FusionCharts('file/charts/MSColumn3D.swf', 'ad_chart_33333', "100%",500);
	myChart.setDataXML(dataXml);
	myChart.render("chartdivv");
	
function clearSelVal(){
	$("#startDate").attr('value','');
	$("#endDate").attr('value','');
	var shops = $("#qShopId").val().split(",");
	for(var i=0;i<shops.length;i++){
	   $("#select_shop").multiselect('deselect',shops[i]);
	}
	$("#qShopId").attr('value','');
	$("#qOrgId").attr('value','');
	ajaxContent('/business/statistics/toSmsFlowSta');
}

function checkDate(){
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	if(startDate!='' && endDate!=''){
		var dtStart = new Date(startDate);
		var dtEnd = new Date(endDate);
		var month = (dtEnd.getMonth()+1)+((dtEnd.getFullYear()-dtStart.getFullYear())*12) - (dtStart.getMonth()+1);
		$("#monthnums").attr('value',month);
		if(month>3){
			alert("查询日期区间为4个月，请修改");
			return;
		}
	}
	splitPage(1);
}
function down(ctx){
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var orgId = $('#qOrgId').val();
	var shopId = $('#qShopId').val();
	var url=ctx+'/business/statistics/downSmsFlowFile?startDate='+startDate+'&endDate='+endDate+'&orgId='+orgId+"&shopId="+shopId;
	window.location.href=url;
}
</script>
