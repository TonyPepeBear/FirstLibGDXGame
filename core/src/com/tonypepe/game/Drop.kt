package com.tonypepe.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Drop : Game() {
    lateinit var batch: SpriteBatch
    lateinit var font: BitmapFont

    override fun create() {
        batch = SpriteBatch()
        font = BitmapFont()
        setScreen(MainMenuScreen(this))
    }

    override fun dispose() {
        super.dispose()
        batch.dispose()
        font.dispose()
    }
}
