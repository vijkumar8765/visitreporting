<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%
%><%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!-- Displays a list of messages from the previous operation -->

		<c:forEach items="${param.warningMessages}" var="msg">
			<div class="alert-message warning fade in" data-alert="alert" >
				<a class="close" href="#">&times;</a>
				<p>${msg}</p>
			</div>
		</c:forEach>
		<c:forEach items="${warningMessages}" var="msg">
			<div class="alert-message warning fade in" data-alert="alert" >
				<a class="close" href="#">&times;</a>
				<p>${msg}</p>
			</div>
		</c:forEach>
		<c:forEach items="${param.errorMessages}" var="msg">
			<div class="alert-message error fade in" data-alert="alert" >
				<a class="close" href="#">&times;</a>
				<p>${msg}</p>
			</div>
		</c:forEach>
				<c:forEach items="${errorMessages}" var="msg">
			<div class="alert-message error fade in" data-alert="alert" >
				<a class="close" href="#">&times;</a>
				<p>${msg}</p>
			</div>
		</c:forEach>
		<c:forEach items="${param.successMessages}" var="msg">
			<div class="alert-message success fade in" data-alert="alert" >
				<a class="close" href="#">&times;</a>
				<p>${msg}</p>
			</div>
		</c:forEach>
		<c:forEach items="${successMessages}" var="msg">
			<div class="alert-message success fade in" data-alert="alert" >
				<a class="close" href="#">&times;</a>
				<p>${msg}</p>
			</div>
		</c:forEach>
		<c:forEach items="${param.infoMessages}" var="msg">
			<div class="alert-message info fade in" data-alert="alert" >
				<a class="close" href="#">&times;</a>
				<p>${msg}</p>
			</div>
		</c:forEach>
		<c:forEach items="${infoMessages}" var="msg">
			<div class="alert-message info fade in" data-alert="alert" >
				<a class="close" href="#">&times;</a>
				<p>${msg}</p>
			</div>
		</c:forEach>
		