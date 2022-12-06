package com.pack.unscramble.ui.uiState

data class UiState(
    val currentWordNumber: Int = 1,
    val score: Int = 0,
    val scrambledWord: String,
    val isUserGuessedWordWrong: Boolean = false,
    val isGameOver: Boolean = false
)