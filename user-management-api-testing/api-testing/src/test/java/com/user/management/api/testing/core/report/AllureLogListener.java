package com.user.management.api.testing.core.report;

import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import io.qameta.allure.Allure;

public class AllureLogListener implements ITestListener {

    private static final java.util.regex.Pattern ANSI_PATTERN =
            java.util.regex.Pattern.compile("\u001B\\[[;\\d]*m");

    private ListAppender<ILoggingEvent> listAppender;

    @Override
    public void onTestStart(ITestResult result) {

        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

        listAppender = new ListAppender<>();
        listAppender.start();

        rootLogger.addAppender(listAppender);
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        attachLogs();
    }

    @Override
    public void onTestFailure(ITestResult result) {

        attachLogs();
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        attachLogs();
    }

    private void attachLogs() {

        if (listAppender == null) {
            return;
        }

        String logs = listAppender.list.stream()
                .map(ILoggingEvent::getFormattedMessage)
                .map(msg -> ANSI_PATTERN.matcher(msg).replaceAll(""))
                .collect(Collectors.joining("\n"));

        Allure.addAttachment("Logs", "text/plain", logs);

        listAppender.stop();
    }
}

