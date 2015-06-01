<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="content-type" content="text/html;charset=utf-8" />
		<meta name="keywords" content="品派联盟 流量派" />
		<meta name="description" content="品派联盟 流量派" />
		<meta name="mark" content="this is mark for login page,do not delete it" />
		<title>品派联盟</title>
		<script src="${pageContext.request.contextPath}/js/security.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/jsbase.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.ba-resize.js" type="text/javascript"></script>
		<style>
			* {padding: 0;margin: 0;}
			img {border: 0;}
			.fl {float: left;}
			.fr {float: right;}
			.cl {clear: both;}
			body {font-family: "微软雅黑";background: #f8f8f8;font-size: 14px;color: #666;}
			.dibu {width: 100%;height: 60px;}
			.head {width: 980px;height: 60px;margin: 0 auto;}
			h1 {width: 60px;height: 60px;background: url(images/logo.png) no-repeat 0 0;cursor: pointer;margin-right: 15px;}
			.head h1 a {display: none;}
			.head .nav {width: 200px;padding-top: 30px;height: 30px;}
			.head .nav ul {list-style: none;}
			.head .nav ul li {float: left;text-decoration: none;margin-right: 20px;}
			.head .nav ul li a:link, .head .nav ul li a:visited {color: #666;font-size: 12px;text-decoration: none;}
			.head .nav ul li a:hover {text-decoration: underline;}
			.head p {font-size: 26px;color: #666;padding-top: 10px;height: 50px;}
			.banner {width: 100%;height: 300px;margin-bottom: 40px;position: relative;}
			.banner img {width: 1350px;height: 300px;}
			.banner .denglukuang {width: 360px;height: 350px;position: absolute;top: 260px;right: 160px;background: url(images/denglu_bj.png) no-repeat 0 0;}
			.banner .denglukuang h4 {margin-bottom: 15px;width: 350px;height: 44px;padding-top: 13px;color: #666;font-size: 16px;text-align: center;}
			.banner .denglukuang .denglu {margin: 0 auto;width: 250px;height: 30px;position: relative;line-height: 18px;font-size: 14px;margin-bottom: 40px;}
			.banner .denglukuang .denglu input {height: 30px;width: 150px;position: absolute;left: 60px;outline: none;border: 0;background: none;line-height: 18px;font-size: 14px;}
			.banner .denglukuang .dengluanniu {width: 310px;height: 40px;}
			.banner .denglukuang .dengluanniu input {width: 310px;height: 40px;left: 0;background: #50a0ec;font-size: 18px;font-weight: bold;color: #fff;cursor: pointer;border-radius: 5px;}
			.banner .banner_juzhong {position: relative;width: 1350px;height: 300px;margin: 0 auto;}
			.neirong {width: 100%;}
			.neirong .neirong_juzhong {width: 1024px;margin: 0 auto;clear: both;}
			h2 {height: 35px;font-size: 18px;color: #616161;margin-bottom: 20px;background: url(images/xuxian.png) no-repeat 0 29px;}
			.neirong .neirong_juzhong ul {text-decoration: none;width: 670px;overflow: hidden;clear: both;}
			.neirong .neirong_juzhong ul li {list-style: none;width: 157px;height: 55px;float: left;margin-right: 5px;margin-bottom: 10px;}
			.neirong .neirong_juzhong ul {margin-top: 20px;margin-bottom: 20px;}
			.neirong .neirong_juzhong ul li h3 {color: #666;font-size: 14px;}
			.neirong .neirong_juzhong ul li p {font-size: 12px;color: #888;}
			.neirong .neirong_juzhong ul li .pic {border: 1px dashed #ccc;margin-right: 10px;width: 50px;height: 50px;overflow: hidden;}
			.neirong .neirong_juzhong ul li .pic1 {background: url(images/ad_1.png) no-repeat 0 0;}
			.neirong .neirong_juzhong ul li .pic2 {background: url(images/ad_2.png) no-repeat 0 0;}
			.neirong .neirong_juzhong ul li .pic3 {background: url(images/ad_3.png) no-repeat 0 0;}
			.neirong .neirong_juzhong ul li .pic4 {background: url(images/ad_4.png) no-repeat 0 0;}
			.neirong .neirong_juzhong ul li .pic5 {background: url(images/ad_5.png) no-repeat 0 0;}
			.neirong .neirong_juzhong ul li  .jieshao {width: 90px;}
			.neirong .neirong_juzhong ul li .pic img {width: 50px;height: 50px;}
			.suc {width: 980px;margin: 0 auto;}
			.suc p {color: #616161;font-size: 18px;font-weight: bold;width: 980px;height: 35px;margin: 0 auto;background: url(images/xuxian.png) no-repeat 0 29px;}
			.suc.gudong {width: 560px;height: 80px;background: red;}
			footer{width:100%;height: 60px;background: #0A8BD7;text-align: center;font-size: 14px;color: #fff;}
			footer .copyright{width: 100%;height: 28px;line-height: 40px;margin: 0 auto;}
			.last {margin-right: 2px;}
			#wufeng {width: 560px;height: 45px;position: relative;overflow: hidden;margin-top: 20px;margin-bottom: 20px;}
			#wufeng ul {list-style: none;width: 12000px;position: absolute;left: 0px;}
			#wufeng ul li {float: left;margin-right: 20px;}
			/*案例结束*/
			
			/*轮播开始*/
			#lunbo {width: 1350px;height: 300px;position: relative;overflow: hidden;}
			#lunbo ul {list-style: none;}
			#lunbo #tupian ul {width: 8000px;position: absolute;left: 0px;}
			#lunbo #tupian ul li {float: left;}
			#lunbo .anniu .zuo {display: block;width: 45px;height: 45px;background: url(images/png24.png) no-repeat 0 0;position: absolute;top: 40%;left: 50px;cursor: pointer;z-index: 2;}
			#lunbo .anniu .you {display: block;width: 45px;height: 45px;background: url(images/png24.png) no-repeat 0 -45px;position: absolute;top: 40%;right: 50px;cursor: pointer;z-index: 2;}
			#lunbo .xiaoyuandian {z-index: 2;position: absolute;bottom: 30px;right: 180px;height: 25px;}
			#lunbo .xiaoyuandian ul {list-style: none;}
			#lunbo .xiaoyuandian ul li {float: left;width: 13px;height: 13px;background: url(images/png24.png) no-repeat -23px -126px;margin-right: 3px;cursor: pointer;}
			#lunbo .xiaoyuandian ul li.cur {background: url(images/png24.png) no-repeat -9px -126px;}
			/*轮播结束*/
		</style>
		<script type="text/javascript">
			$(function(){
				refreshPageLayout();
				$(window).resize(function(){
					refreshPageLayout();
				});
			});
			$(document).keyup(function(e) {//监听enter事件
	        	if(e.keyCode == 13){
	        		go();
				}
	        });
			//更新footer的位置
			function refreshPageLayout(){
				$("footer").css("position","static");
				$(document).scrollTop(300);
				if($(document).scrollTop() == 0){
					$("footer").css({"position":"fixed","bottom":"0"});
				}
				$(document).scrollTop(0);
			}
			function getXMLRequest() {
				var request;
				try {
					//for火狐等浏览器  
					request = new XMLHttpRequest();
				} catch (e) {
					try {
						//for IE  
						request = new ActiveXObject("Microsoft.XMLHttp");
					} catch (e) {
						alert("您的浏览器不支持AJAX!!!");
						return null;
					}
				}
				return request;
			}
			function go() {
				var userName = $('#name').val();
		  		if(userName==undefined || userName==''){
		  			$('#msg').text('用户名不能为空！');
		  			$('#name').focus();
		  			return;
		  		}
		  		var password = $('#pwd').val();
		  		if(password==undefined || password==''){
		  			$('#msg').text('密码不能为空！');
		  			$('#pwd').focus();
		  			return;
		  		}
				if($("input[name=validateCode]").val().length == 0){
					$('#msg').text('验证码不能为空！');
					$('input[name=validateCode]').focus();
					return;
				}
				if($("input[name=validateCode]").val().length != 4){
					$('#msg').text('验证码填写有误！');
					$('input[name=validateCode]').focus();
					return;
				}
				var key = RSAUtils.getKeyPair('${exponent}', '', '${modulus}');
				var key2 = "name=" + $('#name').val() + "&pwd=" + $('#pwd').val();
		
				$('#key').val(RSAUtils.encryptedString(key, key2));
				$('#login').submit();
			}
			function changeCode() {
<<<<<<< HEAD
				$("#validateCode").attr("src", "servlet/captchaCode?"+Math.floor(Math.random()*100));
// 				var request = getXMLRequest();//得到XMLHttpRequest对象  
// 			    request.onreadystatechange = function(){  
// 			        if(request.readyState == 4){  
// 			            document.getElementById("validateCode").src = "servlet/captchaCode";//改变验证码图片  
// 			        }  
// 			    };
// 			                //将请求发送出去  
// 			    request.open("GET","servlet/captchaCode",true);  
// 			    request.send(null);  
=======
				/* $("#validateCode").attr("src", "validateCode.gif?date=" + new Date()); */
				var request = getXMLRequest();//得到XMLHttpRequest对象  
			    request.onreadystatechange = function(){  
			        if(request.readyState == 4){  
			            document.getElementById("validateCode").src = "servlet/captchaCode";//改变验证码图片  
			        }  
			    };
			                //将请求发送出去  
			    request.open("GET","servlet/captchaCode",true);  
			    request.send(null);  
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
			}
		</script>
	</head>
	<body>
		<div class="dibu">
			<div class="head">
				<!--head结束-->
				<h1 class="logo  fl">
					<a href="#">流量TT</a>
				</h1>
				<p class="fl">品派联盟</p>
				<div class="nav fr">
					<ul>
						<li><a href="page/help/help.html" target="_blank">使用帮助</a></li>
						<li><a href="page/help/help.html" target="_blank">新手上路</a></li>
					</ul>
				</div>
				<div class="cl"></div>
			</div>
			<!--head结束-->
		</div>
		<div class="banner">
			<div class="banner_juzhong">
				<!--轮播开始-->
				<div id="lunbo">
					<div id="tupian" class="tupian">
						<ul>
							<li><a href="#"><img src="images/1.png" /></a></li>
							<li><a href="#"><img src="images/2.png" /></a></li>
							<li><a href="#"><img src="images/3.png" /></a></li>
							<li><a href="#"><img src="images/4.png" /></a></li>
							<li><a href="#"><img src="images/5.png" /></a></li>
						</ul>
					</div>
					<div id="anniu" class="anniu">
						<a class="zuo"></a> <a class="you"></a>
					</div>
					<div id="xiaoyuandian" class="xiaoyuandian">
						<ul></ul>
					</div>
				</div>
				<!--轮播结束-->
				<div class="denglukuang">
					<form name="login" action="${pageContext.request.contextPath}/login"
						id="login" method="post">
	
						<h4>登录品派联盟</h4>
						<div class="denglu dengluhang">
							<input type="text" id="name" placeholder="Username" value="${name}" autofocus required />
						</div>
						<div class="denglu mima" style="margin-bottom: 30px;">
							<input id="pwd" type="password" placeholder="Password" required />
							<input id="modulus" type="hidden" value="${modulus}" />
							<input id="exponent" type="hidden" value="${exponent}" />
						</div>
						<div style="text-align: center; margin-bottom: 40px;">
<<<<<<< HEAD
							<input type="text" maxlength="4" name="validateCode" style="width: 80px; height: 23px;" />
							<a href="javascript:void(0);" onclick="changeCode()" style="color: #50A0EC; font-weight: bold;">
								<img id="validateCode" src="servlet/captchaCode?<%=new java.util.Date().getTime()%>"
										style="width: 80px; height: 23px; margin-bottom: -6px;" />
									看不清？换一张
							</a>
=======
							<input type="text" maxlength="4" name="validateCode"
								style="width: 80px; height: 23px;" /> <a
								href="javascript:void(0);" onclick="changeCode()"
								style="color: #50A0EC; font-weight: bold;"> <img
								id="validateCode"
								src="servlet/captchaCode?_sed=<%=new java.util.Date().getTime()%>"
								style="width: 80px; height: 23px; margin-bottom: -6px;" />
								看不清？换一张
							</a> </a>
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
						</div>
						<div
							style="text-align: center; float: left; margin-left: 30%; margin-top: -30px;">
							<font color='red' id="msg">${msg}</font>
						</div>
						<div class="denglu dengluanniu" style="margin-bottom: 10px;">
							<input type="button" value="登录" onclick="go()" />
						</div>
						<input type="hidden" name="key" id="key"></input>
						<div style="text-align: center;">
							没有帐号？<a href="#"
								style="color: #50A0EC; font-weight: bold; text-decoration: none;">免费注册一个</a>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="neirong">
			<div class="neirong_juzhong">
				<h2>功能特点</h2>
				<ul class="mokuai">
					<li>
						<div class="pic fl pic1"></div>
						<div class="jieshao fl">
							<h3>效果监制</h3>
							<p>用户信服的第三方数据。</p>
						</div>
					</li>
					<li>
						<div class="pic fl pic2"></div>
						<div class="jieshao fl">
							<h3>精准投放</h3>
							<p>提升每一次PV的收益</p>
						</div>
					</li>
					<li>
						<div class="pic fl pic3"></div>
						<div class="jieshao  fl">
							<h3>收益优化</h3>
							<p>剩余流量的值最大化。</p>
						</div>
					</li>
					<li>
						<div class="pic fl pic4"></div>
						<div class="jieshao  fl">
							<h3>多种角色</h3>
							<p>权责分明，安全便捷</p>
						</div>
					</li>
				</ul>
	
	
			</div>
		</div>
		<!--内容介绍结束-->
	
		<!--成功案例开始-->
		<div class="suc">
			<p>成功案例</p>
			<div class="gudong">
				<div id="wufeng">
					<ul>
						<li><a href="http://www.58.com"><img
								src="images/logo1.png" /></a></li>
						<li><a href="http://www.lashou.com"><img
								src="images/logo2.png" /></a></li>
						<li><a href="http://www.nuomi.com"><img
								src="images/logo3.png" /></a></li>
						<li><a href="http://t.dianping.com"><img
								src="images/logo4.png" /></a></li>
						<li><a href="http://www.qq.com"><img
								src="images/logo5.png" /></a></li>
						<li><a href="http://www.55tuan.com"><img
								src="images/logo6.png" /></a></li>
						<li><a href="http://www.58.com"><img
								src="images/logo1.png" /></a></li>
						<li><a href="http://www.lashou.com"><img
								src="images/logo2.png" /></a></li>
						<li><a href="http://www.nuomi.com"><img
								src="images/logo3.png" /></a></li>
						<li><a href="http://t.dianping.com"><img
								src="images/logo4.png" /></a></li>
						<li><a href="http://www.qq.com"><img
								src="images/logo5.png" /></a></li>
						<li><a href="http://www.55tuan.com"><img
								src="images/logo6.png" /></a></li>
						<li><a href="http://www.58.com"><img
								src="images/logo1.png" /></a></li>
						<li><a href="http://www.lashou.com"><img
								src="images/logo2.png" /></a></li>
						<li><a href="http://www.nuomi.com"><img
								src="images/logo3.png" /></a></li>
						<li><a href="http://t.dianping.com"><img
								src="images/logo4.png" /></a></li>
						<li><a href="http://www.qq.com"><img
								src="images/logo5.png" /></a></li>
						<li><a href="http://www.55tuan.com"><img
								src="images/logo6.png" /></a></li>
						<li><a href="http://www.58.com"><img
								src="images/logo1.png" /></a></li>
						<li><a href="http://www.lashou.com"><img
								src="images/logo2.png" /></a></li>
						<li><a href="http://www.nuomi.com"><img
								src="images/logo3.png" /></a></li>
						<li><a href="http://t.dianping.com"><img
								src="images/logo4.png" /></a></li>
						<li><a href="http://www.qq.com"><img
								src="images/logo5.png" /></a></li>
						<li><a href="http://www.55tuan.com"><img
								src="images/logo6.png" /></a></li>
						<li><a href="http://www.58.com"><img
								src="images/logo1.png" /></a></li>
						<li><a href="http://www.lashou.com"><img
								src="images/logo2.png" /></a></li>
						<li><a href="http://www.nuomi.com"><img
								src="images/logo3.png" /></a></li>
						<li><a href="http://t.dianping.com"><img
								src="images/logo4.png" /></a></li>
						<li><a href="http://www.qq.com"><img
								src="images/logo5.png" /></a></li>
						<li><a href="http://www.55tuan.com"><img
								src="images/logo6.png" /></a></li>
	
					</ul>
				</div>
				<div class="cl"></div>
			</div>
	
		</div>
		<!--成功案例开始-->
	
<!-- 		<footer> -->
<!-- 			<div class="copyright">ICP备案号：粤ICP备08122927号-7</div> -->
<!-- 			<div >广州因孚网络科技有限公司 版权所有@CopyRight 2014-2015 </div> -->
<!-- 		</footer> -->
		<script type="text/javascript">
			$(document).ready(function() {
				//步骤一：复制一倍的结点
				$("#wufeng ul").html(
						$("#wufeng ul").html() + $("#wufeng ul").html());
				var myTimer = null;
				var myTimer2 = null;
				var nowleft = 0; //信号量，标识当前ul元素的left值

				shebiao(); //调用设表函数
				function shebiao() {
					clearInterval(myTimer); //设表先关
					myTimer = setInterval(function() {
						if (nowleft == -3000) {
							//判终停表
							nowleft = 0;
						} else {
							nowleft = nowleft - 10; //信号量带着效果飞
							//这里一定要注意步标整除原则
						}
						$("#wufeng ul").css({
							"left" : nowleft
						});
						if ((-1) * nowleft % 195 == 0) {
							clearInterval(myTimer); //让滚动停止
							myTimer2 = setTimeout(shebiao, 1000);
						}
					}, 30);

				}

				$("#wufeng ul li img").mouseenter(function() {
					clearInterval(myTimer); //停表，鼠标进入停止滚动
					clearTimeout(myTimer2);
				});
				$("#wufeng ul li img").mouseout(function() {
					shebiao(); //鼠标离开，继续滚动
				});
			});
		</script>
		<script type="text/javascript">
			$(document).ready(function() {
				//信号量，指示当前图片编号
				var nowshowpic = 0;

				//得到所有图片的数量
				var imgamount = $("#tupian ul li").length;
				//控制小圆点，和图片数量一样多
				for (var i = 0; i < imgamount; i++) {
					$("#xiaoyuandian ul").append("<li></li>");
				}
				//让第一个小圆点变蓝
				shezhixiaoyuandian(0);

				//每2秒种，模拟点击一次右按钮
				var mytimer = null;
				zidong();
				function zidong() {
					window.clearInterval(mytimer);
					mytimer = window.setInterval(function() {
						$("#anniu .you").trigger("click");
					}, 2000);
				}

				//鼠标进入，图片停止轮播
				$("#lunbo,#anniu").mouseenter(function() {
					window.clearInterval(mytimer);
				});

				//鼠标离开，图片继续轮播
				$("#lunbo,#anniu").mouseleave(function() {
					zidong();
				});

				//给右边按钮添加监听
				$("#anniu .you").click(function() {
					if (!$("#tupian ul").is(":animated")) {
						if (nowshowpic < imgamount - 1) {
							nowshowpic++;
						} else {
							nowshowpic = 0;
						}
						huantu(nowshowpic);
						shezhixiaoyuandian(nowshowpic);
					}
				});

				//给右边按钮添加监听
				$("#anniu .zuo").click(function() {
					if (!$("#tupian ul").is(":animated")) {
						if (nowshowpic > 0) {
							nowshowpic--;
						} else {
							nowshowpic = imgamount - 1;
						}
						huantu(nowshowpic);
						shezhixiaoyuandian(nowshowpic);
					}
				});

				//给所有小圆点添加监听
				$("#xiaoyuandian ul li").click(function() {
					nowshowpic = $(this).index();
					huantu(nowshowpic);
					shezhixiaoyuandian(nowshowpic);
				});

				//让第num个li有cur，其余没cur
				function shezhixiaoyuandian(num) {
					$("#xiaoyuandian ul li").eq(num).addClass("cur")
							.siblings().removeClass("cur");
				}

				//核心函数，跑火车的执行者。
				function huantu(num) {
					$("#tupian ul").animate({
						"left" : -1 * $("#tupian ul li img").width() * num
					}, 500);
				}
			});
		</script>
	</body>
</html>