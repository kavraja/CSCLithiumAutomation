package com.cisco.csc.pageobjects;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import com.cisco.csc.genericLib.ExtentTestManager;
import com.cisco.csc.genericLib.WebDriverCommonLib;
import com.relevantcodes.extentreports.LogStatus;

public class LoginPage  {

	WebDriver driver;
	WebDriverCommonLib sh;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		// This initElements method will create all WebElements
		PageFactory.initElements(driver, this);
	}
	
	
	@FindBy(id = "userInput")
	public WebElement cscUserName;

	@FindBy(id = "passwordInput")
	public WebElement cscPassword;

	@FindBy(id = "login-button")
	public WebElement cscSignINButton;
	
	@FindBy(xpath = "(//a[text()='Login'])[1]")
	public WebElement loginUAT;

	@FindBy(id = "login-button")
	public WebElement cscNextButton;

	public void waitForLogin() {
		sh = new WebDriverCommonLib(driver);
		sh.waitForLoad();
	}

	public void enterUser(String userName) {

		cscUserName.sendKeys(userName);

	}

	public void enterPassword(String password) {
		cscPassword.sendKeys(password);
	}

	public HomePage submitLogin() {
		cscSignINButton.click();
		sh.waitForLoad();

		return new HomePage(driver);

	}

	public HomePage loginWithCookies(String env, String userName, String password,
			Map<String, Cookie> userCookies) throws InterruptedException {
		HomePage temp = loginWithCreds(env, userName, password);		
		Reporter.log("Environment Login check");
		if (userCookies.get(userName) == null) {
			Cookie cookie = driver.manage().getCookieNamed("ObSSOCookie");
			userCookies.put(userName, cookie);
		}

		return temp;
	}



	public HomePage loginWithCreds(String environment, String userName, String password)
			throws InterruptedException {

		if (!loginButtonPresent()) {
			Reporter.log("checked login button presence");
			return new HomePage(driver);
		}

		else if (environment.equals("UAT") || environment.equals("prod")) {

			sh = new WebDriverCommonLib(driver);
			loginButtonHomePage().click();
			sh.waitForLoad();
			if (driver.getCurrentUrl().contains("sso")) {
				Reporter.log("*******");
				enterUser(userName);
				nextLoginClick();
				enterPassword(password);
				sh.waitForLoad();
				return submitLogin();
				
			}
			sh.waitForLoad();
			return new HomePage(driver);

		}

		return null;
	}

	public boolean loginButtonPresent() {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		sh = new WebDriverCommonLib(driver);
		try {
			return sh.isElementPresent(loginUAT);
		} catch (Exception ex) {
			return false;
		} finally {
			Reporter.log("in finally login");
			driver.manage().timeouts().implicitlyWait(2, TimeUnit.MINUTES);
		}

	}

	public HomePage nextLoginClick() {
		cscNextButton.click();
		return new HomePage(driver);

	}
	
	
	public WebElement loginButtonHomePage() throws InterruptedException {

		sh = new WebDriverCommonLib(driver);
		// sh.waitForLoad();
		sh.waitForEleVisible("visible", loginUAT);
		return loginUAT;

	}



}
