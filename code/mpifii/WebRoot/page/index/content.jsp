<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a></li>
	</ul>
</div>
<div class="sortable row-fluid">
	<div style=" height: 750px; border: #ccc solid 1px;" id="dituContent"></div>
</div>
<script type="text/javascript">
	var refreshInterval = 300000;
	var statusTimeOut;
	var map = new BMap.Map("dituContent");
	map.centerAndZoom("${city}",12);
	map.enableScrollWheelZoom();     
	map.addControl(new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT}));
	map.addControl(new BMap.NavigationControl());
	var geoc = new BMap.Geocoder(); 
	var geolocation = new BMap.Geolocation();
	geolocation.getCurrentPosition(function(r){
		if(this.getStatus() == BMAP_STATUS_SUCCESS){
			map.panTo(r.point);
		}
		else {
		}        
	},{enableHighAccuracy: true})
	
	
	var hight;
	var myChart;
	//**************************统计控件开始********************************
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
		$(div).css({"color": "gray","cursor": "default","right": "0px","background": "rgba(255, 255, 255, 0.8)","filter": "alpha(opacity=80)"});
// 		$(div).html("网点总量：0<br>点亮网点：0<br> 未点亮：0<br>在线总人数：0<br>今日总人流：0<br>昨日总人流：0");
		try{
			clearTimeout('startTime()');
		}catch(e){}
		startTime();
		$.ajax({
			type: "POST",
			global: false,
// 			async:false,
			url: "open/getTotal",
			data:{admin:"no"},
			success: function(data,status,xhr){
				if("success" == status){
					var xmlDate = data.sonTot;
					hight = data.hight;
					if(hight<=50){
						hight = hight+10;
					}
					var html = "<div class='nav-collapse sidebar-nav' style='padding: 10px'>";
					
					html = html + "<p id='time22' style='text-align: center;font-size:10px;font-family:微软雅黑'></p>";
					html = html + "<p style='text-align: center;color:#2079D4;font-weight: bold;font-size:16px;font-family:微软雅黑'>"+data.orgName+"运营数据</p>";
					
					html = html + "<p style='font-size:14px;font-family:微软雅黑'>网点总量："+data.shopNum+"</p>";
					html = html + "<p style='font-size:14px;font-family:微软雅黑'>在线网点："+data.onlineShopNum+"</p>";
					html = html + "<p style='font-size:14px;font-family:微软雅黑'>离线网点："+data.offlineShopNum+"</p>";
					html = html + "<p style='font-size:14px;font-family:微软雅黑'>在线总人数："+data.onlineNum+"</p>";
					html = html + "<p id='today_person' style='font-size:14px;font-family:微软雅黑'>今日总人流："+data.tNum+"</p>";
					html = html + "<p style='font-size:14px;font-family:微软雅黑'>昨日总人流："+data.yNum+"</p>";
					
					html = html + "<div id='chartdivv'/>";
					html = html + "</div>";
					$(div).html(html);
					myChart = new FusionCharts('file/charts/Bar2D.swf', 'map_chart_2088', "250",hight);
					myChart.setTransparent(true);
					
					var tilthtml = "<chart bgAlpha='0' canvasBgAlpha='0' showplotborder='0' baseFont='微软雅黑' baseFontSize='12' baseFontColor='808080' showBorder='1' showYAxisValues='0'  bgcolor='FFFFFF' showalternatevgridcolor='0' showplotborder='0'  divlinecolor='CCCCCC' tooltipbordercolor='FFFFFF' palettecolors='008ee4' canvasborderalpha='1' showborder='0'>";
					tilthtml = tilthtml + xmlDate;
					tilthtml = tilthtml + "</chart>";
					myChart.setDataXML(tilthtml);
					if(hight>10){
						myChart.render("chartdivv");
					}
// 					$.ajax({//单独加载今日总人流，否则整体出来的特别慢
// 						type: "POST",
// 						global: false,
// //			 			async:false,
// 						url: "open/getTotal",
// 						data:{admin:"no",type:"todayPerson"},
// 						success: function(data,status,xhr){
// 							$("#today_person").text("今日总人流："+data.tNum); 
// 						}
// 					});
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
	function startTime(){ 
		if(undefined != $("#time22")){
			$("#time22").text(new Date().toLocaleString()); 
			setTimeout('startTime()',1000); 
		}
	} 
	//**************************统计控件结束********************************
	//**************************快速定位控件开始********************************
	// 定义一个控件类,即function
	function PosControl(){
	  // 默认停靠位置和偏移量
	  this.defaultAnchor = BMAP_ANCHOR_TOP_LEFT;
	  this.defaultOffset = new BMap.Size(200, 0);
	}
	// 通过JavaScript的prototype属性继承于BMap.Control
	PosControl.prototype = new BMap.Control();
	// 自定义控件必须实现自己的initialize方法,并且将控件的DOM元素返回
	// 在本方法中创建个div元素作为控件的容器,并将其添加到地图容器中
	PosControl.prototype.initialize = function(map){
	  // 创建一个DOM元素
	  var div1 = document.createElement("div1");
	  // 设置样式
	  $(div1).html("<input type='text' id='faddr' placeholder='右键输入地址快速定位' class='input-xlarge'/> <button type='button' class='btn' style='position: relative;top: -5px;left: -28px;' onclick='find()'>快速定位</button>");
	  
	  // 添加DOM元素到地图中
	  map.getContainer().appendChild(div1);
	  // 将DOM元素返回
	  return div1;
	}
	// 创建控件
	var myPosCtrl = new PosControl();
	// 添加到地图当中
	map.addControl(myPosCtrl);
	//**************************快速定位控件结束********************************

	function find(){
		var inAddr = $("#faddr").val();
		if(inAddr != ''){
			window.geoc.getPoint(inAddr, function(point){
				if (point) {
					window.map.centerAndZoom(point, 16);
				}
			}, "江西省");
		}
	}
	
	
	$.ajax({
		type: "POST",
		global: false,
		url: "open/getShop",
		success: function(data,status,xhr){
			var shopList = data;
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
			var label;
			for(var i=0;i<shopList.length;i++){
				var shop = shopList[i];
				var marker;
			  	if(shop.online>0){
			  		marker= new BMap.Marker(new BMap.Point(shop.lng, shop.lat),{icon:onIcon});
			  	}else{
			  		marker= new BMap.Marker(new BMap.Point(shop.lng, shop.lat),{icon:offIcon});
			  	}
			  	map.addOverlay(marker); 
			  	shopList[i].marker = marker;
 				marker.addEventListener("mouseover",mouseover);
 				marker.addEventListener("mouseout",mouseout);
				marker.addEventListener("click",click);
			}
			
			
			function mouseover(e){
				var thisMarker = getMarker(this);
				var opts2 = {
				  position : new BMap.Point(thisMarker.lng, thisMarker.lat),    // 指定文本标注所在的地理位置
				  offset   : new BMap.Size(10, -10)    //设置文本偏移量
				}
				label = new BMap.Label(thisMarker.name, opts2);  // 创建文本标注对象
				label.setStyle({
					 color : "black",
					 fontSize : "12px",
					 height : "20px",
					 lineHeight : "20px",
					 fontFamily:"微软雅黑"
				});
				map.addOverlay(label); 
			}
			
			function mouseout(e){
				map.removeOverlay(label);
			}
			
			
			function click(e){
				var thisMarker = getMarker(this);
				var onlineNum = 0;
				$.ajax({
					type: "POST",
					global: false,
					async:false,
					url: "open/getOnLineTotal",
					data: {shopId:thisMarker.id},
					success: function(data,status,xhr){
						if("success" == status){
							onlineNum = data.onlineNum;
						}
					}
				});
				
				this.openInfoWindow(new BMap.InfoWindow("名称："+thisMarker.name+"</br>在线："+onlineNum+"人</br>地址："+thisMarker.addr, opts));
			}
			
			function getMarker(marker){
				for(var i=0;i<shopList.length;i++){
					if(shopList[i].marker == marker){
						return shopList[i];
					}
				}
			}
		}
	})
	
</script>