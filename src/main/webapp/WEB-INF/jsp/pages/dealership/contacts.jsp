<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta name="dealer" content="admin" />
	<title>${dealership.dealerName}</title>
	<meta name="subTitle" content="Contacts" />
</head>

<body>

	<div class="row">
		<%@ include file="/WEB-INF/jsp/pages/dealership/header.jspf" %>
	
		<div class="span4">
			<h3>Help</h3>
			<p>Display the contact details for all people associated to this dealership.</p>
		</div>
	</div>

	<%@ include file="/WEB-INF/jsp/pages/dealership/tabs.jspf" %>

	<div class="row">
		<div class="span16">	      
			<table class="zebra-striped" id="dealerSearchResults">
				<thead>
					<tr>
						<th>Name</th>
						<th>Position</th>
						<th>Telephone</th>
						<th>Mobile</th>
						<th>Email</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${contacts}" var="item">
						<tr>
							<td>${(empty item.fullname) ? 'unavailable' : item.fullname}</td>
							<td>${(empty item.positions) ? 'unavailable' : item.positions}</td>
							<td>${(empty item.landline) ? 'unavailable' : item.landline}</td>
							<td>${(empty item.mobile) ? 'unavailable' : item.mobile}</td>
							<td>
								<c:if test="${not empty item.emailAddress}">
									<a href="mailto:${item.emailAddress}">${item.emailAddress}</a>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>               

</body>

</html>