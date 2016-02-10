package com.vw.visitreporting.common;

import java.util.Properties;


/**
 * The Maven2 build script for this project has project.build.resources.resource.filtering=true
 * which means that certain build properties are substituted into the resource file /build.properties
 * as the file is copied to the target folder. This class extracts those build properties to make
 * them available to the application via static properties of this class. 
 */
public final class AppInfo {

	private static String version;
	private static String artifactName;
	private static String buildDirectory;
	private static boolean noPreAuth;

	static {
		try {

			Properties props = new Properties();
			props.load(AppInfo.class.getResourceAsStream("/build.properties"));
			props.load(AppInfo.class.getResourceAsStream("/config.properties"));
			version = props.getProperty("pom.version");
			artifactName = props.getProperty("project.build.finalName");
			buildDirectory = props.getProperty("project.build.directory");
			noPreAuth = "true".equalsIgnoreCase(props.getProperty("config.noPreAuth"));

			if("${pom.version}".equals(version)) {
				version = "(Development)";
			}
			if("${project.build.finalName}".equals(artifactName)) {
				artifactName = "dev";
			}
			if("${project.build.directory}".equals(buildDirectory)) {
				buildDirectory = ".";
			}

		} catch(Throwable err) {
			version = "<Unknown>";
			artifactName = "dev";
			buildDirectory = ".";
			noPreAuth = false;
		}
	}

	/**
	 * Gets the version number or this application from Maven2 build file.
	 */
	public static String getVersion() {
		return version;
	}
	
	/**
	 * Gets the name of the WAR file (without .war extension) of this application from Maven2 build file.
	 */
	public static String getArtifactName() {
		return artifactName;
	}
	
	/**
	 * Gets the full path of the directory that will contain the new WAR file after a build
	 */
	public static String getBuildDirectory() {
		return buildDirectory;
	}
	
	/**
	 * Gets whether or not a pre-authentication solution is in place.
	 */
	public static boolean isNoPreAuth() {
		return noPreAuth;
	}

	/**
	 * Private constructor. This class cannot be instantiated.
	 * Only the static methods should be used.
	 */
	private AppInfo() {
	}
}
