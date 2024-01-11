package com.openai.guru.adapter.datastore.entities

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.util.UUID

@Entity
@Table(name = "map", schema = "public")
data class MapEntity(@Id
                     @GeneratedValue(generator = "UUID")
                     @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
                     @Column(unique = true,columnDefinition="VARCHAR(40)")
                     val id : UUID,
                     @Column(columnDefinition="VARCHAR(5000)")
                     var numerologyMap : String,
                     @OneToOne
                     val user: UserEntity)
