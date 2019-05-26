package com.tonypepe.game

import com.badlogic.gdx.Gdx

fun Any.logger(function: String, message: Any?) {
    Gdx.app.log(this::class.java.simpleName, "$function: $message")
}
