package com.example.chess

import com.example.chess.model.*
import org.junit.Before
import org.junit.Test

class ChessboardTest {
    lateinit var chessboard: Chessboard

    @Before
    fun setup() {
        chessboard = Chessboard()
    }

    @Test
    fun test_indexing() {
        assert(chessboard.boardTiles.size == 64)

        val tile = chessboard["A1"]
        assert(tile.name == "A1")

        val tile2 = chessboard["A8"]
        assert(tile2.name == "A8")

        val tile3 = chessboard["G4"]
        assert(tile3.name == "G4")
    }

    @Test
    fun test_format() {
        chessboard.reset()
        chessboard.basicPrint()
    }

    @Test
    fun test_knightMoveLegality() {
        chessboard.clear()
        chessboard.placePiece(Piece.WhiteKnight, "A1")
        assert(chessboard.knightMoveValid("A1", "B3"))
        assert(!chessboard.knightMoveValid("A1", "B2"))
        assert(chessboard.knightMoveValid("E5", "G4"))
    }

    @Test
    fun test_bishopMoveLegality() {
        chessboard.clear()
        chessboard.placePiece(Piece.WhiteBishop, "A1")
        assert(chessboard.bishopMoveValid("A1", "G7"))
        assert(!chessboard.bishopMoveValid("A1", "G8"))
        assert(chessboard.bishopMoveValid("A1", "E5"))
    }

    @Test
    fun test_rookMoveLegality() {
        chessboard.clear()
        chessboard.placePiece(Piece.WhiteRook, "A1")
        assert(chessboard.rookMoveValid("A1", "A5"))
        assert(!chessboard.rookMoveValid("A1", "B5"))
        assert(chessboard.rookMoveValid("A1", "F1"))
    }

    @Test
    fun test_queenMoveLegality() {
        chessboard.clear()
        chessboard.placePiece(Piece.WhiteQueen, "E5")
        assert(chessboard.queenMoveValid("E5", "F4"))
        assert(chessboard.queenMoveValid("E5", "H8"))
        assert(chessboard.queenMoveValid("E5", "A1"))
        assert(!chessboard.queenMoveValid("E5", "A7"))
    }

    @Test
    fun test_kingMoveLegality() {
        chessboard.clear()
        chessboard.placePiece(Piece.WhiteKing, "E1")
        assert(chessboard.kingMoveValid("E5", "F4"))
        assert(!chessboard.kingMoveValid("E5", "G4"))
        assert(chessboard.kingMoveValid("E5", "E4"))
    }

    @Test
    fun test_pawnMoveLegality() {
        chessboard.clear()
        chessboard.placePiece(Piece.WhitePawn, "E2")
        assert(chessboard.pawnMoveValid(Side.White,"E2", "E3"))
        assert(chessboard.pawnMoveValid(Side.White,"E2", "E4"))
        assert(!chessboard.pawnMoveValid(Side.White,"E2", "E5"))
        assert(!chessboard.pawnMoveValid(Side.White,"E2", "F3"))
        assert(!chessboard.pawnMoveValid(Side.White,"E2", "E1"))

        chessboard.clear()
        chessboard.placePiece(Piece.BlackPawn, "E7")
        assert(chessboard.pawnMoveValid(Side.Black,"E7", "E6"))
        assert(chessboard.pawnMoveValid(Side.Black,"E7", "E5"))
        assert(!chessboard.pawnMoveValid(Side.Black,"E7", "E4"))
        assert(!chessboard.pawnMoveValid(Side.Black,"E7", "F6"))
        assert(!chessboard.pawnMoveValid(Side.Black,"E7", "E8"))

        chessboard.clear()
        chessboard.placePiece(Piece.WhitePawn, "E2")
        assert(chessboard.pawnCaptureValid(Side.White, "E2", "D3"))
        assert(chessboard.pawnCaptureValid(Side.White, "E2", "F3"))
        assert(!chessboard.pawnCaptureValid(Side.White, "E2", "E3"))
    }

    @Test
    fun test_generateTileSequences() {
        val s1 = generateTileSequence("A1", "A8")
        assert(s1 == listOf("A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8"))
        val s2 = generateTileSequence("A8", "A1")
        assert(s2 == listOf("A8", "A7", "A6", "A5", "A4", "A3", "A2", "A1"))
        val s3 = generateTileSequence("A8", "H1")
        assert(s3 == listOf("A8", "B7", "C6", "D5", "E4", "F3", "G2", "H1"))
        val s4 = generateTileSequence("A1", "H8")
        assert(s4 == listOf("A1", "B2", "C3", "D4", "E5", "F6", "G7", "H8"))
        val s5 = generateTileSequence("H4", "D8")
        assert(s5 == listOf("H4", "G5", "F6", "E7", "D8"))
        val s6 = generateTileSequence("B6", "G6")
        assert(s6 == listOf("B6", "C6", "D6", "E6", "F6", "G6"))
    }

    @Test
    fun test_clone() {
        chessboard.clear()
        chessboard.placePiece(Piece.WhiteRook, "A1")
        chessboard.placePiece(Piece.WhitePawn, "A2")

        val n = chessboard.clone()
        assert(n.allPieces() == chessboard.allPieces())
    }

    @Test
    fun test_disambiguation() {
        chessboard.clear()
        chessboard.placePiece(Piece.BlackKnight, "B1")
        print(chessboard.disambiguateMove("B1", "D2"))
        assert(chessboard.disambiguateMove("B1", "D2") == "")

        chessboard.placePiece(Piece.BlackKnight, "F1")
        assert(chessboard.disambiguateMove("B1", "D2") == "B")

        chessboard.placePiece(Piece.BlackKnight, "B3")
        assert(chessboard.disambiguateMove("B1", "D2") == "B1")
    }

     @Test
     fun test_pieceBlocking() {
         chessboard.clear()
         chessboard.placePiece(Piece.WhiteRook, "A1")
         chessboard.placePiece(Piece.WhitePawn, "A2")
         assert(chessboard.anyBlocking("A1", "A8"))
         assert(chessboard.anyBlocking("A8", "A1"))
         assert(!chessboard.anyBlocking("A8", "A2"))
         assert(!chessboard.anyBlocking("A8", "A3"))

         chessboard.placePiece(Piece.BlackPawn, "A7")
         assert(chessboard.anyBlocking("A8", "A3"))

         chessboard.clear()
         chessboard.placePiece(Piece.WhiteBishop, "A1")
         chessboard.placePiece(Piece.WhitePawn, "D4")
         chessboard.placePiece(Piece.WhitePawn, "E5")
         assert(chessboard.anyBlocking("A1", "H8"))
         assert(!chessboard.anyBlocking("A1", "C3"))
         assert(!chessboard.anyBlocking("H8", "F6"))
     }

    @Test
    fun test_check() {
        chessboard.clear()
        chessboard.placePiece(Piece.WhiteRook, "A1")
        chessboard.placePiece(Piece.WhiteKing, "D1")
        assert(!chessboard.kingInCheck(Side.White))

        chessboard.placePiece(Piece.BlackKing, "A7")
        assert(chessboard.kingInCheck(Side.Black))

        chessboard.placePiece(Piece.BlackQueen, "A6")
        assert(!chessboard.kingInCheck(Side.Black))

        assert(chessboard.kingWouldBeInCheck(Side.Black, "A6", "B6"))
    }
}