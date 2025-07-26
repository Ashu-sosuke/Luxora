package com.example.e_commerse.login

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val REMEMBER_KEY = booleanPreferencesKey("remember_me")
    }

    fun saveUser(email: String, password: String, rememberMe: Boolean) {
        runBlocking {
            context.dataStore.edit { prefs ->
                prefs[EMAIL_KEY] = email
                prefs[PASSWORD_KEY] = password
                prefs[REMEMBER_KEY] = rememberMe
            }
        }
    }

    fun clearUser() {
        runBlocking {
            context.dataStore.edit { it.clear() }
        }
    }

    fun getRememberMe(): Boolean = runBlocking {
        context.dataStore.data.first()[REMEMBER_KEY] ?: false
    }

    fun getEmail(): String = runBlocking {
        context.dataStore.data.first()[EMAIL_KEY] ?: ""
    }

    fun getPassword(): String = runBlocking {
        context.dataStore.data.first()[PASSWORD_KEY] ?: ""
    }
}
