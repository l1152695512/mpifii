<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<<<<<<< HEAD
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="box" id="choice_adv_list">
=======

<div class="box" id="choice_role_list">
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
	<div class="box-content">
		<table class="table table-striped table-bordered bootstrap-datatable ">
			<thead>
				<tr>
<<<<<<< HEAD
					<th width="15px"><input type="checkbox" name="select_all_adv"/></th>
					<th>广告位名称</th>
					<th>广告描述</th>
				</tr>
			</thead>
			<tbody id="adv_type_list">
			<c:if test="${fn:length(advs) == 0}">
				<tr><td colspan="2"><font color="red">暂无可授权广告！</font></td></tr>
			</c:if>
			<c:if test="${fn:length(advs) > 0}">
				<c:forEach var="adv" items="${advs}">
				<tr>
					<td><input type="checkbox" <c:if test="${adv.checked=='1'}">checked</c:if> name="selected_adv" value="${adv.id}"/></td>
					<td>${adv.adv_name}</td>
					<td>${adv.des}</td>
				</tr>
				</c:forEach>
			</c:if>
			</tbody>
=======
					<th width="15px"></th>
					<th>角色</th>
				</tr>
			</thead>
			<tbody id="role_list"></tbody>
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
		</table>
		<div class="holder"></div>
	</div>
</div>
<script type="text/javascript">
<<<<<<< HEAD
	$("#choice_adv_list .holder").jPages({
		containerID:"adv_type_list",
		perPage: 5
	});
	$("#choice_adv_list input[name='select_all_adv']").click(function(){
		if($(this).is(':checked')){
			$("#choice_adv_list input[name='selected_adv']").attr("checked",true);
		}else{
			$("#choice_adv_list input[name='selected_adv']").removeAttr("checked");
		}
		$(this).blur();
		$(this).focus();
	});
	function submitAdvSetting(){
		var selectedAdvs = [];
		$("#choice_adv_list input[name='selected_adv']:checked").each(function(){
			selectedAdvs.push($(this).val());
		});
		var data = $.param({orgId:"${orgId}",selectedAdvs:selectedAdvs},true);
		$.ajax({
			type : 'POST',
			url :"business/oper/adv/permission/save",
			data:data,
			success : function(data) {
				closePop();
				ajaxContent('/business/oper/adv/permission');
			}
		});
=======
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
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
	}
</script>