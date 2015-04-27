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
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/statistics/toWorkOrderSta" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="#" >运营分析－工单统计</a>
			</li>
		</ul>
	</div>
	<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-edit"></i> 工单信息查询</h2>
				<div class="box-icon">
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-down"></i></a>
				</div>
			</div>
			<div class="box-content" style="display: none;" >
				<fieldset><!-- 这下面搜索的字段要与数据库里表的字段对应 -->
					<div class="control-group">
					<label class="control-label" for="focusedInput">客户经理用户：</label>
					<div class="controls">
					   <input class="input-xlarge focused" type="text" id="userName" name="_query.userName" value='${splitPage.queryParam.userName}' maxlength="50" >
					</div>
					<label class="control-label" for="focusedInput">开始日期：</label>
				    <div class="controls">
						<input type="text" id="startDate" name="_query.startDate" value="${splitPage.queryParam.startDate}" readonly="readonly" class="input-xlarge datepicker" />
					</div>
					<label class="control-label" for="focusedInput">结束日期：</label>
					<div class="controls">
						<input type="text" id="endDate" name="_query.endDate" value="${splitPage.queryParam.endDate}"  readonly="readonly" class="input-xlarge datepicker" />
					</div>
					</div>
					<div class="form-actions">
						<button type="button" class="btn btn-primary" onclick="splitPage(1);">查询</button>
						<button type="reset" class="btn" onclick="cleardata()">清除</button>
					</div>
				</fieldset>
			</div>
		</div><!--/span-->
	</div>
	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i> 工单统计报表</h2>
			</div>
			<div class="box-content">
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
							<th>客户经理名称</th>
							<th>创建商铺数量</th>
							<th>创建工单数量</th>
							<th>吸顶数量</th>
							<th>智能路由数量</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="wo" items="${splitPage.page.list}" >
							<tr>
								<td>${wo.get("name")}</td>
								<td>${wo.get("shopSum")}</td>
                                <td>${wo.get("woSum")}</td>
                                <td>${wo.get("apSum")}</td>
                                <td>${wo.get("routerSum")}</td>
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
		var woid = $(this).parent().data("user");
		ajaxContent('/business/app/workOrder/edit',{woid:woid});
	});
	
	function cleardata(){
		$("#userName").attr('value','');
		$("#startDate").attr('value','');
		$("#endDate").attr('value','');
	}

</script>