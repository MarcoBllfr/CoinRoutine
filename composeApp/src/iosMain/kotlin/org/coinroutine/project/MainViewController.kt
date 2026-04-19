package org.coinroutine.project

import androidx.compose.ui.window.ComposeUIViewController
import org.coinroutine.project.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }