<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="com.vw.visitreporting.entity.referencedata.Positions"%>
<%@page import="java.util.Enumeration"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
<title>Create / Update Position</title>
</head>

<body>
	<c:set var="pageUrl" scope="request">
		<c:url value="/content/positions/save" />
	</c:set>
	<form:form commandName="crudObj" action="${pageUrl}" method="POST">
		<fieldset>
		<form:input type="hidden" path="id" />
			<spring:bind path="description">
				<div class="clearfix ${not empty status.errorMessage?'error':''}">
					<form:label path="description">Position Name</form:label>
					<div class="input">
						<form:input path="description" />
						<form:errors path="description" cssClass="help-inline" />
						<c:if test="${empty status.errorMessage}">
							<span class="help-inline">position name (between 2-200 characters)</span>
						</c:if>
					</div>
				</div>
			</spring:bind>
			
			<spring:bind path="organisation.id">
				<div class="clearfix ${not empty status.errorMessage?'error':''}">
					<form:label for="organisation.id" path="organisation.id">Organisation </form:label>
					<div class="input">
						<form:select id="organisation.id" name="organisation.id" path="organisation.id">
							<form:options items="${org_list}" itemLabel="name" itemValue="id" />
						</form:select>
						<form:errors path="organisation.id" cssClass="help-inline" />
					</div>
				</div>
			</spring:bind>
			
			<spring:bind path="brands">
				<div class="clearfix ${not empty status.errorMessage?'error':''}">
					<form:label for="brands" path="brands">Brand </form:label>
					<div class="input ${not empty status.errorMessage?'error':''}">
						<form:select path="brands" multiple="multiple" size="5">
							<form:options items="${brand_list}" itemLabel="name" itemValue="id" />
						</form:select>
						<form:errors path="brands" cssClass="help-inline" />
						<c:if test="${empty status.errorMessage}">
							<span class="help-inline">Use Ctrl key to select more than one</span>
						</c:if>
					</div>
				</div>
			</spring:bind>

			<div class="clearfix">
				<form:label for="level" path="level">Level </form:label>
				<div class="input">
					<form:select path="level">
						<form:options items="${level_list}" itemLabel="name" itemValue="id" />
					</form:select>
				</div>
			</div>

			<div class="clearfix">
                 <label for="visibleWithinContactScreen">Visible within Contact Screen?</label>
                 <div class="input inline-inputs">
                     <form:checkbox path="visibleWithinContactScreen" />
                     <span class="help-inline">Display contacts with this position in lists?</span>
                 </div>
            </div>
	
			<spring:bind path="positionSequence">
				<div class="clearfix ${not empty status.errorMessage?'error':''}">
					<form:label path="positionSequence">Position Sequence</form:label>
					<div class="input">
						<form:input path="positionSequence" />
						<form:errors path="positionSequence" cssClass="help-inline" />
						<c:if test="${empty status.errorMessage}">
							<span class="help-inline">indicates sequence in which the
							Positions will be displayed within the Contacts screen</span>
						</c:if>
					</div>
				</div>
			</spring:bind>
	</fieldset>

		<div class="actions">
			<input type="submit" class="btn primary" value="Save changes" />&nbsp;
			<button type="reset" class="btn">Reset</button>&nbsp; 
			<a href="<c:url value="/content/positions/list" />" class="btn">Cancel</a>&nbsp;
			<c:if test="${not empty crudObj.id}">
				<a href="<c:url value="/content/positions/${crudObj.id}/delete" />" class="btn danger">Delete</a>
			</c:if>
		</div>
	</form:form>
</body>
</html>