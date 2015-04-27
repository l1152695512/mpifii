<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ include file="../../common/splitPage.jsp" %> 

<style type="text/css">
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

<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/device" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="javascript:void(0);" onclick="ajaxContent('/business/device/index');">盒子管理</a>
			</li>
		</ul>
	</div>
	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i>盒子列表</h2>
				<div class="box-icon">
					<a href="javascript:void(0);" class="btn btn-round" title="查找"><i class="icon-search"></i></a>
					<a href="javascript:void(0);" class="btn btn-round" title="添加盒子" onclick="ajaxContent('/business/device/addOrModify');"><i class="icon-plus-sign"></i></a>
					<a href="javascript:void(0);" class="btn btn-round" title="导入盒子" onclick="importDevice()"><i class="icon-random"></i></a>
				</div>
			</div>
			<div class="box-content">
				<div class="search-content"><!-- 这里必须取名为search-content，否则搜索框离开焦点的点击事件不会触发搜索框的隐藏 -->
					<fieldset><!-- 这下面搜索的字段要与数据库里表的字段对应 -->
					  	<div class="control-group">
							<label class="control-label" for="focusedInput">SN：</label>
							<div class="controls">
						  		<input class="input-xlarge focused" type="text" id="routerSn" name="_query.router_sn_like" value='${splitPage.queryParam.router_sn_like}' maxlength="20" >
							</div>
					  	</div>
					  
					  	<div class="control-group">
							<label class="control-label">商铺名称：</label>
							<div class="controls">
						  		<input class="input-xlarge" type="text" id="shopName" name="_query.name" value='${splitPage.queryParam.name}' maxlength="40" >
							</div>
					  	</div>
					
					  	<div class="form-actions">
							<button type="button" class="btn btn-primary" onclick="splitPage(1);">查询</button>
							<button type="reset" class="btn" onclick="clearform()">清除</button>
					  	</div>
					</fieldset>
				</div>
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
							<th>用户名</th>
							<th>商铺名称</th>
							<th>盒子名称</th>
							<th>盒子类型</th>
							<th>认证时长（分钟）</th>
							<th>SN</th>
							<th>注册时间</th>
							<th width="220">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="device" items="${splitPage.page.list}" >
							<tr>
								<td>${device.get("user_name")}</td>
								<td class="center">
									${device.get("shop_name")}
								</td>
								<td class="center">${device.get("name")}</td>
								<td class="center">${device.get("type")==1?'route':'AP'}</td>
								<td class="center">${device.get("time_out")}</td>
								<td class="center">${device.get("router_sn")}</td>
								<td class="center">${device.get("create_date")}</td>
								<td class="center" data-id='${device.get("id")}'>
									<a class="btn btn-info" href="javascript:void(0);"> <i class="icon-edit icon-white"></i> 编辑</a>
									<a class="btn btn-danger" href="javascript:void(0);"> <i class="icon-trash icon-white"></i> 删除</a>
									<a class="btn btn-default btn-sm"  href="javascript:void(0);"><i class="icon-wrench icon-black"></i> 升级</a>
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
	$("#splitPage .search-content").hide();
	$("#splitPage .search-content").click(function(){
		return false;//禁止该事件冒泡
	});
	$("#splitPage .box-header .box-icon .icon-search").parent().click(function(){
		var obj = $("#splitPage .search-content");
		if(obj.is(':hidden')){
			$("#splitPage .search-content").slideDown();
			return false;//禁止该事件冒泡
		}
	});
	$("#splitPage .box-content tbody .icon-edit").parent().click(function(){
		var id = $(this).parent().data("id");
		ajaxContent('/business/device/addOrModify',{id:id});
	});
	$("#splitPage .box-content tbody .icon-trash").parent().click(function(){
		var id = $(this).parent().data("id");
		myConfirm("确定要删除该数据？",function(){
			$.ajax({
				type: "POST",
				url: "business/device/delete",
				data: {id:id},
				success: function(data,status,xhr){
					ajaxContent('/business/device');
				}
			});
		});
	});
	$("#splitPage .box-content tbody .icon-wrench").parent().click(function(){
		var id = $(this).parent().data("id");
		$.ajax({
			type: "POST",
			url: "business/device/checkUpdate",
			data: {id:id},
			success: function(data){
				if(data.state==0){
					myConfirm("确定对选中的盒子进行升级？",function(){
						$.ajax({
							type: "POST",
							url: "business/device/deviceUpdate",
							data: {id:id},
							success: function(rep){
								if(rep.status==0 || rep.status==1){
									myAlert("升级成功!");
								}else{
									myAlert("升级失败,请稍后再试!");
								}
							},
							error: function(){
								myAlert("升级失败,请稍后再试!");
							}
						});
					});
				}else if(data.state==1){
					myAlert("对不起，您的盒子不在线，当前不能进行升级操作！");
				}else{
					myAlert("对不起，当前不能进行升级操作！");
				}
			},
			error: function(data){
				myAlert("对不起，当前不能进行升级操作！");
			}
		});
	});
	/***
	盒子导入
	**/
	function importDevice(){
		$.fn.SimpleModal({
			title: '盒子导入',
			width: 750,
	        keyEsc:true,
			buttons: [{
				text:'确定',
	    		classe:'btn primary btn-margin',
	    		clickEvent:function() {
	    			$("#device_import_form").ajaxSubmit({
	    				success : function(data) {
	    	   	  	    		if(data.state == "success"){
	    	   	  	    			if(data.msg!="导入成功"){
		    	   	  	    			myAlert("导入成功，但有警告!",function(){
			  							});
	    	   	  	    				$("#device_import_form_noticeDiv").empty().html(data.msg);
	    	   	  	    			}else{
		    	   	  	    			myAlert("导入成功!",function(){
		    								closePop();
		    							});
	    	   	  	    			}
	    	   	  	    			
	    	   	  	    		}else if(data.state == 'errorTxt'){
		    	   	  	    		myAlert("导入失败!",function(){
		  							});
	    	   	  	    			$("#device_import_form_noticeDiv").empty().html(data.msg);
	    	   	  	    		}else if(data.state == "fail"){
		    	   	  	    		myAlert("请选择要导入的xls文件或dbf文件!",function(){
		  								closePop();
		  							});
	    	   	   		        }else if(data.state == "Onlyfail"){
		    	   	   		      	myAlert("只能导入xls文件或dbf文件!",function(){
		  								closePop();
		  							});
	    	   	   		        }else if(data.state == "noComplete"){
		    	   	   		      	myAlert("数据导入不完全，请检查模板后重新进行导入!",function(){
		  								closePop();
		  							});
	    	   	   		        }else{
			    	   	   		    myAlert("导入出错!",function(){
		  							});
			    	   	   		$("#device_import_form_noticeDiv").empty().html(data.msg);
	    	   	   		        }
	    				}
	    			});
	    		}
	    	},
	    	{
	    		text:'取消',
	    		classe:'btn secondary'
	    	}
			],
			param: {
				url: cxt+'/business/device/importFile'
			}
		}).showModal();
	}
	function clearform(){
		$("#routerSn").val("");
		$("#shopName").val("");
	}
</script>