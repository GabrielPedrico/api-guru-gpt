package com.openai.guru.core.port.out

import com.openai.guru.core.model.ThreadResponseModel
import java.util.*

interface CreateNumerologyMapPortOut {

    fun createMap(userId : UUID) : ThreadResponseModel
}