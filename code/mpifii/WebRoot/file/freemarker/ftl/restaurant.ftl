<?xml version="1.0" encoding="UTF-8"?>
<xml-body>
<#list menuTypelist as menuType> 
    <food_type>
		<name>${(menuType.name)!}</name>
		<#list menulist as menu>
<#if menu.type=menuType.id>
		<food>
			<id>${(menu.id)!}</id>
			<logo>${(menu.icon)!}</logo>
			<name>${(menu.name)!}</name>
			<oldPrice>${menu.old_price!'0'}</oldPrice>
			<newPrice>${menu.new_price!'0'}</newPrice>
			<eatTimes>${menu.times!'0'}</eatTimes>
			<hot>0</hot>
		</food>
</#if>
		</#list>
    </food_type>
</#list>
</xml-body>
