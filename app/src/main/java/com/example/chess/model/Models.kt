package com.example.chess.model

enum class PieceType {
    King,
    Queen,
    Rook,
    Bishop,
    Knight,
    Pawn
}

enum class Side {
    Black,
    White
}

enum class TileColor {
    Light,
    Dark
}

data class Piece(val pieceType: PieceType, val pieceColor: Side) {
    val repr: String
    get() = when {
        pieceType == PieceType.King && pieceColor == Side.White -> "♔"
        pieceType == PieceType.Queen && pieceColor == Side.White -> "♕"
        pieceType == PieceType.Rook && pieceColor == Side.White -> "♖"
        pieceType == PieceType.Bishop && pieceColor == Side.White -> "♗"
        pieceType == PieceType.Knight && pieceColor == Side.White -> "♘"
        pieceType == PieceType.Pawn && pieceColor == Side.White -> "♙"
        pieceType == PieceType.King && pieceColor == Side.Black -> "♚"
        pieceType == PieceType.Queen && pieceColor == Side.Black -> "♛"
        pieceType == PieceType.Rook && pieceColor == Side.Black -> "♜"
        pieceType == PieceType.Bishop && pieceColor == Side.Black -> "♝"
        pieceType == PieceType.Knight && pieceColor == Side.Black -> "♞"
        pieceType == PieceType.Pawn && pieceColor == Side.Black -> "♟"
        else -> throw IllegalStateException("invalid piece!")
    }

    val basicRepr: String
    get() = when {
        pieceType == PieceType.King && pieceColor == Side.White -> "K"
        pieceType == PieceType.Queen && pieceColor == Side.White -> "Q"
        pieceType == PieceType.Rook && pieceColor == Side.White -> "R"
        pieceType == PieceType.Bishop && pieceColor == Side.White -> "B"
        pieceType == PieceType.Knight && pieceColor == Side.White -> "N"
        pieceType == PieceType.Pawn && pieceColor == Side.White -> "P"
        pieceType == PieceType.King && pieceColor == Side.Black -> "k"
        pieceType == PieceType.Queen && pieceColor == Side.Black -> "q"
        pieceType == PieceType.Rook && pieceColor == Side.Black -> "r"
        pieceType == PieceType.Bishop && pieceColor == Side.Black -> "b"
        pieceType == PieceType.Knight && pieceColor == Side.Black -> "n"
        pieceType == PieceType.Pawn && pieceColor == Side.Black -> "p"
        else -> throw IllegalStateException("invalid piece!")
    }

    companion object Pieces {
        val WhiteKing: Piece = Piece(pieceType = PieceType.King, pieceColor = Side.White)
        val WhiteQueen: Piece = Piece(pieceType = PieceType.Queen, pieceColor = Side.White)
        val WhiteRook: Piece = Piece(pieceType = PieceType.Rook, pieceColor = Side.White)
        val WhiteBishop: Piece = Piece(pieceType = PieceType.Bishop, pieceColor = Side.White)
        val WhiteKnight: Piece = Piece(pieceType = PieceType.Knight, pieceColor = Side.White)
        val WhitePawn: Piece = Piece(pieceType = PieceType.Pawn, pieceColor = Side.White)
        val BlackKing: Piece = Piece(pieceType = PieceType.King, pieceColor = Side.Black)
        val BlackQueen: Piece = Piece(pieceType = PieceType.Queen, pieceColor = Side.Black)
        val BlackRook: Piece = Piece(pieceType = PieceType.Rook, pieceColor = Side.Black)
        val BlackBishop: Piece = Piece(pieceType = PieceType.Bishop, pieceColor = Side.Black)
        val BlackKnight: Piece = Piece(pieceType = PieceType.Knight, pieceColor = Side.Black)
        val BlackPawn: Piece = Piece(pieceType = PieceType.Pawn, pieceColor = Side.Black)
    }
}


class ChessboardTile(val column: String, val row: String, val color: TileColor) {
    val name: String
        get() = column + row

    var piece: Piece? = null
}

class Chessboard {
    val size: Int = 8
    val boardTiles: List<ChessboardTile> by lazy {
        initBoardTiles()
    }
    val columns: List<String>
        get() = ('A'..'H').map{ "${it}" }.toList()
    val rows: List<String>
        get() = ('1'..'8').map{"${it}"}.toList()

    fun clone(): Chessboard {
        val n = Chessboard()
        n.clear()
        this.allPieces().forEach { (p, ind) ->
            n.placePiece(p, ind)
        }
        return n
    }

    private fun columnToIndex(column: String): Int {
        return ('H' downTo 'A').map{"${it}"}.indexOf(column)
    }

    private fun rowToIndex(row: String): Int {
        return ('8' downTo '1').map{"${it}"}.indexOf(row)
    }

    private fun initBoardTiles(): List<ChessboardTile> {
        return columns.reversed().mapIndexed{ i, column ->
            rows.reversed().mapIndexed { j, row ->
                ChessboardTile(column, row, color = if ((i + j) % 2 == 1) TileColor.Dark else TileColor.Light)
            }
        }.flatMap { it }
    }

    private fun removePiece(index: String) {
        this[index].piece = null
    }

    fun movePiece(fromIndex: String, toIndex: String): Piece? {
        val p = this[fromIndex].piece ?: throw IllegalStateException("No piece on " + fromIndex + "!")
        val q = this[toIndex].piece
        removePiece(fromIndex)
        q?.also {
            if (p.pieceColor == q!!.pieceColor) throw IllegalStateException("Cannot capture same colored piece!")
        }
        placePiece(p, toIndex)
        return q
    }

    private fun placePiecesInitial() {
        placePiece(Piece.WhitePawn, "A2")
        placePiece(Piece.WhitePawn, "B2")
        placePiece(Piece.WhitePawn, "C2")
        placePiece(Piece.WhitePawn, "D2")
        placePiece(Piece.WhitePawn, "E2")
        placePiece(Piece.WhitePawn, "F2")
        placePiece(Piece.WhitePawn, "G2")
        placePiece(Piece.WhitePawn, "H2")
        placePiece(Piece.WhiteRook, "A1")
        placePiece(Piece.WhiteKnight, "B1")
        placePiece(Piece.WhiteBishop, "C1")
        placePiece(Piece.WhiteQueen, "D1")
        placePiece(Piece.WhiteKing, "E1")
        placePiece(Piece.WhiteBishop, "F1")
        placePiece(Piece.WhiteKnight, "G1")
        placePiece(Piece.WhiteRook, "H1")

        placePiece(Piece.BlackPawn, "A7")
        placePiece(Piece.BlackPawn, "B7")
        placePiece(Piece.BlackPawn, "C7")
        placePiece(Piece.BlackPawn, "D7")
        placePiece(Piece.BlackPawn, "E7")
        placePiece(Piece.BlackPawn, "F7")
        placePiece(Piece.BlackPawn, "G7")
        placePiece(Piece.BlackPawn, "H7")
        placePiece(Piece.BlackRook, "A8")
        placePiece(Piece.BlackKnight, "B8")
        placePiece(Piece.BlackBishop, "C8")
        placePiece(Piece.BlackQueen, "D8")
        placePiece(Piece.BlackKing, "E8")
        placePiece(Piece.BlackBishop, "F8")
        placePiece(Piece.BlackKnight, "G8")
        placePiece(Piece.BlackRook, "H8")
    }

    init {
        placePiecesInitial()
    }

    fun whitePieces(): List<Pair<Piece, String>> {
        return pieces(Side.White)
    }

    fun blackPieces(): List<Pair<Piece, String>> {
        return pieces(Side.Black)
    }

    fun allPieces(): List<Pair<Piece, String>> {
        return whitePieces() + blackPieces()
    }

    fun pieces(side: Side): List<Pair<Piece, String>> {
        return boardTiles.filter { it.piece != null && it.piece!!.pieceColor == side}.map { Pair(it.piece!!, it.name) }
    }

    fun pieceAt(index: String): Piece? {
        return this[index].piece
    }

    fun pieceOccupied(side: Side, index: String): Boolean {
        val p = this[index].piece ?: return false
        return p.pieceColor == side
    }

    fun clear() {
        boardTiles.forEach {
            it.piece = null
        }
    }

    fun placePiece(piece: Piece, index: String) {
        this[index].piece = piece
    }

    fun reset() {
        clear()
        placePiecesInitial()
    }

    operator fun get(index: String): ChessboardTile {
        require(index.length == 2)
        val col = "${index[0]}"
        val row = "${index[1]}"
        return boardTiles[columnToIndex(col)*size + rowToIndex(row)]
        //return ChessboardTile(column = "" +index[0], row = "" +index[1]) ?: throw java.lang.IllegalArgumentException()
    }


}