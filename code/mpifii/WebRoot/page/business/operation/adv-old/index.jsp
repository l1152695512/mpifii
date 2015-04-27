<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ include file="../../../common/splitPage.jsp" %> 

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
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/operation/adv" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="javascript:void(0);" onclick="ajaxContent('/business/operation/adv');">广告权限</a>
			</li>
		</ul>
	</div>

	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i>商铺组列表</h2>
				<div class="box-icon">
<!-- 					<a href="javascript:void(0);" class="btn btn-round" title="查找"><i class="icon-search"></i></a> -->
					<a href="javascript:void(0);" class="btn btn-round" style="width:90px;" title="添加分组" onclick="addOrModifyAdvGroup('商铺组添加');"><i class="icon-plus-sign"></i><span>添加分组</span></a>
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
							<th>所属用户</th>
							<th>分组名称</th>
							<th>设备数量</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="rowData" items="${splitPage.page.list}" >
							<tr>
								<td>${rowData.get("userName")}</td>
								<td class="center">${rowData.get("groupName")}</td>
								<td class="center">${rowData.get("deviceNum")}</td>
								<td class="center" data-id='${rowData.get("id")}'>
									<c:if test="${not empty isAdmin}">
										<a class="btn btn-default btn-sm" href="javascript:void(0);"> <i class="icon-wrench icon-black"></i>权限分配</a>
<!-- 										<a class="btn btn-success" href="javascript:void(0);"> <i class="icon-zoom-in icon-white"></i>查看</a> -->
									</c:if>
									<a class="btn btn-info" href="javascript:void(0);">  <i class="icon-wrench icon-white"></i>设置广告</a>
									<a class="btn btn-info" href="javascript:void(0);"> <i class="icon-edit icon-white"></i> 编辑</a>
									<c:if test='${rowData.get("access_delete") == "1"}'>
										<a class="btn btn-danger" href="javascript:void(0);"> <i class="icon-trash icon-white"></i> 删除</a>
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
	if($("#splitPage .box-content tbody .icon-wrench.icon-black")){
		$("#splitPage .box-content tbody .icon-wrench.icon-black").parent().click(function(){
			var id = $(this).parent().data("id");
			ajaxContent('/business/operation/adv/permission',{'_query.groupId':id});
		});
	}
	$("#splitPage .box-content tbody .icon-wrench.icon-white").parent().click(function(){
		var id = $(this).parent().data("id");
		ajaxContent('/business/operation/adv/putin',{'_query.groupId':id});
	});
	$("#splitPage .box-content tbody .icon-edit").parent().click(function(){
		var id = $(this).parent().data("id");
// 		ajaxContent('/business/operation/adv/addOrModify',{'_query.groupId':id});
		addOrModifyAdvGroup("商铺组修改",id);
	});
	function addOrModifyAdvGroup(title,id){
		var data = {};
		if(id != undefined){
			data = {id:id};
		}
		$.fn.SimpleModal({
			title: title,
			width: 450,
			param: {
				url: 'business/operation/adv/addOrModify',
				data: data
			},
			buttons: [{
	    		text:'确定',
	    		classe:'btn primary btn-margin',
	    		clickEvent:function() {
// 	    			$("#adv_group_info form input[name='shopGroupId']").val("${shopGroupId}");
	    			$("#adv_group_info form").ajaxSubmit({
	    				success : function(resp) {
	    					closePop();
	    					ajaxContent('/business/operation/adv');
	    				}
	    			});
	            }
	    	},{
	    		text:'取消',
	    		classe:'btn secondary'
	    	}]
		}).showModal();
	}
	
	$("#splitPage .box-content tbody .icon-trash").parent().click(function(){
		var id = $(this).parent().data("id");
		myConfirm("确定要删除该分组？<br><br><br>提示：删除后，该分组下的商铺会自动转换到默认分组里。",function(){
			$.ajax({
				type : "POST",
				url : 'business/operation/adv/delete',
				data : {id:id},
				success : function(data) {
					ajaxContent('/business/operation/adv');
				}
			});
		});
	});
</script>