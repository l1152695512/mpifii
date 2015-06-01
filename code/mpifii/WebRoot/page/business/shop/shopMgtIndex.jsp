<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ include file="../../common/splitPage.jsp" %> 

<style type="text/css">
	#splitPage .search-content{
		position: absolute;
		width: 98%;
		background-color: white;
		border: 1px solid #DDDDDD;
		-webkit-box-shadow: #666 0px 0px 10px;
		-moz-box-shadow: #666 0px 0px 10px;
		box-shadow: #666 0px 0px 10px;
		padding-top: 20px;
	}
	#splitPage .box-content{
		position: relative;
	}
	#splitPage .form-actions{
		margin-top: 0px;
		margin-bottom: 0px;
	}
</style>
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/business/shop/index" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="javascript:void(0);" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
			<li>
				<a href="javascript:void(0);" onclick="ajaxContent('/business/shop/index');">商铺管理</a>
			</li>
		</ul>
	</div>
	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2><i class="icon-user"></i> 商铺列表</h2>
				<div class="box-icon">
					<a href="javascript:void(0);" class="btn btn-round" title="查找"><i class="icon-search"></i></a>
					<a href="javascript:void(0);" class="btn btn-round" title="添加商铺" onclick="ajaxContent('/business/shop/add');"><i class="icon-plus-sign"></i></a>
<<<<<<< HEAD
					<a href="javascript:void(0);" class="btn btn-round" title="导入商铺" onclick="importShop()"><i class="icon-random"></i></a>
=======
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
				</div>
			</div>
			<div class="box-content">
				<div class="search-content"><!-- 这里必须取名为search-content，否则搜索框离开焦点的点击事件不会触发搜索框的隐藏 -->
					<fieldset><!-- 这下面搜索的字段要与数据库里表的字段对应 -->
					  	<div class="control-group">
							<label class="control-label" for="focusedInput">商铺名称：</label>
							<div class="controls">
						  		<input class="input-xlarge focused" type="text" name="_query.name_like" value='${splitPage.queryParam.name_like}' maxlength="20" >
							</div>
					  	</div>
					  	<div class="form-actions">
							<button type="button" class="btn btn-primary" onclick="splitPage(1);">查询</button>
							<button type="reset" class="btn">清除</button>
					  	</div>
					</fieldset>
				</div>
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
<<<<<<< HEAD
							<th>编号</th>
							<th>商铺名称</th>
							<th>商铺位置</th>
							<th>联系方式</th>
						    <th>所属客户</th>
							<th>所属商户</th>
							<th>所属组织</th>
=======
							<th>商铺名称</th>
							<th>商铺位置</th>
							<th>联系方式</th>
							<th>所属商户</th>
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
<!-- 							<th>所属组</th> -->
							<th>设备数</th>
							<th width="320">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="shop" items="${splitPage.page.list}" >
							<tr>
<<<<<<< HEAD
								<td>${shop.get("sn")}</td>
								<td>${shop.get("name")}</td>
								<td class="center">${shop.get("location")}</td>
								<td  class="center">${shop.get("tel")}</td>
								<td>${shop.get("customer")}</td>
								<td class="center">${shop.get("username")}</td>
								<td class="center">${shop.get("orgName")}</td>
=======
								<td>${shop.get("name")}</td>
								<td class="center">${shop.get("location")}</td>
								<td  class="center">${shop.get("tel")}</td>
								<td class="center">${shop.get("username")}</td>
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
<%-- 								<td class="center">${shop.get("groupName")}</td> --%>
								<td class="center">${shop.get("sbs")}</td>
								<td class="center" data-id='${shop.get("id")}', data-user='${shop.get("userid")}'>
									<a class="btn btn-info" href="javascript:void(0);">  <i class="icon-edit icon-white"></i> 编辑</a>
									<a class="btn btn-danger" href="javascript:void(0);"> <i class="icon-trash icon-white"></i> 删除</a>
									<a class="btn btn-default btn-sm"  href="javascript:void(0);"> <i class="icon-wrench icon-black"></i>盒子管理</a>
									<a class="btn btn-success" href="javascript:void(0);"> <i class="icon-zoom-in icon-white"></i>查看</a>
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
		ajaxContent('/business/shop/edit',{id:id});
	});
	$("#splitPage .box-content tbody .icon-trash").parent().click(function(){
		var id = $(this).parent().data("id");
		var url='/business/shop/delete?id='+id;
		myConfirm("确定删除吗？",function(){
			$.ajax({
				type : 'POST',
				dataType : "json",
				url :encodeURI(encodeURI(cxt + url)),
				success : function(data) {
					ajaxContent("/business/shop");
// 					if(eval(data).state=='error'){
// 						myAlert("删除失败！",function(){
// 							var url = "/business/shop";
// 							ajaxContent(url);
// 						});
// 					}else{
// 						myAlert("删除成功！",function(){
// 							var url = "/business/shop";
// 							ajaxContent(url);
// 						});
						
// 					}
				}
			});
		});
	});
	
	$("#splitPage .box-content tbody .icon-wrench").parent().click(function(){
		var id = $(this).parent().data("id");
		var userid = $(this).parent().data("user");
		var url ="/business/shop/checkOwner?id="+id;
		$.ajax({
			type : 'POST',
			dataType : "json",
			url :encodeURI(encodeURI(cxt + url)),
			success : function(data) {
				if(eval(data).state=='error'){
					myAlert("请先绑定商户再进行此操作！")
				}else{
					$.fn.SimpleModal({
						title: '盒子管理',
<<<<<<< HEAD
// 						width: 750,
=======
						width: 750,
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
				        keyEsc:true,
						buttons: [{
				    		text:'确定',
				    		classe:'btn primary btn-margin',
				    		clickEvent:function() {
				    			var shopId = $("#info_id").val();
				    	    	var ids = "";
				    	    	$("input[name='device_checkselect_list']:checked").each(function(obj) {//遍历所有选中状态的checkBox
				    				var id = $(this).val();
				    				ids += id + ",";
				    			});
				    	    	if (ids != '') {
				    	    		ids = ids.substr(0,ids.length-1);
				    	    	}
				    			$.ajax({
				    					type : 'POST',
				    					url : cxt + '/business/shop/saveShopAssignedDevice?ids='+ids+'&shopId='+shopId,
				    					success : function(data) {
				    						myAlert("配置成功!",function(){
				    							closePop();
				    							ajaxContent("/business/shop/index");
				    						});
				    					}
				    			});
				    			
				            }
				    	},{
				    		text:'取消',
				    		classe:'btn secondary'
				    	}],
						param: {
							url: 'business/shop/configDevice?id='+id+"&userid="+userid
						}
					}).showModal();
					/* ajaxDiaLog('/business/shop/configDevice',{id:id,userid:userid}); */
				}
			}
		});
	});
<<<<<<< HEAD
	/***
	盒子导入
	**/
	function importShop(){
		$.fn.SimpleModal({
			title: '商铺导入',
			width: 750,
	        keyEsc:true,
			buttons: [{
				text:'确定',
	    		classe:'btn primary btn-margin',
	    		clickEvent:function() {
	    			$("#shop_import_form").ajaxSubmit({
	    				success : function(data) {
	    	   	  	    		if(data.state == "success"){
	    	   	  	    			myAlert("导入成功!",function(){
	    								closePop();
	    							});
	    	   	  	    		}else if(data.state == 'errorTxt'){
		    	   	  	    		myAlert("导入失败!",function(){
		  							});
	    	   	  	    			$("#shop_import_form_noticeDiv").empty().html(data.msg);
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
			    	   	   		$("#shop_import_form_noticeDiv").empty().html(data.msg);
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
				url: cxt+'/business/shop/importFile'
			}
		}).showModal();
	}
	
=======
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
	$("#splitPage .box-content tbody .icon-zoom-in").parent().click(function(){
		var id = $(this).parent().data("id");
		var userid = $(this).parent().data("user");
		ajaxContent('/business/shop/view',{id:id,userid:userid});
	});
</script>