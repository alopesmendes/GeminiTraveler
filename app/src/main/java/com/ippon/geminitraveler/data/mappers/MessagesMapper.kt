package com.ippon.geminitraveler.data.mappers

import com.ippon.geminitraveler.data.datasource.database.entities.MessageEntity
import com.ippon.geminitraveler.domain.model.ModelResponse
import com.ippon.geminitraveler.domain.model.ModelRequest
import com.ippon.geminitraveler.domain.model.Role

fun ModelRequest.mapToModelResponse(): ModelResponse = ModelResponse(
    data = data,
    role = Role.USER,
    createAt = createAt,
)

fun ModelResponse.mapToMessageEntity(): MessageEntity = MessageEntity(
    message = data,
    role = role,
    createAt = createAt,
)

fun MessageEntity.mapToModelResponse(): ModelResponse = ModelResponse(
    data = message,
    role = role,
    createAt = createAt
)