<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#guide_adv_putin_day_time{font-size: 13px;}
	#guide_adv_putin_day_time .controls label:first-child{margin-left: 10px;}
</style>

<div id="guide_adv_putin_day_time">
	<div class="control-group">
		<label class="control-label">星期：</label>
		<div class="controls">
		<c:forEach var="rowData" items="${days}" >
			<label class="checkbox inline"><input type="checkbox" name="advDays" <c:if test="${rowData.get('selected') == '1'}">checked="checked"</c:if> value="${rowData.get('key')}">${rowData.get('value')}</label>
		</c:forEach>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">时间：</label>
		<div class="controls">
		<c:forEach var="rowData" items="${times}" >
			<label class="checkbox inline"><input type="checkbox" name="advTimes" <c:if test="${rowData.get('selected') == '1'}">checked="checked"</c:if> value="${rowData.get('key')}">${rowData.get('value')}</label>
		</c:forEach>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function(){
		
	});
</script>