<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
.settd{
	border-collapse: collapse;
	border-spacing: 0; 
	width:99%; 
	font-size:12px; 
	border-top:1px solid #E8F0FB; 
	border-left:1px solid #E8F0FB; 
	margin-top:5px;
}
.settd td{
	padding:5px;
	border-right:1px solid #d7e2ea;
	border-bottom:1px solid #d7e2ea;
	color:#666;
}
.settd th{
	text-align:left;
	border-right:1px solid #d7e2ea;
	border-bottom:1px solid #d7e2ea;
	padding:5px;
	background:#e5f2f8;
	color:#666;
}
</style>
<form id="device_import_form" method="post" action="${cxt}/business/device/saveImport" enctype="multipart/form-data">
	<table class="settd" align="center">
		<tr>
			<th width="100">模板下载</th>
			<td><a href="#" onclick="download_model()"><font color="red">设备导入模板.xls</font></a></td>
		</tr>
		<tr>
			<th>数据导入</th>
			<td><input type="file" name="daoru" ></td>
		</tr>
	</table>
</form>	
<fieldset style="height: 150px;padding: 4px;">
	<legend>说明</legend>
	<div id="device_import_form_noticeDiv" style="height: 99%;width: 99%;overflow: scroll;"></div>
</fieldset>
<script type="text/javascript">
function download_model(){
	window.location.href  ='<%=request.getContextPath()%>/templates/device.xls';
}
</script>
