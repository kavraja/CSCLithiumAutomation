package com.cisco.csc.helpers;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.Test;

public class Screenshot() {
  @Test
  public void captureScreenshot() {
	  TakesScreenshot ts = (TakesScreenshot) driver;
	  File source = ts.getScreenshotAs(OutputType.FILE);
	  FileUtils.copyFile(source, new File ());
	  
  }
}
