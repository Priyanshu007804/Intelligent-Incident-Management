package com.priyanshu.iims.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.priyanshu.iims.dto.DashboardSummaryResponse;
import com.priyanshu.iims.model.enums.IncidentPriority;
import com.priyanshu.iims.model.enums.IncidentStatus;
import com.priyanshu.iims.repository.IncidentRepository;

class DashboardServiceTest {

    @Test
    void getSummaryReturnsIncidentCounts() {
        IncidentRepository incidentRepository = org.mockito.Mockito.mock(IncidentRepository.class);
        MongoTemplate mongoTemplate = org.mockito.Mockito.mock(MongoTemplate.class);
        when(incidentRepository.count()).thenReturn(12L);
        when(incidentRepository.countByStatus(IncidentStatus.OPEN)).thenReturn(5L);
        when(incidentRepository.countByStatus(IncidentStatus.IN_PROGRESS)).thenReturn(3L);
        when(incidentRepository.countByStatus(IncidentStatus.RESOLVED)).thenReturn(2L);
        when(incidentRepository.countByStatus(IncidentStatus.CLOSED)).thenReturn(2L);
        when(incidentRepository.countByPriority(IncidentPriority.CRITICAL)).thenReturn(4L);
        DashboardService dashboardService = new DashboardService(incidentRepository, mongoTemplate);

        DashboardSummaryResponse summary = dashboardService.getSummary();

        assertEquals(12L, summary.getTotalIncidents());
        assertEquals(5L, summary.getOpenIncidents());
        assertEquals(3L, summary.getInProgressIncidents());
        assertEquals(2L, summary.getResolvedIncidents());
        assertEquals(2L, summary.getClosedIncidents());
        assertEquals(4L, summary.getCriticalIncidents());
        verify(incidentRepository).count();
    }
}