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


    /*
     * Updates currentWord and currentScrambledWord with the next word.
     */
    fun getNextWord() {
        currentWord = Utils.getNotUsedWord(usedWordsList)

        Log.d("Hangman", "currentWord= $currentWord")
        _wordToFind.value = currentWord

        _games.value = _games.value?.inc()
        _tries.value = 10
        _maskedWord.value = "".padEnd(currentWord.length,'_')

        Log.i(TAG, "attempts = "+_games.value)
    }

    /*
     * Re-initializes the game data to restart the game.
     */
    fun resetScores() {
        _score.value = 0
        _games.value = 0
        usedWordsList.clear()
        getNextWord()
    }

    /*
    * Increases the game score if the word is completed before 10 attempts
    */
    fun increaseScore() {
        _score.value = _score.value?.inc()
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