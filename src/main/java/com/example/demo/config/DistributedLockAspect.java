package com.example.demo.config;

import com.example.demo.api.common.infra.DistributedLock;
import com.example.demo.api.common.infra.MySqlNamedLockManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAspect {
    private final MySqlNamedLockManager lockManager;

    @Around("@annotation(com.example.demo.api.common.infra.DistributedLock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        DistributedLock lockAnnotation = signature.getMethod().getAnnotation(DistributedLock.class);

        String lockKey = lockAnnotation.key();
        long timeout = lockAnnotation.waitTime();

        boolean lockAcquired = lockManager.tryLock(lockKey, timeout);
        if (!lockAcquired) {
            throw new IllegalStateException("Could not acquire named lock: " + lockKey);
        }

        try {
            return joinPoint.proceed();
        } finally {
            lockManager.releaseLock(lockKey);
        }
    }
}

