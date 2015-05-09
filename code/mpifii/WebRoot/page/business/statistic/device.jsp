<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
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
</style>
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/statistics/toDevice" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="#">运营分析－盒子</a>
			</li>
		</ul>
	</div>
	
	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i>报表分析</h2>
				<div class="box-icon">
<!-- 					<a href="javascript:void(0);" class="btn btn-round" title="查找"><i class="icon-search"></i></a> -->
				</div>
			</div>
			<div class="box-content">
				<div >
					<div id="chartdivv"></div>
				</div>
			</div>
		</div>
	</div>
		

	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i>盒子列表</h2>
				<div class="box-icon">
					<input type="hidden" id="queryid" name="_query.id_in" value='${shopIds}' >
					<a href="javascript:void(0);" class="btn btn-round" title="查找"><i class="icon-search"></i></a>
				</div>
			</div>
			<div class="box-content">
				<div class="search-content"><!-- 这里必须取名为search-content，否则搜索框离开焦点的点击事件不会触发搜索框的隐藏 -->
					<fieldset><!-- 这下面搜索的字段要与数据库里表的字段对应 -->
						<div class="modal-body">
							<div class="row-fluid sortable">
								<div class="box span6">
									<div class="box-header well" data-original-title>
										<h2>
											<i class="icon-user"></i>未选择
										</h2>
										<div class="box-icon">
										</div>
									</div>
									<div class="box-content">
										<select id="allShop" multiple size="10" ondblclick="addChecked(this);" >
											<c:forEach items="${shopList}" var="shop">
												<option value="${shop.id}">${shop.name}</option>
											</c:forEach>
									  	</select>
									</div>
								</div>
								
								<div class="box span6">
									<div class="box-header well" data-original-title>
										<h2>
											<i class="icon-user"></i>已选择
										</h2>
										<div class="box-icon">
										<a href="javascript:void(0);" class="btn btn-round" title="确定"><i id="searchOK" class="icon-search"></i></a>
										</div>
									</div>
									<div class="box-content">
										<select id="checkedShop" multiple size="10" ondblclick="delChecked(this);" >
									  		<c:forEach items="${ckShopList}" var="shop">
												<option value="${shop.id}">${shop.name}</option>
											</c:forEach>
									  	</select>
									</div>
								</div>
							</div>  
						</div>
					</fieldset>
				</div>
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
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
								<td class="center">${device.get("shopName")}</td>
								<td class="center">${device.get("name")}</td>
								<td class="center">${device.get("router_sn")}</td>
								<td class="center">${device.get("online_num")>=0?'在线':'离线'}</td>
								<td class="center">route</td>
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
	
	var myChart = new FusionCharts('file/charts/Pie3D.swf', 'ad_chart_2014', "100%", 120);
	myChart.setDataXML("<chart palette='4' decimals='0' showValues='1' labelSepChar=':' enableSmartLabels='1' enableRotation='0'  bgColor='99CCFF,FFFFFF' bgAlpha='40,100' bgRatio='0,100' bgAngle='360' showBorder='0' startingAngle='80' ><set label='在线' value='13'/><set label='离线' value='7'/></chart>");
	myChart.render("chartdivv");
	
</script>



<script type="text/javascript">
	
	function addChecked(sNode){
		var index = sNode.selectedIndex;
		var option = sNode.options[index];
		option.selected = false;
		document.getElementById("checkedShop").add(option);
	}

	function delChecked(sNode){
		var index = sNode.selectedIndex;
		var option = sNode.options[index];
		option.selected = false;
		document.getElementById("allShop").add(option);
	}
	function queryShop(){
		var name  = $("input[name='shopName']").val();
		 $.ajax({
			type: "post",
			dataType: "json",
			data:{name:name},
			url: "${cxt}/business/sysupdate/checkVersion",
			success: function(result){
				if(result.state == "success"){
				
		       	}else{
		       		
		       	}
			}
		});
	}
	
	$("#searchOK").parent().click(function(){
		var checkedRole = document.getElementById("checkedShop");
		var length = checkedRole.length;
		var shopIds = "";
		for (var i=0; i<length; i++){
			if(i==0){
				shopIds += checkedRole.options[i].value ;
			}else{
				shopIds += ","+checkedRole.options[i].value ;
			}
	    }
		
		$("#queryid").val(shopIds);
		
		splitPage(1);
	});
	
	
	function getAdCharts(){
		
	};
</script>