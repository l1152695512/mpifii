<!DOCTYPE html>
<html>
	<head>
		<title>${(shop.name)!}</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0,user-scalable=no"/>
		<meta name="MobileOptimized" content="320"/>
		<link rel="stylesheet" href="index/css/jquery.scrollintro.css"/>
		<link rel="stylesheet" href="index/css/animate.css"/>
        <link type="text/css" href="index/css/jquery.mobile-1.4.2.min.css" rel="stylesheet" />
        <link type="text/css" href="index/css/mama_style.css" rel="stylesheet" />
		<script src="../commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script src="../commonjs/jquery.cookie.js" type="text/javascript"></script>
		<script src="../commonjs/TouchSlide.1.1.js" type="text/javascript"></script>
		<script src="../commonjs/commons.js" type="text/javascript"></script>
		
        <script src="index/js/jquery.media.js" type="text/javascript"></script> 
		<script src="index/js/modernizr.custom.49511.js" type="text/javascript"></script>
		<script src="index/js/jquery.scrollintro.min.js" type="text/javascript"></script>
		<style type="text/css">
			.ad{height:35px;background:url(${(bottomAdv.image)!}) no-repeat center center;-moz-background-size:cover;-webkit-background-size:cover;-ms-background-size:cover;background-size:cover;position:fixed;bottom:0;}	
			.modular ul li.four_app{margin-right:0;}
		</style>
	</head>
	<body>
		<div id="content">
			<div class="line3"></div><!--渐变介绍-->
			<!-- 焦点图 -->
			<div id="slideBox" class="slideBox">
				<div class="bd">
					<ul data-easein="rollIn">
						<#list adlist as ad>
						<#if ad_index == 0>
							<li class="animate-block animate0 rollIn">
						<#else>
							<li>
						</#if>	
								<a class="pic" href="${(ad.href)!}"><img src="logo/${(ad.src)!}"  style="height:190px;min-height:190px;"/></a>
							</li>
					</#list>
					</ul>
				</div>
				<div class="animate-block animate5 fadeIn hd">
					<ul></ul>
				</div>
				<script type="text/javascript">
					try{
						TouchSlide({ 
							slideCell:"#slideBox",
							titCell:".hd ul", //开启自动分页 autoPage:true ，此时设置 titCell 为导航元素包裹层
							mainCell:".bd ul", 
							effect:"leftLoop", 
							autoPage:true,//自动分页
							autoPlay:true //自动播放
						});
					}catch(e){
					}
				</script>
			</div>
			<div class="ad-bj"></div>
			<div class="shangjia">
				<div class="inner animate-block animate0 fadeInLeft">
					<div class="animate-block animate1 fadeInLeft sj-logo fl"><img src="logo/${(shop.icon)!}"/></div>
					<h3 class="animate-block animate2 fadeInRight">${(shop.name)!}</h3>
					<p class="animate-block animate3 fadeInRight">电话：${(shop.tel)!}</p>
					<p class="animate-block animate4 fadeInRight">地址：${(shop.addr)!}</p>
					<div class="cl"></div>
				</div>
			</div>
		  	<div class="line3"></div><!--万能分割线-->
            <div class="animate-block animate0 fadeInUp app_list" data-easein="fadeInUp">
            	<p class="animate-block animate1 fadeInUp fl">应用</p>
                <div class="cl"></div>
            </div>
			<div class="modular" id="update_data"><!--模板展示-->
				<ul data-easein="fadeInUp">
				<#list applist as app>
					<li class="animate-block animate${app_index*2} fadeInUp <#if app_index%4==3>four_app<#else></#if>">
					<#if app.link?index_of("?")!=-1>
						<a href="${(app.link)!}&rid=${app.id}" class="pic" ><img src="logo/${(app.icon)!}"/></a>
					<#else>
						<a href="${(app.link)!}?rid=${app.id}" class="pic" ><img src="logo/${(app.icon)!}"/></a>
					</#if>
						<p class="animate-block animate${app_index*2+1} fadeInUp">${(app.name)!}</p>
					</li>
				</#list>
				</ul>
				<div class="cl"></div>
			</div>
		</div><!--大盒子over-->
		<footer class="animate-block animate1 fadeInUp"><a href="#" >CopyRight &copy; 派路由&nbsp;&nbsp;&nbsp;帮助中心</a></footer>
		<!--
		<div class="ad_wrapper"> 
            <a href="${(bottomAdv.link)!}"><div class="ad"></div></a>
            <div class="close"></div>
        </div>
        -->
        <script type="text/javascript">
            $(window).load(function(){
// 	            $(".close").click(function(){
// 	                $(".ad_wrapper").hide();
// 	            });
            });
            
        </script>
		<script type="text/javascript">
			insertClickData();//添加访问主页的数据
			$(document).ready(function(){
				var _scroller = $('.wrapper').scrollIntro();
				var authServerPath = serverUrl.substring(0,serverUrl.lastIndexOf("/"));
				$("footer a").attr("href",authServerPath+"/portal/mb/help");
				changALink();
				changHref();//检查跳转的页面是否存在
			});
			function changALink(){//将mac和routersn放到每个href后
				$("a").each(function(){
					var href = $(this).attr("href");
					if(href.length > 0 && href != "#"){
						if(href.indexOf("?") != -1){
							href += "&";
						}else{
							href += "?";
						}
						href += "routersn="+routersn+"&mac="+mac;
					}else{
						href = "#";
					}
					$(this).attr("href",href);
				});
			}
		</script>
	</body>
</html>