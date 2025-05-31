package com.example.demo.config;

import com.example.demo.api.common.infra.CustomSpringELParser;
import com.example.demo.api.common.infra.DistributedLock;
import com.example.demo.api.common.infra.MySqlNamedLockManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAspect {
    private static final String NAMED_LOCK_PREFIX = "LOCK:";
    private final MySqlNamedLockManager lockManager;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.example.demo.api.common.infra.DistributedLock)")
    public Object around(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock lockAnnotation = method.getAnnotation(DistributedLock.class);

        String key = NAMED_LOCK_PREFIX + method.getName() +
                ":" + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), lockAnnotation.key());

        return lockManager.executeWithLock(key, lockAnnotation.waitTime(), () -> {
            try {
                return aopForTransaction.proceed(joinPoint);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }
}

