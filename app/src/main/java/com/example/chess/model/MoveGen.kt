package com.example.chess.model

fun generateKnightMoves(index: String): List<String> {
    val allCols = "ABCDEFGH"
    val allRows = "12345678"
    val col = index[0]; val row = index[1]
    val colIndex = allCols.indexOf(col); val rowIndex = allRows.indexOf(row)
    val moveOffsetList = listOf( //column row offsets clockwise
        Pair(1, 2),
        Pair(2, 1),
        Pair(2, -1),
        Pair(1, -2),
        Pair(-1, -2),
        Pair(-2, -1),
        Pair(-2, 1),
        Pair(-1, 2)
    )
    return moveOffsetList.filter { (co, ro) ->
        colIndex + co >= 0 && colIndex + co < allCols.length && rowIndex + ro >= 0 && rowIndex + ro < allRows.length}
        .map { (co, ro) -> ""+ allCols[colIndex+co] + allRows[rowIndex+ro] }
}

fun generateBishopMoves(index: String): List<String> {
    val allCols = "ABCDEFGH"
    val allRows = "12345678"
    val col = index[0]; val row = index[1]
    val colIndex = allCols.indexOf(col); val rowIndex = allRows.indexOf(row)

    val colRange1 = colIndex+1..allCols.length-1
    val colRange2 = colIndex-1 downTo 0
    val rowRange1 = rowIndex+1..allRows.length-1
    val rowRange2 = rowIndex-1 downTo 0

    return colRange1.zip(rowRange1) { c, r ->
        "${allCols[c]}${allRows[r]}"
    } + colRange1.zip(rowRange2) { c, r ->
        "${allCols[c]}${allRows[r]}"
    } + colRange2.zip(rowRange1) { c, r ->
        "${allCols[c]}${allRows[r]}"
    } + colRange2.zip(rowRange2) { c, r ->
        "${allCols[c]}${allRows[r]}"
    }
}

fun generateRookMoves(index: String): List<String> {
    val allCols = "ABCDEFGH"
    val allRows = "12345678"
    val col = index[0]; val row = index[1]
    val colIndex = allCols.indexOf(col); val rowIndex = allRows.indexOf(row)

    val colRange1 = colIndex+1..allCols.length-1
    val colRange2 = colIndex-1 downTo 0
    val rowRange1 = rowIndex+1..allRows.length-1
    val rowRange2 = rowIndex-1 downTo 0

    return colRange1.map {
        "${allCols[it]}${row}"
    } + colRange2.map {
        "${allCols[it]}${row}"
    } + rowRange1.map {
        "${col}${allRows[it]}"
    } + rowRange2.map {
        "${col}${allRows[it]}"
    }
}

fun generateQueenMoves(index: String): List<String> {
    return generateRookMoves(index) + generateBishopMoves(index)
}

fun generateKingMoves(index: String): List<String> {
    val allCols = "ABCDEFGH"
    val allRows = "12345678"
    val col = index[0]; val row = index[1]
    val colIndex = allCols.indexOf(col); val rowIndex = allRows.indexOf(row)
    val l = mutableListOf<String>()
    if (colIndex + 1 <allCols.length && rowIndex + 1 < allRows.length) {
        l.add("${allCols[colIndex+1]}${allRows[rowIndex+1]}")
    }
    if (colIndex + 1 < allCols.length) {
        l.add("${allCols[colIndex+1]}${allRows[rowIndex]}")
    }
    if (colIndex + 1 < allCols.length && rowIndex - 1 >= 0) {
        l.add("${allCols[colIndex+1]}${allRows[rowIndex-1]}")
    }
    if (rowIndex - 1 >= 0) {
        l.add("${allCols[colIndex]}${allRows[rowIndex-1]}")
    }
    if (colIndex - 1 >= 0 && rowIndex - 1 >= 0) {
        l.add("${allCols[colIndex-1]}${allRows[rowIndex-1]}")
    }
    if (colIndex - 1 >= 0) {
        l.add("${allCols[colIndex-1]}${allRows[rowIndex]}")
    }
    if (colIndex -1 >= 0 && rowIndex + 1 < allRows.length) {
        l.add("${allCols[colIndex-1]}${allRows[rowIndex+1]}")
    }
    if (rowIndex + 1 < allRows.length) {
        l.add("${allCols[colIndex]}${allRows[rowIndex+1]}")
    }
    return l
}

fun generatePawnMoves(side: Side, index: String): List <String> {
    val allCols = "ABCDEFGH"
    val allRows = "12345678"
    val col = index[0]; val row = index[1]
    val colIndex = allCols.indexOf(col); val rowIndex = allRows.indexOf(row)
    val l = mutableListOf<String>()

    if (side == Side.White) {
        if (row == '2') {
            l.add("${col}${allRows[rowIndex+2]}")
        }
        l.add("${col}${allRows[rowIndex+1]}")
    }else {
        if (row == '7') {
            l.add("${col}${allRows[rowIndex-2]}")
        }
        l.add("${col}${allRows[rowIndex-1]}")
    }

    return l
}

fun generatePawnCaptures(side: Side, index: String): List<String> {
    val allCols = "ABCDEFGH"
    val allRows = "12345678"
    val col = index[0]; val row = index[1]
    val colIndex = allCols.indexOf(col); val rowIndex = allRows.indexOf(row)
    val l = mutableListOf<String>()

    if (side == Side.White) {
        if (colIndex + 1 < allCols.length)
            l.add("${allCols[colIndex+1]}${allRows[rowIndex+1]}")
        if (colIndex - 1 >= 0)
            l.add("${allCols[colIndex-1]}${allRows[rowIndex+1]}")
    }
    else {
        if (colIndex + 1 < allCols.length)
            l.add("${allCols[colIndex+1]}${allRows[rowIndex-1]}")
        if (colIndex -1 >= 0)
            l.add("${allCols[colIndex-1]}${allRows[rowIndex-1]}")
    }
    return l
}

/*
fun Chessboard.generateCastlingMoves(side: Side): List<String> {
    val m = mutableListOf<String>()
    if (this.castleValid(side, CastleType.Kingside)) {
        m.add(if (side == Side.White) "G1" else "G8")
    }
    if (this.castleValid(side, CastleType.Queenside)) {
        m.add(if (side == Side.White) "C1" else "C8")
    }
    return m
}

 */

fun Chessboard.generateLegalMoves(index: String): List<String> {
    val p = this[index].piece ?: throw java.lang.IllegalArgumentException("No piece on " + index + "!")
    val side = p.pieceColor; val otherSide = if (side == Side.White) Side.Black else Side.White
    val isKingInCheck = this.kingInCheck(side)

    if (p.pieceType == PieceType.Pawn) {
        return (generatePawnMoves(side, index).filter { !this.pieceOccupied(side, it) && !this.pieceOccupied(otherSide, it) && !this.anyBlocking(index, it) } +
                generatePawnCaptures(side, index).filter { this.pieceOccupied(otherSide, it) })
                    .filter { !this.kingWouldBeInCheck(side, index, it) }
    }

    return when (p.pieceType) {
        PieceType.Knight -> generateKnightMoves(index)
        PieceType.Bishop -> generateBishopMoves(index)
        PieceType.Rook -> generateRookMoves(index)
        PieceType.Queen -> generateQueenMoves(index)
        PieceType.King -> generateKingMoves(index)
        else -> throw IllegalStateException("Unknown type " + p.pieceType + " for piece at " + index + "!")
    }.filter {
        !this.pieceOccupied(side, it)
                && !this.kingWouldBeInCheck(side, index, it)
                && (p.pieceType in listOf(PieceType.Knight, PieceType.King)|| !this.anyBlocking(index, it))
    }
    return emptyList()
}