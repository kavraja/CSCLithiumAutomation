package com.cisco.csc.genericLib;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class PropertyHandler {

	static Properties prop = null;

	public static void load(String env) {
		try {
			prop = new Properties();
			prop.load(new FileReader(getEnvConfigXMLPath(env)));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static Properties getProperties(String env) {
		if (prop == null)
			load(env);
		return prop;

	}

	private static String getEnvConfigXMLPath(String env) {
		StringBuffer srtBuffer = new StringBuffer("TestData/");
		// System env variable is mapped to configuration/[env] folder
		srtBuffer.append(env);
		srtBuffer.append("/site_details.properties");
		return srtBuffer.toString();
	}
	

}
