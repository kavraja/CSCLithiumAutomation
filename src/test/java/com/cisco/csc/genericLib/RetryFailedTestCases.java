package com.cisco.csc.genericLib;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryFailedTestCases implements IRetryAnalyzer {
	private  int retryCount = 0;
	private int maxRetryCount = 0;
	private boolean setRetryCount = false;

	public RetryFailedTestCases()
	{
		System.out.println("Calling retry");
		
	}
	
	
	
	// Below method returns 'true' if the test method has to be retried else
	// 'false'
	// and it takes the 'Result' as parameter of the test method that just ran
	public boolean retry(ITestResult result) {
		if(!setRetryCount)
		{
			setRetryCount = true;
			String retryValue = result.getTestContext().getCurrentXmlTest().getParameter("retry");
			maxRetryCount = Integer.parseInt(retryValue);
		}
		
		
		
		if (retryCount < maxRetryCount) {
			System.out.println("Retrying test " + result.getName() + " with status " + getResultStatusName(result.getStatus()) + " for the " + (retryCount + 1) + " time(s).");
			retryCount++;
			return true;
		}
		return false;
	}

	public String getResultStatusName(int status) {
		String resultName = null;
		if (status == 1)
			resultName = "SUCCESS";
		if (status == 2)
			resultName = "FAILURE";
		if (status == 3)
			resultName = "SKIP";
		return resultName;
	}

}
