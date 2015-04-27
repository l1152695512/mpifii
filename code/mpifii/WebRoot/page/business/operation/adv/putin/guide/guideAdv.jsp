<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#ad_picture_edit{white-space: nowrap;}
	#ad_picture_edit .adv_preview{display: inline-block;height: 580px;width: 300px;position: relative;vertical-align: top;}
	#ad_picture_edit .phone_img{position:absolute;width:100%;height:100%;}
	#ad_picture_edit .content{position: relative;padding: 91px 0 0 33px;width: 238px;height: 414px;overflow: hidden;}
	#ad_picture_edit .content img{width:100%;display: block;}
	#ad_picture_edit .content .banner_img{height:100%;}
	#ad_picture_edit .adv_content{display: inline-block;height: 580px;width: 640px;overflow: auto;vertical-align: top;margin-top: 15px;}
	#ad_picture_edit .upload_list .button_test{
		position:relative;
		width: 170px;
		line-height:40px;
	 	text-align:center;
		color:#FFF;
		background:#54B4EB;
		border-radius:5px;
		font-weight:bold;
		cursor: pointer;
		overflow: hidden;
		display: inline-block;
	}
	#ad_picture_edit .button_test:hover{background:#1a9aff;}
	#ad_picture_edit .button_test input{
		width:3774px;
		height: 285px;
		position:absolute;
		opacity:0;
		filter: alpha(opacity=0);
		right:0;
		top:0;
		cursor:pointer;
		font-size: 40px;
	}
	/*上传按钮后来改的样式结束*/

	#ad_picture_edit .yilou{width: 100%;height: 95px;white-space: nowrap;}
	#ad_picture_edit .yilou .wenzi{width: 72px;height: 100%;text-align: right;font-size:14px;color: #2d343b;float:left;line-height: 38px;}
	#ad_picture_edit .yilou .pic{width: 170px;height: 88px;overflow:hidden;margin: 20px 0;}
/* 	#ad_picture_edit .yilou span{margin-top: 59px;margin-left: 10px;display: inline-block;} */
	#ad_picture_edit .yilou .pic img{width: 100%;height: 100%;}
	#ad_picture_edit .yilou .upload_list{display: inline-block;max-width: 538px;overflow-x: auto;overflow-y: hidden;}
	#ad_picture_edit .yilou .upload_list li{padding-left: 15px;display: inline-block;vertical-align: top;}
/* 	#ad_picture_edit .yilou .upload_list .shangchuan{padding-top: 38px;padding-left: 20px;} */
	#ad_picture_edit .erlou{width: 100%;height: 65px;white-space: nowrap;}
	#ad_picture_edit .erlou .wenzi{width: 72px;height: 100%;line-height:40px;font-size:14px;color: #2d343b;text-align: right;float:left;}
	#ad_picture_edit .erlou .kuang{height: 100%;align:left;margin-left:2px;text-align: left;float:left;}
	#ad_picture_edit .erlou .kuang input{width: 510px;height: 38px;border:1px solid #abadb3;line-height: 30px;font-size: 16px;margin-left: 16px;}
	#ad_picture_edit .sanlou{width: 100%;height: 155px;white-space: nowrap;}
	#ad_picture_edit .sanlou .shuoming{height: 100%;float:left;}
	#ad_picture_edit .sanlou .wenzi{width: 72px;height: 100%;line-height:95px;font-size:14px;color: #2d343b;text-align: right;float:left;}
	#ad_picture_edit .sanlou .shuoming textarea{width: 510px;font-size: 17px;text-align: left;margin-left: 16px;}
</style>	

<div id="ad_picture_edit">
	<div class="adv_preview"><!-- 预览投放广告 -->
		<img class="phone_img" src="images/business/adv/moreniphone.png" onmousedown="return false;"/>
		<div class="content"></div>
<!-- 		<iframe id="adv_putin_preview_iframe" src="" framespacing="0" name="adv_putin_preview_iframe_name" frameborder="0" scrolling="no" allowtrancparency="true"></iframe> -->
	</div>
	<div class="adv_content"><!-- 投放广告物料 -->
		<input type="hidden" name="contentId"/>
		<div class="erlou">
			<div class="wenzi fl">名称：</div>
			<div class="kuang f1"><input type="text" name="name"/></div>
			<div class="cl"></div>
		</div>
<!-- 		<div class="yilou"> -->
<!-- 			<div class="wenzi">图片：</div> -->
<%-- 			<div class="pic"><img id ="adv_img" src="${img}" onerror="this.src='images/business/ad-1.jpg'" /></div> --%>
<!-- 			<div class="shangchuan"> -->
<!-- 				<div class="button_test"><input type="file" width="640" height="330" name="upload"/>本地上传</div> -->
<!-- 			</div> -->
<%-- 			<span>建议尺寸：${size}</span> --%>
<!-- 			<div style="clear:both;"></div> -->
<!-- 		</div> -->
		<div class="erlou">
			<div class="wenzi">链接地址：</div>
			<div class="kuang"><input type="text" name="link"/></div>
			<div style="clear:both;"></div>
		</div>
		<div class="sanlou">
			<div class="wenzi">说明：</div>
			<div class="shuoming">
				<textarea rows="6" cols="" name="des"></textarea>
			</div>
			<div style="clear:both;"></div>
		</div>
		<div class="yilou">
			<div class="wenzi">图片：</div>
			<div class="upload_list">
				<ul></ul>
			</div>
			<div style="clear:both;"></div>
		</div>
	</div>
	<div style="clear:both;"></div>
</div>
<script type="text/javascript">
	var currentAdvType = "";
	function updateAdvPutinImgUpload(advType,imgsSize,contentId,advName,link,des){
		currentAdvType = advType;
		$("#ad_picture_edit input[name='contentId']").val(contentId);
		$("#ad_picture_edit input[name='name']").val(advName);
		$("#ad_picture_edit input[name='link']").val(link);
		$("#ad_picture_edit textarea[name='des']").val(des);
		
		var sizeArray = imgsSize.split(",");
		var imgsHtml = "";
		for(var i=0;i<sizeArray.length;i++){
			var imgInfo = sizeArray[i].split("*");
			var width = imgInfo[0];
			var height = imgInfo[1];
			var imgSrc = "aa.jpg";
			if(imgInfo.length > 3){
				imgSrc = imgInfo[3];
			}
			var parentHeight = parseInt(height)/parseInt(width)*170;
			imgsHtml += '<li>'+
							'<div class="shangchuan">'+
								'<div class="button_test">上传('+width+'px*'+height+'px)<input type="file" width="640" height="330" name="upload_'+width+'*'+height+'"/></div>'+
							'</div>'+
							'<div class="pic" style="height:'+parentHeight+'px;"><img src="'+imgSrc+'" data-default-img="'+imgInfo[2]+'" data-img-width="'+width+'" data-img-height="'+height+'" onerror="this.src=\''+imgInfo[2]+'\'" onmousedown="return false;"/></div>'+
							'<div class="button_test" onclick="previewAdvPutin(this);">预览</div>'+
						'</li>';
		}
		$("#ad_picture_edit .upload_list ul").html(imgsHtml);
		if(sizeArray.length > 0){
			var imgInfo =  sizeArray[0].split("*");
			var width =imgInfo[0];
			var height = imgInfo[1];
			var imgSrc = "";
			if(imgInfo.length > 3){
				imgSrc = imgInfo[3];
			}
			updateAdvPutinPreview(advType,imgSrc,width,height);
		}
		addAdvPutinPreviewEvent();
	}
	
	function updateAdvPutinPreview(advType,imgSrc,imgWidth,imgHeight){
		if(imgSrc == ''){
			imgSrc = 'aa.jpg';//防止火狐浏览器无法触发，onerror事件
		}
		if(advType == 'adv_start'){
			$("#ad_picture_edit .content").html('<img class="banner_img" src="'+imgSrc+'" onerror="this.src=\'images/business/adv/transition.png\'" onmousedown="return false;"/>');
		}else if(advType == 'adv'){
			$("#ad_picture_edit .content").html('<div class="img_div"><img class="banner_img" src="'+imgSrc+'" onerror="this.src=\'images/business/adv/adv_default.jpg\'" onmousedown="return false;"/></div>'+
					'<img src="images/business/adv/adv_preview.png" onmousedown="return false;"/>');
			var bannerImgHeight = parseInt(imgHeight)/parseInt(imgWidth)*$("#ad_picture_edit .adv_preview .content").width();
			$("#ad_picture_edit .content .img_div").css("height",bannerImgHeight);
		}
	}
	function addAdvPutinPreviewEvent(){
		$("#ad_picture_edit .upload_list li input").each(function(){
			var imgArray = [];
			imgArray.push($(this).parent().parent().parent().find("img"));
			imgArray.push($("#ad_picture_edit .adv_preview .content img").eq(0));
			$(this).uploadPreview({Img: imgArray,Callback:function(){
				if(currentAdvType == "adv"){
					var imgWidth = $(this.Img[0]).data("imgWidth");
					var imgHeight = $(this.Img[0]).data("imgHeight");
					$(this.Img[1]).parent().css("height",parseInt(imgHeight)/parseInt(imgWidth)*$("#ad_picture_edit .adv_preview .content").width());
				}
			}});
		});
	}
	
	function previewAdvPutin(thisObj){
		var imgObj = $(thisObj).parent().find("img");
		var imgSrc = imgObj.attr("src");
		var imgWidth = imgObj.data("imgWidth");
		var imgHeight = imgObj.data("imgHeight");
		
		var previewImgObj = $("#ad_picture_edit .adv_preview .content img").eq(0);
		if(currentAdvType == "adv"){
			previewImgObj.parent().css("height",parseInt(imgHeight)/parseInt(imgWidth)*$("#ad_picture_edit .adv_preview .content").width());
		}
		previewImgObj.attr("src",imgSrc);
	}
	function checkAdvPutinContent(){
		var name = $("#ad_picture_edit input[name='name']").val();
		if(name == ""){
			myAlert("广告名称必须填写！");
			return true;
		}
		var advContentId = $("#ad_picture_edit input[name='contentId']").val();
		var uploadFileError = 0;
		$("#ad_picture_edit .upload_list input[type='file']").each(function(index){
			var filePath = $(this).val();
			if(advContentId=="" && filePath==""){
				uploadFileError = 1;
				if($("#ad_picture_edit .upload_list input[type='file']").length > 1){
					myAlert("请上传所有图片！");
				}else{
					myAlert("请上传图片！");
				}
				return false;
			}
		});
		if(uploadFileError > 0){
			return true;
		}
		var link = $("#ad_picture_edit input[name='link']").val();
		if(link && link.length > 255){
			myAlert("链接地址字符太长！");
			return true;
		}
		return false;
	}
</script>