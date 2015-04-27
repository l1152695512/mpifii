<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<style type="text/css">
	#adv_putin_guide .box-content ul,#adv_putin_guide .box-content li{list-style: none;margin: 0; padding: 0;}
	
	#adv_putin_guide .box-content .action{text-align: center;}
	#adv_putin_guide .box-content .action span:hover{background: #1a9aff;}
	#adv_putin_guide .box-content .action span{display: inline-block;font-size: 16px;width: 150px;height: 40px;line-height: 40px;text-align: center;margin-right: 20px;background: #54B4EB;border-radius: 3px;cursor: pointer;font-weight: bold;color: white;}
</style>
<form id="adv_putin_guide" class="form-horizontal" enctype="multipart/form-data"
		action="${pageContext.request.contextPath}/business/oper/adv/putin/save" method="POST">
	<div>
		<ul class="breadcrumb"> 
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li><a href="javascript:void(0);" onclick="ajaxContent('/business/oper/adv/putin');">广告投放</a><span class="divider"></span></li>
		</ul>
	</div>
	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2>广告投放向导</h2>
			</div>
			<div class="box-content">
				<ul>
					<li class="space"><!-- 选择广告位 -->
						<%@ include file="choiceAdvSpace.jsp" %>
<!-- 						<input type="hidden" name="adv_spaces_id"> -->
<!-- 						<ul id="adv_spaces_list"></ul> -->
<!-- 						<div style="clear:both;"></div> -->
<!-- 						<div class="holder"></div> -->
					</li>
					<li class="adv"><!-- 选择或者上传广告物料 -->
						<%@ include file="guideAdv.jsp" %>
					</li>
					<li class="order"><!-- 选择投放策略顺序及投放时间段 -->
						<%@ include file="setPutinDate.jsp" %>
						<%@ include file="choicePlotOrder.jsp" %>
					</li>
					<li class="time" data-plot-type="weeksAndTimes"><!-- 选择投放时间 -->
						<%@ include file="weekTime.jsp" %>
					</li>
					<li class="industry" data-plot-type="industry"><!-- 选择投放行业 -->
						<%@ include file="industry.jsp" %>
					</li>
					<li class="org" data-plot-type="org"><!-- 选择投放区域 -->
						<%@ include file="org.jsp" %>
					</li>
					<li class="confirmation"><!-- 确认信息 -->
						<%@ include file="confirmation.jsp" %>
					</li>
					<div style="clear:both;"></div>
				</ul>
				<hr>
				<div class="action">
					<span>上一步</span>
					<span>下一步</span>
					<span>投放</span>
<!-- 					<span>统一投放</span> -->
				</div>
			</div>
		</div>
	</div>
</form>
<script type="text/javascript">
	checkAdvGuideActionButton(0);//显示向导的第一步
	$("#adv_putin_guide .action span").eq(0).click(function(){
		advGuidePreviousStep();
	});
	$("#adv_putin_guide .action span").eq(1).click(function(){
		advGuideNextStep();
	});
	$("#adv_putin_guide .action span").eq(2).click(function(){
		myConfirm("确定要投放该广告？",function(){
			$("#adv_putin_guide").ajaxSubmit({
				success: function(resp){
					ajaxContent('/business/oper/adv/putin');
				}
			});
		});
	});
	
	function advGuideNextStep(){
		var currentStep = $("#adv_putin_guide .box-content>ul>li:visible").index();
		var maxLength = $("#adv_putin_guide .box-content>ul>li").length-1;
		if(currentStep<maxLength){
			currentStep++;
		}
		checkAdvGuideActionButton(currentStep);
	}
	function advGuidePreviousStep(){
		var currentStep = $("#adv_putin_guide .box-content>ul>li:visible").index();
		if(currentStep > 0){
			currentStep--;
		}
		checkAdvGuideActionButton(currentStep);
	}
	function checkAdvGuideActionButton(showStep){
		var currentStep = $("#adv_putin_guide .box-content>ul>li:visible").index();
		var stepNum = $("#adv_putin_guide .box-content>ul>li").length;
		if(showStep > currentStep){
			var returnStep = checkAdvPutinGuideVerify();
			if(returnStep >= 0){
				if(returnStep != $("#adv_putin_guide .box-content>ul>li:visible").index()){
					checkAdvGuideActionButton(returnStep);
				}
				return;
			}
		}
		if(showStep < 1){//没有上一步了
			$("#adv_putin_guide .action span").eq(0).hide();
		}else{
			$("#adv_putin_guide .action span").eq(0).show();
		}
		if(showStep >= stepNum-1){//没有下一步了
			$("#adv_putin_guide .action span").eq(1).hide();
		}else{
			$("#adv_putin_guide .action span").eq(1).show();
		}
		if(showStep == stepNum-1){//最后一步
			$("#adv_putin_guide .action span").eq(2).show();
// 			$("#adv_putin_guide .action span").eq(3).show();
		}else{
			$("#adv_putin_guide .action span").eq(2).hide();
// 			$("#adv_putin_guide .action span").eq(3).hide();
		}
		$("#adv_putin_guide .box-content>ul>li").eq(showStep).fadeIn().siblings().hide();
	}
	function checkAdvPutinGuideVerify(){
		var currentStep = $("#adv_putin_guide .box-content>ul>li:visible");
		var hasError = false;
		var loadData = false;
		if(currentStep.hasClass("space")){
			hasError = checkAdvputinSpace();
		}else if(currentStep.hasClass("adv")){
			hasError = checkAdvPutinContent();
		}else if(currentStep.hasClass("order")){
			hasError = checkAdvPutinDate();
			loadData = true;
		}else if(currentStep.hasClass("time")){
			hasError = checkAdvPutinWeekAndTimes();
			loadData = true;
		}else if(currentStep.hasClass("industry")){
			hasError = checkAdvPutinIndustry();
			loadData = true;
		}else if(currentStep.hasClass("org")){
			hasError = checkAdvPutinOrg();
			loadData = true;
		}
		if(hasError){
			return currentStep.index();
		}else{
			if(loadData){
				if(currentStep.next().hasClass("confirmation")){//下一步为确认信息
					loadConfirmation();
				}else{
					return getNextPlotData(currentStep.index());
				}
			}
			return -1;
		}
	}
	function getNextPlotData(currentIndex){
		var thisPageIndex = -1;
		var nextStepType = $("#adv_putin_guide .box-content>ul>li:visible").next().data("plotType");
		if(undefined != nextStepType && "" != nextStepType){
			var params = getSelectedParams();
			params.plotType = nextStepType;
			var data = $.param(params,true);
			$.ajax({
				type : "POST",
				async:false,
				url : 'business/oper/adv/putin/getPlot',
				data : data,
				success : function(data){
					if(data.length == 0){
						thisPageIndex = currentIndex;
						if(nextStepType == "weeksAndTimes"){
							myAlert("没有可投放的时间，请重新选择投放策略！");
						}else if(nextStepType == "industry"){
							myAlert("没有可投放的行业，请重新选择投放策略！");
						}else if(nextStepType == "org"){
							myAlert("没有可投放的区域，请重新选择投放策略！");
						}
					}else{
						if(nextStepType == "weeksAndTimes"){
							generWeekAndTimes(data);
						}else if(nextStepType == "industry"){
							generIndustrys(data);
						}else if(nextStepType == "org"){
							generOrgs(data);
						}
					}
				}
			});
		}
		return thisPageIndex;
	}
	
	function getSelectedParams(){
		var params = {};
		params.spaceId = $("#adv_putin_space input[name='adv_spaces_id']").val();
		params.startDate = $("#adv_putin_date input[name='start_time']").val();
		params.endDate = $("#adv_putin_date input[name='end_time']").val();
		var showIndex = $("#adv_putin_guide .box-content>ul>li:visible").index();
		var advPutinTime = $("#adv_putin_guide .box-content>ul>li.time");
		if(advPutinTime.index()<=showIndex){
			var timeAndWeekArray = [];
			$("#adv_putin_week_time input[name='weeks_times']:checked").each(function(){
				timeAndWeekArray.push($(this).val());
			});
			params.weeks_times = timeAndWeekArray;
		}
		var advPutinIndustry = $("#adv_putin_guide .box-content>ul>li.industry");
		if(advPutinIndustry.index()<=showIndex){
			var industrysArray = [];
			$("#adv_putin_industry input[name='industrys']:checked").each(function(){
				industrysArray.push($(this).val());
			});
			params.industrys = industrysArray;
		}
		var advPutinOrg = $("#adv_putin_guide .box-content>ul>li.org");
		if(advPutinOrg.index()<=showIndex){
			var orgsArray = [];
			$("#adv_putin_org input[name='orgs']:checked").each(function(){
				orgsArray.push($(this).val());
			});
			params.orgs = orgsArray;
		}
		return params;
	}
</script>