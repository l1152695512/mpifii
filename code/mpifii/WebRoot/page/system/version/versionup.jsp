<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a><span class="divider">/</span></li>
		<li><a href="#" onclick="">版本升级</a><span class="divider">/</span></li>
<!-- 		<li><a href="#"></a></li> -->
	</ul>
</div>
<div class="row-fluid">
	<div class="box span12">
		<div class="box-header well">
			<h2>
				<i class="icon-edit"></i>
				版本信息
<!-- 				<font style="color: red"> -->
<%-- 				<c:if test="${info==null}"> --%>
<!-- 					(上次升级版本:初始版本) -->
<%-- 				</c:if> --%>
<%-- 				<c:if test="${info!=null}"> --%>
<%-- 					(上次升级版本:${info.version}) --%>
<%-- 				</c:if> --%>
<!-- 				</font> -->
			</h2>
		</div>
		<div class="box-content">
			<form class="form-horizontal" id="editForm" action="${cxt}/business/sysupdate/save" method="POST">
				<div class="control-group">
					<label class="control-label">升级类型</label>
					<div class="controls">
						<select name="sysupdate.file_type" id="upType" onclick="simOptionClick5IE()"  onchange="change(this)" class="combox">
							<option value="script" onclick="showVersionValue( this )" >系统(rouer)</option>
							<option value="scriptAp" onclick="showVersionValue( this )" >系统(AP)</option>
							<option value="data" onclick="showVersionValue( this )" >TF卡(rouer)</option>
						</select>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">上次版本号</label>
					<div class="controls">
						<input type="text" id="lastVserion"  value="" readonly="true" class="input-xlarge">
						<input type="hidden" name="sysupdate.file_type" id="file_type"  value="${sysupdate.file_type}" >
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">升级版本号</label>
					<div class="controls">
						<input type="text" name="sysupdate.version" id="version"  value="${sysupdate.version}" class="input-xlarge"
							maxlength="16" vMin="1" vType="numberZX" placeholder='正数（可包含小数点）' onblur="onblurVali(this);" >
						<span class="help-inline">每次升级所填写版本号必须大于上次升级版本号</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">升级版本包</label>
					<div class="controls">
						<input  type="file" name="versionPack" id="versionPack" />
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">升级范围</label>
					<div class="controls">
						<select name="sysupdate.type" id="upScope" onclick="simOptionClick4IE()"  class="combox">
							<option value="1" selected="selected">全部</option>
							<option value="2" onclick="showOptionValue( this )" >商铺</option>
							<option value="3" onclick="showOptionValue( this )" >盒子</option>
						</select>
						<span class="help-inline">选择'全部'后针对全部盒子进行升级，选择商铺后针对单个或多个商铺进行升级，选择盒子后针对单个或多个盒子升级</span>
						<input id="selectId" name="selectId"  type="hidden" value="" />
					</div>
				</div>
				<div class="form-actions">
					<button class="btn btn-primary" type="button"  onclick="submitInfo(this.form);">确定</button>
					<button class="btn" type="button"  onclick="ajaxContentReturn();">取消</button>
				</div>
			</form>
		</div>
	</div>
	<!--/span-->
</div>
<script type="text/javascript">
showVersionValue();
function simOptionClick4IE(){  
    var evt=window.event  ;  
    var selectObj=evt?evt.srcElement:null;  
    // IE Only  
    if (evt && selectObj &&  evt.offsetY && evt.button!=2  
        && (evt.offsetY > selectObj.offsetHeight || evt.offsetY<0 ) ) {  
              
            // 记录原先的选中项  
            var oldIdx = selectObj.selectedIndex;  
  
            setTimeout(function(){  
                var option=selectObj.options[selectObj.selectedIndex];  
                // 此时可以通过判断 oldIdx 是否等于 selectObj.selectedIndex  
                // 来判断用户是不是点击了同一个选项,进而做不同的处理.  
                showOptionValue(option);
                showVersionValue(option);
            }, 60);  
    }  
}  
function simOptionClick5IE(){  
    var evt=window.event  ;  
    var selectObj=evt?evt.srcElement:null;  
    // IE Only  
    if (evt && selectObj &&  evt.offsetY && evt.button!=2  
        && (evt.offsetY > selectObj.offsetHeight || evt.offsetY<0 ) ) {  
              
            // 记录原先的选中项  
            var oldIdx = selectObj.selectedIndex;  
  
            setTimeout(function(){  
                var option=selectObj.options[selectObj.selectedIndex];  
                // 此时可以通过判断 oldIdx 是否等于 selectObj.selectedIndex  
                // 来判断用户是不是点击了同一个选项,进而做不同的处理.  
                showVersionValue(option);
            }, 60);  
    }  
} 
function showOptionValue(opt,msg){  
	var val = $("#upScope").val();
	if(val==2 ){
		$.fn.SimpleModal({
			title: '选择商铺升级',
			width: 550,
	        keyEsc:true,
			buttons: [{
	    		text:'确定',
	    		classe:'btn primary btn-margin',
	    		clickEvent:function() {
	    			var checkedRole = document.getElementById("checkedShop");
	    			var length = checkedRole.length;
	    			var shopIds = "";
	    			for (var i=0; i<length; i++){
	    				shopIds += checkedRole.options[i].value + ",";
	    		    }
	    			$("#selectId").attr("value",shopIds.substring(0,shopIds.length-1));
	    			closePop();
	            }
	    	},{
	    		text:'取消',
	    		classe:'btn secondary'
	    	}],
			param: {
				url: '${cxt}/business/sysupdate/shopUpgrade'
			}
		}).showModal();
	}else if( val==3){
		var deviceType = "1";
		var val = $("#upType").val();
		if(val=='scriptAp'){
			deviceType = "2";
		}
		$.fn.SimpleModal({
			title: '选择盒子升级',
			width: 650,
	        keyEsc:true,
			buttons: [{
	    		text:'确定',
	    		classe:'btn primary btn-margin',
	    		clickEvent:function() {
	    			var checkedRole = document.getElementById("checkedDevice");
	    			var length = checkedRole.length;
	    			var shopIds = "";
	    			for (var i=0; i<length; i++){
	    				shopIds += checkedRole.options[i].value + ",";
	    		    }
	    			$("#selectId").attr("value",shopIds.substring(0,shopIds.length-1));
	    			closePop();
	            }
	    	},{
	    		text:'取消',
	    		classe:'btn secondary'
	    	}],
			param: {
				url: '${cxt}/business/sysupdate/deviceUpgrade',
				data:{"deviceType":deviceType}
			}
		}).showModal();
	}
}  
$('#version').blur(function(){
	var version = Number($("#version").val());
	var fileType = $("#upType").val();
	 $.ajax({
		type: "post",
		dataType: "json",
		data:{version:version,fileType:fileType},
		url: "${cxt}/business/sysupdate/checkVersion",
		success: function(result){
			if(result.state == "success"){
				hiddenInputColor($('#version'));
				showInputColor($('#version'), "success");
	       	}else{
	       		hiddenInputColor($('#version'));
				showInputColor($('#version'), "error");
				$('#version').next(".help-inline").text(result.msg);
	       	}
		}
	});
});
function submitInfo(form){

	var controlGroupDiv = $('#version').parent().parent();
	var text = controlGroupDiv.attr("class");
	if(text.indexOf("success")<0){
		return;
	}
	if($("#versionPack").val()==""){
		myAlert("请选择升级包");
		return;
	}
	var errorCount = formVali(form);
	if(errorCount != 0){
		return;
	}
	
	var selectId = $("#selectId").val();
	var upScope = $("#upScope").val();
	if(upScope == '2' && selectId==''){
		myAlert("请选择商铺");
		return;
	}
	if(upScope == '3' && selectId==''){
		myAlert("请选择盒子");
		return;
	}
	
	myConfirm("版本升级不可逆，确定升级？",function(){
		$("#editForm").ajaxSubmit({
		     success: function(resp){
		  	   myAlert("升级成功");
		     },
		     error: function( err ){
		  	   myAlert("升级失败");
		     }
		});
	});
}

function showVersionValue(opt,msg){  
	var upType = $("#upType").val();
	$.ajax({
		type: "post",
		dataType: "json",
		data:{upType:upType},
		url: "${cxt}/business/sysupdate/showUpVserion",
		success: function(result){
			if(result.state == "success"){
				$('#lastVserion').val(result.msg);
	       	}else{
				$('#lastVserion').val(result.msg);
	       	}
		}
	});
	
}


function change(){
	$("#upScope").val(1);
}
</script>