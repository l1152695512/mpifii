<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#ad_picture_edit{
		width:620px;
	}
	#ad_picture_edit .shangchuan .button_test{
		position:relative;
		width: 170px;
		line-height:40px;
	 	text-align:center;
		color:#FFF;
		background:#1a9aff;
		border-radius:5px;
		font-weight:bold;
		cursor: pointer;
		overflow: hidden;
		display: inline-block;
	}
	#ad_picture_edit .button_test:hover{
		background:#54B4EB;
	}
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

	#ad_picture_edit .yilou{
		width: 100%;
		height: 95px;
	}
	#ad_picture_edit .yilou .wenzi{
		width: 72px;
		height: 100%;
		line-height: 75px;
		text-align: right;
		font-size:14px;
		color: #2d343b;
		float:left;
	}
	#ad_picture_edit .yilou .pic{
		width: 150px;
		height: 78px;
		margin-left: 17px;
/* 		margin:0 15px; */
		overflow:hidden;
		float:left;
	}
	#ad_picture_edit .yilou span{
		margin-top: 59px;
		margin-left: 10px;
		display: inline-block;
	}
	#ad_picture_edit .yilou .pic img{
		width: 100%;
		height: 100%;
	}
	#ad_picture_edit .yilou .shangchuan{
/* 		width: 170px; */
/* 		height: 60px; */
		padding-top: 38px;
		padding-left: 20px;
		float:left;
	}
	#ad_picture_edit .erlou{
		width: 100%;
		height: 65px;
	}
	#ad_picture_edit .erlou .wenzi{
		width: 72px;
		height: 100%;
		line-height:40px;
		font-size:14px;
		color: #2d343b;
		text-align: right;
		float:left;
	}
	#ad_picture_edit .erlou .kuang{
/* 		width: 588px; */
		height: 100%;
		align:left;
		margin-left:2px;
		text-align: left;
		float:left;
/* 		line-height: 65px; */
	}
	#ad_picture_edit .erlou .kuang input{
		width: 510px;
		height: 38px;
		border:1px solid #abadb3;
		line-height: 30px;
		font-size: 16px;
/* 		text-indent: 1em; */
		margin-left: 16px;
	}
	/*erlou 结束*/
	#ad_picture_edit .sanlou{
		width: 100%;
		height: 155px;
	}
	#ad_picture_edit .sanlou .shuoming{
/* 		width:588px; */
		height: 100%;
		float:left;
	}
	#ad_picture_edit .sanlou .wenzi{
		width: 72px;
		height: 100%;
		line-height:95px;
		font-size:14px;
		color: #2d343b;
		text-align: right;
		float:left;
	}
	#ad_picture_edit .sanlou .shuoming textarea{
		width: 510px;
		font-size: 17px;
/* 		text-indent: 1em; */
		text-align: left;
		margin-left: 16px;
	}/*三楼结束*/
	#ad_picture_edit .silou{
		width: 100%;
		height: 60px;
		text-align: center;
	}
	#ad_picture_edit .silou input{
		width: 138px;
		height: 35px;
		border-radius: 3px;
		border: 1px solid #0080e3;
		background: #2da2ff;
		cursor: pointer;
		text-align: center;
		color: #fff;
		font-weight: bold;
		font-size: 14px;
		margin-right: 15px;
	}
</style>	
<div id="ad_picture_edit">
	<form id="adv_form" action="business/operation/adv/putin/save" method="post" enctype="multipart/form-data">
			<input type="hidden" name="advId" value="${advId}">
			<input type="hidden" name="groupRoleId" value="${groupRoleId}">
			<input type="hidden" name="groupId" value="${groupId}">
			<div class="yilou">
				<div class="wenzi">图片：</div>
				<div class="pic"><img id ="adv_img" src="${image}" onerror="this.src='images/business/ad-1.jpg'" /></div>
				<div class="shangchuan">
					<div class="button_test"><input type="file" width="640" height="330"  id="uploadImage"  name="upload" />本地上传</div>
				</div>
				<span>建议尺寸：${size}</span>
				<div style="clear:both;"></div>
			</div>
			<div class="erlou">
				<div class="wenzi">链接地址：</div>
				<div class="kuang"><input type="text" name="url" value="${url}"/></div>
				<div style="clear:both;"></div>
			</div>
			<div class="sanlou">
				<div class="wenzi">说明：</div>
				<div class="shuoming">
					<textarea rows="6" cols="" name="des" >${des}</textarea>
				</div>
				<div style="clear:both;"></div>
			</div>
			<div class="silou">
				<input type="button" onclick="submitInfo()" value="确定"/>
				<input type="button" onclick="closePop();" value="取消">
			</div>
	</form>
</div>
<script type="text/javascript">
	$("#uploadImage").uploadPreview({ Img: $("#adv_img")});
	function submitInfo(){
		var localImgPath = $("#ad_picture_edit form input[name=upload]").val();
		if("${adv.id}"=="" && localImgPath==""){
			myAlert("请上传图片！");
			return;
		}
		$("#ad_picture_edit form input[name=advId]").val("${advId}");
		$("#ad_picture_edit form input[name=groupRoleId]").val("${groupRoleId}");
		$("#ad_picture_edit form input[name=groupId]").val("${groupId}");
		$("#adv_form").ajaxSubmit({
			success: function(resp){
				closePop();
				ajaxContent('/business/operation/adv/putin',{'_query.groupId':"${groupId}"});
			}
		});
	}
</script>