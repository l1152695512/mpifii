<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">首页</a> <span class="divider">/</span></li>
		<li><a href="#" onclick="ajaxContent('/system/user');">用户管理</a> <span class="divider">/</span></li>
		<li><a href="#">查看详情</a></li>
	</ul>
</div>

<div class="row-fluid sortable">
	<div class="box span12">
		
		<div class="box-header well" data-original-title>
			<h2>
				<i class="icon-edit"></i>查看详情
			</h2>
<<<<<<< HEAD
=======
			<div class="box-icon">
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
				<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
			</div>
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
		</div>
		
		<div class="box-content">
			<form class="form-horizontal" method="POST">
				<fieldset>
<<<<<<< HEAD
<!-- 					<div class="control-group"> -->
<!-- 						<label class="control-label">用户头像</label> -->
<!-- 						<div class="controls"> -->
<%-- 							 <img  src="${cxt}/${user.icon}" id="shop_icon" style="width: 48px;height: 48px" onerror="${cxt}/images/guest.jpg"> --%>
<!-- 						</div> -->
<!-- 					</div> -->
=======
					<div class="control-group">
						<label class="control-label">用户头像</label>
						<div class="controls">
							 <img  src="${cxt}/${user.icon}" id="shop_icon" style="width: 48px;height: 48px" onerror="${cxt}/images/guest.jpg">
						</div>
					</div>
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
					<div class="control-group">
						<label class="control-label">用户名</label>
						<div class="controls">
							<input type="text" disabled="disabled" value="${user.name}">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">状态</label>
						<div class="controls">
							<input type="text" disabled="disabled" value="${user.status=='0'?'禁用':'启用'}">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">Email</label>
						<div class="controls">
							<input type="text" disabled="disabled" value="${user.eamil}">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">描述</label>
						<div class="controls">
							<input type="text" disabled="disabled" value="${user.des}">
						</div>
					</div>
<<<<<<< HEAD
=======
					<div class="control-group">
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
												<th >设备名称</th>
												<th >设备mac</th>
												<th >设备SN</th>
												<th >所属商铺</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${empty deviceList}">
													<tr>
														<td colspan="4"><font color="red">暂未配置设备！</font></td>
													</tr>
												</c:when>
												<c:otherwise>
														<c:forEach var="device" items="${deviceList}">
																<tr>
																	<td>${device.name}</td>
																	<td class="center">${device.mac}</td>
																	<td class="center">${device.router_sn}</td>
																	<td class="center">${device.shopname}</td>
																</tr>
															</c:forEach>
												</c:otherwise>
											</c:choose>
										</tbody>
									</table>
								</fieldset>
							</div>
						</div>
						<div class="control-group">
							<div class="box-header well" data-original-title>
								<h2><i class="icon-edit"></i>商铺列表</h2>
								<div class="box-icon">
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-down"></i></a>
								</div>
							</div>
							<div class="box-content" style="display: none;">
								<fieldset>
									<table class="table table-striped table-bordered bootstrap-datatable ">
										<thead>
											<tr>
												<th >商铺名称</th>
												<th >商铺地址</th>
												<th >联系方式</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${empty shopList}">
													<tr>
														<td colspan="3"><font color="red">暂无商铺信息！</font></td>
													</tr>
												</c:when>
												<c:otherwise>
														<c:forEach var="shop" items="${shopList}">
																<tr>
																	<td>${shop.name}</td>
																	<td class="center">${shop.addr}</td>
																	<td class="center">${shop.tel}</td>
																</tr>
															</c:forEach>
												</c:otherwise>
											</c:choose>
										</tbody>
									</table>
								</fieldset>
							</div>
					</div>
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
				</fieldset>
			</form>

		</div>
	
	</div>
</div>
