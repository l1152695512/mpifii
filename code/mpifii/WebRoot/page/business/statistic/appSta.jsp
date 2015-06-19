<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ include file="../../common/splitPage.jsp" %> 
<form id="splitPage" class="form-horizontal" action="${cxt}/business/statistics/toAppSta" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="#">运营分析－应用分析</a>
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
			<div class="box-content" style="display: none;" >
				<fieldset>
				 	<div class="control-group">
					  		<label class="control-label" for="focusedInput">开始日期：</label>
							<div class="controls">
								<input type="text" name="_query.startDate" value="${splitPage.queryParam.startDate}" readonly="readonly" class="input-xlarge datepicker" />
							</div>
							<label class="control-label" for="focusedInput">结束日期：</label>
							<div class="controls">
								<input type="text" name="_query.endDate" value="${splitPage.queryParam.endDate}"  readonly="readonly" class="input-xlarge datepicker" />
							</div>
							<label class="control-label" for="focusedInput">组织：</label>
							<div class="controls">
							  	<select id="select_org" multiple="multiple" >
									<c:forEach var="org" items="${orgList}">
										<option value="${org.id}">${org.name}</option>
									</c:forEach>
								</select>
								<input name="_query.org_id" id="qOrgId" value="${splitPage.queryParam.org_id}"  type="hidden" />
							</div>
							<label class="control-label" for="focusedInput">商铺：</label>
							<div class="controls">
							  	<select id="select_shop" multiple="multiple" >
									<option value="">--请先选择组织--</option>
								</select>
								<input name="_query.shop_id" id="qShopId" value="${splitPage.queryParam.shop_id}" type="hidden" />
							</div>
					  	</div>
					  	<div class="form-actions">
							<button type="button" class="btn btn-primary" onclick="splitPage(1);">查询</button>
							<button type="reset" class="btn">清除</button>
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
				<h2><i class="icon-user"></i>应用列表</h2>
			</div>
			<div class="box-content">
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
							<th>日期</th>
							<th>组织</th>
							<th>商铺</th>
							<th>应用</th>
							<th>点击数</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="app" varStatus="stat" items="${splitPage.page.list}" >
							<tr>
								<td class="center">${app.get("days")}</td>
								<td class="center">${app.get("orgname")}</td>
								<td class="center">${app.get("shopname")}</td>
								<td class="center">${app.get("apname")==null?'未绑定应用':app.get("apname")}</td>
								<td class="center">
									${app.get("num")}
								</td>
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
$('#select_org').multiselect({
	enableFiltering: true,
	maxHeight: 150,
	onChange: function(){
		var checkId = $("#select_org").val();
		$("#qOrgId").attr("value",checkId);
		var url ="/business/shop/getShopByOrg?orgids="+checkId;
		$.ajax({
			type : 'POST',
			dataType : "json",
			url :encodeURI(encodeURI(cxt + url)),
			success : function(data) {
				$("#select_shop").multiselect('dataprovider',data);
			}
		});
	}
});
$('#select_shop').multiselect({
	enableFiltering: true,
	maxHeight: 150,
	onChange: function(){
		var checkId = $("#select_shop").val();
		$("#qShopId").attr("value",checkId);
	}
});
var dataXml = $("#dataXml").val();
var myChart = new FusionCharts('file/charts/MSColumn3D.swf', 'ad_chart_2014', "100%",500);
myChart.setDataXML(dataXml);
myChart.render("chartdivv");

</script>
