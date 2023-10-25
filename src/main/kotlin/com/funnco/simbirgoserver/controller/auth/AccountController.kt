package com.funnco.simbirgoserver.controller.auth

import com.funnco.simbirgoserver.database.entity.AccountEntity
import com.funnco.simbirgoserver.dto.http.BasicUserDTO
import com.funnco.simbirgoserver.interceptor.annotation.Authorized
import com.funnco.simbirgoserver.service.JwtService
import com.funnco.simbirgoserver.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@Controller
@RequestMapping("/api/Account")
class AccountController(
    val userService: UserService
) {

    private val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    @Authorized
    @GetMapping("/Me")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    fun getUserInfo(@RequestHeader("Authorization") token: String): AccountEntity{
        try {
            return userService.getUserByHisToken(token)
        } catch (e: DuplicateKeyException){
            throw ResponseStatusException(HttpStatus.CONFLICT, "User with such name already exists", e)
        }
    }

    @PostMapping("/SignUp")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody basicUserDTO: BasicUserDTO){
        try {
            userService.createNewUser(basicUserDTO)
        } catch (e: DuplicateKeyException){
            throw ResponseStatusException(HttpStatus.CONFLICT, "User with such name already exists", e)
        }
    }

    @PostMapping("/SignIn")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun login(@RequestBody basicUserDTO: BasicUserDTO): String{
        try {
            return userService.login(basicUserDTO)
        } catch (e: Exception){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User with such name already exists", e)
        }
    }

    // TODO: Решить что вообще делать с этим, с учетом того, что JWT инвалидировать нельзя
    fun logout(){

    }

    @Authorized
    @PutMapping("/Update")
    @ResponseStatus(HttpStatus.OK)
    fun updateUser(@RequestHeader("Authorization") token: String,@RequestBody basicUserDTO: BasicUserDTO){
        userService.updateUser(basicUserDTO, token)
    }

}