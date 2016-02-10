<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page import="com.vw.visitreporting.entity.referencedata.Organisation"%>
<%@page import="com.vw.visitreporting.entity.referencedata.enums.Level"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta name="menu" content="admin" />
	<meta name="menuItem" content="orgLevels" />
	<title>Organisations</title>
	<meta name="subTitle" content="View Available Levels" />
	<style type="text/css">
	.levelCell {
		text-align:center;
	}
	.orgHeadingCell {
		vertical-align:bottom;
	}
	</style>
</head>

<body>
	<div class="row">
		<div class="span11">
		
			<table class="zebra-striped">
				<thead>
					<tr>
						<th rowspan="2" class="orgHeadingCell">Organisation</th>
						<th colspan="${fn:length(levels)}" class="levelCell">Levels</th>
					</tr>
					<tr>
						<c:forEach items="${levels}" var="level">
							<th class="levelCell">${level.name}</th>
						</c:forEach>					
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${organisations}" var="organisation">
						<tr>
							<td>${organisation.name}</td>
							<c:forEach items="${levels}" var="level">
								<td class="levelCell">${fn:contains(organisation.levels,level)?'Yes':'No'}</td>
							</c:forEach>
						</tr>
					</c:forEach>
					<tr>
						<td colspan="${fn:length(levels)+1}">
							<div>
								<c:set var="pageUrl" scope="request"><c:url value="create" /></c:set>
								<form:form commandName="crudObj" action="${pageUrl}" method="GET">
									<div class="actions">
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<a href="<c:url value="/content/organisation/levels/list.pdf" />" class="btn">Export to pdf</a>&nbsp;
										<a href="<c:url value="/content/organisation/levels/list.xls" />" class="btn">Export to xls</a>&nbsp;
										<a href="<c:url value="/content/organisation/levels/list.xls" />" class="btn">Export to csv</a>&nbsp;
									</div>
								</form:form>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="span4 offset1">
			<div class="span4 offset1">
				<h3>Help</h3>
				<p>This table displays the possible levels of access that a users position can be assigned to</p>
				<p>These levels grant access to data and functionality within the system related to the following scopes:</p>
				<ol>
					<li>UK-wide</li>
					<li>Regional</li>
					<li>Area</li>
					<li>Franchise Group</li>
					<li>Dealership</li>
				</ol>
			</div>
		</div>
	</div>
</body>

</html>