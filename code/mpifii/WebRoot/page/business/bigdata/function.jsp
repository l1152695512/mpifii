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
				<a href="#">用户行为分析</a>
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
				<h2><i class="icon-user"></i>用户行为分析</h2>
				<div class="box-icon">
				</div>
			</div>
			<div class="box-content">
				<div>
					<div id="chartdivv"></div>
				</div>
			</div>
		</div>
	</div>
</form>

<!-- <script type="text/javascript">
		var myChart = new FusionCharts('file/charts/ScrollLine2D.swf', 'ad_chart_2014', "100%", 620);
		myChart.setDataXML("<chart caption='用户行为分析' subcaption='展示' xaxisname='月份' yaxisname='点击量' palette='3' bgcolor='FFFFFF' canvasbgcolor='66D6FF' canvasbgalpha='5' canvasborderthickness='1' canvasborderalpha='20' legendshadow='0' showvalues='0' alternatehgridcolor='ffffff' alternatehgridalpha='100' showborder='0' legendborderalpha='0' legendiconscale='1.5' divlineisdashed='1'><categories><category label='2014年9月' /><category label='2014年10月' /><category label='2014年11月' /><category label='2014年12月' /></categories><dataset seriesname='娱乐' color='48D1CC'><set value='22226' /><set value='32235' /><set value='77777' /><set value='127777' /></dataset><dataset seriesname='新闻报刊'><set value='226' /><set value='225' /><set value='999933' /><set value='212' /></dataset><dataset seriesname='购物达人' color='551A8B'><set value='2216' /><set value='2235' /><set value='1799' /><set value='121882' /></dataset><dataset seriesname='财经' color='666666'><set value='772216' /><set value='15' /><set value='71333' /><set value='2122' /></dataset><dataset seriesname='女性' color='66CD00'><set value='136' /><set value='222577' /><set value='233' /><set value='333212' /></dataset><dataset seriesname='旅行' color='6B8E23'><set value='221677' /><set value='5' /><set value='3' /><set value='5214' /></dataset><dataset seriesname='教育' color='8B0A50'><set value='7772216' /><set value='223577' /><set value='2223333' /><set value='2128888' /></dataset><dataset seriesname='其他' color='CD00CD'><set value='2' /><set value='5521335' /><set value='33' /><set value='21213331' /></dataset><styles><definition><style name='captionFont' type='font' size='15' /></definition><application><apply toobject='caption' styles='captionfont' /></application></styles></chart>");
		myChart.render("chartdivv");
</script> -->

<script type="text/javascript">
		$(document).ready(function() {
			getPFCharts();
		});
		
		function getPFCharts(){
			var myChart = new FusionCharts('file/charts/ScrollLine2D.swf', 'ad_chart_2014', "100%", 410);
				$.ajax({
		    		type: "POST",
		    		dataType: 'text',
		    		data:{},
		    		url: "business/bigdata/big_function",
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