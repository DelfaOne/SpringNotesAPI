package com.delfa.spring_boot_rest_api.database.repository

import com.delfa.spring_boot_rest_api.controllers.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, ObjectId> {
    fun findByEmail(email: String): User?
}