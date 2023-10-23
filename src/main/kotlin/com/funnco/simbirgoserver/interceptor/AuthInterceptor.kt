package com.funnco.simbirgoserver.interceptor

import com.funnco.simbirgoserver.dto.http.BasicUserDTO
import com.funnco.simbirgoserver.interceptor.annotation.AdminAuthorized
import com.funnco.simbirgoserver.interceptor.annotation.Authorized
import com.funnco.simbirgoserver.service.JwtService
import com.funnco.simbirgoserver.service.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.method.HandlerMethod
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.HandlerInterceptor

class AuthInterceptor : HandlerInterceptor {

    @Autowired
    lateinit var jwtService: JwtService

    @Autowired
    lateinit var userService: UserService

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        val controllerMethod = handler as HandlerMethod
        val rawToken = request.getHeader("Authorization")

        if(controllerMethod.hasMethodAnnotation(Authorized::class.java)){
            if(!jwtService.validateToken(rawToken)){
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is invalid")
            }
        }
        if(controllerMethod.hasMethodAnnotation(AdminAuthorized::class.java)){
            if(jwtService.validateToken(rawToken)){
                if(userService.getUserByHisId(jwtService.getUserIdFromToken(rawToken)).roleId != 2) {
                    throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "This is only admin request")
                }
            } else {
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is invalid")
            }
        }

        return super.preHandle(request, response, handler)
    }
}