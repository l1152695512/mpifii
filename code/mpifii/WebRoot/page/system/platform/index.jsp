<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<<<<<<< HEAD
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
=======
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
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
<<<<<<< HEAD
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
=======
					<!-- <a href="javascript:void(0);" class="btn btn-round" title="查找"><i class="icon-search"></i></a> -->
					<a href="javascript:void(0);" class="btn btn-round" title="添加用户" onclick="ajaxContent('/system/platform/addOrModify');"><i class="icon-plus-sign"></i></a>
<!-- 					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>  -->
<!-- 					<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> -->
				</div>
			</div>
			<div class="box-content">
				<div class="search-content"><!-- 这里必须取名为search-content，否则搜索框离开焦点的点击事件不会触发搜索框的隐藏 -->
					<fieldset><!-- 这下面搜索的字段要与数据库里表的字段对应 -->
					  	<div class="control-group">
							<label class="control-label" for="focusedInput">账号：</label>
							<div class="controls">
						  		<input class="input-xlarge focused" type="text" name="_query.name_like" value='${splitPage.queryParam.name_like}' maxlength="20" >
							</div>
					  	</div>
					  
					  	<div class="control-group">
							<label class="control-label">邮箱：</label>
							<div class="controls">
						  		<input class="input-xlarge" type="text" name="_query.email_like" value='${splitPage.queryParam.email_like}' maxlength="40" >
							</div>
					  	</div>
					
					  	<div class="form-actions">
							<button type="button" class="btn btn-primary" onclick="splitPage(1);">查询</button>
							<button type="reset" class="btn">清除</button>
					  	</div>
					</fieldset>
				</div>
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
							<th onclick="orderbyFun('name')">平台名称</th>
							<th onclick="orderbyFun('down_url')">下载地址</th>
							<th onclick="orderbyFun('auth_url')">认证地址</th>
							<th onclick="orderbyFun('log_ip')">登录ip</th>
							<th onclick="orderbyFun('log_port')">登录端口</th>
							<th onclick="orderbyFun('home_url')">主页地址</th>
							<th onclick="orderbyFun('create_date')">创建时间</th>
							<th width="300">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="user" items="${splitPage.page.list}" >
							<tr>
								<td>${user.get("name")}</td>
								<td class="center">${user.get("down_url")}</td>
								<td class="center">${user.get("auth_url")}</td>
								<td class="center">${user.get("log_ip")}</td>
								<td class="center">${user.get("log_port")}</td>
								<td class="center">${user.get("home_url")}</td>
								<td class="center">${user.get("create_date")}</td>
								<td class="center" data-id='${user.get("id")}'>
									<a class="btn btn-info" href="javascript:void(0);"> <i class="icon-edit icon-white"></i> 编辑</a>
									<a class="btn btn-danger" href="javascript:void(0);"> <i class="icon-trash icon-white"></i>删除</a>
									<a class="btn btn-default btn-sm" href="javascript:void(0);"> <i class="icon-wrench icon-black"></i> 分配盒子</a> 
									

>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
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
<<<<<<< HEAD
=======
// 	$("#splitPage .box-content tbody .icon-wrench").parent().click(function(){
// 		var id = $(this).parent().data("id");
// 		ajaxDiaLog('/system/user/configDevice',{id:id});
// 	});
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
	$("#splitPage .box-content tbody .icon-zoom-in").parent().click(function(){
		var id = $(this).parent().data("id");
		ajaxContent('/system/platform/view',{id:id});
	});
	$("#splitPage .box-content tbody .icon-wrench").parent().click(function(){
<<<<<<< HEAD
		var platNo = $(this).parent().data("id");
		var url ="/system/platform/checkOwner?id="+id;
=======
		var id = $(this).parent().data("id");
		var url ="/system/platform/checkOwner?id="+id;
/* 		$.ajax({
			type : 'POST',
			dataType : "json",
			url :encodeURI(encodeURI(cxt + url)),
			success : function(data) {
				{
					
				}
			}
		}); */
		
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
		
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
<<<<<<< HEAD
				url: 'system/platform/configDevice?id='+id,
				data:{"platNo":platNo}
=======
				url: 'system/platform/configDevice?id='+id
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
			}
		}).showModal();
	});	
	
</script>