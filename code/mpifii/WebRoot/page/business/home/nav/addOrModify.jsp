<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">
	.sn_style{
		font-size: 18px;
		font-weight: bold;
		line-height: 28px;
	}
</style>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
		<li><a href="#" onclick="ajaxContent('/business/home/nav');">mo生活管理</a><span class="divider">/</span></li>
	</ul>
</div>
<div class="row-fluid">
	<div class="box span12">
		<div class="box-header well">
			<h2>
				<i class="icon-edit"></i>
				<c:choose>
					<c:when test="${empty navInfo.id}">添加应用</c:when>
					<c:otherwise>修改应用</c:otherwise>
				</c:choose>
			</h2>
		</div>
		<div class="box-content">
			<form class="form-horizontal" id="editForm" action="${cxt}/business/home/nav/save" enctype="multipart/form-data" method="POST">
				<input type="hidden" name="nav.id" value="${navInfo.id}">
				<div class="control-group">
					<label class="control-label">类型</label>
					<div class="controls">
						<select name="nav.type" class="input-xlarge">
							<option value="1" <c:if test="${nav.type == '1'}">selected="selected"</c:if>>九宫格</option>
							<option value="2" <c:if test="${nav.type == '2'}">selected="selected"</c:if>>专题</option>
						</select>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">标题</label>
					<div class="controls">
						<input type="text" name="nav.title" value="${navInfo.title}" class="input-xlarge"
							maxlength="20" vMin="0" vType="length" placeholder='20位以内字母或数字'
							onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">图标</label>
					<div class="controls">
						<img src="${cxt}/${navInfo.logo}" id="nav_logo" style="width: 48px;height: 48px" onerror="javascript:this.src='${cxt}/images/guest.jpg';">
						<input type="file" name="navImage" id="navImage" accept="*.jpg,*.png,*.jpeg" value="上传图标"/>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">链接</label>
					<div class="controls">
						<input type="text" name="nav.url" value="${navInfo.url}" class="input-xlarge"
							vType="url" placeholder='http://m.pifii.com/ifidc/pifii.html' onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">描述</label>
					<div class="controls">
						<input type="text" name="nav.des" value="${navInfo.des}" class="input-xlarge"
							maxlength="500" vMin="0" vType="length" placeholder='500位以内' onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="form-actions">
					<button class="btn btn-primary" type="button" onclick="submitInfo(this.form);">提交</button>
					<button class="btn" type="button"  onclick="ajaxContent('/business/home/nav');">取消</button>
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
			beforeSubmit:function(){
				var filename = $("input[name='navImage']").val();
				if(filename==""){
					myAlert("请上传图标!");
	          		return false; 
				}
				var extStart=filename.lastIndexOf(".");
		        var ext=filename.substring(extStart,filename.length).toUpperCase();
		        if(ext!=".BMP"&&ext!=".PNG"&&ext!=".GIF"&&ext!=".JPG"&&ext!=".JPEG"){
		         	myAlert("图片限于bmp,png,gif,jpeg,jpg格式!");
	          		return false;  
		        }
			},
			success: function(resp){
	    	   myAlert("保存成功",function(){
	    		   ajaxContent('/business/home/nav');
	    	   });
	        },
	        error: function( err ){
	    	   myAlert("保存失败",function(){
	    	   });
	        }
		});
	}
	
	$("#navImage").on("change", function() {
		var files = !!this.files ? this.files : [];
		if (!files.length || !window.FileReader)
			return;
		if (/^image/.test(files[0].type)) {
			var reader = new FileReader();
			reader.readAsDataURL(files[0]);
			reader.onloadend = function() {
				$("#nav_logo").attr("src", this.result);
			};
		}
	});
</script>