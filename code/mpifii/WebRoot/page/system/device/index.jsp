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
<<<<<<< HEAD
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/system/warehouse" method="POST">
=======
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/device" method="POST">
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
<<<<<<< HEAD
				<a href="javascript:void(0);" onclick="ajaxContent('/system/warehouse/index');">盒子分配</a>
=======
				<a href="javascript:void(0);" onclick="ajaxContent('/system/page/index');">盒子分配</a>
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
			</li>
		</ul>
	</div>

	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i>盒子列表</h2>
				<div class="box-icon">
<<<<<<< HEAD
					<a href="javascript:void(0);" class="btn btn-round" title="查找"><i class="icon-search"></i></a>
					<a href="javascript:void(0);" class="btn btn-round" title="添加盒子" onclick="ajaxContent('/system/warehouse/addOrModify');"><i class="icon-plus-sign"></i></a>
=======
<!-- 					<a href="javascript:void(0);" class="btn btn-round" title="查找"><i class="icon-search"></i></a> -->
					<a href="javascript:void(0);" class="btn btn-round" title="添加盒子" onclick="ajaxContent('/system/page/addOrModify');"><i class="icon-plus-sign"></i></a>
<!-- 					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>  -->
<!-- 					<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> -->
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
				</div>
			</div>
			<div class="box-content">
				<div class="search-content"><!-- 这里必须取名为search-content，否则搜索框离开焦点的点击事件不会触发搜索框的隐藏 -->
					<fieldset><!-- 这下面搜索的字段要与数据库里表的字段对应 -->
					  	<div class="control-group">
<<<<<<< HEAD
							<label class="control-label" for="focusedInput">SN</label>
							<div class="controls">
						  		<input class="input-xlarge focused" type="text" name="_query.sn_like" id="sn_like" value='${splitPage.queryParam.sn_like}' maxlength="20" >
							</div>
					  	</div>
					  	
					  	<div class="control-group">
							<label class="control-label">类型</label>
							<div class="controls">
								<select name="_query.type" id="type" class="input-xlarge">
									<option value="">全部</option>
									<option value="1" <c:if test="${splitPage.queryParam.type == '1'}">selected="selected"</c:if>>route</option>
									<option value="2" <c:if test="${splitPage.queryParam.type == '2'}">selected="selected"</c:if>>AP</option>
								</select>
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label">平台</label>
							<div class="controls">
								<select name="_query.plat_no" id="plat_no" class="input-xlarge">
									<option value="">全部</option>
									<option value="-1" <c:if test="${splitPage.queryParam.plat_no=='-1' }">selected="selected"</c:if>>待定</option>
									<c:forEach var="plat" items="${platList}" >
										<option value="${plat.plat_no }" <c:if test="${splitPage.queryParam.plat_no==plat.plat_no }">selected="selected"</c:if>>${plat.name }[${plat.plat_no }]</option>
									</c:forEach>	
								</select>
							</div>
						</div>
					
					  	<div class="form-actions">
							<button type="button" class="btn btn-primary" onclick="splitPage(1);">查询</button>
							<button type="reset" class="btn" onclick="clearform()">清除</button>
=======
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
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
					  	</div>
					</fieldset>
				</div>
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
<<<<<<< HEAD
							<th>SN</th>
							<th>类型</th>
							<th>平台</th>
=======
							<th>类型</th>
							<th>SN</th>
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
							<th>注册时间</th>
							<th width="180">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="device" items="${splitPage.page.list}" >
							<tr>
<<<<<<< HEAD
								<td class="center">${device.get("sn")}</td>
								<td class="center">${device.get("type")==1?'route':'AP'}</td>
								<c:choose>
									<c:when test='${device.get("plat_no")=="-1"}'>
										<td class="center">待定</td>
									</c:when>
									<c:otherwise>
										<td class="center">${device.get("plat_no") }[${device.get("name") }]</td>
									</c:otherwise>
								</c:choose>
								
=======
								<td class="center">${device.get("palt_type")}</td>
								<td class="center">${device.get("sn")}</td>
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
								<td class="center">${device.get("create_date")}</td>
								<td class="center" data-id='${device.get("id")}'>
									<a class="btn btn-info" href="javascript:void(0);"> <i class="icon-edit icon-white"></i> 编辑</a>
									<a class="btn btn-danger" href="javascript:void(0);"> <i class="icon-trash icon-white"></i> 删除</a>
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
<<<<<<< HEAD
		ajaxContent('/system/warehouse/addOrModify',{id:id});
=======
		ajaxContent('/system/page/addOrModify',{id:id});
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
	});
	$("#splitPage .box-content tbody .icon-trash").parent().click(function(){
		var id = $(this).parent().data("id");
		myConfirm("确定要删除该数据？",function(){
			$.ajax({
				type: "POST",
<<<<<<< HEAD
				url: "system/warehouse/delete",
				data: {id:id},
				success: function(data,status,xhr){
					ajaxContent('/system/warehouse');
=======
				url: "system/page/delete",
				data: {id:id},
				success: function(data,status,xhr){
					ajaxContent('/system/page');
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
				}
			});
		});
	});
<<<<<<< HEAD
	
	function clearform(){
		$("#sn_like").val("");
		$("#type").val("");
		$("#plat_no").val("");
	}
=======
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
</script>