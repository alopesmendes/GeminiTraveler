package com.ippon.geminitraveler.data.mappers

import com.ippon.geminitraveler.data.datasource.database.entities.MessageEntity
import com.ippon.geminitraveler.domain.model.ModelResponse
import com.ippon.geminitraveler.domain.model.ModelRequest
import com.ippon.geminitraveler.domain.model.Role

fun ModelRequest.mapToPlanTravel(): ModelResponse = ModelResponse(
    data = data,
    role = Role.USER,
)

fun ModelResponse.mapToMessageEntity(): MessageEntity = MessageEntity(
    message = data,
    isGemini = role == Role.MODEL,
)