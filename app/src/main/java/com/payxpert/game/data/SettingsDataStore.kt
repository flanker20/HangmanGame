package com.payxpert.game.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.payxpert.game.data.SettingsDataStore.PreferencesKeys.GAMES
import com.payxpert.game.data.SettingsDataStore.PreferencesKeys.SCORE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Class that handles saving and retrieving layout setting preferences
 */

private const val PREFS_HANGMAN = "prefs_hangman"

// Create a DataStore instance using the preferencesDataStore delegate, with the Context as
// receiver.
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFS_HANGMAN)

data class UserPreferences(
    val score: Int,
    val games: Int
)

class SettingsDataStore(preference_datastore: DataStore<Preferences>) {
    private object PreferencesKeys {
        val SCORE = intPreferencesKey("score")
        val GAMES = intPreferencesKey("games")
    }

    val preferenceFlow: Flow<UserPreferences> = preference_datastore.data
        .map { preferences ->
            mapUserPreferences(preferences)
        }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        // Get the sort order from preferences and convert it to a [SortOrder] object
        val score = preferences[SCORE] ?: 0
        val games = preferences[GAMES] ?: 0
        return UserPreferences(score, games)
    }

    suspend fun saveScoreToPreferencesStore(score: Int, games: Int, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[SCORE] = score
            preferences[GAMES] = games
        }
    }
}