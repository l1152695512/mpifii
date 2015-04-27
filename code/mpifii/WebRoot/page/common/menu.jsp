<%@ page language="java" contentType="text/html; charset=UTF-8"
	deferredSyntaxAllowedAsLiteral="true" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!-- left menu starts -->
<div class="span2 main-menu-span">
	<div class="well nav-collapse sidebar-nav">
		<ul class="nav nav-tabs nav-stacked main-menu">
			<c:forEach items="${menuList}" var="menu">
				<c:if test="${menu.children== null}">
					<li class="nav-header hidden-tablet">${menu.text}</li>
				</c:if>
				<c:if test="${menu.children!= null && fn:length(menu.children) > 0}">
					<li class="nav-header hidden-tablet">${menu.text}</li>
					<c:forEach items="${menu.children}" var="children">
						<li>
							<a href="javascript:void(0);" onclick="ajaxContent('${children.attributes.get('url')}');" > 
								<i class="icon-th"></i>
							 	<span class="hidden-tablet">${children.text}</span>
							</a>
						</li>
					</c:forEach>
				</c:if>
			</c:forEach>
		</ul>
		
	</div>
	<!--/.well -->
</div>
<!--/span-->
<!-- left menu ends -->

<%-- class="ajax-link" target="main_content" name="${CONTEXT_PATH}${children.attributes.get('url')}"  --%>
