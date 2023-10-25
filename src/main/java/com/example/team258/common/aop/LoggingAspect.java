package com.example.team258.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // [3] AOP 포인트컷 정의 - 실행 위치

    // 모든 도메인의 컨트롤러 포인트컷
    @Pointcut("commonControllerMethods() || adminControllerMethods() || bookSearchControllerMethods() || donationControllerMethods() || memberControllerMethods() || userControllerMethods()")
    private void allDomainControllerMethods() {
    }
    // 모든 도메인의 서비스 포인트컷
    @Pointcut("commonServiceMethods() || adminServiceMethods() || bookSearchServiceMethods() || donationServiceMethods() || userServiceMethods()") //memberServiceMethods()
    private void allDomainServiceMethods() {
    }
    // 모든 도메인의 Repository 포인트컷
    @Pointcut("commonRepositoryMethods() || adminRepositoryMethods() || donationRepositoryMethods() || userRepositoryMethods()") // bookSearchRepositoryMethods() || memberRepositoryMethods() ||
    private void allDomainRepositoryMethods() {
    }
    // Controller의 메소드 호출 시 로그 출력
    @Pointcut("execution(* com.example.team258.common.controller..*(..))")
    private void commonControllerMethods() {
    }
    @Pointcut("execution(* com.example.team258.domain.admin.controller..*(..))")
    private void adminControllerMethods() {
    }
    @Pointcut("execution(* com.example.team258.domain.bookSearch.controller..*(..))")
    private void bookSearchControllerMethods() {
    }
    @Pointcut("execution(* com.example.team258.domain.donation.controller..*(..))")
    private void donationControllerMethods() {
    }
    @Pointcut("execution(* com.example.team258.domain.member.controller..*(..))")
    private void memberControllerMethods() {
    }
    @Pointcut("execution(* com.example.team258.domain.user.controller..*(..))")
    private void userControllerMethods() {
    }

    // Service의 메소드 호출 시 로그 출력
    @Pointcut("execution(* com.example.team258.common.service..*(..))")
    private void commonServiceMethods() {
    }
    @Pointcut("execution(* com.example.team258.domain.admin.service..*(..))")
    private void adminServiceMethods() {
    }
    @Pointcut("execution(* com.example.team258.domain.bookSearch.service..*(..))")
    private void bookSearchServiceMethods() {
    }
    @Pointcut("execution(* com.example.team258.domain.donation.service..*(..))")
    private void donationServiceMethods() {
    }
    //@Pointcut("execution(* com.example.team258.domain.member.service..*(..))")
    //private void memberServiceMethods() {
    //}
    @Pointcut("execution(* com.example.team258.domain.user.service..*(..))")
    private void userServiceMethods() {
    }
    // Repository의 메소드 호출 시 로그 출력
    @Pointcut("execution(* com.example.team258.common.repository..*(..))")
    private void repositoryMethods() {
    }
    // Repository의 메소드 호출 시 로그 출력
    @Pointcut("execution(* com.example.team258.common.repository..*(..))")
    private void commonRepositoryMethods() {
    }
    @Pointcut("execution(* com.example.team258.domain.admin.repository..*(..))")
    private void adminRepositoryMethods() {
    }
    //@Pointcut("execution(* com.example.team258.domain.bookSearch.repository..*(..))")
    //private void bookSearchRepositoryMethods() {
    //}
    @Pointcut("execution(* com.example.team258.domain.donation.repository..*(..))")
    private void donationRepositoryMethods() {
    }
    //@Pointcut("execution(* com.example.team258.domain.member.repository..*(..))")
    //private void memberRepositoryMethods() {
    //}
    @Pointcut("execution(* com.example.team258.domain.user.repository..*(..))")
    private void userRepositoryMethods() {
    }

    // Controller, Service, Repository 모두 포함하는 메소드 호출 시 로그 출력
    @Before("allDomainControllerMethods() || allDomainServiceMethods() || allDomainRepositoryMethods()")
    public void logAllMethods(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("메소드 < {} > 호출", methodName);
    }
}

//
//
//// Controller의 메소드 반환 후 로그 출력
//@AfterReturning(
//        pointcut = "controllerMethods()",
//        returning = "result"
//)
//
//public void logControllerMethodResult(JoinPoint joinPoint, Object result) {
//    String methodName = joinPoint.getSignature().getName();
//    logger.info("Controller 메소드 '{}' 호출 -> 반환 : {}", methodName, result.toString());
//}
//
//
//
//// Service의 메소드 반환 후 로그 출력
//@AfterReturning(
//        pointcut = "serviceCreateMethods()",
//        returning = "result"
//)
//public void logServiceMethodResult(JoinPoint joinPoint, Object result) {
//    String methodName = joinPoint.getSignature().getName();
//    logger.info("Service 메소드 '{}' 호출 -> 반환 : {}", methodName, result.toString());
//}
//
//
//
//// Repository의 메소드 반환 후 로그 출력
//@AfterReturning(
//        pointcut = "repositoryMethods()",
//        returning = "result"
//)
//public void logRepositoryMethodResult(JoinPoint joinPoint, Object result) {
//    String methodName = joinPoint.getSignature().getName();
//    logger.info("Repository 메소드 '{}' 호출 -> 반환 : {}", methodName, result.toString());
//}