package com.openai.guru.core.biz

import com.openai.guru.core.model.CreateMapModel
import com.openai.guru.core.model.ThreadResponseModel
import com.openai.guru.core.port.inbound.CreateNumerologyMapPortIn
import com.openai.guru.core.port.out.CreateNumerologyMapPortOut
import org.springframework.stereotype.Service

@Service
class CreateMapBiz(
    val portOut: CreateNumerologyMapPortOut,
) : CreateNumerologyMapPortIn {
    override fun createNumerologyMap(createMapModel: CreateMapModel): ThreadResponseModel =
        portOut.createMap(createMapModel.userId, createMapModel.correlationId)
}
