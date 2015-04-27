<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#guide_adv_putin_plot{font-size: 13px;}
/* 	#guide_adv_putin_plot .show_field{float:left;} */
	#guide_adv_putin_plot label.radio{margin-left: 28px;margin-right: 20px;}
	#guide_adv_putin_plot select{width: 100px;margin-left: 10px;}
	#guide_adv_putin_plot .controls label.checkbox:first-child{margin-left: 10px;}
	#guide_adv_putin_plot .city_data{display:none;}
	#guide_adv_putin_plot .area_data{display:none;}
</style>	

<div id="guide_adv_putin_plot">
	<div class="control-group area">
		<label class="control-label">区域：</label>
		<div class="controls">
			<select name="province" class="combox">
			<c:forEach var="rowData" items="${provinces}" >
				<option value="${rowData.get('id')}">${rowData.get('value')}</option>
			</c:forEach>
			</select>
			<select name="city" class="combox">
<!-- 				<option value=""></option> -->
			</select>
			<select name="area" class="combox">
<!-- 				<option value=""></option> -->
			</select>
			
			<select class="city_data"><!-- 保存所有的市，当选中省时，从该元素中取，此处多使用一个select的原因是因为select中的元素太多时，下拉列表后面的选项显示不了（下拉列表没高度) -->
			<c:forEach var="rowData" items="${citys}" >
				<option value="${rowData.get('id')}" class="${rowData.get('key')}">${rowData.get('value')}</option>
			</c:forEach>
			</select>
			<select class="area_data"><!-- 保存所有的区，当选中市时，从该元素中取 -->
			<c:forEach var="rowData" items="${areas}" >
				<option value="${rowData.get('id')}" class="${rowData.get('key')}">${rowData.get('value')}</option>
			</c:forEach>	
			</select>
		</div>
	</div>
	<div class="control-group industry">
		<label class="control-label">行业：</label>
		<div class="controls">
		<c:forEach var="rowData" items="${industrys}" >
			<label class="checkbox inline"><input type="checkbox" name="industryCheckBox" value="${rowData.get('id')}">${rowData.get('value')}</label>
		</c:forEach>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function(){
// 		var checkedPlot = "";
// 		$("#guide_adv_putin_plot input[name='plotRadio']").change(function(){ 
// 			var selectedPlot = $("#guide_adv_putin_plot input[name='plotRadio']:checked").val();
// 			if(checkedPlot != selectedPlot){
// 				checkedPlot = selectedPlot;
// 				showPlot(checkedPlot);
// 			}
// 		});
// 		$("#guide_adv_putin_plot input[name='plotRadio']").eq(0).attr("checked","checked");
// 		$("#guide_adv_putin_plot input[name='plotRadio']").eq(0).change();
// 		function showPlot(plotValue){
// 			$("#guide_adv_putin_plot ."+plotValue).show().siblings().hide();
// 		}
		$("#guide_adv_putin_plot select[name='province']").change(function(){
			$("#guide_adv_putin_plot select[name='city']").empty();
			$("#guide_adv_putin_plot select[name='area']").empty();
			var provinceValue=$(this).children('option:selected').val();
			$("#guide_adv_putin_plot select.city_data option."+provinceValue).clone().appendTo($("#guide_adv_putin_plot select[name='city']"));
// 			$("#guide_adv_putin_plot select[name='city']").prepend('<option value="" selected="selected"></option>');
// 			$("#guide_adv_putin_plot select[name='area']").prepend('<option value="" selected="selected"></option>');
		});
		
		$("#guide_adv_putin_plot select[name='city']").change(function(){
			$("#guide_adv_putin_plot select[name='area']").empty();
			var cityValue=$(this).children('option:selected').val();
			if("" != cityValue){
				$("#guide_adv_putin_plot select.area_data option."+cityValue).clone().appendTo($("#guide_adv_putin_plot select[name='area']"));
			}
// 			$("#guide_adv_putin_plot select[name='area']").prepend('<option value="" selected="selected"></option>');
		});
		$("#guide_adv_putin_plot select[name='province']").change();
	});
</script>