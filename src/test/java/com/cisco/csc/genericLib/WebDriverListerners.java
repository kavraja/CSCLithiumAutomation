package com.cisco.csc.genericLib;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

public class WebDriverListerners extends AbstractWebDriverEventListener {
private By lastFindBy;
private WebElement lastElement;
private String originalValue;
private long DefaultPageLoadTimeOut = 10;
/*
 *  URL NAVIGATION | NAVIGATE() & GET()
 */
 // Prints the URL before Navigating to specific URL "get("http://www.google.com");"
 @Override
 public void beforeNavigateTo(String url, WebDriver driver) {
  //System.out.println("Before Navigating To : " + url + ", my url was: "    + driver.getCurrentUrl());
 }

  // Prints the current URL after Navigating to specific URL "get("http://www.google.com");"
 @Override
 public void afterNavigateTo(String url, WebDriver driver) {
 // System.out.println("After Navigating To: " + url + ", my url is: "    + driver.getCurrentUrl());
 }

  // Prints the URL before Navigating back "navigate().back()"
 @Override
 public void beforeNavigateBack(WebDriver driver) {
 // System.out.println("Before Navigating Back. I was at "    + driver.getCurrentUrl());
 }

  // Prints the current URL after Navigating back "navigate().back()"
 @Override
 public void afterNavigateBack(WebDriver driver) {
  //System.out.println("After Navigating Back. I'm at "    + driver.getCurrentUrl());
 }

  // Prints the URL before Navigating forward "navigate().forward()"
 @Override
 public void beforeNavigateForward(WebDriver driver) {
 // System.out.println("Before Navigating Forward. I was at "    + driver.getCurrentUrl());
 }

  // Prints the current URL after Navigating forward "navigate().forward()"
 @Override
 public void afterNavigateForward(WebDriver driver) {
 // System.out.println("After Navigating Forward. I'm at "    + driver.getCurrentUrl());
 }


/*
 * ON EXCEPTION | SCREENSHOT, THROWING ERROR
 */
 // Takes screenshot on any Exception thrown during test execution
 @Override
 public void onException(Throwable throwable, WebDriver webdriver) {
  
 }


/*
 * FINDING ELEMENTS | FINDELEMENT() & FINDELEMENTS()
 */
 // Called before finding Element(s)
 @Override
 public void beforeFindBy(By by, WebElement element, WebDriver driver) {
  lastFindBy = by;
  //System.out.println("Trying to find: '" + lastFindBy + "'.");
 // System.out.println("Trying to find: " + by.toString()); // This is optional and an alternate way
       }

 // Called after finding Element(s)
 @Override
 public void afterFindBy(By by, WebElement element, WebDriver driver) {
  lastFindBy = by;
  //System.out.println("Found: '" + lastFindBy + "'.");
  //System.out.println("Found: " + by.toString() + "'."); // This is optional and an alternate way
 }


/*
 * CLICK | CLICK()
 */
 // Called before clicking an Element
 @Override
 public void beforeClickOn(WebElement element, WebDriver driver) {
  //System.out.println("Trying to click: '" + element + "'");
  // Highlight Elements before clicking
	 
	WebDriverCommonLib sh = new WebDriverCommonLib(driver);
	sh.scrollIntoView(element);
	 
  for (int i = 0; i < 1; i++) {
   JavascriptExecutor js = (JavascriptExecutor) driver;
   js.executeScript(
     "arguments[0].setAttribute('style', arguments[1]);",
     element, "color: black; border: 3px solid black;");
  }
  
	WebDriverCommonLib sh1 = new WebDriverCommonLib(driver);
sh1.scrollIntoView(element);
 }

 // Called after clicking an Element
 @Override
 public void afterClickOn(WebElement element, WebDriver driver) {
 // System.out.println("Clicked Element with: '" + element + "'");
 }


/*
 * CHANGING VALUES | CLEAR() & SENDKEYS()
 */
 // Before Changing values
 public void beforeChangeValueOf(WebElement element, WebDriver driver) {
  lastElement = element;
  originalValue = element.getText();

   // What if the element is not visible anymore?
  if (originalValue.isEmpty()) {
   originalValue = element.getAttribute("value");
  }
 }

  // After Changing values
 public void afterChangeValueOf(WebElement element, WebDriver driver) {
  lastElement = element;
  String changedValue = "";
  try {
   changedValue = element.getText();
  } catch (StaleElementReferenceException e) {
   //System.out.println("Could not log change of element, because of a stale"       + " element reference exception.");
   return;
  }
  // What if the element is not visible anymore?
  if (changedValue.isEmpty()) {
   changedValue = element.getAttribute("value");
  }

 //  System.out.println("Changing value in element found " + lastElement    + " from '" + originalValue + "' to '" + changedValue + "'");
 }


/*
 * SCRIPT - this section will be modified ASAP
 */
 // Called before RemoteWebDriver.executeScript(java.lang.String, java.lang.Object[])
 @Override
 public void beforeScript(String script, WebDriver driver) {
 // TODO Auto-generated method stub
       }

  // Called before RemoteWebDriver.executeScript(java.lang.String, java.lang.Object[])
  @Override
        public void afterScript(String script, WebDriver driver) {
 // TODO Auto-generated method stub
       }

}
