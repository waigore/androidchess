package com.example.chess

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chess.model.*
import com.example.chess.ui.theme.ChessTheme

@Composable
fun ChessGameView(vm: ChessGameViewModel) {
    val chessGame = vm.chessGame
    val chessboard = chessGame.chessboard
    val gameState by vm.gameState.collectAsState()
    val nextMove by vm.gameNextMove.collectAsState()
    val pieceCaptured by vm.gamePieceCaptured.collectAsState()

    Column {
        ChessboardView(chessboard,
            nextMove = nextMove,
            selectedIndex = vm.selectedIndex,
            legalMoves = vm.legalMoves,
            castlingMoves = vm.castlingMoves,
            enPassantMoves = vm.enPassantMoves,
            onPieceSelected = {p, i -> vm.toggleLegalMoves(p, i)},
            onCastle = {s, c -> vm.castle(s, c) }
        ) { f, t, c ->
            vm.makeMove(f, t, c)
        }
        if (pieceCaptured != null) {
            CapturedPiecesView(vm.chessGame.formatCapturedPieces(Side.White), vm.chessGame.formatCapturedPieces(Side.Black))
        }
        if (nextMove != null) {
            MoveHistoryView(vm.chessGame.moveHistoryStandardNotation())
        }
    }

}

@Composable
fun CapturedPiecesView(whiteCapturedPieces: String, blackCapturedPieces: String) {
    Column {
        Text(whiteCapturedPieces, fontSize = 20.sp)
        Text(blackCapturedPieces, fontSize = 20.sp)
    }
}

@Composable
fun MoveHistoryView(moveHistory: List<String>) {
    val whiteMoves = mutableListOf<String>()
    val blackMoves = mutableListOf<String>()
    moveHistory.forEachIndexed {i, move ->
        if (i % 2 == 0) whiteMoves.add(move)
        else blackMoves.add(move)
    }
    Row {
        Column {
            whiteMoves.forEachIndexed {j, move ->
                Text("${j+1}. " + move)
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Column {
            blackMoves.forEachIndexed {j, move ->
                Text( move)
            }
        }
    }

}


@Composable
fun ChessboardView(chessboard: Chessboard,
                   nextMove: Side = Side.White,
                   selectedIndex: String = "",
                   legalMoves: List<String> = emptyList(),
                   castlingMoves: List<Pair<CastleType, String>> = emptyList(),
                   enPassantMoves: List<Pair<String, String>> = emptyList(),
                   onPieceSelected: (p: Piece, index: String)-> Unit = {p, i ->},
                   onCastle: (side: Side, castleType: CastleType)->Unit = {s, c->},
                   onMakeMove: (fromIndex: String, toIndex: String, captureIndex: String)-> Unit = {f, t, c->}) {

    Column(modifier =
    Modifier
        .padding(10.dp)
        .height(IntrinsicSize.Min)
        .width(IntrinsicSize.Min)
    ) {
        (chessboard.rows.reversed()).forEachIndexed {i, row ->
            Row {
                (chessboard.columns).forEachIndexed {j, col ->
                    val tile = chessboard[col + row]
                    Box(modifier = Modifier
                        .size(45.dp)
                        .background(
                            color = when {
                                tile.color == TileColor.Dark -> Color(0xFF769656)
                                else -> Color(0xFFF5F5F5)
                            }
                        )
                        .border(
                            3.dp,
                            if (selectedIndex == col + row) Color.Cyan else Color.Transparent
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedIndex != "" && legalMoves.contains(col+row)) {
                            LegalMoveIndicator(selectedIndex, col+row, onMakeMove = onMakeMove)
                        }
                        else if (selectedIndex != "" && castlingMoves.any {(c, s)-> s == col+row }) {
                            val castleType =
                                castlingMoves.first { (c, s) -> s == col + row }.first
                            CastleIndicator(nextMove, castleType, onCastle = onCastle)
                        }
                        else if (selectedIndex != "" && enPassantMoves.any {(t, c)-> t == col+row}) {
                            val enPassantMove = enPassantMoves.first {(t, c) -> t == col+row}
                            LegalMoveIndicator(selectedIndex, col + row, onMakeMove = onMakeMove, captureIndex = enPassantMove.second)
                        }
                        else if (tile.piece != null) {
                            val p = tile.piece!!
                            ChessboardPieceView(p, clickable = nextMove == p.pieceColor, onClicked = {
                                onPieceSelected(p, col+row)
                            })
                        }
                    }
                }
            }

        }
    }
}

/*
@Composable
fun EnPassantMoveIndicator(selectedIndex: String, toIndex: String, captureIndex: String, onMakeMove: (fromIndex: String, toIndex: String, captureIndex: String)-> Unit) {
    Box(modifier = Modifier
        .size(20.dp)
        .clip(CircleShape)
        .background(Color(0xFFAB2929))
        .clickable {
            onMakeMove(selectedIndex, toIndex, captureIndex)
        })
    Box(modifier = Modifier
        .size(10.dp)
        .clip(CircleShape)
        .background(Color.Red))
}
 */

@Composable
fun LegalMoveIndicator(selectedIndex: String, toIndex: String,
                       onMakeMove: (fromIndex: String, toIndex: String, captureIndex: String) -> Unit,
                        captureIndex: String = "") {
    Box(modifier = Modifier
        .size(20.dp)
        .clip(CircleShape)
        .background(Color(0xFFAB2929))
        .clickable {
            onMakeMove(selectedIndex, toIndex, captureIndex)
        })
    Box(modifier = Modifier
        .size(10.dp)
        .clip(CircleShape)
        .background(Color.Red))
}

@Composable
fun CastleIndicator(side: Side, castleType: CastleType, onCastle: (side: Side, castleType: CastleType) -> Unit) {
    Box(modifier = Modifier
        .size(20.dp)
        .clip(CircleShape)
        .background(Color(0xFF247D91))
        .clickable {
            //val castleType =
            //    castlingMoves.first { (c, s) -> s == col + row }.first
            onCastle(side, castleType)
        })
    Box(modifier = Modifier
        .size(10.dp)
        .clip(CircleShape)
        .background(Color.Cyan))
}

@Composable
fun ChessboardPieceView(piece: Piece, clickable: Boolean = true, onClicked: (piece: Piece) -> Unit = {}) {
    Text(piece.repr, fontSize = 38.sp, modifier = Modifier.clickable(enabled = clickable) { onClicked(piece) })
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChessTheme {
        ChessboardView(chessboard = Chessboard())
    }
}