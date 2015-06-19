<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ include file="../common/splitPage.jsp" %> 

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
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/system/syslog" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="#">系统日志</a>
			</li>
		</ul>
	</div>

	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i> 系统日志列表</h2>
				<div class="box-icon">
					<a href="javascript:void(0);" class="btn btn-round" title="查找"><i class="icon-search"></i></a>
				</div>
			</div>
			<div class="box-content">
				<div class="search-content"><!-- 这里必须取名为search-content，否则搜索框离开焦点的点击事件不会触发搜索框的隐藏 -->
					<fieldset><!-- 这下面搜索的字段要与数据库里表的字段对应 -->
					  	<div class="control-group">
							<label class="control-label" for="focusedInput">用户：</label>
							<div class="controls">
						  		<input class="input-xlarge focused" type="text" name="_query.username" value='${splitPage.queryParam.username}' maxlength="20" >
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
							<th>用户</th>
							<th>菜单</th>
							<th>操作事件</th>
							<th>日期</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="log" items="${splitPage.page.list}" >
							<tr>
								<td>${log.get("username")}</td>
								<td class="center">${log.get("resname")}</td>
								<td class="center">
								  <c:if test='${log.get("operation")==0}'>未知</c:if>
								  <c:if test='${log.get("operation")==2}'>登录系统</c:if>
								  <c:if test='${log.get("operation")==3}'>添加</c:if>
								  <c:if test='${log.get("operation")==4}'>修改</c:if>
								  <c:if test='${log.get("operation")==5}'>删除</c:if>
								  <c:if test='${log.get("operation")==6}'>授权</c:if>
								  <c:if test='${log.get("operation")==7}'>导出或下载</c:if>
								  <c:if test='${log.get("operation")==8}'>导入或上传</c:if>
								  <c:if test='${log.get("operation")==9}'>注销系统</c:if>
								</td>
								<td class="center">${log.get("createdate")}</td>
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

</script>