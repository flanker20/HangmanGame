package com.payxpert.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.payxpert.game.databinding.HangmanFragmentBinding

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

        // Set the viewModel for data binding - this allows the bound layout access
        // to all the data in the VieWModel
        binding.gameViewModel = viewModel
        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Setup a click listener for the Submit and Skip buttons.
        binding.gameinput.btnSubmitChar.setOnClickListener { onSubmitChar() }
        binding.gamestats.btnReset.setOnClickListener{ onResetScore() }

        startNewGame()
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

            setMaskedWord()
            binding.gameinput.txtCharToSubmit.text.clear()
            if (isComplete(viewModel.maskedWord.value)) {
                viewModel.increaseScore()
                displayStats()
                showFinalScoreDialog(getString(R.string.you_scored, viewModel.score.value, viewModel.games.value))
            } else if (0 == viewModel.tries.value) {
                showFinalScoreDialog(getString(R.string.you_loose, viewModel.wordToFind.value))
            }
        }
    }

    private fun displayStats() {
        binding.gamestats.txtGames.text = viewModel.games.value.toString()
        binding.gamestats.txtVictories.text = viewModel.score.value.toString()
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
     * Re-initializes the data in the ViewModel and updates the views with the new data, to
     * restart the game.
     */
    private fun startNewGame() {
        viewModel.getNextWord()
        setMaskedWord()
        displayStats()
    }

    /*
     * Exits the game.
     */
    private fun exitGame() {
        activity?.finish()
    }
}