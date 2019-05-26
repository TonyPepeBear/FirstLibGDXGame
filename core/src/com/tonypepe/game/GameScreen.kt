package com.tonypepe.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.TimeUtils

class GameScreen(val game: Drop) : Screen {
    var dropImage = Texture(Gdx.files.internal("droplet.png"))
    var bucketImage = Texture(Gdx.files.internal("bucket.png"))
    var dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"))
    var rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3")).apply { isLooping = true }
    var camera = OrthographicCamera().apply { setToOrtho(false, 800F, 480F) }
    var bucket = Rectangle().apply {
        x = (800 / 2 - 64 / 2).toFloat()
        y = 20F
        width = 64F
        height = 64F
    }
    var rainDrops = arrayListOf<Rectangle>()
    var lastDropTime = 0L
    var dropsGathered = 0L

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0F, 0F, 0.2F, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()
        game.batch.projectionMatrix = camera.combined
        with(game.batch) {
            begin()
            game.font.draw(this, "Drops Collected: $dropsGathered", 0F, 480F)
            draw(bucketImage, bucket.x, bucket.y, bucket.width, bucket.height)
            rainDrops.forEach {
                draw(dropImage, it.x, it.y)
            }
            end()
        }
        moveBucket()
    }

    private fun moveBucket() {
        if (Gdx.input.isTouched) {
            val touchPosition = Vector3()
            touchPosition.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0F)
            camera.unproject(touchPosition)
            bucket.x = touchPosition.x - 64 / 2
        }
        if (bucket.x < 0) bucket.x = 0F
        if (bucket.x > 800 - 64) bucket.x = (800 - 64).toFloat()
        if (TimeUtils.millis() - lastDropTime > 1000) spawnRaindrop()
        val removeList = mutableListOf<Rectangle>()
        rainDrops.forEach {
            it.y -= 200 * Gdx.graphics.deltaTime
            if (it.y + 64 < 0) removeList.add(it)
            else if (it.overlaps(bucket)) {
                dropsGathered++
                logger("getDrop", dropsGathered)
                dropSound.play()
                removeList.add(it)
            }
        }
        if (removeList.isNotEmpty()) {
            logger("removeList", removeList)
        }
        rainDrops.removeAll(removeList)
    }

    private fun spawnRaindrop() {
        logger("spawnRaindrop", "size: ${rainDrops.size}")
        rainDrops.add(
                Rectangle().apply {
                    x = MathUtils.random(0, 800 - 64).toFloat()
                    y = 480F
                    width = 64F
                    height = 64F
                }
        )
        lastDropTime = TimeUtils.millis()
    }

    override fun hide() {}

    override fun show() {
        rainMusic.play()
    }

    override fun pause() {}

    override fun resume() {}

    override fun resize(width: Int, height: Int) {}

    override fun dispose() {
        dropImage.dispose()
        bucketImage.dispose()
        dropSound.dispose()
        rainMusic.dispose()
    }
}
