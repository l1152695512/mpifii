<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#adv_putin_plot_choice_order{font-size: 13px;}
	#adv_putin_plot_choice_order .controls label.radio{margin-left: 20px;margin-right: 20px;}
</style>

<div id="adv_putin_plot_choice_order" class="control-group">
	<label class="control-label">投放策略：</label>
	<div class="controls">
		<label class="radio inline"><input data-plot-index="time,industry,org" type="radio" name="plot_type" checked="checked" value="time">按时间投放</label>
		<label class="radio inline"><input data-plot-index="org,industry,time" type="radio" name="plot_type" value="org">按区域投放</label>
		<label class="radio inline"><input data-plot-index="industry,time,org" type="radio" name="plot_type" value="industry">按行业投放</label>
	</div>
</div>
<script type="text/javascript">
	var checkedPlotType = "time";
	$("#adv_putin_plot_choice_order input[name='plot_type']").change(function(){
		var checkedRadio = $("#adv_putin_plot_choice_order input[name='plot_type']:checked");
		var selectedPlot = checkedRadio.val();
		if(checkedPlotType != selectedPlot){
			checkedPlotType = selectedPlot;
			var plotIndexArray = checkedRadio.data("plotIndex").split(",");
			for(var i=0;i<plotIndexArray.length;i++){
				$("#adv_putin_guide .box-content>ul>li."+plotIndexArray[i]).insertBefore($("#adv_putin_guide .box-content>ul>li.confirmation"));
			}
		}
	});
</script>