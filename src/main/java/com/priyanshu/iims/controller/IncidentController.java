package com.priyanshu.iims.controller;

import org.springframework.http.HttpStatus;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.priyanshu.iims.dto.*;
import com.priyanshu.iims.model.Incident;
import com.priyanshu.iims.service.IncidentService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import com.priyanshu.iims.model.enums.*;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Incident> createIncident(
            @Valid @RequestBody IncidentRequest request) {

        Incident incident = incidentService.createIncident(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(incident);
    }
    @GetMapping
    public ResponseEntity<Page<Incident>> getAllIncidents(
    		
    		@RequestParam(required=false)
    		String search,

            @RequestParam(required = false)
            IncidentPriority priority,

            @RequestParam(required = false)
            IncidentStatus status,

            @RequestParam(required = false)
            IncidentCategory category,

            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable) {

        Page<Incident> incidents =
                incidentService.getFilteredIncidents(
                		search,
                        priority,
                        status,
                        category,
                        pageable
                );

        return ResponseEntity.ok(incidents);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncidentById(@PathVariable String id){
    	return incidentService.getIncidentById(id).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','ENGINEER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Incident> updateIncident(@PathVariable String id, @Valid @RequestBody IncidentUpdateRequest request){
    	return incidentService.updateIncident(id, request).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','ENGINEER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable String id){
    	incidentService.deleteIncident(id);
    	return ResponseEntity.noContent().build();
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'ENGINEER')")
    @PutMapping("/assign/{id}")
    public ResponseEntity<Incident> assignIncident(
            @PathVariable String id,
            @Valid @RequestBody IncidentAssignmentRequest request) {

        Incident incident = incidentService.assignIncident(
                id,
                request.getEngineerEmail()
        );

        return ResponseEntity.ok(incident);
    }
    
}