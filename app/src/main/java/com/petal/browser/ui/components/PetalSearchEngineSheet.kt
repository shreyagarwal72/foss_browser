/*
 * PetalSearchEngineSheet.kt
 * ─────────────────────────────────────────────────────────────────────────
 * Material 3 Search Engine Selection Modal & Preference Sheet for Petal Browser.
 * Prompts user on first startup and allows switching default search engines anytime in Settings.
 */

package com.petal.browser.ui.components

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import androidx.preference.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.petal.browser.ui.theme.PetalExpressiveTheme

data class SearchEngineItem(
    val index: Int,
    val name: String,
    val description: String,
    val url: String
)

val availableSearchEngines = listOf(
    SearchEngineItem(0, "Google", "Fast and comprehensive global search", "https://google.com"),
    SearchEngineItem(1, "DuckDuckGo", "Privacy search without tracking", "https://duckduckgo.com"),
    SearchEngineItem(2, "Startpage", "Google results with total privacy protection", "https://startpage.com"),
    SearchEngineItem(3, "Brave Search", "Independent, privacy-focused search index", "https://search.brave.com"),
    SearchEngineItem(4, "Bing", "Microsoft intelligent search & discovery", "https://bing.com"),
    SearchEngineItem(5, "SearXNG", "Open-source decentralized metasearch", "https://searx.be"),
    SearchEngineItem(6, "Qwant", "European privacy search engine", "https://qwant.com"),
    SearchEngineItem(7, "Ecosia", "Search engine that plants trees", "https://ecosia.org")
)

object PetalSearchEngineBridge {
    @JvmStatic
    fun showSearchEngineDialog(activity: ComponentActivity, onDismiss: Runnable? = null) {
        try {
            val dialog = BottomSheetDialog(activity)
            val composeView = ComposeView(activity).apply {
                setViewTreeLifecycleOwner(activity)
                setViewTreeSavedStateRegistryOwner(activity)
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    PetalExpressiveTheme {
                        PetalSearchEngineSheet(
                            onSelectEngine = { index ->
                                val sp = PreferenceManager.getDefaultSharedPreferences(activity)
                                sp.edit()
                                    .putString("sp_search_engine", index.toString())
                                    .putBoolean("sp_search_engine_chosen", true)
                                    .apply()
                                try { dialog.dismiss() } catch (ignored: Exception) {}
                                onDismiss?.run()
                            },
                            onDismiss = {
                                try { dialog.dismiss() } catch (ignored: Exception) {}
                                onDismiss?.run()
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
fun PetalSearchEngineSheet(
    onSelectEngine: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sp = remember { PreferenceManager.getDefaultSharedPreferences(context) }
    var selectedIndex by remember {
        val current = sp.getString("sp_search_engine", "0") ?: "0"
        mutableIntStateOf(current.toIntOrNull() ?: 0)
    }

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
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Column {
                    Text(
                        text = "Choose Default Search Engine",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Selected engine will handle omnibox & query searches",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            // Search Engines Cards List
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                availableSearchEngines.forEach { engine ->
                    val isSelected = engine.index == selectedIndex
                    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    val bgColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surfaceContainer

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = bgColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                selectedIndex = engine.index
                                onSelectEngine(engine.index)
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = engine.name,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = engine.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    selectedIndex = engine.index
                                    onSelectEngine(engine.index)
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
