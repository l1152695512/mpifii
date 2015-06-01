//当前第一次调用ajax时触发，如果同时有多个ajax请求或者在调用ajax前已有ajax未完成，只有第一次ajax会触发该事件
$( document ).ajaxStart(function() {//用于弹出遮罩层显示加载中
	$.fn.SimpleModal({
		model: 'loading',
		width: 200,
		zindexAdd:100,//加载提示的zindex比普通的弹窗大100
		closeButton: false,
//		hideHeader: false,
		keyEsc:true,
<<<<<<< HEAD
		animate:false
=======
		animate:false,
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
//		hideFooter: false
	}).showModal();
//}).ajaxSend(function( event, jqXHR, ajaxOptions) {//每次ajax调用之前会触发该事件
}).ajaxError(function(event, jqXHR, ajaxSettings, thrownError){
	if(jqXHR.status == 0){
		myAlert("无法访问服务器！");
	}else if(jqXHR.status == 500){
		myAlert("服务器请求出现错误，请联系管理员！");
	}else if(jqXHR.status == 401){
		myAlert("无权限操作！");
	}else{
		myAlert("请求失败，稍后请重试！");
	}
}).ajaxComplete(function(event, jqXHR, ajaxOptions) {//每次ajax调用完成后会触发该事件
	var responseText = jqXHR.responseText;
//	if(jqXHR.readyState != 4 || (jqXHR.status-200 < 0 && jqXHR.status-200 >= 100 )){
//		myAlert("加载失败，稍后请重试！");
//	}else 
	if(isLoginPage(responseText)){//授权失效
		//弹出授权框
		myAlert("登录过时，请重新登录！",function(e){
			var url=encodeURI(encodeURI(cxt + "/loginView"));
			window.location.href=url;
		});
	}
}).ajaxStop(function() {//一批或者一个ajax调用完成后触发一次，用于所有请求完成后隐藏遮罩层
	closePop();
});
<<<<<<< HEAD
=======

>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
function isLoginPage(responseText){
	if(responseText.indexOf("html") != -1 && 
			responseText.indexOf("this is mark for login page,do not delete it") != -1){
		return true;
	}else{
		return false;
	}
}

function loadPage(url,params,$object){
	$.ajax({
		type: "POST",
		dataType: 'html',
		data : params,
		url: url,
		async: false,
		success: function(responseText,textStatus,jqXHR){
			if(!isLoginPage(responseText)){
				$object.html(responseText);
			}
		}
	});
}

function loadPageWithCallback(url,params,callback){
	$.ajax({
		type: "GET",
		dataType: 'html',
		data : params,
		url: url,
		success: function(responseText,textStatus,jqXHR){
			if(!isLoginPage(responseText)){
				callback(responseText);
			}
		}
	});
}

function myAlert(contents,afterCloseCallback,buttonText){
	if(buttonText == undefined || '' == buttonText){
		buttonText = '关闭';
	}
	var buttons = [{
		text:buttonText,
		classe:'btn primary'
	}];
	$.fn.SimpleModal({
		title: '提示',
        overlayClick:  true,
        closeButton: true,
        buttons: buttons,
        keyEsc: true,
        width: 400,
        contents: contents,
        afterClose:afterCloseCallback
    }).showModal();
}

function myAlertClosePop(contents){
	myAlert(contents,function(){
    	closePop();
    });
}

function myConfirm(contents,callback){
	$.fn.SimpleModal({
        title: '确认',
        width: 400,
        keyEsc:true,
        buttons: [{
	    		text:'确定',
	    		classe:'btn primary btn-margin',
	    		clickEvent:function() {
	            	var optionsIndex = this._popOptionsIndex();
		        	if(optionsIndex != -1){
		        		callback.apply(this);
	                    this.hideModal();
		        	}
	            }
	    	},{
	    		text:'取消',
	    		classe:'btn secondary'
	    	}],
        contents: contents
    }).showModal();
}

function closePop(){
	var index = $.fn._maxZIndexOptionIndex();
	if(index != -1){
		var popId = window.popsOption[index].id;
		$("#simple-modal-"+popId).hideModal();
	}
}