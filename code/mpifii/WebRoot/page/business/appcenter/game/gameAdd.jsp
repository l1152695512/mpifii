<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a>
			<span class="divider">/</span></li>
		<li><a href="#" onclick="ajaxContent('/business/app/index');">应用管理</a> <span
			class="divider">/</span></li>
			<li><a href="#" onclick="ajaxContent('/business/app/game/index');">游戏管理</a> <span
			class="divider">/</span></li>
		<li><a href="#">添加游戏</a></li>
	</ul>
</div>
<div class="row-fluid sortable">
	<div class="box span12">
		<div class="box-header well">
			<h2>
				<i class="icon-edit"></i> 添加游戏
			</h2>
			<div class="box-icon">
				<a href="#" class="btn btn-minimize btn-round"><i
					class="icon-chevron-up"></i></a> <a href="#"
					class="btn btn-close btn-round"><i class="icon-remove"></i></a>
			</div>
		</div>
		<div class="box-content">
			<form class="form-horizontal" id="game_add_from" enctype="multipart/form-data"
				action="${cxt}/business/app/game/save" method="POST">
				<input type="hidden" name="game.id" value="${game.id}">
				<div class="control-group">
					<label class="control-label">游戏名称</label>
					<div class="controls">
						<input type="text" name="game.name" value="${game.name}" class="input-xlarge"
							maxlength="50" vMin="1" vType="length"
							onblur="onblurVali(this);"> <span class="help-inline">1-50位</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">周围人玩的次数</label>
					<div class="controls">
						<input type="text" name="game.times" value="${game.times}" class="input-xlarge"
							maxlength="4" vMin="1" vType="numberZ"
							onblur="onblurVali(this);"> <span class="help-inline">1-4位正整数</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">游戏图标</label>
					<div class="controls">
						 <img  src="${cxt}/${game.icon}" id="game_icon" style="width: 48px;height: 48px" onerror="${cxt}/images/guest.jpg">
						<input  type="file" name="gameImage" id="gameImage" accept="*.jpg,*.png,*.jpeg" value="上传图像"  />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">上传游戏</label>
					<div class="controls">
						<input  type="file" name="gameInfo" id="gameInfo"  value="上传游戏"  />
					</div>
				</div>
				<div class="form-actions">
					<button class="btn btn-primary" type="button"
						onclick="submitInfo(this.form);">提交</button>
					<button class="btn" type="button" onclick="ajaxContent('/business/app/game');">取消</button>
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
			 $("#game_add_from").ajaxSubmit({
			       success: function(resp){
			    	   myAlert("保存成功",function(){
			    		   ajaxContent('/business/app/game');
			    	   });
			       },
			       error: function( err ){
			    	   myAlert("保存失败",function(){
			    		   ajaxContent('/business/app/game');
			    	   });
			       }
			  	});
		}
		$("#gameImage").on("change", function() {
			var files = !!this.files ? this.files : [];
			if (!files.length || !window.FileReader)
				return;
			if (/^image/.test(files[0].type)) {
				var reader = new FileReader();
				reader.readAsDataURL(files[0]);
				reader.onloadend = function() {
					$("#game_icon").attr("src", this.result);
				};
			}
		});
</script>