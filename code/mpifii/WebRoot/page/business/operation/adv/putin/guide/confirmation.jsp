<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#adv_putin_confirmation .week_time_group{white-space: nowrap;}
	#adv_putin_confirmation .controls{margin-left: 0;margin-top: 5px;}
	#adv_putin_confirmation .controls.week_time{display: inline-block;}
	#adv_putin_confirmation ul{border-bottom: 1px solid #C7C7C7;}
	#adv_putin_confirmation ul.week_title{border-top: 1px solid #C7C7C7;}
	#adv_putin_confirmation ul.week_title li{width:70px;height:35px;}
	#adv_putin_confirmation li{width:70px;height:35px;line-height:35px;float: left;text-align: center;border-right: 1px solid #C7C7C7;}
	#adv_putin_confirmation li.disabled{background-color: rgb(218, 218, 218);}
</style>

<div id="adv_putin_confirmation">
	<div class="control-group">
		<label class="control-label">广告位：</label>
		<div class="controls">
			
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">投放区域：</label>
		<div class="controls">
			
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">投放行业：</label>
		<div class="controls">
			
		</div>
	</div>
	<div class="control-group week_time_group">
		<label class="control-label">投放时间：</label>
		<div class="controls week_time">
			<ul class="week_title">
			<li style="width:100px;"></li>
			<c:forEach var="week" items="${weeks}" >
			<li>
				<span>${week.value}</span>
			</li>
			</c:forEach>
			<li style="float: none;width: 0;"></li>
		</ul>
		<c:forEach var="time" items="${times}">
		<ul>
			<li style="width:100px;" class="timeTitle">
				<span>${time.value}</span>
			</li>
			<c:forEach var="week" items="${weeks}" >
			<li style="width:70px;" class="weeksAndTimes">
				<label class="checkbox inline"><input disabled="disabled" style="display:none;" type="checkbox"></label>
			</li>
			</c:forEach>
			<li style="float: none;width: 0;"></li>
		</ul>
		</c:forEach>
		</div>
	</div>
</div>
<script type="text/javascript">
	function loadConfirmation(){
		$("#adv_putin_confirmation .controls").eq(0).html($("#adv_spaces_list div.selected p").text());
		
		var orgs = "";
		$("#adv_putin_org input[name='orgs']:checked").each(function(){
			orgs += ","+$(this).parent().text();
		});
		if(orgs.length > 0){
			orgs = orgs.substring(1);
		}
		$("#adv_putin_confirmation .controls").eq(1).html(orgs);
		
		var industrys = "";
		$("#adv_putin_industry input[name='industrys']:checked").each(function(){
			industrys += ","+$(this).parent().text();
		});
		if(industrys.length > 0){
			industrys = industrys.substring(1);
		}
		$("#adv_putin_confirmation .controls").eq(2).html(industrys);
		
		var weekAndTime = "";
		$("#adv_putin_week_time .weeksAndTimes input[name='weeks_times']:checked").each(function(){
			var weekIndex = $(this).parent().parent().index();
			var timeIndex = $(this).parent().parent().parent().index();
			var thisInput = $("#adv_putin_confirmation .controls").eq(3).children().eq(timeIndex).children().eq(weekIndex).find("input");
			thisInput.attr("checked","checked");
			thisInput.show();
		});
	}
</script>