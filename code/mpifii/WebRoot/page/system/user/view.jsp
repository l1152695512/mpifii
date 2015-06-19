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
		</div>
		
		<div class="box-content">
			<form class="form-horizontal" method="POST">
				<fieldset>
<!-- 					<div class="control-group"> -->
<!-- 						<label class="control-label">用户头像</label> -->
<!-- 						<div class="controls"> -->
<%-- 							 <img  src="${cxt}/${user.icon}" id="shop_icon" style="width: 48px;height: 48px" onerror="${cxt}/images/guest.jpg"> --%>
<!-- 						</div> -->
<!-- 					</div> -->
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
				</fieldset>
			</form>

		</div>
	
	</div>
</div>
