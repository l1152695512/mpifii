/***
 * 全局禁止回车事件
 */
window.onload = function (){   
	document.body.onkeydown=function(event){
		if(event.keyCode==13){
				event.keyCod=0; return false;
			}
		}
};
/**
 * 分页输出
 * @param totalRow
 * @param pageSize
 * @param pageNumber
 * @param totalPages
 * @param isSelectPage
 * @param isSelectSize
 * @param orderColunm
 * @param orderMode
 */
function splitPageOut(totalRow, pageSize, pageNumber, totalPages, isSelectPage, isSelectSize, orderColunm, orderMode){
	var splitStr = '<ul>';
	
	if (pageNumber == 1 || totalPages == 0) {
		splitStr += '<li><a href="javascript:void(0)">上一页</a></li>';
	} else {
		splitStr += '<li><a href="javascript:splitPage(' + (pageNumber - 1) + ');">上一页</a></li>';
	}
	
	for (var i = 1; i <= totalPages; i++) {
        if (i == 2 && pageNumber - 4 > 1) {
        	splitStr += '<li><a href="javascript:void(0)">...</a></li>';
            i = pageNumber - 4;
        } else if (i == pageNumber + 4 && pageNumber + 4 < totalPages) {
        	splitStr += '<li><a href="javascript:void(0)">...</a></li>';
            i = totalPages - 1;
        } else {
            if (pageNumber == i) {
            	splitStr += '<li class="active"><a href="javascript:void(0)" style="color: #272727; font-size: 14px; text-decoration: none;">' + pageNumber + '</a></li>';
            } else {
            	splitStr += '<li><a href="javascript:splitPage(' + i + ');" style="color: #898989; font-size: 14px;">';
            	splitStr += i;
            	splitStr += '</a></li>';
            }
        }
    }
	
	if (pageNumber == totalPages || totalPages == 0) {
		splitStr += '<li><a href="javascript:void(0)">下一页</a></li>';
	} else {
		splitStr += '<li><a href="javascript:splitPage(' + (pageNumber + 1) + ');">下一页</a></li>';
	}
	
	if(isSelectPage == true){
		splitStr += '&nbsp;&nbsp;<li><select id="pageNumberId" name="pageNumber" onChange="splitPage(this.value);" style="width: 130px; height:35px;">';
		for (var i = 1; i <= totalPages; i++) {
			if (i == pageNumber) {
				splitStr += '<option selected value="' + i + '">跳转到第' + i + '页</option>';
			} else {
				splitStr += '<option value="' + i + '">跳转到第' + i + '页</option>';
			}
		}
		if(totalPages == 0){
			splitStr += '<option value="0">无跳转数据</option>';
		}
		splitStr += '</select>';
		splitStr += '<li>&nbsp;&nbsp;';
	}else{
		splitStr += '<input type="hidden" id="pageNumberId" name="pageNumber">';
	}
	
	if(isSelectSize == true){
		splitStr += '<li><select id="pageSizeId" name="pageSize" onChange="splitPage(1);" style="width: 90px; height:35px;">';
		
		var optionStr = '<option value="10">每页10条</option>';
		optionStr += '<option value="20">每页20条</option>';
		optionStr += '<option value="40">每页40条</option>';
		optionStr += '<option value="80">每页80条</option>';
		optionStr += '<option value="100">每页100条</option>';
		optionStr += '<option value="200">每页200条</option>';
		optionStr = optionStr.replace('"' + pageSize + '"', '"' + pageSize + '" selected="selected"');
		
		splitStr += optionStr;
		
		splitStr += '</select></li>';
	}else{
		splitStr += '<input type="hidden" id="pageSizeId" name="pageSize">';
	}
	
	splitStr += '&nbsp;&nbsp;<li>共<strong>' + totalRow + '</strong>条记录</li>';
	
	splitStr += '</ul>';

	splitStr += '<input type="hidden" id="orderColunmId" name="orderColunm" value="'+orderColunm+'"/>';
	splitStr += '<input type="hidden" id="orderModeId" name="orderMode" value="'+orderMode+'"/>';
	
	return splitStr;
}

/**
 * 分页链接处理
 * @param toPage
 */
function splitPage(toPage){
	$("#pageNumberId" ).val(toPage);
	ajaxForm("splitPage");
}

/**
 * 分页列排序点击事件处理
 * @param td
 */
function orderbyFun(colunmName){
	var orderColunmNode = $("#orderColunmId");
	var orderColunm = orderColunmNode.val();
	
	var orderModeNode = $("#orderModeId");
	var orderMode = orderModeNode.val();
	
	if(colunmName == orderColunm){
		if(orderMode == ""){
			orderModeNode.val("asc");
		}else if(orderMode == "asc"){
			orderModeNode.val("desc");
		}else if(orderMode == "desc"){
			orderModeNode.val("");
		}
	}else{
		orderColunmNode.val(colunmName);
		orderModeNode.val("asc");
	}
	//alert(orderColunmNode.val()+"--"+orderModeNode.val());
	ajaxForm("splitPage");
}

/**
 * ajax提交form替换content
 * @param formId
 */
function ajaxForm(formId){
	/**遮罩层**/
	$('#content').fadeOut().parent().append('<div id="loading" class="center">Loading...<div class="center"></div></div>');
	$("#" + formId).ajaxSubmit({
		cache: false,
	    success:  function (data) {
	    	$("#content").html(data);
	    	$('#loading').remove();
			$('#content').fadeIn();
			docReady();
	    }
	});
}
/**
 * ajax请求url替换指定div
 * @param url
 * @param data
 */
function ajaxDiv(divId, url, data){
	$('#content').fadeOut().parent().append('<div id="loading" class="center">Loading...<div class="center"></div></div>');
	
	$.ajax({
		type : "post",
		url : encodeURI(encodeURI(cxt + url)),
		data : data,
		dataType : "html",
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		async: false,
		cache: false,
		success:function(returnData){
			$("#" + divId).html(returnData);
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) { 
			alert("请求出现错误！");
        },
        complete: function(XMLHttpRequest, textStatus) { 
	    	$('#loading').remove();
			$('#content').fadeIn();
			docReady();
        }
	});
}


/**
 * ajax请求url替换content
 * @param url
 * @param data
 */
var historyAjaxContent = [];//记录历史加载的内容
function ajaxContent(url, data ,callback){
	var historyInfo = {};
	var data = data || {};
	historyInfo = {id:randomString(15),url:url,data:data,callback:callback};
	historyAjaxContent.push(historyInfo);
	/*$('#content').fadeOut().parent().append('<div id="loading" class="center">Loading...<div class="center"></div></div>');*/
	$.ajax({
		type : "post",
		url :  encodeURI(encodeURI(cxt + url)),
		data : data,
		dataType : "html",
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",
//		async: false,
//		cache: false,
		success:function(returnData){
//			if(undefined != rememberHistory && rememberHistory && 
			if(undefined != callback){
				var previousPageHtml = $('<div id="'+historyInfo.id+'" style="display:none;"></div>').appendTo($("body"));
				$("#content").children().appendTo(previousPageHtml);
			}
			$("#content").html(returnData);
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) { 
			// 这个方法有三个参数：XMLHttpRequest 对象，错误信息，（可能）捕获的错误对象。
			// 通常情况下textStatus和errorThown只有其中一个有值
            // alert(XMLHttpRequest.status);
            // alert(XMLHttpRequest.readyState);
            // alert(textStatus);
			alert("请求出现错误！");
        },
        complete: function(XMLHttpRequest, textStatus) { 
        	// 请求完成后回调函数 (请求成功或失败时均调用)。参数： XMLHttpRequest 对象，成功信息字符串。
            // 调用本次AJAX请求时传递的options参数
	    	$('#loading').remove();
			$('#content').fadeIn();
			docReady();
        }
	});
}
//返回到上一个加载的页面
function ajaxContentReturn(returnData){
	var currentPageInfo = historyAjaxContent.pop();
	if(undefined != currentPageInfo){
		if(undefined != currentPageInfo.callback){
			$('#content').fadeOut();
			$('#content').html("");
			$("#"+currentPageInfo.id).children().appendTo($('#content'));
			$("#"+currentPageInfo.id).remove();
			$('#content').fadeIn();
			 currentPageInfo.callback(returnData);
		}else{
			var previousPageInfo = historyAjaxContent.pop();//删除当前页面
			if(undefined != previousPageInfo){
				ajaxContent(previousPageInfo.url, previousPageInfo.data);
			}else{
				myAlert("没有返回页面,请检查代码！");
			}
		}
	}else{
		myAlert("没有返回页面,请检查代码！");
	}
}

function randomString(len) {
	len = len || 32;
	var $chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890';
	var maxPos = $chars.length;
	var pwd = '';
	for (i = 0; i < len; i++) {
		pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
	}
	return pwd;
}

/**
 * ajax请求url替换DiaLog
 * @param url
 * @param data
 */
function ajaxDiaLog(url, data){
	$('#content').fadeOut().parent().append('<div id="loading" class="center">Loading...<div class="center"></div></div>');
	$.ajax({
		type : "post",
		url : encodeURI(encodeURI(cxt + url)),
		data : data,
		dataType : "html",
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		async: false,
		cache: false,
		success:function(returnData){
			$('#myModal').html(returnData);
			$('#myModal').modal('show');
	    	$('#loading').remove();
			$('#content').fadeIn();
		}
	});
}

/**
 *  @description：获取当前tab面板下所有选中的checkbox的方法
 *  功能：根据传过来的checkbox的name属性定位所有的元素，返回选中的对象的值数组
 *  @params：checkboxname - checkbox的name属性值
 *  		 objPkey - 实体对应的数据库主键
 */
 function getCheckedBoxesIds (checkboxname, objPkey){
	var idsArray = [];
	$("input[name='"+checkboxname+"']:checked").each(function(obj){//遍历所有选中状态的checkbox
		if($(this).val() != "")
			idsArray.push(objPkey + "=" + $(this).val());
	});
	return idsArray;
};
/**
 * 模块单选
 * @param moduleId
 * @param moduleName
 * @param checkedIds
 */
function moduleRadioDiaLog(moduleId, moduleName, checkedIds){
	$.ajax({
		type : "post",
		url : '${pageContext.request.contextPath}' + "/jf/module/toUrl",
		data : { "toUrl" : "/pingtai/module/radio.html", "ids" : checkedIds, "moduleId" : moduleId, "moduleName" : moduleName },
		dataType : "html",
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		async: false,
		success:function(data){
			$('#myModal').html(data);
			$('#myModal').modal('show');
		}
	});
}
/**
 * 组织选择
 */
function selectOrg() {
	$("#orgSelect_Div").load(encodeURI(encodeURI(cxt+"/page/system/org/orgSelect.jsp")),function(){
		var orgObj = $("#org_id");
		var orgOffset = $("#org_id").offset();
		$("#orgContent").css({left:orgOffset.left + "px", top:orgOffset.top + orgObj.outerHeight() + "px"}).slideDown("fast");
		$("body").bind("mousedown", onBodyDown);
	});
}
/**
 * 商铺选择
 */
function selectShop() {
	$("#orgSelect_Div").load(encodeURI(encodeURI(cxt+"/page/system/org/orgSelect.jsp")),function(){
		var orgObj = $("#org_id");
		var orgOffset = $("#org_id").offset();
		$("#orgContent").css({left:orgOffset.left + "px", top:orgOffset.top + orgObj.outerHeight() + "px"}).slideDown("fast");
		$("body").bind("mousedown", onBodyDown);
	});
}
/**
 * 组织单选
 * @param stationId
 * @param stationName
 * @param checkedIds
 */
function orgRadioDiaLog(orgId, orgName, checkedId){
	$.fn.SimpleModal({
		title: '组织单选',
		width: 550,
        keyEsc:true,
		buttons: [{
    		text:'确定',
    		classe:'btn primary btn-margin',
    		clickEvent:function() {
    			$("#orgId").val(checkedNodeIds);
    			$("#orgName").val(checkedNodeName);
    			onblurVali($("#orgName"));
    			closePop();
            }
    	},{
    		text:'取消',
    		classe:'btn secondary'
    	}],
		param: {
			url: cxt+ "/system/org/orgChoice",
			data : {"id" : checkedId, "orgId" : orgId, "orgName" : orgName }
		}
	}).showModal();
}

/**
 * 角色单选
 * @param stationId
 * @param stationName
 * @param checkedIds
 */
function roleRadioDiaLog(roleId, roleName, checkedId){
	$.fn.SimpleModal({
		title: '角色单选',
		width: 550,
        keyEsc:true,
		buttons: [{
    		text:'确定',
    		classe:'btn primary btn-margin',
    		clickEvent:function() {
    			$("#roleId").val(checkedNodeIds);
    			$("#roleName").val(checkedNodeName);
    			onblurVali($("#roleName"));
    			closePop();
            }
    	},{
    		text:'取消',
    		classe:'btn secondary'
    	}],
		param: {
			url: cxt+ "/system/role/roleChoice",
			data : {"id" : checkedId, "roleId" : roleId, "roleName" : roleName }
		}
	}).showModal();
}
function generRandomCharacters(characterLength){
	characterLength = characterLength || 5;
	var chars = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
	var randomCharacters = "";
    for(var i = 0; i < characterLength ; i ++) {
        var index = Math.floor(Math.random()*(chars.length));
        randomCharacters += chars[index];
    }
    return randomCharacters;
}
/**************************************		功能定制函数	end	***************************************************/