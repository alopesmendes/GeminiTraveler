package com.ippon.geminitraveler.domain.model

import java.time.Instant

data class Chat(
    val id: Long,
    val title: String,
    val createAt: Instant,
)
