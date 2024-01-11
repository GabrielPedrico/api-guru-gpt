package com.openai.guru.adapter.datastore.repositories

import com.openai.guru.adapter.datastore.entities.MapEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MapRepository : JpaRepository<MapEntity, UUID> {
}