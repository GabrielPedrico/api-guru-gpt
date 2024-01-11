package com.openai.guru.core.port.`in`

import com.openai.guru.core.model.ThreadResponseModel
import java.util.UUID

interface CreateNumerologyMapPortIn {

    fun createNumerologyMap(userId : UUID): ThreadResponseModel
}