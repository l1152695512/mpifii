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
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/oper/adv/permission" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider"></span></li>
		</ul>
	</div>

	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i>广告权限分配</h2>
				<div class="box-icon">
<!-- 					<a href="javascript:void(0);" class="btn btn-round" title="查找"><i class="icon-search"></i></a> -->
<!-- 					<a href="javascript:void(0);" class="btn btn-round" title="添加盒子" onclick="ajaxContent('/business/device/addOrModify');"><i class="icon-plus-sign"></i></a> -->
<!-- 					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>  -->
<!-- 					<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> -->
				</div>
			</div>
			<div class="box-content">
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
							<th>组织机构</th>
							<th>用户数量</th>
							<th>商铺数量</th>
							<th>已授权广告数量</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="rowData" items="${splitPage.page.list}" >
							<tr>
								<td>${rowData.get("name")}</td>
								<td class="center">${rowData.get("persons")}</td>
								<td class="center">${rowData.get("shops")}</td>
								<td class="center">${rowData.get("adv_num")}</td>
								<td class="center" data-id='${rowData.get("id")}'>
									<a class="btn btn-default btn-sm" href="javascript:void(0);"> <i class="icon-wrench icon-black"></i>分配广告</a>
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
</script>