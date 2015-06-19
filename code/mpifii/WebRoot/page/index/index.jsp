<%@ page language="java" contentType="text/html; charset=UTF-8"
	deferredSyntaxAllowedAsLiteral="true" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>品派联盟</title>
	
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
	<meta name="author" content="Muhammad Usman">

	<!-- The styles -->
	<link id="bs-css" href="${pageContext.request.contextPath}/js/ui/charisma/css/bootstrap-${theme}.css" rel="stylesheet">
	<style type="text/css">
	  body {
		padding-bottom: 40px;
	  }
	  .sidebar-nav {
		padding: 9px 0;
	  }
	</style>
	<script type="text/javascript">
		var cxt = "${cxt}";
	</script>
	<link href="${pageContext.request.contextPath}/js/ui/charisma/css/bootstrap-responsive.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/js/ui/charisma/css/charisma-app.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/js/ui/charisma/css/jquery-ui-1.8.21.custom.css" rel="stylesheet">
	<link href='${pageContext.request.contextPath}/js/ui/charisma/css/fullcalendar.css' rel='stylesheet'>
	<link href='${pageContext.request.contextPath}/js/ui/charisma/css/fullcalendar.print.css' rel='stylesheet'  media='print'>
	<link href='${pageContext.request.contextPath}/js/ui/charisma/css/chosen.css' rel='stylesheet'>
	<link href='${pageContext.request.contextPath}/js/ui/charisma/css/uniform.default.css' rel='stylesheet'>
	<link href='${pageContext.request.contextPath}/js/ui/charisma/css/colorbox.css' rel='stylesheet'>
	<link href='${pageContext.request.contextPath}/js/ui/charisma/css/jquery.cleditor.css' rel='stylesheet'>
	<link href='${pageContext.request.contextPath}/js/ui/charisma/css/jquery.noty.css' rel='stylesheet'>
	<link href='${pageContext.request.contextPath}/js/ui/charisma/css/noty_theme_default.css' rel='stylesheet'>
	<link href='${pageContext.request.contextPath}/js/ui/charisma/css/elfinder.min.css' rel='stylesheet'>
	<link href='${pageContext.request.contextPath}/js/ui/charisma/css/elfinder.theme.css' rel='stylesheet'>
	<link href='${pageContext.request.contextPath}/js/ui/charisma/css/jquery.iphone.toggle.css' rel='stylesheet'>
	<link href='${pageContext.request.contextPath}/js/ui/charisma/css/opa-icons.css' rel='stylesheet'>
	<link href='${pageContext.request.contextPath}/js/ui/charisma/css/uploadify.css' rel='stylesheet'>
	<link href='${pageContext.request.contextPath}/js/ui/bootstrap/css/bootstrap-multiselect.css' rel='stylesheet'>
	<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
	<link rel="stylesheet" href="js/BreakingNews/BreakingNews.css" type="text/css"></link>
	<script type="text/javascript" src="js/BreakingNews/BreakingNews.js"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/js/jsFile/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<style type="text/css">
		#wapper{
   			 position: relative;   /*重要！保证footer是相对于wapper位置绝对*/
    		 height: auto;          /* 保证页面能撑开浏览器高度时显示正常*/
    		 min-height: 100%  /* IE6不支持，IE6要单独配置*/
		}
		#footer{
  			 position: absolute;  bottom: 0; /* 关键 */
   			 left:0; /* IE下一定要记得 */
   			 height: 60px;         /* footer的高度一定要是固定值*/
		}
/* 		#content{ */
/*    			padding-bottom: 60px; */
/* 		} */
		.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
		.ztree li span.button.accessControl {margin-right:2px; background-position:-126px -64px; vertical-align:top; *vertical-align:middle}
		.ztree li span.button.icon {margin-right:2px; background-position:-126px -80px; vertical-align:top; *vertical-align:middle}
	</style>
	
	<link rel="stylesheet" href="${pageContext.request.contextPath}/js/jsFile/charisma/jquery-ui-timepicker-addon.css" type="text/css">
	
</head>
<body >
	<%@ include file="../layout/top.jsp" %> 
	<div style="height: 100%" id="wapper" class="container-fluid">
		<div class="row-fluid">
			<%@ include file="../common/menu.jsp" %> 
			<noscript>
				<div class="alert alert-block span10">
					<h4 class="alert-heading">Warning!</h4>
					<p>You need to have <a href="http://en.wikipedia.org/wiki/JavaScript" target="_blank">JavaScript</a> enabled to use this site.</p>
				</div>
			</noscript>
			
			<div id="content" class="span10">
			<!-- content starts -->
				
			<!-- content ends -->
			</div><!--/#content.span10-->
		
		</div><!--/fluid-row-->
		<div id="myModal" class="modal hide fade"></div>

		<input type="hidden" id="messageButtonId" class="btn btn-primary noty" 
	  			data-noty-options='{"text":"操作成功！","layout":"bottom","type":"information","closeButton":"true"}'/>

	</div><!--/.fluid-container-->
	<!-- external javascript ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<!-- jQuery -->
	<script type="text/javascript">
		var my_theme = "${theme}";
	</script>
<%-- 	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/jquery-1.7.2.min.js"></script> --%>
	<!-- jQuery UI -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/jquery-ui-1.8.21.custom.min.js"></script>
	<!-- transition / effect library -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/bootstrap-transition.js"></script>
	<!-- alert enhancer library -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/bootstrap-alert.js"></script>
	<!-- modal / dialog library -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/bootstrap-modal.js"></script>
	<!-- custom dropdown library -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/bootstrap-dropdown.js"></script>
	<!-- scrolspy library -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/bootstrap-scrollspy.js"></script>
	<!-- library for creating tabs -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/bootstrap-tab.js"></script>
	<!-- library for advanced tooltip -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/bootstrap-tooltip.js"></script>
	<!-- popover effect library -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/bootstrap-popover.js"></script>
	<!-- button enhancer library -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/bootstrap-button.js"></script>
	<!-- accordion library (optional, not used in demo) -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/bootstrap-collapse.js"></script>
	<!-- carousel slideshow library (optional, not used in demo) -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/bootstrap-carousel.js"></script>
	<!-- autocomplete library -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/bootstrap-typeahead.js"></script>
	<!-- tour library -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/bootstrap-tour.js"></script>
	<!-- library for cookie management -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/jquery.cookie.js"></script>
	<!-- calander plugin -->
	<script src='${pageContext.request.contextPath}/js/ui/charisma/js/fullcalendar.min.js'></script>
	<!-- data table plugin -->
	<script src='${pageContext.request.contextPath}/js/ui/charisma/js/jquery.dataTables.min.js'></script>
	<!-- chart libraries start -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/excanvas.js"></script>
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/jquery.flot.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/jquery.flot.pie.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/jquery.flot.stack.js"></script>
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/jquery.flot.resize.min.js"></script>
	<!-- chart libraries end -->
	<!-- select or dropdown enhancer -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/jquery.chosen.min.js"></script>
	<!-- checkbox, radio, and file input styler -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/jquery.uniform.min.js"></script>
	<!-- plugin for gallery image view -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/jquery.colorbox.min.js"></script>
	<!-- rich text editor library -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/jquery.cleditor.min.js"></script>
	<!-- notification plugin -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/jquery.noty.js"></script>
	<!-- file manager library -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/jquery.elfinder.min.js"></script>
	<!-- star rating plugin -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/jquery.raty.min.js"></script>
	<!-- for iOS style toggle switch -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/jquery.iphone.toggle.js"></script>
	<!-- autogrowing textarea plugin -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/jquery.autogrow-textarea.js"></script>
	<!-- multiple file upload plugin -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/jquery.uploadify-3.1.min.js"></script>
 	<!-- history.js for cross-browser state change on ajax -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/jquery.history.js"></script>
	<!-- application script for Charisma demo -->
	<script src="${pageContext.request.contextPath}/js/ui/charisma/js/charisma.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsFile/jquery/jquery.form.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsFile/jquery/jquery.validate.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsFile/utils.js" type="text/javascript"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsFile/charisma/common.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsFile/zTree/js/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsFile/zTree/js/jquery.ztree.excheck-3.5.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsFile/zTree/js/jquery.ztree.exedit-3.5.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsFile/echarts/echarts-plain-map.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsFile/charisma/jquery-ui-timepicker-addon.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/bootstrap/js/bootstrap-multiselect.js"></script>
	<link rel="stylesheet" href="js/jquery-simple-modal/assets/css/simplemodal.css" type="text/css" media="screen" title="no title" charset="utf-8">
	<script src="js/jquery-simple-modal/simple-modal.js" type="text/javascript" charset="utf-8"></script>
	<script src="js/common.js" type="text/javascript"></script>
	<script type="text/javascript" src="js/jPage/js/jPages.js" ></script>
	<link rel="stylesheet" href="js/jPage/css/jPages.css" type="text/css" media="screen" title="no title" charset="utf-8">
	<script type="text/javascript" src="js/uploadImagePreview.js"></script>
	
	<link rel="stylesheet" type="text/css" href="js/fusionChartsJs/prettify.css"/>
	<script type="text/javascript" src="js/fusionChartsJs/FusionCharts.js"></script>
	<script type="text/javascript" src="js/fusionChartsJs/FusionChartsExportComponent.js"></script>
	<script type="text/javascript" src="js/fusionChartsJs/FusionCharts.js"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=XpGabbd4W3nxxzOi4WCu03yt"></script>
	<script type="text/javascript">
		$("body").click(function(){
			$(".search-content").slideUp();//隐藏搜索框
		});
		ajaxContent("/content");
	</script>
</body>
</html>


