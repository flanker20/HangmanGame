package com.payxpert.game

import android.os.Bundle
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
        binding.gameinput?.newLetter?.setOnClickListener { onSubmitChar() }
        if (viewModel.isGameOver()) { showFinalScoreDialog() }
    }

    /*
    * Checks the char, and updates the word accordingly.
    */
    private fun onSubmitChar() {
        val charToTest = binding.gameinput?.txtLetter?.text.toString()[0]

        viewModel.isCharInWord(charToTest)
    }

    /*
     * Creates and shows an AlertDialog with final score.
     */
    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
//            .setTitle(getString(R.string.congratulations))
//            .setMessage(getString(R.string.you_scored, viewModel.score.value))
            .setCancelable(false)
//            .setNegativeButton(getString(R.string.exit)) { _, _ ->
//                exitGame()
//            }
//            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
//                restartGame()
//            }
            .show()
    }

    /*
     * Re-initializes the data in the ViewModel and updates the views with the new data, to
     * restart the game.
     */
    private fun restartGame() {
        viewModel.resetScores()
    }

    /*
     * Exits the game.
     */
    private fun exitGame() {
        activity?.finish()
    }
}