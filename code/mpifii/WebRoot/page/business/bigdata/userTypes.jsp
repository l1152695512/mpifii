<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%-- <%@ include file="../../common/splitPage.jsp" %> 
 --%>
<style type="text/css">
</style>
<form id="splitPage" class="form-horizontal"
	action="${pageContext.request.contextPath}/business/bigdata/user_preference"
	method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);"
				onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li><a href="#">用户偏好分析</a></li>
		</ul>
	</div>
	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well">
				<h2>
					<i class="icon-user"></i>用户偏好分析
				</h2>
				<div class="box-icon"></div>
			</div>

			<div class="row-fluid sortable">
				<div class="box span12">
					<div class="box-header well" data-original-title>
						<h2>
							<i class="icon-edit"></i>数据查询
						</h2>
						<div class="box-icon">
							<a href="#" class="btn btn-minimize btn-round"><i
								class="icon-chevron-down"></i></a>
						</div>
					</div>
					<div class="box-content" style="display: none;">
						<fieldset>
							<div class="control-group">
					  		<label class="control-label" for="focusedInput">开始日期：</label>
							<div class="controls">
								<input type="text" id="startDate" name="startDate" value="${splitPage.queryParam.startDate}" readonly="readonly" class="input-xlarge datepicker" />
							</div>
							<label class="control-label" for="focusedInput">结束日期：</label>
							<div class="controls">
								<input type="text" id="endDate" name="endDate" value="${splitPage.queryParam.endDate}"  readonly="readonly" class="input-xlarge datepicker" />
							</div>
							<label class="control-label" for="focusedInput">组织：</label>
								<div class="controls">
									<input name="org_id" id="qOrgId" value="${splitPage.queryParam.org_id}"  type="hidden" />
									<input class="input-xlarge focused" style="width: 150PX" id="org_id" type="text" value="" onclick="selectOrg(this)"  />
								</div>
								<div id="orgSelect_Div">
								</div>
							<label class="control-label" for="focusedInput">商铺：</label>
								<div class="controls">
									<select id="select_shop" multiple="multiple" >
										<c:forEach var="shop" items="${shopList}">
											<option value="${shop.id}">${shop.name}</option>
										</c:forEach>
									</select>
									<input class="input-xlarge focused" name="shop_id" id="qShopId" type="hidden" onclick="selectShop(this)" value="${splitPage.queryParam.shop_id}"  />
								</div>
					  	</div>
					  	<div class="form-actions">
							<button type="button" class="btn btn-primary" onclick="splitPage(1);">查询</button>
							<button type="button" class="btn btn-primary" onclick="resertForm();">重置</button>
					  	</div>
						</fieldset>
					</div>
				</div>
				<!--/span-->
			</div>
			<!--/row-->
			<div class="row-fluid sortable">
				<div class="box span12">
					<div class="box-header well" data-original-title>
						<h2>
							<i class="icon-edit"></i> 分析报表
						</h2>
						<div class="box-icon">
							<a href="#" class="btn btn-minimize btn-round"><i
								class="icon-chevron-up"></i></a>
						</div>
					</div>
					<div class="box-content">
						<input id="pieXml" value="${pieXml}" type="hidden" />
						<fieldset>
							<div class="control-group">
								<div id="chartPieXml"></div>
							</div>
						</fieldset>
					</div>
				</div>
				<!--/span-->
			</div>
			<!--/row-->
			<div class="box-content">
				<input id="lineXml" value="${lineXml}" type="hidden" />
				<fieldset>
					<div>
						<div id="chartLineXml"></div>
					</div>
				</fieldset>
			</div>
			<div class="row-fluid sortable"></div>
		</div>
	</div>
</form>
<script type="text/javascript">
	var dates = $("#startDate,#endDate");
	dates.datepicker({
		maxDate:0,
	    onSelect: function(selectedDate){
	    	if(this.id == "startDate"){  
	            // 如果是选择了开始时间（startDate）设置结束时间（endDate）的最小时间和最大时间  
	            option = "minDate"; //最小时间  
	            var selectedTime = getTimeByDateStr(selectedDate);  
	            var minTime = selectedTime;  
	        //最小时间 为开第一个日历控制选择的时间  
	            targetDate = new Date(minTime);   
	            //设置结束时间的最大时间  
	            optionEnd = "maxDate";  
	        //因为只能做三天内的查询  所以是间隔2天  当前时间加上2*24*60*60*1000  
	            targetDateEnd = new Date();  
	          }else{  
	            // 如果是选择了结束时间（endDate）设置开始时间（startDate）的最小时间和最大时间  
	            option = "maxDate"; //最大时间  
	            var selectedTime = getTimeByDateStr(selectedDate);  
	            var maxTime = selectedTime;  
	            targetDate = new Date(maxTime);  
	            //设置最小时间   
	            optionEnd = "minDate";  
	            targetDateEnd = new Date(maxTime-7*24*60*60*1000);  
	          }  
	          dates.not(this).datepicker("option", option, targetDate);    
	          dates.not(this).datepicker("option", optionEnd, targetDateEnd);    
	    }
	});
	//根据日期字符串取得其时间  
	function getTimeByDateStr(dateStr){  
	    var year = parseInt(dateStr.substring(0,4));  
	    var month = parseInt(dateStr.substring(5,7),10)-1;  
	    var day = parseInt(dateStr.substring(8,10),10);  
	    return new Date(year, month, day).getTime();  
	}  
	var pieXml = $("#pieXml").val();
	var myChart = new FusionCharts('file/charts/Pie3D.swf?ChartNoDataText=无数据显示', 'ad_chart_'+generRandomCharacters(10), "100%", 410);
	myChart.setDataXML(pieXml);
	myChart.render("chartPieXml");
	var lineXml = $("#lineXml").val();
	var lineChart = new FusionCharts('file/charts/MSColumn3D.swf?ChartNoDataText=无数据显示', 'aa_chart_'+generRandomCharacters(10), "100%", 410);
	lineChart.setDataXML(lineXml);
	lineChart.render("chartLineXml");
	$('#select_shop').multiselect({
		enableFiltering: true,
		maxHeight: 150,
		onChange: function(){
			var checkId = $("#select_shop").val();
			$("#qShopId").attr("value",checkId);
		}
	});
</script>