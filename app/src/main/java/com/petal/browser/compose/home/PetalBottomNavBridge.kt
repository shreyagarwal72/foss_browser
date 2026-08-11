package com.petal.browser.compose.home

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import com.petal.browser.ui.components.PetalBottomNavBar
import com.petal.browser.ui.components.PetalNavTab
import com.petal.browser.ui.theme.PetalExpressiveTheme

interface PetalBottomNavHandler {
    fun onHomeClick()
    fun onNewTabClick()
    fun onTabsClick()
    fun onMenuClick()
}

object PetalBottomNavBridge {
    @JvmStatic
    fun bindBottomNav(
        composeView: ComposeView,
        activity: ComponentActivity,
        selectedTab: PetalNavTab,
        tabCount: Int,
        handler: PetalBottomNavHandler
    ) {
        composeView.apply {
            ViewTreeLifecycleOwner.set(this, activity)
            ViewTreeSavedStateRegistryOwner.set(this, activity)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                PetalExpressiveTheme {
                    PetalBottomNavBar(
                        selectedTab = selectedTab,
                        tabCount = tabCount,
                        onHomeClick = { handler.onHomeClick() },
                        onNewTabClick = { handler.onNewTabClick() },
                        onTabsClick = { handler.onTabsClick() },
                        onMenuClick = { handler.onMenuClick() }
                    )
                }
            }
        }
    }
}
