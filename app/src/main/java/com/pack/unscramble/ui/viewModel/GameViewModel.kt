package com.pack.unscramble.ui.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.pack.unscramble.data.MAX_Words
import com.pack.unscramble.data.SCORE_INCREASE
import com.pack.unscramble.data.allWords
import com.pack.unscramble.ui.uiState.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    private val usedWords: MutableSet<String> = mutableSetOf()



    var userGuess by mutableStateOf("")
        private set

    fun updateUserGuess(userGuess: String) {
        this.userGuess = userGuess
    }


    private val mUiState = MutableStateFlow(UiState(scrambledWord = pickRandomWordAndShuffle()))
    val uiState: StateFlow<UiState>
        get() = mUiState
    private lateinit var mCurrentWord: String
    private fun pickRandomWordAndShuffle(): String {

        mCurrentWord = allWords.random()

        return if (mCurrentWord in usedWords)
            pickRandomWordAndShuffle()
        else {
            Log.i("MAD",mCurrentWord)
            usedWords.add(mCurrentWord)
            var shuffledWord = mCurrentWord.toCharArray()
            shuffledWord.shuffle()
            while (String(shuffledWord) == mCurrentWord) {
                shuffledWord = mCurrentWord.toCharArray()
                shuffledWord.shuffle()
            }
            String(shuffledWord)
        }
    }


    fun checkUserGuess() {
        if (userGuess == mCurrentWord) {
            val isGameOver: Boolean = mUiState.value.currentWordNumber >= MAX_Words
            mUiState.update { currentState ->
                if (isGameOver)
                    currentState.copy(
                        isUserGuessedWordWrong = false,
                        score = currentState.score + SCORE_INCREASE,
                        isGameOver = true
                    )
                else
                    currentState.copy(
                        isUserGuessedWordWrong = false,
                        score = currentState.score + SCORE_INCREASE,
                        currentWordNumber = currentState.currentWordNumber + 1,
                        scrambledWord = pickRandomWordAndShuffle()
                    )
            }
            updateUserGuess("")

        } else {
            mUiState.update { currentState ->
                currentState.copy(
                    isUserGuessedWordWrong = true
                )
            }
        }
    }

    fun skipToNextWord() {
        val isGameOver: Boolean = mUiState.value.currentWordNumber >= MAX_Words
        mUiState.update { currentState ->
            if (!isGameOver)
                currentState.copy(
                    scrambledWord = pickRandomWordAndShuffle(),
                    isUserGuessedWordWrong = false,
                    currentWordNumber = currentState.currentWordNumber + 1
                )
            else
                currentState.copy(
                    isUserGuessedWordWrong = false,
                    isGameOver = true
                )
        }
        updateUserGuess("")
    }

    fun resetGame() {
        usedWords.clear()
        mUiState.value = UiState(scrambledWord = pickRandomWordAndShuffle())
        updateUserGuess("")
    }
}