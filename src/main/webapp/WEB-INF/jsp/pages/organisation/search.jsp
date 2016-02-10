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
	<meta name="menu" content="organisation" />
	<meta name="menuItem" content="organisationSearch" />
	<title>Contacts List</title>
	<meta name="subTitle" content="Search contacts for an Organisation" />
</head>

<body>
	<div class="row">
		<div class="span12">
		
			<form:form action="search" method="post">
				<fieldset>

					<spring:bind path="organisationId">
						<div class="clearfix ${status.error?'error':''}">
							<form:label path="organisationId">Organisation:</form:label>
							<div class="input">
								<form:select path="organisationId">
									<form:option value="-1" label=" " />
									<form:options items="${organisations}" itemValue="id" itemLabel="name" />
								</form:select>
							</div>
						</div>
					</spring:bind>
					
					<spring:bind path="dealershipNumber">
						<div class="clearfix ${status.error?'error':''}">
							<form:label path="dealershipNumber">Dealer Number:</form:label>
							<div class="input">
								<form:input path="dealershipNumber" onkeyup="if(document.getElementById('dealershipNumber').value.length>5){alert('Maximum of 5 digits allowed.')}"/>
								<form:errors path="dealershipNumber" cssClass="help-inline" />
								<c:if test="${not status.errors.hasErrors()}">
									<span class="help-inline">(maximum of five digits)</span>
								</c:if>
							</div>
						</div>
					</spring:bind>

					<spring:bind path="businessArea">
						<div class="clearfix ${status.error?'error':''}">
							<form:label path="businessArea">Business Area:</form:label>
							<div class="input">
								<form:select path="businessArea">
										<form:option value="-1" label=" " />
										<form:options items="${businessArea}" itemValue="id" itemLabel="name" />
								</form:select>
							</div>
						</div>
					</spring:bind>

					<spring:bind path="jobRole">
						<div class="clearfix ${status.error?'error':''}">
							<form:label path="jobRole">Job Role:</form:label>
							<div class="input">
								<form:input path="jobRole" />
								<form:errors path="jobRole" cssClass="help-inline" />
								<c:if test="${not status.errors.hasErrors()}">
									<span class="help-inline">(can contain * wildcards)</span>
								</c:if>
							</div>
						</div>
					</spring:bind>
					
					<spring:bind path="geographicalArea">
						<div class="clearfix ${status.error?'error':''}">
							<form:label path="geographicalArea">Brand Geographical Area:</form:label>
							<div class="input">
								<form:input path="geographicalArea" />
								<form:errors path="geographicalArea" cssClass="help-inline" />
								<c:if test="${not status.errors.hasErrors()}">
									<span class="help-inline">(can contain * wildcards)</span>
								</c:if>
							</div>
						</div>
					</spring:bind>
					
					<spring:bind path="level">
						<div class="clearfix ${status.error?'error':''}">
							<form:label path="level">Level:</form:label>
							<div class="input">
								<form:select path="level">
										<form:option value="-1" label=" " />
										<form:options items="${levels}" itemValue="id" itemLabel="name" />
								</form:select>								
								<%-- <form:input path="level" />
								<form:errors path="level" cssClass="help-inline" />
								<c:if test="${not status.errors.hasErrors()}">
									<span class="help-inline">(can contain * wildcards)</span>
								</c:if> --%>
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
	
					<h2>Organisation Search Results</h2>
					<table class="tablesorter bordered-table zebra-striped" id="dealerSearchResults">
						<thead>
							<tr>
							<c:forEach items="${command.searchResults}" var="item">
								<c:if test="${item.organisation.id == '0'}">
									<c:if test="${item.userBrands != null}">
										<th>Brand</th>
									</c:if>
									<th>Dealer Name</th>
									<th>Dealer Number</th>
									<th>Franchise Group Name</th>
								</c:if>
							</c:forEach>
								<th>Name of Contact<br>(First Name, Surname)</th>
								<th>Position</th>
								<th>Position Sequence</th>
								<th>Landline Telephone Number</th>
								<th>Mobile Telephone Number</th>
								<th>Email Address</th>
								<th>Effective Start Date<br>in current Position</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${command.searchResults}" var="item">
								<tr>
									<c:if test="${item.organisation.id == '0'}">
									<c:if test="${item.userBrands != null}">
										<c:forEach items="${item.userBrands}" var="item1">
											<td>${item1}</td>
									</c:forEach>
									</c:if>
										<td>${item.dealership.dealerName}</td>
										<td>${item.dealership.dealerNumber}</td>
										<td>${item.dealership.franchiseGroup.name}</td>
									</c:if>
									<td>${item.firstName}, ${item.surName}, ${item.organisation.id}</td>
									<td>${item.positions.level}</td>
									<td>${item.positions.positionSequence}</td>
									<td>${item.landline}</td>
									<td>${item.mobile}</td>
									<td>${item.emailAddress}</td>
									<td>No column in table</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div id="pager" class="pagination">
						<ul>
							<li class="prev disabled"><a href="#">&larr; Previous</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li class="next"><a href="#">Next &rarr; </a></li>
						</ul>
<!-- 
						<form>
							<img src="../addons/pager/icons/first.png" class="first"/>
							<img src="../addons/pager/icons/prev.png" class="prev"/>
							<input type="text" class="pagedisplay"/>
							<img src="../addons/pager/icons/next.png" class="next"/>
							<img src="../addons/pager/icons/last.png" class="last"/>
							<select class="pagesize">
								<option selected="selected"  value="10">10</option>
								<option value="20">20</option>
								<option value="30">30</option>
								<option  value="40">40</option>
							</select>
						</form>
 -->
 					</div>
					<script type="text/javascript">
						$(document).ready(function(){
				  			$("table#dealerSearchResults").tablesorter({widthFixed: true, widgets: ['zebra']})
				  			                         .tablesorterPager({container: $("#pager")});
						});
					</script>

				</c:if>
			</c:if>
		</div>
	</div>
</body>
</html>