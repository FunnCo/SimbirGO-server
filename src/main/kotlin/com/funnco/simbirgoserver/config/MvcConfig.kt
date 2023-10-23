package com.funnco.simbirgoserver.config

import com.funnco.simbirgoserver.interceptor.AuthInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableWebMvc
@Configuration
class MvcConfig : WebMvcConfigurer {

    @Bean
    fun authInterceptor(): AuthInterceptor{
        return AuthInterceptor()
    }
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor())
        super.addInterceptors(registry)
    }
}