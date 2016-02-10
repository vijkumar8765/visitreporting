<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %><%
%><%@page import="com.vw.visitreporting.common.AppInfo"%><%
%><%@page import="com.vw.visitreporting.security.authentication.UserDetailsImpl"%><%
%><%@page import="org.springframework.security.core.context.SecurityContextHolder"%><%
%><%@page import="org.springframework.security.core.Authentication"%><%
%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%

	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	if(auth != null) {
		pageContext.setAttribute("userDetails", auth.getPrincipal());
	}
	pageContext.setAttribute("appVersion", AppInfo.getVersion());
	
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
    <meta http-equiv="content-type" content="text/html;charset=UTF-8" />
    <title>Visit Reporting ${appVersion} - <decorator:title /> - <decorator:getProperty property="meta.subTitle" /></title>
 
	<jsp:include page="/WEB-INF/jsp/template/css.jsp" />
	<jsp:include page="/WEB-INF/jsp/template/js.jsp" />

	<decorator:head />
</head>


<body>
	<div class="topbar" data-dropdown="dropdown">
		<div class="topbar-inner">
			<div class="container">
				<h3><a class="brand" href="<c:url value="/" />">Visit Reporting</a></h3>
				
				<c:set var="selectedMenu" scope="request"><decorator:getProperty property="meta.menu" /></c:set>
				<c:set var="selectedMenuItem" scope="request"><decorator:getProperty property="meta.menuItem" /></c:set>
				<c:set var="helpAnchor" scope="request"><decorator:getProperty property="meta.helpAnchor" /></c:set>
				
				<%@ include file="/WEB-INF/jsp/template/menu.jspf" %>
				
				<c:if test="${not empty helpAnchor}">
					<ul class="nav secondary-nav">
						<li><a target="_blank" href="<c:url value="/static/pages/help.html" />#${helpAnchor}" style="padding:0px;"><img style="margin-top:2px;" src="<c:url value="/static/images/help.png" />" /></a></li>
					</ul>
				</c:if>
				<c:if test="${not empty userDetails}">
					<ul class="nav secondary-nav">
						<li class="dropdown">
							<a href="#" class="dropdown-toggle">${userDetails.user}</a>
							<ul class="dropdown-menu">
								<li><a href="<c:url value="/logout.jsp" />">Logout</a></li>
							</ul>
						</li>
					</ul>
					<p class="pull-right">Logged in as</p>
				</c:if>
			</div>
		</div>
	</div>
	
	<div class="container">
		<div class="content">
		
			<div class="row">
				<div class="span16">
					<h1><decorator:title /> <small><decorator:getProperty property="meta.subTitle" /></small></h1>
				</div>
			</div>
	
			<div class="row">
				<jsp:include page="/WEB-INF/jsp/pages/messages.jsp"/>
			
				<decorator:body />
			</div>
				
			<div class="row">
				<div class="span16">
					<hr />
					<footer><!-- footer is an HTML5 tag so will xhtml DTD will complain about it -->
						<p>&copy; Volkswagen Group UK Limited  2011</p>
					</footer>
				</div>
			</div>
			
		</div>
	</div>

</body>
</html>