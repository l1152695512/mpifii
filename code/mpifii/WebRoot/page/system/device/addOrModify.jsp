<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">
	.sn_style{
		font-size: 18px;
		font-weight: bold;
		line-height: 28px;
	}
</style>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
		<li><a href="#" onclick="ajaxContent('/system/warehouse');">设备管理</a><span class="divider">/</span></li>
	</ul>
</div>
<div class="row-fluid">
	<div class="box span12">
		<div class="box-header well">
			<h2>
				<i class="icon-edit"></i>
				<c:choose>
					<c:when test="${empty warehouseInfo.id}">添加设备</c:when>
					<c:otherwise>修改设备</c:otherwise>
				</c:choose>
			</h2>
		</div>
		<div class="box-content">
			<form class="form-horizontal" id="editForm" action="${cxt}/system/warehouse/save" method="POST">
				<input type="hidden" name="wareHouse.id" value="${warehouseInfo.id}">
				<div class="control-group">
					<label class="control-label">SN</label>
					<div class="controls">
						<c:choose>
							<c:when test="${empty warehouseInfo.id}">
								<input type="text" name="wareHouse.sn"
									class="input-xlarge" autocomplete="off" maxlength="16" vMin='15'
									vType="letterNumber" placeholder='15或16位0-9或a-f' onblur="onblurVali(this);">
							</c:when>
							<c:otherwise>
								<span class="sn_style">${warehouseInfo.sn}</span>
							</c:otherwise>
						</c:choose>
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">类型</label>
					<div class="controls">
						<select name="wareHouse.type" class="input-xlarge">
							<option value="1" <c:if test="${warehouseInfo.type == '1'}">selected="selected"</c:if>>route</option>
							<option value="2" <c:if test="${warehouseInfo.type == '2'}">selected="selected"</c:if>>AP</option>
						</select>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">所属平台</label>
					<div class="controls">
						<select name="wareHouse.plat_no" class="input-xlarge">
							<option value="-1" >待定</option>
							<c:forEach var="plat" items="${platList}" >
								<option value="${plat.plat_no }" <c:if test="${warehouseInfo.plat_no==plat.plat_no }">selected="selected"</c:if>>${plat.name }[${plat.plat_no }]</option>
							</c:forEach>	
						</select>
					</div>
				</div>
				<div class="form-actions">
					<button class="btn btn-primary" type="button" onclick="submitInfo(this.form);">提交</button>
					<button class="btn" type="button"  onclick="ajaxContent('/system/warehouse');">取消</button>
				</div>
			</form>
		</div>
	</div>
	<!--/span-->
</div>
<!--/row-->
<script type="text/javascript">
// 	$("#editForm input[name='wareHouse.sn']").focusout(function(){
// 		var sn = $(this).val();
// 		if(sn.match(/[0-9a-f]{16}/gi)  == null){
// 			setInputError($(this),"16位0-9或a-f！");
// 		}else{
// 			hiddenInputColor($(this));
// 			showInputColor($(this), "success");
// 			$(this).next(".help-inline").text("");
// 		}
// 	});
	function submitInfo(form) {
		var errorCount = formVali(form);
		if(errorCount != 0){
			return;
		}
		$("#editForm").ajaxSubmit({
			success : function(resp) {
				if("snRepeat"==resp.error){
					setInputError($("#editForm input[name='wareHouse.sn']"),"该SN已存在！");
				}else{
					ajaxContentReturn(resp);
				}
			}
		});
	}
</script>