<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a></li>
	</ul>
</div>
<<<<<<< HEAD
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
					html = html + "<p id='today_person' style='font-size:14px;font-family:微软雅黑'>今日总人流：加载中</p>";
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
					$.ajax({//单独加载今日总人流，否则整体出来的特别慢
						type: "POST",
						global: false,
//			 			async:false,
						url: "open/getTotal",
						data:{admin:"no",type:"todayPerson"},
						success: function(data,status,xhr){
							$("#today_person").text("今日总人流："+data.tNum); 
						}
					});
// 					myChart.setDataXML("<chart bgAlpha='0' canvasBgAlpha='0' showplotborder='0' divLineAlpha='0' palettecolors='008ee4' canvasborderalpha='1' baseFont='微软雅黑' baseFontSize='12' baseFontColor='2079D4' shadowAlpha='0' showYAxisValues='0' canvasborderalpha='0' formatNumberScale='0' showvalues='0' showborder='0'><set label='没有用' value='44' /><set label='测试用' value='33' /><set label='演示用' value='22' /></chart>");
// 					myChart.render("chartdivv");
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
	var shopList = $.parseJSON('${shopList}');
	for(var i=0;i<shopList.length;i++){
		var marker;
		var options = {};
	  	if(shopList[i].online>0){
	  		options.icon = onIcon;
	  	}else{
	  		options.icon = offIcon;
	  	}
	  	marker= new BMap.Marker(new BMap.Point(shopList[i].lng, shopList[i].lat),options);
	  	map.addOverlay(marker);
	  	shopList[i].markerR = marker.R;
		marker.addEventListener("mouseover",function(){
			var thisMarker = getMarker(this.R);
			var opts2 = {
			  position : this.point,    // 指定文本标注所在的地理位置
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
			
			this.addEventListener("mouseout", 
				function(){
					map.removeOverlay(label);
				}
			);
		});
		marker.addEventListener("click", function(){
			var thisMarker = getMarker(this.R);
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
		});
	}
	function getMarker(markerR){
		for(var i=0;i<shopList.length;i++){
			if(shopList[i].markerR == markerR){
				return shopList[i];
			}
		}
	}
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
	
=======

<div class="sortable row-fluid">
	<div style=" height: 800px; border: #ccc solid 1px;" id="dituContent"></div>
</div>
<script type="text/javascript">
var opts = {
	      width : 100,     // 信息窗口宽度
	      height: 50    // 信息窗口高度
	    }
	    //创建和初始化地图函数：
	    function initMap() {
	        createMap(); //创建地图
	        setMapEvent(); //设置地图事件
	        addMapControl(); //向地图添加控件
	    }

	    //创建地图函数：
	    function createMap() {
	        var map = new BMap.Map("dituContent"); //在百度地图容器中创建一个地图
	        /* var point = new BMap.Point(25.03,102.72); //定义一个中心点坐标,彭州市人民政府 */
	        map.centerAndZoom('昆明', 14); //设定地图的中心点和坐标并将地图显示在地图容器中，5代表级别，级别越高地图越详细，1.2支持1到19级，1.1支持1到18，
	                                   //本例为1.1，src="http://api.map.baidu.com/api?key=&v=1.1,而1.2支持中文，map.centerAndZoom('北京');
	        window.map = map; //将map变量存储在全局
	        
	        //向地图添加标注
	        var bounds = map.getBounds();    //得到地图边界有maxx minx maxy miny
	        var point = new BMap.Point(116.326655,39.902095);
	        var marker = new BMap.Marker(point);
	        var dataUrl = "/business/shop/getAllShopInfo";
	        $.ajax({
				type : 'POST',
				dataType : "json",
				url :encodeURI(encodeURI(cxt + dataUrl)),
				success : function(data) {
					$(data).each(function(i) { 
						var lng = data[i].lng;
						var lat = data[i].lat;
						var name = data[i].name;
						var addr = data[i].addr==null?'暂无':data[i].addr;
						var tel = data[i].tel==null?'暂无':data[i].tel;
 						var myIcon = new BMap.Icon(cxt+"/images/flag.png", new BMap.Size(15, 15), {
					            offset: new BMap.Size(10, 25),                  // 指定定位位置
					       }); 
						var point =  new BMap.Point(lng, lat); 
						var marker = new BMap.Marker(point,{icon:myIcon});
					    var label = new BMap.Label(name,{"offset":new BMap.Size(9,-15)});
					    marker.setLabel(label);
					    map.addOverlay(marker);
					    marker.addEventListener("click", function(){this.openInfoWindow(new BMap.InfoWindow("地址:"+addr+"</br>Tel:"+tel, opts));});
					}); 
				}
			});
	    }
	    
	    // 编写自定义函数,创建标注
	    function addMarker(point, index) {
	        var myIcon = new BMap.Icon(encodeURI(encodeURI(cxt + "/images/flag.png")), new BMap.Size(23, 25), {
	            offset: new BMap.Size(10, 25),                  // 指定定位位置
	            imageOffset: new BMap.Size(0, 0 - index * 25)   // 设置图片偏移
	        }); 
	        var marker = new BMap.Marker(point, { icon:myIcon});
	        map.addOverlay(marker);
	    }

	    //地图事件设置函数：
	    function setMapEvent() {
	        map.enableDragging(); //启用地图拖拽事件，默认启用(可不写)
	        map.enableScrollWheelZoom(); //启用地图滚轮放大缩小
	        map.enableDoubleClickZoom(); //启用鼠标双击放大，默认启用(可不写)
	        map.enableKeyboard(); //启用键盘上下左右键移动地图
	    }

	    //地图控件添加函数：
	    
	    function addMapControl() {
	        //向地图中添加缩放控件
	        var ctrl_nav = new BMap.NavigationControl({ anchor: BMAP_ANCHOR_TOP_LEFT, type: BMAP_NAVIGATION_CONTROL_LARGE });
	        map.addControl(ctrl_nav);
	        //向地图中添加缩略图控件
	        var ctrl_ove = new BMap.OverviewMapControl({ anchor: BMAP_ANCHOR_BOTTOM_RIGHT, isOpen: 1 });
	        map.addControl(ctrl_ove);
	        //向地图中添加比例尺控件
	        var ctrl_sca = new BMap.ScaleControl({ anchor: BMAP_ANCHOR_BOTTOM_LEFT });
	        map.addControl(ctrl_sca);
	    }
	    initMap(); //创建和初始化地图
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
</script>