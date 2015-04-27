<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<h3>设备列表</h3>
		</div>
		<div class="modal-body">
			<form id="splitPage" class="form-horizontal"
				action="${pageContext.request.contextPath}/business/shop/index"
				method="POST">
				<input type="hidden" id="info_id" value="${infoid}" />
<%-- 				<input type="hidden" id="info_type" value="${type}"> --%>
				<div class="row-fluid">
					<div class="box span12">
						<div class="box-content">
							<table
								class="table table-striped table-bordered bootstrap-datatable ">
								<thead>
									<tr>
										<th width="15px"></th>
										<th>设备名称</th>
										<th>设备SN</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<c:when test="${empty deviceList}">
											<tr>
												<td colspan="3"><font color="red">暂无可配置设备！</font></td>
											</tr>
										</c:when>
										<c:otherwise>
												<c:forEach var="device" items="${deviceList}">
														<tr>
															<td><input type="checkbox" <c:if test="${device.isMe==1}">checked="checked"</c:if> name="device_checkselect_list" value="${device.id}" /></td>
															<td>${device.name}</td>
															<td class="center">${device.router_sn}</td>
														</tr>
													</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">
    function submitInfo(){
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
						ajaxContent("/business/shop/index");
					});
				}
		});
    };

</script>