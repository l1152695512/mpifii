<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


	<style type="text/css">
<<<<<<< HEAD
		#allmap {width:100%;height:450px;}
	</style>
	<title>地图定位</title>

	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" >
				<h2>点击定位</h2>
				<div class="box-icon">
					<input type="text" id="inAddr" class="input-xlarge" maxlength="50" vMin="3" vType="length" placeholder='输入地址定位' onblur="onblurVali(this);">
					<button class="btn btn-primary" type="button" onclick="find('f')">查询</button>	
					<button class="btn btn-primary" type="button" onclick="find('o')">确定</button>
				</div>
			</div>
			<div class="box-content">
				<div id="allmap"></div>
			</div>
		</div>
		<!--/span-->
	</div>
	
=======
		#allmap {width:100%;height:500px;}
	</style>
	<title>地图定位</title>

	<div id="allmap"></div>
	<p>点击地图展示详细地址</p>
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901

<script type="text/javascript">
	// 百度地图API功能
	var map = new BMap.Map("allmap");
<<<<<<< HEAD
	map.enableScrollWheelZoom();     
	map.addControl(new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT}));
	map.addControl(new BMap.NavigationControl());
	map.disableAutoResize();
	var geoc = new BMap.Geocoder();  
	if('${lng}' != ""){
		var point = new BMap.Point('${lng}', '${lat}');
		map.centerAndZoom(point, 16);
		map.addOverlay(new BMap.Marker(point)); 
	}else{
		map.centerAndZoom("南昌",12);
=======
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
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
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
	
	
<<<<<<< HEAD
	
	function find(v){
		window.map.clearOverlays();  
		var inAddr = $("#inAddr").val();
		if(inAddr != ''){
			window.geoc.getPoint(inAddr, function(point){
				if (point) {
					window.map.centerAndZoom(point, 16);
					window.map.addOverlay(new BMap.Marker(point));
					if(v=='o'){
						window.geoc.getLocation(point, function(rs){
							var addComp = rs.addressComponents;
							var addr = addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;
							
							myConfirm(addr,function(){
								getAddr(point.lng,point.lat,addr);
							});
						}); 
					}
				}
			}, "江西省");
		}
		
	}
	
	
=======
>>>>>>> b48516a961edf89e15d5b6cd3ea0be5952846901
	</script>
