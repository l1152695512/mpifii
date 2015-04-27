<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<script type="text/javascript">
	
	var setting = {
		async: {
			enable : true,
			url : '${cxt}/system/res/treeData',
			autoParam : ["id=moduleIds"],//, "name=n", "level=lv"
			otherParam : {"id" : '${id}'},
			dataFilter : filter,
			type : "post"
		},
		view: {
			fontCss: getFont,
			expandSpeed:"",
			selectedMulti: false
		},
		check: {
			enable: true,
			chkStyle: "checkbox"
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			beforeAsync: beforeAsync,	//用于捕获异步加载之前的事件回调函数,zTree 根据返回值确定是否允许进行异步加载
			onAsyncSuccess: onAsyncSuccess,	//用于捕获异步加载出现异常错误的事件回调函数
			onAsyncError: onAsyncError,	//用于捕获异步加载正常结束的事件回调函数
			
			beforeCheck: beforeCheck,
			onCheck: onCheck
		}
	};

	//节点数据过滤 + 默认选中
	var moduleIds = 'init';
	var operatorIds = '';
	function filter(treeId, parentNode, childNodes) {
		if(moduleIds == 'init'){
			getCheckedByRole();
		}
		if (!childNodes) {
			return null;
		}
		var childNode;
		var childNodeId;
		var childNodeName;
		var zTree = $.fn.zTree.getZTreeObj("zTreeContent");
		for (var i=0, l=childNodes.length; i<l; i++) {
			childNode = childNodes[i];
			childNodeId = childNode.id;
			childNodeName = childNode.name;
			childNode.name = childNodeName.replace(/\.n/g, '.');
			
			if(moduleIds.indexOf(childNodeId + ',') != -1){
				zTree.checkNode(childNode, true, false, false);
			}else if(operatorIds.indexOf(childNodeId + ',') != -1){
				zTree.checkNode(childNode, true, false, false);
			}
		}
		return childNodes;
	}
	
	//字体设置
	function getFont(treeId, node) {
		return node.font ? node.font : {};
	}

	////////////////////下面是处理展开//////////////////
	
	//用于捕获异步加载之前的事件回调函数,zTree 根据返回值确定是否允许进行异步加载
	function beforeAsync() {
		curAsyncCount++;
	}
	
	//用于捕获异步加载出现异常错误的事件回调函数
	function onAsyncSuccess(event, treeId, treeNode, msg) {
		curAsyncCount--;
		if (curStatus == "expand") {
			expandNodes(treeNode.children);
		} else if (curStatus == "async") {
			asyncNodes(treeNode.children);
		}

		if (curAsyncCount <= 0) {
			if (curStatus != "init" && curStatus != "") {
				//$("#demoMsg").text((curStatus == "expand") ? "全部展开完毕" : "后台异步加载完毕");
				asyncForAll = true;
			}
			curStatus = "";
		}
	}

	//用于捕获异步加载正常结束的事件回调函数
	function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
		curAsyncCount--;

		if (curAsyncCount <= 0) {
			curStatus = "";
			if (treeNode!=null) asyncForAll = true;
		}
	}
	
	var curStatus = "init", curAsyncCount = 0, asyncForAll = false, goAsync = false;
	function expandAll() {
		if (!check()) {
			return;
		}
		var zTree = $.fn.zTree.getZTreeObj("zTreeContent");
		if (asyncForAll) {
			//$("#demoMsg").text("已经异步加载完毕，使用 expandAll 方法");
			zTree.expandAll(true);
		} else {
			expandNodes(zTree.getNodes());
			if (!goAsync) {
				//$("#demoMsg").text("已经异步加载完毕，使用 expandAll 方法");
				curStatus = "";
			}
		}
	}
	
	function expandNodes(nodes) {
		if (!nodes) {
			return;
		}
		curStatus = "expand";
		var zTree = $.fn.zTree.getZTreeObj("zTreeContent");
		for (var i=0, l=nodes.length; i<l; i++) {
			zTree.expandNode(nodes[i], true, false, false);
			if (nodes[i].isParent && nodes[i].zAsync) {
				expandNodes(nodes[i].children);
			} else {
				goAsync = true;
			}
		}
	}

	function asyncAll() {
		if (!check()) {
			return;
		}
		var zTree = $.fn.zTree.getZTreeObj("zTreeContent");
		if (asyncForAll) {
			//$("#demoMsg").text("已经异步加载完毕，不再重新加载");
		} else {
			asyncNodes(zTree.getNodes());
			if (!goAsync) {
				//$("#demoMsg").text("已经异步加载完毕，不再重新加载");
				curStatus = "";
			}
		}
	}
	function asyncNodes(nodes) {
		if (!nodes) {
			return;
		}
		curStatus = "async";
		var zTree = $.fn.zTree.getZTreeObj("zTreeContent");
		for (var i=0, l=nodes.length; i<l; i++) {
			if (nodes[i].isParent && nodes[i].zAsync) {
				asyncNodes(nodes[i].children);
			} else {
				goAsync = true;
				zTree.reAsyncChildNodes(nodes[i], "refresh", true);
			}
		}
	}

	function reset() {
		if (!check()) {
			return;
		}
		asyncForAll = false;
		goAsync = false;
		//$("#demoMsg").text("");
		$.fn.zTree.init($("#zTreeContent"), setting);
	}

	function check() {
		if (curAsyncCount > 0) {
			//$("#demoMsg").text("正在进行异步加载，请等一会儿再点击...");
			return false;
		}
		return true;
	}

	//////////////////选中事件处理////////////////////
	
	var className = "dark", checkedModuleNodeIds = "", checkedOperatorNodeIds = "";
	
	function beforeCheck(treeId, treeNode) {
		className = (className === "dark" ? "":"dark");
		return (treeNode.doCheck !== false);
	}
	
	function parentChecked(parentNode, checked){
		if(parentNode == null) return;
		var childrenNodes = parentNode.children;
		var length = childrenNodes.length;
		var checkedCount = 0;
		for (var i=0; i<length; i++) {
			var treeNode = childrenNodes[i];
			var checked = treeNode.checked;
			if(checked){
				checkedCount += 1;
			}
		}
		
		if(length == checkedCount){
			var id = parentNode.id + ",";
			if(checked && checkedModuleNodeIds.indexOf(id) == -1){
				checkedModuleNodeIds += id;
			}else{
				checkedModuleNodeIds = checkedModuleNodeIds.replace(id, "");
			}
			parentChecked(parentNode.getParentNode(), checked);
		}
	}
	
	function childrenCheck(nodes, checked) {
		if (!nodes) {
			return;
		}
		for (var i=0, l=nodes.length; i<l; i++) {
			var treeNode = nodes[i];
			var treeNodeId = treeNode.id;
			var id = treeNodeId + ",";
			
			if(treeNodeId.indexOf('module_') != -1){
				if(checked && checkedModuleNodeIds.indexOf(id) == -1){
					checkedModuleNodeIds += id;
				}else{
					checkedModuleNodeIds = checkedModuleNodeIds.replace(id, "");
				}
			}else if(treeNodeId.indexOf('operator_') != -1){
				if(checked && checkedOperatorNodeIds.indexOf(id) == -1){
					checkedOperatorNodeIds += id;
				}else{
					checkedOperatorNodeIds = checkedOperatorNodeIds.replace(id, "");
				}
			}
			childrenCheck(treeNode.children, checked);
		}
	}
	
	function onCheck(e, treeId, treeNode) {
		var treeNodeId = treeNode.id;
		var checked = treeNode.checked;
			
		var id = treeNode.id + ",";
		
		if(treeNodeId.indexOf('module_') != -1){
			if(checked && checkedModuleNodeIds.indexOf(id) == -1){
				checkedModuleNodeIds += id;
			}else{
				checkedModuleNodeIds = checkedModuleNodeIds.replace(id, "");
			}
		}else if(treeNodeId.indexOf('operator_') != -1){
			if(checked && checkedOperatorNodeIds.indexOf(id) == -1){
				checkedOperatorNodeIds += id;
			}else {
				checkedOperatorNodeIds = checkedOperatorNodeIds.replace(id, "");
			}
		}
		parentChecked(treeNode.getParentNode(), checked);
		childrenCheck(treeNode.children, checked);
	}
	
	function getCheckedByRole(){
		$.ajax({
		   	type: "POST",
		   	url: "${cxt}/system/role/getRoleAuth",
		   	data: 'id=${id}',
			async : false,
		   	success: function(data){
		   		if(data != "error"){
					//var json = eval(data);
					moduleIds = (data.moduleids == null? '': data.moduleids); 
					//operatorIds = (data.operatorids == null? '': data.operatorids);
					checkedModuleNodeIds = moduleIds;
					//checkedOperatorNodeIds = operatorIds;
		     	}else{
		     		alert("获取角色拥有的功能失败!");
		     	}
		   	}
		});
	}
	function setCheckValue(){
		//checkedModuleNodeIds = checkedModuleNodeIds.replace(/module_/g, '');
		//checkedModuleNodeIds = checkedModuleNodeIds.substring(0, checkedModuleNodeIds.length-1);
		if(moduleIds != checkedModuleNodeIds || operatorIds != checkedOperatorNodeIds){
			$.ajax({
			   	type: "POST",
			   	url: "${cxt}/system/role/setRoleAuth",
			   	data: { "id" : "${id}", "moduleIds" : checkedModuleNodeIds, "operatorIds" : checkedOperatorNodeIds},
				async : false,
			   	success: function(data){
			   		if(data != "error"){
						
			     	}else{
			     		alert("设置角色拥有的功能失败！");
			     	}
			   	}
			});
		}
	}

	//////////////////初始化////////////////////

	$(document).ready(function(){
		$.fn.zTree.init($("#zTreeContent"), setting);
		//$("#callbackTrigger").bind("change", {}, setTrigger);	//拖拽节点时自动展开父节点是否触发
		//$("#expandAllBtn").bind("click", expandAll);	//全部展开
		//$("#asyncAllBtn").bind("click", asyncAll);	//背后展开
		//$("#resetBtn").bind("click", reset);	//重置
	});

	setTimeout(function(){
		expandAll();
	}, 500);
</script>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal">×</button>
	<h3>角色授权</h3>
</div>
<div class="modal-body">
	<ul id="zTreeContent" class="ztree"></ul>
</div>
<div class="modal-footer">
	<a href="#" class="btn" data-dismiss="modal">关闭</a>
	<a href="#" class="btn btn-primary" data-dismiss="modal" onclick="setCheckValue();">确定</a>
</div>