<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a>
			<span class="divider">/</span></li>
		<li><a href="#" onclick="ajaxContent('/business/app/index');">应用管理</a> <span
			class="divider">/</span></li>
			<li><a href="#" onclick="ajaxContent('/business/app/video/index');">视频管理</a> <span
			class="divider">/</span></li>
		<li><a href="#">添加视频</a></li>
	</ul>
</div>
<div class="row-fluid sortable">
	<div class="box span12">
		<div class="box-header well">
			<h2>
				<i class="icon-edit"></i> 添加视频
			</h2>
			<div class="box-icon">
				<a href="#" class="btn btn-minimize btn-round"><i
					class="icon-chevron-up"></i></a> <a href="#"
					class="btn btn-close btn-round"><i class="icon-remove"></i></a>
			</div>
		</div>
		<div class="box-content">
			<form class="form-horizontal" id="video_add_from" enctype="multipart/form-data"
				action="${cxt}/business/app/video/save" method="POST">
				<input type="hidden" name="video.id" value="${video.id}">
				<div class="control-group">
					<label class="control-label">视频名称</label>
					<div class="controls">
						<input type="text" name="video.name" value="${video.name}" class="input-xlarge"
							maxlength="50" vMin="1" vType="length"
							onblur="onblurVali(this);"> <span class="help-inline">1-50位</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">视频图标</label>
					<div class="controls">
						 <img  src="${cxt}/${video.icon}" id="video_icon" style="width: 48px;height: 48px" onerror="${cxt}/images/guest.jpg">
						<input  type="file" name="videoImage" id="videoImage" accept="*.jpg,*.png,*.jpeg" value="上传图像"  />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">上传视频</label>
					<div class="controls">
						<input  type="file" name="videoInfo" id="videoInfo"  value="上传视频"  />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">视频类型</label>
					<div class="controls">
							<input type="hidden" value="${video.type}" name="video.type" id="video_type">
							<c:forEach var="vtype" items="${typeList}">
								<input name="video-type" type="checkbox" value="${vtype.id}" />${vtype.name}
							</c:forEach>
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">描述</label>
					<div class="controls">
						<input type="text" name="video.des" value="${video.des}" class="input-xlarge"
							maxlength="500" vMin="1" vType="length" > <span class="help-inline">1-500位</span>
					</div>
				</div>
				<div class="form-actions">
					<button class="btn btn-primary" type="button"
						onclick="submitInfo(this.form);">提交</button>
					<button class="btn" type="button" onclick="ajaxContent('/business/app/video');">取消</button>
				</div>
			</form>
		</div>
	</div>
	<!--/span-->
</div>
<!--/row-->
<script type="text/javascript">
		$(function(){
			if($("#video_type").val()!=null){
				var types = $("#video_type").val();//1,2
				$('input:checkbox').each(function() {
					var type = $(this).val();
					if(types.indexOf(type)>=0){
						$(this).attr("checked",true);
					}
				});
			}
			$('input:checkbox').eq(0).attr("checked",'true');
		});
		function submitInfo(form){
			var errorCount = formVali(form);
			if(errorCount != 0){
				return;
			}
			if($("input[name='video-type']:checked").length==0){
				myAlert("请选择视频类型");
				return;
			}
			var types = new Array();
			$('input:checkbox').each(function() {
		        if ($(this).is(':checked') ==true) {
		                types+=$(this).val()+",";
		        }
			});
			var type = types.substr(0,types.length-1);
			$("#video_type").attr("value",type);
			 $("#video_add_from").ajaxSubmit({
			       success: function(resp){
			    	   myAlert("保存成功",function(){
			    		   ajaxContent('/business/app/video');
			    	   });
			       },
			       error: function( err ){
			    	   myAlert("保存失败",function(){
			    		   ajaxContent('/business/app/video');
			    	   });
			       }
			  	});
		}
		$("#videoImage").on("change", function() {
			var files = !!this.files ? this.files : [];
			if (!files.length || !window.FileReader)
				return;
			if (/^image/.test(files[0].type)) {
				var reader = new FileReader();
				reader.readAsDataURL(files[0]);
				reader.onloadend = function() {
					$("#video_icon").attr("src", this.result);
				};
			}
		});
</script>