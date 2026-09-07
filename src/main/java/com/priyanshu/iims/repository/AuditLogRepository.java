package com.priyanshu.iims.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.priyanshu.iims.audit.AuditLog;

public interface AuditLogRepository extends MongoRepository<AuditLog, String> {

    List<AuditLog> findByIncidentIdOrderByTimestampDesc(String incidentId);
}