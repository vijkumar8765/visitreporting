<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="com.vw.visitreporting.common.AppInfo"%>
<%@page session="true" %>
<%

	//TODO: remove this  when pre-authentication solution is in place

	pageContext.setAttribute("showLoginForm", AppInfo.isNoPreAuth());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<title>${showLoginForm ? 'Quick Login' : 'Access Denied'}</title>
</head>

<body>

	<c:if test="${fn:contains(sessionScope.SPRING_SECURITY_403_EXCEPTION, 'AccessDeniedException')}">
		<p>You do not have permission to perform this operation.</p>
		<p>If you feel that you should have access to this function, please contact you local Visit Reporting administrator or <a href="mailto:Support.vwg@vwg.co.uk?Subject=Visit Reporting Access Denied&Body=Hi,%0D%0A%0D%0AI believe that I should have access to do the following in Visit Reporting..%0D%0A%0D%0AWho should I contact to get this access?">Support.vwg@vwg.co.uk</a> if you do not know who your local administrator is.</p>
		<p>Click <a href="javascript:history.go(-1)">here</a> to navigate back to the page you were on previously.</p>
	</c:if>

	<c:if test="${! showLoginForm and empty sessionScope.SPRING_SECURITY_LAST_EXCEPTION}">
		<p>Click <a href="<c:url value="/content/home" />">here</a> for main page.</p>
		<p>If this page is unavailable please contact Support.</p>
	</c:if>

	<c:if test="${fn:contains(sessionScope.SPRING_SECURITY_LAST_EXCEPTION, 'AuthenticationCredentialsNotFoundException')}">
		<p>The system is currently un-available due to a technical failure.</p>
		<p>Please try again later or contact Support if the problem persists.</p>
	</c:if>
	<c:if test="${fn:contains(sessionScope.SPRING_SECURITY_LAST_EXCEPTION, 'AuthenticationServiceException')}">
		<p>The system is currently un-available due to a technical failure.</p>
		<p>Please try again later or contact Support if the problem persists.</p>
	</c:if>
	
	<c:if test="${fn:contains(sessionScope.SPRING_SECURITY_LAST_EXCEPTION, 'UsernameNotFoundException')}">
		<p>Your details could not be found in the system.</p>
		<p>Please contact the system administrator to request access.</p>
	</c:if>

	<c:if test="${fn:contains(sessionScope.SPRING_SECURITY_LAST_EXCEPTION,'DisabledException')}">
		<p>Your account in this system has been disabled.</p>
		<p>Please contact the system administrator to request access.</p>
	</c:if>

	<!-- else -->
	<c:if test="${not empty sessionScope.SPRING_SECURITY_LAST_EXCEPTION && ! fn:contains(sessionScope.SPRING_SECURITY_LAST_EXCEPTION,'AuthenticationCredentialsNotFoundException') && ! fn:contains(sessionScope.SPRING_SECURITY_LAST_EXCEPTION,'AuthenticationServiceException') && ! fn:contains(sessionScope.SPRING_SECURITY_LAST_EXCEPTION,'UsernameNotFoundException') && ! fn:contains(sessionScope.SPRING_SECURITY_LAST_EXCEPTION,'DisabledException')}">
		<p>The system is currently un-available due to a technical failure.</p>
		<p>Please try again later or contact Support if the problem persists.</p>
	</c:if>
	
	
	<c:if test="${showLoginForm}">
		<%
			session.removeAttribute("userId");
			session.invalidate();
		%>
		<form id="loginForm" action="#" method="get">
			<fieldset>
				<div class="clearfix">
					<label for="userIdSelect">Preset:</label>
					<div class="input">
						<select id="userIdSelect" size="10" multiple onchange="alert(document.getElementById('userIdSelect').value);alert(document.getElementById('loginForm').action);document.getElementById('userId').value=this.value; document.getElementById('loginForm').submit();">
							<option value="dba">DBA</option>
							<option value="fox0jc1">Jerry</option>
							<option value="fox0shn">Neraj</option>
							<option value="fox0bd5">Datta</option>
							<option value="fox0cpi">Chris</option>
							<option value="fox0bvi">Vijay</option>
							<option value="fox0kh8">Hemanth</option>
						</select>
					</div>
				</div>
				
				<div class="clearfix">
					<label for="userId">Username:</label>
					<div class="input">
						<input type="text" id="userId" name="userId" value="" />
						<span class="help-inline">any windows username</span>
					</div>
				</div>
				
				<div class="actions">
					<input type="submit" class="btn primary" value="Login" />
				</div>	
			</fieldset>
		</form>
	</c:if>
</body>

</html>