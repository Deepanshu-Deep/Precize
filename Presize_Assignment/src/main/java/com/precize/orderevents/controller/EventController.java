package com.precize.orderevents.controller;

import com.precize.orderevents.processor.EventProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventProcessor eventProcessor;

    @GetMapping("/process")
    public String processEvents() throws IOException {
        try {
            eventProcessor.processEventsFromFile("events.txt");
            return "Events processed successfully!";
        } catch (Exception e) {
            return "Error while processing events: " + e.getMessage();
        }
    }
}