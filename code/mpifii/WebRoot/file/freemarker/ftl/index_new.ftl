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
		<script src="index/js/scrollText.js" type="text/javascript"></script>
		<style type="text/css">
			.ad{height:35px;background:url(${(bottomAdv.image)!}) no-repeat center center;-moz-background-size:cover;-webkit-background-size:cover;-ms-background-size:cover;background-size:cover;position:fixed;bottom:0;}		
			.footer img{width: 16px;vertical-align: middle;padding-bottom: 4px;padding-right: 2px;}
		</style>
	</head>
	<body>
		<div id="content">
			<div id="slideBox" class="slideBox">
				<div class="bd">
					<ul data-easein="rollIn">
					<#list adlist as ad>
						<li class="fadeInRight">
							<a class="pic" href="${(ad.href)!}">
								<img src="logo/${(ad.src)!}" onerror="this.src='index/img/ad-1.jpg'"/>
							</a>
						</li>
					</#list>
					</ul>
				</div>
				<div class="animate5 fadeIn hd">
					<ul></ul>
				</div>
				<script type="text/javascript">
				 	var bannerImgWidth = $(document.body).width();
		        	var bannerImgHeight = 150/290*bannerImgWidth;
		        	$("#slideBox .bd img").css({"width":bannerImgWidth,"height":bannerImgHeight});
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
				<div class="animate1 fadeInLeft sj-logo fl">
					<img src="logo/${(shop.icon)!}" onerror="this.src='index/img/morentouxiang.png'"/>
				</div>
				<div class="inner animate0">
					<span class="shopName animate2 fadeInRight" title="${(shop.name)!}">${(shop.name)!}</span>
					<div class="animate3 fadeInRight" title="${(shop.tel)!}">
						<span>电话:</span>
						<span class="content">${(shop.tel)!}</span>
					</div>
					<div class="animate4 fadeInRight" title="${(shop.addr)!}">
						<span>地址:</span>
						<span class="content">${(shop.addr)!}</span>
					</div>
				</div>
			</div>
		  	<div class="line3"></div><!--万能分割线-->
            <div class="animate0 fadeInUp app_list" data-easein="fadeInUp">
<!--             	<p class="animate1 fadeInUp fl">应用</p> -->
                <div class="cl"></div>
            </div>
			<div class="modular" id="update_data"><!--模板展示-->
				<ul data-easein="fadeInUp">
				<#list applist as app>
					<li class="animate${app_index} fadeInUp <#if app_index%4==3>four_app<#else></#if>">
					<#if app.link?index_of("?")!=-1>
						<a href="${(app.link)!}&rid=${app.id}" class="pic" ><img src="logo/${(app.icon)!}"/></a>
					<#else>
						<a href="${(app.link)!}?rid=${app.id}" class="pic" ><img src="logo/${(app.icon)!}"/></a>
					</#if>
						<p class="animate${app_index} fadeInUp">${(app.name)!}</p>
					</li>
				</#list>
				</ul>
				<div class="cl"></div>
			</div>
		</div><!--大盒子over-->
		<div class="animate1 fadeInUp footer"><a href="${(help)!}" >CopyRight &copy; 派路由&nbsp;&nbsp;&nbsp;<img src="index/img/hm_bz.png">帮助中心</a></div>
		<script type="text/javascript">
			insertClickData();//添加访问主页的数据
			
			$(document).ready(function(){
				$(".shangjia .inner").width($(".shangjia").width()-$(".shangjia .sj-logo").width()
						-getMarginPaddingBorderWidth($(".shangjia .sj-logo"))
						-getMarginPaddingBorderWidth($(".shangjia .inner")));
				//更改应用图标的宽和高
				$(".modular li img").each(function(){
					var thisWidth = $(this).width();
					$(this).css("height",thisWidth);
				});
				scrollMe($(".inner>span"));//滚动商铺名称
				$(".inner span.content").each(function(){//滚动电话和地址
					var parentWidth = $(this).parent().width();
					scrollMe($(this),parentWidth-$(this).parent().children("span").eq(0).width());
				});
				changeSpanLineHeight();
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
			function changeSpanLineHeight(){
				$(".inner span").css("line-height",$(".shangjia").height()/3+"px");
			}
		</script>
	</body>
</html>