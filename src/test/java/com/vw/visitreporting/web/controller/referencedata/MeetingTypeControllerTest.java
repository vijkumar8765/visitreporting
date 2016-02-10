package com.vw.visitreporting.web.controller.referencedata;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.persistence.NoResultException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.apache.commons.lang.StringUtils;
import com.vw.visitreporting.entity.referencedata.MeetingType;
import com.vw.visitreporting.service.referencedata.MeetingTypeService;
import com.vw.visitreporting.web.validator.DoNothingValidator;


/**
 * Unit Test of the MeetingTypeController class.
 * Ensures that the correct service API calls are made and placed on to the request.
 */
@RunWith(JUnit4.class)
public class MeetingTypeControllerTest {

	private MeetingTypeController controller;
    private MeetingTypeService mockService;


    /**
     * Create an instance of the controller to test and inject a mock instance of the service.
     */
    @Before
    public void setUp() {
        mockService = mock(MeetingTypeService.class);
        controller = new MeetingTypeController();
        controller.setMeetingTypeService(mockService);
        controller.setValidator(new DoNothingValidator());
    }

    @After
    public void tearDown() {
        controller = null;
        mockService = null;                
    }
    
    
    /**
     * Test that the list page obtains a list of meeting types from the service
     * and provides an empty meeting type instance to used to create new items.
     */
	@Test
    public void testList() {
    	
		//set return value of mock service method
    	List<MeetingType> meetingTypes = Collections.emptyList();
    	when(mockService.list()).thenReturn(meetingTypes);
    	
    	
    	//invoke controller method
    	Model model = new ExtendedModelMap();
    	
    	String viewName = controller.list(model);
    	
    	
        //verify that our mockService.list Method was called (only once)
        verify(mockService, times(1)).list();
    	
        
    	//check return values of controller method
    	assertThat(viewName, is(equalTo("meetingtype/list")));
    	
    	Map<String, ?> modelMap = model.asMap();
    	MeetingType newInst = (MeetingType)modelMap.get("crudObj");
    	
    	assertThat(newInst, is(notNullValue()));
    	assertThat(newInst.getId(), is(nullValue()));
    	assertThat(modelMap.get("list"), is(equalTo((Object)meetingTypes)));
    }

	/**
	 * Test that trying to add meeting type with a null name returns a validation
	 * error message and does not modify the database.
	 */
	@Test
	public void testSaveWithNullName() {
		assertSaveFailWithMeetingTypeName(null);
	}
	
	/**
	 * Test that trying to add meeting type with an empty name returns a validation
	 * error message and does not modify the database.
	 */
	@Test
	public void testSaveWithEmptyName() {
		assertSaveFailWithMeetingTypeName("");
	}	
	
	/**
	 * Test that trying to add meeting type with name of insufficient length returns a validation
	 * error message and does not modify the database.
	 */
	@Test
	public void testSaveWithTooShortName() {
		assertSaveFailWithMeetingTypeName("a");
	}

	/**
	 * Test that trying to add meeting type with name of too great a length returns a validation
	 * error message and does not modify the database.
	 */
	@Test
	public void testSaveWithTooLongName() {
		assertSaveFailWithMeetingTypeName(StringUtils.repeat("a", 201));
	}
	
	/**
	 * Test that trying to add meeting type with shortest allowed name successfully
	 * creates a new meeting type in the database.
	 */
	@Test
	public void testSaveWithValidName1() {
		assertSaveSuccessWithMeetingTypeName("aa");
	}

	/**
	 * Test that trying to add meeting type with longest allowed name successfully
	 * creates a new meeting type in the database.
	 */
	@Test
	public void testSaveWithValidName2() {
		assertSaveSuccessWithMeetingTypeName(StringUtils.leftPad("", 200, 'a'));
	}

	/**
	 * Helper method that calls the controller save() method with a given meeting type
	 * name and asserts that the response is a failure message.
	 * @param name - the name of the meeting type to insert
	 */
	private void assertSaveFailWithMeetingTypeName(String name) {
		
    	//set return values of mock service
    	List<MeetingType> meetingTypes = Collections.emptyList();
    	when(mockService.list()).thenReturn(meetingTypes);

    	
    	LocalValidatorFactoryBean validatorFactory = new LocalValidatorFactoryBean();
    	//validatorFactory.setConstraintValidatorFactory(Validation.buildDefaultValidatorFactory());
    	validatorFactory.afterPropertiesSet();
    	//ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

		//setup request params
    	MeetingType entity = new MeetingType();
    	MutablePropertyValues mpv = new MutablePropertyValues();
    	mpv.addPropertyValue("name", name);
    	DataBinder binder = new DataBinder(entity);
    	binder.setValidator(validatorFactory);
    	binder.bind(mpv);
    	binder.validate();
    	BindingResult bindingResult = binder.getBindingResult();

    	//set return values of mock service
    	when(mockService.save(entity)).thenReturn(entity);
    	
    	
    	//invoke controller method
    	Model model = new ExtendedModelMap();
    	String viewName = controller.save(entity, bindingResult, model);

    	
        //verify that our mockService.delete and getById() methods were called (only once)
        verify(mockService, times(0)).save(entity);
        
    	
    	//check return values of controller method
        assertThat(bindingResult.hasErrors(), is(equalTo(true)));

        assertThat(viewName, is(equalTo("meetingtype/list")));
    	
    	Map<String, ?> modelMap = model.asMap();
    	
    	assertThat(modelMap.get("list"), is(equalTo((Object)meetingTypes)));

    	assertThat(modelMap.get("success"), is(nullValue()));
	}
	
	/**
	 * Helper method that calls the controller save() method with a given meeting type
	 * name and asserts that the response is a success message.
	 * @param name - the name of the meeting type to insert
	 */
	private void assertSaveSuccessWithMeetingTypeName(String name) {

		//setup request params
    	MeetingType entity = new MeetingType();
    	MutablePropertyValues mpv = new MutablePropertyValues();
    	mpv.addPropertyValue("name", name);
    	DataBinder binder = new DataBinder(entity);
    	binder.bind(mpv);
    	BindingResult bindingResult = binder.getBindingResult();

    	//set return values of mock service
    	when(mockService.save(entity)).thenReturn(entity);
    	
    	//invoke controller method
    	Model model = new ExtendedModelMap();
    	String viewName = controller.save(entity, bindingResult, model);

    	
        //verify that our mockService.delete and getById() methods were called (only once)
        verify(mockService, times(1)).save(entity);

        
    	//check return values of controller method
        assertThat(bindingResult.hasErrors(), is(equalTo(false)));

        assertThat(viewName, is(equalTo("redirect:/content/meetingtype/list")));
    	
    	Map<String, ?> modelMap = model.asMap();
    	
    	assertThat(modelMap.get("successMessages"), is(notNullValue()));
	}
	
	
	/**
	 * Tests that the delete request successfully deletes the meeting type with id=0.
	 */
	@Test
	public void testDelete() {

		MeetingType meetingTypeToDelete = new MeetingType();
		meetingTypeToDelete.setId(0);
    	
		//set return value of mock service method
    	when(mockService.getById(meetingTypeToDelete.getId())).thenReturn(meetingTypeToDelete);
    	when(mockService.save(meetingTypeToDelete)).thenReturn(meetingTypeToDelete);
		
		
    	//invoke controller method
    	Model model = new ExtendedModelMap();
    	String viewName = controller.delete(meetingTypeToDelete.getId().toString(), model);


        //verify that our mockService.delete and getById() methods were called (only once)
        verify(mockService, times(1)).getById(meetingTypeToDelete.getId());
        verify(mockService, times(1)).save(meetingTypeToDelete);
        verify(mockService, times(0)).delete(meetingTypeToDelete);

    	
    	//check return values of controller method
    	assertThat(viewName, is(equalTo("redirect:/content/meetingtype/list")));
    	
    	assertThat(meetingTypeToDelete.isDisabled(), is(equalTo(true)));
	}
	
	
	/**
	 * Test that trying to delete a meeting type that doesn't exist throws an Exception.
	 */
	@Test(expected=NoResultException.class)
	public void testDeleteNonExistingEntity() {
		when(mockService.getById(Integer.valueOf(-1))).thenThrow(new NoResultException());
    	controller.delete("-1", new ExtendedModelMap());
	}
	
	
	/**
	 * Test that the /create page from the abstract controller class is not exposed
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void testCreate() {
		controller.create(new ExtendedModelMap());
	}
	
	/**
	 * Test that the /update page from the abstract controller class is not exposed
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void testUpdate() {
		controller.update("0", new ExtendedModelMap());
	}
}
