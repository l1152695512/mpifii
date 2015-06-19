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
			body, p, input, h1, h2, h3, h4, h5, h6, ul, li { margin: 0; padding: 0; list-style: none; vertical-align: middle; font-weight:normal; }
			img { border:0; margin: 0; padding: 0;   }
			body { color: #000; -webkit-user-select: none; -webkit-text-size-adjust: none; font:normal 16px/200% "微软雅黑", helvetica, arial; text-align:left;   }
			header, section, footer { display: block; margin: 0; padding: 0 }
			a{text-decoration:none;color:#000;}
			body{ background:#f4f4f4;  }
			.fl{float:left}
			.fr{float:right;}
			.cl{clear:both;}
			h3{font-size:16px;text-align: center;color:#252525;width:100%;height:20px;line-height: 20px;text-indent: 2px;margin-bottom: 1%;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;}
			/* 头部 */
			header{  background:#F5AB38; padding-bottom:10px; border-bottom:3px solid #CF7F21;  }
			header .logo{ padding:10px 0 5px 10px;  color:#fff;  font-style:italic; font-weight:bold; text-shadow:1px 1px 1px #965e18; font-size:12px; }
			header .logo a{ font-size:30px; line-height:30px; color:#fff;  }

			/* 尾部 */

			/* 内容 */
			#content{background:#fff;  }
			.path{text-align: center;height: 35px;font-size:18px;line-height: 35px;}
			.line-jianbian{background: url(../../commonimg/line-jianbian.png);width: 100%;height:7px;}


			.tro{width: 92%;margin:0 auto;margin-top:10px;}
			.tro li{position: relative;margin-bottom:7px;width: 49%;}
			.tro li:nth-child(2n-1){float:left;margin-right:1%;}
			.tro li:nth-child(2n){float:right;}
			.pic{width:100%;height:100px; display: block;margin-bottom: 1%;overflow: hidden;}
			.tro li p{font-size: 14px;line-height: 14px;color: #888888;text-align: center;}
			.pic img{width:100%;height:100%;}
			.bantouming{position: absolute;left:0;bottom:0;width:100%;height:25px;background:rgba(0,0,0,0.4);color:#fff;text-align: center;line-height: 25px;font-size:16px;z-index: 1;}
			.bofang{position: absolute;left: 40%;bottom: 50%;background: url(img/bofang.png) no-repeat 50% 50%;width:40px;height:40px;}
			/* 本例子css -------------------------------------- */
		</style>
	</head>
	<body>
		<div id="content">
			<div class="path">${(name)!}</div>
			<div class="line-jianbian"></div>
			<ul class="tro">
				<#list videolist as video>
					<li>
    					<a href="detail.html?url=${(video.link)!}&name=${(video.name)!}" class="pic">
							<img src="${(video.icon)!}"/>
							<div class="bofang"></div>
						</a>
						<h3>${(video.name)!}</h3>
						<p>${(video.type)!}</p>
					</li>
				</#list>
			</ul>
		</div>
	</body>
	<script type="text/javascript">
		insertClickData();//添加访问数据
		changALink();
		function addLinkParams(href){
			var newHref = href;
			if(href.indexOf("rid=") != -1){
				newHref = newHref+"&routersn="+routersn+"&mac="+mac;
			}
			return newHref;
		}
		function changALink(){
			$("a").each(function(){
				var href = $(this).attr("href");
				$(this).attr("href",addLinkParams(href));
			});
		}
	</script>
</html>
