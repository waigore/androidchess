package com.example.chess

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chess.model.Chessboard
import com.example.chess.model.Piece
import com.example.chess.model.TileColor
import com.example.chess.ui.theme.ChessTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val chessGameViewModel: ChessGameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            ChessTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //ChessboardUI(chessboard = Chessboard())
                    ChessGameView(vm = chessGameViewModel)
                }
            }
        }
    }
}
/*
@Composable
fun ChessboardPiece(piece: Piece) {

   var offsetX by remember { mutableStateOf(0f) }
   var offsetY by remember { mutableStateOf(0f) }


   Text(piece.repr, fontSize = 38.sp,
       modifier = Modifier.offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
           .pointerInput(Unit) {
               detectDragGestures { change, dragAmount ->
                   change.consumeAllChanges()
                   offsetX += dragAmount.x
                   offsetY += dragAmount.y
               }
           })

   Text(piece.repr, fontSize = 38.sp, modifier = Modifier.clickable(enabled = true) {  })
}

    */

/*
@Composable
fun ChessboardUI(chessboard: Chessboard) {
    Column(modifier =
    Modifier
        .padding(10.dp)
        .height(IntrinsicSize.Min)
        .width(IntrinsicSize.Min)
        ) {
        (chessboard.rows.reversed()).forEachIndexed {i, vertChar ->
            Row {
                (chessboard.columns).forEachIndexed {j, horizChar ->
                    val tile = chessboard[horizChar + vertChar]
                    Box(modifier = Modifier.size(45.dp).background(color = when {
                        tile.color == TileColor.Dark -> Color (0xFF769656)
                        else -> Color(0xFFF5F5F5)
                    }),
                        contentAlignment = Alignment.Center
                    ) {
                        //Text(tile.piece?.repr ?: "", fontSize = 38.sp)
                        if (tile.piece != null) {
                            ChessboardPiece(tile.piece!!)
                        }
                    }
                }
            }

        }
    }
}

 */

/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChessTheme {
        ChessboardUI(chessboard = Chessboard())
    }
}

 */