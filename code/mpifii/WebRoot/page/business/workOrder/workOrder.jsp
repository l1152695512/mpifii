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
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/app/workOrder/index" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="javascript:void(0);" onclick="ajaxContent('/business/app/workOrder/index');">工单预处理</a>
			</li>
		</ul>
	</div>
	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i> 工单列表</h2>
				<div class="box-icon">
					<a href="javascript:void(0);" class="btn btn-round" title="查找"><i class="icon-search"></i></a>
					<a href="javascript:down('${pageContext.request.contextPath}')" class="btn btn-round" title="导出"><i class="icon-download"></i></a>
					<a href="javascript:void(0);" class="btn btn-round" title="添加工单" onclick="ajaxContent('/business/app/workOrder/add');"><i class="icon-plus-sign"></i></a>
				</div>
			</div>
			<div class="box-content">
				<div class="search-content"><!-- 这里必须取名为search-content，否则搜索框离开焦点的点击事件不会触发搜索框的隐藏 -->
					<fieldset><!-- 这下面搜索的字段要与数据库里表的字段对应 -->
					  	<div class="control-group">
					  	
					  		<label class="control-label" for="focusedInput">工单编号：</label>
							<div class="controls">
						  		<input class="input-xlarge focused" type="text" name="queryWoid"  value="${splitPage.queryParam.queryWoid}" maxlength="20" >
							</div>
							
							<label class="control-label" for="focusedInput">商铺名称：</label>
							<div class="controls">
						  		<input class="input-xlarge focused" type="text" name="queryName" value='${splitPage.queryParam.queryName}' maxlength="50" >
							</div>
							
							<label class="control-label" for="focusedInput">工单类型：</label>
							<div class="controls">
							  	<select name="queryWoType" id="queryWoType" class="combox" 
								style="width:278px;" >
								   <option value="" >--请选择--</option>
								   <option value="1" <c:if test="${splitPage.queryParam.queryWoType=='1'}">selected="selected"</c:if>>新工单</option>
								   <option value="2" <c:if test="${splitPage.queryParam.queryWoType=='2'}">selected="selected"</c:if>>追加工单</option>
								</select>
							</div>
							
							<label class="control-label" for="focusedInput">工单状态：</label>
							<div class="controls">
							  	<select name="queryWoState" id="queryWoState" class="combox" 
								style="width:278px;" >
								   <option value="" >--请选择--</option>
								   <option value="1" <c:if test="${splitPage.queryParam.queryWoState == 1}">selected="selected"</c:if>>派单中</option>
								   <option value="2" <c:if test="${splitPage.queryParam.queryWoState == 2}">selected="selected"</c:if>>完成</option>
								</select>
							</div>
					  	</div>
					  	<div class="form-actions">
							<button type="button" class="btn btn-primary" onclick="splitPage(1);">查询</button>
							<button type="reset" class="btn" onclick="clearData()">清除</button>
					  	</div>
					</fieldset>
				</div>
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
							<th>工单编号</th>
							<!--<th>商铺编号</th>-->
							<th>商铺名称</th>
							<th>工单类型</th>
							<th>工单状态</th>
							<th>商铺位置</th>
							<th>联系方式</th>
							<th>行业</th>
						    <th>盒子式数量</th>
							<th>吸顶式数量</th>
							<th>盒子式激活数量</th>
							<th>吸顶式激活数量</th>
 					       
							<th width="320">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="shop" items="${splitPage.page.list}" >
							<tr>
								<td>${shop.get("woId")}</td>
								<!--<td>${shop.get("sn")}</td>-->
								<td>${shop.get("name")}</td>
                                <td>${shop.get("wotype")==1?"新增":"追加"}</td>
								<td  class="center">${shop.get("woState")==1?"派单中":"完成"}</td>
								<td class="center">${shop.get("workAddr")}</td>
								<td class="center">${shop.get("phone")}</td>
								<td class="center">${shop.get("trde")}</td>
								<td>${shop.get("routerNum")}</td>
								<td class="center">${shop.get("apNum")}</td>
								<td class="center">${shop.get("routerActiveNum")}</td>
								<td class="center">${shop.get("apActiveNum")}</td>
								<td class="center" data-id='${shop.get("id")}', data-user='${shop.get("woId")}'>
									<a class="btn btn-info" href="javascript:void(0);">  <i class="icon-edit icon-white"></i> 编辑</a>
									<a class="btn btn-danger" href="javascript:void(0);"> <i class="icon-trash icon-white"></i> 删除</a>
									<a class="btn btn-success" href="javascript:void(0);"> <i class="icon-zoom-in icon-white"></i>查看</a>
									<a class="btn btn-default btn-sm"  href="javascript:void(0);"> <i class="icon-wrench icon-black"></i>完成</a>
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
		var woid = $(this).parent().data("user");
		ajaxContent('/business/app/workOrder/edit',{woid:woid});
	});
	
	$("#splitPage .box-content tbody .icon-trash").parent().click(function(){
		var id = $(this).parent().data("id");
		var woId = $(this).parent().data("user");
		var url='/business/app/workOrder/delete?id='+id+"&woId="+woId;
		myConfirm("确定删除吗？",function(){
			$.ajax({
				type : 'POST',
				dataType : "json",
				url :encodeURI(encodeURI(cxt + url)),
				success : function(data) {
					ajaxContent("/business/app/workOrder");

				}
			});
		});
	});
	
	$("#splitPage .box-content tbody .icon-zoom-in").parent().click(function(){
		//var id = $(this).parent().data("id");
		var woId = $(this).parent().data("user");
		ajaxContent('/business/app/workOrder/view',{woId:woId});
	});
	
	
	$("#splitPage .box-content tbody .icon-wrench").parent().click(function(){
		
		var woId = $(this).parent().data("user");
		var url='/business/app/workOrder/comp?woId='+woId;
		myConfirm("确定完成该工单吗？",function(){
			$.ajax({
				type : 'POST',
				dataType : "json",
				url :encodeURI(encodeURI(cxt + url)),
				success : function(data) {
					ajaxContent("/business/app/workOrder");
				}
			});
		});
	});
	
	function clearData(){
		$("input[name='queryWoid']").attr("value",'');
		$("input[name='queryName']").attr("value",'');
	    $('#queryWoType option').each(function() {
	    	$(this).removeAttr("selected");
		});
	    $('#queryWoState option').each(function() {
	    	$(this).removeAttr("selected");
		});
	}
	
	function down(ctx){
		var woid = $("input[name='queryWoid']").val();
		var name = $("input[name='queryName']").val();
		var wotype = $('#queryWoType').val();
		var wostate = $('#queryWoState').val();
		window.location.href=ctx+'/business/app/workOrder/downinfo?woid='+woid+'&name='+name+'&wotype='+wotype+"&wostate="+wostate;
	}
</script>