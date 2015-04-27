<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
		<li><a href="#" onclick="ajaxContent('/system/res');">菜单管理</a><span class="divider">/</span></li>
<!-- 		<li><a href="#"></a></li> -->
	</ul>
</div>
<div class="row-fluid">
	<div class="box span12">
		<div class="box-header well">
			<h2>
				<i class="icon-edit"></i>
				<c:choose>
					<c:when test="${empty res.id}">添加菜单</c:when>
					<c:otherwise>修改菜单</c:otherwise>
				</c:choose>
			</h2>
		</div>
		<div class="box-content">
			<form class="form-horizontal" id="editForm" action="${cxt}/system/res/save" method="POST">
				<input type="hidden" name="res.id" value="${res.id}">
				<div class="control-group">
					<label class="control-label">菜单名称</label>
					<div class="controls">
						<input type="text" name="res.name" value="${res.name}" class="input-xlarge"
							maxlength="28" vMin="5" onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">菜单URL</label>
					<div class="controls">
						<input type="text" name="res.url" value="${res.url}" class="input-xlarge"
							maxlength="28" vMin="5" onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">排序</label>
					<div class="controls">
						<input type="text" name="res.seq" value="${res.seq}" class="input-xlarge"
							maxlength="28" vMin="5" onblur="onblurVali(this);">
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">菜单类型</label>
					<div class="controls">
						<label class="radio-inline">
						<input name="res.type" type="radio" value="1" />模块
						</label>
						<label class="radio-inline">
						<input name="res.type" type="radio" value="2" checked="checked" />菜单
						</label>
						<label class="radio-inline">
						<input name="res.type" type="radio" value="3" />按钮
						</label>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">是否可用</label>
					<div class="controls">
						<select name="res.useing" class="combox">
							<option value="1" <c:if test="${res.useing == '1'}">selected="selected"</c:if>>启用</option>
							<option value="0" <c:if test="${res.useing == '0'}">selected="selected"</c:if>>禁用</option>
						</select>
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">是否权限配置</label>
					<div class="controls">
						<label class="radio-inline">
							<input name="res.isgrantset" type="radio" value="1" checked="checked" />是
						</label>
						<label class="radio-inline">
							<input name="res.isgrantset" type="radio" value="0" />否
						</label>
					</div>
				</div>
				<div class="form-actions">
					<button class="btn btn-primary" type="button" onclick="submitInfo(this.form);">提交</button>
					<button class="btn" type="button"  onclick="ajaxContentReturn();">取消</button>
				</div>
			</form>
		</div>
	</div>
	<!--/span-->
</div>
<!--/row-->
<script type="text/javascript">
	function submitInfo(form) {
		var errorCount = formVali(form);
		if(errorCount != 0){
			return;
		}
		$("#editForm").ajaxSubmit({
			   success: function(resp){
		    	   myAlert("保存成功",function(){
		    		   ajaxContent('/system/res');
		    	   });
		       },
		       error: function( err ){
		    	   myAlert("保存失败",function(){
		    	   });
		       }
		});
	}
</script>