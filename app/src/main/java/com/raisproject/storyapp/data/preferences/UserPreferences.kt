package com.raisproject.storyapp.data.preferences

import android.content.Context

internal class UserPreferences(context: Context) {

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val TOKEN = "token"


        @Volatile
        private var instance : UserPreferences? = null
        fun getInstance(
            context: Context
        ) : UserPreferences =
            instance ?: synchronized(this) {
                instance ?: UserPreferences(context)
            }.also { instance = it }
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: User) {
        val editor = preferences.edit()
        editor.putString(TOKEN, value.token)
        editor.apply()
    }

    fun getUser(): User {
        val model = User()
        model.token = preferences.getString(TOKEN, "")

        return model
    }

    fun deleteUser() {
        val editor = preferences.edit()
        editor.clear()
        editor.commit()
    }

}