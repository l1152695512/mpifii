<!DOCTYPE html>
<html>
	<head>
		<title>${(name)!}</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0,user-scalable=no"/>
		<meta name="MobileOptimized" content="320"/>
		<script src="../../commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script src="../../commonjs/jquery.cookie.js" type="text/javascript"></script>
		<script src="../../commonjs/TouchSlide.1.1.js" type="text/javascript"></script>
		<script src="../../commonjs/commons.js" type="text/javascript"></script>
		<style type="text/css">
			/* css 重置 */
			body, p, input, h1, h2, h3, h4, h5, h6, ul, li, dl, dt, dd, form { margin: 0; padding: 0; list-style: none; vertical-align: middle;}
			img { border:0; margin: 0; padding: 0;   }
			body { color: #000; -webkit-user-select: none; -webkit-text-size-adjust: none; font:normal 14px/200% "微软雅黑", helvetica, arial; text-align:left;   }
			header, section, footer { display: block; margin: 0; padding: 0 }
			a{text-decoration:none;color:#000;}
			body{ background:#f4f4f4;  }
			.fl{float:left}
			.fr{float:right;}
			.cl{clear:both;}
			h3{font-size:16px;color:#252525;height: 22px;line-height: 22px;}
			/* 头部 */
			header{  background:#F5AB38; padding-bottom:10px; border-bottom:3px solid #CF7F21;  }
			header .logo{ padding:10px 0 5px 10px;  color:#fff;  font-style:italic; font-weight:bold; text-shadow:1px 1px 1px #965e18; font-size:12px; }
			header .logo a{ font-size:30px; line-height:30px; color:#fff;  }

			/* 尾部 */
			footer{ text-align:center; padding:10px 0; line-height:18px; font-size:14px; }
			/* 内容 */
			#content{background:#fff;  }
			.path{text-align: center;height: 35px;font-size:18px;line-height: 35px;}

			/* 本例子css -------------------------------------- */
			.tabBox .hd{ height:40px; line-height:40px; font-size:20px; overflow:hidden; background:#D5E2ED; border-top:1px solid #ccc;}
			.tabBox .hd h3{ float:left; font-size:24px; }
			.tabBox .hd h3 span{color:#649AC9; font-family:Georgia; margin-left:10px;  }
			.tabBox .hd ul{ float:right;  width:100%;}
			.tabBox .hd ul li{ float:left;vertical-align:top;  width:33%;text-align: center;font-size: 16px;}
			.tabBox .hd ul li a{color: #649AC9;}
			.tabBox .hd ul li.on a{ color:#fff; display:block; height:38px; line-height:38px;border-bottom:2px solid #027EDE; background: #027EDE; }
			.tabBox .bd ul{ padding:3% 5%;}
			.tabBox .bd ul li{ border-bottom:1px dotted #ddd;clear: both;height: 70px;padding-top: 10px;}
			.pic{width:55px;height: 55px;overflow: hidden;margin-right:2%;}
			.tro{width: 78%;overflow: hidden;height: 70px;}
			.tro p{font-size:12px;line-height: 18px;color: #1f1f1f;text-overflow:ellipsis;height:30%;overflow: hidden;}
			.tro p span{font-size:12px;display: block;float:left;width: 15px;height: 15px;margin-right: 2px;overflow: hidden;background: url(img/xinghao.png) no-repeat -2px -1px;}
			.tro p span.none{background: url(img/xinghao.png) no-repeat -2px -16px;}
			.tro p span:last-child{width: 30px;height: 15px;float:right;background: none;}
			.pic img{width: 100%;}

			.tabBox .bd li a{ -webkit-tap-highlight-color:rgba(0,0,0,0); display: block;}  /* 去掉链接触摸高亮 */
			.readMore{ display:block; height:30px; line-height:30px; margin:10px auto 20px auto;  text-align:center; text-decoration:underline;}
		</style>
	</head>
	<body>
		<div id="content">
			<div class="path">${(name)!}</div>
			<!-- 本例主要代码 Start ======================= -->
			<!-- Tab切换（高度自适应示范） -->
			<div id="tabBox1" class="tabBox">
				<div class="hd">
					<h3></h3>
					<ul>
						<#list apptheme as theme>
						<li><a href="javascript:void(0)">${theme.name}</a></li>
					</#list>
					</ul>
				</div>
				<div class="bd"><!-- 添加id，js用到 -->
					<#list apptheme as theme>
					<div class="con" ><!-- 高度自适应需添加外层 -->
						<ul>
						<#list applist as app>
						<#if app.theme=theme.id>
							<li><a href="${app.link}">
								<div class="pic fl"><img src="${app.icon}"/></div>
								<div class="tro fr">
									<h3>${app.name}</h3>
									<p>
									<#if app.score=0>
										<span class="none"></span>
										<span class="none"></span>
										<span class="none"></span>
										<span class="none"></span>
										<span class="none"></span>
								   </#if>
								   <#if app.score=1>
										<span class="orange"></span>
										<span class="none"></span>
										<span class="none"></span>
										<span class="none"></span>
										<span class="none"></span>
								   </#if>
								   <#if app.score=2>
										<span class="orange"></span>
										<span class="orange"></span>
										<span class="none"></span>
										<span class="none"></span>
										<span class="none"></span>
								   </#if>
								   <#if app.score=3>
										<span class="orange"></span>
										<span class="orange"></span>
										<span class="orange"></span>
										<span class="none"></span>
										<span class="none"></span>
								   </#if>
								   <#if app.score=4>
										<span class="orange"></span>
										<span class="orange"></span>
										<span class="orange"></span>
										<span class="orange"></span>
										<span class="none"></span>
								   </#if>
								   <#if app.score=5>
										<span class="orange"></span>
										<span class="orange"></span>
										<span class="orange"></span>
										<span class="orange"></span>
										<span class="orange"></span>
								   </#if>
										<span>下载</span>
									</p>
									<p>${app.down_num}下载</p>
								</div>
								</a>
							</li><!--app1 结束-->
							</#if>
						</#list>
						</ul>
					</div>
				</#list>	
				</div>
			</div>
			
			<script type="text/javascript">
				TouchSlide({
					slideCell:"#tabBox1",
					endFun:function(i){ //高度自适应
						var bd = document.getElementById("tabBox1-bd");
						bd.parentNode.style.height = bd.children[i].children[0].offsetHeight+"px";
						if(i>0)bd.parentNode.style.transition="200ms";//添加动画效果
					}
				});
			</script>
		</div>
	</body>
	<script type="text/javascript">
		insertClickData();//添加访问数据
		$(".con ul li").click(function(){
			$(this).addClass("bdli-cur").siblings().removeClass("bdli-cur");
		});
	</script>
</html>
