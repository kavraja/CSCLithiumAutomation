package com.cisco.csc.testscripts;

import java.io.IOException;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.cisco.csc.genericLib.ExcelUtil;
import com.cisco.csc.genericLib.ExtentTestManager;
import com.cisco.csc.genericLib.Priority;
import com.cisco.csc.genericLib.WebDriverCommonLib;
import com.cisco.csc.pageobjects.HomePage;
import com.cisco.csc.pageobjects.LoginPage;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import jxl.read.biff.BiffException;


public class HomeTest extends BaseTest {

	HomePage homepage;
	LoginPage loginpage;
	String disName;
	WebDriverCommonLib sh;
	ExtentTest test;
	
	@Factory(dataProvider = "dataProviderForHome")
	public HomeTest(String s_no, String userName, String pass, String userNameTwo, String passTwo) {

		this.S_no = s_no;
		this.userNameOne = userName;
		this.passOne = pass;
		this.userNameTwo = userNameTwo;
		this.passTwo = passTwo;
	
		
	}

	@DataProvider(name = "dataProviderForHome", parallel = false)
	public static Object[][] dataProviderMethod(ITestContext context) throws IOException, BiffException {
		Object[][] returnObject = null;		
		setExcelPath(context);
		browser = context.getCurrentXmlTest().getLocalParameters().get("browser");
		returnObject = ExcelUtil.fetchData(ContentCreationDataFilePath, "HomeTest");
		return returnObject;
	}
	
	

	@Priority(1)
	@Test
	public void HomePageLinkValidation() throws InterruptedException {		
		refreshOrRestartBrowser();
		loginpage = new LoginPage(driver);
		homepage= loginpage.loginWithCookies(environment, userNameOne, passOne,userCookies);
		Assert.assertTrue(homepage.verifyprivateMessageLinkPresent(), " Private Message Link is not present");
		ExtentTestManager.ReportLogInfo("Private Message presence : " + homepage.verifyprivateMessageLinkPresent(), "TRUE");
		Assert.assertTrue(homepage.verifynotificationLinkPresent(), " Notification Link is not present");
		ExtentTestManager.ReportLogInfo("Notificaiton link presence : " + homepage.verifynotificationLinkPresent(), "TRUE");		

	}
}
