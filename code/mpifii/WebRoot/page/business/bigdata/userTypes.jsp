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
				<a href="#">用户偏好分析</a>
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
				<h2><i class="icon-user"></i>用户偏好分析</h2>
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
		myChart.setDataXML("<chart palette='5' caption='用户偏好分析' numberprefix='' rotatevalues='1' placevaluesinside='1' legendshadow='0' legendborderalpha='0' legendbgcolor='FFFFFF' showborder='0'><categories><category label='2014-12-08' /><category label='2014-12-09' /><category label='2014-12-10' /><category label='2014-12-11' /><category label='2014-12-12' /></categories><dataset seriesname='娱乐' color='D2691E' showvalues='1'><set value='1831137' /><set value='69833' /><set value='9338' /><set value='833345' /><set value='8195' /><set value='7684' /></dataset><dataset seriesname='新闻报刊' color='CD950C' showvalues='1'><set value='12446' /><set value='39235' /><set value='7891152' /><set value='114424' /><set value='324925' /><set value='2324328' /></dataset><dataset seriesname='购物达人' color='B23AEE' showvalues='1'><set value='77446' /><set value='555935' /><set value='1123452' /><set value='99993' /><set value='11493325' /><set value='224332338' /></dataset><dataset seriesname='财经' color='9A32CD' showvalues='1'><set value='112446' /><set value='223935' /><set value='223452' /><set value='224424' /><set value='492523' /><set value='43282222' /></dataset><dataset seriesname='女性' color='7A378B' showvalues='1'><set value='442446' /><set value='773935' /><set value='883452' /><set value='664424' /><set value='554925' /><set value='444328' /></dataset><dataset seriesname='旅行' color='1E90FF' showvalues='1'><set value='52446' /><set value='5533935' /><set value='5553452' /><set value='9924424' /><set value='4392588' /><set value='432877' /></dataset><dataset seriesname='教育' color='BF3EFF' showvalues='1'><set value='44445' /><set value='8839' /><set value='132452' /><set value='22441' /><set value='554925' /><set value='444328' /></dataset><dataset seriesname='其他' color='F1C7D2' showvalues='1'><set value='777777' /><set value='888888' /><set value='443427' /><set value='88888' /><set value='500000' /><set value='10000' /></dataset></chart>");
		myChart.render("chartdivv");
</script>
 -->

<script type="text/javascript">
		$(document).ready(function() {
			getPFCharts();
		});
		function to_user_type(){
			var starttime=$("#starttime").val();
			var endtime=$("#endtime").val();
			console.log(starttime);
				$.ajax({
		    		type: "GET",
		    		dataType: 'text',
		    		data:{},
		    		url: "business/bigdata/to_user_type?starttime="+starttime+"&endtime="+endtime,
		    		success: function(data,status,xhr){
		    			if(status == "success"){
		    				
		    			}
		    		}
		    	});
		};
		function getPFCharts(){
			var myChart = new FusionCharts('file/charts/MSColumn3D.swf', 'ad_chart_2014', "100%", 410);
				$.ajax({
		    		type: "POST",
		    		dataType: 'text',
		    		data:{},
		    		url: "business/bigdata/to_user_type",
		    		success: function(data,status,xhr){
		    			if(status == "success"){
		    				myChart.setDataXML(data);
							myChart.render("chartdivv");
		    			}
		    		}
		    	});
		};
</script>