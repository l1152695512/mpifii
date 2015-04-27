<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#guide_adv_putin_plot_org{font-size: 13px;}
	#guide_adv_putin_plot_org .controls label.checkbox:first-child{margin-left: 10px;}
</style>	

<div id="guide_adv_putin_plot_org">
	<div class="control-group">
<!-- 		<div class="controls"> -->
			<label class="checkbox inline"><input type="checkbox" name="orgs" checked="checked" value="${rowData.get('key')}">${rowData.get('value')}</label>
<!-- 		</div> -->
	</div>
	<span>加载下级组织</span>
</div>
<script type="text/javascript">
	$(function(){
		$("#guide_adv_putin_plot_org select[name='province']").change(function(){
			$("#guide_adv_putin_plot_org select[name='city']").empty();
			$("#guide_adv_putin_plot_org select[name='area']").empty();
			var provinceValue=$(this).children('option:selected').val();
			$("#guide_adv_putin_plot_org select.city_data option."+provinceValue).clone().appendTo($("#guide_adv_putin_plot_org select[name='city']"));
// 			$("#guide_adv_putin_plot_org select[name='city']").prepend('<option value="" selected="selected"></option>');
// 			$("#guide_adv_putin_plot_org select[name='area']").prepend('<option value="" selected="selected"></option>');
		});
		
		$("#guide_adv_putin_plot_org select[name='city']").change(function(){
			$("#guide_adv_putin_plot_org select[name='area']").empty();
			var cityValue=$(this).children('option:selected').val();
			if("" != cityValue){
				$("#guide_adv_putin_plot_org select.area_data option."+cityValue).clone().appendTo($("#guide_adv_putin_plot_org select[name='area']"));
			}
// 			$("#guide_adv_putin_plot_org select[name='area']").prepend('<option value="" selected="selected"></option>');
		});
		$("#guide_adv_putin_plot_org select[name='province']").change();
	});
</script>