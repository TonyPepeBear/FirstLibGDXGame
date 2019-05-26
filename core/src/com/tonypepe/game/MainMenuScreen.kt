package com.tonypepe.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera

class MainMenuScreen(val game: Drop) : Screen {
    var camera: OrthographicCamera = OrthographicCamera().apply { setToOrtho(false, 800F, 480F) }

    override fun hide() {}

    override fun show() {}

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0F, 0F, 0.2F, 1F)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()
        game.batch.apply {
            projectionMatrix = camera.combined
            begin()
            with(game) {
                font.draw(this@apply, "Welcome to Drop!!!", 100F, 150F)
                font.draw(this@apply, "Tap anywhere to begin!", 100F, 100F)
            }
            end()
        }
        if (Gdx.input.isTouched) {
            game.screen = GameScreen(game)
            logger("render", "Star game")
            dispose()
        }
    }

    override fun pause() {}

    override fun resume() {}

    override fun resize(width: Int, height: Int) {}

    override fun dispose() {}
}