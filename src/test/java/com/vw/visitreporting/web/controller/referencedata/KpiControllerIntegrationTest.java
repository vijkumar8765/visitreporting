package com.vw.visitreporting.web.controller.referencedata;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import com.vw.visitreporting.common.GenericService;
import com.vw.visitreporting.entity.referencedata.Kpi;
import com.vw.visitreporting.security.SecurityUtils;
import com.vw.visitreporting.web.AbstractControllerIntegrationTest;

public class KpiControllerIntegrationTest extends AbstractControllerIntegrationTest {

	@Autowired
	private KpiController kpiController;
		
	@Autowired
	private AnnotationMethodHandlerAdapter handlerAdapter;
	
	@Autowired
	private SecurityUtils securityUtils;
	
	@Autowired
	private GenericService<Kpi> genericService;
	
	private MockHttpServletRequest request; 
    private MockHttpServletResponse response; 

    @Before 
    public void setUp() { 
       request = new MockHttpServletRequest(); 
       response = new MockHttpServletResponse(); 
    } 
	
	/**
	 *  Test the list screen functionality of KPI
	 *  
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")	
	@Test
	public void testKpiListLandingPage() throws Exception { //NOPMD

		securityUtils.loginUser("test_user");
		
		request.setRequestURI("/kpi/list"); 
        request.setMethod("GET");
		
        final ModelAndView mav = handlerAdapter.handle(request, response,  kpiController); 
		ModelAndViewAssert.assertViewName(mav, "kpi/list");
		
		List<Kpi> kpiListInModel = (List<Kpi>)mav.getModelMap().get("list");

		assertTrue("Check KpiList size in Model for sample data size of 0", kpiListInModel.size() >= 0);		
		assertTrue("Check Kpi Business Areas Size in kpilist for sample data size of 0", 
												kpiListInModel.get(0).getBusinessAreas().size() >= 0);
		
		assertTrue("Check Kpi Business Areas Size in kpilist for sample data size of 0", 
				kpiListInModel.get(0).getKpiBrands().size() >= 0);	
	}
	
	/**
	 * Test the Landing page for a page that creates a new KPI type.
	 *  
	 * @throws Exception
	 */
	@Test 
	public void testShowCreateKpiSuccessScenario() throws Exception { // NOPMD

		securityUtils.loginUser("test_user");
		
		request.setRequestURI("/kpi/create");
		request.setMethod("GET");

		final ModelAndView mav = handlerAdapter.handle(request, response, kpiController);
		ModelAndViewAssert.assertViewName(mav, "kpi/createEdit");
	} 
	
	/**
	 * Test method to create kpi with the minimum data required.
	 * 
	 * @throws Exception
	 */
	@Test 
	public void testCreateMinimumDataKpiSuccessScenario() throws Exception { //NOPMD
		
		request.setRequestURI("/kpi/save");	
		request.setMethod("POST");
		
		request.addParameter("kpiDescription", "Retail Hours");
		request.addParameter("organisation.id", "1");
		request.addParameter("effectiveStartDate", "2011-12-12");
		request.addParameter("effectiveEndDate", "2012-12-12");
		//request.addParameter("status", "2");
		//request.addParameter("createdBy", "fox0kh8");
		//request.addParameter("createdDate", "2011-12-14");			
		
		final ModelAndView mav = handlerAdapter.handle(request, response, kpiController);
		
		String successMessages = (String)mav.getModelMap().get("successMessages");
		assertTrue("Check for the sucesses message for kpi", successMessages.contains("Saved kpi"));
		
		Kpi crudObj = (Kpi)mav.getModelMap().get("crudObj");
		assertTrue("Check kpiDescription valued modified is available in model crudObj", crudObj.getId().intValue() >= 3);
				
		ModelAndViewAssert.assertViewName(mav, "redirect:/content/kpi/list");
	} 
	
	
	/**
	 * Test method to create kpi with the maximum data can be supplied.
	 * 
	 * @throws Exception
	 */
	@Test 
	public void testCreateMaximumDataKpiSuccessScenario() throws Exception { // NOPMD
		
		request.setRequestURI("/kpi/save");	
		request.setMethod("POST");
		
		request.addParameter("kpiDescription", "Retail Hours");
		request.addParameter("organisation.id", "2");
		request.addParameter("effectiveStartDate", "2011-12-12");
		request.addParameter("effectiveEndDate", "2012-12-12");
		request.addParameter("status", "2");
		request.addParameter("createdBy", "fox0kh8");
		request.addParameter("createdDate", "2011-12-14");
		request.addParameter("kpiCategory.id", "3");
		
		request.addParameter("newStatus", "1");
		request.addParameter("newStatusEffectiveDate", "2012-12-12");
		request.addParameter("percentage", "0");
		request.addParameter("displaySequence", "3");
		
		final ModelAndView mav = handlerAdapter.handle(request, response, kpiController);
		
		String successMessages = (String)mav.getModelMap().get("successMessages");
		assertTrue("Check for the sucesses message for kpi", successMessages.contains("Saved kpi"));
		
		Kpi crudObj = (Kpi)mav.getModelMap().get("crudObj");
		assertTrue("Check kpiDescription valued modified is available in model crudObj", crudObj.getId().intValue() >= 3);
				
		ModelAndViewAssert.assertViewName(mav, "redirect:/content/kpi/list");
	} 
	
	/**
	 * Test method when a mandatory filed is missing
	 * 
	 * @throws Exception
	 */
	@Test	
	public void testCreateKpiDescriptionMissingErrorScenario() throws Exception { // NOPMD
		
		request.setRequestURI("/kpi/save");	
		request.setMethod("POST");
		
		//request.addParameter("kpiDescription", "Retail Hours");
		request.addParameter("organisation.id", "2");
		request.addParameter("effectiveStartDate", "2011-12-12");
		request.addParameter("effectiveEndDate", "2012-12-12");
		request.addParameter("status", "2");
		request.addParameter("createdBy", "fox0kh8");
		request.addParameter("createdDate", "2011-12-14");
		request.addParameter("kpiCategory.id", "3");
		
		request.addParameter("newStatus", "1");
		request.addParameter("newStatusEffectiveDate", "2012-12-12");
		request.addParameter("percentage", "0");
		request.addParameter("displaySequence", "3");
		
		final ModelAndView mav = handlerAdapter.handle(request, response, kpiController);
		
		String errorMessages = (String)mav.getModelMap().get("errorMessages");
		assertNotNull("Erroe Message Should Not be Null", errorMessages);
				
		ModelAndViewAssert.assertViewName(mav, "kpi/createEdit");
	} 
	
	
	/**
	 * Test the Landing page for a page to edit KPI type.
	 *  
	 * @throws Exception
	 */
	@Test 
	public void testShowEditKpiSuccessScenario() throws Exception { // NOPMD

		securityUtils.loginUser("test_user");
		
		request.setRequestURI("/kpi/1/update");
		request.setMethod("GET");

		final ModelAndView mav = handlerAdapter.handle(request, response, kpiController);
		ModelAndViewAssert.assertViewName(mav, "kpi/createEdit");
		
		
		Kpi crudObj = (Kpi)mav.getModelMap().get("crudObj");
		assertEquals("Check kpiDescription valued modified is available in model crudObj", crudObj.getId().intValue(), 1);	
	} 
	
	
	/**
	 * Test method to update kpi with minimum data required. 
	 * 
	 * @throws Exception
	 */
	@Test 
	public void testEditKpiMinimumSuccessScenario() throws Exception { // NOPMD
		
		request.setRequestURI("/kpi/save");	
		request.setMethod("POST");
		
		request.addParameter("id", "1");
		request.addParameter("kpiDescription", "Retail Hours");
		
		final ModelAndView mav = handlerAdapter.handle(request, response, kpiController);
		
		String successMessages = (String)mav.getModelMap().get("successMessages");
		assertTrue("Check for the sucesses message for kpi", successMessages.contains("Saved kpi"));
		
		Kpi crudObj = (Kpi)mav.getModelMap().get("crudObj");
		assertEquals("Check kpiDescription valued modified is available in model crudObj", crudObj.getKpiDescription(), "Retail Hours");		
				
		ModelAndViewAssert.assertViewName(mav, "redirect:/content/kpi/list");
	} 
	
	/**
	 * Test method to update kpi with maximum data required. 
	 * 
	 * @throws Exception
	 */
	@Test 
	public void testEditKpiMaximumSuccessScenario() throws Exception { // NOPMD
		
		request.setRequestURI("/kpi/save");	
		request.setMethod("POST");
		
		request.addParameter("id", "1");
		request.addParameter("kpiDescription", "Retail Hours");
		request.addParameter("organisation.id", "2");
		request.addParameter("effectiveStartDate", "2011-12-12");
		request.addParameter("effectiveEndDate", "2012-12-12");
		request.addParameter("status", "1");
		request.addParameter("createdBy", "fox0kh8");
		request.addParameter("createdDate", "2011-12-15");
		request.addParameter("kpiCategory.id", "2");
		
		request.addParameter("newStatus", "1");
		request.addParameter("newStatusEffectiveDate", "2012-12-12");
		request.addParameter("percentage", "0");
		request.addParameter("displaySequence", "3");
		
		final ModelAndView mav = handlerAdapter.handle(request, response, kpiController);		
		
		Kpi crudObj = (Kpi)mav.getModelMap().get("crudObj");
		
		String successMessages = (String)mav.getModelMap().get("successMessages");
		assertTrue("Check the sucesses message for kpi", successMessages.contains("Saved kpi"));		
		assertEquals("Check kpiDescription valued modified is available in model crudObj", crudObj.getOrganisation().getId().intValue(), 2);	
		
		Kpi kpiGeneric = genericService.getById(Long.valueOf(1), Kpi.class);
		assertNotSame("Check kpiDescription valued modified is available in model crudObj", 2 , kpiGeneric.getOrganisation().getId().intValue());	
								
		ModelAndViewAssert.assertViewName(mav, "redirect:/content/kpi/list");
	} 
	
	
}
