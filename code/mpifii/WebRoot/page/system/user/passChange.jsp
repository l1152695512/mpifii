<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form id="passChangeForm" action="${cxt}/system/user/changePassword" method="POST">
	<div class="modal-body">
			<div class="form-actions">
				<fieldset>
					<div class="control-group">
						<label class="control-label">请输入您的旧密码</label>
						<div class="controls">
							<input type="password" id="oldPass" name="oldPass" class="input-xlarge" autocomplete="off" 
								maxlength="18" vMin='3' vType="letterNumber" onblur="onblurVali(this);">
							<span class="help-inline">3-18位字母数字</span>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">请输入您的新密码</label>
						<div class="controls">
							<input type="password" id="newPass1" name="newPass" class="input-xlarge" autocomplete="off" 
								maxlength="18" vMin='6'  onblur="onblurVali(this);">
							<span class="help-inline">6-18位字母数字</span>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">请重复输入您的新密码</label>
						<div class="controls">
							<input type="password" id="newPass2" name="repeatNewPass" class="input-xlarge" 
								maxlength="18" vMin='6'onblur="onblurVali(this);">
							<span class="help-inline">6-18位字母数字</span>
						</div>
					</div>
				</fieldset>
			</div>
	</div>
</form>