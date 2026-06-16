package com.naman.framework.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Automatically retries flaky tests up to MAX_RETRY times.
 */
public class RetryListener implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int MAX_RETRY = 2;

    @Override
    public boolean retry(ITestResult result) {
        if (!result.isSuccess() && retryCount < MAX_RETRY) {
            retryCount++;
            result.setStatus(ITestResult.FAILURE);
            System.out.println("[RETRY] Attempt " + retryCount + " for: " + result.getName());
            return true;
        }
        return false;
    }
}
