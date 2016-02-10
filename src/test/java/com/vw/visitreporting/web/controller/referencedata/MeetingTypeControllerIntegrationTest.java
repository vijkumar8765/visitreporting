package com.vw.visitreporting.web.controller.referencedata;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.ModelAndViewAssert;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import com.vw.visitreporting.entity.referencedata.MeetingType;
import com.vw.visitreporting.service.referencedata.MeetingTypeService;
import com.vw.visitreporting.web.AbstractControllerIntegrationTest;


/**
 * Performs an integration test using the MeetingTypeController class.
 */
public class MeetingTypeControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private MeetingTypeService meetingTypeService;
	
    
	/**
	 * Test that edit functions landing page has necessary data.
	 */
	@Test
    public void testListLandingPage() {

        ModelAndView mav = handle("GET", "/meetingtype/list");
        
        ModelAndViewAssert.assertViewName(mav,"meetingtype/list");
        
        assertContainsAllMeetingTypes(mav);
    }
    
    /**
     * Test that a meeting type can be created, if the name property is valid,
     * or that a validation error occurs if the name is not valid.
     */
    @Test
    public void testCreatingMeetingType() {
    	assertFalse("empty meeting type",         tryCreate(""));
    	assertFalse("too short meeting type",     tryCreate("a"));
    	assertTrue("minimum length meeting type", tryCreate("aa"));
    	assertTrue("maximum length meeting type", tryCreate(StringUtils.repeat("a", 200)));
    	assertFalse("too long meeting type",      tryCreate(StringUtils.repeat("a", 201)));
    }
    
    private boolean tryCreate(String meetingTypeName) {

    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("name", meetingTypeName);

        ModelAndView mav = handle("POST", "/meetingtype/save", params);
		
		ModelAndViewAssert.assertViewName(mav,"meetingtype/list");
		
		assertContainsAllMeetingTypes(mav);
		
		//check new entity is in database
		@SuppressWarnings("unchecked")
		List<MeetingType> list = (List<MeetingType>)mav.getModel().get("list");
		for(MeetingType type : list) {
			if(type.getName().equals(meetingTypeName)) {
				return true;
			}
		}
		
		//if not check for validation error
		BindingResult errors = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "org.springframework.validation.BindingResult.crudObj", BindingResult.class);
		assertThat(errors.getFieldErrorCount("name"), is(greaterThan(0)));
		
		return false;
    }
    
    
    /**
     * Test that a meeting type can be deleted.
     */
    @Test
    public void testDeletingMeetingType() {
    	
    	int prevCount = meetingTypeService.list().size();
    	
        ModelAndView mav = handle("GET", "/meetingtype/0/delete");
		
		ModelAndViewAssert.assertViewName(mav,"meetingtype/list");
		
		assertContainsAllMeetingTypes(mav);

		assertThat(prevCount, is(greaterThan(meetingTypeService.list().size())));
    }
    	
    
    private void assertContainsAllMeetingTypes(ModelAndView mav) {
    	ModelAndViewAssert.assertSortAndCompareListModelAttribute(mav, "list", meetingTypeService.list(), new Comparator<MeetingType>() {
			public int compare(MeetingType o1, MeetingType o2) {
				return o1.getId().compareTo(o2.getId());
			}
    	});
    }
}
