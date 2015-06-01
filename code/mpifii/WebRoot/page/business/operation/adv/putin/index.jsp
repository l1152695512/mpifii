<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ include file="../../../../common/splitPage.jsp" %> 

<style type="text/css">
<<<<<<< HEAD
	#splitPage table th,#splitPage table td{
		text-align: center;
	}
	#splitPage table td span{
		display: block;
		height: 55px;
		overflow: hidden;
=======
	#splitPage table th{
		text-align: center;
	}
	#splitPage table td{
		text-align: center;
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
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
<<<<<<< HEAD
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/oper/adv/putin" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider"></span></li>
=======
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/operation/adv/putin" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="javascript:void(0);" onclick="ajaxContent('/business/operation/adv');">广告权限</a>
			</li>
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
		</ul>
	</div>

	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
<<<<<<< HEAD
				<h2><i class="icon-user"></i>广告投放</h2>
				<div class="box-icon">
					<a href="javascript:void(0);" class="btn btn-round putin_adv" style="width:90px;"><i class="icon-plus-sign"></i><span>投放广告</span></a>
				</div>
			</div>
			<div class="box-content">
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
							<th style="width:50px;">广告名称</th>
							<th style="width:50px;">广告位</th>
							<th style="width:100px;">投放区域</th>
							<th style="width:100px;">投放行业</th>
							<th style="width:100px;">开始时间</th>
							<th style="width:100px;">结束时间</th>
							<th style="width:50px;">状态</th>
							<th style="width:100px;">投放时间</th>
							<th style="width:100px;">下架时间</th>
							<th style="width:150px;">操作</th>
=======
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
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
						</tr>
					</thead>
					<tbody>
						<c:forEach var="rowData" items="${splitPage.page.list}" >
							<tr>
<<<<<<< HEAD
								<td style="width:50px;"><span>${rowData.get("adv_name")}</span></td>
								<td style="width:50px;"><span>${rowData.get("spaces_name")}</span></td>
								<td style="width:50px;" title="${rowData.get('orgs')}"><span>${rowData.get("orgs")}</span></td>
								<td style="width:100px;" title="${rowData.get('industrys')}"><span>${rowData.get("industrys")}</span></td>
								<td style="width:100px;"><span>${rowData.get("start_date")}</span></td>
								<td style="width:100px;"><span>${rowData.get("end_date")}</span></td>
								<td style="width:50px;">
									<span>
									<c:if test="${rowData.get('enable')=='1'}">使用中</c:if>
									<c:if test="${rowData.get('enable')!='1'}">已下架</c:if>
									</span>
								</td>
								<td style="width:100px;"><span>${rowData.get("create_date")}</span></td>
								<td style="width:100px;"><span>${rowData.get("shelf_date")}</span></td>
								<td style="width:150px;" data-id='${rowData.get("id")}' >
								<c:if test="${rowData.get('enable')=='1'}">
									<a class="btn btn-warning" href="javascript:void(0);">  <i class="icon-arrow-down icon-white"></i>下架</a>
								</c:if>
								<c:if test="${rowData.get('enable')!='1' && rowData.get('expire')!='1'}">
									<a class="btn btn-success" href="javascript:void(0);">  <i class="icon-arrow-up icon-black"></i>投放</a>
								</c:if>
								<c:if test="${rowData.get('edit_adv')=='1'}">
									<a class="btn btn-info" href="javascript:void(0);">  <i class="icon-edit icon-white"></i>编辑广告</a>
<!-- 									<a class="btn btn-default" href="javascript:void(0);">  <i class="icon-time icon-black"></i>更改时间</a> -->
								</c:if>
<!-- 									<a class="btn btn-default" href="javascript:void(0);">  <i class="icon-trash icon-black"></i>删除</a> -->
=======
								<td>${rowData.get("templateName")}</td>
								<td>${rowData.get("advName")}</td>
								<td><img style="height: 80px;" src="${rowData.get("image")}"/></td>
								<td>${rowData.get("link")}</td>
								<td>${rowData.get("des")}</td>
								<td data-adv-id='${rowData.get("advId")}' data-group-role-id='${rowData.get("groupRoleId")}' data-img-size='${rowData.get("img_size_tips")}'>
									<a class="btn btn-info" href="javascript:void(0);">  <i class="icon-edit icon-white"></i>编辑广告</a>
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
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
<<<<<<< HEAD
	$("#splitPage .putin_adv").click(function(){
		ajaxContent('/business/oper/adv/putin/guide/guide');
	});
	$("#splitPage .box-content tbody .icon-arrow-down").parent().click(function(){
		var data = {
				advPutinId:$(this).parent().data("id"),
				status:"0"
		};
		myConfirm("确定要下架该广告？",function(){
			changeAdvPutinStatus(data);
		});
	});
	$("#splitPage .box-content tbody .icon-arrow-up").parent().click(function(){
		var data = {
				advPutinId:$(this).parent().data("id"),
				status:"1"
		};
		myConfirm("确定要再次投放该广告？",function(){
			changeAdvPutinStatus(data);
		});
	});
	$("#splitPage .box-content tbody .icon-edit").parent().click(function(){
		ajaxContent('/business/oper/adv/putin/editPutinAdv',{advPutinId:$(this).parent().data("id")});
	});
// 	$("#splitPage .box-content tbody .icon-time").parent().click(function(){
// 		ajaxContent('/business/oper/adv/putin/editAdvPutinDayTime',{advPutinId:$(this).parent().data("id")});
// 	});
	
	function changeAdvPutinStatus(data){
		$.ajax({
			type : "POST",
			url : 'business/oper/adv/putin/changeAdvPutinStatus',
			data : data,
			success : function(data) {
				if(data.success == "1"){
					ajaxContent('/business/oper/adv/putin');
				}else{
					myAlert(data.msg);
				}
			}
		});
	}
=======
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
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
</script>