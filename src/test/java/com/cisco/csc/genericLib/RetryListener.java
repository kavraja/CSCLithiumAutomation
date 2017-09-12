package com.cisco.csc.genericLib;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

import com.sun.net.httpserver.Authenticator.Retry;

public class RetryListener implements IAnnotationTransformer {

	@Override
	public void transform(ITestAnnotation testannotation, Class testClass, Constructor testConstructor, Method testMethod) {
		IRetryAnalyzer retry = testannotation.getRetryAnalyzer();

		if (retry == null) {
			
			testannotation.setRetryAnalyzer(RetryFailedTestCases.class);
			
		}

	}
}