<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
	.top_adv_putin_message{position: absolute;top: 0;width: 100%;height: 40px;}
	.top_adv_putin_message .BreakingNewsController{text-align: center;height: 100%;color:white;}
	.top_adv_putin_message .BreakingNewsController ul li a{color:rgb(216, 216, 216);}
	.top_adv_putin_message .BreakingNewsController ul li a:hover{color:white;}
</style>

<!-- topbar starts -->
<div class="navbar">
	<div class="navbar-inner">
		<div class="container-fluid">
			<a class="btn btn-navbar" data-toggle="collapse" data-target=".top-nav.nav-collapse,.sidebar-nav.nav-collapse">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</a>
<!-- 			<a class="brand" href="#" onclick="ajaxContent('/content');">  -->
			<a href="#" onclick="ajaxContent('/content');"> 
				<img alt="Charisma Logo" src="${user_logo}" /> 
<!-- 				<span>品派联盟</span> -->
			</a>
			<!-- theme selector starts -->
			<div class="btn-group pull-right theme-container" style="z-index: 1;">
				<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
					<i class="icon-tint"></i><span class="hidden-phone"> 切换皮肤</span>
					<span class="caret"></span>
				</a>
				<ul class="dropdown-menu" id="themes">
					<li><a data-value="classic" href="#"><i class="icon-blank"></i>经典主题</a></li>
					<li><a data-value="cerulean" href="#"><i class="icon-blank"></i>天蓝主题</a></li>
					<li><a data-value="Turquoise" href="#"><i class="icon-blank"></i>深绿主题</a></li>
					<li><a data-value="cyborg" href="#"><i class="icon-blank"></i>暗黑主题</a></li>
					<li><a data-value="redy" href="#"><i class="icon-blank"></i>红色主题</a></li>
					<li><a data-value="journal" href="#"><i class="icon-blank"></i>纯白主题</a></li>
					<li><a data-value="simplex" href="#"><i class="icon-blank"></i>海蓝主题</a></li>
					<li><a data-value="slate" href="#"><i class="icon-blank"></i>黑格子主题</a></li>
					<li><a data-value="spacelab" href="#"><i class="icon-blank"></i>默认主题</a></li>
					<li><a data-value="united" href="#"><i class="icon-blank"></i>棕红主题</a></li>
				</ul>
			</div>
			<!-- theme selector ends -->
			<!-- user dropdown starts -->
			<div class="btn-group pull-right" style="z-index: 1;">
				<!-- <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
					<i class="icon-th-large"></i><span class="hidden-phone"> 系统切换</span>
					<span class="caret"></span>
				</a> -->
				<ul class="dropdown-menu">
					<%-- <% for(systems in systemsList){ %>
						<li><a href="${pageContext.request.contextPath}/js/jf/index?ids=${systems.ids!}">${escapeXml(systems.names!)}</a></li>
						<li class="divider"></li>
					<% } %> --%>
				</ul>
			</div>
			<!-- user dropdown ends -->
			
			<!-- user dropdown starts -->
			<div class="btn-group pull-right" style="z-index: 1;">
				<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
					<i class="icon-user"></i><span class="hidden-phone">个人中心</span>
					<span class="caret"></span>
				</a>
				<ul class="dropdown-menu">
					<li><a href="#" onclick="getUserInfo()">用户信息</a></li>
					<li><a href="#" onclick="changePass()">密码设置</a></li>
					<li class="divider"></li>
					<li><a href="${pageContext.request.contextPath}/loginOut">退出系统</a></li>
				</ul>
			</div>
			<div class="btn-group pull-right" style="z-index: 1;">
				<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
					<i class="icon-bell blue"></i><span class="hidden-phone">帮助中心</span>
					<span class="caret"></span>
				</a>
				<ul class="dropdown-menu">
					<li><a href="${cxt}/file/operations.mp4" target="_blank"  >视频教程</a></li>
				</ul>
			</div>
		</div>
	</div>
</div>
<div class="top_adv_putin_message">
	<div class="BreakingNewsController easing" id="breakingNews1">
<!-- 	<div class="bn-title"></div> -->
	    <ul>
<!-- 	        <li><a href="javascript:void(0);" onclick="ajaxContent('/business/oper/adv/putinMsg');">a</a></li> -->
<!-- 	        <li><a href="javascript:void(0);" onclick="ajaxContent('/business/oper/adv/putinMsg');">b</a></li> -->
	    </ul>
<!-- 			    <div class="bn-arrows"><span class="bn-arrows-left"></span><span class="bn-arrows-right"></span></div>     -->
	</div>
</div>
<!-- topbar ends -->
<script type="text/javascript">
	function getUserInfo(){
		/* $.fn.SimpleModal({
			title: '用户信息',
			width: 550,
	        keyEsc:true,
			buttons: [{
	    		text:'取消',
	    		classe:'btn secondary'
	    	}],
			param: {
				url: 'system/user/getUserInfo'
			}
		}).showModal(); */
		ajaxContent('/system/user/getUserInfo');
	}
	function changePass(){
		$.fn.SimpleModal({
			title: '修改密码',
			width: 550,
	        keyEsc:true,
			buttons: [{
	    		text:'确定',
	    		classe:'btn primary btn-margin',
	    		clickEvent:function() {
	    			var newPass1 = $("#newPass1").val();
	    			var newPass2 = $("#newPass2").val();
	    			if(newPass1 != newPass2){
	    				myAlert("两次输入的密码不一致，请重新输入!");
	    		 		return;
	    			}
	    			var errorCount = formVali($("#passChangeForm").get(0));
	    			if(errorCount != 0){
	    		 		return;
	    			} 
	    			$("#passChangeForm").ajaxSubmit({
	    		        success: function(data){
	    		        	myAlert(data.msg,function(){
								closePop();
							});
	    		        }
	    		  	}); 
	            }
	    	},{
	    		text:'取消',
	    		classe:'btn secondary'
	    	}],
			param: {
				url: 'page/system/user/passChange.jsp'
			}
		}).showModal();
	}
	var checkUserTipsMessageTimerLength = 5000;
	$('#breakingNews1').BreakingNews({
		title: '消息',
		timer:checkUserTipsMessageTimerLength
	});
	checkUserTipsMessageTimer();
	function checkUserTipsMessageTimer(){
		$.ajax({
			type: "POST",
			global:false,
			url: "business/oper/adv/putinMsg/checkMsg",
			success : function(data) {
				var msgsHtml = '';
				if(data.length > 0){
					for(var i=0;i<data.length;i++){
						var displayStyle=' style="display: list-item;"';
						if(i!=0){
							displayStyle=' style="display: none;"';
						}
						msgsHtml +='<li'+displayStyle+'><a href="javascript:void(0);" onclick="ajaxContent(\''+data[i].url+'\');">'+data[i].tip+'</a></li>';
					}
				}
				$('.top_adv_putin_message .BreakingNewsController ul').html(msgsHtml);
				if(data.length > 0){
					setTimeout(checkUserTipsMessageTimer,checkUserTipsMessageTimerLength*data.length);
				}
			}
		});
	}
</script>