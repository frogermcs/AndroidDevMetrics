package com.frogermcs.androiddevmetrics.aspect;

import android.app.Activity;
import android.os.Debug;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;

import com.frogermcs.androiddevmetrics.internal.MethodsTracingManager;
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

    public static boolean isEnabled() {
        return enabled;
    }

    public static final String METHOD_ON_CREATE = "onCreate";
    public static final String METHOD_ON_START = "onStart";
    public static final String METHOD_ON_RESUME = "onResume";

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
            result = executeWithTracingIfEnabled(joinPoint, methodName);
            ActivityLifecycleMetrics.getInstance().logPostOnResume((Activity) joinPoint.getTarget());
        } else if (METHOD_ON_START.equals(methodName)) {
            ActivityLifecycleMetrics.getInstance().logPreOnStart((Activity) joinPoint.getTarget());
            result = executeWithTracingIfEnabled(joinPoint, methodName);
            ActivityLifecycleMetrics.getInstance().logPostOnStart((Activity) joinPoint.getTarget());
        } else if (METHOD_ON_CREATE.equals(methodName)) {
            ActivityLifecycleMetrics.getInstance().logPreOnCreate((Activity) joinPoint.getTarget());
            result = executeWithTracingIfEnabled(joinPoint, methodName);
            ActivityLifecycleMetrics.getInstance().logPostOnCreate((Activity) joinPoint.getTarget());
        } else {
            result = null;
        }

        return result;
    }

    private Object executeWithTracingIfEnabled(ProceedingJoinPoint joinPoint, String methodName) throws Throwable {
        final String targetName = joinPoint.getTarget().getClass().getName();
        if (MethodsTracingManager.getInstance().shouldTraceMethod(targetName, methodName)) {
            MethodsTracingManager.getInstance().disableMethodTracing(targetName, methodName);
            String traceName = "/sdcard/" + joinPoint.getTarget().getClass().getSimpleName() + methodName + ".trace";

            Debug.startMethodTracing(traceName);
            Object result = joinPoint.proceed();
            Debug.stopMethodTracing();

            MethodsTracingManager.getInstance().addTracedMethod(traceName);
            return result;
        } else {
            return joinPoint.proceed();
        }
    }

}
