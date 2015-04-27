<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a>
			<span class="divider">/</span></li>
			<li><a href="javascript:void(0);" onclick="ajaxContentReturn();">组织管理</a><span class="divider">/</span></li>
		<li><a href="#" onclick="ajaxContentReturn();">用户管理</a> <span
			class="divider">/</span></li>
		<c:choose>
			<c:when test="${empty role.id}"><li><a href="#">添加角色</a></li></c:when>
			<c:otherwise><li><a href="#">修改角色</a></li></c:otherwise>
		</c:choose>
	</ul>
</div>
<div class="row-fluid">
	<div class="box span12">
		<div class="box-header well">
			<h2>
				<i class="icon-edit"></i>
				<c:choose>
					<c:when test="${empty role.id}">添加角色</c:when>
					<c:otherwise>修改角色</c:otherwise>
				</c:choose>
			</h2>
		</div>
		<div class="box-content">
			<form class="form-horizontal" id="editForm" action="${cxt}/business/org/role/save" method="POST">
				<input type="hidden" name="role.id" value="${role.id}">
				<input type="hidden" name="role.org_id" value="${role.org_id}">
				<div class="control-group">
					<label class="control-label">角色名</label>
					<div class="controls">
						<input type="text" name="role.name" value="${role.name}" class="input-xlarge"
							maxlength="16" vMin="1" vType="length" placeholder='1-16位'
							onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label">描述</label>
					<div class="controls">
						<input type="text" name="role.des" value="${role.des}" class="input-xlarge"
							maxlength="500" vMin="0" vType="length"  />
						<span class="help-inline">0-500位</span>
					</div>
				</div>

				<div class="form-actions">
					<button class="btn btn-primary" type="button" onclick="submitInfo(this.form);">提交</button>
					<button class="btn" type="button"  onclick="ajaxContentReturn();">取消</button>
				</div>
			</form>
		</div>
	</div>
	<!--/span-->
</div>
<!--/row-->
<script type="text/javascript">
	
	function submitInfo(form) {
		var errorCount = formVali(form);
		if(errorCount != 0){
			return;
		}
		$("#editForm").ajaxSubmit({
			success : function(resp) {
				ajaxContentReturn(resp);
			}
		});
	}
</script>