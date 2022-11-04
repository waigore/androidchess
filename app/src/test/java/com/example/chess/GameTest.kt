package com.example.chess

import com.example.chess.model.*
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class GameTest {
    lateinit var chessGame: ChessGame

    @Before
    fun setUp() {
        chessGame = ChessGame()
    }

    @Test
    fun test_simpleGame() {
        chessGame.reset()
        chessGame.start()

        chessGame.makeMove("E2", "E4")
        assert(chessGame.nextMoveSide == Side.Black)

        chessGame.makeMove("E7", "E5")
        assert(chessGame.nextMoveSide == Side.White)

        val moves = chessGame.moveHistoryStandardNotation()
        assert(moves == listOf("e4", "e5"))

        chessGame.makeMove("G1", "F3")
        chessGame.makeMove("B8", "C6")
        chessGame.makeMove("F1", "B5")
        val moves2 = chessGame.moveHistoryStandardNotation()
        assert(moves2 == listOf("e4", "e5", "Nf3", "Nc6", "Bb5"))
    }

    @Test
    fun test_legalMoves() {
        chessGame.reset()
        chessGame.start()

        chessGame.makeMove("E2", "E4")
        chessGame.makeMove("E7", "E5")
        assertThrows(IllegalArgumentException::class.java) {
            chessGame.makeMove("E4", "E5")
        }
        assertThrows(IllegalStateException::class.java) {
            chessGame.makeMove("D7", "D6")
        }
    }

    @Test
    fun test_operaGame() {
        chessGame.reset()
        chessGame.start()

        chessGame.makeMove("E2", "E4")
        chessGame.makeMove("E7", "E5")
        chessGame.makeMove("G1", "F3")
        chessGame.makeMove("D7", "D6")
        chessGame.makeMove("D2", "D4")
        chessGame.makeMove("C8", "G4")
        val moves = chessGame.moveHistoryStandardNotation()
        assert(moves == listOf("e4", "e5", "Nf3", "d6", "d4", "Bg4"))

        chessGame.makeMove("D4", "E5")
        chessGame.makeMove("G4", "F3")
        val moves2 = chessGame.moveHistoryStandardNotation()
        assert(moves2 == listOf("e4", "e5", "Nf3", "d6", "d4", "Bg4", "dxe5", "Bxf3"))

        chessGame.makeMove("D1", "F3")
        chessGame.makeMove("D6", "E5")
        chessGame.makeMove("F1", "C4")
        chessGame.makeMove("G8", "F6")
        val moves3 = chessGame.moveHistoryStandardNotation()
        //chessGame.chessboard.basicPrint()
        assert(moves3 == listOf("e4", "e5", "Nf3", "d6", "d4", "Bg4", "dxe5", "Bxf3", "Qxf3", "dxe5", "Bc4", "Nf6"))

        chessGame.makeMove("F3", "B3")
        chessGame.makeMove("D8", "E7")
        chessGame.makeMove("B1", "C3")
        chessGame.makeMove("C7", "C6")
        val moves4 = chessGame.moveHistoryStandardNotation()
        assert(moves4 == listOf("e4", "e5", "Nf3", "d6", "d4", "Bg4", "dxe5", "Bxf3", "Qxf3", "dxe5", "Bc4", "Nf6",
                            "Qb3", "Qe7", "Nc3", "c6"))

        chessGame.makeMove("C1", "G5")
        chessGame.makeMove("B7", "B5")
        chessGame.makeMove("C3", "B5")
        chessGame.makeMove("C6", "B5")
        val moves5 = chessGame.moveHistoryStandardNotation()
        assert(moves5 == listOf("e4", "e5", "Nf3", "d6", "d4", "Bg4", "dxe5", "Bxf3", "Qxf3", "dxe5", "Bc4", "Nf6",
            "Qb3", "Qe7", "Nc3", "c6", "Bg5", "b5", "Nxb5", "cxb5"))

        chessGame.makeMove("C4", "B5")
        chessGame.makeMove("B8", "D7")
        val moves6 = chessGame.moveHistoryStandardNotation()
        assert(moves6 == listOf("e4", "e5", "Nf3", "d6", "d4", "Bg4", "dxe5", "Bxf3", "Qxf3", "dxe5", "Bc4", "Nf6",
            "Qb3", "Qe7", "Nc3", "c6", "Bg5", "b5", "Nxb5", "cxb5", "Bxb5+", "Nbd7"))

        chessGame.doCastle(Side.White, CastleType.Queenside)
        chessGame.makeMove("A8", "D8")
        val moves7 = chessGame.moveHistoryStandardNotation()
        assert(moves7 == listOf("e4", "e5", "Nf3", "d6", "d4", "Bg4", "dxe5", "Bxf3", "Qxf3", "dxe5", "Bc4", "Nf6",
            "Qb3", "Qe7", "Nc3", "c6", "Bg5", "b5", "Nxb5", "cxb5", "Bxb5+", "Nbd7", "0-0-0", "Rd8"))

        chessGame.makeMove("D1", "D7")
        chessGame.makeMove("D8", "D7")
        chessGame.makeMove("H1", "D1")
        chessGame.makeMove("E7", "E6")
        val moves8 = chessGame.moveHistoryStandardNotation()
        assert(moves8 == listOf("e4", "e5", "Nf3", "d6", "d4", "Bg4", "dxe5", "Bxf3", "Qxf3", "dxe5", "Bc4", "Nf6",
            "Qb3", "Qe7", "Nc3", "c6", "Bg5", "b5", "Nxb5", "cxb5", "Bxb5+", "Nbd7", "0-0-0", "Rd8",
        "Rxd7", "Rxd7", "Rd1", "Qe6"))
        //chessGame.chessboard.basicPrint()

        chessGame.makeMove("B5", "D7")
        chessGame.makeMove("F6", "D7")
        chessGame.makeMove("B3", "B8")
        chessGame.makeMove("D7", "B8")
        val moves9 = chessGame.moveHistoryStandardNotation()
        assert(moves9 == listOf("e4", "e5", "Nf3", "d6", "d4", "Bg4", "dxe5", "Bxf3", "Qxf3", "dxe5", "Bc4", "Nf6",
            "Qb3", "Qe7", "Nc3", "c6", "Bg5", "b5", "Nxb5", "cxb5", "Bxb5+", "Nbd7", "0-0-0", "Rd8",
            "Rxd7", "Rxd7", "Rd1", "Qe6", "Bxd7+", "Nxd7", "Qb8+", "Nxb8"))

        chessGame.makeMove("D1", "D8")
        val moves10 = chessGame.moveHistoryStandardNotation()
        assert(moves10 == listOf("e4", "e5", "Nf3", "d6", "d4", "Bg4", "dxe5", "Bxf3", "Qxf3", "dxe5", "Bc4", "Nf6",
            "Qb3", "Qe7", "Nc3", "c6", "Bg5", "b5", "Nxb5", "cxb5", "Bxb5+", "Nbd7", "0-0-0", "Rd8",
            "Rxd7", "Rxd7", "Rd1", "Qe6", "Bxd7+", "Nxd7", "Qb8+", "Nxb8", "Rd8#"))
    }

    @Test
    fun test_byrneFischer() {
        chessGame.reset()
        chessGame.start()

        chessGame.makeMove("G1", "F3")
        chessGame.makeMove("G8", "F6")
        chessGame.makeMove("C2", "C4")
        chessGame.makeMove("G7", "G6")
        chessGame.makeMove("B1", "C3")
        chessGame.makeMove("F8", "G7")
        chessGame.makeMove("D2", "D4")
        chessGame.doCastle(Side.Black, CastleType.Kingside)
        val moves1 = chessGame.moveHistoryStandardNotation()
        assert(moves1 == listOf("Nf3", "Nf6", "c4", "g6", "Nc3", "Bg7", "d4", "0-0"))

        chessGame.makeMove("C1", "F4")
        chessGame.makeMove("D7", "D5")
        chessGame.makeMove("D1", "B3")
        chessGame.makeMove("D5", "C4")
        chessGame.makeMove("B3", "C4")
        chessGame.makeMove("C7", "C6")
        chessGame.makeMove("E2", "E4")
        chessGame.makeMove("B8", "D7")
        val moves2 = chessGame.moveHistoryStandardNotation()
        assert(moves2 == listOf(
            "Nf3", "Nf6", "c4", "g6", "Nc3", "Bg7", "d4", "0-0",
            "Bf4", "d5", "Qb3", "dxc4", "Qxc4", "c6", "e4", "Nbd7"))

        chessGame.makeMove("A1", "D1")
        chessGame.makeMove("D7", "B6")
        chessGame.makeMove("C4", "C5")
        chessGame.makeMove("C8", "G4")
        chessGame.makeMove("F4", "G5")
        chessGame.makeMove("B6", "A4")
        chessGame.makeMove("C5", "A3")
        chessGame.makeMove("A4", "C3")
        val moves3 = chessGame.moveHistoryStandardNotation()
        assert(moves3 == listOf(
            "Nf3", "Nf6", "c4", "g6", "Nc3", "Bg7", "d4", "0-0",
            "Bf4", "d5", "Qb3", "dxc4", "Qxc4", "c6", "e4", "Nbd7",
            "Rd1", "Nb6", "Qc5", "Bg4", "Bg5", "Na4", "Qa3", "Nxc3"
        ))

        chessGame.makeMove("B2", "C3")
        chessGame.makeMove("F6", "E4")
        chessGame.makeMove("G5", "E7")
        chessGame.makeMove("D8", "B6")
        chessGame.makeMove("F1", "C4")
        chessGame.makeMove("E4", "C3")
        chessGame.makeMove("E7", "C5")
        chessGame.makeMove("F8", "E8")
        val moves4 = chessGame.moveHistoryStandardNotation()
        assert(moves4 == listOf(
            "Nf3", "Nf6", "c4", "g6", "Nc3", "Bg7", "d4", "0-0",
            "Bf4", "d5", "Qb3", "dxc4", "Qxc4", "c6", "e4", "Nbd7",
            "Rd1", "Nb6", "Qc5", "Bg4", "Bg5", "Na4", "Qa3", "Nxc3",
            "bxc3", "Nxe4", "Bxe7", "Qb6", "Bc4", "Nxc3", "Bc5", "Rfe8+"
        ))

        chessGame.makeMove("E1", "F1")
        chessGame.makeMove("G4", "E6")
        chessGame.makeMove("C5", "B6")
        chessGame.makeMove("E6", "C4")
        chessGame.makeMove("F1", "G1")
        chessGame.makeMove("C3", "E2")
        chessGame.makeMove("G1", "F1")
        chessGame.makeMove("E2", "D4")
        val moves5 = chessGame.moveHistoryStandardNotation()
        assert(moves5 == listOf(
            "Nf3", "Nf6", "c4", "g6", "Nc3", "Bg7", "d4", "0-0",
            "Bf4", "d5", "Qb3", "dxc4", "Qxc4", "c6", "e4", "Nbd7",
            "Rd1", "Nb6", "Qc5", "Bg4", "Bg5", "Na4", "Qa3", "Nxc3",
            "bxc3", "Nxe4", "Bxe7", "Qb6", "Bc4", "Nxc3", "Bc5", "Rfe8+",
            "Kf1", "Be6", "Bxb6", "Bxc4+", "Kg1", "Ne2+", "Kf1", "Nxd4+"
        ))

        chessGame.makeMove("F1", "G1")
        chessGame.makeMove("D4", "E2")
        chessGame.makeMove("G1", "F1")
        chessGame.makeMove("E2", "C3")
        chessGame.makeMove("F1", "G1")
        chessGame.makeMove("A7", "B6")
        chessGame.makeMove("A3", "B4")
        chessGame.makeMove("A8", "A4")
        val moves6 = chessGame.moveHistoryStandardNotation()
        assert(moves6 == listOf(
            "Nf3", "Nf6", "c4", "g6", "Nc3", "Bg7", "d4", "0-0",
            "Bf4", "d5", "Qb3", "dxc4", "Qxc4", "c6", "e4", "Nbd7",
            "Rd1", "Nb6", "Qc5", "Bg4", "Bg5", "Na4", "Qa3", "Nxc3",
            "bxc3", "Nxe4", "Bxe7", "Qb6", "Bc4", "Nxc3", "Bc5", "Rfe8+",
            "Kf1", "Be6", "Bxb6", "Bxc4+", "Kg1", "Ne2+", "Kf1", "Nxd4+",
            "Kg1", "Ne2+", "Kf1", "Nc3+", "Kg1", "axb6", "Qb4", "Ra4"
        ))

        chessGame.makeMove("B4", "B6")
        chessGame.makeMove("C3", "D1")
        chessGame.makeMove("H2", "H3")
        chessGame.makeMove("A4", "A2")
        chessGame.makeMove("G1", "H2")
        chessGame.makeMove("D1", "F2")
        chessGame.makeMove("H1", "E1")
        chessGame.makeMove("E8", "E1")
        val moves7 = chessGame.moveHistoryStandardNotation()
        assert(moves7 == listOf(
            "Nf3", "Nf6", "c4", "g6", "Nc3", "Bg7", "d4", "0-0",
            "Bf4", "d5", "Qb3", "dxc4", "Qxc4", "c6", "e4", "Nbd7",
            "Rd1", "Nb6", "Qc5", "Bg4", "Bg5", "Na4", "Qa3", "Nxc3",
            "bxc3", "Nxe4", "Bxe7", "Qb6", "Bc4", "Nxc3", "Bc5", "Rfe8+",
            "Kf1", "Be6", "Bxb6", "Bxc4+", "Kg1", "Ne2+", "Kf1", "Nxd4+",
            "Kg1", "Ne2+", "Kf1", "Nc3+", "Kg1", "axb6", "Qb4", "Ra4",
            "Qxb6", "Nxd1", "h3", "Rxa2", "Kh2", "Nxf2", "Re1", "Rxe1"
        ))

        chessGame.makeMove("B6", "D8")
        chessGame.makeMove("G7", "F8")
        chessGame.makeMove("F3", "E1")
        chessGame.makeMove("C4", "D5")
        chessGame.makeMove("E1", "F3")
        chessGame.makeMove("F2", "E4")
        chessGame.makeMove("D8", "B8")
        chessGame.makeMove("B7", "B5")
        val moves8 = chessGame.moveHistoryStandardNotation()
        assert(moves8 == listOf(
            "Nf3", "Nf6", "c4", "g6", "Nc3", "Bg7", "d4", "0-0",
            "Bf4", "d5", "Qb3", "dxc4", "Qxc4", "c6", "e4", "Nbd7",
            "Rd1", "Nb6", "Qc5", "Bg4", "Bg5", "Na4", "Qa3", "Nxc3",
            "bxc3", "Nxe4", "Bxe7", "Qb6", "Bc4", "Nxc3", "Bc5", "Rfe8+",
            "Kf1", "Be6", "Bxb6", "Bxc4+", "Kg1", "Ne2+", "Kf1", "Nxd4+",
            "Kg1", "Ne2+", "Kf1", "Nc3+", "Kg1", "axb6", "Qb4", "Ra4",
            "Qxb6", "Nxd1", "h3", "Rxa2", "Kh2", "Nxf2", "Re1", "Rxe1",
            "Qd8+", "Bf8", "Nxe1", "Bd5", "Nf3", "Ne4", "Qb8", "b5"
        ))

        chessGame.makeMove("H3", "H4")
        chessGame.makeMove("H7", "H5")
        chessGame.makeMove("F3", "E5")
        chessGame.makeMove("G8", "G7")
        chessGame.makeMove("H2", "G1")
        chessGame.makeMove("F8", "C5")
        chessGame.makeMove("G1", "F1")
        chessGame.makeMove("E4", "G3")
        val moves9 = chessGame.moveHistoryStandardNotation()
        assert(moves9 == listOf(
            "Nf3", "Nf6", "c4", "g6", "Nc3", "Bg7", "d4", "0-0",
            "Bf4", "d5", "Qb3", "dxc4", "Qxc4", "c6", "e4", "Nbd7",
            "Rd1", "Nb6", "Qc5", "Bg4", "Bg5", "Na4", "Qa3", "Nxc3",
            "bxc3", "Nxe4", "Bxe7", "Qb6", "Bc4", "Nxc3", "Bc5", "Rfe8+",
            "Kf1", "Be6", "Bxb6", "Bxc4+", "Kg1", "Ne2+", "Kf1", "Nxd4+",
            "Kg1", "Ne2+", "Kf1", "Nc3+", "Kg1", "axb6", "Qb4", "Ra4",
            "Qxb6", "Nxd1", "h3", "Rxa2", "Kh2", "Nxf2", "Re1", "Rxe1",
            "Qd8+", "Bf8", "Nxe1", "Bd5", "Nf3", "Ne4", "Qb8", "b5",
            "h4", "h5", "Ne5", "Kg7", "Kg1", "Bc5+", "Kf1", "Ng3+"
        ))

        chessGame.makeMove("F1", "E1")
        chessGame.makeMove("C5", "B4")
        chessGame.makeMove("E1", "D1")
        chessGame.makeMove("D5", "B3")
        chessGame.makeMove("D1", "C1")
        chessGame.makeMove("G3", "E2")
        chessGame.makeMove("C1", "B1")
        chessGame.makeMove("E2", "C3")
        chessGame.makeMove("B1", "C1")
        chessGame.makeMove("A2", "C2")
        val moves10 = chessGame.moveHistoryStandardNotation()
        assert(moves10 == listOf(
            "Nf3", "Nf6", "c4", "g6", "Nc3", "Bg7", "d4", "0-0",
            "Bf4", "d5", "Qb3", "dxc4", "Qxc4", "c6", "e4", "Nbd7",
            "Rd1", "Nb6", "Qc5", "Bg4", "Bg5", "Na4", "Qa3", "Nxc3",
            "bxc3", "Nxe4", "Bxe7", "Qb6", "Bc4", "Nxc3", "Bc5", "Rfe8+",
            "Kf1", "Be6", "Bxb6", "Bxc4+", "Kg1", "Ne2+", "Kf1", "Nxd4+",
            "Kg1", "Ne2+", "Kf1", "Nc3+", "Kg1", "axb6", "Qb4", "Ra4",
            "Qxb6", "Nxd1", "h3", "Rxa2", "Kh2", "Nxf2", "Re1", "Rxe1",
            "Qd8+", "Bf8", "Nxe1", "Bd5", "Nf3", "Ne4", "Qb8", "b5",
            "h4", "h5", "Ne5", "Kg7", "Kg1", "Bc5+", "Kf1", "Ng3+",
            "Ke1", "Bb4+", "Kd1", "Bb3+", "Kc1", "Ne2+", "Kb1", "Nc3+",
            "Kc1", "Rc2#"
        ))
    }

    @Test
    fun test_capturedPieces() {
        chessGame.reset()
        chessGame.start()

        chessGame.makeMove("E2", "E4")
        chessGame.makeMove("D7", "D5")
        chessGame.makeMove("E4", "D5")
        chessGame.makeMove("D8", "D5")

        val whiteCapturedPieces = chessGame.formatCapturedPieces(Side.White)
        val blackCapturedPieces = chessGame.formatCapturedPieces(Side.Black)
        assert(whiteCapturedPieces == "♙")
        assert(blackCapturedPieces == "♟")

        chessGame.makeMove("B1", "C3")
        chessGame.makeMove("D5", "E5")
        chessGame.makeMove("D1", "E2")
        chessGame.makeMove("E5", "E2")
        val whiteCapturedPieces2 = chessGame.formatCapturedPieces(Side.White)
        assert(whiteCapturedPieces2 == "♕ ♙")
    }

    @Test
    fun test_enPassant() {
        chessGame.reset()
        chessGame.start()

        chessGame.makeMove("E2", "E4")
        chessGame.makeMove("D7", "D5")
        chessGame.makeMove("E4", "E5")
        chessGame.makeMove("F7", "F5")
        chessGame.makeMove("E5", "F6", "F5")
        val moves = chessGame.moveHistoryStandardNotation()
        assert(moves == listOf("e4", "d5", "e5", "f5", "exf6"))

        chessGame.reset()
        chessGame.start()

        chessGame.makeMove("G1", "F3")
        chessGame.makeMove("D7", "D5")
        chessGame.makeMove("D2", "D3")
        chessGame.makeMove("D5", "D4")
        chessGame.makeMove("E2", "E4")
        chessGame.makeMove("D4", "E3", "E4")
        val moves2 = chessGame.moveHistoryStandardNotation()
        assert(moves2 == listOf("Nf3", "d5", "d3", "d4", "e4", "dxe3"))
    }
}