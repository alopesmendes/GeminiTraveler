package com.ippon.geminitraveler.domain.model

import java.time.Instant

data class ModelRequest(
    val data: String,
    val createAt: Instant,
    val chatId: Long,
)
