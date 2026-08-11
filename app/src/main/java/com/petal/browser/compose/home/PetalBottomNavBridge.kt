package com.petal.browser.compose.home

import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
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
        selectedTab: PetalNavTab,
        tabCount: Int,
        handler: PetalBottomNavHandler
    ) {
        composeView.apply {
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
