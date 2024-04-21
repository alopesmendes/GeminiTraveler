package com.ippon.geminitraveler.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.ippon.geminitraveler.R
import com.ippon.geminitraveler.ui.models.ChatHistoryItem
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatHistoryList(
    modifier: Modifier = Modifier,
    chatHistoryItems: List<ChatHistoryItem>,
    scope: CoroutineScope,
    drawerState: DrawerState,
    onUpdateTitle: (id: Long, value: String) -> Unit,
    onDelete: (Long) -> Unit,
    onNavigate: (Long) -> Unit,
) {
    var selectedItem by rememberSaveable { mutableLongStateOf(-1) }
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        item {
            Text(
                stringResource(id = R.string.history_title),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
        items(chatHistoryItems, key = { it.id }) {item ->
            DrawerItem(
                label = item.title,
                selected = item.id == selectedItem,
                scope = scope,
                drawerState = drawerState,
                modifier = Modifier.animateItem(),
                onEdit = {
                    onUpdateTitle(item.id, it)
                },
                onDelete = { onDelete(item.id) },
            ) {
                selectedItem = item.id
                onNavigate(item.id)
            }
        }

    }
}

@Preview
@PreviewScreenSizes
@PreviewDynamicColors
@PreviewFontScale
@PreviewLightDark
@Composable
private fun ChatHistoryListPreview() {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
   val chatHistoryItems = remember {
       mutableStateListOf(
           *(1..10).map {
               ChatHistoryItem(
                   id = it.toLong(),
                   title = "Title $it",
                   createAt = "01/01/2024"
               )
           }.toTypedArray()
       )
   }
    ChatHistoryList(
        modifier = Modifier.padding(8.dp),
        chatHistoryItems = chatHistoryItems,
        scope = scope,
        drawerState = drawerState,
        onDelete = { id ->
            chatHistoryItems.removeIf { it.id == id }
        },
        onUpdateTitle = { id, value ->
            val index = chatHistoryItems.indexOfFirst { it.id == id }
            chatHistoryItems[index] = chatHistoryItems[index].copy(title = value)
        }
    ) {

    }
}