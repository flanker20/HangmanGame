package com.payxpert.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.payxpert.game.Utils.Companion.unmask

/**
 * ViewModel containing the app data and methods to process the data
 */
class HangmanViewModel : ViewModel() {
    private val TAG = HangmanViewModel::class.java.simpleName

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private val _games = MutableLiveData(0)
    val games: LiveData<Int>
        get() = _games

    private val _tries = MutableLiveData(10)
    val tries: LiveData<Int>
        get() = _tries

    private val _wordToFind = MutableLiveData<String>()
    val wordToFind: LiveData<String>
        get() = _wordToFind

    private val _maskedWord = MutableLiveData<String>()
    val maskedWord: LiveData<String>
        get() = _maskedWord

    // List of words used in the game
    private var usedWordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String
    private var isGameStarted = false

    /*
     * Updates currentWord and currentScrambledWord with the next word.
     */
    fun getNextWord() {
        if (!isGameStarted) {
            isGameStarted = true
            currentWord = Utils.getNotUsedWord(usedWordsList)

            Log.d("Hangman", "currentWord= $currentWord")
            _wordToFind.value = currentWord

            _tries.value = 10
            _maskedWord.value = "".padEnd(currentWord.length, '_')
            _games.value = _games.value?.inc()

            Log.i(TAG, "new game score=${score.value} games=${games.value}")
        } else {
            Log.d(TAG, "Game already started")
        }
    }

    fun setScores(score: Int, games: Int) {
        Log.i(TAG, "set scores=$score games=$games")
        _score.value = score
        _games.value = games
    }

    /*
     * Re-initializes the game data to restart the game.
     */
    fun resetScores() {
        setScores(0,0)
        usedWordsList.clear()
        getNextWord()
    }

    /*
    * Increases the game score if the word is completed before 10 attempts
    */
    fun endGame(gameResult: Boolean) {
        if (gameResult) {
            _score.value = _score.value?.inc()
        }
        isGameStarted = false
    }

    /*
    * Returns true if the player word is correct.
    * Increases the score accordingly.
    */
    fun isCharInWord(charToTest: Char): Boolean {
        if (currentWord.contains(charToTest,true)) {
            _maskedWord.value = unmask(charToTest, currentWord, _maskedWord.value ?: "")
            return true
        }
        _tries.value = _tries.value?.dec()
        return false
    }
}