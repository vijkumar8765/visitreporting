<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page import="java.util.Enumeration"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta name="menu" content="admin" />
	<meta name="menuItem" content="positions" />
	<title>Edit Positions</title>
</head>

<body>
	<div class="row">
		<div class="span16">
			<table class="bordered-table zebra-striped">
				<thead>
					<tr>
						<th colspan="2">Organisation Details</th>
						<th colspan="1">Name of Contact</th>
						<th colspan="1">Position</th>
						<th colspan="3">Position Sequence</th>
						<th colspan="2">Landline Telephone <br> Number</th>
						<th colspan="1">Mobile Telephone <br> Number</th>
						<th colspan="1">Email Address</th>
						<th colspan="1">Effective Start Date <br> in current Position</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="item">
						<tr>
							<td colspan="2"><a href="${item.id}/update">${item.description}</a><br/>
							<c:if test="${empty item.modifiedBy}">
								<em class="help-inline">${item.createdBy} created on ${item.createdDate}</em>
							</c:if>
							<em class="help-inline">${item.modifiedBy} ${empty item.modifiedBy?'':'updated on'}  ${item.modifiedDate}</em>
							</td>
							<td colspan="1">${item.organisation.name}</td>
							<td colspan="1">
								<c:forEach items="${item.brands}" var="itemBrand">${itemBrand.shortName} <br/></c:forEach>
							</td>
							<td colspan="3">${fn:contains(item.visibleWithinContactScreen, true)?'Yes':'No'}</td>
							<td colspan="2">${item.level.value}</td>
							<td colspan="1">${empty item.positionSequence?'N/A':item.positionSequence}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="span16">
		<c:set var="pageUrl" scope="request"><c:url value="create" /></c:set>
			<form:form commandName="crudObj" action="${pageUrl}" method="GET">
				<div class="actions">
					<input type="submit" class="btn primary" value="Add New Position" />
				</div>
			</form:form>
		</div>
	</div>
</body>
</html>