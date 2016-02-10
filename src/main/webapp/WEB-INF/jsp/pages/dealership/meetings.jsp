<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta name="dealer" content="admin" />
	<title>${dealership.dealerName}</title>
	<meta name="subTitle" content="Meetings" />
	
	<style type="text/css">
	.confidentialRow {
		background-color: red;
	}
	</style>
</head>

<body>

	<div class="row">
		<%@ include file="/WEB-INF/jsp/pages/dealership/header.jspf" %>
	
		<div class="span4">
			<h3>Help</h3>
			<p>Details of meeting agendas and the details of meetings captured in visit reports are shown below.</p>
			<p>Click on the title of an item to see it&apos;s details.</p>
		</div>
	</div>

	<%@ include file="/WEB-INF/jsp/pages/dealership/tabs.jspf" %>

	<div class="row">
		<div class="span16">	      
			<c:if test="${empty meetings}">

				<h2 style="text-align:center;">No Meetings Found!</h2>

			</c:if>
			<c:if test="${not empty meetings}">

				<table class="zebra-striped" id="meetingSearchResults">
					<thead>
						<tr>
							<th>Type</th>
							<th>Date</th>
							<th>Title</th>
							<th>Meeting Type</th>
							<th>Organiser</th>
							<th>Documents</th>
							<th>Actions</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${meetings}" var="item">
							<tr class="${item.confidential?'confidentialRow':''}">
								<td>${(empty item.visitReport) ? 'Agenda' : 'Visit Report'}</td>
								<td>${item.dateOfMeeting}</td>
								<td><a href="<c:url value="/content/agenda/${item.id}/view" />">${item.titleOfMeeting}</a></td>
								<td>${item.meetingType.name}</td>
								<td>${item.meetingOrganiserUserId}</td>
								<td></td>
								<td></td>
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
			  			$("table#meetingSearchResults").tablesorter({widthFixed: true, widgets: ['zebra']})
			  			                               .tablesorterPager({container: $("#pager"), positionFixed:false});
			  			updatePage();
					});
					function updatePage() {
						document.getElementById('curPageNumb').innerHTML = "page "+document.getElementById('disp').value;
					}
				</script>
			</c:if>

			<div class="well" style="padding: 14px 19px;">
				<a href="<c:url value="/content/agenda/create?dealershipId=" />${dealership.id}" class="btn primary">Create New Agenda</a>
				<a href="<c:url value="/content/visitreport/create?dealershipId=" />${dealership.id}" class="btn">Create New Visit Report</a>
				
				<c:if test="${moreMeetings!='true'}">
					<a href="meetings" class="btn" style="float:right">More Meetings</a>
				</c:if>
				<c:if test="${moreMeetings=='true'}">
					<a href="mymeetings" class="btn" style="float:right">Less Meetings</a>
				</c:if>
			</div>

		</div>
	</div>               

</body>

</html>