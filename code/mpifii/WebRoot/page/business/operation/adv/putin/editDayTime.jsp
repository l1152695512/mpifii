<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<style type="text/css">
	#putin_day_time_edit .box-content ul,#putin_day_time_edit .box-content li{list-style: none;margin: 0; padding: 0;}
	#putin_day_time_edit .box-content .action{text-align: center;}
	#putin_day_time_edit .box-content .action span:hover{background: #1a9aff;}
	#putin_day_time_edit .box-content .action span{display: inline-block;font-size: 16px;width: 150px;height: 40px;line-height: 40px;text-align: center;margin-right: 20px;background: #54B4EB;border-radius: 3px;cursor: pointer;font-weight: bold;color: white;}
	
</style>
<form id="putin_day_time_edit" class="form-horizontal" method="POST"
		action="${pageContext.request.contextPath}/business/oper/adv/putin/saveAdvPutinDayTime" >
	<div>
		<ul class="breadcrumb"> 
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li><a href="javascript:void(0);" onclick="ajaxContent('/business/oper/adv/putin');">广告投放</a><span class="divider"></span></li>
		</ul>
	</div>
	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2>投放时间修改</h2>
			</div>
			<div class="box-content">
				<input type="hidden" name="advPutinId" value="${advPutinId}">
				<ul>
					<li><!-- 选择或者上传广告物料 -->
						<%@ include file="guide/guideDayTime.jsp" %>
					</li>
					<div style="clear:both;"></div>
				</ul>
				<hr>
				<div class="action">
					<span>保存</span>
					<span>取消</span>
				</div>
			</div>
		</div>
	</div>
</form>
<script type="text/javascript">
	$(function(){
		$("#putin_day_time_edit .action span").eq(0).click(function(){
			$("#putin_day_time_edit").ajaxSubmit({
				success: function(resp){
					ajaxContent('/business/oper/adv/putin');
				}
			});
		});
		$("#putin_day_time_edit .action span").eq(1).click(function(){
			ajaxContent('/business/oper/adv/putin');
		});
	});
</script>