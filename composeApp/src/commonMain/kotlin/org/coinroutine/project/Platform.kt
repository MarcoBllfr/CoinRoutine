package org.coinroutine.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform