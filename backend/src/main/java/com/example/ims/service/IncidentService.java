package com.example.ims.service;

import com.example.ims.model.Incident;
import com.example.ims.model.Signal;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IncidentService {

    private Map<String, Incident> activeIncidents = new HashMap<>();

    public Incident processSignal(Signal signal) {

        String component = signal.getComponent();

        try {
            // Simulate heavy work like database saving or complex calculations
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Check if incident already exists
        if (activeIncidents.containsKey(component)) {
            return activeIncidents.get(component);
        }

        // Else create new incident
        Incident incident = new Incident(component);
        activeIncidents.put(component, incident);

        return incident;
    }

    public Collection<Incident> getAllIncidents() {
        return activeIncidents.values();
    }

    public Incident getIncidentById(int id) {
        return activeIncidents.values().stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Incident not found"));
    }

    public Incident updateStatus(int id, String status) {
        Incident incident = getIncidentById(id);
        String currentStatus = incident.getStatus();

        // Enforce flow: OPEN -> INVESTIGATING -> RESOLVED -> CLOSED
        boolean valid = false;
        if (currentStatus.equals("OPEN") && status.equals("INVESTIGATING"))
            valid = true;
        if (currentStatus.equals("INVESTIGATING") && status.equals("RESOLVED"))
            valid = true;
        if (currentStatus.equals("RESOLVED") && status.equals("CLOSED"))
            valid = true;

        if (!valid)
            throw new RuntimeException("Invalid status transition from " + currentStatus + " to " + status);
        if (status.equals("CLOSED") && (incident.getRca() == null || incident.getRca().trim().isEmpty())) {
            throw new RuntimeException("Cannot close without RCA");
        }

        if (status.equals("CLOSED")) {
            incident.close();
        }
        incident.setStatus(status);
        return incident;
    }

    public Incident addRca(int id, String rca) {
        Incident incident = getIncidentById(id);
        incident.setRca(rca);
        return incident;
    }
}