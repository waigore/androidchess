package com.example.chess

import com.example.chess.model.*
import org.junit.Before
import org.junit.Test

class CastleTest {
    lateinit var chessboard: Chessboard

    @Before
    fun setup() {
        chessboard = Chessboard()
    }

    @Test
    fun test_validCastles() {
        chessboard.clear()
        chessboard.placePiece(Piece.WhiteKing, "E5")
        assert(!chessboard.castleValid(Side.White, CastleType.Kingside))

        chessboard.movePiece("E5", "E1")
        chessboard.placePiece(Piece.WhiteRook, "H1")
        chessboard.placePiece(Piece.WhiteRook, "A1")
        assert(chessboard.castleValid(Side.White, CastleType.Kingside))
        assert(chessboard.castleValid(Side.White, CastleType.Queenside))

        chessboard.placePiece(Piece.BlackQueen, "F8")
        assert(!chessboard.castleValid(Side.White, CastleType.Kingside))
        assert(chessboard.castleValid(Side.White, CastleType.Queenside))

        chessboard.placePiece(Piece.BlackBishop, "E8")
        chessboard.movePiece("F8", "B8")
        assert(chessboard.castleValid(Side.White, CastleType.Kingside))
        assert(!chessboard.castleValid(Side.White, CastleType.Queenside))
    }
}