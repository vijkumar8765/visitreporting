<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page import="com.vw.visitreporting.entity.referencedata.MeetingType"%>
<%@page import="java.util.Enumeration"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta name="menu" content="admin" />
	<meta name="menuItem" content="meetingTypes" />
	<title>Meeting Types</title>
	<meta name="subTitle" content="Edit" />
</head>

<body>
	<div class="row">
		<div class="span6">
		
			<table class="zebra-striped">
				<thead>
					<tr>
						<th colspan="2">Types of Meeting</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="item">
						<tr>
							<td>${item}</td>
							<td><a href="${item.id}/delete">remove</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
		</div>
		<div class="span4 offset6">
			<h3>Help</h3>
			<p>When creating an agenda or visit report, you will be provided with this list of meeting types to categorise that meeting.</p>
			<p>This list can be modified, by clicking the <i>remove</i> links next to existing meeting types or entering a new meeting type in the form below and clicking Add.</p>
		</div>
	</div>
	<div class="row">
		<div class="span16">

			<h3>Add Meeting Type</h3>
			<form:form commandName="crudObj" action="save" method="POST">

				<fieldset>
					<spring:bind path="name">
						<div class="clearfix ${status.error?'error':''}">
							<form:label path="name">Name:</form:label>
							<div class="input">
								<form:input path="name" />
								<form:errors path="name" cssClass="help-inline" />
								<c:if test="${not status.errors.hasErrors()}">
									<span class="help-inline">display name (between 2-200 characters)</span>
								</c:if>
							</div>
						</div>
					</spring:bind>
				</fieldset>

				<div class="actions">
					<input type="submit" class="btn primary" value="Add" />
				</div>
			</form:form>
		</div>		
	</div>
</body>

</html>