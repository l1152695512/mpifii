<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/js/jsFile/zTree/css/demo.css" type="text/css">
<title>Insert title here</title>
</head>
<body>
	<div id="orgContent"  style="display: none; position: absolute;">
	       <ul id="orgTree" class="ztree" style="margin-top: 0; width: 150px;">
	       </ul>
	</div>
</body>
</html>
<script type="text/javascript">
	function beforeClick(treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj("orgTree");
		zTree.checkNode(treeNode, !treeNode.checked, null, true);
		return false;
	}
	var setting = {
			check: {
				enable: true,
				chkStyle: "radio",
				radioType: "all"
			},
			view: {
				dblClickExpand: false
			},
			async: {
				enable: true,
				url:"${cxt}/system/org/treeData",
				autoParam:["id", "name=n"],
				dataFilter: filter
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeClick: beforeClick,
				onCheck: onCheck
			}
	};
	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) return null;
		for (var i=0, l=childNodes.length; i<l; i++) {
			childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
		}
		return childNodes;
	}
	function onCheck(e, treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj("orgTree"),
		nodes = zTree.getCheckedNodes(true),
		v = "";
		id= "";
		for (var i=0, l=nodes.length; i<l; i++) {
			v += nodes[i].name + ",";
			id+= nodes[i].id + ",";
		}
		if (v.length > 0 ) v = v.substring(0, v.length-1);
		if (id.length > 0 ) id = id.substring(0, id.length-1);
		var orgObj = $("#org_id");
		orgObj.attr("value", v);
		var orgId = $("#qOrgId");
		orgId.attr("value", id);
		var url ="/business/shop/getShopByOrg?orgids="+id;
		$.ajax({
			type : 'POST',
			dataType : "json",
			url :cxt + url,
			success : function(data) {
				$("#select_shop").multiselect('dataprovider',data);
			}
		});
	}
	function hideMenu() {
		$("#orgContent").fadeOut("fast");
		$("body").unbind("mousedown", onBodyDown);
	}
	function onBodyDown(event) {
		if (!(event.target.id == "menuBtn" || event.target.id == "org_id" || event.target.id == "orgContent" || $(event.target).parents("#orgContent").length>0)) {
			hideMenu();
		}
	}
	$(document).ready(function(){
		$.fn.zTree.init($("#orgTree"), setting);
	});
</script>
