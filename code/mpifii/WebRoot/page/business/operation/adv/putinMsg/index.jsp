<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ include file="../../../../common/splitPage.jsp" %> 

<style type="text/css">
	#splitPage table th,#splitPage table td{
		text-align: center;
	}
	#splitPage table td span{
		display: block;
		height: 55px;
		overflow: hidden;
	}
	#splitPage .search-content{
/* 		display: none; */
		position: absolute;
		width: 98%;
		background-color: white;
		border: 1px solid #DDDDDD;
		-webkit-box-shadow: #666 0px 0px 10px;
		-moz-box-shadow: #666 0px 0px 10px;
		box-shadow: #666 0px 0px 10px;
		padding-top: 20px;
/* 		border-right: 1px solid #DDDDDD; */
/* 		border-bottom: 1px solid #DDDDDD; */
	}
	#splitPage .box-content{
		position: relative;
	}
	#splitPage .form-actions{
		margin-top: 0px;
		margin-bottom: 0px;
	}
</style>
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/oper/adv/putinMsg" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider"></span></li>
		</ul>
	</div>

	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i>广告投放消息</h2>
<!-- 				<div class="box-icon"> -->
<!-- 					<a href="javascript:void(0);" class="btn btn-round putin_adv" style="width:90px;"><i class="icon-plus-sign"></i><span>投放广告</span></a> -->
<!-- 				</div> -->
			</div>
			<div class="box-content">
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
							<th style="width:50px;"><span>广告名称</span></th>
							<th style="width:50px;"><span>广告位</span></th>
							<th style="width:100px;"><span>投放行业</span></th>
							<th style="width:100px;"><span>投放时间</span></th>
							<th style="width:100px;"><span>开始时间</span></th>
							<th style="width:100px;"><span>结束时间</span></th>
							<th style="width:150px;"><span>操作</span></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="rowData" items="${splitPage.page.list}" >
							<tr>
								<td style="width:50px;"><span>${rowData.get("adv_name")}</span></td>
								<td style="width:50px;"><span>${rowData.get("spaces_name")}</span></td>
								<td style="width:100px;" title="${rowData.get("industrys")}"><span>${rowData.get("industrys")}</span></td>
								<td style="width:100px;" title="${rowData.get("weeks_times")}"><span>${rowData.get("weeks_times")}</span></td>
								<td style="width:100px;"><span>${rowData.get("start_date")}</span></td>
								<td style="width:100px;"><span>${rowData.get("end_date")}</span></td>
								<td data-id='${rowData.get("id")}' style="width:150px;">
								<c:if test="${rowData.get('status')=='0'}">
									<a class="btn btn-success" href="javascript:void(0);"><i class="icon-arrow-up icon-black"></i>投放</a>
									<a class="btn btn-default" href="javascript:void(0);"><i class="icon-trash icon-black"></i>忽略</a>
								</c:if>
								<c:if test="${rowData.get('status')=='1'}">
									已投放
								</c:if>
								<c:if test="${rowData.get('status')=='2'}">
									已忽略
								</c:if>
								<c:if test="${rowData.get('status')=='3'}">
									已下架
								</c:if>
								<c:if test="${rowData.get('status')=='4'}">
									已过期
								</c:if>
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
	$(function(){
		$("#splitPage .box-content tbody .icon-arrow-up").parent().click(function(){
			var dataVal = {id:$(this).parent().data("id")};
			myConfirm("确定要投放该广告？",function(){
		 		$.ajax({//检查是否可投放
		 			type : "POST",
		 			url : 'business/oper/adv/putinMsg/checkPutinError',
		 			data : dataVal,
		 			success : function(data) {
		 				if(undefined != data.msg && "" != data.msg){
		 					if(data.refresh == "1"){
		 						myAlert(data.msg,function(){
		 							ajaxContent('/business/oper/adv/putinMsg');
		 						});
		 					}else{
		 						myAlert(data.msg);
		 					}
		 				}else{
		 					$.fn.SimpleModal({
		 						title: '广告投放 - 区域选择',
		 						width: 600,
		 				        keyEsc:true,
		 						param: {
		 							url: 'business/oper/adv/putinMsg/choiceOrgs',
		 							data: dataVal
		 						},
		 						buttons: [{
		 				    		text:'确定',
		 				    		classe:'btn primary btn-margin',
		 				    		clickEvent:function() {
		 				    			if(!checkAdvPutinOrg()){
		 				    				var orgs = [];
			 				    			$("#adv_putin_org input[name='orgs']:checked").each(function(){
			 				    				orgs.push($(this).val());
			 				    			});
			 				    			var advPutinMsgId = $("#adv_msg_putin input[name='advPutinMsgId']").val();
			 				    			var orgData = $.param({orgs:orgs,id:advPutinMsgId},true);
			 				    			$.ajax({
		 				    					type : "POST",
		 				    					url : 'business/oper/adv/putinMsg/putin',
		 				    					data : orgData,
		 				    					success : function(data) {
		 				    						if(undefined != data.msg && "" != data.msg){
		 				   		 						myAlert(data.msg);
			 				   		 				}else{
		 				    							ajaxContent('/business/oper/adv/putinMsg');
		 				    							closePop();
		 				    						}
		 				    					}
		 				    				});
		 				    			}
		 				            }
		 				    	},{
		 				    		text:'取消',
		 				    		classe:'btn secondary'
		 				    	}]
		 					}).showModal();
		 				}
		 			}
		 		});
			});
		});
		
		$("#splitPage .box-content tbody .icon-trash").parent().click(function(){
			var data = {id:$(this).parent().data("id")};
			myConfirm("确定要忽略该广告？",function(){
				$.ajax({
					type : "POST",
					url : 'business/oper/adv/putinMsg/ignore',
					data : data,
					success : function(data) {
						ajaxContent('/business/oper/adv/putinMsg');
					}
				});
			});
		});
	});
</script>