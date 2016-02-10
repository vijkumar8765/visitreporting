<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta name="dealer" content="admin" />
	<title>${dealership.dealerName}</title>
	<meta name="subTitle" content="Actions" />
	<style type="text/css">
	
	.dueToday {
		color: orange;
	}
	.dueInPast {
		color: red;
	}
	</style>
</head>

<body>

	<div class="row">
		<%@ include file="/WEB-INF/jsp/pages/dealership/header.jspf" %>
	
		<div class="span4">
			<h3>Help</h3>
			<p>Actions associated with this retailer. Open are shown by default, click "more" for other recent actions.</p>
		</div>
	</div>

	<%@ include file="/WEB-INF/jsp/pages/dealership/tabs.jspf" %>

	<div class="row">
		<div class="span16">	      
			<c:if test="${empty actions}">

				<h2 style="text-align:center;">No Actions Found!</h2>

			</c:if>
			<c:if test="${not empty actions}">

				<table class="zebra-striped" id="actionSearchResults">
					<thead>
						<tr>
							<th>Creator</th>
							<th>Action</th>
							<th>Status</th>
							<th>Due</th>
							<th>Started</th>
							<th>Updated</th>
							<th>Owner(s)</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${actions}" var="item">
							<tr>
								<td>${item.createdBy}</td>
								<td>
									<a href="<c:url value="/content/action/${item.id}/view" />">${item.description}</a>
									<c:if test="${not empty item.latestProgress}">
										<br/><em>${item.latestProgress.description}</em>
									</c:if>
								</td>
								<td>${item.status.name}</td>
								<td class="${item.dueToday ? 'dueToday' : (item.dueInPast ? 'dueInPast':'')}">
									${item.dueDate}
								</td>
								<td>
									<c:if test="${not empty item.firstProgress}">
										${item.firstProgress.progressDate}
									</c:if>
								</td>
								<td>
									<c:if test="${not empty item.latestProgress}">
										${item.latestProgress.progressDate}
									</c:if>
								</td>
								<td>
									<c:forEach items="${item.owners}" var="owner">
										<p>${owner}</p>
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
			  			$("table#actionSearchResults").tablesorter({widthFixed: true, widgets: ['zebra']})
			  			                               .tablesorterPager({container: $("#pager"), positionFixed:false});
			  			updatePage();
					});
					function updatePage() {
						document.getElementById('curPageNumb').innerHTML = "page "+document.getElementById('disp').value;
					}
				</script>
			</c:if>

			<div class="well" style="padding: 14px 19px;">
				<c:if test="${moreActions!='true'}">
					<a href="actions" class="btn" style="float:right">More Actions</a>
				</c:if>
				<c:if test="${moreActions=='true'}">
					<a href="myactions" class="btn" style="float:right">Less Actions</a>
				</c:if>
			</div>

		</div>
	</div>               

</body>

</html>