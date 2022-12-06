package com.pack.unscramble.ui.composables

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pack.unscramble.R
import com.pack.unscramble.data.MAX_Words
import com.pack.unscramble.ui.viewModel.GameViewModel



@Composable
fun GameScreen(
    gameViewModel: GameViewModel = viewModel()
) {

    val gameUiState by gameViewModel.uiState.collectAsState()

    Column {
        StatisticsSection(
            currentWordNumber = gameUiState.currentWordNumber,
            totalWords = MAX_Words,
            currentScore = gameUiState.score
        )


        GameSection(
            currentWord = gameUiState.scrambledWord,
            hint = stringResource(R.string.hint),
            userGuess = gameViewModel.userGuess,
            onUserGuessChanged = {
                gameViewModel.updateUserGuess(it)
            },
            isUserGuessWrong = gameUiState.isUserGuessedWordWrong
        )


        ButtonsSection(
            onSkipClicked = {
                gameViewModel.skipToNextWord()
            },
            onSubmitClicked = {
                gameViewModel.checkUserGuess()
            }
        )
    }

    if (gameUiState.isGameOver) {
        val activity = (LocalContext.current as Activity)
        CustomAlertDialog(
            score = gameUiState.score,
            onPlayAgainClicked = {
                gameViewModel.resetGame()
            },
            onExitClicked = {
                activity.finish()
            }
        )
    }
}

@Composable
fun CustomAlertDialog(
    score: Int,
    onPlayAgainClicked: () -> Unit,
    onExitClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {  },
        title = {
            Text(text = stringResource(R.string.Congrats))
        },
        text = {
            Text(text = stringResource(R.string.youScored, score))
        },
        dismissButton = {
            TextButton(onClick = { onExitClicked() }) {
                Text(text = stringResource(R.string.exit))
            }
        },
        confirmButton = {
            TextButton(onClick = { onPlayAgainClicked() }) {
                Text(text = stringResource(R.string.playAgain))
            }
        }
    )

}


@Composable
fun StatisticsSection(currentWordNumber: Int, totalWords: Int, currentScore: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(text = stringResource(R.string.wordCountStats, currentWordNumber, totalWords))
        Text(text = stringResource(R.string.scoreStats, currentScore))
    }
}


@Composable
fun GameSection(
    currentWord: String,
    hint: String,
    userGuess: String,
    onUserGuessChanged: (String) -> Unit,
    isUserGuessWrong: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = currentWord,
            fontSize = 45.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = hint,
            fontSize = 17.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        OutlinedTextField(
            value = userGuess,
            onValueChange = {
                onUserGuessChanged(it)
            },
            label = {
                Text(
                    text = if (isUserGuessWrong) stringResource(id = R.string.wrongGuess) else stringResource(
                        R.string.EnterYourWord
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            isError = isUserGuessWrong
        )
    }
}


@Composable
fun ButtonsSection(onSkipClicked: () -> Unit, onSubmitClicked: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(16.dp)
    ) {

        OutlinedButton(
            onClick = { onSkipClicked() },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(text = stringResource(R.string.Skip))
        }



        OutlinedButton(
            onClick = { onSubmitClicked() },
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
        ) {
            Text(text = stringResource(R.string.Submit))
        }


    }
}

@Preview(name = "Statistics", widthDp = 360)
@Composable
fun StatisticsSectionPreview() {
    GameScreen()
}
