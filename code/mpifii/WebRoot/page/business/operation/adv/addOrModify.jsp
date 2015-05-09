<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<style type="text/css">
/* 	#adv_group_info .row{padding:0;margin-bottom:10px;} */
/* 	#adv_group_info .row>span{float: left;line-height: 40px;width:100px;height:30px;color:gray;font-size:15px;font-weight:bold;text-align:right;display:inline-block;} */
/* 	#adv_group_info .row input{ border:1px solid #b4c0ce; color:#666;margin-left:20px; height:30px;border-radius:3px;} */
/* 	#adv_group_info .fl{float: left;} */
	#adv_group_info .control-label{
		float: left;
		line-height: 26px;
		width: 50px;
		text-align: center;
	}
</style>
<div id="adv_group_info">
	<form action="${pageContext.request.contextPath}/business/operation/adv/save" method="POST">
		<input type="hidden" name="id" value="${id}">
		<div class="control-group">
			<label class="control-label">名称</label>
<!-- 			<span>名称</span> -->
			<div class="controls">
				<input type="text" name="name" value="${name}" class="input-xlarge"
					maxlength="20" vMin="1" vType="length" placeholder='1-20位'
					onblur="onblurVali(this);">
				<span class="help-inline"></span>
			</div>
		</div>
	</form>
</div>
<script type="text/javascript">
</script>

