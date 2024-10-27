package com.spring.EZTasks.utils.func;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogHelper {
    private static final String SEPARATOR = "=====================================================";

    public void logSection(String sectionTitle) {
        log.info("\n{}\n{}: {}\n{}", SEPARATOR, "Section", sectionTitle, SEPARATOR);
    }

    public void logMessage(String context, String message) {
        log.info("Context: [{}] - {}", context, message);
    }

    public void logError(String context, String errorMessage) {
        log.error("Context: [{}] - ERROR: {}", context, errorMessage);
    }

    public void logDebug(String context, String debugMessage) {
        log.debug("Context: [{}] - DEBUG: {}", context, debugMessage);
    }

    public void logSeparator() {
        log.info(SEPARATOR);
    }
}
