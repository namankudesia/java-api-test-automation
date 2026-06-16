package com.naman.framework.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Generates a rich Extent HTML report automatically after every test run.
 */
public class ExtentReportListener implements ITestListener {
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        ExtentSparkReporter reporter = new ExtentSparkReporter("reports/ExtentReport_" + timestamp + ".html");
        reporter.config().setDocumentTitle("API Test Report");
        reporter.config().setReportName("Naman Kudesia — API Automation Suite");
        extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Environment", System.getProperty("env", "QA"));
        extent.setSystemInfo("Author", "Naman Kudesia");
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName(),
            result.getMethod().getDescription());
        testThread.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testThread.get().pass("Test PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testThread.get().fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testThread.get().skip("Test SKIPPED: " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) extent.flush();
    }
}
