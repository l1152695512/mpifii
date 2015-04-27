<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#adv_putin_week_time{white-space: nowrap;}
/* 	#adv_putin_week_time .controls label:first-child{margin-left: 10px;} */
	#adv_putin_week_time .controls{display: inline-block;margin-left: 0;}
	#adv_putin_week_time ul{border-bottom: 1px solid #C7C7C7;}
	#adv_putin_week_time ul.week_title{border-top: 1px solid #C7C7C7;}
	#adv_putin_week_time ul.week_title li{width:70px;height:50px;line-height: 20px;}
	#adv_putin_week_time li{width:70px;height:50px;line-height:35px;float: left;text-align: center;border-right: 1px solid #C7C7C7;}
	#adv_putin_week_time li.disabled{background-color: rgb(218, 218, 218);}
</style>

<div class="control-group" id="adv_putin_week_time">
	<label class="control-label">选择时间：</label>
	<div class="controls">
<!-- 		<label class="checkbox inline"><input type="checkbox" name="weeks_times" checked="checked" value=""></label> -->
		<ul class="week_title">
			<li style="width:100px;"></li>
			<c:forEach var="week" items="${weeks}" >
			<li>
				<span>${week.value}</span>
				<br>
				<label class="checkbox inline"><input type="checkbox">全天</label>
			</li>
			</c:forEach>
			<li style="float: none;width: 0;"></li>
		</ul>
		<c:forEach var="time" items="${times}">
		<ul>
			<li style="width:100px;line-height: 20px;" class="timeTitle">
				<span>${time.value}</span>
				<br>
				<label class="checkbox inline"><input type="checkbox">全时段</label>
			</li>
			<c:forEach var="week" items="${weeks}" >
			<li style="width:70px;" data-week="${week.id}" data-time="${time.id}" class="weeksAndTimes">
				<label class="checkbox inline"><input type="checkbox" name="weeks_times" class="${week.id}_${time.id}" value="${week.id}_${time.id}"></label>
			</li>
			</c:forEach>
			<li style="float: none;width: 0;"></li>
		</ul>
		</c:forEach>
	</div>
</div>
<script type="text/javascript">
	$(function(){
		$("#adv_putin_week_time .weeksAndTimes label").hide();
		$("#adv_putin_week_time .weeksAndTimes input").attr('disabled', 'disabled');
		$("#adv_putin_week_time .weeksAndTimes").addClass("disabled");
		$("#adv_putin_week_time .weeksAndTimes").mouseenter(function(){
			if(!$(this).hasClass("disabled")){
				$(this).children("label").show();
			}
		}).mouseleave(function() {
			if(!$(this).find("input").is(":checked")){
				$(this).children("label").hide();
			}
		});
		$("#adv_putin_week_time .week_title input").click(function(){
			var thisIndex = $(this).parent().parent().index();
			if($(this).is(":checked")){
				$("#adv_putin_week_time .weeksAndTimes").each(function(){
					if($(this).index() == thisIndex && undefined == $(this).find("input").attr("disabled")){
						$(this).find("input").attr("checked",true);
						$(this).children("label").show();
					}
				});
			}else{
				$("#adv_putin_week_time .weeksAndTimes").each(function(){
					if($(this).index() == thisIndex){
						$(this).find("input").removeAttr("checked");
						$(this).children("label").hide();
					}
				});
			}
		});
		
		$("#adv_putin_week_time .timeTitle input").click(function(){
			if($(this).is(":checked")){
				$(this).parent().parent().parent().find(".weeksAndTimes input").each(function(){
					if(undefined == $(this).attr("disabled")){
						$(this).attr("checked",true);
						$(this).parent().show();
					}
				});
			}else{
				var checkInput = $(this).parent().parent().parent().find(".weeksAndTimes input");
				checkInput.removeAttr("checked");
				checkInput.parent().hide();
			}
		});
	});
	function generWeekAndTimes(data){
		var inputs = $("#adv_putin_week_time .weeksAndTimes input");
		inputs.parent().hide();
		inputs.parent().parent().removeClass("disabled").addClass("disabled");
		inputs.attr('disabled', 'disabled');
// 		inputs.removeAttr("checked");
		$("#adv_putin_week_time input").removeAttr("checked");
		for(var i=0;i<data.length;i++){
			var thisInput = $("#adv_putin_week_time .weeksAndTimes input."+data[i].key);
			thisInput.removeAttr('disabled');
			thisInput.parent().parent().removeClass("disabled");
		}
	}
	
	function checkAdvPutinWeekAndTimes(){
		if($("#adv_putin_week_time .weeksAndTimes input[name='weeks_times']:checked").length == 0){
			myAlert("请选择时间！");
			return true;
		}
		return false;
	}
</script>