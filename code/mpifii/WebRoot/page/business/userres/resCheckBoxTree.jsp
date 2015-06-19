<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<input type="hidden" id="userId" value="${id}"></input>
<input type="hidden" id="treeIds"></input>
<div class="modal-body">
	<ul id="resTree" class="ztree"></ul>
</div>
<script type="text/javascript">
	var userid=$("#userId").val();
	var setting = {
		async: {
			enable : true,
			url : '${cxt}/business/app/userRes/loadResTree?id='+userid,
			type : "post"
		},
		view: {
			fontCss: getFont,
			expandSpeed:"",
			selectedMulti: false
		},
		check: {
			enable: true
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			onCheck: resTreeOnCheck
		}
	};
	
	//字体设置
	function getFont(treeId, node) {
		return node.font ? node.font : {};
	}

	var code;

	function setCheck() {
		var zTree = $.fn.zTree.getZTreeObj("resTree"),
		/*py = $("#py").attr("checked")? "p":"",
		sy = $("#sy").attr("checked")? "s":"",
		pn = $("#pn").attr("checked")? "p":"",
		sn = $("#sn").attr("checked")? "s":"",
		type = { "Y":py + sy, "N":pn + sn};
		zTree.setting.check.chkboxType = type;*/
		type = { "Y" : "ps", "N" : "ps" };
		setting.check.chkboxType = { "Y" : "ps", "N" : "ps" };
		showCode('setting.check.chkboxType = { "Y" : "' + type.Y + '", "N" : "' + type.N + '" };');
	}
	function showCode(str) {
		if (!code) code = $("#code");
		code.empty();
		code.append("<li>"+str+"</li>");
	}
	
	function resTreeOnCheck(){
		var treeObj = $.fn.zTree.getZTreeObj("resTree");
		var nodes = treeObj.getChangeCheckedNodes();
		var treeIds='';
		for(var i=0;i<nodes.length;i++){
			if(nodes[i].id.indexOf("_")!=-1){
				if(i==nodes.length-1){
					treeIds+=nodes[i].pId+nodes[i].id;
				}else{
					treeIds+=nodes[i].pId+nodes[i].id+",";
				}
			}
		}
		$("#treeIds").attr("value",treeIds);
	}

	$(document).ready(function(){
		$.fn.zTree.init($("#resTree"), setting);
		setCheck();
		$("#py").bind("change", setCheck);
		$("#sy").bind("change", setCheck);
		$("#pn").bind("change", setCheck);
		$("#sn").bind("change", setCheck);
	});
</script>
