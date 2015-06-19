<!doctype html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
        <meta name="apple-touch-fullscreen" content="yes" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <link type="text/css" rel="stylesheet" href="index/css/common.css" />
        <link type="text/css" rel="stylesheet" href="index/css/ad_page.css" />
        <script type="text/javascript" src="../commonjs/jquery-1.8.3.min.js"></script>
		<script src="../commonjs/jquery.cookie.js" type="text/javascript"></script>
		<script src="../commonjs/commons.js" type="text/javascript"></script>
		<title>${(title)!}</title>
		
		<script type="text/javascript">
	       
        	$(function(){
				var w = $(window).width() || document.documentElement.clientWidth;
				var h = $(window).height() || document.documentElement.clientHeight;
				$(".ad_pic").css({"width":w + "px","height":h + "px"});
				$(".ad_pic ul li").css({"width":w + "px","height":h + "px"});
				//显示每张广告的URL
				var url = new Array();
				var pic = new Array();
				
				<#list advs as ad>
					url[${ad_index}] = '${(ad.link)!}';
					pic[${ad_index}] = '${(ad.image)!}';
				</#list>
				
				var enter = 500;
				$(".ad_pic").hide();
				$(".ad_pic").fadeIn(enter);
				document.getElementById("time").innerHTML = 10;//倒计时时间为10秒
				$("#time").css("display","none");
				function auto(){
					function run(){
						var s = document.getElementById("time");
						if(s.innerHTML == 0){
								window.location.href = "${(gotoUrl)!}?mac="+mac+"&routersn="+routersn+"&"+randomString(10);
							return false;
						}
						s.innerHTML = s.innerHTML * 1 - 1;//s.innerHTML -= 1;
					}
					window.setInterval(run,1000);
				}
				function imgURL(){
					var sum;
					for(sum in pic){
						$(".ad_pic ul li").children("img")[sum].src = pic[sum];
					}
				}
				imgURL();
				
				//广告淡隐淡现
				var defaultOpts ={ interval:3000, fadeInTime:3000, fadeOutTime:2500};
				var len = $(".ad_pic ul > li");
				var current = 0;
				function showPic(index){
					index = current;//等号两边互换的话,index值没定义初始值
					len.eq(index).siblings(".ad_pic ul > li").stop().animate({opacity:0},defaultOpts.fadeOutTime);
					len.eq(index).stop().animate({opacity:1},defaultOpts.fadeInTime);
				}
				function autorun(){
					current++;
					$(".ad_btn").children("a").attr({"href":url[current]});
					if(current == len.length){
						//current = 0;
						$("#time").css("display","block");
						auto();//调用倒计时方法
					}
					showPic();
				}
				
				window.setInterval(autorun,defaultOpts.interval);
			});
        </script>
    </head>
    <body>
        <div class="ad_pic">
            <ul>
            	<#list advs as adv>
            		<li><img src="" alt=""></li>
            	</#list>
            </ul>
        </div>
        <div class="ad_btn"><a href="">了解更多</a></div>
        <span id="time" style="display:none;"></span>
    </body>
</html>