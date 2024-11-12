package com.emadsolutions.LMS.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* com.emadsolutions.LMS.auth.AuthService.*(..)) ||" +
            " execution(* com.emadsolutions.LMS.book.BookService.*(..)) ||"+
            " execution(* com.emadsolutions.LMS.patron.PatronService.*(..)) ||"+
            " execution(* com.emadsolutions.LMS.borrowingRecord.BorrowingRecordService.*(..))"
    )
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        logger.info(ANSI_BLUE + "Method {} called with arguments: {}" + ANSI_RESET, methodName, args);
    }

    // Log after method returns in green
    @AfterReturning(pointcut = "execution(* com.emadsolutions.LMS.auth.AuthService.*(..)) ||" +
            " execution(* com.emadsolutions.LMS.book.BookService.*(..)) ||" +
            " execution(* com.emadsolutions.LMS.patron.PatronService.*(..)) ||" +
            " execution(* com.emadsolutions.LMS.borrowingRecord.BorrowingRecordService.*(..))",
            returning = "result"
    )
    public void logMethodReturn(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        logger.info(ANSI_GREEN + "Method {} returned with result: {}" + ANSI_RESET, methodName, result);
    }

    // Log exceptions in red
    @AfterThrowing(pointcut = "execution(* com.emadsolutions.LMS.auth.AuthService.*(..)) ||" +
            " execution(* com.emadsolutions.LMS.book.BookService.*(..)) ||" +
            " execution(* com.emadsolutions.LMS.patron.PatronService.*(..)) ||" +
            " execution(* com.emadsolutions.LMS.borrowingRecord.BorrowingRecordService.*(..))",
            throwing = "ex"
    )
    public void logMethodException(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        logger.error(ANSI_RED + "Method {} threw exception: {}" + ANSI_RESET, methodName, ex.getMessage(), ex);
    }
}
