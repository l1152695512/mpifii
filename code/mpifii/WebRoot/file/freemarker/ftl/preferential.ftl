<!doctype html>
<html lang="en">
	<head>
		<title>${(name)!}</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
		<link rel="stylesheet" type="text/css" href="css/global.css"/>
		<link rel="stylesheet" type="text/css" href="css/index.css"/>
		<script src="../../commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script src="../../commonjs/jquery.cookie.js" type="text/javascript"></script>
		<script src="../../commonjs/commons.js" type="text/javascript"></script>
	</head>
	<body>
		<div id="wrap">
			<ul>
				<#list preferentiallist as pft>
					<li>
						<p class="title">${(pft.title)!}</p>
						<div class="pic"><img src="${(pft.img)!}"/></div>
						<div class="detail">
							<div>${(pft.txt)!}</div>
						</div>
					</li>
				</#list>	
			</ul>
			<div class="cl"></div>
		</div>
	</body>
	<script type="text/javascript">
		insertClickData();//添加访问数据
	</script>
</html>