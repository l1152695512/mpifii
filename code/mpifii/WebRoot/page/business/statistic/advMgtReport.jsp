<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="../../common/splitPage.jsp" %> 
<form id="splitPage" class="form-horizontal" action="${cxt}/business/statistics/toAdvMgtSta" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="#">运营分析－广告效果统计</a>
			</li>
		</ul>
	</div>
	<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-edit"></i> 广告效果统计查询</h2>
				<div class="box-icon">
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-down"></i></a>
				</div>
			</div>
			<div class="box-content" style="display: none;" >
				<fieldset>
				 	<div class="control-group">
				 			<label class="control-label" for="focusedInput">广告名称：</label>
							<div class="controls">
						  		<select id="select_as" name="_query.as_id" class="combox">
						  		    <option value="" selected="selected">--请选择--</option>
									<c:forEach var="as" items="${aslist}">
										<option value="${as.id}">${as.name}</option>
									</c:forEach>
								</select>
								<input  id="qAsId" value="${splitPage.queryParam.as_id}"  type="hidden" />
							</div>
							<label class="control-label" for="focusedInput">广告名称：</label>
							<div class="controls">
						  		<input class="input-xlarge focused" type="text" id="advName" name="_query.advName" value='${splitPage.queryParam.advName}' maxlength="50" >
							</div>
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
										<option value="${org.id}">${org.name}</option>
									</c:forEach>
								</select>
								<input  id="qOrgId" value="${splitPage.queryParam.org_id}"  type="hidden" />
							</div>
							<label class="control-label" for="focusedInput">商铺：</label>
							<div class="controls">
							  	<select id="select_shop" multiple="multiple" >
									<option value="">--请先选择组织--</option>
								</select>
								<input name="_query.shop_id" id="qShopId" value="${splitPage.queryParam.shop_id}" type="hidden" />
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
				<h2><i class="icon-edit"></i>广告效果统计报表</h2>
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
				<h2><i class="icon-user"></i>广告报告统计列表</h2>
			</div>
			<div class="box-content">
					<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
						    <th>组织</th>
						    <th>商铺</th>
						    <th>广告位</th>
						    <th>广告名称</th>
							<th>投放时间</th>
							<th>人流量</th>
							<th>展现次数</th>
							<th>点击次数</th>
							<th>点击率</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="adv" items="${splitPage.page.list}" >
							<tr>
							    <td class="center">${adv.get("orgname")}</td>
								<td class="center">${adv.get("shopname")}</td>
								<td class="center">${adv.get("spacename")}</td>
								<td class="center">${adv.get("advname")}</td>
								<td class="center">${adv.get("start_date")}</td>
								<td class="center">${adv.get("uv")}</td>
								<td class="center">${adv.get("show_num")}</td>
								<td class="center">${adv.get("click_num")}</td>
								<td class="center">
								<c:if test='${adv.get("show_num")==0 || adv.get("click_num")==0}'>0</c:if>
									<c:if test='${adv.get("show_num")!=0 && adv.get("click_num")!=0}'>
										<fmt:formatNumber value='${adv.get("click_num")/adv.get("show_num")}' pattern="##.##"   minFractionDigits="2" ></fmt:formatNumber>
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
var asId=$("#qAsId").val();
$('#select_as option').each(function() {
	if ($(this).val() == asId) {
		$(this).attr("value",asId);
	    $(this).attr("selected","selected");
	}else{
		$(this).removeAttr("selected");
	}
});

$('#select_org').multiselect({
	enableFiltering: true,
	maxHeight: 150,
	onChange: function(){
		var checkId = $("#select_org").val();
		$("#qOrgId").attr("value",checkId);
		$("#qShopId").attr('value','');
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
	maxDate:0,
    onSelect: function(selectedDate){
       var option = this.id == "startDate"?"minDate" : "maxDate";
       dates.not(this).datepicker("option", option, selectedDate);
    }
});
    var dataXml =$("#dataXml").val();
	var myChart = new FusionCharts('file/charts/MSColumn3D.swf', 'ad_chart_88888888', "100%",500);
	myChart.setDataXML(dataXml);
	myChart.render("chartdivv");
	
function clearSelVal(){
	$('#select_as option').each(function() {
		if ($(this).val() == '') {
			$(this).attr("value",'');
		    $(this).attr("selected","selected");
		}else{
			$(this).removeAttr("selected");
		}
	});
	$("#advName").attr('value','');
	$("#startDate").attr('value','');
	$("#endDate").attr('value','');
	var shops = $("#qShopId").val().split(",");
	for(var i=0;i<shops.length;i++){
	   $("#select_shop").multiselect('deselect',shops[i]);
	}
	var orgs = $("#qOrgId").val().split(",");
	for(var i=0;i<orgs.length;i++){
		 $('#select_org').multiselect('deselect', orgs[i]);
	}
	$("#select_shop").multiselect('dataprovider',[{value:'',label:'--请先选择组织--'}]);
	$("#qShopId").attr('value','');
	$("#qOrgId").attr('value','');
	
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

</script>
