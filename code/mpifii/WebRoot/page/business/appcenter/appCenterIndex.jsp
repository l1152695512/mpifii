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
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/app/index" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="javascript:void(0);" onclick="ajaxContent('/business/app/index');">应用管理</a>
			</li>
		</ul>
	</div>
	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i>应用列表</h2>
				<div class="box-icon">
					<a href="javascript:void(0);" class="btn btn-round" title="查找"><i class="icon-search"></i></a>
					<!-- <a href="javascript:void(0);" class="btn btn-round" title="添加应用" onclick="ajaxContent('/business/app/add');"><i class="icon-plus-sign"></i></a> -->
				</div>
			</div>
			<div class="box-content">
				<div class="search-content"><!-- 这里必须取名为search-content，否则搜索框离开焦点的点击事件不会触发搜索框的隐藏 -->
					<fieldset><!-- 这下面搜索的字段要与数据库里表的字段对应 -->
					  	<div class="control-group">
							<label class="control-label" for="focusedInput">应用名称：</label>
							<div class="controls">
						  		<input class="input-xlarge focused" type="text" name="_query.name_like" value='${splitPage.queryParam.name_like}' maxlength="20" >
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
							<th width="60" >应用名称</th>
							<th width="60" >应用分类</th>
							<th width="60" >应用状态</th>
							<th width="60" >应用URL</th>
							<th width="60" >应用link</th>
							<th width="50">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="app" items="${splitPage.page.list}" >
							<tr>
								<td>${app.get("name")}</td>
								<td class="center">${app.get("className")}</td>
								<td  class="center">
									<c:if test="${app.get('status')=='1'}">
										启用
									</c:if>
									<c:if test="${app.get('status')=='0'}">
										冻结
									</c:if>
								</td>
								<td class="center">${app.get("edit_url")}</td>
								<td class="center">${app.get("link")}</td>
								<td class="center" data-id='${app.get("id")}'>
									<!-- <a class="btn btn-info" href="javascript:void(0);">  <i class="icon-edit icon-white"></i> 编辑</a>
									<a class="btn btn-danger" href="javascript:void(0);"> <i class="icon-trash icon-white"></i> 删除</a> -->
									<a class="btn btn-default btn-sm"  href="javascript:void(0);"> <i class="icon-wrench icon-black"></i> 配置</a>
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
		ajaxContent('/business/app/edit',{id:id});
	});
	$("#splitPage .box-content tbody .icon-trash").parent().click(function(){
		var id = $(this).parent().data("id");
		var url='/business/app/delete?id='+id;
		myConfirm("确定删除吗？",function(){
			$.ajax({
				type : 'POST',
				dataType : "json",
				url :encodeURI(encodeURI(cxt + url)),
				success : function(data) {
					if(eval(data).state=='error'){
						myAlert("删除失败！",function(){
							var url = "/business/app";
							ajaxContent(url);
						});
					}else{
						myAlert("删除成功！",function(){
							var url = "/business/app";
							ajaxContent(url);
						});
						
					}
				}
			});
		});
	});
	$("#splitPage .box-content tbody .icon-wrench").parent().click(function(){
		var id = $(this).parent().data("id");
		var url='/business/app/configInfo?id='+id;
		$.ajax({
			type : 'POST',
			dataType : "json",
			url :encodeURI(encodeURI(cxt + url)),
			success : function(data) {
				if(eval(data).state=='error'){
					myAlert("暂不可进行此操作！")
				}else{
					var url = eval(data).state;
					ajaxContent(url);
				}
			}
		});
	});
</script>