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

fun ChessGame.formatCapturedPieces(side: Side): String {
    val capturedPieces = if (side == Side.White) this.whiteCapturedPieces else this.blackCapturedPieces
    return capturedPieces.keys.sortedByDescending { it.materialValue }.filter {capturedPieces[it]!! > 0}.map { pieceType ->
        List(capturedPieces[pieceType]!!) {
            Piece(pieceType, side).repr
        }.joinToString(separator = "")
    }.joinToString(separator = " ")
}