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
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider"></span></li>
		</ul>
	</div>

	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i>广告投放</h2>
				<div class="box-icon">
<!-- 					<a href="javascript:void(0);" class="btn btn-round" style="width:90px;" title="添加分组" onclick="addOrModifyAdvGroup('商铺组添加');"><i class="icon-plus-sign"></i><span>添加广告</span></a> -->
				</div>
			</div>
			<div class="box-content">
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
							<th>模板</th>
							<th>广告名称</th>
							<th>广告位置</th>
							<th>广告图</th>
							<th>链接地址</th>
							<th>描述</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="rowData" items="${splitPage.page.list}" >
							<tr>
								<td>${rowData.get("template_name")}</td>
								<td>${rowData.get("adv_name")}</td>
								<td>${rowData.get("adv_index")}</td>
								<td><img style="height: 80px;" src="${rowData.get('img')}" onerror="this.src='images/business/ad-1.jpg'" /></td>
								<td>${rowData.get("link")}</td>
								<td>${rowData.get("des")}</td>
								<td data-adv-id='${rowData.get("adv_id")}' data-content-id='${rowData.get("content_id")}' 
										data-img-size='${rowData.get("img_size_tips")}' data-name='${rowData.get("adv_name")}'>
									<c:if test="${rowData.get('content_id')!=''}">
										<a class="btn btn-info" href="javascript:void(0);">  <i class="icon-edit icon-white"></i>编辑广告</a>
									</c:if>
									<a class="btn btn-default" href="javascript:void(0);">  <i class="icon-wrench icon-black"></i>投放广告</a>
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
	$("#splitPage .box-content tbody .icon-edit").parent().click(function(){
		var data = {
				advId:$(this).parent().data("advId"),
				contentId:$(this).parent().data("contentId"),
				advName:$(this).parent().data("name"),
				size:$(this).parent().data("imgSize")
		};
		settingAdv(data);
	});
	$("#splitPage .box-content tbody .icon-wrench").parent().click(function(){
		var data = {
				advId:$(this).parent().data("advId"),
				size:$(this).parent().data("imgSize")
		};
		settingAdv(data);
	});
	
	function settingAdv(data){
		if(data.size == undefined || "" == data.size){
			data.size = "290px * 150px";
		}
		$.fn.SimpleModal({
			title: '广告投放',
			param: {
				url: 'business/oper/adv/putin/edit',
				data:data
			}
		}).showModal();
	}
</script>