package com.ippon.geminitraveler.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ippon.geminitraveler.R
import com.ippon.geminitraveler.ui.models.ChatHistoryItem
import kotlinx.coroutines.CoroutineScope

@Composable
fun ChatHistoryList(
    modifier: Modifier,
    chatHistoryItems: List<ChatHistoryItem>,
    scope: CoroutineScope,
    drawerState: DrawerState,
    onNavigate: (Long) -> Unit,
) {
    var selectedItem by rememberSaveable { mutableLongStateOf(-1) }
    LazyColumn(modifier = modifier) {
        item {
            Text(
                stringResource(id = R.string.history_title),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
        items(chatHistoryItems, key = { it.id }) {
            DrawerItem(
                label = it.title,
                scope = scope,
                drawerState = drawerState,
                date = it.createAt,
                selected = it.id == selectedItem
            ) {
                selectedItem = it.id
                onNavigate(it.id)
            }
        }

    }
}