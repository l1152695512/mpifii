<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="../../common/splitPage.jsp" %> 
<form id="splitPage" class="form-horizontal" action="${cxt}/business/statistics/toCustomer" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="javascript:void(0);" onclick="ajaxContent('/business/statistics/toCustomer');">运营分析－客户分析</a>
			</li>
		</ul>
	</div>
	<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-edit"></i> 客户信息查询</h2>
				<div class="box-icon">
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-down"></i></a>
				</div>
			</div>
			<div class="box-content" style="display: none;" >
				<fieldset>
				 	<div class="control-group">
					  		<label class="control-label" for="focusedInput">开始日期：</label>
							<div class="controls">
								<input type="text" id="startDate" name="_query.startDate" value="${splitPage.queryParam.startDate}" readonly="readonly" class="input-xlarge datepicker" />
							</div>
							<label class="control-label" for="focusedInput">结束日期：</label>
							<div class="controls">
								<input type="text" id="endDate" name="_query.endDate" value="${splitPage.queryParam.endDate}"  readonly="readonly" class="input-xlarge datepicker" />
							</div>
							<label class="control-label" for="focusedInput">组织：</label>
								<div class="controls">
									<input name="_query.org_id" id="qOrgId" value="${splitPage.queryParam.org_id}"  type="hidden" />
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
									<input class="input-xlarge focused" name="_query.shop_id" id="qShopId" type="hidden" onclick="selectShop(this)" value="${splitPage.queryParam.shop_id}"  />
								</div>
							<label class="control-label" for="focusedInput">卡类型：</label>
							<div class="controls">
							  	<select id="select_type" name="_query.cardtype" >
							  		<option value="">--请选择--</option>
							  		<option value="移动" <c:if test="${splitPage.queryParam.cardtype == '移动'}">selected="selected"</c:if>>移动</option>
							  		<option value="联通" <c:if test="${splitPage.queryParam.cardtype == '联通'}">selected="selected"</c:if>>联通</option>
							  		<option value="电信" <c:if test="${splitPage.queryParam.cardtype == '电信'}">selected="selected"</c:if>>电信</option>
							  		<option value="其他" <c:if test="${splitPage.queryParam.cardtype == '其他'}">selected="selected"</c:if>>其他</option>
								</select>
							</div>
							<label class="control-label" for="focusedInput">手机号码</label>
							<div class="controls">
								<input  class="input-xlarge focused" style="width: 150PX" type="text" name="_query.phone" value="${splitPage.queryParam.phone}" vMin="0" vType="phone" onchange="onblurVali(this);"/>
								<span class="help-inline"></span>
							</div>
					  	</div>
					  	<div class="form-actions">
							<button type="button" class="btn btn-primary" onclick="splitPage(1);">查询</button>
							<button type="button" class="btn btn-primary" onclick="resertForm();">重置</button>
					  	</div>
				</fieldset>
			</div>
		</div><!--/span-->
	</div><!--/row-->
	<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-edit"></i> 分析报表</h2>
				<div class="box-icon">
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
				</div>
			</div>
			<div class="box-content" >
				<input id="dataXml" value="${dataXml}" type="hidden" />
				<fieldset>
				 	<div class="control-group">
				 		<div id="chartdivv"></div>
				 	</div>
				</fieldset>
			</div>
		</div><!--/span-->
	</div><!--/row-->
	<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-user"></i>客户信息列表</h2>
				<div class="box-icon" style="display:${displayVal}">
					<a href="javascript:down('${pageContext.request.contextPath}')" class="btn btn-round" title="导出"><i class="icon-download"></i></a>
				</div>
			</div>
			<div class="box-content">
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
							<th >序号</th>
							<th onclick="orderbyFun('orgname')">组织</th>
							<th onclick="orderbyFun('shopname')">商户</th>
							<th onclick="orderbyFun('auth_type')">认证类型</th>
							<th onclick="orderbyFun('info')">用户</th>
							<th onclick="orderbyFun('cardtype')">卡类型</th>
							<th onclick="orderbyFun('address')">归属地</th>
							<th onclick="orderbyFun('mac')">mac地址</th>
							<th onclick="orderbyFun('ftutype')">终端</th>
							<th onclick="orderbyFun('auth_date')">认证时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="custo" varStatus="stat" items="${splitPage.page.list}" >
							<tr>
								<td class="center">${stat.count}</td>
								<td class="center">${custo.get("orgname")}</td>
								<td class="center">${custo.get("shopname")}</td>
								<td class="center">
									<c:choose>
									    <c:when test="${custo.get('auth_type')=='phone'}">
									        手机认证
									    </c:when>
									    <c:when test="${custo.get('auth_type')=='weixin'}}">
									        微信认证
									    </c:when>   
									    <c:otherwise>
									        一键认证
									    </c:otherwise>  
    								</c:choose>
								</td>
								<td class="center">
									<c:choose>
									    <c:when test="${custo.get('auth_type')=='phone'}">
									         ${fn:substring(fn:substring(custo.get("info"),fn:length(custo.get("info"))-11,-1),0,6)}*****
									    </c:when>
									    <c:otherwise>
									        暂无
									    </c:otherwise>  
    								</c:choose>
								</td>
								<td class="center">
									<c:choose>
									    <c:when test="${custo.get('cardtype')==null}">
									         未知
									    </c:when>
									    <c:otherwise>
									        ${custo.get("cardtype")}
									    </c:otherwise>  
    								</c:choose>
								</td>
								<td class="center">
									<c:choose>
									    <c:when test="${custo.get('address')==null}">
									         未知
									    </c:when>
									    <c:otherwise>
									        ${custo.get("address")}
									    </c:otherwise>  
    								</c:choose>
								</td>
								<td class="center">
								${fn:substring(custo.get("mac"),0,3)}****${fn:substring(custo.get("mac"),14,17)}
								</td>
								<td class="center">
									<c:choose>
									    <c:when test="${custo.get('ftutype')==null}">
									         未知
									    </c:when>
									    <c:otherwise>
									        ${custo.get("ftutype")}
									    </c:otherwise>  
    								</c:choose>
								</td>
								<td class="center">
									<fmt:formatDate value='${custo.get("auth_date")}' pattern='yyyy-MM-dd HH:mm:ss' />
								</td>
							</tr>	
						</c:forEach>
					</tbody>
				</table>
				<div id="splitPageDiv" class="pagination pagination-centered"></div>
			</div>
		</div>
		<!--/span-->
	</div>
	<!--/row-->
</form>	
<script type="text/javascript">
$('#select_shop').multiselect({
		enableFiltering: true,
		maxHeight: 150,
		onChange: function(){
			var checkId = $("#select_shop").val();
			$("#qShopId").attr("value",checkId);
		}
	});
 var dates = $("#startDate,#endDate");
dates.datepicker({
	maxDate:0,
    onSelect: function(selectedDate){
       var option = this.id == "startDate"?"minDate" : "maxDate";
       dates.not(this).datepicker("option", option, selectedDate);
    }
}); 
	var dataXml =$("#dataXml").val();
	var myChart = new FusionCharts('file/charts/MSColumn3D.swf', 'ad_chart_2014', "100%",500);
	myChart.setDataXML(dataXml);
	myChart.render("chartdivv"); 
	function resertForm(){
		$(':input','#splitPage')   
		.not(':button, :submit, :reset, :hidden')   
		.val('')   
		.removeAttr('checked')   
		.removeAttr('selected');  
		ajaxContent('/business/statistics/toCustomer');
	}
	
	function down(ctx){
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var orgId = $('#qOrgId').val();
		var shopId = $('#qShopId').val();
		var type = encodeURI($('#select_type').val());
		var phone = $("input[name='_query.phone']").val();
		var url=ctx+'/business/statistics/downCustStaFile?startDate='+startDate+'&endDate='+endDate+'&orgId='+orgId+"&shopId="+shopId+"&phone="+phone+"&type="+encodeURI(type);
		window.location.href=url;
	}
</script>
