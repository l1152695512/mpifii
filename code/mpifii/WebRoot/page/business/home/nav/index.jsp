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
		z-index: 1;
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

<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/home/nav" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="javascript:void(0);" onclick="ajaxContent('/business/home/nav/index');">mo生活管理</a>
			</li>
		</ul>
	</div>
	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i>应用列表</h2>
				<div class="box-icon">
					<a href="javascript:void(0);" class="btn btn-round" title="查找"><i class="icon-search"></i></a>
					<a href="javascript:void(0);" class="btn btn-round" title="添加" onclick="ajaxContent('/business/home/nav/addOrModify');"><i class="icon-plus-sign"></i></a>
				</div>
			</div>
			<div class="box-content">
				<div class="search-content"><!-- 这里必须取名为search-content，否则搜索框离开焦点的点击事件不会触发搜索框的隐藏 -->
					<fieldset><!-- 这下面搜索的字段要与数据库里表的字段对应 -->
					  	<div class="control-group">
							<label class="control-label">标题：</label>
							<div class="controls">
						  		<input class="input-xlarge" type="text" id="navTitle" name="_query.title_like" value='${splitPage.queryParam.title_like}' maxlength="40" >
							</div>
					  	</div>
					  	<div class="control-group">
							<label class="control-label">类型：</label>
							<div class="controls">
						  		<select id="navType" name="_query.type" class="input-xlarge">
						  			<option value="" <c:if test="${splitPage.queryParam.type == ''}">selected="selected"</c:if>>全部</option>
									<option value="1" <c:if test="${splitPage.queryParam.type == '1'}">selected="selected"</c:if>>九宫格</option>
									<option value="2" <c:if test="${splitPage.queryParam.type == '2'}">selected="selected"</c:if>>专题</option>
								</select>
							</div>
					  	</div>
					  	<div class="control-group">
							<label class="control-label">状态：</label>
							<div class="controls">
						  		<select id="navStatus" name="_query.status" class="input-xlarge">
						  			<option value="" <c:if test="${splitPage.queryParam.status == ''}">selected="selected"</c:if>>全部</option>
									<option value="0" <c:if test="${splitPage.queryParam.status == '0'}">selected="selected"</c:if>>下线</option>
									<option value="1" <c:if test="${splitPage.queryParam.status == '1'}">selected="selected"</c:if>>上线</option>
								</select>
							</div>
					  	</div>
					
					  	<div class="form-actions">
							<button type="button" class="btn btn-primary" onclick="splitPage(1);">查询</button>
							<button type="reset" class="btn" onclick="clearform()">清除</button>
					  	</div>
					</fieldset>
				</div>
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
							<th>序号</th>
							<th>标题</th>
							<th>类型</th>
							<th>图标</th>
							<th>链接</th>
							<th>描述</th>
							<th>创建时间</th>
							<th>状态</th>
							<th width="220">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="nav" items="${splitPage.page.list}" varStatus="status">  
							<tr>
								<td>${status.index + 1}</td>
								<td>${nav.get("title")}</td>
								<td class="center">${nav.get("type")==1?'九宫格':'专题'}</td>
								<td class="center"><img style="height:40px;" src='${cxt}/${nav.get("logo")}'  /></td>
								<td class="center">${nav.get("url")}</td>
								<td class="center">${nav.get("des")}</td>
								<td class="center">${nav.get("create_date")}</td>
								<td class="center">${nav.get("status")==0?'下线':'上线'}</td>
								<td class="center" data-status='${nav.get("status")}' data-id='${nav.get("id")}'>
									<a class="btn btn-info" href="javascript:void(0);"> <i class="icon-edit icon-white"></i> 编辑</a>
									<a class="btn btn-danger" href="javascript:void(0);"> <i class="icon-trash icon-white"></i> 删除</a>
									<a class="btn btn-default btn-sm"  href="javascript:void(0);"><i class="icon-wrench icon-black"></i>
									${nav.get("status")==0?'上线':'下线'}
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
		ajaxContent('/business/home/nav/addOrModify',{id:id});
	});
	$("#splitPage .box-content tbody .icon-trash").parent().click(function(){
		var id = $(this).parent().data("id");
		myConfirm("确定要删除该数据？",function(){
			$.ajax({
				type: "POST",
				url: "business/home/nav/delete",
				data: {id:id},
				success: function(data,status,xhr){
					ajaxContent('/business/home/nav');
				}
			});
		});
	});
	$("#splitPage .box-content tbody .icon-wrench").parent().click(function(){
		var id = $(this).parent().data("id");
		var status = $(this).parent().data("status");
		var flag="";
		if(status==0){//下线的
			flag="确定上线？";
		}else{
			flag="确定下线？";
		}
		myConfirm(flag,function(){
			$.ajax({
				type: "POST",
				url: "business/home/nav/changeStatus",
				data: {id:id},
				success: function(data,status,xhr){
					if(status){
						myAlert("操作成功!",function(){
							var url = "/business/home/nav";
							ajaxContent(url);
						});
					}else{
						myAlert("操作失败,请稍后再试!",function(){
							var url = "/business/home/nav";
							ajaxContent(url);
						});
					}
				},
				error: function(){
					myAlert("操作失败,请稍后再试!");
				}
			});
		});
	});

	function clearform(){
		$("#navTitle").val("");
		$("#navType").val("");
		$("#navStatus").val("");
	}
</script>