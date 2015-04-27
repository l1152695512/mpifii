<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#adv_putin_industry{font-size: 13px;}
	#adv_putin_industry .controls label.checkbox:first-child{margin-left: 10px;}
</style>	

<div class="control-group" id="adv_putin_industry">
	<label class="control-label">选择行业：</label>
	<div class="controls">
		<label class="checkbox inline"><input type="checkbox" name="orgs" checked="checked" value=""></label>
	</div>
</div>
<script type="text/javascript">
	function generIndustrys(industrys){
		var industrysHtml="";
		for(var i=0;i<industrys.length;i++){
			industrysHtml += '<label class="checkbox inline"><input type="checkbox" checked="checked" name="industrys" value="'+industrys[i].key+'">'+industrys[i].value+'</label>';
		}
		$("#adv_putin_industry .controls").html(industrysHtml);
	}
	
	function checkAdvPutinIndustry(){
		if($("#adv_putin_industry input[name='industrys']:checked").length == 0){
			myAlert("请选择行业！");
			return true;
		}
		return false;
	}
</script>