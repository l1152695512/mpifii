<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<style type="text/css">
	#adv_msg_putin .box-content ul,#adv_msg_putin .box-content li{list-style: none;margin: 0; padding: 0;}
	#adv_msg_putin .box-content .action{text-align: center;}
	#adv_msg_putin .box-content .action span:hover{background: #1a9aff;}
	#adv_msg_putin .box-content .action span{display: inline-block;font-size: 16px;width: 150px;height: 40px;line-height: 40px;text-align: center;margin-right: 20px;background: #54B4EB;border-radius: 3px;cursor: pointer;font-weight: bold;color: white;}
</style>
<div class="row-fluid" id="adv_msg_putin">
	<div class="box span12">
<!-- 		<div class="box-header well" > -->
<!-- 			<h2>投放时间修改</h2> -->
<!-- 		</div> -->
		<div class="box-content">
			<input type="hidden" name="advPutinMsgId" value="${advPutinMsgId}">
			<ul>
				<li><!-- 选择或者上传广告物料 -->
					<%@ include file="../putin/guide/org.jsp" %>
				</li>
				<div style="clear:both;"></div>
			</ul>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function(){
		$.ajax({
			type : "POST",
			url : 'business/oper/adv/putinMsg/getAvailableOrgsAction',
			data : {id:'${advPutinMsgId}'},
			success : function(data) {
				if(data.length == 0){
					myAlert("没有可以选择的区域！");
				}else{
					generOrgs(data);
				}
			}
		});
	});
</script>