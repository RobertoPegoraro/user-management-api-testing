package com.user.management.api.testing.core.http.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.user.management.api.testing.core.http.model.ObjectMapperHolder;

public class HttpLogger {

    private static final Logger log = LoggerFactory.getLogger(HttpLogger.class);

    private static final String RESET = "\u001B[0m";

    private static final String CYAN = "\u001B[36m";

    private static final String BLUE = "\u001B[34m";

    private static final String GREEN = "\u001B[32m";

    private static final String YELLOW = "\u001B[33m";

    private static final String RED = "\u001B[31m";

    private HttpLogger() {}

    public static void logExchange(String method, String url, String requestBody, int statusCode, String responseBody) {

        StringBuilder sb = new StringBuilder();
        sb.append(CYAN).append("▶ ").append(method).append(" ").append(url).append(RESET);

        if (requestBody != null) {
            sb.append("\n").append(BLUE).append("  Request: ")
                    .append(colorLines(prettyPrint(requestBody), BLUE)).append(RESET);
        }

        String statusColor = statusCode >= 400 ? RED : GREEN;
        sb.append("\n").append(statusColor).append("◀ HTTP ").append(statusCode).append(RESET);
        sb.append("\n").append(YELLOW).append("  Response: ")
                .append(colorLines(prettyPrint(responseBody), YELLOW)).append(RESET);

        log.info("{}", sb);
    }

    private static String prettyPrint(String json) {

        if (json == null || json.isBlank()) {
            return "";
        }
        try {
            Object parsed = ObjectMapperHolder.INSTANCE.readValue(json, Object.class);
            return ObjectMapperHolder.INSTANCE.writerWithDefaultPrettyPrinter().writeValueAsString(parsed);
        } catch (JsonProcessingException e) {
            return json;
        }
    }

    private static String colorLines(String text, String color) {

        return text.replace("\n", "\n" + color);
    }
}
