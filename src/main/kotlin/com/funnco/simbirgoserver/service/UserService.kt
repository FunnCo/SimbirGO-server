package com.funnco.simbirgoserver.service

import com.funnco.simbirgoserver.database.entity.AccountEntity
import com.funnco.simbirgoserver.dto.http.BasicUserDTO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService {

    private val logger = LoggerFactory.getLogger(this::class.java.simpleName)

    @Autowired
    lateinit var jwtService: JwtService

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    private fun checkIfUserExists(username: String): Boolean {
        try {
            val user = jdbcTemplate.queryForObject(
                "SELECT * FROM public.account WHERE username = ?",
                BeanPropertyRowMapper(AccountEntity::class.java),
                username
            )
            if (user != null) {
                return true
            }
        } catch (e: EmptyResultDataAccessException) {
            return false
        }
        return false
    }

    fun getUserByHisToken(token: String): AccountEntity {
        return getUserByHisId(jwtService.getUserIdFromToken(token))
    }
    fun getUserByHisId(id: String): AccountEntity {
        try {
            val user = jdbcTemplate.queryForObject(
                "SELECT * FROM public.account WHERE id=?",
                BeanPropertyRowMapper(AccountEntity::class.java),
                id
            )
            return user!!
        } catch (e: Exception){
            logger.info("Error while getting user ${id}, ${e.message}")
            throw e
        }
    }

    fun createNewUser(userDTO: BasicUserDTO, balance: Double = 0.0, roleId: Int = 1) {
        if (!checkIfUserExists(userDTO.username)) {
            try {
                jdbcTemplate.update(
                    "INSERT INTO public.account (id, role_id, balance, username, hashed_password) VALUES (?,?,?,?,?)",
                    UUID.randomUUID(),
                    roleId,
                    balance,
                    userDTO.username,
                    jwtService.hashPassword(userDTO.password)
                )
                logger.info("Successfully finished registration of user with email ${userDTO.username}")
            } catch (e: Exception) {
                logger.info("Error while creating user ${userDTO.username}, ${e.message}")
                throw e
            }
        } else {
            throw DuplicateKeyException("Couldn't add new user with email ${userDTO.username} because it already exists")
        }
    }

    fun login(userDTO: BasicUserDTO): String {
        try {
            val user = jdbcTemplate.queryForObject(
                "SELECT * FROM public.account WHERE username = ? AND hashed_password = ?",
                BeanPropertyRowMapper(AccountEntity::class.java),
                userDTO.username,
                jwtService.hashPassword(userDTO.password)
            )
            return jwtService.getTokenForUser(user!!)
        } catch (e: EmptyResultDataAccessException) {
            throw ChangeSetPersister.NotFoundException()
        }
    }

    fun updateUser(userDTO: BasicUserDTO, userToken: String) {
        val userId = jwtService.getUserIdFromToken(userToken)
        if (!checkIfUserExists(userDTO.username)) {
            try {
                jdbcTemplate.update(
                    "UPDATE public.account SET account.username = ?, account.hashed_password = ? WHERE account.id= ?",
                    userDTO.username,
                    jwtService.hashPassword(userDTO.password),
                    userId
                )
                logger.info("Successfully finished update of user with email ${userDTO.username}")
            } catch (e: Exception) {
                logger.info("Error while creating user ${userDTO.username}, ${e.message}")
                throw e
            }
        } else {
            throw DuplicateKeyException("Couldn't add user with email ${userDTO.username} because it already exists")
        }
    }
}