<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="../../common/splitPage.jsp" %> 
<style type="text/css"> .ui-datepicker-calendar { display: none; } </style>
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/bigdata/user_detail" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="#">精准用户分析</a>
			</li>
		</ul>
	</div>


<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-edit"></i> 应用查询</h2>
				<div class="box-icon">
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-down"></i></a>
				</div>
			</div>
			<div class="box-content" style="display:none;" >
				<fieldset>
				 	<div class="control-group">
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
								<label class="control-label" for="focusedInput">日期：</label>
								<div class="controls">
									<input type="text" id="selectDate" name="_query.date" value="${splitPage.queryParam.date}" readonly="readonly" class="input-xlarge datepicker" />
								</div>
							</div>
				  	<div class="form-actions">
						<button type="button" class="btn btn-primary" onclick="splitPage(1);">查询</button>
						<button type="reset" class="btn">清除</button>
				  	</div>
				</fieldset>
			</div>
		</div>
	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i> 用户列表</h2>
				<div class="box-icon">
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
				</div>
				<div class="box-icon">
					<a href="javascript:down('${pageContext.request.contextPath}')" class="btn btn-round" title="导出"><i class="icon-download"></i></a>
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
			</div>
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
							<th onclick="orderbyFun('phone')">手机号码</th>
							<th onclick="orderbyFun('typename')">用户类型</th>
							<th onclick="orderbyFun('name')">访问站点</th>
							<th onclick="orderbyFun('name')">组织</th>
							<th onclick="orderbyFun('startime')">访问时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="user" items="${splitPage.page.list}" >
							<tr>
								<td>
								${fn:substring(fn:substring(user.get("phone"),fn:length(user.get("phone"))-11,-1),0,6)}*****
								</td>
								<td class="center">${user.get("typename")}</td>
								<td class="center">${user.get("shopname")}</td>
								<td class="center">${user.get("orgname")}</td>
								<td class="center">${user.get("dates")}</td>
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
var dates = $("#selectDate");
dates.datepicker({
	changeMonth: true, 
    changeYear: true, 
    showButtonPanel: true, 
    dateFormat: 'yy_mm', 
    onClose: function(dateText, inst) { 
	     var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val(); 
	     var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val(); 
	     $(this).datepicker('setDate', new Date(year, month, 1)); 
    } 
});
var dataXml = $("#dataXml").val();
var myChart = new FusionCharts('file/charts/MSColumn3D.swf', 'ad_chart', "100%", 400);
myChart.setDataXML(dataXml);
myChart.render("chartdivv");
$('#select_shop').multiselect({
	enableFiltering: true,
	maxHeight: 150,
	onChange: function(){
		var checkId = $("#select_shop").val();
		$("#qShopId").attr("value",checkId);
	}
});
function down(ctx){
	var date = $("#selectDate").val();
	var orgId = $('#qOrgId').val();
	var shopId = $('#qShopId').val();
	var url=ctx+'/business/bigdata/downDetailFile?date='+date+'&orgId='+orgId+"&shopId="+shopId;
	window.location.href=url;
}
</script>