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
	<title>Access Denied</title>
</head>

<body>
	<p>You do not have permission to perform this operation.</p>

	<p>If you feel that you should have access to this function, please contact you local Visit Reporting administrator or <a href="mailto:Support.vwg@vwg.co.uk?Subject=Visit Reporting Access Denied&Body=Hi,%0D%0A%0D%0AI believe that I should have access to do the following in Visit Reporting..%0D%0A%0D%0AWho should I contact to get this access?">Support.vwg@vwg.co.uk</a> if you do not know who your local administrator is.</p>
	
	<p>Click <a href="javascript:history.go(-1)">here</a> to navigate back to the page you were on previously.</p>
</body>

</html>
