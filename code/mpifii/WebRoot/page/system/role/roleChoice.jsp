<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<script type="text/javascript">
	
	var setting = {
		async: {
			enable : true,
			url : '${cxt}/system/role/treeData',
			autoParam : ["id=id"],//, "name=n", "level=lv"
			//otherParam : {"otherParam" : "zTreeAsyncTest"},
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
			chkStyle: "radio",
			radioType: "all"
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

	//节点数据过滤
	var roleIds = '${id},';
	function filter(treeId, parentNode, childNodes) {
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
			
			if(roleIds != '' && roleIds.indexOf(childNodeId + ',') != -1){
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
	
	var className = "dark", checkedNodeIds = "${id}", checkedNodeName = "";
	
	function beforeCheck(treeId, treeNode) {
		className = (className === "dark" ? "":"dark");
		return (treeNode.doCheck !== false);
	}
	
	function onCheck(e, treeId, treeNode) {
		checkedNodeIds = treeNode.id;
		checkedNodeName = treeNode.name;
	}
	
<<<<<<< HEAD
	/* function setCheckValue(){
		$("#roleId").val(checkedNodeIds);
		$("#roleName").val(checkedNodeName);
		onblurVali($("#roleName"));
	} */
=======
	function setCheckValue(){
		$("#roleId").val(checkedNodeIds);
		$("#roleName").val(checkedNodeName);
		onblurVali($("#roleName"));
	}
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
	
	//////////////////初始化////////////////////

	$(document).ready(function(){
		$.fn.zTree.init($("#zTreeContent"), setting);

		//$("#expandAllBtn").bind("click", expandAll);	//全部展开
		//$("#asyncAllBtn").bind("click", asyncAll);	//背后展开
		//$("#resetBtn").bind("click", reset);	//重置
	});

	setTimeout(function(){
		expandAll();
	}, 500);
</script>

<<<<<<< HEAD
<div class="modal-body">
	<ul id="zTreeContent" class="ztree"></ul>
</div>
<!-- <div class="modal-footer">
	<a href="#" class="btn" data-dismiss="modal">关闭</a>
	<a href="#" class="btn btn-primary" data-dismiss="modal" onclick="setCheckValue();">确定</a>
</div> -->
=======
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal">×</button>
	<h3>角色单选</h3>
</div>
<div class="modal-body">
	<ul id="zTreeContent" class="ztree"></ul>
</div>
<div class="modal-footer">
	<a href="#" class="btn" data-dismiss="modal">关闭</a>
	<a href="#" class="btn btn-primary" data-dismiss="modal" onclick="setCheckValue();">确定</a>
</div>
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
