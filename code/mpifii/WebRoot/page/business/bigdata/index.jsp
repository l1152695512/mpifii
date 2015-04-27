<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="../../common/splitPage.jsp" %> 

<style type="text/css">
	#splitPage .search-content{
/* 		display: none; */
		position: absolute;
		width: 98%;
		background-color: white;
		border: 1px solid #DDDDDD;
		-webkit-box-shadow: #666 0px 0px 10px;
		-moz-box-shadow: #666 0px 0px 10px;
		box-shadow: #666 0px 0px 10px;
		padding-top: 20px;
/* 		border-right: 1px solid #DDDDDD; */
/* 		border-bottom: 1px solid #DDDDDD; */
	}
	#splitPage .box-content{
		position: relative;
	}
	#splitPage .form-actions{
		margin-top: 0px;
		margin-bottom: 0px;
	}
</style><!--  -->
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/realtrue/index" method="POST">
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
								<label class="control-label" for="focusedInput">开始日期：</label>
								<div class="controls">
									<input type="text" id="starttime" name="_query.startDate" value="${startDate}" readonly="readonly" class="input-xlarge datepicker" />
								</div>
								<label class="control-label"for="focusedInput">结束日期：</label>
								<div class="controls">
									<input type="text"   id="endtime"  name="_query.endDate" value="${endDate}"  readonly="readonly" class="input-xlarge datepicker" />
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
								<td class="center">${user.get("name")}</td>
								<td class="center">${user.get("startime")}</td>
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
/* 		$(document).ready(function() {
			getPFCharts();
		});
function to_user_type(){
		var myChart = new FusionCharts('file/charts/Line.swf?ChartNoDataText=无数据显示', 'ad_chart_1', "100%", 410);
			var starttime=$("#starttime").val();
			var endtime=$("#endtime").val();
			var qOrgId=$("#qOrgId").val();
			var qShopId=$("#qShopId").val();
				$.ajax({
		    		type: "POST",
		    		dataType: 'text',
		    		data:{starttime:starttime,endtime:endtime,qOrgId:qOrgId,qShopId:qShopId},
		    		url: "business/realtrue/to_user_type?starttime="+starttime+"&endtime="+endtime,
		    		success: function(data,status,xhr){
		    			if(status == "success"){
		    				console.log(data);
		    				myChart.setDataXML(data);
							myChart.render("chartdivv");
		    			}
		    		}
		    	});
};
		function getPFCharts(){
					var myChart = new FusionCharts('file/charts/ZoomLine.swf?ChartNoDataText=无数据显示', 'ad_chart_2014', "100%", 410);
						$.ajax({
				    		type: "POST",
				    		dataType: 'text',
				    		data:{},
				    		url: "business/realtrue/to_user_type",
				    		success: function(data,status,xhr){
				    			if(status == "success"){
				    				myChart.setDataXML(data);
									myChart.render("chartdivv");
				    			}
				    		}
				    	});
	};
	*/
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
console.log(dataXml);
var myChart = new FusionCharts('file/charts/MSColumn3D.swf', 'ad_chart', "100%", 400);
myChart.setDataXML(dataXml);
myChart.render("chartdivv");
</script>