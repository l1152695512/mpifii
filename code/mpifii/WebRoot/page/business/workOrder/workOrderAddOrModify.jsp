<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a>
			<span class="divider">/</span></li>
		<li><a href="#" onclick="ajaxContent('/business/app/workOrder');">工单预处理 </a> <span
			class="divider">/</span></li>
		<c:choose>
		    
			<c:when test="${empty workOrder.woId}"><li><a href="#">添加商铺工单</a></li></c:when>
			<c:otherwise><li><a href="#">修改商铺工单</a></li></c:otherwise>
		</c:choose>
		
	</ul>
</div>

<div class="row-fluid sortable">
	<div class="box span12">
		<div class="box-header well">
			<h2>
				<i class="icon-edit"></i>
				<c:choose>
					<c:when test="${empty workOrder.woId}">添加商铺工单</c:when>
					<c:otherwise>修改商铺工单</c:otherwise>
				</c:choose>
			</h2>
		</div>
		<div class="box-content">
			<form class="form-horizontal" id="shop_add_from" action="${cxt}/business/app/workOrder/save" method="POST">
				<input type="hidden" name="workOrder.wo_id" value="${workOrder.woId}"/>
				<input name="shop.id" id="shopId" value="${workOrder.id}"  type="hidden" />
				<input id="shoptrde" value="${workOrder.trde}"  type="hidden"/>

		        <div class="control-group">
		          <label class="control-label" >工单类型</label>
						<div class="controls">
							<select name="workOrder.wo_type" id="owTypeSel" class="combox" 
							style="width:278px;" onchange="woTypeChange()">
							   <option value="1" <c:if test="${workOrder.wotype == 1}">selected="selected"</c:if>>新工单</option>
							   <option value="2" <c:if test="${workOrder.wotype == 2}">selected="selected"</c:if>>追加工单</option>
							</select>
						</div>
		        </div>
		        
		        <div class="control-group"  id="shopSelDiv" style="display:none;">
				    <label class="control-label" >商铺</label>
				    <div class="controls">
						<select id="selectShops"  multiple="multiple" class="combox" >
						   <c:forEach var="shop" items="${shopList}">
							  <option value="${shop.id}">${shop.name}</option>
						   </c:forEach>
					    </select>
					 </div>
				</div>
				
			   <div class="control-group">
				    <label class="control-label" >是否集团客户</label>
			          <div class="controls">
			            <label class="radio inline" style="margin-left: 18px;">            
			              <input value="1" id="istradeRadio" name="shop.is_trde_cust" type="radio" <c:if test="${workOrder.isTrdeCust == 1}">checked="checked"</c:if> 
			              onclick='$("#orgDiv").fadeIn("fast");'>是</label>
			            <label class="radio inline" >            
			              <input value="2" id="notradeRadio" name="shop.is_trde_cust" type="radio" <c:if test="${workOrder.isTrdeCust != 1}">checked="checked"</c:if>
			              style="margin-left: 13px;" onclick='$("#orgDiv").hide();'>否</label>
			          </div>
				</div>
				
				<div class="control-group"  id="shopSelDiv" style="display:none;">
				    <label class="control-label" >商铺</label>
				    <div class="controls">
						<select id="selectShops"  multiple="multiple" onchange="loadShopData()">
						   <c:forEach var="shop" items="${shopList}">
							  <option value="${shop.id}">${shop.name}</option>
						   </c:forEach>
					    </select>
					 </div>
				</div>
				
				<div class="control-group" id="orgDiv">
					<label class="control-label">关联组织</label>
					<div class="controls">
                    <input type="hidden" name="shop.org_id" value="${workOrder.orgId}"/>
				    <input id="citySel" type="text" name="org_name" disabled="disabled" 
								class="input-xlarge"  value="${workOrder.orgName}"/>
					
					<div id="menuContent" style="display:none; position: absolute;background-color: #f5f5f5;">
					    <ul id="treeOrgs" class="ztree" style="margin-top:0; width:270px;"></ul>
					</div>
					
				    <input type="button" class="btn" value="选择组织"  onclick="showMenu();"/>
					<span class="help-inline"></span>
					</div>
				</div>
				

				
				<div class="control-group" id="shopname_input">
					<label class="control-label">商铺名称</label>
					<div class="controls">
						<input type="text" name="shop.name" value="${workOrder.name}" class="input-xlarge"
							maxlength="50" vMin="3" vType="length" onblur="onblurVali(this);">
							<span class="help-inline">3-50位</span>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label">商铺地址</label>
					<div class="controls">
						<input type="text" name="shop.work_addr" value="${workOrder.workAddr}" class="input-xlarge"
							maxlength="50" vMin="3" vType="length" onblur="onblurVali(this);">
							<span class="help-inline">商铺的详细地址（省、市、区、街道）</span>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label">商户联系方式</label>
					<div class="controls">
						<input type="text" name="_phone" value="${workOrder.phone}" class="input-xlarge" 
							maxlength="30" vMin="4" vType="phone" onchange="onblurVali(this);" />
						<span class="help-inline">联系方式默认为商铺平台登录用户帐号</span>
					</div>
				</div>
							
				<div class="control-group">
					<label class="control-label">吸顶智能wifi数量</label>
					<div class="controls">
						<input type="text" name="workOrder.ap_num" value="${workOrder.apNum}" class="input-xlarge" 
							maxlength="7" vMin="0" vType="number" onchange="onblurVali(this);" placeholder="0-7位数字" />
						<span class="help-inline">poe网线供电、吸顶天花板上、价格较贵</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">普通智能wifi数量</label>
					<div class="controls">
						<input type="text" name="workOrder.router_num" value="${workOrder.routerNum}" class="input-xlarge" 
							maxlength="7" vMin="0" vType="number" onchange="onblurVali(this);" placeholder="0-7位数字" />
						<span class="help-inline">电源线供电、价格便宜、桌面式</span>
					</div>
				</div>
				<div class="control-group">
						<label class="control-label">行业</label>
						<div class="controls">
							<select name="shop.trde" class="combox" id="shoptrdeSel" style="width:278px;">
							   <option value="">--请选择--</option>
							   <c:forEach var="dic" items="${diclist}">
									<option value="${dic.id}">${dic.value}</option>
							   </c:forEach>
							</select>
						</div>
			    </div>
				
				<div class="control-group">
					<label class="control-label">商铺宽带运营商类型</label>
					<div class="controls">
						<select name="shop.broadband_type" class="combox" id="broadbandtypeSel" style="width:278px;">
							 <option value="">--请选择--</option>
							 <option value="中国移动" <c:if test="${workOrder.broadbandType == '中国移动'}">selected="selected"</c:if>>中国移动</option>
							 <option value="中国联通" <c:if test="${workOrder.broadbandType == '中国联通'}">selected="selected"</c:if>>中国联通</option>
							 <option value="中国电信" <c:if test="${workOrder.broadbandType == '中国电信'}">selected="selected"</c:if>>中国电信</option>
							 <option value="其他" <c:if test="${workOrder.broadbandType == '其他'}">selected="selected"</c:if>>其他</option>
						</select>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label">固定电话号码</label>
					<div class="controls">
						<input type="text" name="shop.tel" value="${workOrder.tel}" class="input-xlarge" 
							maxlength="11"   onchange="onblurVali(this);"  />
						<span class="help-inline"></span>
					</div>
				</div>
				
				<div class="form-actions">
					<button class="btn btn-primary" type="button" onclick="submitShopInfo(this.form);">提交</button>
					<button type="button" class="btn" onclick="ajaxContent('/business/app/workOrder/index');">取消</button>
				</div>
			</form>
		</div>
	</div>
</div>


<script>
/*-----------------------------*/
$('#shoptrdeSel option').each(function() {
  if ($(this).val() == $("#shoptrde").val()) {
	  $(this).attr("selected","selected");
  }
});
    
 //工单类型
 if($("#shop_add_from input[name='workOrder.wo_id']").val()!=''){
	 $("#owTypeSel").attr("disabled","disabled"); 
 }

if(document.getElementById("notradeRadio").checked==true){
	$("#orgDiv").hide();
}else{
	$("#orgDiv").show("normal");
}
//新建工单
if($("#owTypeSel").val()==1){
	$("#shopSelDiv").hide();
	remdisabled();
}

//追加工单
if($("#owTypeSel").val()==2){
	$("#shopSelDiv").show("normal");
	loadshoplist();
	adddisabled();

}
function adddisabled(){
	$("#shop_add_from input[name='shop.name']").attr("readonly","readonly");
	$("#shop_add_from input[name='shop.work_addr']").attr("readonly","readonly");
	$("#shop_add_from input[name='_phone']").attr("readonly","readonly");
	$("#shoptrdeSel").attr("readonly","readonly");
}

function remdisabled(){
	$("#shop_add_from input[name='shop.name']").removeAttr("readonly");
	$("#shop_add_from input[name='shop.work_addr']").removeAttr("readonly");
	$("#shop_add_from input[name='_phone']").removeAttr("readonly");
	$("#shoptrdeSel").removeAttr("readonly");
}
//修改页面时不允许修改联系方式
if($("#shop_add_from input[name='workOrder.wo_id']").val()!=''){
	 $("#shop_add_from input[name='_phone']").attr("readonly","readonly");
}

function woTypeChange(){
	if($("#owTypeSel").val()==1){
		$("#shopSelDiv").hide();
		remdisabled();
		clearData();
	}
	if($("#owTypeSel").val()==2){
		$("#shopSelDiv").show("normal");
		loadshoplist();
		adddisabled();
	}
}
/*-----------------------------*/

function submitShopInfo(form){
	
	if($("#owTypeSel").val()==2){
		if($('#selectShops').val()==null ||$('#selectShops').val()==''){
			myAlert("请选择对应的商铺！");
			return;
		}
	}
	
	if(document.getElementById("istradeRadio").checked==true){
		if($('#citySel').val()==null ||$('#citySel').val()==''){
			setInputError($("#shop_add_from input[name='org_name']"),"组织不能为空！");
			return;
		}
	}
	
	var errorCount = formVali(form);
	if(errorCount != 0){
		return;
	}

	$("#shop_add_from").ajaxSubmit({
        success: function(resp){
        	if("phoneRepeat"==resp.error){
				setInputError($("#shop_add_from input[name='_phone']"),"商户联系方式不可重复！");
				return;
			}
			ajaxContent('/business/app/workOrder');
        }
  	});
}

function clearStyle(){
	hiddenInputColor($("#shop_add_from input[name='shop.name']"));
	hiddenInputColor($("#shop_add_from input[name='_phone']"));
	hiddenInputColor($("#shop_add_from input[name='shop.work_addr']"));
	$("#shop_add_from input[name='shop.name']").parent().children(".help-inline").text("");
	$("#shop_add_from input[name='_phone']").parent().children(".help-inline").text("");
	$("#shop_add_from input[name='shop.work_addr']").parent().children(".help-inline").text("");
}
function clearData(){
	$("#shop_add_from input[name='shop.name']").attr("value","");
	$("#shop_add_from input[name='shop.work_addr']").attr("value","");
	$("#shop_add_from input[name='_phone']").attr("value","");
	
	$("#shop_add_from input[name='shop.org_id']").attr("value","");
	$("#shop_add_from input[name='org_name']").attr("value","");
	$("#shop_add_from select[name='shop.trde']").attr("value","");
	
	$("#shop_add_from input[name='shop.tel']").attr("value","");
	$("#shop_add_from select[name='shop.broadband_type']").attr("value","");
	
	$('#selectShops').multiselect('deselect', $("#shopId").val());
	$("#shopId").attr("value","");
}

function loadShopData(data){
	var shopId = data;
	if(shopId.length==0){
		clearData();
	}else{
		var url='/business/app/workOrder/loadShop?id='+shopId;
		$.ajax({
			type : 'POST',
			dataType : "json",
			url :encodeURI(encodeURI(cxt + url)),
			success : function(data) {
				clearStyle();
			    $('#shoptrdeSel option').each(function() {
					if ($(this).val() == data.trde) {
						$(this).attr("value",data.trde);
					    $(this).attr("selected","selected");
					}else{
						$(this).removeAttr("selected");
					}
				});
			    $('#broadbandtypeSel option').each(function() {
					if ($(this).val() == data.broadbandType) {
						$(this).attr("value",data.broadbandType);
					    $(this).attr("selected","selected");
					}else{
						$(this).removeAttr("selected");
					}
				});
				$("#shop_add_from input[name='shop.name']").attr("value",data.name);
				$("#shop_add_from input[name='shop.work_addr']").attr("value",data.workAddr);
				$("#shop_add_from input[name='_phone']").attr("value",data.phone);
				
				$("#shop_add_from input[name='shop.org_id']").attr("value",data.orgId);
				$("#shop_add_from input[name='org_name']").attr("value",data.orgName);
				$("#shop_add_from input[name='shop.trde']").attr("value",data.trde);

				$("#shop_add_from input[name='shop.tel']").attr("value",data.tel);
				return;
			}
		});
	}

}
function loadshoplist() {
	var sel = $('#selectShops').multiselect({
		 enableFiltering: true,
		 maxHeight:200,
		 onChange: function(option, checked) {
			$("#shopId").attr("value",option.val());
		    var values = [];
		    $('#selectShops option').each(function() {
				 if ($(this).val() != option.val()) {
				     values.push($(this).val());
				 }
			});

			$('#selectShops').multiselect('deselect', values);
			//var checkId = $("#selectShops").val();
			loadShopData(option.val());
			return;
		 }
	 });
	
	/*$('#selectShops option').each(function() {
		 if ($(this).val() != $("#shopId").val()) {
			 $('#selectShops').multiselect('deselect', $(this).val());
		 }
	});*/
	
    $('#selectShops').multiselect('select', $("#shopId").val())
 }
 
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
			$.fn.zTree.init($("#treeOrgs"), setting);
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
		
		/*$(document).ready(function(){
			//$.fn.zTree.init($("#treeOrgs"), setting);
			
		});*/


</script>

