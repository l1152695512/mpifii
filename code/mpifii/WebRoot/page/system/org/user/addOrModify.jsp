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
			<c:when test="${empty user.id}"><li><a href="#">添加用户</a></li></c:when>
			<c:otherwise><li><a href="#">修改用户</a></li></c:otherwise>
		</c:choose>
	</ul>
</div>
<div class="row-fluid">
	<div class="box span12">
		<div class="box-header well">
			<h2>
				<i class="icon-edit"></i>
				<c:choose>
					<c:when test="${empty user.id}">添加用户</c:when>
					<c:otherwise>修改用户</c:otherwise>
				</c:choose>
			</h2>
		</div>
		<div class="box-content">
			<form class="form-horizontal" id="editForm" action="${cxt}/business/org/user/save" method="POST">
				<input type="hidden" name="user.id" value="${user.id}">
				<input type="hidden" name="user.org_id" value="${user.org_id}">
				<div class="control-group">
					<label class="control-label">登录名</label>
					<div class="controls">
						<input type="text" name="user.name" <c:if test="${not empty user.id}">disabled="disabled"</c:if> value="${user.name}" class="input-xlarge"
							maxlength="16" vMin="5" vType="letterNumber" placeholder='5-16位字母数字'
							onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				<c:if test="${empty user.id}">
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
				
				<div class="control-group">
					<label class="control-label">所属角色</label>
					<div class="controls">
					  	<select id="select1" multiple="multiple" >
							<c:forEach var="role" items="${roleList}">
								<option value="${role.id}" <c:if test="${role.user_id != null}"> selected="selected"</c:if>>${role.name}</option>
							</c:forEach>
						</select>
						<input name="roleIds" id="roleIds"  type="hidden" />
					</div>
			  	</div>
				
				
				
				<div class="control-group">
					<label class="control-label">状态</label>
					<div class="controls">
						<select name="user.status" class="combox">
							<option value="1" <c:if test="${user.status == '1'}">selected="selected"</c:if>>启用</option>
							<option value="0" <c:if test="${user.status == '0'}">selected="selected"</c:if>>禁用</option>
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
	$('#select1').multiselect({
		enableFiltering: true,
		maxHeight: 150,
		onChange: function(){
			var checkId = $("#select1").val();
			$("#roleIds").attr("value",checkId);
		}
	});


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
		
		var checkId = $("#select1").val();
		$("#roleIds").attr("value",checkId);
		
		$("#editForm").ajaxSubmit({
			success : function(resp) {
				if("userNameRepeat"==resp.error){
					setInputError($("#editForm input[name='user.name']"),"用户名重复！");
				}else if("choiceRole"==resp.error){
					setInputError($("#editForm input[name='roleIds']"),"必须选择角色！");
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