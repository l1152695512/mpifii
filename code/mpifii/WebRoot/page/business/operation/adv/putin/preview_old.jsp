<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0,user-scalable=no">
		<meta name="MobileOptimized" content="320">
		<script src="${pageContext.request.contextPath}/js/jquery-1.8.3.min.js" type="text/javascript"></script>
	    <style>
	    	*{padding: 0;margin: 0;}
			img{border:0;}
			body{font-family:"微软雅黑","Helvetica","Arial";font-size:16px;letter-spacing: 1px;}
			.phone_img{position:absolute;width:100%;height:100%;}
			.content{position: relative;padding: 91px 0 0 33px;width: 238px;height: 414px;overflow: hidden;}
			.content img{width:100%;display: block;}
		</style>
	</head>
	<body>
		<img class="phone_img" src="${pageContext.request.contextPath}/images/business/adv/moreniphone.png" onmousedown="return false;"/>
		<div class="content">
		<c:if test="${advType=='adv_start'}">
			<img class="adv_img" src="${imgSrc}" onerror="this.src='${pageContext.request.contextPath}/images/business/adv/transition.png'" onmousedown="return false;"/>
		</c:if>
		<c:if test="${advType=='adv'}">
			<img class="adv_img" src="${imgSrc}" onerror="this.src='${pageContext.request.contextPath}/images/business/adv/adv_default.jpg'" onmousedown="return false;"/>
			<img src="${pageContext.request.contextPath}/images/business/adv/adv_preview.png" onmousedown="return false;"/>
		</c:if>
		</div>
		<script type="text/javascript">
// 			window.parent.addPreviewEvent();
			if("${advType}"=='adv' && "${imgHeight}" != '' && "${imgWeight}" != ''){
				var bannerImgHeight = parseInt("${imgHeight}")/parseInt("${imgWeight}")*$(".content").width();
				$(".adv_img").css("height",bannerImgHeight);
			}
			function updatePreviewImgSrc(imgSrc,imgWidth,imgHeight){
				$(".adv_img").attr("src",imgSrc);
				if("${advType}"=='adv'){
					var bannerImgHeight = parseInt(imgHeight)/parseInt(imgWidth)*$(".content").width();
					$(".adv_img").css("height",bannerImgHeight);
				}
			}
		</script>
	</body>
</html>