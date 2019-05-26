package com.tonypepe.game.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.tonypepe.game.Drop

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration().apply {
            title = "TonyPepe"
            width = 800
            height = 400
            resizable = false
        }
        LwjglApplication(Drop(), config)
    }
}
