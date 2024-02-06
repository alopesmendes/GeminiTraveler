package com.ippon.geminitraveler.domain.model

import java.time.Instant

data class ModelResponse(
    val data: String,
    val role: Role,
    val createAt: Instant
)
