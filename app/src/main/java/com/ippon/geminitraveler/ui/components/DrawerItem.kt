package com.ippon.geminitraveler.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerItem(
    label: String,
    scope: CoroutineScope,
    drawerState: DrawerState,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(text = label) },
        selected = false,
        onClick = {
            onClick()
            scope.launch {
                drawerState.close()
            }
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )

}