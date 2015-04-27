<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<style type="text/css">
	#putin_adv_edit .box-content ul,#putin_adv_edit .box-content li{list-style: none;margin: 0; padding: 0;}
	#putin_adv_edit .box-content .action{text-align: center;}
	#putin_adv_edit .box-content .action span:hover{background: #1a9aff;}
	#putin_adv_edit .box-content .action span{display: inline-block;font-size: 16px;width: 150px;height: 40px;line-height: 40px;text-align: center;margin-right: 20px;background: #54B4EB;border-radius: 3px;cursor: pointer;font-weight: bold;color: white;}
	
</style>
<form id="putin_adv_edit" class="form-horizontal" enctype="multipart/form-data"
		action="${pageContext.request.contextPath}/business/oper/adv/putin/savePutinAdv" method="POST">
	<div>
		<ul class="breadcrumb"> 
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li><a href="javascript:void(0);" onclick="ajaxContent('/business/oper/adv/putin');">广告投放</a><span class="divider"></span></li>
		</ul>
	</div>
	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2>广告修改</h2>
			</div>
			<div class="box-content">
				<input type="hidden" name="adv_spaces_id" value="${advSpacesId}">
				<ul>
					<li><!-- 选择或者上传广告物料 -->
						<%@ include file="guide/guideAdv.jsp" %>
					</li>
					<div style="clear:both;"></div>
				</ul>
				<hr>
				<div class="action">
					<span>保存</span>
					<span>取消</span>
				</div>
			</div>
		</div>
	</div>
</form>
<script type="text/javascript">
	$(function(){
		$("#putin_adv_edit .action span").eq(0).click(function(){
			var uploadFileError = 0;
			$("#putin_adv_edit .upload_list input[type='file']").each(function(){
				var filePath = $(this).val();
				var thisImg = $(this).parent().parent().parent().find("img");
				if(thisImg.attr("src")==thisImg.data("defaultImg") && filePath==""){
					uploadFileError = 1;
					if($("#putin_adv_edit .upload_list input[type='file']").length > 1){
						myAlert("请上传所有图片！");
					}else{
						myAlert("请上传图片！");
					}
					return false;
				}
			});
			if(uploadFileError == 0){
				$("#putin_adv_edit").ajaxSubmit({
					success: function(resp){
						ajaxContent('/business/oper/adv/putin');
					}
				});
			}
		});
		$("#putin_adv_edit .action span").eq(1).click(function(){
			ajaxContent('/business/oper/adv/putin');
		});
		updateAdvPutinImgUpload("${advType}","${imgs}","${contentId}","${name}","${link}","${des}");
	});
</script>