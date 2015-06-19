<!DOCTYPE html>
<html>
	<head>
		<title>${(name)!}</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0,user-scalable=no"/>
		<meta name="MobileOptimized" content="320"/>
		<script src="../../commonjs/jquery-1.8.3.min.js" type="text/javascript" ></script>
		<script src="../../commonjs/jquery.cookie.js" type="text/javascript"></script>
		<script src="../../commonjs/TouchSlide.1.1.js" type="text/javascript" ></script>
		<script src="../../commonjs/commons.js" type="text/javascript" ></script>
		<style type="text/css">
			/* css 重置 */
			body, p, input, h1, h2, h3, h4, h5, h6, ul, li, dl, dt, dd, form { margin: 0; padding: 0; list-style: none; vertical-align: middle; font-weight:normal; }
			img { border:0; margin: 0; padding: 0;   }
			body { color: #000; -webkit-user-select: none; -webkit-text-size-adjust: none; font:normal 16px/200% "微软雅黑", helvetica, arial; text-align:left;   }
			header, section, footer { display: block; margin: 0; padding: 0 }
			a{text-decoration:none;color:#000;}
			body{ background:#f4f4f4;  }
			.fl{float:left}
			.fr{float:right;}
			.cl{clear:both;}
			h3{float:left;font-size:16px;color:#252525;margin-right:4%;}
			/* 头部 */
			header{  background:#F5AB38; padding-bottom:10px; border-bottom:3px solid #CF7F21;  }
			header .logo{ padding:10px 0 5px 10px;  color:#fff;  font-style:italic; font-weight:bold; text-shadow:1px 1px 1px #965e18; font-size:12px; }
			header .logo a{ font-size:30px; line-height:30px; color:#fff;  }
			/* 尾部 */
			footer{ text-align:center; padding:10px 0; line-height:18px; font-size:14px; }
			/* 内容 */
			#content{background:#fff;  }
			.path{text-align: center;height: 35px;font-size:18px;line-height: 35px;}
			.line-jianbian{background: url(../../commonimg/line-jianbian.png);width: 100%;height:7px;}
			.tro{width: 92%;margin:0 auto;margin-top:10px;}
			.tro li{position: relative;margin-bottom:10px;width: 49%;height:130px;overflow: hidden;background: }
			.tro li:nth-child(2n-1){float:left;margin-right: 1%;}
			.tro li:nth-child(2n){float:right;}
			.pic{width:100%;height:130px; display: block;}
			.pic img{width:100%;}
			.bantouming{position: absolute;left:0;bottom:0;width:100%;height:25px;background:rgba(0,0,0,0.4);color:#fff;text-align: center;line-height: 25px;font-size:16px;z-index: 1;}
			.bofang{position: absolute;z-index: 2;left: 40%;bottom: 41%;background: url(img/bofang.png) no-repeat 50% 50%;width:40px;height:40px;}
			/* 本例子css -------------------------------------- */
		</style>
	</head>
	<body>
		<div id="content">
			<div class="path">${(name)!}</div>
			<div class="line-jianbian"></div><!--渐变介绍-->
			<ul class="tro">
			
			<#list audiolist as audio>
				<li>
					<a href="detail.html?name=${audio.name}&link=${audio.link}" class="pic"><img src="${audio.icon}"/></a>
					<div class="bantouming">${audio.name}</div>
					<a href="detail.html?name=${audio.name}&link=${audio.link}"><div class="bofang"></div></a>
				</li>
			</#list>
			
			</ul>
		</div>
	</body>
	<script type="text/javascript">
		insertClickData();//添加访问数据
	</script>
</html>
