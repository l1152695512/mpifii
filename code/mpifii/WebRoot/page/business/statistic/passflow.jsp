<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ include file="../../common/splitPage.jsp" %> 
<form id="splitPage" class="form-horizontal" action="${cxt}/business/statistics/toPassFlow" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="javascript:void(0);" onclick="ajaxContent('/business/statistics/toPassFlow');">运营分析－客流</a>
			</li>
		</ul>
	</div>
	<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-edit"></i> 客流查询</h2>
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
					  	<div class="form-actions">
							<button type="button" class="btn btn-primary" onclick="splitPage(1);">查询</button>
							<button type="button" class="btn btn-primary" onclick="resertForm();">重置</button>
					  	</div>
				</fieldset>
			</div>
		</div><!--/span-->
	</div><!--/row-->
 	<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-edit"></i> 分析报表</h2>
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
				<h2><i class="icon-user"></i>客流列表</h2>
				<div class="box-icon">
					<a href="javascript:down('${pageContext.request.contextPath}')" class="btn btn-round" title="导出"><i class="icon-download"></i></a>
				</div>
			</div>
			<div class="box-content">
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
						 	<th onclick="orderbyFun('dates')">日期</th>
							<th onclick="orderbyFun('orgname')">组织</th>
							<th onclick="orderbyFun('shopname')">商铺</th>
							<th onclick="orderbyFun('counts')">人数</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="pflow" varStatus="stat" items="${splitPage.page.list}" >
							<tr>
								<td class="center">${pflow.get("dates")}</td>
								<td class="center">${pflow.get("orgname")}</td>
								<td class="center">${pflow.get("shopname")}</td>
								<td class="center">${pflow.get("counts")}</td>
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
	maxDate:-1,
    onSelect: function(selectedDate){
       var option = this.id == "startDate"?"minDate" : "maxDate";
       dates.not(this).datepicker("option", option, selectedDate);
    }
});
 	var dataXml =$("#dataXml").val();
	var myChart = new FusionCharts('file/charts/MSColumn3D.swf', 'ad_chart_2014', "100%",500);
	myChart.setDataXML(dataXml);
	myChart.render("chartdivv"); 
	function resertForm(){
		$(':input','#splitPage')   
		.not(':button, :submit, :reset, :hidden')   
		.val('')   
		.removeAttr('checked')   
		.removeAttr('selected');  
		ajaxContent('/business/statistics/toPassFlow');
	}
	function down(ctx){
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var orgId = $('#qOrgId').val();
		var shopId = $('#qShopId').val();
		var url=ctx+'/business/statistics/downPassFlowFile?startDate='+startDate+'&endDate='+endDate+'&orgId='+orgId+"&shopId="+shopId;
		window.location.href=url;
	}
</script>
