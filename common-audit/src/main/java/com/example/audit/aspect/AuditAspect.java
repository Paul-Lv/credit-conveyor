package com.example.audit.aspect;

import com.example.audit.dto.AuditEvent;
import com.example.audit.producer.AuditProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditProducer auditProducer;

    @Value("${spring.application.name:unknown}")
    private String serviceName;

    @Around("@annotation(com.example.audit.annotation.AuditAction)")
    public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        UUID auditId = UUID.randomUUID();

        // START event
        AuditEvent startEvent = new AuditEvent(
                auditId,
                serviceName,
                "START",
                "Started: " + methodName,
                LocalDateTime.now()
        );
        auditProducer.sendAuditEvent(startEvent);

        try {
            Object result = joinPoint.proceed();

            // SUCCESS event
            AuditEvent successEvent = new AuditEvent(
                    auditId,
                    serviceName,
                    "SUCCESS",
                    "Success: " + methodName,
                    LocalDateTime.now()
            );
            auditProducer.sendAuditEvent(successEvent);

            return result;
        } catch (Exception e) {
            // FAILURE event
            AuditEvent failureEvent = new AuditEvent(
                    auditId,
                    serviceName,
                    "FAILURE",
                    "Failed: " + methodName + " - " + e.getMessage(),
                    LocalDateTime.now()
            );
            auditProducer.sendAuditEvent(failureEvent);
            throw e;
        }
    }
}