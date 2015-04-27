<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ include file="../../../../common/splitPage.jsp" %> 

<style type="text/css">
	#splitPage table th{
		text-align: center;
	}
	#splitPage table td{
		text-align: center;
	}
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
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/operation/adv/putin" method="POST">
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
				<h2><i class="icon-user"></i>设置广告</h2>
				<div class="box-icon">
<!-- 					<a href="javascript:void(0);" class="btn btn-round" title="查找"><i class="icon-search"></i></a> -->
<!-- 					<a href="javascript:void(0);" class="btn btn-round" title="添加盒子" onclick="ajaxContent('/business/device/addOrModify');"><i class="icon-plus-sign"></i></a> -->
<!-- 					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>  -->
<!-- 					<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> -->
				</div>
			</div>
			<div class="box-content">
				<div class="search-content"><!-- 这里必须取名为search-content，否则搜索框离开焦点的点击事件不会触发搜索框的隐藏 -->
					<input type="hidden" name="_query.groupId" value="${groupId}"/>
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
							<th>模板</th>
							<th>广告名称</th>
							<th>广告图</th>
							<th>链接地址</th>
							<th>描述</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="rowData" items="${splitPage.page.list}" >
							<tr>
								<td>${rowData.get("templateName")}</td>
								<td>${rowData.get("advName")}</td>
								<td><img style="height: 80px;" src="${rowData.get("image")}"/></td>
								<td>${rowData.get("link")}</td>
								<td>${rowData.get("des")}</td>
								<td data-adv-id='${rowData.get("advId")}' data-group-role-id='${rowData.get("groupRoleId")}' data-img-size='${rowData.get("img_size_tips")}'>
									<a class="btn btn-info" href="javascript:void(0);">  <i class="icon-edit icon-white"></i>编辑广告</a>
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
		var advId = $(this).parent().data("advId");
		var groupRoleId = $(this).parent().data("groupRoleId");
		var defaultSize = $(this).parent().data("imgSize");
		if(defaultSize == undefined || "" == defaultSize){
			defaultSize = "290px * 150px";
		}
		$.fn.SimpleModal({
			title: '广告配置',
			param: {
				url: 'business/operation/adv/putin/edit',
				data:{advId:advId,groupRoleId:groupRoleId,size:defaultSize,groupId:"${groupId}"}
			}
		}).showModal();
	});
</script>