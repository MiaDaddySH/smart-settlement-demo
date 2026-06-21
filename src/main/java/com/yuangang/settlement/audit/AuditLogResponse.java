package com.yuangang.settlement.audit;

import java.time.Instant;

public record AuditLogResponse(
        Long id,
        String action,
        String entityType,
        Long entityId,
        String details,
        Instant createdAt
) {

    static AuditLogResponse from(AuditLog auditLog) {
        return new AuditLogResponse(auditLog.getId(), auditLog.getAction(), auditLog.getEntityType(),
                auditLog.getEntityId(), auditLog.getDetails(), auditLog.getCreatedAt());
    }
}
