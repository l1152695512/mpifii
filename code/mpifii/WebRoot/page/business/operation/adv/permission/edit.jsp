<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="box" id="choice_role_list">
	<div class="box-content">
		<table class="table table-striped table-bordered bootstrap-datatable ">
			<thead>
				<tr>
					<th width="15px"></th>
					<th>角色</th>
				</tr>
			</thead>
			<tbody id="role_list"></tbody>
		</table>
		<div class="holder"></div>
	</div>
</div>
<script type="text/javascript">
	$("#choice_role_list .holder").jPages({
		containerID:"role_list",
		realPagination:true,
		perPage: 5,
	    serverParams:{
	    	url:"business/operation/adv/permission/getRoleList",
	    	generDataHtml:generRolesData
	    }
	});
	
	function generRolesData(data,searchParams){
		var recHtml = "";
		if(data.length > 0){
			for(var i=0;i<data.length;i++){
				var checked = "";
				if(i==0 || data[i].id == "${roleId}"){
					checked = "checked";
				}
				recHtml += '<tr>'+
								'<td><input type="radio" '+checked+' name="selected_role" value="'+data[i].id+'"/></td>'+
								'<td>'+data[i].name+'</td>'+
							'</tr>';
			}
		}else{
			recHtml = '<tr><td colspan="2"><font color="red">暂无角色！</font></td></tr>';
		}
		$("#role_list").html(recHtml);
	}
</script>