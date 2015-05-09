<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="modal-body">
  	<div class="control-group">
		<label class="control-label" for="focusedInput">商铺名称：</label>
		<div class="controls">
	  		<input class="input-xlarge focused" type="text" name="shopName" value='' maxlength="20" >
		</div>
		<button type="button" class="btn btn-primary" onclick="queryShop(this);">查询</button>
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
				<select id="allShop" multiple size="18"  ondblclick="addChecked(this);" >
					<c:forEach items="${shopList}" var="shop">
						<option value="${shop.id}">${shop.name}(${shop.username})</option>
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
				<select id="checkedShop" multiple size="18" ondblclick="delChecked(this);" >
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
	    $('#allShop option:selected').appendTo('#checkedShop');
	});
	//移到左边
	$('#remove').click(function() {
	    $('#checkedShop option:selected').appendTo('#allShop');
	});
	//全部移到右边
	$('#add_all').click(function() {
	    //获取全部的选项,删除并追加给对方
	    $('#allShop option').appendTo('#checkedShop');
	});
	//全部移到左边
	$('#remove_all').click(function() {
	    $('#checkedShop option').appendTo('#allShop');
	});
	function addChecked(sNode){
		var index = sNode.selectedIndex;
		var option = sNode.options[index];
		option.selected = false;
		document.getElementById("checkedShop").add(option);
	}
	
	function delChecked(sNode){
		var index = sNode.selectedIndex;
		var option = sNode.options[index];
		option.selected = false;
		document.getElementById("allShop").add(option);
	}
	function queryShop(){
		var name  = $("input[name='shopName']").val();
		 $.ajax({
				type: "post",
				dataType: "json",
				data:{name:name},
				url: "${cxt}/business/shop/findByName",
				success: function(data){
					if(data.length==0){
						$("#allShop").empty();
						$("#checkedShop").empty();
					}else{
						$("#allShop").empty();
						for(var i=0;i<data.length;i++){
							$("#allShop").append("<option value='"+data[i].id+"'>"+data[i].name+"("+data[i].username+")"+"</option>");  
						}
					}
				}
			});
	}
	function queryRest(){
		 $("input[name='shopName']").attr("value","");
		 $.ajax({
				type: "post",
				dataType: "json",
				data:{name:name},
				url: "${cxt}/business/shop/findByName",
				success: function(data){
					if(data.length==0){
						$("#allShop").empty();
						$("#checkedShop").empty();
					}else{
						$("#allShop").empty();
						for(var i=0;i<data.length;i++){
							$("#allShop").append("<option value='"+data[i].id+"'>"+data[i].name+"("+data[i].username+")"+"</option>");  
						}
					}
				}
			});
	}
</script>