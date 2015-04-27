<%@ page language="java" contentType="text/html; charset=UTF-8"
	deferredSyntaxAllowedAsLiteral="true" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>点亮商铺</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
	<meta name="author" content="Muhammad Usman">
	
	
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=XpGabbd4W3nxxzOi4WCu03yt"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.3.min.js"></script>

	<style type="text/css">
		#mapShows{width:100%; height:800px;}
	</style>

</head>


<body>
<div id="mapShows"></div>
	
	<script type="text/javascript">
		var map = new BMap.Map("mapShows");
		map.centerAndZoom("${city}",12);
		map.setCurrentCity("${city}"); 
		map.enableScrollWheelZoom();     
		map.addControl(new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT}));
		map.addControl(new BMap.NavigationControl());
		
		// 定义一个控件类,即function
		function ZoomControl(){
		  // 默认停靠位置和偏移量
		  this.defaultAnchor = BMAP_ANCHOR_TOP_RIGHT;
		  this.defaultOffset = new BMap.Size(10, 10);
		}
		// 通过JavaScript的prototype属性继承于BMap.Control
		ZoomControl.prototype = new BMap.Control();
		// 自定义控件必须实现自己的initialize方法,并且将控件的DOM元素返回
		// 在本方法中创建个div元素作为控件的容器,并将其添加到地图容器中
		ZoomControl.prototype.initialize = function(map){
		  // 创建一个DOM元素
		  var div = document.createElement("div");
		  // 设置样式
		$(div).css({"cursor": "default","border": "1px solid gray","right": "0px",
				"padding": "10px 10px","background": "white",
				"-webkit-box-shadow": "gray 0px 0px 5px",
				"-moz-box-shadow": "gray 0px 0px 5px",
				"box-shadow": "gray 0px 0px 5px"});


		  $(div).html("网点总量：0<br>点亮网点：0<br> 未点亮：0<br>在线总人数：0<br>今日总人流：0<br>昨日总人流：0");
		  
		  $.ajax({
				type: "POST",
				global: false,
// 				async:false,
				url: "${pageContext.request.contextPath}/open/getTotal",
				data:{admin:"yes"},
				success: function(data,status,xhr){
					if("success" == status){
						$(div).html("<h1 style='font-size:14px;text-align:center;'>江西省运营数据</h1>网点总量："+data.shopNum+"<br>点亮网点："+data.onlineShopNum+"<br>未点亮："+data.offlineShopNum+"<br>在线总人数："+data.onlineNum+"<br>今日总人流："+data.tNum+"<br>昨日总人流："+data.yNum);
					}
				}
			});
		  // 添加DOM元素到地图中
		  map.getContainer().appendChild(div);
		  // 将DOM元素返回
		  return div;
		}
		
		// 创建控件
		var myZoomCtrl = new ZoomControl();
		// 添加到地图当中
		map.addControl(myZoomCtrl);
		
		var opts = {
	      width : 110,     // 信息窗口宽度
	      height: 80    // 信息窗口高度
	    }
		var onIcon = new BMap.Icon("${pageContext.request.contextPath}/images/mo.gif", new BMap.Size(36, 47), {
			anchor: new BMap.Size(14, 48)
       	}); 
		
		var offIcon = new BMap.Icon("${pageContext.request.contextPath}/images/offIcon.png", new BMap.Size(36, 47), {
			anchor: new BMap.Size(14, 48)
       	}); 
		
		
		
		
		
		<c:forEach items="${shopList}" var="shop"> 
		  	var marker;
		  	if("${shop.online}">0){
		  		marker= new BMap.Marker(new BMap.Point("${shop.lng}", "${shop.lat}"),{icon:onIcon});
		  	}else{
		  		marker= new BMap.Marker(new BMap.Point("${shop.lng}", "${shop.lat}"),{icon:offIcon});
		  	}
			map.addOverlay(marker); 


			marker.addEventListener("mouseover", 
				function(){
					var opts2 = {
					  position : new BMap.Point("${shop.lng}", "${shop.lat}"),    // 指定文本标注所在的地理位置
					  offset   : new BMap.Size(10, -10)    //设置文本偏移量
					}
					var label = new BMap.Label("${shop.name}", opts2);  // 创建文本标注对象
					label.setStyle({
						 color : "black",
						 fontSize : "12px",
						 height : "20px",
						 lineHeight : "20px",
						 fontFamily:"微软雅黑"
					});
					map.addOverlay(label); 
					
					this.addEventListener("mouseout", 
						function(){
							map.removeOverlay(label);
						}
					);
				}
			);
			
			
			marker.addEventListener("click", 
				function(){
					var onlineNum = 0;
					$.ajax({
						type: "POST",
						global: false,
						async:false,
						url: "${pageContext.request.contextPath}/open/getOnLineTotal",
						data: {shopId:"${shop.id}"},
						success: function(data,status,xhr){
							if("success" == status){
								onlineNum = data.onlineNum;
							}
						}
					});
					
					this.openInfoWindow(new BMap.InfoWindow("名称："+"${shop.name}"+"</br>在线："+onlineNum+"人</br>地址："+"${shop.addr}", opts));
				}
			);
		</c:forEach> 
		$(window).resize(function(){
			 changLayout();
		});
		 changLayout();
		 function changLayout(){
			 var nowHeight= $(window).height();
				$("#mapShows").css("height",nowHeight-20);
		 }
	</script>
</body>
</html>


