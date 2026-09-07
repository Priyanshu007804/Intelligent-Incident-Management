package com.priyanshu.iims.service;

import org.springframework.stereotype.Service;

import com.priyanshu.iims.dto.DashboardSummaryResponse;
import com.priyanshu.iims.model.Incident;
import com.priyanshu.iims.model.enums.IncidentPriority;
import com.priyanshu.iims.model.enums.IncidentStatus;
import com.priyanshu.iims.repository.IncidentRepository;
import java.util.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

@Service
public class DashboardService {

    private final IncidentRepository incidentRepository;
    private final MongoTemplate mongoTemplate;

    public DashboardService(IncidentRepository incidentRepository, MongoTemplate mongoTemplate) {
        this.incidentRepository = incidentRepository;
        this.mongoTemplate=mongoTemplate;
    }

    public DashboardSummaryResponse getSummary() {

        long totalIncidents =
                incidentRepository.count();

        long openIncidents =
                incidentRepository.countByStatus(IncidentStatus.OPEN);

        long inProgressIncidents =
                incidentRepository.countByStatus(IncidentStatus.IN_PROGRESS);

        long resolvedIncidents =
                incidentRepository.countByStatus(IncidentStatus.RESOLVED);

        long closedIncidents =
                incidentRepository.countByStatus(IncidentStatus.CLOSED);

        long criticalIncidents =
                incidentRepository.countByPriority(IncidentPriority.CRITICAL);

        return new DashboardSummaryResponse(
                totalIncidents,
                openIncidents,
                inProgressIncidents,
                resolvedIncidents,
                closedIncidents,
                criticalIncidents
        );
    }
    
    
    public Map<String, Long> getIncidentsByCategory(){
    	Aggregation aggregation=Aggregation.newAggregation(Aggregation.group("category").count().as("count"));
    	
    	AggregationResults<Map> results=mongoTemplate.aggregate(aggregation, Incident.class, Map.class);
    	
    	Map<String,Long> categoryCounts= new LinkedHashMap<String, Long>();
    	
    	for(Map result: results.getMappedResults()) {
    		String category= String.valueOf(result.get("_id"));
    		
    		Number count=(Number) result.get("count");
    		categoryCounts.put(category, count.longValue());
    	}
    	
    	return categoryCounts;
    }
    
    public Map<String, Long> getIncidentsByPriority(){
    	Aggregation aggregation=Aggregation.newAggregation(Aggregation.group("priority").count().as("count"));
    	
    	AggregationResults<Map> results=mongoTemplate.aggregate(aggregation, Incident.class, Map.class);
    	
    	Map<String,Long> priorityCounts= new LinkedHashMap<String, Long>();
    	
    	for(Map result: results.getMappedResults()) {
    		String category= String.valueOf(result.get("_id"));
    		
    		Number count=(Number) result.get("count");
    		priorityCounts.put(category, count.longValue());
    	}
    	
    	return priorityCounts;
    }
}