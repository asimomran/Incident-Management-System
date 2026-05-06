package com.example.ims.controller;

import com.example.ims.model.Signal;
import com.example.ims.service.DataLakeService;
import com.example.ims.service.SignalQueueService;
import com.example.ims.util.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/signals")
public class SignalController {

    @Autowired
    private SignalQueueService signalQueueService;

    @Autowired
    private DataLakeService dataLakeService;

    @Autowired
    private RateLimiter rateLimiter;

    @PostMapping
    public ResponseEntity<?> receiveSignal(@RequestBody Signal signal) {
        if (!rateLimiter.tryAcquire()) {
            return ResponseEntity.status(429).body("Too Many Requests");
        }
        signalQueueService.enqueue(signal);
        return ResponseEntity.accepted().body("Accepted");
    }

    @GetMapping("/raw")
    public ResponseEntity<List<Signal>> getRawSignals() {
        return ResponseEntity.ok(dataLakeService.readAll());
    }
}