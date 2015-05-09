<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a>
			<span class="divider">/</span></li>
		<li><a href="#" onclick="ajaxContent('/system/user');">用户管理</a> <span
			class="divider">/</span></li>
		<li><a href="#">添加用户</a></li>
	</ul>
</div>
<div class="row-fluid sortable">
	<div class="box span12">
		<div class="box-header well">
			<h2>
				<i class="icon-edit"></i> 添加用户
			</h2>
			<div class="box-icon">
				<a href="#" class="btn btn-minimize btn-round"><i
					class="icon-chevron-up"></i></a> <a href="#"
					class="btn btn-close btn-round"><i class="icon-remove"></i></a>
			</div>
		</div>
		<div class="box-content">
			<form class="form-horizontal" id="editForm"
				action="${cxt}/system/user/save" method="POST">
				<div class="control-group">
					<label class="control-label">登录名</label>
					<div class="controls">
						<input type="text" name="user.name" value="" class="input-xlarge"
							maxlength="16" vMin="5" vType="letterNumber"
							onblur="onblurVali(this);"> <span class="help-inline">5-16位字母数字</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">输入密码</label>
					<div class="controls">
						<input type="password" id="pass1Id" name="password"
							class="input-xlarge" autocomplete="off" maxlength="18" vMin='6'
							vType="letterNumber" onblur="onblurVali(this);"> <span
							class="help-inline">6-18位字母数字</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">确认密码</label>
					<div class="controls">
						<input type="password" id="pass2Id" class="input-xlarge"
							autocomplete="off" maxlength="18" vMin='6' vType="letterNumber"
							onblur="onblurVali(this);"> <span class="help-inline">6-18位字母数字</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">邮箱</label>
					<div class="controls">
						<input type="text" name="user.email" value="" class="input-xlarge"
							maxlength="30" vMin="6" vType="email" onblur="onblurVali(this);">
						<span class="help-inline">6-30位邮箱规则</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">头像</label>
					<div class="controls">
						 <img src="${cxt}/images/guest.jpg" id="icon" style="width: 48px;height: 48px" onerror="${cxt}/images/guest.jpg">
						<input type="file" name="userImage" id="userImage" accept="*.jpg,*.png,*.jpeg" value="上传图像"  />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">状态</label>
					<div class="controls">
						
						<select name="user.status" class="combox">
							<option value="1" <c:if test="${operator.privilege == '1'}">selected="selected"</c:if> >启用</option>
							<option value="0" <c:if test="${operator.privilege == '0'}">selected="selected"</c:if> >禁用</option>
						</select> <span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">描述</label>
					<div class="controls">
						<input type="text" name="user.des" value="" class="input-xlarge"
							maxlength="500" vMin="1" vType="length"
							onblur="onblurVali(this);"> <span class="help-inline">1-500位</span>
					</div>
				</div>
				<!-- <div class="control-group">
					<label class="control-label">所属角色</label>
						<div class="controls">
							<input type="hidden" id="stationId" name="user.stationids" value=""/>
							<input type="text" id="stationName" name="user.stationnames" value="" 
								class="input-xlarge" readonly="readonly" maxlength="100" vMin="1" vType="length" onblur="onblurVali(this);">
							<button class="btn" type="button" onclick="stationRadioDiaLog('stationId', 'stationName');">选择</button>
							<span class="help-inline"></span>
						</div>
				</div> -->
				<div class="form-actions">
					<button class="btn btn-primary" type="button"
						onclick="submitInfo(this.form);">提交</button>
					<button class="btn" onclick="ajaxContent('/system/user');">取消</button>
				</div>
			</form>
		</div>
	</div>
	<!--/span-->
</div>
<!--/row-->
<script type="text/javascript">
function submitInfo(form){
	 dataVali(form);
	 $("#editForm").ajaxSubmit({
	       success: function(resp){
	    	   myAlert("保存成功");
	    	   ajaxContent('/system/user');
	       },
	       error: function( err ){
	    	   myAlert("保存失败");
	    	   ajaxContent('/system/user');
	       }
	  	});
}
$("#userImage").on("change", function() {
	var files = !!this.files ? this.files : [];
	if (!files.length || !window.FileReader)
		return;
	if (/^image/.test(files[0].type)) {
		var reader = new FileReader();
		reader.readAsDataURL(files[0]);
		reader.onloadend = function() {
			$("#icon").attr("src", this.result);
		};
	}
});
	function dataVali(form) {
		var messageButton = $('#messageButtonId');
		var pass1Id = $("#pass1Id").val();
		var pass2Id = $("#pass2Id").val();
		if (pass1Id != pass2Id) {
			messageButton
					.attr(
							"data-noty-options",
							'{"text":"两次输入密码不一致，请从新输入！","layout":"bottom","type":"information","closeButton":"true"}');
			messageButton.click();
			return false;
		}
		var errorCount = formVali(form);
		if (errorCount != 0) {
			messageButton
					.attr(
							"data-noty-options",
							'{"text":"有'
									+ errorCount
									+ '错误，请修正！","layout":"bottom","type":"information","closeButton":"true"}');
			messageButton.click();
			return false;
		}
		ajaxForm("editForm");
	}
</script>