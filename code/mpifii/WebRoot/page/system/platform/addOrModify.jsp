<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
		<li><a href="#" onclick="ajaxContent('/system/platform');">平台管理</a><span class="divider">/</span></li>
	</ul>
</div>
<div class="row-fluid">
	<div class="box span12">
		<div class="box-header well">
			<h2>
				<i class="icon-edit"></i>
				<c:choose>
					<c:when test="${empty platInfo.id}">添加平台</c:when>
					<c:otherwise>修改平台</c:otherwise>
				</c:choose>
			</h2>
		</div>
		<div class="box-content">
			<form class="form-horizontal" id="editForm" action="${cxt}/system/platform/save" method="POST">
				<input type="hidden" name="platform.id" value="${platInfo.id}">
				<div class="control-group">
					<label class="control-label">平台编号</label>
					<div class="controls">
						<input type="text" name="platform.plat_no" <c:if test="${not empty platInfo.id}"></c:if> value="${platInfo.plat_no}" class="input-xlarge"
							maxlength="16" vMin="5" vType="letterNumber" placeholder='5-16位字母数字' onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">平台名称</label>
					<div class="controls">
						<input type="text" name="platform.name" <c:if test="${not empty platInfo.id}"></c:if> value="${platInfo.name}" class="input-xlarge"
							vMin="1" vType="chinaLetterNumber" placeholder='请输入平台名称' onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">下载地址</label>
					<div class="controls">
						<input type="text" name="platform.down_url" <c:if test="${not empty platInfo.id}"></c:if> value="${platInfo.down_url}" class="input-xlarge"
							vMin="1" vType="url" onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">认证地址</label>
					<div class="controls">
						<input type="text" name="platform.auth_url" <c:if test="${not empty platInfo.id}"></c:if> value="${platInfo.auth_url}" class="input-xlarge"
							vMin="1" vType="url" onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">日志服务IP</label>
					<div class="controls">
						<input type="text" name="platform.log_ip" <c:if test="${not empty platInfo.id}"></c:if> value="${platInfo.log_ip}" class="input-xlarge"
							vMin="1" vType="ip" onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">日志服务端口</label>
					<div class="controls">
						<input type="text" name="platform.log_port" <c:if test="${not empty platInfo.id}"></c:if> value="${platInfo.log_port}" class="input-xlarge"
							vMin="1" vType="numberZ" onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">Home地址</label>
					<div class="controls">
						<input type="text" name="platform.home_url" <c:if test="${not empty platInfo.id}"></c:if> value="${platInfo.home_url}" class="input-xlarge"
							vMin="1" vType="url" onblur="onblurVali(this);">
						<span class="help-inline"></span>
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
				if("platNoRepeat"==resp.error){
					setInputError($("#editForm input[name='platform.plat_no']"),"平台编号重复！");
				}else{
					ajaxContentReturn(resp);
				}
			}
		});
	}
	
</script>