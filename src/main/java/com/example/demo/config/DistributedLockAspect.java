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
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAspect {
    private static final String NAMED_LOCK_PREFIX = "LOCK:";
    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.example.demo.api.common.infra.DistributedLock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock lockAnnotation = method.getAnnotation(DistributedLock.class);

        String key = NAMED_LOCK_PREFIX + method.getName() +
                ":" + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), lockAnnotation.key());

        RLock lock = redissonClient.getLock(key);
        boolean lockAcquired = lock.tryLock(lockAnnotation.waitTime(), TimeUnit.SECONDS);
        if (!lockAcquired) {
            log.error("Could not acquire named lock: {}", key);
            throw new IllegalStateException("Could not acquire named lock: " + key);
        }

        try {
            return aopForTransaction.proceed(joinPoint);
        } finally {
            lock.unlock();
        }
    }
}

