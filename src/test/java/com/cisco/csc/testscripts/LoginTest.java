package com.cisco.csc.testscripts;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.Parameters;
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

public class LoginTest extends BaseTest {

	HomePage homepage;
	LoginPage loginpage;
	String disName;

	
	WebDriverCommonLib sh;
	ExtentTest test;
	
	@Factory(dataProvider = "dataProviderForLogin")
	public LoginTest(String s_no, String userName, String pass, String userNameTwo, String passTwo) {

		this.S_no = s_no;
		this.userNameOne = userName;
		this.passOne = pass;
		this.userNameTwo = userNameTwo;
		this.passTwo = passTwo;
	
		
	}

	@DataProvider(name = "dataProviderForLogin", parallel = false)
	public static Object[][] dataProviderMethod(ITestContext context) throws IOException, BiffException {
		Object[][] returnObject = null;		
		setExcelPath(context);
		browser = context.getCurrentXmlTest().getLocalParameters().get("browser");
		returnObject = ExcelUtil.fetchData(ContentCreationDataFilePath, "LoginTest");
		return returnObject;
	}
	
	

	@Priority(1)
	@Test
	public void verifyUserIsAbleToLogin() throws InterruptedException {
		refreshOrRestartBrowser();
		loginpage = new LoginPage(driver);
		homepage = loginpage.loginWithCookies(environment, userNameOne, passOne,userCookies);
		System.out.println("returned homepage : "+homepage);


	}
	
}

