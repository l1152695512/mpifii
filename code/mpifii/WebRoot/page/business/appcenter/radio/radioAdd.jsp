<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a>
			<span class="divider">/</span></li>
		<li><a href="#" onclick="ajaxContent('/business/app/index');">应用管理</a> <span
			class="divider">/</span></li>
			<li><a href="#" onclick="ajaxContent('/business/app/radio/index');">音乐管理</a> <span
			class="divider">/</span></li>
		<li><a href="#">添加音乐</a></li>
	</ul>
</div>
<div class="row-fluid sortable">
	<div class="box span12">
		<div class="box-header well">
			<h2>
				<i class="icon-edit"></i> 添加音乐
			</h2>
			<div class="box-icon">
				<a href="#" class="btn btn-minimize btn-round"><i
					class="icon-chevron-up"></i></a> <a href="#"
					class="btn btn-close btn-round"><i class="icon-remove"></i></a>
			</div>
		</div>
		<div class="box-content">
			<form class="form-horizontal" id="radio_add_from" enctype="multipart/form-data"
				action="${cxt}/business/app/radio/save" method="POST">
				<input type="hidden" name="radio.id" value="${radio.id}">
				<div class="control-group">
					<label class="control-label">音乐名称</label>
					<div class="controls">
						<input type="text" name="radio.name" value="${radio.name}" class="input-xlarge"
							maxlength="50" vMin="1" vType="length"
							onblur="onblurVali(this);"> <span class="help-inline">1-50位</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">歌手</label>
					<div class="controls">
						<input type="text" name="radio.singer" value="${radio.singer}" class="input-xlarge"
							maxlength="50" vMin="1" vType="length"
							onblur="onblurVali(this);"> <span class="help-inline">1-50位</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">音乐图标</label>
					<div class="controls">
						 <img  src="${cxt}/${radio.icon}" id="radio_icon" style="width: 48px;height: 48px" onerror="${cxt}/images/guest.jpg">
						<input  type="file" name="radioImage" id="radioImage" accept="*.jpg,*.png,*.jpeg" value="上传图像"  />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">上传音乐</label>
					<div class="controls">
						<input  type="file" name="radioInfo" id="radioInfo"  value="上传音乐"  />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">音乐类型</label>
					<div class="controls">
						<select name="radio.type" class="combox">
							<c:forEach var="vtype" items="${typeList}">
								<option value="${vtype.id}" <c:if test="${vtype.id==radio.type}"> selected="selected"</c:if>>${vtype.name}</option>
							</c:forEach>
						</select>
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">描述</label>
					<div class="controls">
						<input type="text" name="vtype.des" value="${vtype.des}" class="input-xlarge"
							maxlength="500" vMin="1" vType="length" > <span class="help-inline">1-500位</span>
					</div>
				</div>
				<div class="form-actions">
					<button class="btn btn-primary" type="button"
						onclick="submitInfo(this.form);">提交</button>
					<button class="btn" type="button" onclick="ajaxContent('/business/app/radio');">取消</button>
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
			 $("#radio_add_from").ajaxSubmit({
			       success: function(resp){
			    	   myAlert("保存成功",function(){
			    		   ajaxContent('/business/app/radio');
			    	   });
			       },
			       error: function( err ){
			    	   myAlert("保存失败",function(){
			    		   ajaxContent('/business/app/radio');
			    	   });
			       }
			  	});
		}
		$("#radioImage").on("change", function() {
			var files = !!this.files ? this.files : [];
			if (!files.length || !window.FileReader)
				return;
			if (/^image/.test(files[0].type)) {
				var reader = new FileReader();
				reader.readAsDataURL(files[0]);
				reader.onloadend = function() {
					$("#radio_icon").attr("src", this.result);
				};
			}
		});
</script>