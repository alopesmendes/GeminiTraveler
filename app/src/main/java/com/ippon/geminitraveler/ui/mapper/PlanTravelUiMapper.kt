package com.ippon.geminitraveler.ui.mapper

import com.ippon.geminitraveler.domain.model.PlanTravel
import com.ippon.geminitraveler.domain.model.Role
import com.ippon.geminitraveler.ui.models.PlanTravelUi
import com.ippon.geminitraveler.ui.models.RoleUi

fun PlanTravel.mapToPlanTravelUi(): PlanTravelUi = PlanTravelUi(
    data = data,
    role = role.mapToRoleUi()
)

fun Role.mapToRoleUi(): RoleUi {
    return when(this) {
        Role.USER -> RoleUi.USER
        Role.MODEL -> RoleUi.MODEL
    }
}