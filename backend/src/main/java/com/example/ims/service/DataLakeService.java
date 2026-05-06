package com.example.ims.service;

import com.example.ims.model.Signal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataLakeService {

    private static final String FILE_PATH = "signals.json";
    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public synchronized void save(Signal signal) {
        List<Signal> signals = readAll();
        signals.add(signal);
        try {
            mapper.writeValue(new File(FILE_PATH), signals);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Signal> readAll() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();
        try {
            Signal[] arr = mapper.readValue(file, Signal[].class);
            return new ArrayList<>(List.of(arr));
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
