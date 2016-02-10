<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page import="com.vw.visitreporting.entity.referencedata.MeetingType"%>
<%@page import="java.util.Enumeration"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta name="menu" content="dealer" />
	<meta name="menuItem" content="dealerSearch" />
	<title>Dealerships</title>
	<meta name="subTitle" content="Search for a Dealer" />
</head>

<body>
	<div class="row">
		<div class="span12">
		
			<form:form action="search" method="post">
				<fieldset>

					<spring:bind path="dealershipNumber">
						<div class="clearfix ${status.error?'error':''}">
							<form:label path="dealershipNumber">Dealer Number:</form:label>
							<div class="input">
								<form:input path="dealershipNumber" />
								<form:errors path="dealershipNumber" cssClass="help-inline" />
								<c:if test="${not status.errors.hasErrors()}">
									<span class="help-inline">maximum of five digits</span>
								</c:if>
							</div>
						</div>
					</spring:bind>

					<spring:bind path="dealershipName">
						<div class="clearfix ${status.error?'error':''}">
							<form:label path="dealershipName">Dealer Name:</form:label>
							<div class="input">
								<form:input path="dealershipName" />
								<form:errors path="dealershipName" cssClass="help-inline" />
								<c:if test="${not status.errors.hasErrors()}">
									<span class="help-inline">can contain * wildcards</span>
								</c:if>
							</div>
						</div>
					</spring:bind>

					<spring:bind path="franchiseGroupName">
						<div class="clearfix ${status.error?'error':''}">
							<form:label path="franchiseGroupName">Franchise Group Name:</form:label>
							<div class="input">
								<form:input path="franchiseGroupName" />
								<form:errors path="franchiseGroupName" cssClass="help-inline" />
								<c:if test="${not status.errors.hasErrors()}">
									<span class="help-inline">can contain * wildcards</span>
								</c:if>
							</div>
						</div>
					</spring:bind>

				</fieldset>

				<div class="actions">
					<input type="submit" class="btn primary" value="Search" />
					<input type="reset" class="btn" value="Reset" />
				</div>
			</form:form>
			
		</div>
		<div class="span4">
			<h3>Help</h3>
			<p>Search for a dealer using their dealer number or the first part of their
				name. You can also search for a dealers by their franchise groups.</p>
		</div>
	</div>
	<div class="row">
		<div class="span16">
		
			<c:if test="${command.searchResults!=null}">

				<c:if test="${empty command.searchResults}">
	
					<h2 style="text-align:center;">No Dealerships Found!</h2>
	
				</c:if>
				<c:if test="${not empty command.searchResults}">
	
					<h2>Dealer Search Results</h2>

					<table class="tablesorter bordered-table zebra-striped" id="dealerSearchResults">
						<thead>
							<tr>
								<th>Dealer Number</th>
								<th>Dealer Name</th>
								<th>Franchise Group</th>
								<th>Location</th>
								<th>Geographical Category</th>
								<th>Region</th>
								<th>Area</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${command.searchResults}" var="item">
								<tr>
									<c:set var="number"><fmt:formatNumber value="${item.dealerNumber}" pattern="00000" /></c:set>
					
									<td><a href="<c:url value="/content/dealership/${number}/view" />">${number}</a></td>
									<td><a href="<c:url value="/content/dealership/${number}/view" />">${item.dealerName}</a></td>
									<td>${item.franchiseGroup.name}</td>
									<td>${item.tradingAddress}</td>
									<td>
										<c:forEach items="${item.geographicalAreas}" var="area">
											<p>${area.geographicalRegion.geographicalStructureCategory}</p>
										</c:forEach>
									</td>
									<td>
										<c:forEach items="${item.geographicalAreas}" var="area">
											<p>${area.geographicalRegion}</p>
										</c:forEach>
									</td>
									<td>
										<c:forEach items="${item.geographicalAreas}" var="area">
											<p>${area}</p>
										</c:forEach>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

					<div id="pager" class="pagination">
						<ul>
							<li class="prev first"><a href="#" onmouseup="setTimeout('updatePage()',500);">&lt;&lt; First</a></li>
							<li class="prev"><a href="#" onmouseup="setTimeout('updatePage()',500);">&lt; Prev</a></li>
							<li class="active"><a href="#" id="curPageNumb">page</a></li>
							<li class="next"><a href="#" onmouseup="setTimeout('updatePage()',500);">Next &gt;</a></li>
							<li class="next last"><a href="#" onmouseup="setTimeout('updatePage()',500);">Last &gt;&gt;</a></li>
						</ul>
						<input type="hidden" class="pagedisplay" id="disp" />
						<input type="hidden" class="pagesize" value="10" />
 					</div>

					<script type="text/javascript">
						$(document).ready(function(){
				  			$("table#dealerSearchResults").tablesorter({widthFixed: true, widgets: ['zebra']})
				  			                              .tablesorterPager({container: $("#pager"), positionFixed:false});
				  			updatePage();
						});
						
						function updatePage() {
							document.getElementById('curPageNumb').innerHTML = "page "+document.getElementById('disp').value;
						}
					</script>

				</c:if>
			</c:if>
		</div>
	</div>


</body>

</html>