<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
  <head>  
  	<meta name="menu" content="admin" />
	<meta name="menuItem" content="Kpis" /> 
	<title>KPIs</title>       
  </head>
  <body>   
    <table class="zebra-striped" id="dealerMeetings">
        <thead>
          <tr>
	        <th>Organisations</th>
            <th>Brands and Benchmarks</th>
            <th>Business Areas</th>
            <th>Category</th>
            <th>KPI</th>            
            <th>Valid</th>
            <th>Modified By</th>            
            <th>Modified Date</th>
          </tr>
        </thead>
        <tbody>         
          <c:forEach items="${list}" var="item">
				<tr>
			        <td><c:out value="${item.organisation.name}"/></td>
		            <td>
		            	<table>		            			            			
			            	<c:forEach items="${item.kpiBrands}" var="brandVar"  varStatus="status">
			            		<tr>	
				            		<td><c:out value="${brandVar.brand.name}"/></td>	
				            		<td><c:out value="${brandVar.benchmarkFigure}"/></td>	
			            		</tr>	            		
			            	</c:forEach>
		            	</table>
		            </td>
		            <td>
		            	<c:forEach items="${item.businessAreas}" var="busArea">
		            		<c:out value="${busArea.name}"/><br/>
		            	</c:forEach>
		            </td>
		            <td><c:out value="${item.kpiCategory.name}"/></td>
		            <td><a href="<c:out value="${item.id}"/>/update"><c:out value="${item.kpiDescription}"/></a></td>
		            <td>${item.status}<br/><em>${item.effectiveStartDate} to ${item.effectiveEndDate}</em></td>
		            <td>
		            	<c:choose>
		            		<c:when test="${item.modifiedBy != null}"><c:out value="${item.modifiedBy}"/></c:when>
		            		<c:otherwise><c:out value="${item.createdBy}"/></c:otherwise>
		            	</c:choose>		            
		            </td>
		            <td>
		            	<c:choose>
		            		<c:when test="${item.modifiedDate != null}"><c:out value="${item.modifiedDate}"/></c:when>
		            		<c:otherwise><c:out value="${item.createdDate}"/></c:otherwise>
		            	</c:choose>
		            </td>
		         </tr>
			</c:forEach>          
       </tbody>
      </table>

	  <div class="well" style="padding: 14px 19px;">
        <a href="create" class="btn primary">Create New KPI</a>&nbsp;	        
      </div>     
	  <script type="text/javascript">
		$(document).ready(function(){
  			$("#dealerMeetings").tablesorter( { sortList: [[ 1, 0 ]] } );
		});
	   </script>
  </body>
</html>
