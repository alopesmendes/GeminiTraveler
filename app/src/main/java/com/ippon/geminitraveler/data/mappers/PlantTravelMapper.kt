package com.ippon.geminitraveler.data.mappers

import com.ippon.geminitraveler.domain.model.PlanTravel
import com.ippon.geminitraveler.domain.model.RequestPlan
import com.ippon.geminitraveler.domain.model.Role

fun RequestPlan.mapToPlanTravel(): PlanTravel = PlanTravel(
    data = data,
    role = Role.USER,
)