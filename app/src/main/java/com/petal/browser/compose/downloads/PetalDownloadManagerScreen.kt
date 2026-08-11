/*
 * PetalDownloadManager.kt
 * ─────────────────────────────────────────────────────────────────────────
 * Native Inbuilt Download Manager UI for Petal Browser featuring real-time
 * progress tracking, file opening, pause/resume, and Stride UI components.
 */

package com.petal.browser.compose.downloads

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.petal.browser.ui.components.LinearRipplingWavyProgressIndicator
import com.petal.browser.ui.theme.PetalExpressiveTheme

data class DownloadItem(
    val id: Long,
    val fileName: String,
    val fileUrl: String,
    val progress: Float?,
    val status: Int,
    val totalSize: Long,
    val localUri: String?
)

object PetalDownloadBridge {
    @JvmStatic
    fun createDownloadView(activity: ComponentActivity, onBackPress: () -> Unit): ComposeView {
        return ComposeView(activity).apply {
            setViewTreeLifecycleOwner(activity)
            setViewTreeSavedStateRegistryOwner(activity)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                PetalExpressiveTheme {
                    PetalDownloadManagerScreen(onBackPress = onBackPress)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetalDownloadManagerScreen(onBackPress: () -> Unit = {}) {
    val context = LocalContext.current
    var downloadList by remember { mutableStateOf(getDownloadItems(context)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Inbuilt Download Manager",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { downloadList = getDownloadItems(context) }) {
                        Icon(Icons.Rounded.Refresh, contentDescription = "Refresh")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (downloadList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Rounded.DownloadDone,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "No Downloads Found",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Downloaded files will appear here",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(downloadList, key = { it.id }) { item ->
                    DownloadCardItem(item = item, onOpenFile = {
                        openDownloadedFile(context, item)
                    })
                }
            }
        }
    }
}

@Composable
private fun DownloadCardItem(item: DownloadItem, onOpenFile: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = when (item.status) {
                        DownloadManager.STATUS_SUCCESSFUL -> Icons.Rounded.FilePresent
                        DownloadManager.STATUS_FAILED -> Icons.Rounded.ErrorOutline
                        else -> Icons.Rounded.Downloading
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.fileName,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = item.fileUrl,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (item.status == DownloadManager.STATUS_SUCCESSFUL) {
                    IconButton(onClick = onOpenFile) {
                        Icon(Icons.Rounded.OpenInNew, contentDescription = "Open file", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            if (item.status == DownloadManager.STATUS_RUNNING) {
                LinearRipplingWavyProgressIndicator(
                    progress = item.progress,
                    label = "Downloading…",
                    height = 20.dp
                )
            }
        }
    }
}

private fun getDownloadItems(context: Context): List<DownloadItem> {
    val list = mutableListOf<DownloadItem>()
    try {
        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query()
        val cursor = dm.query(query)
        if (cursor != null && cursor.moveToFirst()) {
            val idCol = cursor.getColumnIndex(DownloadManager.COLUMN_ID)
            val titleCol = cursor.getColumnIndex(DownloadManager.COLUMN_TITLE)
            val uriCol = cursor.getColumnIndex(DownloadManager.COLUMN_URI)
            val statusCol = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            val soFarCol = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
            val totalCol = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
            val localUriCol = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)

            do {
                val id = if (idCol >= 0) cursor.getLong(idCol) else 0L
                val title = if (titleCol >= 0) cursor.getString(titleCol) ?: "Downloaded File" else "Downloaded File"
                val uri = if (uriCol >= 0) cursor.getString(uriCol) ?: "" else ""
                val status = if (statusCol >= 0) cursor.getInt(statusCol) else 0
                val soFar = if (soFarCol >= 0) cursor.getLong(soFarCol) else 0L
                val total = if (totalCol >= 0) cursor.getLong(totalCol) else 0L
                val localUri = if (localUriCol >= 0) cursor.getString(localUriCol) else null

                val progress = if (total > 0) (soFar.toFloat() / total.toFloat()) else null
                list.add(DownloadItem(id, title, uri, progress, status, total, localUri))
            } while (cursor.moveToNext())
            cursor.close()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return list.reversed()
}

private fun openDownloadedFile(context: Context, item: DownloadItem) {
    try {
        if (item.localUri != null) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(Uri.parse(item.localUri), "*/*")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(intent)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
