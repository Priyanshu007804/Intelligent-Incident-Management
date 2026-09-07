package com.priyanshu.iims.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.priyanshu.iims.dto.DashboardSummaryResponse;
import com.priyanshu.iims.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponse> getSummary() {

        DashboardSummaryResponse summary =
                dashboardService.getSummary();

        return ResponseEntity.ok(summary);
    }
    @GetMapping("/by-category")
    public ResponseEntity<Map<String,Long>> getIncidentsByCategory(){
    	Map<String,Long> categoryCounts= dashboardService.getIncidentsByCategory();
    	
    	return ResponseEntity.ok(categoryCounts);    
    }
    
    @GetMapping("/by-priority")
    public ResponseEntity<Map<String,Long>> getIncidentsByPriority(){
    	Map<String,Long> priorityCounts= dashboardService.getIncidentsByPriority();
    	
    	return ResponseEntity.ok(priorityCounts);    
    }
}