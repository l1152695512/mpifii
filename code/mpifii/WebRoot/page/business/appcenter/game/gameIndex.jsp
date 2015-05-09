<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ include file="../../../common/splitPage.jsp" %> 

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
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/app/game" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="#" onclick="ajaxContent('/business/app');">应用管理</a><span class="divider">/</span>
			</li>
			<li>
				<a href="#" onclick="ajaxContent('/business/app/game');">游戏管理</a>
			</li>
		</ul>
	</div>
	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i>游戏列表</h2>
				<div class="box-icon">
					<a href="javascript:void(0);" class="btn btn-round" title="查找"><i class="icon-search"></i></a>
					<a href="javascript:void(0);" class="btn btn-round" title="添加游戏" onclick="ajaxContent('/business/app/game/add');"><i class="icon-plus-sign"></i></a>
				</div>
			</div>
			<div class="box-content">
				<div class="search-content"><!-- 这里必须取名为search-content，否则搜索框离开焦点的点击事件不会触发搜索框的隐藏 -->
					<fieldset><!-- 这下面搜索的字段要与数据库里表的字段对应 -->
					  	<div class="control-group">
							<label class="control-label" for="focusedInput">游戏名称：</label>
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
							<th onclick="orderbyFun('name')">游戏名称</th>
							<th onclick="orderbyFun('className')">次数</th>
							<th onclick="orderbyFun('tel')">游戏链接</th>
							<th onclick="orderbyFun('status')">状态</th>
							<th width="280">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="game" items="${splitPage.page.list}" >
							<tr>
								<td>${game.get("name")}</td>
								<td class="center">${game.get("times")}</td>
								<td class="center">${game.get("link")}</td>
								<td class="center">${game.get("status")==0?'未发布':'已发布'}</td>
								<td class="center" data-id='${game.get("id")}' data-status='${game.get('status')}' >
									<a class="btn btn-info" href="javascript:void(0);">  <i class="icon-edit icon-white"></i> 编辑</a>
									<a class="btn btn-danger" href="javascript:void(0);"> <i class="icon-trash icon-white"></i> 删除</a>
									<a class="btn btn-default btn-sm"  href="javascript:void(0);"> <i class="icon-wrench icon-black"></i>
									 <c:if test="${game.get('status')==0}"><span>发布</span></c:if>
									  <c:if test="${game.get('status')==1}"><span>取消发布</span></c:if>
									</a>
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
		ajaxContent('/business/app/game/edit',{id:id});
	});
	$("#splitPage .box-content tbody .icon-trash").parent().click(function(){
		var id = $(this).parent().data("id");
		var url='/business/app/game/delete?id='+id;
		myConfirm("确定删除吗？",function(){
			$.ajax({
				type : 'POST',
				dataType : "json",
				url :encodeURI(encodeURI(cxt + url)),
				success : function(data) {
					if(eval(data).state=='error'){
						myAlert("删除失败！",function(){
							var url = "/business/app/game";
							ajaxContent(url);
						});
					}else{
						myAlert("删除成功！",function(){
							var url = "/business/app/game";
							ajaxContent(url);
						});
					}
				}
			});
		});
	});
	$("#splitPage .box-content tbody .icon-wrench").parent().click(function(){
		var id = $(this).parent().data("id");
		var status = $(this).parent().data("status");
		if(status==0){//未发布(发布)
			var url= "/business/app/game/changeStatus?id="+id;
			$.ajax({
				type : 'POST',
				dataType : "json",
				url :encodeURI(encodeURI(cxt + url)),
				success : function(data) {
					if(eval(data).state=='error'){
						myAlert("发布失败！",function(){
							var url = "/business/app/game";
							ajaxContent(url);
						});
					}else{
						myAlert("发布成功！",function(){
							var url = "/business/app/game";
							ajaxContent(url);
							$(this).find("span").text("取消发布");
						});
					}
				}
			});
		}else{//已发布(取消发布)
			var url= "/business/app/game/changeStatus?id="+id;
			$.ajax({
				type : 'POST',
				dataType : "json",
				url :encodeURI(encodeURI(cxt + url)),
				success : function(data) {
					if(eval(data).state=='error'){
						myAlert("取消发布失败！",function(){
							var url = "/business/app/game";
							ajaxContent(url);
						});
					}else{
						myAlert("取消发布成功！",function(){
							var url = "/business/app/game";
							ajaxContent(url);
							$(this).find("span").text("发布");
						});
					}
				}
			});
		}
	});
</script>