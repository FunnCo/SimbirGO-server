package com.funnco.simbirgoserver.controller.auth

import com.funnco.simbirgoserver.database.entity.AccountEntity
import com.funnco.simbirgoserver.interceptor.annotation.AdminAuthorized
import com.funnco.simbirgoserver.service.UserService
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.server.ResponseStatusException

@Controller
@RequestMapping("/api/Admin/Account")
class AdminAccountController(
    val userService: UserService
) {

    @AdminAuthorized
    @GetMapping
    fun getAllAccounts(){

    }
    @AdminAuthorized
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun getAccount(@PathVariable("id") id: String): AccountEntity{
        try {
            return userService.getUserByHisId(id)
        } catch (e: Exception){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "something broke", e)
        }
    }
    @AdminAuthorized
    @PostMapping
    fun createNewAccount(){

    }
    @AdminAuthorized
    @PutMapping("/{id}")
    fun updateAccount(@PathVariable("id") id: String){

    }
    @AdminAuthorized
    @DeleteMapping("/{id}")
    fun deleteAccount(@PathVariable("id") id: String){

    }


}