<!doctype html>
<html lang="en">
	<head>
<<<<<<< HEAD
		<title>${(name)!}</title>
=======
		<title>开心一笑</title>
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
		<link rel="stylesheet" type="text/css" href="css/jokeGlobal.css"/>
		<link rel="stylesheet" type="text/css" href="css/joke.css"/>
		<script src="../../commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script src="../../commonjs/jquery.cookie.js" type="text/javascript"></script>
		<script src="../../commonjs/commons.js" type="text/javascript"></script>
	</head>
	<body>
<<<<<<< HEAD
		<div class="topTitle"><h1>${(name)!}</h1>
=======
		<div class="topTitle"><h1>节操段子</h1>
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
			<div><img src="img/refresh.png"/></div>
		</div>
		<div class="kg40"></div>
		<div id="wrap">
			<ul>
				<#list funnylist as funny>
					<li>
						<div class="contentTitle">
							<h3 class="fl">${(funny.title)!}</h3>
							<div class="fr time">${(funny.create_date)!}</div>
							<div class="cl"></div>
						</div>
						<div class="detail">
							${(funny.txt)!}
						</div>
						<div class="pic">
							<div><img src="${(funny.img)!}"/></div>
						</div>
					</li>
				</#list>   
				<div class="cl"></div>
			</ul>
		</div>
	</body>
	<script type="text/javascript">
		insertClickData();//添加访问数据
<<<<<<< HEAD
// 		$(document).ready(function(){
// 			$(".pic").find("img")[0].onload=function(){
// 				var imgHeight = parseInt($(".pic").find("img").height());
// 				var imgWidth = parseInt($(".pic").find("img").width());
// 				$(".pic").find("div").height(imgHeight+"px");
// 				$(".pic").find("div").width(imgWidth+"px");
// 			};
// 		});
=======
		$(document).ready(function(){
			$(".pic").find("img")[0].onload=function(){
				var imgHeight = parseInt($(".pic").find("img").height());
				var imgWidth = parseInt($(".pic").find("img").width());
				$(".pic").find("div").height(imgHeight+"px");
				$(".pic").find("div").width(imgWidth+"px");
			};
		});
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
	</script>
</html>