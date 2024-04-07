package com.openai.guru.adapter.datastore.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDate

@Entity
@Table(name = "tb_user", schema = "public")
data class UserEntity(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(unique = true, columnDefinition = "VARCHAR(40)")
    val id: String,
    var name: String,
    var lastname: String,
    var birthday: LocalDate?,
    var email: String
)
