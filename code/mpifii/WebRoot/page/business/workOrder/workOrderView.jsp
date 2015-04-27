<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">首页</a> <span class="divider">/</span></li>
		<li><a href="#" onclick="ajaxContent('/business/app/workOrder');">工单预处理 </a> <span class="divider">/</span></li>
		<li><a href="#" onclick="findDesc()">查看详情</a></li>
	</ul>
</div>

<div class="row-fluid sortable">
	<div class="box span12">
		
		<div class="box-header well" data-original-title>
			<h2>
				<i class="icon-edit"></i>查看详情
			</h2>
			<div class="box-icon">
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
				<a href="#" class="btn btn-round" onclick="printInfo()"><i class="icon-print"></i></a>
				<a href="${pageContext.request.contextPath}/business/app/workOrder/downWorkOrderInfo?woId=${workOrder.woId}" class="btn btn-round" >
				   <i class="icon-download"></i></a>  
				<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
			</div>
		</div>
		<div class="box-content">
			<form class="form-horizontal" method="POST">
				<fieldset>
					<div class="control-group">
						<label class="control-label">工单编号</label>
						<div class="controls">
							 <input type="text" id="woId" disabled="disabled" value="${workOrder.woId}"/>
						</div>
					</div>
					
					<div class="control-group">
						<label class="control-label">工单类型</label>
						<div class="controls">
							 <input type="text" disabled="disabled" value="${workOrder.wotype}"/>
						</div>
					</div>
					
					<div class="control-group">
						<label class="control-label">状态</label>
						<div class="controls">
							<input type="text" disabled="disabled" value="${workOrder.woState==1?'派单中':'完成'}"/>
						</div>
					</div>
					<%--<div class="control-group">
						<label class="control-label">商铺编号</label>
						<div class="controls">
							<input type="text" disabled="disabled" value="${workOrder.sn}">
						</div>
					</div> --%>
					<div class="control-group">
						<label class="control-label">商铺名称</label>
						<div class="controls">
							<input type="text" disabled="disabled" value="${workOrder.name}"/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">所属组织</label>
						<div class="controls">
							<input type="text" disabled="disabled" value="${workOrder.orgName}"/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">商铺地址</label>
						<div class="controls">
							<input type="text" disabled="disabled" value="${workOrder.workAddr}"/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">联系方式</label>
						<div class="controls">
							<input type="text" disabled="disabled" value="${workOrder.phone}"/>
						</div>
					</div>

				    <div class="control-group">
						<label class="control-label">吸顶智能wifi数量</label>
						<div class="controls">
							<input type="text" disabled="disabled" value="${workOrder.apNum}"/>
						</div>
					</div>
					
					<div class="control-group">
						<label class="control-label">普通智能wifi数量</label>
						<div class="controls">
							<input type="text" disabled="disabled" value="${workOrder.routerNum}"/>
						</div>
					</div>
					
					<div class="control-group">
						<label class="control-label">行业</label>
						<div class="controls">
							<input type="text" disabled="disabled" value="${workOrder.trde}"/>
						</div>
					</div>
					
					<div class="control-group">
						<label class="control-label">商铺宽带运营商类型</label>
						<div class="controls">
							<input type="text" disabled="disabled" value="${workOrder.broadbandType}"/>
						</div>
					</div>
					
					<div class="control-group">
						<label class="control-label">固定电话号码</label>
						<div class="controls">
							<input type="text" disabled="disabled" value="${workOrder.tel}"/>
						</div>
					</div>

					<%-- <div class="control-group">
							<div class="box-header well" data-original-title>
								<h2><i class="icon-edit"></i>盒子列表</h2>
								<div class="box-icon">
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-down"></i></a>
								</div>
							</div>
							<div class="box-content" style="display: none;">
								<fieldset>
									<table class="table table-striped table-bordered bootstrap-datatable ">
										<thead>
											<tr>
												<th>设备名称</th>
												<th>设备SN</th>
												<th>设备类型</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${empty deviceList}">
													<tr>
														<td colspan="2"><font color="red">暂未配置设备！</font></td>
													</tr>
												</c:when>
												<c:otherwise>
														<c:forEach var="device" items="${deviceList}">
																<tr>
																	<td>${device.name}</td>
																	<td class="center">${device.router_sn}</td>
																	<td>${device.type==1?"route":"AP"}</td>
																</tr>
															</c:forEach>
												</c:otherwise>
											</c:choose>
										</tbody>
									</table>
								</fieldset>
							</div>
					</div>--%>
				</fieldset>
			</form>

		</div>
	
	</div>
	<!--/span-->
</div>
<!--/row-->

<script type="text/javascript">
function findDesc(){
	var woId = $("#woId").val();
	ajaxContent('/business/app/workOrder/view',{woId:woId});
}

function downFile(){
	var woId = $("#woId").val();
	ajaxContent('/business/app/workOrder/downWorkOrderInfo',{woId:woId});
}

function printInfo(){
	bdhtml=window.document.body.innerHTML;//获取当前页的html代码 
	printhtml=$(".box-content").html();//获取当前页的html代码 
	
	window.document.body.innerHTML=printhtml;
	window.print();
	window.document.body.innerHTML=bdhtml;
}
</script>