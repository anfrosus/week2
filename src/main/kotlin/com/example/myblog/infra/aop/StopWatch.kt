package com.example.myblog.infra.aop

import org.hibernate.annotations.Type
import java.lang.annotation.ElementType

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class StopWatch()
