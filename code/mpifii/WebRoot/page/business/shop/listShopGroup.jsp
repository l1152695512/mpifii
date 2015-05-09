<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="box" id="choice_shop_group_list">
	<div class="box-content">
		<table class="table table-striped table-bordered bootstrap-datatable ">
			<thead>
				<tr>
					<th width="15px"></th>
					<th>分组名称</th>
				</tr>
			</thead>
			<tbody id="shop_group_list">
			</tbody>
		</table>
		<div class="holder"></div>
	</div>
</div>
<script type="text/javascript">
	$("#choice_shop_group_list .holder").jPages({
		containerID:"shop_group_list",
		realPagination:true,
		perPage: 5,
	    serverParams:{
	    	url:"business/shop/getShopGroup",
	    	data: {userId:"${userId}"},
	    	generDataHtml:generShopGroupData
	    }
	});
	
	function generShopGroupData(data,searchParams){
		var recHtml = "";
		if(data.length > 0){
			for(var i=0;i<data.length;i++){
				var selected = "";
				if(i==0){
					selected = "checked";
				}
				recHtml += '<tr>'+
								'<td><input type="radio" '+selected+' name="select_group" value="'+data[i].id+'"/></td>'+
								'<td>'+data[i].name+'</td>'+
							'</tr>';
			}
		}else{
			recHtml = '<tr><td colspan="2"><font color="red">暂无分组！</font></td></tr>';
		}
		$("#shop_group_list").html(recHtml);
	}
	
</script>