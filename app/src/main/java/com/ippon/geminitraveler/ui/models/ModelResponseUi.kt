package com.ippon.geminitraveler.ui.models

import androidx.compose.runtime.Immutable

@Immutable
data class ModelResponseUi(
    val data: String,
    val role: RoleUi,
)
