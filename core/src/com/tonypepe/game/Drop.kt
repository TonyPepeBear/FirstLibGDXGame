package com.tonypepe.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import java.awt.Rectangle

class Drop : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var img: Texture
    private lateinit var dropImege: Texture
    private lateinit var bucketImage: Texture
    private lateinit var dropSound: Sound
    private lateinit var rainMusic: Music
    private lateinit var camera: OrthographicCamera
    private lateinit var bucket: Rectangle

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
            x = 800 / 2 - 64 / 2
            y = 20
            width = 64
            height = 64
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
            end()
        }
        if (Gdx.input.isTouched) {
            val touchPos = Vector3()
            touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            camera.unproject(touchPos)
            bucket.x = (touchPos.x - 64 / 2).toInt()
        }
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
    }
}
