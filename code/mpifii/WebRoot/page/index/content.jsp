<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a></li>
	</ul>
</div>

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
</script>