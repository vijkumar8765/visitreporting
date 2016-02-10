package com.vw.visitreporting.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.not;
import org.junit.Test;


/**
 * Unit test the AppInfo class
 */
public class AppInfoTest {

	/**
	 * Test the the required properties in the build.properties resource are successfully
	 * being read by the application.
	 */
	@Test
	public void testVersionAndArtifactNameAreAvailable() {
		
		assertThat(AppInfo.getVersion(), notNullValue());
		assertThat(AppInfo.getVersion(), not(equalTo("${pom.version}")));
		assertThat(AppInfo.getVersion(), not(equalTo("<Unknown>")));

		assertThat(AppInfo.getArtifactName(), notNullValue());
		assertThat(AppInfo.getArtifactName(), not(equalTo("${project.build.finalName}")));
		assertThat(AppInfo.getArtifactName(), not(equalTo("dev")));

		assertThat(AppInfo.getBuildDirectory(), notNullValue());
		assertThat(AppInfo.getBuildDirectory(), not(equalTo("${project.build.directory}")));
		assertThat(AppInfo.getBuildDirectory(), not(equalTo(".")));
	}
}
