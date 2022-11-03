package com.example.chess.model

import com.example.chess.util.IObservable
import com.example.chess.util.IObserver
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.Delegates

enum class GameState {
    NotStarted,
    InProgress,
    WhiteWin,
    BlackWin,
    Draw
}

interface IChessGameObserver: IObserver {
    fun gameStateChanged(old: GameState, new: GameState)
    fun nextMoveChanged(old: Side, new: Side)
    fun tilesChanged(tiles: List<String>)
    fun castlingRightsChanged(side: Side, queensideAllowed: Boolean, kingsideAllowed: Boolean)
}

@Singleton
class ChessGame @Inject constructor(): IObservable {
    val chessboard: Chessboard by lazy { Chessboard() }
    var gameState: GameState by Delegates.observable (GameState.NotStarted) {prop, oldVal, newVal ->
        notifyGameStateChanged(oldVal, newVal)
    }
        private set
    var nextMoveSide: Side by Delegates.observable(Side.White) {prop, oldVal, newVal ->
        notifyNextMoveChanged(oldVal, newVal)
    }
    val moveHistory: MutableList<ChessMove> = mutableListOf()

    val qCastlingRights: MutableMap<Side, Boolean> = mutableMapOf(Side.White to true, Side.Black to true)
    val kCastlingRights: MutableMap<Side, Boolean> = mutableMapOf(Side.White to true, Side.Black to true)

    override val observers: ArrayList<IObserver> by lazy {
        ArrayList()
    }

    private fun notifyGameStateChanged(old: GameState, new: GameState) {
        observers.forEach { (it as IChessGameObserver).gameStateChanged(old, new) }
    }

    private fun notifyNextMoveChanged(old: Side, new: Side) {
        observers.forEach { (it as IChessGameObserver).nextMoveChanged(old, new) }
    }

    private fun notifyTilesChanged(tiles: List<String>) {
        observers.forEach { (it as IChessGameObserver).tilesChanged(tiles)}
    }

    private fun notifyCastlingRightsChanged(side: Side, queensideAllowed: Boolean, kingsideAllowed: Boolean) {
        observers.forEach { (it as IChessGameObserver).castlingRightsChanged(side, queensideAllowed, kingsideAllowed)}
    }

    fun reset() {
        chessboard.reset()
        gameState = GameState.NotStarted
        moveHistory.clear()
    }

    fun start() {
        gameState = GameState.InProgress
        nextMoveSide = Side.White
        qCastlingRights[Side.White] = true; qCastlingRights[Side.Black] = true
        kCastlingRights[Side.White] = true; kCastlingRights[Side.Black] = true
    }

    private fun updateCastlingRights(piece: Piece, fromIndex: String) {
        if (piece.pieceType == PieceType.Rook && fromIndex in listOf("A1", "A8")) {
            qCastlingRights[piece.pieceColor] = false
        }
        else if (piece.pieceType == PieceType.Rook && fromIndex in listOf("H1", "H8")) {
            kCastlingRights[piece.pieceColor] = false
        }
        else if (piece.pieceType == PieceType.King) {
            qCastlingRights[piece.pieceColor] = false
            kCastlingRights[piece.pieceColor] = false
        }
        notifyCastlingRightsChanged(piece.pieceColor, qCastlingRights[piece.pieceColor]!!, kCastlingRights[piece.pieceColor]!!)
    }

    fun doCastle(side: Side, castleType: CastleType) {
        if (gameState != GameState.InProgress) throw IllegalStateException("Game is not in progress!")
        val otherSide = if (side == Side.White) Side.Black else Side.White
        if (nextMoveSide != side) throw IllegalStateException("Piece to be moved does not belong to " + nextMoveSide + "!")
        if (castleType == CastleType.Kingside && !kCastlingRights[side]!!) {
            throw IllegalStateException("No kingside castling rights for " + side + "!")
        }
        if (castleType == CastleType.Queenside && !qCastlingRights[side]!!) {
            throw IllegalStateException("No queenside castling rights for " + side + "!")
        }

        if (!chessboard.castleValid(side, castleType)) throw IllegalStateException("Castle " + castleType + " is not valid!")

        var fromIndex: String = ""
        val piece: Piece = if (side == Side.White) Piece.WhiteKing else Piece.BlackKing
        val tilesChanged = mutableListOf<String>()
        if (side == Side.White && castleType == CastleType.Kingside) {
            chessboard.movePiece("E1", "G1")
            chessboard.movePiece("H1", "F1")
            fromIndex = "E1"
            tilesChanged.addAll(listOf("E1", "G1", "H1", "F1"))
        }
        else if (side == Side.White && castleType == CastleType.Queenside) {
            chessboard.movePiece("E1", "C1")
            chessboard.movePiece("A1", "D1")
            fromIndex = "E1"
            tilesChanged.addAll(listOf("E1", "C1", "A1", "D1"))
        }
        else if (side == Side.Black && castleType == CastleType.Kingside) {
            chessboard.movePiece("E8", "G8")
            chessboard.movePiece("H8", "F8")
            fromIndex = "E8"
            tilesChanged.addAll(listOf("D8", "G8", "H8", "F8"))
        }
        else if (side == Side.Black && castleType == CastleType.Queenside) {
            chessboard.movePiece("E8", "C8")
            chessboard.movePiece("A8", "D8")
            fromIndex = "E8"
            tilesChanged.addAll(listOf("E8", "C8", "A8", "D8"))
        }
        else {
            throw IllegalStateException("Unknown side " + side + " and castle type " + castleType + "!")
        }
        updateCastlingRights(piece, fromIndex)

        val chessMove = ChessMove(piece = piece,
            fromIndex = "",
            toIndex = "",
            moveType = if (castleType == CastleType.Kingside) MoveType.KingsideCastle else MoveType.QueensideCastle
        )
        moveHistory.add(chessMove)

        notifyTilesChanged(tilesChanged)

        nextMoveSide = otherSide
    }

    fun generateCastlingMoves(side: Side): List<Pair<CastleType, String>> {
        if (gameState != GameState.InProgress) throw IllegalStateException("Game is not in progress!")

        val m = mutableListOf<Pair<CastleType, String>>()
        if (qCastlingRights[side]!! && chessboard.castleValid(side, CastleType.Queenside)) {
            m.add(Pair(CastleType.Queenside, if (side == Side.White) "C1" else "C8"))
        }
        if (kCastlingRights[side]!! && chessboard.castleValid(side, CastleType.Kingside)) {
            m.add(Pair(CastleType.Kingside, if(side == Side.White) "G1" else "G8"))
        }
        return m
    }

    fun generateLegalMoves(index: String): List<String> {
        if (gameState != GameState.InProgress) throw IllegalStateException("Game is not in progress!")
        val piece = chessboard.pieceAt(index) ?: throw java.lang.IllegalArgumentException("No piece at " + index + "!")

        return chessboard.generateLegalMoves(index)
    }

    fun makeMove(fromIndex: String, toIndex: String) {
        if (gameState != GameState.InProgress) throw IllegalStateException("Game is not in progress!")
        val piece = chessboard.pieceAt(fromIndex) ?: throw java.lang.IllegalArgumentException("No piece at " + fromIndex + "!")
        val otherSide = if (piece.pieceColor == Side.White) Side.Black else Side.White
        if (nextMoveSide != piece.pieceColor) throw IllegalStateException("Piece to be moved does not belong to " + nextMoveSide + "!")

        val legalMoves = chessboard.generateLegalMoves(fromIndex)
        if (!legalMoves.any {it == toIndex}) throw java.lang.IllegalArgumentException(fromIndex + "->" + toIndex + " is not legal!")

        val targetPiece = chessboard.pieceAt(toIndex)
        val moveType = if (targetPiece != null) MoveType.Capture else MoveType.Normal
        //val checkType: CheckType = if (chessboard.kingWouldBeInCheck(otherSide, fromIndex, toIndex)) CheckType.Check else CheckType.None
        val isCheckmate = chessboard.kingWouldBeCheckmated(otherSide, fromIndex, toIndex)
        val checkType = when {
            isCheckmate -> CheckType.Checkmate
            chessboard.kingWouldBeInCheck(otherSide, fromIndex, toIndex) ->CheckType.Check
            else -> CheckType.None
        }
        val disambiguation = if (piece.pieceType != PieceType.Pawn) chessboard.disambiguateMove(fromIndex, toIndex).lowercase() else ""

        updateCastlingRights(piece, fromIndex)
        chessboard.movePiece(fromIndex, toIndex)

        val chessMove = ChessMove(
            piece = piece,
            fromIndex = fromIndex, toIndex = toIndex,
            moveType = moveType, checkType = checkType,
            disambiguation = disambiguation)
        moveHistory.add(chessMove)

        notifyTilesChanged(listOf(fromIndex, toIndex))

        /*
        if (checkType == CheckType.Checkmate) {
            endGame(piece.pieceColor)
        }
        else {
            nextMoveSide = otherSide
        }
        */
        nextMoveSide = otherSide
    }

    fun endGame(winningSide: Side?) {
        gameState = when {
            winningSide == Side.White -> GameState.WhiteWin
            winningSide == Side.Black -> GameState.BlackWin
            else -> GameState.Draw
        }
    }

    fun moveHistoryStandardNotation(): List<String> {
        return moveHistory.map {
            it.repr
        }
    }
}