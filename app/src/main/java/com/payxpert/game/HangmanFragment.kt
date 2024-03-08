package com.payxpert.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.payxpert.game.data.SettingsDataStore
import com.payxpert.game.data.dataStore
import com.payxpert.game.databinding.HangmanFragmentBinding
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

/**
 * Fragment where the game is played, contains the game logic.
 */
class HangmanFragment : Fragment() {

    private val TAG = HangmanFragment::class.java.simpleName

    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: HangmanFragmentBinding

    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same GameViewModel instance created by the
    // first fragment.
    private val viewModel: HangmanViewModel by viewModels()

    private lateinit var settingsDataStore: SettingsDataStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = DataBindingUtil.inflate(inflater, R.layout.hangman_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")

        // Set the viewModel for data binding - this allows the bound layout access
        // to all the data in the VieWModel
        binding.gameViewModel = viewModel
        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Setup a click listener for the Submit and Skip buttons.
        binding.gameinput.btnSubmitChar.setOnClickListener { onSubmitChar() }
        binding.gamestats.btnReset.setOnClickListener{ onResetScore() }

        getScoresFromDatastore()
    }

    private fun getScoresFromDatastore() {
        Log.d(TAG, "getScoresFromDatastore")
        settingsDataStore = SettingsDataStore(requireContext().dataStore)
        lifecycleScope.launch {
            settingsDataStore.preferenceFlow.collect { up ->
                viewModel.setScores(up.score, up.games)
                this.coroutineContext.job.cancel()
                startNewGame()
            }
        }
    }

    /*
    * start a new game.
    */
    private fun startNewGame() {
        Log.d(TAG, "startNewGame")
        viewModel.getNextWord()
        setMaskedWord()
        displayStats()
    }

    private fun onResetScore() {
        viewModel.resetScores()
        displayStats()
    }

    /*
    * Checks the char, and updates the word accordingly.
    */
    private fun onSubmitChar() {
        val charSubmitted = binding.gameinput.txtCharToSubmit.text.toString()
        Log.d(TAG, "Char submitted = $charSubmitted")
        if (charSubmitted.isNotEmpty()) {
            val charToTest = charSubmitted[0]

            viewModel.isCharInWord(charToTest)
            displayStats() // games stats updates after the first character submitted

            setMaskedWord()
            binding.gameinput.txtCharToSubmit.text.clear()
            if (isComplete(viewModel.maskedWord.value)) {
                Log.i(TAG,"Success")
                viewModel.endGame(true)
                displayStats()
                showFinalScoreDialog(getString(R.string.you_scored, viewModel.score.value, viewModel.games.value))
            } else if (0 == viewModel.tries.value) {
                viewModel.endGame(false)
                showFinalScoreDialog(getString(R.string.you_loose, viewModel.wordToFind.value))
            }
        }
    }

    private fun displayStats() {
        val games = viewModel.games.value.toString()
        val score = viewModel.score.value.toString()
        Log.i(TAG, "displayStats score=$score games=$games")
        binding.gamestats.txtGames.text = games
        binding.gamestats.txtVictories.text = score
        viewLifecycleOwner.lifecycleScope.launch { settingsDataStore.saveScoreToPreferencesStore(viewModel.score.value!!, viewModel.games.value!!, requireContext()) }
    }

    private fun isComplete(maskedWord: String?): Boolean {
        return !(maskedWord?.contains("_") ?: true)
    }

    private fun setMaskedWord() {
        Log.d(TAG, "result = "+viewModel.maskedWord.value)
        binding.gameresult.resultWord.text = viewModel.maskedWord.value
        binding.gameresult.triesLeft.text = resources.getQuantityString(R.plurals.tries_left, viewModel.tries.value!!, viewModel.tries.value!!)
    }

    /*
     * Creates and shows an AlertDialog with final score.
     */
    private fun showFinalScoreDialog(msg: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.app_name))
            .setMessage(msg)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                startNewGame()
            }
            .show()
    }

    /*
     * Exits the game.
     */
    private fun exitGame() {
        activity?.finish()
    }
}