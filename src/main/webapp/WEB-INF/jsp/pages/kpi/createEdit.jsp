<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>	
	<title>Create / Update KPI</title>
	<script type="text/javascript">
		
		function showDetails() {
			$('#highlightsPill, #BenchMarksPill').removeClass("active");
			$('#detailsPill').addClass("active");
			
			$('#detailsDiv').show();
			$('#highlightsDiv, #BenchMarkDiv').hide();			
		}
		
		function showHighlights() {
			$('#detailsPill, #BenchMarksPill').removeClass("active");
			$('#highlightsPill').addClass("active");
			
			$('#detailsDiv, #BenchMarkDiv').hide();
			$('#highlightsDiv').show();	
		}
		
		function showBenchMarks() {
			$('#highlightsPill, #detailsPill').removeClass("active");
			$('#BenchMarksPill').addClass("active");
			
			$('#detailsDiv, #highlightsDiv').hide();
			$('#BenchMarkDiv').show();	
		}
		
		function addBusinessRuleRow(){
			
			alert("hello here");
			
			var benchMarkRow = $('#samplekpiBusRuleTable  tbody>tr:last').clone(true).html();
			benchMarkRow = benchMarkRow.replace("busRuleBrandSelectId", "dynamicKpiBusinessRules" + initialBusinessRulesSize + ".brandId");
			benchMarkRow = benchMarkRow.replace("busRuleBrandSelectName", "dynamicKpiBusinessRules[" + initialBusinessRulesSize + "].brandId");
			
			benchMarkRow = benchMarkRow.replace("busRuleOperatorId", "dynamicKpiBusinessRules" + initialBusinessRulesSize + ".businessRuleOpertor");
			benchMarkRow = benchMarkRow.replace("busRuleOperatorName", "dynamicKpiBusinessRules[" + initialBusinessRulesSize + "].businessRuleOpertor");
			
			benchMarkRow = benchMarkRow.replace("busRuleActualTotalId", "dynamicKpiBusinessRules" + initialBusinessRulesSize + ".actualTotal");
			benchMarkRow = benchMarkRow.replace("busRuleActualTotalName", "dynamicKpiBusinessRules[" + initialBusinessRulesSize + "].actualTotal");
			
			benchMarkRow = benchMarkRow.replace("busRuleColorId", "dynamicKpiBusinessRules" + initialBusinessRulesSize + ".varianceColorId");
			benchMarkRow = benchMarkRow.replace("busRuleColorName", "dynamicKpiBusinessRules[" + initialBusinessRulesSize + "].varianceColorId");
			
			benchMarkRow = "<tr>" + benchMarkRow + "</tr>";
			//alert(benchMarkRow);
			
			$(benchMarkRow).insertAfter('#busRulesTable tbody>tr:last');
			
			initialBenchMarksSize = parseInt(initialBenchMarksSize) + 1;
			
		}
		
		
		function addBenchMarkRow(){	
			
			//$('#sampleBenchMarkTable tbody>tr:last').clone(true).insertAfter('#BenchMarkTable tbody>tr:last');
			
			var benchMarkRow = $('#sampleBenchMarkTable tbody>tr:last').clone(true).html();
			benchMarkRow = benchMarkRow.replace("benchMarkBrandSelectId", "dynamicKpiBrands" + initialValue + ".brandId");
			benchMarkRow = benchMarkRow.replace("benchMarkBrandSelectName", "dynamicKpiBrands[" + initialValue + "].brandId");
			
			benchMarkRow = benchMarkRow.replace("benchMarkTextId", "dynamicKpiBrands" + initialValue + ".benchMark");
			benchMarkRow = benchMarkRow.replace("benchMarkTextName", "dynamicKpiBrands[" + initialValue + "].benchMark");
			benchMarkRow = "<tr>" + benchMarkRow + "</tr>";
			//alert(benchMarkRow);
			
			$(benchMarkRow).insertAfter('#BenchMarkTable tbody>tr:last');
			
			initialValue = parseInt(initialValue) + 1;
		}
		
		function deleteBenchMarkRow(){	
			//alert("delete bench mark row");
			
			$("div#BenchMarkDiv :checkbox:checked").each(  
				function() {					
					var aa = "#benchmarkRow" + $(this).val();					
					//alert($(aa).html());
					$(aa).remove();   
					//$('\"benchmarkRow\" + $(this).val() ').remove();   
			    }  
			 );  

		}
	
		var initialBenchMarksSize;
		var initialBusinessRulesSize;
		$(document).ready(function(){
			initialBenchMarksSize = $('#dynamicKpiBrandsSize').val();
			initialBusinessRulesSize = $('#dynamicKpiBusinessRulesSize').val();
			
			$('#highlightsDiv').hide(); 
			$('#BenchMarkDiv').hide();
		});
	</script>
	  
</head>
<body> 
  <c:set var="pageUrl" scope="request">
	 <c:url value="/content/kpi/save" />
  </c:set>  
  <div class="row">
    <div class="span12">
      <form:form action="${pageUrl}" commandName="crudObj" method="POST">        
          
         <form:hidden path="id"/>          
          
          <spring:bind path="kpiDescription">
	          <div class="clearfix"> 
	            <form:label path="kpiDescription">Name</form:label>
	            <div class="input">
	              <form:input path="kpiDescription" size="30" type="text" class="span4"/>
	              <form:errors path="kpiDescription" cssClass="help-inline" />
	              <c:if test="${empty status.errorMessage}">
					 <span class="help-inline">kpi name (between 2-30 characters)</span>
				  </c:if>
	            </div>
	          </div>
          </spring:bind>

		  <spring:bind path="kpiCategory.id">
	          <div class="clearfix ${not empty status.errorMessage?'error':''}">
	            <form:label for="kpiCategory.id" path="kpiCategory.id">Category </form:label>
	            <div class="input inline-inputs">
	              <form:select id="kpiCategory.id" name="kpiCategory.id" path="kpiCategory.id">
					<form:options items="${kpiCategory_list}" itemLabel="name" itemValue="id" />
				  </form:select>
				  <form:errors path="kpiCategory.id" cssClass="help-inline" />
	            </div>
	          </div>
          </spring:bind>

        <ul class="pills">
          <li class="active" id="detailsPill"><a href="javascript:showDetails()">Details</a></li>
          <li id="highlightsPill"><a href="javascript:showHighlights()">Highlighting</a></li>
          <li id="BenchMarksPill"><a href="javascript:showBenchMarks()">Bench Marks</a></li>
        </ul>

		<div id="detailsDiv">
			<spring:bind path="organisation.id">
		       <div class="clearfix ${not empty status.errorMessage?'error':''}">
		            <form:label for="organisation.id" path="organisation.id">Organisations </form:label>
		            <div class="input inline-inputs">
		              <form:select id="organisation.id" name="organisation.id" path="organisation.id">
						<form:options items="${org_list}" itemLabel="name" itemValue="id" />
					  </form:select>
					  <form:errors path="organisation.id" cssClass="help-inline" />
		            </div>
		        </div>
	        </spring:bind>
			
			<spring:bind path="brands">
				<div class="clearfix ${not empty status.errorMessage?'error':''}">
					<form:label for="brands" path="brands">Brands </form:label>
					<div class="input ${not empty status.errorMessage?'error':''}">
						<form:select path="brands" multiple="multiple">
							<form:options items="${brand_list}" itemLabel="name" itemValue="id" />
						</form:select>
						<form:errors path="brands" cssClass="help-inline" />
						<c:if test="${empty status.errorMessage}">
							<span class="help-inline">Use Ctrl key to select more than one</span>
						</c:if>
					</div>
				</div>
			</spring:bind>
	
	        <spring:bind path="businessAreas">
	         <div class="clearfix ${not empty status.errorMessage?'error':''}">
	           <form:label for="businessAreas" path="businessAreas">Business Areas</form:label>
	           <div class="input ${not empty status.errorMessage?'error':''}">
	             <form:select path="businessAreas" multiple="true">
					<form:options items="${businessArea_list}" itemLabel="name" itemValue="id" />
			  </form:select>
			  <form:errors path="businessAreas" cssClass="help-inline" />
			  <c:if test="${empty status.errorMessage}">
				<span class="help-inline">Use Ctrl key to select more than one</span>
			  </c:if>
	           </div>
	         </div>
	        </spring:bind>
	        
	        <c:if test="${not empty crudObj.id}">
		        <spring:bind path="newStatus">
		         <div class="clearfix ${not empty status.errorMessage?'error':''}">
		           <form:label for="newStatus" path="newStatus">New Status</form:label>
		           <div class="input ${not empty status.errorMessage?'error':''}">
		             <form:select path="newStatus">		             	
						<form:options items="${kpiStatus_list}" itemLabel="name" itemValue="id" />
				  </form:select>
				  <form:errors path="businessAreas" cssClass="help-inline" />
				  &nbsp;&nbsp;&nbsp;Date <form:input type="text" class="datepicker" size="5" path="newStatusEffectiveDate"/>
		           </div>
		         </div>
		        </spring:bind>
	        </c:if>
	        
          <div class="clearfix">
            <label for="dueDateStart">Active between</label>
            <div class="input inline-inputs">
              <form:input type="text" class="datepicker" size="5" path="effectiveStartDate"/> and 
              <form:input type="text" class="datepicker" size="5" path="effectiveEndDate"/>
            </div>
          </div>
        </div>
        
        <div id="highlightsDiv">         	
             	
        	<table id="busRulesTable">
	          <tr>
	            <th>#</th>
	            <th>Brand</th>
	            <th>Business Rule</th>
	            <th>Variance Colour</th>
	          </tr>
	          
	          <c:forEach var="dynamicKpiBusRule" items="${crudObj.dynamicKpiBusinessRules}" varStatus="status">
		          <tr id="busRuleRow${status.index}">
		            <td><input type="checkbox" id="kpiBusRuleDeleteAction" name="kpiBusRuleDeleteAction" value="${status.index}"/>
		            </td>
		            <td>
		            	<form:select id="kpiBrand" path="dynamicKpiBusinessRules[${status.index}].brandId">
			                <form:options items="${brand_list}" itemLabel="name" itemValue="id" />
		           		 </form:select>
		           	</td>
		            <td>Actual Total <form:select  path="dynamicKpiBusinessRules[${status.index}].businessRuleOpertor" style="width: 50px;" id="operator" >
		                <option>&lt;</option>
		                <option>&lt;=</option>
		                <option>=</option>
		                <option>&gt;=</option>
		                <option>&gt;</option>
		            </form:select> <form:input path="dynamicKpiBusinessRules[${status.index}].actualTotal" class="span1" type="text"/></td>
		            <td> 
		            	<form:select id="kpiColour" path="dynamicKpiBusinessRules[${status.index}].varianceColorId">
			                 <form:options items="${varianceColor_list}" itemLabel="name" itemValue="id" />
			            </form:select>
		            </td>
		          </tr>	                 
	          </c:forEach>
	        </table>
	        <table>
	        	 <tr>
	            	<td colspan="4">
	            		<a href="javascript:addBusinessRuleRow()" class="btn small">Add</a>&nbsp;
	            		<a href="#" class="btn small">Delete Selected</a>&nbsp;
	            	</td>            
	          	</tr>	  
	        </table>
        </div>
        
        <div style="display: none;">
        	
        	<input type="hidden" id="dynamicKpiBrandsSize" value="${fn:length(crudObj.dynamicKpiBrands)}"/> 
        	<input type="hidden" id="dynamicKpiBusinessRulesSize" value="${fn:length(crudObj.dynamicKpiBusinessRules)}"/>
        	
        	<table id="sampleBenchMarkTable">
        		<tr>  
        			<td><input type="checkbox" id="kpiBrandDeleteAction" name="kpiBrandDeleteAction"/></td>      			
       				<td>
		            	<form:select path="" id="benchMarkBrandSelectId" name="benchMarkBrandSelectName">
			                <form:options items="${brand_list}" itemLabel="name" itemValue="id" />
		           		</form:select>		           		
		           	</td>
		            <td>
		            	<form:input type="text" path="" class="span2" id="benchMarkTextId" name="benchMarkTextName"/>
		            </td>        			
        		</tr>
        	</table>
        	
        	<table id="samplekpiBusRuleTable">
        		<tr id="busRuleRow${status.index}">
		            <td><input type="checkbox" id="kpiBusRuleDeleteAction" name="kpiBusRuleDeleteAction"/>
		            </td>
		            <td>
		            	<form:select path="" id="busRuleBrandSelectId" name="busRuleBrandSelectName">
			                <form:options items="${brand_list}" itemLabel="name" itemValue="id" />
		           		 </form:select>
		           	</td>
		            <td>Actual Total <form:select  path="" style="width: 50px;" id="busRuleOperatorId" name="busRuleOperatorName">
		                <option>&lt;</option>
		                <option>&lt;=</option>
		                <option>=</option>
		                <option>&gt;=</option>
		                <option>&gt;</option>
		            </form:select> <form:input path="" class="span1" type="text" id="busRuleActualTotalId" name="busRuleActualTotalName"/></td>
		            <td> 
		            	<form:select path="" id="busRuleColorId" name="busRuleColorName">
			                 <form:options items="${varianceColor_list}" itemLabel="name" itemValue="id" />
			            </form:select>
		            </td>
		          </tr>        		
        	</table>
        </div>
        
        
        <div id="BenchMarkDiv">       
        	<table id="BenchMarkTable">
	          <tr>
	            <th>#</th>
	            <th>Brand</th>
	            <th>Bench Mark</th>
	          </tr>	          
	          <c:forEach var="dynamicKpiBrand" items="${crudObj.dynamicKpiBrands}" varStatus="status">
		          <tr id="benchmarkRow${status.index}">
		            <td><input type="checkbox" id="kpiBrandDeleteAction" name="kpiBrandDeleteAction" value="${status.index}"/>
		            </td>
		            <td>
		            	<form:select path="dynamicKpiBrands[${status.index}].brandId">
			                <form:options items="${brand_list}" itemLabel="name" itemValue="id" />
		           		</form:select>		           		
		           	</td>
		            <td>
		            	<form:input type="text" path="dynamicKpiBrands[${status.index}].benchMark" class="span2"/>
		            	<form:hidden path="dynamicKpiBrands[${status.index}].kpiBrandId" />
		            </td>
		          </tr>
	          </c:forEach>
	          	                 
	        </table>
	        <table>
	        	 <tr>
	            	<td colspan="3">
	            		<a href="javascript:addBenchMarkRow()()" class="btn small">Add</a>&nbsp;
	            		<a href="javascript:deleteBenchMarkRow()" class="btn small">Delete Selected</a>&nbsp;
	            	</td>            
	          	</tr>	  
	        </table>
	        
        </div>
        
        <div class="actions">
          <c:if test="${not empty crudObj.id}">
			   <input type="submit" class="btn primary" value="Update" />&nbsp;
		  </c:if>
		  <c:if test="${empty crudObj.id}">
		  	   <input type="submit" class="btn primary" value="Save" />&nbsp;
		  </c:if>          
          <a href="#" class="btn">Reset</a>&nbsp;
          <a href="#" class="btn">Cancel</a>                     
        </div>        
      </form:form>
    </div>
    <div class="span4">
      <h6>Help</h6>
      <p>Create a Business Area under which contact with retailers can be organised and access right associated.</p>
    </div>
  </div>
  <script type="text/javascript">
	$(document).ready(function() {
		$("input").filter('.datepicker').datepicker({
			minDate : 0,
			dateFormat : 'yy-mm-dd'
		});
	});
</script>
</body>
</html>