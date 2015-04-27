<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#guide_adv_putin_plot_industry{font-size: 13px;}
	#guide_adv_putin_plot_industry .controls label.checkbox:first-child{margin-left: 10px;}
</style>	

<div id="guide_adv_putin_plot_industry">
	<div class="control-group">
			<label class="checkbox inline"><input type="checkbox" name="industrys" value="${rowData.get('id')}">${rowData.get('value')}</label>
	</div>
</div>
<script type="text/javascript">
	$(function(){
		$("#guide_adv_putin_plot_industry select[name='province']").change(function(){
			$("#guide_adv_putin_plot_industry select[name='city']").empty();
			$("#guide_adv_putin_plot_industry select[name='area']").empty();
			var provinceValue=$(this).children('option:selected').val();
			$("#guide_adv_putin_plot_industry select.city_data option."+provinceValue).clone().appendTo($("#guide_adv_putin_plot_industry select[name='city']"));
		});
		
		$("#guide_adv_putin_plot_industry select[name='city']").change(function(){
			$("#guide_adv_putin_plot_industry select[name='area']").empty();
			var cityValue=$(this).children('option:selected').val();
			if("" != cityValue){
				$("#guide_adv_putin_plot_industry select.area_data option."+cityValue).clone().appendTo($("#guide_adv_putin_plot_industry select[name='area']"));
			}
		});
		$("#guide_adv_putin_plot_industry select[name='province']").change();
	});
</script>