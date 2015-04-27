<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<script type="text/javascript">
	
	var setting = {
		async: {  
            enable: true,  
            url:"${cxt}/business/org/role/treeShopData", //异步请求地址  
            autoParam:["id", "name=n", "level=lv"],  
            otherParam : {"roleId" : '${id}',"orgId":'${orgId}'},
            dataFilter: filter  
        },  
		check: {
			enable: true,
			chkStyle: "checkbox",
			chkboxType: { "Y": "s", "N": "ps" }
		},
		data: {
			simpleData: {
				enable: true
			}
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

	
	  var zNodes =[
	               { id:1, pId:0, name:"湖北省", open:true},
	               { id:11, pId:1, name:"武汉市", open:true},
	               { id:111, pId:11, name:"汉口"},
	               { id:112, pId:11, name:"武昌"},
	               { id:12, pId:1, name:"黄石市", open:true},
	               { id:121, pId:12, name:"黄石港区"},
	               { id:122, pId:12, name:"西塞山区"},
	               { id:2, pId:0, name:"湖南省", open:true},
	               { id:21, pId:2, name:"长沙市"},
	               { id:22, pId:2, name:"株洲市", open:true},
	               { id:221, pId:22, name:"天元区"},
	               { id:222, pId:22, name:"荷塘区"},
	               { id:23, pId:2, name:"湘潭市"}
	             ];
	  
	  
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
		
		function check() {
			if (curAsyncCount > 0) {
				//$("#demoMsg").text("正在进行异步加载，请等一会儿再点击...");
				return false;
			}
			return true;
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
		
		
		
		
		  function saveCheckValue(){
			  var treeObj = $.fn.zTree.getZTreeObj("zTreeContent");  
			  var nodes = treeObj.getCheckedNodes(true); 
			  var str_id = "";   
			  for (var node in nodes){   
				  for(var key in nodes[node]){   
					  if("id" == key){  
	                      if(str_id!=""){  
	                          str_id = str_id + ',' + nodes[node][key];   
	                      }else{  
	                          str_id += nodes[node][key];   
	                      }  
	                  }  

				  }
			  }
			  if(str_id != ""){
				  $.ajax({
					   	type: "POST",
					   	url: "${cxt}/business/org/role/setRoleAuth",
					   	data: { "roleId" : "${id}", "menuIds" : str_id,"type":"1"},
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
    });
	
	setTimeout(function(){
		expandAll();
	}, 500);

</script>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal">×</button>
	<h3>管理平台授权</h3>
</div>
<div class="modal-body">
	<ul id="zTreeContent" class="ztree"></ul>
</div>
<div class="modal-footer">
	<a href="#" class="btn" data-dismiss="modal">关闭</a>
	<a href="#" class="btn btn-primary" data-dismiss="modal" onclick="saveCheckValue();">确定</a>
</div>