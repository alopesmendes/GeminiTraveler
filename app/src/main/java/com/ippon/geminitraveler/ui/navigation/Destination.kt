package com.ippon.geminitraveler.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Destination(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList(),
) {

    val fullRoute: String = "$route${
        arguments
        .map { it.name }
        .joinToString(prefix = "/", separator = "/") { "{$it}" }
    }"

    data object Home: Destination(
        route = "home"
    )

    data object Chat: Destination(
        route = "chat",
        arguments = listOf(
            navArgument("id") { type = NavType.IntType }
        )
    )
}
