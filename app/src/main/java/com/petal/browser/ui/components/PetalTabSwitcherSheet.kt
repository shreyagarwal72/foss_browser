/*
 * PetalTabSwitcherSheet.kt
 * ─────────────────────────────────────────────────────────────────────────
 * Chrome Android-style Tab Switcher Overview Sheet with live tab cards,
 * active selection highlights, spring entrance animations, and close controls.
 */

package com.petal.browser.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.petal.browser.browser.AlbumController
import com.petal.browser.browser.BrowserContainer
import com.petal.browser.view.NinjaWebView
import com.petal.browser.ui.theme.PetalExpressiveTheme

data class TabModel(
    val id: Int,
    val title: String,
    val url: String,
    val isActive: Boolean
)

object PetalTabSwitcherBridge {
    @JvmStatic
    fun showTabSwitcherSheet(
        activity: ComponentActivity,
        currentAlbum: AlbumController?,
        onSelectTab: (AlbumController) -> Unit,
        onCloseTab: (AlbumController) -> Unit,
        onCloseAllTabs: () -> Unit,
        onNewTab: () -> Unit
    ) {
        try {
            val dialog = BottomSheetDialog(activity)
            val composeView = ComposeView(activity).apply {
                setViewTreeLifecycleOwner(activity)
                setViewTreeSavedStateRegistryOwner(activity)
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    PetalExpressiveTheme {
                        val currentCount = BrowserContainer.size()
                        val tabsList = remember(currentCount) {
                            BrowserContainer.list().mapIndexed { index, album ->
                                val webView = album as? NinjaWebView
                                val rawTitle = webView?.title
                                val rawUrl = webView?.url
                                val displayTitle = when {
                                    !rawTitle.isNullOrBlank() -> rawTitle
                                    !rawUrl.isNullOrBlank() && !rawUrl.startsWith("file:///android_asset/") -> rawUrl
                                    else -> "New Tab"
                                }
                                val displayUrl = rawUrl ?: "about:blank"
                                TabModel(
                                    id = index,
                                    title = displayTitle,
                                    url = displayUrl,
                                    isActive = album == currentAlbum
                                )
                            }
                        }

                        PetalTabSwitcherSheet(
                            tabs = tabsList,
                            onSelectTab = { model ->
                                try { dialog.dismiss() } catch (ignored: Exception) {}
                                if (model.id >= 0 && model.id < BrowserContainer.size()) {
                                    val album = BrowserContainer.get(model.id)
                                    onSelectTab(album)
                                }
                            },
                            onCloseTab = { model ->
                                if (model.id >= 0 && model.id < BrowserContainer.size()) {
                                    val album = BrowserContainer.get(model.id)
                                    onCloseTab(album)
                                }
                                if (BrowserContainer.size() == 0) {
                                    try { dialog.dismiss() } catch (ignored: Exception) {}
                                }
                            },
                            onCloseAllTabs = {
                                try { dialog.dismiss() } catch (ignored: Exception) {}
                                onCloseAllTabs()
                            },
                            onNewTab = {
                                try { dialog.dismiss() } catch (ignored: Exception) {}
                                onNewTab()
                            },
                            onDismiss = {
                                try { dialog.dismiss() } catch (ignored: Exception) {}
                            }
                        )
                    }
                }
            }
            dialog.setContentView(composeView)
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetalTabSwitcherSheet(
    tabs: List<TabModel>,
    onSelectTab: (TabModel) -> Unit,
    onCloseTab: (TabModel) -> Unit,
    onCloseAllTabs: () -> Unit,
    onNewTab: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Row: Count, New Tab, Close All
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Open Tabs (${tabs.size})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Tap to switch or close tabs",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // New Tab (+) Button
                    FilledTonalIconButton(
                        onClick = {
                            onNewTab()
                            onDismiss()
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Icon(Icons.Rounded.Add, contentDescription = "New Tab")
                    }

                    // Close All Button
                    IconButton(onClick = onCloseAllTabs) {
                        Icon(
                            Icons.Rounded.DeleteSweep,
                            contentDescription = "Close All Tabs",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            // Grid of Chrome Android Style Open Tab Cards
            if (tabs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No open tabs",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = onNewTab) {
                            Icon(Icons.Rounded.Add, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Open New Tab")
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 440.dp)
                ) {
                    itemsIndexed(tabs, key = { _, tab -> tab.id }) { index, tab ->
                        TabCard(
                            tab = tab,
                            index = index,
                            onSelect = {
                                onSelectTab(tab)
                                onDismiss()
                            },
                            onClose = { onCloseTab(tab) }
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TabCard(
    tab: TabModel,
    index: Int,
    onSelect: () -> Unit,
    onClose: () -> Unit
) {
    val borderColor = if (tab.isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
    val borderWidth = if (tab.isActive) 2.5.dp else 1.dp

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .border(borderWidth, borderColor, RoundedCornerShape(20.dp))
            .bouncyClickable(scaleDown = 0.94f, onClick = onSelect)
            .entrance(index = index)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Tab Card Header: Icon, Title, Close X
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Rounded.Public,
                    contentDescription = null,
                    tint = if (tab.isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = if (tab.title.isBlank()) "New Tab" else tab.title,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = "Close Tab",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Tab Preview Body
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (tab.isActive) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        else MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (tab.url.isBlank()) "home.html" else tab.url,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
