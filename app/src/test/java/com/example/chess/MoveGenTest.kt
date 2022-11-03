package com.example.chess

import com.example.chess.model.*
import org.junit.Before
import org.junit.Test

class MoveGenTest {
    lateinit var chessboard: Chessboard

    @Before
    fun setup() {
        chessboard = Chessboard()
    }

    @Test
    fun test_generateKnightMoves() {
        val moves1 = generateKnightMoves("E5")
        assert(moves1 == listOf("F7", "G6", "G4", "F3", "D3", "C4", "C6", "D7"))

        val moves2 = generateKnightMoves("A1")
        assert(moves2 == listOf("B3", "C2"))

        val moves3 = generateKnightMoves("G1")
        assert(moves3 == listOf("H3", "E2", "F3"))
    }

    @Test
    fun test_generateBishopMoves() {
        val moves1 = generateBishopMoves("E5")
        assert(moves1 == listOf("F6", "G7", "H8", "F4", "G3", "H2", "D6", "C7", "B8", "D4", "C3", "B2", "A1"))
        val moves2 = generateBishopMoves("A8")
        assert(moves2 == listOf("B7", "C6", "D5", "E4", "F3", "G2", "H1"))
    }

    @Test
    fun test_generateRookMoves() {
        val moves1 = generateRookMoves("E5")
        assert(moves1 == listOf("F5", "G5", "H5", "D5", "C5", "B5", "A5", "E6", "E7", "E8", "E4", "E3", "E2", "E1"))
        val moves2 = generateRookMoves("A1")
        assert(moves2 == listOf("B1", "C1", "D1", "E1", "F1", "G1", "H1", "A2", "A3", "A4", "A5", "A6", "A7", "A8"))

    }

    @Test
    fun test_generateKingMoves() {
        val moves1 = generateKingMoves("E5")
        assert(moves1 == listOf("F6", "F5", "F4", "E4", "D4", "D5", "D6", "E6"))
        val moves2 = generateKingMoves("A4")
        assert(moves2 == listOf("B5", "B4", "B3", "A3", "A5"))
    }

    @Test
    fun test_generateLegalMoves() {
        chessboard.clear()
        chessboard.placePiece(Piece.WhiteKnight, "E5")
        chessboard.placePiece(Piece.WhiteKnight, "F7")

        val e5N = chessboard.generateLegalMoves("E5")
        assert(e5N == listOf("G6", "G4", "F3", "D3", "C4", "C6", "D7"))
        val f7N = chessboard.generateLegalMoves("F7")
        assert(f7N == listOf("H8", "H6", "G5", "D6", "D8"))


        chessboard.placePiece(Piece.WhiteBishop, "C3")
        val c3B = chessboard.generateLegalMoves("C3")
        assert(c3B == listOf("D4", "D2", "E1", "B4", "A5", "B2", "A1"))

        chessboard.placePiece(Piece.WhiteRook, "C1")
        val c1R = chessboard.generateLegalMoves("C1")
        assert(c1R == listOf("D1", "E1", "F1", "G1", "H1", "B1", "A1", "C2"))

        chessboard.placePiece(Piece.WhitePawn, "C2")
        val c2 = chessboard.generateLegalMoves("C2")
        assert(c2 == emptyList<String>())

        chessboard.placePiece(Piece.BlackPawn, "B3")
        val c2_2 = chessboard.generateLegalMoves("C2")
        assert(c2_2 == listOf("B3"))

        chessboard.placePiece(Piece.WhitePawn, "D3")
        val c2_3 = chessboard.generateLegalMoves("C2")
        assert(c2_3 == listOf("B3"))

        chessboard.placePiece(Piece.WhiteQueen, "D2")
        val d2Q = chessboard.generateLegalMoves("D2")
        assert(d2Q == listOf("E2", "F2", "G2", "H2", "D1", "E3", "F4", "G5", "H6", "E1"))

        chessboard.placePiece(Piece.BlackPawn, "E3")
        val d2Q_2 = chessboard.generateLegalMoves("D2")
        assert(d2Q_2 == listOf("E2", "F2", "G2", "H2", "D1", "E3", "E1"))
    }

    @Test
    fun test_generateMovesFromStart() {
        chessboard.reset()
        val ePawnBlack = chessboard.generateLegalMoves("E7")
        assert(ePawnBlack == listOf("E5", "E6"))

        val ePawnWhite = chessboard.generateLegalMoves("E2")
        assert(ePawnWhite == listOf("E4", "E3"))
    }

    @Test
    fun test_generateKingLegalMoves() {
        chessboard.clear()
        chessboard.placePiece(Piece.WhiteKing, "A8")
        val a8K = chessboard.generateLegalMoves("A8")
        assert(a8K == listOf("B8", "B7", "A7"))

        chessboard.placePiece(Piece.BlackRook, "B1")
        val a8K_2 = chessboard.generateLegalMoves("A8")
        assert(a8K_2 == listOf("A7"))

        chessboard.placePiece(Piece.BlackRook, "D7")
        val a8K_3 = chessboard.generateLegalMoves("A8")
        assert(a8K_3 == emptyList<String>())

        chessboard.placePiece(Piece.BlackPawn, "C7")
        val a8K_4 = chessboard.generateLegalMoves("A8")
        assert(a8K_4 == listOf("A7"))
    }
}