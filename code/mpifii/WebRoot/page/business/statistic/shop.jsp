<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ include file="../../common/splitPage.jsp" %> 
<form id="splitPage" class="form-horizontal" action="${cxt}/business/statistics/toShop" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="javascript:void(0);" onclick="ajaxContent('/business/statistics/toShop');">运营分析－商铺</a>
			</li>
		</ul>
	</div>
	<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-edit"></i> 商铺查询</h2>
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
							  	<select id="select_org" multiple="multiple" >
									<c:forEach var="org" items="${orgList}">
										<option value="${org.id}" >${org.name}</option>
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
							<button type="button" class="btn btn-primary" onclick="resertForm();">重置</button>
					  	</div>
				</fieldset>
			</div>
		</div><!--/span-->
	</div><!--/row-->
	<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-user"></i>商铺列表</h2>
			</div>
			<div class="box-content">
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
							<th>日期</th>
							<th>组织名称</th>
							<th>商铺名称</th>
							<th>客流</th>
							<th>盒子式路由数量</th>
							<th>吸顶式路由数量</th>
							<th>广告展示次数</th>
							<th>广告点击次数</th>
							<th>人均广告展示次数</th>
							<th>广告展示点击次数</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="shop" varStatus="stat" items="${splitPage.page.list}" >
							<tr>
								<td class="center">${shop.get("days")}</td>
								<td class="center">${shop.get("orgname")}</td>
								<td class="center">${shop.get("shopname")}</td>
								<td class="center">${shop.get("rs")}</td>
								<td class="center">${shop.get("router")}</td>
								<td class="center">${shop.get("ap")}</td>
								<td class="center">${shop.get("zss")}</td>
								<td class="center">${shop.get("djs")}</td>
								<td class="center">
							
									<c:if test='${shop.get("zss")==0 || shop.get("rs")==0}'>0</c:if>
									<c:if test='${shop.get("zss")!=0 && shop.get("rs")!=0}'>
										 <fmt:formatNumber value='${shop.get("zss")/shop.get("rs")}'  pattern="##.##"   minFractionDigits="2" ></fmt:formatNumber>
									</c:if>
								</td>
								<td class="center">
									<c:if test='${shop.get("zss")==0 || shop.get("djs")==0}'>0</c:if>
									<c:if test='${shop.get("zss")!=0 && shop.get("djs")!=0}'>
										<fmt:formatNumber value='${shop.get("djs")/shop.get("zss")}'  pattern="##.##"   minFractionDigits="2" ></fmt:formatNumber>
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

var  checkedOrg = $("#qOrgId").val();
if(checkedOrg.length>0){
	var orgs = checkedOrg.split(",");
	for(var i=0;i<orgs.length;i++){
		 $('#select_org').multiselect('select', orgs[i]);
	}
}
var  checkedShop = $("#qShopId").val();
if(checkedOrg.length>0){
	var url ="/business/shop/getShopByOrg?orgids="+checkedOrg;
	$.ajax({
		type : 'POST',
		dataType : "json",
		url :encodeURI(encodeURI(cxt + url)),
		success : function(data) {
			$("#select_shop").multiselect('dataprovider',data);
			var shops = checkedShop.split(",");
			for(var i=0;i<shops.length;i++){
			   $("#select_shop").multiselect('select',shops[i]);
			}
		 
		}
	});
}
var dates = $("#startDate,#endDate");
dates.datepicker({
	maxDate:-1,
    onSelect: function(selectedDate){
       var option = this.id == "startDate"?"minDate" : "maxDate";
       dates.not(this).datepicker("option", option, selectedDate);
    }
});
function resertForm(){
	$(':input','#splitPage')   
	.not(':button, :submit, :reset, :hidden')   
	.val('')   
	.removeAttr('checked')   
	.removeAttr('selected');  
	ajaxContent('/business/statistics/toShop');
}
</script>
