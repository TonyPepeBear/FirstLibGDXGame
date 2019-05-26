package com.tonypepe.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.TimeUtils

class Drop : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var img: Texture
    private lateinit var dropImege: Texture
    private lateinit var bucketImage: Texture
    private lateinit var dropSound: Sound
    private lateinit var rainMusic: Music
    private lateinit var camera: OrthographicCamera
    private lateinit var bucket: Rectangle
    private val raindrops = arrayListOf<Rectangle>()
    private var lastDropTime = 0L

    override fun create() {
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")
        dropImege = Texture(Gdx.files.internal("droplet.png"))
        bucketImage = Texture(Gdx.files.internal("bucket.png"))
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"))
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"))
        rainMusic.isLooping = true
        rainMusic.play()
        camera = OrthographicCamera().apply { setToOrtho(false, 800f, 480f) }
        bucket = Rectangle().apply {
            x = (800 / 2 - 64 / 2).toFloat()
            y = 20F
            width = 64F
            height = 64F
        }
    }

    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()
        batch.apply {
            projectionMatrix = camera.combined
            begin()
            draw(bucketImage, bucket.x.toFloat(), bucket.y.toFloat())
            raindrops.forEach {
                draw(dropImege, it.x.toFloat(), it.y.toFloat())
            }
            end()
        }
        moveBucket()
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop()
        val removeList = mutableListOf<Rectangle>()
        raindrops.forEach {
            it.y -= (200 * Gdx.graphics.deltaTime).toInt()
            if (it.y + 64 < 0) removeList.add(it)
            else if (it.overlaps(bucket)) {
                dropSound.play()
                removeList.add(it)
            }
        }
        raindrops.removeAll(removeList)
    }

    private fun moveBucket() {
        if (Gdx.input.isTouched) {
            val touchPos = Vector3()
            touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            camera.unproject(touchPos)
            bucket.x = touchPos.x - 64 / 2
        }
        when {
            Gdx.input.isKeyPressed(Input.Keys.LEFT) -> {
                bucket.x -= (200 * Gdx.graphics.deltaTime).toInt()
            }
            Gdx.input.isKeyPressed(Input.Keys.RIGHT) -> {
                bucket.x += (200 * Gdx.graphics.deltaTime).toInt()
            }
        }
        if (bucket.x < 0) bucket.x = 0F else if (bucket.x > 800 - 64) bucket.x = (800 - 64).toFloat()
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
    }

    private fun spawnRaindrop() {
        val raindrop = Rectangle().apply {
            x = MathUtils.random(0, 800 - 64).toFloat()
            y = 480F
            width = 64F
            height = 64F
        }
        raindrops.add(raindrop)
        lastDropTime = TimeUtils.nanoTime()
    }
}
