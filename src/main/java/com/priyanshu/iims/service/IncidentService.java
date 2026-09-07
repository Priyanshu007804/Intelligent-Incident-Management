package com.priyanshu.iims.service;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.priyanshu.iims.model.Incident;
import com.priyanshu.iims.model.User;
import com.priyanshu.iims.repository.IncidentRepository;
import com.priyanshu.iims.repository.UserRepository;
import com.priyanshu.iims.dto.IncidentRequest;
import com.priyanshu.iims.dto.IncidentUpdateRequest;
import com.priyanshu.iims.model.enums.*;
import com.priyanshu.iims.exception.ResourceNotFoundException;
import com.priyanshu.iims.exception.InvalidAssignmentException;
import com.priyanshu.iims.exception.InvalidStatusTransitionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Service
public class IncidentService {
	
	private final IncidentRepository incidentRepository;
	private final MongoTemplate mongoTemplate;
	private final UserRepository userRepository;
	private final AuditLogService auditLogService;
	public IncidentService(IncidentRepository incidentRepository, MongoTemplate mongoTemplate, UserRepository userRepository, AuditLogService auditLogService) {
		this.incidentRepository=incidentRepository;
		this.mongoTemplate=mongoTemplate;
		this.userRepository=userRepository;
		this.auditLogService=auditLogService;
	}
	
	public Incident createIncident(IncidentRequest request) {

	    Incident incident = new Incident();
	    Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
	    
	    incident.setReportedBy(authentication.getName());

	    incident.setTitle(request.getTitle());
	    incident.setDescription(request.getDescription());
	    incident.setPriority(request.getPriority());
	    incident.setCategory(request.getCategory());

	    incident.setStatus(IncidentStatus.OPEN);

	    LocalDateTime now = LocalDateTime.now();
	    incident.setCreatedAt(now);
	    incident.setUpdatedAt(now);

	    Incident savedIncident= incidentRepository.save(incident);
	    auditLogService.logAction(savedIncident.getId(),"CREATE", authentication.getName(),"Incident created");
	    return savedIncident;
	}
	
	public Page<Incident> getAllIncidents(Pageable pageable){
		return incidentRepository.findAll(pageable);
	}
	
	
	public Optional<Incident> getIncidentById(String id){
		return incidentRepository.findById(id);
	}
	
	
	public Optional<Incident> updateIncident(
	        String id,
	        IncidentUpdateRequest request) {

	    Optional<Incident> optionalIncident = incidentRepository.findById(id);

	    if (optionalIncident.isEmpty()) {
	        return Optional.empty();
	    }

	    Incident incident = optionalIncident.get();

	    if (request.getTitle() != null) {
	        incident.setTitle(request.getTitle());
	    }

	    if (request.getDescription() != null) {
	        incident.setDescription(request.getDescription());
	    }

	    if (request.getPriority() != null) {
	        incident.setPriority(request.getPriority());
	    }

	    if (request.getStatus() != null) {
	        IncidentStatus currentStatus = incident.getStatus();
	        IncidentStatus newStatus = request.getStatus();

	        if (!isValidStatusTransition(currentStatus, newStatus)) {
	            throw new InvalidStatusTransitionException(
	                "Invalid status transition: " + currentStatus + " → " + newStatus
	            );
	        }

	        incident.setStatus(newStatus);

	        Authentication authentication =
	                SecurityContextHolder.getContext().getAuthentication();

	        auditLogService.logAction(
	                incident.getId(),
	                "STATUS_CHANGE",
	                authentication.getName(),
	                "Status changed from " + currentStatus + " to " + newStatus
	        );
	    }
	    
	    if (request.getCategory() != null) {
	        incident.setCategory(request.getCategory());
	    }

	    incident.setUpdatedAt(LocalDateTime.now());

	    Incident updatedIncident = incidentRepository.save(incident);
	    Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
	    
	    auditLogService.logAction(updatedIncident.getId(),"UPDATE", authentication.getName(), "Incident updated");

	    return Optional.of(updatedIncident);
	}
	
	public void deleteIncident(String id) {

	    Incident incident = incidentRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Incident not found with id: " + id));

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    auditLogService.logAction(
	            incident.getId(),
	            "DELETE",
	            authentication.getName(),
	            "Incident deleted"
	    );

	    incidentRepository.deleteById(id);
	}
	
	public Page<Incident> getFilteredIncidents(
			String search,
	        IncidentPriority priority,
	        IncidentStatus status,
	        IncidentCategory category,
	        Pageable pageable) {

	    Query query = new Query();
	    
	    if(search!=null && !search.isBlank()) {
	    	Criteria searchCriteria= new Criteria().orOperator(Criteria.where("title").regex(search, "i"));
	    	query.addCriteria(searchCriteria);
	    }

	    if (priority != null) {
	        query.addCriteria(Criteria.where("priority").is(priority));
	    }

	    if (status != null) {
	        query.addCriteria(Criteria.where("status").is(status));
	    }

	    if (category != null) {
	        query.addCriteria(Criteria.where("category").is(category));
	    }

	    long total = mongoTemplate.count(query, Incident.class);

	    query.with(pageable);

	    List<Incident> incidents =
	            mongoTemplate.find(query, Incident.class);

	    return new PageImpl<>(incidents, pageable, total);
	}
	
	private boolean isValidStatusTransition(
	        IncidentStatus currentStatus,
	        IncidentStatus newStatus) {

	    return (currentStatus == IncidentStatus.OPEN
	                && newStatus == IncidentStatus.IN_PROGRESS)
	            || (currentStatus == IncidentStatus.IN_PROGRESS
	                && newStatus == IncidentStatus.RESOLVED)
	            || (currentStatus == IncidentStatus.RESOLVED
	                && newStatus == IncidentStatus.CLOSED);
	}
	
	public Incident assignIncident(String incidentId, String engineerEmail) {

	    Incident incident = incidentRepository.findById(incidentId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Incident not found with id: " + incidentId));

	    User engineer = userRepository.findByEmail(engineerEmail);

	    if (engineer == null) {
	        throw new ResourceNotFoundException(
	                "Engineer not found with email: " + engineerEmail);
	    }

	    if (engineer.getRole() != Role.ENGINEER) {
	        throw new InvalidAssignmentException(
	                "User is not an ENGINEER: " + engineerEmail);
	    }

	    incident.setAssignedTo(engineer.getEmail());
	    incident.setUpdatedAt(LocalDateTime.now());

	    Incident updatedIncident = incidentRepository.save(incident);

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    auditLogService.logAction(
	            updatedIncident.getId(),
	            "ASSIGN",
	            authentication.getName(),
	            "Incident assigned to " + engineer.getEmail()
	    );

	    return updatedIncident;
	}
	}
