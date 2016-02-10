<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta name="dealer" content="admin" />
	<title>${dealership.dealerName}</title>
	<meta name="subTitle" content="Topics" />
</head>

<body>

	<div class="row">
		<%@ include file="/WEB-INF/jsp/pages/dealership/header.jspf" %>
	
		<div class="span4">
			<h3>Help</h3>
			<p>Topics for meeting specific to this individual retailer to appear of the standard agenda items for this dealership.</p>
		</div>
	</div>

	<%@ include file="/WEB-INF/jsp/pages/dealership/tabs.jspf" %>

	<div class="row">
		<div class="span16">	      
			<c:if test="${empty topics}">

				<h2 style="text-align:center;">No Topics Found!</h2>

			</c:if>
			<c:if test="${not empty topics}">

				<table class="zebra-striped" id="topicSearchResults">
					<thead>
						<tr>
							<th>Business Area</th>
							<th>Topic</th>
							<th>Description</th>
							<!-- th>&nbsp;</th-->
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${topics}" var="item">
							<tr>
								<td>
									<c:forEach items="${item.businessAreas}" var="busArea">
										<p>${busArea}</p>
									</c:forEach>
								</td>
								<td>${item.standardTopicRef.title}</td>
								<td>${item.standardTopicRef.description}</td>
								<!-- td><a href="${item.id}/delete">remove</a></td-->
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:if>

			<div class="well" style="padding: 14px 19px;">
				<a href="<c:url value="/content/topic/create?dealershipId=" />${dealership.id}" class="btn primary">Create New Topic</a>
			</div>

		</div>
	</div>               

</body>

</html>