<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.vw.visitreporting.security.authentication.UserDetailsImpl"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="org.springframework.security.core.Authentication"%>
<jsp:useBean id="now" class="java.util.Date" /><%

	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	if(auth != null) {
		pageContext.setAttribute("userDetails", auth.getPrincipal());
	}
	
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<title>Error</title>
</head>

<body>
	<p>An error has occured</p>
	
	<p>Click <a href="javascript:history.go(-1)">here</a> to navigate back to the page you were on previously.</p>
	
	<c:if test="${empty userDetails}">
		<p>If this problem persists, please contact <a href="mailto:Support.vwg@vwg.co.uk?Subject=Visit Reporting Error&Body=Hi,%0D%0A%0D%0AI have been experiencing problems with the Visit Reporting application.%0D%0A%0D%0ACould you please look into this issue?">Support.vwg@vwg.co.uk</a></p>
	</c:if>
	<c:if test="${not empty userDetails}">
		<p>If this problem persists, please contact <a href="mailto:Support.vwg@vwg.co.uk?Subject=Visit Reporting Error&Body=Hi,%0D%0A%0D%0AI have been experiencing problems with the Visit Reporting application.%0D%0A%0D%0AMy user id is ${userDetails.user.userId} and the error occured at ${now}.%0D%0A%0D%0ACould you please look into this issue?%0D%0A%0D%0AThanks,%0D%0A${userDetails.user.firstName}">Support.vwg@vwg.co.uk</a></p>
	</c:if>
</body>

</html>
