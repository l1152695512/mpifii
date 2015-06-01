<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a>
			<span class="divider">/</span></li>
		<li><a href="#" onclick="ajaxContent('/business/shop/index');">商铺管理</a> <span
			class="divider">/</span></li>
		<li><a href="#">添加商铺</a></li>
	</ul>
</div>
<div class="row-fluid sortable">
	<div class="box span12">
		<div class="box-header well">
			<h2>
<<<<<<< HEAD
				<i class="icon-edit"></i>
				<c:choose>
					<c:when test="${empty shop.id}">添加商铺</c:when>
					<c:otherwise>修改商铺</c:otherwise>
				</c:choose>
=======
				<i class="icon-edit"></i> 添加商铺
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
			</h2>
<!-- 			<div class="box-icon"> -->
<!-- 				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> -->
<!-- 				<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> -->
<!-- 			</div> -->
		</div>
		<div class="box-content">
			<form class="form-horizontal" id="shop_add_from" action="${cxt}/business/shop/save" method="POST">
				<input type="hidden" name="shop.id" value="${shop.id}">
				<div class="control-group">
<<<<<<< HEAD
					<label class="control-label">所属客户</label>
					<div class="controls">
						<input type="text" name="shop.customer" value="${shop.customer}" class="input-xlarge"
							maxlength="50" vMin="0" vType="length" onblur="onblurVali(this);">
							<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">商铺编号</label>
					<div class="controls">
						<input type="text" name="shop.sn" value="${shop.sn}" class="input-xlarge"
							maxlength="12" vMin="0" vType="length" onblur="onblurVali(this);">
							<span class="help-inline">0-12位</span>
					</div>
				</div>
				
				<div class="control-group">
=======
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
					<label class="control-label">商铺名称</label>
					<div class="controls">
						<input type="text" name="shop.name" value="${shop.name}" class="input-xlarge"
							maxlength="50" vMin="3" vType="length" onblur="onblurVali(this);">
							<span class="help-inline">3-50位</span>
					</div>
				</div>
<!-- 				<div class="control-group"> -->
<!-- 					<label class="control-label">商铺图标</label> -->
<!-- 					<div class="controls"> -->
<%-- 						 <img  src="${cxt}/${shop.icon}" id="shop_icon" style="width: 48px;height: 48px" onerror= "this.src='${cxt}/images/guest.jpg'"> --%>
<!-- 						<input  type="file" name="shopImage" id="shopImage" accept="*.jpg,*.png,*.jpeg" value="上传图像"  /> -->
<!-- 					</div> -->
<!-- 				</div> -->
				<div class="control-group">
					<label class="control-label">商铺位置</label>
					<div class="controls">
						<input type="text" name="shop.location"  readonly="readonly" value="${shop.location}" class="input-xlarge"
							maxlength="50" vMin="1" vType="length" onchange="onblurVali(this);"/> 
						<input  type="button"  class="btn"  value="选择位置"  onclick="mapLocation()"/>
						<input type="hidden" name="shop.lng" value="${shop.lng}" />
						<input type="hidden" name="shop.lat" value="${shop.lat}" />
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">关联用户</label>
					<div class="controls">
<!-- 						<select name="shop.owner" class="combox"> -->
<%-- 							<c:forEach var="user" items="${userList}"> --%>
<%-- 								<option value="${user.id}" <c:if test="${user.id==shop.owner}"> selected="selected"</c:if>>${user.name}(设备数:${user.sbs})</option> --%>
<%-- 							</c:forEach> --%>
<!-- 						</select> -->
						<input type="text" name="userName" disabled="disabled" maxlength="50" value="${shop.owner_name}" 
								class="input-xlarge" vMin="1" vType="length" onchange="onblurVali(this);"/> 
						<c:if test="${empty shop.owner}">
							<input type="button" class="btn" value="选择用户"  onclick="listUsers();"/>
						</c:if>
						<input type="hidden" name="shop.owner" value="${shop.owner}" />
						<span class="help-inline"></span>
					</div>
				</div>
<<<<<<< HEAD
				<%-- <div class="control-group">
=======
				<div class="control-group">
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
					<label class="control-label">分组</label>
					<div class="controls">
						<input type="text" name="shopGroupName" readonly="readonly" value="${shop.groupName}" class="input-xlarge"
							maxlength="50" vMin="1" vType="length" onchange="onblurVali(this);"/> 
						<input type="button" class="btn" value="设置分组"  onclick="listShopGroup();"/>
						<input type="hidden" name="shop.group_id" value="${shop.group_id}" />
						<span class="help-inline"></span>
					</div>
<<<<<<< HEAD
				</div>--%>
				
				<div class="control-group">
					<label class="control-label">关联组织</label>
					<div class="controls">
                    <input type="hidden" name="shop.org_id" value="${shop.org_id}"/>
				    <input id="citySel" type="text" name="org_name" disabled="disabled" maxlength="50"  
								class="input-xlarge" vMin="1" vType="length" onchange="onblurVali(this);" value="${shop.orgName}"/> 
					
					<div id="menuContent" style="display:none; position: absolute;background-color: #f5f5f5;">
					    <ul id="treeOrgs" class="ztree" style="margin-top:0; width:270px;"></ul>
					</div>
					
				    <input type="button" class="btn" value="选择组织"  onclick="showMenu();"/>
					<span class="help-inline"></span>
					</div>
				</div>
				
=======
				</div>
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
				<div class="control-group">
					<label class="control-label">联系方式</label>
					<div class="controls">
						<input type="text" name="shop.tel" value="${shop.tel}" class="input-xlarge" 
<<<<<<< HEAD
							maxlength="30" vMin="0" onchange="onblurVali(this);" />
=======
							maxlength="30" vMin="0" vType="phone" onchange="onblurVali(this);" />
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
						<span class="help-inline">手机号码</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">描述</label>
					<div class="controls">
						<input type="text" name="shop.des" value="${shop.des}" class="input-xlarge"
							maxlength="500" vMin="0" vType="length"  />
						<span class="help-inline">0-500位</span>
					</div>
				</div>
				<div class="form-actions">
					<button class="btn btn-primary" type="button" onclick="submitShopInfo(this.form);">提交</button>
					<button type="button" class="btn" onclick="ajaxContent('/business/shop/index');">取消</button>
				</div>
			</form>
		</div>
	</div>
	<!--/span-->
</div>
<!--/row-->
<script type="text/javascript">
	function submitShopInfo(form){
		var errorCount = formVali(form);
		if(errorCount != 0){
			return;
		}
		$("#shop_add_from").ajaxSubmit({
	        success: function(resp){
				ajaxContent('/business/shop');
	        }
	  	});
	}
	$("#shopImage").on("change", function() {
		var files = !!this.files ? this.files : [];
		if (!files.length || !window.FileReader)
			return;
		if (/^image/.test(files[0].type)) {
			var reader = new FileReader();
			reader.readAsDataURL(files[0]);
			reader.onloadend = function() {
				$("#shop_icon").attr("src", this.result);
			};
		}
	});
	var mlab;
	function getAddr(lng,lat,addr){
		$("#shop_add_from input[name='shop.lng']").val(lng);
		$("#shop_add_from input[name='shop.lat']").val(lat);
		$("#shop_add_from input[name='shop.location']").val(addr);
		onblurVali($("#shop_add_from input[name='shop.location']"));
		mlab.hideModal();
	}
	function addUserCallback(data){//添加完新用户后回调
		if(undefined != data){
			$("#shop_add_from input[name='userName']").val(data.name);
			$("#shop_add_from input[name='shop.owner']").val(data.id);
			onblurVali($("#shop_add_from input[name='userName']"));
		}
	}
	function mapLocation(){
		var lng = $("#shop_add_from input[name='shop.lng']").val();
		var lat = $("#shop_add_from input[name='shop.lat']").val();
		mlab = $.fn.SimpleModal({
// 			model: 'modal-ajax',
			title: '地图定位',
			width: 800,
			height: 550,
// 			hideFooter: true,
			param: {
				url: "business/shop/mapLab?lng="+lng+"&lat="+lat
			}
		});
		mlab.showModal();
	}
	function listUsers(){
		$.fn.SimpleModal({
			title: '关联用户',
			width: 400,
	        keyEsc:true,
			buttons: [{
	    		text:'确定',
	    		classe:'btn primary btn-margin',
	    		clickEvent:function() {
	    			var userId = $("#choice_user_list input[name='select_user']:checked").val();
	    			var userName = $("#choice_user_list input[name='select_user']:checked").parent().next().text();
	    			
	    			$("#shop_add_from input[name='userName']").val(userName);
	    			$("#shop_add_from input[name='shop.owner']").val(userId);
	    			onblurVali($("#shop_add_from input[name='userName']"));
	    			closePop();
	            }
	    	},{
	    		text:'取消',
	    		classe:'btn secondary'
	    	}],
			param: {
				url: 'business/shop/listUser'
			}
		}).showModal();
	}
	function listShopGroup(){
		var shopOwner = $("#shop_add_from input[name='shop.owner']").val();
		if(shopOwner == undefined || shopOwner==""){
			myAlert("请先选择“关联用户”！");
			return;
		}
		$.fn.SimpleModal({
			title: '选择分组',
			width: 400,
	        keyEsc:true,
			buttons: [{
	    		text:'确定',
	    		classe:'btn primary btn-margin',
	    		clickEvent:function() {
	    			var shopGroupId = $("#choice_shop_group_list input[name='select_group']:checked").val();
	    			var shopGroupName = $("#choice_shop_group_list input[name='select_group']:checked").parent().next().text();
	    			
	    			$("#shop_add_from input[name='shopGroupName']").val(shopGroupName);
	    			$("#shop_add_from input[name='shop.group_id']").val(shopGroupId);
	    			
	    			onblurVali($("#shop_add_from input[name='shopGroupName']"));
	    			closePop();
	            }
	    	},{
	    		text:'取消',
	    		classe:'btn secondary'
	    	}],
			param: {
				url: 'business/shop/listShopGroup',
				data: {userId:shopOwner},
			}
		}).showModal();
	}
<<<<<<< HEAD
	
	
	//---------------------
	var setting = {
			async: {
				enable : true,
				url : '${cxt}/system/org/treeData',
				autoParam : ["id=id"],
				dataFilter : filter,
				type:"post"
			},
			view: {
				dblClickExpand: false,
				autoCancelSelected:false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				
				onClick: onClick,
				
				beforeAsync: beforeAsync,	//用于捕获异步加载之前的事件回调函数,zTree 根据返回值确定是否允许进行异步加载
				onAsyncSuccess: onAsyncSuccess,	//用于捕获异步加载出现异常错误的事件回调函数
				onAsyncError: onAsyncError	//用于捕获异步加载正常结束的事件回调函数
			}
		};
		
		function onClick(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("treeOrgs"),
			nodes = zTree.getSelectedNodes(),
			v = "";
			var nodeId;
			nodes.sort(function compare(a,b){return a.id-b.id;});
			for (var i=0, l=nodes.length; i<l; i++) {
				v = nodes[i].name;
				nodeId = nodes[i].id;
			}
			//if (v.length > 0 ) v = v.substring(0, v.length-1);
			
			var cityObj = $("#citySel");
			cityObj.attr("value", v);
			$("input[name='shop.org_id']").attr("value",nodeId);
		}
		
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

		function showMenu() {
		 	var zTree = $.fn.zTree.getZTreeObj("treeOrgs");
		    var selNodes = zTree.getSelectedNodes();
			if(selNodes!=null && selNodes.length>0){
				expandSelNode(zTree,selNodes[0]);
			}
			var cityObj = $("#citySel");
			var cityOffset = $("#citySel").offset();
			$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

			$("body").bind("mousedown", onBodyDown);
		
		}
		//展开选中的节点
		function expandSelNode(zTree,node){
		    if(node!=null){
		    	parentNode = node.getParentNode();
		    	zTree.expandNode(parentNode,true,false,true,false);
		    	expandSelNode(zTree,parentNode);
		    }
		}
		
		function hideMenu() {
			$("#menuContent").fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		}
		function onBodyDown(event) {
			if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
				hideMenu();
			}
		}
		var curStatus = "init", curAsyncCount = 0, asyncForAll = false, goAsync = false;
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
		
		function asyncNodes(nodes) {
			if (!nodes) {
				return;
			}
			curStatus = "async";
			var zTree = $.fn.zTree.getZTreeObj("treeOrgs");
			for (var i=0, l=nodes.length; i<l; i++) {
				if (nodes[i].isParent && nodes[i].zAsync) {
					asyncNodes(nodes[i].children);
				} else {
					goAsync = true;
					zTree.reAsyncChildNodes(nodes[i], "refresh", true);
				}
			}
		}
		
		$(document).ready(function(){
			$.fn.zTree.init($("#treeOrgs"), setting);
			
		});
=======
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
</script>