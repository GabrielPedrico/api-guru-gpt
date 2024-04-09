package com.openai.guru.core.biz

import com.openai.guru.core.model.ThreadResponseModel
import com.openai.guru.core.port.`in`.CreateNumerologyMapPortIn
import com.openai.guru.core.port.out.CreateNumerologyMapPortOut
import org.springframework.stereotype.Service
import java.util.*

@Service
class CreateMapBiz(
    val portOut: CreateNumerologyMapPortOut
): CreateNumerologyMapPortIn {

    override fun createNumerologyMap(userId: UUID): ThreadResponseModel = portOut.createMap(userId)
}