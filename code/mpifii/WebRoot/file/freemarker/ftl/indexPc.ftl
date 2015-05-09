<!doctype html>
<html lang="zh">
	<head>
		<title>免费上网快速登录</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<link rel="stylesheet" type="text/css" href="css/global.css"/>
		<link rel="stylesheet" type="text/css" href="css/index.css"/>
		<script src="../commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script src="../commonjs/jquery.cookie.js" type="text/javascript"></script>
		<script src="../commonjs/jquery.jsonp.js" type="text/javascript"></script>
		<script src="../commonjs/jquery.ba-resize.js" type="text/javascript"></script>
		<script src="../commonjs/commons.js" type="text/javascript"></script>
		
		<script type="text/javascript" src="js/jquery.SuperSlide.2.1.1.js"></script>
		<script type="text/javascript" src="js/lunbo.js"></script>
		
		<link rel="stylesheet" href="js/jquery-simple-modal/assets/css/simplemodal.css" type="text/css" media="screen" title="no title" charset="utf-8"/>
		<script src="js/jquery-simple-modal/simple-modal.js" type="text/javascript" charset="utf-8"></script>
	</head>
	<body>
		<div class="header"><div class="inner"><img title="Pifii" src="img/logo.png"></div></div>
		<div id="content">
			<div class="left fl">
				<div id="lunbo"><!---轮播开始-->
					<div id="tupian" class="tupian">
						<ul>
							<#list adlist as ad>
									<li><a href="#"><img src="../mb/logo/${(ad.src)!}" /></a></li>
							</#list>	
						</ul>
					</div>
					<div id="anniu" class="anniu">
						<a class="zuo"></a>
						<a class="you"></a>
					</div>
					<div id="xiaoyuandian" class="xiaoyuandian">
						<ul></ul>
					</div>
				</div><!---轮播结束-->
			</div>
			<div class="right fr">
				<div class="yicen">手机号码登陆</div>
				<div class="ercen">
					<div class="tel">手&nbsp;机&nbsp;&nbsp;号:<input type="text" placeholder="请输入手机号" maxlength="11" minlength="11"/><span style="display:none;">sdfsa</span></div>
					<div class="password">验&nbsp;证&nbsp;&nbsp;码:<input type="text" placeholder="请输入验证码" />
						<div class="yanzhengma fr">获取验证码</div>
					</div>
					<div class="loginDate" style="display:none;">登录时间:<span></span></div>
					<div style=""></div>
				</div>
				<div class="sicen"><input type="button" value="登陆"/></div>
			</div>
		</div>
		<div id="yinyong">
			<div class="appIcons fl">
				<ul>
				<#list applist as app>
				<#if app.link?index_of("?cmd=auth")==-1  && app.link?index_of("?noAuth=noAuth")==-1>
					<#if app.link?index_of("http:")==-1>
						<#if app.link?index_of("?")!=-1>
							<li><div class="pic" data-url="/ifidc/mb/${app.link}&rid=${app.id}"><img src="../mb/logo/${app.icon}"></div><div class="p1">${app.name}</div></li>
						<#else>
							<li><div class="pic" data-url="/ifidc/mb/${app.link}?rid=${app.id}"><img src="../mb/logo/${app.icon}"></div><div class="p1">${app.name}</div></li>
						</#if>
					<#else>
						<#if app.link?index_of("?")!=-1>
							<li><div class="pic" data-url="${app.link}&rid=${app.id}"><img src="../mb/logo/${app.icon}"></div><div class="p1">${app.name}</div></li>
						<#else>
							<li><div class="pic" data-url="${app.link}?rid=${app.id}"><img src="../mb/logo/${app.icon}"></div><div class="p1">${app.name}</div></li>
						</#if>
					</#if>
				</#if>
				</#list>	
				</ul>
			</div>
			<div class="gonggaolan fr">
				<div class="txtMarquee-top">
					<div class="hd">
						<a class="next"></a>
						<a class="prev"></a>
					</div>
					<div class="bd">
						<ul class="infoList">
							<li>暂无信息</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<div class="footer">
			广州因孚网络科技有限公司 版权所有&copy;CopyRight 2014-2015
		</div>
		<script type="text/javascript">
			$('#yinyong .appIcons li .pic').each(function(){
				var href = $(this).data("url");
				if(href.indexOf("?") != -1){
					href += "&";
				}else{
					href += "?";
				}
				href += "routersn="+routersn+"&mac="+mac;
				$(this).click(function(){
					$.fn.SimpleModal({
						title: '应用',
		 				width: 400,
		 				height: 500,
						param: {
							url: href,
							iframe:true
						}
					}).showModal();
				});
			});
			//$('.lightbox').lightbox();
			//更新footer的位置
			function refreshFooterPosition(){
				$(".footer").css("position","static");
				$(document).scrollTop(300);
				if($(document).scrollTop() == 0){
					$(".footer").css({"position":"fixed","bottom":"0"});
				}
				$(document).scrollTop(0);
			}
			
			//getAuthInfo()、getAuthedPhone()、getMessages()这三个方法不能并行执行，有可能造成线程问题，
			//返回值会出现错乱（getAuthedPhone的返回值可能是getMessages的返回值），即使使用jQuery自带的$.getJSON也一样会出现这种问题
			function getMessages(){
				loading();
				$.jsonp({//使用该方法可以扑捉到错误
					url: serverUrl+"&cmd=pcAccess_messages&routersn="+routersn+"&mac="+mac+"&date="+new Date+"&jsonpCallback=?", 
					success: function(data) {
						$("#yinyong .infoList").html('');
						var messageHtml = "";
						for(var i=0;i<data.messages.length;i++){
							var url = data.messages[i].columns.url
							if(url == ""){
								url = "#";
							}
							messageHtml += '<li><span class="date">'+data.messages[i].columns.create_date+'</span><a href="'+url+'" target="_blank">'+(i+1)+'. '+data.messages[i].columns.title+'</a></li>';
						}
						$("#yinyong .infoList").html(messageHtml);
						$(".txtMarquee-top").slide({mainCell:".bd ul",autoPlay:true,effect:"topMarquee",vis:5,interTime:50,trigger:"click"});
						getAuthInfo(-1);
					},
					error: function(){
						getAuthInfo(-1);
					}
				});
			}
			
			function getAuthInfo(tryTimes,timeout_length){
				timeout_length = timeout_length || 3000;
				if(tryTimes > 0){
					loading();
				}
				$.jsonp({//使用该方法可以扑捉到错误
					url: homeUrl+"&cmd=pcAuthInfo&routersn="+routersn+"&mac="+mac+"&date="+new Date+"&jsonpCallback=?",
					success: function(data) {
						$("#content .right .ercen .loginDate").show();
						$("#content .right .ercen .tel input").hide();
						$("#content .right .ercen .tel span").show();
						$("#content .right .ercen .password").hide();
						$("#content .right .sicen").hide();
						$("#content .right .yicen").text("用户信息");
						
						if(data.tag == ""){
							$("#content .right .ercen .tel span").text("未知");
							$("#content .right .ercen .loginDate span").text("未知");
						}else{
							$("#content .right .ercen .tel span").text(data.tag);
							$("#content .right .ercen .loginDate span").text(data.auth_date);
						}
						closePop();
					},
					error: function(){
						if(tryTimes==-1){
							getAuthedPhone();
						}else if(tryTimes>1){
							tryTimes--;
							timeout_length = timeout_length+1000;
							setTimeout("getAuthInfo("+tryTimes+","+timeout_length+")",timeout_length);
						}else{
							setTimeout("refreshPage()",5000);
							closePop();
							//myAlert("认证失败，稍后请重试！");
						}
					}
				});
			}
			function loading(){
				if($.fn._maxZIndexOptionIndex() == -1){
					$.fn.SimpleModal({
						model: 'loading',
						width: 200,
						zindexAdd:100,
						closeButton: false,
						keyEsc:true,
						animate:false
					}).showModal();
				}
			}
			function myAlert(contents){
				var buttons = [{
					text:'关闭',
					classe:'btn primary'
				}];
				$.fn.SimpleModal({
					title: '提示',
					overlayClick:  true,
					closeButton: true,
					buttons: buttons,
					keyEsc: true,
					width: 400,
					contents: contents
				}).showModal();
			}
			function closePop(){
				var index = $.fn._maxZIndexOptionIndex();
				if(index != -1){
					var popId = window.popsOption[index].id;
					$("#simple-modal-"+popId).hideModal();
				}
			}
			var authedPhone = [];
			function getAuthedPhone(){
				$.jsonp({//使用该方法可以扑捉到错误
					url: serverUrl+"&cmd=pcAccess_phones&routersn="+routersn+"&mac="+mac+"&date="+new Date+"&jsonpCallback=?", 
					success: function(data) {
						authedPhone = data.phones;
						if(data.phones.length > 0){
							$("#content .right .ercen .tel input").val(data.phones[0].columns.phone);
							checkAuthed(data.phones[0].columns.phone);
						}
						closePop();
					},
					error: function(){
						closePop();
					}
				});
			}
			
			$("#content .right .ercen .tel input").keyup(function(){
				checkAuthed($(this).val());
			});
			
			function checkAuthed(phone){
				if(phone.length == 11 && authedPhone.length>0){
					for(var i=0;i<authedPhone.length;i++){
						if(phone == authedPhone[i].columns.phone){
							$("#content .right .ercen .password").hide();
							$("#content .right .sicen input").val("一键登录");
							return;
						}
					}
				}
				$("#content .right .ercen .password").show();
				$("#content .right .sicen input").val("登录");
			}
			
			$("#content .yanzhengma").click(function(){
				getCode();
			});
			
			function getCode(){
				var phone = $("#content .right .ercen .tel input").val();
				if(phone.length != 11){
					myAlert("手机号码填写有误！");
				}else{
					loading();
					$.jsonp({//使用该方法可以扑捉到错误
						url: serverUrl+"&cmd=pcAccess_code&phone="+phone+"&routersn="+routersn+"&mac="+mac+"&date="+new Date+"&jsonpCallback=?", 
						timeout: 10000,
						success: function(data) {
							closePop();
							if(data.success == "1"){
								$("#content .yanzhengma").text("60秒后再次获取");
								$("#content .yanzhengma").css("background","rgb(139, 139, 139)");
								$("#content .yanzhengma").unbind("click");
								refreshLaveSecondsInterval = setInterval("refreshLaveSeconds();",1000);
							}else{
								if(data.errorCode == '1'){
									myAlert("验证码发送失败，稍后请重试！");
								}else if(data.errorCode == '2'){
									myAlert("手机号码有误，请修改！");
								}else if(data.errorCode == '3'){
									myAlert("再次获取验证码的时间未到！");
								}else{
									myAlert(data.errorCode);
								}
							}
						},
						error: function(){
							closePop();
							myAlert("主子，第三方短信平台又偷懒去了，我们玩命处理中！");
						}
					});
				}
			}
			var refreshLaveSecondsInterval;
			function refreshLaveSeconds(){
				var laveSeconds = parseInt($("#content .yanzhengma").text())-1;
				if(isNaN(laveSeconds) || laveSeconds < 1){
					$("#content .yanzhengma").text("获取验证码");
					$("#content .yanzhengma").css("background","#2BB5C9");
					clearInterval(refreshLaveSecondsInterval);
					$("#content .yanzhengma").click(function(){
						getCode();
					});
				}else{
					$("#content .yanzhengma").text(laveSeconds+"秒后再次获取");
				}
			}
			
			$("#content .right .sicen input").click(function(){
				var code = $("#content .right .ercen .password input").val();
				var phone = $("#content .right .ercen .tel input").val();
				if(phone.length != 11){
					myAlert("手机号码填写有误！");
				}else if($("#content .right .ercen .password").is(":visible") && code.length != 4){
					myAlert("验证码填写有误！");
				}else{
					loading();
					$.jsonp({//使用该方法可以扑捉到错误
						url: serverAuthUrl+"&cmd=pcAccess_auth&phone="+phone+"&code="+code+"&routersn="+routersn+"&mac="+mac+"&date="+new Date+"&jsonpCallback=?", 
						timeout: 10000,
						success: function(data) {
							closePop();
							if(data.success == "1"){
								//setTimeout("refreshPage()",500);
								getAuthInfo(5);
							}else{
								myAlert(data.msg);
							}
						},
						error: function(){
							closePop();
							myAlert("暂时无法访问服务器，稍后请重试！");
						}
					});
				}
			});
			
			function refreshPage(){
				var newLocation = window.location.href;
				if(newLocation.indexOf('?') != -1){
					newLocation += '&';
				}else{
					newLocation += '?';
				}
				newLocation += 'date='+new Date();
				location=newLocation;
			}
			
			$("body").resize(function(){
				refreshFooterPosition();
			});
			refreshFooterPosition();
			getMessages();
		</script>
	</body> 
</html>
