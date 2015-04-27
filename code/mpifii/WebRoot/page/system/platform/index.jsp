<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/system/platform" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="javascript:void(0);" onclick="ajaxContent('/system/platform');">平台管理</a>
			</li>
		</ul>
	</div>

	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i>平台列表</h2>
				<div class="box-icon">
					<a href="javascript:void(0);" class="btn btn-round" title="添加用户" onclick="ajaxContent('/system/platform/addOrModify');"><i class="icon-plus-sign"></i></a>
				</div>
			</div>
			<div class="box-content">
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
							<th>编号</th>
							<th>名称</th>
							<th>下载地址</th>
							<th>认证地址</th>
							<th>日志IP</th>
							<th>日志端口</th>
							<th>Home地址</th>
							<th>创建时间</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="plat" items="${splitPage.page.list}" >
							<tr>
								<td class="center">${plat.get("plat_no")}</td>
								<td class="center">${plat.get("name")}</td>
				                <td class="center">
				                ${fn:substring(plat.get("down_url"),0,10) }
				                <c:if test="${fn:length(plat.get('down_url'))>10 }">...</c:if>
				                </td>
								<td class="center">
								 ${fn:substring(plat.get("auth_url"),0,10) }
				                <c:if test="${fn:length(plat.get('auth_url'))>10 }">...</c:if>
								</td>
								<td class="center">${plat.get("log_ip")}</td>
								<td class="center">${plat.get("log_port")}</td>
								<td class="center">
								 ${fn:substring(plat.get("home_url"),0,10) }
				                <c:if test="${fn:length(plat.get('home_url'))>10 }">...</c:if>
								</td>
								<td class="center">${plat.get("create_date")}</td>
								<td class="center" data-id='${plat.get("id")}'>
									<a class="btn btn-info" href="javascript:void(0);"> <i class="icon-edit icon-white"></i> 编辑</a>
									<a class="btn btn-danger" href="javascript:void(0);"> <i class="icon-trash icon-white"></i>删除</a>
									<a class="btn btn-default btn-sm" href="javascript:void(0);"> <i class="icon-wrench icon-black"></i> 分配盒子</a> 
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
		var id = $(this).parent().data("id");
		ajaxContent('/system/platform/addOrModify',{id:id});
	});
	$("#splitPage .box-content tbody .icon-trash").parent().click(function(){
		var id = $(this).parent().data("id");
		myConfirm("确定要删除该数据？",function(){
			$.ajax({
				type: "POST",
				url: "system/platform/delete",
				data: {id:id},
				success: function(data,status,xhr){
					ajaxContent('/system/platform');
				}
			});
		});
	});
	$("#splitPage .box-content tbody .icon-zoom-in").parent().click(function(){
		var id = $(this).parent().data("id");
		ajaxContent('/system/platform/view',{id:id});
	});
	$("#splitPage .box-content tbody .icon-wrench").parent().click(function(){
		var platNo = $(this).parent().data("id");
		var url ="/system/platform/checkOwner?id="+id;
		
		$.fn.SimpleModal({
			title: '盒子分配',
			width: 750,
	        keyEsc:true,
			buttons: [{
	    		text:'确定',
	    		classe:'btn primary btn-margin',
	    		clickEvent:function() {
	    			var shopId = $("#info_id").val();
	    	    	var ids = "";
	    	    	$("input[name='device_checkselect_list']:checked").each(function(obj) {//遍历所有选中状态的checkBox
	    				var id = $(this).val();
	    				ids += id + ",";
	    			});
	    	    	if (ids != '') {
	    	    		ids = ids.substr(0,ids.length-1);
	    	    	}
	    			$.ajax({
	    					type : 'POST',
	    					url : cxt + '/system/page/saveShopAssignedDevice?ids='+ids+'&shopId='+shopId,
	    					success : function(data) {
	    						myAlert("配置成功!",function(){
	    							closePop();
	    							ajaxContent("/system/platform");
	    						});
	    					}
	    			});
	            }
	    	},{
	    		text:'取消',
	    		classe:'btn secondary'
	    	}],
			param: {
				url: 'system/platform/configDevice?id='+id,
				data:{"platNo":platNo}
			}
		}).showModal();
	});	
	
</script>