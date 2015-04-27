<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
/* 	#adv_putin_date{font-size: 13px;} */
/* 	#adv_putin_date .controls label:first-child{margin-left: 10px;} */
</style>

<div id="adv_putin_date" class="control-group">
	<label class="control-label">投放时间：</label>
	<div class="controls">
		<input type="text" name="start_time" readonly="readonly" class="input-xlarge datepicker">
		至
		<input type="text" name="end_time" readonly="readonly" class="input-xlarge datepicker">
	</div>
</div>
<script type="text/javascript">
	$(function(){
		var dates = $("#adv_putin_date input");
		dates.datepicker({
			minDate:0,
		    onSelect: function(selectedDate){
		       var option = this.name == "start_time"?"minDate" : "maxDate";
		       dates.not(this).datepicker("option", option, selectedDate);
		    }
		});
	});
	function checkAdvPutinDate(){
		var startDate = $("#adv_putin_date input[name='start_time']").val();
		if(undefined == startDate || ""==startDate){
			myAlert("请填写开始时间！");
			return true;
		}
		var endDate = $("#adv_putin_date input[name='end_time']").val();
		if(undefined == endDate || ""==endDate){
			myAlert("请填写结束时间！");
			return true;
		}
		return false;
	}
</script>