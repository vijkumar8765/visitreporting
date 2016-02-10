package com.vw.visitreporting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


/**
 * Provides some custom matchers for this system for use with the hamcrest unit testing framework.
 */
public final class CustomMatchers {

	private CustomMatchers() {
	}
	
	/**
	 * Opens a file and checks that it contains a specified string
	 * @param text - the text to search for on a line of the file
	 */
	public static Matcher<File> containsText(final String text) {
		return new TypeSafeMatcher<File>() {
			private File fileTested = null;
			private String errMsg = null;
			public boolean matchesSafely(File item) {
				fileTested = item;
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new InputStreamReader(new FileInputStream(item)));

					String line = null;
					while( (line = reader.readLine()) != null) {
						if(line.contains(text)) {
							return true;
						}
					}

				} catch(IOException err) {
					errMsg = "could not read file: "+err.getMessage();
				} finally {
					try {
						reader.close();
					} catch(IOException err) {
						reader = null;
					}
				}
				return false;
			}

			public void describeTo(Description description) {
				description.appendText(" that file ");
				description.appendValue(fileTested);
				description.appendText(" contained text ");
				description.appendText(text);
				if(errMsg == null) {
					description.appendText(" but it didn't");
				} else {
					description.appendText(" but ");
					description.appendText(errMsg);
				}
			}
		};
	}
}
