package com.frogermcs.androiddevmetrics.aspect;

import com.frogermcs.androiddevmetrics.internal.metrics.InitManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.concurrent.TimeUnit;

@Aspect
public class Dagger2GraphAnalyzer {
    private static volatile boolean enabled = true;

    public static void setEnabled(boolean enabled) {
        Dagger2GraphAnalyzer.enabled = enabled;
    }

    @Pointcut("within(@dagger.Module *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(@javax.inject.Inject *.new(..))")
    public void injectConstructor() {
    }

    @Pointcut("execution(@dagger.Provides * *(..)) && withinAnnotatedClass()")
    public void providesMethod() {
    }

    @Around("providesMethod() || injectConstructor()")
    public Object logAndExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        Object result = joinPoint.proceed();
        long stop = System.nanoTime();
        long took = TimeUnit.NANOSECONDS.toMillis(stop - start);

        if (!enabled) {
            return result;
        }

        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        Class<?> cls = codeSignature.getDeclaringType();
        if (codeSignature instanceof ConstructorSignature) {
            InitManager.getInstance().addInitMetric(cls, joinPoint.getArgs(), took);
        }

        if (isMethodWithReturnType(codeSignature)) {
            InitManager.getInstance().addInitMetric(result.getClass(), joinPoint.getArgs(), took);
        }

        return result;
    }

    private boolean isMethodWithReturnType(CodeSignature codeSignature) {
        return codeSignature instanceof MethodSignature && ((MethodSignature) codeSignature).getReturnType() != void.class;
    }


}
