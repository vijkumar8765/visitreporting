<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta name="dealer" content="admin" />
	<title>${dealership.dealerName}</title>
	<meta name="subTitle" content="Information" />
</head>

<body>

	<div class="row">
		<%@ include file="/WEB-INF/jsp/pages/dealership/header.jspf" %>
	
		<div class="span4">
			<h3>Help</h3>
			<p>Detailed information for the selected dealership.</p>
		</div>
	</div>

	<%@ include file="/WEB-INF/jsp/pages/dealership/tabs.jspf" %>

	<div class="row">
		<div class="span4 offset1">	      
			<dl>
				<dt>Franchise Group</dt>
				<dd><a data-controls-modal="groupDealersPopup" data-backdrop="static">${dealership.franchiseGroup.name}</a></dd>
				<dt>Trading Address</dt>
				<dd><address>
					<c:forEach items="${dealership.tradingAddress.lines}" var="line">
						${line}<br/>
					</c:forEach>           
				</address></dd>       
			</dl>
		</div>
		<div class="span11">	      
			<br/><br/>
			<dl>
				<dt>Telephone</dt>
				<dd>${dealership.mainlineTelephone}</dd>
				<dt>Fax</dt>
				<dd>${dealership.faxNumber}</dd>
				<dt>Website</dt>
				<dd><a target="_blank" href="${dealership.websiteLink}">${dealership.websiteLabel}</a></dd>
			</dl>
		</div>
	</div>               

</body>

</html>