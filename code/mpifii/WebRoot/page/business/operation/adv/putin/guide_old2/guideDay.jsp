<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#guide_adv_putin_plot_day{font-size: 13px;}
	#guide_adv_putin_plot_day .controls label:first-child{margin-left: 10px;}
</style>

<div id="guide_adv_putin_plot_day">
	<div class="control-group">
		<label class="checkbox inline"><input type="checkbox" name="days" checked="checked" value="${rowData.get('key')}">${rowData.get('value')}</label>
	</div>
</div>
<script type="text/javascript">
	$(function(){
		
	});
</script>