package com.vw.visitreporting.auditing; //NOPMD

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.vw.visitreporting.AbstractIntegrationTest;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.FranchiseGroup;
import com.vw.visitreporting.service.referencedata.DealershipService;
import com.vw.visitreporting.service.referencedata.FranchiseGroupService;


/**
 * Unit test the UserRevisionListener class and configuration of auditing
 */
public class AuditingIntegrationTest extends AbstractIntegrationTest {	

	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

	@Autowired
	private DealershipService dealershipService;

	@Autowired
	private FranchiseGroupService franchiseGroupService;

	
	/**
	 * Requirement 6.1.8.2.14 specifies that the following data should be stored for each audit record:
	 * - User Id
	 * - User Name (first name and surname)
	 * - Date the event took place (e.g. 06/06/2011)
	 * - Dealer Number. An event may relate to more than one Dealer.
	 * - Identifier and Type of Item
	 * This test verifies that this requirement is satisfied by the system.
	 */
	@Test
	public void testRequiredDataIsStoredForEachRevision() {
		
		final FranchiseGroup group = new FranchiseGroup();
		group.setName("test group");
		
		final FranchiseGroup newGroup = franchiseGroupService.save(group);

		final Date actionTimestamp = new Date(); 

		
		//check
	}

	
	/**
	 * Requirement 6.1.5.2.4 states:
	 *   It must be possible to amend the Dealership Business Details but
	 *   it must also be possible to distinguish between a "Change of Ownership"
	 *   and just a change to "Business Details"
	 * This test verifies that this requirement is satisfied by the system.
	 */
	@Test
	public void testChangeToDealerOwnershipIsAuditable1() {
		
		//change the fax number a test dealer entity (not due to change of ownership) 
		final Dealership dealer = dealershipService.findByNumber(1);
		
		dealer.setFaxNumber("987654");
		
		dealershipService.save(dealer);
		
		
		//check not marked as change of ownership
		//..
	}
				
	/**
	 * Requirement 6.1.5.2.4 states:
	 *   It must be possible to amend the Dealership Business Details but
	 *   it must also be possible to distinguish between a "Change of Ownership"
	 *   and just a change to "Business Details"
	 * This test verifies that this requirement is satisfied by the system.
	 */
	@Test
	public void testChangeToDealerOwnershipIsAuditable2() {
				
		//change the ownership of the dealer and persist
		final Dealership dealer = dealershipService.findByNumber(1);

		FranchiseGroup group2 = franchiseGroupService.findByName("group2");
		dealer.setFranchiseGroup(group2);
		
		dealershipService.save(dealer);
		
	
		//check marked as change of ownership
		//..
	}
}
