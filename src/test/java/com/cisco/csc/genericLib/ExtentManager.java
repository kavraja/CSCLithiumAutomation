package com.cisco.csc.genericLib;

import java.io.File;

import org.testng.ITestContext;
import org.testng.Reporter;

import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager {
    private static ExtentReports extent;
    private static ITestContext context;

    public synchronized static ExtentReports getInstance() {
        if (extent == null) {
            File outputDirectory = new File(context.getOutputDirectory());
            File resultDirectory = new File(outputDirectory.getParentFile(), "html");
            extent = new ExtentReports(resultDirectory + File.separator +  "Report.html", true);
            Reporter.log("Extent Report directory: " + resultDirectory, true);
        }

        return extent;
    }

    public static void setOutputDirectory(ITestContext context) {
        ExtentManager.context = context;
    }
    }

