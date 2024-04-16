package com.openai.guru.core.port.`in`

import com.openai.guru.core.model.CreateMapModel
import com.openai.guru.core.model.ThreadResponseModel

fun interface CreateNumerologyMapPortIn {

    fun createNumerologyMap(createMapModel: CreateMapModel): ThreadResponseModel
}