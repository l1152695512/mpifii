<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ include file="../../common/splitPage.jsp" %> 
<form id="splitPage" class="form-horizontal" action="${cxt}/business/statistics/toDevice" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="javascript:void(0);" onclick="ajaxContent('/business/statistics/toDevice');">运营分析－盒子</a>
			</li>
		</ul>
	</div>
	<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-edit"></i> 盒子查询</h2>
				<div class="box-icon">
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-down"></i></a>
				</div>
			</div>
			<div class="box-content" style="display: none;" >
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
							<label class="control-label" for="focusedInput">状态：</label>
							<div class="controls">
							  	<select id="select_status" name="_query.type" >
							  		<option value="">--请选择--</option>
									<option value="0" <c:if test="${splitPage.queryParam.type==0}">selected="selected"</c:if>>在线</option>
									<option value="1" <c:if test="${splitPage.queryParam.type==1}">selected="selected"</c:if>>离线</option>
								</select>
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
				<h2><i class="icon-user"></i>盒子列表</h2>
			</div>
			<div class="box-content">
					<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
							<th>组织名称</th>
							<th>商铺名称</th>
							<th>盒子名称</th>
							<th>SN</th>
							<th>状态</th>
							<th>类型</th>
							<th>认证时长（分钟）</th>
							<th>注册时间</th>
							<th>在线人数</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="device" items="${splitPage.page.list}" >
							<tr>
								<td class="center">${device.get("orgname")}</td>
								<td class="center">${device.get("shopname")}</td>
								<td class="center">${device.get("name")}</td>
								<td class="center">${device.get("router_sn")}</td>
								<td class="center">${device.get("online_num")>=0?'在线':'离线'}</td>
								<td class="center">${device.get("type")==1?'router':'AP'}</td>
								<td class="center">${device.get("time_out")}</td>
								<td class="center">${device.get("create_date")}</td>
								<td class="center">${device.get("online_num")>=0?device.get("online_num"):'0'}</td>
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
var dataXml = $("#dataXml").val();
var myChart = new FusionCharts('file/charts/Pie3D.swf', 'ad_chart_'+generRandomCharacters(10), "100%", 120);
myChart.setDataXML(dataXml);
myChart.render("chartdivv");

function resertForm(){
/*      $('option', $('#select_org')).each(function(element) {
          $(this).removeAttr('selected').prop('selected', false);
          $('#select_org').multiselect('refresh');
     });
     $('option', $('#select_shop')).each(function(element) {
         $(this).removeAttr('selected').prop('selected', false);
         $('#select_shop').multiselect('refresh');
    }); */
     $(':input','#splitPage')   
     .not(':button, :submit, :reset, :hidden')   
     .val('')   
     .removeAttr('checked')
     .removeAttr('selected');  
     ajaxContent('/business/statistics/toDevice');
}
 
</script>
