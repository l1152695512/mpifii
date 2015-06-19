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
			body, p, input, h1, h2, h3, h4, h5, h6, ul, li, dl, dt, dd, form { margin: 0; padding: 0; list-style: none; vertical-align: middle; font-weight:normal; }
			img { border:0; margin: 0; padding: 0;   }
			body { color: #000; -webkit-user-select: none; -webkit-text-size-adjust: none; font:normal 16px/200% "微软雅黑", helvetica, arial; text-align:left;   }
			header, section, footer { display: block; margin: 0; padding: 0 }
			a{text-decoration:none;color:#000;}
			body{ background:#f4f4f4;  }
			.fl{float:left}
			.fr{float:right;}
			.cl{clear:both;}
			/* 内容 */
			#content{background:#fff;}
			.path .name{text-align: center;height: 50px;font-size:20px;line-height: 50px;}
			.line-jianbian{background: url(../../commonimg/line-jianbian.png);width: 100%;height:7px;}
			video{width: 100%;}
			.juji{width: 92%;height:40px;margin: 0 auto;padding:0 1%;}
			.juji div{padding-top:10px;text-indent: 50px;}
		</style>
	</head>
	<body>
		<div id="content">
			<div class="path">
				<div class="name">${(introduce.name)!}介绍</div>
			</div>
			<div class="line-jianbian"></div><!--渐变介绍-->
			<div><!--vedio 视频播放开始-->
				<video width="320" height="240" controls="controls" autoplay="autoplay">
					  <source id="videoSource" src="f/${(introduce.link)!}" type="video/mp4">
						<span style="color:red;">你的浏览器不支持播放视频，请升级浏览器</span>
				</video>
			</div><!--视频播放结束-->
			<div class="juji">
				<h3>关于${(introduce.name)!}</h3>
				<div>${(introduce.des)!}</div>
			</div>
		</div>
		<script>
			insertClickData();//添加访问数据
			$('video').media( {
				autoplay: true,
				alwaysShowControls : false,
				videoVolume : 'horizontal',
				features : [ 'playpause', 'progress', 'volume', 'fullscreen' ]
			});
		</script>
	</body>
</html>
