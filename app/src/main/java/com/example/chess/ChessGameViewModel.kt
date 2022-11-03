package com.example.chess

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chess.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChessGameChangedTiles(val tiles: List<String>) {

}

@HiltViewModel
class ChessGameViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val chessGame: ChessGame
): IChessGameObserver, ViewModel() {

    private val _gameNextMove = MutableStateFlow(chessGame.nextMoveSide)
    val gameNextMove: StateFlow<Side>
        get() =_gameNextMove.asStateFlow()

    private val _gameState = MutableStateFlow(chessGame.gameState)
    val gameState: StateFlow<GameState>
        get() = _gameState.asStateFlow()

    private val _gameChangedTiles = MutableStateFlow(ChessGameChangedTiles(emptyList()))
    val gameChangedTiles: StateFlow<ChessGameChangedTiles>
        get() = _gameChangedTiles.asStateFlow()

    var selectedIndex by mutableStateOf("")
    var legalMoves by mutableStateOf(emptyList<String>())
    var castlingMoves by mutableStateOf(emptyList<Pair<CastleType, String>>())

    init {
        chessGame.add(this)
        chessGame.reset()
        chessGame.start()
    }

    fun showLegalMoves(p: Piece, index: String) {
        if (selectedIndex == index) {
            selectedIndex = ""
            legalMoves = emptyList()
            castlingMoves = emptyList()
        } else {
            selectedIndex = index
            legalMoves = chessGame.generateLegalMoves(index)
            if (p.pieceType == PieceType.King) {
                castlingMoves = chessGame.generateCastlingMoves(p.pieceColor)
            }
        }
    }

    fun castle(side: Side, castleType: CastleType) {
        selectedIndex = ""
        legalMoves = emptyList()
        castlingMoves = emptyList()
        chessGame.doCastle(side, castleType)
    }

    fun makeMove(fromIndex: String, toIndex: String) {
        selectedIndex = ""
        legalMoves = emptyList()
        castlingMoves = emptyList()
        chessGame.makeMove(fromIndex, toIndex)
    }

    override fun gameStateChanged(old: GameState, new: GameState) {
        viewModelScope.launch {
            _gameState.emit(new)
        }
    }

    override fun nextMoveChanged(old: Side, new: Side) {
        viewModelScope.launch {
            _gameNextMove.emit(new)
        }
    }

    override fun tilesChanged(tiles: List<String>) {
        viewModelScope.launch {
            _gameChangedTiles.emit(ChessGameChangedTiles(tiles))
        }
    }

    override fun castlingRightsChanged(
        side: Side,
        queensideAllowed: Boolean,
        kingsideAllowed: Boolean
    ) {

    }
}