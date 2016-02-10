<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta name="dealer" content="admin" />
	<title>${dealership.dealerName}</title>
	<meta name="subTitle" content="Performance" />
	
	<style type="text/css">
	
	.expiredKpi {
		background-color: grey;
	}
	.RED {
		color: red;
	}
	.GREEN {
		color: green;
	}
	.AMBER {
		color: orange;
	}
	</style>
</head>

<body>

	<div class="row">
		<%@ include file="/WEB-INF/jsp/pages/dealership/header.jspf" %>
	
		<div class="span4">
			<h3>Help</h3>
			<p>Display recent KPI scores for this dealership.</p>
		</div>
	</div>

	<%@ include file="/WEB-INF/jsp/pages/dealership/tabs.jspf" %>

	<div class="row">
		<div class="span16">	      
			<c:if test="${empty kpis}">

				<h2 style="text-align:center;">No KPIs Found!</h2>

			</c:if>
			<c:if test="${not empty kpis}">

				<table class="zebra-striped" id="kpiSearchResults">
					<thead>
						<tr>
							<th>Business Area</th>
							<th>Category</th>
							<th>KPI</th>
							<th>Target</th>
							<th>Actual</th>
							<th>Area Actual</th>
							<th>Region Actual</th>
							<th>National Actual</th>
							<th>Variance</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${kpis}" var="item">
							<tr>
								<c:set var="numberFormat">#.00${item.percentage?'%':''}</c:set>

								<td>
									<c:forEach items="${item.businessAreas}" var="busArea">
										<p>${busArea}</p>
									</c:forEach>
								</td>
								<td>${item.kpiCategory}</td>
								<td>${item.kpiDescription}</td>
								<c:if test="${item.status == 'EXPIRED'}">
									<td class="expiredKpi ${item.dealershipActualColour}"
										title="KPI Expired" data-content="This score value is for date: ${item.dealershipTarget.mostRecentTargetMonth}/${item.dealershipTarget.year}">
										<fmt:formatNumber value="${item.dealershipTarget.mostRecentTarget}" pattern="${numberFormat}" />
									</td>
								</c:if>
								<c:if test="${item.status != 'EXPIRED'}">
									<td class="${item.dealershipActualColour}">
										<fmt:formatNumber value="${item.dealershipTarget.currentTarget}" pattern="${numberFormat}" />
									</td>
								</c:if>
								<td><fmt:formatNumber value="${item.dealershipActual.dealerScore}" pattern="${numberFormat}" /></td>
								<td><fmt:formatNumber value="${item.dealershipActual.areaScore}" pattern="${numberFormat}" /></td>
								<td><fmt:formatNumber value="${item.dealershipActual.regionalScore}" pattern="${numberFormat}" /></td>
								<td><fmt:formatNumber value="${item.dealershipActual.nationalActualScore}" pattern="${numberFormat}" /></td>
								<td><fmt:formatNumber value="${item.dealershipActual.varianceTotal}" pattern="${numberFormat}" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>

				<span class="span3 offset12"><strong>Total Actual Score:</strong></span>${totalActualScore}<br/>
				<span class="span3 offset12"><strong>National League:</strong></span>${nationalLeaguePosition}<br/>	 
			</c:if>
		</div>
	</div>               

</body>

</html>