package com.example.core.data.source.local.preferences

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SharedPreferences(
    private val context: Context,
) {

    object PreferencesKey {
        const val THEME_SETTING_KEY = "theme_setting"
    }

    private val sharedPreferences = context.getSharedPreferences(PreferencesKey.THEME_SETTING_KEY, Context.MODE_PRIVATE)

    fun getThemeSetting(): Flow<Boolean> {
        return flow {
            val themeSetting = sharedPreferences.getBoolean(PreferencesKey.THEME_SETTING_KEY, false)
            emit(themeSetting)
        }
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        sharedPreferences.edit {
            putBoolean(PreferencesKey.THEME_SETTING_KEY, isDarkModeActive)
        }
    }
}



