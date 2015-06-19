<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style type="text/css">
	#shop_device_manage .box-content{
		position: relative;
	}
	#shop_device_manage .search-content{
		position: absolute;
		z-index: 10;
		width: 96%;
		background-color: white;
		border: 1px solid #DDDDDD;
		-webkit-box-shadow: #666 0px 0px 10px;
		-moz-box-shadow: #666 0px 0px 10px;
		box-shadow: #666 0px 0px 10px;
		padding-top: 20px;
	}
	#shop_device_manage .form-actions{
		margin-top: 0px;
		margin-bottom: 0px;
		padding: 0;
		text-align: center;
		padding-top: 10px;
	}
	#shop_device_manage .control-group{
		text-align: center;
	}
	#shop_device_manage .controls{
		display: inline-block;
	}
	#shop_device_manage .form-horizontal{
		margin: 0 0 0 0;
	}
	#shop_device_manage table{
		width: 500px;
	}
</style>
<div id="shop_device_manage" class="box">
	<div class="box-header well">
		<h2>设备列表</h2>
		<div class="box-icon">
			<a href="javascript:void(0);" class="btn btn-round" title="查找"><i class="icon-search"></i></a>
		</div>
	</div>
	<div class="box-content">
		<div class="search-content"><!-- 这里必须取名为search-content，否则搜索框离开焦点的点击事件不会触发搜索框的隐藏 -->
			<fieldset><!-- 这下面搜索的字段要与数据库里表的字段对应 -->
				<form action="" method="post">
				  	<div class="control-group">
						<label class="control-label" for="focusedInput">SN：</label>
						<div class="controls">
					  		<input class="input-xlarge focused" type="text" name="sn" value="" maxlength="20"/>
						</div>
				  	</div>
				  	<div class="form-actions">
						<input type="button" class="btn btn-primary" onclick="getUnassignedDevice();" value="确定"/>
<!-- 						<input type="reset" class="btn" value="清除11"/> -->
				  	</div>
			  	</form>
			</fieldset>
		</div>
		<form class="form-horizontal" action="" method="post">
			<input type="hidden" id="info_id" value="${infoid}" />
			<div class="box-content">
				<table class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
							<th width="15px"></th>
							<th width="30px">序号</th>
							<th>设备名称</th>
							<th>设备SN</th>
						</tr>
					</thead>
					<tbody id="shop_device_manage_data_list">
					</tbody>
				</table>
				<div class="holder"></div>
			</div>
		</form>
	</div>
</div>
<script type="text/javascript">
	$("#shop_device_manage .search-content").hide();
	$("#shop_device_manage .search-content").click(function(){
		return false;//禁止该事件冒泡
	});
	$("#shop_device_manage .box-header .box-icon .icon-search").parent().click(function(){
		var obj = $("#shop_device_manage .search-content");
		if(obj.is(':hidden')){
			$("#shop_device_manage .search-content").slideDown();
			return false;//禁止该事件冒泡
		}
	});
	getUnassignedDevice();
	function getUnassignedDevice(){
		$("#shop_device_manage .search-content").slideUp();//隐藏搜索框
		var sn = $("#shop_device_manage input[name='sn']").val();
		$("#shop_device_manage .holder").jPages({
			containerID:"shop_device_manage_data_list",
			perPage : 5,
			startPage:1,
	        serverParams:{
	        	url:"business/shop/getUnassignedDevice",
	        	data:{shopId:"${infoid}",sn:sn},
	        	generDataHtml:generUnassignedDeviceDataList
	        }
		});
	}
	function generUnassignedDeviceDataList(data,searchParams){
		var recHtml = "";
		if(data.length > 0){
			for(var i=0;i<data.length;i++){
				recHtml += '<tr>';
				if("1"==data[i].isMe){
					recHtml += '<td><input type="checkbox" checked="checked" name="device_checkselect_list" value="'+data[i].id+'" /></td>';
				}else{
					recHtml += '<td><input type="checkbox" name="device_checkselect_list" value="'+data[i].id+'" /></td>';
				}
				recHtml += '<td>'+(i+1)+'</td>'+
							'<td>'+data[i].name+'</td>'+
							'<td class="center">'+data[i].router_sn+'</td>'+
						'</tr>';
			}
		}else{
			recHtml = '<tr>'+
							'<td colspan="4" style="text-align: center;"><font color="red">暂无可配置设备！</font></td>'+
						'</tr>';
		}
		$("#shop_device_manage_data_list").html(recHtml);
	}
	
//     function submitInfo(){
//     	var shopId = $("#info_id").val();
//     	var ids = "";
//     	$("input[name='device_checkselect_list']:checked").each(function(obj) {//遍历所有选中状态的checkBox
// 			var id = $(this).val();
// 			ids += id + ",";
// 		});
//     	if (ids != '') {
//     		ids = ids.substr(0,ids.length-1);
//     	}
// 		$.ajax({
// 				type : 'POST',
// 				url : cxt + '/business/shop/saveShopAssignedDevice?ids='+ids+'&shopId='+shopId,
// 				success : function(data) {
// 					myAlert("配置成功!",function(){
// 						ajaxContent("/business/shop/index");
// 					});
// 				}
// 		});
//     };

</script>