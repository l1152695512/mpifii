<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


	<style type="text/css">
		#allmap {width:100%;height:500px;}
	</style>
	<title>地图定位</title>

	<div id="allmap"></div>
	<p>点击地图展示详细地址</p>

<script type="text/javascript">
	// 百度地图API功能
	var map = new BMap.Map("allmap");
	var point = new BMap.Point(102.700582,25.04642);
	map.centerAndZoom(point,12);
	map.enableScrollWheelZoom();     
	map.addControl(new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT}));
	map.addControl(new BMap.NavigationControl());
	var geoc = new BMap.Geocoder();    
	
	if('${lng}' != ""){
		var marker = new BMap.Marker(new BMap.Point('${lng}', '${lat}'));
		map.addOverlay(marker); 
		marker.setAnimation(BMAP_ANIMATION_BOUNCE);
	}else{
		map.clearOverlays();   
	}

	map.addEventListener("click", function(e){        
		var pt = e.point;
		geoc.getLocation(pt, function(rs){
			var addComp = rs.addressComponents;
			var addr = addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;
			
			myConfirm(addr,function(){
				getAddr(pt.lng,pt.lat,addr);
			});
		});        
	});
	
	
	</script>
