<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<style type="text/css">
	#adv_putin_guide .box-content ul,#adv_putin_guide .box-content li{list-style: none;margin: 0; padding: 0;}
	#adv_putin_guide .choice_adv_spaces li{float: left;width: 28%;margin: 10px 2% 20px 3%;}
	#adv_putin_guide .choice_adv_spaces li div{opacity: 0.7;filter: alpha(opacity=70);cursor: pointer;}
	#adv_putin_guide .choice_adv_spaces li div:hover{opacity: 1;filter: alpha(opacity=100);}
	#adv_putin_guide .choice_adv_spaces li p{text-align: center;font-size: 15px;margin: 10px 0;}
	#adv_putin_guide .choice_adv_spaces .holder{border-top: 1px solid rgb(241, 241, 241);padding-top: 20px;}
	#adv_putin_guide .box-content .choice_adv_spaces li div.selected{opacity: 1;filter: alpha(opacity=100);}
	#adv_putin_guide .box-content .action{text-align: center;}
	#adv_putin_guide .box-content .action span:hover{background: #1a9aff;}
	#adv_putin_guide .box-content .action span{display: inline-block;font-size: 16px;width: 150px;height: 40px;line-height: 40px;text-align: center;margin-right: 20px;background: #54B4EB;border-radius: 3px;cursor: pointer;font-weight: bold;color: white;}
	
	#adv_putin_guide .box-content .plot_order{text-align: center;margin: 10px 0 20px 0;padding-bottom: 20px;border-bottom: 1px solid rgb(241, 241, 241);}
/* 	#adv_putin_guide .box-content .plot_order span:hover{background: #54B4EB;-webkit-box-shadow: 0 0 20px #54B4EB;-moz-box-shadow: 0 0 20px #54B4EB;box-shadow: 0 0 20px #54B4EB;} */
	#adv_putin_guide .box-content .plot_order span{display: inline-block;font-size: 16px;text-align: center;margin: 0 20px 0 0;padding:0;font-weight: bold;color: white;background: #7DBFE6;-webkit-box-shadow: 0 0 20px #7DBFE6;-moz-box-shadow: 0 0 20px #7DBFE6;box-shadow: 0 0 20px #7DBFE6;}
	#adv_putin_guide .box-content .plot_order span.order{width: 20px;height: 20px;line-height: 18px;border-radius: 10px;}
	#adv_putin_guide .box-content .plot_order span.plot{width: 120px;height: 40px;line-height: 40px;border-radius: 3px;cursor: pointer;}
	
	#adv_putin_guide .box-content .plot_order span:first-child{cursor: default;}
	#adv_putin_guide .box-content .plot_order span:first-child,#adv_putin_guide .box-content .plot_order span.plot:hover{background: #1D9FE9;-webkit-box-shadow: 0 0 20px #1D9FE9;-moz-box-shadow: 0 0 20px #1D9FE9;box-shadow: 0 0 20px #1D9FE9;}
	
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
					<li class="choice_adv_spaces"><!-- 选择广告位 -->
						<input type="hidden" name="adv_spaces_id">
						<ul id="adv_spaces_list"></ul>
						<div style="clear:both;"></div>
						<div class="holder"></div>
					</li>
					<li><!-- 选择或者上传广告物料 -->
						<%@ include file="guideAdv.jsp" %>
					</li>
					<li><!-- 选择投放策略,选择投放时间 -->
						<div class="plot_order">
							<span class="plot" data-content-class="plot_org">组织</span>
							<span class="order">></span>
							<span class="plot" data-content-class="plot_industry">行业</span>
							<span class="order">></span>
							<span class="plot" data-content-class="plot_week">星期</span>
							<span class="order">></span>
							<span class="plot" data-content-class="plot_time">时间</span>
						</div>
						<ul class="plot_content">
							<li class="plot_org"><%@ include file="guideOrg.jsp" %></li>
							<li class="plot_industry"><%@ include file="guideDayTime.jsp" %></li>
							<li class="plot_week"><%@ include file="guideOrg.jsp" %></li>
							<li class="plot_time"><%@ include file="guideDayTime.jsp" %></li>
						</ul>
					</li>
					<div style="clear:both;"></div>
				</ul>
				<hr>
				<div class="action">
					<span>上一步</span>
					<span>下一步</span>
					<span>权限内投放</span>
					<span>统一投放</span>
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
		putinAdv("0");
	});
	$("#adv_putin_guide .action span").eq(3).click(function(){
		putinAdv("1");
	});
	function putinAdv(type){
// 		var selectedPlot = $("#adv_putin_guide input[name='plotRadio']:checked").val();
// 		if("area" == selectedPlot){
			var provinceValue=$("#guide_adv_putin_plot select[name='province'] option:selected").val();
			if(undefined == provinceValue){
				myAlert("没有省份数据，请联系管理员添加省份数据！");
				return;
			}else if(provinceValue == ""){
				myAlert("请选择省份！");
				return;
			}
			
			var cityValue=$("#guide_adv_putin_plot select[name='city'] option:selected").val();
			if(undefined == cityValue){
				myAlert("没有该省份的城市数据，请联系管理员添加该省份的城市数据！");
				return;
			}else if(cityValue == ""){
				myAlert("请选择城市！");
				return;
			}
			var areaValue=$("#guide_adv_putin_plot select[name='area'] option:selected").val();
			if(undefined == areaValue){
				myAlert("没有该城市的区数据，请联系管理员添加该城市的区数据！");
				return;
			}else if(areaValue == ""){
				myAlert("请选择区！");
				return;
			}
// 		}else if("industry" == selectedPlot){
			var checkedLength = $("#adv_putin_guide input[name='industryCheckBox']:checked").length;
			if(checkedLength == 0){
				myAlert("请至少选择一个行业！");
				return;
			}
// 		}
		var dayLength = $("#adv_putin_guide input[name='advDays']:checked").length;
		if(dayLength == 0){
			myAlert("请至少选择一天！");
			return;
		}
		var timeLength = $("#adv_putin_guide input[name='advTimes']:checked").length;
		if(timeLength == 0){
			myAlert("请至少选择一个时间段！");
			return;
		}
		$("#adv_putin_guide input[name='putinType']").val(type);
		return
		$("#adv_putin_guide").ajaxSubmit({
			success: function(resp){
				ajaxContent('/business/oper/adv/putin');
			}
		});
	}
	
	function advGuideNextStep(){
		var currentStep = $("#adv_putin_guide .box-content>ul>li:visible").index();
		if(currentStep < $("#adv_putin_guide .box-content>ul>li").length-1){
			currentStep++;
		}
		if(currentStep == 2){//选择投放策略
			var currentPlotStep = $("#adv_putin_guide .box-content .plot_content>li:visible").index();
			currentStep += currentPlotStep;
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
		var stepNum = $("#adv_putin_guide .box-content>ul>li").length;
		var plotStepNum = $("#adv_putin_guide .box-content .plot_content>li").length;
		if(showStep<0){
			showStep=0;
		}else if(showStep>stepNum-1+plotStepNum-1){
			showStep = $("#adv_putin_guide .box-content>ul>li").length-1;
		}
		var returnStep = checkAdvPutinGuideVerify(showStep);
		if(returnStep >= 0){
			if(returnStep != $("#adv_putin_guide .box-content>ul>li:visible").index()){
				checkAdvGuideActionButton(returnStep);
			}
			return;
		}
		if(showStep < 1){//没有上一步了
			$("#adv_putin_guide .action span").eq(0).hide();
		}else{
			$("#adv_putin_guide .action span").eq(0).show();
		}
		if(showStep >= stepNum-1+plotStepNum-1){//没有下一步了
			$("#adv_putin_guide .action span").eq(1).hide();
		}else{
			$("#adv_putin_guide .action span").eq(1).show();
		}
		if(showStep == stepNum-1+plotStepNum-1){//最后一步
			$("#adv_putin_guide .action span").eq(2).show();
			$("#adv_putin_guide .action span").eq(3).show();
		}else{
			$("#adv_putin_guide .action span").eq(2).hide();
			$("#adv_putin_guide .action span").eq(3).hide();
		}
		$("#adv_putin_guide .box-content>ul>li").eq(showStep).fadeIn().siblings().hide();
	}
	function checkAdvPutinGuideVerify(showStep){
		if(showStep>0){//第一步已完成
			if($("#adv_putin_guide input[name='adv_spaces_id']").val() == ''){
				myAlert("请先选择广告位！");
				return 0;
			}
		}
		if(showStep>1){//第二步已完成
			var name = $("#ad_picture_edit input[name='name']").val();
			if(name == ""){
				myAlert("广告名称必须填写！");
				return 1;
			}
			var advContentId = $("#ad_picture_edit input[name='contentId']").val();
			var uploadFileError = 0;
			$("#ad_picture_edit .upload_list input[type='file']").each(function(index){
				var filePath = $(this).val();
				if(advContentId=="" && filePath==""){
					uploadFileError = 1;
					if($("#ad_picture_edit .upload_list input[type='file']").length > 1){
						myAlert("请上传所有图片！");
					}else{
						myAlert("请上传图片！");
					}
					return false;
				}
			});
			if(uploadFileError > 0){
				return 1;
			}
			var link = $("#ad_picture_edit input[name='link']").val();
			if(link && link.length > 255){
				myAlert("链接地址字符太长！");
				return 1;
			}
		}
		if(showStep>2){
			$("#adv_putin_guide .box-content .plot_order span.plot:hidden").each(function(){
				var plotType = $(this).data("contentClass");
			});
			var currentPlotType = $("#adv_putin_guide .box-content .plot_order span.plot").eq(0).data("contentClass");
			
			
			
			var checkedLength = $("#adv_putin_guide input[name='industryCheckBox']:checked").length;
			if(checkedLength == 0){
				myAlert("请至少选择一个行业！");
				return;
			}
		}
		return -1;
	}
	function checkPlot(plotType){
		if("plot_org" == plotType){
			var checkedOrgs = $("#guide_adv_putin_plot_org .control-group").last().find("input[name='orgs']:checked");
			if(checkedOrgs.length <= 0){
				
			}
		}else if("plot_industry" == plotType){
			var industrys = $("#guide_adv_putin_plot_industry .control-group").last().find("input[name='industrys']:checked").val();
			params.industrys=industrys;
		}else if("plot_week" == plotType){
			var days = $("#guide_adv_putin_plot_day .control-group").last().find("input[name='days']:checked").val();
			params.days=days;
		}else if("plot_time" == plotType){
			var times = $("#guide_adv_putin_plot_time .control-group").last().find("input[name='times']:checked").val();
			params.times=times;
		}
	}
	
	
	$("#adv_putin_guide .holder").jPages({
		containerID:"adv_spaces_list",
		perPage : 3,
		startPage:1,
		keyBrowse : true,
		realPagination:true,
	    serverParams:{
	    	url:"business/oper/adv/putin/advSpacesList",
	    	generDataHtml:generAdvSpacesListData
	    }
	});
	function generAdvSpacesListData(data,searchParams){
		var recHtml = "";
		if(data.length > 0){
			var selectedAdvSpaces = $("#adv_putin_guide input[name='adv_spaces_id']").val();
			for(var i=0;i<data.length;i++){
				var selected = "";
				if(data[i].id == selectedAdvSpaces){
					selected = "class='selected'";
				}
				recHtml += '<li data-id="'+data[i].id+'" data-adv-type="'+data[i].adv_type+'" data-imgs="'+data[i].imgs+'"><div '+selected+'><img src="'+data[i].preview_img+'"><p>'+data[i].name+'</p></div></li>';;
			}
		}else{
			recHtml = '<div><font color="red">没有可投放的广告位！</font></div>';
		}
		$("#adv_spaces_list").html(recHtml);
		$("#adv_spaces_list div").click(function(){
			$(this).addClass("selected").parent().siblings().find("div").removeClass("selected");
			$("#adv_putin_guide input[name='adv_spaces_id']").val($(this).parent().data("id"));
			updateAdvPutinImgUpload($(this).parent().data("advType"),$(this).parent().data("imgs"),'','','','');
// 			$("#adv_putin_preview_iframe").attr("src","business/oper/adv/putin/showAdvPreview?advSpacesId="+advSpacesId);
			advGuideNextStep();
		});
	}
	plotOrderClickEvent();
	function plotOrderClickEvent(){
		$("#adv_putin_guide .box-content .plot_order span.plot").unbind("click");
		$("#adv_putin_guide .box-content .plot_order span.plot").click(function(){
			if($(this).index() == 0 || $(this).is(":hidden")){
				return;
			}
			$(this).prev().prependTo($(this).parent());
			$(this).prependTo($(this).parent());
			
			$("#adv_putin_guide .box-content .plot_content ."+$(this).data("contentClass")).show().siblings().hide();
		});
	}
	$("#adv_putin_guide .box-content .plot_content ."+$("#adv_putin_guide .box-content .plot_order span.plot").eq(0).data("contentClass")).show().siblings().hide();
	function getPlotSelectedParams(){
		var params = {};
		$("#adv_putin_guide .box-content .plot_order span.plot:hidden").each(function(){
			var plotType = $(this).data("contentClass");
			if("plot_org" == plotType){
				var orgs = $("#guide_adv_putin_plot_org .control-group").last().find("input[name='orgs']:checked").val();
				params.orgs=orgs;
			}else if("plot_industry" == plotType){
				var industrys = $("#guide_adv_putin_plot_industry .control-group").last().find("input[name='industrys']:checked").val();
				params.industrys=industrys;
			}else if("plot_week" == plotType){
				var days = $("#guide_adv_putin_plot_day .control-group").last().find("input[name='days']:checked").val();
				params.days=days;
			}else if("plot_time" == plotType){
				var times = $("#guide_adv_putin_plot_time .control-group").last().find("input[name='times']:checked").val();
				params.times=times;
			}
		});
		console.debug(params);
		return params;
// 		var selectedAuthTypes = [];
// 		$("#auth_type_setting input[name='selected_row']:checked").each(function(){
// 			selectedAuthTypes.push($(this).val());
// 		});
// 		var data = $.param({shopId:getSelectedShopId(),selectedAuthTypes:selectedAuthTypes},true);
	}
</script>