package com.frogermcs.androiddevmetrics.aspect;

import android.app.Activity;

import com.frogermcs.androiddevmetrics.internal.metrics.ActivityLifecycleMetrics;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by Miroslaw Stanek on 28.02.2016.
 */
@Aspect
public class ActivityLifecycleAnalyzer {
    private static volatile boolean enabled = true;

    public static void setEnabled(boolean enabled) {
        ActivityLifecycleAnalyzer.enabled = enabled;
    }

    private static final String METHOD_ON_CREATE = "onCreate";
    private static final String METHOD_ON_START = "onStart";
    private static final String METHOD_ON_RESUME = "onResume";

    @Pointcut("execution(void *.onCreate(..)) && this(android.app.Activity+)")
    public void onCreateMethod() {
    }

    @Pointcut("execution(void *.onStart(..)) && this(android.app.Activity+)")
    public void onStartMethod() {
    }

    @Pointcut("execution(void *.onResume(..)) && this(android.app.Activity+)")
    public void onResumeMethod() {
    }

    @Around("onCreateMethod() || onStartMethod() || onResumeMethod()")
    public Object logAndExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!enabled) return joinPoint.proceed();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        final Object result;
        if (METHOD_ON_RESUME.equals(methodName)) {
            ActivityLifecycleMetrics.getInstance().logPreOnResume((Activity) joinPoint.getTarget());
            result = joinPoint.proceed();
            ActivityLifecycleMetrics.getInstance().logPostOnResume((Activity) joinPoint.getTarget());
        } else if (METHOD_ON_START.equals(methodName)) {
            ActivityLifecycleMetrics.getInstance().logPreOnStart((Activity) joinPoint.getTarget());
            result = joinPoint.proceed();
            ActivityLifecycleMetrics.getInstance().logPostOnStart((Activity) joinPoint.getTarget());
        } else if (METHOD_ON_CREATE.equals(methodName)) {
            ActivityLifecycleMetrics.getInstance().logPreOnCreate((Activity) joinPoint.getTarget());
            result = joinPoint.proceed();
            ActivityLifecycleMetrics.getInstance().logPostOnCreate((Activity) joinPoint.getTarget());
        } else {
            result = null;
        }

        return result;
    }

}
