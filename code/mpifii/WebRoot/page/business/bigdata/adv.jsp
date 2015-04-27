<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<style type="text/css">
</style>
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/bigdata/toUserTypes" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="#">广告分析展示</a>
			</li>
		</ul>
	</div>
		<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-edit"></i> 应用查询</h2>
				<div class="box-icon">
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-down"></i></a>
				</div>
			</div>
			<div class="box-content" style="display: none;" >
				<fieldset>
				 	<div class="control-group">
					  		<label class="control-label" for="focusedInput">开始日期：</label>
							<div class="controls">
								<input type="text" id="starttime" name="starttime" readonly="readonly" class="input-xlarge datepicker" ">
							</div>
							<label class="control-label" for="focusedInput">结束日期：</label>
							<div class="controls">
								<input type="text" name="endtime" readonly="readonly" class="input-xlarge datepicker" ">
							</div>
					  	</div>
					  	<div class="form-actions">
							<button type="button" class="btn btn-primary" onclick="to_user_type()">查询</button>
							<button type="reset" class="btn">清除</button>
					  	</div>
				</fieldset>
			</div>
		</div><!--/span-->
	</div><!--/row-->
	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i>广告分析展示</h2>
				<div class="box-icon">
				</div>
			</div>
			<div class="box-content">
				<div >
					<div id="chartdivv"></div>
				</div>
			</div>
		</div>
	</div>
</form>
<!-- <script type="text/javascript">
		var myChart = new FusionCharts('file/charts/MSColumn3D.swf', 'ad_chart_2014', "100%", 620);
		myChart.setDataXML("<chart palette='5' caption='广告分析展示' numberprefix='' rotatevalues='1' placevaluesinside='1' legendshadow='0' legendborderalpha='0' legendbgcolor='FFFFFF' showborder='0'><categories><category label='2014/12/3' /><category label='2014/12/4' /><category label='2014/12/5' /><category label='2014/12/6' /></categories><dataset seriesname='广告一' color='836FFF' showvalues='1'><set value='24114' /><set value='18426' /><set value='774' /><set value='5542' /><set value='4426' /></dataset><dataset seriesname='广告二' color='0000EE' showvalues='1'><set value='12561' /><set value='8244' /><set value='14426' /><set value='4272' /><set value='9926' /></dataset><dataset seriesname='广告三' color='008B00' showvalues='1'><set value='24141' /><set value='426' /><set value='7426' /><set value='4242' /><set value='424426' /></dataset><dataset seriesname='广告四' color='8B2323' showvalues='1'><set value='12461' /><set value='14482' /><set value='14226' /><set value='14242' /><set value='42416' /></dataset></chart>");
		myChart.render("chartdivv");
</script>
 -->
 
<script type="text/javascript">
		$(document).ready(function() {
			getPFCharts();
		});
		function getPFCharts(){
			var myChart = new FusionCharts('file/charts/MSColumn3D.swf', 'ad_chart_2014', "100%", 410);
				$.ajax({
		    		type: "POST",
		    		dataType: 'text',
		    		data:{},
		    		url: "business/bigdata/big_adv",
		    		success: function(data,status,xhr){
		    			if(status == "success"){
		    				console.log(data);
		    				myChart.setDataXML(data);
							myChart.render("chartdivv");
		    			}
		    		}
		    	});
		};
</script>