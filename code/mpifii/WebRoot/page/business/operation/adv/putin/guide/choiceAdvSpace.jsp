<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#adv_putin_space li{float: left;width: 32%;margin: 10px 2% 20px 3%;}
	#adv_putin_space li div{opacity: 0.7;filter: alpha(opacity=70);cursor: pointer;}
	#adv_putin_space li div:hover{opacity: 1;filter: alpha(opacity=100);}
	#adv_putin_space li p{text-align: center;font-size: 15px;margin: 10px 0;}
	#adv_putin_space .holder{border-top: 1px solid rgb(241, 241, 241);padding-top: 20px;}
	#adv_putin_space li div.selected{opacity: 1;filter: alpha(opacity=100);}
</style>

<div id="adv_putin_space">
	<input type="hidden" name="adv_spaces_id">
	<ul id="adv_spaces_list"></ul>
	<div style="clear:both;"></div>
	<div class="holder"></div>
</div>
<script type="text/javascript">
	$("#adv_putin_space .holder").jPages({
		containerID:"adv_spaces_list",
		perPage : 3,
		startPage:1,
		keyBrowse : true,
		realPagination:true,
	    serverParams:{
	    	url:"business/oper/adv/putin/advSpacesList",
	    	generDataHtml:generAdvSpacesListData
	    }
	});
	function generAdvSpacesListData(data,searchParams){
		var recHtml = "";
		if(data.length > 0){
			var selectedAdvSpaces = $("#adv_putin_space input[name='adv_spaces_id']").val();
			for(var i=0;i<data.length;i++){
				var selected = "";
				if(data[i].id == selectedAdvSpaces){
					selected = "class='selected'";
				}
				recHtml += '<li data-id="'+data[i].id+'" data-adv-type="'+data[i].adv_type+'" data-imgs="'+data[i].imgs+'"><div '+selected+'><img src="'+data[i].preview_img+'"><p>'+data[i].name+'</p></div></li>';;
			}
		}else{
			recHtml = '<div><font color="red">没有可投放的广告位！</font></div>';
		}
		$("#adv_spaces_list").html(recHtml);
		$("#adv_spaces_list div").click(function(){
			$(this).addClass("selected").parent().siblings().find("div").removeClass("selected");
			$("#adv_putin_space input[name='adv_spaces_id']").val($(this).parent().data("id"));
			updateAdvPutinImgUpload($(this).parent().data("advType"),$(this).parent().data("imgs"),'','','','');
			advGuideNextStep();
		});
	}
	function checkAdvputinSpace(){
		if($("#adv_putin_space input[name='adv_spaces_id']").val() == ''){
			myAlert("请先选择广告位！");
			return true;
		}
		return false;
	}
</script>