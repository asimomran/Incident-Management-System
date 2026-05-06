package com.example.ims.controller;

import com.example.ims.model.Incident;
import com.example.ims.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/incidents")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    @GetMapping
    public Collection<Incident> getAllIncidents() {
        return incidentService.getAllIncidents();
    }

    @PutMapping("/{id}/status")
    public Incident updateStatus(@PathVariable int id, @RequestParam String status) {
        return incidentService.updateStatus(id, status);
    }

    @PostMapping("/{id}/rca")
    public Incident addRca(@PathVariable int id, @RequestBody String rca) {
        return incidentService.addRca(id, rca);
    }
}
