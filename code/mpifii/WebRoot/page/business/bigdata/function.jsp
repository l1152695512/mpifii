<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="../../common/splitPage.jsp" %> 
<style type="text/css">
</style>
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/bigdata/function" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="#">偏好对比分析</a>
			</li>
		</ul>
	</div>
	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i>偏好对比分析</h2>
				<div class="box-icon">
				</div>
			</div>
			<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-edit"></i>数据查询</h2>
				<div class="box-icon">
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-down"></i></a>
				</div>
			</div>
			<div class="box-content" style="display:none;" >
				<fieldset>
				 	<div class="control-group">
					  		<label class="control-label" for="focusedInput">组织：</label>
							<div class="controls">
							  	<select id="select_org" multiple="multiple" >
									<c:forEach var="org" items="${orgList}">
										<option value="${org.id}">${org.name}</option>
									</c:forEach>
								</select>
								<input name="qOrgId" id="qOrgId" value="${splitPage.queryParam.org_id}"  type="hidden" />
							</div>
							<label class="control-label" for="focusedInput">商铺：</label>
							<div class="controls">
							  	<select id="select_shop" multiple="multiple" >
									<option value="">--请先选择组织--</option>
								</select>
								<input name="_query.shop_id" id="qShopId" value="${splitPage.queryParam.shop_id}" type="hidden" />
								</div>
								<label class="control-label" for="focusedInput">开始日期：</label>
								<div class="controls">
									<input type="text" id="starttime" name="starttime" value="${startDate}" readonly="readonly" class="input-xlarge datepicker" />
								</div>
								<label class="control-label"for="focusedInput">结束日期：</label>
								<div class="controls">
									<input type="text"   id="endtime"  name="endtime" value="${endDate}"  readonly="readonly" class="input-xlarge datepicker" />
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
			
			<div class="box-content">
				<div>
					<div id="chartdivv"></div>
				</div>
			</div>
			</div>
		</div>
	</div>
	
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
							<th onclick="orderbyFun('phone')">用户手机号码</th>
							<th onclick="orderbyFun('typename')">用户类型</th>
							<th onclick="orderbyFun('count')">点击次数</th>
							<th onclick="orderbyFun('startime')">访问时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="user" items="${splitPage.page.list}" >
							<tr>
								<td>
								${fn:substring(fn:substring(user.get("phone"),fn:length(user.get("phone"))-11,-1),0,6)}*****
								</td>
								<td class="center">${user.get("typename")}</td>
								<td class="center">${user.get("count")}</td>
								<td class="center">${user.get("startime")}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div id="splitPageDiv" class="pagination pagination-centered"></div>
			</div>
		</div>
		<!--/span-->
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
	
 function to_function(){
	 var starttime=$("#starttime").val();
		var endtime=$("#endtime").val();
		var qOrgId=$("#qOrgId").val();
		var qShopId=$("#qShopId").val();
			var myChart = new FusionCharts('file/charts/MSColumn3D.swf?ChartNoDataText=无数据显示', 'ad_chart_1', "100%", 410);
				var starttime=$("#starttime").val();
				var endtime=$("#endtime").val();
				var endtime=$("#endtime").val();
					$.ajax({
			    		type: "POST",
			    		dataType: 'text',
			    		data:{starttime:starttime,endtime:endtime,qOrgId:qOrgId,qShopId:qShopId},
			    		url: "business/bigdata/big_function?starttime="+starttime+"&endtime="+endtime,
			    		success: function(data,status,xhr){
			    			if(status == "success"){
			    				console.log(data);
			    				myChart.setDataXML(data);
								myChart.render("chartdivv");
			    			}
			    		}
			    	});
	};
	function getPFCharts(){
		var starttime=$("#starttime").val();
		var endtime=$("#endtime").val();
		var qOrgId=$("#qOrgId").val();
		var qShopId=$("#qShopId").val();
		var myChart = new FusionCharts('file/charts/MSColumn3D.swf?ChartNoDataText=无数据显示', 'ad_chart_2014', "100%", 410);
			$.ajax({
	    		type: "POST",
	    		dataType: 'text',
	    		data:{starttime:starttime,endtime:endtime,qOrgId:qOrgId,qShopId:qShopId},
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
$('#select_org').multiselect({
	enableFiltering: true,
	maxHeight: 150,
	onChange: function(){
		var checkId = $("#select_org").val();
		$("#qOrgId").attr("value",checkId);
		var url ="/business/shop/getShopByOrg?orgids="+checkId;
		$.ajax({
			type : 'POST',
			dataType : "json",
			url :encodeURI(encodeURI(cxt + url)),
			success : function(data) {
				$("#select_shop").multiselect('dataprovider',data);
			}
		});
	}
});
$('#select_shop').multiselect({
		enableFiltering: true,
		maxHeight: 150,
		onChange: function(){
			var checkId = $("#select_shop").val();
			$("#qShopId").attr("value",checkId);
		}
});
/* var dataXml = $("#dataXml").val();
console.log(dataXml);
var myChart = new FusionCharts('file/charts/MSColumn3D.swf?ChartNoDataText=无数据显示', 'ad_chart_', "100%", 120);
myChart.setDataXML(dataXml);
myChart.render("chartdivv"); */
</script>