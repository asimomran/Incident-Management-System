package com.example.ims.worker;

import com.example.ims.model.Signal;
import com.example.ims.service.DataLakeService;
import com.example.ims.service.IncidentService;
import com.example.ims.service.SignalQueueService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SignalWorker {

    @Autowired
    private SignalQueueService queueService;

    @Autowired
    private IncidentService incidentService;

    @Autowired
    private DataLakeService dataLakeService;

    @PostConstruct
    public void startWorker() {
        new Thread(() -> {
            while (true) {
                try {
                    Signal signal = queueService.take();
                    dataLakeService.save(signal);         // Step 1: save to data lake
                    incidentService.processSignal(signal); // Step 2: process as before
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "Signal-Worker-Thread").start();
    }
}
