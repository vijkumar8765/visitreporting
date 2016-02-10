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
	<meta name="menuItem" content="profilePermissions" />
	<meta name="helpAnchor" content="functions" />
	<title>User Profiles</title>
	<meta name="subTitle" content="Edit Access to System Functions" />
</head>

<body>
	<div class="span12">
		<form action="save" method="post">
			<fieldset>

				<div class="clearfix">
					<label for="profileId">Name:</label>
					<div class="input">
						<select name="profileId" id="profileId" onchange="if(document.getElementById('profileId').value!='') location.href='<c:url value="/content/userprofile/" />'+document.getElementById('profileId').value+'/permissions/update';">
							<option value="">&nbsp;</option>
							<c:forEach items="${userProfiles}" var="profile">
								<option value="${profile.id}" ${profile.id==profileId?'selected':''}>${profile.name}</option>
							</c:forEach>
						</select>
						<c:if test="${not empty profileId}">
							<c:if test="${not empty modifiedBy}">
								<span class="help-block">Last updated by: ${modifiedBy} on ${modifiedDate}</span>
							</c:if>
							<c:if test="${empty modifiedBy}">
								<span class="help-block">Never been updated</span>
							</c:if>
						</c:if>
					</div>
				</div>

				<c:if test="${not empty profileId}">
				
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
		<p>Select a user profile to view which functionality its users have access to.</p>
		<p>Tick or un-tick the boxes next to each function as appropriate to grant or revoke access to that function for users assigned to that user profile.</p>
		<p>This will affect the functionality within the application that a user with this profile is able to access.</p>
	</div>

</body>

</html>