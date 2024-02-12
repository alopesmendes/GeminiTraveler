package com.ippon.geminitraveler.ui.components

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerItem(
    label: String,
    selected: Boolean,
    scope: CoroutineScope,
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    onEdit: (String) -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isPressed by interactionSource.collectIsPressedAsState()
    var isEditModeActive by rememberSaveable {
        mutableStateOf(false)
    }
    var value by rememberSaveable {
        mutableStateOf(label)
    }
    val icon by remember(isEditModeActive) {
        derivedStateOf {
            if (isEditModeActive) {
                Icons.Filled.Check
            } else {
                Icons.Filled.Edit
            }
        }
    }

    LaunchedEffect(isPressed) {
        Log.d("DrawerItem", "DrawerItem: $isPressed")
        if (isPressed && !isEditModeActive) {
            onClick()
            scope.launch {
                drawerState.close()
            }
        }
    }

    NavigationDrawerItem(
        badge = {
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "delete item",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
        icon = {
            IconButton(
                onClick = {
                    isEditModeActive = !isEditModeActive
                    if (!isEditModeActive && label != value) {
                        onEdit(value)
                    }
                }
            ) {
                Icon(
                    icon,
                    contentDescription = "edit item"
                )
            }
        },
        label = {
            BasicTextField(
                value = value,
                onValueChange = { value = it },
                interactionSource = interactionSource,
                readOnly = !isEditModeActive,
                textStyle = MaterialTheme.typography.titleMedium
                    .copy(color = MaterialTheme.colorScheme.onSurface),
                singleLine = true,
                cursorBrush = SolidColor(
                    MaterialTheme.colorScheme.onSurface
                )
            )
        },
        selected = selected,
        onClick = {
            onClick()
            scope.launch {
                drawerState.close()
            }
        },
        modifier = modifier
            .fillMaxWidth()
    )
}

@Composable
@Preview
@PreviewScreenSizes
@PreviewLightDark
private fun DrawerItemPreview() {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    var selected by remember {
        mutableStateOf(false)
    }
    DrawerItem(
        label = "One Piece",
        selected = selected,
        scope = scope,
        drawerState = drawerState,
        onEdit = { },
        onDelete = { },
    ) {
        selected = !selected
    }
}