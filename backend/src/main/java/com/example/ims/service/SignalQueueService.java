package com.example.ims.service;

import com.example.ims.model.Signal;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class SignalQueueService {
    private final BlockingQueue<Signal> queue = new LinkedBlockingQueue<>();

    public void enqueue(Signal signal) {
        queue.offer(signal);
    }

    public Signal take() throws InterruptedException {
        return queue.take();
    }
}
