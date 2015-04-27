<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
	
	var setting = {
		async: {
			enable : true,
			url : '${cxt}/system/org/treeData',
			autoParam : ["id=id"],//, "name=n", "level=lv"
			//otherParam : {"otherParam" : "zTreeAsyncTest"},
			dataFilter : filter,
			type:"post"
		},
		view: {
			fontCss: getFont,
			expandSpeed:"",
			addHoverDom: addHoverDom,
			removeHoverDom: removeHoverDom,
			selectedMulti: false
		},
		edit: {
			enable: true,
			removeTitle: "删除组织",
			renameTitle: "编辑组织名称"
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			beforeRemove: beforeRemove,	//节点被删除之前的事件,并且根据返回值确定是否允许删除操作
			beforeRename: beforeRename,	//用于捕获节点编辑名称结束
			
			beforeAsync: beforeAsync,	//用于捕获异步加载之前的事件回调函数,zTree 根据返回值确定是否允许进行异步加载
			onAsyncSuccess: onAsyncSuccess,	//用于捕获异步加载出现异常错误的事件回调函数
			onAsyncError: onAsyncError,	//用于捕获异步加载正常结束的事件回调函数
			
			beforeDrag: beforeDrag,	//用于捕获节点被拖拽之前的事件回调函数，并且根据返回值确定是否允许开启拖拽操作
			beforeDrop: beforeDrop,	//用于捕获节点拖拽操作结束之前的事件回调函数，并且根据返回值确定是否允许此拖拽操作
			beforeDragOpen: beforeDragOpen,	//用于捕获拖拽节点移动到折叠状态的父节点后，即将自动展开该父节点之前的事件回调函数，并且根据返回值确定是否允许自动展开操作
			onDrag: onDrag,	//用于捕获节点被拖拽的事件回调函数
			onDrop: onDrop,	//用于捕获节点拖拽操作结束的事件回调函数
			onExpand: onExpand	//用于捕获节点被展开的事件回调函数
		}
	};

	//节点数据过滤
	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) {
			return null;
		}
		for (var i=0, l=childNodes.length; i<l; i++) {
			childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
		}
		return childNodes;
	}
	
	//字体设置
	function getFont(treeId, node) {
		return node.font ? node.font : {};
	}

	////////////////////下面是处理增删改节点//////////////////
	//节点被删除之前的事件,并且根据返回值确定是否允许删除操作
	function beforeRemove(treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj("zTreeRoleContent");
		zTree.selectNode(treeNode);
		var isdelorg = false;
		if(confirm("确认删除<" + treeNode.name + ">组织吗？")){
			$.ajax({
				type: "POST",
				async:false,
				url: "system/org/delete",
				data: {id:treeNode.id},
				success: function(data,status,xhr){
					if(data == 'org'){
						myAlert("请先删除子组织！");
					}else if(data == 'shop'){
						myAlert("请先删除当前组织下的商铺！");
					}else if(data == 'user'){
						myAlert("请先删除当前组织下的用户！");
					}else if(data == treeNode.id){
						isdelorg = true;
					}else{
						myAlert("删除组织失败！");
					}
				}
			});
		}
		return isdelorg;
	}		
	
	//用于捕获节点编辑名称
	function beforeRename(treeId, treeNode, newName, isCancel) {
		if (treeNode.name == newName) {
			return true;
		}else if (newName.length == 0) {
			myAlert("组织名称不能为空！");
			return false;
		}else{
			$.ajax({
				type : "post",
				url : "${cxt}/system/org/edit",
				data : { "id" : treeNode.id, "name" : newName },
				dataType : "html",
				contentType: "application/x-www-form-urlencoded; charset=UTF-8",
				async: false,
				success:function(data){
					if(data == treeNode.id){
						return true;
			     	}else{
			     		myAlert("修改<" + treeNode.name + ">组织名称失败！");
						return false;
			     	}
				}
			});
			
			return true;
		}
	}
	
	//添加功能按钮
	var newCount = 1;
	function addHoverDom(treeId, treeNode) {
		var sObj = $("#" + treeNode.tId + "_span");
		if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) {
			return;
		}
		
		//1.处理添加按钮
		var addStr = "<span class='button add' id='addBtn_" + treeNode.tId + "' title='添加组织' onfocus='this.blur();'></span>";
		sObj.after(addStr);
		var btn = $("#addBtn_"+treeNode.tId);
		if (btn) btn.bind("click", function(){
			var addName = "新增组织" + (newCount++);//组织初始化名称
			var orderIds = (treeNode.children == undefined ? 1 : (treeNode.children.length + 1));
			
			$.ajax({
				type : "post",
				url : "${cxt}/system/org/add",
				data : { 
					"pid" : treeNode.id, 
					"name" : addName, 
					"orderId" : orderIds
				},
				dataType : "html",
				contentType: "application/x-www-form-urlencoded; charset=UTF-8",
				async: false,
				success:function(data){
			     	if(data != ""){
						var zTree = $.fn.zTree.getZTreeObj("zTreeRoleContent");
			     		zTree.addNodes(treeNode, {id : data, pId : treeNode.id, name : addName});
			     	}else{
			     		myAlert("新增组织失败！");
			     	}
				}
			});
			
			return false;
		});

// 		//2.商铺管理
// 		var shopControlStr = "<span class='button accessControl' id='shopControlBtn_" + treeNode.tId + "' title='商铺管理' onfocus='this.blur();'></span>";
// 		sObj.after(shopControlStr);
// 		var btn = $("#shopControlBtn_"+treeNode.tId);
// 		if (btn) btn.bind("click", function(){
// 			ajaxContent('/business/org/shop',{orgId:treeNode.id},backOrg);
// 			return false;
// 		});
		
		
// 		//3.用户管理
// 		var userControlStr = "<span class='button accessControl' id='userControlBtn_" + treeNode.tId + "' title='用户管理' onfocus='this.blur();'></span>";
// 		sObj.after(userControlStr);
// 		var btn2 = $("#userControlBtn_"+treeNode.tId);
// 		if (btn) btn2.bind("click", function(){
// 			ajaxContent('/business/org/user',{orgId:treeNode.id},backOrg);
// 			return false;
// 		});
		
// 		//3.角色管理
// 		var roleControlStr = "<span class='button accessControl' id='roleControlBtn_" + treeNode.tId + "' title='角色管理' onfocus='this.blur();'></span>";
// 		sObj.after(roleControlStr);
// 		var btn3 = $("#roleControlBtn_"+treeNode.tId);
// 		if (btn) btn3.bind("click", function(){
// 			ajaxContent('/business/org/role',{orgId:treeNode.id},backOrg);
// 			return false;
// 		});
		
	};
	
	function removeHoverDom(treeId, treeNode) {
		//1.处理添加按钮
		$("#addBtn_"+treeNode.tId).unbind().remove();
// 		//2.处理商铺管理
// 		$("#shopControlBtn_"+treeNode.tId).unbind().remove();
// 		//2.处理用户管理
// 		$("#userControlBtn_"+treeNode.tId).unbind().remove();
// 		//2.处理角色管理
// 		$("#roleControlBtn_"+treeNode.tId).unbind().remove();
	};

	////////////////////下面是处理展开//////////////////
	
	//用于捕获异步加载之前的事件回调函数,zTree 根据返回值确定是否允许进行异步加载
	function beforeAsync() {
		/* curAsyncCount++;  */
		 return true; 
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
		var zTree = $.fn.zTree.getZTreeObj("zTreeRoleContent");
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
		var zTree = $.fn.zTree.getZTreeObj("zTreeRoleContent");
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
		var zTree = $.fn.zTree.getZTreeObj("zTreeRoleContent");
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
		var zTree = $.fn.zTree.getZTreeObj("zTreeRoleContent");
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
		$.fn.zTree.init($("#treeDemo"), setting);
	}

	function check() {
		if (curAsyncCount > 0) {
			//$("#demoMsg").text("正在进行异步加载，请等一会儿再点击...");
			return false;
		}
		return true;
	}
	
	////////////////////下面是处理拖拽///////////////////
	
	function dropPrev(treeId, nodes, targetNode) {
		var pNode = targetNode.getParentNode();
		if (pNode && pNode.dropInner === false) {
			return false;
		} else {
			for (var i=0,l=curDragNodes.length; i<l; i++) {
				var curPNode = curDragNodes[i].getParentNode();
				if (curPNode && curPNode !== targetNode.getParentNode() && curPNode.childOuter === false) {
					return false;
				}
			}
		}
		return true;
	}
	
	function dropInner(treeId, nodes, targetNode) {
		if (targetNode && targetNode.dropInner === false) {
			return false;
		} else {
			for (var i=0,l=curDragNodes.length; i<l; i++) {
				if (!targetNode && curDragNodes[i].dropRoot === false) {
					return false;
				} else if (curDragNodes[i].parentTId && curDragNodes[i].getParentNode() !== targetNode && curDragNodes[i].getParentNode().childOuter === false) {
					return false;
				}
			}
		}
		return true;
	}
	
	function dropNext(treeId, nodes, targetNode) {
		var pNode = targetNode.getParentNode();
		if (pNode && pNode.dropInner === false) {
			return false;
		} else {
			for (var i=0,l=curDragNodes.length; i<l; i++) {
				var curPNode = curDragNodes[i].getParentNode();
				if (curPNode && curPNode !== targetNode.getParentNode() && curPNode.childOuter === false) {
					return false;
				}
			}
		}
		return true;
	}

	//用于捕获节点被拖拽之前的事件回调函数，并且根据返回值确定是否允许开启拖拽操作
	var log, className = "dark", curDragNodes, autoExpandNode;
	function beforeDrag(treeId, treeNodes) {
		className = (className === "dark" ? "":"dark");
		for (var i=0,l=treeNodes.length; i<l; i++) {
			if (treeNodes[i].drag === false) {
				curDragNodes = null;
				return false;
			} else if (treeNodes[i].parentTId && treeNodes[i].getParentNode().childDrag === false) {
				curDragNodes = null;
				return false;
			}
		}
		curDragNodes = treeNodes;
		return true;
	}
	
	//用于捕获拖拽节点移动到折叠状态的父节点后，即将自动展开该父节点之前的事件回调函数，并且根据返回值确定是否允许自动展开操作
	function beforeDragOpen(treeId, treeNode) {
		autoExpandNode = treeNode;
		return true;
	}
	
	//用于捕获节点拖拽操作结束之前的事件回调函数，并且根据返回值确定是否允许此拖拽操作
	function beforeDrop(treeId, treeNodes, targetNode, moveType, isCopy) {
		className = (className === "dark" ? "" : "dark");
		return true;
	}
	
	//用于捕获节点被拖拽的事件回调函数
	function onDrag(event, treeId, treeNodes) {
		className = (className === "dark" ? "":"dark");
	}
	
	//用于捕获节点拖拽操作结束的事件回调函数
	function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {
		className = (className === "dark" ? "":"dark");
		//alert(treeNodes.length + ":"+treeNodes[0].id + ", " + (targetNode ? (targetNode.id + ", " + targetNode.name) : "isRoot" ));
		if( treeNodes.length > 0 && targetNode){
			var dragId = treeNodes[0].id;//被拖拽组织
			var dragName = treeNodes[0].name;//被拖拽组织
			var targetId = targetNode.id;//拖拽到的目标组织
			var targetName = targetNode.name;//拖拽到的目标组织
			myConfirm("确定要移动["+dragName+"]到["+targetName+"]组织？",function(){
				$.ajax({
					type: "POST",
					url: "system/org/update",
					data: {ids:dragId,pIds:targetId},
					success: function(data,status,xhr){
						
					}
				});
			});
		}
	}
	
	//用于捕获节点被展开的事件回调函数
	function onExpand(event, treeId, treeNode) {
		if (treeNode === autoExpandNode) {
			className = (className === "dark" ? "":"dark");
		}
	}

	function setTrigger() {
		var zTree = $.fn.zTree.getZTreeObj("zTreeRoleContent");
		zTree.setting.edit.drag.autoExpandTrigger = $("#callbackTrigger").attr("checked");
	}

	//////////////////初始化////////////////////

	$(document).ready(function(){
		$.fn.zTree.init($("#zTreeRoleContent"), setting);
		//$("#expandAllBtn").bind("click", expandAll);	//全部展开
		//$("#asyncAllBtn").bind("click", asyncAll);	//背后展开
		//$("#resetBtn").bind("click", reset);	//重置
	});

	setTimeout(function(){
// 		expandAll();
		expandNodes(1);
	}, 500);
	
	
	function backOrg(){
		
	}
	
</script>
	
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a> <span class="divider">/</span></li>
		<li>
			<a href="#">组织管理</a>
		</li>
	</ul>
</div>

<div class="row-fluid sortable">
	<div class="box span12">
		<div class="box-header well" data-original-title>
			<h2><i class="icon-edit"></i>组织管理</h2>
			<div class="box-icon">
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
			</div>
		</div>
		<div class="box-content">
			<ul id="zTreeRoleContent" class="ztree"></ul>
		</div>
	</div><!--/span-->
</div><!--/row-->