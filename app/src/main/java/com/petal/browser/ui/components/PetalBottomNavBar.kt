package com.petal.browser.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class PetalNavTab {
    HOME, TABS, NEW_TAB, MENU
}

/**
 * Floating bottom navigation bar modeled after StrideFloatingNav,
 * featuring Home, Chrome Android-style live Tab Counter & Switcher, New Tab (+), and Menu.
 */
@Composable
fun PetalBottomNavBar(
    selectedTab: PetalNavTab,
    tabCount: Int,
    onHomeClick: () -> Unit,
    onTabsClick: () -> Unit,
    onNewTabClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shadowElevation = 14.dp,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // 1st Item: Home
            NavItemPill(
                selected = selectedTab == PetalNavTab.HOME,
                label = "Home",
                onClick = onHomeClick
            ) { color ->
                Icon(
                    imageVector = Icons.Rounded.Home,
                    contentDescription = "Home",
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }

            // 2nd Item: Live Tab Switcher (Chrome Android Style Badge)
            NavItemPill(
                selected = selectedTab == PetalNavTab.TABS,
                label = "Tabs ($tabCount)",
                onClick = onTabsClick
            ) { color ->
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .border(
                            width = 2.dp,
                            color = color,
                            shape = RoundedCornerShape(6.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (tabCount > 99) "99+" else tabCount.toString(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = color,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // 3rd Item: New Tab (+)
            NavItemPill(
                selected = selectedTab == PetalNavTab.NEW_TAB,
                label = "New",
                onClick = onNewTabClick
            ) { color ->
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "New Tab",
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }

            // 4th Item: Menu
            NavItemPill(
                selected = selectedTab == PetalNavTab.MENU,
                label = "Menu",
                onClick = onMenuClick
            ) { color ->
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = "Menu",
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun NavItemPill(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
    iconContent: @Composable (Color) -> Unit
) {
    val pop = remember { Animatable(1f) }

    LaunchedEffect(selected) {
        if (selected) {
            pop.snapTo(0.7f)
            pop.animateTo(
                1f,
                spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium,
                ),
            )
        }
    }

    val contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    val containerColor = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent

    Surface(
        shape = CircleShape,
        color = containerColor,
        modifier = Modifier
            .bouncyClickable(scaleDown = 0.88f, onClick = onClick)
            .clip(CircleShape)
            .animateContentSize(),
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = if (selected) 14.dp else 8.dp,
                vertical = 8.dp,
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Box(
                modifier = Modifier.graphicsLayer {
                    scaleX = pop.value
                    scaleY = pop.value
                }
            ) {
                iconContent(contentColor)
            }

            AnimatedVisibility(
                visible = selected,
                enter = expandHorizontally() + fadeIn(),
                exit = shrinkHorizontally() + fadeOut(),
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = contentColor,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
