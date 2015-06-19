<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ include file="../../common/splitPage.jsp" %> 

<style type="text/css">
	#splitPage .search-content{
		position: absolute;
		width: 98%;
		background-color: white;
		border: 1px solid #DDDDDD;
		-webkit-box-shadow: #666 0px 0px 10px;
		-moz-box-shadow: #666 0px 0px 10px;
		box-shadow: #666 0px 0px 10px;
		padding-top: 20px;
	}
	#splitPage .box-content{
		position: relative;
	}
	#splitPage .form-actions{
		margin-top: 0px;
		margin-bottom: 0px;
	}
</style>
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/statistics/toDeviceCustFlow" method="POST">
    <input type="hidden" id="isHistoryQuery" name="_query.isHistoryQuery" value="${splitPage.queryParam.isHistoryQuery}" />
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="#" >运营分析－流量统计</a>
			</li>
		</ul>
	</div>
	<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-edit"></i>流量信息查询</h2>
				<div class="box-icon">
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-down"></i></a>
				</div>
			</div>
			<div class="box-content" style="display: none;" >
				<fieldset>
					<div class="control-group">
						<label class="control-label" for="focusedInput">手机号码：</label>
						<div class="controls">
							<input class="input-xlarge focused" type="text" id="phone"
								name="_query.phone" value='${splitPage.queryParam.phone}'
								maxlength="20">
						</div>
						<label class="control-label" for="focusedInput">开始日期：</label>
						<div class="controls">
							<input type="text" id="startDate" name="_query.startDate"
								value="${splitPage.queryParam.startDate}" readonly="readonly"
								class="input-xlarge datepicker" />
						</div>
						<label class="control-label" for="focusedInput">结束日期：</label>
						<div class="controls">
							<input type="text" id="endDate" name="_query.endDate"
								value="${splitPage.queryParam.endDate}" readonly="readonly"
								class="input-xlarge datepicker" />
						</div>
						<label class="control-label" for="focusedInput">组织：</label>
						<div class="controls">
							<select id="select_org" multiple="multiple">
								<c:forEach var="org" items="${orgList}">
									<option value="${org.id}">${org.name}</option>
								</c:forEach>
							</select> <input name="_query.org_id" id="qOrgId"
								value="${splitPage.queryParam.org_id}" type="hidden" />
						</div>
						<label class="control-label" for="focusedInput">商铺：</label>
						<div class="controls">
							<select id="select_shop" multiple="multiple">
								<option value="">--请先选择组织--</option>
							</select> <input name="_query.shop_id" id="qShopId"
								value="${splitPage.queryParam.shop_id}" type="hidden" />
						</div>
					</div>
					<div class="form-actions">
						<button type="button" class="btn btn-primary"
							onclick="splitPage(1);">查询</button>
						<button type="reset" class="btn" onclick="cleardata();">清除</button>
					</div>
				</fieldset>
			</div>
		</div><!--/span-->
	</div>
	<ul class="nav nav-tabs" id="myTab">
		<li ><a data-toggle="tab"  onclick="homeClick();">当前客户流量</a></li>
		<li class="active" ><a data-toggle="tab"  onclick="profileClick();">历史客户流量</a></li>
	</ul>
	<div class="row-fluid">
		<div class="box span12">
			<div class="tab-content">
				<div class="tab-pane " id="home">
					<div class="box-content">
						<table
							class="table table-striped table-bordered bootstrap-datatable ">
							<thead>
								<tr>
									<th>日期</th>
									<th>组织名称</th>
									<th>商铺名称</th>
									<th>盒子名称</th>
									<th>手机号码</th>
									<th>终端地址</th>
									<th>上传流量(M)</th>
									<th>下载流量(M)</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="wo" items="${splitPage.page.list}">
									<tr>
										<td>${wo.get("start_date")}</td>
										<td>${wo.get("orgname")}</td>
										<td>${wo.get("shopname")}</td>
										<td>${wo.get("devicename")}</td>
										<td>${wo.get("tag")}</td>
										<td>${wo.get("client_mac")}</td>
										<td>${wo.get("upload")}</td>
										<td>${wo.get("download")}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				<div class="tab-pane active" id="profile">
					<div class="box-content">
						<table
							class="table table-striped table-bordered bootstrap-datatable ">
							<thead>
								<tr>
									<th>日期</th>
									<th>组织名称</th>
									<th>商铺名称</th>
									<th>盒子名称</th>
									<th>手机号码</th>
									<th>终端地址</th>
									<th>上传流量(M)</th>
									<th>下载流量(M)</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="wo" items="${splitPage.page.list}">
									<tr>
										<td>${wo.get("start_date")}</td>
										<td>${wo.get("orgname")}</td>
										<td>${wo.get("shopname")}</td>
										<td>${wo.get("devicename")}</td>
										<td>${wo.get("tag")}</td>
										<td>${wo.get("client_mac")}</td>
										<td>${wo.get("upload")}</td>
										<td>${wo.get("download")}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>

					</div>
				</div>
			</div>
			<div id="splitPageDiv" class="pagination pagination-centered"></div>
		</div>
	</div>
	<%-- 	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i> 流量统计报表</h2>
				<div class="box-icon" style="display:${displayVal}">
					<a href="javascript:down('${pageContext.request.contextPath}')" class="btn btn-round" title="导出"><i class="icon-download"></i></a>
				</div>
			</div>
			<div class="box-content">
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
						    <th>日期</th>
							<th>组织名称</th>
							<th>商铺名称</th>
							<th>盒子名称</th>
							<th>手机号码</th>
							<th>终端地址</th>
							<th>上传流量(M)</th>
							<th>下载流量(M)</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="wo" items="${splitPage.page.list}" >
							<tr>
							    <td>${wo.get("create_date")}</td>
								<td>${wo.get("orgname")}</td>
								<td>${wo.get("shopname")}</td>
                                <td>${wo.get("devicename")}</td>
                                <td>
                                ${wo.get("tag")}
                                </td>
                                <td>${wo.get("client_mac")}</td>
                                <td>
                                ${wo.get("upload")}
                                </td>
                                <td>${wo.get("download")}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div id="splitPageDiv" class="pagination pagination-centered"></div>
			</div>
		</div>
		<!--/span-->
	</div>
	<!--/row-->--%>
</form>
<script type="text/javascript">
	
	$("#splitPage .search-content").hide();
	$("#splitPage .search-content").click(function(){
		return false;//禁止该事件冒泡
	});
	$("#splitPage .box-header .box-icon .icon-search").parent().click(function(){
		var obj = $("#splitPage .search-content");
		if(obj.is(':hidden')){
			$("#splitPage .search-content").slideDown();
			return false;//禁止该事件冒泡
		}
	});
	
	$("#splitPage .box-content tbody .icon-edit").parent().click(function(){
		var woid = $(this).parent().data("user");
		ajaxContent('/business/app/workOrder/edit',{woid:woid});
	});

	$('#select_org').multiselect({
		enableFiltering : true,
		maxHeight : 150,
		onChange : function() {
			var checkId = $("#select_org").val();
			$("#qOrgId").attr("value", checkId);
			$("#qShopId").attr('value', '');
			var url = "/business/shop/getShopByOrg?orgids=" + checkId;
			$.ajax({
				type : 'POST',
				dataType : "json",
				url : encodeURI(encodeURI(cxt + url)),
				success : function(data) {
					$("#select_shop").multiselect('dataprovider', data);
				}
			});
		}
	});
	$('#select_shop').multiselect({
		enableFiltering : true,
		maxHeight : 150,
		onChange : function() {
			var checkId = $("#select_shop").val();
			$("#qShopId").attr("value", checkId);
		}
	});

	var checkedOrg = $("#qOrgId").val();
	if (checkedOrg.length > 0) {
		var orgs = checkedOrg.split(",");
		for (var i = 0; i < orgs.length; i++) {
			$('#select_org').multiselect('select', orgs[i]);
		}
	}
	var checkedShop = $("#qShopId").val();

	if (checkedOrg.length > 0) {
		var url = "/business/shop/getShopByOrg?orgids=" + checkedOrg;
		$.ajax({
			type : 'POST',
			dataType : "json",
			url : encodeURI(encodeURI(cxt + url)),
			success : function(data) {
				$("#select_shop").multiselect('dataprovider', data);
				var shops = checkedShop.split(",");
				for (var i = 0; i < shops.length; i++) {
					$("#select_shop").multiselect('select', shops[i]);
				}

			}
		});
	}
	var dates = $("#startDate,#endDate");
	dates.datepicker({
		maxDate : 0,
		onSelect : function(selectedDate) {
			var option = this.id == "startDate" ? "minDate" : "maxDate";
			dates.not(this).datepicker("option", option, selectedDate);
		}
	});

	function cleardata() {
		$("#phone").attr('value', '');
		$("#startDate").attr('value', '');
		$("#endDate").attr('value', '');
		var shops = $("#qShopId").val().split(",");
		for (var i = 0; i < shops.length; i++) {
			$("#select_shop").multiselect('deselect', shops[i]);
		}
		var orgs = $("#qOrgId").val().split(",");
		for (var i = 0; i < orgs.length; i++) {
			$('#select_org').multiselect('deselect', orgs[i]);
		}
		$("#select_shop").multiselect('dataprovider', [ {
			value : '',
			label : '--请先选择组织--'
		} ]);
		$("#qShopId").attr('value', '');
		$("#qOrgId").attr('value', '');
	}


	$('#myTab a:first').on("shown", function(event) {
		if ($("#isHistoryQuery").val() == "1") {
			$('#myTab li:first').attr("class", "");
			$('#myTab li:first').removeClass("active");
			$('#myTab li:last').addClass("active");
			$('#myTab a:last').tab('show');//初始化显示哪个tab
			$('#home').removeClass("active");
			$('#profile').addClass("active");
		} else {
			$('#myTab li:last').removeClass("active");
			$('#myTab li:first').addClass("active");
			$('#myTab a:first').tab('show');//初始化显示哪个tab
			$('#profile').removeClass("active");
			$('#home').addClass("active");
		}
	});

	function homeClick() {
		$("#isHistoryQuery").attr("value", "0");
		splitPage(1);
	}
	function down(ctx){
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var orgId = $('#qOrgId').val();
		var shopId = $('#qShopId').val();
		var phone = $("input[name='_query.phone']").val();
		var url=ctx+'/business/statistics/downFlowStaFile?startDate='+startDate+'&endDate='+endDate+'&orgId='+orgId+"&shopId="+shopId+"&phone="+phone;
		window.location.href=url;
	}
	function profileClick() {
		$("#isHistoryQuery").attr("value", "1");
		splitPage(1);
	}
</script>