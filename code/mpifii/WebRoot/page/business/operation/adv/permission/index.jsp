<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ include file="../../../../common/splitPage.jsp" %> 

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
<<<<<<< HEAD
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/oper/adv/permission" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider"></span></li>
=======
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/operation/adv/permission" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="javascript:void(0);" onclick="ajaxContent('/business/operation/adv');">广告权限</a>
			</li>
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
		</ul>
	</div>

	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
<<<<<<< HEAD
				<h2><i class="icon-user"></i>广告权限分配</h2>
=======
				<h2><i class="icon-user"></i>广告权限列表</h2>
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
				<div class="box-icon">
<!-- 					<a href="javascript:void(0);" class="btn btn-round" title="查找"><i class="icon-search"></i></a> -->
<!-- 					<a href="javascript:void(0);" class="btn btn-round" title="添加盒子" onclick="ajaxContent('/business/device/addOrModify');"><i class="icon-plus-sign"></i></a> -->
<!-- 					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>  -->
<!-- 					<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> -->
				</div>
			</div>
			<div class="box-content">
<<<<<<< HEAD
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
							<th>组织机构</th>
							<th>用户数量</th>
							<th>商铺数量</th>
							<th>已授权广告数量</th>
=======
				<div class="search-content"><!-- 这里必须取名为search-content，否则搜索框离开焦点的点击事件不会触发搜索框的隐藏 -->
					<fieldset><!-- 这下面搜索的字段要与数据库里表的字段对应 -->
					  	<input type="hidden" name="_query.groupId" value="${groupId}"/>
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
							<th>模板</th>
							<th>广告名称</th>
							<th>所属角色</th>
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="rowData" items="${splitPage.page.list}" >
							<tr>
<<<<<<< HEAD
								<td>${rowData.get("name")}</td>
								<td class="center">${rowData.get("persons")}</td>
								<td class="center">${rowData.get("shops")}</td>
								<td class="center">${rowData.get("adv_num")}</td>
								<td class="center" data-id='${rowData.get("id")}'>
									<a class="btn btn-default btn-sm" href="javascript:void(0);"> <i class="icon-wrench icon-black"></i>分配广告</a>
=======
								<td>${rowData.get("templateName")}</td>
								<td class="center">${rowData.get("advName")}</td>
								<td class="center">${rowData.get("roleName")}</td>
								<td class="center" data-id='${rowData.get("id")}' data-group-role-id='${rowData.get("groupRoleId")}' data-role-id='${rowData.get("roleId")}'>
									<a class="btn btn-default btn-sm" href="javascript:void(0);"> <i class="icon-wrench icon-black"></i>设置权限</a>
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
<<<<<<< HEAD
	$("#splitPage .box-content tbody .icon-wrench").parent().click(function(){
		var id = $(this).parent().data("id");
		$.fn.SimpleModal({
			title: '广告授权',
			width: 600,
	        keyEsc:true,
			param: {
				url: 'business/oper/adv/permission/edit',
				data: {orgId:id}
			},
			buttons: [{
	    		text:'确定',
	    		classe:'btn primary btn-margin',
	    		clickEvent:function() {
	    			submitAdvSetting();
	            }
	    	},{
	    		text:'取消',
	    		classe:'btn secondary'
	    	}]
		}).showModal();
	});
=======
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
	if($("#splitPage .box-content tbody .icon-wrench")){
		$("#splitPage .box-content tbody .icon-wrench").parent().click(function(){
			var id = $(this).parent().data("id");
			var groupRoleId = $(this).parent().data("groupRoleId");
			var oldRoleId = $(this).parent().data("roleId");
// 			ajaxContent('/business/operation/adv/permission/edit',{id:id,groupId:"${groupId}",groupRoleId:groupRoleId});
			$.fn.SimpleModal({
				title: '设置权限',
				width: 400,
		        keyEsc:true,
				buttons: [{
		    		text:'确定',
		    		classe:'btn primary btn-margin',
		    		clickEvent:function() {
		    			var selectedRoleId = $("#choice_role_list input[name='selected_role']:checked").val();
		    			if(selectedRoleId == undefined){
		    				myAlert("请选择角色！");
		    				return;
		    			}
		    			if(selectedRoleId != oldRoleId){
		    				$.ajax({
			    				type : 'POST',
			    				url :"business/operation/adv/permission/save",
			    				data:{groupRoleId:groupRoleId,groupId:"${groupId}",roleId:selectedRoleId,advId:id},
			    				success : function(data) {
			    					closePop();
			    					ajaxContent('/business/operation/adv/permission',{'_query.groupId':"${groupId}"});
			    				}
			    			});
		    			}else{
		    				closePop();
		    			}
		            }
		    	},{
		    		text:'取消',
		    		classe:'btn secondary'
		    	}],
				param: {
					url: 'business/operation/adv/permission/edit',
					data: {roleId:oldRoleId}
				}
			}).showModal();
		});
	}
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
</script>