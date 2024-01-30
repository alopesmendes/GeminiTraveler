package com.ippon.geminitraveler.ui.mapper

import com.ippon.geminitraveler.domain.model.ModelResponse
import com.ippon.geminitraveler.domain.model.Role
import com.ippon.geminitraveler.ui.models.ModelResponseUi
import com.ippon.geminitraveler.ui.models.RoleUi

fun ModelResponse.mapToPlanTravelUi(): ModelResponseUi = ModelResponseUi(
    data = data,
    role = role.mapToRoleUi()
)

fun Role.mapToRoleUi(): RoleUi {
    return when(this) {
        Role.USER -> RoleUi.USER
        Role.MODEL -> RoleUi.MODEL
    }
}