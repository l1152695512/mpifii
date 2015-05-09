<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
		<li><a href="#" onclick="ajaxContent('/system/user');">用户管理</a><span class="divider">/</span></li>
<!-- 		<li><a href="#"></a></li> -->
	</ul>
</div>
<div class="row-fluid">
	<div class="box span12">
		<div class="box-header well">
			<h2>
				<i class="icon-edit"></i>
				<c:choose>
					<c:when test="${empty userInfo.id}">添加用户</c:when>
					<c:otherwise>修改用户</c:otherwise>
				</c:choose>
			</h2>
<!-- 			<div class="box-icon"> -->
<!-- 				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>  -->
<!-- 				<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> -->
<!-- 			</div> -->
		</div>
		<div class="box-content">
			<form class="form-horizontal" id="editForm" action="${cxt}/system/user/save" method="POST">
				<input type="hidden" name="user.id" value="${userInfo.id}">
				<div class="control-group">
					<label class="control-label">登录名</label>
					<div class="controls">
						<input type="text" name="user.name" <c:if test="${not empty userInfo.id}">disabled="disabled"</c:if> value="${userInfo.name}" class="input-xlarge"
							maxlength="16" vMin="5" vType="letterNumber" placeholder='5-16位字母数字'
							onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				<c:if test="${empty userInfo.id}">
					<div class="control-group">
						<label class="control-label">输入密码</label>
						<div class="controls">
							<input type="password" id="pass1Id" name="password"
								class="input-xlarge" autocomplete="off" maxlength="18" vMin='6'
								vType="letterNumber" placeholder='6-18位字母数字' onblur="onblurVali(this);">
							<span class="help-inline"></span>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">确认密码</label>
						<div class="controls">
							<input type="password" id="pass2Id" class="input-xlarge"
								autocomplete="off" maxlength="18" vMin='6' vType="letterNumber"
								placeholder='6-18位字母数字' onblur="onblurVali(this);">
							<span class="help-inline"></span>
						</div>
					</div>
				</c:if>
				<c:if test="${empty userInfo.id && not empty userType}">
					<div class="control-group">
						<label class="control-label">角色</label>
						<div class="controls">
							<input type="hidden" id="roleId" name="roleId" value="${roleId}"/>
							<input type="text" id="roleName" name="roleName value="${roleName}" 
								class="input-xlarge" readonly="readonly" maxlength="100" vMin="1" vType="length" onblur="onblurVali(this);">
							<button class="btn" type="button" onclick="roleRadioDiaLog('roleId', 'roleName','${roleId}');">选择</button>
							<span class="help-inline"></span>
						</div>
					</div>
				</c:if>
				<div class="control-group">
					<label class="control-label">状态</label>
					<div class="controls">
						<select name="user.status" class="combox">
							<option value="1" <c:if test="${userInfo.status == '1'}">selected="selected"</c:if>>启用</option>
							<option value="0" <c:if test="${userInfo.status == '0'}">selected="selected"</c:if>>禁用</option>
						</select>
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
	$("#pass1Id").focusout(function(){
		checkUserPassword($(this));
	});
	$("#pass2Id").focusout(function(){
		checkUserPassword($(this));
	});
	function checkUserPassword(thisInput){
		var result = inputDataVali(thisInput);
		if(result == true){
			var pass1Id = $("#pass1Id").val();
			var pass2Id = $("#pass2Id").val();
			if(pass1Id !="" && pass2Id != "" && pass1Id != pass2Id){
				inputDataVali($("#pass1Id"));
				inputDataVali($("#pass2Id"));
				setTimeout(setInputError,100,thisInput,"两次密码输入不一致！");
// 				setInputError(thisInput,"两次密码输入不一致！");
			}
		}
	}
	
	function submitInfo(form) {
		var errorCount = formVali(form);
		if(errorCount != 0){
			return;
		}
// 		var pass1Id = $("#pass1Id").val();
// 		var pass2Id = $("#pass2Id").val();
// 		if (pass1Id != pass2Id) {
// 			hiddenInputColor($("#pass2Id"));
// 			showInputColor($("#pass2Id"), "error");
// 			$("#pass2Id").next("help-inline").text("两次密码输入不一致！");
// // 			setInputError($("#pass2Id"),"两次密码输入不一致！")
// 			return;
// 		}
// 		ajaxForm("editForm");
		$("#editForm").ajaxSubmit({
			success : function(resp) {
				if("userNameRepeat"==resp.error){
					setInputError($("#editForm input[name='user.name']"),"用户名重复！");
				}else if("choiceRole"==resp.error){
					setInputError($("#editForm input[name='roleId']"),"必须选择角色！");
				}else{
					ajaxContentReturn(resp);
				}
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
</script>