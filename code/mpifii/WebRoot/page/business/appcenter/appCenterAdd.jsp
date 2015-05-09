<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a>
			<span class="divider">/</span></li>
		<li><a href="#" onclick="ajaxContent('/business/app/index');">应用管理</a> <span
			class="divider">/</span></li>
		<li><a href="#">添加应用</a></li>
	</ul>
</div>
<div class="row-fluid sortable">
	<div class="box span12">
		<div class="box-header well">
			<h2>
				<i class="icon-edit"></i> 添加应用
			</h2>
<!-- 			<div class="box-icon"> -->
<!-- 				<a href="#" class="btn btn-minimize btn-round"><iclass="icon-chevron-up"></i></a> -->
<!-- 				<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> -->
<!-- 			</div> -->
		</div>
		<div class="box-content">
			<form class="form-horizontal" id="app_add_from" enctype="multipart/form-data"
				action="${cxt}/business/app/save" method="POST">
				<input type="hidden" name="app.id" value="${app.id}">
				<div class="control-group">
					<label class="control-label">应用名称</label>
					<div class="controls">
						<input type="text" name="app.name" value="${app.name}" class="input-xlarge"
							maxlength="50" vMin="1" vType="length"
							onblur="onblurVali(this);"> <span class="help-inline">1-50位</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">应用图标</label>
					<div class="controls">
						 <img  src="${cxt}/${app.icon}" id="app_icon" style="width: 48px;height: 48px" onerror="${cxt}/images/guest.jpg">
						<input  type="file" name="appImage" id="appImage" accept="*.jpg,*.png,*.jpeg" value="上传图像"  />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">应用URL</label>
					<div class="controls">
						<input type="text" name="app.edit_url"  value="${app.edit_url}" placeholder="此处若添加应用为基础应用URL必须为空" class="input-xlarge"
							maxlength="50" vMin="1" > 
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">应用link</label>
					<div class="controls">
						<input type="text" name="app.link" value="${app.link}"  class="input-xlarge"
							maxlength="50" vMin="1"> <span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">配置URL</label>
					<div class="controls">
						<input type="text" name="app.configurl" value="${app.configurl}"  placeholder="此处URL应填写应用配置的访问路径" class="input-xlarge"
							maxlength="100" vMin="1" > <span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">应用类型</label>
					<div class="controls">
						<select name="app.classify" class="combox">
							<c:forEach var="classfy" items="${classifyList}">
								<option value="${classfy.id}" <c:if test="${classfyid==app.classify}"> selected="selected"</c:if>>${classfy.name}</option>
							</c:forEach>
						</select>
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">应用状态</label>
					<div class="controls">
						<select name="app.status" class="combox">
							<option value="1">启用</option>
							<option value="0">禁用</option>
						</select>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">描述</label>
					<div class="controls">
						<input type="text" name="app.des" value="${app.des}" class="input-xlarge"
							maxlength="500" vMin="1" vType="length"> <span class="help-inline">1-500位</span>
					</div>
				</div>
				<div class="form-actions">
					<button class="btn btn-primary" type="button"
						onclick="submitInfo(this.form);">提交</button>
					<button class="btn" type="button" onclick="ajaxContent('/business/app');">取消</button>
				</div>
			</form>
		</div>
	</div>
	<!--/span-->
</div>
<!--/row-->
<script type="text/javascript">
		function submitInfo(form){
		
			var errorCount = formVali(form);
			if(errorCount != 0){
				return;
			}
			 $("#app_add_from").ajaxSubmit({
			       success: function(resp){
			    	   myAlert("保存成功",function(){
			    		   ajaxContent('/business/app');
			    	   });
			       },
			       error: function( err ){
			    	   myAlert("保存失败",function(){
			    		   ajaxContent('/business/app');
			    	   });
			       }
			  	});
		}
		$("#appImage").on("change", function() {
			var files = !!this.files ? this.files : [];
			if (!files.length || !window.FileReader)
				return;
			if (/^image/.test(files[0].type)) {
				var reader = new FileReader();
				reader.readAsDataURL(files[0]);
				reader.onloadend = function() {
					$("#app_icon").attr("src", this.result);
				};
			}
		});
</script>