<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- <%@taglib uri="http://www.springframework.org/security/tags" prefix="sec" %> --%>


<ul class="nav">
	<li class="dropdown ${param.selectedMenu=='dealer' ? 'active' : ''}">
		<a class="dropdown-toggle" href="#">Dealerships</a>
		<ul class="dropdown-menu">
			<li><a href="<c:url value="/content/dealership/search" />" class="${param.selectedMenuItem=='dealerSearch' ? 'active' : ''}">Search Dealerships</a></li>
		</ul>
	</li>
	<li class="dropdown ${param.selectedMenu=='organisation' ? 'active' : ''}">
		<a class="dropdown-toggle" href="#">Organisations</a>
		<ul class="dropdown-menu">
			<li><a href="<c:url value="/content/organisation/search" />" class="${param.selectedMenuItem=='OrganisationSearch' ? 'active' : ''}">Search Organisations</a></li>
		</ul>
	</li>
	<li class="dropdown ${param.selectedMenu=='meeting' ? 'active' : ''}">
		<a class="dropdown-toggle" href="#">Meetings</a>
		<ul class="dropdown-menu">
			<li><a href="#">Arrange Meeting</a></li>
			<li><a href="#">Create Visit Report</a></li>
		</ul>
	</li>
	<li class="dropdown ${param.selectedMenu=='admin' ? 'active' : ''}">
		<a class="dropdown-toggle" href="#">Admin</a>
		<ul class="dropdown-menu">
			<li><a href="<c:url value="/content/userprofile/permissions/list" />" class="${param.selectedMenuItem=='profilePermissions' ? 'active' : ''}">Edit User Profile Functions</a></li>
			<li><a href="<c:url value="/content/organisation/permissions/list" />" class="${param.selectedMenuItem=='orgPermissions' ? 'active' : ''}">Edit Organisation Functions</a></li>
			<li><a href="<c:url value="/content/organisation/levels/list" />" class="${param.selectedMenuItem=='orgLevels' ? 'active' : ''}">View Organisation Levels</a></li>
			<li><a href="<c:url value="/content/organisation/sharingrules/list" />" class="${param.selectedMenuItem=='sharingRules' ? 'active' : ''}">Edit Information Sharing Rules</a></li>
			<li><a href="<c:url value="/content/meetingtype/list" />" class="${param.selectedMenuItem=='meetingTypes' ? 'active' : ''}">Edit Meeting Types</a></li>
			<li><a href="<c:url value="/content/positions/list" />" class="${param.selectedMenuItem=='positions' ? 'active' : ''}">Edit Positions</a></li>
		</ul>
	</li>
</ul>