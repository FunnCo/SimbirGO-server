package com.funnco.simbirgoserver.interceptor

import com.funnco.simbirgoserver.dto.http.BasicUserDTO
import com.funnco.simbirgoserver.interceptor.annotation.Authorized
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

class AuthInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        return super.preHandle(request, response, handler)
    }
}