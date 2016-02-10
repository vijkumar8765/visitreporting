<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page import="java.util.Set"%>
<%@page import="java.util.List"%>
<%@page import="com.vw.visitreporting.entity.referencedata.enums.Brand"%>
<%@page import="com.vw.visitreporting.entity.referencedata.Organisation"%>
<%@page import="com.vw.visitreporting.web.form.OrganisationBrand"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta name="menu" content="admin" />
	<meta name="menuItem" content="sharingRules" />
	<meta name="helpAnchor" content="sharing" />
	<title>Organisations</title>
	<meta name="subTitle" content="Edit Information Sharing Rules" />
</head>

<body>
	<div class="span12">
		<form action="save" method="post">
			<fieldset>

				<c:set var="pageUrl" scope="request"><c:url value="/content/organisation/" /></c:set>

				<div class="clearfix">
					<label for="rootOrgBrand">Organisation:</label>
					<div class="input">
						<c:set var="orgChooser_elementId" value="rootOrgBrand" />
						<c:set var="orgChooser_onchange" value="if(document.getElementById('rootOrgBrand').value!='') location.href='${pageUrl}'+document.getElementById('rootOrgBrand').value+'/sharingrules/update';" />
						<%@ include file="/WEB-INF/jsp/components/orgChooser.jspf" %>
						<c:if test="${not empty rootOrgBrand}">
							<c:if test="${not empty modifiedBy}">
								<span class="help-block">Last updated by: ${modifiedBy} on ${modifiedDate}</span>
							</c:if>
							<c:if test="${empty modifiedBy}">
								<span class="help-block">Never been updated</span>
							</c:if>
						</c:if>
					</div>
				</div>
				
				<c:if test="${not empty rootOrgBrand}">
				
					<div class="clearfix">
						<label for="name">Shares information with:</label>
						<div class="input">
							<ul class="inputs-list">
								<c:forEach items="${organisationBrands}" var="orgBrand">
									<li>
										<label>
											<input type="checkbox" name="targetOrgBrands" id="targetOrgBrands" value="${orgBrand.id}" ${fn:contains(targetOrgBrands,orgBrand)?'checked':''} />
											<span>${orgBrand}</span>
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
		<p>Select an organisation and brand to view which other organisations have access to data related to it.</p>
		<p>Tick or un-tick the boxes next to each organisation and brand as appropriate to grant or revoke access to data for users belonging to the that organisation and brand.</p>
	</div>

</body>

</html>