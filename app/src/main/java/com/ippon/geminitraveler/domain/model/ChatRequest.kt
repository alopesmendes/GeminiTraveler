package com.ippon.geminitraveler.domain.model

import java.time.Instant

data class ChatRequest(
    val title: String,
    val createAt: Instant,
)
