package com.example.chess.model

fun Chessboard.basicPrint() {
    println("+-+-+-+-+-+-+-+-+")
    this.rows.reversed().forEach { i ->
        println("|" + this.columns.map { j ->
            this[j + i].piece?.basicRepr ?: "-"
        }.joinToString(separator = "|") + "|")
    }
    println("+-+-+-+-+-+-+-+-+")
}