/*
 * PetalHomeScreen.kt
 * ─────────────────────────────────────────────────────────────────────────
 * Material 3 Expressive home ("new tab") screen for Petal Browser.
 */

package com.petal.browser.compose.home

import android.content.Context
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Downloading
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Tab
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

// ── 1. Color tokens ────

private object PetalColorsLight {
    val primary = Color(0xFF676013)
    val onPrimary = Color(0xFFFFFFFF)
    val primaryContainer = Color(0xFFEFE58B)
    val onPrimaryContainer = Color(0xFF1F1C00)
    val secondaryContainer = Color(0xFFEAE3BD)
    val onSecondaryContainer = Color(0xFF1E1C05)
    val tertiaryContainer = Color(0xFFD3EC9E)
    val onTertiaryContainer = Color(0xFF141F00)
    val background = Color(0xFFFEF9EB)
    val onBackground = Color(0xFF1D1C14)
    val surfaceContainer = Color(0xFFF3EEE0)
    val surfaceContainerHigh = Color(0xFFEDE8DA)
    val surfaceContainerHighest = Color(0xFFE7E2D5)
    val outline = Color(0xFF7A7768)
    val outlineVariant = Color(0xFFCBC7B5)
}

private object PetalColorsDark {
    val primary = Color(0xFFC8CC78)
    val onPrimary = Color(0xFF313300)
    val primaryContainer = Color(0xFF3C3F00)
    val onPrimaryContainer = Color(0xFFD1D480)
    val secondaryContainer = Color(0xFF48473B)
    val onSecondaryContainer = Color(0xFFE5E2D9)
    val tertiaryContainer = Color(0xFFA5653C)
    val onTertiaryContainer = Color(0xFFFFFFFF)
    val background = Color(0xFF14140E)
    val onBackground = Color(0xFFE5E2D9)
    val surfaceContainer = Color(0xFF201F17)
    val surfaceContainerHigh = Color(0xFF2A2A1F)
    val surfaceContainerHighest = Color(0xFF353429)
    val outline = Color(0xFF929181)
    val outlineVariant = Color(0xFF48483A)
}

@Composable
fun PetalTheme(
    darkTheme: Boolean = androidx.compose.foundation.isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = PetalColorsDark.primary,
            onPrimary = PetalColorsDark.onPrimary,
            primaryContainer = PetalColorsDark.primaryContainer,
            onPrimaryContainer = PetalColorsDark.onPrimaryContainer,
            secondaryContainer = PetalColorsDark.secondaryContainer,
            onSecondaryContainer = PetalColorsDark.onSecondaryContainer,
            tertiaryContainer = PetalColorsDark.tertiaryContainer,
            onTertiaryContainer = PetalColorsDark.onTertiaryContainer,
            background = PetalColorsDark.background,
            onBackground = PetalColorsDark.onBackground,
            surfaceContainer = PetalColorsDark.surfaceContainer,
            surfaceContainerHigh = PetalColorsDark.surfaceContainerHigh,
            surfaceContainerHighest = PetalColorsDark.surfaceContainerHighest,
            outline = PetalColorsDark.outline,
            outlineVariant = PetalColorsDark.outlineVariant,
        )
    } else {
        lightColorScheme(
            primary = PetalColorsLight.primary,
            onPrimary = PetalColorsLight.onPrimary,
            primaryContainer = PetalColorsLight.primaryContainer,
            onPrimaryContainer = PetalColorsLight.onPrimaryContainer,
            secondaryContainer = PetalColorsLight.secondaryContainer,
            onSecondaryContainer = PetalColorsLight.onSecondaryContainer,
            tertiaryContainer = PetalColorsLight.tertiaryContainer,
            onTertiaryContainer = PetalColorsLight.onTertiaryContainer,
            background = PetalColorsLight.background,
            onBackground = PetalColorsLight.onBackground,
            surfaceContainer = PetalColorsLight.surfaceContainer,
            surfaceContainerHigh = PetalColorsLight.surfaceContainerHigh,
            surfaceContainerHighest = PetalColorsLight.surfaceContainerHighest,
            outline = PetalColorsLight.outline,
            outlineVariant = PetalColorsLight.outlineVariant,
        )
    }
    MaterialTheme(colorScheme = colors, content = content)
}

// ── 2. Data model ───────────────────────────────────────────────────────

data class PetalShortcut(
    val label: String,
    val url: String,
    val letter: String = label.take(1).uppercase(),
)

val defaultPetalShortcuts = listOf(
    PetalShortcut("Wiki", "https://wikipedia.org"),
    PetalShortcut("GitHub", "https://github.com"),
    PetalShortcut("DuckDuckGo", "https://duckduckgo.com"),
    PetalShortcut("Reddit", "https://reddit.com"),
    PetalShortcut("News", "https://news.ycombinator.com"),
)

private val petalShapes: List<Shape> = listOf(
    RoundedCornerShape(28.dp),
    RoundedCornerShape(topStart = 28.dp, topEnd = 12.dp, bottomEnd = 28.dp, bottomStart = 12.dp),
    RoundedCornerShape(topStart = 12.dp, topEnd = 28.dp, bottomEnd = 12.dp, bottomStart = 28.dp),
    CutCornerShape(topStart = 20.dp, bottomEnd = 20.dp),
    RoundedCornerShape(24.dp),
)

// ── 3. Java Interop Callback Interface ──────────────────────────────────

interface PetalHomeActionHandler {
    fun onSearch(query: String)
    fun onOpenUrl(url: String)
    fun onAddShortcut()
    fun onNewTab()
    fun onOpenBookmarks()
    fun onOpenHistory()
    fun onOpenDownloads()
    fun onOpenSettings()
}

// Helper object for Java caller to instantiate ComposeView effortlessly
object PetalComposeBridge {
    @JvmStatic
    fun createComposeHomeView(
        context: Context,
        handler: PetalHomeActionHandler
    ): ComposeView {
        return ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                PetalTheme {
                    PetalHomeScreen(
                        onSearch = { handler.onSearch(it) },
                        onOpenShortcut = { handler.onOpenUrl(it.url) },
                        onAddShortcut = { handler.onAddShortcut() },
                        onNewTab = { handler.onNewTab() },
                        onOpenBookmarks = { handler.onOpenBookmarks() },
                        onOpenHistory = { handler.onOpenHistory() },
                        onOpenDownloads = { handler.onOpenDownloads() },
                        onOpenSettings = { handler.onOpenSettings() }
                    )
                }
            }
        }
    }
}

// ── 4. Screen Composable ────────────────────────────────────────────────

@Composable
fun PetalHomeScreen(
    greetingName: String? = null,
    shortcuts: List<PetalShortcut> = defaultPetalShortcuts,
    onSearch: (String) -> Unit = {},
    onOpenShortcut: (PetalShortcut) -> Unit = {},
    onAddShortcut: () -> Unit = {},
    onNewTab: () -> Unit = {},
    onOpenBookmarks: () -> Unit = {},
    onOpenHistory: () -> Unit = {},
    onOpenDownloads: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = onNewTab,
                shape = RoundedCornerShape(24.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Icon(Icons.Rounded.Tab, contentDescription = "New tab")
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(28.dp))

            Text(
                text = greeting(greetingName),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-0.5).sp,
                ),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = "Petal",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(24.dp))

            PetalSearchBar(onSearch = onSearch)

            Spacer(Modifier.height(36.dp))

            PetalBloom(
                shortcuts = shortcuts,
                onOpenShortcut = onOpenShortcut,
                onAddShortcut = onAddShortcut,
            )

            Spacer(Modifier.height(36.dp))

            QuickActionRow(
                onOpenBookmarks = onOpenBookmarks,
                onOpenHistory = onOpenHistory,
                onOpenDownloads = onOpenDownloads,
                onOpenSettings = onOpenSettings,
            )

            Spacer(Modifier.height(96.dp))
        }
    }
}

private fun greeting(name: String?): String {
    val base = when (java.time.LocalTime.now().hour) {
        in 5..11 -> "Good morning"
        in 12..17 -> "Good afternoon"
        else -> "Good evening"
    }
    return if (name.isNullOrBlank()) base else "$base, $name"
}

@Composable
private fun PetalSearchBar(onSearch: (String) -> Unit) {
    Surface(
        onClick = { onSearch("") },
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 20.dp),
        ) {
            Icon(
                Icons.Rounded.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.width(12.dp))
            Text(
                "Search or type a URL",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
            Icon(
                Icons.Rounded.Mic,
                contentDescription = "Voice search",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun PetalBloom(
    shortcuts: List<PetalShortcut>,
    onOpenShortcut: (PetalShortcut) -> Unit,
    onAddShortcut: () -> Unit,
) {
    val petalSize = 64.dp
    val budSize = 56.dp
    val ringRadius = 92.dp

    RadialLayout(
        radius = ringRadius,
        modifier = Modifier
            .fillMaxWidth()
            .height(ringRadius * 2 + petalSize),
    ) {
        Surface(
            onClick = onAddShortcut,
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.size(budSize),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Rounded.Add, contentDescription = "Add shortcut")
            }
        }

        shortcuts.take(6).forEachIndexed { index, shortcut ->
            val shape = petalShapes[index % petalShapes.size]
            Surface(
                onClick = { onOpenShortcut(shortcut) },
                shape = shape,
                color = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(petalSize),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Text(
                        shortcut.letter,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    )
                }
            }
        }
    }
    Spacer(Modifier.height(8.dp))
    val labels = shortcuts.take(6).joinToString("  ·  ") { it.label }
    Text(
        labels,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.outline,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun RadialLayout(
    radius: Dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints.copy(minWidth = 0, minHeight = 0)) }
        val width = constraints.maxWidth
        val height = radius.roundToPx() * 2 + (placeables.firstOrNull()?.height ?: 0)
        layout(width, height) {
            if (placeables.isEmpty()) return@layout
            val centerX = width / 2
            val centerY = height / 2
            val center = placeables.first()
            center.place(centerX - center.width / 2, centerY - center.height / 2)

            val petals = placeables.drop(1)
            val step = 360f / petals.size.coerceAtLeast(1)
            petals.forEachIndexed { i, placeable ->
                val angleDeg = -90f + step * i
                val angleRad = Math.toRadians(angleDeg.toDouble())
                val x = centerX + (radius.roundToPx() * cos(angleRad)).roundToInt() - placeable.width / 2
                val y = centerY + (radius.roundToPx() * sin(angleRad)).roundToInt() - placeable.height / 2
                placeable.place(x, y)
            }
        }
    }
}

@Composable
private fun QuickActionRow(
    onOpenBookmarks: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenDownloads: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        QuickAction(Icons.Rounded.Bookmarks, "Bookmarks", Modifier.weight(1f), onOpenBookmarks)
        QuickAction(Icons.Rounded.History, "History", Modifier.weight(1f), onOpenHistory)
        QuickAction(Icons.Rounded.Downloading, "Downloads", Modifier.weight(1f), onOpenDownloads)
        QuickAction(Icons.Rounded.Settings, "Settings", Modifier.weight(1f), onOpenSettings)
    }
}

@Composable
private fun QuickAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = modifier.heightIn(min = 76.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(vertical = 10.dp),
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(6.dp))
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Preview(showBackground = true, name = "Petal — light")
@Composable
private fun PetalHomeScreenLightPreview() {
    PetalTheme(darkTheme = false) { PetalHomeScreen() }
}

@Preview(showBackground = true, name = "Petal — dark")
@Composable
private fun PetalHomeScreenDarkPreview() {
    PetalTheme(darkTheme = true) { PetalHomeScreen(greetingName = "Alex") }
}
