package com.openai.guru.adapter.datastore.repositories

import com.openai.guru.adapter.datastore.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, String> {
}