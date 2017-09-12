
package com.cisco.csc.pageobjects;

import java.util.ArrayList;
import java.util.List;



import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;


import com.cisco.csc.genericLib.WebDriverCommonLib;
import com.cisco.csc.testscripts.BaseTest;

public class HomePage {

	WebDriver driver;
	WebDriverCommonLib sh;

	
	
	public HomePage(WebDriver driver) {

		this.driver = driver;
		// This initElements method will create all WebElements
		PageFactory.initElements(driver, this);
	}

		@FindBy(xpath = ".//a[@class='lia-link-navigation private-notes-link']")
		public WebElement privateMessageLink;

		@FindBy(xpath = ".//a[@class='lia-link-navigation lia-notification-feed-page-link']")
		public WebElement notificationLink;
		


		public boolean verifyprivateMessageLinkPresent()
		{
			sh = new WebDriverCommonLib(driver);
			if(sh.isElementPresent(privateMessageLink))
			{
				return true;
			}
			
			return false;
		}
	
		public boolean verifynotificationLinkPresent()
		{
			if(sh.isElementPresent(notificationLink))
			{
				return true;
			}
			
			return false;
		}

}
