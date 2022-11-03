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
            onPieceSelected = {p, i -> vm.showLegalMoves(p, i)},
            onCastle = {s, c -> vm.castle(s, c) }
        ) { f, t ->
            vm.makeMove(f, t)
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
                   onPieceSelected: (p: Piece, index: String)-> Unit = {p, i ->},
                   onCastle: (side: Side, castleType: CastleType)->Unit = {s, c->},
                   onMakeMove: (fromIndex: String, toIndex: String)-> Unit = {f, t->}) {

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
                            Box(modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFAB2929))
                                .clickable {
                                    onMakeMove(selectedIndex, col + row)
                                })
                            Box(modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color.Red))
                        }
                        else if (selectedIndex != "" && castlingMoves.any {(c, s)-> s == col+row }) {
                            Box(modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF247D91))
                                .clickable {
                                    val castleType =
                                        castlingMoves.first { (c, s) -> s == col + row }.first
                                    onCastle(nextMove, castleType)
                                })
                            Box(modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color.Cyan))
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