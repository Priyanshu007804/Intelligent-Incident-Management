package com.priyanshu.iims.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.priyanshu.iims.audit.AuditLog;
import com.priyanshu.iims.repository.AuditLogRepository;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logAction(String incidentId,
                           String action,
                           String performedBy,
                           String details) {

        AuditLog auditLog = new AuditLog(
                incidentId,
                action,
                performedBy,
                details,
                LocalDateTime.now()
        );

        auditLogRepository.save(auditLog);
    }

    public List<AuditLog> getAuditLogs(String incidentId) {
        return auditLogRepository
                .findByIncidentIdOrderByTimestampDesc(incidentId);
    }
}