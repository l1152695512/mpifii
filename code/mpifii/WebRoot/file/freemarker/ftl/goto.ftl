<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0,user-scalable=no"/>
		<meta name="MobileOptimized" content="320"/>
        <link type="text/css" href="index/css/jquery.mobile-1.4.2.min.css" rel="stylesheet" />
        <link type="text/css" href="index/css/mama_style.css" rel="stylesheet" />
		<script type="text/javascript" src="../commonjs/jquery-1.8.3.min.js"></script>
		<script src="../commonjs/jquery.cookie.js" type="text/javascript"></script>
		<script src="../commonjs/commons.js" type="text/javascript"></script>
		<title>${(title)!}</title>
        <style type="text/css">
       		.result{
				display:none;
				position:absolute;
				width:100%;
				height:100%;
			}
        </style>
        <script type="text/javascript">
			$(document).ready(function(){
				//$("body").hide();
				$(".result").fadeIn(3000);
				$(".result").fadeOut(2000);
			});
        </script>
	</head>
	<body>
		<a href="javascript:void(0)"><img class="result" src="${(gotoAdv.image)!}"/></a>
    	<noscript>
			该浏览器不能执行javascript！请检查是否有禁用浏览器！
		</noscript>
		<script type="text/javascript" defer>
			$(function(){
				setInterval(goto,4000);
			});
			function goto(){
				window.location.href = "${(gotoAdv.indexUrl)!}?mac="+mac+"&routersn="+routersn+"&"+randomString(10);
			}
        </script>
	</body>
</html>