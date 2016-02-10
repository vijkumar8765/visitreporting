<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="java.util.Set"%>
<%@page import="com.vw.visitreporting.entity.referencedata.enums.Function"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta name="menu" content="admin" />
	<meta name="menuItem" content="orgPermissions" />
	<meta name="helpAnchor" content="functions" />
	<title>Organisations</title>
	<meta name="subTitle" content="Edit Access to System Functions" />
</head>

<body>
	<div class="span12">
		<form action="save" method="post">
			<fieldset>

				<c:set var="pageUrl" scope="request"><c:url value="/content/organisation/" /></c:set>

				<div class="clearfix">
					<label for="organisationBrand">Organisation:</label>
					<div class="input">
					
						<c:set var="orgChooser_elementId" value="organisationBrand" />
						<c:set var="orgChooser_onchange" value="if(document.getElementById('organisationBrand').value!='') location.href='${pageUrl}'+document.getElementById('organisationBrand').value+'/permissions/update';" />
						<%@ include file="/WEB-INF/jsp/components/orgChooser.jspf" %>

						<c:if test="${not empty organisationBrand}">
							<c:if test="${not empty modifiedBy}">
								<span class="help-block">Last updated by: ${modifiedBy} on ${modifiedDate}</span>
							</c:if>
							<c:if test="${empty modifiedBy}">
								<span class="help-block">Never been updated</span>
							</c:if>
						</c:if>
					</div>
				</div>
				
				<c:if test="${not empty organisationBrand}">
				
					<div class="clearfix">
						<label for="name">Function Access</label>
						<div class="input">
							<ul class="inputs-list">
								<c:forEach items="${allFunctions}" var="func">
									<li>
										<label>
											<input type="checkbox" name="allowedFunctions" id="allowedFunctions" value="${func.id}" ${fn:contains(allowedFunctions,func)?'checked':''} />
											<span>${func.name}</span>
										</label>
									</li>
								</c:forEach>
							</ul>
						</div>
					</div>
					
					<div class="actions">
						<input type="submit" class="btn primary" value="Save" />
					</div>
					
				</c:if>
		
			</fieldset>
		</form>
	</div>				

	<div class="span4">
		<h3>Help</h3>
		<p>Select an organisation and brand to view which functionality its users have access to.</p>
		<p>Tick or un-tick the boxes next to each function as appropriate to grant or revoke access to that function for users belonging to the selected organisation and brand.</p>
	</div>

</body>

</html>