<!DOCTYPE html>
<html manifest="">
	<head>
		<title>${(name)!}</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
		<meta name="apple-mobile-web-app-capable" content="yes"/>
		<meta content="telephone=no" name="format-detection" />
		<script src="../../commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script src="../../commonjs/jquery.cookie.js" type="text/javascript"></script>
		<script src="../../commonjs/commons.js" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" media="all" href="css/global_style.css"/>
		<link rel="stylesheet" type="text/css" media="all" href="css/current_page.css"/>
		<link rel="stylesheet" type="text/css" media="all" href="css/stc.css"/>
	</head>
	<body id="touch-screen-ctzt">
		<div class="container">
			<header id="header" class="lge page-header">
				<article class="row pd_small">
					<h1 class="tc f_24 se-f">${(name)!}<a href="javascript:void(1);" class="fl re-back">返回</a></h1>
					<a href="javascript:void(1);" id="mune" class="fr title_cd"></a>
				</article>
			</header>
			<section id="main" class="row mt_10" style="margin-top:45px;">
				<div class="main_content">
				  <ul class="cz_ll_list">
					 <#list flowpacklist as flowpack>
		                  <li>
		                      <a href="#">
		                          <div><img src="${(flowpack.pic)!}"/></div>
		                          <p>${(flowpack.title)!}</p>
		                          <span>${(flowpack.des)!}</span>
		                      </a>
		                  </li>
		               </#list> 
					</ul>
					<div class="clear"></div>
				</div>
			</section>
		</div>
	</body>
	<script type="text/javascript">
		insertClickData();//添加访问数据
	</script>
</html>