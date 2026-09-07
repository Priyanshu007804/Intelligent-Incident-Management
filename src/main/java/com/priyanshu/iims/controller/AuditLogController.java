package com.priyanshu.iims.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.priyanshu.iims.audit.AuditLog;
import com.priyanshu.iims.service.AuditLogService;

@RestController
@RequestMapping("/api/incidents")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping("/{id}/audit")
    public ResponseEntity<List<AuditLog>> getAuditLogs(
            @PathVariable String id) {

        List<AuditLog> auditLogs =
                auditLogService.getAuditLogs(id);

        return ResponseEntity.ok(auditLogs);
    }
}