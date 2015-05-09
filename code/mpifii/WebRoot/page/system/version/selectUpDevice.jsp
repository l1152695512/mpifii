<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="modal-body">
	<div class="control-group">
		<label class="control-label" for="focusedInput">设备SN：</label>
		<div class="controls">
	  		<input class="input-xlarge focused" type="text" name="deviceName" value='' maxlength="20" >
		</div>
		<button type="button" class="btn btn-primary" onclick="queryDevice(this);">查询</button>
		<button type="reset" class="btn" onclick="queryRest(this);">清除</button>
  	</div>
	<div class="row-fluid sortable">
		<div class="box span6">
			<div class="box-header well" data-original-title>
				<h2>
					<i class="icon-user"></i>未选择
				</h2>
				<div class="box-icon">
					
				</div>
			</div>
			<div class="box-content">
				<select id="allDevice" style="width: 275px" multiple size="18" ondblclick="addChecked(this);" >
					<c:forEach items="${deviceList}" var="device">
						<option value="${device.id}">${device.router_sn}(${device.sname}--${device.name})</option>
					</c:forEach>
			  	</select>
			</div>
			<div align="center">
				<button type="button" class="btn btn-default btn-sm" id="add" >选中添加到右边</button>
				<button type="button" class="btn btn-default btn-sm" id="add_all">全部添加到右边</button>
			</div>
		</div>
		
		<div class="box span6">
			<div class="box-header well" data-original-title>
				<h2>
					<i class="icon-user"></i>已选择
				</h2>
				<div class="box-icon">
					
				</div>
			</div>
			<div class="box-content">
				<select id="checkedDevice" style="width: 275px" multiple size="18" ondblclick="delChecked(this);" >
			  	</select>
			</div>
			<div align="center">
				<button type="button" class="btn btn-default btn-sm" id="remove" >选中删除到左边</button>
				<button type="button" class="btn btn-default btn-sm" id="remove_all" >全部删除到左边</button>
       	 	</div>
		</div>
	</div>  
</div>
<script type="text/javascript">
	//移到右边
	$('#add').click(function() {
	//获取选中的选项，删除并追加给对方
	    $('#allDevice option:selected').appendTo('#checkedDevice');
	});
	//移到左边
	$('#remove').click(function() {
	    $('#checkedDevice option:selected').appendTo('#allDevice');
	});
	//全部移到右边
	$('#add_all').click(function() {
	    //获取全部的选项,删除并追加给对方
	    $('#allDevice option').appendTo('#checkedDevice');
	});
	//全部移到左边
	$('#remove_all').click(function() {
	    $('#checkedDevice option').appendTo('#allDevice');
	});
	function addChecked(sNode){
		var index = sNode.selectedIndex;
		var option = sNode.options[index];
		option.selected = false;
		document.getElementById("checkedDevice").add(option);
	}

	function delChecked(sNode){
		var index = sNode.selectedIndex;
		var option = sNode.options[index];
		option.selected = false;
		document.getElementById("allDevice").add(option);
	}
	function queryDevice(){
		var name  = $("input[name='deviceName']").val();
		 $.ajax({
				type: "post",
				dataType: "json",
				data:{name:name,deviceType:'${deviceType}'},
				url: "${cxt}/business/device/findByName",
				success: function(data){
					if(data.length==0){
						$("#allDevice").empty();
						$("#checkedDevice").empty();
					}else{
						$("#allDevice").empty();
						for(var i=0;i<data.length;i++){
							$("#allDevice").append("<option value='"+data[i].id+"'>"+data[i].router_sn+"("+data[i].sname+"--"+data[i].name+")"+"</option>");  
						}
					}
				}
			});
	}
	function queryRest(){
		 $("input[name='deviceName']").attr("value","");
		 $.ajax({
				type: "post",
				dataType: "json",
				data:{name:name,deviceType:'${deviceType}'},
				url: "${cxt}/business/device/findByName",
				success: function(data){
					if(data.length==0){
						$("#allDevice").empty();
						$("#checkedDevice").empty();
					}else{
						$("#allDevice").empty();
						for(var i=0;i<data.length;i++){
							$("#allDevice").append("<option value='"+data[i].id+"'>"+data[i].router_sn+"("+data[i].sname+"--"+data[i].name+")"+"</option>");
						}
					}
				}
			});
	}
</script>