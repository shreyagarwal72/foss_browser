/*
 * PetalOverflowMenuSheet.kt
 * ─────────────────────────────────────────────────────────────────────────
 * Material 3 Expressive Options Menu Sheet with 28dp rounded container corners,
 * dark surface container background, spring-animated top icon row, dividers,
 * expandable More Tools section, and full interop bridge for Petal Browser.
 */

package com.petal.browser.ui.components

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.petal.browser.ui.theme.PetalExpressiveTheme

interface PetalOverflowMenuActionHandler {
    fun onGoForward()
    fun onToggleBookmark()
    fun onOpenDownloadsShortcut()
    fun onOpenPageInfo()
    fun onReload()
    fun onNewTab()
    fun onNewIncognitoTab()
    fun onOpenHistory()
    fun onDeleteBrowsingData()
    fun onOpenDownloads()
    fun onOpenBookmarks()
    fun onBookmarkAllTabs()
    fun onSearchOnSite()
    fun onPrintPdf()
    fun onSavePage()
    fun onShareLink()
    fun onViewSource()
    fun onOpenSettings()
}

object PetalOverflowBridge {
    @JvmStatic
    fun showOverflowMenu(
        activity: ComponentActivity,
        title: String,
        url: String,
        isBookmarked: Boolean,
        canGoForward: Boolean,
        handler: PetalOverflowMenuActionHandler
    ) {
        try {
            val dialog = BottomSheetDialog(activity)
            val composeView = ComposeView(activity).apply {
                setViewTreeLifecycleOwner(activity)
                setViewTreeSavedStateRegistryOwner(activity)
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    PetalExpressiveTheme {
                        PetalOverflowMenuSheet(
                            pageTitle = title,
                            pageUrl = url,
                            isBookmarked = isBookmarked,
                            canGoForward = canGoForward,
                            onGoForward = {
                                dialog.dismiss()
                                handler.onGoForward()
                            },
                            onToggleBookmark = {
                                dialog.dismiss()
                                handler.onToggleBookmark()
                            },
                            onOpenDownloadsShortcut = {
                                dialog.dismiss()
                                handler.onOpenDownloadsShortcut()
                            },
                            onOpenPageInfo = {
                                dialog.dismiss()
                                handler.onOpenPageInfo()
                            },
                            onReload = {
                                dialog.dismiss()
                                handler.onReload()
                            },
                            onNewTab = {
                                dialog.dismiss()
                                handler.onNewTab()
                            },
                            onNewIncognitoTab = {
                                dialog.dismiss()
                                handler.onNewIncognitoTab()
                            },
                            onOpenHistory = {
                                dialog.dismiss()
                                handler.onOpenHistory()
                            },
                            onDeleteBrowsingData = {
                                dialog.dismiss()
                                handler.onDeleteBrowsingData()
                            },
                            onOpenDownloads = {
                                dialog.dismiss()
                                handler.onOpenDownloads()
                            },
                            onOpenBookmarks = {
                                dialog.dismiss()
                                handler.onOpenBookmarks()
                            },
                            onBookmarkAllTabs = {
                                dialog.dismiss()
                                handler.onBookmarkAllTabs()
                            },
                            onSearchOnSite = {
                                dialog.dismiss()
                                handler.onSearchOnSite()
                            },
                            onPrintPdf = {
                                dialog.dismiss()
                                handler.onPrintPdf()
                            },
                            onSavePage = {
                                dialog.dismiss()
                                handler.onSavePage()
                            },
                            onShareLink = {
                                dialog.dismiss()
                                handler.onShareLink()
                            },
                            onViewSource = {
                                dialog.dismiss()
                                handler.onViewSource()
                            },
                            onOpenSettings = {
                                dialog.dismiss()
                                handler.onOpenSettings()
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
fun PetalOverflowMenuSheet(
    pageTitle: String,
    pageUrl: String,
    isBookmarked: Boolean,
    canGoForward: Boolean,
    onGoForward: () -> Unit,
    onToggleBookmark: () -> Unit,
    onOpenDownloadsShortcut: () -> Unit,
    onOpenPageInfo: () -> Unit,
    onReload: () -> Unit,
    onNewTab: () -> Unit,
    onNewIncognitoTab: () -> Unit,
    onOpenHistory: () -> Unit,
    onDeleteBrowsingData: () -> Unit,
    onOpenDownloads: () -> Unit,
    onOpenBookmarks: () -> Unit,
    onBookmarkAllTabs: () -> Unit,
    onSearchOnSite: () -> Unit,
    onPrintPdf: () -> Unit,
    onSavePage: () -> Unit,
    onShareLink: () -> Unit,
    onViewSource: () -> Unit,
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit
) {
    var isMoreToolsExpanded by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
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
                .padding(horizontal = 20.dp, vertical = 4.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Page Info Card
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                if (pageUrl.startsWith("https://")) Icons.Rounded.Lock else Icons.Rounded.Public,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (pageTitle.isBlank()) "New Tab" else pageTitle,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = if (pageUrl.isBlank()) "about:blank" else pageUrl,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Top Icon Row (5 circular icon buttons, evenly spaced) with spring press feedback
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Forward Button
                CircularIconButton(
                    icon = Icons.Rounded.ArrowForward,
                    contentDescription = "Forward",
                    enabled = canGoForward,
                    onClick = onGoForward
                )

                // 2. Bookmark Toggle Button (star filled/outline)
                CircularIconButton(
                    icon = if (isBookmarked) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                    contentDescription = "Toggle Bookmark",
                    tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    onClick = onToggleBookmark
                )

                // 3. Downloads Shortcut Button
                CircularIconButton(
                    icon = Icons.Rounded.Downloading,
                    contentDescription = "Downloads",
                    onClick = onOpenDownloadsShortcut
                )

                // 4. Page Info Button
                CircularIconButton(
                    icon = Icons.Rounded.Shield,
                    contentDescription = "Page Info",
                    onClick = onOpenPageInfo
                )

                // 5. Reload Button
                CircularIconButton(
                    icon = Icons.Rounded.Refresh,
                    contentDescription = "Reload",
                    onClick = onReload
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            // Section 1: New Tab & New Private Tab
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                MenuRowItem(
                    icon = Icons.Rounded.Add,
                    title = "New tab",
                    onClick = onNewTab
                )
                MenuRowItem(
                    icon = Icons.Rounded.VisibilityOff,
                    title = "New Private / Incognito tab",
                    subtitle = "Browse without saving search history",
                    onClick = onNewIncognitoTab
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            // Section 2: History & Delete browsing data
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                MenuRowItem(
                    icon = Icons.Rounded.History,
                    title = "History",
                    onClick = onOpenHistory
                )
                MenuRowItem(
                    icon = Icons.Rounded.DeleteSweep,
                    title = "Delete browsing data",
                    onClick = onDeleteBrowsingData
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            // Section 3: Downloads, Bookmarks, Bookmark all tabs
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                MenuRowItem(
                    icon = Icons.Rounded.Download,
                    title = "Downloads",
                    onClick = onOpenDownloads
                )
                MenuRowItem(
                    icon = Icons.Rounded.Bookmark,
                    title = "Bookmarks",
                    onClick = onOpenBookmarks
                )
                MenuRowItem(
                    icon = Icons.Rounded.BookmarkAdd,
                    title = "Bookmark all tabs",
                    onClick = onBookmarkAllTabs
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            // Section 4: Expandable More tools, View source, Settings
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                // More tools expandable item
                MenuRowItem(
                    icon = Icons.Rounded.Build,
                    title = "More tools",
                    trailingIcon = if (isMoreToolsExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                    onClick = { isMoreToolsExpanded = !isMoreToolsExpanded }
                )

                AnimatedVisibility(
                    visible = isMoreToolsExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        MenuRowItem(
                            icon = Icons.Rounded.Search,
                            title = "Search on site",
                            onClick = onSearchOnSite
                        )
                        MenuRowItem(
                            icon = Icons.Rounded.Print,
                            title = "Print page to PDF",
                            onClick = onPrintPdf
                        )
                        MenuRowItem(
                            icon = Icons.Rounded.SaveAlt,
                            title = "Save page",
                            onClick = onSavePage
                        )
                        MenuRowItem(
                            icon = Icons.Rounded.Share,
                            title = "Share link",
                            onClick = onShareLink
                        )
                    }
                }

                MenuRowItem(
                    icon = Icons.Rounded.Code,
                    title = "View source",
                    onClick = onViewSource
                )
                MenuRowItem(
                    icon = Icons.Rounded.Settings,
                    title = "Settings",
                    onClick = onOpenSettings
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CircularIconButton(
    icon: ImageVector,
    contentDescription: String,
    enabled: Boolean = true,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = if (enabled) tint else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
        modifier = Modifier
            .size(52.dp)
            .bouncyClickable(scaleDown = 0.84f, enabled = enabled, onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = contentDescription, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
private fun MenuRowItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    trailingIcon: ImageVector? = null,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier
            .fillMaxWidth()
            .bouncyClickable(scaleDown = 0.95f, onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (!subtitle.isNullOrBlank()) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (trailingIcon != null) {
                Icon(
                    trailingIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
