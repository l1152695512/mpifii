<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ include file="../../common/splitPage.jsp" %> 
<form id="splitPage" class="form-horizontal" action="${cxt}/business/statistics/toAdvSta" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="#">运营分析－广告</a>
				<a href="javascript:void(0);" onclick="ajaxContent('/business/statistics/toAdvSta');">运营分析－广告</a>
			</li>
		</ul>
	</div>
		<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-edit"></i> 广告统计查询</h2>
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
				<h2><i class="icon-edit"></i> 分析报表(按组织)</h2>
				<div class="box-icon">
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
				</div>
			</div>
			<div class="box-content" >
				<input id="dataXmlOrg" value="${dataXmlOrg}"  type="hidden" />
				<fieldset>
				 	<div class="control-group">
				 		<div id="chartdivOrg"></div>
				 	</div>
				</fieldset>
			</div>
		</div><!--/span-->
	</div><!--/row-->
	<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-user"></i>广告统计列表</h2>
				<div class="box-icon">
					<a href="javascript:down('${pageContext.request.contextPath}')" class="btn btn-round" title="导出"><i class="icon-download"></i></a>
				</div>
			</div>
			<div class="box-content">
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
							<th onclick="orderbyFun('dates')">
							日期
							</th>
							<th onclick="orderbyFun('orgname')">组织</th>
							<th onclick="orderbyFun('shopNmae')">商铺</th>
							<th onclick="orderbyFun('zss')">展示次数</th>
							<th onclick="orderbyFun('djs')" >点击次数</th>
							<th onclick="orderbyFun('djs')">点击率</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="adv" varStatus="stat" items="${splitPage.page.list}" >
							<tr>
								<td  class="center">
									${adv.get("dates")}
								</td>
								<td class="center">
								<c:if test='${adv.get("orgname")==null}'>
								 暂未绑定
								</c:if>
									${adv.get("orgname")}
								</td>
								<td class="center">${adv.get("shopNmae")}</td>
								<td class="center">
								${adv.get("zss")}次
								</td>
								<td class="center">
								${adv.get("djs")}次
								</td>
								<td class="center">
								<c:if test='${adv.get("zss")==0 || adv.get("djs")==0}'>0</c:if>
									<c:if test='${adv.get("zss")!=0 && adv.get("djs")!=0}'>
										<fmt:formatNumber value='${adv.get("djs")/adv.get("zss")}' pattern="##.##"   minFractionDigits="2" ></fmt:formatNumber>
									</c:if>
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
	var dataXml = $("#dataXml").val();
	var myChart = new FusionCharts('file/charts/MSColumn3DLineDY.swf', 'ad_chart_2014', "100%",500);
	myChart.setDataXML(dataXml);
	myChart.render("chartdivv");
	
	var dataXmlOrg = $("#dataXmlOrg").val();
	var myChart = new FusionCharts('file/charts/MSColumn3DLineDY.swf', 'ad_chart_2014_2', "100%",500);
	myChart.setDataXML(dataXmlOrg);
	myChart.render("chartdivOrg");
	function resertForm(){
		$(':input','#splitPage')   
		.not(':button, :submit, :reset, :hidden')   
		.val('')   
		.removeAttr('checked')   
		.removeAttr('selected');  
		ajaxContent('/business/statistics/toAdvSta');
	}
	function down(ctx){
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var orgId = $('#qOrgId').val();
		var shopId = $('#qShopId').val();
		var url=ctx+'/business/statistics/downAdvStaFile?startDate='+startDate+'&endDate='+endDate+'&orgId='+orgId+"&shopId="+shopId;
		window.location.href=url;
	}
</script>
