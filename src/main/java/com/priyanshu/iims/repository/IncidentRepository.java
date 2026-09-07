package com.priyanshu.iims.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.priyanshu.iims.model.Incident;
import com.priyanshu.iims.model.enums.IncidentPriority;
import com.priyanshu.iims.model.enums.IncidentStatus;

public interface IncidentRepository extends MongoRepository<Incident,String>{
	
	long countByStatus(IncidentStatus Status);
	long countByPriority(IncidentPriority priority);
}
