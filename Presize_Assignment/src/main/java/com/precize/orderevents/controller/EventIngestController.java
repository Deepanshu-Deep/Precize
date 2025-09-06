package com.precize.orderevents.controller;

import com.precize.orderevents.event.Event;
import com.precize.orderevents.processor.EventProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/events")
@Slf4j
public class EventIngestController {

    private final EventProcessor processor;

    public EventIngestController(EventProcessor processor) {
        this.processor = processor;
    }

    /**
     * Upload an events file (one JSON object per line). The controller will parse each line and process it.
     * Returns counts of processed and failed lines.
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadEventsFile(@RequestParam("file") MultipartFile file) {

        int success = 0;
        int failed = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    Event event = processor.parseEvent(line);
                    processor.processEvent(event);
                    success++;
                } catch (Exception ex) {
                    failed++;
                    log.warn("Failed to parse/process line: {} ; reason: {}", line, ex.getMessage());
                }
            }
            String msg = String.format("File processed. success=%d, failed=%d", success, failed);
            return ResponseEntity.ok(msg);
        } catch (Exception e) {
            log.error("Failed to read uploaded file : {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to read uploaded file: " + e.getMessage());
        }
    }
}