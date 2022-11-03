package com.example.chess.model

import java.lang.Math.abs

enum class MoveType {
    Normal,
    Capture,
    QueensideCastle,
    KingsideCastle
}

enum class CheckType {
    None,
    Check,
    Checkmate
}

enum class CastleType {
    Queenside,
    Kingside
}

data class ChessMove(
    val piece: Piece,
    val fromIndex: String,
    val toIndex: String,
    val moveType: MoveType = MoveType.Normal,
    val checkType: CheckType = CheckType.None,
    val promotionPiece: PieceType? = null,
    val disambiguation: String = "") {
    val repr: String
        get() = when(moveType) {
            MoveType.QueensideCastle -> "0-0-0"
            MoveType.KingsideCastle -> "0-0"
            else -> formatOtherMove()
        }

    private fun formatOtherMove(): String {
        return when (piece.pieceType) {
            PieceType.Pawn -> if (moveType == MoveType.Capture) "${fromIndex[0]}".lowercase() else ""
            PieceType.Knight -> "N"
            PieceType.Bishop -> "B"
            PieceType.Rook -> "R"
            PieceType.Queen -> "Q"
            PieceType.King -> "K"
        } + disambiguation +
                (if (moveType == MoveType.Capture) "x" else "") +
                toIndex.lowercase() +
                when (promotionPiece) {
                    PieceType.Queen -> "=Q"
                    PieceType.Rook -> "=R"
                    PieceType.Bishop -> "=B"
                    PieceType.Knight -> "=N"
                    else -> ""
                } +
                when (checkType) {
                    CheckType.Check -> "+"
                    CheckType.Checkmate -> "#"
                    else -> ""
                }
    }
}

fun String.calcColumnDistance(s2: String): Int {
    val allCols = "ABCDEFGH"
    return abs(allCols.indexOf(s2) - allCols.indexOf(this))
}

fun String.behind(s2: String): Boolean {
    val allRows = "12345678"
    return allRows.indexOf(s2) - allRows.indexOf(this) >= 1
}

fun String.calcRowDistance(s2: String): Int {
    val allRows = "12345678"
    return abs(allRows.indexOf(s2) - allRows.indexOf(this))
}

fun Chessboard.knightMoveValid(fromIndex: String, toIndex: String): Boolean {
    val c1 = "${fromIndex[0]}"; val c2 = "${toIndex[0]}"; val cd = c1.calcColumnDistance(c2)
    val r1 = "${fromIndex[1]}"; val r2 = "${toIndex[1]}"; val rd = r1.calcRowDistance(r2)
    return abs(cd - rd) == 1 && cd + rd == 3
}

fun Chessboard.bishopMoveValid(fromIndex: String, toIndex: String): Boolean {
    val c1 = "${fromIndex[0]}"; val c2 = "${toIndex[0]}"; val cd = c1.calcColumnDistance(c2)
    val r1 = "${fromIndex[1]}"; val r2 = "${toIndex[1]}"; val rd = r1.calcRowDistance(r2)
    return cd == rd && cd > 0 && rd > 0
}

fun Chessboard.rookMoveValid(fromIndex: String, toIndex: String): Boolean {
    val c1 = "${fromIndex[0]}"; val c2 = "${toIndex[0]}"; val cd = c1.calcColumnDistance(c2)
    val r1 = "${fromIndex[1]}"; val r2 = "${toIndex[1]}"; val rd = r1.calcRowDistance(r2)
    return cd + rd > 0 && (c1 == c2 || r1 == r2)
}

fun Chessboard.queenMoveValid(fromIndex: String, toIndex: String): Boolean {
    val c1 = "${fromIndex[0]}"; val c2 = "${toIndex[0]}"; val cd = c1.calcColumnDistance(c2)
    val r1 = "${fromIndex[1]}"; val r2 = "${toIndex[1]}"; val rd = r1.calcRowDistance(r2)
    return (cd == rd && cd > 0 && rd > 0) || (cd + rd > 0 && (c1 == c2 || r1 == r2))
}

fun Chessboard.kingMoveValid(fromIndex: String, toIndex: String): Boolean {
    val c1 = "${fromIndex[0]}"; val c2 = "${toIndex[0]}"; val cd = c1.calcColumnDistance(c2)
    val r1 = "${fromIndex[1]}"; val r2 = "${toIndex[1]}"; val rd = r1.calcRowDistance(r2)
    return (cd == 1 && rd == 0) || (cd == 0 && rd == 1) || (cd == 1 && rd == 1)
}

fun Chessboard.isCheckmated(side: Side): Boolean {
    val king = this.pieces(side).firstOrNull { (p, i) -> p.pieceType == PieceType.King}
        ?: return false
    if (!kingInCheck(side)) return false
    return this.pieces(side).all { (p, i) ->
        val moves = generateLegalMoves(i)
        moves.isEmpty()
    }
}

fun Chessboard.castleValid(side: Side, castleType: CastleType): Boolean {
    val startingKingSquares = mapOf(Side.White to "E1", Side.Black to "E8")
    val startingQueensideRookSquares = mapOf(Side.White to "A1", Side.Black to "A8")
    val startingKingsideRookSquares = mapOf(Side.White to "H1", Side.Black to "H8")
    val king = this.pieces(side).firstOrNull { (p, i) -> p.pieceType == PieceType.King &&  i == startingKingSquares[side]}
        ?: return false
    val rook = if (castleType == CastleType.Queenside) {
        Pair(this[startingQueensideRookSquares[side]!!].piece, startingQueensideRookSquares[side]!!)
    } else {
        Pair(this[startingKingsideRookSquares[side]!!].piece, startingKingsideRookSquares[side]!!)
    } ?: return false

    if (this.kingInCheck(side)) return false
    if (anyBlocking(king.second, rook.second)) return false

    val otherSide = if (side == Side.Black) Side.White else Side.Black
    val tileSequence = generateTileSequence(king.second, rook.second)
    if (tileSequence.subList(1, tileSequence.size-1).any { t ->
        this.pieces(otherSide).any { (pp, i) ->
            canAttackTile(otherSide, i, t)
        }
        }) return false
    return true
}

fun Chessboard.pawnMoveValid(side: Side, fromIndex: String, toIndex: String): Boolean {
    val c1 = "${fromIndex[0]}"; val c2 = "${toIndex[0]}"; val cd = c1.calcColumnDistance(c2)
    val r1 = "${fromIndex[1]}"; val r2 = "${toIndex[1]}"; val rd = r1.calcRowDistance(r2)
    return when(side) {
        Side.White -> r1.behind(r2) && cd == 0 && ((r1 == "2" && (rd == 1 || rd == 2)) || (r1 != "2" && rd == 1))
        Side.Black -> r2.behind(r1) && cd == 0 && ((r1 == "7" && (rd == 1 || rd == 2)) || (r1 != "7" && rd == 1))
        else -> throw IllegalStateException("Not sure what side " + side + " is!")
    }
}

fun Chessboard.pawnCaptureValid(side: Side, fromIndex: String, toIndex: String): Boolean {
    val allCols = "ABCDEFGH"
    val allRows = "12345678"
    val c1 = "${fromIndex[0]}"; val c2 = "${toIndex[0]}"; val cd = c1.calcColumnDistance(c2)
    val r1 = "${fromIndex[1]}"; val r2 = "${toIndex[1]}"; val rd = r1.calcRowDistance(r2)

    val c1Index = allCols.indexOf(c1); val c2Index = allCols.indexOf(c2)
    val r1Index = allRows.indexOf(r1); val r2Index = allRows.indexOf(r2)

    return when(side) {
        Side.White -> abs(c2Index-c1Index) == 1 && r2Index - r1Index == 1
        Side.Black -> abs(c2Index-c1Index) == 1 && r1Index - r2Index == 1
        else -> throw IllegalStateException("Not sure what side " + side + " is!")
    }
}

fun generateTileSequence(fromIndex: String, toIndex: String): List<String> {
    val c1 = "${fromIndex[0]}"; val c2 = "${toIndex[0]}"; val cd = c1.calcColumnDistance(c2)
    val r1 = "${fromIndex[1]}"; val r2 = "${toIndex[1]}"; val rd = r1.calcRowDistance(r2)
    val allCols = "ABCDEFGH"
    val allRows = "12345678"

    if (cd == 0) {
        val rowIndex1 = allRows.indexOf(r1); val rowIndex2 = allRows.indexOf(r2)
        if (rowIndex1 > rowIndex2) {
            return (rowIndex1 downTo rowIndex2).map{c1 + allRows[it]}
        }
        else {
            return (rowIndex1..rowIndex2).map{c1 + allRows[it]}
        }
    }
    else if (rd == 0) {
        val colIndex1 = allCols.indexOf(c1); val colIndex2 = allCols.indexOf(c2)
        if (colIndex1 > colIndex2) {
            return (colIndex1 downTo colIndex2).map { allCols[it] + r1 }
        }
        else {
            return (colIndex1 .. colIndex2).map { allCols[it] + r1 }
        }
    }
    else if (cd == rd) {
        val rowIndex1 = allRows.indexOf(r1); val rowIndex2 = allRows.indexOf(r2)
        val colIndex1 = allCols.indexOf(c1); val colIndex2 = allCols.indexOf(c2)
        val rows = if (rowIndex1 > rowIndex2) (rowIndex1 downTo rowIndex2).toList() else (rowIndex1..rowIndex2).toList()
        val cols = if (colIndex1 > colIndex2) (colIndex1 downTo colIndex2).toList() else (colIndex1..colIndex2).toList()
        return rows.zip(cols) { r, c -> "${allCols[c]}" + "${allRows[r]}"}
    }
    else {
        throw IllegalArgumentException(fromIndex + " and " + toIndex + " are not a straight line!")
    }
}

/**
 * Checks to see if any pieces 'block' the line of sight between the squares denoted by fromIndex
 * and toIndex (NOT inclusive of either square)
 */
fun Chessboard.anyBlocking(fromIndex: String, toIndex: String): Boolean {
    val c1 = "${fromIndex[0]}"; val c2 = "${toIndex[0]}"; val cd = c1.calcColumnDistance(c2)
    val r1 = "${fromIndex[1]}"; val r2 = "${toIndex[1]}"; val rd = r1.calcRowDistance(r2)

    if (cd == 0 && rd == 1) return false
    else if (cd == 1 && rd == 0) return false
    else if (cd == 1 && rd == 1) return false

    val seq = generateTileSequence(fromIndex, toIndex)
    return seq.subList(fromIndex = 1, toIndex = seq.size-1).any { this[it].piece != null }
}

/**
 * Returns true if the king is in check for the given side
 */
fun Chessboard.kingInCheck(side: Side): Boolean {
    val kingTile = this.boardTiles.firstOrNull { it.piece?.pieceColor == side && it.piece?.pieceType == PieceType.King } ?: return false
    val otherSide = if (side == Side.White) Side.Black else Side.White
    return this.pieces(otherSide).any { (p, sq) ->
        this.canAttackTile(side, sq, kingTile.name)
    }
}

/**
 * Returns true if the king would be in check if the move from fromIndex to toIndex is made
 */
fun Chessboard.kingWouldBeInCheck(side: Side, fromIndex: String, toIndex: String): Boolean {
    val n = this.clone()
    n.movePiece(fromIndex, toIndex)
    return n.kingInCheck(side)
}

fun Chessboard.kingWouldBeCheckmated(side: Side, fromIndex: String, toIndex: String): Boolean {
    val n = this.clone()
    n.movePiece(fromIndex, toIndex)
    return n.isCheckmated(side)
}

fun Chessboard.disambiguateMove(fromIndex: String, toIndex: String): String {
    val p = this[fromIndex].piece ?: throw java.lang.IllegalArgumentException("No piece on " + fromIndex + "!")
    val side = p.pieceColor
    val identicalPieces = this.pieces(side).filter { (piece, index) ->
        piece.pieceType == p.pieceType &&
                index != fromIndex &&
                this.generateLegalMoves(index).contains(toIndex)
    }
    if (identicalPieces.isEmpty())
        return ""
    val sameColumn = identicalPieces.any { (piece, index) ->
        index[0] == fromIndex[0]
    }
    val sameRow = identicalPieces.any { (piece, index) ->
        index[1] == fromIndex[1]
    }
    if (sameColumn && sameRow) {
        return fromIndex
    }
    else if (sameColumn) {
        return "${fromIndex[1]}"
    }
    else if (sameRow) {
        return "${fromIndex[0]}"
    }
    else {
        return "${fromIndex[0]}"
    }
}

fun Chessboard.canAttackTile(side: Side, fromIndex: String, toIndex: String): Boolean {
    val p = this[fromIndex].piece ?: throw java.lang.IllegalArgumentException("No piece on " + fromIndex + "!")
    return when (p.pieceType) {
        PieceType.Knight -> this.knightMoveValid(fromIndex, toIndex)
        PieceType.Bishop -> this.bishopMoveValid(fromIndex, toIndex) && !this.anyBlocking(fromIndex, toIndex)
        PieceType.Rook -> this.rookMoveValid(fromIndex, toIndex) && !this.anyBlocking(fromIndex, toIndex)
        PieceType.Queen -> this.queenMoveValid(fromIndex, toIndex) && !this.anyBlocking(fromIndex, toIndex)
        PieceType.King -> this.kingMoveValid(fromIndex, toIndex)
        PieceType.Pawn -> this.pawnCaptureValid(side, fromIndex, toIndex)
        else -> false
    }
}