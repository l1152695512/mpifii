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
				<a href="#">客流分析展示</a>
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
								<input type="text" name="userInfo.birthday" readonly="readonly" class="input-xlarge datepicker" ">
							</div>
							<label class="control-label" for="focusedInput">结束日期：</label>
							<div class="controls">
								<input type="text" name="userInfo.birthday" readonly="readonly" class="input-xlarge datepicker" ">
							</div>
							<label class="control-label" for="focusedInput">商铺：</label>
							<div class="controls">
							  	<select id="select1">
							  		<option value="">--请选择商铺--</option>
									<c:forEach var="shop" items="${shopList}">
										<option value="${shop.id}">${shop.name}</option>
									</c:forEach>
								</select>
								<input name="_query.shop_id.in" id="qShopId"  type="hidden" />
							</div>
							<label class="control-label" for="focusedInput">应用：</label>
							<div class="controls">
							  	<select id="select2" multiple="multiple" >
							  		<option value="">--请先选择商铺--</option>
								</select>
								<input name="_query.shop_id.in" id="qShopId"  type="hidden" />
							</div>
					  	</div>
					  	<div class="form-actions">
							<button type="button" class="btn btn-primary" onclick="splitPage(1);">查询</button>
							<button type="reset" class="btn">清除</button>
					  	</div>
				</fieldset>
			</div>
		</div><!--/span-->
	</div><!--/row-->
	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i>客流分析展示</h2>
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
		myChart.setDataXML("<chart palette='5' caption='客流分析展示' numberprefix='' rotatevalues='1' placevaluesinside='1' legendshadow='0' legendborderalpha='0' legendbgcolor='FFFFFF' showborder='0'><categories><category label='2014/12/3' /><category label='2014/12/4' /><category label='2014/12/5' /><category label='2014/12/6' /></categories><dataset seriesname='南昌' color='698B22' showvalues='1'><set value='42446' /><set value='24461' /><set value='82446' /><set value='42446' /></dataset><dataset seriesname='九江' color='545454' showvalues='1'><set value='42446' /><set value='24461' /><set value='1182446' /><set value='4222446' /></dataset><dataset seriesname='景德镇' color='32CD32' showvalues='1'><set value='42446' /><set value='1124461' /><set value='8222446' /><set value='4244633' /></dataset></chart>");
		myChart.render("chartdivv");
</script> -->

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
		    		url: "business/bigdata/big_flow",
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