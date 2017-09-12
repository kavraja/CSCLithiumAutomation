package com.cisco.csc.genericLib;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;


public class WebDriverCommonLib {
	
    private static int EXPLICIT_WAIT_TIME_OUT = 15;
	private static WebDriver driver = null;
	private long DefaultPageLoadTimeOut = 10;
	
	 public WebDriverCommonLib(WebDriver driver)
	 {
		 this.driver = driver;
	 }
	 
	  /**@param _driver
	     * @param byKnownElement
	     */
	    public void pageLoad(WebDriver _driver, WebElement byKnownElement) {

	        //assigning driver instance globally.
	        driver = _driver;
	        this.isReadyState();
	        this.correctPageLoadedCheck(byKnownElement);
	    }

	    /**
	     * Verifies correct page was returned.
	     * @param by
	     */
	    private void correctPageLoadedCheck(WebElement byKnownElement) {

	        this.isReadyState();
	        try {
	        	byKnownElement.isDisplayed();
	        } catch (NoSuchElementException ex) {
	            throw ex;
	        }
	    }

	    /* Waits until the Document Object Model is in ready State.
	     */
	    private void isReadyState() {

	        ExpectedCondition<Boolean> pageLoadCondition = new
	                ExpectedCondition<Boolean>() {
	                    public Boolean apply(WebDriver driver) {
	                        return ((JavascriptExecutor) driver).executeScript("document.readyState.domReadyState").equals("complete");
	                    }
	                };

	        WebDriverWait wait = new WebDriverWait(driver, DefaultPageLoadTimeOut);
	        wait.until(pageLoadCondition);
	    }
	 
	 public void refreshPage()
	 {
		 driver.navigate().refresh();
	 }
	 
	
	 public boolean findElementPresent(String xpath)
	 {
			
	 try{
		driver.findElement(By.xpath(xpath)).getLocation();
		}
	 catch(Exception e)
			{return false;}
			return true;
			
	 }
	 
	public void waitForEleVisible(String action, WebElement ele)
	{
		WebDriverWait wait = new WebDriverWait(driver, 30);
		if(action.equals("visible"))
		{
			wait.until(ExpectedConditions.visibilityOf(ele));
		}
		if(action.equals("clickable"))
		{
			wait.until(ExpectedConditions.elementToBeClickable(ele));
		
		}
	 }
	 
	 
	 
	 public void WaitForAjax() throws InterruptedException
	    {

	        while (true)
	        {

	            Boolean ajaxIsComplete = (Boolean) ((JavascriptExecutor)driver).executeScript("return jQuery.active == 0");
	            if (ajaxIsComplete){
	                break;
	            }
	            Thread.sleep(400);
	        }
	    }
	 
	 public void waitForLoad () {
	        ExpectedCondition<Boolean> expectation = new
	                ExpectedCondition<Boolean>() {
	                    public Boolean apply(WebDriver driver) {
	                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
	                    }
	                };
	                try {
	    	            Thread.sleep(1000);
	    	            WebDriverWait wait = new WebDriverWait(driver,70);
	    	            wait.until(expectation);
	    	        } catch (Throwable error) {
	    	            Assert.fail("Timeout waiting for Page Load Request to complete.");
	 
	  }
	 }


 	 public static  void resizeWindow(final int width, final int height) {
	        try {
	            ExtentTestManager.ReportLogInfo("Resize browser window to width " + width + " height " + height);

	            Dimension size = new Dimension(width, height);
	            driver.manage().window().setPosition(new Point(0, 0));
	            driver.manage().window().setSize(size);
	        } catch (Exception ex) { }
	    }

	 public  void maximizeWindow() {
	        try {
	           
	            driver.manage().window().maximize();
	        } catch (Exception ex) {

	            try {
	                ((JavascriptExecutor) driver).executeScript(
	                    "if (window.screen){window.moveTo(0, 0);window.resizeTo(window.screen.availWidth,window.screen.availHeight);}");
	            } catch (Exception ignore) {
	                ExtentTestManager.ReportLogInfo("Unable to maximize browser window. Exception occured: " + ignore.getMessage());
	            }
	        }
	    }
	    
	 public  void assertAlertPresent() {
	        ExtentTestManager.ReportLogInfo("Switching : alert present.");

	        try {
	            driver.switchTo().alert();
	        } catch (Exception ex) {
	            ExtentTestManager.ReportLogInfo("No Switching :expecte alert");
	        }
	    }

	    public  void assertAlertText(final String text) {
	        Alert alert = driver.switchTo().alert();
	        String alertText = alert.getText();
	        
	      //  assertAlertHTML(alertText.contains(text), "assert alert text.");
	    }
	    
	    public  void acceptAlert()  {
	    	WebDriverWait wait = new WebDriverWait(driver, 30 /*timeout in seconds*/);
	    	if(wait.until(ExpectedConditions.alertIsPresent())==null)
	    	{
	    	    ExtentTestManager.ReportLogInfo("alert was not present");
	    	}
	    	else
	    	    {ExtentTestManager.ReportLogInfo("alert was present");
	    	    Alert alert = driver.switchTo().alert();
		        alert.accept();
		        driver.switchTo().defaultContent();
		        }
	        
	    }

	   
	
	    
	    
	    /**
	     * Returns the 'width' property of the underlying WebElement's Dimension.
	     *
	     * @return
	     */
	    public  int getWidth(WebElement element) {
	        return element.getSize().getWidth();
	    }

	    
	    /**
	     * Indicates whether or not the web element is currently displayed in the browser.
	     *
	     * @return
	     */
	    public  boolean isDisplayed(WebElement element) {

	        try {
	            return element.isDisplayed();
	        } catch (Exception e) {
	            return false;
	        }
	    }

	   
	    /**
	     * Indicates whether or not the element is enabled in the browser.
	     *
	     * @return
	     */
	    public  boolean isEnabled(WebElement element) {
	        return element.isEnabled();
	    }

	    /**
	     * Indicates whether or not the element is selected in the browser.
	     *
	     * @return
	     */
	    public  boolean isSelected(WebElement element) {
	        return element.isSelected();
	    }

	    /**
	     * Whether or not the indicated text is contained in the element's getText() attribute.
	     *
	     * @param   text
	     *
	     * @return
	     */
	    public  boolean isTextPresent(String text,WebElement element) {
	        return element.getText().contains(text);
	    }

	    /**
	     * Forces a mouseDown event on the WebElement.
	     */
	    public  void mouseDown(WebElement element) {
	        Mouse mouse = ((HasInputDevices) driver).getMouse();
	        mouse.mouseDown(null);
	    }

	    /**
	     * Forces a mouseOver event on the WebElement.
	     */
	    public  void mouseOver(WebElement element) {
	      

	       // build and perform the mouseOver with Advanced User Interactions API
	         Actions builder = new Actions(driver);
	        builder.moveToElement(element).build().perform();
	       // Locatable hoverItem = (Locatable) element;
	       // Mouse mouse = ((HasInputDevices) driver).getMouse();
	       // mouse.mouseMove(hoverItem.getCoordinates());
	    }

	    
	    //this is written using actions class
	    public void hoverMouseOverElement(WebElement element)
	    {
	    	
	    
	    	Actions action = new Actions(driver);
	    	scrollIntoView(element);
	    	action.moveToElement(element).build().perform();
	    	
	    }
	    

		public void mouseHoverJScript(WebElement HoverElement) {
			try {
				if (isElementPresent(HoverElement)) {
					
					String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
					((JavascriptExecutor) driver).executeScript(mouseOverScript,
							HoverElement);

				} else {
					ExtentTestManager.ReportLogInfo("Element was not visible to hover " + "\n");

				}
			} catch (StaleElementReferenceException e) {
				ExtentTestManager.ReportLogInfo("Element with " + HoverElement
						+ "is not attached to the page document"+ e.getStackTrace());
			} catch (org.openqa.selenium.NoSuchElementException e) {
				ExtentTestManager.ReportLogInfo("Element " + HoverElement + " was not found in DOM"
						+ e.getStackTrace());
			} catch (Exception e) {
				e.printStackTrace();
				ExtentTestManager.ReportLogInfo("Error occurred while hovering"
						+ e.getStackTrace());
			}
		}

		public  boolean isElementPresent(WebElement element) {
			boolean flag = false;
			try {
				if (element.isDisplayed()
						|| element.isEnabled())
					flag = true;
			} catch (NoSuchElementException e) {
				flag = false;
			} catch (StaleElementReferenceException e) {
				flag = false;
			}
			return flag;
		}
	    
	    
	    /**
	     * Forces a mouseOver event on the WebElement using simulate by JavaScript way for some dynamic menu.
	     */
	    public  void simulateMouseOver(WebElement element) {
	        String mouseOverScript =
	            "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript(mouseOverScript, element);
	    }

	    /**
	     * Forces a mouseUp event on the WebElement.
	     */
	    public  void mouseUp(WebElement element) {
	         Mouse mouse = ((HasInputDevices) driver).getMouse();
	        mouse.mouseUp(null);
	    }

	    
	    public void scrollIntoView(WebElement	element){
	        String scrollIntoScript = "arguments[0].scrollIntoView(true);";
		        JavascriptExecutor js = (JavascriptExecutor) driver;
		        js.executeScript(scrollIntoScript, element);
	    	
	    	
	    }
	    
	    /**
	     * Method, which should never be used.
	     */
	    protected  void sleep(final int waitTime) throws InterruptedException {
	        Thread.sleep(waitTime);
	    }

public void handleDropDown(WebElement DD, String txt)
{
	Select dropdown = new Select(DD);	
	dropdown.selectByVisibleText(txt);
}

public void movetoElem(WebElement elem)
{
	Actions act = new Actions(driver);
	act.moveToElement(elem).build().perform();
}

public String getURL()
{
return  driver.getCurrentUrl();	

}


public String getDynamicTitle(String normalTitle) {
	String dynamicTitle = normalTitle;
	try {
		String sysName = InetAddress.getLocalHost().getHostName();
		dynamicTitle += "_" + sysName;
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	String tempTimeStamp = new SimpleDateFormat("dd/MMM/yy_HH:mm:ss").format(new Date());
	dynamicTitle += "_" + tempTimeStamp;

	return dynamicTitle;
}

public void setTimeoutForCheck(int seconds)
{
driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS)	;
}


}
