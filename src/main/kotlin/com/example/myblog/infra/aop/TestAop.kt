package com.example.myblog.infra.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch

@Aspect//Aspect를 정의
@Component//SpringAop는 Bean에만 적용할 수 있으므로
class TestAop {

    private val logger = LoggerFactory.getLogger("Execution Time Logger!")

//    @Pointcut("within(@com.example.myblog.infra.aop.StopWatch *)")
//    fun annotatedWithStopWatch(){}

    //    @Around("execution(* com.example.myblog.domain.user.service.UserService.*(..))")
    //Advice 적용시점 (@Around -> 전후)
    //execution(* ..) -> Pointcut Expression : Advice 적용대상 서술

    //    @Around("@annotation(com.example.myblog.infra.aop.StopWatch)")
    @Around("within(@com.example.myblog.infra.aop.StopWatch *)")
    fun thisIsAdvice(joinPoint: ProceedingJoinPoint): Any? {
        val stopWatch = StopWatch()
        logger.info("====================시작!!")
        stopWatch.start()
        try {
            return joinPoint.proceed()
        } finally {
            stopWatch.stop()
            logger.info("=======================끝!! Times: ${stopWatch.totalTimeMillis}ms")
        }


//        println("name : ${joinPoint.signature.name}")
//        println("args : ${joinPoint.args.joinToString(",")}")
//        println("AOP END!!")
    }
}