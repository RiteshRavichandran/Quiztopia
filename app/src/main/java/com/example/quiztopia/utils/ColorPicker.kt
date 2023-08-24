package com.example.quiztopia.utils

import kotlin.random.Random

object ColorPicker {
    var colors = arrayOf(
        "#ED0A3F",
        "#FF7F49",
        "#0066FF",
        "#3F26BF",
        "#703791",
        "#F77474",
        "#F7BD40",
        "#98D8AA",
        "#338F85",
        "#CF3E90",
        "#CC33CC",
        "#682EBF"
    )
    var currentColorIndex = 0

    fun getColor(): String {
        currentColorIndex = (currentColorIndex + 1) % colors.size
        return colors[currentColorIndex]
    }
}