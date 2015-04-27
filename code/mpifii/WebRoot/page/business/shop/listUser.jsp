<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="box" id="choice_user_list">
	<div class="box-header well" >
		<div class="box-icon">
			<a href="javascript:void(0);" class="btn btn-round" style="width: 85px;" title="添加用户" onclick="addUserFromShop();"><i class="icon-plus-sign"></i><span>添加新用户</span></a>
		</div>
	</div>
	<div class="box-content">
		<table class="table table-striped table-bordered bootstrap-datatable ">
			<thead>
				<tr>
					<th width="15px"></th>
					<th>用户名</th>
				</tr>
			</thead>
			<tbody id="user_name_list">
<%-- 				<c:choose> --%>
<%-- 					<c:when test="${empty userList}"> --%>
<!-- 						<tr><td colspan="2"><font color="red">暂无用户！</font></td></tr> -->
<%-- 					</c:when> --%>
<%-- 					<c:otherwise> --%>
<%-- 						<c:forEach var="user" items="${userList}"> --%>
<!-- 							<tr> -->
<%-- 								<td><input type="radio" name="select_user" value="${user.id}" /></td> --%>
<%-- 								<td>${user.name}</td> --%>
<!-- 							</tr> -->
<%-- 						</c:forEach> --%>
<%-- 					</c:otherwise> --%>
<%-- 				</c:choose> --%>
			</tbody>
		</table>
		<div class="holder"></div>
	</div>
</div>
<script type="text/javascript">
	$("#choice_user_list .holder").jPages({
		containerID:"user_name_list",
		realPagination:true,
		perPage: 5,
	    serverParams:{
	    	url:"business/shop/getUserName",
	    	generDataHtml:generUserNameData
	    }
	});
	
	function generUserNameData(data,searchParams){
		var recHtml = "";
		if(data.length > 0){
			for(var i=0;i<data.length;i++){
				recHtml += '<tr>'+
								'<td><input type="radio" name="select_user" value="'+data[i].id+'"/></td>'+
								'<td>'+data[i].name+'</td>'+
							'</tr>';
			}
		}else{
			recHtml = '<tr><td colspan="2"><font color="red">暂无用户！</font></td></tr>';
		}
		$("#user_name_list").html(recHtml);
	}
	function addUserFromShop(){
		closePop();
		ajaxContent('/system/user/addOrModify',{},addUserCallback);
	}
	
</script>