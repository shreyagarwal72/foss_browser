package com.petal.browser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Refined Material 3 Expressive Options Sheet / Dialog for Petal Browser featuring
 * Stride IconSwitches, quick action grid, rounded 32.dp sheet container, and bouncy physics.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetalOptionsSheet(
    isDesktopSite: Boolean,
    onDesktopSiteChange: (Boolean) -> Unit,
    isIncognito: Boolean,
    onIncognitoChange: (Boolean) -> Unit,
    onNewTab: () -> Unit,
    onBookmarks: () -> Unit,
    onHistory: () -> Unit,
    onDownloads: () -> Unit,
    onFindInPage: () -> Unit,
    onShare: () -> Unit,
    onSettings: () -> Unit,
    onDismiss: () -> Unit,
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
                .padding(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "Browser Options",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Top Quick Grid Action Tiles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OptionTile(Icons.Rounded.Add, "New Tab", Modifier.weight(1f)) {
                    onNewTab()
                    onDismiss()
                }
                OptionTile(Icons.Rounded.Bookmarks, "Bookmarks", Modifier.weight(1f)) {
                    onBookmarks()
                    onDismiss()
                }
                OptionTile(Icons.Rounded.History, "History", Modifier.weight(1f)) {
                    onHistory()
                    onDismiss()
                }
                OptionTile(Icons.Rounded.Downloading, "Downloads", Modifier.weight(1f)) {
                    onDownloads()
                    onDismiss()
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            // Toggles with Stride IconSwitches
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Desktop Site Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Rounded.DesktopWindows, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Column {
                            Text("Desktop Mode", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                            Text("Request desktop version of websites", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    IconSwitch(
                        checked = isDesktopSite,
                        icon = Icons.Rounded.DesktopWindows,
                        onCheckedChange = onDesktopSiteChange
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

                // Incognito Mode Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Rounded.Security, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Column {
                            Text("Private Browsing", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                            Text("Don't save history or cookies", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    IconSwitch(
                        checked = isIncognito,
                        icon = Icons.Rounded.Security,
                        onCheckedChange = onIncognitoChange
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            // List Actions
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                OptionRowItem(Icons.Rounded.Search, "Find in Page") {
                    onFindInPage()
                    onDismiss()
                }
                OptionRowItem(Icons.Rounded.Share, "Share Web Page") {
                    onShare()
                    onDismiss()
                }
                OptionRowItem(Icons.Rounded.Settings, "Browser Settings") {
                    onSettings()
                    onDismiss()
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun OptionTile(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = modifier.height(72.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(6.dp)
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
            Spacer(Modifier.height(4.dp))
            Text(label, style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp), fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun OptionRowItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = androidx.compose.ui.graphics.Color.Transparent,
        modifier = Modifier.fillMaxWidth().height(48.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(22.dp))
            Text(label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium), color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
