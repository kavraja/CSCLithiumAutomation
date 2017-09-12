package com.cisco.csc.testscripts;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.InvalidCookieDomainException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.IResultMap;
import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.internal.annotations.ITest;

import com.cisco.csc.genericLib.ExcelUtil;
import com.cisco.csc.genericLib.ExtentManager;
import com.cisco.csc.genericLib.ExtentTestManager;
import com.cisco.csc.genericLib.PropertyHandler;
import com.cisco.csc.genericLib.WebDriverCommonLib;
import com.cisco.csc.genericLib.WebDriverListerners;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

import jxl.read.biff.BiffException;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;
import net.lightbody.bmp.core.har.HarPage;
import net.lightbody.bmp.proxy.CaptureType;

public class BaseTest {
	public ExcelUtil excelUtil;
	BrowserMobProxy proxy;
	static List<Har> harFiles;
	String contentType = "";
	String descriptionBuilder = "";
	static List<String> previousResults;
	String isProxy = "";
	static Map<String, Cookie> userCookies;
	String currentUser = "";
	String previousUser = "";
	static String environment = "";
	static String ContentCreationDataFilePath = "";
	

	String S_no = "";
	String filePath = "";
	String filePath2 = "";


	String userNameOne = "";
	String passOne = "";
	String userNameTwo = "";
	String passTwo = "";
	



	public BaseTest() {

		excelUtil = new ExcelUtil();

	}

	public BaseTest(String contentType) {
		// TODO Auto-generated constructor stub
		excelUtil = new ExcelUtil();
		this.contentType = contentType;
		OS = System.getProperty("os.name").toLowerCase();
		// userCookies = new HashMap<>();

	}

	public WebDriver driverOne;
	public EventFiringWebDriver driver;
	public WebDriverListerners handle;

	static String url = "";
	private static Map<String, WebDriver> drivers = new HashMap<String, WebDriver>();
	public static String browser;
	String filePathsrc = "./Reports/screenshots/";
	static int i = 1;
	static String OS;
	public static ExtentReports extent;

	@BeforeSuite
	public void extentSetup(ITestContext context) throws InterruptedException {
		setExcelPath(context);
		Reporter.log("before suite");
		Reporter.log("setting up cookie container");
		userCookies = new HashMap<>();
		OS = System.getProperty("os.name").toLowerCase();
		clearTheDirectory();
		ExtentManager.setOutputDirectory(context);
		extent = ExtentManager.getInstance();
		isProxy = context.getCurrentXmlTest().getLocalParameters().get("proxy");

	}

	public static void setExcelPath(ITestContext context) {
		Reporter.log("Setting up environment");
		environment = context.getCurrentXmlTest().getParameter("env");
		Reporter.log("environment is : " + environment);
		Properties prop = PropertyHandler.getProperties(environment);
		url = prop.getProperty("SITE_URL");
		Reporter.log("setting the excel data path");

		ContentCreationDataFilePath = "TestData/" + environment + "/CSC_Chrome.xls";


		Reporter.log("reading excel data from " + ContentCreationDataFilePath);

		Reporter.log("Site Url : " + url);
	}

	@AfterSuite(alwaysRun = true)
	public void generateReport() {
		try{	
		//extent.close();
		}
		catch(Exception ex)
		{
			ExtentTestManager.endTest();
			//extent.close();
		}
	}

	@BeforeMethod(alwaysRun = true)
	public void SetUpTests(Method method, ITestContext context) throws InterruptedException {

		if (!method.getAnnotation(Test.class).description().isEmpty()) {

			ExtentTestManager.startTest(method.getAnnotation(Test.class).description() + contentType)
					.assignCategory(context.getCurrentXmlTest().getName());
		} else {
			{
				ExtentTestManager.startTest(method.getName()).assignCategory(context.getCurrentXmlTest().getName());
			}

		}

		browser = context.getCurrentXmlTest().getLocalParameters().get("browser");
		Reporter.log("Executing method : " + method.getName());
		// startBrowser(browser);

	}

	@AfterMethod(alwaysRun = true)
	public void afterEachTestMethod(ITestResult result) throws InterruptedException {
		ExtentTestManager.getTest().getTest().setStartedTime(getTime(result.getStartMillis())); // new
		ExtentTestManager.getTest().getTest().setEndedTime(getTime(result.getEndMillis())); // new

		Reporter.log("method : " + result.getName().toString().trim() + " status of the method executed : "
				+ result.getStatus());
		for (String group : result.getMethod().getGroups()) {
			
			
			ExtentTestManager.getTest().assignCategory(group); // new
		}

		String methodName = result.getName().toString().trim();
		String className = result.getMethod().getTestClass().getName();
		if (result.getStatus() == 1) {
			ExtentTestManager.getTest().log(LogStatus.PASS, "Test Passed"); // new
		} else if (result.getStatus() == 2) {

			
			//String screenshot_path = takeScreenShot(methodName, className);

			ExtentTestManager.getTest().log(LogStatus.FAIL, getStackTrace(result.getThrowable()));
			//ExtentTestManager.getTest().log(LogStatus.INFO,
				//	ExtentTestManager.getTest().addScreenCapture(screenshot_path));
		} else if (result.getStatus() == 3) {

			ExtentTestManager.getTest().log(LogStatus.SKIP, "Test Skipped"); // new
		}

		ExtentTestManager.endTest(); // new

		extent.flush();
	}

	@AfterClass
	public void afterClassMethod(ITestContext context) throws InterruptedException {
		closeBrowser();
		IResultMap a = context.getSkippedTests();
		if (previousResults == null) {
			previousResults = new ArrayList<>();
		}

		for (ITestNGMethod temp : a.getAllMethods()) {
			String newSkippedTest = temp.getDescription() + contentType;
			if (!previousResults.contains(newSkippedTest)) {
				previousResults.add(newSkippedTest);
				ExtentTestManager.startTest(newSkippedTest);
				ExtentTestManager.getTest().log(LogStatus.SKIP, "Test Skipped");
				ExtentTestManager.endTest(); // new
				extent.flush();

			}

		}

	}

	public void closeBrowser() throws InterruptedException {

		if (isProxy.equals("true"))
			proxy.abort();
		if (driver != null) {
			driver.quit();
			//ExtentTestManager.getTest().log(LogStatus.INFO, "Browser Closed"); // new
		}
	}

	public Proxy getBrowserMobProxy() {

		proxy = new BrowserMobProxyServer();
		proxy.start(0);

		// get the Selenium proxy object
		Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
		proxy.setHarCaptureTypes(CaptureType.REQUEST_CONTENT);
		// configure it as a desired capability
		return seleniumProxy;

	}

	// add it before any action before which the performance has to be started
	public void startPerformanceMeter(String pageName) {
		if (isProxy.equals("true"))
			proxy.newHar(pageName);
	}

	public Har getPerformanceResultsInHar() {
		if (isProxy.equals("true"))
			return proxy.getHar();

		return null;

	}

	public void stopPerfomance() {
		if (isProxy.equals("true")) {
			if (harFiles == null)
				harFiles = new ArrayList<Har>();

			Har har = proxy.getHar();
			harFiles.add(har);
		}
	}

	public void writePerformanceToFile(String ContentType, String fileName) {
		if (isProxy.equals("true")) {
			Har har = new Har();
			HarLog harLog = new HarLog();
			boolean flag = true;

			for (Har temp : harFiles) {

				if (flag) {
					flag = false;
					harLog.setBrowser(temp.getLog().getBrowser());
					harLog.setCreator(temp.getLog().getCreator());
				}

				for (HarPage harpage : temp.getLog().getPages()) {
					harLog.addPage(harpage);
				}
				for (HarEntry harentry : temp.getLog().getEntries()) {
					harLog.addEntry(harentry);
				}
			}

			har.setLog(harLog);
			harFiles.clear();
			harFiles = null;
			if (!ContentType.equals(""))
				ContentType += "_";
			try {
				FileOutputStream fos = new FileOutputStream(ContentType + fileName + ".har");
				har.writeTo(fos);
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void refreshOrRestartBrowser() throws InterruptedException {

		// check if it's the same user for the new test , if so go to the main
		// page and just refresh the browser
		if (currentUser.equals(previousUser)) {
			if (driver == null) {
				Cookie currentUserCookie = userCookies.get(currentUser);
				registerAndSetBrowser(currentUserCookie);
			}
			driver.get(url);
			driver.navigate().refresh();
		} else {

			closeBrowser();
			Cookie currentUserCookie = userCookies.get(currentUser);
			registerAndSetBrowser(currentUserCookie);
		}

		previousUser = currentUser;
		Reporter.log("Browser Set");
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);

	}

	public void registerAndSetBrowser(Cookie browserCookie) {

		DesiredCapabilities capabilities = new DesiredCapabilities();

		if (isProxy.equals("true"))
			capabilities.setCapability(CapabilityType.PROXY, getBrowserMobProxy());

		if (browser.equalsIgnoreCase("firefox")) {

			if (OS.indexOf("mac") >= 0) {

				System.setProperty("webdriver.gecko.driver", "lib/drivers/geckodriver");
			} else if (OS.indexOf("win") >= 0) {
				System.setProperty("webdriver.gecko.driver", "lib/drivers/geckodriver.exe");
			}

			driverOne = new FirefoxDriver(capabilities);
			drivers.put("firefox", driverOne);
		}

		else if (browser.equalsIgnoreCase("ie")) {
			System.setProperty("webdriver.ie.driver", "lib/drivers/IEDriverServer.exe");
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, false);
			capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
			capabilities.setCapability("nativeEvents", false);
			capabilities.setCapability("ignoreZoomSetting", true);
			driverOne = new InternetExplorerDriver(capabilities);

			drivers.put("ie", driverOne);
		} else if (browser.equalsIgnoreCase("chrome")) {

			Reporter.log("OS is  :" + OS);
			if (OS.indexOf("mac") >= 0) {

				System.setProperty("webdriver.chrome.driver", "lib/drivers/chromedriver");
			} else if (OS.indexOf("win") >= 0) {
				System.setProperty("webdriver.chrome.driver", "lib/drivers/chromedriver.exe");
			} else if (OS.contains("Linux")) {
				// getting 32 or 64bit
				String bit = System.getProperty("sun.arch.data.model");
				if (bit.equals("64"))
					System.setProperty("webdriver.chrome.driver", "lib/drivers/chromedriver_64");
				else
					System.setProperty("webdriver.chrome.driver", "lib/drivers/chromedriver_32");
			}

			DesiredCapabilities capabilitiesChrome = DesiredCapabilities.chrome();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("start-maximized");

			options.addArguments("test-type");
			options.addArguments("--disable-web-security");
			options.addArguments("--allow-running-insecure-content");
			options.addArguments("--disable-extensions");
			options.addArguments("disable-infobars");
			capabilitiesChrome.setCapability("chrome.binary", "./src//lib//chromedriver");
			capabilitiesChrome.setCapability(ChromeOptions.CAPABILITY, options);

			if (isProxy.equals("true"))
				capabilitiesChrome.setCapability(CapabilityType.PROXY, getBrowserMobProxy());

			driverOne = new ChromeDriver(capabilitiesChrome);
			drivers.put("chrome", driverOne);

		}

		handle = new WebDriverListerners();
		driver = new EventFiringWebDriver(driverOne);
		driver.register(handle);

		/*if (browser.equals("chrome")) {
			// Maximize window for mac chrome
			java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Point position = new Point(0, 0);
			driver.manage().window().setPosition(position);
			Dimension maximizedScreenSize = new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight());
			driver.manage().window().setSize(maximizedScreenSize);
		}
*/
		driverOne.navigate().to(url);
		ExtentTestManager.getTest().log(LogStatus.INFO, "Browser Initiated"); // new

		// managing timeouts
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		driverOne.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		WebDriverCommonLib sh = new WebDriverCommonLib(driver);
		sh.maximizeWindow();
		sh.waitForLoad();

		if (browserCookie != null) {
			try {
				driver.manage().addCookie(browserCookie);
			} catch (InvalidCookieDomainException exception) {
				Reporter.log("cookie expired");
				userCookies.remove(currentUser);

			}
			driver.navigate().refresh();
		}

	}

	public String takeScreenShot(String methodName, String className) {
		String browser = BaseTest.getBrowserName();
		driverOne = getDriver(browser);
		String screenShotFilePath = "";
		String foldername = filePathsrc.concat(className).concat("/");
		File theDir = new File(foldername);

		if (!theDir.exists()) {
			boolean result = false;

			try {
				theDir.mkdir();
				result = true;
			} catch (SecurityException se) {
				// handle it
			}
			if (result) {
				Reporter.log("DIR created");
			}
		}

		TakesScreenshot ts = ((TakesScreenshot) driverOne);
		File scrFile = ts.getScreenshotAs(OutputType.FILE);
		File desFile = new File(foldername + methodName + ".png");

		try {
			FileUtils.copyFile(scrFile, desFile);

			screenShotFilePath = desFile.getAbsolutePath().replace("\\.", "").replace('\\', '/');
		

		} catch (IOException e) {
			e.printStackTrace();
		}

		Reporter.log("***Placed screen shot in " + desFile);
		Reporter.log("***Placed screen shot in " + screenShotFilePath);

		return screenShotFilePath;

	}

	protected String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return sw.toString();
	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	public void clearTheDirectory() throws InterruptedException {

		// For clearing screenshot folder at test start
		if (i == 1) {
			try {
				if (new File(filePathsrc).exists()) {
					FileUtils.cleanDirectory(new File(filePathsrc));
				}
				Thread.sleep(2000);
				FileUtils.forceMkdir(new File(filePathsrc));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static String getBrowserName() {

		return browser;

	}

	public static WebDriver getDriver(String browserName) {
		return drivers.get(browser);

	}

	@DataProvider
	public Object[][] dropDown() throws BiffException, IOException {
		return ExcelUtil.fetchData(ContentCreationDataFilePath, "dropdown");

	}
	
	public static void ReportLog(String ExpectedResult,String ActualResult)
	{
		ExtentTestManager.getTest().log(LogStatus.INFO, "Actual Result is " + ActualResult+ " and Expected should be :"+ ExpectedResult);
	}

}