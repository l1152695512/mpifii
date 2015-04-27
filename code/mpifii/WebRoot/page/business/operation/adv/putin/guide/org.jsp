<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#adv_putin_org{font-size: 13px;}
	#adv_putin_org .controls label.checkbox:first-child{margin-left: 10px;}
</style>	

<div class="control-group" id="adv_putin_org">
	<label class="control-label">选择区域：</label>
	<div class="controls">
		<label class="checkbox inline"><input type="checkbox" name="orgs" checked="checked" value=""></label>
	</div>
</div>
<script type="text/javascript">
	function generOrgs(orgs){
		var orgsHtml="";
		for(var i=0;i<orgs.length;i++){
			var tips = '';
			if(orgs[i].permission == "0"){
				tips = ' style="color:red;" title="该区域已分配了投放权限，对该区域的广告投放会以消息提醒的方式通知该区域的管理员"';
			}
			orgsHtml += '<label class="checkbox inline"'+tips+'><input type="checkbox" checked="checked" name="orgs" value="'+orgs[i].key+'">'+orgs[i].value+'</label>';
		}
		$("#adv_putin_org .controls").html(orgsHtml);
	}
	
	function checkAdvPutinOrg(){
		if($("#adv_putin_org input[name='orgs']:checked").length == 0){
			myAlert("请选择区域！");
			return true;
		}
		return false;
	}
</script>